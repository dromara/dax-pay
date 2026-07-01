package cn.daxpay.open.payment.merchant.dao.config;

import cn.daxpay.open.payment.merchant.entity.config.MchAppNotifyConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户应用事件通知配置
///
@Mapper
public interface MchAppNotifyConfigMapper extends MPJBaseMapper<MchAppNotifyConfig> {
}
