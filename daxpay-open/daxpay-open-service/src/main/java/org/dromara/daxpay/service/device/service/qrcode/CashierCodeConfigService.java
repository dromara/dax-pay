package org.dromara.daxpay.service.device.service.qrcode;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.device.dao.qrcode.config.CashierCodeConfigManager;
import org.dromara.daxpay.service.device.dao.qrcode.config.CashierCodeSceneConfigManager;
import org.dromara.daxpay.service.device.entity.qrcode.config.CashierCodeConfig;
import org.dromara.daxpay.service.device.entity.qrcode.config.CashierCodeSceneConfig;
import org.dromara.daxpay.service.device.param.qrcode.config.CashierCodeConfigParam;
import org.dromara.daxpay.service.device.param.qrcode.config.CashierCodeConfigQuery;
import org.dromara.daxpay.service.device.param.qrcode.config.CashierCodeSceneConfigParam;
import org.dromara.daxpay.service.device.result.qrcode.config.CashierCodeConfigResult;
import org.dromara.daxpay.service.device.result.qrcode.config.CashierCodeSceneConfigResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    private final CashierCodeSceneConfigManager codeItemConfigManager;


    /**
     * 添加
     */
    public void save(CashierCodeConfigParam param) {
        CashierCodeConfig config = CashierCodeConfig.init(param);
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
     * 分页
     */
    public PageResult<CashierCodeConfigResult> page(PageParam pageParam, CashierCodeConfigQuery query) {
        return MpUtil.toPageResult(cashierCodeConfigManager.page(pageParam, query));
    }

    /**
     * 下拉列表
     */
    public List<LabelValue> dropdown() {
        return cashierCodeConfigManager.findAll().stream()
                .map(o->new LabelValue(o.getName(),o.getId()))
                .collect(Collectors.toList());
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
        // 删除支付场景配置
        codeItemConfigManager.deleteByConfigId(id);
        cashierCodeConfigManager.deleteById(id);
    }

    /**
     * 场景配置列表
     */
    public List<CashierCodeSceneConfigResult> findSceneByConfigId(Long configId) {
        return MpUtil.toListResult(codeItemConfigManager.findAllByConfigId(configId));
    }

    /**
     * 查询场景配置详情
     */
    public CashierCodeSceneConfigResult findItemById(Long id) {
        return codeItemConfigManager.findById(id).map(CashierCodeSceneConfig::toResult).orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
    }

    /**
     * 判断收银场景配置是否存在
     */
    public boolean existsByScene(String scene, Long configId) {
        return codeItemConfigManager.existsByScene(scene, configId);
    }

    /**
     * 判断收银场景配置是否存在
     */
    public boolean existsByScene(String scene, Long configId, Long id) {
        return codeItemConfigManager.existsByScene(scene, configId, id);
    }

    /**
     * 添加明细配置
     */
    public void saveScene(CashierCodeSceneConfigParam param) {
        if (!cashierCodeConfigManager.existedById(param.getConfigId())){
            throw new DataNotExistException("收银码牌配置不存在");
        }
        // 收银场景不能重复
        boolean existed = codeItemConfigManager.existsByScene(param.getScene(), param.getId());
        if (existed){
            throw new DataNotExistException("收银场景不可重复配置");
        }

        CashierCodeSceneConfig entity = CashierCodeSceneConfig.init(param);
        codeItemConfigManager.save(entity);
    }

    /**
     * 更新明细配置
     */
    public void updateScene(CashierCodeSceneConfigParam param) {
        // 收银场景不能重复
        boolean existed = codeItemConfigManager.existsByScene(param.getScene(), param.getId(), param.getId());
        if (existed){
            throw new DataNotExistException("收银场景不可重复配置");
        }
        var channelCashierConfig = codeItemConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
        BeanUtil.copyProperties(param, channelCashierConfig, CopyOptions.create().ignoreNullValue());
        codeItemConfigManager.updateById(channelCashierConfig);
    }

    /**
     * 删除明细配置
     */
    public void deleteScene(Long id) {
        if (!codeItemConfigManager.existedById(id)){
            throw new DataNotExistException("该支付场景配置不存在");
        }
        codeItemConfigManager.deleteById(id);
    }
}
