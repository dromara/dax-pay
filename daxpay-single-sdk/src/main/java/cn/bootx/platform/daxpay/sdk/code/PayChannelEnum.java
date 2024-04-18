package cn.bootx.platform.daxpay.sdk.code;

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
     * 根据编码获取枚举
     */
    public static PayChannelEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(o -> Objects.equals(o.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("该枚举不存在"));
    }

}
