/*
 RefreshTokenRepository.java

 Repository interface for RefreshToken

 Author: Joshua Reid Adams (230317693)

 Date: 11th August 2026
*/

package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.auth.RefreshToken;
import za.ac.cput.domain.enums.UserType;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserIdAndUserType(int userId, UserType userType);

    void deleteByToken(String token);
}
