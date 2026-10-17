package cn.daxpay.open.channel.duolabao.convert.direct;

import cn.daxpay.open.channel.duolabao.entity.direct.DuolabaoDirectKeyConfig;
import cn.daxpay.open.channel.duolabao.param.direct.DuolabaoDirectKeyConfigParam;
import cn.daxpay.open.channel.duolabao.result.direct.DuolabaoDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 哆啦宝直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DuolabaoDirectKeyConfigConvert {
    DuolabaoDirectKeyConfigConvert CONVERT = Mappers.getMapper(DuolabaoDirectKeyConfigConvert.class);

    DuolabaoDirectKeyConfigResult toResult(DuolabaoDirectKeyConfig entity);

    void copy(DuolabaoDirectKeyConfigParam param, @MappingTarget DuolabaoDirectKeyConfig entity);
}
