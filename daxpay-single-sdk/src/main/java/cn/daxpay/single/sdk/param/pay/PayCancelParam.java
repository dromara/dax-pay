package cn.daxpay.single.sdk.param.pay;

import cn.daxpay.single.sdk.model.pay.PayCancelModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付撤销参数
 * @author xxm
 * @since 2023/12/17
 */
@Getter
@Setter
public class PayCancelParam extends DaxPayRequest<PayCancelModel> {

    /** 订单号 */
    private String orderNo;

    /** 商户订单号 */
    private String bizOrderNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/cancel";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<PayCancelModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<PayCancelModel>>() {}, false);
    }
}
