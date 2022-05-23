package dinporapar.purbalinggamemikat.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dinporapar.purbalinggamemikat.model.request.CreateCarouselRequest
import dinporapar.purbalinggamemikat.model.response.WebResponse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
internal class CarouselControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
){
    
    val baseUrl = "/api/carousel"
    
    @Nested
    @DisplayName("POST /api/carousel")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class AddCarousel {
    
        @Test
        fun `should add a new carousel` () {
            // given
            val newCarousel = CreateCarouselRequest(
                "/test/test",
                "www.facebook.com",
                "ini adalah test",
                1,
                1
            )

            // when
            val performPost = mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newCarousel)
            }



            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(
                            WebResponse(
                                code = 200,
                                status = "OK",
                                data = newCarousel
                            ) ))
                    }
                }

        }
    }
}