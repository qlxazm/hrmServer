package org.hrm.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenProvider {
    /**
     * Token有效时间，单位是秒
     */
    private long duration;
    /**
     * 密钥
     */
    private String secretKey = "QWER";

    /**
     * 用户名对应的key
     */
    private final static String SUB_KEY = "sub";

    /**
     * token创建时间对应的key
     */
    private final static String CREATE_KEY = "created";

    /**
     * 生成Token字符串
     * @param userDetails
     * @return
     */
    public String createToken(UserDetails userDetails){
        long createTime = System.currentTimeMillis();
        long expireTime = createTime + 1000L * duration;

        Map<String, Object> claims = new HashMap<>();
        claims.put(SUB_KEY, userDetails.getUsername());
        claims.put(CREATE_KEY, new Date(createTime));

        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setClaims(claims)
                .setExpiration(new Date(expireTime))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes());
        String token = builder.compact();
        return token;
    }

    /**
     * 从token中获取用户名
     * @param tokenStr
     * @return
     */
    public String getUsernameFromToken(final String tokenStr) {
        final Claims claims = getClaimFromToken(tokenStr);
        String username;
        try {
            username = claims.getSubject();
        }catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 验证token的有效性，是否过期 token中的用户名是否与数据库中的一致
     * @param tokenStr
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String tokenStr, UserDetails userDetails) {
        final String username = getUsernameFromToken(tokenStr);
        return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(tokenStr);
    }

    /**
     * 查看token是否过期
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration != null && expiration.before(new Date(System.currentTimeMillis()));
    }

    /**
     * 从token中获取创建时间
     * @param tokenStr
     * @return
     */
    public Date getCreateDateFromToken(String tokenStr) {
        Date created;
        try{
            final Claims claims = getClaimFromToken(tokenStr);
            created = new Date((Long) claims.get("created"));
        }catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 从token中获取失效时间
     * @param tokenStr
     * @return
     */
    public Date getExpirationDateFromToken(String tokenStr) {
        Date expiration;
        try {
            final Claims claims = getClaimFromToken(tokenStr);
            expiration = claims.getExpiration();
        }catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimFromToken(String tokenStr) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(tokenStr)
                    .getBody();
        }catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
