package cn.bootx.platform.daxpay.sdk.param.pay;

import cn.bootx.platform.daxpay.sdk.model.pay.PayOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayRequest;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 支付参数
 * @author xxm
 * @since 2024/2/2
 */
@Getter
@Setter
@ToString
public class PayParam extends DaxPayRequest<PayOrderModel> {

    /** 业务号 */
    private String businessNo;

    /** 支付标题 */
    private String title;

    /** 支付描述 */
    private String description;

    /** 过期时间, 多次传输以第一次为准 */
    private Long expiredTime;

    /** 用户付款中途退出返回商户网站的地址(部分支付场景中可用) */
    private String quitUrl;

    /** 支付通道信息参数 */
    private List<PayChannelParam> payChannels;

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
    public DaxPayResult<PayOrderModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<PayOrderModel>>() {}, false);
    }
}
