package cn.daxpay.multi.service.entity.notice.callback;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.service.common.entity.MchRecordEntity;
import cn.daxpay.multi.service.convert.notice.MerchantCallbackConvert;
import cn.daxpay.multi.service.enums.NoticeSendTypeEnum;
import cn.daxpay.multi.service.result.notice.callback.MerchantCallbackRecordResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 客户回调消息发送记录
 * @author xxm
 * @since 2024/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_merchant_callback_record")
public class MerchantCallbackRecord extends MchRecordEntity implements ToResult<MerchantCallbackRecordResult> {

    /** 任务ID */
    private Long taskId;

    /** 请求次数 */
    private Integer reqCount;

    /** 发送是否成功 */
    private boolean success;

    /**
     * 发送类型, 自动发送, 手动发送
     * @see NoticeSendTypeEnum
     */
    private String sendType;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    /**
     * 转换
     */
    @Override
    public MerchantCallbackRecordResult toResult() {
        return MerchantCallbackConvert.CONVERT.toResult(this);
    }
}
