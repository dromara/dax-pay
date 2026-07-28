package cn.daxpay.open.payment.wx.dao.platform;

import cn.daxpay.open.payment.wx.entity.platform.WxPlatformAppAuthConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 平台微信应用授权认证配置
///
@Mapper
public interface WxPlatformAppAuthConfigMapper extends MPJBaseMapper<WxPlatformAppAuthConfig> {
}
