package cn.bootx.platform.daxpay.core.order.sync.dao;

import cn.bootx.platform.daxpay.core.order.sync.entity.PaySyncOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付同步记录
 * @author xxm
 * @since 2023/7/14
 */
@Mapper
public interface PaySyncOrderMapper extends BaseMapper<PaySyncOrder> {
}
