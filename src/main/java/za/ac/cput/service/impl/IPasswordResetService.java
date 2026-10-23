/*
 IPasswordResetService.java

 Service interface for PasswordReset

 Author: Joshua Reid Adams (230317693)

 Date: 14th August 2026
*/

package za.ac.cput.service.impl;

public interface IPasswordResetService {

    // Always returns true so the reply is the same whether the email exists or not.
    boolean requestReset(String email);

    // Returns the reset token, or null if the code is wrong or expired.
    String verifyCode(String email, String code);

    // Needs the token from verifyCode().
    boolean resetPassword(String email, String resetSessionToken, String newPassword);
}
