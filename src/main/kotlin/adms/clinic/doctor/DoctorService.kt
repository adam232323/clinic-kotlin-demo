package adms.clinic.doctor

import adms.clinic.common.logger
import adms.clinic.doctor.data.DoctorData
import adms.clinic.doctor.entity.Doctor
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class DoctorService(
        private val repository: DoctorRepository
) {
    val log: Logger = logger()

    fun save(data: DoctorData): Doctor {
        val saved = repository.save(Doctor(data.firstName, data.lastName, data.specialization))
        log.debug("Doctor CREATED: {}", saved)
        return saved
    }

    fun update(doctor: Doctor, data: DoctorData): Doctor {
        doctor.firstName = data.firstName
        doctor.lastName = data.lastName
        doctor.specialization = data.specialization

        val updated = repository.save(doctor)
        log.debug("Doctor UPDATED: {}", updated)
        return updated
    }

    fun delete(id: Long){
        repository.deleteById(id)
        log.debug("Doctor DELETED: {}", id)
    }
}