package cn.bootx.platform.daxpay.core.channel.config.dao;

import cn.bootx.platform.daxpay.core.channel.config.entity.PayChannelConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付通道配置
 * @author xxm
 * @date 2023-05-24
 */
@Mapper
public interface PayChannelConfigMapper extends BaseMapper<PayChannelConfig> {
}