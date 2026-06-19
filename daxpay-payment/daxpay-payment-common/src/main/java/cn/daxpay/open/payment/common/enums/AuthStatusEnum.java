package cn.daxpay.open.payment.common.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 预授权状态
///
/// pay_auth 容器的授权状态
/// 字典: auth_status
@Getter
@RequiredArgsConstructor
public enum AuthStatusEnum implements I18nSupport {

    /// 已冻结（授权成功）
    AUTHORIZED("authorized"),
    /// 部分捕获
    PARTIAL_CAPTURED("partial_captured"),
    /// 完全捕获
    CAPTURED("captured"),
    /// 已解冻
    UNFROZEN("unfrozen"),
    /// 授权过期
    EXPIRED("expired"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.auth_status";
    }

    public static AuthStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.authStatusNotExist", code));
    }
}
