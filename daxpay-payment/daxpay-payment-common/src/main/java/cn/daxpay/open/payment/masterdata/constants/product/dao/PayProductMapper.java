package cn.daxpay.open.payment.masterdata.constants.product.dao;

import cn.daxpay.open.payment.masterdata.constants.product.entity.PayProduct;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付产品
///
@Mapper
public interface PayProductMapper extends MPJBaseMapper<PayProduct> {
}
