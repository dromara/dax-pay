package cn.bootx.platform.daxpay.core.channel.config.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.channel.config.dao.PayChannelConfigManager;
import cn.bootx.platform.daxpay.core.channel.config.entity.PayChannelConfig;
import cn.bootx.platform.daxpay.dto.channel.config.PayChannelConfigDto;
import cn.bootx.platform.daxpay.param.channel.config.PayChannelConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付通道配置
 * @author xxm
 * @date 2023-05-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayChannelConfigService {
    private final PayChannelConfigManager configManager;

    /**
     * 添加
     */
    public void add(PayChannelConfigParam param){
        PayChannelConfig payChannelConfig = PayChannelConfig.init(param);
        // 编码不能重复
        if (configManager.existedByField(PayChannelConfig::getCode,param.getCode())){
            throw new BizException("编码已存在");
        }
        configManager.save(payChannelConfig);
    }

    /**
     * 修改
     */
    public void update(PayChannelConfigParam param){
        PayChannelConfig payChannelConfig = configManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        // 编码不能重复
        if (configManager.existedByField(PayChannelConfig::getCode,param.getCode(),param.getId())){
            throw new BizException("编码已存在");
        }
        BeanUtil.copyProperties(param,payChannelConfig, CopyOptions.create().ignoreNullValue());
        configManager.updateById(payChannelConfig);
    }

    /**
     * 分页
     */
    public PageResult<PayChannelConfigDto> page(PageParam pageParam,PayChannelConfigParam payChannelConfigParam){
        return MpUtil.convert2DtoPageResult(configManager.page(pageParam,payChannelConfigParam));
    }

    /**
     * 获取单条
     */
    public PayChannelConfigDto findById(Long id){
        return configManager.findById(id).map(PayChannelConfig::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 删除
     */
    public void delete(Long id){
        configManager.deleteById(id);
    }
}
