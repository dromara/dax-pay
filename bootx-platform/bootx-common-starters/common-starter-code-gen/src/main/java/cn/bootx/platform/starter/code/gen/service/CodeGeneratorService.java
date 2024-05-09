package cn.bootx.platform.starter.code.gen.service;

import cn.bootx.platform.common.core.code.CommonCode;
import cn.bootx.platform.starter.code.gen.code.CodeGenColumnTypeEnum;
import cn.bootx.platform.starter.code.gen.code.CodeGenTemplateVmEnum;
import cn.bootx.platform.starter.code.gen.domain.CodeGenData;
import cn.bootx.platform.starter.code.gen.domain.CodeGenData.CodeGenColumnData;
import cn.bootx.platform.starter.code.gen.dto.CodeGenPreview;
import cn.bootx.platform.starter.code.gen.entity.DatabaseColumn;
import cn.bootx.platform.starter.code.gen.entity.DatabaseTable;
import cn.bootx.platform.starter.code.gen.param.CodeGenParam;
import cn.bootx.platform.starter.code.gen.util.CodeGenUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成模板
 *
 * @author xxm
 * @since 2022/1/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    private final DatabaseTableService databaseTableService;

    /** 生成实体类时要过滤掉的字段(继承自基类) */
    private final List<String> entityFilterFields = Arrays.asList(CommonCode.ID, CommonCode.CREATOR,
            CommonCode.CREATE_TIME, CommonCode.LAST_MODIFIER, CommonCode.LAST_MODIFIED_TIME, CommonCode.VERSION,
            CommonCode.DELETED);

    // 初始化Velocity
    static {
        // 设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
    }

    /**
     * 生成实体类
     */
    public List<CodeGenPreview> codeGenPreview(CodeGenParam codeGenParam) {
        // 获取生成代码所用的数据
        Map<String, Object> map = this.getCodeGenInfo(codeGenParam);
        // 遍历生成代码预览
        return Arrays.stream(CodeGenTemplateVmEnum.values())
            .filter(o -> filterVue(o, codeGenParam.getVueVersion()))
            .map(vmEnum -> {
                VelocityContext context = new VelocityContext(map);
                StringWriter sw = new StringWriter();
                Template template = Velocity.getTemplate(vmEnum.getPath(), CharsetUtil.UTF_8);
                template.merge(context, sw);
                return new CodeGenPreview().setName(vmEnum.getName()).setContent(sw.toString());
            })
            .collect(Collectors.toList());
    }

    /**
     * 过滤vue文件
     */
    private boolean filterVue(CodeGenTemplateVmEnum vmEnum, String vueVersion) {
        if (Objects.equals(vueVersion, "v3")) {
            // vue3时不生成vue2的代码
            return !StrUtil.endWith(vmEnum.getName(), "v2");
        }
        else {
            // vue2时不生成vue3的代码
            return !StrUtil.endWith(vmEnum.getName(), "v3");
        }
    }

    /**
     * 获取生成代码所用的数据
     */
    private Map<String, Object> getCodeGenInfo(CodeGenParam codeGenParam) {
        DatabaseTable databaseTable = databaseTableService.findByTableName(codeGenParam.getTableName());
        List<DatabaseColumn> databaseColumns = databaseTableService.findColumnByTableName(codeGenParam.getTableName());

        // 数据库字段
        List<CodeGenColumnData> columns = databaseColumns.stream()
            .map(databaseColumn -> new CodeGenColumnData().setComments(databaseColumn.getColumnComment())
                .setJavaType(CodeGenColumnTypeEnum.convertJavaType(databaseColumn.getDataType()))
                .setTsType(CodeGenColumnTypeEnum.convertTsType(databaseColumn.getDataType()))
                .setName(NamingCase.toCamelCase(databaseColumn.getColumnName())))
            .filter(codeGenColumn -> !entityFilterFields.contains(codeGenColumn.getName()))
            .collect(Collectors.toList());

        CodeGenData codeGenData = new CodeGenData().setTableName(databaseTable.getTableName())
            .setEntityUpName(codeGenParam.getEntityName())
            .setEntityLowName(StrUtil.lowerFirst(codeGenParam.getEntityName()))
            .setEntityDashName(NamingCase.toKebabCase(codeGenParam.getEntityName()))
            .setBaseClass(codeGenParam.getBaseEntity())
            .setCorePack(codeGenParam.getCorePack())
            .setEditType(codeGenParam.getEditType())
            .setDeleteType(codeGenParam.getDeleteType())
            .setParamPack(codeGenParam.getParamPack())
            .setDtoPack(codeGenParam.getDtoPack())
            .setControllerPack(codeGenParam.getControllerPack())
            .setRequestPath(codeGenParam.getRequestPath())
            .setVueApiPath(codeGenParam.getVueApiPath())
            .setAuthor(codeGenParam.getAuthor())
            .setComments(databaseTable.getTableComment())
            .setColumns(columns);

        return BeanUtil.beanToMap(codeGenData, false, false);
    }

    /**
     * 生成代码文件压缩包
     */
    @SneakyThrows
    @DS("#codeGenParam.getDataSourceCode()")
    public ResponseEntity<byte[]> genCodeZip(CodeGenParam codeGenParam) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        // 将代码放入压缩包内
        for (CodeGenPreview codeGenPreview : this.codeGenPreview(codeGenParam)) {
            // 添加到zip
            CodeGenTemplateVmEnum vmEnum = CodeGenTemplateVmEnum.findByName(codeGenPreview.getName());
            String fileName = codeGenParam.getEntityName() + vmEnum.getFileSuffixName();
            // js后缀特殊处理
            if (vmEnum.getFileSuffixName().equals(".js") || vmEnum.getFileSuffixName().equals(".ts")) {
                fileName = StrUtil.lowerFirst(CodeGenUtil.tableToJava(fileName));
            }
            zip.putNextEntry(new ZipEntry(fileName));
            IOUtils.write(codeGenPreview.getContent(), zip, CharsetUtil.UTF_8);
            zip.flush();
            zip.closeEntry();
        }
        IoUtil.close(zip);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", new String(
                (codeGenParam.getTableName() + ".zip").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

}
