package org.dromara.daxpay.channel.alipay.enums;

import cn.bootx.platform.core.exception.DataNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 *
 * @author xxm
 * @since 2024/11/6
 */
@Getter
@AllArgsConstructor
public enum AlipayOtherContractEnum {

    /** JSAPI 支付 */
    JSAPI("jsapi", "JSAPI","I1080300001000060370"),
    /** 预授权支付 */
    PAY_AUTH("pay_auth", "预授权支付","I1080300001000065324"),
    /** 手机网站支付 */
    WAP("wap", "手机网站支付", "QUICK_WAP_WAY"),
    /** 电脑网站支付 */
    WEB("web", "电脑网站支付", "FAST_INSTANT_TRADE_PAY"),
    /** 订单码支付 */
    ORDER_CODE("order_code", "订单码支付", "I1080300001000068149"),
    /** 商家分账 */
    ALLOCATION("allocation", "商家分账", "DOMESTIC_ALLOC"),
    ;

    private final String code;
    private final String name;
    /** 产品码 */
    private final String productCode;

    public static AlipayOtherContractEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("该产品类型不存在"));
    }
}
