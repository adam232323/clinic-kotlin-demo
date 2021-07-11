package adms.clinic.patient

import adms.clinic.common.dto.IdentifiableDTO
import adms.clinic.common.exception.EntityNotFoundException
import adms.clinic.doctor.data.PatientData
import adms.clinic.doctor.dto.PatientDTO
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("patients")
@RestController
class PatientController(
        private val repository: PatientRepository,
        private val service: PatientService
) {

    @Transactional(readOnly = true)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<PatientDTO> {
        return repository.findAll().map { PatientDTO(it) }
    }

    @Transactional(readOnly = true)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): PatientDTO {
        val doctor = repository.findById(id).orElseThrow { EntityNotFoundException("Patient: $id") }
        return PatientDTO(doctor)
    }

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun save(@Valid @RequestBody data: PatientData): IdentifiableDTO {
        return IdentifiableDTO(service.save(data))
    }

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody data: PatientData): IdentifiableDTO {
        val doctor = repository.findById(id).orElseThrow { EntityNotFoundException("Patient: $id") }
        return IdentifiableDTO(service.update(doctor, data))
    }

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long){
        service.delete(id)
    }
}