package adms.clinic.visit.data

import java.time.Instant
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class VisitCreateData(

        @field: NotNull
        val visitTime: Instant,

        @field: NotEmpty
        @field: Size(min = 3, max = 255)
        val clinic: String,

        @field: NotNull
        val doctorId: Long,

        @field: NotNull
        val patientId: Long
)