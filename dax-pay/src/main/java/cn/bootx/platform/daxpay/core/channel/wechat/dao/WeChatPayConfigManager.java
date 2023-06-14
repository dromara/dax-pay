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


    public Optional<WeChatPayConfig> findByMchAppCode(String mchAppCode){
        return findByField(WeChatPayConfig::getMchAppCode,mchAppCode);
    }
    /**
     * 分页
     */
    public Page<WeChatPayConfig> page(PageParam pageParam, WeChatPayConfigParam param) {
        Page<WeChatPayConfig> mpPage = MpUtil.getMpPage(pageParam, WeChatPayConfig.class);
        return lambdaQuery().select(WeChatPayConfig.class, MpUtil::excludeBigField)
            .like(StrUtil.isNotBlank(param.getName()), WeChatPayConfig::getName, param.getName())
            .like(StrUtil.isNotBlank(param.getAppId()), WeChatPayConfig::getWxAppId, param.getAppId())
            .like(StrUtil.isNotBlank(param.getAppId()), WeChatPayConfig::getWxMchId, param.getMchId())
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }
}
