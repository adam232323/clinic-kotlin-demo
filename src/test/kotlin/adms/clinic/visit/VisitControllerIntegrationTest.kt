package adms.clinic.doctor

import adms.clinic.MvcIntegrationTest
import adms.clinic.doctor.entity.Doctor
import adms.clinic.patient.PatientRepository
import adms.clinic.patient.entity.Patient
import adms.clinic.visit.VisitRepository
import adms.clinic.visit.entity.Visit
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.time.temporal.ChronoUnit

class VisitControllerIntegrationTest :
        MvcIntegrationTest() {

    @Autowired
    private lateinit var patientRepository: PatientRepository
    @Autowired
    private lateinit var docRepository: DoctorRepository
    @Autowired
    private lateinit var repository: VisitRepository

    fun seedPatient() : Patient{
        return patientRepository.save(Patient("Sick", "Patient", "Warsaw"))
    }

    fun seedDoctor() : Doctor{
        return docRepository.save(Doctor("Intelligent", "Doctor", "Dentist"))
    }

    fun seedVisit() : Visit{
        val patient = seedPatient()
        val doctor = seedDoctor()
        return repository.save(Visit(Instant.now(), "Some Clinic", patient, doctor))
    }

    @Test
    fun `should get visit list filtered by patientId`() {
        val patient1 = patientRepository.save(Patient("Patient", "First", "Warsaw"))
        val patient2 = patientRepository.save(Patient("Patient", "Second", "Warsaw"))
        val doctor = seedDoctor()

        repository.save(Visit(Instant.now(), "Some Clinic", patient1, doctor))
        val visit2 = repository.save(Visit(Instant.now(), "Some Clinic", patient2, doctor))

        mvc.perform(get("/visits")
                .param("patientId", patient2.id.toString())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
                .andExpect(jsonPath("\$.size()").value(`is`(1)))
                .andExpect(jsonPath("\$.[0]id").value(`is`(visit2.id.toInt())))
                .andExpect(jsonPath("\$.[0].visitTime").value(`is`(visit2.visitTime.toString())))
                .andExpect(jsonPath("\$.[0].clinic").value(`is`(visit2.clinic)))
    }

    @Test
    fun `should get visit`() {
        val visit = seedVisit()
        val patient = visit.patient
        val doctor = visit.doctor

        mvc.perform(get("/visits/" + visit.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(`is`(visit.id.toInt())))
                .andExpect(jsonPath("\$.visitTime").value(`is`(visit.visitTime.toString())))
                .andExpect(jsonPath("\$.clinic").value(`is`(visit.clinic)))
                .andExpect(jsonPath("\$.version").value(`is`(visit.version)))
                .andExpect(jsonPath("\$.createdAt").value(`is`(visit.createdAt.toString())))
                .andExpect(jsonPath("\$.editedAt").value(`is`(visit.editedAt.toString())))
                .andExpect(jsonPath("\$.patient.id").value(`is`(patient.id.toInt())))
                .andExpect(jsonPath("\$.patient.firstName").value(`is`(patient.firstName)))
                .andExpect(jsonPath("\$.patient.lastName").value(`is`(patient.lastName)))
                .andExpect(jsonPath("\$.doctor.id").value(`is`(doctor.id.toInt())))
                .andExpect(jsonPath("\$.doctor.firstName").value(`is`(doctor.firstName)))
                .andExpect(jsonPath("\$.doctor.lastName").value(`is`(doctor.lastName)))
    }


    @Test
    fun `should save visit`() {
        val patient = seedPatient()
        val doctor = seedDoctor()

        val visitTime = Instant.now()
        val visitName = "Some Clinic"

        val response = mvc.perform(post("/visits")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject(mapOf(
                        "visitTime" to visitTime.toString(),
                        "clinic" to visitName,
                        "patientId" to patient.id,
                        "doctorId" to doctor.id
                )).toString())
        ).andExpect(status().isCreated)
                .andExpect(jsonPath("\$.id").value(notNullValue())).andReturn().response

        val visitId = JSONObject(response.contentAsString).getLong("id")
        val visitOpt = repository.findById(visitId)
        assertThat("Visit found", visitOpt.isPresent)

        visitOpt.ifPresent {
            run {
                assertThat(it.visitTime, `is`(visitTime))
                assertThat(it.clinic, `is`(visitName))
                assertThat(it.patient, `is`(patient))
                assertThat(it.doctor, `is`(doctor))
            }
        }
    }

    @Test
    fun `should update visit`() {
        val visit = seedVisit()

        val newVisitTime = visit.visitTime.plus(15, ChronoUnit.MINUTES)

        mvc.perform(put("/visits/" + visit.id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject(mapOf(
                        "visitTime" to newVisitTime.toString()
                )).toString())
        ).andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(`is`(visit.id.toInt())))

        entityManager.flush()

        assertThat(visit.visitTime, `is`(newVisitTime))
    }

    @Test
    fun `should delete visit`() {
        val visit = seedVisit()

        mvc.perform(delete("/visits/" + visit.id)
        ).andExpect(status().isNoContent)

        val visitOpt = repository.findById(visit.id)
        assertThat("Deleted visit not exists", !visitOpt.isPresent)
    }
}