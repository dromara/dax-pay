package cn.daxpay.single.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 转账类型 微信使用
 * @author xxm
 * @since 2024/6/6
 */
@Getter
@AllArgsConstructor
public enum TransferTypeEnum {
    /** 转账给用户 */
    USER("user", "转账给用户"),
    /** 转账给员工 */
    EMPLOYEE("employee", "转账给员工"),
    /** 转账给合作伙 */
    PARTNER("partner", "转账给合作伙"),
    /** 转账给其他对象 */
    OTHER("other", "转账给其他对象"),;

    private final String code;
    private final String name;

}
