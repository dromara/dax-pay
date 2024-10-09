package org.dromara.daxpay.unisdk.common.util.sign.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.unisdk.common.bean.result.PayException;
import org.dromara.daxpay.unisdk.common.exception.PayErrorException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 *
 * HmacSHA256
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
@Slf4j
public class HmacSha256 {

    /**
     * 签名
     *
     * @param content           需要签名的内容
     * @param key               密钥
     * @param characterEncoding 字符编码
     *
     * @return 签名值
     */
    public static String createSign(String content, String key, String characterEncoding) {
        Mac sha256HMAC;
        try {
            sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(characterEncoding), "HmacSHA256");
            sha256HMAC.init(secretKey);
            byte[] array = sha256HMAC.doFinal(content.getBytes(characterEncoding));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString().toUpperCase();
        }
        catch (UnsupportedEncodingException | GeneralSecurityException e) {
            log.error("", e);
        }

        throw new PayErrorException(new PayException("fail", "HMACSHA256 签名异常"));
    }
}
