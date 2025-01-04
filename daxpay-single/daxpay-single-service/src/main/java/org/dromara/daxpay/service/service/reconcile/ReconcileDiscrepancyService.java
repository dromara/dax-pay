package org.dromara.daxpay.service.service.reconcile;

import cn.bootx.platform.common.mybatisplus.function.CollectorsFunction;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.bo.reconcile.PlatformReconcileTradeBo;
import org.dromara.daxpay.service.dao.reconcile.ReconcileDiscrepancyManager;
import org.dromara.daxpay.service.entity.reconcile.ChannelReconcileTrade;
import org.dromara.daxpay.service.entity.reconcile.ReconcileDiscrepancy;
import org.dromara.daxpay.service.entity.reconcile.ReconcileStatement;
import org.dromara.daxpay.service.param.reconcile.ReconcileDiscrepancyQuery;
import org.dromara.daxpay.service.result.reconcile.ReconcileDiscrepancyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对账差异记录服务
 * @author xxm
 * @since 2024/8/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileDiscrepancyService {
    private final ReconcileDiscrepancyManager reconcileDiscrepancyManager;
    private final ReconcileAssistService reconcileAssistService;

    /**
     * 分页
     */
    public PageResult<ReconcileDiscrepancyResult> page(PageParam pageParam, ReconcileDiscrepancyQuery query) {
        return MpUtil.toPageResult(reconcileDiscrepancyManager.page(pageParam, query));
    }

    /**
     * 详情
     */
    public ReconcileDiscrepancyResult findById(Long id) {
        return reconcileDiscrepancyManager.findById(id).map(ReconcileDiscrepancy::toResult)
                .orElseThrow(() -> new DataNotExistException("对账差异记录不存在"));
    }


    /**
     * 比对生成对账差异单, 通道对账单
     * 1. 远程有, 本地无
     * 2. 远程无, 本地有
     * 3. 远程有, 本地有, 但信息(金额/状态/交易类型)不一致
     *
     * @param statement 对账单
     * @param localTrades 本地交易明细
     * @param channelTrades 通道交易明细
     */
    public List<ReconcileDiscrepancy> generateDiscrepancy(ReconcileStatement statement,
                                                          List<PlatformReconcileTradeBo> localTrades,
                                                          List<ChannelReconcileTrade> channelTrades){
        List<ReconcileDiscrepancy> discrepancies = new ArrayList<>();
        // 通道交易记录
        Map<String, ChannelReconcileTrade> channelDetailMap = channelTrades.stream()
                .collect(Collectors.toMap(ChannelReconcileTrade::getOutTradeNo, Function.identity(), CollectorsFunction::retainLatest));
        // 平台交易记录
        Map<String, PlatformReconcileTradeBo> localTradeMap = localTrades.stream()
                .collect(Collectors.toMap(PlatformReconcileTradeBo::getTradeNo, Function.identity(), CollectorsFunction::retainLatest));

        // 筛选出本地短单订单交易号
        List<String> localShortTradeNos = channelTrades.stream()
                .map(ChannelReconcileTrade::getOutTradeNo)
                // 查询本地不存在的订单记录
                .filter(o-> !localTradeMap.containsKey(o))
                .toList();
        // 针对本地短单的交易进行二次查询, 不限定交易状态, 然后追加到到本地交易记录中
        var localShortTrades = reconcileAssistService.getPlatformTradesByTradeNo(localShortTradeNos);
        localTrades.addAll(localShortTrades);
        var finalLocalTradeMap = localTrades.stream()
                .collect(Collectors.toMap(PlatformReconcileTradeBo::getTradeNo, Function.identity(), CollectorsFunction::retainLatest));

        // 首先使用通道交易记录和本地交易记录进行比对
        for (var channelDetail : channelTrades) {
            var localTrade = finalLocalTradeMap.get(channelDetail.getOutTradeNo());
            // 本地短单, 先进行记录, 然后二次处理
            if (Objects.isNull(localTrade)){
                discrepancies.add(reconcileAssistService.buildDiscrepancy(statement,channelDetail));
                continue;
            }
            // 如果远程和本地都存在, 判断是否有差异
            if (!this.reconcileDiff(channelDetail, localTrade)) {
                discrepancies.add(reconcileAssistService.buildDiscrepancy(statement, localTrade, channelDetail));
            }
        }

        // 使用本地与对账单比对, 找出远程短单的交易
        for (var localTrade : localTrades) {
            var channelTrade = channelDetailMap.get(localTrade.getTradeNo());
            if (Objects.isNull(channelTrade)){
                discrepancies.add(reconcileAssistService.buildDiscrepancy(statement, localTrade));
            }
        }
        return discrepancies;
    }

    /**
     * 判断订单之间存是否有差异, 没有差异返回true, 有差异返回false
     * @param outDetail 下载的对账订单(通道交易)
     * @param localTrade 本地交易订单(平台交易)
     */
    private boolean reconcileDiff(ChannelReconcileTrade outDetail, PlatformReconcileTradeBo localTrade){

        // 判断类型是否相同
        if (!Objects.equals(outDetail.getTradeType(), localTrade.getTradeType())){
            return true;
        }
        // 判断金额是否一致
        if (!Objects.equals(outDetail.getAmount(), localTrade.getAmount())){
            return true;
        }
        // 判断状态是否一致
        if (!Objects.equals(outDetail.getTradeStatus(), localTrade.getTradeStatus())){
            return true;
        }
        return false;
    }
}
