package cn.daxpay.open.channel.sheng.dao;

import cn.daxpay.open.channel.sheng.entity.ShengIsvKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 盛付通服务商密钥配置
@Repository
public class ShengIsvKeyConfigManager extends BaseManager<ShengIsvKeyConfigMapper, ShengIsvKeyConfig> {

    /// 根据产品编码查询(服务商密钥产品级全局唯一)
    public Optional<ShengIsvKeyConfig> findByProduct(String product) {
        return lambdaQuery()
                .eq(ShengIsvKeyConfig::getProduct, product)
                .oneOpt();
    }
}
