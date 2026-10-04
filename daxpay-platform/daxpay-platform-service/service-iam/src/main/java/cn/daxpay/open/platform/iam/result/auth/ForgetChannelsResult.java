package cn.daxpay.open.platform.iam.result.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 找回密码可用通道查询结果
///
@Data
@Accessors(chain = true)
@Schema(title = "找回密码可用通道")
public class ForgetChannelsResult {

    @Schema(description = "邮件通道是否可用(发件箱配置就绪)")
    private Boolean emailEnabled;
}
