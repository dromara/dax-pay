package cn.daxpay.open.channel.wechat.service.direct;

import cn.daxpay.open.channel.wechat.code.WechatDirectAppTypeEnum;
import cn.daxpay.open.channel.wechat.convert.direct.WechatDirectAppConvert;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectApp;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectAppParam;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectAppResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/// # 微信直连商户应用管理
///
/// 提供直连商户应用的增删改查功能，包含同一通道商户下应用ID的唯一性校验、应用类型校验和操作范围校验(防止跨商户操作)。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectAppService {

    private final WechatDirectAppManager wechatDirectAppManager;
    private final WechatDirectAppAuthConfigService wechatDirectAppAuthConfigService;
    private final WechatDirectAppCapabilityService wechatDirectAppCapabilityService;

    /// 根据商户号和通道商户号查询应用列表
    public List<WechatDirectAppResult> listByMchNoAndChannelMchNo(String mchNo, String channelMchNo) {
        var list = wechatDirectAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo);
        return MpUtil.toListResult(list);
    }

    /// 根据ID查询应用详情
    public WechatDirectAppResult findById(Long id) {
        return wechatDirectAppManager.findById(id)
                .map(WechatDirectApp::toResult)
                // 微信: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
    }

    /// 判断同一通道商户下微信应用ID是否已存在
    public boolean existsWxAppIdByChannel(String mchNo, String channelMchNo, String wxAppId, Long excludeId) {
        if (StrUtil.hasBlank(mchNo, channelMchNo, wxAppId)) {
            return false;
        }
        return wechatDirectAppManager.existsByChannelMchNoAndWxAppId(mchNo, channelMchNo, wxAppId, excludeId);
    }

    /// 新增微信直连商户应用
    public void add(WechatDirectAppParam param) {
        this.assertWxAppIdUnique(param.getMchNo(), param.getChannelMchNo(), param.getWxAppId(), null);
        this.validateAppType(param.getAppType());
        var entity = WechatDirectAppConvert.CONVERT.toEntity(param);
        wechatDirectAppManager.save(entity);
    }

    /// 更新微信直连商户应用
    public void update(WechatDirectAppParam param) {
        var entity = wechatDirectAppManager.findById(param.getId())
                // 微信: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
        this.assertScopeMatch(entity, param.getMchNo(), param.getChannelMchNo());
        this.assertWxAppIdUnique(entity.getMchNo(), entity.getChannelMchNo(), param.getWxAppId(), param.getId());
        this.validateAppType(param.getAppType());
        WechatDirectAppConvert.CONVERT.copy(param, entity);
        wechatDirectAppManager.updateById(entity);
    }

    /// 删除应用（级联删除授权认证配置与能力关联）
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        wechatDirectAppManager.findById(id)
                // 微信: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
        wechatDirectAppAuthConfigService.deleteByWechatDirectAppId(id);
        wechatDirectAppCapabilityService.deleteByWechatDirectAppId(id);
        wechatDirectAppManager.deleteById(id);
    }

    /// 校验同一通道商户下应用ID唯一
    private void assertWxAppIdUnique(String mchNo, String channelMchNo, String wxAppId, Long excludeId) {
        if (this.existsWxAppIdByChannel(mchNo, channelMchNo, wxAppId, excludeId)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.appIdDuplicate");
        }
    }

    /// 校验操作范围与记录归属一致
    private void assertScopeMatch(WechatDirectApp entity, String mchNo, String channelMchNo) {
        if (!entity.getMchNo().equals(mchNo) || !entity.getChannelMchNo().equals(channelMchNo)) {
            // 微信: 直连商户应用不存在或商户号归属不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.mchAppNotFound");
        }
    }

    /// 校验应用类型
    private void validateAppType(String appType) {
        boolean valid = Arrays.stream(WechatDirectAppTypeEnum.values())
                .anyMatch(item -> item.getCode().equals(appType));
        if (!valid) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.appTypeInvalid");
        }
    }
}
