package cn.daxpay.open.payment.device.qrcode.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 支付码牌
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "支付码牌")
public class DeviceQrCodeResult extends BaseResult {

    @Schema(description = "码牌编码")
    private String code;

    @Schema(description = "码牌名称")
    private String name;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "所属商户号(空表示未绑定)")
    private String mchNo;

    /// 商户名称(由 mchNo 翻译, 未绑定为空)
    @Trans(
            entity = MerchantInfo.class,
            source = MchBaseResult.Fields.mchNo,
            result = MerchantInfo.Fields.mchName)
    @Schema(description = "商户名称")
    private String mchName;

    @Schema(description = "关联应用号(空表示使用商户默认应用)")
    private String appId;

    /// 绑定门店号(可空; 对应 [MchStoreInfo#storeNo])
    @Schema(description = "绑定门店号(空表示未绑定门店)")
    private String storeNo;

    /// 门店名称(由 storeNo 翻译, 未绑定为空)
    @Trans(
            entity = MchStoreInfo.class,
            source = Fields.storeNo,
            result = MchStoreInfo.Fields.storeName)
    @Schema(description = "门店名称")
    private String storeName;

    /// 落地程序类型
    /// @see cn.daxpay.open.payment.device.enums.QrCodeProgramTypeEnum
    @Schema(description = "落地程序类型(h5/mini_app)")
    private String programType;

    /// 金额类型
    /// @see cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum
    @Schema(description = "金额类型(random-自定义/fixed-固定)")
    private String amountType;

    @Schema(description = "固定金额(分)")
    private Long fixedAmount;

    /// 状态
    /// @see cn.daxpay.open.payment.device.enums.QrCodeStatusEnum
    @Schema(description = "状态(enabled-启用/disabled-停用)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
