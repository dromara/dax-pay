package cn.daxpay.open.payment.unipay.client.service;

import cn.daxpay.open.payment.auth.ChannelAuthService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.util.PayMethodOpenIdSupport;
import cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeStatusEnum;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.ClientRuntimeEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
import cn.daxpay.open.payment.merchant.service.gateway.CodePayResolveService;
import cn.daxpay.open.payment.route.service.runtime.PayRouteService;
import cn.daxpay.open.payment.strategy.risk.PayRiskChecker;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.runtime.service.pay.normal.NormalPayService;
import cn.daxpay.open.payment.unipay.client.result.CodePayInfoResult;
import cn.daxpay.open.payment.unipay.client.result.CodePayOrderStatusResult;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.param.device.CodePayAuthUrlParam;
import cn.daxpay.open.payment.unipay.param.device.CodePayParam;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.param.trade.pay.TerminalInfo;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.daxpay.open.platform.system.enums.PayRiskOpenIdLevelEnum;
import cn.daxpay.open.platform.system.service.config.security.PlatformSecurityConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

/// # 码牌支付编排(公开/H5/小程序侧)
///
/// - 查询: 按编码返回脱敏码牌信息(含 programType / needOpenId)
/// - 授权: 按码牌解析商户与策略后生成 OAuth 链接, returnPath 指向分端页
/// - 支付: 读**码牌支付策略**解析 method(不读聚合配置); payForm 由 programType 映射
/// - 状态: 按 orderNo 查询 cashier_code 来源订单脱敏状态
@Slf4j
@Service
@RequiredArgsConstructor
public class CodePayAssistService {

    private final DeviceQrCodeManager deviceQrCodeManager;
    private final MerchantInfoManager merchantInfoManager;
    private final MerchantContextLoader merchantContextLoader;
    private final CodePayResolveService codePayResolveService;
    private final NormalPayService normalPayService;
    private final PayRouteService payRouteService;
    private final ChannelAuthService channelAuthService;
    private final NormalPayOrderManager normalPayOrderManager;
    /// 风控检查器（可选 SPI：用于判断是否存在 openId 黑名单, 决定是否触发强制 OAuth）
    private final ObjectProvider<PayRiskChecker> payRiskCheckerProvider;
    /// 平台安全配置（读取用户标识拦截级别, 决定 NORMAL 模式下不触发强制 OAuth）
    private final PlatformSecurityConfigService platformSecurityConfigService;

    /// 根据码牌编码查询支付信息(公开接口, 脱敏返回)
    ///
    /// @param clientEnv 可选; 传入时解析策略 method 并填充 needOpenId
    public CodePayInfoResult getByCode(String code, String clientEnv) {
        DeviceQrCode entity = this.loadEnabledAssigned(code);
        // 忽略租户查商户: 公开 H5 无商户上下文; 仅取展示名, 不回 mchNo
        MerchantInfo merchant = merchantInfoManager.findByMchNoNotTenant(entity.getMchNo())
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.mchNotFound"));
        // 展示优先简称, 空则回退全称
        String mchShortName = StrUtil.blankToDefault(merchant.getMchShortName(), merchant.getMchName());
        CodePayInfoResult result = new CodePayInfoResult()
                .setCode(entity.getCode())
                .setName(entity.getName())
                .setMchShortName(mchShortName)
                .setAmountType(entity.getAmountType())
                .setFixedAmount(entity.getFixedAmount())
                .setProgramType(entity.getProgramType());
        if (StrUtil.isNotBlank(clientEnv)) {
            try {
                ClientEnvEnum env = ClientEnvEnum.findByCode(clientEnv);
                String method = this.resolveMethod(entity, clientEnv);
                // openId 触发判定: 业务必需(JSAPI/MINI) 或 存在 openId 黑名单且当前环境可 OAuth
                result.setNeedOpenId(this.resolveNeedOpenId(method, env));
            } catch (Exception e) {
                // 策略未配置等: 不误强制授权(false)；支付时会因策略失败再报错
                log.warn("码牌 needOpenId 解析失败 code={} clientEnv={}: {}", code, clientEnv, e.getMessage());
                result.setNeedOpenId(false);
            }
        }
        return result;
    }

    /// 码牌发起支付: 普通订单 + source=cashier_code; 策略仅读码牌配置
    public NormalPayResult pay(CodePayParam param) {
        DeviceQrCode entity = this.loadEnabledAssigned(param.getCode());
        merchantContextLoader.initMch(entity.getMchNo());
        var mchApp = merchantContextLoader.resolveApp(entity.getMchNo(), entity.getAppId());

        long amount = this.resolveAmount(entity, param.getAmount());
        ClientEnvEnum clientEnv = ClientEnvEnum.findByCode(param.getClientEnv());
        if (clientEnv == ClientEnvEnum.BROWSER) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.clientEnvNotSupport");
        }

