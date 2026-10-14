package cn.daxpay.open.platform.iam.result.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 扫码登录二维码
///
/// PC 登录页发起公众号扫码登录时返回: 票据(ticket)供后续轮询登录结果,
/// 页面地址(qrUrl)由前端渲染为二维码, 手机微信扫码打开后完成授权登录.
@Data
@Accessors(chain = true)
@Schema(title = "扫码登录二维码")
public class SocialLoginScanQrResult {

    /// 扫码登录票据(5 分钟过期, 轮询登录结果的凭证)
    @Schema(description = "扫码登录票据")
    private String ticket;

    /// 手机微信扫码打开的 H5 发起页地址(含票据参数)
    @Schema(description = "二维码页面地址")
    private String qrUrl;
}
