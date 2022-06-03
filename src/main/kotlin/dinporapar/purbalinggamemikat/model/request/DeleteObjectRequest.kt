package dinporapar.purbalinggamemikat.model.request

data class DeleteObjectRequest (

    var softDelete : Boolean? = true,

    var deletedBy : Int? = 1
)