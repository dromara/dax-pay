package cn.daxpay.open.payment.device.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 设备厂商
///
/// 设备厂商固定枚举, 管理端以卡片形式展示, 不入库。
/// 字典: device_vendor
@Getter
@RequiredArgsConstructor
public enum DeviceVendorEnum implements I18nSupport {

    /// 商米
    SUNMI("sunmi"),

    /// 智谷联
    ZHIGULIAN("zhigulian"),

    /// 博实结
    BOSHIJIE("boshijie");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.device_vendor";
    }

    /// 根据编码查找
    public static DeviceVendorEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 未找到对应的设备厂商: {0}
                .orElseThrow(() -> new DataNotExistException("error.device.vendor.vendorNotFound", code));
    }
}
