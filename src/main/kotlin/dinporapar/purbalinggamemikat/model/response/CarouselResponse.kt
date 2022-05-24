package dinporapar.purbalinggamemikat.model.response

import java.util.*

data class CarouselResponse(

    val id: Long,

    val photo: String,

    val link: String?,

    val IsActive: Boolean?,

    val description: String?,

    val createdAt: Date,

    val createdBy: Int,

    val updatedAt: Date?,

    val updatedBy: Int?,

    val deletedAt: Date?,

    val deletedBy: Int?,
)