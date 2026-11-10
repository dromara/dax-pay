package cn.daxpay.open.device.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 设备厂商
///
/// 设备厂商标识。开源版仅虚拟设备一条产品线:
/// VIRTUAL 为平台自有虚拟设备(DaxPay 收款播报 APP, 接入类型为平台 MQTT 直连),
/// 凭证为每设备 secret, 无厂商配置行; 实体云音箱/云打印厂商纵深不在开源版范围。
@Getter
@RequiredArgsConstructor
public enum DeviceVendorEnum implements I18nSupport {

    /// 平台虚拟设备(DaxPay 收款播报 APP, 直连平台 Artemis MQTT)
    ///
    /// 播报打印一体机: 台账/生成/激活/推送链路全自持于虚拟设备域(device_virtual 独立台账),
    /// 小票打印搭同一条 pay.success 通道(core 地址 daxpay.device.audio.{deviceNo};
    /// 设备侧订阅斜杠主题 daxpay/device/audio/{deviceNo})。
    VIRTUAL("virtual");

    /// 编码
    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.device_vendor";
    }

    /// 根据编码查找
    public static DeviceVendorEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 设备: 未找到对应的设备厂商
                .orElseThrow(() -> new DataNotExistException("error.device.virtual.vendorNotFound", code));
    }
}
