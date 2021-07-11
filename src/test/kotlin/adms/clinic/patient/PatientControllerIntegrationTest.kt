package adms.clinic.doctor

import adms.clinic.MvcIntegrationTest
import adms.clinic.patient.PatientRepository
import adms.clinic.patient.entity.Patient
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

class PatientControllerIntegrationTest :
        MvcIntegrationTest() {

    companion object {
        const val ADDRESS_1: String = "Poznan ul. Solna 2"
        const val ADDRESS_2: String = "Wrocław ul. Skyrna 3"
    }

    @Autowired
    private lateinit var repository: PatientRepository

    @Test
    fun `should get patient list`() {
        val patient = repository.save(Patient("Adam", "Test", ADDRESS_1))

        mvc.perform(get("/patients")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
                .andExpect(jsonPath("\$.size()").value(`is`(1)))
                .andExpect(jsonPath("\$.[0]id").value(`is`(patient.id.toInt())))
                .andExpect(jsonPath("\$.[0].firstName").value(`is`(patient.firstName)))
                .andExpect(jsonPath("\$.[0].lastName").value(`is`(patient.lastName)))
                .andExpect(jsonPath("\$.[0].address").value(`is`(patient.address)))
                .andExpect(jsonPath("\$.[0].version").value(`is`(patient.version)))
                .andExpect(jsonPath("\$.[0].createdAt").value(`is`(patient.createdAt.toString())))
                .andExpect(jsonPath("\$.[0].editedAt").value(`is`(patient.editedAt.toString())))
    }

    @Test
    fun `should get patient`() {
        val patient = repository.save(Patient("Adam", "Test", ADDRESS_2))

        mvc.perform(get("/patients/" + patient.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(`is`(patient.id.toInt())))
                .andExpect(jsonPath("\$.firstName").value(`is`(patient.firstName)))
                .andExpect(jsonPath("\$.lastName").value(`is`(patient.lastName)))
                .andExpect(jsonPath("\$.address").value(`is`(patient.address)))
                .andExpect(jsonPath("\$.version").value(`is`(patient.version)))
                .andExpect(jsonPath("\$.createdAt").value(`is`(patient.createdAt.toString())))
                .andExpect(jsonPath("\$.editedAt").value(`is`(patient.editedAt.toString())))
    }


    @Test
    fun `should save patient`() {
        val response = mvc.perform(post("/patients")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject(mapOf(
                        "firstName" to "Adam",
                        "lastName" to "Test",
                        "address" to ADDRESS_1
                )).toString())
        ).andExpect(status().isCreated)
                .andExpect(jsonPath("\$.id").value(notNullValue())).andReturn().response

        val patientId = JSONObject(response.contentAsString).getLong("id")
        val patientOpt = repository.findById(patientId)
        assertThat("Patient found", patientOpt.isPresent)

        patientOpt.ifPresent {
            run {
                assertThat(it.firstName, `is`("Adam"))
                assertThat(it.lastName, `is`("Test"))
                assertThat(it.address, `is`(ADDRESS_1))
                assertThat(it.version, `is`(1))
                assertThat(it.createdAt, notNullValue())
                assertThat(it.editedAt, notNullValue())
            }
        }
    }

    @Test
    fun `should update patient`() {
        val patient = repository.save(Patient("Adam", "Test", ADDRESS_1))
        val oldEditedAt = patient.editedAt
        val oldVersion = patient.version

        mvc.perform(put("/patients/" + patient.id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject(mapOf(
                        "firstName" to "Janusz",
                        "lastName" to "Testowy",
                        "address" to ADDRESS_2
                )).toString())
        ).andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(`is`(patient.id.toInt())))

        entityManager.flush()

        assertThat(patient.firstName, `is`("Janusz"))
        assertThat(patient.lastName, `is`("Testowy"))
        assertThat(patient.address, `is`(ADDRESS_2))
        assertThat(patient.version, `is`(oldVersion + 1))
        assertThat(patient.createdAt, `is`(patient.createdAt))
        assertThat("Updated patient editedAt date is later then previous on", patient.editedAt.isAfter(oldEditedAt))
    }

    @Test
    fun `should delete patient`() {
        val patient = repository.save(Patient("Adam", "Test", ADDRESS_1))

        mvc.perform(delete("/patients/" + patient.id)
        ).andExpect(status().isNoContent)

        val visitOpt = repository.findById(patient.id)
        assertThat("Deleted patient not exists", !visitOpt.isPresent)
    }
}