package cn.bootx.platform.starter.file.convert;

import cn.bootx.platform.starter.file.entity.FilePlatform;
import cn.bootx.platform.starter.file.result.FilePlatformResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/8/12
 */
@Mapper
public interface FilePlatformConvert {
    FilePlatformConvert CONVERT = Mappers.getMapper(FilePlatformConvert.class);

    FilePlatformResult toResult(FilePlatform filePlatform);

}
