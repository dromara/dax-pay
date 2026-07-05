package cn.daxpay.open.channel.lakala.dao.isv;

import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 拉卡拉服务商密钥配置
///
@Slf4j
@Service
public class LakalaIsvKeyConfigManager extends BaseManager<LakalaIsvKeyConfigMapper, LakalaIsvKeyConfig> {

    /// 根据产品编码查询(平台为唯一服务商, 密钥全局唯一)
    public Optional<LakalaIsvKeyConfig> findByProduct(String product) {
        return lambdaQuery()
                .eq(LakalaIsvKeyConfig::getProduct, product)
                .oneOpt();
    }
}
