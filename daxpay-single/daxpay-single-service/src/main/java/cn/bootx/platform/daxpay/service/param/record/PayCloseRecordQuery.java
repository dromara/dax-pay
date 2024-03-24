package cn.bootx.platform.daxpay.service.param.record;

import cn.bootx.platform.common.core.rest.param.QueryOrder;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付关闭记录")
public class PayCloseRecordQuery extends QueryOrder {

    @Schema(description = "支付记录id")
    private Long paymentId;

    @Schema(description = "业务号")
    private String businessNo;

    /**
     * 关闭的异步支付通道, 可以为空
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "关闭的异步支付通道")
    private String asyncChannel;

    @Schema(description = "请求链路ID")
    private String reqId;
}
