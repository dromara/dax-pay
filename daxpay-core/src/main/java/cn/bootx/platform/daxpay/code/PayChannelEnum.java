package cn.bootx.platform.daxpay.code;

import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    CASH("cash_pay", "现金支付"),
    WALLET("wallet_pay", "钱包支付"),
    VOUCHER("voucher_pay", "储值卡支付");

    /** 支付通道编码 */
    private final String code;

    /** 支付通道名称 */
    private final String name;

    /**
     * 根据字符编码获取
     */
    public static PayChannelEnum findByCode(String code) {
        return Arrays.stream(PayChannelEnum.values())
            .filter(e -> Objects.equals(code, e.getCode()))
            .findFirst()
            .orElseThrow(() -> new PayFailureException("不存在的支付渠道"));
    }

    public static boolean existsByCode(String code) {
        return Arrays.stream(PayChannelEnum.values())
            .anyMatch(payChannelEnum -> Objects.equals(payChannelEnum.getCode(), code));
    }

    /** 支付宝 UA */
    public static final String UA_ALI_PAY = "Alipay";

    /** 微信 UA */
    public static final String UA_WECHAT_PAY = "MicroMessenger";

    /** 异步支付渠道 */
    public static final List<PayChannelEnum> ASYNC_TYPE = Collections.unmodifiableList(Arrays.asList(ALI, WECHAT, UNION_PAY));

    public static final List<String> ASYNC_TYPE_CODE = Collections.unmodifiableList(Arrays.asList(ALI.code, WECHAT.code, UNION_PAY.code));

}
