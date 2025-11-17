package org.dromara.daxpay.payment.isv.service.gateway;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.payment.isv.convert.gateway.IsvCheckoutCounterConfigConvert;
import org.dromara.daxpay.payment.isv.dao.gateway.IsvCheckoutCounterConfigManager;
import org.dromara.daxpay.payment.isv.entity.gateway.IsvCheckoutCounterConfig;
import org.dromara.daxpay.payment.isv.param.gateway.IsvCheckoutCounterConfigParam;
import org.dromara.daxpay.payment.isv.result.gateway.IsvCheckoutCounterConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收银台配置
 *
 * @author xxm
 * @since 2025/3/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IsvCheckoutCounterConfigService {
    private final IsvCheckoutCounterConfigManager checkoutCounterManager;

    /**
     * 获取指定类型收银台分组列表
     */
    public List<IsvCheckoutCounterConfigResult> findAll(String isvNo, String type){
        return MpUtil.toListResult(checkoutCounterManager.findAllByIsvNoAndType(isvNo,type));
    }

    /**
     * 获取指定ID的配置项
     */
    public IsvCheckoutCounterConfigResult findById(Long id){
        return checkoutCounterManager.findById(id)
                .map(IsvCheckoutCounterConfig::toResult)
                .orElseThrow(() -> new DataNotExistException("服务商收银台配置不存在"));
    }

    /**
     * 保存配置项
     */
    public void save(IsvCheckoutCounterConfigParam param) {
        var entity = IsvCheckoutCounterConfig.init(param);
        entity.setIsvNo(param.getIsvNo());
        checkoutCounterManager.save(entity);
    }

    /**
     * 更新配置
     */
    public void update(IsvCheckoutCounterConfigParam param) {
        var checkoutCounterConfig = checkoutCounterManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("收银台配置不存在"));
        IsvCheckoutCounterConfigConvert.CONVERT.copy(param, checkoutCounterConfig);
        checkoutCounterManager.updateById(checkoutCounterConfig);
    }

    /**
     * 删除配置
     */
    public void delete(Long id) {
        checkoutCounterManager.deleteById(id);
    }

}
