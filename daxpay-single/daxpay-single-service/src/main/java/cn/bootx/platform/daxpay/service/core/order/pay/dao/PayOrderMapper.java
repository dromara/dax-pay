package cn.bootx.platform.daxpay.service.core.order.pay.dao;

import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 支付订单
 * @author xxm
 * @since 2023/12/18
 */
@Mapper
public interface PayOrderMapper extends BaseMapper<PayOrder> {

    int insertList(@Param("list")List<PayOrder> list);

    @Select("select sum(amount) from pay_order ${ew.customSqlSegment}")
    Integer getTalAmount(@Param(Constants.WRAPPER) QueryWrapper<PayOrder> param);
}
