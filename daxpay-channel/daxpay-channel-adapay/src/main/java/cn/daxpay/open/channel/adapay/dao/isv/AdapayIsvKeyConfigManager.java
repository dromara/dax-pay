package cn.daxpay.open.channel.adapay.dao.isv;

import cn.daxpay.open.channel.adapay.entity.isv.AdapayIsvKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # Adapay 服务商密钥配置 Manager
@Slf4j
@Service
public class AdapayIsvKeyConfigManager extends BaseManager<AdapayIsvKeyConfigMapper, AdapayIsvKeyConfig> {

    /// 根据沙箱环境查询(平台为唯一服务商, 同一环境仅一条配置)
    public Optional<AdapayIsvKeyConfig> findBySandbox(boolean sandbox) {
        return lambdaQuery()
                .eq(AdapayIsvKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }
}
