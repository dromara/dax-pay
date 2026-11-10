package cn.daxpay.open.device.print;

/// # 打印终端设备契约
///
/// 虚拟设备的打印相关字段面, 小票渲染管线(内置版式渲染 ESC/POS, 58/80 双流)按本接口取设备信息,
/// 台账实体([cn.daxpay.open.device.virtual.entity.DeviceVirtual])实现本接口。
/// 虚拟设备无别名字段, 默认无别名。
public interface PrintTerminal {

    /// 设备号(渲染变量 deviceName 兜底与日志定位)
    String getDeviceNo();

    /// 设备名称
    String getName();

    /// 设备别名(无别名的设备类型默认空)
    default String getAlias() {
        return null;
    }

    /// 设备纸宽(58/80, mm)
    Integer getPaperWidth();

    /// 归属商户号(模板归属校验用; 平台库存态为 null)
    default String getMchNo() {
        return null;
    }
}
