package com.iconectiv.irsf.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.impl.crypto.MacProvider

import java.security.Key

/**
 * Created by echang on 3/23/2017.
 */
class JWTUtil {
	private static final String secretKey = "iconectiv.irsf.jwt";
    public static String createToken(String subject, String audience, Map<String, Object> claims = null, Date expirationTime = null) {
        JwtBuilder builder = Jwts.builder().setSubject(subject).setAudience(audience).setIssuer("iconectiv").setIssuedAt(new Date())

        if (claims) {
            builder.setClaims(claims)
        }
        if (expirationTime) {
            builder.setExpiration(expirationTime)
        }
        return builder.signWith(SignatureAlgorithm.HS256, secretKey).compact()
    }

    def static parseToken(String jwt) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
        return jws
    }
}


