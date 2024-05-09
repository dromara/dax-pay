package cn.bootx.platform.baseapi.controller;

import cn.bootx.platform.baseapi.core.dataresult.entity.DataResultSql;
import cn.bootx.platform.baseapi.core.dataresult.service.DataResultSqlService;
import cn.bootx.platform.baseapi.core.dataresult.service.SqlQueryService;
import cn.bootx.platform.baseapi.dto.dataresult.SqlQueryResult;
import cn.bootx.platform.baseapi.param.dataresult.SqlQueryParam;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.hutool.db.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SQL查询
 *
 * @author xxm
 * @since 2023/3/9
 */
@Tag(name = "SQL查询")
@RestController
@RequestMapping("/data/result")
@RequiredArgsConstructor
public class DataResultController {

    private final DataResultSqlService dataResultSqlService;
    private final SqlQueryService sqlQueryService;


    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<DataResultSql>> page(){
        return Res.ok(dataResultSqlService.page());
    }

    @Operation(summary = "新建")
    @PostMapping("/add")
    public ResResult<Void> add(){
        dataResultSqlService.add();
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public ResResult<Void> update(){
        dataResultSqlService.update();
        return Res.ok();
    }

    @Operation(summary = "测试SQL解析和执行")
    @PostMapping("/test")
    public ResResult<Void> test(@RequestBody Map<String, Object> map) {
        dataResultSqlService.querySql(map);
        return Res.ok();
    }

//    @Operation(summary = "通过SQL查出结果字段")
//    @PostMapping("/queryFieldBySql")
//    public ResResult<List<String>> queryFieldBySql(@RequestBody SqlQueryParam param) {
//        return Res.ok(dataResultService.queryFieldBySql(param));
//    }

    @Operation(summary = "执行SQL查询语句")
    @PostMapping("/querySql")
    public ResResult<SqlQueryResult> querySql(@RequestBody SqlQueryParam param, PageParam pageParam){
        return Res.ok(sqlQueryService.query(param,pageParam));
    }

    @SneakyThrows
    @Operation(summary = "导出SQL查询的结果")
    @PostMapping("/exportQueryResult")
    public ResponseEntity<byte[]> exportQueryResult(@RequestBody SqlQueryParam param, @ParameterObject PageParam pageParam){
        byte[] bytes = sqlQueryService.exportQueryResult(param,pageParam);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}
