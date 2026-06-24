package cn.daxpay.open.platform.notify.controller.notice;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.platform.notify.param.notice.NotifyNoticeParam;
import cn.daxpay.open.platform.notify.param.notice.NotifyNoticeQuery;
import cn.daxpay.open.platform.notify.result.notice.NotifyNoticeResult;
import cn.daxpay.open.platform.notify.service.notice.NotifyNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// 公告管理(管理端)
@PermCode(menuCode = "system:notify")
@Validated
@Tag(name = "公告管理")
@RestController
@RequestMapping("/notify/notice")
@RequiredArgsConstructor
public class NotifyNoticeController {

    private final NotifyNoticeService noticeService;

    @PermCode(code = "notice:add", nameCn = "公告管理", nameEn = "Notice Manage")
    @Operation(summary = "新建公告")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody NotifyNoticeParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        noticeService.add(param);
        return Res.ok();
    }

    @PermCode(code = "notice:add", nameCn = "公告管理", nameEn = "Notice Manage")
    @Operation(summary = "编辑公告")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody NotifyNoticeParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        noticeService.update(param);
        return Res.ok();
    }

    @PermCode(code = "notice:add", nameCn = "公告管理", nameEn = "Notice Manage")
    @Operation(summary = "删除公告")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        noticeService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "notice:publish", nameCn = "公告发布", nameEn = "Notice Publish")
    @Operation(summary = "发布公告")
    @PostMapping("/publish")
    public Result<Void> publish(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        noticeService.publish(id);
        return Res.ok();
    }

    @PermCode(code = "notice:publish", nameCn = "公告发布", nameEn = "Notice Publish")
    @Operation(summary = "下线公告")
    @PostMapping("/offline")
    public Result<Void> offline(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        noticeService.offline(id);
        return Res.ok();
    }

    @PermCode(code = "notice:view", nameCn = "公告查看", nameEn = "Notice View")
    @Operation(summary = "公告详情")
    @GetMapping("/get")
    public Result<NotifyNoticeResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(noticeService.findById(id));
    }

    @PermCode(code = "notice:view", nameCn = "公告查看", nameEn = "Notice View")
    @Operation(summary = "公告分页")
    @GetMapping("/page")
    public Result<PageResult<NotifyNoticeResult>> page(PageParam pageParam, NotifyNoticeQuery query) {
        return Res.ok(noticeService.page(pageParam, query));
    }
}
