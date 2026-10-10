package cn.daxpay.open.channel.douyin.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 抖音转账配置保存参数(商户端)
///
/// 商户端保存/更新抖音转账配置时接收的请求参数。
/// 商户端专用, 不含商户号(防越权: 由控制器从 [cn.daxpay.open.payment.common.context.PaymentContext] 组装),
/// 对照运营端参数 [DouyinTransferConfigParam]。
///
/// 一对一 upsert: 存在则更新, 不存在则新增。`transferAppRefId` 允许为空(支持清空),
/// 但发起转账时必须已配置, 由转账策略校验。
@Data
@Accessors(chain = true)
@Schema(title = "抖音转账配置保存参数(商户端)")
public class MchDouyinTransferConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "转账发起应用引用(指向 dy_mch_app 主键, 须为网站应用 web_app)")
    private Long transferAppRefId;
}
