package cn.daxpay.open.payment.trade.order.result.export;

import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 退款订单导出结果
///
/// 所有金额已换算为元（主币种单位），枚举已翻译为国际化文本，时间已格式化为字符串。
@Data
@Accessors(chain = true)
@Schema(title = "退款订单导出")
public class RefundOrderExportResult {

    @ExcelProperty("退款号")
    @ColumnWidth(24)
    private String refundNo;

    @ExcelProperty("商户号")
    @ColumnWidth(18)
    private String mchNo;

    @ExcelProperty("商户名称")
    @ColumnWidth(20)
    private String mchName;

    @ExcelProperty("应用号")
    @ColumnWidth(18)
    private String appId;

    @ExcelProperty("资金交易号")
    @ColumnWidth(24)
    private String tradeNo;

    @ExcelProperty("交易类型")
    @ColumnWidth(14)
    private String tradeType;

    @ExcelProperty("商户订单号")
    @ColumnWidth(24)
    private String bizOrderNo;

    @ExcelProperty("订单标题")
    @ColumnWidth(30)
    private String title;

    @ExcelProperty("退款金额(元)")
    @ColumnWidth(16)
    private String amount;

    @ExcelProperty("订单金额(元)")
    @ColumnWidth(16)
    private String orderAmount;

    @ExcelProperty("币种")
    @ColumnWidth(10)
    private String currency;

    @ExcelProperty("退款状态")
    @ColumnWidth(14)
    private String status;

    @ExcelProperty("支付通道")
    @ColumnWidth(14)
    private String channel;

    @ExcelProperty("支付产品")
    @ColumnWidth(14)
    private String product;

    @ExcelProperty("通道商户号")
    @ColumnWidth(20)
    private String channelMchNo;

    @ExcelProperty("通道订单号")
    @ColumnWidth(24)
    private String outOrderNo;

    @ExcelProperty("通道退款流水号")
    @ColumnWidth(24)
    private String outRefundNo;

    @ExcelProperty("门店号")
    @ColumnWidth(14)
    private String storeNo;

    @ExcelProperty("退款完成时间")
    @ColumnWidth(22)
    private String finishTime;

    @ExcelProperty("创建时间")
    @ColumnWidth(22)
    private String createTime;
}
