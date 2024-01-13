package cn.bootx.platform.daxpay.service.core.system.payinfo.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.service.core.system.payinfo.dao.PayChannelInfoManager;
import cn.bootx.platform.daxpay.service.core.system.payinfo.entity.PayChannelInfo;
import cn.bootx.platform.daxpay.service.dto.system.payinfo.PayChannelInfoDto;
import cn.bootx.platform.daxpay.service.param.system.payinfo.PayChannelInfoParam;
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
public class PayChannelInfoService {
    private final PayChannelInfoManager manager;

    /**
     * 列表
     */
    public List<PayChannelInfoDto> findAll(){
        return manager.findAll().stream()
                .map(PayChannelInfo::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 单条
     */
    public PayChannelInfoDto findById(Long id){
        return manager.findById(id).map(PayChannelInfo::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 设置是否启用
     */
    public void setEnable(String code,boolean enable){
        PayChannelInfo info = manager.findByCode(code)
                .orElseThrow(DataNotExistException::new);
        info.setEnabled(enable);
        manager.updateById(info);
    }

    /**
     * 更新
     */
    public void update(PayChannelInfoParam param){
        PayChannelInfo info = manager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param,info, CopyOptions.create().ignoreNullValue());
        manager.updateById(info);
    }
}
