/*
 EmployeeInviteTokenRepository.java

 Repository interface for EmployeeInviteToken

 Author: Joshua Reid Adams (230317693)

 Date: 11th August 2026
*/

package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.EmployeeInviteToken;

import java.util.Optional;

public interface EmployeeInviteTokenRepository extends JpaRepository<EmployeeInviteToken, Integer> {

    Optional<EmployeeInviteToken> findByToken(String token);

    @Transactional
    void deleteByEmail(String email);
}
