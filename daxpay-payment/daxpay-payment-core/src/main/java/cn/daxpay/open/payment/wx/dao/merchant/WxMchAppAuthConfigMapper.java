package cn.daxpay.open.payment.wx.dao.merchant;

import cn.daxpay.open.payment.wx.entity.merchant.WxMchAppAuthConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户微信应用授权认证配置
///
@Mapper
public interface WxMchAppAuthConfigMapper extends MPJBaseMapper<WxMchAppAuthConfig> {
}
