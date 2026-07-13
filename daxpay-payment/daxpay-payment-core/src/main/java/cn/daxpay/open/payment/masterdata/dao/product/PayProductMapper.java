package cn.daxpay.open.payment.masterdata.dao.product;

import cn.daxpay.open.payment.masterdata.entity.product.PayProduct;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付产品
///
@Mapper
public interface PayProductMapper extends MPJBaseMapper<PayProduct> {
}
