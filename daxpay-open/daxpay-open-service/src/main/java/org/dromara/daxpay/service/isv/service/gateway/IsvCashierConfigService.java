package org.dromara.daxpay.service.isv.service.gateway;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.core.enums.GatewayCashierTypeEnum;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.service.isv.dao.gateway.IsvCashierGroupConfigManager;
import org.dromara.daxpay.service.isv.dao.gateway.IsvCashierItemConfigManager;
import org.dromara.daxpay.service.isv.entity.gateway.IsvCashierGroupConfig;
import org.dromara.daxpay.service.isv.entity.gateway.IsvCashierItemConfig;
import org.dromara.daxpay.service.isv.param.gateway.IsvCashierGroupConfigParam;
import org.dromara.daxpay.service.isv.param.gateway.IsvCashierItemConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvCashierGroupConfigResult;
import org.dromara.daxpay.service.isv.result.gateway.IsvCashierItemConfigResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 收银台配置
 *
 * @author xxm
 * @since 2025/3/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IsvCashierConfigService {
    private final IsvCashierGroupConfigManager cashierGroupConfigManager;
    private final IsvCashierItemConfigManager cashierItemConfigManager;

    /**
     * 获取指定类型收银台分组列表
     */
    public List<IsvCashierGroupConfigResult> listByGroup(String cashierType) {
        return MpUtil.toListResult(cashierGroupConfigManager.findAllByType(cashierType));
    }

    /**
     * 获取收银台分组配置
     */
    public IsvCashierGroupConfigResult findGroupById(Long id) {
        return cashierGroupConfigManager.findById(id)
                .map(IsvCashierGroupConfig::toResult)
                .orElseThrow(() -> new DataNotExistException("收银台分组配置不存在"));
    }

    /**
     * 获取配置项列表
     */
    public List<IsvCashierItemConfigResult> listByItem(Long groupId) {
        return MpUtil.toListResult(cashierItemConfigManager.findAllByGroupId(groupId));
    }

    /**
     * 获取配置项
     */
    public IsvCashierItemConfigResult findItemById(Long groupId) {
        return cashierItemConfigManager.findById(groupId)
                .map(IsvCashierItemConfig::toResult)
                .orElseThrow(() -> new DataNotExistException("收银台配置项不存在"));
    }

    /**
     * 保存分组配置
     */
    public void saveGroup(IsvCashierGroupConfigParam param) {
        // h5只能创建一个
        if (Objects.equals(param.getCashierType(), GatewayCashierTypeEnum.H5.getCode()) &&
                cashierGroupConfigManager.existedByType(param.getCashierType())) {
            throw new DataErrorException("H5网关支付分组只能有一个");
        }
        var entity = IsvCashierGroupConfig.init(param);
        cashierGroupConfigManager.save(entity);
    }

    /**
     * 保存默认分组配置(h5)
     */
    public void saveDefaultGroup() {
        // h5只能创建一个
        if (cashierGroupConfigManager.existedByType(GatewayCashierTypeEnum.H5.getCode())) {
            throw new DataErrorException("H5网关支付分组只能有一个");
        }
        var entity = new IsvCashierGroupConfig()
                .setCashierType(GatewayCashierTypeEnum.H5.getCode())
                .setSortNo(0.0)
                .setName("H5收银台");

        cashierGroupConfigManager.save(entity);
    }

    /**
     * 更新分组配置
     */
    public void updateGroup(IsvCashierGroupConfigParam param) {
        var cashierGroupConfig = cashierGroupConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("分组配置不存在"));
        BeanUtil.copyProperties(param, cashierGroupConfig, CopyOptions.create()
                .ignoreNullValue());
        cashierGroupConfigManager.updateById(cashierGroupConfig);
    }

    /**
     * 删除分组配置
     */
    public void deleteGroup(Long id) {
        if (!cashierGroupConfigManager.existedById(id)) {
            throw new DataNotExistException("分组配置不存在");
        }
        if (cashierItemConfigManager.existedByGroupId(id)) {
            throw new OperationFailException("该分组下存在配置项，请先删除配置项");
        }
        cashierGroupConfigManager.deleteById(id);
    }

    /**
     * 保存配置项
     */
    public void saveItem(IsvCashierItemConfigParam param) {
        if (!cashierGroupConfigManager.existedById(param.getGroupId())) {
            throw new DataNotExistException("所属分组配置不存在");
        }
        var entity = IsvCashierItemConfig.init(param);
        cashierItemConfigManager.save(entity);

    }

    /**
     * 更新配置项
     */
    public void updateItem(IsvCashierItemConfigParam param) {
        var cashierItemConfig = cashierItemConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("配置项不存在"));
        BeanUtil.copyProperties(param, cashierItemConfig, CopyOptions.create()
                .ignoreNullValue());
        cashierItemConfigManager.updateById(cashierItemConfig);
    }

    /**
     * 删除配置项
     */
    public void deleteItem(Long id) {
        if (!cashierItemConfigManager.existedById(id)) {
            throw new DataNotExistException("配置项不存在");
        }
        cashierItemConfigManager.deleteById(id);
    }
}
