package cn.bootx.platform.starter.wechat.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.wechat.core.media.service.WeChatMediaService;
import cn.bootx.platform.starter.wechat.dto.media.WeChatMediaDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem;

/**
 * @author xxm
 * @since 2022/8/9
 */
@Tag(name = "微信素材管理")
@RestController
@RequestMapping("/wechat/media")
@RequiredArgsConstructor
public class WeChatMediaController {

    private final WeChatMediaService weChatMediaService;

    @Operation(summary = "非图文素材分页")
    @GetMapping("/pageFile")
    public ResResult<PageResult<WeChatMediaDto>> pageFile(PageParam pageParam, String type) {
        return Res.ok(weChatMediaService.pageFile(pageParam, type));
    }

    @Operation(summary = "图文素材分页")
    @GetMapping("/pageNews")
    public ResResult<PageResult<WxMaterialNewsBatchGetNewsItem>> pageNews(PageParam pageParam) {
        return Res.ok(weChatMediaService.pageNews(pageParam));
    }

    @Operation(summary = "删除素材")
    @DeleteMapping("/deleteFile")
    public ResResult<Void> deleteFile(String mediaId) {
        weChatMediaService.deleteFile(mediaId);
        return Res.ok();
    }

    @Operation(summary = "上传素材")
    @PostMapping("/uploadFile")
    public ResResult<Void> uploadFile(String mediaType, MultipartFile file) {
        weChatMediaService.uploadFile(mediaType, file);
        return Res.ok();
    }

}
