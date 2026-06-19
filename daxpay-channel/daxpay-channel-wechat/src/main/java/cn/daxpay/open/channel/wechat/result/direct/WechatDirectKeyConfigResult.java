package cn.daxpay.open.channel.wechat.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信直连密钥配置
///
/// 微信直连密钥和证书的返回结果对象，含商户号、通道商户号信息，敏感字段脱敏展示。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信直连密钥配置")
public class WechatDirectKeyConfigResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "APIv3密钥")
    private String apiKeyV3;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "支付公钥")
    private String publicKey;

    @Schema(description = "支付公钥ID")
    private String publicKeyId;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "商户私钥")
    private String privateKey;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "商户证书")
    private String privateCert;

    @Schema(description = "证书序列号")
    private String certSerialNo;
}
