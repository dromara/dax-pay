package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static cn.bootx.platform.daxpay.service.code.AliPayCode.QUERY_ACCOUNT_TYPE;

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
    public void transfer() {
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        AlipayFundTransToaccountTransferResponse response = AliPayApi.transferToResponse(model);
    }
}
