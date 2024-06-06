package cn.daxpay.single.service.core.order.transfer.dao;

import cn.daxpay.single.service.core.order.transfer.entity.TransferOrderExtra;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 转账订单扩展
 * @author xxm
 * @since 2024/6/6
 */
@Mapper
public interface TransferOrderExtraMapper extends BaseMapper<TransferOrderExtra> {
}
