package cn.daxpay.single.code;

import cn.daxpay.single.exception.pay.PayFailureException;
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

    ALI("ali_pay", "支付宝"),
    WECHAT("wechat_pay", "微信支付"),
    UNION_PAY("union_pay", "云闪付"),
    WALLET("wallet_pay", "钱包支付"),
    ;

    /** 支付通道编码 */
    private final String code;

    /** 支付通道名称 */
    private final String name;

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
