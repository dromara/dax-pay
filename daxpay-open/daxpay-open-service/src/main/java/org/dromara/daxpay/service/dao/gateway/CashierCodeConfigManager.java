package org.dromara.daxpay.service.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.entity.gateway.CashierCodeConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/11/20
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CashierCodeConfigManager extends BaseManager<CashierCodeConfigMapper, CashierCodeConfig> {

    /**
     * 根据AppId查询列表
     */
    public List<CashierCodeConfig> findAllByAppId(String appId) {
        return this.lambdaQuery()
                .eq(CashierCodeConfig::getAppId, appId)
                .list();
    }

    /**
     * 根据Code查询码牌信息
     */
    public Optional<CashierCodeConfig> findByCode(String code) {
        return findByField(CashierCodeConfig::getCode, code);
    }

}
