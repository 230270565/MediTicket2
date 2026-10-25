/*
 EmployeeInviteToken.java

 Stores the 6-digit invite code sent to an employee after admin approval.

 Author: Joshua Reid Adams (230317693)

 Date: 11th August 2026
*/

package za.ac.cput.domain;

import jakarta.persistence.*;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserType;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_invite_token")
public class EmployeeInviteToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private StaffRole staffRole;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    protected EmployeeInviteToken() {}

    private EmployeeInviteToken(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.userType = builder.userType;
        this.staffRole = builder.staffRole;
        this.token = builder.token;
        this.expiryDate = builder.expiryDate;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public UserType getUserType() { return userType; }
    public StaffRole getStaffRole() { return staffRole; }
    public String getToken() { return token; }
    public LocalDateTime getExpiryDate() { return expiryDate; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    @Override
    public String toString() {
        return "EmployeeInviteToken{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                ", staffRole=" + staffRole +
                ", expiryDate=" + expiryDate +
                '}';
    }

    public static class Builder {
        private int id;
        private String email;
        private UserType userType;
        private StaffRole staffRole;
        private String token;
        private LocalDateTime expiryDate;

        public Builder setId(int id) { this.id = id; return this; }
        public Builder setEmail(String email) { this.email = email; return this; }
        public Builder setUserType(UserType userType) { this.userType = userType; return this; }
        public Builder setStaffRole(StaffRole staffRole) { this.staffRole = staffRole; return this; }
        public Builder setToken(String token) { this.token = token; return this; }
        public Builder setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; return this; }

        // Generates a 6-digit code that expires in 48 hours.
        public Builder generateFor(String email, UserType userType, StaffRole staffRole) {
            this.email = email;
            this.userType = userType;
            this.staffRole = staffRole;
            this.token = String.valueOf(100000 + new java.security.SecureRandom().nextInt(900000));
            this.expiryDate = LocalDateTime.now().plusHours(48);
            return this;
        }

        public EmployeeInviteToken build() {
            return new EmployeeInviteToken(this);
        }
    }
}
