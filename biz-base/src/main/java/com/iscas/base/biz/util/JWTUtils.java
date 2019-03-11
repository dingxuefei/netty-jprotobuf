package com.iscas.base.biz.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.iscas.base.biz.exception.ValidTokenException;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/16 22:29
 * @since jdk1.8
 */
public class JWTUtils {
    private JWTUtils(){}
    public static final String SECRET = "ISCAS";
    public static String createToken(String username, int expire) throws UnsupportedEncodingException {
        Date iatDate = new Date();
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE,expire);
        Date expiresDate = nowTime.getTime();
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ","JWT");
        String token = JWT.create()
                .withHeader(map)
                .withClaim("username", username)
                .withClaim("date", iatDate)
                .withExpiresAt(expiresDate)
                .withIssuedAt(iatDate)
                .sign(Algorithm.HMAC256(SECRET));

        //将token缓存起来
        CaffCacheUtils.set(token, iatDate);
        return token;
    }
    public static Map<String, Claim> verifyToken(String token) throws UnsupportedEncodingException, ValidTokenException {
        Object obj = CaffCacheUtils.get(token);
        if(obj == null){
            throw new ValidTokenException("登录凭证校验失败","token:" + token + "不存在或已经被注销");
        }
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = jwtVerifier.verify(token);
        }catch (Exception e){
            throw new ValidTokenException("登录凭证校验失败","token:" + token + "校验失败");
        }
        return decodedJWT.getClaims();
    }

}
