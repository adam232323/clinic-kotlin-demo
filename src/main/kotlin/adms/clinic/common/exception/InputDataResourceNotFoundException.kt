package adms.clinic.common.exception

class InputDataResourceNotFoundException(
        val resource : String,
        val identifier : Long
) : RuntimeException() {
}