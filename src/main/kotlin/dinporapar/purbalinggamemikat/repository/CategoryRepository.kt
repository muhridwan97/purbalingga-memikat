package dinporapar.purbalinggamemikat.repository

import dinporapar.purbalinggamemikat.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface CategoryRepository: JpaRepository<CategoryEntity, Long>, JpaSpecificationExecutor<CategoryEntity> {

    @Query(value = "SELECT max(the_order) FROM ref_categories",nativeQuery=true)
    fun getLastOrder(): Int?
}