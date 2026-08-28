package cn.daxpay.open.platform.iam.param.social;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 小程序快捷绑定参数
///
/// 已登录用户在小程序内发起三方绑定时直传平台登录 code,
/// 终端与用户身份从登录态取, 不接受前端传参.
///
@Data
@Accessors(chain = true)
@Schema(title = "小程序快捷绑定参数")
public class SocialAppletBindParam {

    /// 平台来源(weChatApplet/alipayApplet/douyinApplet)
    @Schema(description = "平台来源")
    @NotBlank(message = "{validation.field.source.notBlank}")
    private String source;

    /// 平台登录凭证(uni.login / my.getAuthCode / tt.login 返回的 code)
    @Schema(description = "平台登录凭证")
    @NotBlank(message = "{validation.field.oauthCode.notBlank}")
    private String code;
}
