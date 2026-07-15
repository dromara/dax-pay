package cn.daxpay.open.plugin.risk.service;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.PayErrorCode;
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
        if (payBlacklistManager.existsDuplicate(param.getType(), param.getValue(),
                param.getChannel(), param.getChannelAppId(), null)) {
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
        PayBlacklistConvert.CONVERT.copy(param, entity);
        entity.setType(originType);
        entity.setValue(originValue);
        if (StrUtil.isNotBlank(param.getStatus())) {
            validateStatus(param.getStatus());
        }
        if (payBlacklistManager.existsDuplicate(entity.getType(), entity.getValue(),
                entity.getChannel(), entity.getChannelAppId(), entity.getId())) {
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
    public boolean isBlocked(String type, String value, String channel, String channelAppId) {
        return findActive(type, value, channel, channelAppId).isPresent();
    }

    /// 查找有效名单行
    public Optional<PayBlacklist> findActive(String type, String value, String channel, String channelAppId) {
        return payBlacklistManager.findActiveHit(type, value, channel, channelAppId);
    }

    /// 供命中处理「加入黑名单」：无有效项则新建
    @Transactional(rollbackFor = Exception.class)
    public PayBlacklist ensureBlacklist(String type, String value, String channel, String reason) {
        Optional<PayBlacklist> existing = payBlacklistManager.findActiveHit(type, value, channel, null);
        if (existing.isPresent()) {
            return existing.get();
        }
        PayBlacklist entity = new PayBlacklist()
                .setType(type)
                .setValue(value)
                .setChannel(channel)
                .setStatus(PayBlacklistStatusEnum.ENABLE.getCode())
                .setReason(StrUtil.blankToDefault(reason, "risk hit auto add"));
        payBlacklistManager.save(entity);
        return entity;
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
}
