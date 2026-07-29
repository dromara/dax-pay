package cn.daxpay.open.payment.trade.runtime.service.pay.gateway;

import cn.daxpay.open.payment.auth.UnifiedAuthService;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCashierItemManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCashierItem;
import cn.daxpay.open.payment.merchant.enums.CashierItemResolveModeEnum;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.ClientRuntimeEnum;
import cn.daxpay.open.payment.merchant.enums.GatewayCashierTypeEnum;
import cn.daxpay.open.payment.merchant.service.gateway.ClientEnvPayResolveService;
import cn.daxpay.open.payment.route.service.runtime.PayRouteService;
import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.param.gateway.GatewayAuthUrlParam;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.payment.douyin.facade.DouyinAppFacade;
import cn.daxpay.open.payment.douyin.facade.DyAppView;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/// # 网关 H5 授权服务
///
/// 公开端(无商户签名)根据网关订单生成 OAuth 链接, 用于收银台/聚合页取 openId。
/// 统一委托 [UnifiedAuthService]:
/// - **支付宝**: 服务内走平台级 OAuth(本服务跳过通道路由)
/// - **微信/抖音**: 先解析支付路由再交 UnifiedAuthService → 支付产品策略
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayAuthService {

    private static final Set<String> MINI_CLIENT_ENVS = Set.of(
            ClientEnvEnum.WECHAT.getCode(),
            ClientEnvEnum.ALIPAY.getCode(),
            ClientEnvEnum.UNION_PAY.getCode(),
            ClientEnvEnum.DOUYIN.getCode()
    );

    /// 网关 H5 授权支持的认证类型
    private static final Set<ChannelAuthTypeEnum> SUPPORTED_AUTH_TYPES = Set.of(
            ChannelAuthTypeEnum.ALIPAY,
            ChannelAuthTypeEnum.WECHAT,
            ChannelAuthTypeEnum.DOUYIN
    );

    private final GatewayPayAssistService gatewayPayAssistService;
    private final UnifiedAuthService unifiedAuthService;
    private final ClientEnvPayResolveService clientEnvPayResolveService;
    private final PayRouteService payRouteService;
    private final GatewayCashierItemManager gatewayCashierItemManager;
    private final WxAppFacade wxAppFacade;
    private final DouyinAppFacade douyinAppFacade;

    /// 生成授权链接
    public AuthUrlResult generateAuthUrl(GatewayAuthUrlParam param) {
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        ChannelAuthTypeEnum authType = ChannelAuthTypeEnum.findByCode(param.getAuthType());

        if (!SUPPORTED_AUTH_TYPES.contains(authType)) {
            // 网关: 不支持的客户端环境
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvNotSupport");
        }

        GenerateAuthUrlParam authParam = new GenerateAuthUrlParam();
        authParam.setMchNo(order.getMchNo());
        authParam.setAppId(order.getAppId());
        authParam.setReturnPath(param.getReturnPath());
        authParam.setAuthType(authType.getCode());

        // 支付宝由 UnifiedAuthService 走平台 OAuth, 无需通道应用路由; 微信/抖音须同源 resolve
        if (authType != ChannelAuthTypeEnum.ALIPAY) {
            RouteSnapshot route = resolveRoute(order, param);
            authParam.setChannelMchNo(route.channelMchNo());
            // 微信/抖音认证: 入口层 resolve 选定应用, 把档位+主键塞入 param 供策略查密钥
            resolveChannelAppRef(order.getMchNo(), route, authParam, authType);
        }
        return unifiedAuthService.generateAuthUrl(authParam);
    }

    /// 解析通道路由: 订单已锁定优先用快照; 否则按聚合/收银台与支付同源解析
    private RouteSnapshot resolveRoute(GatewayPayOrder order, GatewayAuthUrlParam param) {
        // 支付中已回填通道信息: 与锁定通道同源, 避免二次路由漂移
        if (Objects.equals(order.getStatus(), GatewayOrderStatusEnum.PAYING.getCode())
                && StrUtil.isNotBlank(order.getChannelMchNo())
                && StrUtil.isNotBlank(order.getProduct())) {
            return new RouteSnapshot(
                    order.getProduct(),
                    order.getChannelMchNo(),
                    order.getCapability(),
                    order.getChannelAppId());
        }

        String gatewayType = order.getGatewayType();
        if (Objects.equals(gatewayType, GatewayPayTypeEnum.AGGREGATE.getCode())) {
            return resolveAggregateRoute(order, param);
        }
        if (Objects.equals(gatewayType, GatewayPayTypeEnum.CASHIER.getCode())) {
            return resolveCashierRoute(order, param);
        }
        // 网关: 网关订单类型不匹配
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.typeMismatch");
    }

    /// 聚合: ClientEnvPayResolve + PayRouteService(与 AggregatePayService 同源)
    private RouteSnapshot resolveAggregateRoute(GatewayPayOrder order, GatewayAuthUrlParam param) {
        if (StrUtil.isBlank(param.getClientEnv())) {
            // 参数校验: 客户端环境不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.clientEnv.notBlank");
        }
        ClientEnvEnum clientEnv = ClientEnvEnum.findByCode(param.getClientEnv());
        ClientRuntimeEnum runtime = ClientRuntimeEnum.ofOrDefault(param.getRuntime());
        var resolved = clientEnvPayResolveService.resolveRequired(order.getAppId(), clientEnv, runtime);

        NormalPayParam routeParam = new NormalPayParam();
        routeParam.setMchNo(order.getMchNo());
        routeParam.setAppId(order.getAppId());
        routeParam.setMethod(resolved.method());
        routeParam.setChannelMchNo(resolved.channelMchNo());
        routeParam.setCapability(resolved.capability());
        payRouteService.resolve(routeParam);
        return new RouteSnapshot(
                routeParam.getProduct(),
                routeParam.getChannelMchNo(),
                routeParam.getCapability(),
                null);
    }

    /// 收银台: 按 itemId 解析 METHOD/DIRECT 后再路由(与 CashierPayService 同源)
    private RouteSnapshot resolveCashierRoute(GatewayPayOrder order, GatewayAuthUrlParam param) {
        if (param.getItemId() == null) {
            // 参数校验: 收银台支付项 ID 不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.itemId.notBlank");
        }
        if (StrUtil.isBlank(param.getCashierType())) {
            // 参数校验: 收银台类型不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.cashierType.notBlank");
        }
        GatewayCashierTypeEnum typeEnum = GatewayCashierTypeEnum.findByCode(param.getCashierType());
        String bucketClientEnv = normalizeClientEnvForBucket(typeEnum, param.getClientEnv());
        GatewayCashierItem item = loadAndCheckItem(param.getItemId(), order.getAppId(), typeEnum, bucketClientEnv);

        String method = null;
        String channelMchNo = null;
        String capability = null;
        CashierItemResolveModeEnum resolveMode = CashierItemResolveModeEnum.findByCode(item.getResolveMode());
        switch (resolveMode) {
            case METHOD -> {
                if (StrUtil.isBlank(item.getMethod())) {
                    // 网关: 指定支付方式时支付方式必填
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.cashierItemMethodRequired");
                }
                method = item.getMethod();
            }
            case DIRECT -> {
                if (StrUtil.isBlank(item.getChannelMchNo()) || StrUtil.isBlank(item.getCapability())) {
                    // 网关: 直接指定时通道商户号必填
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.cashierItemChannelMchRequired");
                }
                channelMchNo = item.getChannelMchNo();
                capability = item.getCapability();
                method = item.getMethod();
            }
            default -> {
                // 网关: 不支持的客户端环境
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.clientEnvNotSupport");
            }
        }

        NormalPayParam routeParam = new NormalPayParam();
        routeParam.setMchNo(order.getMchNo());
        routeParam.setAppId(order.getAppId());
        routeParam.setMethod(method);
        routeParam.setChannelMchNo(channelMchNo);
        routeParam.setCapability(capability);
        payRouteService.resolve(routeParam);
        return new RouteSnapshot(
                routeParam.getProduct(),
                routeParam.getChannelMchNo(),
                routeParam.getCapability(),
                null);
    }

    /// 加载收银台支付项并校验归属与类型分桶
    private GatewayCashierItem loadAndCheckItem(Long itemId, String appId,
                                                GatewayCashierTypeEnum typeEnum, String bucketClientEnv) {
        GatewayCashierItem item = gatewayCashierItemManager.findById(itemId)
                // 网关: 收银台支付项不存在
                .orElseThrow(() -> new DataNotExistException("pay.error.gateway.cashierItemNotFound"));
        if (!Objects.equals(item.getAppId(), appId)) {
            // 网关: 收银台支付项不存在（应用号不匹配）
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.cashierItemNotFound");
        }
        if (!Objects.equals(item.getCashierType(), typeEnum.getCode())) {
            // 网关: 收银台支付项不存在（类型不匹配）
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.cashierItemNotFound");
        }
        if (!typeEnum.requiresClientEnv()) {
            if (StrUtil.isNotBlank(item.getClientEnv())) {
                // 网关: 收银台支付项不存在（类型与环境不匹配）
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.cashierItemNotFound");
            }
        } else if (!Objects.equals(item.getClientEnv(), bucketClientEnv)) {
            // 网关: 收银台支付项不存在（环境不匹配）
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.cashierItemNotFound");
        }
        return item;
    }

    /// 规范化分桶用 clientEnv: WEB 固定 null; H5 五档; MINI 四档(无 browser)
    private String normalizeClientEnvForBucket(GatewayCashierTypeEnum typeEnum, String clientEnv) {
        if (!typeEnum.requiresClientEnv()) {
            return null;
        }
        if (StrUtil.isBlank(clientEnv)) {
            // 网关: H5收银台必须指定客户端环境
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvRequired");
        }
        ClientEnvEnum env = ClientEnvEnum.findByCode(clientEnv);
        if (typeEnum == GatewayCashierTypeEnum.MINI && !MINI_CLIENT_ENVS.contains(env.getCode())) {
            // 网关: 不支持的客户端环境
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvNotSupport");
        }
        return env.getCode();
    }

    /// 通道认证: 入口层 resolve 选定应用, 把档位 + 主键塞入 param
    ///
    /// 认证只取一个应用做 OAuth, 直连/服务商/聚合通道统一走单应用 resolve
    /// (通道档优先、平台档兜底, 遵循"先查通道后查平台"原则), 不感知 sp/sub。
    /// 档位语义通道无关, 统一写入 appScope/appRefId:
    /// - **微信**: [WxAppFacade#resolve] 按 capability 选应用
    /// - **抖音**: [DouyinAppFacade#resolveWebAppForH5Auth] 固定选网站应用(web_app)
    private void resolveChannelAppRef(String mchNo, RouteSnapshot route,
                                      GenerateAuthUrlParam authParam, ChannelAuthTypeEnum authType) {
        if (authType == ChannelAuthTypeEnum.WECHAT) {
            WxAppView app = wxAppFacade.resolve(
                    mchNo, route.channelMchNo(), route.capability(), route.channelAppId(), route.product());
            authParam.setAppScope(app.scope().getCode());
            authParam.setAppRefId(app.id());
        }
        else if (authType == ChannelAuthTypeEnum.DOUYIN) {
            // 抖音 H5 silent_auth 固定使用网站应用(web_app)
            DyAppView app = douyinAppFacade.resolveWebAppForH5Auth(
                    mchNo, route.channelMchNo(), route.channelAppId());
            authParam.setAppScope(app.scope().getCode());
            authParam.setAppRefId(app.id());
        }
    }

    /// 路由快照(供组装 GenerateAuthUrlParam)
    private record RouteSnapshot(String product, String channelMchNo, String capability, String channelAppId) {
    }
}
