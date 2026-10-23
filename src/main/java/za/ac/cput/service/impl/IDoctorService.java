/*
 IDoctorService.java

 Service interface for Doctor

 Author: Jaden Clayton Abrahams (222206721)

 Date: 6th July 2026
*/

package za.ac.cput.service.impl;

import za.ac.cput.domain.user.Doctor;

import java.util.Optional;

public interface IDoctorService extends IService <Doctor,Integer> {

    Optional<Doctor> findByEmail(String email);

}

