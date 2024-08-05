package cn.bootx.platform.common.core.util;

import cn.hutool.core.codec.Base64Encoder;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * 证书工具类
 *
 * @author xxm
 * @since 2022/2/24
 */
@UtilityClass
public class CertUtil {

    /**
     * 根据证书文本获取证书
     */
    @SneakyThrows
    public String getCertByContent(String certContent) {
        CertificateFactory cf = CertificateFactory. getInstance("X.509");
        Certificate cert = cf.generateCertificate(new ByteArrayInputStream(certContent.getBytes(StandardCharsets.UTF_8)));
        PublicKey publicKey = cert.getPublicKey();
        return Base64Encoder.encode(publicKey.getEncoded());
    }

}
