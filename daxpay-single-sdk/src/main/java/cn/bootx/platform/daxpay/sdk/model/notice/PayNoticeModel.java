package cn.bootx.platform.daxpay.sdk.model.notice;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 支付异步通知类
 * @author xxm
 * @since 2024/1/7
 */
@Getter
@Setter
@ToString
public class PayNoticeModel {

    /** 订单号 */
    private String orderNo;

    /** 商户订单号 */
    private String bizOrderNo;

    /** 标题 */
    private String title;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    private String channel;

    /** 支付方式 */
    private String method;

    /** 支付金额 */
    private Integer amount;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    private String status;

    /** 支付成功时间 */
    private Long payTime;

    /** 支付关闭时间 */
    private Long closeTime;

    /** 支付创建时间 */
    private Long createTime;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /** 签名 */
    private String sign;

}
