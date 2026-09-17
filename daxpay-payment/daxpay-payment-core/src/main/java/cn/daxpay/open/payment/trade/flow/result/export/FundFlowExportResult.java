package cn.daxpay.open.payment.trade.flow.result.export;

import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 资金流水导出结果
///
/// 所有金额已换算为元, 枚举已翻译为国际化文本, 时间已格式化为字符串。
/// 表头注解值为 i18n 词条 key（`export.fund_flow.*`），渲染时由 [cn.daxpay.open.platform.common.excel.ExcelHeadI18nHandler]
/// 按当前请求语言翻译，中文文案真相源见 `common-i18n/src/main/resources/i18n/zh-CN/export/fund_flow.json`。
@Data
@Accessors(chain = true)
@Schema(title = "资金流水导出")
public class FundFlowExportResult {

    /// 资金交易号
    @ExcelProperty("export.fund_flow.tradeNo")
    @ColumnWidth(24)
    private String tradeNo;

    /// 商户号
    @ExcelProperty("export.fund_flow.mchNo")
    @ColumnWidth(18)
    private String mchNo;

    /// 商户名称
    @ExcelProperty("export.fund_flow.mchName")
    @ColumnWidth(20)
    private String mchName;

    /// 应用号
    @ExcelProperty("export.fund_flow.appId")
    @ColumnWidth(18)
    private String appId;

    /// 流水类型
    @ExcelProperty("export.fund_flow.flowType")
    @ColumnWidth(12)
    private String flowType;

    /// 商户业务单号
    @ExcelProperty("export.fund_flow.bizOrderNo")
    @ColumnWidth(22)
    private String bizOrderNo;

    /// 退款单号
    @ExcelProperty("export.fund_flow.refundNo")
    @ColumnWidth(22)
    private String refundNo;

    /// 订单标题
    @ExcelProperty("export.fund_flow.title")
    @ColumnWidth(30)
    private String title;

    /// 流水金额(主币种单位, 按币种小数位换算)
    @ExcelProperty("export.fund_flow.amount")
    @ColumnWidth(16)
    private String amount;

    /// 币种
    @ExcelProperty("export.fund_flow.currency")
    @ColumnWidth(10)
    private String currency;

    /// 支付通道
    @ExcelProperty("export.fund_flow.channel")
    @ColumnWidth(14)
    private String channel;

    /// 支付渠道
    @ExcelProperty("export.fund_flow.provider")
    @ColumnWidth(14)
    private String provider;

    /// 通道商户号
    @ExcelProperty("export.fund_flow.channelMchNo")
    @ColumnWidth(20)
    private String channelMchNo;

    /// 通道交易号
    @ExcelProperty("export.fund_flow.outOrderNo")
    @ColumnWidth(24)
    private String outOrderNo;

    /// 资金完成时间
    @ExcelProperty("export.fund_flow.finishTime")
    @ColumnWidth(22)
    private String finishTime;

    /// 创建时间
    @ExcelProperty("export.fund_flow.createTime")
    @ColumnWidth(22)
    private String createTime;
}
