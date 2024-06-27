package cn.daxpay.single.service.param.reconcile;

import cn.bootx.platform.common.core.rest.param.QueryOrder;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.ReconcileTradeEnum;
import cn.daxpay.single.service.code.ReconcileDiffTypeEnum;
import cn.bootx.table.modify.annotation.DbColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

    /** 对账号 */
    @DbColumn(comment = "对账号")
    private String reconcileNo;

    /** 本地交易号 */
    @Schema(description = "本地交易号")
    private String tradeNo;

    /** 通道交易号 */
    @Schema(description = "通道交易号")
    private String outTradeNo;

    /** 订单标题 */
    @Schema(description = "订单标题")
    private String title;

    /**
     * 通道
     * @see PayChannelEnum
     */
    @Schema(description = "通道")
    private String channel;

    /**
     * 对账订单类型
     * @see ReconcileTradeEnum
     */
    @Schema(description = "对账订单类型")
    private String tradeType;

    /**
     * 差异类型
     * @see ReconcileDiffTypeEnum
     */
    @Schema(description = "差异类型")
    private String diffType;

}
