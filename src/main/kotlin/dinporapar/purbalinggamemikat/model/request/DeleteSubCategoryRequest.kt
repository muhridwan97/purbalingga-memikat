package dinporapar.purbalinggamemikat.model.request

data class DeleteSubCategoryRequest (

    var softDelete : Boolean? = true,

    var deletedBy : Int? = 1
        )