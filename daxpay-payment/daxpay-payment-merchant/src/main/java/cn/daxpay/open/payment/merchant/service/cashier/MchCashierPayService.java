package cn.daxpay.open.payment.merchant.service.cashier;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminal;
import cn.daxpay.open.payment.merchant.param.cashier.MchCashierOrderStatusParam;
import cn.daxpay.open.payment.merchant.param.cashier.MchCashierPayParam;
import cn.daxpay.open.payment.merchant.result.cashier.MchCashierPayResult;
import cn.daxpay.open.payment.merchant.service.store.MchStoreInfoService;
import cn.daxpay.open.payment.trade.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.pay.normal.NormalPayService;
import cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.param.trade.pay.TerminalInfo;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.enums.pay.pay.PayStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/// # 商户收银服务(PC收款精灵)
///
/// 登录态被扫收款: 复用 [NormalPayService#pay] 全链路, 不复制支付逻辑, 不新增订单表。
/// 幂等键取 clientOrderNo, 直接作为 bizOrderNo 落 [NormalPayOrder], 依托
/// `uk_normal_order_mch_biz(mch_no, biz_order_no)` 保证同商户同单号唯一;
/// 已存在订单时校验金额一致后直接返回当前状态, 并发竞态下捕获支付链路的重复支付异常后回查订单状态。
/// 外层按 clientOrderNo 整体加锁(独立 key), 覆盖「预查 + 支付」全过程, 避免支付链路内层锁释放后
/// 第二个并发请求对同一 trade 再次调用通道。
///
/// 商户身份统一取自 [PaymentContext], 请求参数不携带 mchNo。
@Slf4j
@Service
@RequiredArgsConstructor
public class MchCashierPayService {

    /// 收银设备名称(终端信息上送)
    private static final String CASHIER_DEVICE_NAME = "PC收款精灵";

    /// 默认支付标题
    private static final String DEFAULT_TITLE = "扫码收款";

    /// 外层收银锁持有时间(毫秒), 与支付链路内层锁一致
    private static final long CASHIER_LOCK_EXPIRE_MS = 60_000L;

    /// 外层收银锁等待时间(毫秒)
    private static final long CASHIER_LOCK_WAIT_MS = 3_000L;

    private final PaymentContext paymentContext;
    private final LockExecutor lockExecutor;
    private final NormalPayOrderManager normalPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final NormalPayService normalPayService;
    private final PaySyncService paySyncService;
    private final TerminalDeviceManager terminalDeviceManager;
    private final MchStoreInfoService mchStoreInfoService;
    /// 门店感知应用解析(显式请求门店时取门店默认应用, 见 [#resolveAppId])
    private final MerchantContextLoader merchantContextLoader;

    /// 被扫支付
    ///
    /// 外层按 clientOrderNo 串行化「预查 + 支付」: [NormalPayService] 内部锁仅在单次 pay 调用期间持有,
    /// 首个请求返回后即释放, 第二个并发请求会重入 payHandle 对同一 trade 再次调用通道;
    /// 故此处用独立 key(不复用 `payment:pay:`)的整体锁覆盖全过程, 等待超时直接拒绝。
    /// 幂等规则: 同商户同 clientOrderNo 已存在订单时校验金额一致后返回当前状态
    /// (已支付返回 success 而非报错); 并发竞态下支付链路抛出的重复支付异常同样回查订单后返回。
    public MchCashierPayResult pay(MchCashierPayParam param) {
        String mchNo = this.requireMchNo();
        return lockExecutor.execute(
                "cashier:pay:" + param.getClientOrderNo(),
                CASHIER_LOCK_EXPIRE_MS, CASHIER_LOCK_WAIT_MS,
                () -> this.payInLock(param, mchNo),
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.cashier.processing")
        );
    }

