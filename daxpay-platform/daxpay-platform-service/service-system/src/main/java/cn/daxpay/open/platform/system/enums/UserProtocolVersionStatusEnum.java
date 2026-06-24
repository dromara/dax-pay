package cn.daxpay.open.platform.system.enums;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// 用户协议版本状态
@Getter
@RequiredArgsConstructor
public enum UserProtocolVersionStatusEnum implements I18nSupport {

    /// 草稿
    DRAFT("DRAFT"),
    /// 已发布
    PUBLISHED("PUBLISHED"),
    /// 已归档
    ARCHIVED("ARCHIVED");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.user_protocol_status";
    }

    public static UserProtocolVersionStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equalsIgnoreCase(code))
                .findFirst()
                // 通用: 未知的用户协议版本状态
                .orElseThrow(() -> new BizException("error.common.enumUnknown", "UserProtocolVersionStatus"));
    }
}
