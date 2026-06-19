package cn.daxpay.open.platform.core.enums.pay.route;

import cn.daxpay.open.platform.core.exception.business.UnsupportedAbilityException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 通道路由模式枚举
///
/// 字典: pay_route_mode
@Getter
@RequiredArgsConstructor
public enum PayRouteModeEnum implements I18nSupport {

    /// 基础模式
    BASIC("basic"),
    /// 场景模式
    SCENE("scene"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.pay_route_mode";
    }

    /// 按编码解析路由模式，不存在则抛出 UnsupportedAbilityException
    public static PayRouteModeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(o -> Objects.equals(o.getCode(), code))
                .findFirst()
                // 路由模式不存在: {0}
                .orElseThrow(() -> new UnsupportedAbilityException("pay.route.error.routeModeNotExist", code));
    }
}
