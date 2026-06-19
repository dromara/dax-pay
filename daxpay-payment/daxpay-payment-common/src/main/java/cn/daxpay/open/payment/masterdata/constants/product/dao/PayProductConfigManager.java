package cn.daxpay.open.payment.masterdata.constants.product.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.payment.masterdata.constants.product.entity.PayProductConfig;
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
