package cn.daxpay.multi.sdk.param.allocation;

import cn.daxpay.multi.sdk.result.allocation.AllocResult;
import cn.daxpay.multi.sdk.net.DaxPayRequest;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.daxpay.multi.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分账请求接口
 * @author xxm
 * @since 2024/2/7
 */
@Getter
@Setter
public class AllocationParam extends DaxPayRequest<AllocResult> {

    /** 商户分账单号 */
    private String bizAllocNo;

    /** 支付订单号 */
    private String orderNo;

    /** 商户订单号 */
    private String bizOrderNo;

    /** 分账描述 */
    private String description;

    /**
     * 优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    private String groupNo;

    /** 分账接收方列表 */
    private List<AllocReceiverParam> receivers;

    /** 回调通知地址 */
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;


    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/allocation/start";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<AllocResult> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AllocResult>>() {});
    }
}
