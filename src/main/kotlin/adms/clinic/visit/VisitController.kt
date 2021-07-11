package adms.clinic.visit

import adms.clinic.common.dto.IdentifiableDTO
import adms.clinic.common.exception.EntityNotFoundException
import adms.clinic.visit.data.VisitCreateData
import adms.clinic.visit.data.VisitUpdateData
import adms.clinic.visit.dto.VisitDTO
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import javax.validation.Valid

@RequestMapping("visits")
@RestController
class VisitController(
        private val repository: VisitRepository,
        private val service: VisitService
) {

    @Transactional(readOnly = true)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(@RequestParam(required = false) patientId: Long?): List<VisitDTO> {
        return repository.findByPatientId(patientId).map { VisitDTO(it) }.collect(Collectors.toList())
    }

    @Transactional(readOnly = true)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): VisitDTO {
        val visit = repository.findById(id).orElseThrow { EntityNotFoundException("Visit: $id") }
        return VisitDTO(visit)
    }

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun save(@Valid @RequestBody data: VisitCreateData): IdentifiableDTO {
        return IdentifiableDTO(service.save(data))
    }

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody data: VisitUpdateData): IdentifiableDTO {
        val visit = repository.findById(id).orElseThrow { EntityNotFoundException("Visit: $id") }
        return IdentifiableDTO(service.update(visit, data))
    }

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }
}