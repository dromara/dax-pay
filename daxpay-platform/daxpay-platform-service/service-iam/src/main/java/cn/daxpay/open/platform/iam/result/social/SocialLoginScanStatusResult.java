package cn.daxpay.open.platform.iam.result.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 扫码登录状态
///
/// PC 登录页凭票据轮询公众号扫码登录的进度, success 时一次性返回登录令牌.
@Data
@Accessors(chain = true)
@Schema(title = "扫码登录状态")
public class SocialLoginScanStatusResult {

    /// 票据不存在或已过期(统一按不存在返回)
    public static final String NOT_EXIST = "not_exist";

    /// 手机端已在确认页取消本次登录(终态: PC 停止轮询并提示刷新二维码)
    public static final String CANCELLED = "cancelled";

    /// 已扫码中间态(手机端已打开 H5 发起页, PC 提示扫码成功待操作, 继续轮询)
    public static final String SCANNED = "scanned";

    /// 登录状态: waiting 等待扫码授权 / scanned 已扫码待手机端操作 / not_bound 微信未绑定账号 / cancelled 手机端已取消 / success 登录完成 / not_exist 票据不存在或已过期
    @Schema(description = "登录状态(waiting/scanned/not_bound/cancelled/success/not_exist)")
    private String status;

    /// 登录 token(status=success 时返回, 票据一次性消费)
    @Schema(description = "登录token")
    private String token;
}
