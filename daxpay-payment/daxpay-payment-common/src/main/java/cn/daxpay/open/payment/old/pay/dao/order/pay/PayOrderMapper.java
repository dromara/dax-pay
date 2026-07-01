package cn.daxpay.open.payment.old.pay.dao.order.pay;

import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface PayOrderMapper extends MPJBaseMapper<PayOrder> {

    @Select("select sum(amount)::bigint from pay_order ${ew.customSqlSegment}")
    Long getTotalAmount(@Param(Constants.WRAPPER) QueryWrapper<PayOrder> param);
}
