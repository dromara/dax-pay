package cn.daxpay.open.payment.admin.result.develop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 签名调试结果
@Data
@Accessors(chain = true)
@Schema(title = "签名调试结果")
public class DevelopSignResult {

    /// 待签名原文(键值拼接串)
    @Schema(description = "待签名原文")
    private String signStr;

    /// 签名值
    @Schema(description = "签名值")
    private String sign;
}
