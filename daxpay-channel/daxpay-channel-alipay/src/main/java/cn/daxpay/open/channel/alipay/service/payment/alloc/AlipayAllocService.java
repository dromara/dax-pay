package cn.daxpay.open.channel.alipay.service.payment.alloc;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.req.AlipayAllocReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayAllocResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.enums.AllocDetailResultEnum;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// # 支付宝分账执行业务服务
///
/// 通过 [AlipayChannelClient] 调用子应用 dax-pay-channel-one 完成支付宝分账
/// (alipay.trade.order.settle / alipay.trade.order.settle.query)。
/// 请求构建、响应解析与状态映射在本类中完成。
///
/// 支付宝分账为纯查询式(无异步回调), 发起返回 settle_no 后靠同步轮询推进状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayAllocService {

    /// 支付宝时间格式(东八区本地时间字面量)
    private static final DateTimeFormatter ALIPAY_DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /// 异步分账模式
    private static final String ROYALTY_MODE_ASYNC = "async";

    private final AlipayChannelClient alipayChannelClient;

    /// 发起分账(alipay.trade.order.settle)
    ///
    /// 支付宝分账发起返回 settle_no, 但实际分账结果需查询确认(异步分账)。
    /// 本方法返回的明细结果均为 pending, 由编排层注册延迟同步。
    public AllocResultBo alloc(List<AllocDetailInfo> details, String allocNo, String outOrderNo,
                               AlipaySdkCredential credential) {
        AlipayAllocReq req = new AlipayAllocReq()
                .setOutRequestNo(allocNo)
                .setTradeNo(outOrderNo)
                .setRoyaltyMode(ROYALTY_MODE_ASYNC)
                .setCredential(credential);
        // 构建分账子参数
        List<AlipayAllocReq.RoyaltyParam> royaltyParams = new ArrayList<>();
        for (AllocDetailInfo d : details) {
            royaltyParams.add(new AlipayAllocReq.RoyaltyParam()
                    .setTransInType(d.transInType())
                    .setTransIn(d.transIn())
                    .setAmount(d.amount()));
        }
        req.setRoyaltyParameters(royaltyParams);

        DaxResult<AlipayAllocResp> result = alipayChannelClient.alloc(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.alipay.allocFailed", result.getMsg());
        }
        AlipayAllocResp resp = result.getData();
        // 支付宝业务失败(如接收方未绑定)
        if (StrUtil.isNotBlank(resp.getSubCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.alipay.allocFailed",
                    StrUtil.blankToDefault(resp.getSubMsg(), resp.getSubCode()));
        }
        // 发起成功: 返回 settle_no, 明细均为 pending(异步分账)
        AllocResultBo bo = new AllocResultBo().setOutAllocNo(resp.getSettleNo());
        List<AllocResultBo.DetailResult> detailResults = new ArrayList<>();
        for (AllocDetailInfo d : details) {
            detailResults.add(new AllocResultBo.DetailResult()
                    .setReceiverAccount(d.transIn())
                    .setResult(AllocDetailResultEnum.PENDING.getCode()));
        }
        bo.setDetails(detailResults);
        return bo;
    }

    /// 同步查询分账状态(alipay.trade.order.settle.query)
    public AllocResultBo sync(String allocNo, String outOrderNo,
                              List<String> receiverAccounts, AlipaySdkCredential credential) {
        AlipayAllocReq req = new AlipayAllocReq()
                .setOutRequestNo(allocNo)
                .setTradeNo(outOrderNo)
                .setCredential(credential);

        DaxResult<AlipayAllocResp> result = alipayChannelClient.allocSync(req);
        if (result.getCode() != 0) {
            return new AllocResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }
        AlipayAllocResp resp = result.getData();
        // 支付宝业务失败(如分账单不存在): 透传错误, 保持 processing 由定时任务兜底
        if (StrUtil.isNotBlank(resp.getSubCode())) {
            log.warn("支付宝分账查询业务失败: allocNo={}, subCode={}, subMsg={}",
                    allocNo, resp.getSubCode(), resp.getSubMsg());
            return new AllocResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorCode(resp.getSubCode())
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSubMsg(), "支付宝分账查询失败"));
        }
        // 映射逐明细结果
        AllocResultBo bo = new AllocResultBo().setOutAllocNo(resp.getSettleNo());
        List<AllocResultBo.DetailResult> detailResults = new ArrayList<>();
        if (resp.getRoyaltyDetailList() != null) {
            for (AlipayAllocResp.RoyaltyDetailResult r : resp.getRoyaltyDetailList()) {
                detailResults.add(new AllocResultBo.DetailResult()
                        .setReceiverAccount(r.getTransIn())
                        .setOutDetailId(r.getDetailId())
                        .setResult(mapDetailResult(r.getState()))
                        .setErrorMsg(r.getErrorDesc())
                        .setFinishTime(parseAlipayDate(r.getExecuteDt())));
            }
        }
        bo.setDetails(detailResults);
        return bo;
    }

    /// 映射支付宝明细状态到平台明细结果
    /// PROCESSING → pending / SUCCESS → success / 其他 → fail
    private String mapDetailResult(String state) {
        if (Objects.equals(state, "SUCCESS")) {
            return AllocDetailResultEnum.SUCCESS.getCode();
        } else if (Objects.equals(state, "PROCESSING")) {
            return AllocDetailResultEnum.PENDING.getCode();
        }
        return AllocDetailResultEnum.FAIL.getCode();
    }

    /// 解析支付宝时间字面量(东八区 yyyy-MM-dd HH:mm:ss)为 OffsetDateTime
    private OffsetDateTime parseAlipayDate(String dateStr) {
        if (StrUtil.isBlank(dateStr)) {
            return null;
        }
        return LocalDateTime.parse(dateStr, ALIPAY_DATE_FMT).atOffset(ZoneOffset.ofHours(8));
    }

    /// 分账明细信息(策略层传递, 避免直接依赖实体)
    public record AllocDetailInfo(String transInType, String transIn, Long amount) {
    }
}
