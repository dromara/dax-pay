package cn.daxpay.single.service.dto.order.reconcile;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.ReconcileTradeEnum;
import cn.daxpay.single.service.code.ReconcileDiffTypeEnum;
import cn.daxpay.single.service.core.payment.reconcile.domain.ReconcileDiffDetail;
import cn.bootx.table.modify.annotation.DbColumn;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
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
@ExcelIgnoreUnannotated
public class ReconcileDiffDto extends BaseDto {

    /** 对账单ID */
    @Schema(description = "对账单ID")
    private Long reconcileId;

    /** 对账单号 */
    @Schema(description = "对账单号")
    private String reconcileNo;

    /** 对账单明细ID */
    @Schema(description = "对账单明细ID")
    private Long detailId;

    /** 对账日期 */
    @Schema(description = "对账日期")
    private LocalDate reconcileDate;

    /** 本地交易号 */
    @Schema(description = "本地交易号")
    private String tradeNo;

    /** 通道交易号 */
    @Schema(description = "通道交易号")
    private String outTradeNo;

    /** 交易时间 */
    @Schema(description = "交易时间")
    private LocalDateTime tradeTime;

    /** 订单标题 */
    @Schema(description = "订单标题")
    private String title;

    /**
     * 通道
     * @see PayChannelEnum
     */
    @Schema(description = "通道")
    private String channel;

    /** 交易金额 */
    @Schema(description = "交易金额")
    private Integer amount;

    /** 通道交易金额 */
    @DbColumn(comment = "通道交易金额")
    private Integer outAmount;

    /**
     * 订单类型
     * @see ReconcileTradeEnum
     */
    @Schema(description = "订单类型")
    private String tradeType;

    /**
     * 差异类型
     * @see ReconcileDiffTypeEnum
     */
    @Schema(description = "差异类型")
    private String diffType;

    /**
     * 差异内容, 存储json字符串, 格式为
     * {属性: '标题', 本地字段值:'标题1', 网关字段值: '标题2'}
     * @see ReconcileDiffDetail
     */
    @Schema(description = "差异内容")
    private List<ReconcileDiffDetail> diffs;
}
