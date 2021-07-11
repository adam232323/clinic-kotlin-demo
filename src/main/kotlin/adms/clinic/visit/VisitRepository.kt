package adms.clinic.visit

import adms.clinic.visit.entity.GRAPH_WITH_DOCTOR_AND_PATIENT
import adms.clinic.visit.entity.Visit
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.stream.Stream

interface VisitRepository : CrudRepository<Visit, Long> {

    @EntityGraph(GRAPH_WITH_DOCTOR_AND_PATIENT)
    @Query("SELECT v FROM Visit v WHERE (:patientId is null or v.patient.id = :patientId)")
    fun findByPatientId(patientId: Long?) : Stream<Visit>
}