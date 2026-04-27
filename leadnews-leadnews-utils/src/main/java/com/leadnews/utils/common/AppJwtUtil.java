package com.leadnews.utils.common;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public class AppJwtUtil {

    // TOKEN鐨勬湁鏁堟湡涓€澶╋紙S锛?
    private static final int TOKEN_TIME_OUT = 3_600;
    // 鍔犲瘑KEY
    private static final String TOKEN_ENCRY_KEY = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY";
    // 鏈€灏忓埛鏂伴棿闅?S)
    private static final int REFRESH_TIME = 300;

    // 鐢熶骇ID
    public static String getToken(Long id){
        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put("id",id);
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(currentTime))  //绛惧彂鏃堕棿
                .setSubject("system")  //璇存槑
                .setIssuer("leadnews") //绛惧彂鑰呬俊鎭?
                .setAudience("app")  //鎺ユ敹鐢ㄦ埛
                .compressWith(CompressionCodecs.GZIP)  //鏁版嵁鍘嬬缉鏂瑰紡
                .signWith(SignatureAlgorithm.HS512, generalKey()) //鍔犲瘑鏂瑰紡
                .setExpiration(new Date(currentTime + TOKEN_TIME_OUT * 1000))  //杩囨湡鏃堕棿鎴?
                .addClaims(claimMaps) //cla淇℃伅
                .compact();
    }

    /**
     * 鑾峰彇token涓殑claims淇℃伅
     *
     * @param token
     * @return
     */
    private static Jws<Claims> getJws(String token) {
            return Jwts.parser()
                    .setSigningKey(generalKey())
                    .parseClaimsJws(token);
    }

    /**
     * 鑾峰彇payload body淇℃伅
     *
     * @param token
     * @return
     */
    public static Claims getClaimsBody(String token) {
        try {
            return getJws(token).getBody();
        }catch (ExpiredJwtException e){
            return null;
        }
    }

    /**
     * 鑾峰彇hearder body淇℃伅
     *
     * @param token
     * @return
     */
    public static JwsHeader getHeaderBody(String token) {
        return getJws(token).getHeader();
    }

    /**
     * 鏄惁杩囨湡
     *
     * @param claims
     * @return -1锛氭湁鏁堬紝0锛氭湁鏁堬紝1锛氳繃鏈燂紝2锛氳繃鏈?
     */
    public static int verifyToken(Claims claims) {
        if(claims==null){
            return 1;
        }
        try {
            claims.getExpiration()
                    .before(new Date());
            // 闇€瑕佽嚜鍔ㄥ埛鏂癟OKEN
            if((claims.getExpiration().getTime()-System.currentTimeMillis())>REFRESH_TIME*1000){
                return -1;
            }else {
                return 0;
            }
        } catch (ExpiredJwtException ex) {
            return 1;
        }catch (Exception e){
            return 2;
        }
    }

    /**
     * 鐢卞瓧绗︿覆鐢熸垚鍔犲瘑key
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(TOKEN_ENCRY_KEY.getBytes());
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    public static void main(String[] args) {
       /* Map map = new HashMap();
        map.put("id","11");*/
        System.out.println(AppJwtUtil.getToken(1102L));
        Jws<Claims> jws = AppJwtUtil.getJws("eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAC2L0QqDMAwA_yXPFkw6a-LfxDawDoRCK2yM_bsR9nbHcV94jQobRGHKO-UwG1F4mEkQ1hJy4ZjKWjRyggmqDtgwIQuvssgE_dz97p8-7Lh7765Pq4e66VnctDVne7f_KbjcZ_WGONPvAsM25luDAAAA._HLSpxHpSl4KZbYtSx1xnyeaRpsJTQ5xz6wMfFehqUr5etW6pOhCuP4EdrhSBefJZ5evmfYcUAj_dbHkLVdxSQ");
        Claims claims = jws.getBody();
        int i = AppJwtUtil.verifyToken(claims);
        System.out.println(i);
        System.out.println(claims.get("id"));
        /*Date date = new Date(20000000000000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        System.out.println(format);*/

    }

}

