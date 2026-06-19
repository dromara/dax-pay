package cn.daxpay.open.channel.wechat.dao.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectKeyConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 微信直连密钥配置
///
/// 微信直连密钥配置 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface WechatDirectKeyConfigMapper extends MPJBaseMapper<WechatDirectKeyConfig> {
}
