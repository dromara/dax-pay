package cn.bootx.platform.daxpay.sdk.param.pay;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import cn.bootx.platform.daxpay.sdk.model.pay.PayOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayRequest;
import cn.bootx.platform.daxpay.sdk.param.ChannelParam;
import cn.bootx.platform.daxpay.sdk.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.sdk.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.sdk.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.sdk.param.channel.WeChatPayParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * 简单支付请求参数
 * @author xxm
 * @since 2024/2/2
 */
@Getter
@Setter
@ToString
public class SimplePayParam extends DaxPayRequest<PayOrderModel> {

    /** 业务号 */
    @NotNull
    private String businessNo;

    /** 支付标题 */
    @NotNull
    private String title;

    /** 支付描述 */
    private String description;

    /** 过期时间 */
    private Long expiredTime;

    /** 用户付款中途退出返回商户网站的地址(部分支付场景中可用) */
    private String quitUrl;

    /**
     * 支付通道
     * @see PayChannelEnum#getCode()
     */
    @NotNull
    private String channel;

    /**
     * 支付方式编码
     * @see PayWayEnum#getCode()
     */
    @NotNull
    private String payWay;

    /** 支付金额 */
    @NotNull
    private Integer amount;

    /**
     * 附加支付参数
     * @see AliPayParam
     * @see WeChatPayParam
     * @see VoucherPayParam
     * @see WalletPayParam
     */
    private ChannelParam channelParam;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /** 是否不进行同步通知的跳转 */
    private boolean notReturn;

    /** 同步跳转URL, 部分接口不支持该配置，传输了也不会生效 */
    private String returnUrl;

    /** 是否不启用异步通知 */
    private boolean notNotify;

    /** 异步通知地址 */
    private String notifyUrl;

    /**
     * 请求路径
     */
    @Override
    public String path() {
        return "/unipay/simplePay";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<PayOrderModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<PayOrderModel>>() {}, false);
    }
}
