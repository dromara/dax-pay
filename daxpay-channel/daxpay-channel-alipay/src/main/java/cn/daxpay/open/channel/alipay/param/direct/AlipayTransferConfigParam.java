package cn.daxpay.open.channel.alipay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝转账配置保存参数
///
/// 一对一 upsert: 存在则更新, 不存在则新增。`transferAppRefId` 必填,
/// 未绑定时发起转账将报错提示先绑定转出应用。
///
@Data
@Accessors(chain = true)
@Schema(title = "支付宝转账配置保存参数")
public class AlipayTransferConfigParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotNull(message = "{validation.field.transferAppRefId.notNull}")
    @Schema(description = "转账转出应用引用(指向 alipay_direct_app 主键)")
    private Long transferAppRefId;
}
