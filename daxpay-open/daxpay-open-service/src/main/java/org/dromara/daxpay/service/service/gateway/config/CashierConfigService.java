package org.dromara.daxpay.service.service.gateway.config;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.core.enums.GatewayCashierTypeEnum;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.gateway.CashierGroupConfigManager;
import org.dromara.daxpay.service.dao.gateway.CashierItemConfigManager;
import org.dromara.daxpay.service.entity.gateway.CashierGroupConfig;
import org.dromara.daxpay.service.entity.gateway.CashierItemConfig;
import org.dromara.daxpay.service.param.gateway.CashierGroupConfigParam;
import org.dromara.daxpay.service.param.gateway.CashierItemConfigParam;
import org.dromara.daxpay.service.result.gateway.config.CashierGroupConfigResult;
import org.dromara.daxpay.service.result.gateway.config.CashierItemConfigResult;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 收银台配置
 * @author xxm
 * @since 2025/3/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierConfigService {
    private final CashierGroupConfigManager cashierGroupConfigManager;
    private final CashierItemConfigManager cashierItemConfigManager;
    private final PaymentAssistService paymentAssistService;

    /**
     * 获取指定类型收银台分组列表
     */
    public List<CashierGroupConfigResult> listByGroup(String appId, String gatewayType){
        return MpUtil.toListResult(cashierGroupConfigManager.findAllByAppIdAndType(appId, gatewayType));
    }

    /**
     * 获取收银台分组配置
     */
    public CashierGroupConfigResult findGroupById(Long id){
        return cashierGroupConfigManager.findById(id).map(CashierGroupConfig::toResult)
                .orElseThrow(() -> new DataNotExistException("收银台分组配置不存在"));
    }

    /**
     * 获取配置项列表
     */
    public List<CashierItemConfigResult> listByItem(Long groupId){
        return MpUtil.toListResult(cashierItemConfigManager.findAllByGroupId(groupId));
    }

    /**
     * 获取配置项
     */
    public CashierItemConfigResult findItemById(Long groupId){
        return cashierItemConfigManager.findById(groupId)
                .map(CashierItemConfig::toResult)
                .orElseThrow(() -> new DataNotExistException("收银台配置项不存在"));
    }

    /**
     * 保存分组配置
     */
    public void saveGroup(CashierGroupConfigParam param) {
        // h5只能创建一个
        if (Objects.equals(param.getCashierType(), GatewayCashierTypeEnum.H5.getCode()) &&
                cashierGroupConfigManager.existedByType(param.getCashierType(), param.getAppId())){
            throw new DataErrorException("H5网关支付分组只能有一个");
        }
        paymentAssistService.initMchAndApp(param.getAppId());
        var mchApp = PaymentContextLocal.get().getMchAppInfo();
        var entity = CashierGroupConfig.init(param);
        entity.setMchNo(mchApp.getMchNo());
        cashierGroupConfigManager.save(entity);
    }

    /**
     * 更新分组配置
     */
    public void updateGroup(CashierGroupConfigParam param) {
        var cashierGroupConfig = cashierGroupConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("分组配置不存在"));
        BeanUtil.copyProperties(param, cashierGroupConfig, CopyOptions.create().ignoreNullValue());
        cashierGroupConfigManager.updateById(cashierGroupConfig);
    }

    /**
     * 删除分组配置
     */
    public void deleteGroup(Long id) {
        if (!cashierGroupConfigManager.existedById(id)){
            throw new DataNotExistException("分组配置不存在");
        }
        if (cashierItemConfigManager.existedByGroupId(id)){
            throw new OperationFailException("该分组下存在配置项，请先删除配置项");
        }
        cashierGroupConfigManager.deleteById(id);
    }

    /**
     * 保存配置项
     */
    public void saveItem(CashierItemConfigParam param) {
        if (!cashierGroupConfigManager.existedById(param.getGroupId())){
            throw new DataNotExistException("所属分组配置不存在");
        }
        paymentAssistService.initMchAndApp(param.getAppId());
        var mchApp = PaymentContextLocal.get().getMchAppInfo();
        var entity = CashierItemConfig.init(param);
        entity.setMchNo(mchApp.getMchNo());
        cashierItemConfigManager.save(entity);

    }

    /**
     * 更新配置项
     */
    public void updateItem(CashierItemConfigParam param) {
        CashierItemConfig cashierItemConfig = cashierItemConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("配置项不存在"));
        BeanUtil.copyProperties(param, cashierItemConfig, CopyOptions.create().ignoreNullValue());
        cashierItemConfigManager.updateById(cashierItemConfig);
    }

    /**
     * 删除配置项
     */
    public void deleteItem(Long id) {
        if (!cashierItemConfigManager.existedById(id)){
            throw new DataNotExistException("配置项不存在");
        }
        cashierItemConfigManager.deleteById(id);
    }
}
