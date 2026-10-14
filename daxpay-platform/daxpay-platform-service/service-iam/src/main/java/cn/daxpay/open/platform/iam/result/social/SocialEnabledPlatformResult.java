package cn.daxpay.open.platform.iam.result.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 已启用的第三方登录平台
///
/// 登录页未登录场景下的最小公开返回, 仅暴露平台编码(source),
/// 不含 clientId/clientSecret/redirectUri/extra 等任何敏感字段.
/// 平台显示名/图标/品牌色由前端本地映射表(socialEnum.ts)决定.
///
@Data
@Accessors(chain = true)
@Schema(title = "已启用的第三方登录平台")
public class SocialEnabledPlatformResult {

    /// 平台编码(weChat/weCom/qq/github/gitee/feishu/dingTalk/douyin)
    @Schema(description = "平台编码")
    private String source;

    /// 是否仅微信客户端内可完成授权(公众号), PC 端发起绑定时须改走扫码跨设备流程
    @Schema(description = "是否需要扫码绑定")
    private boolean scanBind;
}
