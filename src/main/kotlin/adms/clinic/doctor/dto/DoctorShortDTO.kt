package adms.clinic.doctor.dto

import adms.clinic.common.dto.IdentifiableDTOBase
import adms.clinic.doctor.entity.Doctor

class DoctorShortDTO(doctor: Doctor) : IdentifiableDTOBase(doctor) {
    val firstName: String = doctor.firstName
    val lastName: String = doctor.lastName
    val specialization: String = doctor.specialization
}