    /// 锁内执行: 预查(命中校验金额) -> 支付 -> 回查
    private MchCashierPayResult payInLock(MchCashierPayParam param, String mchNo) {
        Optional<NormalPayOrder> existing = normalPayOrderManager.findByBizOrderNo(param.getClientOrderNo());
        if (existing.isPresent()) {
            return this.buildExistingResult(existing.get(), param);
        }
        NormalPayParam payParam = this.buildNormalPayParam(param, mchNo);
        try {
            NormalPayResult payResult = normalPayService.pay(payParam);
            // 支付链路已落库, 回查容器与资金凭证返回权威状态
            return normalPayOrderManager.findByBizOrderNo(param.getClientOrderNo())
                    .map(order -> this.buildExistingResult(order, param))
                    .orElseGet(() -> this.fallbackResult(payResult, param));
        } catch (BizInfoException e) {
            if (!this.isRepetitivePayError(e)) {
                throw e;
            }
            // 并发竞态: 另一请求已建单/已在支付, 回查订单返回当前状态
            return normalPayOrderManager.findByBizOrderNo(param.getClientOrderNo())
                    .map(order -> this.buildExistingResult(order, param))
                    .orElseThrow(() -> e);
        }
    }

    /// 幂等命中: 金额不一致视为单号复用, 拒绝把他笔金额的结果当成功返回
    private MchCashierPayResult buildExistingResult(NormalPayOrder order, MchCashierPayParam param) {
        if (!Objects.equals(order.getAmount(), param.getAmount())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.cashier.bizOrderAmountMismatch");
        }
        return this.buildResult(order);
    }

