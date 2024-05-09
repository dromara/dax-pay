package cn.bootx.platform.baseapi.controller;

import cn.bootx.platform.baseapi.dto.dynamicsource.DynamicDataSourceDto;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.baseapi.core.dynamicsource.service.DynamicDataSourceService;
import cn.bootx.platform.baseapi.param.dynamicsource.DynamicDataSourceParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 动态数据源管理
 *
 * @author xxm
 * @since 2022-09-24
 */
@Tag(name = "动态数据源管理")
@RestController
@RequestMapping("/dynamic/source")
@RequiredArgsConstructor
public class DynamicDataSourceController {

    private final DynamicDataSourceService dynamicDataSourceService;

    @Operation(summary = "添加")
    @PostMapping(value = "/add")
    public ResResult<Void> add(@RequestBody DynamicDataSourceParam param) {
        dynamicDataSourceService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public ResResult<Void> update(@RequestBody DynamicDataSourceParam param) {
        dynamicDataSourceService.update(param);
        return Res.ok();
    }

    @Operation(summary = "分页查询")
    @GetMapping(value = "/page")
    public ResResult<PageResult<DynamicDataSourceDto>> page(PageParam pageParam,
                                                            DynamicDataSourceParam dynamicDataSourceParam) {
        return Res.ok(dynamicDataSourceService.page(pageParam, dynamicDataSourceParam));
    }

    @Operation(summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public ResResult<DynamicDataSourceDto> findById(Long id) {
        return Res.ok(dynamicDataSourceService.findById(id));
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(dynamicDataSourceService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(dynamicDataSourceService.existsByCode(code, id));
    }

    @Operation(summary = "查询所有")
    @GetMapping(value = "/findAll")
    public ResResult<List<DynamicDataSourceDto>> findAll() {
        return Res.ok(dynamicDataSourceService.findAll());
    }

    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete")
    public ResResult<Void> delete(Long id) {
        dynamicDataSourceService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "测试连接(根据参数)")
    @PostMapping(value = "/testConnection")
    public ResResult<String> testConnection(@RequestBody DynamicDataSourceParam param) {
        return Res.ok(dynamicDataSourceService.testConnection(param));
    }

    @Operation(summary = "测试连接(根据主键ID)")
    @GetMapping(value = "/testConnectionById")
    public ResResult<String> testConnectionById(Long id) {
        return Res.ok(dynamicDataSourceService.testConnection(id));
    }

    @Operation(summary = "根据id进行添加到连接池中")
    @PostMapping(value = "/addDynamicDataSourceById")
    public ResResult<String> addDynamicDataSourceById(Long id) {
        dynamicDataSourceService.addDynamicDataSourceById(id);
        return Res.ok();
    }

    @Operation(summary = "是否已经添加到连接池中")
    @GetMapping(value = "/existsByDataSourceKey")
    public ResResult<Boolean> existsByDataSourceKey(String code) {
        return Res.ok(dynamicDataSourceService.existsByDataSourceKey(code));
    }

    @Operation(summary = "查询当前数据源列表")
    @GetMapping(value = "/findAllDataSource")
    public ResResult<List<KeyValue>> findAllDataSource() {
        return Res.ok(dynamicDataSourceService.findAllDataSource());
    }

    @Operation(summary = "从数据源列表中删除指定数据源")
    @DeleteMapping("/removeDataSourceByKey")
    public ResResult<Void> removeDataSourceByKey(String key) {
        dynamicDataSourceService.removeDataSourceByKey(key);
        return Res.ok();
    }

}
