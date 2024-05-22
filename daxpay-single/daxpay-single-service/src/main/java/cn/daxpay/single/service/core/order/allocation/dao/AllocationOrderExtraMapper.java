package cn.daxpay.single.service.core.order.allocation.dao;

import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderExtra;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分账订单扩展
 * @author xxm
 * @since 2024/5/22
 */
@Mapper
public interface AllocationOrderExtraMapper extends BaseMapper<AllocationOrderExtra> {
}
