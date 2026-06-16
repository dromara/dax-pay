package org.dromara.daxpay.payment.masterdata.constants.provider.dao;

import org.dromara.daxpay.payment.masterdata.constants.provider.entity.PayProvider;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/// # 支付渠道
@Repository
@RequiredArgsConstructor
public class PayProviderManager extends BaseManager<PayProviderMapper, PayProvider> {

    /// 按编码查询
    public Optional<PayProvider> findByCode(String code) {
        return lambdaQuery().eq(PayProvider::getCode, code).oneOpt();
    }

    /// 全部未删除记录，按 sortNo、id 排序
    public List<PayProvider> listAllOrdered() {
        return lambdaQuery()
                .orderByAsc(PayProvider::getSortNo)
                .orderByAsc(PayProvider::getId)
                .list();
    }

    /// code -> 实体
    public Map<String, PayProvider> mapByCode() {
        return listAllOrdered().stream()
                .collect(Collectors.toMap(PayProvider::getCode, e -> e, (a, b) -> a));
    }
}