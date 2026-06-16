package org.dromara.daxpay.payment.old.pay.dao.order.pay;

import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;


@Mapper
public interface PayOrderMapper extends MPJBaseMapper<PayOrder> {

    @Select("select sum(amount) from pay_order ${ew.customSqlSegment}")
    BigDecimal getTotalAmount(@Param(Constants.WRAPPER) QueryWrapper<PayOrder> param);
}
