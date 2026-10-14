package cn.daxpay.open.platform.iam.result.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 扫码绑定二维码
///
/// PC 端发起公众号扫码绑定时返回: 票据(ticket)供后续轮询绑定结果,
/// 页面地址(qrUrl)由前端渲染为二维码, 手机微信扫码打开后完成授权绑定.
@Data
@Accessors(chain = true)
@Schema(title = "扫码绑定二维码")
public class SocialBindScanQrResult {

    /// 扫码绑定票据(5 分钟过期, 轮询绑定结果的凭证)
    @Schema(description = "扫码绑定票据")
    private String ticket;

    /// 手机微信扫码打开的 H5 发起页地址(含票据参数)
    @Schema(description = "二维码页面地址")
    private String qrUrl;
}
