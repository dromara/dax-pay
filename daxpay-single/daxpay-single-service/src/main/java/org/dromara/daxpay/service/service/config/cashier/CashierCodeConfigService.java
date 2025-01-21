package org.dromara.daxpay.service.service.config.cashier;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.core.result.cashier.CashierCodeResult;
import org.dromara.daxpay.service.dao.config.cashier.CashierCodeConfigManager;
import org.dromara.daxpay.service.dao.config.cashier.CashierCodeTypeConfigManager;
import org.dromara.daxpay.service.entity.config.cashier.CashierCodeConfig;
import org.dromara.daxpay.service.entity.config.cashier.CashierCodeTypeConfig;
import org.dromara.daxpay.service.entity.config.PlatformConfig;
import org.dromara.daxpay.service.param.config.cashier.CashierCodeConfigParam;
import org.dromara.daxpay.service.result.config.cashier.CashierCodeConfigResult;
import org.dromara.daxpay.service.service.config.PlatformConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierCodeConfigService {

    private final CashierCodeConfigManager cashierCodeConfigManager;

    private final CashierCodeTypeConfigManager cashierCodeTypeConfigManager;

    private final PlatformConfigService platformConfigService;

      /**
     * 添加
     */
    public void save(CashierCodeConfigParam param) {
        String uuid = UUID.fastUUID().toString(true);
        CashierCodeConfig config = CashierCodeConfig.init(param);
        config.setCode(uuid);
        cashierCodeConfigManager.save(config);
    }

    /**
     * 更新
     */
    public void update(CashierCodeConfigParam param) {
        CashierCodeConfig channelCashierConfig = cashierCodeConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
        BeanUtil.copyProperties(param, channelCashierConfig, CopyOptions.create().ignoreNullValue());
        cashierCodeConfigManager.updateById(channelCashierConfig);
    }

    /**
     * 列表
     */
    public List<CashierCodeConfigResult> findAllByAppId(String appId) {
        return MpUtil.toListResult(cashierCodeConfigManager.findAllByAppId(appId));
    }

    /**
     * 查询详情
     */
    public CashierCodeConfigResult findById(Long id) {
        return cashierCodeConfigManager.findById(id).map(CashierCodeConfig::toResult).orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (cashierCodeConfigManager.existedById(id)){
            throw new DataNotExistException("收银码牌配置不存在");
        }
        // 删除类型配置
        cashierCodeTypeConfigManager.deleteByCashierCode(id);
        cashierCodeConfigManager.deleteById(id);
    }

    /**
     * 获取码牌地址
     */
    public String getCashierCodeUrl(Long id) {
        var codeConfig= cashierCodeConfigManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
        PlatformConfig platformConfig = platformConfigService.getConfig();
        String serverUrl = platformConfig.getGatewayMobileUrl();
        return StrUtil.format("{}/cashier/{}", serverUrl, codeConfig.getCode());
    }

    /**
     * 根据码牌code和类型查询信息和配置
     */
    public CashierCodeResult findByCashierType(String cashierCode, String cashierType) {
        CashierCodeConfig codeConfig = cashierCodeConfigManager.findByCode(cashierCode)
                .orElseThrow(() -> new DataNotExistException("支付码牌不存在"));
        // 是否启用
        if (!codeConfig.isEnable()) {
            throw new ConfigNotEnableException("支付码牌已禁用");
        }

        CashierCodeTypeConfig codeTypeConfig = cashierCodeTypeConfigManager.findByCashierType(codeConfig.getId(), cashierType)
                .orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
        return new CashierCodeResult()
                .setAppId(codeConfig.getAppId())
                .setAllocation(codeTypeConfig.isAllocation())
                .setChannel(codeTypeConfig.getChannel())
                .setPayMethod(codeTypeConfig.getPayMethod())
                .setName(codeConfig.getName())
                .setTemplateCode(codeConfig.getTemplateCode());
    }
}
