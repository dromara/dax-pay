package cn.daxpay.open.payment.merchant.result.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

/// # 支付码牌导出结果(商户端)
///
/// 商户端导出全为本人数据, 不含商户号/商户名称列(冗余), 与收款设备导出(商户端)口径一致;
/// 应用号/门店号数值列同样省略, 只保留翻译后的应用名称/门店名称。
/// 固定金额已换算为元(两位小数)，落地程序/金额类型/状态已翻译为国际化文本，
/// 是否分账已翻译为是/否，时间已按当前用户时区格式化为字符串。
/// 表头注解值为 i18n 词条 key(`export.qrcode.*`)，渲染时由
/// [cn.daxpay.open.platform.common.excel.ExcelHeadI18nHandler] 按当前请求语言翻译，
/// 中文文案真相源见 `common-i18n/src/main/resources/i18n/zh-CN/export/qrcode.json`。
@Data
@Accessors(chain = true)
@Schema(title = "支付码牌导出(商户端)")
public class MchDeviceQrCodeExportResult {

    /// 码牌编码
    @ExcelProperty("export.qrcode.code")
    @ColumnWidth(22)
    private String code;

    /// 码牌名称
    @ExcelProperty("export.qrcode.name")
    @ColumnWidth(20)
    private String name;

    /// 批次号
    @ExcelProperty("export.qrcode.batchNo")
    @ColumnWidth(18)
    private String batchNo;

    /// 应用名称(未绑定为空)
    @ExcelProperty("export.qrcode.appName")
    @ColumnWidth(20)
    private String appName;

    /// 门店名称(未绑定为空)
    @ExcelProperty("export.qrcode.storeName")
    @ColumnWidth(20)
    private String storeName;

    /// 落地程序(h5/mini_app 已翻译为文本)
    @ExcelProperty("export.qrcode.programType")
    @ColumnWidth(12)
    private String programType;

    /// 金额类型(random/fixed 已翻译为文本)
    @ExcelProperty("export.qrcode.amountType")
    @ColumnWidth(12)
    private String amountType;

    /// 固定金额(元, 非固定金额为空)
    @ExcelProperty("export.qrcode.fixedAmount")
    @ColumnWidth(14)
    private String fixedAmount;

    /// 状态(enabled/disabled 已翻译为文本)
    @ExcelProperty("export.qrcode.status")
    @ColumnWidth(12)
    private String status;

    /// 是否分账(是/否)
    @ExcelProperty("export.qrcode.allocation")
    @ColumnWidth(12)
    private String allocation;

    /// 备注
    @ExcelProperty("export.qrcode.remark")
    @ColumnWidth(24)
    private String remark;

    /// 创建时间
    @ExcelProperty("export.qrcode.createTime")
    @ColumnWidth(22)
    private String createTime;
}
