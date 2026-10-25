/*
 NotificationControllerTest.java

 Test class for NotificationController

 Author: Raul Jaaim Everts (230270565)

 Date: 13th July 2026
*/

package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.service.ClinicStaffService;
import za.ac.cput.service.DoctorService;
import za.ac.cput.service.NotificationService;
import za.ac.cput.service.PatientService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private ClinicStaffService clinicStaffService;

    @InjectMocks
    private NotificationController controller;

    private Notification notification;
    private Notification invalidNotification;

    @BeforeEach
    void setUp() {
        Name name = new Name.Builder()
                .setFirstName("John")
                .setMiddleName("A")
                .setLastName("Doe")
                .build();

        Patient patient = new Patient.Builder()
                .setUserId(1)
                .setName(name)
                .setEmail("john.doe@email.com")
                .setCellPhone("0821234567")
                .setPassword("password123")
                .setDob(LocalDate.of(1990, 1, 1))
                .setAccountStatus(UserStatus.ACTIVE)
                .setDateRegistered(LocalDate.now())
                .setEmergencyContact("0829876543")
                .build();

        Appointment appointment = new Appointment.Builder()
                .setAppointmentId(1)
                .setAppointmentDate(LocalDate.now())
                .setAppointmentTime(LocalTime.of(10, 0))
                .setConfirmationStatus(ConfirmationStatus.CONFIRMED)
                .build();

        PatientTicket ticket = new PatientTicket.Builder()
                .setTicketId(1)
                .setTicketDescription("General checkup")
                .setTicketCreatedDate(LocalDateTime.now())
                .setPatient(patient)
                .setAppointment(appointment)
                .build();

        // A fully valid notification — passes factory validation.
        notification = new Notification.Builder()
                .setNotificationId(1)
                .setNotificationType(NotificationType.EMAIL)
                .setNotificationStatus(NotificationStatus.PENDING)
                .setNotificationMessage("Your appointment is confirmed.")
                .setPatient(patient)
                .setTicket(ticket)
                .setAppointment(appointment)
                .setNotificationDate(LocalDateTime.now())
                .build();

        // Missing type + status — factory returns null, triggers 400.
        invalidNotification = new Notification.Builder()
                .setNotificationId(2)
                .build();
    }

    @Test
    void create_returnsOk_whenNotificationIsValid() {
        when(notificationService.create(any(Notification.class))).thenReturn(notification);

        ResponseEntity<Notification> response = controller.create(notification);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(notificationService, times(1)).create(any(Notification.class));
    }

    @Test
    void create_returnsBadRequest_whenNotificationIsInvalid() {
        ResponseEntity<Notification> response = controller.create(invalidNotification);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(notificationService, never()).create(any());
    }

    @Test
    void read_returnsOk_whenFound() {
        when(notificationService.read(1)).thenReturn(notification);

        ResponseEntity<Notification> response = controller.read(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notification, response.getBody());
    }

    @Test
    void read_returnsNotFound_whenNotFound() {
        when(notificationService.read(99)).thenReturn(null);

        ResponseEntity<Notification> response = controller.read(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void update_returnsOk_whenExistingNotificationFound() {
        when(notificationService.read(1)).thenReturn(notification);
        when(notificationService.update(notification)).thenReturn(notification);

        ResponseEntity<Notification> response = controller.update(notification);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notification, response.getBody());
        verify(notificationService, times(1)).update(notification);
    }

    @Test
    void update_returnsNotFound_whenNotificationDoesNotExist() {
        when(notificationService.read(1)).thenReturn(null);

        ResponseEntity<Notification> response = controller.update(notification);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(notificationService, never()).update(any());
    }

    @Test
    void delete_returnsNoContent_whenExists() {
        when(notificationService.read(1)).thenReturn(notification);
        doNothing().when(notificationService).delete(1);

        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(notificationService, times(1)).delete(1);
    }

    @Test
    void delete_returnsNotFound_whenNotExists() {
        when(notificationService.read(99)).thenReturn(null);

        ResponseEntity<Void> response = controller.delete(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(notificationService, never()).delete(anyInt());
    }

    @Test
    void getAll_returnsOkWithList() {
        when(notificationService.getAll()).thenReturn(List.of(notification));

        ResponseEntity<List<Notification>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findByNotificationStatus_returnsOkWithMatchingList() {
        when(notificationService.findByNotificationStatus(NotificationStatus.PENDING))
                .thenReturn(List.of(notification));

        ResponseEntity<List<Notification>> response =
                controller.findByNotificationStatus(NotificationStatus.PENDING);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(notificationService, times(1)).findByNotificationStatus(NotificationStatus.PENDING);
    }

    @Test
    void findByNotificationType_returnsOkWithMatchingList() {
        when(notificationService.findByNotificationType(NotificationType.EMAIL))
                .thenReturn(List.of(notification));

        ResponseEntity<List<Notification>> response =
                controller.findByNotificationType(NotificationType.EMAIL);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(notificationService, times(1)).findByNotificationType(NotificationType.EMAIL);
    }
}
