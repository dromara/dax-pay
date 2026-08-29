package cn.daxpay.open.platform.iam.result.session;

import cn.daxpay.open.platform.core.rest.result.PageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 登录锁定分页结果
///
/// 附带锁定功能开关标志, 未启用时前端展示提示(列表数据为历史残留)。
@Data
@Accessors(chain = true)
@Schema(title = "登录锁定分页结果")
public class LoginLockPageResult {

    @Schema(description = "登录锁定功能是否启用")
    private Boolean lockoutEnabled;

    @Schema(description = "分页数据")
    private PageResult<LoginLockResult> page;
}
