package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.code.WechatIsvAppTypeEnum;
import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvMchAppConvert;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchApp;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvMchAppParam;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvMchAppResult;
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

/// # 微信服务商通道商户应用管理
///
/// 提供服务商通道商户应用(子商户应用)的增删改查功能,包含同一通道商户下应用AppId的唯一性校验、
/// 应用类型校验、操作范围校验(防止跨商户操作),以及删除时级联清理授权配置与能力关联。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvMchAppService {

    private final WechatIsvMchAppManager wechatIsvMchAppManager;
    private final WechatIsvMchAppAuthConfigService wechatIsvMchAppAuthConfigService;
    private final WechatIsvMchAppCapabilityService wechatIsvMchAppCapabilityService;

    /// 根据商户号和通道商户号查询应用列表
    public List<WechatIsvMchAppResult> listByMchNoAndChannelMchNo(String mchNo, String channelMchNo) {
        var list = wechatIsvMchAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo);
        return MpUtil.toListResult(list);
    }

    /// 根据ID查询应用详情
    public WechatIsvMchAppResult findById(Long id) {
        return wechatIsvMchAppManager.findById(id)
                .map(WechatIsvMchApp::toResult)
                // 微信: 服务商通道商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
    }

    /// 判断同一通道商户下微信应用AppId是否已存在
    public boolean existsWxAppIdByChannel(String mchNo, String channelMchNo, String wxAppId, Long excludeId) {
        if (StrUtil.hasBlank(mchNo, channelMchNo, wxAppId)) {
            return false;
        }
        return wechatIsvMchAppManager.existsByChannelMchNoAndWxAppId(mchNo, channelMchNo, wxAppId, excludeId);
    }

    /// 新增服务商通道商户应用
    public void add(WechatIsvMchAppParam param) {
        this.assertWxAppIdUnique(param.getMchNo(), param.getChannelMchNo(), param.getWxAppId(), null);
        this.validateAppType(param.getAppType());
        var entity = WechatIsvMchAppConvert.CONVERT.toEntity(param);
        wechatIsvMchAppManager.save(entity);
    }

    /// 更新服务商通道商户应用
    public void update(WechatIsvMchAppParam param) {
        var entity = wechatIsvMchAppManager.findById(param.getId())
                // 微信: 服务商通道商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
        this.assertScopeMatch(entity, param.getMchNo(), param.getChannelMchNo());
        this.assertWxAppIdUnique(entity.getMchNo(), entity.getChannelMchNo(), param.getWxAppId(), param.getId());
        this.validateAppType(param.getAppType());
        WechatIsvMchAppConvert.CONVERT.copy(param, entity);
        wechatIsvMchAppManager.updateById(entity);
    }

    /// 删除应用(级联删除授权配置与能力关联)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        wechatIsvMchAppManager.findById(id)
                // 微信: 服务商通道商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
        wechatIsvMchAppAuthConfigService.deleteByWechatIsvMchAppId(id);
        wechatIsvMchAppCapabilityService.deleteByWechatIsvMchAppId(id);
        wechatIsvMchAppManager.deleteById(id);
    }

    /// 校验同一通道商户下应用AppId唯一
    private void assertWxAppIdUnique(String mchNo, String channelMchNo, String wxAppId, Long excludeId) {
        if (this.existsWxAppIdByChannel(mchNo, channelMchNo, wxAppId, excludeId)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.appIdDuplicate");
        }
    }

    /// 校验操作范围与记录归属一致
    private void assertScopeMatch(WechatIsvMchApp entity, String mchNo, String channelMchNo) {
        if (!entity.getMchNo().equals(mchNo) || !entity.getChannelMchNo().equals(channelMchNo)) {
            // 微信: 服务商通道商户应用不存在或商户号归属不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.mchAppNotFound");
        }
    }

    /// 校验应用类型(复用服务商应用类型枚举)
    private void validateAppType(String appType) {
        boolean valid = Arrays.stream(WechatIsvAppTypeEnum.values())
                .anyMatch(item -> item.getCode().equals(appType));
        if (!valid) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.appTypeInvalid");
        }
    }
}
