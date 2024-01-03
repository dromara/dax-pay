package cn.bootx.platform.daxpay.code;

import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付同步状态
 *
 * @author xxm
 * @since 2021/4/21
 */
@Getter
@AllArgsConstructor
public enum PaySyncStatusEnum {

    NOT_SYNC("not_sync", "不需要同步"),
    PAY_SUCCESS("pay_success", "支付成功"),
    PAY_WAIT("pay_wait", "等待付款中"),
    CLOSED("closed", "已关闭"),
    REFUND("refund", "已退款"),
    NOT_FOUND("not_found", "未查询到订单"),
    /** 例如支付宝支付后, 客户未进行操作, 将不会创建出订单, 所以同步会返回未查询到订单 */
    IGNORE("ignore", "忽略"),
    /** 本地订单到了超时时间, 但是网关和本地都未关闭, 需要触发关闭相关处理 */
    TIMEOUT("timeout", "超时未关闭"),
    FAIL("fail", "查询失败");

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;

    public static PaySyncStatusEnum getByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("不存在的支付同步状态"));
    }
}
