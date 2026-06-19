package cn.daxpay.open.payment.masterdata.constants.channel.dao;

import cn.daxpay.open.payment.masterdata.constants.channel.entity.PayChannel;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付通道
///
@Mapper
public interface PayChannelMapper extends MPJBaseMapper<PayChannel> {
}