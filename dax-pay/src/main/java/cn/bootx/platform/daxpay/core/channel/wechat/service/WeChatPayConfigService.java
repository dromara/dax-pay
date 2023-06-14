package cn.bootx.platform.daxpay.core.channel.wechat.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.paymodel.WeChatPayWay;
import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPayConfigManager;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.core.merchant.dao.MchAppPayConfigManager;
import cn.bootx.platform.daxpay.core.merchant.entity.MchAppPayConfig;
import cn.bootx.platform.daxpay.core.merchant.service.MchAppService;
import cn.bootx.platform.daxpay.dto.channel.wechat.WeChatPayConfigDto;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信支付配置
 *
 * @author xxm
 * @date 2021/3/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayConfigService {

    private final WeChatPayConfigManager weChatPayConfigManager;
    private final MchAppService mchAppService;
    private final MchAppPayConfigManager mchAppPayConfigManager;

    /**
     * 添加微信支付配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(WeChatPayConfigParam param) {
        // 是否有管理关系判断
        if (mchAppService.checkMatch(param.getMchCode(), param.getMchAppCode())) {
            throw new BizException("应用信息与商户信息不匹配");
        }

        WeChatPayConfig weChatPayConfig = WeChatPayConfig.init(param);
        weChatPayConfigManager.save(weChatPayConfig);
        // 保存关联关系
        MchAppPayConfig mchAppPayConfig = new MchAppPayConfig().setAppCode(weChatPayConfig.getMchAppCode())
                .setConfigId(weChatPayConfig.getId())
                .setChannel(PayChannelEnum.WECHAT.getCode())
                .setState(weChatPayConfig.getState());
        mchAppPayConfigManager.save(mchAppPayConfig);
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(WeChatPayConfigParam param) {
        WeChatPayConfig weChatPayConfig = weChatPayConfigManager.findById(param.getId())
            .orElseThrow(() -> new PayFailureException("微信支付配置不存在"));
        param.setActivity(null);
        BeanUtil.copyProperties(param, weChatPayConfig, CopyOptions.create().ignoreNullValue());
        // 支付方式
        if (CollUtil.isNotEmpty(param.getPayWayList())) {
            weChatPayConfig.setPayWays(String.join(",", param.getPayWayList()));
        }
        else {
            weChatPayConfig.setPayWays(null);
        }
        weChatPayConfigManager.updateById(weChatPayConfig);
    }

    /**
     * 分页
     */
    public PageResult<WeChatPayConfigDto> page(PageParam pageParam, WeChatPayConfigParam param) {
        return MpUtil.convert2DtoPageResult(weChatPayConfigManager.page(pageParam, param));
    }

    /**
     * 获取
     */
    public WeChatPayConfigDto findById(Long id) {
        return weChatPayConfigManager.findById(id).map(WeChatPayConfig::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 微信支持支付方式
     */
    public List<KeyValue> findPayWayList() {
        return WeChatPayWay.getPayWays()
            .stream()
            .map(e -> new KeyValue(e.getCode(), e.getName()))
            .collect(Collectors.toList());
    }

}
