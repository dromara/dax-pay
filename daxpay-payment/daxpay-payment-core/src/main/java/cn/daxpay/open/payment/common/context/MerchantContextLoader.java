package cn.daxpay.open.payment.common.context;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.enums.merchant.MchAppStatusEnum;
import cn.daxpay.open.platform.core.enums.merchant.MerchantStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigNotEnableException;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.payment.common.access.MchAppInfoAccessInfo;
import cn.daxpay.open.payment.merchant.service.access.MerchantAccessQueryService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/// # 商户上下文装载器（多端共用）
///
/// 将**可信**商户号写入 [PaymentContext]，供 TenantLine / 自动填充读取。
/// 不负责 SQL 隔离本身（隔离内核是 `MchNoTenantLineHandler`）。
///
/// - `initMch`:商户身份进上下文(交易/网关/开放 API 等场景),含启用校验
/// - `bindMchNoForCallback`:通道回调身份装载,仅写入 mchNo,不校验启用
/// - `resolveApp`:应用解析(推导默认/校验启用),仅需要应用归属时调
///
/// **商户端**特判：忽略入参 mchNo，强制用登录上下文已装载的商户号（防越权自报）。
/// 引导阶段通过 [MerchantAccessQueryService] 的 `*NotTenant` 读路径；
/// **initMch 成功之后**业务链路应走正常租户过滤，勿再散落类级 `@IgnoreTenant`。
/// appId 不进线程上下文。详见 `_doc/design/mch-no-tenant-isolation.md`。
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantContextLoader {

    private final ClientCodeService clientCodeService;
    private final MerchantAccessQueryService merchantAccessQueryService;
    private final PaymentContext paymentContext;

    /// 通道回调身份装载:仅将 path 上的 mchNo 写入上下文,不查库、不校验商户启用。
    ///
    /// 禁用商户的历史在途单仍可能收到通道回调,须能完成状态更新(与超时关单 `setMchNo` 策略一致)。
    /// 由各 `*CallbackController` 入口显式调用,替代已删除的 CallbackMchContextFilter。
    public void bindMchNoForCallback(String mchNo) {
        if (StrUtil.isBlank(mchNo)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.mchContextMissing");
        }
        paymentContext.setMchNo(mchNo);
    }

    /// 初始化商户身份:校验商户启用,将商户号写入线程上下文。
    /// 商户端商户号由系统下发,不允许自行设置。
    public void initMch(String mchNo) {
        // 商户端:商户号读取系统,不允许自行设置
        if (Objects.equals(clientCodeService.getClientCode(), ClientEnum.MERCHANT.getCode())) {
            mchNo = paymentContext.getMchNo();
        }
        var merchant = Optional.ofNullable(merchantAccessQueryService.getMerchantByMchNo(mchNo))
                // 未找到指定的商户配置
                .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.specifiedMchConfigNotFound"));
        // 商户状态校验
        if (!Objects.equals(merchant.getStatus(), MerchantStatusEnum.ENABLE.getCode())) {
            // 商户未启用
            throw new ConfigNotEnableException(CommonCode.FAIL_CODE, "pay.error.assist.mchNotEnabled");
        }
        paymentContext.setMchNo(merchant.getMchNo());
    }

    /// 解析应用:appId 空则取商户默认应用,否则按 appId 查询;校验启用与商户匹配。
    /// 返回应用信息(含推导后的 appId),由调用方显式赋值到实体,不进线程上下文。
    public MchAppInfoAccessInfo resolveApp(String mchNo, String appId) {
        MchAppInfoAccessInfo mchApp;
        if (StrUtil.isBlank(appId)) {
            // appId 为空,取商户默认应用
            mchApp = Optional.ofNullable(merchantAccessQueryService.getDefaultAppByMchNo(mchNo))
                    // 未找到商户默认应用配置
                    .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.defaultAppConfigNotFound"));
        } else {
            mchApp = Optional.ofNullable(merchantAccessQueryService.getAppByAppId(appId))
                    // 未找到指定的应用配置
                    .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.specifiedAppConfigNotFound"));
        }
        // 应用状态校验
        if (!Objects.equals(mchApp.getStatus(), MchAppStatusEnum.ENABLE.getCode())) {
            // 商户应用未启用
            throw new ConfigNotEnableException(CommonCode.FAIL_CODE, "pay.error.assist.mchAppNotEnabled");
        }
        // 商户与应用匹配校验
        if (!Objects.equals(mchApp.getMchNo(), mchNo)) {
            // 商户号和应用号不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.mchNoAppNoMatch");
        }
        return mchApp;
    }

    /// 按应用号反推商户身份并初始化(应用号已知、商户号未知的场景,如渠道配置/通道认证)。
    /// 商户端会校验应用归属当前登录商户。
    public void initMchByApp(String appId) {
        var mchApp = Optional.ofNullable(merchantAccessQueryService.getAppByAppId(appId))
                // 未找到指定的应用配置
                .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.specifiedAppConfigNotFound"));
        // 应用状态校验
        if (!Objects.equals(mchApp.getStatus(), MchAppStatusEnum.ENABLE.getCode())) {
            // 商户应用未启用
            throw new ConfigNotEnableException(CommonCode.FAIL_CODE, "pay.error.assist.mchAppNotEnabled");
        }
        // 商户端:校验应用归属当前商户
        if (Objects.equals(clientCodeService.getClientCode(), ClientEnum.MERCHANT.getCode())) {
            if (!Objects.equals(mchApp.getMchNo(), paymentContext.getMchNo())) {
                // 该商户不拥有该应用
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.mchNotOwnApp");
            }
        }
        // 复用 initMch 完成商户身份初始化(含商户状态校验)
        this.initMch(mchApp.getMchNo());
    }
}
