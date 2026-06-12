package org.dromara.daxpay.platform.capability.wechat.auth.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信授权链接结果
///
@Data
@Accessors(chain = true)
@Schema(title = "微信授权链接结果")
public class WechatAuthUrlResult {

    @Schema(description = "授权链接")
    private String authUrl;

    @Schema(description = "查询码")
    private String queryCode;
}
