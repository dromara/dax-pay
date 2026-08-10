package cn.daxpay.open.channel.douyin.service.payment.alloc;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.req.DouyinAllocReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinAllocResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.enums.AllocDetailResultEnum;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// # 抖音分账执行业务服务
///
/// 通过 [DouyinChannelClient] 调用子应用 dax-pay-channel-one 完成抖音分账
/// (splitFund / querySplitFund)。
/// 抖音分账有异步回调, 发起时需传 notifyUrl。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinAllocService {

    private final DouyinChannelClient douyinChannelClient;

    /// 发起分账(splitFund, unfreeze_unsplit=true 自动解冻剩余)
    ///
    /// 抖音分账发起返回 orderId, 实际分账结果通过异步回调或查询确认。
    public AllocResultBo alloc(List<AllocDetailInfo> details, String allocNo, String tradeNo,
                               String notifyUrl, DouyinSdkCredential credential) {
        DouyinAllocReq req = new DouyinAllocReq()
                .setOutTradeNo(allocNo)
                .setTradeNo(tradeNo)
                .setNotifyUrl(notifyUrl)
                .setCredential(credential);
        List<DouyinAllocReq.ReceiverInfo> receiverInfos = new ArrayList<>();
        for (AllocDetailInfo d : details) {
            receiverInfos.add(new DouyinAllocReq.ReceiverInfo()
                    .setType(d.type())
                    .setAccount(d.account())
                    .setName(d.name())
                    .setAmount(d.amount()));
        }
        req.setReceiverInfoDtos(receiverInfos);

        DaxResult<DouyinAllocResp> result = douyinChannelClient.alloc(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.douyin.allocFailed", result.getMsg());
        }
        DouyinAllocResp resp = result.getData();
        if (StrUtil.isNotBlank(resp.getErrorCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.douyin.allocFailed",
                    StrUtil.blankToDefault(resp.getErrorMsg(), resp.getErrorCode()));
        }
        AllocResultBo bo = new AllocResultBo().setOutAllocNo(resp.getOrderId());
        List<AllocResultBo.DetailResult> detailResults = new ArrayList<>();
        for (AllocDetailInfo d : details) {
            detailResults.add(new AllocResultBo.DetailResult()
                    .setReceiverAccount(d.account())
                    .setResult(AllocDetailResultEnum.PENDING.getCode()));
        }
        bo.setDetails(detailResults);
        return bo;
    }

    /// 同步查询分账状态(querySplitFund)
    public AllocResultBo sync(String allocNo, String tradeNo, DouyinSdkCredential credential) {
        DouyinAllocReq req = new DouyinAllocReq()
                .setOutTradeNo(allocNo)
                .setTradeNo(tradeNo)
                .setCredential(credential);

        DaxResult<DouyinAllocResp> result = douyinChannelClient.allocSync(req);
        if (result.getCode() != 0) {
            return new AllocResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }
        DouyinAllocResp resp = result.getData();
        if (StrUtil.isNotBlank(resp.getErrorCode())) {
            return new AllocResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorCode(resp.getErrorCode())
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getErrorMsg(), "抖音分账查询失败"));
        }
        AllocResultBo bo = new AllocResultBo().setOutAllocNo(resp.getOrderId());
        List<AllocResultBo.DetailResult> detailResults = new ArrayList<>();
        if (resp.getReceiverSplitResultDtos() != null) {
            for (DouyinAllocResp.ReceiverSplitResult r : resp.getReceiverSplitResultDtos()) {
                detailResults.add(new AllocResultBo.DetailResult()
                        .setReceiverAccount(r.getAccount())
                        .setResult(mapDetailResult(r.getSplitStatus()))
                        .setErrorMsg(r.getFailReason())
                        .setFinishTime(r.getFinishTime()));
            }
        }
        bo.setDetails(detailResults);
        return bo;
    }

    /// 映射抖音明细状态(SUCCESS → success / CLOSED → fail / PROCESSING → pending)
    private String mapDetailResult(String state) {
        if (Objects.equals(state, "SUCCESS")) {
            return AllocDetailResultEnum.SUCCESS.getCode();
        } else if (Objects.equals(state, "CLOSED") || Objects.equals(state, "FAIL")) {
            return AllocDetailResultEnum.FAIL.getCode();
        }
        return AllocDetailResultEnum.PENDING.getCode();
    }

    /// 分账明细信息
    public record AllocDetailInfo(String type, String account, String name, Long amount) {
    }
}
