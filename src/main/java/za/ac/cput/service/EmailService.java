/*
 EmailService.java

 Service class for Email

 Author: Joshua Reid Adams (230317693)

 Date: 11th August 2026
*/

package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.repository.NotificationRepository;
import za.ac.cput.service.impl.IEmailService;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private final JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    // Used only to flag a notification as emailed once the send succeeds.
    @Autowired
    private NotificationRepository notificationRepository;

    @Value("${app.verification.base-url}")
    private String verificationBaseUrl;

    @Value("${app.employee-invite.base-url}")
    private String employeeInviteBaseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendVerificationEmail(String toEmail, String firstName, String token) {
        String verificationLink = verificationBaseUrl + "?token=" + token;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Verify your Mediticket account");
            message.setText(
                    "Hi " + firstName + ",\n\n" +
                            "Thanks for signing up to Mediticket. Click the link below to verify your account and log in:\n\n" +
                            verificationLink + "\n\n" +
                            "This link will expire in 24 hours.\n\n" +
                            "If you did not create this account, please ignore this email."
            );
            mailSender.send(message);
        } catch (Exception e) {
            logger.warn("Verification email to {} failed: {}", toEmail, e.getMessage());
        }
    }

    @Async
    @Override
    public void sendEmployeeInviteEmail(String toEmail, UserType userType, String token) {
        String role = userType == UserType.DOCTOR ? "doctor" : "clinic staff";
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("You're invited to join MediTicket as a " + role);
            message.setText(
                    "Hello,\n\n" +
                            "You have been invited to register as a " + role + " on MediTicket.\n\n" +
                            "To complete your signup, open the MediTicket desktop app, choose " +
                            "\"Already have a token? Enter it here\" on the access request screen, and enter the code below:\n\n" +
                            token + "\n\n" +
                            "This code will expire in 48 hours.\n\n" +
                            "If you did not expect this invitation, please ignore this email."
            );
            mailSender.send(message);
        } catch (Exception e) {
            logger.warn("Employee invite email to {} failed: {}", toEmail, e.getMessage());
        }
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("MediTicket Password Reset");
        message.setText(
                "Hello,\n\n" +
                        "Your password reset code is:\n\n" +
                        code + "\n\n" +
                        "This code will expire in 10 minutes.\n\n" +
                        "If you did not request a password reset, you can safely ignore this email."
        );
        mailSender.send(message);
    }

    // Emails a copy of a notification. Runs in the background so the request doesn't wait for Gmail.
    // If it fails we just log it.
    @Async
    public void sendNotificationEmail(int notificationId, String toEmail, String firstName,
                                      String subject, String body) {
        if (toEmail == null || toEmail.isBlank()) return;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("MediTicket: " + subject);
            message.setText(
                    "Hi " + (firstName != null ? firstName : "there") + ",\n\n" +
                            body + "\n\n" +
                            "You can see the details in the MediTicket app under Notifications.\n\n" +
                            "MediTicket Clinic"
            );
            mailSender.send(message);
            notificationRepository.markEmailSent(notificationId);
        } catch (Exception e) {
            logger.warn("Notification email to {} failed: {}", toEmail, e.getMessage());
        }
    }
}
