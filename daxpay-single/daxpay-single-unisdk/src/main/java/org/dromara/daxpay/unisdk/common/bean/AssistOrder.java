package org.dromara.daxpay.unisdk.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 辅助订单实体
 *
 * @author egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public class AssistOrder implements Order {


    /**
     * 支付平台订单号,交易号, 平台批次单号
     * -- GETTER --
     *  支付平台订单号,交易号
     *
     *
     * -- SETTER --
     *  支付平台订单号,交易号
     *
     @return 支付平台订单号, 交易号
      * @param tradeNo 支付平台订单号,交易号

     */
    @Setter
    @Getter
    private String tradeNo;
    /**
     * 商户订单号,商家批次单号
     * -- GETTER --
     *  获取商户订单号,商家批次单号
     *
     *
     * -- SETTER --
     *  设置商户订单号,商家批次单号
     *
     @return 商户订单号, 商家批次单号
      * @param outTradeNo 商户订单号,商家批次单号

     */
    @Setter
    @Getter
    private String outTradeNo;
    /**
     * 交易类型
     */
    @Setter
    @Getter
    private TransactionType transactionType;

    /**
     * 异步回调通知
     */
    @Setter
    @Getter
    private String notifyUrl;

    /**
     * 订单附加信息，可用于预设未提供的参数，这里会覆盖以上所有的订单信息，
     */
    @JSONField(serialize = false)
    private volatile Map<String, Object> attr;

    public AssistOrder() {
    }

    public AssistOrder(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public AssistOrder(String tradeNo, String outTradeNo) {
        this.tradeNo = tradeNo;
        this.outTradeNo = outTradeNo;
    }

    public AssistOrder(String tradeNo, TransactionType transactionType) {
        this.tradeNo = tradeNo;
        this.transactionType = transactionType;
    }


    @Override
    public Map<String, Object> getAttrs() {
        if (null == attr) {
            attr = new HashMap<>();
        }
        return attr;
    }

    @Override
    public Object getAttr(String key) {
        return getAttrs().get(key);
    }


    /**
     * 添加订单信息
     *
     * @param key   key
     * @param value 值
     */
    @Override
    public void addAttr(String key, Object value) {
        getAttrs().put(key, value);
    }

}
