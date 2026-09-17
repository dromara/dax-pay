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
@Data
@Accessors(chain = true)
@Schema(title = "资金交易凭证导出")
public class PayTradeExportResult {

    @ExcelProperty("平台交易号")
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

    @ExcelProperty("订单标题")
    @ColumnWidth(30)
    private String title;

    @ExcelProperty("交易形态")
    @ColumnWidth(14)
    private String tradeType;

    @ExcelProperty("支付产品")
    @ColumnWidth(14)
    private String product;

    @ExcelProperty("支付通道")
    @ColumnWidth(14)
    private String channel;

    @ExcelProperty("支付方式")
    @ColumnWidth(14)
    private String method;

    @ExcelProperty("支付渠道")
    @ColumnWidth(14)
    private String provider;

    @ExcelProperty("交易金额(元)")
    @ColumnWidth(16)
    private String amount;

    @ExcelProperty("币种")
    @ColumnWidth(10)
    private String currency;

    @ExcelProperty("入账金额(元)")
    @ColumnWidth(16)
    private String postedAmount;

    @ExcelProperty("可退金额(元)")
    @ColumnWidth(16)
    private String refundableBalance;

    @ExcelProperty("交易状态")
    @ColumnWidth(14)
    private String status;

    @ExcelProperty("分账状态")
    @ColumnWidth(14)
    private String allocStatus;

    @ExcelProperty("支付成功时间")
    @ColumnWidth(22)
    private String payTime;

    @ExcelProperty("通道商户号")
    @ColumnWidth(20)
    private String channelMchNo;

    @ExcelProperty("门店号")
    @ColumnWidth(14)
    private String storeNo;

    @ExcelProperty("通道订单号")
    @ColumnWidth(24)
    private String outOrderNo;

    @ExcelProperty("订单来源")
    @ColumnWidth(14)
    private String source;

    @ExcelProperty("创建时间")
    @ColumnWidth(22)
    private String createTime;
}
