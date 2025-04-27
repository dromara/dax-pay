package org.dromara.daxpay.sdk.result.notice;

import org.dromara.daxpay.sdk.code.ChannelEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分账异步通知类
 * @author xxm
 * @since 2024/5/21
 */
@Data
public class AllocNoticeResult {
    /**
     * 分账单号
     */
    private String allocNo;

    /**
     * 商户分账单号
     */
    private String bizAllocNo;

    /**
     * 通道分账号
     */
    private String outAllocNo;

    /**
     * 支付订单号
     */
    private String orderNo;

    /**
     * 商户支付订单号
     */
    private String bizOrderNo;


    /**
     * 支付订单标题
     */
    private String title;

    /**
     * 所属通道
     * @see ChannelEnum
     */
    private String channel;

    /**
     * 总分账金额
     */
    private BigDecimal amount;

    /**
     * 分账描述
     */
    private String description;

    /**
     * 状态
     * @see AllocOrderStatusEnum
     */
    private String status;

    /**
     * 处理结果
     * @see AllocOrderResultEnum
     */
    private String result;

    /** 分账订单完成时间 */
    private Long finishTime;

    /** 商户扩展参数 */
    private String attach;

    /** 分账明细 */
    private List<AllocDetailNoticeResult> details;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;
}
