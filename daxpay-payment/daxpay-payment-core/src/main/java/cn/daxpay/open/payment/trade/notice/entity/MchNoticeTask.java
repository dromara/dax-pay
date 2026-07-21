package cn.daxpay.open.payment.trade.notice.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.trade.notice.convert.MchNoticeTaskConvert;
import cn.daxpay.open.payment.trade.notice.result.MchNoticeTaskResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeContentModeEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeEventEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeProtocolEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeSourceEnum;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 商户出站通知任务
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("mch_notice_task")
public class MchNoticeTask extends MchBaseEntity implements ToResult<MchNoticeTaskResult> {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 业务主键ID
    private Long bizId;

    /// 业务单号
    private String bizNo;

    /// 通知事件码
    /// @see NoticeEventEnum
    private String event;

    /// 通知协议
    /// @see NoticeProtocolEnum
    private String protocol;

    /// URL 来源
    /// @see NoticeSourceEnum
    private String source;

    /// 内容策略
    /// @see NoticeContentModeEnum
    private String contentMode;

    /// 通知内容(快照或引用指针)
    private String content;

    /// 商户接收地址
    private String url;

    /// 是否发送成功
    private boolean success;

    /// 已发送次数
    private Integer sendCount;

    /// 延迟重试次数
    private Integer delayCount;

    /// 下次发送时间
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private OffsetDateTime nextTime;

    /// 最后发送时间
    private OffsetDateTime latestTime;

    /// 最近一次错误摘要
    private String errorMsg;

    /// 错误摘要截断
    public MchNoticeTask setErrorMsg(String errorMsg) {
        this.errorMsg = StrUtil.sub(errorMsg, 0, 300);
        return this;
    }

    @Override
    public MchNoticeTaskResult toResult() {
        return MchNoticeTaskConvert.CONVERT.toResult(this);
    }
}
