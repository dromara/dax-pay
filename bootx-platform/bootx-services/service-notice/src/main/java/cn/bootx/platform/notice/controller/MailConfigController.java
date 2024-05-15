package cn.bootx.platform.notice.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.notice.core.mail.service.MailConfigService;
import cn.bootx.platform.notice.dto.mail.MailConfigDto;
import cn.bootx.platform.notice.param.mail.MailConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxm
 * @since 2020/5/2 14:38
 */
@Tag(name = "邮箱配置")
@RestController
@RequestMapping("/mail/config")
@AllArgsConstructor
public class MailConfigController {

    private final MailConfigService mailConfigService;

    @Operation(summary = "通过 id 获取指定邮箱配置")
    @GetMapping("/findById")
    public ResResult<MailConfigDto> findById(Long id) {
        MailConfigDto mailConfig = mailConfigService.findById(id);
        return Res.ok(mailConfig);
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<MailConfigDto>> page(PageParam pageParam, MailConfigParam param) {
        return Res.ok(mailConfigService.page(pageParam, param));
    }

    @Operation(summary = "增加新邮箱配置")
    @PostMapping("/add")
    public ResResult<MailConfigDto> add(@RequestBody MailConfigParam param) {
        ValidationUtil.validateParam(param);
        MailConfigDto mailConfig = mailConfigService.add(param);
        return Res.ok(mailConfig);
    }

    @Operation(summary = "更新邮箱配置")
    @PostMapping("/update")
    public ResResult<MailConfigDto> updateMailConfig(@RequestBody MailConfigParam param) {
        ValidationUtil.validateParam(param);
        MailConfigDto update = mailConfigService.update(param);
        return Res.ok(update);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(Long id) {
        mailConfigService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "设置启用的邮箱配置")
    @PostMapping("/setUpActivity")
    public ResResult<Void> setUpActivity(Long id) {
        mailConfigService.setUpActivity(id);
        return Res.ok();
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(mailConfigService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(mailConfigService.existsByCode(code, id));
    }

}
