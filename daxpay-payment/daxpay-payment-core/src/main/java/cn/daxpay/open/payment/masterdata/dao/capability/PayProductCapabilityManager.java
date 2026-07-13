package cn.daxpay.open.payment.masterdata.dao.capability;

import cn.daxpay.open.payment.masterdata.entity.product.PayProductCapability;
import cn.hutool.core.collection.CollUtil;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/// # 支付产品能力关联
@Repository
@RequiredArgsConstructor
public class PayProductCapabilityManager extends BaseManager<PayProductCapabilityMapper, PayProductCapability> {

    /// 某能力下的全部关联
    public List<PayProductCapability> listByCapability(String capabilityCode) {
        return lambdaQuery()
                .eq(PayProductCapability::getCapabilityCode, capabilityCode)
                .orderByAsc(PayProductCapability::getSortNo)
                .orderByAsc(PayProductCapability::getId)
                .list();
    }

    /// 某产品下的全部关联
    public List<PayProductCapability> listByProduct(String productCode) {
        return lambdaQuery()
                .eq(PayProductCapability::getProductCode, productCode)
                .orderByAsc(PayProductCapability::getSortNo)
                .orderByAsc(PayProductCapability::getId)
                .list();
    }

    /// 多个产品下的全部关联
    public List<PayProductCapability> listByProductCodes(Collection<String> productCodes) {
        if (CollUtil.isEmpty(productCodes)) {
            return Collections.emptyList();
        }
        return lambdaQuery()
                .in(PayProductCapability::getProductCode, productCodes)
                .orderByAsc(PayProductCapability::getProductCode)
                .orderByAsc(PayProductCapability::getSortNo)
                .orderByAsc(PayProductCapability::getId)
                .list();
    }

    /// 全部未删除关联
    public List<PayProductCapability> listAllOrdered() {
        return lambdaQuery()
                .orderByAsc(PayProductCapability::getProductCode)
                .orderByAsc(PayProductCapability::getSortNo)
                .orderByAsc(PayProductCapability::getId)
                .list();
    }

    /// 产品是否关联该能力
    public boolean exists(String productCode, String capabilityCode) {
        return lambdaQuery()
                .eq(PayProductCapability::getProductCode, productCode)
                .eq(PayProductCapability::getCapabilityCode, capabilityCode)
                .exists();
    }
}