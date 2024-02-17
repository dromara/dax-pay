package cn.bootx.platform.daxpay.service.core.channel.voucher.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.code.VoucherPayWay;
import cn.bootx.platform.daxpay.service.core.channel.voucher.dao.VoucherConfigManager;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.VoucherConfig;
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
 * 储值卡配置
 * @author xxm
 * @since 2024/2/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherConfigService {
    private final VoucherConfigManager manager;

    private final static Long ID = 0L;
    private final PayChannelConfigService payChannelConfigService;

    /**
     * 获取钱包配置
     */
    public VoucherConfig getConfig(){
        return manager.findById(ID).orElseThrow(() -> new DataNotExistException("钱包配置不存在"));
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(WalletConfigParam param){
        VoucherConfig walletConfig = manager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        // 启用或停用
        if (!Objects.equals(param.getEnable(), walletConfig.getEnable())){
            payChannelConfigService.setEnable(PayChannelEnum.VOUCHER.getCode(), param.getEnable());
        }
        BeanUtil.copyProperties(param,walletConfig);
        manager.updateById(walletConfig);
    }

    public List<LabelValue> findPayWays() {
        return VoucherPayWay.getPayWays()
                .stream()
                .map(e -> new LabelValue(e.getName(),e.getCode()))
                .collect(Collectors.toList());
    }
}
