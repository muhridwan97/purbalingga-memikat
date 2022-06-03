package dinporapar.purbalinggamemikat.service.impl

import dinporapar.purbalinggamemikat.model.response.UserInfoResponse
import dinporapar.purbalinggamemikat.service.KeycloakAuthService
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class KeycloakAuthServiceImpl : KeycloakAuthService {
    override fun getPreferredUsername(principal: Principal): String? {
        val keycloakAuthenticationToken = principal as KeycloakAuthenticationToken
        val accessToken = keycloakAuthenticationToken.account.keycloakSecurityContext.token
        return accessToken.preferredUsername
    }

    override fun getUserInfo(principal: Principal): UserInfoResponse {
        val keycloakAuthenticationToken = principal as KeycloakAuthenticationToken
        val accessToken = keycloakAuthenticationToken.account.keycloakSecurityContext.token

        var name: String? = null
        var unit: LinkedHashMap<*, *>? = null
        var subunit: LinkedHashMap<*, *>? = null

        val officer: LinkedHashMap<*, *>?  = accessToken.otherClaims["officer"] as LinkedHashMap<*, *>?
        if (officer !== null) {
            name = officer.get("nama").toString()
            unit = officer.get("unit") as LinkedHashMap<*, *>
            subunit = officer.get("subunit") as LinkedHashMap<*, *>
        }

        val unitId: String? = if (unit !== null) {
            unit.get("id").toString()
        } else {
            null
        }

        val subUnitId: String? = if (subunit !== null) {
            subunit.get("id").toString()
        } else {
            null
        }

        return UserInfoResponse(
            username = accessToken.preferredUsername,
            name = name,
            unitId = unitId,
            subUnitId = subUnitId
        )
    }
}