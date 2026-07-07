package cn.daxpay.open.channel.hkrt.dao.isv;

import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 海科融通服务商密钥配置
///
@Slf4j
@Service
public class HkrtIsvKeyConfigManager extends BaseManager<HkrtIsvKeyConfigMapper, HkrtIsvKeyConfig> {

    /// 根据产品编码查询(平台为唯一服务商, 密钥全局唯一)
    public Optional<HkrtIsvKeyConfig> findByProduct(String product) {
        return lambdaQuery()
                .eq(HkrtIsvKeyConfig::getProduct, product)
                .oneOpt();
    }
}
