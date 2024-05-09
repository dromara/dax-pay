package cn.bootx.platform.starter.wechat.core.notice.service;

import cn.bootx.platform.common.websocket.entity.WsRes;
import cn.bootx.platform.common.websocket.service.UserWsNoticeService;
import cn.bootx.platform.starter.wechat.param.notice.WeChatTemplateParam;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.starter.wechat.core.notice.dao.WeChatTemplateManager;
import cn.bootx.platform.starter.wechat.core.notice.entity.WeChatTemplate;
import cn.bootx.platform.starter.wechat.dto.notice.WeChatTemplateDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信消息模板
 *
 * @author xxm
 * @since 2022/7/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatTemplateService {

    private final WxMpService wxMpService;

    private final WeChatTemplateManager weChatTemplateManager;

    private final UserWsNoticeService userWsNoticeService;

    /**
     * 修改
     */
    public void update(WeChatTemplateParam param) {
        WeChatTemplate weChatTemplate = weChatTemplateManager.findById(param.getId())
            .orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param, weChatTemplate, CopyOptions.create().ignoreNullValue());
        weChatTemplateManager.updateById(weChatTemplate);
    }

    /**
     * 分页
     */
    public PageResult<WeChatTemplateDto> page(PageParam pageParam, WeChatTemplateParam weChatTemplateParam) {
        return MpUtil.convert2DtoPageResult(weChatTemplateManager.page(pageParam, weChatTemplateParam));
    }

    /**
     * 获取单条
     */
    public WeChatTemplateDto findById(Long id) {
        return weChatTemplateManager.findById(id).map(WeChatTemplate::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<WeChatTemplateDto> findAll() {
        return ResultConvertUtil.dtoListConvert(weChatTemplateManager.findAll());
    }

    /**
     * 编码是否已经存在(不包含自身)
     */
    public boolean existsByCode(String code, Long id) {
        return weChatTemplateManager.existsByCode(code, id);
    }

    /**
     * 同步
     */
    @SneakyThrows
    @Async("asyncExecutor")
    @Transactional(rollbackFor = Exception.class)
    public void sync() {
        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();
        // 微信公众号订阅模板
        List<WxMpTemplate> wxTemplates = templateMsgService.getAllPrivateTemplate()
            .stream()
            .filter(o -> StrUtil.isNotBlank(o.getPrimaryIndustry()))
            .collect(Collectors.toList());
        List<String> wxTemplateIds = wxTemplates.stream().map(WxMpTemplate::getTemplateId).collect(Collectors.toList());

        // 系统中模板
        List<WeChatTemplate> weChatTemplates = weChatTemplateManager.findAll();
        List<String> weChatTemplatesIds = weChatTemplates.stream()
            .map(WeChatTemplate::getTemplateId)
            .collect(Collectors.toList());

        // 删除 本地有有,服务端没有
        List<Long> deleteIds = weChatTemplates.stream()
            .filter(o -> !wxTemplateIds.contains(o.getTemplateId()))
            .map(MpIdEntity::getId)
            .collect(Collectors.toList());
        // 新增 服务端有且本地没有
        List<WeChatTemplate> saveTemplate = wxTemplates.stream()
            .filter(o -> !weChatTemplatesIds.contains(o.getTemplateId()))
            .map(WeChatTemplate::init)
            .collect(Collectors.toList());
        weChatTemplateManager.saveAll(saveTemplate);
        weChatTemplateManager.deleteByIds(deleteIds);
        SecurityUtil.getCurrentUser()
            .ifPresent(userDetail -> userWsNoticeService.sendMessageByUser(WsRes.notificationInfo("微信消息模板同步成功"),
                    userDetail.getId()));
    }

}
