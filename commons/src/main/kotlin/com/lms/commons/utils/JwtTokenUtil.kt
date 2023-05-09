package com.lms.commons.utils

import com.lms.commons.constants.ApplicationExceptionTypes
import com.lms.commons.constants.Constant.SESSION_ID
import com.lms.commons.constants.Constant.TOKEN_EXPIRY_TIME
import com.lms.commons.models.ApplicationException
import com.lms.commons.models.UserSession
import org.springframework.beans.factory.annotation.Value
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

/**
 * @author Shashwat Singh
 * JWT token management util, it provides features to generate token, fetch custom payload from token
 * validate token and check token expiration.
 */

@Component
class JwtTokenUtil {

    @Value("\${jwt-secret}")
    private lateinit var secret: String

    private var log = LoggerFactory.getLogger(JwtTokenUtil::class.java)

    fun generateToken(session: UserSession): String {
        val key: Key = Keys.hmacShaKeyFor(secret.toByteArray())
        val currentTimeInMillis = System.currentTimeMillis()
        val expirationTimeInMillis = currentTimeInMillis + TOKEN_EXPIRY_TIME
        return Jwts.builder()
            .setSubject(session.toJson())
            .claim(SESSION_ID, session.id)
            .setIssuedAt(Date(currentTimeInMillis))
            .setExpiration(Date(expirationTimeInMillis))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    private fun fetchCustomPayload(token: String): String {
        try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(secret.toByteArray())
                .build()
                .parseClaimsJws(token)
                .body
            return claims[SESSION_ID] as String
        } catch (e: JwtException) {
            log.info("fetchCustomPayload - sessionId not able to fetch from the given token <$token>")
            throw ApplicationException(ApplicationExceptionTypes.UNAUTHORIZED)
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(secret.toByteArray())
                .build()
                .parseClaimsJws(token)
                .body
            val expirationDate = claims.expiration
            return expirationDate.before(Date())
        } catch (e: MalformedJwtException) {
            log.info("isTokenExpired - Given invalid token <$token>.")
            throw ApplicationException(ApplicationExceptionTypes.INVALID_JWT_TOKEN)
        }  catch (e: ExpiredJwtException) {
            log.info("isTokenExpired - Given token already expired <$token>")
            throw ApplicationException(ApplicationExceptionTypes.JWT_TOKEN_EXPIRED)
        }
    }

    fun validateTokenFetchSessionId(token: String): String {
        if (isTokenExpired(token)) {
            log.info("validateTokenFetchSessionId - Given token <$token> were expired.")
            throw ApplicationException(ApplicationExceptionTypes.UNAUTHORIZED)
        }
        return fetchCustomPayload(token)
    }
}