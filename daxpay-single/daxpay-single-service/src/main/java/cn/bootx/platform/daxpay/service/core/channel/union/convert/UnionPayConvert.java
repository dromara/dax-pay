package cn.bootx.platform.daxpay.service.core.channel.union.convert;

import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayRecord;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.GeneralReconcileRecord;
import cn.bootx.platform.daxpay.service.dto.channel.union.UnionPayConfigDto;
import cn.bootx.platform.daxpay.service.dto.channel.union.UnionPayRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/3/11
 */
@Mapper
public interface UnionPayConvert {

    UnionPayConvert CONVERT = Mappers.getMapper(UnionPayConvert.class);

    UnionPayConfigDto convert(UnionPayConfig in);

    UnionPayRecordDto convert(UnionPayRecord in);

    GeneralReconcileRecord convertReconcileRecord(UnionPayRecord in);

}
