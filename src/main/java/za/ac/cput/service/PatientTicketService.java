/*
 PatientTicketService.java

 Service class for PatientTicket

 Author: Joshua Reid Adams (230317693)

 Date: 6th July 2026
*/

package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.repository.AppointmentRepository;
import za.ac.cput.repository.PatientTicketRepository;
import za.ac.cput.service.impl.IPatientTicketService;

import java.util.List;

@Service
public class PatientTicketService implements IPatientTicketService {

    @Autowired
    private PatientTicketRepository repository;

    // Uses the repository directly because AppointmentService already uses this service.
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public PatientTicket create(PatientTicket ticket) {
        if (ticket == null) return null;

        PatientTicket saved = repository.save(ticket);
        saved.addStatus(StatusType.OPEN);
        return repository.save(saved);
    }

    @Override
    public PatientTicket read(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public PatientTicket update(PatientTicket ticket) {
        if (ticket == null) return null;
        return repository.save(ticket);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<PatientTicket> getAll() {
        return repository.findAll();
    }

    @Override
    public List<PatientTicket> findByCurrentStatus(StatusType status) {
        return repository.findByCurrentStatus(status);
    }

    @Override
    public List<PatientTicket> findByPatientUserId(int patientId) {
        return repository.findByPatientUserId(patientId);
    }

    @Override
    public PatientTicket progressStatus(int ticketId, StatusType newStatus, String notes) {
        PatientTicket ticket = read(ticketId);
        if (ticket == null) return null;

        boolean wasResolved = ticket.getCurrentStatus() == StatusType.RESOLVED;

        ticket.addStatus(newStatus, notes);
        PatientTicket saved = repository.save(ticket);

        completeAppointmentIfResolved(saved);

        // Only the first time it gets resolved, so the nurse isn't notified twice.
        if (newStatus == StatusType.RESOLVED && !wasResolved) {
            notifyStaffConsultationComplete(saved);
        }

        return saved;
    }

    @Override
    public PatientTicket findByAppointment(Appointment appointment) {
        if (appointment == null) return null;
        return repository.findByAppointment(appointment);
    }

    // Tells the nurse who approved the booking that the payment can be made.
    private void notifyStaffConsultationComplete(PatientTicket ticket) {
        Appointment appointment = ticket.getAppointment();
        if (appointment == null || appointment.getStaff() == null) return;

        String patient = ticket.getPatient() != null && ticket.getPatient().getName() != null
                ? ticket.getPatient().getName().getFirstName() + " " + ticket.getPatient().getName().getLastName()
                : "the patient";
        String message = "Consultation complete for " + patient + " (ticket TK-"
                + String.format("%03d", ticket.getTicketId())
                + "). The ticket is ready for a payment request.";

        notificationService.notifyStaff(appointment.getStaff(), message, "TICKET_RESOLVED", ticket, appointment);
    }

    // Once the doctor resolves the ticket the appointment is marked COMPLETED.
    private void completeAppointmentIfResolved(PatientTicket ticket) {
        if (ticket.getCurrentStatus() != StatusType.RESOLVED || ticket.getAppointment() == null) {
            return;
        }

        // Reload the appointment by id, the one on the ticket is a lazy proxy.
        Appointment appointment = appointmentRepository
                .findById(ticket.getAppointment().getAppointmentId())
                .orElse(null);
        if (appointment == null) return;
        if (appointment.getConfirmationStatus() == ConfirmationStatus.COMPLETED) {
            return; // already completed
        }

        Appointment updated = new Appointment.Builder()
                .copy(appointment)
                .setConfirmationStatus(ConfirmationStatus.COMPLETED)
                .build();

        appointmentRepository.save(updated);
    }
}
