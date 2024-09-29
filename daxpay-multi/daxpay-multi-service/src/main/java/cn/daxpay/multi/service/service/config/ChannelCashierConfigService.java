package cn.daxpay.multi.service.service.config;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.daxpay.multi.service.dao.config.ChannelCashierConfigManage;
import cn.daxpay.multi.service.entity.config.ChannelCashierConfig;
import cn.daxpay.multi.service.param.config.ChannelCashierConfigParam;
import cn.daxpay.multi.service.result.config.ChannelCashierConfigResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
    private final ChannelCashierConfigManage cashierConfigManage;

    /**
     * 分页
     */
    public List<ChannelCashierConfigResult> findByAppId(String appId) {
        return MpUtil.toListResult(cashierConfigManage.findAll());
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

}
