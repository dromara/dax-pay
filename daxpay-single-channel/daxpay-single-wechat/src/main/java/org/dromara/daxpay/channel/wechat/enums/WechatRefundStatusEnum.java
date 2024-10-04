package org.dromara.daxpay.channel.wechat.enums;

import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 微信退款状态
 * @see org.dromara.daxpay.core.enums.RefundStatusEnum
 * @author xxm
 * @since 2024/7/21
 */
@Getter
@AllArgsConstructor
public enum WechatRefundStatusEnum {

    //SUCCESS：退款成功
    SUCCESS("SUCCESS", RefundStatusEnum.SUCCESS),
    //CLOSED：退款关闭
    CLOSED("CLOSED", RefundStatusEnum.CLOSE),
    //PROCESSING：退款处理中
    PROCESSING("PROCESSING", RefundStatusEnum.PROGRESS),
    //ABNORMAL：退款异常
    ABNORMAL("ABNORMAL", RefundStatusEnum.FAIL);

    /** 编码 */
    private final String code;
    /** 映射到标准退款方式 */
    private final RefundStatusEnum refundStatus;

    /**
     * 根据编码获取枚举
     */
    public static WechatRefundStatusEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(o -> Objects.equals(o.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("该微信退款状态不存在"));
    }

}
