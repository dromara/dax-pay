package cn.bootx.platform.starter.dingtalk.core.robot.service;

import cn.bootx.platform.starter.dingtalk.param.notice.msg.Msg;
import cn.bootx.platform.starter.dingtalk.util.DingTalkUtil;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.starter.dingtalk.code.DingTalkCode;
import cn.bootx.platform.starter.dingtalk.core.robot.dao.DingRobotConfigManager;
import cn.bootx.platform.starter.dingtalk.core.robot.entity.DingRobotConfig;
import cn.bootx.platform.starter.dingtalk.core.base.result.DingTalkResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.bootx.platform.starter.dingtalk.code.DingTalkCode.SUCCESS_CODE;

/**
 * 钉钉机器人消息发送
 *
 * @author xxm
 * @since 2020/11/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DingRobotSendService {

    private final DingRobotConfigManager dingRobotConfigManager;

    /**
     * 发送钉钉机器人消息
     */
    public void sendNotice(String code, Msg body) {
        DingRobotConfig dingRobotConfig = dingRobotConfigManager.findByCode(code)
            .orElseThrow(() -> new DataNotExistException("钉钉机器人配置不存在"));
        long timestamp = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<>(3);
        map.put(DingTalkCode.ACCESS_TOKEN, dingRobotConfig.getAccessToken());
        String url;
        // 验签
        if (dingRobotConfig.isEnableSignatureCheck()) {
            url = DingTalkCode.ROBOT_SEND_SIGN_URL;
            map.put(DingTalkCode.SIGN, DingTalkUtil.generateSign(timestamp, dingRobotConfig.getSignSecret()));
            map.put(DingTalkCode.TIMESTAMP, timestamp);
        }
        else {
            url = DingTalkCode.ROBOT_SEND_NOT_SIGN_URL;
        }

        // 请求消息
        String responseBody = HttpUtil.createPost(StrUtil.format(url, map))
            .body(JacksonUtil.toJson(body))
            .execute()
            .body();
        DingTalkResult<?> dingTalkResult = JacksonUtil.toBean(responseBody, DingTalkResult.class);
        if (!Objects.equals(SUCCESS_CODE, dingTalkResult.getCode())) {
            log.error("钉钉机器人发送消息失败: {}", dingTalkResult.getMsg());
        }
    }

}
