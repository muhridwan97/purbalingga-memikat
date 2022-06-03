package dinporapar.purbalinggamemikat.model.request

data class DeleteNewsRequest (

    var softDelete : Boolean? = true,

    var deletedBy : Int? = 1
        )