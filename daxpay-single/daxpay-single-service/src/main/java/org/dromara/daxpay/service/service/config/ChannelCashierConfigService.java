package org.dromara.daxpay.service.service.config;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.service.dao.config.ChannelCashierConfigManage;
import org.dromara.daxpay.service.dao.merchant.MchAppManager;
import org.dromara.daxpay.service.entity.config.ChannelCashierConfig;
import org.dromara.daxpay.service.entity.config.PlatformConfig;
import org.dromara.daxpay.service.entity.merchant.MchApp;
import org.dromara.daxpay.service.param.config.ChannelCashierConfigParam;
import org.dromara.daxpay.service.result.config.ChannelCashierConfigResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通道支付收银台配置
 * @author xxm
 * @since 2024/9/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelCashierConfigService {
    private final MchAppManager mchAppManager;

    private final ChannelCashierConfigManage cashierConfigManage;

    private final PlatformConfigService platformConfigService;

    /**
     * 列表
     */
    public List<ChannelCashierConfigResult> findByAppId(String appId) {
        return MpUtil.toListResult(cashierConfigManage.findAllByAppId(appId));
    }

    /**
     * 查询详情
     */
    public ChannelCashierConfigResult findById(Long id) {
        return cashierConfigManage.findById(id).map(ChannelCashierConfig::toResult).orElseThrow(() -> new DataNotExistException("通道支付收银台配置不存在"));
    }

    /**
     * 根据类型查询
     */
    public ChannelCashierConfigResult findByCashierType(String cashierType) {
        return cashierConfigManage.findByCashierType(cashierType)
                .map(ChannelCashierConfig::toResult)
                .orElseThrow(() -> new DataNotExistException("通道支付收银台配置不存在"));
    }

    /**
     * 判断类型是否存在
     */
    public boolean existsByType(String type, String appId) {
        return cashierConfigManage.existsByType(type, appId);
    }

    /**
     * 判断类型是否存在
     */
    public boolean existsByType(String type, String appId, Long id) {
        return cashierConfigManage.existsByType(type, appId, id);
    }

    /**
     * 添加
     */
    public void save(ChannelCashierConfigParam param) {
        // 收银台类型不能重复
        boolean existed = cashierConfigManage.existedByField(ChannelCashierConfig::getCashierType, param.getCashierType());
        if (existed){
            throw new DataNotExistException("收银台类型不可重复配置");
        }
        cashierConfigManage.save(ChannelCashierConfig.init(param));
    }

    /**
     * 更新
     */
    public void update(ChannelCashierConfigParam param) {
        // 收银台类型不能重复
        boolean existed = cashierConfigManage.existedByField(ChannelCashierConfig::getCashierType, param.getCashierType(), param.getId());
        if (existed){
            throw new DataNotExistException("收银台类型不可重复配置");
        }
        ChannelCashierConfig channelCashierConfig = cashierConfigManage.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("通道支付收银台配置不存在"));
        BeanUtil.copyProperties(param, channelCashierConfig, CopyOptions.create().ignoreNullValue());
        cashierConfigManage.updateById(channelCashierConfig);
    }
    /**
     * 删除
     */
    public void delete(Long id) {
        cashierConfigManage.deleteById(id);
    }

    /**
     * 获取码牌地址
     */
    public String qrCodeUrl(String appId) {
        MchApp mchApp = mchAppManager.findByAppId(appId).orElseThrow(() -> new DataNotExistException("未找到指定的应用配置"));
        PlatformConfig platformConfig = platformConfigService.getConfig();
        String serverUrl = platformConfig.getGatewayMobileUrl();
        return StrUtil.format("{}/channel/cashier/{}", serverUrl, mchApp.getAppId());
    }
}
