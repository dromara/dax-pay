package cn.bootx.platform.starter.code.gen.dao;

import cn.bootx.platform.starter.code.gen.entity.DatabaseColumn;
import cn.bootx.platform.starter.code.gen.entity.DatabaseTable;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * 表信息查询
 *
 * @author xxm
 * @since 2022/1/27
 */
@Mapper
public interface DatabaseTableMapper {

    /**
     * 查询表信息 列表
     */
    @Select("select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables"
            + " where table_schema = (select database()) ${ew.customSqlSegment}")
    List<DatabaseTable> findAll(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 查询表信息 分页
     */
    @Select("select * from (select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables"
            + " where table_schema = (select database())) as t ${ew.customSqlSegment}")
    Page<DatabaseTable> page(Page<DatabaseTable> mpPage, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 查询表信息 详情
     */
    @Select("select table_name, engine, table_comment, create_time from information_schema.tables"
            + "	where table_schema = (select database()) and table_name = #{tableName}")
    Optional<DatabaseTable> findByTableName(@Param("tableName") String tableName);

    /**
     * 查询表信息 详细结构
     */
    @Select("select column_name, data_type, column_comment, column_key, extra from information_schema.columns"
            + " where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
    List<DatabaseColumn> findColumnByTableName(@Param("tableName") String tableName);

}
