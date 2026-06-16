package org.dromara.daxpay.payment.masterdata.constants.channel.dao;

import org.dromara.daxpay.payment.masterdata.constants.channel.entity.PayChannel;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付通道
///
@Mapper
public interface PayChannelMapper extends MPJBaseMapper<PayChannel> {
}