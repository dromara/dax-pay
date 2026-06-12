package org.dromara.daxpay.payment.pay.dao.masterdata.method;

import org.dromara.daxpay.payment.pay.entity.masterdata.method.PayMethod;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/// # 支付方式
@Repository
@RequiredArgsConstructor
public class PayMethodManager extends BaseManager<PayMethodMapper, PayMethod> {

    /// 按编码查询
    public Optional<PayMethod> findByCode(String code) {
        return lambdaQuery().eq(PayMethod::getCode, code).oneOpt();
    }

    /// 全部未删除，按 sortNo、id 排序
    public List<PayMethod> listAllOrdered() {
        return lambdaQuery()
                .orderByAsc(PayMethod::getSortNo)
                .orderByAsc(PayMethod::getId)
                .list();
    }

    /// code -> 实体
    public Map<String, PayMethod> mapByCode() {
        return listAllOrdered().stream()
                .collect(Collectors.toMap(PayMethod::getCode, e -> e, (a, b) -> a));
    }
}