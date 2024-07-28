package cn.daxpay.multi.channel.alipay.service.sync;

import cn.daxpay.multi.channel.alipay.code.AliPayCode.TransferStatus;
import cn.daxpay.multi.channel.alipay.entity.config.AliPayConfig;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.service.bo.sync.TransferSyncResultBo;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.daxpay.multi.service.enums.TransferSyncResultEnum;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
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
    public TransferSyncResultBo syncTransferStatus(TransferOrder transferOrder, AliPayConfig aliPayConfig){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(aliPayConfig);
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
            var response = alipayClient.execute(request);
            // 设置网关订单号
            syncResult.setSyncInfo(JSONUtil.toJsonStr(response));
            // 设置网关订单号
            syncResult.setOutTransferNo(response.getPayFundOrderId());

            //  SUCCESS：转账成功； WAIT_PAY：等待支付； CLOSED：订单超时关闭； FAIL：失败（适用于"单笔转账到银行卡"）；
            //  DEALING：处理中（适用于"单笔转账到银行卡"）； REFUND：退票（适用于"单笔转账到银行卡"）；
            String status = response.getStatus();
            if (Objects.equals(status, TransferStatus.SUCCESS)){
                return syncResult.setSyncStatus(TransferSyncResultEnum.SUCCESS);
            }
            if (List.of(TransferStatus.DEALING,TransferStatus.WAIT_PAY).contains(status)){
                return syncResult.setSyncStatus(TransferSyncResultEnum.PROGRESS);
            }
            if (List.of(TransferStatus.CLOSED,TransferStatus.REFUND, TransferStatus.FAIL).contains(status)){
                return syncResult.setSyncStatus(TransferSyncResultEnum.FAIL);
            }
            return syncResult;
        } catch (AlipayApiException e) {
            log.error("支付宝转账记录同步失败:", e);
            return syncResult.setErrorMsg(e.getErrMsg());
        }
    }
}
