package cn.daxpay.single.service.core.system.config.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.daxpay.single.service.core.system.config.dao.PayChannelConfigManager;
import cn.daxpay.single.service.core.system.config.entity.PayChannelConfig;
import cn.daxpay.single.service.dto.system.config.PayChannelConfigDto;
import cn.daxpay.single.service.param.system.payinfo.PayChannelInfoParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 支付通道信息
 * @author xxm
 * @since 2024/1/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayChannelConfigService {
    private final PayChannelConfigManager manager;

    /**
     * 列表
     */
    public List<PayChannelConfigDto> findAll(){
        return manager.findAll().stream()
                .map(PayChannelConfig::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 单条
     */
    public PayChannelConfigDto findById(Long id){
        return manager.findById(id).map(PayChannelConfig::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 设置是否启用
     */
    public void setEnable(String code,boolean enable){
        PayChannelConfig info = manager.findByCode(code)
                .orElseThrow(DataNotExistException::new);
        info.setEnable(enable);
        manager.updateById(info);
    }

    /**
     * 更新
     */
    public void update(PayChannelInfoParam param){
        PayChannelConfig info = manager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param,info, CopyOptions.create().ignoreNullValue());
        manager.updateById(info);
    }
}
