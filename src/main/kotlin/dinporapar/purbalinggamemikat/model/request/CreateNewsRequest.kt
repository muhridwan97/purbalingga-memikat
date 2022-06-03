package dinporapar.purbalinggamemikat.model.request;

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateNewsRequest (

        @field:NotBlank
        var title: String?,

        @field:NotBlank
        var body : String?,

        @field:NotBlank
        var slug : String?,

        @field:NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        var date : Date?,

        @field:NotNull
        var time: String?,

        @field:NotBlank
        var writer : String?,

        @SuppressWarnings("java:S1948")
        var file: MultipartFile? = null,

        var link : String?="",

        var isActive: Boolean? = true,

        var description : String?="",

        @field:NotNull
        @field:Min(value = 0)
        var createdBy: Int,
)
