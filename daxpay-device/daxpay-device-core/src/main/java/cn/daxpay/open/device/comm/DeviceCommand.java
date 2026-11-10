package cn.daxpay.open.device.comm;

import cn.daxpay.open.device.enums.DeviceTypeEnum;
import cn.daxpay.open.device.enums.DeviceVendorEnum;
import cn.daxpay.open.device.print.PrintTerminal;
import cn.daxpay.open.device.virtual.entity.DeviceVirtual;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 设备指令(设备引用 + 厂商中立载荷)
///
/// 对虚拟设备推送 client 的唯一沟通形态: "对哪台设备、下什么指令、业务事实是什么",
/// 调用侧不感知协议实现(渲染/加密/传输)。设备引用只承载指令执行所需的最小设备面
/// (标识/厂商/形态/凭证/打印面), 台账实体不出通用层。
/// 开源版仅虚拟设备一条产品线, 工厂方法只保留 virtual 形态
/// (paySuccess 播报打印一体 / test 试播; 纯打印 print.ticket 与踢出 kick 不走本指令,
/// 由 [cn.daxpay.open.device.virtual.client.service.VirtualDevicePushClient] 直发)。
/// 异常策略由下发通道决定: 支付旁路吞异常, 试播试打等同步运营操作异常上抛。
@Data
@Accessors(chain = true)
public class DeviceCommand {

    /// 指令类型
    private Type type;

    /// 设备号
    private String deviceNo;

    /// 设备厂商码(路由依据, 恒 virtual)
    private String vendor;

    /// 设备形态(恒 VIRTUAL)
    private DeviceTypeEnum deviceType;

    /// 激活凭证(虚拟设备每设备 secret; 未激活为空)
    private String secret;

    /// 业务事实载荷(试播纯语音指令可空)
    private DeviceNotifyPayload payload;

    /// 打印面(纸宽/别名; 小票渲染用, 无打印语义的指令可空)
    private PrintTerminal terminal;

    /// 是否要求附带小票(虚拟设备播报打印一体, 恒附带)
    private boolean print;

    /// 指令类型(当前仅支付成功播报与试播两类; 踢出属虚拟设备域内部凭证生命周期, 不走本指令)
    public enum Type {

        /// 支付成功播报(虚拟设备播报打印一体, 附带小票)
        PAY_SUCCESS,

        /// 试播/试打(运营端端到端验证, 样例交易)
        TEST
    }

    /// 虚拟设备支付成功(播报打印一体: 恒附带小票)
    ///
    /// 虚拟设备为独立产品线([DeviceTypeEnum#VIRTUAL]), 厂商恒为内置 virtual, 无厂商配置行。
    public static DeviceCommand paySuccess(DeviceVirtual device, DeviceNotifyPayload payload) {
        return base(device.getDeviceNo(), DeviceVendorEnum.VIRTUAL.getCode(), DeviceTypeEnum.VIRTUAL, device.getSecret())
                .setType(Type.PAY_SUCCESS)
                .setPayload(payload)
                .setTerminal(device)
                .setPrint(true);
    }

    /// 虚拟设备试播(固定金额语音验证, 无小票渲染)
    public static DeviceCommand test(DeviceVirtual device) {
        return base(device.getDeviceNo(), DeviceVendorEnum.VIRTUAL.getCode(), DeviceTypeEnum.VIRTUAL, device.getSecret())
                .setType(Type.TEST)
                .setTerminal(device);
    }

    private static DeviceCommand base(String deviceNo, String vendor, DeviceTypeEnum deviceType, String secret) {
        return new DeviceCommand()
                .setDeviceNo(deviceNo)
                .setVendor(vendor)
                .setDeviceType(deviceType)
                .setSecret(secret);
    }
}
