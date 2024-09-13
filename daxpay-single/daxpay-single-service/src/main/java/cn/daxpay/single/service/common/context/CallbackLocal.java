package cn.daxpay.single.service.common.context;

import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.service.code.PayCallbackStatusEnum;
import cn.daxpay.single.service.code.TradeTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 回调信息上下文 针对支付和退款的回调
 * @author xxm
 * @since 2024/1/24
 */
@Data
@Accessors(chain = true)
public class CallbackLocal {

    /** 回调参数内容 */
    private Map<String, String> callbackParam = new HashMap<>();

    /** 交易号 */
    private String tradeNo;

    /**
     * 通道交易号
     */
    private String outTradeNo;

    /**
     * 通道
     */
    private String channel;

    /**
     * 三方支付系统返回状态
     * @see PayStatusEnum 支付状态
     * @see RefundStatusEnum 退款状态
     */
    private String outStatus;

    /** 金额(元) */
    private String amount;

    /** 完成时间(支付/退款) */
    private LocalDateTime finishTime;

    /** 回调类型 */
    private TradeTypeEnum callbackType;

    /**
     * 回调处理状态
     * @see PayCallbackStatusEnum
     */
    private PayCallbackStatusEnum callbackStatus = PayCallbackStatusEnum.SUCCESS;

    /** 提示信息 */
    private String errorMsg;
}
