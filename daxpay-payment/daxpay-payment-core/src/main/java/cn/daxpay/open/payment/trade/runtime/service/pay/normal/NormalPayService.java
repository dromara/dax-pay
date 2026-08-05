package cn.daxpay.open.payment.trade.runtime.service.pay.normal;

import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordSceneEnum;
import cn.daxpay.open.platform.capability.sensitiveword.service.SensitiveWordCheckService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.ChannelResultUnknownException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.util.PayBarCodeUtil;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.runtime.service.pay.common.PayRiskAssistService;
import cn.daxpay.open.payment.trade.runtime.service.pay.common.PayUniHandleService;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.route.service.runtime.PayRouteService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.unipay.param.trade.pay.GoodsDetail;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 普通支付服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class NormalPayService {

    private final NormalPayAssistService payAssistService;
    private final PayUniHandleService payUniHandleService;
    private final LockExecutor lockExecutor;
    private final PayRouteService payRouteService;
    private final MerchantContextLoader merchantContextLoader;
    private final SensitiveWordCheckService sensitiveWordCheckService;
    private final PayRiskAssistService payRiskAssistService;

    /// 自注入，保证 [NormalPayService#paySuccess] 走 Spring 事务代理
    @Lazy
    private final NormalPayService self;

    /// 支付入口
    public NormalPayResult pay(NormalPayParam payParam) {
        // 客户端IP兜底: 商户未传时从当前HTTP请求提取, 供通道 location_info 等场景使用
        if (StrUtil.isBlank(payParam.getClientIp())) {
            payParam.setClientIp(WebServletUtil.getClientIp());
        }
        // 敏感词：支付标题/描述/商品名（开放 API 不走 Spring @Validated 注解注入）
        this.assertSensitiveWordClean(payParam);
        payAssistService.validationExpiredTime(payParam.getExpiredTime());
        String bizOrderNo = payParam.getBizOrderNo();
        // 锁租期 60s 覆盖通道 HTTP 超时(40s), 等待 3s 让并发同号请求排队而非立即失败;
        // 原 10s 默认值在慢通道下会提前释放, 导致同号请求重入并发调通道
        return lockExecutor.execute(
                "payment:pay:" + bizOrderNo,
                60000, 3000,
                () -> this.payHandle(payParam),
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.processing")
        );
    }

    /// 校验支付展示类文本敏感词
    private void assertSensitiveWordClean(NormalPayParam payParam) {
        if (payParam == null) {
            return;
        }
        sensitiveWordCheckService.assertClean(payParam.getTitle(), SensitiveWordSceneEnum.PAY_TITLE);
        sensitiveWordCheckService.assertClean(payParam.getDescription(), SensitiveWordSceneEnum.PAY_DESCRIPTION);
        if (CollUtil.isEmpty(payParam.getGoodsDetail())) {
            return;
        }
        for (GoodsDetail goods : payParam.getGoodsDetail()) {
            if (goods == null) {
                continue;
            }
            sensitiveWordCheckService.assertClean(goods.getGoodsName(), SensitiveWordSceneEnum.GOODS_NAME);
            sensitiveWordCheckService.assertClean(goods.getDescription(), SensitiveWordSceneEnum.GOODS_DESCRIPTION);
        }
    }

    /// 支付操作
    /// 拆分为多阶段: 1.应用解析与校验 2.付款码识别 3.通道路由 4.通道预处理 5.风控 6.建单 7.发起支付 8.成功后处理
    public NormalPayResult payHandle(NormalPayParam payParam) {
        // 应用解析: 空则取商户默认应用, 校验启用与归属, 回填到 payParam
        var mchApp = merchantContextLoader.resolveApp(payParam.getMchNo(), payParam.getAppId());
        payParam.setAppId(mchApp.getAppId());
        // 付款码: 有 authCode 且 method 空时按前缀识别并回填分钱包 method, 再走路由
        this.resolveBarcodeMethodIfNeeded(payParam);
        // 路由解析：直接指定(已传 channelMchNo)优先，否则按 appId+method 跟随通道路由匹配
        payRouteService.resolve(payParam);
        var payStrategy = PaymentStrategyFactory.createByProduct(payParam.getProduct(), AbsNormalPayStrategy.class);
        // 支付前处理: 校验与通道配置组装(只依赖请求参数), 含微信 channelAppId 回填; 失败直接抛出不持久化
        var context = new PayStrategyContext().setPayParam(payParam);
        payStrategy.doBeforePay(context);
        // 风控前置: 须在 doBeforePay 之后, 微信 openId 名单依赖已回填的 channelAppId 精确匹配
        payRiskAssistService.checkBeforePay(payParam, this.resolveRiskScene(payParam));
        // 查询已有订单并校验，结果填充到 context
        payAssistService.findAndCheckOrder(payParam.getBizOrderNo(), context);
        // 已拉起支付则返回缓存的支付参数(payBody 仅在容器)
        if (Objects.nonNull(context.getNormalOrder())
                && StrUtil.isNotBlank(context.getNormalOrder().getPayBody())
                && Objects.nonNull(context.getTrade())) {
            return payAssistService.buildResult(context.getTrade(), context.getNormalOrder());
        }
        // 订单不存在则新建（填充 context）
        if (Objects.isNull(context.getTrade())) {
            payAssistService.createOrder(payParam, context);
        }
        PayTrade trade = context.getTrade();
        // 支付操作, 失败标记 trade 为 FAIL 并持久化
        PayTradeResultBo result;
        try {
            result = payStrategy.doPay(context);
        } catch (ChannelResultUnknownException e) {
            // 通道结果未知(网络超时/USER_PAYING/authCode 已使用等): 保持 PROCESSING, 交由定时同步纠正,
            // 避免误判 FAIL 导致"资金已动但订单失败"悬挂; 仅记录错误信息到容器, 不改资金态/不通知
            log.warn("通道结果未知, 保持处理中交由同步纠正: tradeNo={}", trade.getTradeNo(), e);
            payAssistService.recordPayError(context, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("支付出现异常", e);
            trade.setStatus(PayFundStatusEnum.FAIL.getCode());
            String errMsg = (e instanceof PayFailureException)
                    ? e.getMessage() : "支付出现异常: " + e.getMessage();
            payUniHandleService.payFail(trade, errMsg);
            throw e;
        }
        // 事后风控在 payAfterHandel(SUCCESS) 内统一补录
        return self.paySuccess(trade, result);
    }

    /// 付款码 method 回填: 仅 authCode 时识别; 已传分钱包条码 method 时校验前缀一致
    private void resolveBarcodeMethodIfNeeded(NormalPayParam payParam) {
        String authCode = payParam.getAuthCode();
        if (StrUtil.isBlank(authCode)) {
            return;
        }
        if (StrUtil.isBlank(payParam.getMethod())) {
            payParam.setMethod(PayBarCodeUtil.resolveMethodCode(authCode));
            return;
        }
        PayBarCodeUtil.validateMethodMatchesAuthCode(payParam.getMethod(), authCode);
    }

    /// 支付成功后操作
    @Transactional(rollbackFor = Exception.class)
    public NormalPayResult paySuccess(PayTrade trade, PayTradeResultBo result) {
        if (result.isComplete()) {
            trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
            trade.setPayTime(result.getFinishTime());
        }
        // trade.status 在 complete=false 时保持 PROCESSING(createOrder 时已设)
        trade.setOutOrderNo(result.getOutOrderNo());
        // 回执与 payBody 写容器, 由 payAfterHandel 统一处理（含事后风控补录）
        payUniHandleService.payAfterHandel(trade, result);
        return payAssistService.buildResult(trade);
    }

    /// 按交易来源派生风控场景（与 PayRiskHitSceneEnum 编码对齐）：
    /// 码牌(cashier_code)→code；其余 API 直连→api
    private String resolveRiskScene(NormalPayParam payParam) {
        String source = payParam.getSource();
        if (StrUtil.isNotBlank(source) && TradeSourceEnum.CASHIER_CODE.getCode().equals(source)) {
            return "code";
        }
        return "api";
    }
}
