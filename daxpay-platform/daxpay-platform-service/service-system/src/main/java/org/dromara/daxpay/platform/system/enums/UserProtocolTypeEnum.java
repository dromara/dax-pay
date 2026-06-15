package org.dromara.daxpay.platform.system.enums;

import org.dromara.daxpay.platform.core.exception.BizException;
import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// 用户协议类型
@Getter
@RequiredArgsConstructor
public enum UserProtocolTypeEnum implements I18nSupport {

    /// 用户协议
    USER_AGREEMENT("USER_AGREEMENT"),
    /// 隐私政策
    PRIVACY_POLICY("PRIVACY_POLICY"),
    /// 第三方信息共享清单
    THIRD_PARTY_INFO("THIRD_PARTY_INFO"),
    /// 未成年人隐私说明
    CHILDREN_POLICY("CHILDREN_POLICY");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.user_protocol_type";
    }

    public static UserProtocolTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equalsIgnoreCase(code))
                .findFirst()
                // 通用: 未知的用户协议类型
                .orElseThrow(() -> new BizException("error.common.enumUnknown", "UserProtocolType"));
    }
}
