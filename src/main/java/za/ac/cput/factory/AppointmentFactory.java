/*
 AppointmentFactory.java

 Factory class for Appointment

 Author: Joshua Peter Bonzet (221312536)

 Date: 22nd June 2026
*/

package za.ac.cput.factory;

import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.util.Helper;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentFactory {

    public static Appointment createAppointment(int appointmentId,
                                                LocalDate appointmentDate,
                                                LocalTime appointmentTime,
                                                ConfirmationStatus confirmationStatus,
                                                Doctor doctor,
                                                ClinicStaff staff) {

        if (!Helper.isValidInt(appointmentId)) return null;
        if (!Helper.isValidObject(appointmentDate) || !Helper.isValidObject(appointmentTime)) return null;
        if (appointmentDate.isBefore(LocalDate.now())) return null;
        if (!Helper.isValidObject(confirmationStatus)) return null;
        if (!Helper.isValidObject(doctor) || !Helper.isValidObject(staff)) return null;

        return new Appointment.Builder()
                .setAppointmentId(appointmentId)
                .setAppointmentDate(appointmentDate)
                .setAppointmentTime(appointmentTime)
                .setConfirmationStatus(confirmationStatus)
                .setDoctor(doctor)
                .setStaff(staff)
                .build();
    }
}
