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
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.service.access.MerchantAccessQueryService;
import cn.daxpay.open.platform.core.enums.merchant.StoreStatusEnum;
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
/// - `resolveApp`:**内部链路**的应用解析(应用优先, 其次门店默认应用, 最后商户默认应用);
///   两参重载仅供不涉及门店的调用方使用, 开放 API/网关内核保持两参语义不变
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
    /// 门店 DAO: 门店默认支付应用解析用(直接注入 DAO, 避免与门店 Service 形成循环依赖)
    private final MchStoreInfoManager mchStoreInfoManager;

    /// 通道回调身份装载:仅将 path 上的 mchNo 写入上下文,不查库、不校验商户启用。
    ///
    /// 禁用商户的历史在途单仍可能收到通道回调,须能完成状态更新(与超时关单 `setMchNo` 策略一致)。
    /// 由各 `*CallbackController` 入口显式调用。
    public void bindMchNoForCallback(String mchNo) {
        if (StrUtil.isBlank(mchNo)) {
            // 商户: 商户上下文未装载
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
    ///
    /// 异常分类(便于排查):
    /// - 未传 appId 且无默认应用 → `error.payment.merchant.defaultAppConfigNotFound`
    /// - 未传 appId 且默认应用已停用 → `error.payment.merchant.defaultAppDisabled`
    /// - 显式 appId 不存在 → `error.payment.merchant.specifiedAppConfigNotFound`
    /// - 显式 appId 已停用 → `pay.error.assist.mchAppNotEnabled`
    public MchAppInfoAccessInfo resolveApp(String mchNo, String appId) {
        MchAppInfoAccessInfo mchApp;
        boolean fallbackDefault = StrUtil.isBlank(appId);
        if (fallbackDefault) {
            // appId 为空,取商户默认应用
            mchApp = Optional.ofNullable(merchantAccessQueryService.getDefaultAppByMchNo(mchNo))
                    // 未找到商户默认应用配置
                    .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.defaultAppConfigNotFound"));
        } else {
            mchApp = Optional.ofNullable(merchantAccessQueryService.getAppByAppId(appId))
                    // 未找到指定的应用配置
                    .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.specifiedAppConfigNotFound"));
        }
        // 应用状态校验: 默认应用停用与指定应用停用分别提示, 便于排查
        if (!Objects.equals(mchApp.getStatus(), MchAppStatusEnum.ENABLE.getCode())) {
            if (fallbackDefault) {
                // 商户默认应用已停用, 请先启用或转交默认标记
                throw new ConfigNotEnableException(CommonCode.FAIL_CODE, "error.payment.merchant.defaultAppDisabled");
            }
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

    /// 解析应用(门店感知, 仅内部链路: 码牌支付/码牌授权/抖音授权路由):
    /// 显式应用 > 门店默认应用 > 商户默认应用。
    ///
    /// 解析顺序:
    /// 1. appId 非空 → 与两参重载完全一致(校验存在/启用/归属), 门店不参与
    /// 2. appId 空 且 storeNo 空 → 回落商户默认应用(与两参重载完全一致)
    /// 3. appId 空 且 storeNo 非空 → 取门店配置的默认支付应用; 门店不存在/停用/未配置应用,
    ///    或门店应用不存在/停用/不属于本商户 → 回落商户默认应用(应用无效时记业务告警), **不抛门店相关异常**
    ///
    /// **storeNo 必须是调用方的显式值**(请求原值/码牌实体值), 禁止传入下单链路解析后的默认门店——
    /// 否则等于所有未指定门店的支付都改走默认门店应用。门店的严格校验(存在/归属/启用)仍由
    /// [cn.daxpay.open.payment.merchant.service.store.MchStoreInfoService#validateStoreForPay] 在建单阶段负责, 本方法保持宽容。
    ///
    /// @param mchNo   商户号
    /// @param appId   显式应用号(可空)
    /// @param storeNo 显式门店号(可空)
    public MchAppInfoAccessInfo resolveApp(String mchNo, String appId, String storeNo) {
        if (StrUtil.isNotBlank(appId) || StrUtil.isBlank(storeNo)) {
            // 显式应用优先; 未指定门店时与两参重载语义完全一致
            return this.resolveApp(mchNo, appId);
        }
        MchAppInfoAccessInfo storeApp = this.resolveStoreApp(mchNo, storeNo);
        if (storeApp != null) {
            return storeApp;
        }
        // 门店应用不可用: 回落商户默认应用(不抛门店相关错误)
        return this.resolveApp(mchNo, null);
    }

    /// 解析门店配置的默认支付应用: 可用(门店启用 + 应用存在/启用/属同商户)才返回, 否则返回 null 由调用方回落。
    ///
    /// 门店查询按 `mch_no + store_no` 在租户内进行(码牌链路调用时已 initMch),
    /// 同商户限定天然防跨商户; 无法识别的门店一律按"未配置应用"处理。
    private MchAppInfoAccessInfo resolveStoreApp(String mchNo, String storeNo) {
        MchStoreInfo store = mchStoreInfoManager.findByStoreNo(storeNo).orElse(null);
        if (store == null || !Objects.equals(store.getMchNo(), mchNo)) {
            // 门店不存在或不属于当前商户: 解析层宽容放行, 归属校验由建单链路负责
            return null;
        }
        if (!Objects.equals(store.getStatus(), StoreStatusEnum.ENABLE.getCode())) {
            // 门店停用: 门店应用不参与解析, 回落商户默认应用(建单链路会按门店停用拦截)
            return null;
        }
        String storeAppId = store.getAppId();
        if (StrUtil.isBlank(storeAppId)) {
            // 门店未配置应用: 语义即跟随商户默认应用
            return null;
        }
        MchAppInfoAccessInfo storeApp = merchantAccessQueryService.getAppByAppId(storeAppId);
        if (storeApp == null) {
            log.warn("门店默认应用不存在, 已回落商户默认应用: mchNo={}, storeNo={}, storeAppId={}", mchNo, storeNo, storeAppId);
            return null;
        }
        if (!Objects.equals(storeApp.getStatus(), MchAppStatusEnum.ENABLE.getCode())) {
            log.warn("门店默认应用已停用, 已回落商户默认应用: mchNo={}, storeNo={}, storeAppId={}", mchNo, storeNo, storeAppId);
            return null;
        }
        if (!Objects.equals(storeApp.getMchNo(), mchNo)) {
            log.warn("门店默认应用不属于该商户, 已回落商户默认应用: mchNo={}, storeNo={}, storeAppId={}, appMchNo={}",
                    mchNo, storeNo, storeAppId, storeApp.getMchNo());
            return null;
        }
        return storeApp;
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
