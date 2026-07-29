package cn.daxpay.open.payment.douyin.service.merchant;

import cn.daxpay.open.payment.douyin.convert.merchant.DyMchAppConvert;
import cn.daxpay.open.payment.douyin.dao.channel.DyChannelAppCapabilityManager;
import cn.daxpay.open.payment.douyin.dao.merchant.DyMchAppManager;
import cn.daxpay.open.payment.douyin.entity.merchant.DyMchApp;
import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.douyin.enums.DyAppTypeEnum;
import cn.daxpay.open.payment.douyin.param.merchant.DyMchAppParam;
import cn.daxpay.open.payment.douyin.result.merchant.DyMchAppResult;
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

/// # 商户抖音应用管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DyMchAppService {

    private final DyMchAppManager dyMchAppManager;
    private final DyChannelAppCapabilityManager dyChannelAppCapabilityManager;

    /// 按商户号查询应用列表
    public List<DyMchAppResult> listByMchNo(String mchNo) {
        return MpUtil.toListResult(dyMchAppManager.listByMchNo(mchNo));
    }

    /// 根据ID查询应用详情
    public DyMchAppResult findById(Long id) {
        return dyMchAppManager.findById(id)
                .map(DyMchApp::toResult)
                // 抖音: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.douyin.mchAppNotFound"));
    }

    /// 同商户下抖音应用 AppId 是否已存在（excludeId 可空）
    public boolean existsDouyinAppId(String mchNo, String douyinAppId, Long excludeId) {
        if (StrUtil.isBlank(douyinAppId)) {
            return false;
        }
        return dyMchAppManager.existsByMchNoAndDouyinAppId(mchNo, douyinAppId, excludeId);
    }

    /// 新增商户抖音应用（运营端必须显式带 mchNo）
    @Transactional(rollbackFor = Exception.class)
    public void add(DyMchAppParam param) {
        if (StrUtil.isBlank(param.getMchNo())) {
            // 抖音: 商户号必填
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.douyin.mchAppNotFound");
        }
        this.assertDouyinAppIdUnique(param.getMchNo(), param.getDouyinAppId(), null);
        this.validateAppType(param.getAppType());
        DyMchApp entity = DyMchAppConvert.CONVERT.toEntity(param);
        // 运营端写 MchBaseEntity 必须显式 setMchNo
        entity.setMchNo(param.getMchNo());
        dyMchAppManager.save(entity);
    }

    /// 更新商户抖音应用
    @Transactional(rollbackFor = Exception.class)
    public void update(DyMchAppParam param) {
        DyMchApp entity = dyMchAppManager.findById(param.getId())
                // 抖音: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.douyin.mchAppNotFound"));
        String mchNo = entity.getMchNo();
        this.assertDouyinAppIdUnique(mchNo, param.getDouyinAppId(), param.getId());
        this.validateAppType(param.getAppType());
        DyMchAppConvert.CONVERT.copy(param, entity);
        // 保持主表商户号不变
        entity.setMchNo(mchNo);
        dyMchAppManager.updateById(entity);
    }

    /// 删除应用（被通道能力绑定引用时拒删；级联删除授权认证配置）
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        dyMchAppManager.findById(id)
                // 抖音: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.douyin.mchAppNotFound"));
        // 通道绑定仍引用时拒删
        if (dyChannelAppCapabilityManager.existsByScopeAndRefId(AppScopeEnum.MERCHANT.getCode(), id)) {
            // 抖音: 应用仍被引用，不可删除
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE, "error.payment.douyin.appInUse");
        }
        dyMchAppManager.deleteById(id);
    }

    /// 校验同商户下 douyinAppId 唯一
    private void assertDouyinAppIdUnique(String mchNo, String douyinAppId, Long excludeId) {
        if (this.existsDouyinAppId(mchNo, douyinAppId, excludeId)) {
            // 抖音: AppId 已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.douyin.appIdDuplicate");
        }
    }

    /// 校验应用类型
    private void validateAppType(String appType) {
        if (DyAppTypeEnum.findByCode(appType) == null) {
            // 抖音: 应用类型无效
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.douyin.appTypeInvalid");
        }
    }
}
