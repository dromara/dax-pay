package cn.bootx.platform.daxpay.core.channel.wechat.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.code.paymodel.WeChatPayWay;
import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPayConfigManager;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.dto.paymodel.wechat.WeChatPayConfigDto;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.param.paymodel.wechat.WeChatPayConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    /**
     * 添加微信支付配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(WeChatPayConfigParam param) {
        WeChatPayConfig weChatPayConfig = WeChatPayConfig.init(param);
        weChatPayConfig.setActivity(false);
        weChatPayConfigManager.save(weChatPayConfig);
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
     * 设置启用的支付宝配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void setUpActivity(Long id) {
        WeChatPayConfig weChatPayConfig = weChatPayConfigManager.findById(id)
            .orElseThrow(() -> new PayFailureException("微信支付配置不存在"));
        if (Objects.equals(weChatPayConfig.getActivity(), Boolean.TRUE)) {
            return;
        }
        weChatPayConfigManager.removeAllActivity();
        weChatPayConfig.setActivity(true);
        weChatPayConfigManager.updateById(weChatPayConfig);
    }

    /**
     * 清除启用状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearActivity(Long id) {
        WeChatPayConfig weChatPayConfig = weChatPayConfigManager.findById(id)
            .orElseThrow(() -> new PayFailureException("微信支付配置不存在"));
        if (Objects.equals(weChatPayConfig.getActivity(), Boolean.TRUE)) {
            return;
        }
        weChatPayConfig.setActivity(false);
        weChatPayConfigManager.updateById(weChatPayConfig);
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
