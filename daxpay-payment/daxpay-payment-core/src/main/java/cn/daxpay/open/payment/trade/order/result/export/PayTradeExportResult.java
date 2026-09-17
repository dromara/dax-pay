package cn.daxpay.open.payment.trade.order.result.export;

import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 资金交易凭证导出结果
///
/// 安全子集：排除 buyerId、openid、authCode、attach、notifyUrl、clientIp、errorMsg 等敏感字段。
/// 所有金额已换算为元（主币种单位），枚举已翻译为国际化文本，时间已格式化为字符串。
/// 表头注解值为 i18n 词条 key（`export.pay_trade.*`），渲染时由 [cn.daxpay.open.platform.common.excel.ExcelHeadI18nHandler]
/// 按当前请求语言翻译，中文文案真相源见 `common-i18n/src/main/resources/i18n/zh-CN/export/pay_trade.json`。
@Data
@Accessors(chain = true)
@Schema(title = "资金交易凭证导出")
public class PayTradeExportResult {

    /// 平台交易号
    @ExcelProperty("export.pay_trade.tradeNo")
    @ColumnWidth(24)
    private String tradeNo;

    /// 商户号
    @ExcelProperty("export.pay_trade.mchNo")
    @ColumnWidth(18)
    private String mchNo;

    /// 商户名称
    @ExcelProperty("export.pay_trade.mchName")
    @ColumnWidth(20)
    private String mchName;

    /// 应用号
    @ExcelProperty("export.pay_trade.appId")
    @ColumnWidth(18)
    private String appId;

    /// 订单标题
    @ExcelProperty("export.pay_trade.title")
    @ColumnWidth(30)
    private String title;

    /// 交易形态
    @ExcelProperty("export.pay_trade.tradeType")
    @ColumnWidth(14)
    private String tradeType;

    /// 支付产品
    @ExcelProperty("export.pay_trade.product")
    @ColumnWidth(14)
    private String product;

    /// 支付通道
    @ExcelProperty("export.pay_trade.channel")
    @ColumnWidth(14)
    private String channel;

    /// 支付方式
    @ExcelProperty("export.pay_trade.method")
    @ColumnWidth(14)
    private String method;

    /// 支付渠道
    @ExcelProperty("export.pay_trade.provider")
    @ColumnWidth(14)
    private String provider;

    /// 交易金额(主币种单位, 按币种小数位换算)
    @ExcelProperty("export.pay_trade.amount")
    @ColumnWidth(16)
    private String amount;

    /// 币种
    @ExcelProperty("export.pay_trade.currency")
    @ColumnWidth(10)
    private String currency;

    /// 入账金额(主币种单位, 按币种小数位换算)
    @ExcelProperty("export.pay_trade.postedAmount")
    @ColumnWidth(16)
    private String postedAmount;

    /// 可退金额(主币种单位, 按币种小数位换算)
    @ExcelProperty("export.pay_trade.refundableBalance")
    @ColumnWidth(16)
    private String refundableBalance;

    /// 交易状态
    @ExcelProperty("export.pay_trade.status")
    @ColumnWidth(14)
    private String status;

    /// 分账状态
    @ExcelProperty("export.pay_trade.allocStatus")
    @ColumnWidth(14)
    private String allocStatus;

    /// 支付成功时间
    @ExcelProperty("export.pay_trade.payTime")
    @ColumnWidth(22)
    private String payTime;

    /// 通道商户号
    @ExcelProperty("export.pay_trade.channelMchNo")
    @ColumnWidth(20)
    private String channelMchNo;

    /// 门店号
    @ExcelProperty("export.pay_trade.storeNo")
    @ColumnWidth(14)
    private String storeNo;

    /// 通道订单号
    @ExcelProperty("export.pay_trade.outOrderNo")
    @ColumnWidth(24)
    private String outOrderNo;

    /// 订单来源
    @ExcelProperty("export.pay_trade.source")
    @ColumnWidth(14)
    private String source;

    /// 创建时间
    @ExcelProperty("export.pay_trade.createTime")
    @ColumnWidth(22)
    private String createTime;
}
