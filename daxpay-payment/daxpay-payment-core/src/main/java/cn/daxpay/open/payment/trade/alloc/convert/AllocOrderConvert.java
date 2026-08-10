package cn.daxpay.open.payment.trade.alloc.convert;

import cn.daxpay.open.payment.trade.alloc.entity.AllocDetail;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.result.AllocCreateResult;
import cn.daxpay.open.payment.trade.alloc.result.AllocDetailResult;
import cn.daxpay.open.payment.trade.alloc.result.AllocOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/// # 分账订单转换器
///
/// 仅做实体到 Result 的同名映射, 明细列表单独转换方法。
@Mapper
public interface AllocOrderConvert {

    AllocOrderConvert CONVERT = Mappers.getMapper(AllocOrderConvert.class);

    /// AllocOrder → Result
    AllocOrderResult toResult(AllocOrder entity);

    /// AllocDetail → Result
    AllocDetailResult toDetailResult(AllocDetail entity);

    /// 明细列表转换
    List<AllocDetailResult> toDetailResults(List<AllocDetail> details);

    /// AllocOrder → 发起结果
    AllocCreateResult toCreateResult(AllocOrder entity);
}
