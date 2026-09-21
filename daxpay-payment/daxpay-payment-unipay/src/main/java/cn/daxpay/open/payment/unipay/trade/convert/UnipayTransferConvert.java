package cn.daxpay.open.payment.unipay.trade.convert;

import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.unipay.param.trade.transfer.TransferParam;
import cn.daxpay.open.payment.unipay.result.trade.transfer.TransferOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 转账转换器(对外)
///
/// 汇聚转账场景的对外映射:
/// - 转账凭证实体 → 对外查询结果 [TransferOrderResult] (transferNo 由平台凭证 tradeNo 改名而来)
/// - 对外签名入参 [TransferParam] → 内部编排参数 (金额同为分, 同名直通; mchNo 同名映射透传供商户维度幂等/归属校验;
///   appId 同名映射透传供出站通知派发; sign 等签名字段丢弃; channel 为 [TransferStartService#start] 的独立入参, 不映射)
@Mapper
public interface UnipayTransferConvert {

    UnipayTransferConvert CONVERT = Mappers.getMapper(UnipayTransferConvert.class);

    /// 转账凭证实体 → 对外查询结果(transferNo ← tradeNo 改名映射, 其余同名映射)
    @Mapping(target = "transferNo", source = "tradeNo")
    TransferOrderResult toResult(TransferTrade trade);

    /// 对外签名入参 → 内部编排参数(同名直通, 金额两侧均为分)
    cn.daxpay.open.payment.trade.transfer.param.TransferParam toRuntime(TransferParam param);
}
