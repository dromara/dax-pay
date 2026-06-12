package org.dromara.daxpay.payment.pay.entity.order.pay;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchAppBaseEntity;
import org.dromara.daxpay.payment.pay.convert.order.pay.PayOrderConvert;
import org.dromara.daxpay.payment.pay.result.order.pay.PayOrderExpandResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/// # 支付订单扩展存储参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_order_expand",autoResultMap = true)
public class PayOrderExpand extends MchAppBaseEntity implements ToResult<PayOrderExpandResult>{

    /// 同步跳转地址
    private String returnUrl;

    /// 异步通知地址
    private String notifyUrl;

    /// 通道附加参数序列化为Json字符串
    private String extraParam;

    /// 商户扩展参数,回调时会原样返回
    private String attach;

    /// 请求时间
    private LocalDateTime reqTime;

    /// 实收金额
    private BigDecimal realAmount;

    /// 终端设备编码
    private String terminalNo;

    /// 支付终端ip
    private String clientIp;

    /// 付款用户ID
    private String buyerId;

    /// 用户标识 未使用
    private String userId;

    /// 通道方记录的支付产品(支付产品或类型)
    private String tradeProduct;

    /// 通道方记录的交易方式
    private String tradeWay;

    /// 银行卡类型
    /// 借记卡/贷记卡
    private String bankType;

    /// 参加活动类型
    private String promotionType;

    /// 付款码
    private String barCode;

    /// jsapi支付时的OpenId
    private String jsapiOpenId;

    /// 支付通道返回支付参数
    private String payBody;

    /// 支付通道返回支付参数
    private String payBodyType;

    /// 扩展参数存储字段
    private String ext;

    /// 转换
    @Override
    public PayOrderExpandResult toResult() {
        return PayOrderConvert.CONVERT.toResult(this);
    }
}

