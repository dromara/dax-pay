package cn.daxpay.open.payment.unipay.result.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信OpenId查询结果
///
@Data
@Accessors(chain = true)
@Schema(title = "微信OpenId查询结果")
public class OpenIdResult {

    @Schema(description = "OpenId")
    private String openId;
}
