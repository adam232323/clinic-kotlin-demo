package adms.clinic.doctor.dto

import adms.clinic.common.dto.VerionableDTOBase
import adms.clinic.patient.entity.Patient

class PatientDTO(patient: Patient) : VerionableDTOBase(patient) {
    val firstName: String = patient.firstName
    val lastName: String = patient.lastName
    val address: String = patient.address
}