package dinporapar.purbalinggamemikat.controller.advice

import org.springframework.beans.PropertyAccessException
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.validation.BindingResult
import org.springframework.validation.DefaultBindingErrorProcessor
import org.springframework.validation.FieldError
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder
import java.text.SimpleDateFormat
import java.util.*

@ControllerAdvice
class BindingController {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.initDirectFieldAccess()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        binder.registerCustomEditor(Date::class.java, CustomDateEditor(dateFormat, true))
        binder.bindingErrorProcessor = object : DefaultBindingErrorProcessor() {
            override fun processPropertyAccessException(ex: PropertyAccessException, bindingResult: BindingResult) {
                val propertyName: String? = ex.propertyName
                val value: Any? = ex.value
                bindingResult.addError(
                    FieldError(
                        bindingResult.objectName,
                        propertyName!!,
                        value,
                        true,
                        arrayOf("moderation.field.error"),
                        arrayOf(propertyName, value),
                        "Invalid value for $propertyName($value)"
                    )
                )
            }
        }
    }
}