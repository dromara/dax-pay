package cn.daxpay.open.channel.longpay.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 建行龙支付直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class LongpayDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 建行商户号(MERCHANTID)
    private String merchantId;

    /// 建行柜台号(POSID)
    private String posId;

    /// 建行分行号(BRANCHID)
    private String branchId;

    /// 柜台公钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantPublicKey;

    /// 建行公钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String ccbPublicKey;

    /// 柜台公钥是否已配置
    private boolean merchantPublicKeyConfigured;

    /// 建行公钥是否已配置
    private boolean ccbPublicKeyConfigured;
}
