package cn.bootx.platform.baseapi.core.dataresult.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询语句
 * @author xxm
 * @since 2023/8/29
 */
@Mapper
public interface SqlQueryMapper {

    /**
     * 查询带分页
     * @param page 分页
     * @param sql 查询语句
     */
    @Select("${sql}")
    Page<Map<String,Object>> queryByPage(Page<?> page, @Param("sql") String sql);

    /**
     * 查询
     * @param sql 查询语句
     */
    @Select("${sql}")
    List<Map<String,Object>> query(@Param("sql") String sql);
}
