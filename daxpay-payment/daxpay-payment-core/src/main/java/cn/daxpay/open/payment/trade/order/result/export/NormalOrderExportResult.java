package cn.daxpay.open.payment.trade.order.result.export;

import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 普通支付业务单导出结果
///
/// 所有金额已换算为元（主币种单位），枚举已翻译为国际化文本，时间已格式化为字符串。
/// 表头注解值为 i18n 词条 key（`export.normal_order.*`），渲染时由 [cn.daxpay.open.platform.common.excel.ExcelHeadI18nHandler]
/// 按当前请求语言翻译，中文文案真相源见 `common-i18n/src/main/resources/i18n/zh-CN/export/normal_order.json`。
@Data
@Accessors(chain = true)
@Schema(title = "普通支付业务单导出")
public class NormalOrderExportResult {

    /// 平台业务单号
    @ExcelProperty("export.normal_order.orderNo")
    @ColumnWidth(24)
    private String orderNo;

    /// 商户号
    @ExcelProperty("export.normal_order.mchNo")
    @ColumnWidth(18)
    private String mchNo;

    /// 商户名称
    @ExcelProperty("export.normal_order.mchName")
    @ColumnWidth(20)
    private String mchName;

    /// 应用号
    @ExcelProperty("export.normal_order.appId")
    @ColumnWidth(18)
    private String appId;

    /// 商户订单号
    @ExcelProperty("export.normal_order.bizOrderNo")
    @ColumnWidth(24)
    private String bizOrderNo;

    /// 订单标题
    @ExcelProperty("export.normal_order.title")
    @ColumnWidth(30)
    private String title;

    /// 业务状态
    @ExcelProperty("export.normal_order.status")
    @ColumnWidth(14)
    private String status;

    /// 支付通道
    @ExcelProperty("export.normal_order.channel")
    @ColumnWidth(14)
    private String channel;

    /// 支付产品
    @ExcelProperty("export.normal_order.product")
    @ColumnWidth(14)
    private String product;

    /// 支付能力
    @ExcelProperty("export.normal_order.capability")
    @ColumnWidth(18)
    private String capability;

    /// 金额(主币种单位, 按币种小数位换算)
    @ExcelProperty("export.normal_order.amount")
    @ColumnWidth(16)
    private String amount;

    /// 币种
    @ExcelProperty("export.normal_order.currency")
    @ColumnWidth(10)
    private String currency;

    /// 通道商户号
    @ExcelProperty("export.normal_order.channelMchNo")
    @ColumnWidth(20)
    private String channelMchNo;

    /// 门店号
    @ExcelProperty("export.normal_order.storeNo")
    @ColumnWidth(14)
    private String storeNo;

    /// 支付成功时间
    @ExcelProperty("export.normal_order.payTime")
    @ColumnWidth(22)
    private String payTime;

    /// 创建时间
    @ExcelProperty("export.normal_order.createTime")
    @ColumnWidth(22)
    private String createTime;
}
