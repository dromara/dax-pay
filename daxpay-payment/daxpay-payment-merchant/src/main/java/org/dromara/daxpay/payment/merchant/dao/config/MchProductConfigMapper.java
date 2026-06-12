package org.dromara.daxpay.payment.merchant.dao.config;

import org.dromara.daxpay.payment.merchant.entity.config.MchProductConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户产品配置
///
@Mapper
public interface MchProductConfigMapper extends MPJBaseMapper<MchProductConfig> {
}
