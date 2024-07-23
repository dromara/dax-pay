package cn.daxpay.multi.sdk.param.allocation;

import cn.daxpay.multi.sdk.result.allocation.AllocSyncModel;
import cn.daxpay.multi.sdk.net.DaxPayRequest;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.daxpay.multi.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Getter;
import lombok.Setter;

/**
 * 分账订单同步
 * @author xxm
 * @since 2024/5/27
 */
@Getter
@Setter
public class AllocSyncParam extends DaxPayRequest<AllocSyncModel> {

    /** 分账号 */
    private String allocNo;

    /** 商户分账号 */
    private String bizAllocNo;

    @Override
    public String path() {
        return "/unipay/sync/allocation";
    }

    @Override
    public DaxPayResult<AllocSyncModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AllocSyncModel>>() {});
    }
}
