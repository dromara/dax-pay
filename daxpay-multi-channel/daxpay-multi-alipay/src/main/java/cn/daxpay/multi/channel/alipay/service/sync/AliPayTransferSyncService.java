package cn.daxpay.multi.channel.alipay.service.sync;

import cn.daxpay.multi.channel.alipay.entity.config.AliPayConfig;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.service.bo.sync.TransferSyncResultBo;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.daxpay.multi.service.enums.TransferSyncResultEnum;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    @SneakyThrows
    public TransferSyncResultBo syncTransferStatus(TransferOrder transferOrder, AliPayConfig aliPayConfig){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(aliPayConfig);
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
        AlipayFundTransCommonQueryResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        return new TransferSyncResultBo().setSyncStatus(TransferSyncResultEnum.FAIL);
    }
}
