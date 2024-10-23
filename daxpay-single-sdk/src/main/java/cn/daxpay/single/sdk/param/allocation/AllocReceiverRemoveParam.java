package cn.daxpay.single.sdk.param.allocation;

import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.daxpay.single.sdk.model.allocation.AllocReceiverRemoveModel;
import cn.daxpay.single.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账接收者删除参数
 * @author xxm
 * @since 2024/5/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AllocReceiverRemoveParam extends DaxPayRequest<AllocReceiverRemoveModel> {

    /** 接收者编号 */
    private String receiverNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/alloc/receiver/remove";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<AllocReceiverRemoveModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AllocReceiverRemoveModel>>() {});
    }
}
