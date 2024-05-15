package cn.bootx.platform.notice.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.notice.core.site.domain.SiteMessageInfo;
import cn.bootx.platform.notice.core.site.service.SiteMessageService;
import cn.bootx.platform.notice.dto.site.SiteMessageDto;
import cn.bootx.platform.notice.param.site.SendSiteMessageParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2021/8/8
 */
@Tag(name = "站内信")
@RestController
@RequestMapping("/site/message")
@RequiredArgsConstructor
public class SiteMessageController {

    private final SiteMessageService siteMessageService;

    @Operation(summary = "保存站内信草稿")
    @PostMapping("/saveOrUpdate")
    public ResResult<Void> saveOrUpdate(@RequestBody SendSiteMessageParam param) {
        siteMessageService.saveOrUpdate(param);
        return Res.ok();
    }

    @Operation(summary = "发送站内信")
    @PostMapping("/send")
    public ResResult<Void> send(Long id) {
        siteMessageService.send(id);
        return Res.ok();
    }

    @Operation(summary = "消息详情")
    @GetMapping("/findById")
    public ResResult<SiteMessageDto> findById(Long id) {
        return Res.ok(siteMessageService.findById(id));
    }

    @IgnoreAuth
    @Operation(summary = "获取未读的接收消息条数")
    @GetMapping("/countByReceiveNotRead")
    public ResResult<Integer> countByReceiveNotRead(SiteMessageInfo query) {
        return Res.ok(siteMessageService.countByReceiveNotRead(query));
    }

    @IgnoreAuth
    @Operation(summary = "接收消息分页")
    @GetMapping("/pageByReceive")
    public ResResult<PageResult<SiteMessageInfo>> pageByReceive(PageParam pageParam, SiteMessageInfo query) {
        return Res.ok(siteMessageService.pageByReceive(pageParam, query));
    }

    @Operation(summary = "发送消息分页")
    @GetMapping("/pageBySender")
    public ResResult<PageResult<SiteMessageDto>> pageBySender(PageParam pageParam, SiteMessageInfo query) {
        return Res.ok(siteMessageService.pageBySender(pageParam, query));
    }

    @Operation(summary = "撤回消息")
    @PostMapping("/cancel")
    public ResResult<Void> cancel(Long id) {
        siteMessageService.cancel(id);
        return Res.ok();
    }

    @Operation(summary = "删除消息")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(Long id) {
        siteMessageService.delete(id);
        return Res.ok();
    }

    @IgnoreAuth
    @Operation(summary = "标为已读")
    @PostMapping("/read")
    public ResResult<Void> read(Long id) {
        siteMessageService.read(id);
        return Res.ok();
    }

    @IgnoreAuth
    @Operation(summary = "小程序获取未读的接收消息标题列表")
    @GetMapping("/listByReceiveNotRead")
    public ResResult<List<String>> listByReceiveNotRead(SiteMessageInfo query) {
        return Res.ok(siteMessageService.listByReceiveNotRead(query));
    }
}
