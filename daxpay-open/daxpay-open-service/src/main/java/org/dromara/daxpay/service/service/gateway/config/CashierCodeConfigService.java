package org.dromara.daxpay.service.service.gateway.config;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.service.common.local.MchContextLocal;
import org.dromara.daxpay.service.dao.gateway.CashierCodeConfigManager;
import org.dromara.daxpay.service.dao.gateway.CashierCodeItemConfigManager;
import org.dromara.daxpay.service.entity.config.PlatformConfig;
import org.dromara.daxpay.service.entity.gateway.CashierCodeConfig;
import org.dromara.daxpay.service.entity.gateway.CashierCodeItemConfig;
import org.dromara.daxpay.service.param.gateway.CashierCodeConfigParam;
import org.dromara.daxpay.service.param.gateway.CashierCodeItemConfigParam;
import org.dromara.daxpay.service.result.gateway.config.CashierCodeConfigResult;
import org.dromara.daxpay.service.result.gateway.config.CashierCodeItemConfigResult;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.config.PlatformConfigService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2025/4/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierCodeConfigService {

    private final CashierCodeConfigManager cashierCodeConfigManager;

    private final CashierCodeItemConfigManager codeItemConfigManager;

    private final PlatformConfigService platformConfigService;

    private final PaymentAssistService paymentAssistService;

    /**
     * 添加
     */
    public void save(CashierCodeConfigParam param) {
        paymentAssistService.initMchAndApp(param.getAppId());
        String uuid = UUID.fastUUID().toString(true);
        CashierCodeConfig config = CashierCodeConfig.init(param);
        config.setCode(uuid)
                .setMchNo(MchContextLocal.getMchNo());
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
        if (!cashierCodeConfigManager.existedById(id)){
            throw new DataNotExistException("收银码牌配置不存在");
        }
        // 删除类型配置
        codeItemConfigManager.deleteByCode(id);
        cashierCodeConfigManager.deleteById(id);
    }

    /**
     * 获取码牌地址
     */
    public String getCashierCodeUrl(Long id) {
        var codeConfig= cashierCodeConfigManager.findById(id).orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
        PlatformConfig platformConfig = platformConfigService.getConfig();
        String serverUrl = platformConfig.getGatewayMobileUrl();
        return StrUtil.format("{}/cashier/code/{}", serverUrl, codeConfig.getCode());
    }

    /**
     * 明细配置列表
     */
    public List<CashierCodeItemConfigResult> findItemByCodeId(Long codeId) {
        return MpUtil.toListResult(codeItemConfigManager.findAllByCodeId(codeId));
    }

    /**
     * 查询明细配置详情
     */
    public CashierCodeItemConfigResult findItemById(Long id) {
        return codeItemConfigManager.findById(id).map(CashierCodeItemConfig::toResult).orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
    }

    /**
     * 判断明细配置收银台类型是否存在
     */
    public boolean existsByTypeItem(String type, Long codeId) {
        return codeItemConfigManager.existsByType(type, codeId);
    }

    /**
     * 判断明细配置收银台类型是否存在
     */
    public boolean existsByTypeItem(String type, Long codeId, Long id) {
        return codeItemConfigManager.existsByType(type, codeId, id);
    }

    /**
     * 添加明细配置
     */
    public void saveItem(CashierCodeItemConfigParam param) {
        var codeConfig = cashierCodeConfigManager.findById(param.getCodeId())
                .orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
        // 收银码牌类型不能重复
        boolean existed = codeItemConfigManager.existsByType(param.getType(), param.getId());
        if (existed){
            throw new DataNotExistException("收银码牌类型不可重复配置");
        }

        CashierCodeItemConfig entity = CashierCodeItemConfig.init(param);
        entity.setAppId(codeConfig.getAppId())
                .setMchNo(codeConfig.getMchNo());
        codeItemConfigManager.save(entity);
    }

    /**
     * 更新明细配置
     */
    public void updateItem(CashierCodeItemConfigParam param) {
        // 收银码牌类型不能重复
        boolean existed = codeItemConfigManager.existsByType(param.getType(), param.getId(), param.getId());
        if (existed){
            throw new DataNotExistException("收银码牌类型不可重复配置");
        }
        var channelCashierConfig = codeItemConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
        BeanUtil.copyProperties(param, channelCashierConfig, CopyOptions.create().ignoreNullValue());
        codeItemConfigManager.updateById(channelCashierConfig);
    }
    /**
     * 删除明细配置
     */
    public void deleteItem(Long id) {
        if (!codeItemConfigManager.existedById(id)){
            throw new DataNotExistException("该码牌类型配置不存在");
        }
        codeItemConfigManager.deleteById(id);
    }

}
