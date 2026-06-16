package org.dromara.daxpay.payment.masterdata.constants.product.dao;

import org.dromara.daxpay.payment.masterdata.constants.product.entity.PayProduct;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付产品
///
@Mapper
public interface PayProductMapper extends MPJBaseMapper<PayProduct> {
}
