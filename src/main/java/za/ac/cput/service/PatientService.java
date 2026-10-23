/*
 PatientService.java

 Service class for Patient

 Author: Aidan Barends (230255639)

 Date: 6th July 2026
*/

package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.repository.AppointmentRepository;
import za.ac.cput.repository.NotificationRepository;
import za.ac.cput.repository.PatientRepository;
import za.ac.cput.repository.PatientTicketRepository;
import za.ac.cput.repository.PaymentRepository;
import za.ac.cput.service.impl.IPatientService;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PatientService implements IPatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientTicketRepository patientTicketRepository;
    private final NotificationRepository notificationRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          PatientTicketRepository patientTicketRepository,
                          NotificationRepository notificationRepository,
                          PaymentRepository paymentRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientTicketRepository = patientTicketRepository;
        this.notificationRepository = notificationRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Patient create(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient read(Integer id) {
        return patientRepository.findById(id).get();
    }

    @Override
    public Patient update(Patient patient) {
        Patient existing = this.patientRepository.findById(patient.getUserId()).orElse(null);
        if (existing == null) {
            return null;
        }

        Patient updated = new Patient.Builder()
                .copy(existing)
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setCellPhone(patient.getCellPhone())
                .setPassword(patient.getPassword())
                .setDob(patient.getDob())
                .setAccountStatus(patient.getAccountStatus())
                .setDateRegistered(patient.getDateRegistered())
                .setEmergencyContact(patient.getEmergencyContact())
                .build();

        return this.patientRepository.save(updated);
    }

    // Delete the patient's records first (notifications, payments, tickets, appointments)
    // or the foreign keys stop the delete.
    @Override
    @Transactional
    public void delete(Integer id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) {
            return;
        }

        List<Appointment> appointments = appointmentRepository.findByPatient_UserId(id);
        List<PatientTicket> tickets = patientTicketRepository.findByPatientUserId(id);

        Set<Notification> notifications = new LinkedHashSet<>(notificationRepository.findByPatient(patient));
        if (!appointments.isEmpty()) {
            notifications.addAll(notificationRepository.findByAppointmentIn(appointments));
        }
        if (!tickets.isEmpty()) {
            notifications.addAll(notificationRepository.findByTicketIn(tickets));
        }
        notificationRepository.deleteAll(notifications);

        if (!appointments.isEmpty()) {
            List<Payment> payments = paymentRepository.findByAppointmentIn(appointments);
            paymentRepository.deleteAll(payments);
        }

        patientTicketRepository.deleteAll(tickets);
        appointmentRepository.deleteAll(appointments);

        patientRepository.delete(patient);
    }

    @Override
    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return Optional.ofNullable(patientRepository.findByEmail(email));
    }

    @Override
    public List<Patient> findByDateRegistered(LocalDate dateRegistered) {
        return patientRepository.findByDateRegistered(dateRegistered);
    }
}
