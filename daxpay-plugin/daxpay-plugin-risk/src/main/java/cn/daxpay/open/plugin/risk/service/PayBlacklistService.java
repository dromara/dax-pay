package cn.daxpay.open.plugin.risk.service;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.PayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.plugin.risk.convert.PayBlacklistConvert;
import cn.daxpay.open.plugin.risk.dao.PayBlacklistManager;
import cn.daxpay.open.plugin.risk.entity.PayBlacklist;
import cn.daxpay.open.plugin.risk.enums.PayBlacklistStatusEnum;
import cn.daxpay.open.plugin.risk.enums.PayBlacklistTypeEnum;
import cn.daxpay.open.plugin.risk.param.PayBlacklistParam;
import cn.daxpay.open.plugin.risk.param.PayBlacklistQuery;
import cn.daxpay.open.plugin.risk.result.PayBlacklistResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/// # 黑名单服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayBlacklistService {

    private final PayBlacklistManager payBlacklistManager;

    /// 分页
    public PageResult<PayBlacklistResult> page(PageParam pageParam, PayBlacklistQuery query) {
        return MpUtil.toPageResult(payBlacklistManager.page(pageParam, query));
    }

    /// 详情
    public PayBlacklistResult findById(Long id) {
        return getEntity(id).toResult();
    }

    /// 新增
    @Transactional(rollbackFor = Exception.class)
    public PayBlacklistResult add(PayBlacklistParam param) {
        validateType(param.getType());
        if (StrUtil.isNotBlank(param.getStatus())) {
            validateStatus(param.getStatus());
        }
        normalizeScope(param);
        if (payBlacklistManager.existsDuplicate(param.getType(), param.getValue(), param.getWxAppId(), null)) {
            // 黑名单已存在
            throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklistDuplicate");
        }
        PayBlacklist entity = PayBlacklistConvert.CONVERT.toEntity(param);
        if (StrUtil.isBlank(entity.getStatus())) {
            entity.setStatus(PayBlacklistStatusEnum.ENABLE.getCode());
        }
        payBlacklistManager.save(entity);
        return entity.toResult();
    }

    /// 修改（type/value 不可变）
    @Transactional(rollbackFor = Exception.class)
    public void update(PayBlacklistParam param) {
        PayBlacklist entity = getEntity(param.getId());
        String originType = entity.getType();
        String originValue = entity.getValue();
        param.setType(originType);
        normalizeScope(param);
        PayBlacklistConvert.CONVERT.copy(param, entity);
        entity.setType(originType);
        entity.setValue(originValue);
        // MapStruct IGNORE 不会清空 null
        if (!PayBlacklistTypeEnum.WECHAT_OPENID.getCode().equals(originType)) {
            entity.setWxAppId(null);
        } else {
            entity.setWxAppId(StrUtil.trimToNull(param.getWxAppId()));
        }
        if (StrUtil.isNotBlank(param.getStatus())) {
            validateStatus(param.getStatus());
        }
        if (payBlacklistManager.existsDuplicate(entity.getType(), entity.getValue(),
                entity.getWxAppId(), entity.getId())) {
            throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklistDuplicate");
        }
        payBlacklistManager.updateById(entity);
    }

    /// 删除
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        getEntity(id);
        payBlacklistManager.deleteById(id);
    }

    /// 是否命中有效黑名单
    public boolean isBlocked(String type, String value, String wxAppId) {
        return findActive(type, value, wxAppId).isPresent();
    }

    /// 是否存在有效的用户标识类黑名单（供网关层智能触发 OAuth）
    public boolean hasActiveOpenIdBlacklist() {
        return payBlacklistManager.hasActiveUserIdentityBlacklist();
    }

    /// 查找有效名单行
    public Optional<PayBlacklist> findActive(String type, String value, String wxAppId) {
        return payBlacklistManager.findActiveHit(type, value, wxAppId);
    }

    /// 供命中处理「加入黑名单」
    ///
    /// 按 hit 的 type/channel 映射；微信无 wxAppId 时抛错（避免非法行）。
    @Transactional(rollbackFor = Exception.class)
    public PayBlacklist ensureBlacklist(String hitType, String value, String channel,
                                         String wxAppId, String reason) {
        if (StrUtil.isBlank(value)) {
            throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklistTypeInvalid");
        }
        ResolvedIdentity resolved = resolveIdentity(hitType, channel, wxAppId);
        if (resolved == null) {
            // 微信缺 AppId 或无法映射
            if (ChannelEnum.WECHAT.getCode().equals(channel)
                    || PayBlacklistTypeEnum.WECHAT_OPENID.getCode().equals(hitType)
                    || ("open_id".equals(hitType) && ChannelEnum.WECHAT.getCode().equals(channel))) {
                throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklistWechatAppRequired");
            }
            throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklistChannelInvalid");
        }
        Optional<PayBlacklist> existing = payBlacklistManager.findActiveHit(
                resolved.type(), value, resolved.wxAppId());
        if (existing.isPresent()) {
            return existing.get();
        }
        PayBlacklist entity = new PayBlacklist()
                .setType(resolved.type())
                .setValue(value)
                .setWxAppId(resolved.wxAppId())
                .setStatus(PayBlacklistStatusEnum.ENABLE.getCode())
                .setReason(StrUtil.blankToDefault(reason, "risk hit auto add"));
        payBlacklistManager.save(entity);
        return entity;
    }

    /// 规范化作用域：微信必须 wxAppId；其它类型强制清空
    private void normalizeScope(PayBlacklistParam param) {
        String type = param.getType();
        if (PayBlacklistTypeEnum.WECHAT_OPENID.getCode().equals(type)) {
            String wxAppId = StrUtil.trimToNull(param.getWxAppId());
            if (StrUtil.isBlank(wxAppId)) {
                throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklistWechatAppRequired");
            }
            param.setWxAppId(wxAppId);
            return;
        }
        if (PayBlacklistTypeEnum.IP.getCode().equals(type)
                || PayBlacklistTypeEnum.ALIPAY_USER.getCode().equals(type)) {
            param.setWxAppId(null);
            return;
        }
        throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklistTypeInvalid");
    }

    /// 将命中快照映射为名单 type + wxAppId；微信缺 AppId 则返回 null
    private ResolvedIdentity resolveIdentity(String hitType, String channel, String wxAppId) {
        if (PayBlacklistTypeEnum.IP.getCode().equals(hitType)) {
            return new ResolvedIdentity(PayBlacklistTypeEnum.IP.getCode(), null);
        }
        if (PayBlacklistTypeEnum.ALIPAY_USER.getCode().equals(hitType)
                || ChannelEnum.ALIPAY.getCode().equals(channel)) {
            return new ResolvedIdentity(PayBlacklistTypeEnum.ALIPAY_USER.getCode(), null);
        }
        if (PayBlacklistTypeEnum.WECHAT_OPENID.getCode().equals(hitType)
                || ChannelEnum.WECHAT.getCode().equals(channel)) {
            String app = StrUtil.trimToNull(wxAppId);
            if (StrUtil.isBlank(app)) {
                log.warn("命中加黑跳过：微信名单缺少 wxAppId, hitType={}, channel={}", hitType, channel);
                return null;
            }
            return new ResolvedIdentity(PayBlacklistTypeEnum.WECHAT_OPENID.getCode(), app);
        }
        // 兼容旧 hitType=open_id
        if ("open_id".equals(hitType)) {
            if (ChannelEnum.ALIPAY.getCode().equals(channel)) {
                return new ResolvedIdentity(PayBlacklistTypeEnum.ALIPAY_USER.getCode(), null);
            }
            if (ChannelEnum.WECHAT.getCode().equals(channel)) {
                String app = StrUtil.trimToNull(wxAppId);
                if (StrUtil.isBlank(app)) {
                    return null;
                }
                return new ResolvedIdentity(PayBlacklistTypeEnum.WECHAT_OPENID.getCode(), app);
            }
        }
        return null;
    }

    private PayBlacklist getEntity(Long id) {
        return payBlacklistManager.findById(id)
                // 黑名单不存在
                .orElseThrow(() -> new DataNotExistException("pay.error.risk.blacklistNotFound"));
    }

    private void validateType(String type) {
        if (PayBlacklistTypeEnum.findByCode(type).isEmpty()) {
            throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklistTypeInvalid");
        }
    }

    private void validateStatus(String status) {
        if (PayBlacklistStatusEnum.findByCode(status).isEmpty()) {
            throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklistStatusInvalid");
        }
    }

    private record ResolvedIdentity(String type, String wxAppId) {
    }
}
