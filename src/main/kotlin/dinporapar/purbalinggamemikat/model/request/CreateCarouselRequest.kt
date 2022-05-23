package dinporapar.purbalinggamemikat.model.request

import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateCarouselRequest(

//    @field:NotBlank
//    val photo: String?,

    val link: String?,

    var description: String?,

    var isActive: Int?,

    @field:NotNull
    var createdBy: Int,

    @SuppressWarnings("java:S1948")
    var file: MultipartFile? = null
        )