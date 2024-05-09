package cn.bootx.platform.starter.wechat.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.wechat.core.menu.service.WeChatMenuService;
import cn.bootx.platform.starter.wechat.dto.menu.WeChatMenuDto;
import cn.bootx.platform.starter.wechat.param.menu.WeChatMenuParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微信菜单管理
 *
 * @author xxm
 * @since 2022/8/6
 */
@Tag(name = "微信菜单管理")
@RestController
@RequestMapping("/wechat/menu")
@RequiredArgsConstructor
public class WeChatMenuController {

    private final WeChatMenuService weChatMenuService;

    @Operation(summary = "添加")
    @PostMapping(value = "/add")
    public ResResult<Void> add(@RequestBody WeChatMenuParam param) {
        weChatMenuService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public ResResult<Void> update(@RequestBody WeChatMenuParam param) {
        weChatMenuService.update(param);
        return Res.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete")
    public ResResult<Void> delete(Long id) {
        weChatMenuService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public ResResult<WeChatMenuDto> findById(Long id) {
        return Res.ok(weChatMenuService.findById(id));
    }

    @Operation(summary = "查询所有")
    @GetMapping(value = "/findAll")
    public ResResult<List<WeChatMenuDto>> findAll() {
        return Res.ok(weChatMenuService.findAll());
    }

    @Operation(summary = "分页查询")
    @GetMapping(value = "/page")
    public ResResult<PageResult<WeChatMenuDto>> page(PageParam pageParam, WeChatMenuParam weChatMenuParam) {
        return Res.ok(weChatMenuService.page(pageParam, weChatMenuParam));
    }

    @Operation(summary = "发布菜单")
    @PostMapping("/publish")
    public ResResult<Void> publish(Long id) {
        weChatMenuService.publish(id);
        return Res.ok();
    }

    @Operation(summary = "导入微信自定义菜单到系统中")
    @PostMapping("/importMenu")
    public ResResult<Void> importMenu() {
        weChatMenuService.importMenu();
        return Res.ok();
    }

    @Operation(summary = "清空微信自定义菜单")
    @PostMapping("/clearMenu")
    public ResResult<Void> clearMenu() {
        weChatMenuService.clearMenu();
        return Res.ok();
    }

}
