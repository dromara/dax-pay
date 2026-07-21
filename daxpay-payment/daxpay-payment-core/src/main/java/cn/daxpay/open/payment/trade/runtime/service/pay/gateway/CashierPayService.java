package cn.daxpay.open.payment.trade.runtime.service.pay.gateway;

import cn.daxpay.open.payment.common.util.PayMethodOpenIdSupport;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCashierItemManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCashierItem;
import cn.daxpay.open.payment.merchant.enums.CashierItemResolveModeEnum;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.GatewayCashierTypeEnum;
import cn.daxpay.open.payment.strategy.risk.PayRiskChecker;
import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.unipay.param.gateway.CashierPayParam;
import cn.daxpay.open.payment.unipay.result.gateway.CashierItemPublicResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.system.enums.PayRiskOpenIdLevelEnum;
import cn.daxpay.open.platform.system.service.config.security.PlatformSecurityConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/// # 网关收银台支付服务
///
/// 用户点选 [GatewayCashierItem] 后解析 METHOD/DIRECT, 委托 [GatewayPayHandleService] 拉起通道。
/// 与 [AggregatePayService] 并列的产品适配层; 配置敏感字段不下发到公开列表。
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierPayService {

    /// 小程序收银台允许的 clientEnv(与配置侧白名单一致, 不含 browser)
    private static final Set<String> MINI_CLIENT_ENVS = Set.of(
            ClientEnvEnum.WECHAT.getCode(),
            ClientEnvEnum.ALIPAY.getCode(),
            ClientEnvEnum.UNION_PAY.getCode(),
            ClientEnvEnum.DOUYIN.getCode()
    );

    private final GatewayPayAssistService gatewayPayAssistService;
    private final GatewayPayHandleService gatewayPayHandleService;
    private final GatewayCashierItemManager gatewayCashierItemManager;
    /// 风控检查器（可选 SPI：用于判断是否存在 openId 黑名单, 决定是否触发强制 OAuth）
    private final ObjectProvider<PayRiskChecker> payRiskCheckerProvider;
    /// 平台安全配置（读取用户标识拦截级别, 决定 NORMAL 模式下不触发强制 OAuth）
    private final PlatformSecurityConfigService platformSecurityConfigService;

    /// 公开支付项列表(落地页展示)
    public List<CashierItemPublicResult> listPublicItems(String orderNo, String cashierType, String clientEnv) {
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(orderNo);
        if (!Objects.equals(order.getGatewayType(), GatewayPayTypeEnum.CASHIER.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.typeMismatch");
        }
        GatewayCashierTypeEnum typeEnum = GatewayCashierTypeEnum.findByCode(cashierType);
        String bucketClientEnv = normalizeClientEnvForBucket(typeEnum, clientEnv);
        ClientEnvEnum envEnum = StrUtil.isBlank(bucketClientEnv) ? null : ClientEnvEnum.findByCode(bucketClientEnv);
        // 订单已发起支付(支付中)且 method 有值时, 支付方式已锁定, 需标记匹配的支付项供前端禁用切换
        boolean orderLocked = Objects.equals(order.getStatus(), GatewayOrderStatusEnum.PAYING.getCode())
                && StrUtil.isNotBlank(order.getMethod());
        List<GatewayCashierItem> items = gatewayCashierItemManager.listByAppAndBucket(
                order.getAppId(), typeEnum.getCode(), bucketClientEnv);
        // 订单已锁定但当前收银台桶(cashierType + clientEnv)无匹配项: 跨环境打开(如 web 锁定后 h5 打开),
        // 当前环境无法继续该锁定的支付, 提前拒绝, 避免用户进入后才在点支付时被拦截
        if (orderLocked && items.stream().noneMatch(item -> this.isLockedItem(item, order))) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.channelLocked");
        }
        return items.stream()
                .map(item -> this.toPublicResult(item, envEnum)
                        // 命中锁定的支付项置 locked=true, 前端据此自动选中并禁用其他项
                        .setLocked(orderLocked && this.isLockedItem(item, order)))
                .toList();
    }

    /// 判断收银台支付项是否为订单已锁定的支付方式
    ///
    /// - METHOD 模式: 比较支付方式编码
    /// - DIRECT 模式: 比较通道商户号 + 支付能力(均为路由回填到容器的权威值)
    private boolean isLockedItem(GatewayCashierItem item, GatewayPayOrder order) {
        CashierItemResolveModeEnum resolveMode = CashierItemResolveModeEnum.findByCode(item.getResolveMode());
        if (resolveMode == CashierItemResolveModeEnum.METHOD) {
            return Objects.equals(item.getMethod(), order.getMethod());
        }
        if (resolveMode == CashierItemResolveModeEnum.DIRECT) {
            return Objects.equals(item.getChannelMchNo(), order.getChannelMchNo())
                    && Objects.equals(item.getCapability(), order.getCapability());
        }
        return false;
    }

    /// 收银台发起支付
    public NormalPayResult pay(CashierPayParam param) {
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        if (!Objects.equals(order.getGatewayType(), GatewayPayTypeEnum.CASHIER.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.typeMismatch");
        }

        GatewayCashierTypeEnum typeEnum = GatewayCashierTypeEnum.findByCode(param.getCashierType());
        String bucketClientEnv = normalizeClientEnvForBucket(typeEnum, param.getClientEnv());
        GatewayCashierItem item = loadAndCheckItem(param.getItemId(), order.getAppId(), typeEnum, bucketClientEnv);

        String method;
        String channelMchNo = null;
        String capability = null;
        CashierItemResolveModeEnum resolveMode = CashierItemResolveModeEnum.findByCode(item.getResolveMode());
        switch (resolveMode) {
            case METHOD -> {
                // 跟随通道路由: 仅 method
                if (StrUtil.isBlank(item.getMethod())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.cashierItemMethodRequired");
                }
                method = item.getMethod();
            }
            case DIRECT -> {
                // 直接指定: channelMchNo + capability; method 可空由路由反推
                if (StrUtil.isBlank(item.getChannelMchNo()) || StrUtil.isBlank(item.getCapability())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.cashierItemChannelMchRequired");
                }
                channelMchNo = item.getChannelMchNo();
                capability = item.getCapability();
                method = item.getMethod();
            }
            default -> throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvNotSupport");
        }

        String clientIp = StrUtil.blankToDefault(param.getClientIp(), WebServletUtil.getClientIp());
        // H5/MINI 带 clientEnv 编码; WEB 分桶 clientEnv 为 null, 仍可把入参原值写入订单快照
        String clientEnvForOrder = typeEnum.requiresClientEnv()
                ? bucketClientEnv
                : param.getClientEnv();
        return gatewayPayHandleService.handle(order, null, method, channelMchNo, capability,
                param.getOpenId(), clientEnvForOrder, param.getDevice(), clientIp);
    }

    /// 加载支付项并校验归属与分桶
    private GatewayCashierItem loadAndCheckItem(Long itemId, String appId,
                                                GatewayCashierTypeEnum typeEnum, String bucketClientEnv) {
        GatewayCashierItem item = gatewayCashierItemManager.findById(itemId)
                .orElseThrow(() -> new DataNotExistException("pay.error.gateway.cashierItemNotFound"));
        if (!Objects.equals(item.getAppId(), appId)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.cashierItemNotFound");
        }
        if (!Objects.equals(item.getCashierType(), typeEnum.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.cashierItemNotFound");
        }
        // H5/MINI: clientEnv 必须一致; WEB: 项上 clientEnv 为空
        if (!typeEnum.requiresClientEnv()) {
            if (StrUtil.isNotBlank(item.getClientEnv())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.cashierItemNotFound");
            }
        } else if (!Objects.equals(item.getClientEnv(), bucketClientEnv)) {
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
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvRequired");
        }
        ClientEnvEnum env = ClientEnvEnum.findByCode(clientEnv);
        if (typeEnum == GatewayCashierTypeEnum.MINI && !MINI_CLIENT_ENVS.contains(env.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvNotSupport");
        }
        return env.getCode();
    }

    private CashierItemPublicResult toPublicResult(GatewayCashierItem item, ClientEnvEnum clientEnv) {
        // DIRECT 模式下 method 被强制清空(见 GatewayCashierConfigService#normalizeAndValidate),
        // 此时用 capability 兜底判断 needOpenId; capability 与 PayMethodEnum 同码(如 wechat_jsapi),
        // 可直接喂给 PayMethodOpenIdSupport.needsOpenId 判定 JSAPI/MINI 类需求
        String methodOrCapability = StrUtil.blankToDefault(item.getMethod(), item.getCapability());
        return new CashierItemPublicResult()
                .setId(item.getId())
                .setName(item.getName())
                .setIcon(item.getIcon())
                .setRecommend(item.getRecommend())
                .setSortNo(item.getSortNo())
                // openId 触发判定: JSAPI/MINI 业务必需, 或存在 openId 黑名单且环境可 OAuth
                .setNeedOpenId(this.resolveItemNeedOpenId(methodOrCapability, clientEnv));
    }

    /// openId 触发判定（与聚合/码牌同源逻辑）
    ///
    /// 1. JSAPI/MINI 类方式: 业务必需, 永远 true
    /// 2. 主扫/H5 等免用户标识方式: 仅当用户标识拦截级别为 ENHANCED,
    ///    且存在用户标识黑名单且当前 clientEnv 可 OAuth 时 true
    private boolean resolveItemNeedOpenId(String method, ClientEnvEnum clientEnv) {
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
}
