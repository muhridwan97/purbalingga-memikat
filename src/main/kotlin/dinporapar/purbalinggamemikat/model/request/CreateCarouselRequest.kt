package dinporapar.purbalinggamemikat.model.request

import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateCarouselRequest(

//    @field:NotBlank
//    val photo: String?,

    val link: String? = "",

    var description: String? = "",

    var isActive: Boolean?= true,

    @field:NotNull
    @field:Min(value = 0)
    var createdBy: Int,

    @SuppressWarnings("java:S1948")
    var file: MultipartFile? = null
        )