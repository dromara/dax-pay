package cn.bootx.platform.starter.audit.log.core.db.dao;

import cn.bootx.platform.starter.audit.log.core.db.entity.OperateLogDb;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogDbMapper extends MPJBaseMapper<OperateLogDb> {

}
