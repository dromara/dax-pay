package org.dromara.daxpay.payment.miniapp.param.order;

import cn.bootx.platform.core.rest.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * 小程序退款订单查询参数
 * @author xxm
 * @since 2025/6/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "小程序退款订单查询参数")
public class MiniRefundOrderQuery extends PageParam {
    @Schema(description = "退款号")
    private String refundNo;
    @Schema(description = "开始日期")
    private LocalDate startTime;
    @Schema(description = "结束日期")
    private LocalDate endTime;
    @Schema(description = "退款状态")
    private List<String> refundStatus;
    @Schema(description = "支付通道")
    private List<String> channel;
    @Schema(description = "应用号")
    private String appId;
}
