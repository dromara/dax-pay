package cn.daxpay.open.channel.leshua.dao.isv;

import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 乐刷服务商密钥配置
///
@Slf4j
@Service
public class LeshuaIsvKeyConfigManager extends BaseManager<LeshuaIsvKeyConfigMapper, LeshuaIsvKeyConfig> {

    /// 根据产品编码查询(平台为唯一服务商, 密钥全局唯一)
    public Optional<LeshuaIsvKeyConfig> findByProduct(String product) {
        return lambdaQuery()
                .eq(LeshuaIsvKeyConfig::getProduct, product)
                .oneOpt();
    }

    /// 根据产品编码和沙箱标志查询(双环境并存)
    public Optional<LeshuaIsvKeyConfig> findByProductAndSandbox(String product, boolean sandbox) {
        return lambdaQuery()
                .eq(LeshuaIsvKeyConfig::getProduct, product)
                .eq(LeshuaIsvKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }
}
