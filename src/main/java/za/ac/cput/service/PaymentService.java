/*
 PaymentService.java

 Service class for Payment

 Author: Abdullahi Raage Farah (230971091)

 Date: 10th July 2026
*/

package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.PaymentMethod;
import za.ac.cput.domain.enums.PaymentStatus;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.repository.AppointmentRepository;
import za.ac.cput.repository.PatientTicketRepository;
import za.ac.cput.repository.PaymentRepository;
import za.ac.cput.service.impl.IPaymentService;

import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository repository;

    // Used to close the ticket once the payment is PAID.
    @Autowired
    private PatientTicketRepository patientTicketRepository;

    // Reloads the appointment so we have the patient for notifications.
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Payment create(Payment payment) {
        if (payment == null) return null;
        Payment saved = repository.save(payment);

        String method = saved.getPaymentMethod() != null
                ? " by " + saved.getPaymentMethod().name().replace('_', ' ')
                : "";
        notifyPatient(saved, "A payment of R" + saved.getPaymentAmount() + " is due" + method
                + ". You can pay it from your Payments page.", "PAYMENT_DUE", "Payment due");

        return saved;
    }

    @Override
    public Payment read(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Payment update(Payment payment) {
        if (payment == null) return null;

        Payment saved = repository.save(payment);
        closeTicketIfPaid(saved);
        return saved;
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Payment> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Payment> findByPaymentStatus(PaymentStatus paymentStatus) {
        return repository.findByPaymentStatus(paymentStatus);
    }

    @Override
    public List<Payment> findByPaymentMethod(PaymentMethod paymentMethod) {
        return repository.findByPaymentMethod(paymentMethod);
    }

    // Isolated as a private helper so update() stays readable.
    private void closeTicketIfPaid(Payment payment) {
        if (payment.getPaymentStatus() != PaymentStatus.PAID || payment.getAppointment() == null) {
            return;
        }

        PatientTicket ticket = patientTicketRepository.findByAppointment(payment.getAppointment());
        if (ticket == null || ticket.getCurrentStatus() == StatusType.CLOSED) {
            return; // no ticket yet, or it is already closed
        }

        ticket.addStatus(StatusType.CLOSED, "Closed automatically — payment received");
        patientTicketRepository.save(ticket);

        // Inside the "not already closed" branch, so it is sent once only.
        notifyPatient(payment, "Payment of R" + payment.getPaymentAmount()
                + " received. Your ticket has been closed. Thank you.", "TICKET_CLOSED", "Payment received");
    }

    private void notifyPatient(Payment payment, String message, String stage, String emailSubject) {
        if (payment.getAppointment() == null) return;

        Appointment appointment = appointmentRepository
                .findById(payment.getAppointment().getAppointmentId())
                .orElse(null);
        if (appointment == null || appointment.getPatient() == null) return;

        PatientTicket ticket = patientTicketRepository.findByAppointment(appointment);
        notificationService.notifyPatient(appointment.getPatient(), message, stage,
                ticket, appointment, emailSubject);
    }
}
