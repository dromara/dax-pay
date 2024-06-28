package cn.daxpay.multi.service.dao.order.pay;

import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author xxm
 * @since 2024/6/27
 */
@Mapper
public interface PayOrderMapper extends MPJBaseMapper<PayOrder> {
}
