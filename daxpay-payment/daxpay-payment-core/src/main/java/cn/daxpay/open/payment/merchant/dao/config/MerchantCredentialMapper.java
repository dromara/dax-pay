package cn.daxpay.open.payment.merchant.dao.config;

import cn.daxpay.open.payment.merchant.entity.config.MerchantCredential;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户凭证
///
@Mapper
public interface MerchantCredentialMapper extends MPJBaseMapper<MerchantCredential> {
}
