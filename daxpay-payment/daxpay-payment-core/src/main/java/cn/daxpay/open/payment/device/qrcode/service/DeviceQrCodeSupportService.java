package cn.daxpay.open.payment.device.qrcode.service;

import cn.daxpay.open.payment.common.access.MchAppInfoAccessInfo;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeProgramTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeStatusEnum;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeAllocWarningResult;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayPayConfigResolveService;
import cn.daxpay.open.payment.route.service.runtime.PayRouteService;
import cn.daxpay.open.payment.trade.alloc.runtime.service.AllocCapabilityService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// # 码牌公共支撑服务
///
/// 承载运营端([cn.daxpay.open.payment.admin.service.device.DeviceQrCodeAdminService])与商户端
/// ([cn.daxpay.open.payment.merchant.service.device.MchDeviceQrCodeService])的同源业务逻辑:
/// 分账能力预警、扫码链接构建、应用/门店归属校验、业务字段更新。
/// 两端壳层负责各自的商户号来源与归属校验(运营端显式传参, 商户端登录态校验), 之后委托本类。
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceQrCodeSupportService {

    /// H5 码牌支付页路径前缀(与 dax-pay-h5 RoutePath.CODE_PAY 一致: /h/:code)
    public static final String CODE_H5_PATH = "/h/";

    /// 小程序码牌扫码 path 前缀(映射域名落地后置, 本期仅生成链接)
    public static final String CODE_MINI_PATH = "/m/";

    private final DeviceQrCodeManager deviceQrCodeManager;
    private final MchStoreInfoManager mchStoreInfoManager;
    private final MerchantContextLoader merchantContextLoader;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final GatewayPayConfigResolveService gatewayPayConfigResolveService;
    private final PayRouteService payRouteService;
    private final AllocCapabilityService allocCapabilityService;

    /// 分账能力预警: 按当前网关支付配置解析各扫码场景路由出的产品, 返回不支持分账的场景清单
    ///
    /// 码牌开启分账开关前的预检提示(不阻断保存); 与支付侧同路径解析, 网关配置未覆盖的场景跳过。
    /// 支付时降级不依赖此预检(下单链路实时判定), 结果仅供前端提示。
    public List<DeviceQrCodeAllocWarningResult> allocCapabilityWarning(String mchNo, String appId) {
        // 与支付侧一致: appId 空取商户默认应用
        String resolvedAppId = resolveAppId(mchNo, appId);
        List<DeviceQrCodeAllocWarningResult> warnings = new ArrayList<>();
        for (CodePayFormEnum payForm : CodePayFormEnum.values()) {
            for (ClientEnvEnum clientEnv : ClientEnvEnum.values()) {
                if (clientEnv == ClientEnvEnum.BROWSER) {
                    continue;
                }
                GatewayPayConfigResolveService.Resolved resolved;
                try {
                    resolved = gatewayPayConfigResolveService.resolve(resolvedAppId, clientEnv, payForm);
                } catch (BizException e) {
                    // 该场景未配置支付方法(BizException 为预期常态, 商户通常只覆盖部分场景):
                    // 支付时会明确报错, 不属于分账预警范畴, debug 级避免日志噪音
                    log.debug("分账预警跳过未配置场景: appId={}, clientEnv={}, payForm={}",
                            resolvedAppId, clientEnv.getCode(), payForm.getCode());
                    continue;
                } catch (Exception e) {
                    // 非预期异常(DB 抖动/编程错误)会让预警清单缺项, 误导运营的分账开关决策, 须留痕
                    log.warn("分账预警场景解析异常, 场景被跳过: appId={}, clientEnv={}, payForm={}",
                            resolvedAppId, clientEnv.getCode(), payForm.getCode(), e);
                    continue;
                }
                // 跟随支付同路径路由出产品(路由失败同样跳过)
                NormalPayParam routeParam = new NormalPayParam();
                routeParam.setAppId(resolvedAppId);
                routeParam.setMethod(resolved.method());
                routeParam.setChannelMchNo(resolved.channelMchNo());
                routeParam.setCapability(resolved.capability());
                try {
                    payRouteService.resolve(routeParam);
                } catch (BizException e) {
                    // 路由失败(未启用/未配置)同属预期常态, debug 级
                    log.debug("分账预警跳过路由失败场景: appId={}, clientEnv={}, payForm={}",
                            resolvedAppId, clientEnv.getCode(), payForm.getCode());
                    continue;
                } catch (Exception e) {
                    // 非预期异常会让预警清单缺项, 须留痕
                    log.warn("分账预警路由检查异常, 场景被跳过: appId={}, clientEnv={}, payForm={}",
                            resolvedAppId, clientEnv.getCode(), payForm.getCode(), e);
                    continue;
                }
                String channel = ProductEnum.findByCode(routeParam.getProduct()).getChannel();
                if (!allocCapabilityService.supports(channel)) {
                    warnings.add(new DeviceQrCodeAllocWarningResult()
                            .setClientEnv(clientEnv.getCode())
                            .setPayForm(payForm.getCode())
                            .setProduct(routeParam.getProduct())
                            .setChannel(channel));
                }
            }
        }
        return warnings;
    }

    /// 构建码牌扫码链接
    ///
    /// 同一 paymentGatewayBaseUrl 下按 programType 分流 path:
    /// - h5 → /h/{code}
    /// - mini_app → /m/{code}
    /// 加载与归属校验由调用方壳层完成。
    public String buildCodeLink(String code, String programType) {
        PlatformUrlConfig urlConfig = platformUrlConfigService.getUrlConfig();
        String gatewayBase = urlConfig.getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(gatewayBase)) {
            // 支付网关前端地址未配置
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.common.gatewayUrlNotConfigured");
        }
        // 按落地程序类型选 path; 历史空值按 H5
        String path = QrCodeProgramTypeEnum.MINI_APP.getCode().equals(programType)
                ? CODE_MINI_PATH
                : CODE_H5_PATH;
        return gatewayBase + path + code;
    }

    /// 应用解析: 复用 [MerchantContextLoader#resolveApp], appId 空取商户默认应用, 返回解析后的 appId
    public String resolveAppId(String mchNo, String appId) {
        MchAppInfoAccessInfo mchApp = merchantContextLoader.resolveApp(mchNo, appId);
        return mchApp.getAppId();
    }

    /// 应用解析(可选): 空返回 null 表示不指定(支付时取商户默认应用), 非空校验归属后返回规范化 appId
    ///
    /// 与 [DeviceQrCodeSupportService#resolveAppId] 的区别: 后者空值会兜底取默认应用,
    /// 适合"必须有应用"的场景; 归属绑定类操作须保留 null 语义([DeviceQrCode#appId] 为空即默认应用), 用本方法。
    public String resolveOptionalAppId(String mchNo, String appId) {
        if (StrUtil.isBlank(appId)) {
            return null;
        }
        return resolveAppId(mchNo, appId);
    }

    /// 门店存在且归属指定商户(绑定阶段不强制启用态; mchNo 空时跳过归属比对, 由调用方保证语义)
    public void validateStoreBelongToMch(String storeNo, String mchNo) {
        MchStoreInfo store = mchStoreInfoManager.findByStoreNo(storeNo)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        if (StrUtil.isNotBlank(mchNo) && !Objects.equals(store.getMchNo(), mchNo)) {
            // 商户: 门店不属于当前商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.merchant.storeNoMatch");
        }
    }

    /// 门店解析(可选): 空返回 null, 非空校验归属后返回
    ///
    /// 归属绑定类操作使用, 空值写 null 防止跨商户脏数据(换绑商户时原门店不随行)
    public String resolveStoreNoForBind(String storeNo, String mchNo) {
        if (StrUtil.isBlank(storeNo)) {
            return null;
        }
        validateStoreBelongToMch(storeNo, mchNo);
        return storeNo;
    }

    /// 应用业务字段更新(编码/归属不可改; 归属走 bind/unbind), 实体加载与归属校验由调用方壳层完成
    public void applyUpdate(DeviceQrCode entity, String name, String amountTypeCode,
                            Long fixedAmount, Boolean allocation, String remark) {
        // 金额类型校验
        QrCodeAmountTypeEnum amountType = QrCodeAmountTypeEnum.findByCode(amountTypeCode);
        validateFixedAmount(amountType, fixedAmount);
        entity.setName(name)
                .setAmountType(amountType.getCode())
                .setFixedAmount(amountType == QrCodeAmountTypeEnum.FIXED ? fixedAmount : null)
                .setAllocation(Boolean.TRUE.equals(allocation))
                .setRemark(remark);
        deviceQrCodeManager.updateById(entity);
    }

    /// 应用状态变更(启用/停用), 实体加载与归属校验由调用方壳层完成
    public void applyStatus(DeviceQrCode entity, String status) {
        QrCodeStatusEnum statusEnum = QrCodeStatusEnum.findByCode(status);
        entity.setStatus(statusEnum.getCode());
        deviceQrCodeManager.updateById(entity);
    }

    /// 固定金额校验: fixed 类型必填且大于 0
    public void validateFixedAmount(QrCodeAmountTypeEnum amountType, Long fixedAmount) {
        if (amountType == QrCodeAmountTypeEnum.FIXED) {
            if (Objects.isNull(fixedAmount) || fixedAmount <= 0) {
                // 码牌: 固定金额必须大于 0
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.fixedAmountInvalid");
            }
        }
    }
}
