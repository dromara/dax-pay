package cn.daxpay.open.channel.alipay.result.isv;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 支付宝服务商分账接收方结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商分账接收方结果")
public class AlipayIsvAllocReceiverResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "接收方类型")
    private String receiverType;

    @Schema(description = "接收方账号(解密后明文)")
    private String receiverAccount;

    @Schema(description = "接收方名称(解密后明文)")
    private String receiverName;

    @Schema(description = "绑定状态")
    private String status;

    @Schema(description = "最近一次绑定/解绑失败原因")
    private String errorMsg;

    @Schema(description = "绑定成功时间")
    private OffsetDateTime bindTime;

    @Schema(description = "解绑成功时间")
    private OffsetDateTime unbindTime;
}
