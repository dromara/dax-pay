package cn.daxpay.open.payment.unipay.trade.convert;

import cn.daxpay.open.payment.trade.alloc.entity.AllocDetail;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.param.AllocParam;
import cn.daxpay.open.payment.unipay.param.trade.alloc.AllocOrderQueryParam;
import cn.daxpay.open.payment.unipay.param.trade.alloc.AllocSyncParam;
import cn.daxpay.open.payment.unipay.param.trade.alloc.UnipayAllocParam;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocResult;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocSyncResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/// # 分账订单对外转换器
///
/// 聚合分账场景所有对外映射(发起响应/查询结果/入参降级/明细)。
/// 对外签名入参转内部编排参数时, 签名/应用字段无目标自动丢弃; mchNo 同名映射透传(商户维度幂等与归属校验依赖)。
@Mapper
public interface UnipayAllocOrderConvert {

    UnipayAllocOrderConvert CONVERT = Mappers.getMapper(UnipayAllocOrderConvert.class);

    /// 对外签名入参 → 内部编排参数(同名字段映射, mchNo 透传; 签名/应用字段丢弃)
    AllocParam toRuntime(UnipayAllocParam param);

    /// 分账单实体 → 发起响应(同名映射)
    AllocResult toAllocResult(AllocOrder order);

    /// 分账单实体 → 查询结果(同名映射)
    AllocOrderResult toResult(AllocOrder order);

    /// 明细实体 → 明细结果(同名映射)
    AllocOrderResult.AllocDetailResult toDetailResult(AllocDetail detail);

    /// 明细列表转换
    List<AllocOrderResult.AllocDetailResult> toDetailResults(List<AllocDetail> details);
}
