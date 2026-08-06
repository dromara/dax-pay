package cn.daxpay.open.payment.merchant.dao.config;

import cn.daxpay.open.payment.merchant.entity.config.MchRiskConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户风控配置
///
@Mapper
public interface MchRiskConfigMapper extends MPJBaseMapper<MchRiskConfig> {
}
