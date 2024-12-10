package org.dromara.daxpay.channel.alipay.service.allocation;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.AlipayTradeOrderSettleModel;
import com.alipay.api.domain.OpenApiRoyaltyDetailInfoPojo;
import com.alipay.api.domain.SettleExtendParams;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.alipay.code.AliPayCode;
import org.dromara.daxpay.channel.alipay.service.config.AliPayConfigService;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocOrder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付宝分账服务
 * @author xxm
 * @since 2024/12/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayAllocationService {
    private final AliPayConfigService aliPayConfigService;

    /**
     * 发起分账
     */
    public AllocStartResultBo start(AllocOrder allocOrder, List<AllocDetail> orderDetails){
        // 分账主体参数
        AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
        model.setOutRequestNo(allocOrder.getAllocNo());
        model.setTradeNo(allocOrder.getOutOrderNo());
        model.setRoyaltyMode(AliPayCode.ALLOC_ASYNC);

        // 分账子参数 根据Id排序
        orderDetails.sort(Comparator.comparing(MpIdEntity::getId));
        List<OpenApiRoyaltyDetailInfoPojo> royaltyParameters = orderDetails.stream()
                .map(o -> {
                    OpenApiRoyaltyDetailInfoPojo infoPojo = new OpenApiRoyaltyDetailInfoPojo();
                    infoPojo.setAmount(PayUtil.toDecimal(o.getAmount()).toPlainString());
                    infoPojo.setTransIn(o.getReceiverAccount());
                    return infoPojo;
                })
                .collect(Collectors.toList());
        model.setRoyaltyParameters(royaltyParameters);
        AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
        request.setBizModel(model);
        AlipayTradeOrderSettleResponse response = null;
        try {
            response = aliPayConfigService.execute(request);
            this.verifyErrorMsg(response);
        } catch (AlipayApiException e) {
            log.error("网关返回分账失败: {}", e.getMessage());
            throw new TradeFailException(e.getMessage());
        }
        // 需要写入到分账订单中
        String settleNo = response.getSettleNo();
        return new AllocStartResultBo().setOutAllocNo(settleNo);
    }

    /**
     * 分账完结
     */
    public void finish(AllocOrder allocOrder, List<AllocDetail> orderDetails){
        // 分账主体参数
        AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
        model.setOutRequestNo(String.valueOf(allocOrder.getAllocNo()));
        model.setTradeNo(allocOrder.getOutOrderNo());
        model.setRoyaltyMode(AliPayCode.ALLOC_ASYNC);
        // 分账完结参数
        SettleExtendParams extendParams = new SettleExtendParams();
        extendParams.setRoyaltyFinish(Boolean.TRUE.toString());
        model.setExtendParams(extendParams);

        // 分账子参数 根据Id排序
        orderDetails.sort(Comparator.comparing(MpIdEntity::getId));
        List<OpenApiRoyaltyDetailInfoPojo> royaltyParameters = orderDetails.stream()
                .map(o -> {
                    OpenApiRoyaltyDetailInfoPojo infoPojo = new OpenApiRoyaltyDetailInfoPojo();
                    infoPojo.setAmount(PayUtil.toDecimal(o.getAmount()).toPlainString());
                    infoPojo.setTransIn(o.getReceiverAccount());
                    return infoPojo;
                })
                .collect(Collectors.toList());
        model.setRoyaltyParameters(royaltyParameters);
        AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
        request.setBizModel(model);
        AlipayTradeOrderSettleResponse response = null;
        try {
            response = aliPayConfigService.execute(request);
        } catch (AlipayApiException e) {
            log.error("网关返回分账失败: {}", e.getMessage());
            throw new TradeFailException(e.getMessage());
        }
        this.verifyErrorMsg(response);
    }

    /**
     * 分账状态同步
     */
//    @SneakyThrows
//    public AllocRemoteSyncResult sync(AllocOrder allocOrder, List<AllocDetail> allocOrderDetails, AliPayConfig config){
//        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(config);
//        AlipayTradeOrderSettleQueryModel model = new AlipayTradeOrderSettleQueryModel();
//        model.setTradeNo(allocOrder.getOutOrderNo());
//        model.setOutRequestNo(allocOrder.getAllocNo());
//        AlipayTradeOrderSettleQueryRequest request = new AlipayTradeOrderSettleQueryRequest();
//        request.setBizModel(model);
//        AlipayTradeOrderSettleQueryResponse response = alipayClient.execute(request);
//        // 验证
//        this.verifyErrorMsg(response);
//        Map<String, AllocOrderDetail> detailMap = allocOrderDetails.stream()
//                .collect(Collectors.toMap(AllocOrderDetail::getReceiverAccount, Function.identity(), CollectorsFunction::retainLatest));
//        List<RoyaltyDetail> royaltyDetailList = response.getRoyaltyDetailList();
//        for (RoyaltyDetail receiver : royaltyDetailList) {
//            AllocOrderDetail detail = detailMap.get(receiver.getTransIn());
//            if (Objects.nonNull(detail)) {
//                detail.setResult(this.getDetailResultEnum(receiver.getState()).getCode());
//                detail.setErrorCode(receiver.getErrorCode());
//                detail.setErrorMsg(receiver.getErrorDesc());
//                // 如果是完成, 更新时间
//                if (AllocDetailResultEnum.SUCCESS.getCode().equals(detail.getResult())){
//                    LocalDateTime finishTime = LocalDateTimeUtil.of(receiver.getExecuteDt());
//                    detail.setFinishTime(finishTime)
//                            .setErrorMsg(null)
//                            .setErrorCode(null);
//                }
//            }
//        }
//        return new AllocRemoteSyncResult().setSyncInfo(JSONUtil.toJsonStr(response));
//    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(AlipayResponse response) {
        if (!response.isSuccess()) {
            String errorMsg = response.getSubMsg();
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = response.getMsg();
            }
            log.error("分账处理失败 {}", errorMsg);
            throw new TradeFailException(errorMsg);
        }
    }

    /**
     * 转换支付宝分账类型到系统中统一的状态
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
