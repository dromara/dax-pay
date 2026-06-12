package org.dromara.daxpay.platform.capability.nonce.result;

import lombok.Data;
import lombok.experimental.Accessors;

/// # Nonce生成结果
///
@Data
@Accessors(chain = true)
public class NonceResult {

    /// nonce值
    private String nonce;

    /// 服务器时间戳（毫秒）
    private long timestamp;

}
