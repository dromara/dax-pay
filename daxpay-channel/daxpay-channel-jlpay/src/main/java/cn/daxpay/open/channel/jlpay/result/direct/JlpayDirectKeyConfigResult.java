package cn.daxpay.open.channel.jlpay.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 嘉联直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class JlpayDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 嘉联开放平台应用 appid
    private String appId;

    /// 嘉联商户号(mch_id)
    private String mchId;

    /// 嘉联终端号(term_no)
    private String termNo;

    /// 商户 SM2 私钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String merchantPrivateKey;

    /// 嘉联平台 SM2 公钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6, compact = true)
    private String platformPublicKey;

    /// 商户 SM2 私钥是否已配置
    private boolean merchantPrivateKeyConfigured;

    /// 平台 SM2 公钥是否已配置
    private boolean platformPublicKeyConfigured;
}
