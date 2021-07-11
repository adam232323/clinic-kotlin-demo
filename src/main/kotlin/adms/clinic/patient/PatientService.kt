package adms.clinic.patient

import adms.clinic.common.logger
import adms.clinic.doctor.data.PatientData
import adms.clinic.patient.entity.Patient
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class PatientService(
        private val repository: PatientRepository
) {
    val log: Logger = logger()

    fun save(data: PatientData): Patient {
        val saved = repository.save(Patient(data.firstName, data.lastName, data.address))

        log.debug("Patient CREATED: {}", saved)
        return saved
    }

    fun update(patient: Patient, data: PatientData): Patient {
        patient.firstName = data.firstName
        patient.lastName = data.lastName
        patient.address = data.address

        val updated = repository.save(patient)
        log.debug("Patient UPDATED: {}", updated)

        return updated
    }

    fun delete(id: Long){
        repository.deleteById(id)
        log.debug("Patient DELETED: {}", id)
    }
}