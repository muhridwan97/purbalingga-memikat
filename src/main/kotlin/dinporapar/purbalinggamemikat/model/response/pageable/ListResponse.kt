package dinporapar.purbalinggamemikat.model.response.pageable

import dinporapar.purbalinggamemikat.model.response.pageable.PagingResponse
import dinporapar.purbalinggamemikat.model.response.pageable.SortingResponse

data class ListResponse<T>(

    val items: List<T>,

    val paging: PagingResponse? = null,

    val sorting: SortingResponse? = null
)