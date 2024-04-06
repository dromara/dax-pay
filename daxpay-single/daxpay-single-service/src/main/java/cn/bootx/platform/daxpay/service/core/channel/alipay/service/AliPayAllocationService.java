package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrder;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.hutool.core.util.IdUtil;
import com.alipay.api.domain.AlipayTradeOrderSettleModel;
import com.alipay.api.domain.OpenApiRoyaltyDetailInfoPojo;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 支付宝分账服务
 * @author xxm
 * @since 2024/4/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayAllocationService {

    private final AliPayConfigService aliPayConfigService;

    /**
     * 发起分账
     */
    @SneakyThrows
    public void allocation(AllocationOrder allocationOrder, List<AllocationOrderDetail> orderDetails){


        aliPayConfigService.initConfig(aliPayConfigService.getConfig());

        AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();

//        model.setOutRequestNo(String.valueOf(allocationOrder.getId()));
//        model.setTradeNo(allocationOrder.getGatewayOrderNo());

        model.setOutRequestNo(IdUtil.getSnowflakeNextIdStr());
        model.setTradeNo("");
//        model.setRoyaltyMode("async");
        OpenApiRoyaltyDetailInfoPojo detailInfoPojo = new OpenApiRoyaltyDetailInfoPojo();
        detailInfoPojo.setTransIn("");
        detailInfoPojo.setAmount("0.01");


        List<OpenApiRoyaltyDetailInfoPojo> royaltyParameters = Collections.singletonList(detailInfoPojo);

        model.setRoyaltyParameters(royaltyParameters);

        AlipayTradeOrderSettleResponse response = AliPayApi.tradeOrderSettleToResponse(model);
        System.out.println(response);
    }

    /**
     * 分账完结
     */
    public void x2(){

    }
}
