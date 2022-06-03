package dinporapar.purbalinggamemikat.service

import dinporapar.purbalinggamemikat.model.response.UserInfoResponse
import java.security.Principal

interface KeycloakAuthService {

    fun getPreferredUsername(principal: Principal): String?

    fun getUserInfo(principal: Principal): UserInfoResponse

}