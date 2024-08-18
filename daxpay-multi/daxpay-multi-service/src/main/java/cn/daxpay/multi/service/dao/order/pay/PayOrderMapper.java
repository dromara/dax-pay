package cn.daxpay.multi.service.dao.order.pay;

import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author xxm
 * @since 2024/6/27
 */
@Mapper
public interface PayOrderMapper extends MPJBaseMapper<PayOrder> {


    @Select("select sum(amount) from pay_order ${ew.customSqlSegment}")
    Integer getTotalAmount(@Param(Constants.WRAPPER) QueryWrapper<PayOrder> param);
}
