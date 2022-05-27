package dinporapar.purbalinggamemikat.controller

import dinporapar.purbalinggamemikat.service.IpAddressService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

@RestController
class IpAddressController(val ipAddressService: IpAddressService) {


    @RequestMapping("/")
    fun index(request: HttpServletRequest): ModelAndView {
        val model = ModelAndView("index")
        val clientIp: String = ipAddressService.getClientIp(request)
        model.addObject("clientIp", clientIp)
        return model
    }
}