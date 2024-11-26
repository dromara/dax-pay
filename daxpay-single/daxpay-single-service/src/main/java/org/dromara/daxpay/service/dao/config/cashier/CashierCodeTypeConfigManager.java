package org.dromara.daxpay.service.dao.config.cashier;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.entity.config.cashier.CashierCodeTypeConfig;
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
public class CashierCodeTypeConfigManager extends BaseManager<CashierCodeTypeConfigMapper, CashierCodeTypeConfig> {

     /**
     * 根据码牌ID查询列表
     */
    public List<CashierCodeTypeConfig> findAllByCashierCode(Long cashierCodeId) {
        return this.lambdaQuery()
                .eq(CashierCodeTypeConfig::getCashierCodeId, cashierCodeId)
                .list();
    }

    /**
     * 判断类型是否存在
     */
    public boolean existsByType(String type, Long cashierCodeId) {
        return this.lambdaQuery()
                .eq(CashierCodeTypeConfig::getType, type)
                .eq(CashierCodeTypeConfig::getCashierCodeId,cashierCodeId)
                .exists();
    }
    /**
     * 判断类型是否存在
     */
    public boolean existsByType(String type, Long cashierCodeId, Long id) {
        return this.lambdaQuery()
                .eq(CashierCodeTypeConfig::getType, type)
                .eq(CashierCodeTypeConfig::getCashierCodeId,cashierCodeId)
                .ne(CashierCodeTypeConfig::getId,id)
                .exists();
    }

    /**
     * 根据类型查询
     */
    public Optional<CashierCodeTypeConfig> findByCashierType(Long cashierCodeId, String cashierType) {
        return lambdaQuery()
                .eq(CashierCodeTypeConfig::getCashierCodeId, cashierCodeId)
                .eq(CashierCodeTypeConfig::getType, cashierType)
                .oneOpt();
    }

    /**
     * 根据码牌ID删除对应的类型配置
     */
    public void deleteByCashierCode(Long cashierCodeId) {
        this.lambdaUpdate()
                .eq(CashierCodeTypeConfig::getCashierCodeId, cashierCodeId)
                .remove();
    }
}
