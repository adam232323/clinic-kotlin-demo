package adms.clinic.common.dto

import adms.clinic.common.contract.Identifiable

data class IdentifiableDTO(
        val id: Long?
){
    constructor(identifiable: Identifiable) : this(identifiable.id)
}