package adms.clinic.doctor

import adms.clinic.MvcIntegrationTest
import adms.clinic.doctor.entity.Doctor
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

class DoctorControllerIntegrationTest :
        MvcIntegrationTest() {

    @Autowired
    private lateinit var repository: DoctorRepository

    @Test
    fun `should get doctor list`() {
        val doc = repository.save(Doctor("Adam", "Test", "Surgery"))

        mvc.perform(get("/doctors")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
                .andExpect(jsonPath("\$.size()").value(`is`(1)))
                .andExpect(jsonPath("\$.[0]id").value(`is`(doc.id.toInt())))
                .andExpect(jsonPath("\$.[0].firstName").value(`is`(doc.firstName)))
                .andExpect(jsonPath("\$.[0].lastName").value(`is`(doc.lastName)))
                .andExpect(jsonPath("\$.[0].specialization").value(`is`(doc.specialization)))
                .andExpect(jsonPath("\$.[0].version").value(`is`(doc.version)))
                .andExpect(jsonPath("\$.[0].createdAt").value(`is`(doc.createdAt.toString())))
                .andExpect(jsonPath("\$.[0].editedAt").value(`is`(doc.editedAt.toString())))
    }

    @Test
    fun `should get doctor`() {
        val doc = repository.save(Doctor("Adam", "Test", "Surgery"))

        mvc.perform(get("/doctors/" + doc.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(`is`(doc.id.toInt())))
                .andExpect(jsonPath("\$.firstName").value(`is`(doc.firstName)))
                .andExpect(jsonPath("\$.lastName").value(`is`(doc.lastName)))
                .andExpect(jsonPath("\$.specialization").value(`is`(doc.specialization)))
                .andExpect(jsonPath("\$.version").value(`is`(doc.version)))
                .andExpect(jsonPath("\$.createdAt").value(`is`(doc.createdAt.toString())))
                .andExpect(jsonPath("\$.editedAt").value(`is`(doc.editedAt.toString())))
    }


    @Test
    fun `should save doctor`() {
        val response = mvc.perform(post("/doctors")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject(mapOf(
                        "firstName" to "Adam",
                        "lastName" to "Test",
                        "specialization" to "Orthopedics"
                )).toString())
        ).andExpect(status().isCreated)
                .andExpect(jsonPath("\$.id").value(notNullValue())).andReturn().response

        val doctorId = JSONObject(response.contentAsString).getLong("id")
        val doctorOpt = repository.findById(doctorId)
        assertThat("Doctor found", doctorOpt.isPresent)

        doctorOpt.ifPresent {
            run {
                assertThat(it.firstName, `is`("Adam"))
                assertThat(it.lastName, `is`("Test"))
                assertThat(it.specialization, `is`("Orthopedics"))
                assertThat(it.version, `is`(1))
                assertThat(it.createdAt, notNullValue())
                assertThat(it.editedAt, notNullValue())
            }
        }
    }

    @Test
    fun `should update doctor`() {
        val doc = repository.save(Doctor("Adam", "Test", "Surgery"))
        val oldEditedAt = doc.editedAt
        val oldVersion = doc.version

        mvc.perform(put("/doctors/" + doc.id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject(mapOf(
                        "firstName" to "Janusz",
                        "lastName" to "Testowy",
                        "specialization" to "Psychiatry"
                )).toString())
        ).andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(`is`(doc.id.toInt())))

        entityManager.flush()

        assertThat(doc.firstName, `is`("Janusz"))
        assertThat(doc.lastName, `is`("Testowy"))
        assertThat(doc.specialization, `is`("Psychiatry"))
        assertThat(doc.version, `is`(oldVersion + 1))
        assertThat(doc.createdAt, `is`(doc.createdAt))
        assertThat("Updated dctor editedAt date is later then previous on", doc.editedAt.isAfter(oldEditedAt))
    }

    @Test
    fun `should delete doctor`() {
        val doc = repository.save(Doctor("Adam", "Test", "Surgery"))

        mvc.perform(delete("/doctors/" + doc.id)
        ).andExpect(status().isNoContent)

        val docOpt = repository.findById(doc.id)
        assertThat("Deleted doctor not exists", !docOpt.isPresent)
    }
}