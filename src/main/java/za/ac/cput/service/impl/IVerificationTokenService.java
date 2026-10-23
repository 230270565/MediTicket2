/*
 IVerificationTokenService.java

 Service interface for VerificationToken

 Author: Joshua Reid Adams (230317693)

 Date: 11th August 2026
*/

package za.ac.cput.service.impl;

import za.ac.cput.domain.VerificationToken;
import za.ac.cput.domain.enums.UserType;

import java.util.Optional;

// Extends the generic base interface, plus token-specific lookups
public interface IVerificationTokenService extends IService<VerificationToken, Integer> {

    Optional<VerificationToken> findByToken(String token);

    VerificationToken generateAndSaveToken(int userId, UserType userType);

    void deleteExistingToken(int userId, UserType userType);
}
