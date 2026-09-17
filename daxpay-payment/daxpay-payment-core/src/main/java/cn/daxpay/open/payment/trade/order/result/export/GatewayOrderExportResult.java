package cn.daxpay.open.payment.trade.order.result.export;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 网关支付业务单导出结果
///
/// 所有金额已换算为元（主币种单位），枚举已翻译为国际化文本，时间已格式化为字符串。
@Data
@Accessors(chain = true)
@Schema(title = "网关支付业务单导出")
public class GatewayOrderExportResult {

    @ExcelProperty("平台网关单号")
    @ColumnWidth(24)
    private String orderNo;

    @ExcelProperty("商户号")
    @ColumnWidth(18)
    private String mchNo;

    @ExcelProperty("商户名称")
    @ColumnWidth(20)
    private String mchName;

    @ExcelProperty("应用号")
    @ColumnWidth(18)
    private String appId;

    @ExcelProperty("商户订单号")
    @ColumnWidth(24)
    private String bizOrderNo;

    @ExcelProperty("订单标题")
    @ColumnWidth(30)
    private String title;

    @ExcelProperty("网关类型")
    @ColumnWidth(14)
    private String gatewayType;

    @ExcelProperty("业务状态")
    @ColumnWidth(14)
    private String status;

    @ExcelProperty("支付通道")
    @ColumnWidth(14)
    private String channel;

    @ExcelProperty("支付渠道")
    @ColumnWidth(14)
    private String provider;

    @ExcelProperty("支付产品")
    @ColumnWidth(14)
    private String product;

    @ExcelProperty("支付能力")
    @ColumnWidth(18)
    private String capability;

    @ExcelProperty("金额(元)")
    @ColumnWidth(16)
    private String amount;

    @ExcelProperty("币种")
    @ColumnWidth(10)
    private String currency;

    @ExcelProperty("通道商户号")
    @ColumnWidth(20)
    private String channelMchNo;

    @ExcelProperty("门店号")
    @ColumnWidth(14)
    private String storeNo;

    @ExcelProperty("支付成功时间")
    @ColumnWidth(22)
    private String payTime;

    @ExcelProperty("创建时间")
    @ColumnWidth(22)
    private String createTime;
}
