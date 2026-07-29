package cn.daxpay.open.payment.wx.service.platform;

import cn.daxpay.open.payment.wx.convert.platform.WxPlatformAppConvert;
import cn.daxpay.open.payment.wx.dao.channel.WxChannelAppCapabilityManager;
import cn.daxpay.open.payment.wx.dao.platform.WxPlatformAppCapabilityManager;
import cn.daxpay.open.payment.wx.dao.platform.WxPlatformAppManager;
import cn.daxpay.open.payment.wx.entity.platform.WxPlatformApp;
import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.wx.enums.WxAppTypeEnum;
import cn.daxpay.open.payment.wx.param.platform.WxPlatformAppParam;
import cn.daxpay.open.payment.wx.result.platform.WxPlatformAppResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/// # 平台微信应用管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WxPlatformAppService {

    private final WxPlatformAppManager wxPlatformAppManager;
    private final WxChannelAppCapabilityManager wxChannelAppCapabilityManager;
    private final WxPlatformAppCapabilityManager wxPlatformAppCapabilityManager;

    /// 查询全部应用列表
    public List<WxPlatformAppResult> listAll() {
        return MpUtil.toListResult(wxPlatformAppManager.listAll());
    }

    /// 根据ID查询应用详情
    public WxPlatformAppResult findById(Long id) {
        return wxPlatformAppManager.findById(id)
                .map(WxPlatformApp::toResult)
                // 微信: 平台应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.wx.appNotFound"));
    }

    /// 微信应用 AppId 是否已存在（excludeId 可空）
    public boolean existsWxAppId(String wxAppId, Long excludeId) {
        if (StrUtil.isBlank(wxAppId)) {
            return false;
        }
        return wxPlatformAppManager.existsByWxAppId(wxAppId, excludeId);
    }

    /// 新增平台微信应用
    @Transactional(rollbackFor = Exception.class)
    public void add(WxPlatformAppParam param) {
        this.assertWxAppIdUnique(param.getWxAppId(), null);
        this.validateAppType(param.getAppType());
        WxPlatformApp entity = WxPlatformAppConvert.CONVERT.toEntity(param);
        wxPlatformAppManager.save(entity);
    }

    /// 更新平台微信应用
    @Transactional(rollbackFor = Exception.class)
    public void update(WxPlatformAppParam param) {
        WxPlatformApp entity = wxPlatformAppManager.findById(param.getId())
                // 微信: 平台应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.wx.appNotFound"));
        this.assertWxAppIdUnique(param.getWxAppId(), param.getId());
        this.validateAppType(param.getAppType());
        WxPlatformAppConvert.CONVERT.copy(param, entity);
        wxPlatformAppManager.updateById(entity);
    }

    /// 删除应用（被能力绑定引用时拒删；级联删除授权认证配置）
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        wxPlatformAppManager.findById(id)
                // 微信: 平台应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.wx.appNotFound"));
        // 通道绑定或平台默认能力绑定仍引用时拒删
        if (wxChannelAppCapabilityManager.existsByScopeAndRefId(AppScopeEnum.PLATFORM.getCode(), id)
                || wxPlatformAppCapabilityManager.existsByWxPlatformAppId(id)) {
            // 微信: 应用仍被引用，不可删除
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE, "error.payment.wx.appInUse");
        }
        wxPlatformAppManager.deleteById(id);
    }

    /// 校验微信应用 AppId 唯一
    private void assertWxAppIdUnique(String wxAppId, Long excludeId) {
        if (this.existsWxAppId(wxAppId, excludeId)) {
            // 微信: AppId 已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.wx.appIdDuplicate");
        }
    }

    /// 校验应用类型
    private void validateAppType(String appType) {
        if (WxAppTypeEnum.findByCode(appType) == null) {
            // 微信: 应用类型无效
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.wx.appTypeInvalid");
        }
    }
}
