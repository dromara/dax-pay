package cn.daxpay.open.channel.vbill.dao.isv;

import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 随行付服务商密钥配置 Manager
@Slf4j
@Service
public class VbillIsvKeyConfigManager extends BaseManager<VbillIsvKeyConfigMapper, VbillIsvKeyConfig> {

    /// 根据产品编码查询(平台为唯一服务商, 密钥全局唯一)
    public Optional<VbillIsvKeyConfig> findByProduct(String product) {
        return lambdaQuery()
                .eq(VbillIsvKeyConfig::getProduct, product)
                .oneOpt();
    }

    /// 根据产品编码和沙箱标志查询(双环境并存)
    public Optional<VbillIsvKeyConfig> findByProductAndSandbox(String product, boolean sandbox) {
        return lambdaQuery()
                .eq(VbillIsvKeyConfig::getProduct, product)
                .eq(VbillIsvKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }
}
