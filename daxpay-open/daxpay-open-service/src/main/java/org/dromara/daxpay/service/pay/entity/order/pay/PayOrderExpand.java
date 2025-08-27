package org.dromara.daxpay.service.pay.entity.order.pay;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.pay.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.pay.convert.order.pay.PayOrderConvert;
import org.dromara.daxpay.service.pay.result.order.pay.PayOrderExpandResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付订单扩展存储参数
 * @author xxm
 * @since 2025/6/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_order_expand",autoResultMap = true)
public class PayOrderExpand extends MchAppBaseEntity implements ToResult<PayOrderExpandResult>{

    /** 付款用户ID */
    private String buyerId;

    /** 用户标识 */
    private String userId;

    /**
     * 支付产品
     * 三方通道所使用的支付产品或类型
     */
    private String tradeProduct;

    /**
     * 交易方式
     */
    private String tradeWay;

    /**
     * 银行卡类型
     * 借记卡/贷记卡
     */
    private String bankType;

    /**
     * 透传订单号
     * 三方通道使用微信/支付宝/银联支付时产生的订单号
     */
    private String transOrderNo;

    /** 参加活动类型 */
    private String promotionType;

    /** 扩展参数存储字段 */
    private String ext;

    /**
     * 转换
     */
    @Override
    public PayOrderExpandResult toResult() {
        return PayOrderConvert.CONVERT.toResult(this);
    }
}
