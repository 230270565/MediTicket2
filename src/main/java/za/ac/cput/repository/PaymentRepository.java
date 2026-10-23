/*
 PaymentRepository.java

 Repository interface for Payment

 Author: Abdullahi Raage Farah (230971091)

 Date: 5th July 2026
*/

package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.enums.PaymentMethod;
import za.ac.cput.domain.enums.PaymentStatus;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    // Used when deleting a patient. Payments have to go before the appointments.
    List<Payment> findByAppointmentIn(List<Appointment> appointments);

}
