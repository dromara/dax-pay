package cn.bootx.platform.daxpay.core.order.pay.dao;

import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderChannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付订单关联支付时通道信息
 * @author xxm
 * @since 2023/12/20
 */
@Mapper
public interface PayOrderChannelMapper extends BaseMapper<PayOrderChannel> {
}
