package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.daxpay.code.AllocDetailResultEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.AliPayCode;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrder;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.*;
import com.alipay.api.request.AlipayTradeOrderSettleQueryRequest;
import com.alipay.api.response.AlipayTradeOrderSettleQueryResponse;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 支付宝分账服务
 * @author xxm
 * @since 2024/4/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayAllocationService {

    /**
     * 发起分账
     */
    @SneakyThrows
    public void allocation(AllocationOrder allocationOrder, List<AllocationOrderDetail> orderDetails){
        // 分账主体参数
        AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
        model.setOutRequestNo(allocationOrder.getOrderNo());
        model.setTradeNo(allocationOrder.getOutOrderNo());
        model.setRoyaltyMode(AliPayCode.ALLOC_ASYNC);

        // 分账子参数 根据Id排序
        orderDetails.sort(Comparator.comparing(MpIdEntity::getId));
        List<OpenApiRoyaltyDetailInfoPojo> royaltyParameters = orderDetails.stream()
                .map(o -> {
                    OpenApiRoyaltyDetailInfoPojo infoPojo = new OpenApiRoyaltyDetailInfoPojo();
                    infoPojo.setAmount(String.valueOf(o.getAmount() / 100.0));
                    infoPojo.setTransIn(o.getReceiverAccount());
                    return infoPojo;
                })
                .collect(Collectors.toList());
        model.setRoyaltyParameters(royaltyParameters);

        AlipayTradeOrderSettleResponse response = AliPayApi.tradeOrderSettleToResponse(model);
        // 需要写入到分账订单中
        String settleNo = response.getSettleNo();
        PaymentContextLocal.get().getAllocationInfo().setOutAllocationNo(settleNo);
        this.verifyErrorMsg(response);
    }

    /**
     * 分账完结
     */
    @SneakyThrows
    public void finish(AllocationOrder allocationOrder, List<AllocationOrderDetail> orderDetails ){
        // 分账主体参数
        AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
        model.setOutRequestNo(String.valueOf(allocationOrder.getOrderNo()));
        model.setTradeNo(allocationOrder.getOutOrderNo());
        model.setRoyaltyMode(AliPayCode.ALLOC_ASYNC);
        // 分账完结参数
        SettleExtendParams extendParams = new SettleExtendParams();
        extendParams.setRoyaltyFinish("true");
        model.setExtendParams(extendParams);

        // 分账子参数 根据Id排序
        orderDetails.sort(Comparator.comparing(MpIdEntity::getId));
        List<OpenApiRoyaltyDetailInfoPojo> royaltyParameters = orderDetails.stream()
                .map(o -> {
                    OpenApiRoyaltyDetailInfoPojo infoPojo = new OpenApiRoyaltyDetailInfoPojo();
                    infoPojo.setAmount(String.valueOf(o.getAmount() / 100.0));
                    infoPojo.setTransIn(o.getReceiverAccount());
                    return infoPojo;
                })
                .collect(Collectors.toList());
        model.setRoyaltyParameters(royaltyParameters);
        AlipayTradeOrderSettleResponse response = AliPayApi.tradeOrderSettleToResponse(model);
        this.verifyErrorMsg(response);
    }

    /**
     * 分账状态同步
     */
    @SneakyThrows
    public void sync(AllocationOrder allocationOrder, List<AllocationOrderDetail> allocationOrderDetails){
        AlipayTradeOrderSettleQueryModel model = new AlipayTradeOrderSettleQueryModel();
        model.setTradeNo(allocationOrder.getOutOrderNo());
        model.setOutRequestNo(allocationOrder.getOrderNo());
        AlipayTradeOrderSettleQueryRequest request = new AlipayTradeOrderSettleQueryRequest();
        request.setBizModel(model);
        AlipayTradeOrderSettleQueryResponse response = AliPayApi.execute(request);
        // 验证
        this.verifyErrorMsg(response);
        Map<String, AllocationOrderDetail> detailMap = allocationOrderDetails.stream()
                .collect(Collectors.toMap(AllocationOrderDetail::getReceiverAccount, Function.identity(), CollectorsFunction::retainLatest));
        List<RoyaltyDetail> royaltyDetailList = response.getRoyaltyDetailList();
        // 转换成通用的明细详情
        for (RoyaltyDetail receiver : royaltyDetailList) {
            AllocationOrderDetail detail = detailMap.get(receiver.getTransIn());
            if (Objects.nonNull(detail)) {
                detail.setResult(this.getDetailResultEnum(receiver.getState()).getCode());
                detail.setErrorCode(receiver.getErrorCode());
                detail.setErrorMsg(receiver.getErrorDesc());
                // 如果是完成, 更新时间
                if (AllocDetailResultEnum.SUCCESS.getCode().equals(detail.getResult())){
                    LocalDateTime finishTime = LocalDateTimeUtil.of(receiver.getExecuteDt());
                    detail.setFinishTime(finishTime);
                }
            }
        }
    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(AlipayResponse alipayResponse) {
        if (!Objects.equals(alipayResponse.getCode(), AliPayCode.SUCCESS)) {
            String errorMsg = alipayResponse.getSubMsg();
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = alipayResponse.getMsg();
            }
            log.error("分账处理失败 {}", errorMsg);
            throw new PayFailureException(errorMsg);
        }
    }

    /**
     * 转换支付宝分账类型到系统中统一的
     */
    private AllocDetailResultEnum getDetailResultEnum (String result){
        // 进行中
        if(Objects.equals(AliPayCode.ALLOC_PROCESSING, result)){
            return AllocDetailResultEnum.PENDING;
        }
        // 成功
        if(Objects.equals(AliPayCode.ALLOC_SUCCESS, result)){
            return AllocDetailResultEnum.SUCCESS;
        }
        // 失败
        return AllocDetailResultEnum.FAIL;
    }
}
