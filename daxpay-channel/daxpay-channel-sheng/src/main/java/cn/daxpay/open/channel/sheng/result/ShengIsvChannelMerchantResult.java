package cn.daxpay.open.channel.sheng.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 盛付通服务商通道商户绑定返回结果
///
/// 绑定行回显: 子商户身份字段 + [cn.daxpay.open.payment.common.entity.MchBaseEntity] 基类字段。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "盛付通服务商通道商户绑定返回结果")
public class ShengIsvChannelMerchantResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "子商户号")
    private String subMchId;

    @Schema(description = "盛付通分配的子商户应用ID")
    private String sdpAppId;
}
