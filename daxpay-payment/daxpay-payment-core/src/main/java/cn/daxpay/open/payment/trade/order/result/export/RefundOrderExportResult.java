package cn.daxpay.open.payment.trade.order.result.export;

import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 退款订单导出结果
///
/// 所有金额已换算为元（主币种单位），枚举已翻译为国际化文本，时间已格式化为字符串。
/// 表头注解值为 i18n 词条 key（`export.refund_order.*`），渲染时由 [cn.daxpay.open.platform.common.excel.ExcelHeadI18nHandler]
/// 按当前请求语言翻译，中文文案真相源见 `common-i18n/src/main/resources/i18n/zh-CN/export/refund_order.json`。
@Data
@Accessors(chain = true)
@Schema(title = "退款订单导出")
public class RefundOrderExportResult {

    /// 退款号
    @ExcelProperty("export.refund_order.refundNo")
    @ColumnWidth(24)
    private String refundNo;

    /// 商户号
    @ExcelProperty("export.refund_order.mchNo")
    @ColumnWidth(18)
    private String mchNo;

    /// 商户名称
    @ExcelProperty("export.refund_order.mchName")
    @ColumnWidth(20)
    private String mchName;

    /// 应用号
    @ExcelProperty("export.refund_order.appId")
    @ColumnWidth(18)
    private String appId;

    /// 资金交易号
    @ExcelProperty("export.refund_order.tradeNo")
    @ColumnWidth(24)
    private String tradeNo;

    /// 交易类型
    @ExcelProperty("export.refund_order.tradeType")
    @ColumnWidth(14)
    private String tradeType;

    /// 商户订单号
    @ExcelProperty("export.refund_order.bizOrderNo")
    @ColumnWidth(24)
    private String bizOrderNo;

    /// 订单标题
    @ExcelProperty("export.refund_order.title")
    @ColumnWidth(30)
    private String title;

    /// 退款金额(主币种单位, 按币种小数位换算)
    @ExcelProperty("export.refund_order.amount")
    @ColumnWidth(16)
    private String amount;

    /// 订单金额(主币种单位, 按币种小数位换算)
    @ExcelProperty("export.refund_order.orderAmount")
    @ColumnWidth(16)
    private String orderAmount;

    /// 币种
    @ExcelProperty("export.refund_order.currency")
    @ColumnWidth(10)
    private String currency;

    /// 退款状态
    @ExcelProperty("export.refund_order.status")
    @ColumnWidth(14)
    private String status;

    /// 支付通道
    @ExcelProperty("export.refund_order.channel")
    @ColumnWidth(14)
    private String channel;

    /// 支付产品
    @ExcelProperty("export.refund_order.product")
    @ColumnWidth(14)
    private String product;

    /// 通道商户号
    @ExcelProperty("export.refund_order.channelMchNo")
    @ColumnWidth(20)
    private String channelMchNo;

    /// 通道订单号
    @ExcelProperty("export.refund_order.outOrderNo")
    @ColumnWidth(24)
    private String outOrderNo;

    /// 通道退款流水号
    @ExcelProperty("export.refund_order.outRefundNo")
    @ColumnWidth(24)
    private String outRefundNo;

    /// 门店号
    @ExcelProperty("export.refund_order.storeNo")
    @ColumnWidth(14)
    private String storeNo;

    /// 退款完成时间
    @ExcelProperty("export.refund_order.finishTime")
    @ColumnWidth(22)
    private String finishTime;

    /// 创建时间
    @ExcelProperty("export.refund_order.createTime")
    @ColumnWidth(22)
    private String createTime;
}
