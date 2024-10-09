package org.dromara.daxpay.unisdk.common.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单信息
 *
 * @author egan
 * <pre>
 *      email egzosn@gmail.com
 *      date 2016/10/19 22:34
 *  </pre>
 */
@Setter
@Getter
public class UniOrder extends AssistOrder {
    /**
     * 商品名称
     */
    private String subject;
    /**
     * 商品描述
     */
    private String body;
    /**
     * 附加信息
     */
    private String addition;
    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 银行卡类型
     */
    private String bankType;
    /**
     * 付款条码串,人脸凭证，有关支付代码相关的，
     */
    private String authCode;
    /**
     * 用户唯一标识
     * 微信含 sub_openid 字段
     * 支付宝 buyer_id
     */
    private String openid;

    /**
     * 支付币种
     */
    private CurType curType;
    /**
     * 订单过期时间
     */
    private Date expirationTime;


}
