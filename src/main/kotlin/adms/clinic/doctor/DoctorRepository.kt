package adms.clinic.doctor

import adms.clinic.doctor.entity.Doctor
import org.springframework.data.repository.CrudRepository

interface DoctorRepository : CrudRepository<Doctor, Long> {
}