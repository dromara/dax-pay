package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.AliPayWay;
import cn.bootx.platform.daxpay.service.core.channel.union.dao.UnionPayConfigManager;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.system.config.service.PayChannelConfigService;
import cn.bootx.platform.daxpay.service.param.channel.alipay.AliPayConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 云闪付支付配置
 * @author xxm
 * @since 2022/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayConfigService {
    /** 默认云闪付配置的主键ID */
    private final static Long ID = 0L;
    private final UnionPayConfigManager unionPayConfigManager;
    private final PayChannelConfigService payChannelConfigService;

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(AliPayConfigParam param) {
        UnionPayConfig unionPayConfig = unionPayConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("支付宝配置不存在"));
        // 启用或停用
        if (!Objects.equals(param.getEnable(), unionPayConfig.getEnable())){
            payChannelConfigService.setEnable(PayChannelEnum.UNION_PAY.getCode(), param.getEnable());
        }

        BeanUtil.copyProperties(param, unionPayConfig, CopyOptions.create().ignoreNullValue());
        unionPayConfigManager.updateById(unionPayConfig);
    }

    /**
     * 支付宝支持支付方式
     */
    public List<LabelValue> findPayWays() {
        return AliPayWay.getPayWays()
                .stream()
                .map(e -> new LabelValue(e.getName(),e.getCode()))
                .collect(Collectors.toList());
    }

    /**
     * 获取支付配置
     */
    public UnionPayConfig getConfig(){
        return unionPayConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("支付宝配置不存在"));
    }

    /**
     * 获取并检查支付配置
     */
    public UnionPayConfig getAndCheckConfig() {
        UnionPayConfig unionPayConfig = this.getConfig();
        if (!unionPayConfig.getEnable()){
            throw new PayFailureException("云闪付支付未启用");
        }
        return unionPayConfig;
    }

}
