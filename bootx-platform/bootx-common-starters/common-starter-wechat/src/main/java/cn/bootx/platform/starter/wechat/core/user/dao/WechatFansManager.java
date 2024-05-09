package cn.bootx.platform.starter.wechat.core.user.dao;

import cn.bootx.platform.starter.wechat.core.user.entity.WechatFans;
import cn.bootx.platform.starter.wechat.param.user.WechatFansParam;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 微信公众号粉丝
 *
 * @author xxm
 * @since 2022-07-16
 */
@Repository
@RequiredArgsConstructor
public class WechatFansManager extends BaseManager<WechatFansMapper, WechatFans> {

    /**
     * 分页
     */
    public Page<WechatFans> page(PageParam pageParam, WechatFansParam param) {
        Page<WechatFans> mpPage = MpUtil.getMpPage(pageParam, WechatFans.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId).page(mpPage);
    }

    /**
     * 获取最新的一条
     */
    public Optional<WechatFans> findLatest() {
        Page<WechatFans> mpPage = new Page<>(0, 1);
        Page<WechatFans> fansPage = this.lambdaQuery().orderByDesc(MpIdEntity::getId).page(mpPage);
        if (fansPage.getTotal() > 0) {
            return Optional.of(fansPage.getRecords().get(0));
        }
        return Optional.empty();
    }

}
