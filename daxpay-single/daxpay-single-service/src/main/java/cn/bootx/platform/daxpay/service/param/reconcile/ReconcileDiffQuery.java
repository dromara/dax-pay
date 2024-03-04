package cn.bootx.platform.daxpay.service.param.reconcile;

import cn.bootx.platform.common.core.rest.param.QueryOrder;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.platform.daxpay.service.code.ReconcileDiffTypeEnum;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对账差异查询参数
 * @author xxm
 * @since 2024/3/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "对账差异查询参数")
public class ReconcileDiffQuery extends QueryOrder {

    /** 对账单ID */
    @Schema(description = "对账单ID")
    private Long recordId;

    /** 对账单明细ID */
    @Schema(description = "对账单明细ID")
    private Long detailId;

    /** 本地订单id */
    @Schema(description = "本地订单id")
    private Long orderId;

    /** 订单标题 */
    @Schema(description = "订单标题")
    private String title;

    /**
     * 对账订单类型
     * @see ReconcileTradeEnum
     */
    @Schema(description = "对账订单类型")
    private String orderType;

    /**
     * 差异类型
     * @see ReconcileDiffTypeEnum
     */
    @Schema(description = "差异类型")
    private String diffType;

    /** 差异内容 */
    @Schema(description = "差异内容")
    private List<ReconcileDiff> diffs;

    /** 网关订单号 */
    @Schema(description = "网关订单号")
    private String gatewayOrderNo;

    /** 订单时间 */
    @Schema(description = "订单时间")
    private LocalDateTime orderTime;
}
