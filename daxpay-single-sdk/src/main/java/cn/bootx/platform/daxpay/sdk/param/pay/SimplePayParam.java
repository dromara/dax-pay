package cn.bootx.platform.daxpay.sdk.param.pay;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import cn.bootx.platform.daxpay.sdk.model.PayOrderModel;
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
    private String businessNo;

    /** 支付标题 */
    private String title;

    /** 支付描述 */
    private String description;

    /** 过期时间 */
    private Long expiredTime;

    /** 用户付款中途退出返回商户网站的地址(部分支付场景中可用) */
    private String quitUrl;

    /**
     * @see PayChannelEnum#getCode()
     */
    private String channel;

    /**
     * 支付方式编码
     * @see PayWayEnum#getCode()
     */
    private String payWay;

    /** 支付金额 */
    private Integer amount;

    /**
     * 附加支付参数
     * @see AliPayParam
     * @see WeChatPayParam
     * @see VoucherPayParam
     * @see WalletPayParam
     */
    private ChannelParam channelParam;

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
