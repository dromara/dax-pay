package org.dromara.daxpay.service.isv.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.isv.entity.gateway.IsvCashierGroupConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 网关收银台分组配置
 * @author xxm
 * @since 2025/3/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class IsvCashierGroupConfigManager extends BaseManager<IsvCashierGroupConfigMapper, IsvCashierGroupConfig> {


    /**
     * 查询指定类型的分组, 并进行排序
     */
    public List<IsvCashierGroupConfig> findAllByType(String cashierType){
        return lambdaQuery()
                .eq(IsvCashierGroupConfig::getCashierType, cashierType)
                .orderByAsc(IsvCashierGroupConfig::getSortNo)
                .list();
    }

    /**
     * 判断指定类型的分组是否存在
     */
    public boolean existedByType(String cashierType) {
        return lambdaQuery()
                .eq(IsvCashierGroupConfig::getCashierType, cashierType)
                .exists();
    }
}
