package cn.daxpay.open.demo.artemis.controller;
import cn.daxpay.open.demo.artemis.service.DemoMessageStore;
import cn.daxpay.open.demo.artemis.result.DemoMessageResult;
import cn.daxpay.open.demo.artemis.param.SendDemoMessageParam;
import cn.daxpay.open.demo.artemis.model.DemoArtemisMessage;
import cn.daxpay.open.demo.artemis.constants.DemoArtemisConstants;
import cn.hutool.core.lang.UUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

/// # Artemis 消息队列演示接口
///
/// 演示 JMS 三类核心场景：点对点队列、发布订阅、延时消息。
/// 消费记录暂存于内存，前端通过 `/list` 轮询拉取。
///
/// 鉴权：URL 前缀 `/demo/**` 已在白名单，类上叠加 `@IgnoreAuth` 双保险。
@IgnoreAuth
@Validated
@Tag(name = "Artemis 消息队列演示")
@RestController
@RequestMapping("/demo/artemis")
@RequiredArgsConstructor
public class ArtemisDemoController {

    private final ArtemisTemplateService artemisTemplateService;
    private final DemoMessageStore store;

    /// 发送演示消息（按 scene 路由到对应 address）
    @Operation(summary = "发送演示消息")
    @PostMapping("/send")
    public Result<Void> send(@Validated @RequestBody SendDemoMessageParam param) {
        SendDemoMessageParam.SendScene scene = param.getScene();
        // 校验场景相关必填字段
        validateSceneParam(param, scene);

        // 构造消息体（公共字段）
        DemoArtemisMessage message = new DemoArtemisMessage()
                .setId(UUID.randomUUID().toString(true))
                .setContent(param.getContent())
                .setScene(scene.name())
                .setSendTime(OffsetDateTime.now());

        // 序列化为 JSON 字符串，发送层只负责搬运文本，不参与对象转换
        String json = JacksonUtil.toJson(message, false);

        switch (scene) {
            case QUEUE -> {
                // 点对点
                artemisTemplateService.send(DemoArtemisConstants.QUEUE, json);
            }
            case TOPIC -> {
                // 发布订阅：广播（必须走 sendTopic，否则 broker 端 multicast 地址会触发 ANYCAST 路由错误）
                artemisTemplateService.sendTopic(DemoArtemisConstants.TOPIC, json);
            }
            case DELAY -> {
                // 延时：调用 sendDelay（已校验 delaySeconds 非空）
                artemisTemplateService.sendDelay(
                        DemoArtemisConstants.DELAY_QUEUE, json, param.getDelaySeconds());
            }
        }
        return Res.ok();
    }

    /// 拉取最近的消费记录（最新在前）
    @Operation(summary = "拉取消费记录")
    @GetMapping("/list")
    public Result<List<DemoMessageResult>> list() {
        return Res.ok(store.list());
    }

    /// 清空全部消费记录
    @Operation(summary = "清空消费记录")
    @PostMapping("/clear")
    public Result<Void> clear() {
        store.clear();
        return Res.ok();
    }

    /// 校验与场景绑定的必填字段
    private void validateSceneParam(SendDemoMessageParam param, SendDemoMessageParam.SendScene scene) {
        if (scene == SendDemoMessageParam.SendScene.DELAY && param.getDelaySeconds() == null) {
            throw new BizInfoException("error.demo.delaySecondsRequired");
        }
    }
}
