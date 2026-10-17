package cn.daxpay.open.channel.xingyi.service.payment.sync;

import cn.daxpay.open.channel.xingyi.client.XingyiChannelClient;
import cn.daxpay.open.channel.xingyi.client.credential.XingyiSdkCredential;
import cn.daxpay.open.channel.xingyi.client.req.XingyiSyncReq;
import cn.daxpay.open.channel.xingyi.client.resp.XingyiSyncResp;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/// # 星驿付支付同步业务服务
///
/// 通过 [XingyiChannelClient] 调用子应用查询星驿付订单状态(orderQuery),
/// 将星驿付订单状态(orderStatus: 1=支付成功, 其他=未支付/处理中, 参考插件仅区分两类)
/// 映射为平台 [PayFundStatusEnum]。
///
/// 查单按商户订单号 + 订单创建日期(orderTime=yyyyMMdd)路由, 不依赖通道订单号。
@Slf4j
@Service
@RequiredArgsConstructor
public class XingyiSyncService {

    /// 星驿付订单状态: 支付成功
    private static final String ORDER_STATUS_SUCCESS = "1";

    /// 查单路由日期格式(yyyyMMdd, 东八区)
    private static final DateTimeFormatter ORDER_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /// 东八区偏移
    private static final ZoneOffset CST = ZoneOffset.ofHours(8);

    private final XingyiChannelClient xingyiChannelClient;

    /// 执行星驿付支付同步
    public PaySyncResultBo sync(PayTrade trade, XingyiSdkCredential credential) {
        XingyiSyncReq req = new XingyiSyncReq();
        req.setCredential(credential);
        req.setOutTradeNo(trade.getTradeNo());
        // 查单路由日期: 订单创建日期(yyyyMMdd, 东八区), 创建时间缺失兜底当前日期
        req.setOrderTime(Objects.isNull(trade.getCreateTime())
                ? java.time.OffsetDateTime.now(CST).format(ORDER_DATE_FORMATTER)
                : trade.getCreateTime().withOffsetSameInstant(CST).format(ORDER_DATE_FORMATTER));

        DaxResult<XingyiSyncResp> result = xingyiChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("星驿付通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return this.toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(XingyiSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setFinishTime(resp.getFinishTime());
        bo.setBuyerId(resp.getBuyerId());
        // 星驿付订单状态映射(参考插件仅区分成功/非成功两类)
        if (ORDER_STATUS_SUCCESS.equals(resp.getTradeState())) {
            bo.setPayStatus(PayFundStatusEnum.SUCCESS);
            bo.setAmount(resp.getRealAmount());
        } else {
            bo.setPayStatus(PayFundStatusEnum.PROCESSING);
        }
        return bo;
    }
}
