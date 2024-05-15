package cn.bootx.platform.notice.core.site.service;

import cn.bootx.platform.common.core.code.CommonCode;
import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.websocket.entity.WsRes;
import cn.bootx.platform.common.websocket.service.UserWsNoticeService;
import cn.bootx.platform.notice.param.site.SendSiteMessageParam;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.notice.core.site.dao.SiteMessageManager;
import cn.bootx.platform.notice.core.site.dao.SiteMessageUserManager;
import cn.bootx.platform.notice.core.site.domain.SiteMessageInfo;
import cn.bootx.platform.notice.core.site.entity.SiteMessage;
import cn.bootx.platform.notice.core.site.entity.SiteMessageUser;
import cn.bootx.platform.notice.dto.site.SiteMessageDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.DesensitizedUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.notice.code.SiteMessageCode.*;

/**
 * 站内信
 *
 * @author xxm
 * @since 2021/8/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SiteMessageService {

    private final SiteMessageManager siteMessageManager;

    private final SiteMessageUserManager siteMessageUserManager;

    private final UserWsNoticeService userWsNoticeService;

    /**
     * 保存或更新草稿
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SendSiteMessageParam param) {
        SiteMessage siteMessage;
        if (Objects.nonNull(param.getId())) {
            siteMessage = siteMessageManager.findById(param.getId())
                    .orElseThrow(() -> new DataNotExistException("站内信信息不存在"));
            BeanUtil.copyProperties(param, siteMessage, CopyOptions.create().ignoreNullValue());
        } else {
            siteMessage = new SiteMessage().setTitle(param.getTitle())
                    .setSendState(STATE_DRAFT)
                    .setContent(param.getContent())
                    .setReceiveType(param.getReceiveType())
                    .setEfficientTime(param.getEfficientTime())
                    .setSenderTime(LocalDateTime.now());
            // 添加消息关联人信息 暂时这段逻辑用不到, 现在发布的都是全体用户信心
            if (Objects.equals(RECEIVE_USER, param.getReceiveType())) {
                List<SiteMessageUser> siteMessageUsers = param.getReceiveIds()
                        .stream()
                        .map(userId -> new SiteMessageUser().setMessageId(param.getId()).setReceiveId(userId))
                        .collect(Collectors.toList());
                siteMessageUserManager.saveAll(siteMessageUsers);
            }
        }
        // 新增或更新站内信内容
        val userDetail = SecurityUtil.getCurrentUser();
        siteMessage.setTitle(param.getTitle())
                .setSenderId(userDetail.map(UserDetail::getId).orElse(DesensitizedUtil.userId()))
                .setSenderName(userDetail.map(UserDetail::getName).orElse("未知"));
        siteMessageManager.saveOrUpdate(siteMessage);

    }

    /**
     * 站内信发送消息
     */
    @Transactional(rollbackFor = Exception.class)
    public void send(Long id) {
        SiteMessage siteMessage = siteMessageManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("站内信信息不存在"));
        val userDetail = SecurityUtil.getCurrentUser();

        // 新增站内信内容
        siteMessage.setSenderId(userDetail.map(UserDetail::getId).orElse(DesensitizedUtil.userId()))
                .setSendState(STATE_SENT)
                .setSenderName(userDetail.map(UserDetail::getName).orElse("未知"))
                .setSenderTime(LocalDateTime.now());
        siteMessageManager.updateById(siteMessage);
        userWsNoticeService.sendMessageByAll(WsRes.eventNotice(EVENT_MESSAGE_UPDATE));
    }

    /**
     * 发送站内信
     */
    @Transactional(rollbackFor = Exception.class)
    public void send(SendSiteMessageParam param) {

        // 新增站内信内容
        SiteMessage siteMessage = new SiteMessage().setTitle(param.getTitle())
                .setContent(param.getContent())
                .setSendState(STATE_SENT)
                .setReceiveType(param.getReceiveType())
                .setEfficientTime(param.getEfficientTime())
                .setSenderId(param.getSenderId())
                .setSenderName(param.getSenderName())
                .setSenderTime(LocalDateTime.now());
        siteMessageManager.save(siteMessage);
        // 添加消息关联人信息
        if (Objects.equals(RECEIVE_USER, param.getReceiveType())) {

            List<SiteMessageUser> siteMessageUsers = param.getReceiveIds()
                    .stream()
                    .map(userId -> new SiteMessageUser().setMessageId(siteMessage.getId()).setReceiveId(userId))
                    .collect(Collectors.toList());
            siteMessageUserManager.saveAll(siteMessageUsers);
            userWsNoticeService.sendMessageByUsers(WsRes.eventNotice(EVENT_MESSAGE_UPDATE), param.getReceiveIds());
        }
    }

    /**
     * 发送给单个用户信息, 发送人为系统
     */
    public void sendSingleUserBySystem(String title, String content, Long userId) {
        val param = new SendSiteMessageParam().setTitle(title)
                .setContent(content)
                .setSenderId(CommonCode.SYSTEM_DEFAULT_USERID)
                .setSenderName(CommonCode.SYSTEM_DEFAULT_USERNAME)
                .setReceiveType(RECEIVE_USER)
                .setReceiveIds(Collections.singletonList(userId));
        this.send(param);
    }

    /**
     * 发送给多个用户信息, 发送人为系统
     */
    public void sendMultiUserBySystem(String title, String content, List<Long> userIds) {
        val param = new SendSiteMessageParam().setTitle(title)
                .setContent(content)
                .setSenderId(CommonCode.SYSTEM_DEFAULT_USERID)
                .setSenderName(CommonCode.SYSTEM_DEFAULT_USERNAME)
                .setReceiveType(RECEIVE_USER)
                .setReceiveIds(userIds);
        this.send(param);
    }

    /**
     * 撤回消息
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long messageId) {
        SiteMessage siteMessage = siteMessageManager.findById(messageId)
                .orElseThrow(() -> new DataNotExistException("站内信不存在"));
        siteMessage.setCancelTime(LocalDateTime.now()).setSendState(STATE_CANCEL);
        siteMessageManager.updateById(siteMessage);
        userWsNoticeService.sendMessageByAll(WsRes.eventNotice(EVENT_MESSAGE_UPDATE));
    }

    /**
     * 删除消息
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long messageId) {
        SiteMessage siteMessage = siteMessageManager.findById(messageId)
                .orElseThrow(() -> new DataNotExistException("站内信不存在"));
        if (!CollUtil.toList(STATE_CANCEL, STATE_DRAFT).contains(siteMessage.getSendState())) {
            throw new BizException("站内信不是撤回或草稿状态，无法被删除");
        }
        siteMessageManager.deleteById(messageId);
        siteMessageUserManager.deleteByMessageId(messageId);
    }

    /**
     * 未读消息数量
     */
    public Integer countByReceiveNotRead(SiteMessageInfo query) {
        Long userId = SecurityUtil.getUserId();
        // Long userId = 0L;
        return siteMessageManager.countByReceiveNotRead(userId);
    }

    /**
     * 接收消息分页
     */
    public PageResult<SiteMessageInfo> pageByReceive(PageParam pageParam, SiteMessageInfo query) {
        Long userId = SecurityUtil.getUserId();
        return MpUtil.convert2PageResult(siteMessageManager.pageByReceive(pageParam, query, userId));
    }

    /**
     * 发送消息分页
     */
    public PageResult<SiteMessageDto> pageBySender(PageParam pageParam, SiteMessageInfo query) {
        Long userId = SecurityUtil.getUserId();
        return MpUtil.convert2DtoPageResult(siteMessageManager.pageBySender(pageParam, query, userId));
    }

    /**
     * 查询详情
     */
    public SiteMessageDto findById(Long id) {
        return siteMessageManager.findById(id).map(SiteMessage::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 阅读
     */
    public void read(Long messageId) {
        Long userId = SecurityUtil.getUserId();
        SiteMessageUser siteMessageUser = siteMessageUserManager.findByMessageId(messageId)
                .orElse(new SiteMessageUser().setReceiveId(userId).setMessageId(messageId));
        siteMessageUser.setHaveRead(true).setReadTime(LocalDateTime.now());
        siteMessageUserManager.saveOrUpdate(siteMessageUser);
    }

    public List<String> listByReceiveNotRead(SiteMessageInfo query) {
        Long userId = SecurityUtil.getUserId();
        return siteMessageManager.listByReceiveNotRead(userId);
    }
}
