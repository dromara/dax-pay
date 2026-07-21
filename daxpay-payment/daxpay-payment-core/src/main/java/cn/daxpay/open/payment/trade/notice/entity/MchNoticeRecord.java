package cn.daxpay.open.payment.trade.notice.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.trade.notice.convert.MchNoticeRecordConvert;
import cn.daxpay.open.payment.trade.notice.result.MchNoticeRecordResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeSendTypeEnum;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户出站通知发送记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("mch_notice_record")
public class MchNoticeRecord extends MchBaseEntity implements ToResult<MchNoticeRecordResult> {

    /// 通知任务ID
    private Long taskId;

    /// 本次对应的发送序号
    private Integer reqCount;

    /// 发送类型
    /// @see NoticeSendTypeEnum
    private String sendType;

    /// 是否成功
    private boolean success;

    /// HTTP 状态码
    private Integer httpStatus;

    /// 错误摘要
    private String errorMsg;

    /// 请求摘要(截断)
    private String requestDigest;

    /// 错误摘要截断
    public MchNoticeRecord setErrorMsg(String errorMsg) {
        this.errorMsg = StrUtil.sub(errorMsg, 0, 300);
        return this;
    }

    /// 请求摘要截断
    public MchNoticeRecord setRequestDigest(String requestDigest) {
        this.requestDigest = StrUtil.sub(requestDigest, 0, 500);
        return this;
    }

    @Override
    public MchNoticeRecordResult toResult() {
        return MchNoticeRecordConvert.CONVERT.toResult(this);
    }
}
