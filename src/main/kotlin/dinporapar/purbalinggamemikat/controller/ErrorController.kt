package dinporapar.purbalinggamemikat.controller

import dinporapar.purbalinggamemikat.error.*
import dinporapar.purbalinggamemikat.model.response.WebResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ErrorController {
    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun validationHandler(constraintViolationException: ConstraintViolationException): WebResponse<String> {
        return WebResponse(
            code = 400,
            status = "BAD REQUEST",
            data = constraintViolationException.message!!
        )
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun notFound(notFoundException: NotFoundException):WebResponse<String>{
        return WebResponse(
            code = 404,
            status = "NOT FOUND",
            data = "Not Found"
        )
    }

    @ExceptionHandler(value = [UnauthorizedException::class])
    fun unauthorized(unauthorizedException: UnauthorizedException):WebResponse<String>{
        return WebResponse(
            code = 401,
            status = "UNAUTHORIZED",
            data = "Please put your X-Api-Key"
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handledBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(value = [DeleteCarouselException::class])
    fun deleteCarouselFile(deleteCarouselException: DeleteCarouselException):WebResponse<String>{
        return WebResponse(
            code = 400,
            status = "BAD REQUEST",
            data = "Delete failed"
        )
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [FilterKeyException::class])
    fun keyFilterError(filterKeyException: FilterKeyException): WebResponse<String> {
        return WebResponse(
            code = 400,
            status = "BAD REQUEST",
            data = "Must use valid key for filter."
        )
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [FilterOperatorExecption::class])
    fun operatorFilterError(filterOperatorExecption: FilterOperatorExecption): WebResponse<String> {
        return WebResponse(
            code = 400,
            status = "BAD REQUEST",
            data = "Must use valid operator for filter."
        )
    }
}