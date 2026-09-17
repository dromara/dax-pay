package cn.daxpay.open.platform.common.excel;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import org.apache.fesod.sheet.metadata.Head;
import org.apache.fesod.sheet.write.handler.SheetWriteHandler;
import org.apache.fesod.sheet.write.metadata.holder.WriteSheetHolder;
import org.apache.fesod.sheet.write.metadata.holder.WriteWorkbookHolder;

import java.util.List;

/// # Excel 表头国际化处理器
///
/// Fesod 注解式表头([ExcelProperty] 的 value 是编译期常量)无法在渲染时翻译,
/// 本处理器在 sheet 创建完成后、表头尚未写入 POI 前, 将表头元数据中的文本按当前请求语言解析:
/// 注解值即词条 key(如 `export.normal_order.orderNo`), 未命中词条时保留原文(兼容中文直写或无词条环境)。
///
/// 替换发生在 [Head] 元数据层而非 POI 单元格层, 列宽自适应等后续渲染仍基于翻译后的文本工作。
public class ExcelHeadI18nHandler implements SheetWriteHandler {

    /// 无状态处理器, 导出工具内复用单例
    public static final ExcelHeadI18nHandler INSTANCE = new ExcelHeadI18nHandler();

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        writeSheetHolder.getExcelWriteHeadProperty().getHeadMap()
                .values()
                .forEach(ExcelHeadI18nHandler::translateHead);
    }

    /// 将单个表头列的名称按词条 key 解析(多层表头逐层翻译)
    private static void translateHead(Head head) {
        List<String> headNameList = head.getHeadNameList();
        if (headNameList == null || headNameList.isEmpty()) {
            return;
        }
        // I18nUtil 未命中词条时原样返回, 中文直写表头(如 demo)行为不变
        head.setHeadNameList(headNameList.stream().map(I18nUtil::get).toList());
    }
}
