package org.dromara.daxpay.platform.core.enums.client;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import java.util.Optional;

/// # 终端类型枚举
///
@Getter
@RequiredArgsConstructor
public enum ClientEnum implements I18nSupport {

    /// 支付网关
    GATEWAY("gateway"),
    /// 运营端
    ADMIN("admin"),
    /// 商户端
    MERCHANT("merchant");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.client";
    }
    public static Optional<ClientEnum> findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst();
    }

}
