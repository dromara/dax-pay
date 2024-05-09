package cn.bootx.platform.starter.file.convert;

import cn.bootx.platform.starter.file.dto.UploadFileDto;
import cn.bootx.platform.starter.file.entity.UploadFileInfo;
import org.dromara.x.file.storage.core.FileInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/1/12
 */
@Mapper
public interface    FileConvert {

    FileConvert CONVERT = Mappers.getMapper(FileConvert.class);

    UploadFileDto convert(UploadFileInfo in);

    UploadFileInfo convert(FileInfo in);

    FileInfo toFileInfo(UploadFileInfo in);

    UploadFileDto toDto(FileInfo in);


}
