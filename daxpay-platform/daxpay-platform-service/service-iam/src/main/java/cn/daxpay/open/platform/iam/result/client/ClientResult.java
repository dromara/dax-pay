package cn.daxpay.open.platform.iam.result.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 登录终端(身份域)主数据
///
@Data
@Accessors(chain = true)
@Schema(title = "登录终端主数据")
public class ClientResult {

    /// 身份域编码(admin/merchant/gateway)
    @Schema(description = "身份域编码")
    private String code;

    /// 展示名(当前语言)
    @Schema(description = "展示名")
    private String name;
}
