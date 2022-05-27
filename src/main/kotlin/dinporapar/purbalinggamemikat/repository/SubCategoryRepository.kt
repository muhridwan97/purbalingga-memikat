package dinporapar.purbalinggamemikat.repository

import dinporapar.purbalinggamemikat.entity.SubCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface SubCategoryRepository : JpaRepository<SubCategoryEntity, Long>, JpaSpecificationExecutor<SubCategoryEntity> {
}