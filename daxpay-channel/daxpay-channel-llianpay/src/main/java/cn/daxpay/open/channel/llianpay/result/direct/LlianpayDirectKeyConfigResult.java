package cn.daxpay.open.channel.llianpay.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 连连支付直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class LlianpayDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 连连商户编号(mch_id)
    private String mchId;

    /// 二级商户号(平台商模式)
    private String subMchid;

    /// 收款方用户ID
    private String payeeUid;

    /// 业务属性(busi_type)
    private String busiType;

    /// 商户私钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantPrivateKey;

    /// 连连平台公钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String llianpayPublicKey;

    /// 商户私钥是否已配置
    private boolean merchantPrivateKeyConfigured;

    /// 连连平台公钥是否已配置
    private boolean llianpayPublicKeyConfigured;
}
