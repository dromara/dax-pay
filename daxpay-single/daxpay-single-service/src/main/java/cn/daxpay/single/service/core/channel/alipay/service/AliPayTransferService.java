package cn.daxpay.single.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.code.TransferStatusEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.common.context.TransferLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.util.OrderNoGenerateUtil;
import cn.daxpay.single.util.PayUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.ijpay.alipay.AliPayApi;
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
        model.setAmount(PayUtil.conversionAmount(order.getAmount()).toString());
        model.setOutBizNo(OrderNoGenerateUtil.transfer());
        model.setPayeeType(order.getPayeeType());
        model.setPayeeAccount(order.getPayeeAccount());
        model.setPayerShowName(order.getPayerShowName());
        // 标题
        model.setExtParam(StrUtil.format("{order_title: '{}'}", order.getTitle()));
        model.setRemark(order.getReason());
        AlipayFundTransToaccountTransferResponse response = AliPayApi.transferToResponse(model);
        if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
            log.error("网关返回退款失败: {}", response.getSubMsg());
            throw new PayFailureException(response.getSubMsg());
        }
        TransferLocal transferInfo = PaymentContextLocal.get().getTransferInfo();
        // 通道转账号
        transferInfo.setOutTransferNo(response.getOrderId());
        // 有完成时间代表处理完成
        String payDate = response.getPayDate();
        if (StrUtil.isNotBlank(payDate)){
            LocalDateTime time = LocalDateTimeUtil.parse(payDate, DatePattern.NORM_DATETIME_PATTERN);
            transferInfo.setFinishTime(time)
                    .setStatus(TransferStatusEnum.SUCCESS);
        }
    }
}
