package cn.daxpay.open.payment.unipay.param.device;

import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.ClientRuntimeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 码牌发起支付参数(H5/小程序)
@Data
@Schema(title = "码牌支付参数")
public class CodePayParam {

    /// 码牌编码
    @Schema(description = "码牌编码")
    @NotBlank(message = "{validation.field.code.notBlank}")
    @Size(max = 64, message = "{validation.field.code.size}")
    private String code;

    /// 支付金额(分); 固定金额码牌可空(服务端取码牌配置)
    @Schema(description = "支付金额(分, 自定义金额时必填)")
    @Min(value = 1, message = "{validation.field.amount.min}")
    private Long amount;

    /// 备注/描述(与普通支付 description 长度对齐)
    @Schema(description = "备注")
    @Size(max = 50, message = "{validation.field.description.size}")
    private String description;

    /// 客户端环境
    /// @see ClientEnvEnum
    @Schema(description = "客户端环境(wechat_pay/alipay/union_pay/douyin)")
    @NotBlank(message = "{validation.field.clientEnv.notBlank}")
    @Size(max = 32, message = "{validation.field.clientEnv.size}")
    private String clientEnv;

    /// 运行形态, 默认 h5
    /// @see ClientRuntimeEnum
    @Schema(description = "运行形态(h5/mini, 默认 h5)")
    @Size(max = 16, message = "{validation.field.runtime.size}")
    private String runtime;

    @Schema(description = "OpenId(微信/支付宝 JSAPI 等)")
    @Size(max = 128, message = "{validation.field.openId.size}")
    private String openId;

    @Schema(description = "设备(mobile/pc)")
    @Size(max = 16, message = "{validation.field.device.size}")
    private String device;

    @Schema(description = "客户端IP")
    @Size(max = 64, message = "{validation.field.clientIp.size}")
    private String clientIp;
}
