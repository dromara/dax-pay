package cn.daxpay.open.channel.kuaiqian.convert.direct;

import cn.daxpay.open.channel.kuaiqian.entity.direct.KuaiqianDirectKeyConfig;
import cn.daxpay.open.channel.kuaiqian.param.direct.KuaiqianDirectKeyConfigParam;
import cn.daxpay.open.channel.kuaiqian.result.direct.KuaiqianDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 快钱直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface KuaiqianDirectKeyConfigConvert {
    KuaiqianDirectKeyConfigConvert CONVERT = Mappers.getMapper(KuaiqianDirectKeyConfigConvert.class);

    KuaiqianDirectKeyConfigResult toResult(KuaiqianDirectKeyConfig entity);

    void copy(KuaiqianDirectKeyConfigParam param, @MappingTarget KuaiqianDirectKeyConfig entity);
}
