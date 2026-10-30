package cn.daxpay.open.device.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 设备类型
///
/// 设备产品线标识。开源版仅虚拟设备一条产品线:
/// VIRTUAL(虚拟设备)为独立产品线: 播报打印一体, 台账/生成/激活/推送全链路自持,
/// 内置厂商 virtual 仅归属本类型; 云音箱/云打印形态不在开源版范围。
@Getter
@RequiredArgsConstructor
public enum DeviceTypeEnum implements I18nSupport {

    /// 虚拟设备(播报打印一体, 收款播报 APP)
    VIRTUAL("virtual");

    /// 编码
    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.device_type";
    }
}
