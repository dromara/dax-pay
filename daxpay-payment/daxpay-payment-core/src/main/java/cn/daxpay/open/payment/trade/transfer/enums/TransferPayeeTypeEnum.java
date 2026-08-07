package cn.daxpay.open.payment.trade.transfer.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 收款人账号类型
///
/// 转账收款人账号类型，按通道差异化使用：
/// - 微信: 仅支持 openid
/// - 支付宝: 支持 user_id / login_name
/// - 抖音: 支持 openid / phone(手机号, 复用收款人账号字段)
/// 字典: transfer_payee_type
@Getter
@RequiredArgsConstructor
public enum TransferPayeeTypeEnum implements I18nSupport {

    /// 微信 openid(微信专属)
    OPENID("openid"),
    /// 支付宝用户ID(2088开头)
    USER_ID("user_id"),
    /// 支付宝开放ID
    OPEN_ID("open_id"),
    /// 支付宝登录账号(手机号/邮箱)
    LOGIN_NAME("login_name"),
    /// 抖音收款手机号(需抖音支付实名一致, 子应用证书加密上送)
    PHONE("phone"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.transfer_payee_type";
    }

    public static TransferPayeeTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.dataNotExist", code));
    }
}
