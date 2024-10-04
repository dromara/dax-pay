package org.dromara.daxpay.channel.alipay.service.sync;

import cn.bootx.platform.core.util.JsonUtil;
import org.dromara.daxpay.channel.alipay.code.AliPayCode.TransferStatus;
import org.dromara.daxpay.channel.alipay.service.config.AliPayConfigService;
import org.dromara.daxpay.core.enums.TransferStatusEnum;
import org.dromara.daxpay.service.bo.sync.TransferSyncResultBo;
import org.dromara.daxpay.service.entity.order.transfer.TransferOrder;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 支付宝转账同步
 * @author xxm
 * @since 2024/7/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayTransferSyncService {

    private final AliPayConfigService aliPayConfigService;

    /**
     * 转账同步
     */
    public TransferSyncResultBo syncTransferStatus(TransferOrder transferOrder){
        TransferSyncResultBo syncResult = new TransferSyncResultBo();
        // 构造请求参数以调用接口
        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
        AlipayFundTransCommonQueryModel model = new AlipayFundTransCommonQueryModel();
        // 设置销售产品码
        model.setProductCode("STD_RED_PACKET");
        // 设置描述特定的业务场景
        model.setBizScene("PERSONAL_PAY");
        // 设置商户转账唯一订单号
        model.setOutBizNo(transferOrder.getTransferNo());
        request.setBizModel(model);
        try {
            var response = aliPayConfigService.execute(request);
            // 设置网关订单号
            syncResult.setSyncData(JsonUtil.toJsonStr(response));
            // 设置网关订单号
            syncResult.setOutTransferNo(response.getPayFundOrderId());

            String status = response.getStatus();
            //  SUCCESS：转账成功；
            if (Objects.equals(status, TransferStatus.SUCCESS)){
                return syncResult.setTransferStatus(TransferStatusEnum.SUCCESS);
            }
            //  WAIT_PAY：等待支付；DEALING：处理中（适用于"单笔转账到银行卡"）
            if (List.of(TransferStatus.DEALING,TransferStatus.WAIT_PAY).contains(status)){
                return syncResult.setTransferStatus(TransferStatusEnum.PROGRESS);
            }
            //  FAIL：失败（适用于"单笔转账到银行卡"）；
            if (Objects.equals(TransferStatus.FAIL, status)){
                return syncResult.setTransferStatus(TransferStatusEnum.FAIL).setTradeErrorMsg(response.getFailReason());
            }
            //  REFUND：退票（适用于"单笔转账到银行卡"）； CLOSED：订单超时关闭；
            if (List.of(TransferStatus.CLOSED, TransferStatus.REFUND).contains(status)){
                return syncResult.setTransferStatus(TransferStatusEnum.CLOSE).setTradeErrorMsg(response.getFailReason());
            }
            return syncResult;
        } catch (AlipayApiException e) {
            log.error("支付宝转账记录同步失败:", e);
            return syncResult.setSyncErrorMsg(e.getErrMsg()).setSyncSuccess(false);
        }
    }
}
