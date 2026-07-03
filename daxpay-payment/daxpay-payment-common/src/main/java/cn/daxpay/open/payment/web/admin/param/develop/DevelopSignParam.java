package cn.daxpay.open.payment.web.admin.param.develop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 签名调试-生成签名参数
@Data
@Accessors(chain = true)
@Schema(title = "签名调试-生成签名参数")
public class DevelopSignParam {

    /// 待签名 JSON 字符串
    @Schema(description = "待签名JSON字符串")
    private String json;

    /// 私钥(PEM 格式)
    @Schema(description = "私钥(PEM格式)")
    private String privateKey;
}
