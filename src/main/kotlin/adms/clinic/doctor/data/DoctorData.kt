package adms.clinic.doctor.data

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class DoctorData(

        @field:NotEmpty
        @field:Size(min=3, max=100)
        val firstName: String,

        @field:NotEmpty
        @field:Size(min=3, max=100)
        val lastName: String,

        @field:NotEmpty
        @field:Size(min=3, max=100)
        val specialization: String
)