package cn.bootx.platform.daxpay.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分账接收方类型枚举
 * @author xxm
 * @since 2024/4/1
 */
@Getter
@AllArgsConstructor
public enum AllocationReceiverTypeEnum {
    /** 个人 */
    WX_PERSONAL("PERSONAL_OPENID", "个人"),
    /** 商户 */
    WX_MERCHANT("MERCHANT_ID", "商户"),

    /** userId 以2088开头的纯16位数字 */
    ALI_USER_ID("userId", "账号ID"),
    /** openId  */
    ALI_OPEN_ID("openId", "登录号"),
    /** 账号 */
    ALI_LOGIN_NAME("loginName", "openId");

    private final String code;
    private final String name;

}
