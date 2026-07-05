package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppAuthConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 微信服务商通道商户应用授权认证配置
///
/// 微信服务商通道商户应用授权认证配置 MyBatis-Plus Mapper,继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface WechatIsvMchAppAuthConfigMapper extends MPJBaseMapper<WechatIsvMchAppAuthConfig> {
}
