package org.dromara.daxpay.service.dao.config.checkout;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutGroupConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收银台类目配置
 * @author xxm
 * @since 2024/11/25
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CheckoutGroupConfigManager extends BaseManager<CheckoutGroupConfigMapper, CheckoutGroupConfig> {

    /**
     * 查询指定类型的分组, 并进行排序
     */
    public List<CheckoutGroupConfig> findAllSortByAppIdAndType(String appId, String type){
        return lambdaQuery()
                .eq(CheckoutGroupConfig::getAppId, appId)
                .eq(CheckoutGroupConfig::getType, type)
                .orderByAsc(CheckoutGroupConfig::getSortNo)
                .list();
    }

}
