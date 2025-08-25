package org.dromara.daxpay.service.isv.service.gateway;

import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.service.isv.dao.gateway.IsvGatewayPayConfigManager;
import org.dromara.daxpay.service.isv.entity.gateway.IsvGatewayPayConfig;
import org.dromara.daxpay.service.isv.param.gateway.IsvGatewayPayConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvGatewayPayConfigResult;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 网关支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IsvGatewayPayConfigService {
    private final IsvGatewayPayConfigManager gatewayPayConfigManager;

    /**
     * 获取网关支付配置
     */
    public IsvGatewayPayConfigResult getConfig(){
        var gatewayPayConfig = gatewayPayConfigManager.findFirst();
        if (gatewayPayConfig.isEmpty()){
            var payConfig = new IsvGatewayPayConfig();
            gatewayPayConfigManager.save(payConfig);
            return payConfig.toResult();
        }
        return gatewayPayConfig.get().toResult();
    }

    /**
     * 更新网关支付配置
     */
    public void update(IsvGatewayPayConfigParam param){
        var gatewayPayConfig = gatewayPayConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("收银台网关支付不存在"));
        BeanUtil.copyProperties(param, gatewayPayConfig);
        gatewayPayConfigManager.updateById(gatewayPayConfig);
    }

}
