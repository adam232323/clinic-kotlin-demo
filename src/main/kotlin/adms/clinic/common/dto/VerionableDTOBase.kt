package adms.clinic.common.dto

import adms.clinic.common.entity.VersionableEntityBase
import java.time.Instant

abstract class VerionableDTOBase(
        val id: Long = 0,
        val version: Int = 0,
        val createdAt: Instant = Instant.MIN,
        val editedAt: Instant = Instant.MIN
) {
    constructor(entityBase: VersionableEntityBase) : this(
            entityBase.id,
            entityBase.version,
            entityBase.createdAt,
            entityBase.editedAt)
}