package cn.bootx.platform.daxpay.service.code;

import cn.bootx.platform.daxpay.code.PayMethodEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 钱包支付方式
 * @author xxm
 * @since 2024/2/17
 */
@UtilityClass
public class WalletPayWay {

    // 支付方式
    private static final List<PayMethodEnum> PAY_WAYS = Collections.singletonList(PayMethodEnum.NORMAL);

    /**
     * 根据编码获取
     */
    public PayMethodEnum findByCode(String code) {
        return PAY_WAYS.stream()
                .filter(e -> Objects.equals(code, e.getCode()))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("不存在的支付方式"));
    }

    /**
     * 获取支持的支付方式
     */
    public List<PayMethodEnum> getPayWays() {
        return PAY_WAYS;
    }
}
