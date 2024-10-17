package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.UnsupportedAbilityException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 转账接收方类型
 * 字典: transfer_payee_type
 * @author xxm
 * @since 2024/4/1
 */
@Getter
@AllArgsConstructor
public enum TransferPayeeTypeEnum {
    /** userId  */
    USER_ID("user_id", "用户ID"),
    /** openId  */
    OPEN_ID("open_id", "OpenId"),
    /** 用户账号 */
    LOGIN_NAME("login_name", "用户账号");

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;

    /**
     * 根据编码查找
     */
    public static TransferPayeeTypeEnum findByCode(String code) {
        return Arrays.stream(TransferPayeeTypeEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new UnsupportedAbilityException("未找到对应的转账接收方类型"));
    }
}
