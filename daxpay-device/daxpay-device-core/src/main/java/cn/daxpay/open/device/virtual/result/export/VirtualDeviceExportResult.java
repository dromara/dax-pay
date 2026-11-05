package cn.daxpay.open.device.virtual.result.export;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

/// # 虚拟设备导出结果
///
/// 布尔状态(激活/在线/启用)已翻译为国际化文本，时间已按当前用户时区格式化为字符串。
/// 表头注解值为 i18n 词条 key(`export.virtual_device.*`)，渲染时由
/// [cn.daxpay.open.platform.common.excel.ExcelHeadI18nHandler] 按当前请求语言翻译，
/// 中文文案真相源见 `common-i18n/src/main/resources/i18n/zh-CN/export/virtual_device.json`。
/// 台账含激活凭证(secret)，导出列**严禁**携带任何凭证字段。
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备导出")
public class VirtualDeviceExportResult {

    /// 设备号
    @ExcelProperty("export.virtual_device.deviceNo")
    @ColumnWidth(24)
    private String deviceNo;

    /// 设备名称
    @ExcelProperty("export.virtual_device.name")
    @ColumnWidth(20)
    private String name;

    /// 批次号
    @ExcelProperty("export.virtual_device.batchNo")
    @ColumnWidth(18)
    private String batchNo;

    /// 归属商户号
    @ExcelProperty("export.virtual_device.mchNo")
    @ColumnWidth(18)
    private String mchNo;

    /// 商户名称
    @ExcelProperty("export.virtual_device.mchName")
    @ColumnWidth(20)
    private String mchName;

    /// 关联应用号
    @ExcelProperty("export.virtual_device.appId")
    @ColumnWidth(18)
    private String appId;

    /// 应用名称
    @ExcelProperty("export.virtual_device.appName")
    @ColumnWidth(20)
    private String appName;

    /// 门店号
    @ExcelProperty("export.virtual_device.storeNo")
    @ColumnWidth(14)
    private String storeNo;

    /// 门店名称
    @ExcelProperty("export.virtual_device.storeName")
    @ColumnWidth(20)
    private String storeName;

    /// 激活状态(已激活/未激活)
    @ExcelProperty("export.virtual_device.activated")
    @ColumnWidth(12)
    private String activated;

    /// 在线状态(在线/离线)
    @ExcelProperty("export.virtual_device.online")
    @ColumnWidth(12)
    private String online;

    /// 启用状态(启用/禁用)
    @ExcelProperty("export.virtual_device.enable")
    @ColumnWidth(12)
    private String enable;

    /// 最后在线时间
    @ExcelProperty("export.virtual_device.lastOnlineTime")
    @ColumnWidth(22)
    private String lastOnlineTime;

    /// 创建时间
    @ExcelProperty("export.virtual_device.createTime")
    @ColumnWidth(22)
    private String createTime;
}
