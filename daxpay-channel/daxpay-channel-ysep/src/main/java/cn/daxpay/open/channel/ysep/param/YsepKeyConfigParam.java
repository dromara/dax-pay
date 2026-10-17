package cn.daxpay.open.channel.ysep.param;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 银盛通道密钥配置保存参数
///
/// 以 channelMchNo(通道商户号) 作为唯一标识定位记录,
/// mchNo(平台商户号) 为不可变身份字段, 创建时写入后永不可改, 不参与保存;
/// 证书/密码字段为空时保留原值(不覆盖)。
@Data
@Accessors(chain = true)
@Schema(title = "银盛通道密钥配置保存参数")
public class YsepKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品(ysep_pay 银盛支付 / yseqt_pay 银盛e企通)")
    private String product;

    @Schema(description = "发起方商户号(ysep_pay=partner_id / yseqt_pay=srcMerchantNo)")
    private String sponsorMchNo;

    @Schema(description = "收款商户号(ysep_pay=seller_id / yseqt_pay=payeeMerchantNo)")
    private String payeeMchNo;

    @Schema(description = "业务代码(ysep_pay 专用 business_code; yseqt_pay 不使用)")
    private String businessCode;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户私钥证书 pfx 内容(Base64, PKCS12)")
    private String merchantCertPfx;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户私钥证书密码")
    private String merchantCertPassword;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "银盛平台公钥证书 cer 内容(Base64/PEM)")
    private String ysepayPublicCert;
}
