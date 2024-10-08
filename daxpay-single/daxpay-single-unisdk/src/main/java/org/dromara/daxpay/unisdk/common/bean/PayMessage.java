package org.dromara.daxpay.unisdk.common.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付回调消息
 * 基础实现，具体可根据具体支付回调的消息去实现
 *
 * @author egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2017/3/7 16:37
 *  </pre>
 */
@Getter
public class PayMessage implements Serializable {
    private Map<String, Object> payMessage = null;
    @Setter
    private String payType;
    @Setter
    private String transactionType;
    @Setter
    private String fromPay;
    @Setter
    private String describe;


    public PayMessage() {
    }

    public PayMessage(Map<String, Object> payMessage) {
        this.payMessage = payMessage;
    }

    public PayMessage(Map<String, Object> payMessage, String payType) {
        this.payMessage = payMessage;
        this.payType = payType;
    }


    public PayMessage(Map<String, Object> payMessage, String payType, String transactionType) {
        this.payMessage = payMessage;
        this.payType = payType;
        this.transactionType = transactionType;
    }

    protected void setPayMessage(Map<String, Object> payMessage) {
        this.payMessage = payMessage;
    }


    public String getDiscount() {
        return (String) payMessage.get("discount");
    }

    public String getSubject() {
        return (String) payMessage.get("subject");
    }


    /////////微信与支付宝共用
    public String getOutTradeNo() {
        return (String) payMessage.get("out_trade_no");
    }

    public String getSign() {
        return (String) payMessage.get("sign");
    }

    public Number getTotalFee() {
        String totalFee = (String) payMessage.get("total_fee");
        if (null == totalFee || totalFee.isEmpty()) {
            return 0;
        }
        if (isNumber(totalFee)) {
            return new BigDecimal(totalFee);
        }
        return 0;
    }

    /////////微信与支付宝共用


    public boolean isNumber(String str) {
        return str.matches("^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$");
    }


    @Override
    public String toString() {
        return payMessage.toString();
    }


}
