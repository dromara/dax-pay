package cn.bootx.platform.starter.dingtalk.util;

import cn.hutool.crypto.digest.HmacAlgorithm;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 钉钉工具类
 *
 * @author xxm
 * @since 2022/4/2
 */
@Slf4j
@UtilityClass
public class DingTalkUtil {

    /**
     * 生成sign
     */
    public String generateSign(Long timestamp, String sec) {
        String combine = String.format("%d\n%s", timestamp, sec);
        try {
            Mac mac = Mac.getInstance(HmacAlgorithm.HmacSHA256.getValue());
            mac.init(new SecretKeySpec(sec.getBytes(StandardCharsets.UTF_8), HmacAlgorithm.HmacSHA256.getValue()));
            byte[] signData = mac.doFinal(combine.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(signData);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

}
