package org.dromara.daxpay.channel.wechat.dao.isv;

import org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvAppAuthConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 微信服务商应用授权认证配置
///
@Mapper
public interface WechatIsvAppAuthConfigMapper extends MPJBaseMapper<WechatIsvAppAuthConfig> {
}
