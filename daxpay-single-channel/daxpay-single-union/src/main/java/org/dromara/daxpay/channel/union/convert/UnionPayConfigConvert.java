package org.dromara.daxpay.channel.union.convert;

import org.dromara.daxpay.channel.union.entity.config.UnionPayConfig;
import org.dromara.daxpay.channel.union.param.config.UnionPayConfigParam;
import org.dromara.daxpay.channel.union.result.UnionPayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/9/6
 */
@Mapper
public interface UnionPayConfigConvert {

    UnionPayConfigConvert CONVERT = Mappers.getMapper(UnionPayConfigConvert.class);

    UnionPayConfigResult toResult(UnionPayConfig unionPayConfig);

    UnionPayConfig toEntity(UnionPayConfigParam param);

    UnionPayConfig copy(UnionPayConfig unionPayConfig);


}
