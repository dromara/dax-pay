package org.dromara.daxpay.payment.miniapp.param.order;

import cn.bootx.platform.core.rest.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * 小程序支付订单查询参数
 * @author xxm
 * @since 2025/4/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "小程序支付订单查询参数")
public class MiniPayOrderQuery extends PageParam {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "开始日期")
    private LocalDate startTime;

    @Schema(description = "结束日期")
    private LocalDate endTime;

    @Schema(description = "支付状态")
    private List<String> payStatus;

    @Schema(description = "支付通道")
    private List<String> channel;

}
