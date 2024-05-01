package cn.bootx.platform.daxpay.service.core.system.payinfo.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.daxpay.service.core.system.payinfo.dao.PayMethodInfoManager;
import cn.bootx.platform.daxpay.service.core.system.payinfo.entity.PayMethodInfo;
import cn.bootx.platform.daxpay.service.dto.system.payinfo.PayMethodInfoDto;
import cn.bootx.platform.daxpay.service.param.system.payinfo.PayWayInfoParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 支付方式信息
 * @author xxm
 * @since 2024/1/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayMethodInfoService {
    private final PayMethodInfoManager manager;

    /**
     * 列表
     */
    public List<PayMethodInfoDto> findAll(){
        return manager.findAll().stream()
                .map(PayMethodInfo::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 单条
     */
    public PayMethodInfoDto findById(Long id){
        return ResultConvertUtil.dtoConvert(manager.findById(id));
    }

    /**
     * 更新
     */
    public void update(PayWayInfoParam param){
        PayMethodInfo info = manager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param,info, CopyOptions.create().ignoreNullValue());
        manager.updateById(info);
    }
}
