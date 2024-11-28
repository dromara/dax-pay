package org.dromara.daxpay.service.dao.config.checkout;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutItemConfig;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 收银台配置项
 * @author xxm
 * @since 2024/11/26
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CheckoutItemConfigManager extends BaseManager<CheckoutItemConfigMapper, CheckoutItemConfig> {

    /**
     * 根据id和appId查询
     */
    public Optional<CheckoutItemConfig> findByIdAndAppId(Long id, String appId) {
        return lambdaQuery()
                .eq(CheckoutItemConfig::getId, id)
                .eq(CheckoutItemConfig::getAppId, appId)
                .oneOpt();
    }

    /**
     * 根据分组查询
     */
    public List<CheckoutItemConfig> findAllByGroupId(Long groupId) {
        return lambdaQuery()
                .eq(CheckoutItemConfig::getGroupId, groupId)
                .list();
    }
    /**
     * 根据分组列表查询
     */
    public List<CheckoutItemConfig> findAllByGroupIds(List<Long> groupIds) {
        return lambdaQuery()
                .in(CheckoutItemConfig::getGroupId, groupIds)
                .orderByDesc(CheckoutItemConfig::getSortNo)
                .list();
    }

    /**
     * 判断分组是否有数据
     */
    public boolean existedByGroupId(Long groupId) {
        return existedByField(CheckoutItemConfig::getGroupId, groupId);
    }

}
