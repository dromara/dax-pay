package cn.bootx.platform.daxpay.core.channel.wechat.dao;

import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信支付配置
 *
 * @author xxm
 * @since 2021/3/19
 */
@Mapper
public interface WeChatPayConfigMapper extends BaseMapper<WeChatPayConfig> {

}
