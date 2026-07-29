package cn.daxpay.open.payment.wx.service.merchant;

import cn.daxpay.open.payment.wx.convert.merchant.WxMchAppConvert;
import cn.daxpay.open.payment.wx.dao.channel.WxChannelAppCapabilityManager;
import cn.daxpay.open.payment.wx.dao.merchant.WxMchAppManager;
import cn.daxpay.open.payment.wx.entity.merchant.WxMchApp;
import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.wx.enums.WxAppTypeEnum;
import cn.daxpay.open.payment.wx.param.merchant.WxMchAppParam;
import cn.daxpay.open.payment.wx.result.merchant.WxMchAppResult;
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

/// # 商户微信应用管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WxMchAppService {

    private final WxMchAppManager wxMchAppManager;
    private final WxChannelAppCapabilityManager wxChannelAppCapabilityManager;

    /// 按商户号查询应用列表
    public List<WxMchAppResult> listByMchNo(String mchNo) {
        return MpUtil.toListResult(wxMchAppManager.listByMchNo(mchNo));
    }

    /// 根据ID查询应用详情
    public WxMchAppResult findById(Long id) {
        return wxMchAppManager.findById(id)
                .map(WxMchApp::toResult)
                // 微信: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.wx.mchAppNotFound"));
    }

    /// 同商户下微信应用 AppId 是否已存在（excludeId 可空）
    public boolean existsWxAppId(String mchNo, String wxAppId, Long excludeId) {
        if (StrUtil.isBlank(wxAppId)) {
            return false;
        }
        return wxMchAppManager.existsByMchNoAndWxAppId(mchNo, wxAppId, excludeId);
    }

    /// 新增商户微信应用（运营端必须显式带 mchNo）
    @Transactional(rollbackFor = Exception.class)
    public void add(WxMchAppParam param) {
        if (StrUtil.isBlank(param.getMchNo())) {
            // 微信: 商户号必填
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.wx.mchAppNotFound");
        }
        this.assertWxAppIdUnique(param.getMchNo(), param.getWxAppId(), null);
        this.validateAppType(param.getAppType());
        WxMchApp entity = WxMchAppConvert.CONVERT.toEntity(param);
        // 运营端写 MchBaseEntity 必须显式 setMchNo
        entity.setMchNo(param.getMchNo());
        wxMchAppManager.save(entity);
    }

    /// 更新商户微信应用
    @Transactional(rollbackFor = Exception.class)
    public void update(WxMchAppParam param) {
        WxMchApp entity = wxMchAppManager.findById(param.getId())
                // 微信: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.wx.mchAppNotFound"));
        String mchNo = entity.getMchNo();
        this.assertWxAppIdUnique(mchNo, param.getWxAppId(), param.getId());
        this.validateAppType(param.getAppType());
        WxMchAppConvert.CONVERT.copy(param, entity);
        // 保持主表商户号不变
        entity.setMchNo(mchNo);
        wxMchAppManager.updateById(entity);
    }

    /// 删除应用（被通道能力绑定引用时拒删；级联删除授权认证配置）
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        wxMchAppManager.findById(id)
                // 微信: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.wx.mchAppNotFound"));
        // 通道绑定仍引用时拒删
        if (wxChannelAppCapabilityManager.existsByScopeAndRefId(AppScopeEnum.MERCHANT.getCode(), id)) {
            // 微信: 应用仍被引用，不可删除
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE, "error.payment.wx.appInUse");
        }
        wxMchAppManager.deleteById(id);
    }

    /// 校验同商户下 wxAppId 唯一
    private void assertWxAppIdUnique(String mchNo, String wxAppId, Long excludeId) {
        if (this.existsWxAppId(mchNo, wxAppId, excludeId)) {
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
