package cn.bootx.platform.starter.dingtalk.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.dingtalk.core.robot.service.DingRobotConfigService;
import cn.bootx.platform.starter.dingtalk.dto.robot.DingRobotConfigDto;
import cn.bootx.platform.starter.dingtalk.param.robot.DingRobotConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2021/9/25
 */
@Tag(name = "钉钉机器人配置")
@RestController
@RequestMapping("/ding/robot/config")
@RequiredArgsConstructor
public class DingRobotConfigController {

    private final DingRobotConfigService dingRobotConfigService;

    @Operation(summary = "新增机器人配置")
    @PostMapping("/add")
    public ResResult<DingRobotConfigDto> add(@RequestBody DingRobotConfigParam param) {
        return Res.ok(dingRobotConfigService.add(param));
    }

    @Operation(summary = "修改机器人配置")
    @PostMapping("/update")
    public ResResult<DingRobotConfigDto> update(@RequestBody DingRobotConfigParam param) {
        return Res.ok(dingRobotConfigService.update(param));
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<DingRobotConfigDto>> page(PageParam pageParam, DingRobotConfigParam param) {
        return Res.ok(dingRobotConfigService.page(pageParam, param));
    }

    @Operation(summary = "查询全部")
    @GetMapping("/findAll")
    public ResResult<List<DingRobotConfigDto>> findAll() {
        return Res.ok(dingRobotConfigService.findAll());
    }

    @Operation(summary = "获取详情")
    @GetMapping("/findById")
    public ResResult<DingRobotConfigDto> findById(Long id) {
        return Res.ok(dingRobotConfigService.findById(id));
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(Long id) {
        dingRobotConfigService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(dingRobotConfigService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(dingRobotConfigService.existsByCode(code, id));
    }

}