    /// 订单状态查询
    ///
    /// orderNo 优先, 否则按 bizOrderNo 查询(均按当前商户租户隔离);
    /// sync=true 且当前状态为待支付/支付中时主动同步通道, 同步异常只记日志并返回本地状态。
    public MchCashierPayResult orderStatus(MchCashierOrderStatusParam param) {
        this.requireMchNo();
        if (StrUtil.isBlank(param.getOrderNo()) && StrUtil.isBlank(param.getBizOrderNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        NormalPayOrder order = this.findOrder(param);
        String status = this.resolveStatus(order, this.findTrade(order.getId()));
        if (Boolean.TRUE.equals(param.getSync()) && this.needSync(status)) {
            try {
                paySyncService.syncByContainer(order.getId(), PayTradeTypeEnum.NORMAL.getCode());
            } catch (Exception e) {
                log.warn("收银订单同步异常, 返回本地状态: orderNo={}", order.getOrderNo(), e);
            }
            // 同步后重新读取落库结果(同步异常时仍返回原状态)
            order = normalPayOrderManager.findById(order.getId()).orElse(order);
        }
        return this.buildResult(order);
    }

    /// 组装普通支付参数(来源/请求号/请求时间/终端信息均由服务端填充)
    private NormalPayParam buildNormalPayParam(MchCashierPayParam param, String mchNo) {
        NormalPayParam payParam = new NormalPayParam();
        payParam.setMchNo(mchNo);
        // 应用: 显式应用优先; 仅显式门店时取门店默认应用(无效回落商户默认), 解析失败保持空值交内核
        payParam.setAppId(this.resolveAppId(param, mchNo));
        // 幂等键直接作为商户业务单号
        payParam.setBizOrderNo(param.getClientOrderNo());
        payParam.setTitle(StrUtil.blankToDefault(param.getTitle(), DEFAULT_TITLE));
        payParam.setAmount(param.getAmount());
        payParam.setAuthCode(param.getAuthCode());
        payParam.setAttach(StrUtil.blankToDefault(param.getAttach(), null));
        // 来源服务端强制, 不接受前端传值
        payParam.setSource(TradeSourceEnum.MCH_CASHIER.getCode());
        payParam.setReqId(UUID.randomUUID().toString());
        payParam.setReqTime(OffsetDateTime.now());
        // 终端信息: 操作员取当前登录用户, 设备名固定为收银端; 显式门店优先, 空则走商户默认门店
        TerminalInfo terminal = new TerminalInfo();
        terminal.setOperatorId(SecurityUtil.getCurrentUser()
                .map(UserDetail::getId)
                .map(String::valueOf)
                .orElse(null));
        terminal.setDeviceName(CASHIER_DEVICE_NAME);
        terminal.setStoreNo(param.getStoreNo());
        // 终端号: 请求显式指定优先, 空则回落门店默认终端(门店无默认终端时保持无号)
        terminal.setTerminalNo(this.resolveTerminalNo(param, mchNo));
        payParam.setTerminal(terminal);
        // method 不传: 由 NormalPayService 按 authCode 前缀识别
        return payParam;
    }

    /// 解析支付应用号(门店感知): 请求显式应用优先, 为空且显式门店非空时取门店默认应用
    ///
    /// 与码牌/收款单链路同源([MerchantContextLoader#resolveApp(String, String, String)]):
    /// 门店应用无效(不存在/停用/非本商户)时回落商户默认应用; 只使用请求显式 storeNo,
    /// **绝不使用下单链路解析后的默认门店**(否则等于所有无门店收银都改走默认门店应用)。
    /// 解析结果(含回落得到的商户默认应用)显式回填为应用值, 支付内核两参解析行为不变;
    /// 解析异常(如商户无默认应用)保持空值, 交由支付内核按原口径报既有异常。
    private String resolveAppId(MchCashierPayParam param, String mchNo) {
        if (StrUtil.isNotBlank(param.getAppId()) || StrUtil.isBlank(param.getStoreNo())) {
            return StrUtil.blankToDefault(param.getAppId(), null);
        }
        try {
            return merchantContextLoader.resolveApp(mchNo, null, param.getStoreNo()).getAppId();
        } catch (Exception e) {
            // 应用解析失败: 保持空值, 由支付内核按商户默认应用口径抛既有异常
            return null;
        }
    }

    /// 解析收银终端号: 请求显式指定优先, 为空时回落门店默认终端
    ///
    /// 门店确定与建单链路([cn.daxpay.open.payment.trade.runtime.service.pay.normal.NormalPayAssistService])的
    /// [MchStoreInfoService#resolveStoreNo] 语义一致(显式门店优先, 空则取商户默认门店),
    /// 不改变 storeNo 的既有传递行为; 仅商户收银(精灵)链路做此回落, gateway/码牌/OpenAPI 路径不受影响
    /// (OpenAPI 的 [NormalPayParam] 本就支持终端号直传)。
    private String resolveTerminalNo(MchCashierPayParam param, String mchNo) {
        if (StrUtil.isNotBlank(param.getTerminalNo())) {
            return param.getTerminalNo();
        }
        String storeNo = mchStoreInfoService.resolveStoreNo(mchNo, param.getStoreNo());
        return terminalDeviceManager.findDefaultByStoreNo(mchNo, storeNo)
                .map(DeviceTerminal::getTerminalNo)
                .orElse(null);
    }

    /// 按 orderNo 优先、bizOrderNo 兜底查询当前商户订单
    private NormalPayOrder findOrder(MchCashierOrderStatusParam param) {
        if (StrUtil.isNotBlank(param.getOrderNo())) {
            return normalPayOrderManager.findByOrderNo(param.getOrderNo())
                    .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        }
        return normalPayOrderManager.findByBizOrderNo(param.getBizOrderNo())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
    }

    /// 查询容器关联的资金凭证(缺失时返回 null, 状态按容器映射)
    private PayTrade findTrade(Long containerId) {
        return payTradeManager.findByContainerId(containerId, PayTradeTypeEnum.NORMAL.getCode()).orElse(null);
    }

    /// 构建返回结果(容器权威 + 资金凭证补充)
    private MchCashierPayResult buildResult(NormalPayOrder order) {
        PayTrade trade = this.findTrade(order.getId());
        MchCashierPayResult result = new MchCashierPayResult()
                .setOrderId(order.getId())
                .setOrderNo(order.getOrderNo())
                .setBizOrderNo(order.getBizOrderNo())
                .setAmount(order.getAmount())
                .setChannel(StrUtil.blankToDefault(order.getChannel(),
                        trade != null ? trade.getChannel() : null))
                .setProvider(StrUtil.blankToDefault(order.getProvider(),
                        trade != null ? trade.getProvider() : null))
                .setExpiredTime(order.getExpiredTime())
                .setErrorMsg(order.getErrorMsg())
                .setStatus(this.resolveStatus(order, trade));
        if (trade != null) {
            result.setTradeNo(trade.getTradeNo());
            result.setPayTime(Objects.nonNull(trade.getPayTime()) ? trade.getPayTime() : order.getPayTime());
        } else {
            result.setPayTime(order.getPayTime());
        }
        return result;
    }

    /// 支付链路返回成功但回查容器为空时的兜底结果(正常情况下不会发生)
    private MchCashierPayResult fallbackResult(NormalPayResult payResult, MchCashierPayParam param) {
        return new MchCashierPayResult()
                .setOrderNo(payResult.getOrderNo())
                .setBizOrderNo(StrUtil.blankToDefault(payResult.getBizOrderNo(), param.getClientOrderNo()))
                .setTradeNo(payResult.getTradeNo())
                .setAmount(param.getAmount())
                .setStatus(this.resolveFundStatus(payResult.getStatus()));
    }

    /// 状态映射: 容器已支付/资金已成功优先判成功, 其余以资金态为准, 资金态缺失回退容器状态
    private String resolveStatus(NormalPayOrder order, PayTrade trade) {
        if (Objects.equals(order.getStatus(), NormalPayOrderStatusEnum.PAID.getCode())
                || (trade != null && Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode()))) {
            return PayStatusEnum.SUCCESS.getCode();
        }
        if (trade != null) {
            String fundStatus = trade.getStatus();
            if (Objects.equals(fundStatus, PayFundStatusEnum.INIT.getCode())) {
                return PayStatusEnum.WAIT.getCode();
            }
            if (Objects.equals(fundStatus, PayFundStatusEnum.PROCESSING.getCode())) {
                return PayStatusEnum.PROGRESS.getCode();
            }
            if (Objects.equals(fundStatus, PayFundStatusEnum.FAIL.getCode())) {
                return PayStatusEnum.FAIL.getCode();
            }
            if (Objects.equals(fundStatus, PayFundStatusEnum.CANCEL.getCode())) {
                return PayStatusEnum.CANCEL.getCode();
            }
            if (Objects.equals(fundStatus, PayFundStatusEnum.CLOSE.getCode())) {
                // 关闭态区分主动关闭与超时关闭
                return Objects.equals(order.getStatus(), NormalPayOrderStatusEnum.EXPIRED.getCode())
                        ? PayStatusEnum.TIMEOUT.getCode() : PayStatusEnum.CLOSE.getCode();
            }
        }
        return this.resolveContainerStatus(order.getStatus());
    }

    /// 容器业务状态映射
    private String resolveContainerStatus(String status) {
        if (Objects.equals(status, NormalPayOrderStatusEnum.PAID.getCode())) {
            return PayStatusEnum.SUCCESS.getCode();
        }
        if (Objects.equals(status, NormalPayOrderStatusEnum.FAILED.getCode())) {
            return PayStatusEnum.FAIL.getCode();
        }
        if (Objects.equals(status, NormalPayOrderStatusEnum.CLOSED.getCode())) {
            return PayStatusEnum.CLOSE.getCode();
        }
        if (Objects.equals(status, NormalPayOrderStatusEnum.EXPIRED.getCode())) {
            return PayStatusEnum.TIMEOUT.getCode();
        }
        return PayStatusEnum.WAIT.getCode();
    }

    /// 资金态映射(兜底路径使用)
    private String resolveFundStatus(String fundStatus) {
        if (Objects.equals(fundStatus, PayFundStatusEnum.SUCCESS.getCode())) {
            return PayStatusEnum.SUCCESS.getCode();
        }
        if (Objects.equals(fundStatus, PayFundStatusEnum.PROCESSING.getCode())) {
            return PayStatusEnum.PROGRESS.getCode();
        }
        if (Objects.equals(fundStatus, PayFundStatusEnum.FAIL.getCode())) {
            return PayStatusEnum.FAIL.getCode();
        }
        if (Objects.equals(fundStatus, PayFundStatusEnum.CLOSE.getCode())) {
            return PayStatusEnum.CLOSE.getCode();
        }
        if (Objects.equals(fundStatus, PayFundStatusEnum.CANCEL.getCode())) {
            return PayStatusEnum.CANCEL.getCode();
        }
        return PayStatusEnum.WAIT.getCode();
    }

    /// 待支付/支付中才允许主动同步通道
    private boolean needSync(String status) {
        return Objects.equals(status, PayStatusEnum.WAIT.getCode())
                || Objects.equals(status, PayStatusEnum.PROGRESS.getCode());
    }

    /// 支付链路的重复支付异常(锁等待超时/已有成功单), 属幂等场景
    private boolean isRepetitivePayError(BizInfoException e) {
        String message = e.resolveMessageKey();
        return Objects.equals(message, "pay.error.pay.alreadySuccess")
                || Objects.equals(message, "pay.error.pay.processing");
    }

    /// 商户上下文必须已装载
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (StrUtil.isBlank(mchNo)) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

}
