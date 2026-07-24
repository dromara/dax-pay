package cn.daxpay.open.plugin.risk.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.plugin.risk.entity.PayBlacklist;
import cn.daxpay.open.plugin.risk.enums.PayBlacklistStatusEnum;
import cn.daxpay.open.plugin.risk.enums.PayBlacklistTypeEnum;
import cn.daxpay.open.plugin.risk.param.PayBlacklistQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

    /// 查重：同 type+value+wxAppId（空按空串）
    public boolean existsDuplicate(String type, String value, String wxAppId, Long excludeId) {
        String app = StrUtil.nullToEmpty(wxAppId);
        return lambdaQuery()
                .eq(PayBlacklist::getType, type)
                .eq(PayBlacklist::getValue, value)
                .and(w -> {
                    if (StrUtil.isBlank(app)) {
                        w.and(x -> x.isNull(PayBlacklist::getWxAppId).or().eq(PayBlacklist::getWxAppId, ""));
                    } else {
                        w.eq(PayBlacklist::getWxAppId, app);
                    }
                })
                .ne(excludeId != null, PayBlacklist::getId, excludeId)
                .exists();
    }

    /// 查找有效命中行（enable 且未过期）
    ///
    /// - ip / alipay_user：按 type+value，忽略 wxAppId
    /// - wechat_openid：按 type+value+wxAppId 精确匹配（wxAppId 为空则不命中）
    public Optional<PayBlacklist> findActiveHit(String type, String value, String wxAppId) {
        if (StrUtil.isBlank(type) || StrUtil.isBlank(value)) {
            return Optional.empty();
        }
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        var query = lambdaQuery()
                .eq(PayBlacklist::getType, type)
                .eq(PayBlacklist::getValue, value)
                .eq(PayBlacklist::getStatus, PayBlacklistStatusEnum.ENABLE.getCode())
                .and(w -> w.isNull(PayBlacklist::getExpireTime).or().gt(PayBlacklist::getExpireTime, now));
        if (PayBlacklistTypeEnum.WECHAT_OPENID.getCode().equals(type)) {
            if (StrUtil.isBlank(wxAppId)) {
                return Optional.empty();
            }
            query.eq(PayBlacklist::getWxAppId, wxAppId);
        } else {
            // ip / alipay_user：wx_app_id 必须为空
            query.and(w -> w.isNull(PayBlacklist::getWxAppId).or().eq(PayBlacklist::getWxAppId, ""));
        }
        return query.oneOpt();
    }

    /// 是否存在有效的用户标识类黑名单（支付宝 / 微信）
    public boolean hasActiveUserIdentityBlacklist() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        return lambdaQuery()
                .in(PayBlacklist::getType,
                        PayBlacklistTypeEnum.ALIPAY_USER.getCode(),
                        PayBlacklistTypeEnum.WECHAT_OPENID.getCode())
                .eq(PayBlacklist::getStatus, PayBlacklistStatusEnum.ENABLE.getCode())
                .and(w -> w.isNull(PayBlacklist::getExpireTime).or().gt(PayBlacklist::getExpireTime, now))
                .exists();
    }
}
