package cn.daxpay.single.service.func;

import cn.daxpay.single.service.code.ReconcileFileTypeEnum;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 支付对账策略
 * @author xxm
 * @since 2024/1/18
 */
@Setter
@Getter
public abstract class AbsReconcileStrategy implements PayStrategy {

    /** 对账订单 */
    private ReconcileOrder recordOrder;

    /**
     * 对账前处理, 主要是初始化支付SDK配置
     */
    public void doBeforeHandler() {
    }

    /**
     * 上传对账单解析并保存
     */
    public abstract void upload(MultipartFile file, ReconcileFileTypeEnum fileType);

    /**
     * 下载对账单到本地进行保存
     *
     * return 保存的原始对账文件ID
     */
    public abstract void downAndSave();

}
