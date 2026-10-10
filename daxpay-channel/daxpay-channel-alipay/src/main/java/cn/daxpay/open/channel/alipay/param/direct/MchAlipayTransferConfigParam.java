package cn.daxpay.open.channel.alipay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝转账配置保存参数(商户端)
///
/// 商户端专用参数, 不含商户号(防越权: 商户号由控制器从 [cn.daxpay.open.payment.common.context.PaymentContext]
/// 取当前登录商户组装, 对照运营端参数 [AlipayTransferConfigParam])。
///
/// 一对一 upsert: 存在则更新, 不存在则新增。`transferAppRefId` 必填,
/// 未绑定时发起转账将报错提示先绑定转出应用。
@Data
@Accessors(chain = true)
@Schema(title = "支付宝转账配置保存参数(商户端)")
public class MchAlipayTransferConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotNull(message = "{validation.field.transferAppRefId.notNull}")
    @Schema(description = "转账转出应用引用(指向 alipay_direct_app 主键)")
    private Long transferAppRefId;
}
