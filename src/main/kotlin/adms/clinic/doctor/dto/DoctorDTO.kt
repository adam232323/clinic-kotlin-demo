package adms.clinic.doctor.dto

import adms.clinic.common.dto.VerionableDTOBase
import adms.clinic.doctor.entity.Doctor

class DoctorDTO(doctor: Doctor) : VerionableDTOBase(doctor) {
    val firstName: String = doctor.firstName
    val lastName: String = doctor.lastName
    val specialization: String = doctor.specialization
}