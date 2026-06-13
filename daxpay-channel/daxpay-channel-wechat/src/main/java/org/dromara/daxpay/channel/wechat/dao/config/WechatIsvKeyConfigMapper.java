package org.dromara.daxpay.channel.wechat.dao.config;

import org.dromara.daxpay.channel.wechat.entity.config.WechatIsvKeyConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 微信服务商密钥配置
///
@Mapper
public interface WechatIsvKeyConfigMapper extends MPJBaseMapper<WechatIsvKeyConfig> {
}
