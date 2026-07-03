package cn.daxpay.open.payment.common.enums;

import cn.daxpay.open.platform.core.exception.system.StatusNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 退款订单状态
///
/// refund_order 表的退款状态, 标识单笔退款交易的生命周期
/// 字典: refund_order_status
@Getter
@RequiredArgsConstructor
public enum RefundOrderStatusEnum implements I18nSupport {

    /// 初始化(退款单已创建, 尚未调用通道)
    INIT("init"),
    /// 退款中(已调用通道, 等待结果)
    PROGRESS("progress"),
    /// 退款成功
    SUCCESS("success"),
    /// 退款失败
    FAIL("fail"),
    /// 退款关闭(超时未确认等)
    CLOSE("close"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.refund_order_status";
    }

    /// 根据编码获取枚举
    public static RefundOrderStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 该退款订单状态不存在: {0}
                .orElseThrow(() -> new StatusNotExistException("error.common.refundOrderStatusNotExist", code));
    }
}
