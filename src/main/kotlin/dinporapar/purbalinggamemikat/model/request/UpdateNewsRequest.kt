package dinporapar.purbalinggamemikat.model.request

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.multipart.MultipartFile
import java.util.*

data class UpdateNewsRequest (
    val title : String?,

    val body : String?,

    var slug : String?,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var date : Date?,

    var time: String?,

    var writer : String?,

    @SuppressWarnings("java:S1948")
    var file: MultipartFile? = null,

    var link : String?="",

    var description : String?="",

    val updatedBy: Int? = 1,
        )