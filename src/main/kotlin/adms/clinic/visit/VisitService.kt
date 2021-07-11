package adms.clinic.visit

import adms.clinic.common.exception.InputDataResourceNotFoundException
import adms.clinic.common.logger
import adms.clinic.doctor.DoctorRepository
import adms.clinic.patient.PatientRepository
import adms.clinic.visit.data.VisitCreateData
import adms.clinic.visit.data.VisitUpdateData
import adms.clinic.visit.entity.Visit
import org.slf4j.Logger
import org.springframework.stereotype.Service

//TODO make simple validation for visit time. In one clinic there shouldn't be two visits at same time window
@Service
class VisitService(
        private val repository: VisitRepository,
        private val patientRepository: PatientRepository,
        private val doctorRepository: DoctorRepository
) {
    val log: Logger = logger()

    fun save(data: VisitCreateData): Visit {
        val patient = patientRepository.findById(data.patientId)
                .orElseThrow { InputDataResourceNotFoundException("Patient", data.patientId) }

        val doctor = doctorRepository.findById(data.doctorId)
                .orElseThrow { InputDataResourceNotFoundException("Doctor", data.doctorId) }

        val saved = repository.save(Visit(data.visitTime, data.clinic, patient, doctor))
        log.debug("Visit CREATED {}", saved)
        return saved
    }

    fun update(visit: Visit, data: VisitUpdateData): Visit {
        visit.visitTime = data.visitTime
        val updated = repository.save(visit)
        log.debug("Visit UPDATED {}", updated)
        return updated
    }

    fun delete(id: Long){
        repository.deleteById(id)
        log.debug("Visit DELETED: {}", id)
    }
}