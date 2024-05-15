package cn.bootx.platform.starter.wechat.core.article.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.wechat.dto.article.WeChatArticleDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * @author xxm
 * @since 2022/8/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatArticleService {

    private final WxMpService wxMpService;

    /**
     * 查询图文
     * @return
     */
    @SneakyThrows
    public PageResult<WeChatArticleDto> page(PageParam pageParam) {
        val freePublishService = wxMpService.getFreePublishService();
        val result = freePublishService.getPublicationRecords(pageParam.start(), pageParam.getSize());
        val items = result.getItems().stream().map(WeChatArticleDto::init).collect(Collectors.toList());
        PageResult<WeChatArticleDto> pageResult = new PageResult<>();
        pageResult.setCurrent(pageParam.getCurrent())
            .setRecords(items)
            .setSize(pageParam.getSize())
            .setTotal(result.getTotalCount());
        return pageResult;
    }

}
