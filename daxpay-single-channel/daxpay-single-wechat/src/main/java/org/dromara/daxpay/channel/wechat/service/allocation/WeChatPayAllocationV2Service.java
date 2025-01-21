package org.dromara.daxpay.channel.wechat.service.allocation;

import cn.bootx.platform.common.mybatisplus.function.CollectorsFunction;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.enums.WechatAllocReceiverEnum;
import org.dromara.daxpay.channel.wechat.enums.WechatAllocStatusEnum;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.channel.wechat.util.WechatPayUtil;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.enums.AllocationStatusEnum;
import org.dromara.daxpay.core.exception.ConfigErrorException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.bo.allocation.AllocSyncResultBo;
import org.dromara.daxpay.service.entity.allocation.order.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocOrder;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.profitsharing.request.ProfitSharingQueryRequest;
import com.github.binarywang.wxpay.bean.profitsharing.request.ProfitSharingRequest;
import com.github.binarywang.wxpay.bean.profitsharing.request.ProfitSharingUnfreezeRequest;
import com.github.binarywang.wxpay.bean.profitsharing.request.ProfitSharingV3Request;
import com.github.binarywang.wxpay.bean.profitsharing.result.ProfitSharingQueryResult;
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


/**
 * 微信分账V2版本接口
 * @author xxm
 * @since 2024/12/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayAllocationV2Service {

    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 分账
     */
    public AllocStartResultBo start(AllocOrder allocOrder, List<AllocDetail> details, WechatPayConfig config) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();

        ProfitSharingRequest request = new ProfitSharingRequest();
        request.setAppid(config.getWxAppId());
        request.setOutOrderNo(allocOrder.getAllocNo());
        request.setTransactionId(allocOrder.getOutOrderNo());

        // 分账接收方信息
        var receivers = details.stream()
                .map(o -> {
                    AllocReceiverTypeEnum receiverTypeEnum = AllocReceiverTypeEnum.findByCode(o.getReceiverType());
                    String receiverType = this.getReceiverType(receiverTypeEnum);
                    var receiver = new ProfitSharingV3Request.Receiver();
                    receiver.setType(receiverType);
                    receiver.setAmount(PayUtil.convertCentAmount(o.getAmount()));
                    receiver.setAccount(o.getReceiverAccount());
                    receiver.setName(o.getReceiverName());
                    receiver.setDescription("订单分账");
                    return receiver;
                })
                .toList();

        request.setReceivers(JSONUtil.toJsonStr(receivers));
        try {
            var result = sharingService.multiProfitSharing(request);
            return new AllocStartResultBo().setOutAllocNo(result.getTransactionId());
        } catch (WxPayException e) {
            throw new OperationFailException("微信分账V2失败: "+e.getMessage());
        }
    }

    /**
     * 完结
     */
    public void finish(AllocOrder allocOrder, WechatPayConfig config) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();
        var request = new ProfitSharingUnfreezeRequest();
        request.setOutOrderNo(String.valueOf(allocOrder.getId()));
        request.setTransactionId(allocOrder.getOutOrderNo());
        request.setDescription("分账完结");
        try {
            sharingService.profitSharingFinish(request);
        } catch (WxPayException e) {
            throw new OperationFailException("微信分账完结V2失败: "+e.getMessage());
        }
    }

    /**
     * 同步
     */
    public AllocSyncResultBo sync(AllocOrder allocOrder, List<AllocDetail> details, WechatPayConfig config) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();
        ProfitSharingQueryRequest request = new ProfitSharingQueryRequest();
        // 根据订单状态判断 使用ID还是分账号
        request.setTransactionId(allocOrder.getOutOrderNo());
        if (Objects.equals(AllocationStatusEnum.PROCESSING.getCode(), allocOrder.getStatus())){
            request.setOutOrderNo(allocOrder.getAllocNo());
        } else {
            request.setOutOrderNo(String.valueOf(allocOrder.getId()));
        }

        ProfitSharingQueryResult result;
        try {
            result = sharingService.profitSharingQuery(request);
        } catch (WxPayException e) {
            throw new OperationFailException("微信分账订单查询V2失败: "+e.getMessage());
        }
        var detailMap = details.stream()
                .collect(Collectors.toMap(AllocDetail::getReceiverAccount, Function.identity(), CollectorsFunction::retainLatest));
        var royaltyDetailList = Optional.ofNullable( result.getReceivers()).orElse(new ArrayList<>(0));
        for (var receiver : royaltyDetailList) {
            var detail = detailMap.get(receiver.getAccount());
            if (Objects.nonNull(detail)) {
                detail.setResult(this.getDetailResultEnum(receiver.getResult()).getCode());
                detail.setErrorMsg(receiver.getFailReason());
                detail.setOutDetailId(receiver.getDetailId());
                // 如果是完成, 更新时间
                if (AllocDetailResultEnum.SUCCESS.getCode().equals(detail.getResult())){
                    LocalDateTime finishTime = WechatPayUtil.parseV2(receiver.getFinishTime());
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
