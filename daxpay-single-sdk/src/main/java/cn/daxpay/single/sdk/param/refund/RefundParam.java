package cn.daxpay.single.sdk.param.refund;

import cn.daxpay.single.sdk.model.refund.RefundModel;
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
import lombok.ToString;

/**
 * 退款参数，适用于组合支付的订单退款操作中，
 * @author xxm
 * @since 2023/12/18
 */
@Getter
@Setter
@ToString(callSuper = true)
public class RefundParam extends DaxPayRequest<RefundModel> {


    /**
     * 商户退款号，不可以为空，同样的商户退款号多次请求，同一退款单号多次请求只退一笔
     */
    private String bizRefundNo;

    /**
     * 支付订单号，与商户订单号至少要传输一个，同时传输以订单号为准
     */
    private String orderNo;

    /**
     * 商户支付订单号，与订单号至少要传输一个，同时传输以订单号为准
     */
    private String bizOrderNo;

    /** 退款金额 */
    private Integer amount;


    /** 退款原因 */
    private String reason;

    /**
     * 预留的退款扩展参数
     * @see AliPayParam
     * @see WeChatPayParam
     * @see WalletPayParam
     */
    private ChannelParam extraParam;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /** 是否不启用异步通知 */
    private Boolean notNotify;

    /** 异步通知地址 */
    private String notifyUrl;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/refund";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<RefundModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<RefundModel>>() {}, false);
    }
}
