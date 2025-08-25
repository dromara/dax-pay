package org.dromara.daxpay.service.device.dao.qrcode.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.device.entity.qrcode.config.CashierCodeSceneConfig;
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
public class CashierCodeSceneConfigManager extends BaseManager<CashierCodeSceneConfigMapper, CashierCodeSceneConfig> {

     /**
     * 根据码牌ID查询列表
     */
    public List<CashierCodeSceneConfig> findAllByConfigId(Long configId) {
        return this.lambdaQuery()
                .eq(CashierCodeSceneConfig::getConfigId, configId)
                .list();
    }

    /**
     * 判断收银场景是否存在
     */
    public boolean existsByScene(String scene, Long configId) {
        return this.lambdaQuery()
                .eq(CashierCodeSceneConfig::getScene, scene)
                .eq(CashierCodeSceneConfig::getConfigId,configId)
                .exists();
    }
    /**
     * 判断类型是否存在
     */
    public boolean existsByScene(String scene, Long configId, Long id) {
        return this.lambdaQuery()
                .eq(CashierCodeSceneConfig::getScene, scene)
                .eq(CashierCodeSceneConfig::getConfigId,configId)
                .ne(CashierCodeSceneConfig::getId,id)
                .exists();
    }

    /**
     * 根据码牌配置Id和类型查询
     */
    public Optional<CashierCodeSceneConfig> findByConfigAndScene(Long configId, String scene) {
        return lambdaQuery()
                .eq(CashierCodeSceneConfig::getConfigId, configId)
                .eq(CashierCodeSceneConfig::getScene, scene)
                .oneOpt();
    }

    /**
     * 根据码牌ID删除对应的类型配置
     */
    public void deleteByConfigId(Long configId) {
        this.lambdaUpdate()
                .eq(CashierCodeSceneConfig::getConfigId, configId)
                .remove();
    }
}
