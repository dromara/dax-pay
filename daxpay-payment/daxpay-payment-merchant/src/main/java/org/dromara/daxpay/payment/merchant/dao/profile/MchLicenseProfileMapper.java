package org.dromara.daxpay.payment.merchant.dao.profile;

import org.dromara.daxpay.payment.merchant.entity.profile.MchLicenseProfile;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户营业执照信息
///
@Mapper
public interface MchLicenseProfileMapper extends MPJBaseMapper<MchLicenseProfile> {
}