package org.dromara.daxpay.service.merchant.dao.config;

import org.dromara.daxpay.service.merchant.entity.config.MerchantNotifyConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商户应用消息通知配置
 * @author xxm
 * @since 2024/8/2
 */
@Mapper
public interface MerchantNotifyConfigMapper extends MPJBaseMapper<MerchantNotifyConfig> {
}
