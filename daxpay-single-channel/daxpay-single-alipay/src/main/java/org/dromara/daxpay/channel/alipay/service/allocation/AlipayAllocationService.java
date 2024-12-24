package org.dromara.daxpay.channel.alipay.service.allocation;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.function.CollectorsFunction;
import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.bo.allocation.AllocSyncResultBo;
import org.dromara.daxpay.service.entity.allocation.order.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocOrder;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.*;
import com.alipay.api.request.AlipayTradeOrderSettleQueryRequest;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.response.AlipayTradeOrderSettleQueryResponse;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import lombok.RequiredArgsConstructor;
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
 * @since 2024/12/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayAllocationService {
    private final AlipayConfigService aliPayConfigService;

    /**
     * 发起分账
     */
    public AllocStartResultBo start(AllocOrder allocOrder, List<AllocDetail> orderDetails, AliPayConfig aliPayConfig){
        // 分账主体参数
        AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
        model.setOutRequestNo(allocOrder.getAllocNo());
        model.setTradeNo(allocOrder.getOutOrderNo());
        model.setRoyaltyMode(AlipayCode.ALLOC_ASYNC);

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
        AlipayTradeOrderSettleResponse response;
        try {
            response = aliPayConfigService.execute(request,aliPayConfig);
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
    public void finish(AllocOrder allocOrder, List<AllocDetail> orderDetails, AliPayConfig aliPayConfig){
        // 分账主体参数
        AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
        model.setOutRequestNo(allocOrder.getAllocNo());
        model.setTradeNo(allocOrder.getOutOrderNo());
        model.setRoyaltyMode(AlipayCode.ALLOC_ASYNC);
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
            response = aliPayConfigService.execute(request,aliPayConfig);
        } catch (AlipayApiException e) {
            log.error("网关返回分账失败: {}", e.getMessage());
            throw new TradeFailException(e.getMessage());
        }
        this.verifyErrorMsg(response);
    }

    /**
     * 分账状态同步
     */
    public AllocSyncResultBo sync(AllocOrder allocOrder, List<AllocDetail> allocOrderDetails, AliPayConfig aliPayConfig){
        AlipayTradeOrderSettleQueryModel model = new AlipayTradeOrderSettleQueryModel();
        model.setOutRequestNo(allocOrder.getAllocNo());
        model.setTradeNo(allocOrder.getOutOrderNo());
        AlipayTradeOrderSettleQueryRequest request = new AlipayTradeOrderSettleQueryRequest();
        request.setBizModel(model);
        AlipayTradeOrderSettleQueryResponse response;
        try {
            response = aliPayConfigService.execute(request,aliPayConfig);
        } catch (AlipayApiException e) {
            throw new OperationFailException(e.getMessage());
        }
        // 验证
        this.verifyErrorMsg(response);
        Map<String, AllocDetail> detailMap = allocOrderDetails.stream()
                .collect(Collectors.toMap(AllocDetail::getReceiverAccount, Function.identity(), CollectorsFunction::retainLatest));
        List<RoyaltyDetail> royaltyDetailList = response.getRoyaltyDetailList();
        for (RoyaltyDetail receiver : royaltyDetailList) {
            var detail = detailMap.get(receiver.getTransIn());
            if (Objects.nonNull(detail)) {
                detail.setResult(this.getDetailResultEnum(receiver.getState()).getCode());
                detail.setErrorMsg(receiver.getErrorDesc());
                detail.setOutDetailId(receiver.getDetailId());
                // 如果是完成, 更新时间
                if (AllocDetailResultEnum.SUCCESS.getCode().equals(detail.getResult())){
                    LocalDateTime finishTime = LocalDateTimeUtil.of(receiver.getExecuteDt());
                    detail.setFinishTime(finishTime)
                            .setErrorMsg(null)
                            .setErrorCode(null);
                }
            }
        }
        return new AllocSyncResultBo().setSyncInfo(JSONUtil.toJsonStr(response));
    }

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
        if(Objects.equals(AlipayCode.ALLOC_PROCESSING, result)){
            return AllocDetailResultEnum.PENDING;
        }
        // 成功
        if(Objects.equals(AlipayCode.ALLOC_SUCCESS, result)){
            return AllocDetailResultEnum.SUCCESS;
        }
        // 失败
        return AllocDetailResultEnum.FAIL;
    }

}
