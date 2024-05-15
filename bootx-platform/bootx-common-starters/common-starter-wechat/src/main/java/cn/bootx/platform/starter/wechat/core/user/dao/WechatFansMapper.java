package cn.bootx.platform.starter.wechat.core.user.dao;

import cn.bootx.platform.starter.wechat.core.user.entity.WechatFans;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信公众号粉丝
 *
 * @author xxm
 * @since 2022-07-16
 */
@Mapper
public interface WechatFansMapper extends BaseMapper<WechatFans> {

}
