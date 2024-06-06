package cn.daxpay.single.service.core.channel.alipay.service;

import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.hutool.core.util.IdUtil;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static cn.daxpay.single.service.code.AliPayCode.QUERY_ACCOUNT_TYPE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayTransferService {

    private final AliPayConfigService payConfigService;

    /**
     * 余额查询接口
     */
    @SneakyThrows
    public void queryAccountAmount(AliPayConfig config) {
        AlipayFundAccountQueryModel model = new AlipayFundAccountQueryModel();
        model.setAccountType(QUERY_ACCOUNT_TYPE);
        model.setAlipayUserId(config.getAlipayUserId());
        AlipayFundAccountQueryResponse response = AliPayApi.accountQueryToResponse(model, null);
        System.out.println(response);
    }

    /**
     * 转账接口
     */
    @SneakyThrows
    public void transfer(TransferOrder order) {
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
//        model.setAmount(PayUtil.conversionAmount(order.getAmount()).toString());
        model.setAmount("1.00");
        model.setOutBizNo(IdUtil.getSnowflakeNextIdStr());
        model.setPayeeType("ALIPAY_USERID");
        model.setPayeeAccount("2088722032251651");
        model.setPayerShowName("易杯光年");
        model.setExtParam("{order_title: '订单标题'}");
        model.setRemark("易杯光年的备注");
        AlipayFundTransToaccountTransferResponse response = AliPayApi.transferToResponse(model);
        System.out.println(response);
    }
}
