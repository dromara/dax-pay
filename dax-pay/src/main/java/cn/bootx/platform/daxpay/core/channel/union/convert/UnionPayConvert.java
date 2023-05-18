package cn.bootx.platform.daxpay.core.channel.union.convert;

import cn.bootx.platform.daxpay.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.dto.paymodel.union.UnionPayConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @date 2022/3/11
 */
@Mapper
public interface UnionPayConvert {

    UnionPayConvert CONVERT = Mappers.getMapper(UnionPayConvert.class);

    UnionPayConfigDto convert(UnionPayConfig in);

}
