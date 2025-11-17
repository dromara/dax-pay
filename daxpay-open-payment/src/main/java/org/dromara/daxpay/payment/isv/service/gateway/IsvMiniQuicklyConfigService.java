package org.dromara.daxpay.payment.isv.service.gateway;

import org.dromara.daxpay.payment.isv.convert.gateway.IsvMiniQuicklyConfigConvert;
import org.dromara.daxpay.payment.isv.dao.gateway.IsvMiniQuicklyConfigManager;
import org.dromara.daxpay.payment.isv.entity.gateway.IsvMiniQuicklyConfig;
import org.dromara.daxpay.payment.isv.param.gateway.IsvMiniQuicklyConfigParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 小程序快捷支付配置服务
 * @author xxm
 * @since 2024/11/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IsvMiniQuicklyConfigService {

    private final IsvMiniQuicklyConfigManager manager;

    /**
     * 根据服务商号查询配置
     */
    public IsvMiniQuicklyConfig findByIsvNo(String isvNo) {
        Optional<IsvMiniQuicklyConfig> optional = manager.findByIsvNo(isvNo);
        if (optional.isEmpty()){
            var entity = new IsvMiniQuicklyConfig();
            entity.setIsvNo(isvNo);
            manager.save(entity);
            return entity;
        }
        return optional.get();
    }

    /**
     * 更新配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(IsvMiniQuicklyConfigParam param) {
        var entity = this.findByIsvNo(param.getIsvNo());
        IsvMiniQuicklyConfigConvert.CONVERT.copy(param, entity);
        manager.updateById(entity);
    }
}
