/*
 PasswordResetRequestRepository.java

 Repository interface for PasswordResetRequest

 Author: Joshua Reid Adams (230317693)

 Date: 14th August 2026
*/

package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.PasswordResetRequest;

import java.util.List;

public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequest, Integer> {

    // Newest reset request first. The service takes the first one.
    List<PasswordResetRequest> findByEmailOrderByCreatedDateDesc(String email);

    @Transactional
    void deleteByEmail(String email);
}
