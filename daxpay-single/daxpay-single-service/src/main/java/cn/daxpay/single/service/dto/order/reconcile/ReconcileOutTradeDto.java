package cn.daxpay.single.service.dto.order.reconcile;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.handler.excel.AmountConverter;
import cn.daxpay.single.service.handler.excel.PaymentTypeConvert;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 账通道交易明细
 * @author xxm
 * @since 2024/1/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "账通道交易明细")
@ExcelIgnoreUnannotated
public class ReconcileOutTradeDto extends BaseDto {

    /** 关联对账订单ID */
    @Schema(description = "关联对账订单ID")
    @ExcelIgnore
    private Long reconcileId;

    /** 商品名称 */
    @Schema(description = "商品名称")
    @ExcelProperty("商品名称")
    private String title;

    /** 交易金额 */
    @Schema(description = "交易金额")
    @ExcelProperty(value = "交易金额(元)", converter = AmountConverter.class)
    private Integer amount;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    @Schema(description = "交易类型")
    @ExcelProperty(value = "交易类型",converter = PaymentTypeConvert.class)
    private String type;

    /** 本地交易号 */
    @Schema(description = "本地交易号")
    @ExcelProperty("本地交易号")
    private String tradeNo;

    /** 通道交易号 - 支付宝/微信的订单号 */
    @Schema(description = "通道交易号")
    @ExcelProperty("通道交易号")
    private String outTradeNo;

    /** 交易时间 */
    @Schema(description = "交易时间")
    @ExcelProperty("交易时间")
    private LocalDateTime tradeTime;

}
