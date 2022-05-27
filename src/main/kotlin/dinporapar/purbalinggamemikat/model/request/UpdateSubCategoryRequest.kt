package dinporapar.purbalinggamemikat.model.request

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UpdateSubCategoryRequest (
    @field:NotNull
    var categoryId: Long?,

    @field:NotBlank
    var name: String?,

    @field:NotBlank
    var slug : String?,

    var description: String? = "",

    @field:NotNull
    @field:Min(value = 0)
    val updatedBy: Int? = 0,

    @SuppressWarnings("java:S1948")
    var file: MultipartFile? = null
)