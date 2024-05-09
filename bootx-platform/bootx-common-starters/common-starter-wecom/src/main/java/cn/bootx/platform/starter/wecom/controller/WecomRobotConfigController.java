package cn.bootx.platform.starter.wecom.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.wecom.core.robot.service.WecomRobotConfigService;
import cn.bootx.platform.starter.wecom.dto.robot.WecomRobotConfigDto;
import cn.bootx.platform.starter.wecom.param.robot.WecomRobotConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2022/7/26
 */
@Tag(name = "企业微信机器人配置")
@RestController
@RequestMapping("/wecom/robot/config")
@RequiredArgsConstructor
public class WecomRobotConfigController {

    private final WecomRobotConfigService robotConfigService;

    @Operation(summary = "新增机器人配置")
    @PostMapping("/add")
    public ResResult<Void> add(@RequestBody WecomRobotConfigParam param) {
        robotConfigService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改机器人配置")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody WecomRobotConfigParam param) {
        robotConfigService.update(param);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<WecomRobotConfigDto>> page(PageParam pageParam, WecomRobotConfigParam param) {
        return Res.ok(robotConfigService.page(pageParam, param));
    }

    @Operation(summary = "查询全部")
    @GetMapping("/findAll")
    public ResResult<List<WecomRobotConfigDto>> findAll() {
        return Res.ok(robotConfigService.findAll());
    }

    @Operation(summary = "获取详情")
    @GetMapping("/findById")
    public ResResult<WecomRobotConfigDto> findById(Long id) {
        return Res.ok(robotConfigService.findById(id));
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(Long id) {
        robotConfigService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(robotConfigService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(robotConfigService.existsByCode(code, id));
    }

}
