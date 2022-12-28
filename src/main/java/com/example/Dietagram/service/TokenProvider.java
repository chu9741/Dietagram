package com.example.Dietagram.service;

import com.example.Dietagram.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenProvider {


//    private Map<String, Object> createJWT(User user){
//        Map<String, Object> headers = new HashMap<>();
//        headers.put("typ", "JWT");
//        headers.put("alg", "HS512");
//
//        Map<String, Object> payloads = new HashMap<>();
//        payloads.put("sub", Long.toString(user.getId()));
//        payloads.put("iss", )
//    }
//    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    private static final String SECRET_KEY = "THISKEYISJWTACCESSTOKENFORDIETAGRAMTHISKEYISJWTACCESSTOKENFORDIETAGRAMTHISKEYISJWTACCESSTOKENFORDIETAGRAMTHISKEYISJWTACCESSTOKENFORDIETAGRAM";
//    private byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        this.key = Keys.hmacShaKeyFor(keyBytes);

    public String create(User user) throws UnsupportedEncodingException {
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        return Jwts.builder().setSubject(Long.toString(user.getId()))
                .setIssuer("Dietagram")
                .setHeaderParam("typ","JWT")
                .setId(Long.toString(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
//                .claim("nbf", new Date())
                .setId(Long.toString(user.getId()))
                .claim("type", "access")
                .claim("foo", user.getFollowingList())
//                .claim("fresh",false)
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }

    public String validateAndGetUserId(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();
//        Claims claim = Jwts.parserBuilder().setSigningKey(key).build()
//                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }



}