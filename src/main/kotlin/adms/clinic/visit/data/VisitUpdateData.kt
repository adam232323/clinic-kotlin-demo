package adms.clinic.visit.data

import java.time.Instant
import javax.validation.constraints.NotNull

data class VisitUpdateData(

        @field: NotNull
        val visitTime: Instant
)