package cn.daxpay.open.payment.unipay.trade.service;

import cn.daxpay.open.payment.trade.alloc.runtime.service.AllocFinishService;
import cn.daxpay.open.payment.trade.alloc.runtime.service.AllocStartService;
import cn.daxpay.open.payment.unipay.param.trade.alloc.UnipayAllocFinishParam;
import cn.daxpay.open.payment.unipay.param.trade.alloc.UnipayAllocParam;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocResult;
import cn.daxpay.open.payment.unipay.trade.convert.UnipayAllocOrderConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 分账发起服务(对外编排)
///
/// 纯编排: 对外签名入参 → Convert 转内部 → 委托核心 [AllocStartService]/[AllocFinishService] → Convert 转出参。
/// 不落任何业务逻辑, 不直接触碰 Manager/通道。
@Service
@RequiredArgsConstructor
public class AllocOrderService {

    private final AllocStartService allocStartService;
    private final AllocFinishService allocFinishService;

    /// 发起分账
    public AllocResult alloc(UnipayAllocParam param) {
        String allocNo = allocStartService.start(UnipayAllocOrderConvert.CONVERT.toRuntime(param));
        // 发起后查询分账单返回完整信息
        AllocResult result = new AllocResult()
                .setAllocNo(allocNo)
                .setBizAllocNo(param.getBizAllocNo());
        return result;
    }

    /// 发起分账完结(解冻剩余冻结资金, 多次分账收尾)
    public AllocResult finish(UnipayAllocFinishParam param) {
        String allocNo = allocFinishService.finish(UnipayAllocOrderConvert.CONVERT.toRuntimeFinish(param));
        return new AllocResult()
                .setAllocNo(allocNo)
                .setBizAllocNo(param.getBizAllocNo());
    }
}
