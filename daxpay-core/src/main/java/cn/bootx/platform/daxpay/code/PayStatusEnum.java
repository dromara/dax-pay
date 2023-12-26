package cn.bootx.platform.daxpay.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 支付状态
 * @author xxm
 * @since 2023/12/17
 */
@Getter
@RequiredArgsConstructor
public enum PayStatusEnum {
    UNKNOWN("unknown","未知状态"),
    PROGRESS("progress","支付中"),
    SUCCESS("success","成功"),
    FAIL("fail","失败"),
    CANCEL("cancel","支付取消"),
    CLOSE("close","支付关闭"),
    /** 超时取消 */
    TIMEOUT("timeout","超时取消"),
    PARTIAL_REFUND("partial_refund","部分退款"),
    REFUNDED("REFUNDED","全部退款");

    /** 编码 */
    private final String code;

    /** 名称 */
    private final String name;

}
