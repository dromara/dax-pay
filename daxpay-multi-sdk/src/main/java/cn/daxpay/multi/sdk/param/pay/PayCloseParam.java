package cn.daxpay.multi.sdk.param.pay;

import cn.daxpay.multi.sdk.model.pay.PayCloseModel;
import cn.daxpay.multi.sdk.net.DaxPayRequest;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.daxpay.multi.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付关闭参数
 * @author xxm
 * @since 2023/12/17
 */
@Getter
@Setter
public class PayCloseParam extends DaxPayRequest<PayCloseModel> {

    /** 订单号 */
    private String orderNo;

    /** 商户订单号 */
    private String bizOrderNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/close";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<PayCloseModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<PayCloseModel>>() {});
    }
}
