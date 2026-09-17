package cn.daxpay.open.payment.trade.flow.result.export;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 资金流水导出结果
///
/// 所有金额已换算为元, 枚举已翻译为国际化文本, 时间已格式化为字符串。
@Data
@Accessors(chain = true)
@Schema(title = "资金流水导出")
public class FundFlowExportResult {

    @ExcelProperty("资金交易号")
    @ColumnWidth(24)
    private String tradeNo;

    @ExcelProperty("商户号")
    @ColumnWidth(18)
    private String mchNo;

    @ExcelProperty("商户名称")
    @ColumnWidth(20)
    private String mchName;

    @ExcelProperty("应用号")
    @ColumnWidth(18)
    private String appId;

    @ExcelProperty("流水类型")
    @ColumnWidth(12)
    private String flowType;

    @ExcelProperty("商户业务单号")
    @ColumnWidth(22)
    private String bizOrderNo;

    @ExcelProperty("退款单号")
    @ColumnWidth(22)
    private String refundNo;

    @ExcelProperty("订单标题")
    @ColumnWidth(30)
    private String title;

    @ExcelProperty("流水金额(元)")
    @ColumnWidth(16)
    private String amount;

    @ExcelProperty("币种")
    @ColumnWidth(10)
    private String currency;

    @ExcelProperty("支付通道")
    @ColumnWidth(14)
    private String channel;

    @ExcelProperty("支付渠道")
    @ColumnWidth(14)
    private String provider;

    @ExcelProperty("通道商户号")
    @ColumnWidth(20)
    private String channelMchNo;

    @ExcelProperty("通道交易号")
    @ColumnWidth(24)
    private String outOrderNo;

    @ExcelProperty("资金完成时间")
    @ColumnWidth(22)
    private String finishTime;

    @ExcelProperty("创建时间")
    @ColumnWidth(22)
    private String createTime;
}
