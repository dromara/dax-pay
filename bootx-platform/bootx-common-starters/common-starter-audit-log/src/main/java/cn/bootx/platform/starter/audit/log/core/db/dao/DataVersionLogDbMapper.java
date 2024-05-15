package cn.bootx.platform.starter.audit.log.core.db.dao;

import cn.bootx.platform.starter.audit.log.core.db.entity.DataVersionLogDb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author xxm
 * @since 2022/1/10
 */
@Mapper
public interface DataVersionLogDbMapper extends BaseMapper<DataVersionLogDb> {

    @Select("Select max(version) from starter_audit_data_version where table_name = #{tableName} and data_id =#{dataId}")
    Integer getMaxVersion(@Param("tableName") String tableName, @Param("dataId") String dataId);

}
