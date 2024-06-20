package cn.daxpay.single.sdk.model.transfer;

import cn.daxpay.single.sdk.code.PayChannelEnum;
import cn.daxpay.single.sdk.code.TransferPayeeTypeEnum;
import cn.daxpay.single.sdk.code.TransferStatusEnum;
import cn.daxpay.single.sdk.code.TransferTypeEnum;
import cn.daxpay.single.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 转账订单
 * @author xxm
 * @since 2024/6/20
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TransferOrderModel extends DaxPayResponseModel {

    /** 商户转账号 */
    private String bizTransferNo;

    /** 转账号 */
    private String transferNo;

    /** 通道转账号 */
    private String outTransferNo;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    private String channel;

    /** 转账金额 */
    private Integer amount;

    /** 标题 */
    private String title;

    /** 转账原因/备注 */
    private String reason;

    /**
     * 转账类型, 微信使用
     * @see TransferTypeEnum
     */
    private String transferType;

    /** 付款方 */
    private String payer;

    /**
     * 收款人类型
     * @see TransferPayeeTypeEnum
     */
    private String payeeType;

    /** 收款人账号 */
    private String payeeAccount;

    /** 收款人姓名 */
    private String payeeName;

    /**
     * 状态
     * @see TransferStatusEnum
     */
    private String status;

    /** 成功时间 */
    private LocalDateTime successTime;


    /** 异步通知地址 */
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    private String attach;

    /** 请求时间，时间戳转时间 */
    private LocalDateTime reqTime;

    /** 终端ip */
    private String clientIp;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误原因
     */
    private String errorMsg;
}
