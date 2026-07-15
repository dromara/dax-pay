package cn.daxpay.open.plugin.risk.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.plugin.risk.entity.PayBlacklist;
import cn.daxpay.open.plugin.risk.enums.PayBlacklistStatusEnum;
import cn.daxpay.open.plugin.risk.param.PayBlacklistQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/// # 黑名单 Manager
///
@Repository
public class PayBlacklistManager extends BaseManager<PayBlacklistMapper, PayBlacklist> {

    /// 分页
    public Page<PayBlacklist> page(PageParam pageParam, PayBlacklistQuery query) {
        Page<PayBlacklist> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayBlacklist> wrapper = QueryGenerator.generator(query);
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// 查重：同 type+value+channel+channelAppId（空按空串）
    public boolean existsDuplicate(String type, String value, String channel, String channelAppId, Long excludeId) {
        String ch = StrUtil.nullToEmpty(channel);
        String app = StrUtil.nullToEmpty(channelAppId);
        return lambdaQuery()
                .eq(PayBlacklist::getType, type)
                .eq(PayBlacklist::getValue, value)
                .and(w -> {
                    if (StrUtil.isBlank(ch)) {
                        w.and(x -> x.isNull(PayBlacklist::getChannel).or().eq(PayBlacklist::getChannel, ""));
                    } else {
                        w.eq(PayBlacklist::getChannel, ch);
                    }
                })
                .and(w -> {
                    if (StrUtil.isBlank(app)) {
                        w.and(x -> x.isNull(PayBlacklist::getChannelAppId).or().eq(PayBlacklist::getChannelAppId, ""));
                    } else {
                        w.eq(PayBlacklist::getChannelAppId, app);
                    }
                })
                .ne(excludeId != null, PayBlacklist::getId, excludeId)
                .exists();
    }

    /// 查找有效命中行（enable 且未过期）
    public Optional<PayBlacklist> findActiveHit(String type, String value, String channel, String channelAppId) {
        if (StrUtil.isBlank(type) || StrUtil.isBlank(value)) {
            return Optional.empty();
        }
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        List<PayBlacklist> list = lambdaQuery()
                .eq(PayBlacklist::getType, type)
                .eq(PayBlacklist::getValue, value)
                .eq(PayBlacklist::getStatus, PayBlacklistStatusEnum.ENABLE.getCode())
                .and(w -> w.isNull(PayBlacklist::getExpireTime).or().gt(PayBlacklist::getExpireTime, now))
                .list();
        if (list.isEmpty()) {
            return Optional.empty();
        }
        // 优先精确 channel + channelAppId；再宽匹配 channel 空
        Optional<PayBlacklist> exact = list.stream()
                .filter(e -> matchNullable(e.getChannel(), channel) && matchNullable(e.getChannelAppId(), channelAppId))
                .findFirst();
        if (exact.isPresent()) {
            return exact;
        }
        // 名单 channel 为空视为对该 value 全局生效
        return list.stream()
                .filter(e -> StrUtil.isBlank(e.getChannel()) && StrUtil.isBlank(e.getChannelAppId()))
                .findFirst();
    }

    private static boolean matchNullable(String rule, String actual) {
        if (StrUtil.isBlank(rule)) {
            return StrUtil.isBlank(actual);
        }
        return rule.equals(actual);
    }
}
