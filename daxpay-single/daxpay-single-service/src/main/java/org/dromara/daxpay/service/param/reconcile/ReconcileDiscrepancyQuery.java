package org.dromara.daxpay.service.param.reconcile;

import cn.bootx.platform.core.annotation.QueryParam;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.service.common.param.MchAppQuery;
import org.dromara.daxpay.service.enums.ReconcileDiscrepancyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 对账差异记录查询参数
 * @author xxm
 * @since 2024/8/7
 */
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "对账差异记录查询参数")
public class ReconcileDiscrepancyQuery extends MchAppQuery {
    /** 对账号 */
    @Schema(description = "对账号")
    private String reconcileNo;

    /** 对账日期 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "对账日期")
    private LocalDate reconcileDate;

    /** 对账通道 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "对账通道")
    private String channel;

    /**
     * 差异类型
     * @see ReconcileDiscrepancyTypeEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "差异类型")
    private String discrepancyType;

    /* 平台侧信息 */
    /** 平台交易号 */
    @Schema(description = "平台交易号")
    private String tradeNo;

    /** 商户交易号 */
    @Schema(description = "商户订单号")
    private String bizTradeNo;

    /** 关联通道交易号 */
    @Schema(description = "关联通道交易号")
    private String outTradeNo;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "交易类型")
    private String tradeType;

    /** 交易状态 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "交易状态")
    private String tradeStatus;

    /* 通道侧信息 */
    /** 通道交易号 */
    @Schema(description = "通道交易号")
    private String channelTradeNo;

    @Schema(description = "通道关联平台交易号")
    private String channelOutTradeNo;

    /** 通道交易类型 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "通道交易类型")
    private String channelTradeType;


    /** 通道交易状态 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "通道交易状态")
    private String channelTradeStatus;

}
