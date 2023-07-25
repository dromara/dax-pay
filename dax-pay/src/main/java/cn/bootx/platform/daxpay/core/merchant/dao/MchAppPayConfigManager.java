package cn.bootx.platform.daxpay.core.merchant.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.merchant.entity.MchAppPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 商户应用支付配置
 *
 * @author xxm
 * @since 2023-05-19
 */
@Repository
@RequiredArgsConstructor
public class MchAppPayConfigManager extends BaseManager<MchAppPayConfigMapper, MchAppPayConfig> {

    /**
     * 该商户应用的指定类型的支付配置是否已经存在
     */
    public boolean existsByAppCodeAndChannel(String mchAppCode, String channel){
        return lambdaQuery()
                .eq(MchAppPayConfig::getAppCode,mchAppCode)
                .eq(MchAppPayConfig::getChannel,channel)
                .exists();
    }
}
