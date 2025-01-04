package org.dromara.daxpay.channel.wechat.service.allocation;

import cn.bootx.platform.common.mybatisplus.function.CollectorsFunction;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.enums.WechatAllocReceiverEnum;
import org.dromara.daxpay.channel.wechat.enums.WechatAllocStatusEnum;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.channel.wechat.util.WechatPayUtil;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.exception.ConfigErrorException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.bo.allocation.AllocSyncResultBo;
import org.dromara.daxpay.service.entity.allocation.order.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocOrder;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.profitsharing.request.ProfitSharingUnfreezeV3Request;
import com.github.binarywang.wxpay.bean.profitsharing.request.ProfitSharingV3Request;
import com.github.binarywang.wxpay.bean.profitsharing.result.ProfitSharingV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.ProfitSharingService;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.dromara.daxpay.core.enums.AllocReceiverTypeEnum.MERCHANT_NO;
import static org.dromara.daxpay.core.enums.AllocReceiverTypeEnum.OPEN_ID;
import static org.dromara.daxpay.core.enums.AllocationStatusEnum.*;


/**
 * 微信服务商分账V3版本接口
 * @author xxm
 * @since 2024/12/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPaySubAllocationV3Service {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 发起分账 使用分账号作为请求号
     */
    public AllocStartResultBo start(AllocOrder allocOrder, List<AllocDetail> orderDetails, WechatPayConfig config){
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();

        ProfitSharingV3Request request = new ProfitSharingV3Request();
        request.setAppid(config.getWxAppId());
        request.setOutOrderNo(allocOrder.getAllocNo());
        request.setTransactionId(allocOrder.getOutOrderNo());
        request.setUnfreezeUnsplit(false);

        // 分账接收方信息
        var receivers = orderDetails.stream()
                .map(o -> {
                    AllocReceiverTypeEnum receiverTypeEnum = AllocReceiverTypeEnum.findByCode(o.getReceiverType());
                    String receiverType = this.getReceiverType(receiverTypeEnum);
                    var receiver = new ProfitSharingV3Request.Receiver();
                    receiver.setType(receiverType);
                    receiver.setAmount(PayUtil.convertCentAmount(o.getAmount()));
                    receiver.setAccount(o.getReceiverAccount());
                    receiver.setName(o.getReceiverName());
                    receiver.setDescription("订单分账");
                    receiver.setRelationType("PARTNER");
                    return receiver;
                })
                .toList();

        request.setReceivers(receivers);
        try {
            ProfitSharingV3Result result = sharingService.profitSharingV3(request);
            return new AllocStartResultBo().setOutAllocNo(result.getTransactionId());
        } catch (WxPayException e) {
            throw new OperationFailException("微信分账V3失败: "+e.getMessage());
        }
    }

    /**
     * 分账完结 使用ID作为请求号
     */
    public void finish(AllocOrder allocOrder, WechatPayConfig config){
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();
        var request = new ProfitSharingUnfreezeV3Request();
        request.setOutOrderNo(String.valueOf(allocOrder.getId()));
        request.setTransactionId(allocOrder.getOutOrderNo());
        request.setDescription("分账完结");
        try {
            sharingService.profitSharingUnfreeze(request);
        } catch (WxPayException e) {
            throw new OperationFailException("微信分账完结V3失败: "+e.getMessage());
        }
    }

    /**
     * 同步
     */
    public AllocSyncResultBo sync(AllocOrder allocOrder, List<AllocDetail> details, WechatPayConfig config){
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();
        // 根据订单状态判断 使用ID还是分账号
        String outOrderNo;
        if (List.of(PROCESSING.getCode(), ALLOC_FAILED.getCode(), ALLOC_END.getCode()).contains(allocOrder.getStatus())){
            outOrderNo = allocOrder.getAllocNo();
        } else {
            outOrderNo = String.valueOf(allocOrder.getId());
        }

        ProfitSharingV3Result result;
        try {
            result = sharingService.profitSharingQueryV3(outOrderNo,allocOrder.getOutOrderNo(),config.getSubMchId());
        } catch (WxPayException e) {
            throw new OperationFailException("微信分账订单查询V3失败: "+e.getMessage());
        }
        var detailMap = details.stream()
                .collect(Collectors.toMap(AllocDetail::getReceiverAccount, Function.identity(), CollectorsFunction::retainLatest));
        var royaltyDetailList = Optional.ofNullable(result.getReceivers()).orElse(new ArrayList<>(0));
        for (var receiver : royaltyDetailList) {
            var detail = detailMap.get(receiver.getAccount());
            if (Objects.nonNull(detail)) {
                detail.setResult(this.getDetailResultEnum(receiver.getResult()).getCode());
                detail.setErrorMsg(receiver.getFailReason());
                detail.setOutDetailId(receiver.getDetailId());
                // 如果是完成, 更新时间
                if (AllocDetailResultEnum.SUCCESS.getCode().equals(detail.getResult())){
                    LocalDateTime finishTime = WechatPayUtil.parseV3(receiver.getFinishTime());
                    detail.setFinishTime(finishTime)
                            .setErrorMsg(null)
                            .setErrorCode(null);
                }
            }
        }
        return new AllocSyncResultBo().setSyncInfo(JSONUtil.toJsonStr(result));
    }

    /**
     * 获取分账接收方类型编码
     */
    private String getReceiverType(AllocReceiverTypeEnum receiverTypeEnum){
        if (receiverTypeEnum == OPEN_ID){
            return WechatAllocReceiverEnum.PERSONAL_OPENID.getCode();
        }
        if (receiverTypeEnum == MERCHANT_NO){
            return WechatAllocReceiverEnum.MERCHANT_ID.getCode();
        }
        throw new ConfigErrorException("分账接收方类型错误");
    }

    /**
     * 转换支付宝分账类型到系统中统一的状态
     */
    private AllocDetailResultEnum getDetailResultEnum (String result){
        return WechatAllocStatusEnum.findByCode(result).getResult();
    }


}
