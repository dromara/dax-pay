package org.dromara.daxpay.unisdk.common.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 转账订单
 *
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 * date 2018/1/31
 * </pre>
 */
public class UniTransferOrder implements Order {

    /**
     * 转账批次订单单号
     */
    @Setter
    @Getter
    private String batchNo;

    /**
     * 转账订单单号
     */
    @Setter
    @Getter
    private String outNo;

    /**
     * 收款方账户, 用户openid,卡号等等
     */
    @Setter
    @Getter
    private String payeeAccount;

    /**
     * 转账金额
     */
    @Setter
    @Getter
    private BigDecimal amount;

    /**
     * 付款人名称
     */
    @Setter
    @Getter
    private String payerName;

    /**
     * 收款人名称
     */
    @Setter
    @Getter
    private String payeeName;
    /**
     * 收款人地址
     */
    @Setter
    @Getter
    private String payeeAddress;

    /**
     * 备注
     */
    @Setter
    @Getter
    private String remark;

    /**
     * 收款开户行
     */
    @Setter
    @Getter
    private Bank bank;

    /**
     * 收款开户行地址
     */
    @Setter
    @Getter
    private String payeeBankAddress;

    /**
     * 币种
     */
    @Setter
    @Getter
    private CurType curType;
    /**
     * 国家代码
     */
    @Setter
    @Getter
    private CountryCode countryCode;
    /**
     * 转账类型，收款方账户类型，比如支付宝账户或者银行卡
     */
    @Setter
    @Getter
    private TransferType transferType;

    /**
     * 操作者ip，根据支付平台所需进行设置
     */
    @Setter
    @Getter
    private String ip;


    /**
     * 订单附加信息，可用于预设未提供的参数，这里会覆盖以上所有的订单信息，
     */
    private Map<String, Object> attr;

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
