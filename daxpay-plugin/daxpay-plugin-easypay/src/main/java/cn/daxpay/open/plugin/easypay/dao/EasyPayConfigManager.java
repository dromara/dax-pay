package cn.daxpay.open.plugin.easypay.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EasyPayConfigManager extends BaseManager<EasyPayConfigMapper, EasyPayConfig> {

    public Optional<EasyPayConfig> findByPid(Integer pid) {
        return findByField(EasyPayConfig::getPid, pid);
    }

    public Optional<EasyPayConfig> findByAppId(String appId) {
        return findByField(EasyPayConfig::getAppId, appId);
    }
}
