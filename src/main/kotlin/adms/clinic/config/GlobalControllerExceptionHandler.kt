package adms.clinic.config

import adms.clinic.common.exception.EntityNotFoundException
import adms.clinic.common.exception.InputDataResourceNotFoundException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(ex: MethodArgumentNotValidException): ErrorDTO {
        val result = ex.bindingResult
        val fieldErrors = result.fieldErrors
        return processFieldErrors(fieldErrors)
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableException(ex: HttpMessageNotReadableException): ErrorDTO {
        val cause = ex.cause
        if (cause is MissingKotlinParameterException) {
            val mappedFieldErrors = cause.path.map { FieldErrorDTO(it.fieldName, "Parameter is missing") }
            return ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Incorrect message parameters", mappedFieldErrors)
        } else {
            return ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Message Not readable", emptyList())
        }
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InputDataResourceNotFoundException::class)
    fun inputDataResourceNotFoundException(ex: InputDataResourceNotFoundException): ErrorDTO {
        return ErrorDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Resource not found: " + ex.resource + " - " + ex.identifier,
                emptyList())
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun emptyResultDataAccessException(ex: EmptyResultDataAccessException) {

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException::class)
    fun entityNotFoundException(ex: EntityNotFoundException) {

    }

    private fun processFieldErrors(fieldErrors: List<FieldError>): ErrorDTO {
        val mappedFieldErrors = fieldErrors.map { FieldErrorDTO(it.field, it.defaultMessage ?: "") }
        return ErrorDTO(HttpStatus.BAD_REQUEST.value(), "validation error", mappedFieldErrors)
    }

    data class ErrorDTO(val status: Int,
                        val message: String,
                        val fieldErrors: List<FieldErrorDTO>) {
    }

    data class FieldErrorDTO(
            val field: String,
            val message: String
    )
}