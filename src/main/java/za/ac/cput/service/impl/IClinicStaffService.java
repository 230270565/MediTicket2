/*
 IClinicStaffService.java

 Service interface for ClinicStaff

 Author: Matthew Barron (230398863)

 Date: 6th July 2026
*/

package za.ac.cput.service.impl;

import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.user.ClinicStaff;

import java.util.List;
import java.util.Optional;

public interface IClinicStaffService extends IService<ClinicStaff,Integer> {

    Optional<ClinicStaff> findByEmail(String email);

    List<ClinicStaff> findByDepartment(String department);

    List<ClinicStaff> findByStaffRole(StaffRole staffRole);
}
