package org.dromara.daxpay.service.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.entity.gateway.CashierCodeItemConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 码牌支付配置明细
 * @author xxm
 * @since 2024/11/20
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CashierCodeItemConfigManager extends BaseManager<CashierCodeItemConfigMapper, CashierCodeItemConfig> {

     /**
     * 根据码牌ID查询列表
     */
    public List<CashierCodeItemConfig> findAllByCodeId(Long code) {
        return this.lambdaQuery()
                .eq(CashierCodeItemConfig::getCodeId, code)
                .list();
    }

    /**
     * 判断类型是否存在
     */
    public boolean existsByType(String type, Long codeId) {
        return this.lambdaQuery()
                .eq(CashierCodeItemConfig::getType, type)
                .eq(CashierCodeItemConfig::getCodeId,codeId)
                .exists();
    }
    /**
     * 判断类型是否存在
     */
    public boolean existsByType(String type, Long cashierCodeId, Long id) {
        return this.lambdaQuery()
                .eq(CashierCodeItemConfig::getType, type)
                .eq(CashierCodeItemConfig::getCodeId,cashierCodeId)
                .ne(CashierCodeItemConfig::getId,id)
                .exists();
    }

    /**
     * 根据码牌和类型查询
     */
    public Optional<CashierCodeItemConfig> findByCodeAndType(Long codeId, String type) {
        return lambdaQuery()
                .eq(CashierCodeItemConfig::getCodeId, codeId)
                .eq(CashierCodeItemConfig::getType, type)
                .oneOpt();
    }

    /**
     * 根据码牌ID删除对应的类型配置
     */
    public void deleteByCode(Long codeId) {
        this.lambdaUpdate()
                .eq(CashierCodeItemConfig::getCodeId, codeId)
                .remove();
    }
}
