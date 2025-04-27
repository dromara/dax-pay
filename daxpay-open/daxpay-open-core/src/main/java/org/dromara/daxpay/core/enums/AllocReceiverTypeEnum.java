package org.dromara.daxpay.core.enums;

import cn.bootx.platform.core.exception.DataNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 分账接收方类型
 * alloc_receiver_type
 * @author xxm
 * @since 2024/4/1
 */
@Getter
@AllArgsConstructor
public enum AllocReceiverTypeEnum {
    /** 商户号 */
    MERCHANT_NO("merchant_no", "商户号"),
    /** userId */
    USER_ID("user_id", "用户ID"),
    /** openId  */
    OPEN_ID("open_id", "openId"),
    /** 账号 */
    LOGIN_NAME("login_name", "登录账号");

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;

    /**
     * 根据编码查找
     */
    public static AllocReceiverTypeEnum findByCode(String code) {
        return Arrays.stream(AllocReceiverTypeEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("未找到对应的分账接收方类型"));
    }

}
