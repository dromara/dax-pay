package cn.daxpay.open.payment.merchant.param.develop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 签名调试-验签参数
@Data
@Accessors(chain = true)
@Schema(title = "签名调试-验签参数")
public class DevelopVerifyParam {

    /// 待验签 JSON 字符串(不含 sign 字段)
    @Schema(description = "待验签JSON字符串(不含sign字段)")
    private String json;

    /// 签名值
    @Schema(description = "签名值")
    private String sign;

    /// 公钥(PEM 格式)
    @Schema(description = "公钥(PEM格式)")
    private String publicKey;
}
