package cn.daxpay.open.payment.device.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 设备状态
///
/// 通用设备状态字典, 适用于云音响/云打印等各类设备。
/// 字典: device_status
@Getter
@RequiredArgsConstructor
public enum DeviceStatusEnum implements I18nSupport {

    /// 未绑定
    UNBOUND("unbound"),
    /// 在线
    ONLINE("online"),
    /// 离线
    OFFLINE("offline"),
    /// 故障
    FAULT("fault");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.device_status";
    }

    /// 根据编码查找
    public static DeviceStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 未找到对应的设备状态: {0}
                .orElseThrow(() -> new DataNotExistException("error.device.speaker.deviceStatusNotFound", code));
    }
}
