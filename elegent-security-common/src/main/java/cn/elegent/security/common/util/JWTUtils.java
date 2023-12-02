package cn.elegent.security.common.util;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class JWTUtils {


    /**
     * 签发JWT
     * param claims 令牌存储对象
     * param key 对称加密秘钥
     * param expire 过期时间默认天
     * return 令牌
     */
    public static String createJWT(String id,  Map<String, Object> claims, String key, int expire, TimeUnit timeUnit){
        SecretKey secretKey = generalKey(key);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = getEndDate(expire, timeUnit, zoneId);
        Date endTime = Date.from(zdt.toInstant());
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setId(id) //id
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())) //签发时间
                //.setHeaderParam("alg", "HS256")  //加密算法
                .addClaims(claims)
                .setExpiration(endTime)  //设置过期时间
                .signWith(SignatureAlgorithm.HS256,secretKey);  //用密钥签名
        //生成JWT
        return  builder.compact();
    }

    /**
     * 验证JWT
     * param token 令牌
     * param secret 密钥
     * return 令牌存储对象
     */
    public static Object verifyJWT(String token, String secret) {
        //签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key =  generalKey(secret);
        try {
            Jwt jwt = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return jwt.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成key
     *
     * param jwtSecret
     * return
     */
    private static SecretKey generalKey(String jwtSecret) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] encodedKey = Base64.getMimeDecoder().decode(jwtSecret);//DatatypeConverter.parseBase64Binary(jwtSecret);//Base64.getDecoder().decode(jwtSecret);
        SecretKey key = new SecretKeySpec(encodedKey, signatureAlgorithm.getJcaName());
        return key;
    }



    public static ZonedDateTime getEndDate(int expire, TimeUnit unit, ZoneId zoneId){
        switch (unit){
            case DAYS:
                return LocalDateTime.now().plusDays(expire).atZone(zoneId);
            case HOURS:
                return LocalDateTime.now().plusHours(expire).atZone(zoneId);
            case MINUTES:
                return LocalDateTime.now().plusMinutes(expire).atZone(zoneId);
            case SECONDS:
                return LocalDateTime.now().plusSeconds(expire).atZone(zoneId);
            default:
                return null;
        }
    }



}
