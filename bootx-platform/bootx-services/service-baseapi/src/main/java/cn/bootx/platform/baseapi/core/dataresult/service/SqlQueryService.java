package cn.bootx.platform.baseapi.core.dataresult.service;

import cn.bootx.platform.baseapi.core.dataresult.dao.SqlQueryMapper;
import cn.bootx.platform.baseapi.dto.dataresult.SqlQueryResult;
import cn.bootx.platform.baseapi.param.dataresult.SqlQueryParam;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SQL查询
 * @author xxm
 * @since 2023/8/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SqlQueryService {

    private final SqlQueryMapper SqlQueryMapper;

    /**
     * SQL查询语句
     * TODO 返回结果是无序的, 后期优化成有序的
     */
    public SqlQueryResult query(SqlQueryParam param, PageParam pageParam){
        try {
            Page<?> page = MpUtil.getMpPage(pageParam, Object.class);
            page.setSearchCount(false);
            if (StrUtil.isNotBlank(param.getDatabaseKey())){
                DynamicDataSourceContextHolder.push(param.getDatabaseKey());
            }
            List<Map<String, Object>> records;
            if (param.isEnablePage()){
                records = SqlQueryMapper.queryByPage(page, param.getSql()).getRecords();
            } else {
                records = SqlQueryMapper.query(param.getSql());
            }
            SqlQueryResult sqlQueryResult = new SqlQueryResult();
            // 构造出表头
            if (CollUtil.isNotEmpty(records)){
                Map<String, Object> objectMap = records.get(0);
                sqlQueryResult.setData(records)
                        .setFields(new ArrayList<>(objectMap.keySet()));
            }
            return sqlQueryResult;
        } finally {
            if (StrUtil.isNotBlank(param.getDatabaseKey())) {
                DynamicDataSourceContextHolder.poll();
            }
        }
    }

    /**
     * 导出查询结果
     */
    public byte[] exportQueryResult(SqlQueryParam param, PageParam pageParam){
        SqlQueryResult queryResult = this.query(param, pageParam);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        EasyExcel.write(byteArrayOutputStream)
                .head(this.head(queryResult))
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("result")
                .doWrite(this.dataList(queryResult));
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 单头
     */
    private List<List<String>> head(SqlQueryResult queryResult) {
        return queryResult.getFields()
                .stream()
                .map(Collections::singletonList)
                .collect(Collectors.toList());
    }

    /**
     * 内容
     */
    private List<List<Object>> dataList(SqlQueryResult queryResult) {
        List<Map<String, Object>> data = queryResult.getData();
        List<String> fields = queryResult.getFields();
        return data.stream()
                .map(map->{
                    ArrayList<Object> objects = new ArrayList<>(fields.size());
                    fields.forEach(key->objects.add(StrUtil.toString(map.get(key))));
                    return objects;
                }).collect(Collectors.toList());
    }
}
