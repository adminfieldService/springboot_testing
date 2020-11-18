/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author newbiecihuy
 */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    static final String JWT_SECRET = "lawfirmSecretKey";//lawfirmSecretKey
//    static final Integer JWT_ExpirationMs = 86400000;//1440 minutes --> 24 H
    static final Integer JWT_ExpirationMs = 3600000;//60 minutes --> 1 H

    public String generateJwtToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((new Date()).getTime() + JWT_ExpirationMs))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

//    public static boolean checkRole(Collection<? extends GrantedAuthority> authorities, ERole userRole) {
//
//        for (GrantedAuthority authority : authorities) {
//            if (authority.getAuthority().contains(ERole.ROLE_SysAdmin.toString())) {
//                return true;
//            }
//            if (authority.getAuthority().contains(ERole.ROLE_ADMIN.toString())) {
//                return true;
//            }
//            if (authority.getAuthority().contains(ERole.ROLE_DMP.toString())) {
//                return true;
//            }
//            if (authority.getAuthority().contains(ERole.ROLE_FINANCE.toString())) {
//                return true;
//            }
//            if (authority.getAuthority().contains(ERole.ROLE_LAWYER.toString())) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//    public static String getSubject(HttpServletRequest httpServletRequest, String jwtTokenCookieName, String signingKey) {
//        String token = CookieUtil.getValue(httpServletRequest, jwtTokenCookieName);
//        if (token == null) {
//            return null;
//        }
//        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody().getSubject();
//    }
//    public static void invalidateRelatedTokens(HttpServletRequest httpServletRequest) {
//        RedisUtil.INSTANCE.srem(REDIS_SET_ACTIVE_SUBJECTS, (String) httpServletRequest.getAttribute("username"));
//    }
}
