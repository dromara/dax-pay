package cn.daxpay.open.channel.douyin.service.direct;

import cn.daxpay.open.channel.douyin.convert.direct.DouyinDirectAppAuthConfigConvert;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAppManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAppAuthConfigManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAppAuthConfig;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAppAuthConfigParam;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 抖音直连商户应用授权认证配置
///
/// 管理直连商户应用的密钥和授权认证配置，查询时不存在则创建默认记录，保存时校验应用归属关系。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectAppAuthConfigService {

    private final DouyinDirectAppAuthConfigManager douyinDirectAppAuthConfigManager;
    private final DouyinDirectAppManager douyinDirectAppManager;

    /// 根据应用ID查询授权认证配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public DouyinDirectAppAuthConfig findByDouyinDirectAppId(Long douyinDirectAppId) {
        var existing = douyinDirectAppAuthConfigManager.findByDouyinDirectAppId(douyinDirectAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var app = douyinDirectAppManager.findById(douyinDirectAppId)
                // 抖音: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.douyin.mchAppNotFound"));
        var config = new DouyinDirectAppAuthConfig()
                .setChannelMchNo(app.getChannelMchNo())
                .setDouyinDirectAppId(douyinDirectAppId);
        config.setMchNo(app.getMchNo());
        douyinDirectAppAuthConfigManager.save(config);
        return config;
    }

    /// 根据应用ID查询授权认证配置(运行态认证使用, 忽略租户隔离)
    ///
    /// 与 [#findByDouyinDirectAppId] 的区别: 走 NotTenant 查询路径, 供认证策略(网关端无登录态)调用;
    /// [#save] 等配置态仍调原方法(保留租户隔离)。
    @IgnoreTenant
    @Transactional(rollbackFor = Exception.class)
    public DouyinDirectAppAuthConfig findByDouyinDirectAppIdForAuth(Long douyinDirectAppId) {
        var existing = douyinDirectAppAuthConfigManager.findByDouyinDirectAppIdNotTenant(douyinDirectAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var app = douyinDirectAppManager.findByIdNotTenant(douyinDirectAppId)
                // 抖音: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.douyin.mchAppNotFound"));
        var config = new DouyinDirectAppAuthConfig()
                .setChannelMchNo(app.getChannelMchNo())
                .setDouyinDirectAppId(douyinDirectAppId);
        config.setMchNo(app.getMchNo());
        douyinDirectAppAuthConfigManager.save(config);
        return config;
    }

    /// 保存应用授权认证配置(更新)
    @Transactional(rollbackFor = Exception.class)
    public void save(DouyinDirectAppAuthConfigParam param) {
        var app = douyinDirectAppManager.findById(param.getDouyinDirectAppId())
                // 抖音: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.douyin.mchAppNotFound"));
        if (!app.getMchNo().equals(param.getMchNo()) || !app.getChannelMchNo().equals(param.getChannelMchNo())) {
            // 抖音: 直连商户应用不存在或商户号归属不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.douyin.mchAppNotFound");
        }
        var config = this.findByDouyinDirectAppId(param.getDouyinDirectAppId());
        // Convert copy(IGNORE) 自动处理 appSecret：null 不覆盖，非 null 设置新值
        DouyinDirectAppAuthConfigConvert.CONVERT.copy(param, config);
        douyinDirectAppAuthConfigManager.updateById(config);
    }

    /// 删除应用授权认证配置
    public void deleteByDouyinDirectAppId(Long douyinDirectAppId) {
        douyinDirectAppAuthConfigManager.deleteByDouyinDirectAppId(douyinDirectAppId);
    }
}