        // 策略形态由码牌 programType 决定, 不信任客户端擅自换形态
        CodePayFormEnum payForm = CodePayFormEnum.fromProgramType(entity.getProgramType());
        validateRuntimeMatchesPayForm(param.getRuntime(), payForm);

        var resolved = codePayResolveService.resolveRequired(mchApp.getAppId(), clientEnv, payForm);
        // JSAPI/MINI 必须已在授权回跳页换好 openId，支付只带 openId、禁止支付时再换 code
        if (PayMethodOpenIdSupport.needsOpenId(resolved.method()) && StrUtil.isBlank(param.getOpenId())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.openIdRequired");
        }

        String codeName = StrUtil.blankToDefault(entity.getName(), entity.getCode());
        String amountYuan = String.format("%.2f", amount / 100.0);
        String title = StrUtil.format("{} 码牌收款: {}元", codeName, amountYuan);

        NormalPayParam payParam = new NormalPayParam();
        payParam.setMchNo(entity.getMchNo());
        payParam.setAppId(mchApp.getAppId());
        payParam.setBizOrderNo(TradeNoGenerateUtil.order());
        payParam.setTitle(title);
        payParam.setDescription(param.getDescription());
        payParam.setAmount(amount);
        payParam.setMethod(resolved.method());
        payParam.setChannelMchNo(resolved.channelMchNo());
        payParam.setCapability(resolved.capability());
        payParam.setOpenId(param.getOpenId());
        payParam.setClientIp(StrUtil.blankToDefault(param.getClientIp(), WebServletUtil.getClientIp()));
        payParam.setSource(TradeSourceEnum.CASHIER_CODE.getCode());
        // 门店: 码牌显式 storeNo 优先; 空则由下单侧 resolve 默认门店, 仍注入 terminal 便于统一路径
        // 此处只传码牌上的显式值(可空), NormalPayAssistService 再 resolveStoreNo
        TerminalInfo terminal = new TerminalInfo();
        terminal.setStoreNo(entity.getStoreNo());
        payParam.setTerminal(terminal);

