package org.dromara.daxpay.platform.common.config.properties;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 加密密钥信息
///
@Data
@Accessors(chain = true)
public class EncryptKeyInfo {

    /// 版本号
    private Integer version;

    /// 密钥（32位AES密钥）
    private String key;
}
