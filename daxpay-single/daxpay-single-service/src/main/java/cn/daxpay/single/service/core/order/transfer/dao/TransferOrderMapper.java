package cn.daxpay.single.service.core.order.transfer.dao;

import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.service.param.order.TransferOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author xxm
 * @since 2024/3/21
 */
@Mapper
public interface TransferOrderMapper extends BaseMapper<TransferOrder> {

    /**
     * 查询转账总金额
     */
    @Select("select sum(amount) from pay_transfer_order ${ew.customSqlSegment}")
    Integer getTalAmount(@Param(Constants.WRAPPER) QueryWrapper<TransferOrderQuery> generator);
}
