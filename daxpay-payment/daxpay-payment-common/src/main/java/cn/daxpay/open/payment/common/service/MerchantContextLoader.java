package cn.daxpay.open.payment.common.service;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.enums.merchant.MchAppStatusEnum;
import cn.daxpay.open.platform.core.enums.merchant.MerchantStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigNotEnableException;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.payment.common.runtime.PaymentContext;
import cn.daxpay.open.payment.merchant.dto.MchAppInfoAccessInfo;
import cn.daxpay.open.payment.merchant.dto.MerchantAccessInfo;
import cn.daxpay.open.payment.merchant.service.query.MerchantAccessQueryService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.util.Optional.ofNullable;

/// # 商户上下文装载器
///
/// 负责交易场景的身份初始化,从原 `PaymentAssistService` 拆出(填充职责):
/// - `initMch`:商户身份进上下文(所有交易场景必调)
/// - `resolveApp`:应用解析(推导默认/校验启用),仅需要应用归属时调,返回应用信息供调用方显式 setAppId
///
/// 引导阶段通过 [MerchantAccessQueryService] 的 `*NotTenant` 读路径装载身份;
/// **initMch 成功之后**业务链路应走正常租户过滤,勿再散落 `@IgnoreTenant`。
/// appId 不进线程上下文:它是可空、可推导的业务属性,由调用方按需显式赋值到实体。
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantContextLoader {

    private final ClientCodeService clientCodeService;
    private final MerchantAccessQueryService merchantAccessQueryService;
    private final PaymentContext paymentContext;

    /// 初始化商户身份:校验商户启用,将商户号写入线程上下文。
    /// 商户端商户号由系统下发,不允许自行设置。
    public void initMch(String mchNo) {
        // 商户端:商户号读取系统,不允许自行设置
        if (Objects.equals(clientCodeService.getClientCode(), ClientEnum.MERCHANT.getCode())) {
            mchNo = paymentContext.getMchNo();
        }
        var merchant = ofNullable(merchantAccessQueryService.getMerchantByMchNo(mchNo))
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
            mchApp = ofNullable(merchantAccessQueryService.getDefaultAppByMchNo(mchNo))
                    // 未找到商户默认应用配置
                    .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.defaultAppConfigNotFound"));
        } else {
            mchApp = ofNullable(merchantAccessQueryService.getAppByAppId(appId))
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
        var mchApp = ofNullable(merchantAccessQueryService.getAppByAppId(appId))
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
