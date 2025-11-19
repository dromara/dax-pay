package org.dromara.daxpay.channel.wechat.dao.config;

import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfigEntity;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信支付配置Mapper
 * @author xxm
 * @since 2021/3/1
 */
@Mapper
public interface WechatPayConfigMapper extends MPJBaseMapper<WechatPayConfigEntity> {

}
