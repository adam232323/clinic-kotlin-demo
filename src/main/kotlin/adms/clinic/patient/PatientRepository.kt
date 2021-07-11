package adms.clinic.patient

import adms.clinic.patient.entity.Patient
import org.springframework.data.repository.CrudRepository

interface PatientRepository : CrudRepository<Patient, Long> {
}