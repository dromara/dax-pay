package cn.bootx.platform.daxpay.core.merchant.service;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.code.pay.PayChannelCode;
import cn.bootx.platform.daxpay.core.merchant.dao.MchAppPayConfigManager;
import cn.bootx.platform.daxpay.core.merchant.entity.MchAppPayConfig;
import cn.bootx.platform.daxpay.dto.merchant.MchAppPayConfigDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商户应用支付配置
 * @author xxm
 * @date 2023-05-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MchAppPayConfigService {
    private final MchAppPayConfigManager mchAppPayConfigManager;

    /**
     * 根据应用ID删除
     */
    public void deleteByAppId(Long appId){
        mchAppPayConfigManager.deleteByField(MchAppPayConfig::getAppId,appId);
    }

    /**
     * 支付渠道配置列表
     */
    public List<MchAppPayConfigDto> ListByApp(PageParam pageParam,Long appId){
        val mchAppPayConfigMap = mchAppPayConfigManager.findAllByField(MchAppPayConfig::getAppId, appId)
                .stream()
                .map(MchAppPayConfig::toDto)
                .collect(Collectors.toMap(MchAppPayConfigDto::getChannel, Function.identity()));
        // 进行排序并返回
        return PayChannelCode.SORT_LIST.stream()
                .map(mchAppPayConfigMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
