package cn.daxpay.open.payment.douyin.service.platform;

import cn.daxpay.open.payment.douyin.convert.platform.DyPlatformAppConvert;
import cn.daxpay.open.payment.douyin.dao.channel.DyChannelAppCapabilityManager;
import cn.daxpay.open.payment.douyin.dao.platform.DyPlatformAppCapabilityManager;
import cn.daxpay.open.payment.douyin.dao.platform.DyPlatformAppManager;
import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformApp;
import cn.daxpay.open.payment.douyin.enums.DyAppScopeEnum;
import cn.daxpay.open.payment.douyin.enums.DyAppTypeEnum;
import cn.daxpay.open.payment.douyin.param.platform.DyPlatformAppParam;
import cn.daxpay.open.payment.douyin.result.platform.DyPlatformAppResult;
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

/// # 平台抖音应用管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DyPlatformAppService {

    private final DyPlatformAppManager dyPlatformAppManager;
    private final DyPlatformAppAuthConfigService dyPlatformAppAuthConfigService;
    private final DyChannelAppCapabilityManager dyChannelAppCapabilityManager;
    private final DyPlatformAppCapabilityManager dyPlatformAppCapabilityManager;

    /// 查询全部应用列表
    public List<DyPlatformAppResult> listAll() {
        return MpUtil.toListResult(dyPlatformAppManager.listAll());
    }

    /// 根据ID查询应用详情
    public DyPlatformAppResult findById(Long id) {
        return dyPlatformAppManager.findById(id)
                .map(DyPlatformApp::toResult)
                // 抖音: 平台应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.douyin.appNotFound"));
    }

    /// 抖音应用 AppId 是否已存在（excludeId 可空）
    public boolean existsDouyinAppId(String douyinAppId, Long excludeId) {
        if (StrUtil.isBlank(douyinAppId)) {
            return false;
        }
        return dyPlatformAppManager.existsByDouyinAppId(douyinAppId, excludeId);
    }

    /// 新增平台抖音应用
    public void add(DyPlatformAppParam param) {
        this.assertDouyinAppIdUnique(param.getDouyinAppId(), null);
        this.validateAppType(param.getAppType());
        DyPlatformApp entity = DyPlatformAppConvert.CONVERT.toEntity(param);
        dyPlatformAppManager.save(entity);
    }

    /// 更新平台抖音应用
    public void update(DyPlatformAppParam param) {
        DyPlatformApp entity = dyPlatformAppManager.findById(param.getId())
                // 抖音: 平台应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.douyin.appNotFound"));
        this.assertDouyinAppIdUnique(param.getDouyinAppId(), param.getId());
        this.validateAppType(param.getAppType());
        DyPlatformAppConvert.CONVERT.copy(param, entity);
        dyPlatformAppManager.updateById(entity);
    }

    /// 删除应用（被能力绑定引用时拒删；级联删除授权认证配置）
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        dyPlatformAppManager.findById(id)
                // 抖音: 平台应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.douyin.appNotFound"));
        // 通道绑定或平台默认能力绑定仍引用时拒删
        if (dyChannelAppCapabilityManager.existsByScopeAndRefId(DyAppScopeEnum.PLATFORM.getCode(), id)
                || dyPlatformAppCapabilityManager.existsByDyPlatformAppId(id)) {
            // 抖音: 应用仍被引用，不可删除
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE, "error.payment.douyin.appInUse");
        }
        dyPlatformAppAuthConfigService.deleteByDyPlatformAppId(id);
        dyPlatformAppManager.deleteById(id);
    }

    /// 校验抖音应用 AppId 唯一
    private void assertDouyinAppIdUnique(String douyinAppId, Long excludeId) {
        if (this.existsDouyinAppId(douyinAppId, excludeId)) {
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
