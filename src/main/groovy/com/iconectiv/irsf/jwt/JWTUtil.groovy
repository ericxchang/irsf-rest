package com.iconectiv.irsf.jwt

import com.iconectiv.irsf.portal.core.AppConstants
import com.iconectiv.irsf.portal.model.common.UserDefinition
import com.iconectiv.irsf.util.JsonHelper
import io.jsonwebtoken.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
/**
 * Created by echang on 3/23/2017.
 */
class JWTUtil {
	private static Logger log = LoggerFactory.getLogger(JWTUtil.class)
	
     static String createToken(subject, audience, Map claims = null, Date expirationTime = null) {
        JwtBuilder builder = Jwts.builder()

        if (claims) {
            builder.setClaims(claims)
        }
        if (expirationTime) {
            builder.setExpiration(expirationTime)
        }
		builder.setSubject(subject).setAudience(audience).setIssuer("iconectiv").setIssuedAt(new Date())
        return builder.signWith(SignatureAlgorithm.HS256, AppConstants.SecretKey).compact()
    }

    static UserDefinition parseToken(String jwt) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(AppConstants.SecretKey).parseClaimsJws(jwt)
		
		UserDefinition loginUser = new UserDefinition()
		loginUser.userName = jws.getBody().get("userName")
		loginUser.role = jws.getBody().get("role")
		loginUser.customerId = jws.getBody().get("customerId")
		loginUser.customerName = jws.getBody().get("customerName")

        return loginUser
    }

	 static String createToken(UserDefinition loginUser) {
		def userMap = [:]
		userMap['userName'] = loginUser.userName
		userMap['role'] = loginUser.role
        userMap['hasList'] = loginUser.hasList

		if (loginUser.customerId) {
			userMap['customerId'] = loginUser.customerId
		}
		
		
		if (loginUser.customerName) {
			userMap['customerName'] = loginUser.customerName
		}
		
		if (loginUser.schemaName) {
			userMap['schemaName'] = loginUser.schemaName
		}

        if (log.isDebugEnabled()) log.info(JsonHelper.toJson(userMap))
		return createToken(AppConstants.Subject, AppConstants.Audience, userMap)
	}
}

