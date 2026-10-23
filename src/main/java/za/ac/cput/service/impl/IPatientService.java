/*
 IPatientService.java

 Service interface for Patient

 Author: Aidan Barends (230255639)

 Date: 29th June 2026
*/

package za.ac.cput.service.impl;

import za.ac.cput.domain.user.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IPatientService extends IService <Patient,Integer > {

    Optional<Patient> findByEmail(String email);

    List<Patient> findByDateRegistered(LocalDate dateRegistered);

}
