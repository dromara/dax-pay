package org.dromara.daxpay.single.sdk.param.allocation.receiver;

import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.model.allocation.receiver.AllocReceiverModel;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

/**
 * 分账接收方列表参数
 * @author xxm
 * @since 2024/5/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class QueryAllocReceiverParam extends DaxPayRequest<AllocReceiverModel> {

    /** 分账通道 */
    private String channel;


    /**
     * 方法请求路径
     *
     * @return 请求路径
     */
    @Override
    public String path() {
        return "/unipay/alloc/receiver/list";
    }

    /**
     * 将请求返回结果反序列化为实体类
     *
     * @param json json字符串
     * @return 反序列后的对象
     */
    @Override
    public DaxPayResult<AllocReceiverModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AllocReceiverModel>>() {});
    }
}
