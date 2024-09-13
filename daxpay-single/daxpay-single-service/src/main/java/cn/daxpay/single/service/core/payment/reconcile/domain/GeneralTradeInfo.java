package cn.daxpay.single.service.core.payment.reconcile.domain;

import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.handler.excel.AmountConverter;
import cn.daxpay.single.service.handler.excel.PaymentTypeConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 通用交易对象对象，用于与网关进行对账
 * @author xxm
 * @since 2024/3/1
 */
@Data
@Accessors(chain = true)
public class GeneralTradeInfo {
    /** 标题 */
    @ExcelProperty("标题")
    private String title;

    /** 金额 */
    @ExcelProperty(value = "金额(元)", converter = AmountConverter.class)
    private Integer amount;

    /**
     * 业务类型
     * @see TradeTypeEnum
     */
    @ExcelProperty(value = "业务类型", converter = PaymentTypeConvert.class)
    private String type;

    /** 本地交易号 */
    @ExcelProperty("本地交易号")
    private String tradeNo;

    /** 通道交易号 */
    @ExcelProperty("通道交易号")
    private String outTradeNo;

    /** 网关完成时间 */
    @ExcelProperty("网关完成时间")
    private LocalDateTime finishTime;
}
