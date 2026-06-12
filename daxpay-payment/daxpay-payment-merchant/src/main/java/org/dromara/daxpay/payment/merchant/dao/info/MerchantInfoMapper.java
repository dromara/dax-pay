package org.dromara.daxpay.payment.merchant.dao.info;

import org.dromara.daxpay.payment.merchant.entity.info.MerchantInfo;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户信息
///
@Mapper
public interface MerchantInfoMapper extends MPJBaseMapper<MerchantInfo> {
}
