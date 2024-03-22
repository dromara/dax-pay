package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayTransferService {

    private final AliPayConfigService payConfigService;

    /**
     * 余额查询接口
     */
    @SneakyThrows
    public void queryAccountAmount() {
        AliPayConfig config = payConfigService.getAndCheckConfig();
        payConfigService.initConfig(config);
        AlipayFundAccountQueryModel model = new AlipayFundAccountQueryModel();
        model.setAccountType("ACCTRANS_ACCOUNT");
        model.setAlipayUserId("2088441532699265");
        AlipayFundAccountQueryResponse response = AliPayApi.accountQueryToResponse(model, null);
        System.out.println(response);
    }

    /**
     * 转账接口
     */
    @SneakyThrows
    public void transfer() {

    }
}
