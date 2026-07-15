package cn.daxpay.open.payment.trade.runtime.service.pay.gateway;

import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCashierItemManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCashierItem;
import cn.daxpay.open.payment.merchant.enums.CashierItemResolveModeEnum;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.GatewayCashierTypeEnum;
import cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.unipay.param.gateway.CashierPayParam;
import cn.daxpay.open.payment.unipay.result.gateway.CashierItemPublicResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /// 公开支付项列表(落地页展示)
    public List<CashierItemPublicResult> listPublicItems(String orderNo, String cashierType, String clientEnv) {
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(orderNo);
        if (!Objects.equals(order.getGatewayType(), GatewayPayTypeEnum.CASHIER.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.typeMismatch");
        }
        GatewayCashierTypeEnum typeEnum = GatewayCashierTypeEnum.findByCode(cashierType);
        String bucketClientEnv = normalizeClientEnvForBucket(typeEnum, clientEnv);
        return gatewayCashierItemManager.listByAppAndBucket(order.getAppId(), typeEnum.getCode(), bucketClientEnv)
                .stream()
                .map(this::toPublicResult)
                .toList();
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

    private CashierItemPublicResult toPublicResult(GatewayCashierItem item) {
        return new CashierItemPublicResult()
                .setId(item.getId())
                .setName(item.getName())
                .setIcon(item.getIcon())
                .setRecommend(item.getRecommend())
                .setSortNo(item.getSortNo());
    }
}
