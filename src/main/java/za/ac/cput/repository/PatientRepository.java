/*
 PatientRepository.java

 Repository interface for Patient

 Author: Aidan Barends (230255639)

 Date: 29th June 2026
*/

package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.user.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository <Patient, Integer> {

    List<Patient> findByDateRegistered(LocalDate dateRegistered);

    Patient findByEmail(String email);

}
