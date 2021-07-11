package adms.clinic.doctor.dto

import adms.clinic.common.dto.IdentifiableDTOBase
import adms.clinic.patient.entity.Patient

class PatientShortDTO(patient: Patient) : IdentifiableDTOBase(patient) {
    val firstName: String = patient.firstName
    val lastName: String = patient.lastName
    val address: String = patient.address
}