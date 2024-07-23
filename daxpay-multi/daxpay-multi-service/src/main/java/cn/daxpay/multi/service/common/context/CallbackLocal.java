package cn.daxpay.multi.service.common.context;

import cn.daxpay.multi.core.enums.PayStatusEnum;
import cn.daxpay.multi.core.enums.RefundStatusEnum;
import cn.daxpay.multi.core.enums.CallbackStatusEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
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
     * 三方支付系统返回状态
     * @see PayStatusEnum 支付状态
     * @see RefundStatusEnum 退款状态
     */
    private String outStatus;

    /** 金额(元) */
    private BigDecimal amount;

    /** 完成时间(支付/退款) */
    private LocalDateTime finishTime;


    /** 回调信息错误码 */
    private String errorCode;

    /** 回调信息错误信息 */
    private String errorMsg;

    /**
     * 回调处理状态
     * @see CallbackStatusEnum
     */
    private CallbackStatusEnum callbackStatus = CallbackStatusEnum.SUCCESS;
}
