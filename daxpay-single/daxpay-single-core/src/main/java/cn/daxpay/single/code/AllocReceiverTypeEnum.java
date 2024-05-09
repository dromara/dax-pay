package cn.daxpay.single.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 分账接收方类型
 * @author xxm
 * @since 2024/4/1
 */
@Getter
@AllArgsConstructor
public enum AllocReceiverTypeEnum {
    /** 个人 */
    WX_PERSONAL("wx_personal","PERSONAL_OPENID", "个人"),
    /** 商户 */
    WX_MERCHANT("wx_merchant","MERCHANT_ID", "商户"),

    /** userId 以2088开头的纯16位数字 */
    ALI_USER_ID("ali_user_id","userId", "用户ID"),
    /** openId  */
    ALI_OPEN_ID("ali_open_id","openId", "openId"),
    /** 账号 */
    ALI_LOGIN_NAME("ali_login_name","loginName", "账号");

    /** 编码 */
    private final String code;
    /** 外部编码, 三方支付系统使用的编码 */
    private final String outCode;
    /** 名称 */
    private final String name;

    /**
     * 根据编码查找
     */
    public static AllocReceiverTypeEnum findByCode(String code) {
        return Arrays.stream(AllocReceiverTypeEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未找到对应的分账接收方类型"));
    }

    /** 微信支持类型 */
    public static final List<AllocReceiverTypeEnum> WECHAT_LIST = Collections.unmodifiableList(Arrays.asList(WX_PERSONAL, WX_MERCHANT));
    /** 支付宝支持类型 */
    public static final List<AllocReceiverTypeEnum> ALI_LIST = Collections.unmodifiableList(Arrays.asList(ALI_OPEN_ID, ALI_USER_ID, ALI_LOGIN_NAME));

}
