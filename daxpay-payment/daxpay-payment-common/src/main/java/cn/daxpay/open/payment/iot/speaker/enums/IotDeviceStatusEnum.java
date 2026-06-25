package cn.daxpay.open.payment.iot.speaker.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 云音响设备状态
///
/// 字典: iot_speaker_device_status
@Getter
@RequiredArgsConstructor
public enum IotDeviceStatusEnum implements I18nSupport {

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
        return "enum.iot_speaker_device_status";
    }

    /// 根据编码查找
    public static IotDeviceStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 未找到对应的云音响设备状态: {0}
                .orElseThrow(() -> new DataNotExistException("error.iot.speaker.deviceStatusNotFound", code));
    }
}
