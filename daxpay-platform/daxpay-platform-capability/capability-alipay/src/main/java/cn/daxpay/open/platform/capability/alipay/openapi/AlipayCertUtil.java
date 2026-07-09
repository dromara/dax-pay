package cn.daxpay.open.platform.capability.alipay.openapi;

import lombok.experimental.UtilityClass;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

/// # 支付宝证书工具
///
/// 对齐官方 SDK `AntCertificationUtil`:
/// - app_cert_sn: MD5(issuerDN + serialNumber) 补齐 32 位十六进制
/// - alipay_root_cert_sn: 根证书链中 RSA 签名算法(OID 1.2.840.113549.1.1.*) 证书 SN, 多值 `_` 连接
/// - 从支付宝公钥证书提取 Base64 编码的 X509 公钥
///
@UtilityClass
public class AlipayCertUtil {

    /// RSA 签名算法 OID 前缀
    private static final String RSA_SIG_ALG_OID_PREFIX = "1.2.840.113549.1.1";

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /// 计算应用公钥证书 SN(`app_cert_sn`)
    public String getAppCertSn(String appCertContent) {
        X509Certificate cert = parseCertificate(appCertContent);
        return getCertSn(cert);
    }

    /// 计算支付宝根证书 SN(`alipay_root_cert_sn`)
    public String getRootCertSn(String rootCertContent) {
        X509Certificate[] certs = parseCertificateChain(rootCertContent);
        StringBuilder rootCertSn = null;
        for (X509Certificate cert : certs) {
            if (cert.getSigAlgOID() != null && cert.getSigAlgOID().startsWith(RSA_SIG_ALG_OID_PREFIX)) {
                String certSn = getCertSn(cert);
                if (rootCertSn == null || (rootCertSn.isEmpty())) {
                    rootCertSn = certSn == null ? null : new StringBuilder(certSn);
                } else {
                    rootCertSn.append("_").append(certSn);
                }
            }
        }
        return rootCertSn == null ? null : rootCertSn.toString();
    }

    /// 从支付宝公钥证书提取 Base64 公钥(X509 编码)
    public String getAlipayPublicKeyFromCert(String alipayCertContent) {
        X509Certificate cert = parseCertificate(alipayCertContent);
        PublicKey publicKey = cert.getPublicKey();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /// 计算单本证书 SN
    public String getCertSn(X509Certificate cert) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 与官方 SDK 一致: 使用平台默认字符集对 issuerDN+serial 做 digest
            md.update((cert.getIssuerX500Principal().getName() + cert.getSerialNumber()).getBytes());
            return fillMd5(new BigInteger(1, md.digest()).toString(16));
        } catch (Exception e) {
            throw new IllegalStateException("alipay cert sn failed: " + e.getMessage(), e);
        }
    }

    /// 解析单本证书
    public X509Certificate parseCertificate(String certContent) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
            try (ByteArrayInputStream input = new ByteArrayInputStream(certContent.getBytes(StandardCharsets.UTF_8))) {
                return (X509Certificate) factory.generateCertificate(input);
            }
        } catch (Exception e) {
            throw new IllegalStateException("alipay cert parse failed: " + e.getMessage(), e);
        }
    }

    /// 解析证书链(根证书可能含多本)
    public X509Certificate[] parseCertificateChain(String certContent) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
            try (ByteArrayInputStream input = new ByteArrayInputStream(certContent.getBytes(StandardCharsets.UTF_8))) {
                Collection<? extends Certificate> certificates = factory.generateCertificates(input);
                List<X509Certificate> list = new ArrayList<>(certificates.size());
                for (Certificate certificate : certificates) {
                    list.add((X509Certificate) certificate);
                }
                return list.toArray(new X509Certificate[0]);
            }
        } catch (Exception e) {
            throw new IllegalStateException("alipay cert chain parse failed: " + e.getMessage(), e);
        }
    }

    private String fillMd5(String md5) {
        return md5.length() == 32 ? md5 : fillMd5("0" + md5);
    }
}
