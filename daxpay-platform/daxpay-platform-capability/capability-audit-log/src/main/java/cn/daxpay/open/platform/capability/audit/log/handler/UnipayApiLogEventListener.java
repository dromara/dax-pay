package cn.daxpay.open.platform.capability.audit.log.handler;

import cn.daxpay.open.platform.capability.audit.log.param.UnipayApiLogParam;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpToRegionService;
import cn.daxpay.open.platform.capability.audit.log.service.log.UnipayApiLogService;
import cn.daxpay.open.platform.core.event.UnipayApiAccessLogEvent;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/// # 统一支付接口审计事件监听
///
/// 同步监听仅负责补全归属地并入队，禁止重业务逻辑。
@Slf4j
@Component
@RequiredArgsConstructor
public class UnipayApiLogEventListener {

    private final UnipayApiLogService unipayApiLogService;
    private final IpToRegionService ipToRegionService;

    @EventListener
    public void onUnipayApiAccess(UnipayApiAccessLogEvent event) {
        if (event == null) {
            return;
        }
        try {
            UnipayApiLogParam param = new UnipayApiLogParam()
                    .setMchNo(event.getMchNo())
                    .setReqId(event.getReqId())
                    .setApiPath(event.getApiPath())
                    .setApiTitle(event.getApiTitle())
                    .setRequestMethod(event.getRequestMethod())
                    .setClientIp(event.getClientIp())
                    .setRequestIp(event.getRequestIp())
                    .setSuccess(event.getSuccess())
                    .setErrorCode(event.getErrorCode())
                    .setErrorMsg(event.getErrorMsg())
                    .setDurationMs(event.getDurationMs())
                    .setTraceId(event.getTraceId())
                    .setReqParam(event.getReqParam())
                    .setResBody(event.getResBody())
                    .setOperateTime(event.getOperateTime());

            // IP 归属地
            if (StrUtil.isNotBlank(param.getRequestIp())) {
                try {
                    param.setRequestLocation(ipToRegionService.getRegionStrByIp(param.getRequestIp()));
                } catch (Exception e) {
                    param.setRequestLocation("未知");
                }
            }

            unipayApiLogService.add(param);
        } catch (Exception e) {
            log.warn("处理支付接口审计事件失败: {}", e.getMessage());
        }
    }
}
