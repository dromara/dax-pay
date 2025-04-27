package org.dromara.daxpay.service.dao.order.refund;

import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 *
 * @author xxm
 * @since 2024/6/27
 */
@Mapper
public interface RefundOrderMapper extends MPJBaseMapper<RefundOrder> {


    @Select("select sum(amount) from pay_refund_order ${ew.customSqlSegment}")
    BigDecimal getTotalAmount(@Param(Constants.WRAPPER) QueryWrapper<RefundOrder> generator);
}
