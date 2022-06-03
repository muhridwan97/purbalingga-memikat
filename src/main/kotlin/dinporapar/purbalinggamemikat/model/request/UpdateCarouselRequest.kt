package dinporapar.purbalinggamemikat.model.request

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotNull

data class UpdateCarouselRequest (
    val link: String?,

    var description: String?,

    var isActive: Boolean?= true,

    @SuppressWarnings("java:S1948")
    var file: MultipartFile? = null
)
