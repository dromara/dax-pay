package cn.daxpay.single.service.core.order.pay.dao;

import cn.daxpay.single.service.core.order.pay.entity.PayOrderExtra;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付订单扩展信息
 * @author xxm
 * @since 2023/12/20
 */
@Mapper
public interface PayOrderExtraMapper extends BaseMapper<PayOrderExtra> {
}
