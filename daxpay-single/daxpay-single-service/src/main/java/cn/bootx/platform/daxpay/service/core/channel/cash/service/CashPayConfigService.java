package cn.bootx.platform.daxpay.service.core.channel.cash.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.code.WalletPayWay;
import cn.bootx.platform.daxpay.service.core.channel.cash.dao.CashConfigManager;
import cn.bootx.platform.daxpay.service.core.channel.cash.entity.CashConfig;
import cn.bootx.platform.daxpay.service.core.system.config.service.PayChannelConfigService;
import cn.bootx.platform.daxpay.service.param.channel.wechat.WalletConfigParam;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 现金支付配置
 * @author xxm
 * @since 2024/2/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashPayConfigService {
    private final static Long ID = 0L;
    private final CashConfigManager cashConfigManager;
    private final PayChannelConfigService payChannelConfigService;

    /**
     * 获取钱包配置
     */
    public CashConfig getConfig(){
        return cashConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("钱包配置不存在"));
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(WalletConfigParam param){
        CashConfig walletConfig = cashConfigManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        // 启用或停用
        if (!Objects.equals(param.getEnable(), walletConfig.getEnable())){
            payChannelConfigService.setEnable(PayChannelEnum.CASH.getCode(), param.getEnable());
        }
        BeanUtil.copyProperties(param,walletConfig);
        cashConfigManager.updateById(walletConfig);
    }

    public List<LabelValue> findPayWays() {
        return WalletPayWay.getPayWays()
                .stream()
                .map(e -> new LabelValue(e.getName(),e.getCode()))
                .collect(Collectors.toList());
    }
}
