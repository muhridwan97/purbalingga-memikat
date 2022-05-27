package dinporapar.purbalinggamemikat.model.request

data class DeleteCategoryRequest (
    var softDelete : Boolean? = true,

    var deletedBy : Int? = 1
)