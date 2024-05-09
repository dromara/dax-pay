package cn.bootx.platform.starter.wechat.core.user.service;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.starter.wechat.core.user.entity.WechatFans;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.bean.WxMpUserQuery;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信用户相关接口 (获取不到详细信息了)
 *
 * @author xxm
 * @since 2022/7/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatUserService {

    private final WxMpService wxMpService;

    private static final int SIZE = 100;

    /**
     * 获取关注用户列表
     */
    public void page(PageParam pageParam) {
        WxMpUserService userService = wxMpService.getUserService();
        WxMpUserQuery userQuery = new WxMpUserQuery();
    }

    /**
     * 同步粉丝
     */
    @Async("asyncExecutor")
    public void sync() {
        // 根据公众号查询已同步用户openid 查询最新的一条
    }

    /**
     * 获取微信用户 (获取不到昵称和头像了, 感觉没什么用了)
     * @param nextOpenid 下一组开始的openid
     */
    @SneakyThrows
    public void fetchUser(String nextOpenid) {
        WxMpUserList wxMpUserList = wxMpService.getUserService().userList(nextOpenid);
        // openId 分组 每组 100个 openid
        List<List<String>> openIdsList = CollUtil.split(wxMpUserList.getOpenids(), SIZE)
            .stream()
            .filter(CollUtil::isNotEmpty)
            .collect(Collectors.toList());
        // 处理每个分组. 调用查询用户信息
        for (List<String> openIdList : openIdsList) {
            log.info("开始批量获取用户信息 {}", openIdList);
            List<WechatFans> wxAccountFansList = new ArrayList<>();
            log.info("批量插入用户信息完成 {}", openIdList);
        }
        // 如果nextOpenId 不为空，则继续获取
    }

    /**
     * 构建对象
     */
    private WechatFans buildFans(WxMpUser wxMpUser) {
        return new WechatFans().setOpenid(wxMpUser.getOpenId())
            .setUnionId(wxMpUser.getUnionId())
            .setSubscribe(true)
            .setSubscribeTime(LocalDateTimeUtil.of(wxMpUser.getSubscribeTime()))
            .setLanguage(wxMpUser.getLanguage())
            .setRemark(wxMpUser.getRemark());
    }

}
