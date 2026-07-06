package cn.daxpay.open.channel.ums.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 银联商务直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class UmsDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 银联商务商户号(mid)
    private String merchantNo;

    /// 终端号(tid)
    private String terminalNo;

    /// 银联商务应用 AppId
    private String umsAppId;

    /// 应用密钥(脱敏返回, 保留前后各4位)
    @SensitiveInfo(front = 4, end = 4)
    private String appKey;

    /// 通讯密钥(脱敏返回, 保留前后各4位)
    @SensitiveInfo(front = 4, end = 4)
    private String secretKey;

    /// 应用密钥是否已配置
    private boolean appKeyConfigured;

    /// 通讯密钥是否已配置
    private boolean secretKeyConfigured;
}
