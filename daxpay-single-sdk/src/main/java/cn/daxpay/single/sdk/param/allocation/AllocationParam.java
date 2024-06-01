package cn.daxpay.single.sdk.param.allocation;

import cn.daxpay.single.sdk.model.allocation.AllocationModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
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
public class AllocationParam extends DaxPayRequest<AllocationModel> {

    /** 商户分账单号 */
    private String bizAllocationNo;

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

    /** 是否不启用异步通知 */
    private Boolean notNotify;

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
    public DaxPayResult<AllocationModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<AllocationModel>>() {}, false);
    }
}
