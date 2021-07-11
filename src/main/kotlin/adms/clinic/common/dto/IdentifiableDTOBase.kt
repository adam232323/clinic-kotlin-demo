package adms.clinic.common.dto

import adms.clinic.common.contract.Identifiable

abstract class IdentifiableDTOBase(
        val id: Long = 0
) {
    constructor(identifiable: Identifiable) : this(identifiable.id)
}