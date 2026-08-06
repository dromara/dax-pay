package cn.daxpay.open.payment.merchant.dao.config;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.payment.merchant.entity.config.MchRiskConfig;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 商户风控配置管理
///
@Repository
public class MchRiskConfigManager extends BaseManager<MchRiskConfigMapper, MchRiskConfig> {

    /// 根据商户号查询风控配置
    public Optional<MchRiskConfig> findByMchNo(String mchNo) {
        return findByField(MchRiskConfig::getMchNo, mchNo);
    }
}
