package cn.daxpay.multi.channel.union.convert;

import cn.daxpay.multi.channel.union.UnionPayConfigResult;
import cn.daxpay.multi.channel.union.entity.config.UnionPayConfig;
import cn.daxpay.multi.channel.union.param.config.UnionPayConfigParam;
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
