package dinporapar.purbalinggamemikat.service.impl

import dinporapar.purbalinggamemikat.service.IpAddressService
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.net.InetAddress
import java.net.UnknownHostException
import javax.servlet.http.HttpServletRequest

@Service
class IpAddressServiceImpl : IpAddressService {
    private val LOCALHOST_IPV4 = "127.0.0.1"
    private val LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1"

    override fun getClientIp(request: HttpServletRequest): String {
        var ipAddress = request.getHeader("X-Forwarded-For")
        if (StringUtils.isEmpty(ipAddress) || "unknown".equals(ipAddress, ignoreCase = true)) {
            ipAddress = request.getHeader("Proxy-Client-IP")
        }

        if (StringUtils.isEmpty(ipAddress) || "unknown".equals(ipAddress, ignoreCase = true)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP")
        }

        if (StringUtils.isEmpty(ipAddress) || "unknown".equals(ipAddress, ignoreCase = true)) {
            ipAddress = request.remoteAddr
            if (LOCALHOST_IPV4 == ipAddress || LOCALHOST_IPV6 == ipAddress) {
                try {
                    val inetAddress = InetAddress.getLocalHost()
                    ipAddress = inetAddress.hostAddress
                } catch (e: UnknownHostException) {
                    e.printStackTrace()
                }
            }
        }

        if (!StringUtils.isEmpty(ipAddress) && ipAddress.length > 15 && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","))
        }

        return ipAddress
    }
}