package cn.daxpay.single.service.core.order.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.code.AllocDetailResultEnum;
import cn.daxpay.single.code.AllocOrderStatusEnum;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.PayOrderAllocStatusEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.param.payment.allocation.AllocReceiverParam;
import cn.daxpay.single.param.payment.allocation.AllocationParam;
import cn.daxpay.single.service.common.context.NoticeLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderDetailManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderExtraManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderExtra;
import cn.daxpay.single.service.core.order.allocation.entity.OrderAndDetail;
import cn.daxpay.single.service.core.order.pay.dao.PayOrderManager;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.payment.allocation.dao.AllocationReceiverManager;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocationReceiver;
import cn.daxpay.single.service.dto.allocation.AllocationGroupReceiverResult;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderDetailDto;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderDto;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderExtraDto;
import cn.daxpay.single.service.param.order.AllocationOrderQuery;
import cn.daxpay.single.util.OrderNoGenerateUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分账订单服务
 * @author xxm
 * @since 2024/4/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationOrderService {
    private final AllocationReceiverManager receiverManager;

    private final AllocationOrderManager allocationOrderManager;

    private final AllocationOrderDetailManager allocationOrderDetailManager;

    private final PayOrderManager payOrderManager;

    private final AllocationOrderExtraManager allocationOrderExtraManager;


    /**
     * 获取可以分账的通道
     */
    public List<LabelValue> findChannels(){
        return Arrays.asList(
                new LabelValue(PayChannelEnum.ALI.getName(),PayChannelEnum.ALI.getCode()),
                new LabelValue(PayChannelEnum.WECHAT.getName(),PayChannelEnum.WECHAT.getCode())
        );
    }

    /**
     * 分页查询
     */
    public PageResult<AllocationOrderDto> page(PageParam pageParam, AllocationOrderQuery param){
        return MpUtil.convert2DtoPageResult(allocationOrderManager.page(pageParam, param));
    }


    /**
     * 查询详情
     */
    public AllocationOrderDto findById(Long id) {
        return allocationOrderManager.findById(id).map(AllocationOrder::toDto).orElseThrow(() -> new DataNotExistException("分账订单不存在"));
    }

    /**
     * 查询订单明细列表
     */
    public List<AllocationOrderDetailDto> findDetailsByOrderId(Long orderId){
        return ResultConvertUtil.dtoListConvert(allocationOrderDetailManager.findAllByOrderId(orderId));
    }

    /**
     * 查询订单明细详情
     */
    public AllocationOrderDetailDto findDetailById(Long id){
        return allocationOrderDetailManager.findById(id).map(AllocationOrderDetail::toDto).orElseThrow(() -> new DataNotExistException("分账订单明细不存在"));
    }

    /**
     * 查询扩展订单信息
     */
    public AllocationOrderExtraDto findExtraById(Long id) {
        return allocationOrderExtraManager.findById(id).map(AllocationOrderExtra::toDto)
                .orElseThrow(() -> new DataNotExistException("未找到"));
    }


    /**
     * 生成分账订单, 根据分账组创建
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderAndDetail createAndUpdate(AllocationParam param, PayOrder payOrder, List<AllocationGroupReceiverResult> receiversByGroups){
        // 订单明细
        List<AllocationOrderDetail> details = receiversByGroups.stream()
                .map(o -> {
                    // 计算分账金额, 小数部分直接舍弃, 防止分账金额超过上限
                    Integer rate = o.getRate();
                    // 等同于 payOrder.getAmount() * rate / 10000;
                    int amount = BigDecimal.valueOf(payOrder.getAmount())
                            .multiply(BigDecimal.valueOf(rate))
                            .divide(BigDecimal.valueOf(10000), 0, RoundingMode.DOWN).intValue();

                    AllocationOrderDetail detail = new AllocationOrderDetail()
                            .setReceiverNo(o.getReceiverNo())
                            .setReceiverId(o.getId())
                            .setAmount(amount)
                            .setResult(AllocDetailResultEnum.PENDING.getCode())
                            .setRate(rate)
                            .setReceiverType(o.getReceiverType())
                            .setReceiverName(o.getReceiverName())
                            .setReceiverAccount(o.getReceiverAccount());
                    // 如果金额为0, 设置为分账失败, 不参与分账
                    if (amount == 0){
                        detail.setResult(AllocDetailResultEnum.IGNORE.getCode())
                                .setErrorMsg("分账比例有误或金额太小, 无法进行分账")
                                .setFinishTime(LocalDateTime.now());
                    }
                    return detail;
                })
                .collect(Collectors.toList());
        return this.saveAllocOrder(param, payOrder, details);
    }

    /**
     * 生成分账订单, 通过传入的分账方创建
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderAndDetail createAndUpdate(AllocationParam param, PayOrder payOrder) {
        List<String> receiverNos = param.getReceivers()
                .stream()
                .map(AllocReceiverParam::getReceiverNo)
                .distinct()
                .collect(Collectors.toList());
        if (receiverNos.size() != param.getReceivers().size()){
            throw new PayFailureException("分账接收方编号重复");
        }
        Map<String, Integer> receiverNoMap = param.getReceivers()
                .stream()
                .collect(Collectors.toMap(AllocReceiverParam::getReceiverNo, AllocReceiverParam::getAmount));

        // 查询分账接收方信息
        List<AllocationReceiver> receivers = receiverManager.findAllByReceiverNos(receiverNos);
        if (receivers.size() != receiverNos.size()){
            throw new PayFailureException("分账接收方列表存在无效的分账接收方");
        }
        long allocId = IdUtil.getSnowflakeNextId();

        // 订单明细
        List<AllocationOrderDetail> details = receivers.stream()
                .map(o -> {
                    // 计算分账比例, 不是很精确
                    Integer amount = receiverNoMap.get(o.getReceiverNo());
                    Integer rate = BigDecimal.valueOf(amount)
                            .divide(BigDecimal.valueOf(payOrder.getAmount()), 4, RoundingMode.DOWN)
                            .multiply(BigDecimal.valueOf(10000)).intValue();
                    AllocationOrderDetail detail = new AllocationOrderDetail();
                    detail.setAllocationId(allocId)
                            .setReceiverId(o.getId())
                            .setReceiverNo(o.getReceiverNo())
                            .setAmount(amount)
                            .setResult(AllocDetailResultEnum.PENDING.getCode())
                            .setRate(rate)
                            .setReceiverType(o.getReceiverType())
                            .setReceiverName(o.getReceiverName())
                            .setReceiverAccount(o.getReceiverAccount());
                    return detail;
                })
                .collect(Collectors.toList());
        return this.saveAllocOrder(param, payOrder, details);
    }

    /**
     * 保存分账相关订单信息
     */
    private OrderAndDetail saveAllocOrder(AllocationParam param, PayOrder payOrder, List<AllocationOrderDetail> details ) {
        long allocId = IdUtil.getSnowflakeNextId();
        // 分账明细设置ID
        details.forEach(o -> o.setAllocationId(allocId));
        // 求分账的总额
        Integer sumAmount = details.stream()
                .map(AllocationOrderDetail::getAmount)
                .reduce(0, Integer::sum);
        // 分账订单
        AllocationOrder allocationOrder = new AllocationOrder()
                .setOrderId(payOrder.getId())
                .setOrderNo(payOrder.getOrderNo())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOutOrderNo(payOrder.getOutOrderNo())
                .setTitle(payOrder.getTitle())
                .setAllocationNo(OrderNoGenerateUtil.allocation())
                .setBizAllocationNo(param.getBizAllocationNo())
                .setChannel(payOrder.getChannel())
                .setDescription(param.getDescription())
                .setStatus(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                .setAmount(sumAmount);
        // 如果分账订单金额为0, 设置为忽略状态
        if (sumAmount == 0){
            allocationOrder.setStatus(AllocOrderStatusEnum.IGNORE.getCode())
                    .setFinishTime(LocalDateTime.now())
                    .setResult(AllocOrderStatusEnum.ALLOCATION_FAILED.getCode())
                    .setErrorMsg("分账比例有误或金额太小, 无法进行分账");
        }

        allocationOrder.setId(allocId);
        // 分账订单扩展
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        AllocationOrderExtra extend = new AllocationOrderExtra()
                .setNotifyUrl(noticeInfo.getNotifyUrl())
                .setAttach(param.getAttach());
        extend.setId(allocId);

        // 更新支付订单分账状态
        payOrder.setAllocationStatus(PayOrderAllocStatusEnum.ALLOCATION.getCode());
        payOrderManager.updateById(payOrder);
        allocationOrderDetailManager.saveAll(details);
        allocationOrderExtraManager.save(extend);
        allocationOrderManager.save(allocationOrder);
        return new OrderAndDetail().setOrder(allocationOrder).setDetails(details);
    }
}

