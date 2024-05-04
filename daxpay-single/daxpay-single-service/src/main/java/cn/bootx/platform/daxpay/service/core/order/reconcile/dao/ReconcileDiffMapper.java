package cn.bootx.platform.daxpay.service.core.order.reconcile.dao;

import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDiff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 对账差异单
 * @author xxm
 * @since 2024/2/28
 */
@Mapper
public interface ReconcileDiffMapper extends BaseMapper<ReconcileDiff> {
}
