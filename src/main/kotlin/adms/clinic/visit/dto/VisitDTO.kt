package adms.clinic.visit.dto

import adms.clinic.common.dto.VerionableDTOBase
import adms.clinic.doctor.dto.DoctorShortDTO
import adms.clinic.doctor.dto.PatientShortDTO
import adms.clinic.visit.entity.Visit
import java.time.Instant

class VisitDTO(visit: Visit) : VerionableDTOBase(visit) {
    val visitTime: Instant = visit.visitTime
    val clinic: String = visit.clinic
    val doctor: DoctorShortDTO = DoctorShortDTO(visit.doctor)
    val patient: PatientShortDTO = PatientShortDTO(visit.patient)
}