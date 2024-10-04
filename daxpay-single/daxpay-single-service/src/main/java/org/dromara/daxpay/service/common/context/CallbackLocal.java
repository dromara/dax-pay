package org.dromara.daxpay.service.common.context;

import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.core.enums.CallbackStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.enums.TransferStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 回调信息上下文
 * @author xxm
 * @since 2024/1/24
 */
@Data
@Accessors(chain = true)
public class CallbackLocal {


    /**
     * 原始数据, 回调数据内容不存在的时候使用这个
     * 主要处理类似需要二次解密的回调, 如微信转账退款等
     */
    private String rawData;

    /** 回调数据内容 */
    private Map<String, ?> callbackData = new HashMap<>();

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

    /** 交易类型 */
    private TradeTypeEnum callbackType;

    /**
     * 交易状态状态
     * @see PayStatusEnum 支付状态
     * @see RefundStatusEnum 退款状态
     * @see TransferStatusEnum 转账状态
     */
    private String tradeStatus;

    /** 交易错误信息 */
    private String tradeErrorMsg;

    /** 金额(元) */
    private BigDecimal amount;

    /** 完成时间(支付/退款) */
    private LocalDateTime finishTime;


    /** 回调信息错误码 */
    private String callbackErrorCode;

    /** 回调信息错误信息 */
    private String callbackErrorMsg;

    /**
     * 回调处理状态
     * @see CallbackStatusEnum
     */
    private CallbackStatusEnum callbackStatus = CallbackStatusEnum.SUCCESS;
}
