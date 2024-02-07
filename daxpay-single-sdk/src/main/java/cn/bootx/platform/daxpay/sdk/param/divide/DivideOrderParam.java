package cn.bootx.platform.daxpay.sdk.param.divide;

import cn.bootx.platform.daxpay.sdk.model.divide.DivideOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayRequest;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 分账请求接口(目前未支持)
 * @author xxm
 * @since 2024/2/7
 */
@Getter
@Setter
public class DivideOrderParam extends DaxPayRequest<DivideOrderModel> {

    /** 支付ID */
    private Long paymentId;

    /** 业务号 */
    private String businessNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/divide";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<DivideOrderModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<DivideOrderModel>>() {}, false);
    }
}
