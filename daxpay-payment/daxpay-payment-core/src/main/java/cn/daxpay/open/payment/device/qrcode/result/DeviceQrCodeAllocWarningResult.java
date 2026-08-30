package cn.daxpay.open.payment.device.qrcode.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 码牌分账能力预警项
///
/// 码牌开启分账开关前的预检查询结果: 按当前网关支付配置解析出的扫码场景,
/// 其路由产品不支持分账时返回一条。仅作预警提示, 不阻断保存与支付
/// (支付时会自动降级普通收款, 交易分账状态记为 unsupported)。
@Data
@Accessors(chain = true)
@Schema(title = "码牌分账能力预警项")
public class DeviceQrCodeAllocWarningResult {

    /// 客户端环境(wechat/alipay/union_pay/douyin)
    @Schema(description = "客户端环境")
    private String clientEnv;

    /// 支付形态(h5/mini)
    @Schema(description = "支付形态")
    private String payForm;

    /// 路由解析出的支付产品编码
    @Schema(description = "支付产品编码")
    private String product;

    /// 产品所属通道
    @Schema(description = "支付通道")
    private String channel;
}
