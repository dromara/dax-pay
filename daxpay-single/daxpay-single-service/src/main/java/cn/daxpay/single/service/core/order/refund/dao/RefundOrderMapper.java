package cn.daxpay.single.service.core.order.refund.dao;

import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author xxm
 * @since 2022/3/2
 */
@Mapper
public interface RefundOrderMapper extends BaseMapper<RefundOrder> {

    @Select("select sum(amount) from pay_refund_order ${ew.customSqlSegment}")
    Integer getTalAmount(@Param(Constants.WRAPPER) QueryWrapper<RefundOrder> generator);
}
