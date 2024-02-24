package cn.bootx.platform.daxpay.sdk.model.notice;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 支付异步通知类
 * @author xxm
 * @since 2024/1/7
 */
@Getter
@Setter
@ToString
public class PayNoticeModel {

    /** 支付ID */
    private Long paymentId;

    /** 业务号 */
    private String businessNo;

    /** 是否是异步支付 */
    private boolean asyncPay;

    /**
     * 异步支付通道
     * @see PayChannelEnum
     */
    private String asyncChannel;

    /** 支付金额 */
    private Integer amount;

    /** 支付通道信息 */
    private List<PayChannelModel> payChannels;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    private String status;

    /** 支付创建时间 */
    private Long createTime;

    /** 支付成功时间 */
    private Long payTime;

    /** 支付关闭时间 */
    private Long closeTime;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /** 签名 */
    private String sign;

}
