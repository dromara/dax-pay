package org.dromara.daxpay.channel.wechat.dao.direct;

import org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectAppAuthConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 微信直连商户应用授权认证配置
///
/// 微信直连商户应用授权认证配置 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface WechatDirectAppAuthConfigMapper extends MPJBaseMapper<WechatDirectAppAuthConfig> {
}
