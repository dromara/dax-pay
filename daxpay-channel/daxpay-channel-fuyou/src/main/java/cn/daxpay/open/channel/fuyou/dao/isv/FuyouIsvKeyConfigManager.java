package cn.daxpay.open.channel.fuyou.dao.isv;

import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 富友服务商密钥配置 Manager
@Slf4j
@Service
public class FuyouIsvKeyConfigManager extends BaseManager<FuyouIsvKeyConfigMapper, FuyouIsvKeyConfig> {

    /// 根据产品编码查询(平台为唯一服务商, 密钥全局唯一)
    public Optional<FuyouIsvKeyConfig> findByProduct(String product) {
        return lambdaQuery()
                .eq(FuyouIsvKeyConfig::getProduct, product)
                .oneOpt();
    }

    /// 根据产品编码和沙箱标志查询(双环境并存)
    public Optional<FuyouIsvKeyConfig> findByProductAndSandbox(String product, boolean sandbox) {
        return lambdaQuery()
                .eq(FuyouIsvKeyConfig::getProduct, product)
                .eq(FuyouIsvKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }
}
