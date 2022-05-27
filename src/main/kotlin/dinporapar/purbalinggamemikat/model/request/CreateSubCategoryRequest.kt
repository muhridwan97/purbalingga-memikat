package dinporapar.purbalinggamemikat.model.request

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateSubCategoryRequest (

    @field:NotNull
    var categoryId: Long?,

    @field:NotBlank
    var name: String?,

    @field:NotBlank
    var slug : String?,

    var description: String? = "",

    @field:NotNull
    @field:Min(value = 0)
    var createdBy: Int?,

    @SuppressWarnings("java:S1948")
    var file: MultipartFile? = null
        )