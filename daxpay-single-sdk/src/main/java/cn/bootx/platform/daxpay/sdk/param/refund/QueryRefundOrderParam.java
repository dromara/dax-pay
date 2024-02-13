package cn.bootx.platform.daxpay.sdk.param.refund;

import cn.bootx.platform.daxpay.sdk.model.refund.QueryRefundOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayRequest;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询退款订单参数类
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryRefundOrderParam extends DaxPayRequest<QueryRefundOrderModel> {

    /** 退款ID */
    private Long refundId;

    /** 退款号 */
    private String refundNo;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

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
        return "/uni/query/refundOrder";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<QueryRefundOrderModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<QueryRefundOrderModel>>() {}, false);
    }
}
