package cn.bootx.platform.daxpay.service.dto.order.reconcile;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.platform.daxpay.service.code.ReconcileDiffTypeEnum;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiffDetail;
import cn.bootx.platform.daxpay.service.handler.excel.*;
import cn.bootx.table.modify.annotation.DbColumn;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 对账差异单导出对象
 * @author xxm
 * @since 2024/3/3
 */
@Data
@Schema(title = "对账差异单")
@ExcelIgnoreUnannotated
public class ReconcileDiffExcel {

    /** 对账单ID */
    @Schema(description = "对账单ID")
    @ExcelIgnore
    private Long reconcileId;

    /** 对账单号 */
    @Schema(description = "对账单号")
    @ExcelProperty("对账单号")
    private String reconcileNo;

    /** 对账单明细ID */
    @Schema(description = "对账单明细ID")
    @ExcelIgnore
    private Long detailId;

    /** 对账日期 */
    @Schema(description = "对账日期")
    @ExcelProperty("对账日期")
    private LocalDate reconcileDate;

    /** 本地交易号 */
    @Schema(description = "本地交易号")
    @ExcelProperty("本地交易号")
    private String tradeNo;

    /** 通道交易号 */
    @Schema(description = "通道交易号")
    @ExcelProperty("通道交易号")
    private String outTradeNo;

    /** 交易时间 */
    @Schema(description = "交易时间")
    @ExcelProperty("网关完成时间")
    private LocalDateTime tradeTime;

    /** 订单标题 */
    @Schema(description = "订单标题")
    @ExcelProperty("商品名称")
    private String title;

    /**
     * 通道
     * @see PayChannelEnum
     */
    @Schema(description = "通道")
    @ExcelProperty(value = "通道", converter = PayChannelConvert.class)
    private String channel;

    /** 交易金额 */
    @Schema(description = "交易金额")
    @ExcelProperty(value = "交易金额(元)", converter = AmountConverter.class)
    private Integer amount;

    /** 通道交易金额 */
    @DbColumn(comment = "通道交易金额")
    @ExcelProperty(value = "通道交易金额(元)", converter = AmountConverter.class)
    private Integer outAmount;

    /**
     * 交易类型
     * @see ReconcileTradeEnum
     */
    @Schema(description = "交易类型")
    @ExcelProperty(value = "交易类型", converter = ReconcileTradeConvert.class)
    private String tradeType;

    /**
     * 差异类型
     * @see ReconcileDiffTypeEnum
     */
    @Schema(description = "差异类型")
    @ExcelProperty(value = "差异类型", converter = ReconcileDiffTypeConvert.class)
    private String diffType;

    /**
     * 差异内容, 存储json字符串, 格式为
     * {属性: '标题', 本地字段值:'标题1', 网关字段值: '标题2'}
     * @see ReconcileDiffDetail
     */
    @Schema(description = "差异内容")
    @ExcelProperty(value = "差异内容", converter = ReconcileDiffDetailConvert.class)
    private List<ReconcileDiffDetail> diffs;
}
