package org.dromara.daxpay.service.pay.dao.order.pay;

import org.dromara.daxpay.service.pay.entity.order.pay.PayOrderExpand;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单扩展表
 * @author xxm
 * @since 2025/6/16
 */
@Mapper
public interface PayOrderExpandMapper extends MPJBaseMapper<PayOrderExpand> {
}
