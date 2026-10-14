package cn.daxpay.open.platform.iam.result.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 扫码绑定确认信息
///
/// 手机端 H5 确认页凭票据换取, 展示脱敏的目标账号名供扫码者核对,
/// 防「微信身份抢占」钓鱼(诱导扫码把微信绑到他人账号).
@Data
@Accessors(chain = true)
@Schema(title = "扫码绑定确认信息")
public class SocialBindScanConfirmResult {

    /// 脱敏的目标账号名(手机号/邮箱/用户名按各自规则脱敏)
    @Schema(description = "脱敏的目标账号名")
    private String maskedAccount;
}
