package cn.bootx.platform.daxpay.code;

import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付通道枚举
 *
 * @author xxm
 * @since 2021/7/26
 */
@Getter
@RequiredArgsConstructor
public enum PayChannelEnum {

    ALI("ali_pay", "支付宝", "ali"),
    WECHAT("wechat_pay", "微信支付", "wx"),
    UNION_PAY("union_pay", "云闪付", "uni"),
    WALLET("wallet_pay", "钱包支付",null),
    ;

    /** 支付通道编码 */
    private final String code;

    /** 支付通道名称 */
    private final String name;

    /** 对账前缀 */
    private final String reconcilePrefix;

    /**
     * 根据字符编码获取
     */
    public static PayChannelEnum findByCode(String code) {
        return Arrays.stream(values())
            .filter(e -> Objects.equals(code, e.getCode()))
            .findFirst()
            .orElseThrow(() -> new PayFailureException("不存在的支付通道"));
    }

}
