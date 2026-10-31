package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.channel.mock.client.req.MockAllocFinishReq;
import cn.daxpay.open.channel.mock.client.req.MockAllocReq;
import cn.daxpay.open.channel.mock.client.req.MockAllocSyncReq;
import cn.daxpay.open.channel.mock.client.resp.MockAllocResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.entity.AllocDetail;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.enums.AllocDetailResultEnum;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/// # Mock 通道分账执行业务服务
///
/// 通过 [MockChannelClient] 调用独立 mock 子应用完成模拟分账
/// (alloc / alloc-finish / alloc-sync), 机制对齐支付宝
/// [cn.daxpay.open.channel.alipay.service.payment.alloc.AlipayAllocService]。
///
/// mock 分账为整单三态(纯查询式, 无异步回调):
/// - 发起: 场景规则命中即时终态直接返回对应明细结果, 未命中返回处理中交同步/GUI 推进;
/// - 整体业务失败(mock 侧业务错误码) → 抛业务异常, 编排层置分账单 fail(与支付宝发起失败同构);
/// - 同步: 整单状态映射为全部明细同结果; 完结单映射 [AllocResultBo#setFinishState]。
///
/// 与支付宝的差异: mock 不感知完结模式(finishMode 无随单解冻语义, 剩余资金为内存假想),
/// auto/manual 两种模式下单行为一致, 独立完结走 alloc-finish 端点。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockAllocService {

    /// mock 分账单状态: 处理中
    private static final String STATUS_PROCESSING = "PROCESSING";

    /// mock 分账单状态: 成功
    private static final String STATUS_SUCCESS = "SUCCESS";

    private final MockChannelClient mockChannelClient;
    private final MockCredentialSupport mockCredentialSupport;

    /// 发起分账
    ///
    /// 明细结果按 mock 整单状态映射: SUCCESS → 全部 success / PROCESSING → 全部 pending;
    /// mock 侧整体业务失败时抛异常由编排层置分账单 fail。
    /// 凭证按订单 product 分支(直连/服务商), 不照抄支付宝/微信分账固定走 Direct assembler 的非对称缺陷。
    public AllocResultBo alloc(AllocOrder allocOrder, List<AllocDetail> details) {
        MockAllocReq req = new MockAllocReq()
                .setAllocNo(allocOrder.getAllocNo())
                .setOutTradeNo(allocOrder.getOutOrderNo())
                .setCredential(mockCredentialSupport.build(allocOrder.getProduct(), allocOrder.getChannelMchNo()));
        // 构建分账接收方列表
        List<MockAllocReq.Receiver> receivers = new ArrayList<>();
        for (AllocDetail detail : details) {
            receivers.add(new MockAllocReq.Receiver()
                    .setAccount(detail.getReceiverAccount())
                    .setName(detail.getReceiverName())
                    .setAmount(detail.getAmount()));
        }
        req.setReceivers(receivers);

        DaxResult<MockAllocResp> result = mockChannelClient.alloc(req);
        if (result.getCode() != 0) {
            // 模拟通道: 分账失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.mock.allocFailed", result.getMsg());
        }
        MockAllocResp resp = result.getData();
        return buildResult(resp, details);
    }

    /// 发起完结(解冻剩余冻结资金, 无接收方明细)
    ///
    /// 完结单以 mock 整单状态映射 finishState(pending/success/fail)。
    public AllocResultBo finish(AllocOrder allocOrder) {
        MockAllocFinishReq req = new MockAllocFinishReq()
                .setAllocNo(allocOrder.getAllocNo())
                .setOutTradeNo(allocOrder.getOutOrderNo())
                .setCredential(mockCredentialSupport.build(allocOrder.getProduct(), allocOrder.getChannelMchNo()));
        DaxResult<MockAllocResp> result = mockChannelClient.allocFinish(req);
        if (result.getCode() != 0) {
            // 模拟通道: 分账完结失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.mock.allocFinishFailed", result.getMsg());
        }
        MockAllocResp resp = result.getData();
        return new AllocResultBo()
                .setOutAllocNo(resp.getOutAllocNo())
                .setFinishState(mapState(resp.getStatus()));
    }

    /// 同步查询分账状态
    ///
    /// 通道不可达等失败返回 syncSuccess=false 保持 processing 由定时任务兜底(与支付宝同构);
    /// 成功时整单状态映射为全部明细同结果, 完结单映射 finishState。
    public AllocResultBo sync(AllocOrder allocOrder, List<AllocDetail> details) {
        MockAllocSyncReq req = new MockAllocSyncReq()
                .setAllocNo(allocOrder.getAllocNo())
                .setCredential(mockCredentialSupport.build(allocOrder.getProduct(), allocOrder.getChannelMchNo()));
        DaxResult<MockAllocResp> result = mockChannelClient.allocSync(req);
        if (result.getCode() != 0) {
            log.warn("模拟通道分账同步失败: allocNo={}, msg={}", allocOrder.getAllocNo(), result.getMsg());
            return new AllocResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(result.getMsg(), "模拟通道分账同步失败"));
        }
        return buildResult(result.getData(), details);
    }

    /// 组装分账结果(整单状态 → 逐明细同结果; 完结单 → finishState)
    private AllocResultBo buildResult(MockAllocResp resp, List<AllocDetail> details) {
        AllocResultBo bo = new AllocResultBo()
                .setOutAllocNo(resp.getOutAllocNo());
        // 完结单(无明细): 以整单状态推进
        if (details == null || details.isEmpty()) {
            return bo.setFinishState(mapState(resp.getStatus()));
        }
        // 普通分账单: 整单状态映射为全部明细同结果
        String detailResult = mapState(resp.getStatus());
        List<AllocResultBo.DetailResult> detailResults = new ArrayList<>();
        for (AllocDetail detail : details) {
            detailResults.add(new AllocResultBo.DetailResult()
                    .setReceiverAccount(detail.getReceiverAccount())
                    .setResult(detailResult)
                    .setFinishTime(STATUS_SUCCESS.equals(resp.getStatus()) ? resp.getFinishTime() : null));
        }
        bo.setDetails(detailResults);
        return bo;
    }

    /// mock 整单状态 → 平台明细/完结结果编码
    ///
    /// SUCCESS → success / PROCESSING → pending / 其他(含 FAIL)→ fail
    private String mapState(String status) {
        if (STATUS_SUCCESS.equals(status)) {
            return AllocDetailResultEnum.SUCCESS.getCode();
        }
        if (STATUS_PROCESSING.equals(status)) {
            return AllocDetailResultEnum.PENDING.getCode();
        }
        return AllocDetailResultEnum.FAIL.getCode();
    }
}
