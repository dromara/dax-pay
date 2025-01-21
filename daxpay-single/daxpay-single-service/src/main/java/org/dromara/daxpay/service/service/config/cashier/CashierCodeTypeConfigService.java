package org.dromara.daxpay.service.service.config.cashier;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.dao.config.cashier.CashierCodeConfigManager;
import org.dromara.daxpay.service.dao.config.cashier.CashierCodeTypeConfigManager;
import org.dromara.daxpay.service.entity.config.cashier.CashierCodeConfig;
import org.dromara.daxpay.service.entity.config.cashier.CashierCodeTypeConfig;
import org.dromara.daxpay.service.param.config.cashier.CashierCodeTypeConfigParam;
import org.dromara.daxpay.service.result.config.cashier.CashierCodeTypeConfigResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收银码牌码牌类型配置
 * @author xxm
 * @since 2024/11/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierCodeTypeConfigService {
    private final CashierCodeTypeConfigManager codeTypeConfigManager;
    private final CashierCodeConfigManager codeConfigManager;

    /**
     * 列表
     */
    public List<CashierCodeTypeConfigResult> findByAppId(Long cashierCodeId) {
        return MpUtil.toListResult(codeTypeConfigManager.findAllByCashierCode(cashierCodeId));
    }

    /**
     * 查询详情
     */
    public CashierCodeTypeConfigResult findById(Long id) {
        return codeTypeConfigManager.findById(id).map(CashierCodeTypeConfig::toResult).orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
    }

    /**
     * 判断类型是否存在
     */
    public boolean existsByType(String type, Long cashierCodeId) {
        return codeTypeConfigManager.existsByType(type, cashierCodeId);
    }

    /**
     * 判断类型是否存在
     */
    public boolean existsByType(String type, Long cashierCodeId, Long id) {
        return codeTypeConfigManager.existsByType(type, cashierCodeId, id);
    }

    /**
     * 添加
     */
    public void save(CashierCodeTypeConfigParam param) {
        CashierCodeConfig codeConfig = codeConfigManager.findById(param.getCashierCodeId())
                .orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
        // 收银码牌类型不能重复
        boolean existed = codeTypeConfigManager.existsByType(param.getType(), param.getId());
        if (existed){
            throw new DataNotExistException("收银码牌类型不可重复配置");
        }

        CashierCodeTypeConfig entity = CashierCodeTypeConfig.init(param);
        entity.setAppId(codeConfig.getAppId());
        codeTypeConfigManager.save(entity);
    }

    /**
     * 更新
     */
    public void update(CashierCodeTypeConfigParam param) {
        // 收银码牌类型不能重复
        boolean existed = codeTypeConfigManager.existsByType(param.getType(), param.getId(), param.getId());
        if (existed){
            throw new DataNotExistException("收银码牌类型不可重复配置");
        }
        var channelCashierConfig = codeTypeConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
        BeanUtil.copyProperties(param, channelCashierConfig, CopyOptions.create().ignoreNullValue());
        codeTypeConfigManager.updateById(channelCashierConfig);
    }
    /**
     * 删除
     */
    public void delete(Long id) {
          if (codeTypeConfigManager.existedById(id)){
            throw new DataNotExistException("该码牌类型配置不存在");
        }
        codeTypeConfigManager.deleteById(id);
    }
}

