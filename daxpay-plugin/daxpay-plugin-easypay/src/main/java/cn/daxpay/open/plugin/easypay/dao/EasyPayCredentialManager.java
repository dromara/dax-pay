package cn.daxpay.open.plugin.easypay.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.plugin.easypay.entity.EasyPayCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EasyPayCredentialManager extends BaseManager<EasyPayCredentialMapper, EasyPayCredential> {

    public Optional<EasyPayCredential> findByAppId(String appId) {
        return findByField(EasyPayCredential::getAppId, appId);
    }

    public Optional<EasyPayCredential> findByPid(Integer pid) {
        return findByField(EasyPayCredential::getPid, pid);
    }

    @IgnoreTenant
    public Optional<EasyPayCredential> findByPidNotTenant(Integer pid) {
        return findByField(EasyPayCredential::getPid, pid);
    }
}
