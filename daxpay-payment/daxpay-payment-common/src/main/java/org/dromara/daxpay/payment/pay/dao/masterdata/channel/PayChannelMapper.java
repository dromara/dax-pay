package org.dromara.daxpay.payment.pay.dao.masterdata.channel;

import org.dromara.daxpay.payment.pay.entity.masterdata.channel.PayChannel;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付通道
///
@Mapper
public interface PayChannelMapper extends MPJBaseMapper<PayChannel> {
}