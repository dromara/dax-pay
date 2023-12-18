package cn.bootx.platform.daxpay.core.order.pay.dao;

import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付订单
 * @author xxm
 * @since 2023/12/18
 */
@Mapper
public interface PayOrderMapper extends BaseMapper<PayOrder> {
}
