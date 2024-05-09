package cn.bootx.platform.starter.code.gen.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.code.gen.dto.TableGenParamDto;
import cn.bootx.platform.starter.code.gen.entity.DatabaseColumn;
import cn.bootx.platform.starter.code.gen.entity.DatabaseTable;
import cn.bootx.platform.starter.code.gen.service.DatabaseTableService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xxm
 * @since 2022/1/27
 */
@Tag(name = "数据库表信息")
@RestController
@RequestMapping("/gen/table")
@RequiredArgsConstructor
public class DatabaseTableController {

    private final DatabaseTableService databaseTableService;

    @Operation(summary = "表列表")
    @GetMapping("/findAll")
    public ResResult<List<DatabaseTable>> findAll() {
        return Res.ok(databaseTableService.findAll());
    }

    @Operation(summary = "表列表分页")
    @GetMapping("/page")
    public ResResult<PageResult<DatabaseTable>> page(@ParameterObject PageParam pageParam,
                                                     @ParameterObject DatabaseTable param,
                                                     @ParameterObject String dataSourceCode)
    {
        return Res.ok(databaseTableService.page(pageParam, param,dataSourceCode));
    }

    @Operation(summary = "获取表信息")
    @GetMapping("/findByTableName")
    public ResResult<DatabaseTable> findByTableName(String tableName) {
        return Res.ok(databaseTableService.findByTableName(tableName));
    }

    @Operation(summary = "获取数据表行信息")
    @GetMapping("/findColumnByTableName")
    public ResResult<List<DatabaseColumn>> findColumnByTableName(String tableName) {
        return Res.ok(databaseTableService.findColumnByTableName(tableName));
    }

    @Operation(summary = "获取表相关的代码生成参数信息")
    @GetMapping("/getTableGenParam")
    public ResResult<TableGenParamDto> getTableGenParam(String dataSourceCode, String tableName) {
        return Res.ok(databaseTableService.getTableGenParam(dataSourceCode,tableName));
    }

}
