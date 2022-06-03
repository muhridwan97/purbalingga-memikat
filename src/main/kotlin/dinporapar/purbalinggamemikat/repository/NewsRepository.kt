package dinporapar.purbalinggamemikat.repository

import dinporapar.purbalinggamemikat.entity.NewsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface NewsRepository: JpaRepository<NewsEntity, Long>, JpaSpecificationExecutor<NewsEntity> {

}