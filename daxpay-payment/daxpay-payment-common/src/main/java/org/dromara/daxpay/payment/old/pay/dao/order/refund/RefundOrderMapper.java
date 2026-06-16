package org.dromara.daxpay.payment.old.pay.dao.order.refund;

import org.dromara.daxpay.payment.old.pay.entity.order.refund.RefundOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;


@Mapper
public interface RefundOrderMapper extends MPJBaseMapper<RefundOrder> {

    @Select("select sum(amount) from pay_refund_order ${ew.customSqlSegment}")
    BigDecimal getTotalAmount(@Param(Constants.WRAPPER) QueryWrapper<RefundOrder> generator);
}
