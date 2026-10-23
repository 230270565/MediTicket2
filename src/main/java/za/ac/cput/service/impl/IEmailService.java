/*
 IEmailService.java

 Service interface for Email

 Author: Joshua Reid Adams (230317693)

 Date: 11th August 2026
*/

package za.ac.cput.service.impl;

import za.ac.cput.domain.enums.UserType;

// Not a CRUD service, so it doesn't extend IService.
public interface IEmailService {

    void sendVerificationEmail(String toEmail, String firstName, String token);

    void sendEmployeeInviteEmail(String toEmail, UserType userType, String token);

    void sendPasswordResetEmail(String toEmail, String code);
}
