package cn.bootx.platform.daxpay.code.paymodel;

import cn.bootx.platform.daxpay.code.pay.PayWayEnum;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 支付宝支付方式
 *
 * @author xxm
 * @date 2021/7/2
 */
@UtilityClass
public class AliPayWay {

    // 支付方式
    private static final List<PayWayEnum> PAY_WAYS = Arrays.asList(PayWayEnum.WAP, PayWayEnum.APP, PayWayEnum.WEB,
            PayWayEnum.QRCODE, PayWayEnum.BARCODE);

    /**
     * 根据数字编号获取
     */
    public PayWayEnum findByNo(int no) {
        return PAY_WAYS.stream()
            .filter(e -> e.getNo() == no)
            .findFirst()
            .orElseThrow(() -> new PayFailureException("不存在的支付方式"));
    }

    /**
     * 根据数字编号获取
     */
    public PayWayEnum findByCode(String code) {
        return PAY_WAYS.stream()
            .filter(e -> Objects.equals(code, e.getCode()))
            .findFirst()
            .orElseThrow(() -> new PayFailureException("不存在的支付方式"));
    }

    /**
     * 获取支持的支付方式
     */
    public List<PayWayEnum> getPayWays() {
        return PAY_WAYS;
    }

}
