package cn.daxpay.single.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.TransferStatusEnum;
import cn.daxpay.single.core.exception.TradeFaileException;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.common.context.TransferLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.core.util.PayUtil;
import cn.hutool.core.date.DatePattern;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundAccountQueryRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static cn.daxpay.single.service.code.AliPayCode.QUERY_ACCOUNT_TYPE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayTransferService {

    private final AliPayConfigService aliPayConfigService;

    /**
     * 余额查询接口
     */
    @SneakyThrows
    public void queryAccountAmount(AliPayConfig config, AliPayConfig aliPayConfig){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(aliPayConfig);
        AlipayFundAccountQueryModel model = new AlipayFundAccountQueryModel();
        model.setAccountType(QUERY_ACCOUNT_TYPE);
        model.setAlipayUserId(config.getAlipayUserId());
        AlipayFundAccountQueryRequest request = new AlipayFundAccountQueryRequest();
        request.setBizModel(model);
        AlipayFundAccountQueryResponse response = alipayClient.execute(request);
        System.out.println(response);
    }

    /**
     * 转账接口
     */
    @SneakyThrows
    public void transfer(TransferOrder order, AliPayConfig aliPayConfig){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(aliPayConfig);

        // 构造请求参数以调用接口
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();

        // 设置转账业务的标题
        model.setOrderTitle(order.getTitle());
        // 设置描述特定的业务场景
        model.setBizScene("DIRECT_TRANSFER");
        // 设置业务产品码
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        // 设置转账业务请求的扩展参数
        model.setBusinessParams("{payer_show_name_use_alias: true}");
        // 设置业务备注
        model.setRemark(order.getReason());
        // 设置商家侧唯一订单号
        model.setOutBizNo(order.getTransferNo());
        // 设置订单总金额
        model.setTransAmount(PayUtil.conversionAmount(order.getAmount()).toString());

        // 设置收款方信息
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity(order.getPayeeAccount());
        payeeInfo.setName(order.getPayeeName());
        payeeInfo.setIdentityType(order.getPayeeType());
        model.setPayeeInfo(payeeInfo);
        model.setRemark(order.getReason());
        request.setBizModel(model);
        AlipayFundTransUniTransferResponse response = alipayClient.execute(request);
        if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
            log.error("网关返回退款失败: {}", response.getSubMsg());
            throw new TradeFaileException(response.getSubMsg());
        }
        TransferLocal transferInfo = PaymentContextLocal.get().getTransferInfo();
        // 通道转账号
        transferInfo.setOutTransferNo(response.getOrderId());
        // 有完成时间代表处理完成
        if (Objects.equals(response.getStatus(), AliPayCode.TRANSFER_SUCCESS)){
            // 时间
            String transDate = response.getTransDate();
            LocalDateTime time = LocalDateTimeUtil.parse(transDate, DatePattern.NORM_DATETIME_PATTERN);
            transferInfo.setFinishTime(time).setStatus(TransferStatusEnum.SUCCESS);
        }
    }
}
