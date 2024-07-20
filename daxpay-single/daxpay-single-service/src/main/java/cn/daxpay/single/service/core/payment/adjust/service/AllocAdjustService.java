package cn.daxpay.single.service.core.payment.adjust.service;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.daxpay.single.core.code.AllocDetailResultEnum;
import cn.daxpay.single.core.code.AllocOrderResultEnum;
import cn.daxpay.single.core.code.AllocOrderStatusEnum;
import cn.daxpay.single.core.util.TradeNoGenerateUtil;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderDetailManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.core.payment.adjust.dto.AllocResultItem;
import cn.daxpay.single.service.core.payment.adjust.param.AllocAdjustParam;
import cn.daxpay.single.service.core.payment.notice.service.ClientNoticeService;
import cn.daxpay.single.service.core.record.adjust.entity.TradeAdjustRecord;
import cn.daxpay.single.service.core.record.adjust.service.TradeAdjustRecordService;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分账调整
 * @author xxm
 * @since 2024/7/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocAdjustService {

    private final LockTemplate lockTemplate;
    private final AllocOrderDetailManager allocOrderDetailManager;
    private final AllocOrderManager allocOrderManager;
    private final ClientNoticeService clientNoticeService;
    private final TradeAdjustRecordService tradeAdjustRecordService;

    /**
     * 分账订单处理
     */
    @Transactional(rollbackFor = Exception.class)
    public String adjust(AllocAdjustParam allocAdjustParam){
        AllocOrder allocOrder = allocAdjustParam.getOrder();
        List<AllocResultItem> resultItems = allocAdjustParam.getResultItems();
        List<AllocOrderDetail> details = allocAdjustParam.getDetails();
        LockInfo lock = lockTemplate.lock("adjust:alloc:" + allocOrder.getOrderId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账调整中，请勿重复操作");
        }
        // 如果是分账结束或失败, 不更新状态
        String status = allocOrder.getStatus();
        // 如果是分账结束或失败, 不进行对订单进行处理
        List<String> list = Arrays.asList(AllocOrderStatusEnum.FINISH.getCode(), AllocOrderStatusEnum.FINISH_FAILED.getCode());
        if (!list.contains(status)){
            Map<Long, AllocOrderDetail> detailMap = allocAdjustParam.getDetails()
                    .stream()
                    .collect(Collectors.toMap(AllocOrderDetail::getId, Function.identity()));
            // 更新状态
            for (AllocResultItem resultItem : resultItems) {

            }

            // 判断明细状态. 获取成功和失败的
            long successCount = details.stream()
                    .map(AllocOrderDetail::getResult)
                    .filter(AllocDetailResultEnum.SUCCESS.getCode()::equals)
                    .count();
            long failCount = details.stream()
                    .map(AllocOrderDetail::getResult)
                    .filter(AllocDetailResultEnum.FAIL.getCode()::equals)
                    .count();

            // 成功和失败都为0 表示进行中
            if (successCount == 0 && failCount == 0){
                allocOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                        .setResult(AllocOrderResultEnum.ALL_PENDING.getCode());
            } else {
                if (failCount == details.size()){
                    // 全部失败
                    allocOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                            .setResult(AllocOrderResultEnum.ALL_FAILED.getCode());
                } else if (successCount == details.size()){
                    // 全部成功
                    allocOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                            .setResult(AllocOrderResultEnum.ALL_SUCCESS.getCode());
                } else {
                    // 部分成功
                    allocOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                            .setResult(AllocOrderResultEnum.PART_SUCCESS.getCode());
                }
            }
            // 更新
            allocOrderDetailManager.updateAllById(details);
            allocOrderManager.updateById(allocOrder);

            // 如果状态为完成, 发送通知
            if (Objects.equals(AllocOrderStatusEnum.ALLOCATION_END.getCode(), allocOrder.getStatus())){
                // 发送通知
                clientNoticeService.registerAllocNotice(allocOrder, details);
            }
            return this.saveRecord(allocAdjustParam).getAdjustNo();
        }

        return null;
    }

    /**
     * 保存记录
     */
    private TradeAdjustRecord saveRecord(AllocAdjustParam param){
        AllocOrder order = param.getOrder();
        TradeAdjustRecord record = new TradeAdjustRecord()
                .setAdjustNo(TradeNoGenerateUtil.adjust())
                .setTradeId(order.getId())
                .setChannel(order.getChannel())
                .setSource(param.getSource().getCode())
                .setTradeNo(order.getOrderNo())
                .setType(TradeTypeEnum.ALLOCATION.getCode());
        tradeAdjustRecordService.saveRecord(record);
        return record;
    }
}
