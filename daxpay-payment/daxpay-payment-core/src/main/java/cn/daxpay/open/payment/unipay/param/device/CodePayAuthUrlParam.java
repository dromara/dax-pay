package cn.daxpay.open.payment.unipay.param.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 码牌生成授权链接参数
@Data
@Schema(title = "码牌生成授权链接参数")
public class CodePayAuthUrlParam {

    /// 码牌编码
    @Schema(description = "码牌编码")
    @NotBlank(message = "{validation.field.code.notBlank}")
    @Size(max = 64, message = "{validation.field.code.size}")
    private String code;

    /// 客户端环境
    /// @see cn.daxpay.open.payment.merchant.enums.ClientEnvEnum
    @Schema(description = "客户端环境(wechat/alipay/union_pay/douyin)")
    @NotBlank(message = "{validation.field.clientEnv.notBlank}")
    @Size(max = 32, message = "{validation.field.clientEnv.size}")
    private String clientEnv;
}
