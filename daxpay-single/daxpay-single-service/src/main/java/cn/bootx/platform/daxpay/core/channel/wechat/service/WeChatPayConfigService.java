package cn.bootx.platform.daxpay.core.channel.wechat.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.code.WeChatPayWay;
import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPayConfigManager;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.dto.channel.wechat.WeChatPayConfigDto;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
 * @since 2021/3/5
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
    public List<LabelValue> findPayWayList() {
        return WeChatPayWay.getPayWays()
            .stream()
            .map(e -> new LabelValue(e.getName(),e.getCode()))
            .collect(Collectors.toList());
    }

}
