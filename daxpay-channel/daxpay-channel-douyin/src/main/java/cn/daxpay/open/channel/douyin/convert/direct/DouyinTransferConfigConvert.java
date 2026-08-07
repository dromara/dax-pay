package cn.daxpay.open.channel.douyin.convert.direct;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinTransferConfig;
import cn.daxpay.open.channel.douyin.param.direct.DouyinTransferConfigParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinTransferConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 抖音转账配置转换
///
/// MapStruct 转换器, 负责转账配置在实体、参数和返回结果之间的转换, 更新时空值不覆盖。
/// 冗余展示字段(发起应用名/douyinAppId/应用类型)由 Service 填充, 不经 Convert。
///
@Mapper
public interface DouyinTransferConfigConvert {

    DouyinTransferConfigConvert CONVERT = Mappers.getMapper(DouyinTransferConfigConvert.class);

    /// 转换为返回对象
    DouyinTransferConfigResult toResult(DouyinTransferConfig entity);

    /// 转换为实体
    DouyinTransferConfig toEntity(DouyinTransferConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(DouyinTransferConfigParam param, @MappingTarget DouyinTransferConfig entity);
}
