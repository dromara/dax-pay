package cn.daxpay.open.channel.helipay.param;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 合利宝通道密钥配置保存参数
///
/// 以 channelMchNo(通道商户号) 作为唯一标识定位记录,
/// mchNo(平台商户号) 为不可变身份字段, 创建时写入后永不可改, 不参与保存;
/// 签名密钥为空时保留原值(不覆盖)。
@Data
@Accessors(chain = true)
@Schema(title = "合利宝通道密钥配置保存参数")
public class HelipayKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "合利宝商户编号(customerNumber)")
    private String customerNumber;

    @Schema(description = "子商户号(可空, 设置后以子商户号发起交易)")
    private String subCustomerNumber;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "扫码产品签名密钥(MD5 签名与回调验签共用)")
    private String signKey;

    @Schema(description = "公众号/生活号支付 AppId(微信 JS 支付必填)")
    private String publicAppId;

    @Schema(description = "H5 支付 AppId(可空)")
    private String h5AppId;

    @Schema(description = "扫码支付报备ID(可空)")
    private String reportId;
}
