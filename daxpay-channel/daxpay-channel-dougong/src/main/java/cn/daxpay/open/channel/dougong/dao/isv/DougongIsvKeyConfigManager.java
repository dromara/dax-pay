package cn.daxpay.open.channel.dougong.dao.isv;

import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 斗拱服务商密钥配置 Manager
@Slf4j
@Service
public class DougongIsvKeyConfigManager extends BaseManager<DougongIsvKeyConfigMapper, DougongIsvKeyConfig> {

    /// 根据产品编码查询(平台为唯一服务商, 密钥全局唯一)
    public Optional<DougongIsvKeyConfig> findByProduct(String product) {
        return lambdaQuery()
                .eq(DougongIsvKeyConfig::getProduct, product)
                .oneOpt();
    }
}