        return normalPayService.pay(payParam);
    }

    /// 生成码牌 OAuth 授权链接(公开, 按码牌解析商户/策略/路由)
    ///
    /// 仅 needOpenId 的 method 应调用；回跳落地页立即 code→openId，非支付时再换。
    public AuthUrlResult generateAuthUrl(CodePayAuthUrlParam param) {
        DeviceQrCode entity = this.loadEnabledAssigned(param.getCode());
        merchantContextLoader.initMch(entity.getMchNo());
        var mchApp = merchantContextLoader.resolveApp(entity.getMchNo(), entity.getAppId());

        ClientEnvEnum clientEnv = ClientEnvEnum.findByCode(param.getClientEnv());
        if (clientEnv == ClientEnvEnum.BROWSER) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.clientEnvNotSupport");
        }
        CodePayFormEnum payForm = CodePayFormEnum.fromProgramType(entity.getProgramType());
        var resolved = codePayResolveService.resolveRequired(mchApp.getAppId(), clientEnv, payForm);
        if (!PayMethodOpenIdSupport.canAcquireOpenId(resolved.method(), clientEnv)) {
            // 当前支付方式/环境无法走 OAuth（付款码/APP/PC/外部浏览器/union_pay 一期）
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.authNotRequired");
        }

        // 跟随支付同路径路由, 拿到 product/channelMchNo/capability 供通道认证
        NormalPayParam routeParam = new NormalPayParam();
        routeParam.setMchNo(entity.getMchNo());
        routeParam.setAppId(mchApp.getAppId());
        routeParam.setMethod(resolved.method());
        routeParam.setChannelMchNo(resolved.channelMchNo());
        routeParam.setCapability(resolved.capability());
        payRouteService.resolve(routeParam);

        String returnPath = this.buildReturnPath(clientEnv, entity.getCode());
        GenerateAuthUrlParam authParam = new GenerateAuthUrlParam();
        authParam.setMchNo(entity.getMchNo());
        authParam.setAppId(mchApp.getAppId());
        authParam.setProduct(routeParam.getProduct());
        authParam.setChannelMchNo(routeParam.getChannelMchNo());
        authParam.setCapability(routeParam.getCapability());
        authParam.setReturnPath(returnPath);
        authParam.setAuthType(this.mapAuthType(clientEnv));
        return channelAuthService.generateAuthUrl(authParam);
    }

    /// 查询码牌订单状态(忽略租户; 仅 cashier_code 来源)
    public CodePayOrderStatusResult orderStatus(String orderNo) {
        if (StrUtil.isBlank(orderNo)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "validation.field.orderNo.notBlank");
        }
        NormalPayOrder order = normalPayOrderManager.findByOrderNoNotTenant(orderNo)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        if (!TradeSourceEnum.CASHIER_CODE.getCode().equals(order.getSource())) {
            // 非码牌订单, 视为不存在防越权探测
            throw new DataNotExistException("pay.error.payOrderNotExist");
        }
        return new CodePayOrderStatusResult()
                .setOrderNo(order.getOrderNo())
                .setStatus(order.getStatus())
                .setAmount(order.getAmount())
                .setTitle(order.getTitle());
    }

    /// 请求 runtime 非空时须与 programType 映射的 payForm 一致
    private void validateRuntimeMatchesPayForm(String runtime, CodePayFormEnum payForm) {
        if (StrUtil.isBlank(runtime)) {
            return;
        }
        ClientRuntimeEnum reqRuntime;
        try {
            reqRuntime = ClientRuntimeEnum.findByCode(runtime);
        } catch (Exception e) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.codeRuntimeMismatch");
        }
        boolean expectMini = payForm == CodePayFormEnum.MINI;
        boolean reqMini = reqRuntime == ClientRuntimeEnum.MINI;
        if (expectMini != reqMini) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.codeRuntimeMismatch");
        }
    }

    private DeviceQrCode loadEnabledAssigned(String code) {
        DeviceQrCode entity = deviceQrCodeManager.findByCode(code)
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        if (!QrCodeStatusEnum.ENABLED.getCode().equals(entity.getStatus())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.disabled");
        }
        if (StrUtil.isBlank(entity.getMchNo())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.notAssigned");
        }
        return entity;
    }

    private long resolveAmount(DeviceQrCode entity, Long requestAmount) {
        QrCodeAmountTypeEnum amountType = QrCodeAmountTypeEnum.findByCode(entity.getAmountType());
        if (amountType == QrCodeAmountTypeEnum.FIXED) {
            if (entity.getFixedAmount() == null || entity.getFixedAmount() <= 0) {
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.fixedAmountInvalid");
            }
            return entity.getFixedAmount();
        }
        if (requestAmount == null || requestAmount <= 0) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.amountRequired");
        }
        return requestAmount;
    }

    /// 解析码牌策略 method(供 needOpenId)
    private String resolveMethod(DeviceQrCode entity, String clientEnvCode) {
        ClientEnvEnum clientEnv = ClientEnvEnum.findByCode(clientEnvCode);
        if (clientEnv == ClientEnvEnum.BROWSER) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.clientEnvNotSupport");
        }
        merchantContextLoader.initMch(entity.getMchNo());
        var mchApp = merchantContextLoader.resolveApp(entity.getMchNo(), entity.getAppId());
        CodePayFormEnum payForm = CodePayFormEnum.fromProgramType(entity.getProgramType());
        return codePayResolveService.resolveRequired(mchApp.getAppId(), clientEnv, payForm).method();
    }

    /// openId 触发判定
    ///
    /// 1. JSAPI/MINI 类方式: 业务必需, 永远 true（与历史行为一致）
    /// 2. 主扫/H5 等免用户标识方式: 仅当用户标识拦截级别为 ENHANCED,
    ///    且存在用户标识黑名单且当前 clientEnv 可 OAuth 时 true,
    ///    实现用户标识黑名单在码牌场景的全局拦截
    private boolean resolveNeedOpenId(String method, ClientEnvEnum clientEnv) {
        if (PayMethodOpenIdSupport.needsOpenId(method)) {
            return true;
        }
        PayRiskChecker checker = payRiskCheckerProvider.getIfAvailable();
        if (checker == null || !isEnhancedOpenIdLevel() || !checker.hasOpenIdBlacklist()) {
            return false;
        }
        return PayMethodOpenIdSupport.canAcquireOpenId(method, clientEnv);
    }

    /// 用户标识拦截级别是否为增强模式（NORMAL 时跳过强制 OAuth, 保留用户体验）
    private boolean isEnhancedOpenIdLevel() {
        String level = platformSecurityConfigService.getPaySecurityConfig().getRiskOpenIdLevel();
        return PayRiskOpenIdLevelEnum.ENHANCED.getCode().equals(level);
    }

    /// 分端页 returnPath: /h/wechat|{alipay}/:code?authed=1
    private String buildReturnPath(ClientEnvEnum clientEnv, String code) {
        String segment = switch (clientEnv) {
            case WECHAT -> "wechat";
            case ALIPAY -> "alipay";
            case UNION_PAY -> "union-pay";
            case DOUYIN -> "douyin";
            default -> throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.clientEnvNotSupport");
        };
        return "/h/" + segment + "/" + code + "?authed=1";
    }

    private String mapAuthType(ClientEnvEnum clientEnv) {
        return switch (clientEnv) {
            case ALIPAY -> ChannelAuthTypeEnum.ALIPAY.getCode();
            case WECHAT -> ChannelAuthTypeEnum.WECHAT.getCode();
            case DOUYIN -> ChannelAuthTypeEnum.DOUYIN.getCode();
            default -> ChannelAuthTypeEnum.WECHAT.getCode();
        };
    }
}
