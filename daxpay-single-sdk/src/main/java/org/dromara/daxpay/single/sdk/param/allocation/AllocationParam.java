package org.dromara.daxpay.single.sdk.param.allocation;

import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.model.allocation.AllocationModel;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分账参数
 * @author xxm
 * @since 2024/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AllocationParam extends DaxPayRequest<AllocationModel> {

    /** 商户分账单号 */
    private String bizAllocNo;

    /** 支付订单号 */
    private String orderNo;

    /** 商户支付订单号 */
    private String bizOrderNo;

    /** 分账标题 */
    private String title;

    /** 分账描述 */
    private String description;

    /**
     * 优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    private String groupNo;

    /** 分账接收方列表 */
    private List<ReceiverParam> receivers;

    /** 回调通知地址 */
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;


    /**
     * 分账接收方列表
     */
    @Data
    @Accessors(chain = true)
    public static class ReceiverParam {

        /** 分账接收方编号 */
        private String receiverNo;

        /** 分账金额 */
        private BigDecimal amount;
    }

    /**
     * 方法请求路径
     *
     * @return 请求路径
     */
    @Override
    public String path() {
        return "/unipay/alloc/start";
    }

    /**
     * 将请求返回结果反序列化为实体类
     *
     * @param json json字符串
     * @return 反序列后的对象
     */
    @Override
    public DaxPayResult<AllocationModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AllocationModel>>() {});
    }
}
