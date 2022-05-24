package dinporapar.purbalinggamemikat.model.request

data class RequestParams(

    val page: Int? = 0,

    val size: Int? = 10,

    val sortBy: String? = "created_at:ASC"

)