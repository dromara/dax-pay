package cn.daxpay.open.channel.ums.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 银联商务直连密钥配置保存参数
///
/// 以 channelMchNo(通道商户号) 作为唯一标识定位记录,
/// mchNo(平台商户号)/merchantNo(银联 mid) 为不可变身份字段, 创建时写入后永不可改, 不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "银联商务直连密钥配置保存参数")
public class UmsDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "终端号(tid)")
    private String terminalNo;

    @Schema(description = "银联商务应用 AppId")
    private String umsAppId;

    @Schema(description = "应用密钥(HmacSHA256 签名密钥)")
    private String appKey;

    @Schema(description = "通讯密钥(回调验签密钥)")
    private String secretKey;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
