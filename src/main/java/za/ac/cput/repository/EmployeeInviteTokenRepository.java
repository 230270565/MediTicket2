/*
 EmployeeInviteTokenRepository.java

 Repository interface for EmployeeInviteToken

 Author: Joshua Reid Adams (230317693)

 Date: 11th August 2026
*/

package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.EmployeeInviteToken;

import java.util.Optional;

public interface EmployeeInviteTokenRepository extends JpaRepository<EmployeeInviteToken, Integer> {

    Optional<EmployeeInviteToken> findByToken(String token);

    void deleteByEmail(String email);
}
