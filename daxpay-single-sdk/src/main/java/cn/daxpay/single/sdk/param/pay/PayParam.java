package cn.daxpay.single.sdk.param.pay;

import cn.daxpay.single.sdk.code.PayChannelEnum;
import cn.daxpay.single.sdk.code.PayMethodEnum;
import cn.daxpay.single.sdk.model.pay.PayModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.param.ChannelParam;
import cn.daxpay.single.sdk.param.channel.AliPayParam;
import cn.daxpay.single.sdk.param.channel.WalletPayParam;
import cn.daxpay.single.sdk.param.channel.WeChatPayParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 支付参数
 * @author xxm
 * @since 2024/2/2
 */
@Getter
@Setter
public class PayParam extends DaxPayRequest<PayModel> {

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
     * @see PayChannelEnum#getCode()
     */
    private String channel;

    /**
     * 支付方式编码
     * @see PayMethodEnum#getCode()
     */
    private String method;

    /** 支付金额 */
    private Integer amount;

    /**
     * 支付扩展参数
     * @see AliPayParam
     * @see WeChatPayParam
     * @see WalletPayParam
     */
    private ChannelParam extraParam;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /** 同步跳转地址, 支付完毕后用户浏览器返回到该地址, 不传输跳转到默认地址 */
    private String returnUrl;

    /** 退出地址 */
    private String quitUrl;

    /** 异步通知地址 */
    private String notifyUrl;

    /** 是否不启用异步通知 */
    private Boolean notNotify;

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
    public DaxPayResult<PayModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<PayModel>>() {}, false);
    }
}
