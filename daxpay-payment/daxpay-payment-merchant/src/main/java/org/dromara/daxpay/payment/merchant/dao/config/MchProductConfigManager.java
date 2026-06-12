package org.dromara.daxpay.payment.merchant.dao.config;

import org.dromara.daxpay.payment.merchant.entity.config.MchProductConfig;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 商户产品配置
///
@Repository
public class MchProductConfigManager extends BaseManager<MchProductConfigMapper, MchProductConfig> {

    /// 根据商户号查询产品配置列表
    public List<MchProductConfig> findByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(MchProductConfig::getMchNo, mchNo)
                .list();
    }

    /// 根据商户号和产品编码查询产品配置
    public Optional<MchProductConfig> findByMchNoAndProduct(String mchNo, String product) {
        return lambdaQuery()
                .eq(MchProductConfig::getMchNo, mchNo)
                .eq(MchProductConfig::getProduct, product)
                .oneOpt();
    }
}
