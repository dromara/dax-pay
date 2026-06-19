package cn.daxpay.open.platform.core.enums.pay.config;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 支付环境枚举
///
/// 用于区分生产环境和沙箱环境
@Getter
@RequiredArgsConstructor
public enum PayEnvEnum implements I18nSupport {

    /// 生产环境
    PROD("prod"),
    /// 沙箱环境
    SANDBOX("sandbox"),
    /// 未启用
    NONE("none"),
    ;

    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.pay_env";
    }

    /// 根据编码获取枚举
    public static PayEnvEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                // 通用: 支付环境不存在
                .orElseThrow(() -> new DataNotExistException("error.common.payEnvNotExist"));
    }
}
