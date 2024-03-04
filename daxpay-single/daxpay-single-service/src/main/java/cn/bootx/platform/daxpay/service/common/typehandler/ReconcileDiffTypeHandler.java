package cn.bootx.platform.daxpay.service.common.typehandler;

import cn.bootx.platform.common.mybatisplus.handler.JacksonTypeReferenceHandler;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

/**
 * 对账差异内容类型处理器
 * @author xxm
 * @since 2024/3/4
 */
public class ReconcileDiffTypeHandler extends JacksonTypeReferenceHandler<List<ReconcileDiff>> {
    /**
     * 返回要反序列化的类型对象
     */
    @Override
    public TypeReference<List<ReconcileDiff>> getTypeReference() {
        return new TypeReference<List<ReconcileDiff>>() {};
    }
}
