package cn.daxpay.open.platform.iam.result.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 扫码绑定状态
///
/// PC 端凭票据轮询公众号扫码绑定的进度.
@Data
@Accessors(chain = true)
@Schema(title = "扫码绑定状态")
public class SocialBindScanStatusResult {

    /// 票据不存在或已过期(含票据不属于当前用户的情形, 统一按不存在返回防探测)
    public static final String NOT_EXIST = "not_exist";

    /// 手机端已在确认页取消本次绑定(终态: PC 停止轮询并提示刷新二维码)
    public static final String CANCELLED = "cancelled";

    /// 已扫码中间态(手机端已打开 H5 发起页, PC 提示扫码成功待操作, 继续轮询)
    public static final String SCANNED = "scanned";

    /// 绑定状态: waiting 等待扫码授权 / scanned 已扫码待手机端操作 / success 绑定完成 / cancelled 手机端已取消 / not_exist 票据不存在或已过期
    @Schema(description = "绑定状态(waiting/scanned/success/cancelled/not_exist)")
    private String status;
}
