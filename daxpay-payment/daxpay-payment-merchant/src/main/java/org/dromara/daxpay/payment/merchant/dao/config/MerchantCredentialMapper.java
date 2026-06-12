package org.dromara.daxpay.payment.merchant.dao.config;

import org.dromara.daxpay.payment.merchant.entity.config.MerchantCredential;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户凭证
///
@Mapper
public interface MerchantCredentialMapper extends MPJBaseMapper<MerchantCredential> {
}
