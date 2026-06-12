package org.dromara.daxpay.platform.core.enums.pay.transfer;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.platform.core.exception.business.UnsupportedAbilityException;
import java.util.Arrays;

/// # 转账接收方类型
///
/// 字典: transfer_payee_type
@Getter
@RequiredArgsConstructor
public enum TransferPayeeTypeEnum implements I18nSupport {

    /// userId
    USER_ID("user_id"),
    /// openId
    OPEN_ID("open_id"),
    /// 用户账号
    LOGIN_NAME("login_name");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.transfer_payee_type";
    }
    /// 根据编码查找
    public static TransferPayeeTypeEnum findByCode(String code) {
        return Arrays.stream(TransferPayeeTypeEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 未找到对应的转账接收方类型
                .orElseThrow(() -> new UnsupportedAbilityException("pay.error.transferPayeeTypeNotFound"));
    }

}
