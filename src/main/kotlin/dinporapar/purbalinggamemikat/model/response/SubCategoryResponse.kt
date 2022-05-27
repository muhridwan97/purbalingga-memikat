package dinporapar.purbalinggamemikat.model.response

import java.util.*

data class SubCategoryResponse (
    val id: Long,

    val name: String,

    val categoryName: String,

    val photo: String,

    val slug: String,

    val description: String?,

    val createdAt: Date,

    val createdBy: Int,

    val updatedAt: Date?,

    val updatedBy: Int?,
        )