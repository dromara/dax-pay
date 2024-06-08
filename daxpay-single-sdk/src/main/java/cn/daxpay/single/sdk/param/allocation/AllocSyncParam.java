package cn.daxpay.single.sdk.param.allocation;

import cn.daxpay.single.sdk.model.sync.AllocSyncModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
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
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<AllocSyncModel>>() {}, false);
    }
}
