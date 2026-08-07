package cn.daxpay.open.channel.wechat.dao.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatTransferConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 微信转账配置
///
/// 微信转账配置 MyBatis-Plus Mapper, 继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface WechatTransferConfigMapper extends MPJBaseMapper<WechatTransferConfig> {
}
