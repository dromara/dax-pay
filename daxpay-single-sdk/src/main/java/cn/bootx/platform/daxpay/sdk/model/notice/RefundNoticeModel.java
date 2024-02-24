package cn.bootx.platform.daxpay.sdk.model.notice;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.RefundStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 退款通知消息
 * @author xxm
 * @since 2024/2/22
 */
@Getter
@Setter
@ToString
public class RefundNoticeModel {

    /** 退款ID */
    private Long refundId;

    /** 退款号 */
    private String refundNo;

    /** 是否含有异步通道 */
    private boolean asyncPay;

    /**
     * 异步通道
     * @see PayChannelEnum
     */
    private String asyncChannel;

    /** 退款金额 */
    private Integer amount;

    /** 退款通道信息 */
    private List<RefundChannelModel> refundChannels;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    private String status;

    /** 退款成功时间 */
    private Long refundTime;

    /** 退款创建时间 */
    private Long createTime;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /** 签名 */
    private String sign;
}
