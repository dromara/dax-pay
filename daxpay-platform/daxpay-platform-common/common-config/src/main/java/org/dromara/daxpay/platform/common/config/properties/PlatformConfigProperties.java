package org.dromara.daxpay.platform.common.config.properties;

import org.dromara.daxpay.platform.core.util.RsaSignUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/// # 系统平台配置
///
@Data
@Accessors(chain = true)
@Validated
@ConfigurationProperties(prefix = "daxpay.platform.config")
public class PlatformConfigProperties {
    /// 平台公私钥配置
    @Valid
    private KeyConfig keyConfig = new KeyConfig();

    /// 数据加密配置
    @Valid
    private Encrypt encrypt = new Encrypt();

    /// # 平台公私钥配置
    ///
    @Slf4j
    @Data
    @Accessors(chain = true)
    public static class KeyConfig {
        @NotBlank(message = "{validation.field.publicKey.notBlank}")
        private String publicKey;
        
        @NotBlank(message = "{validation.field.privateKey.notBlank}")
        private String privateKey;

        public KeyConfig setPublicKey(String publicKey) {
            if (StrUtil.isNotBlank(publicKey)) {
                RsaSignUtil.loadPublicKeyFromPem(publicKey);
                log.info("加载平台公钥成功");
            }
            this.publicKey = publicKey;
            return this;
        }

        public KeyConfig setPrivateKey(String privateKey) {
            if (StrUtil.isNotBlank(privateKey)) {
                RsaSignUtil.loadPrivateKeyFromPem(privateKey);
                log.info("加载平台私钥成功");
            }
            this.privateKey = privateKey;
            return this;
        }
    }

    /// # 数据加密配置
    ///
    @Data
    @Accessors(chain = true)
    public static class Encrypt {
        /// 是否开启加密
        private boolean enable = false;
        /// 密钥列表，第一个为当前使用的密钥（用于加密），后续为历史密钥（用于解密）
        private List<EncryptKeyInfo> keys = new ArrayList<>();
    }
}

