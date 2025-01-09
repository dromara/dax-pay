package org.dromara.daxpay.single.sdk.param.trade.pay;

import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.code.ChannelEnum;
import org.dromara.daxpay.single.sdk.code.PayMethodEnum;
import org.dromara.daxpay.single.sdk.model.trade.pay.PayResultModel;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.param.channel.AlipayParam;
import org.dromara.daxpay.single.sdk.param.channel.UnionPayParam;
import org.dromara.daxpay.single.sdk.param.channel.WechatPayParam;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付参数
 * @author xxm
 * @since 2024/2/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PayParam extends DaxPayRequest<PayResultModel> {

    /** 商户订单号 */
    private String bizOrderNo;

    /** 支付标题 */
    private String title;

    /** 支付描述 */
    private String description;

    /** 是否开启分账, 不传输为不开启 */
    private Boolean allocation;

    /** 是否开启自动分账, 不传输为不开启 */
    private Boolean autoAllocation;

    /** 过期时间 */
    private LocalDateTime expiredTime;

    /**
     * 支付通道编码
     * @see ChannelEnum#getCode()
     */
    private String channel;

    /**
     * 支付方式编码
     * @see PayMethodEnum#getCode()
     */
    private String method;

    /** 支付金额 */
    private BigDecimal amount;

    /**
     * 支付扩展参数, json字符串格式
     * @see AlipayParam
     * @see WechatPayParam
     * @see UnionPayParam
     */
    private String extraParam;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /** 同步跳转地址, 支付完毕后用户浏览器返回到该地址, 不传输跳转到默认地址 */
    private String returnUrl;

    /** 退出地址 */
    private String quitUrl;

    /** 异步通知地址 */
    private String notifyUrl;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/pay";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<PayResultModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<PayResultModel>>() {});
    }
}
