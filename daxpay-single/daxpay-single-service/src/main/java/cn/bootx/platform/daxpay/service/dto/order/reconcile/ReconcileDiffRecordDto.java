package cn.bootx.platform.daxpay.service.dto.order.reconcile;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.platform.daxpay.service.code.ReconcileDiffTypeEnum;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff;
import cn.bootx.table.modify.annotation.DbColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对账差异单
 * @author xxm
 * @since 2024/3/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "对账差异单")
public class ReconcileDiffRecordDto extends BaseDto {

    /** 对账单ID */
    @Schema(description = "对账单ID")
    private Long ReconcileId;

    /** 对账单明细ID */
    @Schema(description = "对账单明细ID")
    private Long detailId;

    /** 本地交易号 */
    @Schema(description = "本地交易号")
    private String tradeNo;

    /** 外部交易号 */
    @Schema(description = "外部交易号")
    private String outOrderNo;

    /** 交易时间 */
    @Schema(description = "交易时间")
    private LocalDateTime tradeTime;

    /** 订单标题 */
    @Schema(description = "订单标题")
    private String title;

    /** 交易金额 */
    @Schema(description = "交易金额")
    private Integer amount;

    /** 外部交易金额 */
    @DbColumn(comment = "外部交易金额")
    private Integer outAmount;

    /**
     * 订单类型
     * @see ReconcileTradeEnum
     */
    @Schema(description = "订单类型")
    private String orderType;

    /**
     * 差异类型
     * @see ReconcileDiffTypeEnum
     */
    @Schema(description = "差异类型")
    private String diffType;

    /**
     * 差异内容, 存储json字符串, 格式为
     * {属性: '标题', 本地字段值:'标题1', 网关字段值: '标题2'}
     * @see ReconcileDiff
     */
    @Schema(description = "差异内容")
    private List<ReconcileDiff> diffs;
}
