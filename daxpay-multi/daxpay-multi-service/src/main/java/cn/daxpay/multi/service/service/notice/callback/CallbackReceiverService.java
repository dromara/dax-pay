package cn.daxpay.multi.service.service.notice.callback;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付/退款回调服务
 * @author xxm
 * @since 2024/7/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackReceiverService {

    /**
     * 信息处理
     */
    public String handle(HttpServletRequest request, String channel){

        return "";
    }
}
