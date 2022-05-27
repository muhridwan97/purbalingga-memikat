package dinporapar.purbalinggamemikat.service

import javax.servlet.http.HttpServletRequest

interface IpAddressService {

    fun getClientIp(request: HttpServletRequest) : String
}