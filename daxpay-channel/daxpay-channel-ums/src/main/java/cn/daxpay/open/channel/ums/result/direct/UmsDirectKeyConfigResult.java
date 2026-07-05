package cn.daxpay.open.channel.ums.result.direct;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 银联商务直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class UmsDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 银联商务应用 AppId
    private String umsAppId;

    /// 应用密钥(脱敏)
    private String appKey;

    /// 通讯密钥(脱敏)
    private String secretKey;

    /// 应用密钥是否已配置
    private boolean appKeyConfigured;

    /// 通讯密钥是否已配置
    private boolean secretKeyConfigured;
}
