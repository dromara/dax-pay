package cn.daxpay.open.channel.yisheng.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易生直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class YishengDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 合作伙伴ID(reqId)
    private String partner;

    /// 易生商户编号(merchant_id)
    private String merchantId;

    /// 商户 RSA 私钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantPrivateKey;

    /// 易生平台 RSA 公钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String platformPublicKey;

    /// 商户私钥是否已配置
    private boolean merchantPrivateKeyConfigured;

    /// 平台公钥是否已配置
    private boolean platformPublicKeyConfigured;
}
