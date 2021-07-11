package adms.clinic.doctor

import adms.clinic.common.dto.IdentifiableDTO
import adms.clinic.common.exception.EntityNotFoundException
import adms.clinic.doctor.data.DoctorData
import adms.clinic.doctor.dto.DoctorDTO
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("doctors")
@RestController
class DoctorController(
        private val repository: DoctorRepository,
        private val service: DoctorService
) {

    @Transactional(readOnly = true)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<DoctorDTO> {
        return repository.findAll().map { DoctorDTO(it) }
    }

    @Transactional(readOnly = true)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): DoctorDTO {
        val doctor = repository.findById(id).orElseThrow { EntityNotFoundException("Doctor: $id") }
        return DoctorDTO(doctor)
    }

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun save(@Valid @RequestBody data: DoctorData): IdentifiableDTO {
        return IdentifiableDTO(service.save(data))
    }

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody data: DoctorData): IdentifiableDTO {
        val doctor = repository.findById(id).orElseThrow { EntityNotFoundException("Doctor: $id") }
        return IdentifiableDTO(service.update(doctor, data))
    }

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long){
        service.delete(id)
    }
}