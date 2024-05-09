package cn.bootx.platform.baseapi.core.dataresult.service;

import cn.bootx.platform.baseapi.code.QuerySqlCode;
import cn.bootx.platform.baseapi.core.dataresult.dao.DataResultSqlManager;
import cn.bootx.platform.baseapi.core.dataresult.entity.DataResultSql;
import cn.bootx.platform.baseapi.dto.dataresult.SqlParam;
import cn.bootx.platform.baseapi.core.dynamicsource.dao.DynamicDataSourceManager;
import cn.bootx.platform.baseapi.core.dynamicsource.entity.DynamicDataSource;
import cn.bootx.platform.baseapi.core.dynamicsource.service.DynamicDataSourceService;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.PageResult;
import cn.hutool.db.handler.EntityHandler;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xxm
 * @since 2023/3/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataResultSqlService {

    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    private final DynamicDataSourceManager dynamicDataSourceManager;

    private final DynamicDataSourceService dynamicDataSourceService;

    private final DataResultSqlManager dataResultSqlManager;

    /**
     * 添加查询语句
     */
    public void add() {

    }

    /**
     * 修改
     */
    public void update(){

    }

    /**
     * 分页查询
     */
    public PageResult<DataResultSql> page(){
        return new PageResult<>();
    }

    /**
     * 执行SQL查询
     */
    @SneakyThrows
    public Object querySql(Map<String, Object> map) {
        // 获取SQL语句, 将 #{} 和 ${} 元素进行解析和替换
        String sql = "select * from iam_client where system=#{system}";
        DataResultSql dataResultSql = new DataResultSql().setDatabaseId(1633376006887067648L).setIsList(true).setSql(sql);
        SqlAndParam sqlAndParam = this.sqlParamParser(dataResultSql, map);

        // 对SQL语句进行解析
        DataSource dataSource = this.getDataSource(dataResultSql.getDatabaseId());
        Connection connection = dataSource.getConnection();
        if (Objects.equals(dataResultSql.getIsList(), true)) {
            return SqlExecutor.query(connection, sqlAndParam.sql, new EntityListHandler(),
                    ArrayUtil.toArray(sqlAndParam.param, Object.class));
        }
        else {
            return SqlExecutor.query(connection, sqlAndParam.sql, new EntityHandler(),
                    ArrayUtil.toArray(sqlAndParam.param, Object.class));
        }
    }

    /**
     * 解析SQL
     */
    private SqlAndParam sqlParamParser(DataResultSql dataResultSql, Map<String, Object> map) {
        String sql = dataResultSql.getSql();
        Map<String, SqlParam> sqlParamMap = Optional.ofNullable(dataResultSql.getParams())
            .orElse(new ArrayList<>(0))
            .stream()
            .collect(Collectors.toMap(SqlParam::getName, Function.identity(), CollectorsFunction::retainLatest));
        // # 参数处理
        GenericTokenParser replaceTokenParser = new GenericTokenParser("#{", "}", content -> {
            // 获取类型, 看是否是获取用户信息一类的
            SqlParam sqlParam = sqlParamMap.get(content);
            if (Objects.nonNull(sqlParam) && Objects.equals(sqlParam.getType(), QuerySqlCode.TYPE_USER_ID)) {
                return String.valueOf(SecurityUtil.getUserId());
            }
            return map.get(content).toString();
        });
        sql = replaceTokenParser.parse(sql);

        // $占位参数处理
        List<Object> list = new ArrayList<>();
        GenericTokenParser preparedTokenParser = new GenericTokenParser("${", "}", content -> {
            Object param = map.get(content);
            list.add(param);
            return "?";
        });
        sql = preparedTokenParser.parse(sql);
        return new SqlAndParam(sql, list);
    }

    /**
     * 通过SQL查出结果字段
     */
//    @SneakyThrows
//    public List<String> queryFieldBySql(SqlQueryParam param) {
//        String sql = "select * from iam_client";
//        DataSource dataSource = this.getDataSource(param.getDatabaseId());
//        Connection connection = dataSource.getConnection();
//        Entity query = SqlExecutor.query(connection, sql, new EntityHandler());
//        System.out.println(query);
//        return new ArrayList<>(query.keySet());
//    }

    /**
     * 获取数据源
     */
    private DataSource getDataSource(Long id) {
        DynamicDataSource dataSource = dynamicDataSourceManager.findById(id).orElseThrow(DataNotExistException::new);
        DataSource source = dynamicRoutingDataSource.getDataSource(dataSource.getCode());
        if (Objects.isNull(source)) {
            dynamicDataSourceService.addDynamicDataSource(dataSource);
            source = dynamicRoutingDataSource.getDataSource(dataSource.getCode());
        }
        return source;
    }

    /**
     * 解析后的SQl语句和参数
     */
    @Getter
    @AllArgsConstructor
    private static class SqlAndParam {

        /** 解析后的SQL语句 */
        private final String sql;

        /** 解析后的参数 */
        private List<Object> param;

    }

}
