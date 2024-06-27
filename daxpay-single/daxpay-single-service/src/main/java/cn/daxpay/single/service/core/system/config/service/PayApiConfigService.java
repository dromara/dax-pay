package cn.daxpay.single.service.core.system.config.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.daxpay.single.service.core.system.config.dao.PayApiConfigManager;
import cn.daxpay.single.service.core.system.config.entity.PayApiConfig;
import cn.daxpay.single.service.dto.system.config.PayApiConfigDto;
import cn.daxpay.single.service.param.system.config.PayApiConfigParam;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 开放接口信息
 * @author xxm
 * @since 2023/12/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayApiConfigService {
    private final PayApiConfigManager openApiInfoManager;

    /**
     * 更新
     */
    public void update(PayApiConfigParam param){
        PayApiConfig payApiConfig = openApiInfoManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("支付接口配置信息不存在"));
        BeanUtil.copyProperties(param, payApiConfig);
        openApiInfoManager.updateById(payApiConfig);
    }

    /**
     * 分页
     */
    public List<PayApiConfigDto> findAll(){
        return ResultConvertUtil.dtoListConvert(openApiInfoManager.findAll());
    }

    /**
     * 根据ID获取
     */
    public PayApiConfigDto findById(Long id){
        return openApiInfoManager.findById(id).map(PayApiConfig::toDto).orElseThrow(() -> new DataNotExistException("支付接口配置信息不存在"));
    }
}
