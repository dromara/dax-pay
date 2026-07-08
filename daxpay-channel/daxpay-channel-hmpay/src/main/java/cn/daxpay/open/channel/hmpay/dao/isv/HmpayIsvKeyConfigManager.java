package cn.daxpay.open.channel.hmpay.dao.isv;

import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 河马付服务商密钥配置 Manager
@Slf4j
@Service
public class HmpayIsvKeyConfigManager extends BaseManager<HmpayIsvKeyConfigMapper, HmpayIsvKeyConfig> {

    /// 根据产品编码查询(平台为唯一服务商, 密钥全局唯一)
    public Optional<HmpayIsvKeyConfig> findByProduct(String product) {
        return lambdaQuery()
                .eq(HmpayIsvKeyConfig::getProduct, product)
                .oneOpt();
    }

    /// 根据产品编码和沙箱标志查询(双环境并存)
    public Optional<HmpayIsvKeyConfig> findByProductAndSandbox(String product, boolean sandbox) {
        return lambdaQuery()
                .eq(HmpayIsvKeyConfig::getProduct, product)
                .eq(HmpayIsvKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }
}
