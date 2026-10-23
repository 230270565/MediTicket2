/*
 PatientTicketRepository.java

 Repository interface for PatientTicket

 Author: Joshua Reid Adams (230317693)

 Date: 29th June 2026
*/

package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.StatusType;

import java.util.List;

@Repository
public interface PatientTicketRepository extends JpaRepository<PatientTicket, Integer> {
    List<PatientTicket> findByCurrentStatus(StatusType currentStatus);

    List<PatientTicket> findByPatientUserId(int patientId);

    // Finds the ticket for an appointment, e.g. to close it once paid.
    PatientTicket findByAppointment(Appointment appointment);

}
