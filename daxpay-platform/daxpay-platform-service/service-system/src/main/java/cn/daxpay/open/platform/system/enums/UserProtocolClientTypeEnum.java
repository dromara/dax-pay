package cn.daxpay.open.platform.system.enums;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// 用户协议端类型
@Getter
@RequiredArgsConstructor
public enum UserProtocolClientTypeEnum implements I18nSupport {

    /// Web端
    WEB("WEB"),
    /// App端
    APP("APP"),
    /// 小程序端
    MINIAPP("MINIAPP");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.user_protocol_client_type";
    }

    public static UserProtocolClientTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equalsIgnoreCase(code))
                .findFirst()
                // 通用: 未知的用户协议端类型
                .orElseThrow(() -> new BizException("error.common.enumUnknown", "UserProtocolClientType"));
    }
}
