package cn.daxpay.multi.sdk.param.allocation;

import cn.daxpay.multi.sdk.code.PayChannelEnum;
import cn.daxpay.multi.sdk.model.allocation.AllocReceiversModel;
import cn.daxpay.multi.sdk.net.DaxPayRequest;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.daxpay.multi.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询分账接收者参数
 * @author xxm
 * @since 2024/5/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class QueryAllocReceiverParam extends DaxPayRequest<AllocReceiversModel> {

    /**
     * 所属通道
     * @see PayChannelEnum
     */
    private String channel;

    /** 分账接收方编号 */
    private String receiverNo;



    @Override
    public String path() {
        return "/unipay/query/allocationReceiver";
    }

    @Override
    public DaxPayResult<AllocReceiversModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AllocReceiversModel>>() {});
    }
}
