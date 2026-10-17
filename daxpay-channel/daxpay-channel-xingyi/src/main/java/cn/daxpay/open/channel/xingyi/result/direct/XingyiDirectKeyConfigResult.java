package cn.daxpay.open.channel.xingyi.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 星驿付直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class XingyiDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 机构号(agetId)
    private String agetId;

    /// 星驿付商户号(custId)
    private String custId;

    /// 商户RSA公钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24, compact = true)
    private String merchantPublicKey;

    /// 商户公钥是否已配置
    private boolean merchantPublicKeyConfigured;
}
