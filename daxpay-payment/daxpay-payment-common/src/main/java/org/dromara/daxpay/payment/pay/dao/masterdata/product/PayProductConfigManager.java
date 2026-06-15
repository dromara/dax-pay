package org.dromara.daxpay.payment.pay.dao.masterdata.product;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.pay.entity.masterdata.product.PayProductConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 支付产品配置
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayProductConfigManager extends BaseManager<PayProductConfigMapper, PayProductConfig> {

    /// 根据产品编码查询
    public Optional<PayProductConfig> findByProduct(String product) {
        return lambdaQuery()
                .eq(PayProductConfig::getProduct, product)
                .oneOpt();
    }
}
