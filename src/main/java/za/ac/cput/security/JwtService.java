/*
 JwtService.java

 Service class for Jwt

 Author: Joshua Reid Adams (230317693)

 Date: 11th August 2026
*/

package za.ac.cput.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.dto.EmployeeInviteResponse;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtService {

    private static final String INVITE_PURPOSE = "EMPLOYEE_INVITE";

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Value("${app.jwt.invite-expiration-ms}")
    private long inviteExpirationMs;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    //  Generates a JWT for a given user, embedding their id and role (userType)
    public String generateToken(int userId, String userType){
        return generateToken(userId, userType, null);
    }

    // For staff logins. Adds the staffRole so the filter knows who is an admin.
    // Pass null for patients and doctors.
    public String generateToken(int userId, String userType, String staffRole){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userType", userType)
                .issuedAt(now)
                .expiration(expiry);

        if (staffRole != null) {
            builder.claim("staffRole", staffRole);
        }

        return builder.signWith(getSigningKey()).compact();
    }

    public int extractUserId(String token){
        String subject = extractClaim(token, Claims::getSubject);
        return Integer.parseInt(subject);
    }

    public String extractUserType(String token){
        return extractClaim(token, claims -> claims.get("userType", String.class));
    }

    // Null for patient and doctor tokens.
    public String extractStaffRole(String token){
        return extractClaim(token, claims -> claims.get("staffRole", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Token for the doctor and staff signup invite. Holds the email and role.
    // It can't be used to log in. Pass null staffRole for doctors.
    public String generateInviteToken(String email, UserType userType, StaffRole staffRole) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + inviteExpirationMs);

        JwtBuilder builder = Jwts.builder()
                .subject(email)
                .claim("userType", userType.name())
                .claim("purpose", INVITE_PURPOSE)
                .issuedAt(now)
                .expiration(expiry);

        if (staffRole != null) {
            builder.claim("staffRole", staffRole.name());
        }

        return builder.signWith(getSigningKey()).compact();
    }

    // Reads an invite token. Returns null if it is expired, changed or not an invite.
    public EmployeeInviteResponse parseInviteToken(String token) {
        try {
            Claims claims = extractAllClaims(token);

            String purpose = claims.get("purpose", String.class);
            if (!INVITE_PURPOSE.equals(purpose)) {
                return null;
            }

            String email = claims.getSubject();
            UserType userType = UserType.valueOf(claims.get("userType", String.class));

            String staffRoleClaim = claims.get("staffRole", String.class);
            StaffRole staffRole = staffRoleClaim != null ? StaffRole.valueOf(staffRoleClaim) : null;

            return new EmployeeInviteResponse(email, userType, staffRole);

        } catch (JwtException | IllegalArgumentException e) {
            // Bad token or a role value that doesn't exist.
            return null;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
