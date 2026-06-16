package org.dromara.daxpay.payment.old.pay.dao.masterdata.provider;

import org.dromara.daxpay.payment.old.pay.entity.masterdata.provider.PayProviderMethod;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// # 渠道支付方式目录项
@Repository
@RequiredArgsConstructor
public class PayProviderMethodManager extends BaseManager<PayProviderMethodMapper, PayProviderMethod> {

    /// 全部未删除记录
    public List<PayProviderMethod> listAllOrdered() {
        return lambdaQuery()
                .orderByAsc(PayProviderMethod::getProvider)
                .orderByAsc(PayProviderMethod::getSortNo)
                .orderByAsc(PayProviderMethod::getId)
                .list();
    }

    /// (provider|method) -> 实体
    public Map<String, PayProviderMethod> mapByPairKey() {
        return listAllOrdered().stream()
                .collect(Collectors.toMap(this::pairKey, e -> e, (a, b) -> a));
    }

    private String pairKey(PayProviderMethod row) {
        return pairKey(row.getProvider(), row.getMethod());
    }

    public static String pairKey(String provider, String method) {
        return provider + "|" + method;
    }

    /// 某支付方式下的全部关联（未删除）
    public List<PayProviderMethod> listByMethod(String methodCode) {
        return lambdaQuery()
                .eq(PayProviderMethod::getMethod, methodCode)
                .orderByAsc(PayProviderMethod::getSortNo)
                .orderByAsc(PayProviderMethod::getId)
                .list();
    }

    /// 某支付渠道下的全部关联（未删除）
    public List<PayProviderMethod> listByProvider(String providerCode) {
        return lambdaQuery()
                .eq(PayProviderMethod::getProvider, providerCode)
                .orderByAsc(PayProviderMethod::getSortNo)
                .orderByAsc(PayProviderMethod::getId)
                .list();
    }
}