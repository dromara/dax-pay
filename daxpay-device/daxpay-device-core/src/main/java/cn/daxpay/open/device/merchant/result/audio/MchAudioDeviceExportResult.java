package cn.daxpay.open.device.merchant.result.audio;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

/// # 收款设备导出结果(商户端)
///
/// 商户端导出全为本人数据, 不含商户号/商户名称/应用号/门店号列(冗余);
/// 布尔状态(激活/在线/启用)已翻译为国际化文本，时间已按当前用户时区格式化为字符串。
/// 表头注解值为 i18n 词条 key(`export.audio_device.*`)，渲染时由
/// [cn.daxpay.open.platform.common.excel.ExcelHeadI18nHandler] 按当前请求语言翻译，
/// 中文文案真相源见 `common-i18n/src/main/resources/i18n/zh-CN/export/audio_device.json`。
/// 台账含激活凭证(secret)，导出列**严禁**携带任何凭证字段。
@Data
@Accessors(chain = true)
@Schema(title = "收款设备导出(商户端)")
public class MchAudioDeviceExportResult {

    /// 设备号
    @ExcelProperty("export.audio_device.deviceNo")
    @ColumnWidth(24)
    private String deviceNo;

    /// 设备名称
    @ExcelProperty("export.audio_device.name")
    @ColumnWidth(20)
    private String name;

    /// 批次号
    @ExcelProperty("export.audio_device.batchNo")
    @ColumnWidth(18)
    private String batchNo;

    /// 应用名称
    @ExcelProperty("export.audio_device.appName")
    @ColumnWidth(20)
    private String appName;

    /// 门店名称
    @ExcelProperty("export.audio_device.storeName")
    @ColumnWidth(20)
    private String storeName;

    /// 激活状态(已激活/未激活)
    @ExcelProperty("export.audio_device.activated")
    @ColumnWidth(12)
    private String activated;

    /// 在线状态(在线/离线)
    @ExcelProperty("export.audio_device.online")
    @ColumnWidth(12)
    private String online;

    /// 启用状态(启用/禁用)
    @ExcelProperty("export.audio_device.enable")
    @ColumnWidth(12)
    private String enable;

    /// 最后在线时间
    @ExcelProperty("export.audio_device.lastOnlineTime")
    @ColumnWidth(22)
    private String lastOnlineTime;

    /// 创建时间
    @ExcelProperty("export.audio_device.createTime")
    @ColumnWidth(22)
    private String createTime;
}
