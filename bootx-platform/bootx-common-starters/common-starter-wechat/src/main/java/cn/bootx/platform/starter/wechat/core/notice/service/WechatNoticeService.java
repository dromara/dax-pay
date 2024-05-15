package cn.bootx.platform.starter.wechat.core.notice.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.starter.wechat.core.notice.dao.WeChatTemplateManager;
import cn.bootx.platform.starter.wechat.core.notice.entity.WeChatTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信消息通知功能
 *
 * @author xxm
 * @since 2022/7/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatNoticeService {

    private final WxMpService wxMpService;

    private final WeChatTemplateManager weChatTemplateManager;

    /**
     * 发送模板信息 根据模板编号
     */
    @SneakyThrows
    public String sentNotice(String code, String wxOpenId, List<KeyValue> keyValues) {
        WeChatTemplate weChatTemplate = weChatTemplateManager.findTemplateIdByCode(code)
            .orElseThrow(() -> new DataNotExistException("微信消息模板不存在"));
        return this.sentNoticeByTemplateId(weChatTemplate.getTemplateId(), wxOpenId, keyValues);
    }

    /**
     * 发送模板信息 根据微信消息模板ID
     */
    @SneakyThrows
    public String sentNoticeByTemplateId(String templateId, String wxOpenId, List<KeyValue> keyValues) {
        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();
        WxMpTemplateMessage message = new WxMpTemplateMessage();
        message.setToUser(wxOpenId);
        message.setTemplateId(templateId);

        List<WxMpTemplateData> wxMpTemplateData = keyValues.stream()
            .map(keyValue -> new WxMpTemplateData(keyValue.getKey(), keyValue.getValue()))
            .collect(Collectors.toList());
        message.setData(wxMpTemplateData);
        return templateMsgService.sendTemplateMsg(message);
    }

}
