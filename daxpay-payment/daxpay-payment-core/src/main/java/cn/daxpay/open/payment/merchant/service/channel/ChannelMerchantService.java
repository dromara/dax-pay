package cn.daxpay.open.payment.merchant.service.channel;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProduct;
import cn.daxpay.open.payment.masterdata.service.channel.PayChannelService;
import cn.daxpay.open.payment.masterdata.result.channel.PayChannelResult;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateClientEnvManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCashierItemManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCodeClientEnvManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateClientEnv;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCashierItem;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCodeClientEnv;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantEditParam;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantQuery;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantResult;
import cn.daxpay.open.payment.route.dao.basic.PayRouteBasicConfigManager;
import cn.daxpay.open.payment.route.dao.scene.PayRouteSceneConfigManager;
import cn.daxpay.open.payment.route.entity.basic.PayRouteBasicConfig;
import cn.daxpay.open.payment.route.entity.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/// # 通道商户管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMerchantService {
    private final ChannelMerchantManager channelMerchantManager;
    private final TransService transService;
    private final PayChannelService payChannelService;
    private final PayProductManager payProductManager;

    // === 删除通道商户所需的级联 Manager ===

    // 交易/订单/退款（删除前置校验：硬阻塞）
    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager normalPayOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final RefundOrderManager refundOrderManager;
    // 路由配置（按 channelMchNo 关联）
    private final PayRouteSceneConfigManager payRouteSceneConfigManager;
    private final PayRouteBasicConfigManager payRouteBasicConfigManager;
    // 网关配置子表（DIRECT 模式按 channelMchNo 关联）
    private final GatewayCodeClientEnvManager gatewayCodeClientEnvManager;
    private final GatewayCashierItemManager gatewayCashierItemManager;
    private final GatewayAggregateClientEnvManager gatewayAggregateClientEnvManager;
    // 通道终端台账（按 channelMchNo 关联）
    private final ChannelTerminalManager channelTerminalManager;

    /// 分页
    public PageResult<ChannelMerchantResult> page(PageParam pageParam, ChannelMerchantQuery query){
        PageResult<ChannelMerchantResult> pageResult = MpUtil.toPageResult(channelMerchantManager.page(pageParam,query));
        fillEnvStatus(pageResult.getRecords());
        // 翻译商户名称(mchNo -> mchName, 走系统 @Trans 机制)
        transService.translate(pageResult);
        return pageResult;
    }

    /// 查询详情
    public ChannelMerchantResult findById(Long id){
        ChannelMerchantResult result = channelMerchantManager.findById(id)
                .map(ChannelMerchant::toResult)
                // 通道: 通道商户不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        fillEnvStatus(List.of(result));
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 根据商户号查询通道（开源版不按商户权限过滤，返回全部通道；mchNo 保留以兼容接口）
    public List<PayChannelResult> dropdownByMchNo(String mchNo) {
        return payChannelService.listAll();
    }

    /// 编辑
    @CacheEvict(value = "payment:channel-mch", allEntries = true)
    public void update(ChannelMerchantEditParam param){
        // 通道: 通道商户不存在
        var mchInfo = channelMerchantManager.findById(param.getId()).orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        mchInfo.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchantManager.updateById(mchInfo);
    }

    /// 删除通道商户（逻辑删除）
    ///
    /// 策略：
    /// - **交易类数据存在则拒删**（资金交易/订单/退款硬引用，必须可追溯）
    /// - 通过校验后，按 `channelMchNo` 级联逻辑删除所有平台配置类子表
    /// - 各通道子模块扩展数据通过 [PaymentStrategyFactory#findOptionallyByProduct] 按产品查找
    ///   [ChannelMerchantCleanupStrategy] 策略清理（未实现的通道跳过）
    ///
    /// 设计权衡：
    /// - 路由 `pay_route_*` 与 `gateway_*` 子表均按 `channelMchNo` 强关联，主表删除后必须清理避免脏数据
    /// - 通道扩展表（`XxxChannelMerchant` + `XxxKeyConfig`）由各通道子模块通过 SPI 自清理，
    ///   未实现 SPI 的通道留孤儿数据（主表已删、路由不再命中，对业务无影响）
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "payment:channel-mch", allEntries = true)
    public void delete(Long id){
        // 通道: 通道商户不存在
        var mchInfo = channelMerchantManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        String channelMchNo = mchInfo.getChannelMchNo();

        // 1. 硬阻塞校验：交易/订单/退款存在则拒删
        if (payTradeManager.existedByField(PayTrade::getChannelMchNo, channelMchNo)) {
            // 通道: 通道商户存在交易记录，不允许删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.channelMerchantHasTrade");
        }
        if (normalPayOrderManager.existedByField(NormalPayOrder::getChannelMchNo, channelMchNo)) {
            // 通道: 通道商户存在订单记录，不允许删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.channelMerchantHasOrder");
        }
        if (gatewayPayOrderManager.existedByField(GatewayPayOrder::getChannelMchNo, channelMchNo)) {
            // 通道: 通道商户存在网关订单记录，不允许删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.channelMerchantHasGatewayOrder");
        }
        if (refundOrderManager.existedByField(RefundOrder::getChannelMchNo, channelMchNo)) {
            // 通道: 通道商户存在退款记录，不允许删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.channelMerchantHasRefund");
        }

        // 2. 平台配置类级联清理（按 channelMchNo）
        payRouteSceneConfigManager.deleteByField(PayRouteSceneConfig::getChannelMchNo, channelMchNo);
        payRouteBasicConfigManager.deleteByField(PayRouteBasicConfig::getChannelMchNo, channelMchNo);
        gatewayCodeClientEnvManager.deleteByField(GatewayCodeClientEnv::getChannelMchNo, channelMchNo);
        gatewayCashierItemManager.deleteByField(GatewayCashierItem::getChannelMchNo, channelMchNo);
        gatewayAggregateClientEnvManager.deleteByField(GatewayAggregateClientEnv::getChannelMchNo, channelMchNo);
        channelTerminalManager.deleteByField(ChannelTerminal::getChannelMchNo, channelMchNo);

        // 3. 通道扩展表 + KeyConfig 清理（按 product 一对一,未实现策略的通道静默跳过）
        PaymentStrategyFactory.findOptionallyByProduct(mchInfo.getProduct(), ChannelMerchantCleanupStrategy.class)
                .ifPresent(strategy -> strategy.deleteByChannelMchNo(channelMchNo));

        // 4. 主表逻辑删
        channelMerchantManager.deleteById(id);
    }

    /// 根据商户和支付产品查询通道商户号列表, 多数支付通道配置使用
    public List<LabelValue> dropdown(String mchNo, String product){
        return channelMerchantManager.findAllByMchNoAndProduct(mchNo, product).stream()
                .map(mch -> new LabelValue(mch.getChannelMerchantName(), mch.getChannelMchNo()))
                .toList();
    }

    /// 根据商户号查询所有通道商户
    public List<ChannelMerchantResult> findAllByMchNo(String mchNo){
        List<ChannelMerchantResult> results = channelMerchantManager.findAllByMchNo(mchNo).stream()
                .map(ChannelMerchant::toResult)
                .toList();
        fillEnvStatus(results);
        return results;
    }

    /// 批量填充沙箱支持标志
    ///
    /// - sandbox: 直接读实体固化的字段(创建时按当时产品 activeEnv 写入, 之后不再随产品切换改变)
    /// - sandboxSupport: 该产品是否支持沙箱(决定前端是否显示环境标签), 来自 pay_md_product
    ///
    /// 注意: 不再填充"产品当前 activeEnv"到商户 Result。
    /// 商户环境信息以固化的 [ChannelMerchantResult#isSandbox] 为准;
    /// 产品当前生效环境属于产品级状态(可通过产品配置接口查询), 不应冗余到商户 Result 上。
    private void fillEnvStatus(List<ChannelMerchantResult> results) {
        if (results.isEmpty()) {
            return;
        }
        Set<String> products = results.stream()
                .map(ChannelMerchantResult::getProduct)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (products.isEmpty()) {
            return;
        }
        // 沙箱支持标志(来自支付产品表, 决定前端是否显示环境标签)
        Map<String, Boolean> sandboxMap = payProductManager.lambdaQuery()
                .in(PayProduct::getCode, products)
                .list()
                .stream()
                .collect(Collectors.toMap(PayProduct::getCode, p -> Boolean.TRUE.equals(p.getSandbox()), (a, b) -> a));
        results.forEach(r -> r.setSandboxSupport(sandboxMap.getOrDefault(r.getProduct(), false)));
    }

    /// 更新启用状态
    @CacheEvict(value = "payment:channel-mch", allEntries = true)
    public void updateEnable(Long id, Boolean enable){
        // 通道: 通道商户不存在
        var mchInfo = channelMerchantManager.findById(id).orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        mchInfo.setEnable(enable);
        channelMerchantManager.updateById(mchInfo);
    }

}
