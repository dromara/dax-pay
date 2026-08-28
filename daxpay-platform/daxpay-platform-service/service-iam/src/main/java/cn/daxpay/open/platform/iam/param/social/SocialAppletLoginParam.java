package cn.daxpay.open.platform.iam.param.social;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 小程序快捷登录参数
///
/// 小程序端(微信/支付宝/抖音)通过 uni.login / my.getAuthCode / tt.login 获取平台登录 code 后直传,
/// 无 OAuth 跳转与 state, code 具有平台侧一次性语义.
///
@Data
@Accessors(chain = true)
@Schema(title = "小程序快捷登录参数")
public class SocialAppletLoginParam {

    /// 终端编码(管理端小程序恒为 admin, 预留商户端小程序扩展)
    @Schema(description = "终端编码")
    @NotBlank(message = "{validation.field.client.notBlank}")
    private String client;

    /// 平台来源(weChatApplet/alipayApplet/douyinApplet)
    @Schema(description = "平台来源")
    @NotBlank(message = "{validation.field.source.notBlank}")
    private String source;

    /// 平台登录凭证(uni.login / my.getAuthCode / tt.login 返回的 code)
    @Schema(description = "平台登录凭证")
    @NotBlank(message = "{validation.field.oauthCode.notBlank}")
    private String code;
}
