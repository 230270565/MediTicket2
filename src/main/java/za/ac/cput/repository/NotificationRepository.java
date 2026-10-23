/*
 NotificationRepository.java

 Repository interface for Notification

 Author: Raul Jaaim Everts (230270565)

 Date: 29th June 2026
*/

package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByPatient(Patient patient);

    List<Notification> findByDoctor(Doctor doctor);

    List<Notification> findByClinicStaff(ClinicStaff clinicStaff);

    List<Notification> findByNotificationStatus(NotificationStatus notificationStatus);

    List<Notification> findByNotificationType(NotificationType notificationType);

    // Used when deleting a patient, so no notifications are left pointing at their records.
    List<Notification> findByAppointmentIn(List<Appointment> appointments);

    List<Notification> findByTicketIn(List<PatientTicket> tickets);

    // Updates one column only, so the other fields can't get lost.
    @Modifying
    @Transactional
    @Query("update Notification n set n.notificationStatus = za.ac.cput.domain.enums.NotificationStatus.READ where n.notificationId = :id")
    int markAsRead(@Param("id") int id);

    @Modifying
    @Transactional
    @Query("update Notification n set n.emailSent = true where n.notificationId = :id")
    int markEmailSent(@Param("id") int id);
}
