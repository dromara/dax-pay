package cn.bootx.platform.daxpay.core.channel.wechat.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayConfigParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

/**
 * 微信支付配置
 *
 * @author xxm
 * @date 2021/3/19
 */
@Repository
@RequiredArgsConstructor
public class WeChatPayConfigManager extends BaseManager<WeChatPayConfigMapper, WeChatPayConfig> {

    private Optional<WeChatPayConfig> weChatPayConfig;

    @Override
    public WeChatPayConfig saveOrUpdate(WeChatPayConfig entity) {
        this.clearCache();
        return super.saveOrUpdate(entity);
    }

    @Override
    public WeChatPayConfig updateById(WeChatPayConfig weChatPayConfig) {
        this.clearCache();
        return super.updateById(weChatPayConfig);
    }

    /**
     * 获取启用的微信配置
     */
    public Optional<WeChatPayConfig> findActivity() {
        if (Objects.isNull(weChatPayConfig)) {
            weChatPayConfig = findByField(WeChatPayConfig::getActivity, Boolean.TRUE);
        }
        return weChatPayConfig;
    }

    /**
     * 分页
     */
    public Page<WeChatPayConfig> page(PageParam pageParam, WeChatPayConfigParam param) {
        Page<WeChatPayConfig> mpPage = MpUtil.getMpPage(pageParam, WeChatPayConfig.class);
        return lambdaQuery().select(WeChatPayConfig.class, MpUtil::excludeBigField)
            .like(StrUtil.isNotBlank(param.getName()), WeChatPayConfig::getName, param.getName())
            .like(StrUtil.isNotBlank(param.getAppId()), WeChatPayConfig::getAppId, param.getAppId())
            .like(StrUtil.isNotBlank(param.getAppId()), WeChatPayConfig::getMchId, param.getMchId())
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

    /**
     * 清除所有的被启用的
     */
    public void removeAllActivity() {
        this.clearCache();
        lambdaUpdate().eq(WeChatPayConfig::getActivity, Boolean.TRUE).set(WeChatPayConfig::getActivity, Boolean.FALSE);
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        weChatPayConfig = null;
    }

}
