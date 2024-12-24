package org.dromara.daxpay.channel.alipay.service.sync;

import cn.bootx.platform.core.util.JsonUtil;
import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.code.AlipayCode.PayStatus;
import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.service.bo.sync.PaySyncResultBo;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * 支付宝支付同步
 *
 * @author xxm
 * @since 2021/5/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipaySyncService {
    private final AlipayConfigService aliPayConfigService;

    /**
     * 与支付宝网关同步状态, 退款状态有
     * 1 远程支付成功
     * 2 交易创建，等待买家付款
     * 3 超时关闭
     * 4 查询不到
     * 5 查询失败
     */
    public PaySyncResultBo syncPayStatus(PayOrder payOrder,AliPayConfig aliPayConfig){
        PaySyncResultBo syncResult = new PaySyncResultBo();
        // 查询
        try {
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo(payOrder.getOrderNo());
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            // 特约商户调用
            if (aliPayConfig.isIsv()){
                request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
            }
            request.setBizModel(model);
            AlipayTradeQueryResponse response = aliPayConfigService.execute(request,aliPayConfig);
            String tradeStatus = response.getTradeStatus();
            syncResult.setSyncData(JsonUtil.toJsonStr(response));
            // 设置网关订单号
            syncResult.setOutOrderNo(response.getTradeNo());
            // 支付完成  部分退款无法进行区分, 需要借助对账进行处理
            if (Objects.equals(tradeStatus, PayStatus.TRADE_SUCCESS) || Objects.equals(tradeStatus, PayStatus.TRADE_FINISHED)) {
                // 支付完成时间
                LocalDateTime payTime = LocalDateTimeUtil.of(response.getSendPayDate());
                return syncResult.setPayStatus(PayStatusEnum.SUCCESS).setFinishTime(payTime);
            }
            // 待支付
            if (Objects.equals(tradeStatus, PayStatus.WAIT_BUYER_PAY)) {
                return syncResult.setPayStatus(PayStatusEnum.PROGRESS);
            }
            // 已关闭或支付完成后全额退款
            if (Objects.equals(tradeStatus, PayStatus.TRADE_CLOSED)) {
                // 判断是否有支付时间, 有支付时间说明是退款
                if (Objects.nonNull(response.getSendPayDate())){
                    return syncResult.setPayStatus(PayStatusEnum.SUCCESS);
                } else {
                    return syncResult.setPayStatus(PayStatusEnum.CLOSE);
                }
            }
            // 支付宝支付后, 客户未进行操作将不会创建出订单, 所以交易不存在约等于未查询订单
            if (Objects.equals(response.getSubCode(), AlipayCode.ResponseCode.ACQ_TRADE_NOT_EXIST)) {
                return syncResult.setPayStatus(PayStatusEnum.PROGRESS);
            }
            // 查询失败
            if (!response.isSuccess()) {
                return syncResult.setSyncSuccess(false)
                        .setSyncErrorCode(response.getSubCode())
                        .setSyncErrorMsg(response.getSubMsg());
            }
        }
        catch (AlipayApiException e) {
            log.error("支付订单同步失败:", e);
            syncResult.setSyncErrorMsg(e.getErrMsg())
                    .setSyncSuccess(false)
                    .setPayStatus(PayStatusEnum.FAIL);
        }
        return syncResult;
    }

}
