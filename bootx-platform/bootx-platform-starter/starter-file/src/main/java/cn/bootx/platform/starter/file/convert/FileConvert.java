package cn.bootx.platform.starter.file.convert;

import cn.bootx.platform.starter.file.entity.UploadFileInfo;
import cn.bootx.platform.starter.file.result.UploadFileResult;
import org.dromara.x.file.storage.core.FileInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/1/12
 */
@Mapper
public interface FileConvert {

    FileConvert CONVERT = Mappers.getMapper(FileConvert.class);

    UploadFileResult convert(UploadFileInfo in);

    UploadFileInfo convert(FileInfo in);

    FileInfo toFileInfo(UploadFileInfo in);

    UploadFileResult toResult(FileInfo in);

}
