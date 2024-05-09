package cn.daxpay.single.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付分账明细处理结果
 * @author xxm
 * @since 2024/4/16
 */
@Getter
@AllArgsConstructor
public enum AllocDetailResultEnum {

    PENDING("pending", "待分账"),
    SUCCESS("success", "分账成功"),
    FAIL("fail", "分账失败"),
    ;

    private final String code;
    private final String name;
}
