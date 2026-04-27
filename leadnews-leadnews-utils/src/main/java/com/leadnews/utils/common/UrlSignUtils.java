package com.leadnews.utils.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.SortedMap;

public enum UrlSignUtils {

    getUrlSignUtils;
    private static final Logger logger = LoggerFactory.getLogger(UrlSignUtils.class);

    /**
     * @param params 鎵€鏈夌殑璇锋眰鍙傛暟閮戒細鍦ㄨ繖閲岃繘琛屾帓搴忓姞瀵?
     * @return 寰楀埌绛惧悕
     */
    public String getSign(SortedMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : params.entrySet()) {
            if (!entry.getKey().equals("sign")) { //鎷艰鍙傛暟,鎺掗櫎sign
                if (!StringUtils.isEmpty(entry.getKey()) && !StringUtils.isEmpty(entry.getValue()))
                    sb.append(entry.getKey()).append('=').append(entry.getValue());
            }
        }
        logger.info("Before Sign : {}", sb.toString());
        return DigestUtils.md5Hex(sb.toString()).toUpperCase();
    }

    /**
     * @param params 鎵€鏈夌殑璇锋眰鍙傛暟閮戒細鍦ㄨ繖閲岃繘琛屾帓搴忓姞瀵?
     * @return 楠岃瘉绛惧悕缁撴灉
     */
    public boolean verifySign(SortedMap<String, String> params) {
        if (params == null || StringUtils.isEmpty(params.get("sign"))) return false;
        String sign = getSign(params);
        logger.info("verify Sign : {}", sign);
        return !StringUtils.isEmpty(sign) && params.get("sign").equals(sign);
    }

}
