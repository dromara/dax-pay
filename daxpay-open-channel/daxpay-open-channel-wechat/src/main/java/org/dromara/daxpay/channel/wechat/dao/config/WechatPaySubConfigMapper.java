package org.dromara.daxpay.channel.wechat.dao.config;

import org.dromara.daxpay.channel.wechat.entity.config.WechatPaySubConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信子商户配置Mapper
 * @author xxm
 * @since 2024/12/27
 */
@Mapper
public interface WechatPaySubConfigMapper extends MPJBaseMapper<WechatPaySubConfig> {
}
