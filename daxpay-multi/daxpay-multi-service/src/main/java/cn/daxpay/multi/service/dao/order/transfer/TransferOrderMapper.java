package cn.daxpay.multi.service.dao.order.transfer;

import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.daxpay.multi.service.param.order.transfer.TransferOrderQuery;
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
 * @since 2024/3/21
 */
@Mapper
public interface TransferOrderMapper extends MPJBaseMapper<TransferOrder> {

    /**
     * 查询转账总金额
     */
    @Select("select sum(amount) from pay_transfer_order ${ew.customSqlSegment}")
    BigDecimal getTotalAmount(@Param(Constants.WRAPPER) QueryWrapper<TransferOrderQuery> generator);
}
