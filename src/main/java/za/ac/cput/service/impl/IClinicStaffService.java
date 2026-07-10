package za.ac.cput.service.impl;

import za.ac.cput.domain.user.ClinicStaff;

import java.util.List;

public interface IClinicStaffService {

    ClinicStaff create(ClinicStaff clinicStaff);

    ClinicStaff read(Integer id);

    ClinicStaff update(ClinicStaff clinicStaff);

    void delete(Integer id);

    List<ClinicStaff> getAll();

    ClinicStaff findByEmail(String email);

    List<ClinicStaff> findByDepartment(String department);

    List<ClinicStaff> findByStaffRole(String staffRole);
}