package cn.bootx.platform.starter.file.dao;

import cn.bootx.platform.starter.file.entity.UploadFileInfo;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xxm
 * @since 2022/1/12
 */
@Mapper
public interface UploadFileMapper extends MPJBaseMapper<UploadFileInfo> {

}
