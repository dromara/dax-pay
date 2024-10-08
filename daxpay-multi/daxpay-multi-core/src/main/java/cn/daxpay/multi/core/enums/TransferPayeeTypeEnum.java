package cn.daxpay.multi.core.enums;

import cn.daxpay.multi.core.exception.UnsupportedAbilityException;
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
    /** 微信 个人 */
    WX_PERSONAL("wx_personal","openid", "OpenId(微信)"),
    /** 支付宝 userId 以2088开头的纯16位数字 */
    ALI_USER_ID("ali_user_id","ALIPAY_USER_ID", "用户ID(支付宝)"),
    /** 支付宝 openId  */
    ALI_OPEN_ID("ali_open_id","ALIPAY_OPEN_ID", "OpenId(支付宝)"),
    /** 支付宝 账号 支持邮箱和手机号格式 */
    ALI_LOGIN_NAME("ali_login_name","ALIPAY_LOGON_ID", "账号(支付宝)");

    /** 编码 */
    private final String code;
    /** 外部编码, 三方支付系统使用的编码 */
    private final String outCode;
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
