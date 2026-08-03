package cn.daxpay.open.payment.trade.runtime.service.query;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.result.trade.PayResultRedirectResult;
import cn.daxpay.open.payment.unipay.result.trade.PayResultResult;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/// # 支付结果查询服务
///
/// 供平台 H5 结果页(/pay-result/{tradeNo})的无签名查询接口使用。
/// 凭 tradeNo 跨租户反查资金交易([PayTrade]) → 按 tradeType 反查容器
/// ([NormalPayOrder]/[GatewayPayOrder]), 返回订单状态/摘要;
/// 支付成功(paid)且有商户 returnUrl 时, 生成带平台签名的跳转地址 redirectUrl。
///
/// ## 两条路径共用
/// - 通道同步回跳(支付宝 return_url → /pay-result/{tradeNo}): 通道回跳作触发, 本接口查权威状态
/// - jsapi 前端轮询(收银台/聚合页终态后): 调本接口拿带签名 redirectUrl 再跳商户 returnUrl
///
/// ## 安全模型
/// - 该接口无商户签名(消费者浏览器调用), 凭 tradeNo 高熵不可猜防枚举
/// - 仅返回该订单的公开状态/摘要, 不泄露敏感配置
/// - 跳商户 returnUrl 的业务参数由平台私钥签名, 商户用平台公钥验签(与异步通知同密钥体系)
@Slf4j
@Service
@RequiredArgsConstructor
public class PayResultQueryService {

    /// 终态状态集合(两种容器共用: paid/failed/closed/expired)
    private static final Set<String> FINAL_STATES = Set.of("paid", "failed", "closed", "expired");

    /// 支付成功状态码
    private static final String STATUS_PAID = "paid";

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager normalPayOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final MerchantContextLoader merchantContextLoader;
    private final PlatformConfigProperties platformConfigProperties;

    /// 凭 tradeNo 查询支付结果
    ///
    /// @param tradeNo 资金交易号(通道回跳/jsapi 轮询均携带)
    public PayResultResult queryByTradeNo(String tradeNo) {
        // 跨租户查资金交易(结果页无商户上下文)
        PayTrade trade = payTradeManager.findByTradeNoNotTenant(tradeNo)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.payOrderNotExist"));
        // 装载商户上下文, 后续容器查询走租户隔离
        merchantContextLoader.initMch(trade.getMchNo());

        PayResultResult result = new PayResultResult()
                .setTradeNo(trade.getTradeNo())
                .setAmount(trade.getAmount())
                .setProvider(trade.getProvider());

        // 按 tradeType 反查容器, 取业务字段
        String tradeType = trade.getTradeType();
        if (PayTradeTypeEnum.NORMAL.getCode().equals(tradeType)) {
            fillFromNormal(result, trade);
        } else if (PayTradeTypeEnum.GATEWAY.getCode().equals(tradeType)) {
            fillFromGateway(result, trade);
        } else {
            // 其余形态(authorize/capture/recurring/combine_sub)暂不支持结果页查询
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.payOrderNotExist");
        }

        // 终态判断
        boolean finalState = FINAL_STATES.contains(result.getStatus());
        result.setFinalState(finalState);

        // 仅支付成功(paid) + 有 returnUrl 时, 生成带签名的跳转地址
        // 失败/关闭/过期或无 returnUrl 时, redirectUrl 为空, 前端展示结束页不跳转
        if (finalState && STATUS_PAID.equals(result.getStatus()) && StrUtil.isNotBlank(result.getReturnUrl())) {
            result.setRedirectUrl(buildSignedRedirectUrl(result));
        }
        return result;
    }

    /// 从普通支付容器填充业务字段
    private void fillFromNormal(PayResultResult result, PayTrade trade) {
        NormalPayOrder order = normalPayOrderManager.findByIdNotTenant(trade.getContainerId())
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.payOrderNotExist"));
        result.setOrderNo(order.getOrderNo())
                .setBizOrderNo(order.getBizOrderNo())
                .setStatus(order.getStatus())
                .setTitle(order.getTitle())
                .setCurrency(order.getCurrency())
                .setReturnUrl(order.getReturnUrl());
        // 容器金额权威
        if (order.getAmount() != null) {
            result.setAmount(order.getAmount());
        }
    }

    /// 从网关支付容器填充业务字段
    private void fillFromGateway(PayResultResult result, PayTrade trade) {
        GatewayPayOrder order = gatewayPayOrderManager.findByIdNotTenant(trade.getContainerId())
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.payOrderNotExist"));
        result.setOrderNo(order.getOrderNo())
                .setBizOrderNo(order.getBizOrderNo())
                .setStatus(order.getStatus())
                .setTitle(order.getTitle())
                .setCurrency(order.getCurrency())
                .setReturnUrl(order.getReturnUrl());
        if (order.getAmount() != null) {
            result.setAmount(order.getAmount());
        }
    }

    /// 构建带平台签名的商户跳转地址
    ///
    /// 复用 OAuth 回调签名范式(OpenAuthService): 组装业务参数 → 平台私钥签名 → 拼 query string。
    /// 商户用平台公钥验签, 规则与异步通知/支付接口一致。
    private String buildSignedRedirectUrl(PayResultResult result) {
        PayResultRedirectResult redirect = new PayResultRedirectResult()
                .setCode(CommonCode.SUCCESS_CODE)
                .setMsg(CommonCode.SUCCESS_MSG)
                .setTradeNo(result.getTradeNo())
                .setOrderNo(result.getOrderNo())
                .setBizOrderNo(result.getBizOrderNo())
                .setStatus(result.getStatus())
                .setAmount(result.getAmount());
        // 平台私钥签名
        String privateKey = platformConfigProperties.getKeyConfig().getPrivateKey();
        redirect.setSign(PaySignUtil.sign(redirect, privateKey));
        return appendQueryParams(result.getReturnUrl(), redirect);
    }

    /// 将跳转参数拼接为 query string 追加到商户 returnUrl
    ///
    /// returnUrl 已带 query 时用 `&` 拼接, 否则用 `?` 起头; 值做 URL 编码。
    private String appendQueryParams(String baseUrl, PayResultRedirectResult redirect) {
        StringBuilder sb = new StringBuilder(baseUrl);
        sb.append(baseUrl.contains("?") ? "&" : "?");
        sb.append("code=").append(redirect.getCode());
        sb.append("&msg=").append(URLUtil.encode(redirect.getMsg()));
        if (StrUtil.isNotBlank(redirect.getTradeNo())) {
            sb.append("&tradeNo=").append(URLUtil.encode(redirect.getTradeNo()));
        }
        if (StrUtil.isNotBlank(redirect.getOrderNo())) {
            sb.append("&orderNo=").append(URLUtil.encode(redirect.getOrderNo()));
        }
        if (StrUtil.isNotBlank(redirect.getBizOrderNo())) {
            sb.append("&bizOrderNo=").append(URLUtil.encode(redirect.getBizOrderNo()));
        }
        sb.append("&status=").append(URLUtil.encode(redirect.getStatus()));
        if (redirect.getAmount() != null) {
            sb.append("&amount=").append(redirect.getAmount());
        }
        sb.append("&sign=").append(URLUtil.encode(redirect.getSign()));
        return sb.toString();
    }
}
