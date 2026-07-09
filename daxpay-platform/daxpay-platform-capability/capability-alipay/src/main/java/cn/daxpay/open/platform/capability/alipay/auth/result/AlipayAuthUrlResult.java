package cn.daxpay.open.platform.capability.alipay.auth.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝授权链接生成结果
///
@Data
@Accessors(chain = true)
@Schema(title = "支付宝授权链接生成结果")
public class AlipayAuthUrlResult {

    /// 授权访问链接(前端 location.href 跳转到支付宝)
    @Schema(description = "授权访问链接")
    private String authUrl;

    /// 查询标识码, 用于授权回调后关联上下文(H5 重定向场景)
    @Schema(description = "查询标识码")
    private String queryCode;
}
