package cn.daxpay.single.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.single.core.code.AllocDetailResultEnum;
import cn.daxpay.single.core.exception.TradeFailException;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.core.payment.sync.result.AllocRemoteSyncResult;
import cn.daxpay.single.core.util.PayUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.*;
import com.alipay.api.request.AlipayTradeOrderSettleQueryRequest;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.response.AlipayTradeOrderSettleQueryResponse;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
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
public class AliPayAllocService {
    private final AliPayConfigService aliPayConfigService;
    /**
     * 发起分账
     */
    @SneakyThrows
    public void allocation(AllocOrder allocOrder, List<AllocOrderDetail> orderDetails, AliPayConfig config){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(config);
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
                    infoPojo.setAmount(PayUtil.conversionAmount(o.getAmount()).toString());
                    infoPojo.setTransIn(o.getReceiverAccount());
                    return infoPojo;
                })
                .collect(Collectors.toList());
        model.setRoyaltyParameters(royaltyParameters);
        AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
        request.setBizModel(model);
        AlipayTradeOrderSettleResponse response = alipayClient.execute(request);
        // 需要写入到分账订单中
        String settleNo = response.getSettleNo();
        PaymentContextLocal.get().getAllocationInfo().setOutAllocNo(settleNo);
        this.verifyErrorMsg(response);
    }

    /**
     * 分账完结
     */
    @SneakyThrows
    public void finish(AllocOrder allocOrder, List<AllocOrderDetail> orderDetails, AliPayConfig config){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(config);
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
                    infoPojo.setAmount(PayUtil.conversionAmount(o.getAmount()).toString());
                    infoPojo.setTransIn(o.getReceiverAccount());
                    return infoPojo;
                })
                .collect(Collectors.toList());
        model.setRoyaltyParameters(royaltyParameters);
        AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
        request.setBizModel(model);
        AlipayTradeOrderSettleResponse response = alipayClient.execute(request);
        this.verifyErrorMsg(response);
    }

    /**
     * 分账状态同步
     */
    @SneakyThrows
    public AllocRemoteSyncResult sync(AllocOrder allocOrder, List<AllocOrderDetail> allocOrderDetails, AliPayConfig config){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(config);
        AlipayTradeOrderSettleQueryModel model = new AlipayTradeOrderSettleQueryModel();
        model.setTradeNo(allocOrder.getOutOrderNo());
        model.setOutRequestNo(allocOrder.getAllocNo());
        AlipayTradeOrderSettleQueryRequest request = new AlipayTradeOrderSettleQueryRequest();
        request.setBizModel(model);
        AlipayTradeOrderSettleQueryResponse response = alipayClient.execute(request);
        // 验证
        this.verifyErrorMsg(response);
        Map<String, AllocOrderDetail> detailMap = allocOrderDetails.stream()
                .collect(Collectors.toMap(AllocOrderDetail::getReceiverAccount, Function.identity(), CollectorsFunction::retainLatest));
        List<RoyaltyDetail> royaltyDetailList = response.getRoyaltyDetailList();
        for (RoyaltyDetail receiver : royaltyDetailList) {
            AllocOrderDetail detail = detailMap.get(receiver.getTransIn());
            if (Objects.nonNull(detail)) {
                detail.setResult(this.getDetailResultEnum(receiver.getState()).getCode());
                detail.setErrorCode(receiver.getErrorCode());
                detail.setErrorMsg(receiver.getErrorDesc());
                // 如果是完成, 更新时间
                if (AllocDetailResultEnum.SUCCESS.getCode().equals(detail.getResult())){
                    LocalDateTime finishTime = LocalDateTimeUtil.of(receiver.getExecuteDt());
                    detail.setFinishTime(finishTime)
                            .setErrorMsg(null)
                            .setErrorCode(null);
                }
            }
        }
        return new AllocRemoteSyncResult().setSyncInfo(JSONUtil.toJsonStr(response));
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
            throw new TradeFailException(errorMsg);
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
