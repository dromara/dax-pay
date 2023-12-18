package cn.bootx.platform.daxpay.core.channel.wechat.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayConfigParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 微信支付配置
 *
 * @author xxm
 * @since 2021/3/19
 */
@Repository
@RequiredArgsConstructor
public class WeChatPayConfigManager extends BaseManager<WeChatPayConfigMapper, WeChatPayConfig> {

    /**
     * 分页
     */
    public Page<WeChatPayConfig> page(PageParam pageParam, WeChatPayConfigParam param) {
        Page<WeChatPayConfig> mpPage = MpUtil.getMpPage(pageParam, WeChatPayConfig.class);
        QueryWrapper<WeChatPayConfig> wrapper = QueryGenerator.generator(param);
        wrapper.orderByDesc(MpIdEntity.Id.id);
        return this.page(mpPage,wrapper);
    }
}
