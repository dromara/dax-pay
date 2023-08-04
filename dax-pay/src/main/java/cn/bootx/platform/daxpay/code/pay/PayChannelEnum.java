package cn.bootx.platform.daxpay.code.pay;

import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 支付渠道枚举
 *
 * @author xxm
 * @since 2021/7/26
 */
@Getter
@RequiredArgsConstructor
public enum PayChannelEnum {

    ALI("ali_pay", "支付宝"), WECHAT("wechat_pay", "微信支付"), UNION_PAY("union_pay", "云闪付"), CASH("cash_pay", "现金支付"),
    WALLET("wallet_pay", "钱包支付"), VOUCHER("voucher_pay", "储值卡支付"), CREDIT_CARD("credit_pay", "信用卡支付"),
    APPLE_PAY("apple_pay", "苹果支付"), AGGREGATION("aggregation_pay", "聚合支付");

    /** 支付渠道字符编码 */
    private final String code;

    /** 名称 */
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
    public static final List<PayChannelEnum> ASYNC_TYPE = Arrays.asList(ALI, WECHAT, UNION_PAY, APPLE_PAY);

    public static final List<String> ASYNC_TYPE_CODE = Arrays.asList(ALI.code, WECHAT.code, UNION_PAY.code,
            APPLE_PAY.code);

}
