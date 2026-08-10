package cn.daxpay.open.channel.wechat.service.payment.alloc;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatAllocReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatAllocResp;
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

/// # 微信分账执行业务服务
///
/// 通过 [WechatChannelClient] 调用子应用 dax-pay-channel-one 完成微信分账
/// (V3 profitsharing/orders / profitsharing/orders/{out_order_no})。
/// 请求构建、响应解析与状态映射在本类中完成。
///
/// 微信分账为纯查询式(无异步回调), 发起返回后靠同步轮询推进状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatAllocService {

    private final WechatChannelClient wechatChannelClient;

    /// 发起分账(V3 profitsharing/orders, unfreeze_unsplit=true 自动解冻剩余)
    ///
    /// 微信分账发起返回 transactionId, 实际分账结果需查询确认(异步)。
    /// 本方法返回的明细结果均为 pending, 由编排层注册延迟同步。
    public AllocResultBo alloc(List<AllocDetailInfo> details, String allocNo, String transactionId,
                               WechatSdkCredential credential) {
        WechatAllocReq req = new WechatAllocReq()
                .setOutOrderNo(allocNo)
                .setTransactionId(transactionId)
                .setCredential(credential);
        List<WechatAllocReq.Receiver> receivers = new ArrayList<>();
        for (AllocDetailInfo d : details) {
            receivers.add(new WechatAllocReq.Receiver()
                    .setType(d.type())
                    .setAccount(d.account())
                    .setName(d.name())
                    .setAmount(d.amount())
                    .setDescription("订单分账"));
        }
        req.setReceivers(receivers);

        DaxResult<WechatAllocResp> result = wechatChannelClient.alloc(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.wechat.allocFailed", result.getMsg());
        }
        WechatAllocResp resp = result.getData();
        // 业务失败
        if (StrUtil.isNotBlank(resp.getErrorCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.wechat.allocFailed",
                    StrUtil.blankToDefault(resp.getErrorMsg(), resp.getErrorCode()));
        }
        // 发起成功: 返回 transactionId, 明细均为 pending
        AllocResultBo bo = new AllocResultBo().setOutAllocNo(resp.getTransactionId());
        List<AllocResultBo.DetailResult> detailResults = new ArrayList<>();
        for (AllocDetailInfo d : details) {
            detailResults.add(new AllocResultBo.DetailResult()
                    .setReceiverAccount(d.account())
                    .setResult(AllocDetailResultEnum.PENDING.getCode()));
        }
        bo.setDetails(detailResults);
        return bo;
    }

    /// 同步查询分账状态(V3 profitsharing/orders/{out_order_no})
    public AllocResultBo sync(String allocNo, String transactionId, WechatSdkCredential credential) {
        WechatAllocReq req = new WechatAllocReq()
                .setOutOrderNo(allocNo)
                .setTransactionId(transactionId)
                .setCredential(credential);

        DaxResult<WechatAllocResp> result = wechatChannelClient.allocSync(req);
        if (result.getCode() != 0) {
            return new AllocResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }
        WechatAllocResp resp = result.getData();
        if (StrUtil.isNotBlank(resp.getErrorCode())) {
            log.warn("微信分账查询业务失败: allocNo={}, errorCode={}, errorMsg={}",
                    allocNo, resp.getErrorCode(), resp.getErrorMsg());
            return new AllocResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorCode(resp.getErrorCode())
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getErrorMsg(), "微信分账查询失败"));
        }
        // 映射逐明细结果
        AllocResultBo bo = new AllocResultBo().setOutAllocNo(resp.getTransactionId());
        List<AllocResultBo.DetailResult> detailResults = new ArrayList<>();
        if (resp.getReceivers() != null) {
            for (WechatAllocResp.ReceiverResult r : resp.getReceivers()) {
                detailResults.add(new AllocResultBo.DetailResult()
                        .setReceiverAccount(r.getAccount())
                        .setResult(mapDetailResult(r.getResult()))
                        .setErrorMsg(r.getFailReason())
                        .setFinishTime(r.getFinishTime()));
            }
        }
        bo.setDetails(detailResults);
        return bo;
    }

    /// 映射微信明细状态到平台明细结果
    /// PENDING → pending / SUCCESS → success / CLOSED → fail / 其他 → pending
    private String mapDetailResult(String state) {
        if (Objects.equals(state, "SUCCESS")) {
            return AllocDetailResultEnum.SUCCESS.getCode();
        } else if (Objects.equals(state, "CLOSED")) {
            return AllocDetailResultEnum.FAIL.getCode();
        } else if (Objects.equals(state, "PENDING")) {
            return AllocDetailResultEnum.PENDING.getCode();
        }
        // 未知状态保持 pending
        return AllocDetailResultEnum.PENDING.getCode();
    }

    /// 分账明细信息(策略层传递)
    public record AllocDetailInfo(String type, String account, String name, Long amount) {
    }
}
