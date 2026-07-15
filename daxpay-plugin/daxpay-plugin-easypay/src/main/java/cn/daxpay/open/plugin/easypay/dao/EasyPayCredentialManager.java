package cn.daxpay.open.plugin.easypay.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.plugin.easypay.entity.EasyPayCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 易支付凭证 Manager
///
@Repository
@RequiredArgsConstructor
public class EasyPayCredentialManager extends BaseManager<EasyPayCredentialMapper, EasyPayCredential> {

    /// 按应用号查询
    public Optional<EasyPayCredential> findByAppId(String appId) {
        return findByField(EasyPayCredential::getAppId, appId);
    }

    /// 按易支付商户号查询
    public Optional<EasyPayCredential> findByPid(Integer pid) {
        return findByField(EasyPayCredential::getPid, pid);
    }

    /// 忽略租户按易支付商户号查询（对外验签入口）
    @IgnoreTenant
    public Optional<EasyPayCredential> findByPidNotTenant(Integer pid) {
        return findByField(EasyPayCredential::getPid, pid);
    }
}
