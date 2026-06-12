package org.dromara.daxpay.payment.pay.dao.product;

import org.dromara.daxpay.payment.pay.entity.masterdata.product.PayProduct;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付产品
///
@Mapper
public interface PayProductMapper extends MPJBaseMapper<PayProduct> {
}
