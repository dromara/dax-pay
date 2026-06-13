/*
 Navicat Premium Data Transfer

 Source Server         : 229本地服务
 Source Server Type    : PostgreSQL
 Source Server Version : 160009 (160009)
 Source Host           : 192.168.1.229:5432
 Source Catalog        : daxpay-dev
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 160009 (160009)
 File Encoding         : 65001

 Date: 13/06/2026 09:27:48
*/


-- ----------------------------
-- Sequence structure for mch_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."mch_user_id_seq";
CREATE SEQUENCE "public"."mch_user_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for alipay_mch_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."alipay_mch_app";
CREATE TABLE "public"."alipay_mch_app" (
  "id" int8 NOT NULL,
  "mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "ali_app_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "isv_no" varchar(64) COLLATE "pg_catalog"."default",
  "agent_no" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."alipay_mch_app"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_mch_app"."mch_no" IS '平台商户号';
COMMENT ON COLUMN "public"."alipay_mch_app"."channel_mch_no" IS '通道商户号，关联 mch_channel_merchant.channel_mch_no';
COMMENT ON COLUMN "public"."alipay_mch_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."alipay_mch_app"."ali_app_id" IS '支付宝开放平台应用 APPID';
COMMENT ON COLUMN "public"."alipay_mch_app"."isv_no" IS '服务商号（冗余，继承商户租户）';
COMMENT ON COLUMN "public"."alipay_mch_app"."agent_no" IS '代理商号（冗余，继承商户租户）';
COMMENT ON COLUMN "public"."alipay_mch_app"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."alipay_mch_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_mch_app"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."alipay_mch_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_mch_app"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_mch_app"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."alipay_mch_app" IS '支付宝直连通道商户应用表';

-- ----------------------------
-- Records of alipay_mch_app
-- ----------------------------
INSERT INTO "public"."alipay_mch_app" VALUES (2065322225874116608, 'M1777797520668', '111111', 'cs1', '123', 'ISV1215972714557722', 'AGENT1776138002345', 1, '2026-06-12 14:36:05.136018', 1, '2026-06-12 14:36:05.142534', 0, 'f');

-- ----------------------------
-- Table structure for base_area
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_area";
CREATE TABLE "public"."base_area" (
  "code" varchar(6) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(60) COLLATE "pg_catalog"."default" NOT NULL,
  "city_code" varchar(4) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."base_area"."name" IS '区域名称';
COMMENT ON COLUMN "public"."base_area"."city_code" IS '城市编码';
COMMENT ON TABLE "public"."base_area" IS '县区表';

-- ----------------------------
-- Records of base_area
-- ----------------------------
INSERT INTO "public"."base_area" VALUES ('110101', '东城区', '1101');
INSERT INTO "public"."base_area" VALUES ('110102', '西城区', '1101');
INSERT INTO "public"."base_area" VALUES ('110105', '朝阳区', '1101');
INSERT INTO "public"."base_area" VALUES ('110106', '丰台区', '1101');
INSERT INTO "public"."base_area" VALUES ('110107', '石景山区', '1101');
INSERT INTO "public"."base_area" VALUES ('110108', '海淀区', '1101');
INSERT INTO "public"."base_area" VALUES ('110109', '门头沟区', '1101');
INSERT INTO "public"."base_area" VALUES ('110111', '房山区', '1101');
INSERT INTO "public"."base_area" VALUES ('110112', '通州区', '1101');
INSERT INTO "public"."base_area" VALUES ('110113', '顺义区', '1101');
INSERT INTO "public"."base_area" VALUES ('110114', '昌平区', '1101');
INSERT INTO "public"."base_area" VALUES ('110115', '大兴区', '1101');
INSERT INTO "public"."base_area" VALUES ('110116', '怀柔区', '1101');
INSERT INTO "public"."base_area" VALUES ('110117', '平谷区', '1101');
INSERT INTO "public"."base_area" VALUES ('110118', '密云区', '1101');
INSERT INTO "public"."base_area" VALUES ('110119', '延庆区', '1101');
INSERT INTO "public"."base_area" VALUES ('120101', '和平区', '1201');
INSERT INTO "public"."base_area" VALUES ('120102', '河东区', '1201');
INSERT INTO "public"."base_area" VALUES ('120103', '河西区', '1201');
INSERT INTO "public"."base_area" VALUES ('120104', '南开区', '1201');
INSERT INTO "public"."base_area" VALUES ('120105', '河北区', '1201');
INSERT INTO "public"."base_area" VALUES ('120106', '红桥区', '1201');
INSERT INTO "public"."base_area" VALUES ('120110', '东丽区', '1201');
INSERT INTO "public"."base_area" VALUES ('120111', '西青区', '1201');
INSERT INTO "public"."base_area" VALUES ('120112', '津南区', '1201');
INSERT INTO "public"."base_area" VALUES ('120113', '北辰区', '1201');
INSERT INTO "public"."base_area" VALUES ('120114', '武清区', '1201');
INSERT INTO "public"."base_area" VALUES ('120115', '宝坻区', '1201');
INSERT INTO "public"."base_area" VALUES ('120116', '滨海新区', '1201');
INSERT INTO "public"."base_area" VALUES ('120117', '宁河区', '1201');
INSERT INTO "public"."base_area" VALUES ('120118', '静海区', '1201');
INSERT INTO "public"."base_area" VALUES ('120119', '蓟州区', '1201');
INSERT INTO "public"."base_area" VALUES ('130102', '长安区', '1301');
INSERT INTO "public"."base_area" VALUES ('130104', '桥西区', '1301');
INSERT INTO "public"."base_area" VALUES ('130105', '新华区', '1301');
INSERT INTO "public"."base_area" VALUES ('130107', '井陉矿区', '1301');
INSERT INTO "public"."base_area" VALUES ('130108', '裕华区', '1301');
INSERT INTO "public"."base_area" VALUES ('130109', '藁城区', '1301');
INSERT INTO "public"."base_area" VALUES ('130110', '鹿泉区', '1301');
INSERT INTO "public"."base_area" VALUES ('130111', '栾城区', '1301');
INSERT INTO "public"."base_area" VALUES ('130121', '井陉县', '1301');
INSERT INTO "public"."base_area" VALUES ('130123', '正定县', '1301');
INSERT INTO "public"."base_area" VALUES ('130125', '行唐县', '1301');
INSERT INTO "public"."base_area" VALUES ('130126', '灵寿县', '1301');
INSERT INTO "public"."base_area" VALUES ('130127', '高邑县', '1301');
INSERT INTO "public"."base_area" VALUES ('130128', '深泽县', '1301');
INSERT INTO "public"."base_area" VALUES ('130129', '赞皇县', '1301');
INSERT INTO "public"."base_area" VALUES ('130130', '无极县', '1301');
INSERT INTO "public"."base_area" VALUES ('130131', '平山县', '1301');
INSERT INTO "public"."base_area" VALUES ('130132', '元氏县', '1301');
INSERT INTO "public"."base_area" VALUES ('130133', '赵县', '1301');
INSERT INTO "public"."base_area" VALUES ('130171', '石家庄高新技术产业开发区', '1301');
INSERT INTO "public"."base_area" VALUES ('130172', '石家庄循环化工园区', '1301');
INSERT INTO "public"."base_area" VALUES ('130181', '辛集市', '1301');
INSERT INTO "public"."base_area" VALUES ('130183', '晋州市', '1301');
INSERT INTO "public"."base_area" VALUES ('130184', '新乐市', '1301');
INSERT INTO "public"."base_area" VALUES ('130202', '路南区', '1302');
INSERT INTO "public"."base_area" VALUES ('130203', '路北区', '1302');
INSERT INTO "public"."base_area" VALUES ('130204', '古冶区', '1302');
INSERT INTO "public"."base_area" VALUES ('130205', '开平区', '1302');
INSERT INTO "public"."base_area" VALUES ('130207', '丰南区', '1302');
INSERT INTO "public"."base_area" VALUES ('130208', '丰润区', '1302');
INSERT INTO "public"."base_area" VALUES ('130209', '曹妃甸区', '1302');
INSERT INTO "public"."base_area" VALUES ('130224', '滦南县', '1302');
INSERT INTO "public"."base_area" VALUES ('130225', '乐亭县', '1302');
INSERT INTO "public"."base_area" VALUES ('130227', '迁西县', '1302');
INSERT INTO "public"."base_area" VALUES ('130229', '玉田县', '1302');
INSERT INTO "public"."base_area" VALUES ('130271', '河北唐山芦台经济开发区', '1302');
INSERT INTO "public"."base_area" VALUES ('130272', '唐山市汉沽管理区', '1302');
INSERT INTO "public"."base_area" VALUES ('130273', '唐山高新技术产业开发区', '1302');
INSERT INTO "public"."base_area" VALUES ('130274', '河北唐山海港经济开发区', '1302');
INSERT INTO "public"."base_area" VALUES ('130281', '遵化市', '1302');
INSERT INTO "public"."base_area" VALUES ('130283', '迁安市', '1302');
INSERT INTO "public"."base_area" VALUES ('130284', '滦州市', '1302');
INSERT INTO "public"."base_area" VALUES ('130302', '海港区', '1303');
INSERT INTO "public"."base_area" VALUES ('130303', '山海关区', '1303');
INSERT INTO "public"."base_area" VALUES ('130304', '北戴河区', '1303');
INSERT INTO "public"."base_area" VALUES ('130306', '抚宁区', '1303');
INSERT INTO "public"."base_area" VALUES ('130321', '青龙满族自治县', '1303');
INSERT INTO "public"."base_area" VALUES ('130322', '昌黎县', '1303');
INSERT INTO "public"."base_area" VALUES ('130324', '卢龙县', '1303');
INSERT INTO "public"."base_area" VALUES ('130371', '秦皇岛市经济技术开发区', '1303');
INSERT INTO "public"."base_area" VALUES ('130372', '北戴河新区', '1303');
INSERT INTO "public"."base_area" VALUES ('130402', '邯山区', '1304');
INSERT INTO "public"."base_area" VALUES ('130403', '丛台区', '1304');
INSERT INTO "public"."base_area" VALUES ('130404', '复兴区', '1304');
INSERT INTO "public"."base_area" VALUES ('130406', '峰峰矿区', '1304');
INSERT INTO "public"."base_area" VALUES ('130407', '肥乡区', '1304');
INSERT INTO "public"."base_area" VALUES ('130408', '永年区', '1304');
INSERT INTO "public"."base_area" VALUES ('130423', '临漳县', '1304');
INSERT INTO "public"."base_area" VALUES ('130424', '成安县', '1304');
INSERT INTO "public"."base_area" VALUES ('130425', '大名县', '1304');
INSERT INTO "public"."base_area" VALUES ('130426', '涉县', '1304');
INSERT INTO "public"."base_area" VALUES ('130427', '磁县', '1304');
INSERT INTO "public"."base_area" VALUES ('130430', '邱县', '1304');
INSERT INTO "public"."base_area" VALUES ('130431', '鸡泽县', '1304');
INSERT INTO "public"."base_area" VALUES ('130432', '广平县', '1304');
INSERT INTO "public"."base_area" VALUES ('130433', '馆陶县', '1304');
INSERT INTO "public"."base_area" VALUES ('130434', '魏县', '1304');
INSERT INTO "public"."base_area" VALUES ('130435', '曲周县', '1304');
INSERT INTO "public"."base_area" VALUES ('130471', '邯郸经济技术开发区', '1304');
INSERT INTO "public"."base_area" VALUES ('130473', '邯郸冀南新区', '1304');
INSERT INTO "public"."base_area" VALUES ('130481', '武安市', '1304');
INSERT INTO "public"."base_area" VALUES ('130502', '襄都区', '1305');
INSERT INTO "public"."base_area" VALUES ('130503', '信都区', '1305');
INSERT INTO "public"."base_area" VALUES ('130505', '任泽区', '1305');
INSERT INTO "public"."base_area" VALUES ('130506', '南和区', '1305');
INSERT INTO "public"."base_area" VALUES ('130522', '临城县', '1305');
INSERT INTO "public"."base_area" VALUES ('130523', '内丘县', '1305');
INSERT INTO "public"."base_area" VALUES ('130524', '柏乡县', '1305');
INSERT INTO "public"."base_area" VALUES ('130525', '隆尧县', '1305');
INSERT INTO "public"."base_area" VALUES ('130528', '宁晋县', '1305');
INSERT INTO "public"."base_area" VALUES ('130529', '巨鹿县', '1305');
INSERT INTO "public"."base_area" VALUES ('130530', '新河县', '1305');
INSERT INTO "public"."base_area" VALUES ('130531', '广宗县', '1305');
INSERT INTO "public"."base_area" VALUES ('130532', '平乡县', '1305');
INSERT INTO "public"."base_area" VALUES ('130533', '威县', '1305');
INSERT INTO "public"."base_area" VALUES ('130534', '清河县', '1305');
INSERT INTO "public"."base_area" VALUES ('130535', '临西县', '1305');
INSERT INTO "public"."base_area" VALUES ('130571', '河北邢台经济开发区', '1305');
INSERT INTO "public"."base_area" VALUES ('130581', '南宫市', '1305');
INSERT INTO "public"."base_area" VALUES ('130582', '沙河市', '1305');
INSERT INTO "public"."base_area" VALUES ('130602', '竞秀区', '1306');
INSERT INTO "public"."base_area" VALUES ('130606', '莲池区', '1306');
INSERT INTO "public"."base_area" VALUES ('130607', '满城区', '1306');
INSERT INTO "public"."base_area" VALUES ('130608', '清苑区', '1306');
INSERT INTO "public"."base_area" VALUES ('130609', '徐水区', '1306');
INSERT INTO "public"."base_area" VALUES ('130623', '涞水县', '1306');
INSERT INTO "public"."base_area" VALUES ('130624', '阜平县', '1306');
INSERT INTO "public"."base_area" VALUES ('130626', '定兴县', '1306');
INSERT INTO "public"."base_area" VALUES ('130627', '唐县', '1306');
INSERT INTO "public"."base_area" VALUES ('130628', '高阳县', '1306');
INSERT INTO "public"."base_area" VALUES ('130629', '容城县', '1306');
INSERT INTO "public"."base_area" VALUES ('130630', '涞源县', '1306');
INSERT INTO "public"."base_area" VALUES ('130631', '望都县', '1306');
INSERT INTO "public"."base_area" VALUES ('130632', '安新县', '1306');
INSERT INTO "public"."base_area" VALUES ('130633', '易县', '1306');
INSERT INTO "public"."base_area" VALUES ('130634', '曲阳县', '1306');
INSERT INTO "public"."base_area" VALUES ('130635', '蠡县', '1306');
INSERT INTO "public"."base_area" VALUES ('130636', '顺平县', '1306');
INSERT INTO "public"."base_area" VALUES ('130637', '博野县', '1306');
INSERT INTO "public"."base_area" VALUES ('130638', '雄县', '1306');
INSERT INTO "public"."base_area" VALUES ('130671', '保定高新技术产业开发区', '1306');
INSERT INTO "public"."base_area" VALUES ('130672', '保定白沟新城', '1306');
INSERT INTO "public"."base_area" VALUES ('130681', '涿州市', '1306');
INSERT INTO "public"."base_area" VALUES ('130682', '定州市', '1306');
INSERT INTO "public"."base_area" VALUES ('130683', '安国市', '1306');
INSERT INTO "public"."base_area" VALUES ('130684', '高碑店市', '1306');
INSERT INTO "public"."base_area" VALUES ('130702', '桥东区', '1307');
INSERT INTO "public"."base_area" VALUES ('130703', '桥西区', '1307');
INSERT INTO "public"."base_area" VALUES ('130705', '宣化区', '1307');
INSERT INTO "public"."base_area" VALUES ('130706', '下花园区', '1307');
INSERT INTO "public"."base_area" VALUES ('130708', '万全区', '1307');
INSERT INTO "public"."base_area" VALUES ('130709', '崇礼区', '1307');
INSERT INTO "public"."base_area" VALUES ('130722', '张北县', '1307');
INSERT INTO "public"."base_area" VALUES ('130723', '康保县', '1307');
INSERT INTO "public"."base_area" VALUES ('130724', '沽源县', '1307');
INSERT INTO "public"."base_area" VALUES ('130725', '尚义县', '1307');
INSERT INTO "public"."base_area" VALUES ('130726', '蔚县', '1307');
INSERT INTO "public"."base_area" VALUES ('130727', '阳原县', '1307');
INSERT INTO "public"."base_area" VALUES ('130728', '怀安县', '1307');
INSERT INTO "public"."base_area" VALUES ('130730', '怀来县', '1307');
INSERT INTO "public"."base_area" VALUES ('130731', '涿鹿县', '1307');
INSERT INTO "public"."base_area" VALUES ('130732', '赤城县', '1307');
INSERT INTO "public"."base_area" VALUES ('130771', '张家口经济开发区', '1307');
INSERT INTO "public"."base_area" VALUES ('130772', '张家口市察北管理区', '1307');
INSERT INTO "public"."base_area" VALUES ('130773', '张家口市塞北管理区', '1307');
INSERT INTO "public"."base_area" VALUES ('130802', '双桥区', '1308');
INSERT INTO "public"."base_area" VALUES ('130803', '双滦区', '1308');
INSERT INTO "public"."base_area" VALUES ('130804', '鹰手营子矿区', '1308');
INSERT INTO "public"."base_area" VALUES ('130821', '承德县', '1308');
INSERT INTO "public"."base_area" VALUES ('130822', '兴隆县', '1308');
INSERT INTO "public"."base_area" VALUES ('130824', '滦平县', '1308');
INSERT INTO "public"."base_area" VALUES ('130825', '隆化县', '1308');
INSERT INTO "public"."base_area" VALUES ('130826', '丰宁满族自治县', '1308');
INSERT INTO "public"."base_area" VALUES ('130827', '宽城满族自治县', '1308');
INSERT INTO "public"."base_area" VALUES ('130828', '围场满族蒙古族自治县', '1308');
INSERT INTO "public"."base_area" VALUES ('130871', '承德高新技术产业开发区', '1308');
INSERT INTO "public"."base_area" VALUES ('130881', '平泉市', '1308');
INSERT INTO "public"."base_area" VALUES ('130902', '新华区', '1309');
INSERT INTO "public"."base_area" VALUES ('130903', '运河区', '1309');
INSERT INTO "public"."base_area" VALUES ('130921', '沧县', '1309');
INSERT INTO "public"."base_area" VALUES ('130922', '青县', '1309');
INSERT INTO "public"."base_area" VALUES ('130923', '东光县', '1309');
INSERT INTO "public"."base_area" VALUES ('130924', '海兴县', '1309');
INSERT INTO "public"."base_area" VALUES ('130925', '盐山县', '1309');
INSERT INTO "public"."base_area" VALUES ('130926', '肃宁县', '1309');
INSERT INTO "public"."base_area" VALUES ('130927', '南皮县', '1309');
INSERT INTO "public"."base_area" VALUES ('130928', '吴桥县', '1309');
INSERT INTO "public"."base_area" VALUES ('130929', '献县', '1309');
INSERT INTO "public"."base_area" VALUES ('130930', '孟村回族自治县', '1309');
INSERT INTO "public"."base_area" VALUES ('130971', '河北沧州经济开发区', '1309');
INSERT INTO "public"."base_area" VALUES ('130972', '沧州高新技术产业开发区', '1309');
INSERT INTO "public"."base_area" VALUES ('130973', '沧州渤海新区', '1309');
INSERT INTO "public"."base_area" VALUES ('130981', '泊头市', '1309');
INSERT INTO "public"."base_area" VALUES ('130982', '任丘市', '1309');
INSERT INTO "public"."base_area" VALUES ('130983', '黄骅市', '1309');
INSERT INTO "public"."base_area" VALUES ('130984', '河间市', '1309');
INSERT INTO "public"."base_area" VALUES ('131002', '安次区', '1310');
INSERT INTO "public"."base_area" VALUES ('131003', '广阳区', '1310');
INSERT INTO "public"."base_area" VALUES ('131022', '固安县', '1310');
INSERT INTO "public"."base_area" VALUES ('131023', '永清县', '1310');
INSERT INTO "public"."base_area" VALUES ('131024', '香河县', '1310');
INSERT INTO "public"."base_area" VALUES ('131025', '大城县', '1310');
INSERT INTO "public"."base_area" VALUES ('131026', '文安县', '1310');
INSERT INTO "public"."base_area" VALUES ('131028', '大厂回族自治县', '1310');
INSERT INTO "public"."base_area" VALUES ('131071', '廊坊经济技术开发区', '1310');
INSERT INTO "public"."base_area" VALUES ('131081', '霸州市', '1310');
INSERT INTO "public"."base_area" VALUES ('131082', '三河市', '1310');
INSERT INTO "public"."base_area" VALUES ('131102', '桃城区', '1311');
INSERT INTO "public"."base_area" VALUES ('131103', '冀州区', '1311');
INSERT INTO "public"."base_area" VALUES ('131121', '枣强县', '1311');
INSERT INTO "public"."base_area" VALUES ('131122', '武邑县', '1311');
INSERT INTO "public"."base_area" VALUES ('131123', '武强县', '1311');
INSERT INTO "public"."base_area" VALUES ('131124', '饶阳县', '1311');
INSERT INTO "public"."base_area" VALUES ('131125', '安平县', '1311');
INSERT INTO "public"."base_area" VALUES ('131126', '故城县', '1311');
INSERT INTO "public"."base_area" VALUES ('131127', '景县', '1311');
INSERT INTO "public"."base_area" VALUES ('131128', '阜城县', '1311');
INSERT INTO "public"."base_area" VALUES ('131171', '河北衡水高新技术产业开发区', '1311');
INSERT INTO "public"."base_area" VALUES ('131172', '衡水滨湖新区', '1311');
INSERT INTO "public"."base_area" VALUES ('131182', '深州市', '1311');
INSERT INTO "public"."base_area" VALUES ('140105', '小店区', '1401');
INSERT INTO "public"."base_area" VALUES ('140106', '迎泽区', '1401');
INSERT INTO "public"."base_area" VALUES ('140107', '杏花岭区', '1401');
INSERT INTO "public"."base_area" VALUES ('140108', '尖草坪区', '1401');
INSERT INTO "public"."base_area" VALUES ('140109', '万柏林区', '1401');
INSERT INTO "public"."base_area" VALUES ('140110', '晋源区', '1401');
INSERT INTO "public"."base_area" VALUES ('140121', '清徐县', '1401');
INSERT INTO "public"."base_area" VALUES ('140122', '阳曲县', '1401');
INSERT INTO "public"."base_area" VALUES ('140123', '娄烦县', '1401');
INSERT INTO "public"."base_area" VALUES ('140171', '山西转型综合改革示范区', '1401');
INSERT INTO "public"."base_area" VALUES ('140181', '古交市', '1401');
INSERT INTO "public"."base_area" VALUES ('140212', '新荣区', '1402');
INSERT INTO "public"."base_area" VALUES ('140213', '平城区', '1402');
INSERT INTO "public"."base_area" VALUES ('140214', '云冈区', '1402');
INSERT INTO "public"."base_area" VALUES ('140215', '云州区', '1402');
INSERT INTO "public"."base_area" VALUES ('140221', '阳高县', '1402');
INSERT INTO "public"."base_area" VALUES ('140222', '天镇县', '1402');
INSERT INTO "public"."base_area" VALUES ('140223', '广灵县', '1402');
INSERT INTO "public"."base_area" VALUES ('140224', '灵丘县', '1402');
INSERT INTO "public"."base_area" VALUES ('140225', '浑源县', '1402');
INSERT INTO "public"."base_area" VALUES ('140226', '左云县', '1402');
INSERT INTO "public"."base_area" VALUES ('140271', '山西大同经济开发区', '1402');
INSERT INTO "public"."base_area" VALUES ('140302', '城区', '1403');
INSERT INTO "public"."base_area" VALUES ('140303', '矿区', '1403');
INSERT INTO "public"."base_area" VALUES ('140311', '郊区', '1403');
INSERT INTO "public"."base_area" VALUES ('140321', '平定县', '1403');
INSERT INTO "public"."base_area" VALUES ('140322', '盂县', '1403');
INSERT INTO "public"."base_area" VALUES ('140403', '潞州区', '1404');
INSERT INTO "public"."base_area" VALUES ('140404', '上党区', '1404');
INSERT INTO "public"."base_area" VALUES ('140405', '屯留区', '1404');
INSERT INTO "public"."base_area" VALUES ('140406', '潞城区', '1404');
INSERT INTO "public"."base_area" VALUES ('140423', '襄垣县', '1404');
INSERT INTO "public"."base_area" VALUES ('140425', '平顺县', '1404');
INSERT INTO "public"."base_area" VALUES ('140426', '黎城县', '1404');
INSERT INTO "public"."base_area" VALUES ('140427', '壶关县', '1404');
INSERT INTO "public"."base_area" VALUES ('140428', '长子县', '1404');
INSERT INTO "public"."base_area" VALUES ('140429', '武乡县', '1404');
INSERT INTO "public"."base_area" VALUES ('140430', '沁县', '1404');
INSERT INTO "public"."base_area" VALUES ('140431', '沁源县', '1404');
INSERT INTO "public"."base_area" VALUES ('140471', '山西长治高新技术产业园区', '1404');
INSERT INTO "public"."base_area" VALUES ('140502', '城区', '1405');
INSERT INTO "public"."base_area" VALUES ('140521', '沁水县', '1405');
INSERT INTO "public"."base_area" VALUES ('140522', '阳城县', '1405');
INSERT INTO "public"."base_area" VALUES ('140524', '陵川县', '1405');
INSERT INTO "public"."base_area" VALUES ('140525', '泽州县', '1405');
INSERT INTO "public"."base_area" VALUES ('140581', '高平市', '1405');
INSERT INTO "public"."base_area" VALUES ('140602', '朔城区', '1406');
INSERT INTO "public"."base_area" VALUES ('140603', '平鲁区', '1406');
INSERT INTO "public"."base_area" VALUES ('140621', '山阴县', '1406');
INSERT INTO "public"."base_area" VALUES ('140622', '应县', '1406');
INSERT INTO "public"."base_area" VALUES ('140623', '右玉县', '1406');
INSERT INTO "public"."base_area" VALUES ('140671', '山西朔州经济开发区', '1406');
INSERT INTO "public"."base_area" VALUES ('140681', '怀仁市', '1406');
INSERT INTO "public"."base_area" VALUES ('140702', '榆次区', '1407');
INSERT INTO "public"."base_area" VALUES ('140703', '太谷区', '1407');
INSERT INTO "public"."base_area" VALUES ('140721', '榆社县', '1407');
INSERT INTO "public"."base_area" VALUES ('140722', '左权县', '1407');
INSERT INTO "public"."base_area" VALUES ('140723', '和顺县', '1407');
INSERT INTO "public"."base_area" VALUES ('140724', '昔阳县', '1407');
INSERT INTO "public"."base_area" VALUES ('140725', '寿阳县', '1407');
INSERT INTO "public"."base_area" VALUES ('140727', '祁县', '1407');
INSERT INTO "public"."base_area" VALUES ('140728', '平遥县', '1407');
INSERT INTO "public"."base_area" VALUES ('140729', '灵石县', '1407');
INSERT INTO "public"."base_area" VALUES ('140781', '介休市', '1407');
INSERT INTO "public"."base_area" VALUES ('140802', '盐湖区', '1408');
INSERT INTO "public"."base_area" VALUES ('140821', '临猗县', '1408');
INSERT INTO "public"."base_area" VALUES ('140822', '万荣县', '1408');
INSERT INTO "public"."base_area" VALUES ('140823', '闻喜县', '1408');
INSERT INTO "public"."base_area" VALUES ('140824', '稷山县', '1408');
INSERT INTO "public"."base_area" VALUES ('140825', '新绛县', '1408');
INSERT INTO "public"."base_area" VALUES ('140826', '绛县', '1408');
INSERT INTO "public"."base_area" VALUES ('140827', '垣曲县', '1408');
INSERT INTO "public"."base_area" VALUES ('140828', '夏县', '1408');
INSERT INTO "public"."base_area" VALUES ('140829', '平陆县', '1408');
INSERT INTO "public"."base_area" VALUES ('140830', '芮城县', '1408');
INSERT INTO "public"."base_area" VALUES ('140881', '永济市', '1408');
INSERT INTO "public"."base_area" VALUES ('140882', '河津市', '1408');
INSERT INTO "public"."base_area" VALUES ('140902', '忻府区', '1409');
INSERT INTO "public"."base_area" VALUES ('140921', '定襄县', '1409');
INSERT INTO "public"."base_area" VALUES ('140922', '五台县', '1409');
INSERT INTO "public"."base_area" VALUES ('140923', '代县', '1409');
INSERT INTO "public"."base_area" VALUES ('140924', '繁峙县', '1409');
INSERT INTO "public"."base_area" VALUES ('140925', '宁武县', '1409');
INSERT INTO "public"."base_area" VALUES ('140926', '静乐县', '1409');
INSERT INTO "public"."base_area" VALUES ('140927', '神池县', '1409');
INSERT INTO "public"."base_area" VALUES ('140928', '五寨县', '1409');
INSERT INTO "public"."base_area" VALUES ('140929', '岢岚县', '1409');
INSERT INTO "public"."base_area" VALUES ('140930', '河曲县', '1409');
INSERT INTO "public"."base_area" VALUES ('140931', '保德县', '1409');
INSERT INTO "public"."base_area" VALUES ('140932', '偏关县', '1409');
INSERT INTO "public"."base_area" VALUES ('140971', '五台山风景名胜区', '1409');
INSERT INTO "public"."base_area" VALUES ('140981', '原平市', '1409');
INSERT INTO "public"."base_area" VALUES ('141002', '尧都区', '1410');
INSERT INTO "public"."base_area" VALUES ('141021', '曲沃县', '1410');
INSERT INTO "public"."base_area" VALUES ('141022', '翼城县', '1410');
INSERT INTO "public"."base_area" VALUES ('141023', '襄汾县', '1410');
INSERT INTO "public"."base_area" VALUES ('141024', '洪洞县', '1410');
INSERT INTO "public"."base_area" VALUES ('141025', '古县', '1410');
INSERT INTO "public"."base_area" VALUES ('141026', '安泽县', '1410');
INSERT INTO "public"."base_area" VALUES ('141027', '浮山县', '1410');
INSERT INTO "public"."base_area" VALUES ('141028', '吉县', '1410');
INSERT INTO "public"."base_area" VALUES ('141029', '乡宁县', '1410');
INSERT INTO "public"."base_area" VALUES ('141030', '大宁县', '1410');
INSERT INTO "public"."base_area" VALUES ('141031', '隰县', '1410');
INSERT INTO "public"."base_area" VALUES ('141032', '永和县', '1410');
INSERT INTO "public"."base_area" VALUES ('141033', '蒲县', '1410');
INSERT INTO "public"."base_area" VALUES ('141034', '汾西县', '1410');
INSERT INTO "public"."base_area" VALUES ('141081', '侯马市', '1410');
INSERT INTO "public"."base_area" VALUES ('141082', '霍州市', '1410');
INSERT INTO "public"."base_area" VALUES ('141102', '离石区', '1411');
INSERT INTO "public"."base_area" VALUES ('141121', '文水县', '1411');
INSERT INTO "public"."base_area" VALUES ('141122', '交城县', '1411');
INSERT INTO "public"."base_area" VALUES ('141123', '兴县', '1411');
INSERT INTO "public"."base_area" VALUES ('141124', '临县', '1411');
INSERT INTO "public"."base_area" VALUES ('141125', '柳林县', '1411');
INSERT INTO "public"."base_area" VALUES ('141126', '石楼县', '1411');
INSERT INTO "public"."base_area" VALUES ('141127', '岚县', '1411');
INSERT INTO "public"."base_area" VALUES ('141128', '方山县', '1411');
INSERT INTO "public"."base_area" VALUES ('141129', '中阳县', '1411');
INSERT INTO "public"."base_area" VALUES ('141130', '交口县', '1411');
INSERT INTO "public"."base_area" VALUES ('141181', '孝义市', '1411');
INSERT INTO "public"."base_area" VALUES ('141182', '汾阳市', '1411');
INSERT INTO "public"."base_area" VALUES ('150102', '新城区', '1501');
INSERT INTO "public"."base_area" VALUES ('150103', '回民区', '1501');
INSERT INTO "public"."base_area" VALUES ('150104', '玉泉区', '1501');
INSERT INTO "public"."base_area" VALUES ('150105', '赛罕区', '1501');
INSERT INTO "public"."base_area" VALUES ('150121', '土默特左旗', '1501');
INSERT INTO "public"."base_area" VALUES ('150122', '托克托县', '1501');
INSERT INTO "public"."base_area" VALUES ('150123', '和林格尔县', '1501');
INSERT INTO "public"."base_area" VALUES ('150124', '清水河县', '1501');
INSERT INTO "public"."base_area" VALUES ('150125', '武川县', '1501');
INSERT INTO "public"."base_area" VALUES ('150172', '呼和浩特经济技术开发区', '1501');
INSERT INTO "public"."base_area" VALUES ('150202', '东河区', '1502');
INSERT INTO "public"."base_area" VALUES ('150203', '昆都仑区', '1502');
INSERT INTO "public"."base_area" VALUES ('150204', '青山区', '1502');
INSERT INTO "public"."base_area" VALUES ('150205', '石拐区', '1502');
INSERT INTO "public"."base_area" VALUES ('150206', '白云鄂博矿区', '1502');
INSERT INTO "public"."base_area" VALUES ('150207', '九原区', '1502');
INSERT INTO "public"."base_area" VALUES ('150221', '土默特右旗', '1502');
INSERT INTO "public"."base_area" VALUES ('150222', '固阳县', '1502');
INSERT INTO "public"."base_area" VALUES ('150223', '达尔罕茂明安联合旗', '1502');
INSERT INTO "public"."base_area" VALUES ('150271', '包头稀土高新技术产业开发区', '1502');
INSERT INTO "public"."base_area" VALUES ('150302', '海勃湾区', '1503');
INSERT INTO "public"."base_area" VALUES ('150303', '海南区', '1503');
INSERT INTO "public"."base_area" VALUES ('150304', '乌达区', '1503');
INSERT INTO "public"."base_area" VALUES ('150402', '红山区', '1504');
INSERT INTO "public"."base_area" VALUES ('150403', '元宝山区', '1504');
INSERT INTO "public"."base_area" VALUES ('150404', '松山区', '1504');
INSERT INTO "public"."base_area" VALUES ('150421', '阿鲁科尔沁旗', '1504');
INSERT INTO "public"."base_area" VALUES ('150422', '巴林左旗', '1504');
INSERT INTO "public"."base_area" VALUES ('150423', '巴林右旗', '1504');
INSERT INTO "public"."base_area" VALUES ('150424', '林西县', '1504');
INSERT INTO "public"."base_area" VALUES ('150425', '克什克腾旗', '1504');
INSERT INTO "public"."base_area" VALUES ('150426', '翁牛特旗', '1504');
INSERT INTO "public"."base_area" VALUES ('150428', '喀喇沁旗', '1504');
INSERT INTO "public"."base_area" VALUES ('150429', '宁城县', '1504');
INSERT INTO "public"."base_area" VALUES ('150430', '敖汉旗', '1504');
INSERT INTO "public"."base_area" VALUES ('150502', '科尔沁区', '1505');
INSERT INTO "public"."base_area" VALUES ('150521', '科尔沁左翼中旗', '1505');
INSERT INTO "public"."base_area" VALUES ('150522', '科尔沁左翼后旗', '1505');
INSERT INTO "public"."base_area" VALUES ('150523', '开鲁县', '1505');
INSERT INTO "public"."base_area" VALUES ('150524', '库伦旗', '1505');
INSERT INTO "public"."base_area" VALUES ('150525', '奈曼旗', '1505');
INSERT INTO "public"."base_area" VALUES ('150526', '扎鲁特旗', '1505');
INSERT INTO "public"."base_area" VALUES ('150571', '通辽经济技术开发区', '1505');
INSERT INTO "public"."base_area" VALUES ('150581', '霍林郭勒市', '1505');
INSERT INTO "public"."base_area" VALUES ('150602', '东胜区', '1506');
INSERT INTO "public"."base_area" VALUES ('150603', '康巴什区', '1506');
INSERT INTO "public"."base_area" VALUES ('150621', '达拉特旗', '1506');
INSERT INTO "public"."base_area" VALUES ('150622', '准格尔旗', '1506');
INSERT INTO "public"."base_area" VALUES ('150623', '鄂托克前旗', '1506');
INSERT INTO "public"."base_area" VALUES ('150624', '鄂托克旗', '1506');
INSERT INTO "public"."base_area" VALUES ('150625', '杭锦旗', '1506');
INSERT INTO "public"."base_area" VALUES ('150626', '乌审旗', '1506');
INSERT INTO "public"."base_area" VALUES ('150627', '伊金霍洛旗', '1506');
INSERT INTO "public"."base_area" VALUES ('150702', '海拉尔区', '1507');
INSERT INTO "public"."base_area" VALUES ('150703', '扎赉诺尔区', '1507');
INSERT INTO "public"."base_area" VALUES ('150721', '阿荣旗', '1507');
INSERT INTO "public"."base_area" VALUES ('150722', '莫力达瓦达斡尔族自治旗', '1507');
INSERT INTO "public"."base_area" VALUES ('150723', '鄂伦春自治旗', '1507');
INSERT INTO "public"."base_area" VALUES ('150724', '鄂温克族自治旗', '1507');
INSERT INTO "public"."base_area" VALUES ('150725', '陈巴尔虎旗', '1507');
INSERT INTO "public"."base_area" VALUES ('150726', '新巴尔虎左旗', '1507');
INSERT INTO "public"."base_area" VALUES ('150727', '新巴尔虎右旗', '1507');
INSERT INTO "public"."base_area" VALUES ('150781', '满洲里市', '1507');
INSERT INTO "public"."base_area" VALUES ('150782', '牙克石市', '1507');
INSERT INTO "public"."base_area" VALUES ('150783', '扎兰屯市', '1507');
INSERT INTO "public"."base_area" VALUES ('150784', '额尔古纳市', '1507');
INSERT INTO "public"."base_area" VALUES ('150785', '根河市', '1507');
INSERT INTO "public"."base_area" VALUES ('150802', '临河区', '1508');
INSERT INTO "public"."base_area" VALUES ('150821', '五原县', '1508');
INSERT INTO "public"."base_area" VALUES ('150822', '磴口县', '1508');
INSERT INTO "public"."base_area" VALUES ('150823', '乌拉特前旗', '1508');
INSERT INTO "public"."base_area" VALUES ('150824', '乌拉特中旗', '1508');
INSERT INTO "public"."base_area" VALUES ('150825', '乌拉特后旗', '1508');
INSERT INTO "public"."base_area" VALUES ('150826', '杭锦后旗', '1508');
INSERT INTO "public"."base_area" VALUES ('150902', '集宁区', '1509');
INSERT INTO "public"."base_area" VALUES ('150921', '卓资县', '1509');
INSERT INTO "public"."base_area" VALUES ('150922', '化德县', '1509');
INSERT INTO "public"."base_area" VALUES ('150923', '商都县', '1509');
INSERT INTO "public"."base_area" VALUES ('150924', '兴和县', '1509');
INSERT INTO "public"."base_area" VALUES ('150925', '凉城县', '1509');
INSERT INTO "public"."base_area" VALUES ('150926', '察哈尔右翼前旗', '1509');
INSERT INTO "public"."base_area" VALUES ('150927', '察哈尔右翼中旗', '1509');
INSERT INTO "public"."base_area" VALUES ('150928', '察哈尔右翼后旗', '1509');
INSERT INTO "public"."base_area" VALUES ('150929', '四子王旗', '1509');
INSERT INTO "public"."base_area" VALUES ('150981', '丰镇市', '1509');
INSERT INTO "public"."base_area" VALUES ('152201', '乌兰浩特市', '1522');
INSERT INTO "public"."base_area" VALUES ('152202', '阿尔山市', '1522');
INSERT INTO "public"."base_area" VALUES ('152221', '科尔沁右翼前旗', '1522');
INSERT INTO "public"."base_area" VALUES ('152222', '科尔沁右翼中旗', '1522');
INSERT INTO "public"."base_area" VALUES ('152223', '扎赉特旗', '1522');
INSERT INTO "public"."base_area" VALUES ('152224', '突泉县', '1522');
INSERT INTO "public"."base_area" VALUES ('152501', '二连浩特市', '1525');
INSERT INTO "public"."base_area" VALUES ('152502', '锡林浩特市', '1525');
INSERT INTO "public"."base_area" VALUES ('152522', '阿巴嘎旗', '1525');
INSERT INTO "public"."base_area" VALUES ('152523', '苏尼特左旗', '1525');
INSERT INTO "public"."base_area" VALUES ('152524', '苏尼特右旗', '1525');
INSERT INTO "public"."base_area" VALUES ('152525', '东乌珠穆沁旗', '1525');
INSERT INTO "public"."base_area" VALUES ('152526', '西乌珠穆沁旗', '1525');
INSERT INTO "public"."base_area" VALUES ('152527', '太仆寺旗', '1525');
INSERT INTO "public"."base_area" VALUES ('152528', '镶黄旗', '1525');
INSERT INTO "public"."base_area" VALUES ('152529', '正镶白旗', '1525');
INSERT INTO "public"."base_area" VALUES ('152530', '正蓝旗', '1525');
INSERT INTO "public"."base_area" VALUES ('152531', '多伦县', '1525');
INSERT INTO "public"."base_area" VALUES ('152571', '乌拉盖管委会', '1525');
INSERT INTO "public"."base_area" VALUES ('152921', '阿拉善左旗', '1529');
INSERT INTO "public"."base_area" VALUES ('152922', '阿拉善右旗', '1529');
INSERT INTO "public"."base_area" VALUES ('152923', '额济纳旗', '1529');
INSERT INTO "public"."base_area" VALUES ('152971', '内蒙古阿拉善高新技术产业开发区', '1529');
INSERT INTO "public"."base_area" VALUES ('210102', '和平区', '2101');
INSERT INTO "public"."base_area" VALUES ('210103', '沈河区', '2101');
INSERT INTO "public"."base_area" VALUES ('210104', '大东区', '2101');
INSERT INTO "public"."base_area" VALUES ('210105', '皇姑区', '2101');
INSERT INTO "public"."base_area" VALUES ('210106', '铁西区', '2101');
INSERT INTO "public"."base_area" VALUES ('210111', '苏家屯区', '2101');
INSERT INTO "public"."base_area" VALUES ('210112', '浑南区', '2101');
INSERT INTO "public"."base_area" VALUES ('210113', '沈北新区', '2101');
INSERT INTO "public"."base_area" VALUES ('210114', '于洪区', '2101');
INSERT INTO "public"."base_area" VALUES ('210115', '辽中区', '2101');
INSERT INTO "public"."base_area" VALUES ('210123', '康平县', '2101');
INSERT INTO "public"."base_area" VALUES ('210124', '法库县', '2101');
INSERT INTO "public"."base_area" VALUES ('210181', '新民市', '2101');
INSERT INTO "public"."base_area" VALUES ('210202', '中山区', '2102');
INSERT INTO "public"."base_area" VALUES ('210203', '西岗区', '2102');
INSERT INTO "public"."base_area" VALUES ('210204', '沙河口区', '2102');
INSERT INTO "public"."base_area" VALUES ('210211', '甘井子区', '2102');
INSERT INTO "public"."base_area" VALUES ('210212', '旅顺口区', '2102');
INSERT INTO "public"."base_area" VALUES ('210213', '金州区', '2102');
INSERT INTO "public"."base_area" VALUES ('210214', '普兰店区', '2102');
INSERT INTO "public"."base_area" VALUES ('210224', '长海县', '2102');
INSERT INTO "public"."base_area" VALUES ('210281', '瓦房店市', '2102');
INSERT INTO "public"."base_area" VALUES ('210283', '庄河市', '2102');
INSERT INTO "public"."base_area" VALUES ('210302', '铁东区', '2103');
INSERT INTO "public"."base_area" VALUES ('210303', '铁西区', '2103');
INSERT INTO "public"."base_area" VALUES ('210304', '立山区', '2103');
INSERT INTO "public"."base_area" VALUES ('210311', '千山区', '2103');
INSERT INTO "public"."base_area" VALUES ('210321', '台安县', '2103');
INSERT INTO "public"."base_area" VALUES ('210323', '岫岩满族自治县', '2103');
INSERT INTO "public"."base_area" VALUES ('210381', '海城市', '2103');
INSERT INTO "public"."base_area" VALUES ('210402', '新抚区', '2104');
INSERT INTO "public"."base_area" VALUES ('210403', '东洲区', '2104');
INSERT INTO "public"."base_area" VALUES ('210404', '望花区', '2104');
INSERT INTO "public"."base_area" VALUES ('210411', '顺城区', '2104');
INSERT INTO "public"."base_area" VALUES ('210421', '抚顺县', '2104');
INSERT INTO "public"."base_area" VALUES ('210422', '新宾满族自治县', '2104');
INSERT INTO "public"."base_area" VALUES ('210423', '清原满族自治县', '2104');
INSERT INTO "public"."base_area" VALUES ('210502', '平山区', '2105');
INSERT INTO "public"."base_area" VALUES ('210503', '溪湖区', '2105');
INSERT INTO "public"."base_area" VALUES ('210504', '明山区', '2105');
INSERT INTO "public"."base_area" VALUES ('210505', '南芬区', '2105');
INSERT INTO "public"."base_area" VALUES ('210521', '本溪满族自治县', '2105');
INSERT INTO "public"."base_area" VALUES ('210522', '桓仁满族自治县', '2105');
INSERT INTO "public"."base_area" VALUES ('210602', '元宝区', '2106');
INSERT INTO "public"."base_area" VALUES ('210603', '振兴区', '2106');
INSERT INTO "public"."base_area" VALUES ('210604', '振安区', '2106');
INSERT INTO "public"."base_area" VALUES ('210624', '宽甸满族自治县', '2106');
INSERT INTO "public"."base_area" VALUES ('210681', '东港市', '2106');
INSERT INTO "public"."base_area" VALUES ('210682', '凤城市', '2106');
INSERT INTO "public"."base_area" VALUES ('210702', '古塔区', '2107');
INSERT INTO "public"."base_area" VALUES ('210703', '凌河区', '2107');
INSERT INTO "public"."base_area" VALUES ('210711', '太和区', '2107');
INSERT INTO "public"."base_area" VALUES ('210726', '黑山县', '2107');
INSERT INTO "public"."base_area" VALUES ('210727', '义县', '2107');
INSERT INTO "public"."base_area" VALUES ('210781', '凌海市', '2107');
INSERT INTO "public"."base_area" VALUES ('210782', '北镇市', '2107');
INSERT INTO "public"."base_area" VALUES ('210802', '站前区', '2108');
INSERT INTO "public"."base_area" VALUES ('210803', '西市区', '2108');
INSERT INTO "public"."base_area" VALUES ('210804', '鲅鱼圈区', '2108');
INSERT INTO "public"."base_area" VALUES ('210811', '老边区', '2108');
INSERT INTO "public"."base_area" VALUES ('210881', '盖州市', '2108');
INSERT INTO "public"."base_area" VALUES ('210882', '大石桥市', '2108');
INSERT INTO "public"."base_area" VALUES ('210902', '海州区', '2109');
INSERT INTO "public"."base_area" VALUES ('210903', '新邱区', '2109');
INSERT INTO "public"."base_area" VALUES ('210904', '太平区', '2109');
INSERT INTO "public"."base_area" VALUES ('210905', '清河门区', '2109');
INSERT INTO "public"."base_area" VALUES ('210911', '细河区', '2109');
INSERT INTO "public"."base_area" VALUES ('210921', '阜新蒙古族自治县', '2109');
INSERT INTO "public"."base_area" VALUES ('210922', '彰武县', '2109');
INSERT INTO "public"."base_area" VALUES ('211002', '白塔区', '2110');
INSERT INTO "public"."base_area" VALUES ('211003', '文圣区', '2110');
INSERT INTO "public"."base_area" VALUES ('211004', '宏伟区', '2110');
INSERT INTO "public"."base_area" VALUES ('211005', '弓长岭区', '2110');
INSERT INTO "public"."base_area" VALUES ('211011', '太子河区', '2110');
INSERT INTO "public"."base_area" VALUES ('211021', '辽阳县', '2110');
INSERT INTO "public"."base_area" VALUES ('211081', '灯塔市', '2110');
INSERT INTO "public"."base_area" VALUES ('211102', '双台子区', '2111');
INSERT INTO "public"."base_area" VALUES ('211103', '兴隆台区', '2111');
INSERT INTO "public"."base_area" VALUES ('211104', '大洼区', '2111');
INSERT INTO "public"."base_area" VALUES ('211122', '盘山县', '2111');
INSERT INTO "public"."base_area" VALUES ('211202', '银州区', '2112');
INSERT INTO "public"."base_area" VALUES ('211204', '清河区', '2112');
INSERT INTO "public"."base_area" VALUES ('211221', '铁岭县', '2112');
INSERT INTO "public"."base_area" VALUES ('211223', '西丰县', '2112');
INSERT INTO "public"."base_area" VALUES ('211224', '昌图县', '2112');
INSERT INTO "public"."base_area" VALUES ('211281', '调兵山市', '2112');
INSERT INTO "public"."base_area" VALUES ('211282', '开原市', '2112');
INSERT INTO "public"."base_area" VALUES ('211302', '双塔区', '2113');
INSERT INTO "public"."base_area" VALUES ('211303', '龙城区', '2113');
INSERT INTO "public"."base_area" VALUES ('211321', '朝阳县', '2113');
INSERT INTO "public"."base_area" VALUES ('211322', '建平县', '2113');
INSERT INTO "public"."base_area" VALUES ('211324', '喀喇沁左翼蒙古族自治县', '2113');
INSERT INTO "public"."base_area" VALUES ('211381', '北票市', '2113');
INSERT INTO "public"."base_area" VALUES ('211382', '凌源市', '2113');
INSERT INTO "public"."base_area" VALUES ('211402', '连山区', '2114');
INSERT INTO "public"."base_area" VALUES ('211403', '龙港区', '2114');
INSERT INTO "public"."base_area" VALUES ('211404', '南票区', '2114');
INSERT INTO "public"."base_area" VALUES ('211421', '绥中县', '2114');
INSERT INTO "public"."base_area" VALUES ('211422', '建昌县', '2114');
INSERT INTO "public"."base_area" VALUES ('211481', '兴城市', '2114');
INSERT INTO "public"."base_area" VALUES ('220102', '南关区', '2201');
INSERT INTO "public"."base_area" VALUES ('220103', '宽城区', '2201');
INSERT INTO "public"."base_area" VALUES ('220104', '朝阳区', '2201');
INSERT INTO "public"."base_area" VALUES ('220105', '二道区', '2201');
INSERT INTO "public"."base_area" VALUES ('220106', '绿园区', '2201');
INSERT INTO "public"."base_area" VALUES ('220112', '双阳区', '2201');
INSERT INTO "public"."base_area" VALUES ('220113', '九台区', '2201');
INSERT INTO "public"."base_area" VALUES ('220122', '农安县', '2201');
INSERT INTO "public"."base_area" VALUES ('220171', '长春经济技术开发区', '2201');
INSERT INTO "public"."base_area" VALUES ('220172', '长春净月高新技术产业开发区', '2201');
INSERT INTO "public"."base_area" VALUES ('220173', '长春高新技术产业开发区', '2201');
INSERT INTO "public"."base_area" VALUES ('220174', '长春汽车经济技术开发区', '2201');
INSERT INTO "public"."base_area" VALUES ('220182', '榆树市', '2201');
INSERT INTO "public"."base_area" VALUES ('220183', '德惠市', '2201');
INSERT INTO "public"."base_area" VALUES ('220184', '公主岭市', '2201');
INSERT INTO "public"."base_area" VALUES ('220202', '昌邑区', '2202');
INSERT INTO "public"."base_area" VALUES ('220203', '龙潭区', '2202');
INSERT INTO "public"."base_area" VALUES ('220204', '船营区', '2202');
INSERT INTO "public"."base_area" VALUES ('220211', '丰满区', '2202');
INSERT INTO "public"."base_area" VALUES ('220221', '永吉县', '2202');
INSERT INTO "public"."base_area" VALUES ('220271', '吉林经济开发区', '2202');
INSERT INTO "public"."base_area" VALUES ('220272', '吉林高新技术产业开发区', '2202');
INSERT INTO "public"."base_area" VALUES ('220273', '吉林中国新加坡食品区', '2202');
INSERT INTO "public"."base_area" VALUES ('220281', '蛟河市', '2202');
INSERT INTO "public"."base_area" VALUES ('220282', '桦甸市', '2202');
INSERT INTO "public"."base_area" VALUES ('220283', '舒兰市', '2202');
INSERT INTO "public"."base_area" VALUES ('220284', '磐石市', '2202');
INSERT INTO "public"."base_area" VALUES ('220302', '铁西区', '2203');
INSERT INTO "public"."base_area" VALUES ('220303', '铁东区', '2203');
INSERT INTO "public"."base_area" VALUES ('220322', '梨树县', '2203');
INSERT INTO "public"."base_area" VALUES ('220323', '伊通满族自治县', '2203');
INSERT INTO "public"."base_area" VALUES ('220382', '双辽市', '2203');
INSERT INTO "public"."base_area" VALUES ('220402', '龙山区', '2204');
INSERT INTO "public"."base_area" VALUES ('220403', '西安区', '2204');
INSERT INTO "public"."base_area" VALUES ('220421', '东丰县', '2204');
INSERT INTO "public"."base_area" VALUES ('220422', '东辽县', '2204');
INSERT INTO "public"."base_area" VALUES ('220502', '东昌区', '2205');
INSERT INTO "public"."base_area" VALUES ('220503', '二道江区', '2205');
INSERT INTO "public"."base_area" VALUES ('220521', '通化县', '2205');
INSERT INTO "public"."base_area" VALUES ('220523', '辉南县', '2205');
INSERT INTO "public"."base_area" VALUES ('220524', '柳河县', '2205');
INSERT INTO "public"."base_area" VALUES ('220581', '梅河口市', '2205');
INSERT INTO "public"."base_area" VALUES ('220582', '集安市', '2205');
INSERT INTO "public"."base_area" VALUES ('220602', '浑江区', '2206');
INSERT INTO "public"."base_area" VALUES ('220605', '江源区', '2206');
INSERT INTO "public"."base_area" VALUES ('220621', '抚松县', '2206');
INSERT INTO "public"."base_area" VALUES ('220622', '靖宇县', '2206');
INSERT INTO "public"."base_area" VALUES ('220623', '长白朝鲜族自治县', '2206');
INSERT INTO "public"."base_area" VALUES ('220681', '临江市', '2206');
INSERT INTO "public"."base_area" VALUES ('220702', '宁江区', '2207');
INSERT INTO "public"."base_area" VALUES ('220721', '前郭尔罗斯蒙古族自治县', '2207');
INSERT INTO "public"."base_area" VALUES ('220722', '长岭县', '2207');
INSERT INTO "public"."base_area" VALUES ('220723', '乾安县', '2207');
INSERT INTO "public"."base_area" VALUES ('220771', '吉林松原经济开发区', '2207');
INSERT INTO "public"."base_area" VALUES ('220781', '扶余市', '2207');
INSERT INTO "public"."base_area" VALUES ('220802', '洮北区', '2208');
INSERT INTO "public"."base_area" VALUES ('220821', '镇赉县', '2208');
INSERT INTO "public"."base_area" VALUES ('220822', '通榆县', '2208');
INSERT INTO "public"."base_area" VALUES ('220871', '吉林白城经济开发区', '2208');
INSERT INTO "public"."base_area" VALUES ('220881', '洮南市', '2208');
INSERT INTO "public"."base_area" VALUES ('220882', '大安市', '2208');
INSERT INTO "public"."base_area" VALUES ('222401', '延吉市', '2224');
INSERT INTO "public"."base_area" VALUES ('222402', '图们市', '2224');
INSERT INTO "public"."base_area" VALUES ('222403', '敦化市', '2224');
INSERT INTO "public"."base_area" VALUES ('222404', '珲春市', '2224');
INSERT INTO "public"."base_area" VALUES ('222405', '龙井市', '2224');
INSERT INTO "public"."base_area" VALUES ('222406', '和龙市', '2224');
INSERT INTO "public"."base_area" VALUES ('222424', '汪清县', '2224');
INSERT INTO "public"."base_area" VALUES ('222426', '安图县', '2224');
INSERT INTO "public"."base_area" VALUES ('230102', '道里区', '2301');
INSERT INTO "public"."base_area" VALUES ('230103', '南岗区', '2301');
INSERT INTO "public"."base_area" VALUES ('230104', '道外区', '2301');
INSERT INTO "public"."base_area" VALUES ('230108', '平房区', '2301');
INSERT INTO "public"."base_area" VALUES ('230109', '松北区', '2301');
INSERT INTO "public"."base_area" VALUES ('230110', '香坊区', '2301');
INSERT INTO "public"."base_area" VALUES ('230111', '呼兰区', '2301');
INSERT INTO "public"."base_area" VALUES ('230112', '阿城区', '2301');
INSERT INTO "public"."base_area" VALUES ('230113', '双城区', '2301');
INSERT INTO "public"."base_area" VALUES ('230123', '依兰县', '2301');
INSERT INTO "public"."base_area" VALUES ('230124', '方正县', '2301');
INSERT INTO "public"."base_area" VALUES ('230125', '宾县', '2301');
INSERT INTO "public"."base_area" VALUES ('230126', '巴彦县', '2301');
INSERT INTO "public"."base_area" VALUES ('230127', '木兰县', '2301');
INSERT INTO "public"."base_area" VALUES ('230128', '通河县', '2301');
INSERT INTO "public"."base_area" VALUES ('230129', '延寿县', '2301');
INSERT INTO "public"."base_area" VALUES ('230183', '尚志市', '2301');
INSERT INTO "public"."base_area" VALUES ('230184', '五常市', '2301');
INSERT INTO "public"."base_area" VALUES ('230202', '龙沙区', '2302');
INSERT INTO "public"."base_area" VALUES ('230203', '建华区', '2302');
INSERT INTO "public"."base_area" VALUES ('230204', '铁锋区', '2302');
INSERT INTO "public"."base_area" VALUES ('230205', '昂昂溪区', '2302');
INSERT INTO "public"."base_area" VALUES ('230206', '富拉尔基区', '2302');
INSERT INTO "public"."base_area" VALUES ('230207', '碾子山区', '2302');
INSERT INTO "public"."base_area" VALUES ('230208', '梅里斯达斡尔族区', '2302');
INSERT INTO "public"."base_area" VALUES ('230221', '龙江县', '2302');
INSERT INTO "public"."base_area" VALUES ('230223', '依安县', '2302');
INSERT INTO "public"."base_area" VALUES ('230224', '泰来县', '2302');
INSERT INTO "public"."base_area" VALUES ('230225', '甘南县', '2302');
INSERT INTO "public"."base_area" VALUES ('230227', '富裕县', '2302');
INSERT INTO "public"."base_area" VALUES ('230229', '克山县', '2302');
INSERT INTO "public"."base_area" VALUES ('230230', '克东县', '2302');
INSERT INTO "public"."base_area" VALUES ('230231', '拜泉县', '2302');
INSERT INTO "public"."base_area" VALUES ('230281', '讷河市', '2302');
INSERT INTO "public"."base_area" VALUES ('230302', '鸡冠区', '2303');
INSERT INTO "public"."base_area" VALUES ('230303', '恒山区', '2303');
INSERT INTO "public"."base_area" VALUES ('230304', '滴道区', '2303');
INSERT INTO "public"."base_area" VALUES ('230305', '梨树区', '2303');
INSERT INTO "public"."base_area" VALUES ('230306', '城子河区', '2303');
INSERT INTO "public"."base_area" VALUES ('230307', '麻山区', '2303');
INSERT INTO "public"."base_area" VALUES ('230321', '鸡东县', '2303');
INSERT INTO "public"."base_area" VALUES ('230381', '虎林市', '2303');
INSERT INTO "public"."base_area" VALUES ('230382', '密山市', '2303');
INSERT INTO "public"."base_area" VALUES ('230402', '向阳区', '2304');
INSERT INTO "public"."base_area" VALUES ('230403', '工农区', '2304');
INSERT INTO "public"."base_area" VALUES ('230404', '南山区', '2304');
INSERT INTO "public"."base_area" VALUES ('230405', '兴安区', '2304');
INSERT INTO "public"."base_area" VALUES ('230406', '东山区', '2304');
INSERT INTO "public"."base_area" VALUES ('230407', '兴山区', '2304');
INSERT INTO "public"."base_area" VALUES ('230421', '萝北县', '2304');
INSERT INTO "public"."base_area" VALUES ('230422', '绥滨县', '2304');
INSERT INTO "public"."base_area" VALUES ('230502', '尖山区', '2305');
INSERT INTO "public"."base_area" VALUES ('230503', '岭东区', '2305');
INSERT INTO "public"."base_area" VALUES ('230505', '四方台区', '2305');
INSERT INTO "public"."base_area" VALUES ('230506', '宝山区', '2305');
INSERT INTO "public"."base_area" VALUES ('230521', '集贤县', '2305');
INSERT INTO "public"."base_area" VALUES ('230522', '友谊县', '2305');
INSERT INTO "public"."base_area" VALUES ('230523', '宝清县', '2305');
INSERT INTO "public"."base_area" VALUES ('230524', '饶河县', '2305');
INSERT INTO "public"."base_area" VALUES ('230602', '萨尔图区', '2306');
INSERT INTO "public"."base_area" VALUES ('230603', '龙凤区', '2306');
INSERT INTO "public"."base_area" VALUES ('230604', '让胡路区', '2306');
INSERT INTO "public"."base_area" VALUES ('230605', '红岗区', '2306');
INSERT INTO "public"."base_area" VALUES ('230606', '大同区', '2306');
INSERT INTO "public"."base_area" VALUES ('230621', '肇州县', '2306');
INSERT INTO "public"."base_area" VALUES ('230622', '肇源县', '2306');
INSERT INTO "public"."base_area" VALUES ('230623', '林甸县', '2306');
INSERT INTO "public"."base_area" VALUES ('230624', '杜尔伯特蒙古族自治县', '2306');
INSERT INTO "public"."base_area" VALUES ('230671', '大庆高新技术产业开发区', '2306');
INSERT INTO "public"."base_area" VALUES ('230717', '伊美区', '2307');
INSERT INTO "public"."base_area" VALUES ('230718', '乌翠区', '2307');
INSERT INTO "public"."base_area" VALUES ('230719', '友好区', '2307');
INSERT INTO "public"."base_area" VALUES ('230722', '嘉荫县', '2307');
INSERT INTO "public"."base_area" VALUES ('230723', '汤旺县', '2307');
INSERT INTO "public"."base_area" VALUES ('230724', '丰林县', '2307');
INSERT INTO "public"."base_area" VALUES ('230725', '大箐山县', '2307');
INSERT INTO "public"."base_area" VALUES ('230726', '南岔县', '2307');
INSERT INTO "public"."base_area" VALUES ('230751', '金林区', '2307');
INSERT INTO "public"."base_area" VALUES ('230781', '铁力市', '2307');
INSERT INTO "public"."base_area" VALUES ('230803', '向阳区', '2308');
INSERT INTO "public"."base_area" VALUES ('230804', '前进区', '2308');
INSERT INTO "public"."base_area" VALUES ('230805', '东风区', '2308');
INSERT INTO "public"."base_area" VALUES ('230811', '郊区', '2308');
INSERT INTO "public"."base_area" VALUES ('230822', '桦南县', '2308');
INSERT INTO "public"."base_area" VALUES ('230826', '桦川县', '2308');
INSERT INTO "public"."base_area" VALUES ('230828', '汤原县', '2308');
INSERT INTO "public"."base_area" VALUES ('230881', '同江市', '2308');
INSERT INTO "public"."base_area" VALUES ('230882', '富锦市', '2308');
INSERT INTO "public"."base_area" VALUES ('230883', '抚远市', '2308');
INSERT INTO "public"."base_area" VALUES ('230902', '新兴区', '2309');
INSERT INTO "public"."base_area" VALUES ('230903', '桃山区', '2309');
INSERT INTO "public"."base_area" VALUES ('230904', '茄子河区', '2309');
INSERT INTO "public"."base_area" VALUES ('230921', '勃利县', '2309');
INSERT INTO "public"."base_area" VALUES ('231002', '东安区', '2310');
INSERT INTO "public"."base_area" VALUES ('231003', '阳明区', '2310');
INSERT INTO "public"."base_area" VALUES ('231004', '爱民区', '2310');
INSERT INTO "public"."base_area" VALUES ('231005', '西安区', '2310');
INSERT INTO "public"."base_area" VALUES ('231025', '林口县', '2310');
INSERT INTO "public"."base_area" VALUES ('231071', '牡丹江经济技术开发区', '2310');
INSERT INTO "public"."base_area" VALUES ('231081', '绥芬河市', '2310');
INSERT INTO "public"."base_area" VALUES ('231083', '海林市', '2310');
INSERT INTO "public"."base_area" VALUES ('231084', '宁安市', '2310');
INSERT INTO "public"."base_area" VALUES ('231085', '穆棱市', '2310');
INSERT INTO "public"."base_area" VALUES ('231086', '东宁市', '2310');
INSERT INTO "public"."base_area" VALUES ('231102', '爱辉区', '2311');
INSERT INTO "public"."base_area" VALUES ('231123', '逊克县', '2311');
INSERT INTO "public"."base_area" VALUES ('231124', '孙吴县', '2311');
INSERT INTO "public"."base_area" VALUES ('231181', '北安市', '2311');
INSERT INTO "public"."base_area" VALUES ('231182', '五大连池市', '2311');
INSERT INTO "public"."base_area" VALUES ('231183', '嫩江市', '2311');
INSERT INTO "public"."base_area" VALUES ('231202', '北林区', '2312');
INSERT INTO "public"."base_area" VALUES ('231221', '望奎县', '2312');
INSERT INTO "public"."base_area" VALUES ('231222', '兰西县', '2312');
INSERT INTO "public"."base_area" VALUES ('231223', '青冈县', '2312');
INSERT INTO "public"."base_area" VALUES ('231224', '庆安县', '2312');
INSERT INTO "public"."base_area" VALUES ('231225', '明水县', '2312');
INSERT INTO "public"."base_area" VALUES ('231226', '绥棱县', '2312');
INSERT INTO "public"."base_area" VALUES ('231281', '安达市', '2312');
INSERT INTO "public"."base_area" VALUES ('231282', '肇东市', '2312');
INSERT INTO "public"."base_area" VALUES ('231283', '海伦市', '2312');
INSERT INTO "public"."base_area" VALUES ('232701', '漠河市', '2327');
INSERT INTO "public"."base_area" VALUES ('232721', '呼玛县', '2327');
INSERT INTO "public"."base_area" VALUES ('232722', '塔河县', '2327');
INSERT INTO "public"."base_area" VALUES ('232761', '加格达奇区', '2327');
INSERT INTO "public"."base_area" VALUES ('232762', '松岭区', '2327');
INSERT INTO "public"."base_area" VALUES ('232763', '新林区', '2327');
INSERT INTO "public"."base_area" VALUES ('232764', '呼中区', '2327');
INSERT INTO "public"."base_area" VALUES ('310101', '黄浦区', '3101');
INSERT INTO "public"."base_area" VALUES ('310104', '徐汇区', '3101');
INSERT INTO "public"."base_area" VALUES ('310105', '长宁区', '3101');
INSERT INTO "public"."base_area" VALUES ('310106', '静安区', '3101');
INSERT INTO "public"."base_area" VALUES ('310107', '普陀区', '3101');
INSERT INTO "public"."base_area" VALUES ('310109', '虹口区', '3101');
INSERT INTO "public"."base_area" VALUES ('310110', '杨浦区', '3101');
INSERT INTO "public"."base_area" VALUES ('310112', '闵行区', '3101');
INSERT INTO "public"."base_area" VALUES ('310113', '宝山区', '3101');
INSERT INTO "public"."base_area" VALUES ('310114', '嘉定区', '3101');
INSERT INTO "public"."base_area" VALUES ('310115', '浦东新区', '3101');
INSERT INTO "public"."base_area" VALUES ('310116', '金山区', '3101');
INSERT INTO "public"."base_area" VALUES ('310117', '松江区', '3101');
INSERT INTO "public"."base_area" VALUES ('310118', '青浦区', '3101');
INSERT INTO "public"."base_area" VALUES ('310120', '奉贤区', '3101');
INSERT INTO "public"."base_area" VALUES ('310151', '崇明区', '3101');
INSERT INTO "public"."base_area" VALUES ('320102', '玄武区', '3201');
INSERT INTO "public"."base_area" VALUES ('320104', '秦淮区', '3201');
INSERT INTO "public"."base_area" VALUES ('320105', '建邺区', '3201');
INSERT INTO "public"."base_area" VALUES ('320106', '鼓楼区', '3201');
INSERT INTO "public"."base_area" VALUES ('320111', '浦口区', '3201');
INSERT INTO "public"."base_area" VALUES ('320113', '栖霞区', '3201');
INSERT INTO "public"."base_area" VALUES ('320114', '雨花台区', '3201');
INSERT INTO "public"."base_area" VALUES ('320115', '江宁区', '3201');
INSERT INTO "public"."base_area" VALUES ('320116', '六合区', '3201');
INSERT INTO "public"."base_area" VALUES ('320117', '溧水区', '3201');
INSERT INTO "public"."base_area" VALUES ('320118', '高淳区', '3201');
INSERT INTO "public"."base_area" VALUES ('320205', '锡山区', '3202');
INSERT INTO "public"."base_area" VALUES ('320206', '惠山区', '3202');
INSERT INTO "public"."base_area" VALUES ('320211', '滨湖区', '3202');
INSERT INTO "public"."base_area" VALUES ('320213', '梁溪区', '3202');
INSERT INTO "public"."base_area" VALUES ('320214', '新吴区', '3202');
INSERT INTO "public"."base_area" VALUES ('320281', '江阴市', '3202');
INSERT INTO "public"."base_area" VALUES ('320282', '宜兴市', '3202');
INSERT INTO "public"."base_area" VALUES ('320302', '鼓楼区', '3203');
INSERT INTO "public"."base_area" VALUES ('320303', '云龙区', '3203');
INSERT INTO "public"."base_area" VALUES ('320305', '贾汪区', '3203');
INSERT INTO "public"."base_area" VALUES ('320311', '泉山区', '3203');
INSERT INTO "public"."base_area" VALUES ('320312', '铜山区', '3203');
INSERT INTO "public"."base_area" VALUES ('320321', '丰县', '3203');
INSERT INTO "public"."base_area" VALUES ('320322', '沛县', '3203');
INSERT INTO "public"."base_area" VALUES ('320324', '睢宁县', '3203');
INSERT INTO "public"."base_area" VALUES ('320371', '徐州经济技术开发区', '3203');
INSERT INTO "public"."base_area" VALUES ('320381', '新沂市', '3203');
INSERT INTO "public"."base_area" VALUES ('320382', '邳州市', '3203');
INSERT INTO "public"."base_area" VALUES ('320402', '天宁区', '3204');
INSERT INTO "public"."base_area" VALUES ('320404', '钟楼区', '3204');
INSERT INTO "public"."base_area" VALUES ('320411', '新北区', '3204');
INSERT INTO "public"."base_area" VALUES ('320412', '武进区', '3204');
INSERT INTO "public"."base_area" VALUES ('320413', '金坛区', '3204');
INSERT INTO "public"."base_area" VALUES ('320481', '溧阳市', '3204');
INSERT INTO "public"."base_area" VALUES ('320505', '虎丘区', '3205');
INSERT INTO "public"."base_area" VALUES ('320506', '吴中区', '3205');
INSERT INTO "public"."base_area" VALUES ('320507', '相城区', '3205');
INSERT INTO "public"."base_area" VALUES ('320508', '姑苏区', '3205');
INSERT INTO "public"."base_area" VALUES ('320509', '吴江区', '3205');
INSERT INTO "public"."base_area" VALUES ('320571', '苏州工业园区', '3205');
INSERT INTO "public"."base_area" VALUES ('320581', '常熟市', '3205');
INSERT INTO "public"."base_area" VALUES ('320582', '张家港市', '3205');
INSERT INTO "public"."base_area" VALUES ('320583', '昆山市', '3205');
INSERT INTO "public"."base_area" VALUES ('320585', '太仓市', '3205');
INSERT INTO "public"."base_area" VALUES ('320612', '通州区', '3206');
INSERT INTO "public"."base_area" VALUES ('320613', '崇川区', '3206');
INSERT INTO "public"."base_area" VALUES ('320614', '海门区', '3206');
INSERT INTO "public"."base_area" VALUES ('320623', '如东县', '3206');
INSERT INTO "public"."base_area" VALUES ('320671', '南通经济技术开发区', '3206');
INSERT INTO "public"."base_area" VALUES ('320681', '启东市', '3206');
INSERT INTO "public"."base_area" VALUES ('320682', '如皋市', '3206');
INSERT INTO "public"."base_area" VALUES ('320685', '海安市', '3206');
INSERT INTO "public"."base_area" VALUES ('320703', '连云区', '3207');
INSERT INTO "public"."base_area" VALUES ('320706', '海州区', '3207');
INSERT INTO "public"."base_area" VALUES ('320707', '赣榆区', '3207');
INSERT INTO "public"."base_area" VALUES ('320722', '东海县', '3207');
INSERT INTO "public"."base_area" VALUES ('320723', '灌云县', '3207');
INSERT INTO "public"."base_area" VALUES ('320724', '灌南县', '3207');
INSERT INTO "public"."base_area" VALUES ('320771', '连云港经济技术开发区', '3207');
INSERT INTO "public"."base_area" VALUES ('320772', '连云港高新技术产业开发区', '3207');
INSERT INTO "public"."base_area" VALUES ('320803', '淮安区', '3208');
INSERT INTO "public"."base_area" VALUES ('320804', '淮阴区', '3208');
INSERT INTO "public"."base_area" VALUES ('320812', '清江浦区', '3208');
INSERT INTO "public"."base_area" VALUES ('320813', '洪泽区', '3208');
INSERT INTO "public"."base_area" VALUES ('320826', '涟水县', '3208');
INSERT INTO "public"."base_area" VALUES ('320830', '盱眙县', '3208');
INSERT INTO "public"."base_area" VALUES ('320831', '金湖县', '3208');
INSERT INTO "public"."base_area" VALUES ('320871', '淮安经济技术开发区', '3208');
INSERT INTO "public"."base_area" VALUES ('320902', '亭湖区', '3209');
INSERT INTO "public"."base_area" VALUES ('320903', '盐都区', '3209');
INSERT INTO "public"."base_area" VALUES ('320904', '大丰区', '3209');
INSERT INTO "public"."base_area" VALUES ('320921', '响水县', '3209');
INSERT INTO "public"."base_area" VALUES ('320922', '滨海县', '3209');
INSERT INTO "public"."base_area" VALUES ('320923', '阜宁县', '3209');
INSERT INTO "public"."base_area" VALUES ('320924', '射阳县', '3209');
INSERT INTO "public"."base_area" VALUES ('320925', '建湖县', '3209');
INSERT INTO "public"."base_area" VALUES ('320971', '盐城经济技术开发区', '3209');
INSERT INTO "public"."base_area" VALUES ('320981', '东台市', '3209');
INSERT INTO "public"."base_area" VALUES ('321002', '广陵区', '3210');
INSERT INTO "public"."base_area" VALUES ('321003', '邗江区', '3210');
INSERT INTO "public"."base_area" VALUES ('321012', '江都区', '3210');
INSERT INTO "public"."base_area" VALUES ('321023', '宝应县', '3210');
INSERT INTO "public"."base_area" VALUES ('321071', '扬州经济技术开发区', '3210');
INSERT INTO "public"."base_area" VALUES ('321081', '仪征市', '3210');
INSERT INTO "public"."base_area" VALUES ('321084', '高邮市', '3210');
INSERT INTO "public"."base_area" VALUES ('321102', '京口区', '3211');
INSERT INTO "public"."base_area" VALUES ('321111', '润州区', '3211');
INSERT INTO "public"."base_area" VALUES ('321112', '丹徒区', '3211');
INSERT INTO "public"."base_area" VALUES ('321171', '镇江新区', '3211');
INSERT INTO "public"."base_area" VALUES ('321181', '丹阳市', '3211');
INSERT INTO "public"."base_area" VALUES ('321182', '扬中市', '3211');
INSERT INTO "public"."base_area" VALUES ('321183', '句容市', '3211');
INSERT INTO "public"."base_area" VALUES ('321202', '海陵区', '3212');
INSERT INTO "public"."base_area" VALUES ('321203', '高港区', '3212');
INSERT INTO "public"."base_area" VALUES ('321204', '姜堰区', '3212');
INSERT INTO "public"."base_area" VALUES ('321271', '泰州医药高新技术产业开发区', '3212');
INSERT INTO "public"."base_area" VALUES ('321281', '兴化市', '3212');
INSERT INTO "public"."base_area" VALUES ('321282', '靖江市', '3212');
INSERT INTO "public"."base_area" VALUES ('321283', '泰兴市', '3212');
INSERT INTO "public"."base_area" VALUES ('321302', '宿城区', '3213');
INSERT INTO "public"."base_area" VALUES ('321311', '宿豫区', '3213');
INSERT INTO "public"."base_area" VALUES ('321322', '沭阳县', '3213');
INSERT INTO "public"."base_area" VALUES ('321323', '泗阳县', '3213');
INSERT INTO "public"."base_area" VALUES ('321324', '泗洪县', '3213');
INSERT INTO "public"."base_area" VALUES ('321371', '宿迁经济技术开发区', '3213');
INSERT INTO "public"."base_area" VALUES ('330102', '上城区', '3301');
INSERT INTO "public"."base_area" VALUES ('330105', '拱墅区', '3301');
INSERT INTO "public"."base_area" VALUES ('330106', '西湖区', '3301');
INSERT INTO "public"."base_area" VALUES ('330108', '滨江区', '3301');
INSERT INTO "public"."base_area" VALUES ('330109', '萧山区', '3301');
INSERT INTO "public"."base_area" VALUES ('330110', '余杭区', '3301');
INSERT INTO "public"."base_area" VALUES ('330111', '富阳区', '3301');
INSERT INTO "public"."base_area" VALUES ('330112', '临安区', '3301');
INSERT INTO "public"."base_area" VALUES ('330113', '临平区', '3301');
INSERT INTO "public"."base_area" VALUES ('330114', '钱塘区', '3301');
INSERT INTO "public"."base_area" VALUES ('330122', '桐庐县', '3301');
INSERT INTO "public"."base_area" VALUES ('330127', '淳安县', '3301');
INSERT INTO "public"."base_area" VALUES ('330182', '建德市', '3301');
INSERT INTO "public"."base_area" VALUES ('330203', '海曙区', '3302');
INSERT INTO "public"."base_area" VALUES ('330205', '江北区', '3302');
INSERT INTO "public"."base_area" VALUES ('330206', '北仑区', '3302');
INSERT INTO "public"."base_area" VALUES ('330211', '镇海区', '3302');
INSERT INTO "public"."base_area" VALUES ('330212', '鄞州区', '3302');
INSERT INTO "public"."base_area" VALUES ('330213', '奉化区', '3302');
INSERT INTO "public"."base_area" VALUES ('330225', '象山县', '3302');
INSERT INTO "public"."base_area" VALUES ('330226', '宁海县', '3302');
INSERT INTO "public"."base_area" VALUES ('330281', '余姚市', '3302');
INSERT INTO "public"."base_area" VALUES ('330282', '慈溪市', '3302');
INSERT INTO "public"."base_area" VALUES ('330302', '鹿城区', '3303');
INSERT INTO "public"."base_area" VALUES ('330303', '龙湾区', '3303');
INSERT INTO "public"."base_area" VALUES ('330304', '瓯海区', '3303');
INSERT INTO "public"."base_area" VALUES ('330305', '洞头区', '3303');
INSERT INTO "public"."base_area" VALUES ('330324', '永嘉县', '3303');
INSERT INTO "public"."base_area" VALUES ('330326', '平阳县', '3303');
INSERT INTO "public"."base_area" VALUES ('330327', '苍南县', '3303');
INSERT INTO "public"."base_area" VALUES ('330328', '文成县', '3303');
INSERT INTO "public"."base_area" VALUES ('330329', '泰顺县', '3303');
INSERT INTO "public"."base_area" VALUES ('330381', '瑞安市', '3303');
INSERT INTO "public"."base_area" VALUES ('330382', '乐清市', '3303');
INSERT INTO "public"."base_area" VALUES ('330383', '龙港市', '3303');
INSERT INTO "public"."base_area" VALUES ('330402', '南湖区', '3304');
INSERT INTO "public"."base_area" VALUES ('330411', '秀洲区', '3304');
INSERT INTO "public"."base_area" VALUES ('330421', '嘉善县', '3304');
INSERT INTO "public"."base_area" VALUES ('330424', '海盐县', '3304');
INSERT INTO "public"."base_area" VALUES ('330481', '海宁市', '3304');
INSERT INTO "public"."base_area" VALUES ('330482', '平湖市', '3304');
INSERT INTO "public"."base_area" VALUES ('330483', '桐乡市', '3304');
INSERT INTO "public"."base_area" VALUES ('330502', '吴兴区', '3305');
INSERT INTO "public"."base_area" VALUES ('330503', '南浔区', '3305');
INSERT INTO "public"."base_area" VALUES ('330521', '德清县', '3305');
INSERT INTO "public"."base_area" VALUES ('330522', '长兴县', '3305');
INSERT INTO "public"."base_area" VALUES ('330523', '安吉县', '3305');
INSERT INTO "public"."base_area" VALUES ('330602', '越城区', '3306');
INSERT INTO "public"."base_area" VALUES ('330603', '柯桥区', '3306');
INSERT INTO "public"."base_area" VALUES ('330604', '上虞区', '3306');
INSERT INTO "public"."base_area" VALUES ('330624', '新昌县', '3306');
INSERT INTO "public"."base_area" VALUES ('330681', '诸暨市', '3306');
INSERT INTO "public"."base_area" VALUES ('330683', '嵊州市', '3306');
INSERT INTO "public"."base_area" VALUES ('330702', '婺城区', '3307');
INSERT INTO "public"."base_area" VALUES ('330703', '金东区', '3307');
INSERT INTO "public"."base_area" VALUES ('330723', '武义县', '3307');
INSERT INTO "public"."base_area" VALUES ('330726', '浦江县', '3307');
INSERT INTO "public"."base_area" VALUES ('330727', '磐安县', '3307');
INSERT INTO "public"."base_area" VALUES ('330781', '兰溪市', '3307');
INSERT INTO "public"."base_area" VALUES ('330782', '义乌市', '3307');
INSERT INTO "public"."base_area" VALUES ('330783', '东阳市', '3307');
INSERT INTO "public"."base_area" VALUES ('330784', '永康市', '3307');
INSERT INTO "public"."base_area" VALUES ('330802', '柯城区', '3308');
INSERT INTO "public"."base_area" VALUES ('330803', '衢江区', '3308');
INSERT INTO "public"."base_area" VALUES ('330822', '常山县', '3308');
INSERT INTO "public"."base_area" VALUES ('330824', '开化县', '3308');
INSERT INTO "public"."base_area" VALUES ('330825', '龙游县', '3308');
INSERT INTO "public"."base_area" VALUES ('330881', '江山市', '3308');
INSERT INTO "public"."base_area" VALUES ('330902', '定海区', '3309');
INSERT INTO "public"."base_area" VALUES ('330903', '普陀区', '3309');
INSERT INTO "public"."base_area" VALUES ('330921', '岱山县', '3309');
INSERT INTO "public"."base_area" VALUES ('330922', '嵊泗县', '3309');
INSERT INTO "public"."base_area" VALUES ('331002', '椒江区', '3310');
INSERT INTO "public"."base_area" VALUES ('331003', '黄岩区', '3310');
INSERT INTO "public"."base_area" VALUES ('331004', '路桥区', '3310');
INSERT INTO "public"."base_area" VALUES ('331022', '三门县', '3310');
INSERT INTO "public"."base_area" VALUES ('331023', '天台县', '3310');
INSERT INTO "public"."base_area" VALUES ('331024', '仙居县', '3310');
INSERT INTO "public"."base_area" VALUES ('331081', '温岭市', '3310');
INSERT INTO "public"."base_area" VALUES ('331082', '临海市', '3310');
INSERT INTO "public"."base_area" VALUES ('331083', '玉环市', '3310');
INSERT INTO "public"."base_area" VALUES ('331102', '莲都区', '3311');
INSERT INTO "public"."base_area" VALUES ('331121', '青田县', '3311');
INSERT INTO "public"."base_area" VALUES ('331122', '缙云县', '3311');
INSERT INTO "public"."base_area" VALUES ('331123', '遂昌县', '3311');
INSERT INTO "public"."base_area" VALUES ('331124', '松阳县', '3311');
INSERT INTO "public"."base_area" VALUES ('331125', '云和县', '3311');
INSERT INTO "public"."base_area" VALUES ('331126', '庆元县', '3311');
INSERT INTO "public"."base_area" VALUES ('331127', '景宁畲族自治县', '3311');
INSERT INTO "public"."base_area" VALUES ('331181', '龙泉市', '3311');
INSERT INTO "public"."base_area" VALUES ('340102', '瑶海区', '3401');
INSERT INTO "public"."base_area" VALUES ('340103', '庐阳区', '3401');
INSERT INTO "public"."base_area" VALUES ('340104', '蜀山区', '3401');
INSERT INTO "public"."base_area" VALUES ('340111', '包河区', '3401');
INSERT INTO "public"."base_area" VALUES ('340121', '长丰县', '3401');
INSERT INTO "public"."base_area" VALUES ('340122', '肥东县', '3401');
INSERT INTO "public"."base_area" VALUES ('340123', '肥西县', '3401');
INSERT INTO "public"."base_area" VALUES ('340124', '庐江县', '3401');
INSERT INTO "public"."base_area" VALUES ('340171', '合肥高新技术产业开发区', '3401');
INSERT INTO "public"."base_area" VALUES ('340172', '合肥经济技术开发区', '3401');
INSERT INTO "public"."base_area" VALUES ('340173', '合肥新站高新技术产业开发区', '3401');
INSERT INTO "public"."base_area" VALUES ('340181', '巢湖市', '3401');
INSERT INTO "public"."base_area" VALUES ('340202', '镜湖区', '3402');
INSERT INTO "public"."base_area" VALUES ('340207', '鸠江区', '3402');
INSERT INTO "public"."base_area" VALUES ('340209', '弋江区', '3402');
INSERT INTO "public"."base_area" VALUES ('340210', '湾沚区', '3402');
INSERT INTO "public"."base_area" VALUES ('340212', '繁昌区', '3402');
INSERT INTO "public"."base_area" VALUES ('340223', '南陵县', '3402');
INSERT INTO "public"."base_area" VALUES ('340271', '芜湖经济技术开发区', '3402');
INSERT INTO "public"."base_area" VALUES ('340272', '安徽芜湖三山经济开发区', '3402');
INSERT INTO "public"."base_area" VALUES ('340281', '无为市', '3402');
INSERT INTO "public"."base_area" VALUES ('340302', '龙子湖区', '3403');
INSERT INTO "public"."base_area" VALUES ('340303', '蚌山区', '3403');
INSERT INTO "public"."base_area" VALUES ('340304', '禹会区', '3403');
INSERT INTO "public"."base_area" VALUES ('340311', '淮上区', '3403');
INSERT INTO "public"."base_area" VALUES ('340321', '怀远县', '3403');
INSERT INTO "public"."base_area" VALUES ('340322', '五河县', '3403');
INSERT INTO "public"."base_area" VALUES ('340323', '固镇县', '3403');
INSERT INTO "public"."base_area" VALUES ('340371', '蚌埠市高新技术开发区', '3403');
INSERT INTO "public"."base_area" VALUES ('340372', '蚌埠市经济开发区', '3403');
INSERT INTO "public"."base_area" VALUES ('340402', '大通区', '3404');
INSERT INTO "public"."base_area" VALUES ('340403', '田家庵区', '3404');
INSERT INTO "public"."base_area" VALUES ('340404', '谢家集区', '3404');
INSERT INTO "public"."base_area" VALUES ('340405', '八公山区', '3404');
INSERT INTO "public"."base_area" VALUES ('340406', '潘集区', '3404');
INSERT INTO "public"."base_area" VALUES ('340421', '凤台县', '3404');
INSERT INTO "public"."base_area" VALUES ('340422', '寿县', '3404');
INSERT INTO "public"."base_area" VALUES ('340503', '花山区', '3405');
INSERT INTO "public"."base_area" VALUES ('340504', '雨山区', '3405');
INSERT INTO "public"."base_area" VALUES ('340506', '博望区', '3405');
INSERT INTO "public"."base_area" VALUES ('340521', '当涂县', '3405');
INSERT INTO "public"."base_area" VALUES ('340522', '含山县', '3405');
INSERT INTO "public"."base_area" VALUES ('340523', '和县', '3405');
INSERT INTO "public"."base_area" VALUES ('340602', '杜集区', '3406');
INSERT INTO "public"."base_area" VALUES ('340603', '相山区', '3406');
INSERT INTO "public"."base_area" VALUES ('340604', '烈山区', '3406');
INSERT INTO "public"."base_area" VALUES ('340621', '濉溪县', '3406');
INSERT INTO "public"."base_area" VALUES ('340705', '铜官区', '3407');
INSERT INTO "public"."base_area" VALUES ('340706', '义安区', '3407');
INSERT INTO "public"."base_area" VALUES ('340711', '郊区', '3407');
INSERT INTO "public"."base_area" VALUES ('340722', '枞阳县', '3407');
INSERT INTO "public"."base_area" VALUES ('340802', '迎江区', '3408');
INSERT INTO "public"."base_area" VALUES ('340803', '大观区', '3408');
INSERT INTO "public"."base_area" VALUES ('340811', '宜秀区', '3408');
INSERT INTO "public"."base_area" VALUES ('340822', '怀宁县', '3408');
INSERT INTO "public"."base_area" VALUES ('340825', '太湖县', '3408');
INSERT INTO "public"."base_area" VALUES ('340826', '宿松县', '3408');
INSERT INTO "public"."base_area" VALUES ('340827', '望江县', '3408');
INSERT INTO "public"."base_area" VALUES ('340828', '岳西县', '3408');
INSERT INTO "public"."base_area" VALUES ('340871', '安徽安庆经济开发区', '3408');
INSERT INTO "public"."base_area" VALUES ('340881', '桐城市', '3408');
INSERT INTO "public"."base_area" VALUES ('340882', '潜山市', '3408');
INSERT INTO "public"."base_area" VALUES ('341002', '屯溪区', '3410');
INSERT INTO "public"."base_area" VALUES ('341003', '黄山区', '3410');
INSERT INTO "public"."base_area" VALUES ('341004', '徽州区', '3410');
INSERT INTO "public"."base_area" VALUES ('341021', '歙县', '3410');
INSERT INTO "public"."base_area" VALUES ('341022', '休宁县', '3410');
INSERT INTO "public"."base_area" VALUES ('341023', '黟县', '3410');
INSERT INTO "public"."base_area" VALUES ('341024', '祁门县', '3410');
INSERT INTO "public"."base_area" VALUES ('341102', '琅琊区', '3411');
INSERT INTO "public"."base_area" VALUES ('341103', '南谯区', '3411');
INSERT INTO "public"."base_area" VALUES ('341122', '来安县', '3411');
INSERT INTO "public"."base_area" VALUES ('341124', '全椒县', '3411');
INSERT INTO "public"."base_area" VALUES ('341125', '定远县', '3411');
INSERT INTO "public"."base_area" VALUES ('341126', '凤阳县', '3411');
INSERT INTO "public"."base_area" VALUES ('341171', '中新苏滁高新技术产业开发区', '3411');
INSERT INTO "public"."base_area" VALUES ('341172', '滁州经济技术开发区', '3411');
INSERT INTO "public"."base_area" VALUES ('341181', '天长市', '3411');
INSERT INTO "public"."base_area" VALUES ('341182', '明光市', '3411');
INSERT INTO "public"."base_area" VALUES ('341202', '颍州区', '3412');
INSERT INTO "public"."base_area" VALUES ('341203', '颍东区', '3412');
INSERT INTO "public"."base_area" VALUES ('341204', '颍泉区', '3412');
INSERT INTO "public"."base_area" VALUES ('341221', '临泉县', '3412');
INSERT INTO "public"."base_area" VALUES ('341222', '太和县', '3412');
INSERT INTO "public"."base_area" VALUES ('341225', '阜南县', '3412');
INSERT INTO "public"."base_area" VALUES ('341226', '颍上县', '3412');
INSERT INTO "public"."base_area" VALUES ('341271', '阜阳合肥现代产业园区', '3412');
INSERT INTO "public"."base_area" VALUES ('341272', '阜阳经济技术开发区', '3412');
INSERT INTO "public"."base_area" VALUES ('341282', '界首市', '3412');
INSERT INTO "public"."base_area" VALUES ('341302', '埇桥区', '3413');
INSERT INTO "public"."base_area" VALUES ('341321', '砀山县', '3413');
INSERT INTO "public"."base_area" VALUES ('341322', '萧县', '3413');
INSERT INTO "public"."base_area" VALUES ('341323', '灵璧县', '3413');
INSERT INTO "public"."base_area" VALUES ('341324', '泗县', '3413');
INSERT INTO "public"."base_area" VALUES ('341371', '宿州马鞍山现代产业园区', '3413');
INSERT INTO "public"."base_area" VALUES ('341372', '宿州经济技术开发区', '3413');
INSERT INTO "public"."base_area" VALUES ('341502', '金安区', '3415');
INSERT INTO "public"."base_area" VALUES ('341503', '裕安区', '3415');
INSERT INTO "public"."base_area" VALUES ('341504', '叶集区', '3415');
INSERT INTO "public"."base_area" VALUES ('341522', '霍邱县', '3415');
INSERT INTO "public"."base_area" VALUES ('341523', '舒城县', '3415');
INSERT INTO "public"."base_area" VALUES ('341524', '金寨县', '3415');
INSERT INTO "public"."base_area" VALUES ('341525', '霍山县', '3415');
INSERT INTO "public"."base_area" VALUES ('341602', '谯城区', '3416');
INSERT INTO "public"."base_area" VALUES ('341621', '涡阳县', '3416');
INSERT INTO "public"."base_area" VALUES ('341622', '蒙城县', '3416');
INSERT INTO "public"."base_area" VALUES ('341623', '利辛县', '3416');
INSERT INTO "public"."base_area" VALUES ('341702', '贵池区', '3417');
INSERT INTO "public"."base_area" VALUES ('341721', '东至县', '3417');
INSERT INTO "public"."base_area" VALUES ('341722', '石台县', '3417');
INSERT INTO "public"."base_area" VALUES ('341723', '青阳县', '3417');
INSERT INTO "public"."base_area" VALUES ('341802', '宣州区', '3418');
INSERT INTO "public"."base_area" VALUES ('341821', '郎溪县', '3418');
INSERT INTO "public"."base_area" VALUES ('341823', '泾县', '3418');
INSERT INTO "public"."base_area" VALUES ('341824', '绩溪县', '3418');
INSERT INTO "public"."base_area" VALUES ('341825', '旌德县', '3418');
INSERT INTO "public"."base_area" VALUES ('341871', '宣城市经济开发区', '3418');
INSERT INTO "public"."base_area" VALUES ('341881', '宁国市', '3418');
INSERT INTO "public"."base_area" VALUES ('341882', '广德市', '3418');
INSERT INTO "public"."base_area" VALUES ('350102', '鼓楼区', '3501');
INSERT INTO "public"."base_area" VALUES ('350103', '台江区', '3501');
INSERT INTO "public"."base_area" VALUES ('350104', '仓山区', '3501');
INSERT INTO "public"."base_area" VALUES ('350105', '马尾区', '3501');
INSERT INTO "public"."base_area" VALUES ('350111', '晋安区', '3501');
INSERT INTO "public"."base_area" VALUES ('350112', '长乐区', '3501');
INSERT INTO "public"."base_area" VALUES ('350121', '闽侯县', '3501');
INSERT INTO "public"."base_area" VALUES ('350122', '连江县', '3501');
INSERT INTO "public"."base_area" VALUES ('350123', '罗源县', '3501');
INSERT INTO "public"."base_area" VALUES ('350124', '闽清县', '3501');
INSERT INTO "public"."base_area" VALUES ('350125', '永泰县', '3501');
INSERT INTO "public"."base_area" VALUES ('350128', '平潭县', '3501');
INSERT INTO "public"."base_area" VALUES ('350181', '福清市', '3501');
INSERT INTO "public"."base_area" VALUES ('350203', '思明区', '3502');
INSERT INTO "public"."base_area" VALUES ('350205', '海沧区', '3502');
INSERT INTO "public"."base_area" VALUES ('350206', '湖里区', '3502');
INSERT INTO "public"."base_area" VALUES ('350211', '集美区', '3502');
INSERT INTO "public"."base_area" VALUES ('350212', '同安区', '3502');
INSERT INTO "public"."base_area" VALUES ('350213', '翔安区', '3502');
INSERT INTO "public"."base_area" VALUES ('350302', '城厢区', '3503');
INSERT INTO "public"."base_area" VALUES ('350303', '涵江区', '3503');
INSERT INTO "public"."base_area" VALUES ('350304', '荔城区', '3503');
INSERT INTO "public"."base_area" VALUES ('350305', '秀屿区', '3503');
INSERT INTO "public"."base_area" VALUES ('350322', '仙游县', '3503');
INSERT INTO "public"."base_area" VALUES ('350404', '三元区', '3504');
INSERT INTO "public"."base_area" VALUES ('350405', '沙县区', '3504');
INSERT INTO "public"."base_area" VALUES ('350421', '明溪县', '3504');
INSERT INTO "public"."base_area" VALUES ('350423', '清流县', '3504');
INSERT INTO "public"."base_area" VALUES ('350424', '宁化县', '3504');
INSERT INTO "public"."base_area" VALUES ('350425', '大田县', '3504');
INSERT INTO "public"."base_area" VALUES ('350426', '尤溪县', '3504');
INSERT INTO "public"."base_area" VALUES ('350428', '将乐县', '3504');
INSERT INTO "public"."base_area" VALUES ('350429', '泰宁县', '3504');
INSERT INTO "public"."base_area" VALUES ('350430', '建宁县', '3504');
INSERT INTO "public"."base_area" VALUES ('350481', '永安市', '3504');
INSERT INTO "public"."base_area" VALUES ('350502', '鲤城区', '3505');
INSERT INTO "public"."base_area" VALUES ('350503', '丰泽区', '3505');
INSERT INTO "public"."base_area" VALUES ('350504', '洛江区', '3505');
INSERT INTO "public"."base_area" VALUES ('350505', '泉港区', '3505');
INSERT INTO "public"."base_area" VALUES ('350521', '惠安县', '3505');
INSERT INTO "public"."base_area" VALUES ('350524', '安溪县', '3505');
INSERT INTO "public"."base_area" VALUES ('350525', '永春县', '3505');
INSERT INTO "public"."base_area" VALUES ('350526', '德化县', '3505');
INSERT INTO "public"."base_area" VALUES ('350527', '金门县', '3505');
INSERT INTO "public"."base_area" VALUES ('350581', '石狮市', '3505');
INSERT INTO "public"."base_area" VALUES ('350582', '晋江市', '3505');
INSERT INTO "public"."base_area" VALUES ('350583', '南安市', '3505');
INSERT INTO "public"."base_area" VALUES ('350602', '芗城区', '3506');
INSERT INTO "public"."base_area" VALUES ('350603', '龙文区', '3506');
INSERT INTO "public"."base_area" VALUES ('350604', '龙海区', '3506');
INSERT INTO "public"."base_area" VALUES ('350605', '长泰区', '3506');
INSERT INTO "public"."base_area" VALUES ('350622', '云霄县', '3506');
INSERT INTO "public"."base_area" VALUES ('350623', '漳浦县', '3506');
INSERT INTO "public"."base_area" VALUES ('350624', '诏安县', '3506');
INSERT INTO "public"."base_area" VALUES ('350626', '东山县', '3506');
INSERT INTO "public"."base_area" VALUES ('350627', '南靖县', '3506');
INSERT INTO "public"."base_area" VALUES ('350628', '平和县', '3506');
INSERT INTO "public"."base_area" VALUES ('350629', '华安县', '3506');
INSERT INTO "public"."base_area" VALUES ('350702', '延平区', '3507');
INSERT INTO "public"."base_area" VALUES ('350703', '建阳区', '3507');
INSERT INTO "public"."base_area" VALUES ('350721', '顺昌县', '3507');
INSERT INTO "public"."base_area" VALUES ('350722', '浦城县', '3507');
INSERT INTO "public"."base_area" VALUES ('350723', '光泽县', '3507');
INSERT INTO "public"."base_area" VALUES ('350724', '松溪县', '3507');
INSERT INTO "public"."base_area" VALUES ('350725', '政和县', '3507');
INSERT INTO "public"."base_area" VALUES ('350781', '邵武市', '3507');
INSERT INTO "public"."base_area" VALUES ('350782', '武夷山市', '3507');
INSERT INTO "public"."base_area" VALUES ('350783', '建瓯市', '3507');
INSERT INTO "public"."base_area" VALUES ('350802', '新罗区', '3508');
INSERT INTO "public"."base_area" VALUES ('350803', '永定区', '3508');
INSERT INTO "public"."base_area" VALUES ('350821', '长汀县', '3508');
INSERT INTO "public"."base_area" VALUES ('350823', '上杭县', '3508');
INSERT INTO "public"."base_area" VALUES ('350824', '武平县', '3508');
INSERT INTO "public"."base_area" VALUES ('350825', '连城县', '3508');
INSERT INTO "public"."base_area" VALUES ('350881', '漳平市', '3508');
INSERT INTO "public"."base_area" VALUES ('350902', '蕉城区', '3509');
INSERT INTO "public"."base_area" VALUES ('350921', '霞浦县', '3509');
INSERT INTO "public"."base_area" VALUES ('350922', '古田县', '3509');
INSERT INTO "public"."base_area" VALUES ('350923', '屏南县', '3509');
INSERT INTO "public"."base_area" VALUES ('350924', '寿宁县', '3509');
INSERT INTO "public"."base_area" VALUES ('350925', '周宁县', '3509');
INSERT INTO "public"."base_area" VALUES ('350926', '柘荣县', '3509');
INSERT INTO "public"."base_area" VALUES ('350981', '福安市', '3509');
INSERT INTO "public"."base_area" VALUES ('350982', '福鼎市', '3509');
INSERT INTO "public"."base_area" VALUES ('360102', '东湖区', '3601');
INSERT INTO "public"."base_area" VALUES ('360103', '西湖区', '3601');
INSERT INTO "public"."base_area" VALUES ('360104', '青云谱区', '3601');
INSERT INTO "public"."base_area" VALUES ('360111', '青山湖区', '3601');
INSERT INTO "public"."base_area" VALUES ('360112', '新建区', '3601');
INSERT INTO "public"."base_area" VALUES ('360113', '红谷滩区', '3601');
INSERT INTO "public"."base_area" VALUES ('360121', '南昌县', '3601');
INSERT INTO "public"."base_area" VALUES ('360123', '安义县', '3601');
INSERT INTO "public"."base_area" VALUES ('360124', '进贤县', '3601');
INSERT INTO "public"."base_area" VALUES ('360202', '昌江区', '3602');
INSERT INTO "public"."base_area" VALUES ('360203', '珠山区', '3602');
INSERT INTO "public"."base_area" VALUES ('360222', '浮梁县', '3602');
INSERT INTO "public"."base_area" VALUES ('360281', '乐平市', '3602');
INSERT INTO "public"."base_area" VALUES ('360302', '安源区', '3603');
INSERT INTO "public"."base_area" VALUES ('360313', '湘东区', '3603');
INSERT INTO "public"."base_area" VALUES ('360321', '莲花县', '3603');
INSERT INTO "public"."base_area" VALUES ('360322', '上栗县', '3603');
INSERT INTO "public"."base_area" VALUES ('360323', '芦溪县', '3603');
INSERT INTO "public"."base_area" VALUES ('360402', '濂溪区', '3604');
INSERT INTO "public"."base_area" VALUES ('360403', '浔阳区', '3604');
INSERT INTO "public"."base_area" VALUES ('360404', '柴桑区', '3604');
INSERT INTO "public"."base_area" VALUES ('360423', '武宁县', '3604');
INSERT INTO "public"."base_area" VALUES ('360424', '修水县', '3604');
INSERT INTO "public"."base_area" VALUES ('360425', '永修县', '3604');
INSERT INTO "public"."base_area" VALUES ('360426', '德安县', '3604');
INSERT INTO "public"."base_area" VALUES ('360428', '都昌县', '3604');
INSERT INTO "public"."base_area" VALUES ('360429', '湖口县', '3604');
INSERT INTO "public"."base_area" VALUES ('360430', '彭泽县', '3604');
INSERT INTO "public"."base_area" VALUES ('360481', '瑞昌市', '3604');
INSERT INTO "public"."base_area" VALUES ('360482', '共青城市', '3604');
INSERT INTO "public"."base_area" VALUES ('360483', '庐山市', '3604');
INSERT INTO "public"."base_area" VALUES ('360502', '渝水区', '3605');
INSERT INTO "public"."base_area" VALUES ('360521', '分宜县', '3605');
INSERT INTO "public"."base_area" VALUES ('360602', '月湖区', '3606');
INSERT INTO "public"."base_area" VALUES ('360603', '余江区', '3606');
INSERT INTO "public"."base_area" VALUES ('360681', '贵溪市', '3606');
INSERT INTO "public"."base_area" VALUES ('360702', '章贡区', '3607');
INSERT INTO "public"."base_area" VALUES ('360703', '南康区', '3607');
INSERT INTO "public"."base_area" VALUES ('360704', '赣县区', '3607');
INSERT INTO "public"."base_area" VALUES ('360722', '信丰县', '3607');
INSERT INTO "public"."base_area" VALUES ('360723', '大余县', '3607');
INSERT INTO "public"."base_area" VALUES ('360724', '上犹县', '3607');
INSERT INTO "public"."base_area" VALUES ('360725', '崇义县', '3607');
INSERT INTO "public"."base_area" VALUES ('360726', '安远县', '3607');
INSERT INTO "public"."base_area" VALUES ('360728', '定南县', '3607');
INSERT INTO "public"."base_area" VALUES ('360729', '全南县', '3607');
INSERT INTO "public"."base_area" VALUES ('360730', '宁都县', '3607');
INSERT INTO "public"."base_area" VALUES ('360731', '于都县', '3607');
INSERT INTO "public"."base_area" VALUES ('360732', '兴国县', '3607');
INSERT INTO "public"."base_area" VALUES ('360733', '会昌县', '3607');
INSERT INTO "public"."base_area" VALUES ('360734', '寻乌县', '3607');
INSERT INTO "public"."base_area" VALUES ('360735', '石城县', '3607');
INSERT INTO "public"."base_area" VALUES ('360781', '瑞金市', '3607');
INSERT INTO "public"."base_area" VALUES ('360783', '龙南市', '3607');
INSERT INTO "public"."base_area" VALUES ('360802', '吉州区', '3608');
INSERT INTO "public"."base_area" VALUES ('360803', '青原区', '3608');
INSERT INTO "public"."base_area" VALUES ('360821', '吉安县', '3608');
INSERT INTO "public"."base_area" VALUES ('360822', '吉水县', '3608');
INSERT INTO "public"."base_area" VALUES ('360823', '峡江县', '3608');
INSERT INTO "public"."base_area" VALUES ('360824', '新干县', '3608');
INSERT INTO "public"."base_area" VALUES ('360825', '永丰县', '3608');
INSERT INTO "public"."base_area" VALUES ('360826', '泰和县', '3608');
INSERT INTO "public"."base_area" VALUES ('360827', '遂川县', '3608');
INSERT INTO "public"."base_area" VALUES ('360828', '万安县', '3608');
INSERT INTO "public"."base_area" VALUES ('360829', '安福县', '3608');
INSERT INTO "public"."base_area" VALUES ('360830', '永新县', '3608');
INSERT INTO "public"."base_area" VALUES ('360881', '井冈山市', '3608');
INSERT INTO "public"."base_area" VALUES ('360902', '袁州区', '3609');
INSERT INTO "public"."base_area" VALUES ('360921', '奉新县', '3609');
INSERT INTO "public"."base_area" VALUES ('360922', '万载县', '3609');
INSERT INTO "public"."base_area" VALUES ('360923', '上高县', '3609');
INSERT INTO "public"."base_area" VALUES ('360924', '宜丰县', '3609');
INSERT INTO "public"."base_area" VALUES ('360925', '靖安县', '3609');
INSERT INTO "public"."base_area" VALUES ('360926', '铜鼓县', '3609');
INSERT INTO "public"."base_area" VALUES ('360981', '丰城市', '3609');
INSERT INTO "public"."base_area" VALUES ('360982', '樟树市', '3609');
INSERT INTO "public"."base_area" VALUES ('360983', '高安市', '3609');
INSERT INTO "public"."base_area" VALUES ('361002', '临川区', '3610');
INSERT INTO "public"."base_area" VALUES ('361003', '东乡区', '3610');
INSERT INTO "public"."base_area" VALUES ('361021', '南城县', '3610');
INSERT INTO "public"."base_area" VALUES ('361022', '黎川县', '3610');
INSERT INTO "public"."base_area" VALUES ('361023', '南丰县', '3610');
INSERT INTO "public"."base_area" VALUES ('361024', '崇仁县', '3610');
INSERT INTO "public"."base_area" VALUES ('361025', '乐安县', '3610');
INSERT INTO "public"."base_area" VALUES ('361026', '宜黄县', '3610');
INSERT INTO "public"."base_area" VALUES ('361027', '金溪县', '3610');
INSERT INTO "public"."base_area" VALUES ('361028', '资溪县', '3610');
INSERT INTO "public"."base_area" VALUES ('361030', '广昌县', '3610');
INSERT INTO "public"."base_area" VALUES ('361102', '信州区', '3611');
INSERT INTO "public"."base_area" VALUES ('361103', '广丰区', '3611');
INSERT INTO "public"."base_area" VALUES ('361104', '广信区', '3611');
INSERT INTO "public"."base_area" VALUES ('361123', '玉山县', '3611');
INSERT INTO "public"."base_area" VALUES ('361124', '铅山县', '3611');
INSERT INTO "public"."base_area" VALUES ('361125', '横峰县', '3611');
INSERT INTO "public"."base_area" VALUES ('361126', '弋阳县', '3611');
INSERT INTO "public"."base_area" VALUES ('361127', '余干县', '3611');
INSERT INTO "public"."base_area" VALUES ('361128', '鄱阳县', '3611');
INSERT INTO "public"."base_area" VALUES ('361129', '万年县', '3611');
INSERT INTO "public"."base_area" VALUES ('361130', '婺源县', '3611');
INSERT INTO "public"."base_area" VALUES ('361181', '德兴市', '3611');
INSERT INTO "public"."base_area" VALUES ('370102', '历下区', '3701');
INSERT INTO "public"."base_area" VALUES ('370103', '市中区', '3701');
INSERT INTO "public"."base_area" VALUES ('370104', '槐荫区', '3701');
INSERT INTO "public"."base_area" VALUES ('370105', '天桥区', '3701');
INSERT INTO "public"."base_area" VALUES ('370112', '历城区', '3701');
INSERT INTO "public"."base_area" VALUES ('370113', '长清区', '3701');
INSERT INTO "public"."base_area" VALUES ('370114', '章丘区', '3701');
INSERT INTO "public"."base_area" VALUES ('370115', '济阳区', '3701');
INSERT INTO "public"."base_area" VALUES ('370116', '莱芜区', '3701');
INSERT INTO "public"."base_area" VALUES ('370117', '钢城区', '3701');
INSERT INTO "public"."base_area" VALUES ('370124', '平阴县', '3701');
INSERT INTO "public"."base_area" VALUES ('370126', '商河县', '3701');
INSERT INTO "public"."base_area" VALUES ('370171', '济南高新技术产业开发区', '3701');
INSERT INTO "public"."base_area" VALUES ('370202', '市南区', '3702');
INSERT INTO "public"."base_area" VALUES ('370203', '市北区', '3702');
INSERT INTO "public"."base_area" VALUES ('370211', '黄岛区', '3702');
INSERT INTO "public"."base_area" VALUES ('370212', '崂山区', '3702');
INSERT INTO "public"."base_area" VALUES ('370213', '李沧区', '3702');
INSERT INTO "public"."base_area" VALUES ('370214', '城阳区', '3702');
INSERT INTO "public"."base_area" VALUES ('370215', '即墨区', '3702');
INSERT INTO "public"."base_area" VALUES ('370271', '青岛高新技术产业开发区', '3702');
INSERT INTO "public"."base_area" VALUES ('370281', '胶州市', '3702');
INSERT INTO "public"."base_area" VALUES ('370283', '平度市', '3702');
INSERT INTO "public"."base_area" VALUES ('370285', '莱西市', '3702');
INSERT INTO "public"."base_area" VALUES ('370302', '淄川区', '3703');
INSERT INTO "public"."base_area" VALUES ('370303', '张店区', '3703');
INSERT INTO "public"."base_area" VALUES ('370304', '博山区', '3703');
INSERT INTO "public"."base_area" VALUES ('370305', '临淄区', '3703');
INSERT INTO "public"."base_area" VALUES ('370306', '周村区', '3703');
INSERT INTO "public"."base_area" VALUES ('370321', '桓台县', '3703');
INSERT INTO "public"."base_area" VALUES ('370322', '高青县', '3703');
INSERT INTO "public"."base_area" VALUES ('370323', '沂源县', '3703');
INSERT INTO "public"."base_area" VALUES ('370402', '市中区', '3704');
INSERT INTO "public"."base_area" VALUES ('370403', '薛城区', '3704');
INSERT INTO "public"."base_area" VALUES ('370404', '峄城区', '3704');
INSERT INTO "public"."base_area" VALUES ('370405', '台儿庄区', '3704');
INSERT INTO "public"."base_area" VALUES ('370406', '山亭区', '3704');
INSERT INTO "public"."base_area" VALUES ('370481', '滕州市', '3704');
INSERT INTO "public"."base_area" VALUES ('370502', '东营区', '3705');
INSERT INTO "public"."base_area" VALUES ('370503', '河口区', '3705');
INSERT INTO "public"."base_area" VALUES ('370505', '垦利区', '3705');
INSERT INTO "public"."base_area" VALUES ('370522', '利津县', '3705');
INSERT INTO "public"."base_area" VALUES ('370523', '广饶县', '3705');
INSERT INTO "public"."base_area" VALUES ('370571', '东营经济技术开发区', '3705');
INSERT INTO "public"."base_area" VALUES ('370572', '东营港经济开发区', '3705');
INSERT INTO "public"."base_area" VALUES ('370602', '芝罘区', '3706');
INSERT INTO "public"."base_area" VALUES ('370611', '福山区', '3706');
INSERT INTO "public"."base_area" VALUES ('370612', '牟平区', '3706');
INSERT INTO "public"."base_area" VALUES ('370613', '莱山区', '3706');
INSERT INTO "public"."base_area" VALUES ('370614', '蓬莱区', '3706');
INSERT INTO "public"."base_area" VALUES ('370671', '烟台高新技术产业开发区', '3706');
INSERT INTO "public"."base_area" VALUES ('370672', '烟台经济技术开发区', '3706');
INSERT INTO "public"."base_area" VALUES ('370681', '龙口市', '3706');
INSERT INTO "public"."base_area" VALUES ('370682', '莱阳市', '3706');
INSERT INTO "public"."base_area" VALUES ('370683', '莱州市', '3706');
INSERT INTO "public"."base_area" VALUES ('370685', '招远市', '3706');
INSERT INTO "public"."base_area" VALUES ('370686', '栖霞市', '3706');
INSERT INTO "public"."base_area" VALUES ('370687', '海阳市', '3706');
INSERT INTO "public"."base_area" VALUES ('370702', '潍城区', '3707');
INSERT INTO "public"."base_area" VALUES ('370703', '寒亭区', '3707');
INSERT INTO "public"."base_area" VALUES ('370704', '坊子区', '3707');
INSERT INTO "public"."base_area" VALUES ('370705', '奎文区', '3707');
INSERT INTO "public"."base_area" VALUES ('370724', '临朐县', '3707');
INSERT INTO "public"."base_area" VALUES ('370725', '昌乐县', '3707');
INSERT INTO "public"."base_area" VALUES ('370772', '潍坊滨海经济技术开发区', '3707');
INSERT INTO "public"."base_area" VALUES ('370781', '青州市', '3707');
INSERT INTO "public"."base_area" VALUES ('370782', '诸城市', '3707');
INSERT INTO "public"."base_area" VALUES ('370783', '寿光市', '3707');
INSERT INTO "public"."base_area" VALUES ('370784', '安丘市', '3707');
INSERT INTO "public"."base_area" VALUES ('370785', '高密市', '3707');
INSERT INTO "public"."base_area" VALUES ('370786', '昌邑市', '3707');
INSERT INTO "public"."base_area" VALUES ('370811', '任城区', '3708');
INSERT INTO "public"."base_area" VALUES ('370812', '兖州区', '3708');
INSERT INTO "public"."base_area" VALUES ('370826', '微山县', '3708');
INSERT INTO "public"."base_area" VALUES ('370827', '鱼台县', '3708');
INSERT INTO "public"."base_area" VALUES ('370828', '金乡县', '3708');
INSERT INTO "public"."base_area" VALUES ('370829', '嘉祥县', '3708');
INSERT INTO "public"."base_area" VALUES ('370830', '汶上县', '3708');
INSERT INTO "public"."base_area" VALUES ('370831', '泗水县', '3708');
INSERT INTO "public"."base_area" VALUES ('370832', '梁山县', '3708');
INSERT INTO "public"."base_area" VALUES ('370871', '济宁高新技术产业开发区', '3708');
INSERT INTO "public"."base_area" VALUES ('370881', '曲阜市', '3708');
INSERT INTO "public"."base_area" VALUES ('370883', '邹城市', '3708');
INSERT INTO "public"."base_area" VALUES ('370902', '泰山区', '3709');
INSERT INTO "public"."base_area" VALUES ('370911', '岱岳区', '3709');
INSERT INTO "public"."base_area" VALUES ('370921', '宁阳县', '3709');
INSERT INTO "public"."base_area" VALUES ('370923', '东平县', '3709');
INSERT INTO "public"."base_area" VALUES ('370982', '新泰市', '3709');
INSERT INTO "public"."base_area" VALUES ('370983', '肥城市', '3709');
INSERT INTO "public"."base_area" VALUES ('371002', '环翠区', '3710');
INSERT INTO "public"."base_area" VALUES ('371003', '文登区', '3710');
INSERT INTO "public"."base_area" VALUES ('371071', '威海火炬高技术产业开发区', '3710');
INSERT INTO "public"."base_area" VALUES ('371072', '威海经济技术开发区', '3710');
INSERT INTO "public"."base_area" VALUES ('371073', '威海临港经济技术开发区', '3710');
INSERT INTO "public"."base_area" VALUES ('371082', '荣成市', '3710');
INSERT INTO "public"."base_area" VALUES ('371083', '乳山市', '3710');
INSERT INTO "public"."base_area" VALUES ('371102', '东港区', '3711');
INSERT INTO "public"."base_area" VALUES ('371103', '岚山区', '3711');
INSERT INTO "public"."base_area" VALUES ('371121', '五莲县', '3711');
INSERT INTO "public"."base_area" VALUES ('371122', '莒县', '3711');
INSERT INTO "public"."base_area" VALUES ('371171', '日照经济技术开发区', '3711');
INSERT INTO "public"."base_area" VALUES ('371302', '兰山区', '3713');
INSERT INTO "public"."base_area" VALUES ('371311', '罗庄区', '3713');
INSERT INTO "public"."base_area" VALUES ('371312', '河东区', '3713');
INSERT INTO "public"."base_area" VALUES ('371321', '沂南县', '3713');
INSERT INTO "public"."base_area" VALUES ('371322', '郯城县', '3713');
INSERT INTO "public"."base_area" VALUES ('371323', '沂水县', '3713');
INSERT INTO "public"."base_area" VALUES ('371324', '兰陵县', '3713');
INSERT INTO "public"."base_area" VALUES ('371325', '费县', '3713');
INSERT INTO "public"."base_area" VALUES ('371326', '平邑县', '3713');
INSERT INTO "public"."base_area" VALUES ('371327', '莒南县', '3713');
INSERT INTO "public"."base_area" VALUES ('371328', '蒙阴县', '3713');
INSERT INTO "public"."base_area" VALUES ('371329', '临沭县', '3713');
INSERT INTO "public"."base_area" VALUES ('371371', '临沂高新技术产业开发区', '3713');
INSERT INTO "public"."base_area" VALUES ('371402', '德城区', '3714');
INSERT INTO "public"."base_area" VALUES ('371403', '陵城区', '3714');
INSERT INTO "public"."base_area" VALUES ('371422', '宁津县', '3714');
INSERT INTO "public"."base_area" VALUES ('371423', '庆云县', '3714');
INSERT INTO "public"."base_area" VALUES ('371424', '临邑县', '3714');
INSERT INTO "public"."base_area" VALUES ('371425', '齐河县', '3714');
INSERT INTO "public"."base_area" VALUES ('371426', '平原县', '3714');
INSERT INTO "public"."base_area" VALUES ('371427', '夏津县', '3714');
INSERT INTO "public"."base_area" VALUES ('371428', '武城县', '3714');
INSERT INTO "public"."base_area" VALUES ('371471', '德州天衢新区', '3714');
INSERT INTO "public"."base_area" VALUES ('371481', '乐陵市', '3714');
INSERT INTO "public"."base_area" VALUES ('371482', '禹城市', '3714');
INSERT INTO "public"."base_area" VALUES ('371502', '东昌府区', '3715');
INSERT INTO "public"."base_area" VALUES ('371503', '茌平区', '3715');
INSERT INTO "public"."base_area" VALUES ('371521', '阳谷县', '3715');
INSERT INTO "public"."base_area" VALUES ('371522', '莘县', '3715');
INSERT INTO "public"."base_area" VALUES ('371524', '东阿县', '3715');
INSERT INTO "public"."base_area" VALUES ('371525', '冠县', '3715');
INSERT INTO "public"."base_area" VALUES ('371526', '高唐县', '3715');
INSERT INTO "public"."base_area" VALUES ('371581', '临清市', '3715');
INSERT INTO "public"."base_area" VALUES ('371602', '滨城区', '3716');
INSERT INTO "public"."base_area" VALUES ('371603', '沾化区', '3716');
INSERT INTO "public"."base_area" VALUES ('371621', '惠民县', '3716');
INSERT INTO "public"."base_area" VALUES ('371622', '阳信县', '3716');
INSERT INTO "public"."base_area" VALUES ('371623', '无棣县', '3716');
INSERT INTO "public"."base_area" VALUES ('371625', '博兴县', '3716');
INSERT INTO "public"."base_area" VALUES ('371681', '邹平市', '3716');
INSERT INTO "public"."base_area" VALUES ('371702', '牡丹区', '3717');
INSERT INTO "public"."base_area" VALUES ('371703', '定陶区', '3717');
INSERT INTO "public"."base_area" VALUES ('371721', '曹县', '3717');
INSERT INTO "public"."base_area" VALUES ('371722', '单县', '3717');
INSERT INTO "public"."base_area" VALUES ('371723', '成武县', '3717');
INSERT INTO "public"."base_area" VALUES ('371724', '巨野县', '3717');
INSERT INTO "public"."base_area" VALUES ('371725', '郓城县', '3717');
INSERT INTO "public"."base_area" VALUES ('371726', '鄄城县', '3717');
INSERT INTO "public"."base_area" VALUES ('371728', '东明县', '3717');
INSERT INTO "public"."base_area" VALUES ('371771', '菏泽经济技术开发区', '3717');
INSERT INTO "public"."base_area" VALUES ('371772', '菏泽高新技术开发区', '3717');
INSERT INTO "public"."base_area" VALUES ('410102', '中原区', '4101');
INSERT INTO "public"."base_area" VALUES ('410103', '二七区', '4101');
INSERT INTO "public"."base_area" VALUES ('410104', '管城回族区', '4101');
INSERT INTO "public"."base_area" VALUES ('410105', '金水区', '4101');
INSERT INTO "public"."base_area" VALUES ('410106', '上街区', '4101');
INSERT INTO "public"."base_area" VALUES ('410108', '惠济区', '4101');
INSERT INTO "public"."base_area" VALUES ('410122', '中牟县', '4101');
INSERT INTO "public"."base_area" VALUES ('410171', '郑州经济技术开发区', '4101');
INSERT INTO "public"."base_area" VALUES ('410172', '郑州高新技术产业开发区', '4101');
INSERT INTO "public"."base_area" VALUES ('410173', '郑州航空港经济综合实验区', '4101');
INSERT INTO "public"."base_area" VALUES ('410181', '巩义市', '4101');
INSERT INTO "public"."base_area" VALUES ('410182', '荥阳市', '4101');
INSERT INTO "public"."base_area" VALUES ('410183', '新密市', '4101');
INSERT INTO "public"."base_area" VALUES ('410184', '新郑市', '4101');
INSERT INTO "public"."base_area" VALUES ('410185', '登封市', '4101');
INSERT INTO "public"."base_area" VALUES ('410202', '龙亭区', '4102');
INSERT INTO "public"."base_area" VALUES ('410203', '顺河回族区', '4102');
INSERT INTO "public"."base_area" VALUES ('410204', '鼓楼区', '4102');
INSERT INTO "public"."base_area" VALUES ('410205', '禹王台区', '4102');
INSERT INTO "public"."base_area" VALUES ('410212', '祥符区', '4102');
INSERT INTO "public"."base_area" VALUES ('410221', '杞县', '4102');
INSERT INTO "public"."base_area" VALUES ('410222', '通许县', '4102');
INSERT INTO "public"."base_area" VALUES ('410223', '尉氏县', '4102');
INSERT INTO "public"."base_area" VALUES ('410225', '兰考县', '4102');
INSERT INTO "public"."base_area" VALUES ('410302', '老城区', '4103');
INSERT INTO "public"."base_area" VALUES ('410303', '西工区', '4103');
INSERT INTO "public"."base_area" VALUES ('410304', '瀍河回族区', '4103');
INSERT INTO "public"."base_area" VALUES ('410305', '涧西区', '4103');
INSERT INTO "public"."base_area" VALUES ('410307', '偃师区', '4103');
INSERT INTO "public"."base_area" VALUES ('410308', '孟津区', '4103');
INSERT INTO "public"."base_area" VALUES ('410311', '洛龙区', '4103');
INSERT INTO "public"."base_area" VALUES ('410323', '新安县', '4103');
INSERT INTO "public"."base_area" VALUES ('410324', '栾川县', '4103');
INSERT INTO "public"."base_area" VALUES ('410325', '嵩县', '4103');
INSERT INTO "public"."base_area" VALUES ('410326', '汝阳县', '4103');
INSERT INTO "public"."base_area" VALUES ('410327', '宜阳县', '4103');
INSERT INTO "public"."base_area" VALUES ('410328', '洛宁县', '4103');
INSERT INTO "public"."base_area" VALUES ('410329', '伊川县', '4103');
INSERT INTO "public"."base_area" VALUES ('410371', '洛阳高新技术产业开发区', '4103');
INSERT INTO "public"."base_area" VALUES ('410402', '新华区', '4104');
INSERT INTO "public"."base_area" VALUES ('410403', '卫东区', '4104');
INSERT INTO "public"."base_area" VALUES ('410404', '石龙区', '4104');
INSERT INTO "public"."base_area" VALUES ('410411', '湛河区', '4104');
INSERT INTO "public"."base_area" VALUES ('410421', '宝丰县', '4104');
INSERT INTO "public"."base_area" VALUES ('410422', '叶县', '4104');
INSERT INTO "public"."base_area" VALUES ('410423', '鲁山县', '4104');
INSERT INTO "public"."base_area" VALUES ('410425', '郏县', '4104');
INSERT INTO "public"."base_area" VALUES ('410471', '平顶山高新技术产业开发区', '4104');
INSERT INTO "public"."base_area" VALUES ('410472', '平顶山市城乡一体化示范区', '4104');
INSERT INTO "public"."base_area" VALUES ('410481', '舞钢市', '4104');
INSERT INTO "public"."base_area" VALUES ('410482', '汝州市', '4104');
INSERT INTO "public"."base_area" VALUES ('410502', '文峰区', '4105');
INSERT INTO "public"."base_area" VALUES ('410503', '北关区', '4105');
INSERT INTO "public"."base_area" VALUES ('410505', '殷都区', '4105');
INSERT INTO "public"."base_area" VALUES ('410506', '龙安区', '4105');
INSERT INTO "public"."base_area" VALUES ('410522', '安阳县', '4105');
INSERT INTO "public"."base_area" VALUES ('410523', '汤阴县', '4105');
INSERT INTO "public"."base_area" VALUES ('410526', '滑县', '4105');
INSERT INTO "public"."base_area" VALUES ('410527', '内黄县', '4105');
INSERT INTO "public"."base_area" VALUES ('410571', '安阳高新技术产业开发区', '4105');
INSERT INTO "public"."base_area" VALUES ('410581', '林州市', '4105');
INSERT INTO "public"."base_area" VALUES ('410602', '鹤山区', '4106');
INSERT INTO "public"."base_area" VALUES ('410603', '山城区', '4106');
INSERT INTO "public"."base_area" VALUES ('410611', '淇滨区', '4106');
INSERT INTO "public"."base_area" VALUES ('410621', '浚县', '4106');
INSERT INTO "public"."base_area" VALUES ('410622', '淇县', '4106');
INSERT INTO "public"."base_area" VALUES ('410671', '鹤壁经济技术开发区', '4106');
INSERT INTO "public"."base_area" VALUES ('410702', '红旗区', '4107');
INSERT INTO "public"."base_area" VALUES ('410703', '卫滨区', '4107');
INSERT INTO "public"."base_area" VALUES ('410704', '凤泉区', '4107');
INSERT INTO "public"."base_area" VALUES ('410711', '牧野区', '4107');
INSERT INTO "public"."base_area" VALUES ('410721', '新乡县', '4107');
INSERT INTO "public"."base_area" VALUES ('410724', '获嘉县', '4107');
INSERT INTO "public"."base_area" VALUES ('410725', '原阳县', '4107');
INSERT INTO "public"."base_area" VALUES ('410726', '延津县', '4107');
INSERT INTO "public"."base_area" VALUES ('410727', '封丘县', '4107');
INSERT INTO "public"."base_area" VALUES ('410771', '新乡高新技术产业开发区', '4107');
INSERT INTO "public"."base_area" VALUES ('410772', '新乡经济技术开发区', '4107');
INSERT INTO "public"."base_area" VALUES ('410773', '新乡市平原城乡一体化示范区', '4107');
INSERT INTO "public"."base_area" VALUES ('410781', '卫辉市', '4107');
INSERT INTO "public"."base_area" VALUES ('410782', '辉县市', '4107');
INSERT INTO "public"."base_area" VALUES ('410783', '长垣市', '4107');
INSERT INTO "public"."base_area" VALUES ('410802', '解放区', '4108');
INSERT INTO "public"."base_area" VALUES ('410803', '中站区', '4108');
INSERT INTO "public"."base_area" VALUES ('410804', '马村区', '4108');
INSERT INTO "public"."base_area" VALUES ('410811', '山阳区', '4108');
INSERT INTO "public"."base_area" VALUES ('410821', '修武县', '4108');
INSERT INTO "public"."base_area" VALUES ('410822', '博爱县', '4108');
INSERT INTO "public"."base_area" VALUES ('410823', '武陟县', '4108');
INSERT INTO "public"."base_area" VALUES ('410825', '温县', '4108');
INSERT INTO "public"."base_area" VALUES ('410871', '焦作城乡一体化示范区', '4108');
INSERT INTO "public"."base_area" VALUES ('410882', '沁阳市', '4108');
INSERT INTO "public"."base_area" VALUES ('410883', '孟州市', '4108');
INSERT INTO "public"."base_area" VALUES ('410902', '华龙区', '4109');
INSERT INTO "public"."base_area" VALUES ('410922', '清丰县', '4109');
INSERT INTO "public"."base_area" VALUES ('410923', '南乐县', '4109');
INSERT INTO "public"."base_area" VALUES ('410926', '范县', '4109');
INSERT INTO "public"."base_area" VALUES ('410927', '台前县', '4109');
INSERT INTO "public"."base_area" VALUES ('410928', '濮阳县', '4109');
INSERT INTO "public"."base_area" VALUES ('410971', '河南濮阳工业园区', '4109');
INSERT INTO "public"."base_area" VALUES ('410972', '濮阳经济技术开发区', '4109');
INSERT INTO "public"."base_area" VALUES ('411002', '魏都区', '4110');
INSERT INTO "public"."base_area" VALUES ('411003', '建安区', '4110');
INSERT INTO "public"."base_area" VALUES ('411024', '鄢陵县', '4110');
INSERT INTO "public"."base_area" VALUES ('411025', '襄城县', '4110');
INSERT INTO "public"."base_area" VALUES ('411071', '许昌经济技术开发区', '4110');
INSERT INTO "public"."base_area" VALUES ('411081', '禹州市', '4110');
INSERT INTO "public"."base_area" VALUES ('411082', '长葛市', '4110');
INSERT INTO "public"."base_area" VALUES ('411102', '源汇区', '4111');
INSERT INTO "public"."base_area" VALUES ('411103', '郾城区', '4111');
INSERT INTO "public"."base_area" VALUES ('411104', '召陵区', '4111');
INSERT INTO "public"."base_area" VALUES ('411121', '舞阳县', '4111');
INSERT INTO "public"."base_area" VALUES ('411122', '临颍县', '4111');
INSERT INTO "public"."base_area" VALUES ('411171', '漯河经济技术开发区', '4111');
INSERT INTO "public"."base_area" VALUES ('411202', '湖滨区', '4112');
INSERT INTO "public"."base_area" VALUES ('411203', '陕州区', '4112');
INSERT INTO "public"."base_area" VALUES ('411221', '渑池县', '4112');
INSERT INTO "public"."base_area" VALUES ('411224', '卢氏县', '4112');
INSERT INTO "public"."base_area" VALUES ('411271', '河南三门峡经济开发区', '4112');
INSERT INTO "public"."base_area" VALUES ('411281', '义马市', '4112');
INSERT INTO "public"."base_area" VALUES ('411282', '灵宝市', '4112');
INSERT INTO "public"."base_area" VALUES ('411302', '宛城区', '4113');
INSERT INTO "public"."base_area" VALUES ('411303', '卧龙区', '4113');
INSERT INTO "public"."base_area" VALUES ('411321', '南召县', '4113');
INSERT INTO "public"."base_area" VALUES ('411322', '方城县', '4113');
INSERT INTO "public"."base_area" VALUES ('411323', '西峡县', '4113');
INSERT INTO "public"."base_area" VALUES ('411324', '镇平县', '4113');
INSERT INTO "public"."base_area" VALUES ('411325', '内乡县', '4113');
INSERT INTO "public"."base_area" VALUES ('411326', '淅川县', '4113');
INSERT INTO "public"."base_area" VALUES ('411327', '社旗县', '4113');
INSERT INTO "public"."base_area" VALUES ('411328', '唐河县', '4113');
INSERT INTO "public"."base_area" VALUES ('411329', '新野县', '4113');
INSERT INTO "public"."base_area" VALUES ('411330', '桐柏县', '4113');
INSERT INTO "public"."base_area" VALUES ('411371', '南阳高新技术产业开发区', '4113');
INSERT INTO "public"."base_area" VALUES ('411372', '南阳市城乡一体化示范区', '4113');
INSERT INTO "public"."base_area" VALUES ('411381', '邓州市', '4113');
INSERT INTO "public"."base_area" VALUES ('411402', '梁园区', '4114');
INSERT INTO "public"."base_area" VALUES ('411403', '睢阳区', '4114');
INSERT INTO "public"."base_area" VALUES ('411421', '民权县', '4114');
INSERT INTO "public"."base_area" VALUES ('411422', '睢县', '4114');
INSERT INTO "public"."base_area" VALUES ('411423', '宁陵县', '4114');
INSERT INTO "public"."base_area" VALUES ('411424', '柘城县', '4114');
INSERT INTO "public"."base_area" VALUES ('411425', '虞城县', '4114');
INSERT INTO "public"."base_area" VALUES ('411426', '夏邑县', '4114');
INSERT INTO "public"."base_area" VALUES ('411471', '豫东综合物流产业聚集区', '4114');
INSERT INTO "public"."base_area" VALUES ('411472', '河南商丘经济开发区', '4114');
INSERT INTO "public"."base_area" VALUES ('411481', '永城市', '4114');
INSERT INTO "public"."base_area" VALUES ('411502', '浉河区', '4115');
INSERT INTO "public"."base_area" VALUES ('411503', '平桥区', '4115');
INSERT INTO "public"."base_area" VALUES ('411521', '罗山县', '4115');
INSERT INTO "public"."base_area" VALUES ('411522', '光山县', '4115');
INSERT INTO "public"."base_area" VALUES ('411523', '新县', '4115');
INSERT INTO "public"."base_area" VALUES ('411524', '商城县', '4115');
INSERT INTO "public"."base_area" VALUES ('411525', '固始县', '4115');
INSERT INTO "public"."base_area" VALUES ('411526', '潢川县', '4115');
INSERT INTO "public"."base_area" VALUES ('411527', '淮滨县', '4115');
INSERT INTO "public"."base_area" VALUES ('411528', '息县', '4115');
INSERT INTO "public"."base_area" VALUES ('411571', '信阳高新技术产业开发区', '4115');
INSERT INTO "public"."base_area" VALUES ('411602', '川汇区', '4116');
INSERT INTO "public"."base_area" VALUES ('411603', '淮阳区', '4116');
INSERT INTO "public"."base_area" VALUES ('411621', '扶沟县', '4116');
INSERT INTO "public"."base_area" VALUES ('411622', '西华县', '4116');
INSERT INTO "public"."base_area" VALUES ('411623', '商水县', '4116');
INSERT INTO "public"."base_area" VALUES ('411624', '沈丘县', '4116');
INSERT INTO "public"."base_area" VALUES ('411625', '郸城县', '4116');
INSERT INTO "public"."base_area" VALUES ('411627', '太康县', '4116');
INSERT INTO "public"."base_area" VALUES ('411628', '鹿邑县', '4116');
INSERT INTO "public"."base_area" VALUES ('411671', '河南周口经济开发区', '4116');
INSERT INTO "public"."base_area" VALUES ('411681', '项城市', '4116');
INSERT INTO "public"."base_area" VALUES ('411702', '驿城区', '4117');
INSERT INTO "public"."base_area" VALUES ('411721', '西平县', '4117');
INSERT INTO "public"."base_area" VALUES ('411722', '上蔡县', '4117');
INSERT INTO "public"."base_area" VALUES ('411723', '平舆县', '4117');
INSERT INTO "public"."base_area" VALUES ('411724', '正阳县', '4117');
INSERT INTO "public"."base_area" VALUES ('411725', '确山县', '4117');
INSERT INTO "public"."base_area" VALUES ('411726', '泌阳县', '4117');
INSERT INTO "public"."base_area" VALUES ('411727', '汝南县', '4117');
INSERT INTO "public"."base_area" VALUES ('411728', '遂平县', '4117');
INSERT INTO "public"."base_area" VALUES ('411729', '新蔡县', '4117');
INSERT INTO "public"."base_area" VALUES ('411771', '河南驻马店经济开发区', '4117');
INSERT INTO "public"."base_area" VALUES ('419001', '济源市', '4190');
INSERT INTO "public"."base_area" VALUES ('420102', '江岸区', '4201');
INSERT INTO "public"."base_area" VALUES ('420103', '江汉区', '4201');
INSERT INTO "public"."base_area" VALUES ('420104', '硚口区', '4201');
INSERT INTO "public"."base_area" VALUES ('420105', '汉阳区', '4201');
INSERT INTO "public"."base_area" VALUES ('420106', '武昌区', '4201');
INSERT INTO "public"."base_area" VALUES ('420107', '青山区', '4201');
INSERT INTO "public"."base_area" VALUES ('420111', '洪山区', '4201');
INSERT INTO "public"."base_area" VALUES ('420112', '东西湖区', '4201');
INSERT INTO "public"."base_area" VALUES ('420113', '汉南区', '4201');
INSERT INTO "public"."base_area" VALUES ('420114', '蔡甸区', '4201');
INSERT INTO "public"."base_area" VALUES ('420115', '江夏区', '4201');
INSERT INTO "public"."base_area" VALUES ('420116', '黄陂区', '4201');
INSERT INTO "public"."base_area" VALUES ('420117', '新洲区', '4201');
INSERT INTO "public"."base_area" VALUES ('420202', '黄石港区', '4202');
INSERT INTO "public"."base_area" VALUES ('420203', '西塞山区', '4202');
INSERT INTO "public"."base_area" VALUES ('420204', '下陆区', '4202');
INSERT INTO "public"."base_area" VALUES ('420205', '铁山区', '4202');
INSERT INTO "public"."base_area" VALUES ('420222', '阳新县', '4202');
INSERT INTO "public"."base_area" VALUES ('420281', '大冶市', '4202');
INSERT INTO "public"."base_area" VALUES ('420302', '茅箭区', '4203');
INSERT INTO "public"."base_area" VALUES ('420303', '张湾区', '4203');
INSERT INTO "public"."base_area" VALUES ('420304', '郧阳区', '4203');
INSERT INTO "public"."base_area" VALUES ('420322', '郧西县', '4203');
INSERT INTO "public"."base_area" VALUES ('420323', '竹山县', '4203');
INSERT INTO "public"."base_area" VALUES ('420324', '竹溪县', '4203');
INSERT INTO "public"."base_area" VALUES ('420325', '房县', '4203');
INSERT INTO "public"."base_area" VALUES ('420381', '丹江口市', '4203');
INSERT INTO "public"."base_area" VALUES ('420502', '西陵区', '4205');
INSERT INTO "public"."base_area" VALUES ('420503', '伍家岗区', '4205');
INSERT INTO "public"."base_area" VALUES ('420504', '点军区', '4205');
INSERT INTO "public"."base_area" VALUES ('420505', '猇亭区', '4205');
INSERT INTO "public"."base_area" VALUES ('420506', '夷陵区', '4205');
INSERT INTO "public"."base_area" VALUES ('420525', '远安县', '4205');
INSERT INTO "public"."base_area" VALUES ('420526', '兴山县', '4205');
INSERT INTO "public"."base_area" VALUES ('420527', '秭归县', '4205');
INSERT INTO "public"."base_area" VALUES ('420528', '长阳土家族自治县', '4205');
INSERT INTO "public"."base_area" VALUES ('420529', '五峰土家族自治县', '4205');
INSERT INTO "public"."base_area" VALUES ('420581', '宜都市', '4205');
INSERT INTO "public"."base_area" VALUES ('420582', '当阳市', '4205');
INSERT INTO "public"."base_area" VALUES ('420583', '枝江市', '4205');
INSERT INTO "public"."base_area" VALUES ('420602', '襄城区', '4206');
INSERT INTO "public"."base_area" VALUES ('420606', '樊城区', '4206');
INSERT INTO "public"."base_area" VALUES ('420607', '襄州区', '4206');
INSERT INTO "public"."base_area" VALUES ('420624', '南漳县', '4206');
INSERT INTO "public"."base_area" VALUES ('420625', '谷城县', '4206');
INSERT INTO "public"."base_area" VALUES ('420626', '保康县', '4206');
INSERT INTO "public"."base_area" VALUES ('420682', '老河口市', '4206');
INSERT INTO "public"."base_area" VALUES ('420683', '枣阳市', '4206');
INSERT INTO "public"."base_area" VALUES ('420684', '宜城市', '4206');
INSERT INTO "public"."base_area" VALUES ('420702', '梁子湖区', '4207');
INSERT INTO "public"."base_area" VALUES ('420703', '华容区', '4207');
INSERT INTO "public"."base_area" VALUES ('420704', '鄂城区', '4207');
INSERT INTO "public"."base_area" VALUES ('420802', '东宝区', '4208');
INSERT INTO "public"."base_area" VALUES ('420804', '掇刀区', '4208');
INSERT INTO "public"."base_area" VALUES ('420822', '沙洋县', '4208');
INSERT INTO "public"."base_area" VALUES ('420881', '钟祥市', '4208');
INSERT INTO "public"."base_area" VALUES ('420882', '京山市', '4208');
INSERT INTO "public"."base_area" VALUES ('420902', '孝南区', '4209');
INSERT INTO "public"."base_area" VALUES ('420921', '孝昌县', '4209');
INSERT INTO "public"."base_area" VALUES ('420922', '大悟县', '4209');
INSERT INTO "public"."base_area" VALUES ('420923', '云梦县', '4209');
INSERT INTO "public"."base_area" VALUES ('420981', '应城市', '4209');
INSERT INTO "public"."base_area" VALUES ('420982', '安陆市', '4209');
INSERT INTO "public"."base_area" VALUES ('420984', '汉川市', '4209');
INSERT INTO "public"."base_area" VALUES ('421002', '沙市区', '4210');
INSERT INTO "public"."base_area" VALUES ('421003', '荆州区', '4210');
INSERT INTO "public"."base_area" VALUES ('421022', '公安县', '4210');
INSERT INTO "public"."base_area" VALUES ('421024', '江陵县', '4210');
INSERT INTO "public"."base_area" VALUES ('421071', '荆州经济技术开发区', '4210');
INSERT INTO "public"."base_area" VALUES ('421081', '石首市', '4210');
INSERT INTO "public"."base_area" VALUES ('421083', '洪湖市', '4210');
INSERT INTO "public"."base_area" VALUES ('421087', '松滋市', '4210');
INSERT INTO "public"."base_area" VALUES ('421088', '监利市', '4210');
INSERT INTO "public"."base_area" VALUES ('421102', '黄州区', '4211');
INSERT INTO "public"."base_area" VALUES ('421121', '团风县', '4211');
INSERT INTO "public"."base_area" VALUES ('421122', '红安县', '4211');
INSERT INTO "public"."base_area" VALUES ('421123', '罗田县', '4211');
INSERT INTO "public"."base_area" VALUES ('421124', '英山县', '4211');
INSERT INTO "public"."base_area" VALUES ('421125', '浠水县', '4211');
INSERT INTO "public"."base_area" VALUES ('421126', '蕲春县', '4211');
INSERT INTO "public"."base_area" VALUES ('421127', '黄梅县', '4211');
INSERT INTO "public"."base_area" VALUES ('421171', '龙感湖管理区', '4211');
INSERT INTO "public"."base_area" VALUES ('421181', '麻城市', '4211');
INSERT INTO "public"."base_area" VALUES ('421182', '武穴市', '4211');
INSERT INTO "public"."base_area" VALUES ('421202', '咸安区', '4212');
INSERT INTO "public"."base_area" VALUES ('421221', '嘉鱼县', '4212');
INSERT INTO "public"."base_area" VALUES ('421222', '通城县', '4212');
INSERT INTO "public"."base_area" VALUES ('421223', '崇阳县', '4212');
INSERT INTO "public"."base_area" VALUES ('421224', '通山县', '4212');
INSERT INTO "public"."base_area" VALUES ('421281', '赤壁市', '4212');
INSERT INTO "public"."base_area" VALUES ('421303', '曾都区', '4213');
INSERT INTO "public"."base_area" VALUES ('421321', '随县', '4213');
INSERT INTO "public"."base_area" VALUES ('421381', '广水市', '4213');
INSERT INTO "public"."base_area" VALUES ('422801', '恩施市', '4228');
INSERT INTO "public"."base_area" VALUES ('422802', '利川市', '4228');
INSERT INTO "public"."base_area" VALUES ('422822', '建始县', '4228');
INSERT INTO "public"."base_area" VALUES ('422823', '巴东县', '4228');
INSERT INTO "public"."base_area" VALUES ('422825', '宣恩县', '4228');
INSERT INTO "public"."base_area" VALUES ('422826', '咸丰县', '4228');
INSERT INTO "public"."base_area" VALUES ('422827', '来凤县', '4228');
INSERT INTO "public"."base_area" VALUES ('422828', '鹤峰县', '4228');
INSERT INTO "public"."base_area" VALUES ('429004', '仙桃市', '4290');
INSERT INTO "public"."base_area" VALUES ('429005', '潜江市', '4290');
INSERT INTO "public"."base_area" VALUES ('429006', '天门市', '4290');
INSERT INTO "public"."base_area" VALUES ('429021', '神农架林区', '4290');
INSERT INTO "public"."base_area" VALUES ('430102', '芙蓉区', '4301');
INSERT INTO "public"."base_area" VALUES ('430103', '天心区', '4301');
INSERT INTO "public"."base_area" VALUES ('430104', '岳麓区', '4301');
INSERT INTO "public"."base_area" VALUES ('430105', '开福区', '4301');
INSERT INTO "public"."base_area" VALUES ('430111', '雨花区', '4301');
INSERT INTO "public"."base_area" VALUES ('430112', '望城区', '4301');
INSERT INTO "public"."base_area" VALUES ('430121', '长沙县', '4301');
INSERT INTO "public"."base_area" VALUES ('430181', '浏阳市', '4301');
INSERT INTO "public"."base_area" VALUES ('430182', '宁乡市', '4301');
INSERT INTO "public"."base_area" VALUES ('430202', '荷塘区', '4302');
INSERT INTO "public"."base_area" VALUES ('430203', '芦淞区', '4302');
INSERT INTO "public"."base_area" VALUES ('430204', '石峰区', '4302');
INSERT INTO "public"."base_area" VALUES ('430211', '天元区', '4302');
INSERT INTO "public"."base_area" VALUES ('430212', '渌口区', '4302');
INSERT INTO "public"."base_area" VALUES ('430223', '攸县', '4302');
INSERT INTO "public"."base_area" VALUES ('430224', '茶陵县', '4302');
INSERT INTO "public"."base_area" VALUES ('430225', '炎陵县', '4302');
INSERT INTO "public"."base_area" VALUES ('430281', '醴陵市', '4302');
INSERT INTO "public"."base_area" VALUES ('430302', '雨湖区', '4303');
INSERT INTO "public"."base_area" VALUES ('430304', '岳塘区', '4303');
INSERT INTO "public"."base_area" VALUES ('430321', '湘潭县', '4303');
INSERT INTO "public"."base_area" VALUES ('430371', '湖南湘潭高新技术产业园区', '4303');
INSERT INTO "public"."base_area" VALUES ('430372', '湘潭昭山示范区', '4303');
INSERT INTO "public"."base_area" VALUES ('430373', '湘潭九华示范区', '4303');
INSERT INTO "public"."base_area" VALUES ('430381', '湘乡市', '4303');
INSERT INTO "public"."base_area" VALUES ('430382', '韶山市', '4303');
INSERT INTO "public"."base_area" VALUES ('430405', '珠晖区', '4304');
INSERT INTO "public"."base_area" VALUES ('430406', '雁峰区', '4304');
INSERT INTO "public"."base_area" VALUES ('430407', '石鼓区', '4304');
INSERT INTO "public"."base_area" VALUES ('430408', '蒸湘区', '4304');
INSERT INTO "public"."base_area" VALUES ('430412', '南岳区', '4304');
INSERT INTO "public"."base_area" VALUES ('430421', '衡阳县', '4304');
INSERT INTO "public"."base_area" VALUES ('430422', '衡南县', '4304');
INSERT INTO "public"."base_area" VALUES ('430423', '衡山县', '4304');
INSERT INTO "public"."base_area" VALUES ('430424', '衡东县', '4304');
INSERT INTO "public"."base_area" VALUES ('430426', '祁东县', '4304');
INSERT INTO "public"."base_area" VALUES ('430471', '衡阳综合保税区', '4304');
INSERT INTO "public"."base_area" VALUES ('430472', '湖南衡阳高新技术产业园区', '4304');
INSERT INTO "public"."base_area" VALUES ('430473', '湖南衡阳松木经济开发区', '4304');
INSERT INTO "public"."base_area" VALUES ('430481', '耒阳市', '4304');
INSERT INTO "public"."base_area" VALUES ('430482', '常宁市', '4304');
INSERT INTO "public"."base_area" VALUES ('430502', '双清区', '4305');
INSERT INTO "public"."base_area" VALUES ('430503', '大祥区', '4305');
INSERT INTO "public"."base_area" VALUES ('430511', '北塔区', '4305');
INSERT INTO "public"."base_area" VALUES ('430522', '新邵县', '4305');
INSERT INTO "public"."base_area" VALUES ('430523', '邵阳县', '4305');
INSERT INTO "public"."base_area" VALUES ('430524', '隆回县', '4305');
INSERT INTO "public"."base_area" VALUES ('430525', '洞口县', '4305');
INSERT INTO "public"."base_area" VALUES ('430527', '绥宁县', '4305');
INSERT INTO "public"."base_area" VALUES ('430528', '新宁县', '4305');
INSERT INTO "public"."base_area" VALUES ('430529', '城步苗族自治县', '4305');
INSERT INTO "public"."base_area" VALUES ('430581', '武冈市', '4305');
INSERT INTO "public"."base_area" VALUES ('430582', '邵东市', '4305');
INSERT INTO "public"."base_area" VALUES ('430602', '岳阳楼区', '4306');
INSERT INTO "public"."base_area" VALUES ('430603', '云溪区', '4306');
INSERT INTO "public"."base_area" VALUES ('430611', '君山区', '4306');
INSERT INTO "public"."base_area" VALUES ('430621', '岳阳县', '4306');
INSERT INTO "public"."base_area" VALUES ('430623', '华容县', '4306');
INSERT INTO "public"."base_area" VALUES ('430624', '湘阴县', '4306');
INSERT INTO "public"."base_area" VALUES ('430626', '平江县', '4306');
INSERT INTO "public"."base_area" VALUES ('430671', '岳阳市屈原管理区', '4306');
INSERT INTO "public"."base_area" VALUES ('430681', '汨罗市', '4306');
INSERT INTO "public"."base_area" VALUES ('430682', '临湘市', '4306');
INSERT INTO "public"."base_area" VALUES ('430702', '武陵区', '4307');
INSERT INTO "public"."base_area" VALUES ('430703', '鼎城区', '4307');
INSERT INTO "public"."base_area" VALUES ('430721', '安乡县', '4307');
INSERT INTO "public"."base_area" VALUES ('430722', '汉寿县', '4307');
INSERT INTO "public"."base_area" VALUES ('430723', '澧县', '4307');
INSERT INTO "public"."base_area" VALUES ('430724', '临澧县', '4307');
INSERT INTO "public"."base_area" VALUES ('430725', '桃源县', '4307');
INSERT INTO "public"."base_area" VALUES ('430726', '石门县', '4307');
INSERT INTO "public"."base_area" VALUES ('430771', '常德市西洞庭管理区', '4307');
INSERT INTO "public"."base_area" VALUES ('430781', '津市市', '4307');
INSERT INTO "public"."base_area" VALUES ('430802', '永定区', '4308');
INSERT INTO "public"."base_area" VALUES ('430811', '武陵源区', '4308');
INSERT INTO "public"."base_area" VALUES ('430821', '慈利县', '4308');
INSERT INTO "public"."base_area" VALUES ('430822', '桑植县', '4308');
INSERT INTO "public"."base_area" VALUES ('430902', '资阳区', '4309');
INSERT INTO "public"."base_area" VALUES ('430903', '赫山区', '4309');
INSERT INTO "public"."base_area" VALUES ('430921', '南县', '4309');
INSERT INTO "public"."base_area" VALUES ('430922', '桃江县', '4309');
INSERT INTO "public"."base_area" VALUES ('430923', '安化县', '4309');
INSERT INTO "public"."base_area" VALUES ('430971', '益阳市大通湖管理区', '4309');
INSERT INTO "public"."base_area" VALUES ('430972', '湖南益阳高新技术产业园区', '4309');
INSERT INTO "public"."base_area" VALUES ('430981', '沅江市', '4309');
INSERT INTO "public"."base_area" VALUES ('431002', '北湖区', '4310');
INSERT INTO "public"."base_area" VALUES ('431003', '苏仙区', '4310');
INSERT INTO "public"."base_area" VALUES ('431021', '桂阳县', '4310');
INSERT INTO "public"."base_area" VALUES ('431022', '宜章县', '4310');
INSERT INTO "public"."base_area" VALUES ('431023', '永兴县', '4310');
INSERT INTO "public"."base_area" VALUES ('431024', '嘉禾县', '4310');
INSERT INTO "public"."base_area" VALUES ('431025', '临武县', '4310');
INSERT INTO "public"."base_area" VALUES ('431026', '汝城县', '4310');
INSERT INTO "public"."base_area" VALUES ('431027', '桂东县', '4310');
INSERT INTO "public"."base_area" VALUES ('431028', '安仁县', '4310');
INSERT INTO "public"."base_area" VALUES ('431081', '资兴市', '4310');
INSERT INTO "public"."base_area" VALUES ('431102', '零陵区', '4311');
INSERT INTO "public"."base_area" VALUES ('431103', '冷水滩区', '4311');
INSERT INTO "public"."base_area" VALUES ('431122', '东安县', '4311');
INSERT INTO "public"."base_area" VALUES ('431123', '双牌县', '4311');
INSERT INTO "public"."base_area" VALUES ('431124', '道县', '4311');
INSERT INTO "public"."base_area" VALUES ('431125', '江永县', '4311');
INSERT INTO "public"."base_area" VALUES ('431126', '宁远县', '4311');
INSERT INTO "public"."base_area" VALUES ('431127', '蓝山县', '4311');
INSERT INTO "public"."base_area" VALUES ('431128', '新田县', '4311');
INSERT INTO "public"."base_area" VALUES ('431129', '江华瑶族自治县', '4311');
INSERT INTO "public"."base_area" VALUES ('431171', '永州经济技术开发区', '4311');
INSERT INTO "public"."base_area" VALUES ('431173', '永州市回龙圩管理区', '4311');
INSERT INTO "public"."base_area" VALUES ('431181', '祁阳市', '4311');
INSERT INTO "public"."base_area" VALUES ('431202', '鹤城区', '4312');
INSERT INTO "public"."base_area" VALUES ('431221', '中方县', '4312');
INSERT INTO "public"."base_area" VALUES ('431222', '沅陵县', '4312');
INSERT INTO "public"."base_area" VALUES ('431223', '辰溪县', '4312');
INSERT INTO "public"."base_area" VALUES ('431224', '溆浦县', '4312');
INSERT INTO "public"."base_area" VALUES ('431225', '会同县', '4312');
INSERT INTO "public"."base_area" VALUES ('431226', '麻阳苗族自治县', '4312');
INSERT INTO "public"."base_area" VALUES ('431227', '新晃侗族自治县', '4312');
INSERT INTO "public"."base_area" VALUES ('431228', '芷江侗族自治县', '4312');
INSERT INTO "public"."base_area" VALUES ('431229', '靖州苗族侗族自治县', '4312');
INSERT INTO "public"."base_area" VALUES ('431230', '通道侗族自治县', '4312');
INSERT INTO "public"."base_area" VALUES ('431271', '怀化市洪江管理区', '4312');
INSERT INTO "public"."base_area" VALUES ('431281', '洪江市', '4312');
INSERT INTO "public"."base_area" VALUES ('431302', '娄星区', '4313');
INSERT INTO "public"."base_area" VALUES ('431321', '双峰县', '4313');
INSERT INTO "public"."base_area" VALUES ('431322', '新化县', '4313');
INSERT INTO "public"."base_area" VALUES ('431381', '冷水江市', '4313');
INSERT INTO "public"."base_area" VALUES ('431382', '涟源市', '4313');
INSERT INTO "public"."base_area" VALUES ('433101', '吉首市', '4331');
INSERT INTO "public"."base_area" VALUES ('433122', '泸溪县', '4331');
INSERT INTO "public"."base_area" VALUES ('433123', '凤凰县', '4331');
INSERT INTO "public"."base_area" VALUES ('433124', '花垣县', '4331');
INSERT INTO "public"."base_area" VALUES ('433125', '保靖县', '4331');
INSERT INTO "public"."base_area" VALUES ('433126', '古丈县', '4331');
INSERT INTO "public"."base_area" VALUES ('433127', '永顺县', '4331');
INSERT INTO "public"."base_area" VALUES ('433130', '龙山县', '4331');
INSERT INTO "public"."base_area" VALUES ('440103', '荔湾区', '4401');
INSERT INTO "public"."base_area" VALUES ('440104', '越秀区', '4401');
INSERT INTO "public"."base_area" VALUES ('440105', '海珠区', '4401');
INSERT INTO "public"."base_area" VALUES ('440106', '天河区', '4401');
INSERT INTO "public"."base_area" VALUES ('440111', '白云区', '4401');
INSERT INTO "public"."base_area" VALUES ('440112', '黄埔区', '4401');
INSERT INTO "public"."base_area" VALUES ('440113', '番禺区', '4401');
INSERT INTO "public"."base_area" VALUES ('440114', '花都区', '4401');
INSERT INTO "public"."base_area" VALUES ('440115', '南沙区', '4401');
INSERT INTO "public"."base_area" VALUES ('440117', '从化区', '4401');
INSERT INTO "public"."base_area" VALUES ('440118', '增城区', '4401');
INSERT INTO "public"."base_area" VALUES ('440203', '武江区', '4402');
INSERT INTO "public"."base_area" VALUES ('440204', '浈江区', '4402');
INSERT INTO "public"."base_area" VALUES ('440205', '曲江区', '4402');
INSERT INTO "public"."base_area" VALUES ('440222', '始兴县', '4402');
INSERT INTO "public"."base_area" VALUES ('440224', '仁化县', '4402');
INSERT INTO "public"."base_area" VALUES ('440229', '翁源县', '4402');
INSERT INTO "public"."base_area" VALUES ('440232', '乳源瑶族自治县', '4402');
INSERT INTO "public"."base_area" VALUES ('440233', '新丰县', '4402');
INSERT INTO "public"."base_area" VALUES ('440281', '乐昌市', '4402');
INSERT INTO "public"."base_area" VALUES ('440282', '南雄市', '4402');
INSERT INTO "public"."base_area" VALUES ('440303', '罗湖区', '4403');
INSERT INTO "public"."base_area" VALUES ('440304', '福田区', '4403');
INSERT INTO "public"."base_area" VALUES ('440305', '南山区', '4403');
INSERT INTO "public"."base_area" VALUES ('440306', '宝安区', '4403');
INSERT INTO "public"."base_area" VALUES ('440307', '龙岗区', '4403');
INSERT INTO "public"."base_area" VALUES ('440308', '盐田区', '4403');
INSERT INTO "public"."base_area" VALUES ('440309', '龙华区', '4403');
INSERT INTO "public"."base_area" VALUES ('440310', '坪山区', '4403');
INSERT INTO "public"."base_area" VALUES ('440311', '光明区', '4403');
INSERT INTO "public"."base_area" VALUES ('440402', '香洲区', '4404');
INSERT INTO "public"."base_area" VALUES ('440403', '斗门区', '4404');
INSERT INTO "public"."base_area" VALUES ('440404', '金湾区', '4404');
INSERT INTO "public"."base_area" VALUES ('440507', '龙湖区', '4405');
INSERT INTO "public"."base_area" VALUES ('440511', '金平区', '4405');
INSERT INTO "public"."base_area" VALUES ('440512', '濠江区', '4405');
INSERT INTO "public"."base_area" VALUES ('440513', '潮阳区', '4405');
INSERT INTO "public"."base_area" VALUES ('440514', '潮南区', '4405');
INSERT INTO "public"."base_area" VALUES ('440515', '澄海区', '4405');
INSERT INTO "public"."base_area" VALUES ('440523', '南澳县', '4405');
INSERT INTO "public"."base_area" VALUES ('440604', '禅城区', '4406');
INSERT INTO "public"."base_area" VALUES ('440605', '南海区', '4406');
INSERT INTO "public"."base_area" VALUES ('440606', '顺德区', '4406');
INSERT INTO "public"."base_area" VALUES ('440607', '三水区', '4406');
INSERT INTO "public"."base_area" VALUES ('440608', '高明区', '4406');
INSERT INTO "public"."base_area" VALUES ('440703', '蓬江区', '4407');
INSERT INTO "public"."base_area" VALUES ('440704', '江海区', '4407');
INSERT INTO "public"."base_area" VALUES ('440705', '新会区', '4407');
INSERT INTO "public"."base_area" VALUES ('440781', '台山市', '4407');
INSERT INTO "public"."base_area" VALUES ('440783', '开平市', '4407');
INSERT INTO "public"."base_area" VALUES ('440784', '鹤山市', '4407');
INSERT INTO "public"."base_area" VALUES ('440785', '恩平市', '4407');
INSERT INTO "public"."base_area" VALUES ('440802', '赤坎区', '4408');
INSERT INTO "public"."base_area" VALUES ('440803', '霞山区', '4408');
INSERT INTO "public"."base_area" VALUES ('440804', '坡头区', '4408');
INSERT INTO "public"."base_area" VALUES ('440811', '麻章区', '4408');
INSERT INTO "public"."base_area" VALUES ('440823', '遂溪县', '4408');
INSERT INTO "public"."base_area" VALUES ('440825', '徐闻县', '4408');
INSERT INTO "public"."base_area" VALUES ('440881', '廉江市', '4408');
INSERT INTO "public"."base_area" VALUES ('440882', '雷州市', '4408');
INSERT INTO "public"."base_area" VALUES ('440883', '吴川市', '4408');
INSERT INTO "public"."base_area" VALUES ('440902', '茂南区', '4409');
INSERT INTO "public"."base_area" VALUES ('440904', '电白区', '4409');
INSERT INTO "public"."base_area" VALUES ('440981', '高州市', '4409');
INSERT INTO "public"."base_area" VALUES ('440982', '化州市', '4409');
INSERT INTO "public"."base_area" VALUES ('440983', '信宜市', '4409');
INSERT INTO "public"."base_area" VALUES ('441202', '端州区', '4412');
INSERT INTO "public"."base_area" VALUES ('441203', '鼎湖区', '4412');
INSERT INTO "public"."base_area" VALUES ('441204', '高要区', '4412');
INSERT INTO "public"."base_area" VALUES ('441223', '广宁县', '4412');
INSERT INTO "public"."base_area" VALUES ('441224', '怀集县', '4412');
INSERT INTO "public"."base_area" VALUES ('441225', '封开县', '4412');
INSERT INTO "public"."base_area" VALUES ('441226', '德庆县', '4412');
INSERT INTO "public"."base_area" VALUES ('441284', '四会市', '4412');
INSERT INTO "public"."base_area" VALUES ('441302', '惠城区', '4413');
INSERT INTO "public"."base_area" VALUES ('441303', '惠阳区', '4413');
INSERT INTO "public"."base_area" VALUES ('441322', '博罗县', '4413');
INSERT INTO "public"."base_area" VALUES ('441323', '惠东县', '4413');
INSERT INTO "public"."base_area" VALUES ('441324', '龙门县', '4413');
INSERT INTO "public"."base_area" VALUES ('441402', '梅江区', '4414');
INSERT INTO "public"."base_area" VALUES ('441403', '梅县区', '4414');
INSERT INTO "public"."base_area" VALUES ('441422', '大埔县', '4414');
INSERT INTO "public"."base_area" VALUES ('441423', '丰顺县', '4414');
INSERT INTO "public"."base_area" VALUES ('441424', '五华县', '4414');
INSERT INTO "public"."base_area" VALUES ('441426', '平远县', '4414');
INSERT INTO "public"."base_area" VALUES ('441427', '蕉岭县', '4414');
INSERT INTO "public"."base_area" VALUES ('441481', '兴宁市', '4414');
INSERT INTO "public"."base_area" VALUES ('441502', '城区', '4415');
INSERT INTO "public"."base_area" VALUES ('441521', '海丰县', '4415');
INSERT INTO "public"."base_area" VALUES ('441523', '陆河县', '4415');
INSERT INTO "public"."base_area" VALUES ('441581', '陆丰市', '4415');
INSERT INTO "public"."base_area" VALUES ('441602', '源城区', '4416');
INSERT INTO "public"."base_area" VALUES ('441621', '紫金县', '4416');
INSERT INTO "public"."base_area" VALUES ('441622', '龙川县', '4416');
INSERT INTO "public"."base_area" VALUES ('441623', '连平县', '4416');
INSERT INTO "public"."base_area" VALUES ('441624', '和平县', '4416');
INSERT INTO "public"."base_area" VALUES ('441625', '东源县', '4416');
INSERT INTO "public"."base_area" VALUES ('441702', '江城区', '4417');
INSERT INTO "public"."base_area" VALUES ('441704', '阳东区', '4417');
INSERT INTO "public"."base_area" VALUES ('441721', '阳西县', '4417');
INSERT INTO "public"."base_area" VALUES ('441781', '阳春市', '4417');
INSERT INTO "public"."base_area" VALUES ('441802', '清城区', '4418');
INSERT INTO "public"."base_area" VALUES ('441803', '清新区', '4418');
INSERT INTO "public"."base_area" VALUES ('441821', '佛冈县', '4418');
INSERT INTO "public"."base_area" VALUES ('441823', '阳山县', '4418');
INSERT INTO "public"."base_area" VALUES ('441825', '连山壮族瑶族自治县', '4418');
INSERT INTO "public"."base_area" VALUES ('441826', '连南瑶族自治县', '4418');
INSERT INTO "public"."base_area" VALUES ('441881', '英德市', '4418');
INSERT INTO "public"."base_area" VALUES ('441882', '连州市', '4418');
INSERT INTO "public"."base_area" VALUES ('441900', '东莞市', '4419');
INSERT INTO "public"."base_area" VALUES ('442000', '中山市', '4420');
INSERT INTO "public"."base_area" VALUES ('445102', '湘桥区', '4451');
INSERT INTO "public"."base_area" VALUES ('445103', '潮安区', '4451');
INSERT INTO "public"."base_area" VALUES ('445122', '饶平县', '4451');
INSERT INTO "public"."base_area" VALUES ('445202', '榕城区', '4452');
INSERT INTO "public"."base_area" VALUES ('445203', '揭东区', '4452');
INSERT INTO "public"."base_area" VALUES ('445222', '揭西县', '4452');
INSERT INTO "public"."base_area" VALUES ('445224', '惠来县', '4452');
INSERT INTO "public"."base_area" VALUES ('445281', '普宁市', '4452');
INSERT INTO "public"."base_area" VALUES ('445302', '云城区', '4453');
INSERT INTO "public"."base_area" VALUES ('445303', '云安区', '4453');
INSERT INTO "public"."base_area" VALUES ('445321', '新兴县', '4453');
INSERT INTO "public"."base_area" VALUES ('445322', '郁南县', '4453');
INSERT INTO "public"."base_area" VALUES ('445381', '罗定市', '4453');
INSERT INTO "public"."base_area" VALUES ('450102', '兴宁区', '4501');
INSERT INTO "public"."base_area" VALUES ('450103', '青秀区', '4501');
INSERT INTO "public"."base_area" VALUES ('450105', '江南区', '4501');
INSERT INTO "public"."base_area" VALUES ('450107', '西乡塘区', '4501');
INSERT INTO "public"."base_area" VALUES ('450108', '良庆区', '4501');
INSERT INTO "public"."base_area" VALUES ('450109', '邕宁区', '4501');
INSERT INTO "public"."base_area" VALUES ('450110', '武鸣区', '4501');
INSERT INTO "public"."base_area" VALUES ('450123', '隆安县', '4501');
INSERT INTO "public"."base_area" VALUES ('450124', '马山县', '4501');
INSERT INTO "public"."base_area" VALUES ('450125', '上林县', '4501');
INSERT INTO "public"."base_area" VALUES ('450126', '宾阳县', '4501');
INSERT INTO "public"."base_area" VALUES ('450181', '横州市', '4501');
INSERT INTO "public"."base_area" VALUES ('450202', '城中区', '4502');
INSERT INTO "public"."base_area" VALUES ('450203', '鱼峰区', '4502');
INSERT INTO "public"."base_area" VALUES ('450204', '柳南区', '4502');
INSERT INTO "public"."base_area" VALUES ('450205', '柳北区', '4502');
INSERT INTO "public"."base_area" VALUES ('450206', '柳江区', '4502');
INSERT INTO "public"."base_area" VALUES ('450222', '柳城县', '4502');
INSERT INTO "public"."base_area" VALUES ('450223', '鹿寨县', '4502');
INSERT INTO "public"."base_area" VALUES ('450224', '融安县', '4502');
INSERT INTO "public"."base_area" VALUES ('450225', '融水苗族自治县', '4502');
INSERT INTO "public"."base_area" VALUES ('450226', '三江侗族自治县', '4502');
INSERT INTO "public"."base_area" VALUES ('450302', '秀峰区', '4503');
INSERT INTO "public"."base_area" VALUES ('450303', '叠彩区', '4503');
INSERT INTO "public"."base_area" VALUES ('450304', '象山区', '4503');
INSERT INTO "public"."base_area" VALUES ('450305', '七星区', '4503');
INSERT INTO "public"."base_area" VALUES ('450311', '雁山区', '4503');
INSERT INTO "public"."base_area" VALUES ('450312', '临桂区', '4503');
INSERT INTO "public"."base_area" VALUES ('450321', '阳朔县', '4503');
INSERT INTO "public"."base_area" VALUES ('450323', '灵川县', '4503');
INSERT INTO "public"."base_area" VALUES ('450324', '全州县', '4503');
INSERT INTO "public"."base_area" VALUES ('450325', '兴安县', '4503');
INSERT INTO "public"."base_area" VALUES ('450326', '永福县', '4503');
INSERT INTO "public"."base_area" VALUES ('450327', '灌阳县', '4503');
INSERT INTO "public"."base_area" VALUES ('450328', '龙胜各族自治县', '4503');
INSERT INTO "public"."base_area" VALUES ('450329', '资源县', '4503');
INSERT INTO "public"."base_area" VALUES ('450330', '平乐县', '4503');
INSERT INTO "public"."base_area" VALUES ('450332', '恭城瑶族自治县', '4503');
INSERT INTO "public"."base_area" VALUES ('450381', '荔浦市', '4503');
INSERT INTO "public"."base_area" VALUES ('450403', '万秀区', '4504');
INSERT INTO "public"."base_area" VALUES ('450405', '长洲区', '4504');
INSERT INTO "public"."base_area" VALUES ('450406', '龙圩区', '4504');
INSERT INTO "public"."base_area" VALUES ('450421', '苍梧县', '4504');
INSERT INTO "public"."base_area" VALUES ('450422', '藤县', '4504');
INSERT INTO "public"."base_area" VALUES ('450423', '蒙山县', '4504');
INSERT INTO "public"."base_area" VALUES ('450481', '岑溪市', '4504');
INSERT INTO "public"."base_area" VALUES ('450502', '海城区', '4505');
INSERT INTO "public"."base_area" VALUES ('450503', '银海区', '4505');
INSERT INTO "public"."base_area" VALUES ('450512', '铁山港区', '4505');
INSERT INTO "public"."base_area" VALUES ('450521', '合浦县', '4505');
INSERT INTO "public"."base_area" VALUES ('450602', '港口区', '4506');
INSERT INTO "public"."base_area" VALUES ('450603', '防城区', '4506');
INSERT INTO "public"."base_area" VALUES ('450621', '上思县', '4506');
INSERT INTO "public"."base_area" VALUES ('450681', '东兴市', '4506');
INSERT INTO "public"."base_area" VALUES ('450702', '钦南区', '4507');
INSERT INTO "public"."base_area" VALUES ('450703', '钦北区', '4507');
INSERT INTO "public"."base_area" VALUES ('450721', '灵山县', '4507');
INSERT INTO "public"."base_area" VALUES ('450722', '浦北县', '4507');
INSERT INTO "public"."base_area" VALUES ('450802', '港北区', '4508');
INSERT INTO "public"."base_area" VALUES ('450803', '港南区', '4508');
INSERT INTO "public"."base_area" VALUES ('450804', '覃塘区', '4508');
INSERT INTO "public"."base_area" VALUES ('450821', '平南县', '4508');
INSERT INTO "public"."base_area" VALUES ('450881', '桂平市', '4508');
INSERT INTO "public"."base_area" VALUES ('450902', '玉州区', '4509');
INSERT INTO "public"."base_area" VALUES ('450903', '福绵区', '4509');
INSERT INTO "public"."base_area" VALUES ('450921', '容县', '4509');
INSERT INTO "public"."base_area" VALUES ('450922', '陆川县', '4509');
INSERT INTO "public"."base_area" VALUES ('450923', '博白县', '4509');
INSERT INTO "public"."base_area" VALUES ('450924', '兴业县', '4509');
INSERT INTO "public"."base_area" VALUES ('450981', '北流市', '4509');
INSERT INTO "public"."base_area" VALUES ('451002', '右江区', '4510');
INSERT INTO "public"."base_area" VALUES ('451003', '田阳区', '4510');
INSERT INTO "public"."base_area" VALUES ('451022', '田东县', '4510');
INSERT INTO "public"."base_area" VALUES ('451024', '德保县', '4510');
INSERT INTO "public"."base_area" VALUES ('451026', '那坡县', '4510');
INSERT INTO "public"."base_area" VALUES ('451027', '凌云县', '4510');
INSERT INTO "public"."base_area" VALUES ('451028', '乐业县', '4510');
INSERT INTO "public"."base_area" VALUES ('451029', '田林县', '4510');
INSERT INTO "public"."base_area" VALUES ('451030', '西林县', '4510');
INSERT INTO "public"."base_area" VALUES ('451031', '隆林各族自治县', '4510');
INSERT INTO "public"."base_area" VALUES ('451081', '靖西市', '4510');
INSERT INTO "public"."base_area" VALUES ('451082', '平果市', '4510');
INSERT INTO "public"."base_area" VALUES ('451102', '八步区', '4511');
INSERT INTO "public"."base_area" VALUES ('451103', '平桂区', '4511');
INSERT INTO "public"."base_area" VALUES ('451121', '昭平县', '4511');
INSERT INTO "public"."base_area" VALUES ('451122', '钟山县', '4511');
INSERT INTO "public"."base_area" VALUES ('451123', '富川瑶族自治县', '4511');
INSERT INTO "public"."base_area" VALUES ('451202', '金城江区', '4512');
INSERT INTO "public"."base_area" VALUES ('451203', '宜州区', '4512');
INSERT INTO "public"."base_area" VALUES ('451221', '南丹县', '4512');
INSERT INTO "public"."base_area" VALUES ('451222', '天峨县', '4512');
INSERT INTO "public"."base_area" VALUES ('451223', '凤山县', '4512');
INSERT INTO "public"."base_area" VALUES ('451224', '东兰县', '4512');
INSERT INTO "public"."base_area" VALUES ('451225', '罗城仫佬族自治县', '4512');
INSERT INTO "public"."base_area" VALUES ('451226', '环江毛南族自治县', '4512');
INSERT INTO "public"."base_area" VALUES ('451227', '巴马瑶族自治县', '4512');
INSERT INTO "public"."base_area" VALUES ('451228', '都安瑶族自治县', '4512');
INSERT INTO "public"."base_area" VALUES ('451229', '大化瑶族自治县', '4512');
INSERT INTO "public"."base_area" VALUES ('451302', '兴宾区', '4513');
INSERT INTO "public"."base_area" VALUES ('451321', '忻城县', '4513');
INSERT INTO "public"."base_area" VALUES ('451322', '象州县', '4513');
INSERT INTO "public"."base_area" VALUES ('451323', '武宣县', '4513');
INSERT INTO "public"."base_area" VALUES ('451324', '金秀瑶族自治县', '4513');
INSERT INTO "public"."base_area" VALUES ('451381', '合山市', '4513');
INSERT INTO "public"."base_area" VALUES ('451402', '江州区', '4514');
INSERT INTO "public"."base_area" VALUES ('451421', '扶绥县', '4514');
INSERT INTO "public"."base_area" VALUES ('451422', '宁明县', '4514');
INSERT INTO "public"."base_area" VALUES ('451423', '龙州县', '4514');
INSERT INTO "public"."base_area" VALUES ('451424', '大新县', '4514');
INSERT INTO "public"."base_area" VALUES ('451425', '天等县', '4514');
INSERT INTO "public"."base_area" VALUES ('451481', '凭祥市', '4514');
INSERT INTO "public"."base_area" VALUES ('460105', '秀英区', '4601');
INSERT INTO "public"."base_area" VALUES ('460106', '龙华区', '4601');
INSERT INTO "public"."base_area" VALUES ('460107', '琼山区', '4601');
INSERT INTO "public"."base_area" VALUES ('460108', '美兰区', '4601');
INSERT INTO "public"."base_area" VALUES ('460202', '海棠区', '4602');
INSERT INTO "public"."base_area" VALUES ('460203', '吉阳区', '4602');
INSERT INTO "public"."base_area" VALUES ('460204', '天涯区', '4602');
INSERT INTO "public"."base_area" VALUES ('460205', '崖州区', '4602');
INSERT INTO "public"."base_area" VALUES ('460321', '西沙群岛', '4603');
INSERT INTO "public"."base_area" VALUES ('460322', '南沙群岛', '4603');
INSERT INTO "public"."base_area" VALUES ('460323', '中沙群岛的岛礁及其海域', '4603');
INSERT INTO "public"."base_area" VALUES ('460400', '儋州市', '4604');
INSERT INTO "public"."base_area" VALUES ('469001', '五指山市', '4690');
INSERT INTO "public"."base_area" VALUES ('469002', '琼海市', '4690');
INSERT INTO "public"."base_area" VALUES ('469005', '文昌市', '4690');
INSERT INTO "public"."base_area" VALUES ('469006', '万宁市', '4690');
INSERT INTO "public"."base_area" VALUES ('469007', '东方市', '4690');
INSERT INTO "public"."base_area" VALUES ('469021', '定安县', '4690');
INSERT INTO "public"."base_area" VALUES ('469022', '屯昌县', '4690');
INSERT INTO "public"."base_area" VALUES ('469023', '澄迈县', '4690');
INSERT INTO "public"."base_area" VALUES ('469024', '临高县', '4690');
INSERT INTO "public"."base_area" VALUES ('469025', '白沙黎族自治县', '4690');
INSERT INTO "public"."base_area" VALUES ('469026', '昌江黎族自治县', '4690');
INSERT INTO "public"."base_area" VALUES ('469027', '乐东黎族自治县', '4690');
INSERT INTO "public"."base_area" VALUES ('469028', '陵水黎族自治县', '4690');
INSERT INTO "public"."base_area" VALUES ('469029', '保亭黎族苗族自治县', '4690');
INSERT INTO "public"."base_area" VALUES ('469030', '琼中黎族苗族自治县', '4690');
INSERT INTO "public"."base_area" VALUES ('500101', '万州区', '5001');
INSERT INTO "public"."base_area" VALUES ('500102', '涪陵区', '5001');
INSERT INTO "public"."base_area" VALUES ('500103', '渝中区', '5001');
INSERT INTO "public"."base_area" VALUES ('500104', '大渡口区', '5001');
INSERT INTO "public"."base_area" VALUES ('500105', '江北区', '5001');
INSERT INTO "public"."base_area" VALUES ('500106', '沙坪坝区', '5001');
INSERT INTO "public"."base_area" VALUES ('500107', '九龙坡区', '5001');
INSERT INTO "public"."base_area" VALUES ('500108', '南岸区', '5001');
INSERT INTO "public"."base_area" VALUES ('500109', '北碚区', '5001');
INSERT INTO "public"."base_area" VALUES ('500110', '綦江区', '5001');
INSERT INTO "public"."base_area" VALUES ('500111', '大足区', '5001');
INSERT INTO "public"."base_area" VALUES ('500112', '渝北区', '5001');
INSERT INTO "public"."base_area" VALUES ('500113', '巴南区', '5001');
INSERT INTO "public"."base_area" VALUES ('500114', '黔江区', '5001');
INSERT INTO "public"."base_area" VALUES ('500115', '长寿区', '5001');
INSERT INTO "public"."base_area" VALUES ('500116', '江津区', '5001');
INSERT INTO "public"."base_area" VALUES ('500117', '合川区', '5001');
INSERT INTO "public"."base_area" VALUES ('500118', '永川区', '5001');
INSERT INTO "public"."base_area" VALUES ('500119', '南川区', '5001');
INSERT INTO "public"."base_area" VALUES ('500120', '璧山区', '5001');
INSERT INTO "public"."base_area" VALUES ('500151', '铜梁区', '5001');
INSERT INTO "public"."base_area" VALUES ('500152', '潼南区', '5001');
INSERT INTO "public"."base_area" VALUES ('500153', '荣昌区', '5001');
INSERT INTO "public"."base_area" VALUES ('500154', '开州区', '5001');
INSERT INTO "public"."base_area" VALUES ('500155', '梁平区', '5001');
INSERT INTO "public"."base_area" VALUES ('500156', '武隆区', '5001');
INSERT INTO "public"."base_area" VALUES ('500229', '城口县', '5002');
INSERT INTO "public"."base_area" VALUES ('500230', '丰都县', '5002');
INSERT INTO "public"."base_area" VALUES ('500231', '垫江县', '5002');
INSERT INTO "public"."base_area" VALUES ('500233', '忠县', '5002');
INSERT INTO "public"."base_area" VALUES ('500235', '云阳县', '5002');
INSERT INTO "public"."base_area" VALUES ('500236', '奉节县', '5002');
INSERT INTO "public"."base_area" VALUES ('500237', '巫山县', '5002');
INSERT INTO "public"."base_area" VALUES ('500238', '巫溪县', '5002');
INSERT INTO "public"."base_area" VALUES ('500240', '石柱土家族自治县', '5002');
INSERT INTO "public"."base_area" VALUES ('500241', '秀山土家族苗族自治县', '5002');
INSERT INTO "public"."base_area" VALUES ('500242', '酉阳土家族苗族自治县', '5002');
INSERT INTO "public"."base_area" VALUES ('500243', '彭水苗族土家族自治县', '5002');
INSERT INTO "public"."base_area" VALUES ('510104', '锦江区', '5101');
INSERT INTO "public"."base_area" VALUES ('510105', '青羊区', '5101');
INSERT INTO "public"."base_area" VALUES ('510106', '金牛区', '5101');
INSERT INTO "public"."base_area" VALUES ('510107', '武侯区', '5101');
INSERT INTO "public"."base_area" VALUES ('510108', '成华区', '5101');
INSERT INTO "public"."base_area" VALUES ('510112', '龙泉驿区', '5101');
INSERT INTO "public"."base_area" VALUES ('510113', '青白江区', '5101');
INSERT INTO "public"."base_area" VALUES ('510114', '新都区', '5101');
INSERT INTO "public"."base_area" VALUES ('510115', '温江区', '5101');
INSERT INTO "public"."base_area" VALUES ('510116', '双流区', '5101');
INSERT INTO "public"."base_area" VALUES ('510117', '郫都区', '5101');
INSERT INTO "public"."base_area" VALUES ('510118', '新津区', '5101');
INSERT INTO "public"."base_area" VALUES ('510121', '金堂县', '5101');
INSERT INTO "public"."base_area" VALUES ('510129', '大邑县', '5101');
INSERT INTO "public"."base_area" VALUES ('510131', '蒲江县', '5101');
INSERT INTO "public"."base_area" VALUES ('510181', '都江堰市', '5101');
INSERT INTO "public"."base_area" VALUES ('510182', '彭州市', '5101');
INSERT INTO "public"."base_area" VALUES ('510183', '邛崃市', '5101');
INSERT INTO "public"."base_area" VALUES ('510184', '崇州市', '5101');
INSERT INTO "public"."base_area" VALUES ('510185', '简阳市', '5101');
INSERT INTO "public"."base_area" VALUES ('510302', '自流井区', '5103');
INSERT INTO "public"."base_area" VALUES ('510303', '贡井区', '5103');
INSERT INTO "public"."base_area" VALUES ('510304', '大安区', '5103');
INSERT INTO "public"."base_area" VALUES ('510311', '沿滩区', '5103');
INSERT INTO "public"."base_area" VALUES ('510321', '荣县', '5103');
INSERT INTO "public"."base_area" VALUES ('510322', '富顺县', '5103');
INSERT INTO "public"."base_area" VALUES ('510402', '东区', '5104');
INSERT INTO "public"."base_area" VALUES ('510403', '西区', '5104');
INSERT INTO "public"."base_area" VALUES ('510411', '仁和区', '5104');
INSERT INTO "public"."base_area" VALUES ('510421', '米易县', '5104');
INSERT INTO "public"."base_area" VALUES ('510422', '盐边县', '5104');
INSERT INTO "public"."base_area" VALUES ('510502', '江阳区', '5105');
INSERT INTO "public"."base_area" VALUES ('510503', '纳溪区', '5105');
INSERT INTO "public"."base_area" VALUES ('510504', '龙马潭区', '5105');
INSERT INTO "public"."base_area" VALUES ('510521', '泸县', '5105');
INSERT INTO "public"."base_area" VALUES ('510522', '合江县', '5105');
INSERT INTO "public"."base_area" VALUES ('510524', '叙永县', '5105');
INSERT INTO "public"."base_area" VALUES ('510525', '古蔺县', '5105');
INSERT INTO "public"."base_area" VALUES ('510603', '旌阳区', '5106');
INSERT INTO "public"."base_area" VALUES ('510604', '罗江区', '5106');
INSERT INTO "public"."base_area" VALUES ('510623', '中江县', '5106');
INSERT INTO "public"."base_area" VALUES ('510681', '广汉市', '5106');
INSERT INTO "public"."base_area" VALUES ('510682', '什邡市', '5106');
INSERT INTO "public"."base_area" VALUES ('510683', '绵竹市', '5106');
INSERT INTO "public"."base_area" VALUES ('510703', '涪城区', '5107');
INSERT INTO "public"."base_area" VALUES ('510704', '游仙区', '5107');
INSERT INTO "public"."base_area" VALUES ('510705', '安州区', '5107');
INSERT INTO "public"."base_area" VALUES ('510722', '三台县', '5107');
INSERT INTO "public"."base_area" VALUES ('510723', '盐亭县', '5107');
INSERT INTO "public"."base_area" VALUES ('510725', '梓潼县', '5107');
INSERT INTO "public"."base_area" VALUES ('510726', '北川羌族自治县', '5107');
INSERT INTO "public"."base_area" VALUES ('510727', '平武县', '5107');
INSERT INTO "public"."base_area" VALUES ('510781', '江油市', '5107');
INSERT INTO "public"."base_area" VALUES ('510802', '利州区', '5108');
INSERT INTO "public"."base_area" VALUES ('510811', '昭化区', '5108');
INSERT INTO "public"."base_area" VALUES ('510812', '朝天区', '5108');
INSERT INTO "public"."base_area" VALUES ('510821', '旺苍县', '5108');
INSERT INTO "public"."base_area" VALUES ('510822', '青川县', '5108');
INSERT INTO "public"."base_area" VALUES ('510823', '剑阁县', '5108');
INSERT INTO "public"."base_area" VALUES ('510824', '苍溪县', '5108');
INSERT INTO "public"."base_area" VALUES ('510903', '船山区', '5109');
INSERT INTO "public"."base_area" VALUES ('510904', '安居区', '5109');
INSERT INTO "public"."base_area" VALUES ('510921', '蓬溪县', '5109');
INSERT INTO "public"."base_area" VALUES ('510923', '大英县', '5109');
INSERT INTO "public"."base_area" VALUES ('510981', '射洪市', '5109');
INSERT INTO "public"."base_area" VALUES ('511002', '市中区', '5110');
INSERT INTO "public"."base_area" VALUES ('511011', '东兴区', '5110');
INSERT INTO "public"."base_area" VALUES ('511024', '威远县', '5110');
INSERT INTO "public"."base_area" VALUES ('511025', '资中县', '5110');
INSERT INTO "public"."base_area" VALUES ('511083', '隆昌市', '5110');
INSERT INTO "public"."base_area" VALUES ('511102', '市中区', '5111');
INSERT INTO "public"."base_area" VALUES ('511111', '沙湾区', '5111');
INSERT INTO "public"."base_area" VALUES ('511112', '五通桥区', '5111');
INSERT INTO "public"."base_area" VALUES ('511113', '金口河区', '5111');
INSERT INTO "public"."base_area" VALUES ('511123', '犍为县', '5111');
INSERT INTO "public"."base_area" VALUES ('511124', '井研县', '5111');
INSERT INTO "public"."base_area" VALUES ('511126', '夹江县', '5111');
INSERT INTO "public"."base_area" VALUES ('511129', '沐川县', '5111');
INSERT INTO "public"."base_area" VALUES ('511132', '峨边彝族自治县', '5111');
INSERT INTO "public"."base_area" VALUES ('511133', '马边彝族自治县', '5111');
INSERT INTO "public"."base_area" VALUES ('511181', '峨眉山市', '5111');
INSERT INTO "public"."base_area" VALUES ('511302', '顺庆区', '5113');
INSERT INTO "public"."base_area" VALUES ('511303', '高坪区', '5113');
INSERT INTO "public"."base_area" VALUES ('511304', '嘉陵区', '5113');
INSERT INTO "public"."base_area" VALUES ('511321', '南部县', '5113');
INSERT INTO "public"."base_area" VALUES ('511322', '营山县', '5113');
INSERT INTO "public"."base_area" VALUES ('511323', '蓬安县', '5113');
INSERT INTO "public"."base_area" VALUES ('511324', '仪陇县', '5113');
INSERT INTO "public"."base_area" VALUES ('511325', '西充县', '5113');
INSERT INTO "public"."base_area" VALUES ('511381', '阆中市', '5113');
INSERT INTO "public"."base_area" VALUES ('511402', '东坡区', '5114');
INSERT INTO "public"."base_area" VALUES ('511403', '彭山区', '5114');
INSERT INTO "public"."base_area" VALUES ('511421', '仁寿县', '5114');
INSERT INTO "public"."base_area" VALUES ('511423', '洪雅县', '5114');
INSERT INTO "public"."base_area" VALUES ('511424', '丹棱县', '5114');
INSERT INTO "public"."base_area" VALUES ('511425', '青神县', '5114');
INSERT INTO "public"."base_area" VALUES ('511502', '翠屏区', '5115');
INSERT INTO "public"."base_area" VALUES ('511503', '南溪区', '5115');
INSERT INTO "public"."base_area" VALUES ('511504', '叙州区', '5115');
INSERT INTO "public"."base_area" VALUES ('511523', '江安县', '5115');
INSERT INTO "public"."base_area" VALUES ('511524', '长宁县', '5115');
INSERT INTO "public"."base_area" VALUES ('511525', '高县', '5115');
INSERT INTO "public"."base_area" VALUES ('511526', '珙县', '5115');
INSERT INTO "public"."base_area" VALUES ('511527', '筠连县', '5115');
INSERT INTO "public"."base_area" VALUES ('511528', '兴文县', '5115');
INSERT INTO "public"."base_area" VALUES ('511529', '屏山县', '5115');
INSERT INTO "public"."base_area" VALUES ('511602', '广安区', '5116');
INSERT INTO "public"."base_area" VALUES ('511603', '前锋区', '5116');
INSERT INTO "public"."base_area" VALUES ('511621', '岳池县', '5116');
INSERT INTO "public"."base_area" VALUES ('511622', '武胜县', '5116');
INSERT INTO "public"."base_area" VALUES ('511623', '邻水县', '5116');
INSERT INTO "public"."base_area" VALUES ('511681', '华蓥市', '5116');
INSERT INTO "public"."base_area" VALUES ('511702', '通川区', '5117');
INSERT INTO "public"."base_area" VALUES ('511703', '达川区', '5117');
INSERT INTO "public"."base_area" VALUES ('511722', '宣汉县', '5117');
INSERT INTO "public"."base_area" VALUES ('511723', '开江县', '5117');
INSERT INTO "public"."base_area" VALUES ('511724', '大竹县', '5117');
INSERT INTO "public"."base_area" VALUES ('511725', '渠县', '5117');
INSERT INTO "public"."base_area" VALUES ('511781', '万源市', '5117');
INSERT INTO "public"."base_area" VALUES ('511802', '雨城区', '5118');
INSERT INTO "public"."base_area" VALUES ('511803', '名山区', '5118');
INSERT INTO "public"."base_area" VALUES ('511822', '荥经县', '5118');
INSERT INTO "public"."base_area" VALUES ('511823', '汉源县', '5118');
INSERT INTO "public"."base_area" VALUES ('511824', '石棉县', '5118');
INSERT INTO "public"."base_area" VALUES ('511825', '天全县', '5118');
INSERT INTO "public"."base_area" VALUES ('511826', '芦山县', '5118');
INSERT INTO "public"."base_area" VALUES ('511827', '宝兴县', '5118');
INSERT INTO "public"."base_area" VALUES ('511902', '巴州区', '5119');
INSERT INTO "public"."base_area" VALUES ('511903', '恩阳区', '5119');
INSERT INTO "public"."base_area" VALUES ('511921', '通江县', '5119');
INSERT INTO "public"."base_area" VALUES ('511922', '南江县', '5119');
INSERT INTO "public"."base_area" VALUES ('511923', '平昌县', '5119');
INSERT INTO "public"."base_area" VALUES ('512002', '雁江区', '5120');
INSERT INTO "public"."base_area" VALUES ('512021', '安岳县', '5120');
INSERT INTO "public"."base_area" VALUES ('512022', '乐至县', '5120');
INSERT INTO "public"."base_area" VALUES ('513201', '马尔康市', '5132');
INSERT INTO "public"."base_area" VALUES ('513221', '汶川县', '5132');
INSERT INTO "public"."base_area" VALUES ('513222', '理县', '5132');
INSERT INTO "public"."base_area" VALUES ('513223', '茂县', '5132');
INSERT INTO "public"."base_area" VALUES ('513224', '松潘县', '5132');
INSERT INTO "public"."base_area" VALUES ('513225', '九寨沟县', '5132');
INSERT INTO "public"."base_area" VALUES ('513226', '金川县', '5132');
INSERT INTO "public"."base_area" VALUES ('513227', '小金县', '5132');
INSERT INTO "public"."base_area" VALUES ('513228', '黑水县', '5132');
INSERT INTO "public"."base_area" VALUES ('513230', '壤塘县', '5132');
INSERT INTO "public"."base_area" VALUES ('513231', '阿坝县', '5132');
INSERT INTO "public"."base_area" VALUES ('513232', '若尔盖县', '5132');
INSERT INTO "public"."base_area" VALUES ('513233', '红原县', '5132');
INSERT INTO "public"."base_area" VALUES ('513301', '康定市', '5133');
INSERT INTO "public"."base_area" VALUES ('513322', '泸定县', '5133');
INSERT INTO "public"."base_area" VALUES ('513323', '丹巴县', '5133');
INSERT INTO "public"."base_area" VALUES ('513324', '九龙县', '5133');
INSERT INTO "public"."base_area" VALUES ('513325', '雅江县', '5133');
INSERT INTO "public"."base_area" VALUES ('513326', '道孚县', '5133');
INSERT INTO "public"."base_area" VALUES ('513327', '炉霍县', '5133');
INSERT INTO "public"."base_area" VALUES ('513328', '甘孜县', '5133');
INSERT INTO "public"."base_area" VALUES ('513329', '新龙县', '5133');
INSERT INTO "public"."base_area" VALUES ('513330', '德格县', '5133');
INSERT INTO "public"."base_area" VALUES ('513331', '白玉县', '5133');
INSERT INTO "public"."base_area" VALUES ('513332', '石渠县', '5133');
INSERT INTO "public"."base_area" VALUES ('513333', '色达县', '5133');
INSERT INTO "public"."base_area" VALUES ('513334', '理塘县', '5133');
INSERT INTO "public"."base_area" VALUES ('513335', '巴塘县', '5133');
INSERT INTO "public"."base_area" VALUES ('513336', '乡城县', '5133');
INSERT INTO "public"."base_area" VALUES ('513337', '稻城县', '5133');
INSERT INTO "public"."base_area" VALUES ('513338', '得荣县', '5133');
INSERT INTO "public"."base_area" VALUES ('513401', '西昌市', '5134');
INSERT INTO "public"."base_area" VALUES ('513402', '会理市', '5134');
INSERT INTO "public"."base_area" VALUES ('513422', '木里藏族自治县', '5134');
INSERT INTO "public"."base_area" VALUES ('513423', '盐源县', '5134');
INSERT INTO "public"."base_area" VALUES ('513424', '德昌县', '5134');
INSERT INTO "public"."base_area" VALUES ('513426', '会东县', '5134');
INSERT INTO "public"."base_area" VALUES ('513427', '宁南县', '5134');
INSERT INTO "public"."base_area" VALUES ('513428', '普格县', '5134');
INSERT INTO "public"."base_area" VALUES ('513429', '布拖县', '5134');
INSERT INTO "public"."base_area" VALUES ('513430', '金阳县', '5134');
INSERT INTO "public"."base_area" VALUES ('513431', '昭觉县', '5134');
INSERT INTO "public"."base_area" VALUES ('513432', '喜德县', '5134');
INSERT INTO "public"."base_area" VALUES ('513433', '冕宁县', '5134');
INSERT INTO "public"."base_area" VALUES ('513434', '越西县', '5134');
INSERT INTO "public"."base_area" VALUES ('513435', '甘洛县', '5134');
INSERT INTO "public"."base_area" VALUES ('513436', '美姑县', '5134');
INSERT INTO "public"."base_area" VALUES ('513437', '雷波县', '5134');
INSERT INTO "public"."base_area" VALUES ('520102', '南明区', '5201');
INSERT INTO "public"."base_area" VALUES ('520103', '云岩区', '5201');
INSERT INTO "public"."base_area" VALUES ('520111', '花溪区', '5201');
INSERT INTO "public"."base_area" VALUES ('520112', '乌当区', '5201');
INSERT INTO "public"."base_area" VALUES ('520113', '白云区', '5201');
INSERT INTO "public"."base_area" VALUES ('520115', '观山湖区', '5201');
INSERT INTO "public"."base_area" VALUES ('520121', '开阳县', '5201');
INSERT INTO "public"."base_area" VALUES ('520122', '息烽县', '5201');
INSERT INTO "public"."base_area" VALUES ('520123', '修文县', '5201');
INSERT INTO "public"."base_area" VALUES ('520181', '清镇市', '5201');
INSERT INTO "public"."base_area" VALUES ('520201', '钟山区', '5202');
INSERT INTO "public"."base_area" VALUES ('520203', '六枝特区', '5202');
INSERT INTO "public"."base_area" VALUES ('520204', '水城区', '5202');
INSERT INTO "public"."base_area" VALUES ('520281', '盘州市', '5202');
INSERT INTO "public"."base_area" VALUES ('520302', '红花岗区', '5203');
INSERT INTO "public"."base_area" VALUES ('520303', '汇川区', '5203');
INSERT INTO "public"."base_area" VALUES ('520304', '播州区', '5203');
INSERT INTO "public"."base_area" VALUES ('520322', '桐梓县', '5203');
INSERT INTO "public"."base_area" VALUES ('520323', '绥阳县', '5203');
INSERT INTO "public"."base_area" VALUES ('520324', '正安县', '5203');
INSERT INTO "public"."base_area" VALUES ('520325', '道真仡佬族苗族自治县', '5203');
INSERT INTO "public"."base_area" VALUES ('520326', '务川仡佬族苗族自治县', '5203');
INSERT INTO "public"."base_area" VALUES ('520327', '凤冈县', '5203');
INSERT INTO "public"."base_area" VALUES ('520328', '湄潭县', '5203');
INSERT INTO "public"."base_area" VALUES ('520329', '余庆县', '5203');
INSERT INTO "public"."base_area" VALUES ('520330', '习水县', '5203');
INSERT INTO "public"."base_area" VALUES ('520381', '赤水市', '5203');
INSERT INTO "public"."base_area" VALUES ('520382', '仁怀市', '5203');
INSERT INTO "public"."base_area" VALUES ('520402', '西秀区', '5204');
INSERT INTO "public"."base_area" VALUES ('520403', '平坝区', '5204');
INSERT INTO "public"."base_area" VALUES ('520422', '普定县', '5204');
INSERT INTO "public"."base_area" VALUES ('520423', '镇宁布依族苗族自治县', '5204');
INSERT INTO "public"."base_area" VALUES ('520424', '关岭布依族苗族自治县', '5204');
INSERT INTO "public"."base_area" VALUES ('520425', '紫云苗族布依族自治县', '5204');
INSERT INTO "public"."base_area" VALUES ('520502', '七星关区', '5205');
INSERT INTO "public"."base_area" VALUES ('520521', '大方县', '5205');
INSERT INTO "public"."base_area" VALUES ('520523', '金沙县', '5205');
INSERT INTO "public"."base_area" VALUES ('520524', '织金县', '5205');
INSERT INTO "public"."base_area" VALUES ('520525', '纳雍县', '5205');
INSERT INTO "public"."base_area" VALUES ('520526', '威宁彝族回族苗族自治县', '5205');
INSERT INTO "public"."base_area" VALUES ('520527', '赫章县', '5205');
INSERT INTO "public"."base_area" VALUES ('520581', '黔西市', '5205');
INSERT INTO "public"."base_area" VALUES ('520602', '碧江区', '5206');
INSERT INTO "public"."base_area" VALUES ('520603', '万山区', '5206');
INSERT INTO "public"."base_area" VALUES ('520621', '江口县', '5206');
INSERT INTO "public"."base_area" VALUES ('520622', '玉屏侗族自治县', '5206');
INSERT INTO "public"."base_area" VALUES ('520623', '石阡县', '5206');
INSERT INTO "public"."base_area" VALUES ('520624', '思南县', '5206');
INSERT INTO "public"."base_area" VALUES ('520625', '印江土家族苗族自治县', '5206');
INSERT INTO "public"."base_area" VALUES ('520626', '德江县', '5206');
INSERT INTO "public"."base_area" VALUES ('520627', '沿河土家族自治县', '5206');
INSERT INTO "public"."base_area" VALUES ('520628', '松桃苗族自治县', '5206');
INSERT INTO "public"."base_area" VALUES ('522301', '兴义市', '5223');
INSERT INTO "public"."base_area" VALUES ('522302', '兴仁市', '5223');
INSERT INTO "public"."base_area" VALUES ('522323', '普安县', '5223');
INSERT INTO "public"."base_area" VALUES ('522324', '晴隆县', '5223');
INSERT INTO "public"."base_area" VALUES ('522325', '贞丰县', '5223');
INSERT INTO "public"."base_area" VALUES ('522326', '望谟县', '5223');
INSERT INTO "public"."base_area" VALUES ('522327', '册亨县', '5223');
INSERT INTO "public"."base_area" VALUES ('522328', '安龙县', '5223');
INSERT INTO "public"."base_area" VALUES ('522601', '凯里市', '5226');
INSERT INTO "public"."base_area" VALUES ('522622', '黄平县', '5226');
INSERT INTO "public"."base_area" VALUES ('522623', '施秉县', '5226');
INSERT INTO "public"."base_area" VALUES ('522624', '三穗县', '5226');
INSERT INTO "public"."base_area" VALUES ('522625', '镇远县', '5226');
INSERT INTO "public"."base_area" VALUES ('522626', '岑巩县', '5226');
INSERT INTO "public"."base_area" VALUES ('522627', '天柱县', '5226');
INSERT INTO "public"."base_area" VALUES ('522628', '锦屏县', '5226');
INSERT INTO "public"."base_area" VALUES ('522629', '剑河县', '5226');
INSERT INTO "public"."base_area" VALUES ('522630', '台江县', '5226');
INSERT INTO "public"."base_area" VALUES ('522631', '黎平县', '5226');
INSERT INTO "public"."base_area" VALUES ('522632', '榕江县', '5226');
INSERT INTO "public"."base_area" VALUES ('522633', '从江县', '5226');
INSERT INTO "public"."base_area" VALUES ('522634', '雷山县', '5226');
INSERT INTO "public"."base_area" VALUES ('522635', '麻江县', '5226');
INSERT INTO "public"."base_area" VALUES ('522636', '丹寨县', '5226');
INSERT INTO "public"."base_area" VALUES ('522701', '都匀市', '5227');
INSERT INTO "public"."base_area" VALUES ('522702', '福泉市', '5227');
INSERT INTO "public"."base_area" VALUES ('522722', '荔波县', '5227');
INSERT INTO "public"."base_area" VALUES ('522723', '贵定县', '5227');
INSERT INTO "public"."base_area" VALUES ('522725', '瓮安县', '5227');
INSERT INTO "public"."base_area" VALUES ('522726', '独山县', '5227');
INSERT INTO "public"."base_area" VALUES ('522727', '平塘县', '5227');
INSERT INTO "public"."base_area" VALUES ('522728', '罗甸县', '5227');
INSERT INTO "public"."base_area" VALUES ('522729', '长顺县', '5227');
INSERT INTO "public"."base_area" VALUES ('522730', '龙里县', '5227');
INSERT INTO "public"."base_area" VALUES ('522731', '惠水县', '5227');
INSERT INTO "public"."base_area" VALUES ('522732', '三都水族自治县', '5227');
INSERT INTO "public"."base_area" VALUES ('530102', '五华区', '5301');
INSERT INTO "public"."base_area" VALUES ('530103', '盘龙区', '5301');
INSERT INTO "public"."base_area" VALUES ('530111', '官渡区', '5301');
INSERT INTO "public"."base_area" VALUES ('530112', '西山区', '5301');
INSERT INTO "public"."base_area" VALUES ('530113', '东川区', '5301');
INSERT INTO "public"."base_area" VALUES ('530114', '呈贡区', '5301');
INSERT INTO "public"."base_area" VALUES ('530115', '晋宁区', '5301');
INSERT INTO "public"."base_area" VALUES ('530124', '富民县', '5301');
INSERT INTO "public"."base_area" VALUES ('530125', '宜良县', '5301');
INSERT INTO "public"."base_area" VALUES ('530126', '石林彝族自治县', '5301');
INSERT INTO "public"."base_area" VALUES ('530127', '嵩明县', '5301');
INSERT INTO "public"."base_area" VALUES ('530128', '禄劝彝族苗族自治县', '5301');
INSERT INTO "public"."base_area" VALUES ('530129', '寻甸回族彝族自治县', '5301');
INSERT INTO "public"."base_area" VALUES ('530181', '安宁市', '5301');
INSERT INTO "public"."base_area" VALUES ('530302', '麒麟区', '5303');
INSERT INTO "public"."base_area" VALUES ('530303', '沾益区', '5303');
INSERT INTO "public"."base_area" VALUES ('530304', '马龙区', '5303');
INSERT INTO "public"."base_area" VALUES ('530322', '陆良县', '5303');
INSERT INTO "public"."base_area" VALUES ('530323', '师宗县', '5303');
INSERT INTO "public"."base_area" VALUES ('530324', '罗平县', '5303');
INSERT INTO "public"."base_area" VALUES ('530325', '富源县', '5303');
INSERT INTO "public"."base_area" VALUES ('530326', '会泽县', '5303');
INSERT INTO "public"."base_area" VALUES ('530381', '宣威市', '5303');
INSERT INTO "public"."base_area" VALUES ('530402', '红塔区', '5304');
INSERT INTO "public"."base_area" VALUES ('530403', '江川区', '5304');
INSERT INTO "public"."base_area" VALUES ('530423', '通海县', '5304');
INSERT INTO "public"."base_area" VALUES ('530424', '华宁县', '5304');
INSERT INTO "public"."base_area" VALUES ('530425', '易门县', '5304');
INSERT INTO "public"."base_area" VALUES ('530426', '峨山彝族自治县', '5304');
INSERT INTO "public"."base_area" VALUES ('530427', '新平彝族傣族自治县', '5304');
INSERT INTO "public"."base_area" VALUES ('530428', '元江哈尼族彝族傣族自治县', '5304');
INSERT INTO "public"."base_area" VALUES ('530481', '澄江市', '5304');
INSERT INTO "public"."base_area" VALUES ('530502', '隆阳区', '5305');
INSERT INTO "public"."base_area" VALUES ('530521', '施甸县', '5305');
INSERT INTO "public"."base_area" VALUES ('530523', '龙陵县', '5305');
INSERT INTO "public"."base_area" VALUES ('530524', '昌宁县', '5305');
INSERT INTO "public"."base_area" VALUES ('530581', '腾冲市', '5305');
INSERT INTO "public"."base_area" VALUES ('530602', '昭阳区', '5306');
INSERT INTO "public"."base_area" VALUES ('530621', '鲁甸县', '5306');
INSERT INTO "public"."base_area" VALUES ('530622', '巧家县', '5306');
INSERT INTO "public"."base_area" VALUES ('530623', '盐津县', '5306');
INSERT INTO "public"."base_area" VALUES ('530624', '大关县', '5306');
INSERT INTO "public"."base_area" VALUES ('530625', '永善县', '5306');
INSERT INTO "public"."base_area" VALUES ('530626', '绥江县', '5306');
INSERT INTO "public"."base_area" VALUES ('530627', '镇雄县', '5306');
INSERT INTO "public"."base_area" VALUES ('530628', '彝良县', '5306');
INSERT INTO "public"."base_area" VALUES ('530629', '威信县', '5306');
INSERT INTO "public"."base_area" VALUES ('530681', '水富市', '5306');
INSERT INTO "public"."base_area" VALUES ('530702', '古城区', '5307');
INSERT INTO "public"."base_area" VALUES ('530721', '玉龙纳西族自治县', '5307');
INSERT INTO "public"."base_area" VALUES ('530722', '永胜县', '5307');
INSERT INTO "public"."base_area" VALUES ('530723', '华坪县', '5307');
INSERT INTO "public"."base_area" VALUES ('530724', '宁蒗彝族自治县', '5307');
INSERT INTO "public"."base_area" VALUES ('530802', '思茅区', '5308');
INSERT INTO "public"."base_area" VALUES ('530821', '宁洱哈尼族彝族自治县', '5308');
INSERT INTO "public"."base_area" VALUES ('530822', '墨江哈尼族自治县', '5308');
INSERT INTO "public"."base_area" VALUES ('530823', '景东彝族自治县', '5308');
INSERT INTO "public"."base_area" VALUES ('530824', '景谷傣族彝族自治县', '5308');
INSERT INTO "public"."base_area" VALUES ('530825', '镇沅彝族哈尼族拉祜族自治县', '5308');
INSERT INTO "public"."base_area" VALUES ('530826', '江城哈尼族彝族自治县', '5308');
INSERT INTO "public"."base_area" VALUES ('530827', '孟连傣族拉祜族佤族自治县', '5308');
INSERT INTO "public"."base_area" VALUES ('530828', '澜沧拉祜族自治县', '5308');
INSERT INTO "public"."base_area" VALUES ('530829', '西盟佤族自治县', '5308');
INSERT INTO "public"."base_area" VALUES ('530902', '临翔区', '5309');
INSERT INTO "public"."base_area" VALUES ('530921', '凤庆县', '5309');
INSERT INTO "public"."base_area" VALUES ('530922', '云县', '5309');
INSERT INTO "public"."base_area" VALUES ('530923', '永德县', '5309');
INSERT INTO "public"."base_area" VALUES ('530924', '镇康县', '5309');
INSERT INTO "public"."base_area" VALUES ('530925', '双江拉祜族佤族布朗族傣族自治县', '5309');
INSERT INTO "public"."base_area" VALUES ('530926', '耿马傣族佤族自治县', '5309');
INSERT INTO "public"."base_area" VALUES ('530927', '沧源佤族自治县', '5309');
INSERT INTO "public"."base_area" VALUES ('532301', '楚雄市', '5323');
INSERT INTO "public"."base_area" VALUES ('532302', '禄丰市', '5323');
INSERT INTO "public"."base_area" VALUES ('532322', '双柏县', '5323');
INSERT INTO "public"."base_area" VALUES ('532323', '牟定县', '5323');
INSERT INTO "public"."base_area" VALUES ('532324', '南华县', '5323');
INSERT INTO "public"."base_area" VALUES ('532325', '姚安县', '5323');
INSERT INTO "public"."base_area" VALUES ('532326', '大姚县', '5323');
INSERT INTO "public"."base_area" VALUES ('532327', '永仁县', '5323');
INSERT INTO "public"."base_area" VALUES ('532328', '元谋县', '5323');
INSERT INTO "public"."base_area" VALUES ('532329', '武定县', '5323');
INSERT INTO "public"."base_area" VALUES ('532501', '个旧市', '5325');
INSERT INTO "public"."base_area" VALUES ('532502', '开远市', '5325');
INSERT INTO "public"."base_area" VALUES ('532503', '蒙自市', '5325');
INSERT INTO "public"."base_area" VALUES ('532504', '弥勒市', '5325');
INSERT INTO "public"."base_area" VALUES ('532523', '屏边苗族自治县', '5325');
INSERT INTO "public"."base_area" VALUES ('532524', '建水县', '5325');
INSERT INTO "public"."base_area" VALUES ('532525', '石屏县', '5325');
INSERT INTO "public"."base_area" VALUES ('532527', '泸西县', '5325');
INSERT INTO "public"."base_area" VALUES ('532528', '元阳县', '5325');
INSERT INTO "public"."base_area" VALUES ('532529', '红河县', '5325');
INSERT INTO "public"."base_area" VALUES ('532530', '金平苗族瑶族傣族自治县', '5325');
INSERT INTO "public"."base_area" VALUES ('532531', '绿春县', '5325');
INSERT INTO "public"."base_area" VALUES ('532532', '河口瑶族自治县', '5325');
INSERT INTO "public"."base_area" VALUES ('532601', '文山市', '5326');
INSERT INTO "public"."base_area" VALUES ('532622', '砚山县', '5326');
INSERT INTO "public"."base_area" VALUES ('532623', '西畴县', '5326');
INSERT INTO "public"."base_area" VALUES ('532624', '麻栗坡县', '5326');
INSERT INTO "public"."base_area" VALUES ('532625', '马关县', '5326');
INSERT INTO "public"."base_area" VALUES ('532626', '丘北县', '5326');
INSERT INTO "public"."base_area" VALUES ('532627', '广南县', '5326');
INSERT INTO "public"."base_area" VALUES ('532628', '富宁县', '5326');
INSERT INTO "public"."base_area" VALUES ('532801', '景洪市', '5328');
INSERT INTO "public"."base_area" VALUES ('532822', '勐海县', '5328');
INSERT INTO "public"."base_area" VALUES ('532823', '勐腊县', '5328');
INSERT INTO "public"."base_area" VALUES ('532901', '大理市', '5329');
INSERT INTO "public"."base_area" VALUES ('532922', '漾濞彝族自治县', '5329');
INSERT INTO "public"."base_area" VALUES ('532923', '祥云县', '5329');
INSERT INTO "public"."base_area" VALUES ('532924', '宾川县', '5329');
INSERT INTO "public"."base_area" VALUES ('532925', '弥渡县', '5329');
INSERT INTO "public"."base_area" VALUES ('532926', '南涧彝族自治县', '5329');
INSERT INTO "public"."base_area" VALUES ('532927', '巍山彝族回族自治县', '5329');
INSERT INTO "public"."base_area" VALUES ('532928', '永平县', '5329');
INSERT INTO "public"."base_area" VALUES ('532929', '云龙县', '5329');
INSERT INTO "public"."base_area" VALUES ('532930', '洱源县', '5329');
INSERT INTO "public"."base_area" VALUES ('532931', '剑川县', '5329');
INSERT INTO "public"."base_area" VALUES ('532932', '鹤庆县', '5329');
INSERT INTO "public"."base_area" VALUES ('533102', '瑞丽市', '5331');
INSERT INTO "public"."base_area" VALUES ('533103', '芒市', '5331');
INSERT INTO "public"."base_area" VALUES ('533122', '梁河县', '5331');
INSERT INTO "public"."base_area" VALUES ('533123', '盈江县', '5331');
INSERT INTO "public"."base_area" VALUES ('533124', '陇川县', '5331');
INSERT INTO "public"."base_area" VALUES ('533301', '泸水市', '5333');
INSERT INTO "public"."base_area" VALUES ('533323', '福贡县', '5333');
INSERT INTO "public"."base_area" VALUES ('533324', '贡山独龙族怒族自治县', '5333');
INSERT INTO "public"."base_area" VALUES ('533325', '兰坪白族普米族自治县', '5333');
INSERT INTO "public"."base_area" VALUES ('533401', '香格里拉市', '5334');
INSERT INTO "public"."base_area" VALUES ('533422', '德钦县', '5334');
INSERT INTO "public"."base_area" VALUES ('533423', '维西傈僳族自治县', '5334');
INSERT INTO "public"."base_area" VALUES ('540102', '城关区', '5401');
INSERT INTO "public"."base_area" VALUES ('540103', '堆龙德庆区', '5401');
INSERT INTO "public"."base_area" VALUES ('540104', '达孜区', '5401');
INSERT INTO "public"."base_area" VALUES ('540121', '林周县', '5401');
INSERT INTO "public"."base_area" VALUES ('540122', '当雄县', '5401');
INSERT INTO "public"."base_area" VALUES ('540123', '尼木县', '5401');
INSERT INTO "public"."base_area" VALUES ('540124', '曲水县', '5401');
INSERT INTO "public"."base_area" VALUES ('540127', '墨竹工卡县', '5401');
INSERT INTO "public"."base_area" VALUES ('540171', '格尔木藏青工业园区', '5401');
INSERT INTO "public"."base_area" VALUES ('540172', '拉萨经济技术开发区', '5401');
INSERT INTO "public"."base_area" VALUES ('540173', '西藏文化旅游创意园区', '5401');
INSERT INTO "public"."base_area" VALUES ('540174', '达孜工业园区', '5401');
INSERT INTO "public"."base_area" VALUES ('540202', '桑珠孜区', '5402');
INSERT INTO "public"."base_area" VALUES ('540221', '南木林县', '5402');
INSERT INTO "public"."base_area" VALUES ('540222', '江孜县', '5402');
INSERT INTO "public"."base_area" VALUES ('540223', '定日县', '5402');
INSERT INTO "public"."base_area" VALUES ('540224', '萨迦县', '5402');
INSERT INTO "public"."base_area" VALUES ('540225', '拉孜县', '5402');
INSERT INTO "public"."base_area" VALUES ('540226', '昂仁县', '5402');
INSERT INTO "public"."base_area" VALUES ('540227', '谢通门县', '5402');
INSERT INTO "public"."base_area" VALUES ('540228', '白朗县', '5402');
INSERT INTO "public"."base_area" VALUES ('540229', '仁布县', '5402');
INSERT INTO "public"."base_area" VALUES ('540230', '康马县', '5402');
INSERT INTO "public"."base_area" VALUES ('540231', '定结县', '5402');
INSERT INTO "public"."base_area" VALUES ('540232', '仲巴县', '5402');
INSERT INTO "public"."base_area" VALUES ('540233', '亚东县', '5402');
INSERT INTO "public"."base_area" VALUES ('540234', '吉隆县', '5402');
INSERT INTO "public"."base_area" VALUES ('540235', '聂拉木县', '5402');
INSERT INTO "public"."base_area" VALUES ('540236', '萨嘎县', '5402');
INSERT INTO "public"."base_area" VALUES ('540237', '岗巴县', '5402');
INSERT INTO "public"."base_area" VALUES ('540302', '卡若区', '5403');
INSERT INTO "public"."base_area" VALUES ('540321', '江达县', '5403');
INSERT INTO "public"."base_area" VALUES ('540322', '贡觉县', '5403');
INSERT INTO "public"."base_area" VALUES ('540323', '类乌齐县', '5403');
INSERT INTO "public"."base_area" VALUES ('540324', '丁青县', '5403');
INSERT INTO "public"."base_area" VALUES ('540325', '察雅县', '5403');
INSERT INTO "public"."base_area" VALUES ('540326', '八宿县', '5403');
INSERT INTO "public"."base_area" VALUES ('540327', '左贡县', '5403');
INSERT INTO "public"."base_area" VALUES ('540328', '芒康县', '5403');
INSERT INTO "public"."base_area" VALUES ('540329', '洛隆县', '5403');
INSERT INTO "public"."base_area" VALUES ('540330', '边坝县', '5403');
INSERT INTO "public"."base_area" VALUES ('540402', '巴宜区', '5404');
INSERT INTO "public"."base_area" VALUES ('540421', '工布江达县', '5404');
INSERT INTO "public"."base_area" VALUES ('540422', '米林县', '5404');
INSERT INTO "public"."base_area" VALUES ('540423', '墨脱县', '5404');
INSERT INTO "public"."base_area" VALUES ('540424', '波密县', '5404');
INSERT INTO "public"."base_area" VALUES ('540425', '察隅县', '5404');
INSERT INTO "public"."base_area" VALUES ('540426', '朗县', '5404');
INSERT INTO "public"."base_area" VALUES ('540502', '乃东区', '5405');
INSERT INTO "public"."base_area" VALUES ('540521', '扎囊县', '5405');
INSERT INTO "public"."base_area" VALUES ('540522', '贡嘎县', '5405');
INSERT INTO "public"."base_area" VALUES ('540523', '桑日县', '5405');
INSERT INTO "public"."base_area" VALUES ('540524', '琼结县', '5405');
INSERT INTO "public"."base_area" VALUES ('540525', '曲松县', '5405');
INSERT INTO "public"."base_area" VALUES ('540526', '措美县', '5405');
INSERT INTO "public"."base_area" VALUES ('540527', '洛扎县', '5405');
INSERT INTO "public"."base_area" VALUES ('540528', '加查县', '5405');
INSERT INTO "public"."base_area" VALUES ('540529', '隆子县', '5405');
INSERT INTO "public"."base_area" VALUES ('540530', '错那县', '5405');
INSERT INTO "public"."base_area" VALUES ('540531', '浪卡子县', '5405');
INSERT INTO "public"."base_area" VALUES ('540602', '色尼区', '5406');
INSERT INTO "public"."base_area" VALUES ('540621', '嘉黎县', '5406');
INSERT INTO "public"."base_area" VALUES ('540622', '比如县', '5406');
INSERT INTO "public"."base_area" VALUES ('540623', '聂荣县', '5406');
INSERT INTO "public"."base_area" VALUES ('540624', '安多县', '5406');
INSERT INTO "public"."base_area" VALUES ('540625', '申扎县', '5406');
INSERT INTO "public"."base_area" VALUES ('540626', '索县', '5406');
INSERT INTO "public"."base_area" VALUES ('540627', '班戈县', '5406');
INSERT INTO "public"."base_area" VALUES ('540628', '巴青县', '5406');
INSERT INTO "public"."base_area" VALUES ('540629', '尼玛县', '5406');
INSERT INTO "public"."base_area" VALUES ('540630', '双湖县', '5406');
INSERT INTO "public"."base_area" VALUES ('542521', '普兰县', '5425');
INSERT INTO "public"."base_area" VALUES ('542522', '札达县', '5425');
INSERT INTO "public"."base_area" VALUES ('542523', '噶尔县', '5425');
INSERT INTO "public"."base_area" VALUES ('542524', '日土县', '5425');
INSERT INTO "public"."base_area" VALUES ('542525', '革吉县', '5425');
INSERT INTO "public"."base_area" VALUES ('542526', '改则县', '5425');
INSERT INTO "public"."base_area" VALUES ('542527', '措勤县', '5425');
INSERT INTO "public"."base_area" VALUES ('610102', '新城区', '6101');
INSERT INTO "public"."base_area" VALUES ('610103', '碑林区', '6101');
INSERT INTO "public"."base_area" VALUES ('610104', '莲湖区', '6101');
INSERT INTO "public"."base_area" VALUES ('610111', '灞桥区', '6101');
INSERT INTO "public"."base_area" VALUES ('610112', '未央区', '6101');
INSERT INTO "public"."base_area" VALUES ('610113', '雁塔区', '6101');
INSERT INTO "public"."base_area" VALUES ('610114', '阎良区', '6101');
INSERT INTO "public"."base_area" VALUES ('610115', '临潼区', '6101');
INSERT INTO "public"."base_area" VALUES ('610116', '长安区', '6101');
INSERT INTO "public"."base_area" VALUES ('610117', '高陵区', '6101');
INSERT INTO "public"."base_area" VALUES ('610118', '鄠邑区', '6101');
INSERT INTO "public"."base_area" VALUES ('610122', '蓝田县', '6101');
INSERT INTO "public"."base_area" VALUES ('610124', '周至县', '6101');
INSERT INTO "public"."base_area" VALUES ('610202', '王益区', '6102');
INSERT INTO "public"."base_area" VALUES ('610203', '印台区', '6102');
INSERT INTO "public"."base_area" VALUES ('610204', '耀州区', '6102');
INSERT INTO "public"."base_area" VALUES ('610222', '宜君县', '6102');
INSERT INTO "public"."base_area" VALUES ('610302', '渭滨区', '6103');
INSERT INTO "public"."base_area" VALUES ('610303', '金台区', '6103');
INSERT INTO "public"."base_area" VALUES ('610304', '陈仓区', '6103');
INSERT INTO "public"."base_area" VALUES ('610305', '凤翔区', '6103');
INSERT INTO "public"."base_area" VALUES ('610323', '岐山县', '6103');
INSERT INTO "public"."base_area" VALUES ('610324', '扶风县', '6103');
INSERT INTO "public"."base_area" VALUES ('610326', '眉县', '6103');
INSERT INTO "public"."base_area" VALUES ('610327', '陇县', '6103');
INSERT INTO "public"."base_area" VALUES ('610328', '千阳县', '6103');
INSERT INTO "public"."base_area" VALUES ('610329', '麟游县', '6103');
INSERT INTO "public"."base_area" VALUES ('610330', '凤县', '6103');
INSERT INTO "public"."base_area" VALUES ('610331', '太白县', '6103');
INSERT INTO "public"."base_area" VALUES ('610402', '秦都区', '6104');
INSERT INTO "public"."base_area" VALUES ('610403', '杨陵区', '6104');
INSERT INTO "public"."base_area" VALUES ('610404', '渭城区', '6104');
INSERT INTO "public"."base_area" VALUES ('610422', '三原县', '6104');
INSERT INTO "public"."base_area" VALUES ('610423', '泾阳县', '6104');
INSERT INTO "public"."base_area" VALUES ('610424', '乾县', '6104');
INSERT INTO "public"."base_area" VALUES ('610425', '礼泉县', '6104');
INSERT INTO "public"."base_area" VALUES ('610426', '永寿县', '6104');
INSERT INTO "public"."base_area" VALUES ('610428', '长武县', '6104');
INSERT INTO "public"."base_area" VALUES ('610429', '旬邑县', '6104');
INSERT INTO "public"."base_area" VALUES ('610430', '淳化县', '6104');
INSERT INTO "public"."base_area" VALUES ('610431', '武功县', '6104');
INSERT INTO "public"."base_area" VALUES ('610481', '兴平市', '6104');
INSERT INTO "public"."base_area" VALUES ('610482', '彬州市', '6104');
INSERT INTO "public"."base_area" VALUES ('610502', '临渭区', '6105');
INSERT INTO "public"."base_area" VALUES ('610503', '华州区', '6105');
INSERT INTO "public"."base_area" VALUES ('610522', '潼关县', '6105');
INSERT INTO "public"."base_area" VALUES ('610523', '大荔县', '6105');
INSERT INTO "public"."base_area" VALUES ('610524', '合阳县', '6105');
INSERT INTO "public"."base_area" VALUES ('610525', '澄城县', '6105');
INSERT INTO "public"."base_area" VALUES ('610526', '蒲城县', '6105');
INSERT INTO "public"."base_area" VALUES ('610527', '白水县', '6105');
INSERT INTO "public"."base_area" VALUES ('610528', '富平县', '6105');
INSERT INTO "public"."base_area" VALUES ('610581', '韩城市', '6105');
INSERT INTO "public"."base_area" VALUES ('610582', '华阴市', '6105');
INSERT INTO "public"."base_area" VALUES ('610602', '宝塔区', '6106');
INSERT INTO "public"."base_area" VALUES ('610603', '安塞区', '6106');
INSERT INTO "public"."base_area" VALUES ('610621', '延长县', '6106');
INSERT INTO "public"."base_area" VALUES ('610622', '延川县', '6106');
INSERT INTO "public"."base_area" VALUES ('610625', '志丹县', '6106');
INSERT INTO "public"."base_area" VALUES ('610626', '吴起县', '6106');
INSERT INTO "public"."base_area" VALUES ('610627', '甘泉县', '6106');
INSERT INTO "public"."base_area" VALUES ('610628', '富县', '6106');
INSERT INTO "public"."base_area" VALUES ('610629', '洛川县', '6106');
INSERT INTO "public"."base_area" VALUES ('610630', '宜川县', '6106');
INSERT INTO "public"."base_area" VALUES ('610631', '黄龙县', '6106');
INSERT INTO "public"."base_area" VALUES ('610632', '黄陵县', '6106');
INSERT INTO "public"."base_area" VALUES ('610681', '子长市', '6106');
INSERT INTO "public"."base_area" VALUES ('610702', '汉台区', '6107');
INSERT INTO "public"."base_area" VALUES ('610703', '南郑区', '6107');
INSERT INTO "public"."base_area" VALUES ('610722', '城固县', '6107');
INSERT INTO "public"."base_area" VALUES ('610723', '洋县', '6107');
INSERT INTO "public"."base_area" VALUES ('610724', '西乡县', '6107');
INSERT INTO "public"."base_area" VALUES ('610725', '勉县', '6107');
INSERT INTO "public"."base_area" VALUES ('610726', '宁强县', '6107');
INSERT INTO "public"."base_area" VALUES ('610727', '略阳县', '6107');
INSERT INTO "public"."base_area" VALUES ('610728', '镇巴县', '6107');
INSERT INTO "public"."base_area" VALUES ('610729', '留坝县', '6107');
INSERT INTO "public"."base_area" VALUES ('610730', '佛坪县', '6107');
INSERT INTO "public"."base_area" VALUES ('610802', '榆阳区', '6108');
INSERT INTO "public"."base_area" VALUES ('610803', '横山区', '6108');
INSERT INTO "public"."base_area" VALUES ('610822', '府谷县', '6108');
INSERT INTO "public"."base_area" VALUES ('610824', '靖边县', '6108');
INSERT INTO "public"."base_area" VALUES ('610825', '定边县', '6108');
INSERT INTO "public"."base_area" VALUES ('610826', '绥德县', '6108');
INSERT INTO "public"."base_area" VALUES ('610827', '米脂县', '6108');
INSERT INTO "public"."base_area" VALUES ('610828', '佳县', '6108');
INSERT INTO "public"."base_area" VALUES ('610829', '吴堡县', '6108');
INSERT INTO "public"."base_area" VALUES ('610830', '清涧县', '6108');
INSERT INTO "public"."base_area" VALUES ('610831', '子洲县', '6108');
INSERT INTO "public"."base_area" VALUES ('610881', '神木市', '6108');
INSERT INTO "public"."base_area" VALUES ('610902', '汉滨区', '6109');
INSERT INTO "public"."base_area" VALUES ('610921', '汉阴县', '6109');
INSERT INTO "public"."base_area" VALUES ('610922', '石泉县', '6109');
INSERT INTO "public"."base_area" VALUES ('610923', '宁陕县', '6109');
INSERT INTO "public"."base_area" VALUES ('610924', '紫阳县', '6109');
INSERT INTO "public"."base_area" VALUES ('610925', '岚皋县', '6109');
INSERT INTO "public"."base_area" VALUES ('610926', '平利县', '6109');
INSERT INTO "public"."base_area" VALUES ('610927', '镇坪县', '6109');
INSERT INTO "public"."base_area" VALUES ('610929', '白河县', '6109');
INSERT INTO "public"."base_area" VALUES ('610981', '旬阳市', '6109');
INSERT INTO "public"."base_area" VALUES ('611002', '商州区', '6110');
INSERT INTO "public"."base_area" VALUES ('611021', '洛南县', '6110');
INSERT INTO "public"."base_area" VALUES ('611022', '丹凤县', '6110');
INSERT INTO "public"."base_area" VALUES ('611023', '商南县', '6110');
INSERT INTO "public"."base_area" VALUES ('611024', '山阳县', '6110');
INSERT INTO "public"."base_area" VALUES ('611025', '镇安县', '6110');
INSERT INTO "public"."base_area" VALUES ('611026', '柞水县', '6110');
INSERT INTO "public"."base_area" VALUES ('620102', '城关区', '6201');
INSERT INTO "public"."base_area" VALUES ('620103', '七里河区', '6201');
INSERT INTO "public"."base_area" VALUES ('620104', '西固区', '6201');
INSERT INTO "public"."base_area" VALUES ('620105', '安宁区', '6201');
INSERT INTO "public"."base_area" VALUES ('620111', '红古区', '6201');
INSERT INTO "public"."base_area" VALUES ('620121', '永登县', '6201');
INSERT INTO "public"."base_area" VALUES ('620122', '皋兰县', '6201');
INSERT INTO "public"."base_area" VALUES ('620123', '榆中县', '6201');
INSERT INTO "public"."base_area" VALUES ('620171', '兰州新区', '6201');
INSERT INTO "public"."base_area" VALUES ('620201', '嘉峪关市', '6202');
INSERT INTO "public"."base_area" VALUES ('620302', '金川区', '6203');
INSERT INTO "public"."base_area" VALUES ('620321', '永昌县', '6203');
INSERT INTO "public"."base_area" VALUES ('620402', '白银区', '6204');
INSERT INTO "public"."base_area" VALUES ('620403', '平川区', '6204');
INSERT INTO "public"."base_area" VALUES ('620421', '靖远县', '6204');
INSERT INTO "public"."base_area" VALUES ('620422', '会宁县', '6204');
INSERT INTO "public"."base_area" VALUES ('620423', '景泰县', '6204');
INSERT INTO "public"."base_area" VALUES ('620502', '秦州区', '6205');
INSERT INTO "public"."base_area" VALUES ('620503', '麦积区', '6205');
INSERT INTO "public"."base_area" VALUES ('620521', '清水县', '6205');
INSERT INTO "public"."base_area" VALUES ('620522', '秦安县', '6205');
INSERT INTO "public"."base_area" VALUES ('620523', '甘谷县', '6205');
INSERT INTO "public"."base_area" VALUES ('620524', '武山县', '6205');
INSERT INTO "public"."base_area" VALUES ('620525', '张家川回族自治县', '6205');
INSERT INTO "public"."base_area" VALUES ('620602', '凉州区', '6206');
INSERT INTO "public"."base_area" VALUES ('620621', '民勤县', '6206');
INSERT INTO "public"."base_area" VALUES ('620622', '古浪县', '6206');
INSERT INTO "public"."base_area" VALUES ('620623', '天祝藏族自治县', '6206');
INSERT INTO "public"."base_area" VALUES ('620702', '甘州区', '6207');
INSERT INTO "public"."base_area" VALUES ('620721', '肃南裕固族自治县', '6207');
INSERT INTO "public"."base_area" VALUES ('620722', '民乐县', '6207');
INSERT INTO "public"."base_area" VALUES ('620723', '临泽县', '6207');
INSERT INTO "public"."base_area" VALUES ('620724', '高台县', '6207');
INSERT INTO "public"."base_area" VALUES ('620725', '山丹县', '6207');
INSERT INTO "public"."base_area" VALUES ('620802', '崆峒区', '6208');
INSERT INTO "public"."base_area" VALUES ('620821', '泾川县', '6208');
INSERT INTO "public"."base_area" VALUES ('620822', '灵台县', '6208');
INSERT INTO "public"."base_area" VALUES ('620823', '崇信县', '6208');
INSERT INTO "public"."base_area" VALUES ('620825', '庄浪县', '6208');
INSERT INTO "public"."base_area" VALUES ('620826', '静宁县', '6208');
INSERT INTO "public"."base_area" VALUES ('620881', '华亭市', '6208');
INSERT INTO "public"."base_area" VALUES ('620902', '肃州区', '6209');
INSERT INTO "public"."base_area" VALUES ('620921', '金塔县', '6209');
INSERT INTO "public"."base_area" VALUES ('620922', '瓜州县', '6209');
INSERT INTO "public"."base_area" VALUES ('620923', '肃北蒙古族自治县', '6209');
INSERT INTO "public"."base_area" VALUES ('620924', '阿克塞哈萨克族自治县', '6209');
INSERT INTO "public"."base_area" VALUES ('620981', '玉门市', '6209');
INSERT INTO "public"."base_area" VALUES ('620982', '敦煌市', '6209');
INSERT INTO "public"."base_area" VALUES ('621002', '西峰区', '6210');
INSERT INTO "public"."base_area" VALUES ('621021', '庆城县', '6210');
INSERT INTO "public"."base_area" VALUES ('621022', '环县', '6210');
INSERT INTO "public"."base_area" VALUES ('621023', '华池县', '6210');
INSERT INTO "public"."base_area" VALUES ('621024', '合水县', '6210');
INSERT INTO "public"."base_area" VALUES ('621025', '正宁县', '6210');
INSERT INTO "public"."base_area" VALUES ('621026', '宁县', '6210');
INSERT INTO "public"."base_area" VALUES ('621027', '镇原县', '6210');
INSERT INTO "public"."base_area" VALUES ('621102', '安定区', '6211');
INSERT INTO "public"."base_area" VALUES ('621121', '通渭县', '6211');
INSERT INTO "public"."base_area" VALUES ('621122', '陇西县', '6211');
INSERT INTO "public"."base_area" VALUES ('621123', '渭源县', '6211');
INSERT INTO "public"."base_area" VALUES ('621124', '临洮县', '6211');
INSERT INTO "public"."base_area" VALUES ('621125', '漳县', '6211');
INSERT INTO "public"."base_area" VALUES ('621126', '岷县', '6211');
INSERT INTO "public"."base_area" VALUES ('621202', '武都区', '6212');
INSERT INTO "public"."base_area" VALUES ('621221', '成县', '6212');
INSERT INTO "public"."base_area" VALUES ('621222', '文县', '6212');
INSERT INTO "public"."base_area" VALUES ('621223', '宕昌县', '6212');
INSERT INTO "public"."base_area" VALUES ('621224', '康县', '6212');
INSERT INTO "public"."base_area" VALUES ('621225', '西和县', '6212');
INSERT INTO "public"."base_area" VALUES ('621226', '礼县', '6212');
INSERT INTO "public"."base_area" VALUES ('621227', '徽县', '6212');
INSERT INTO "public"."base_area" VALUES ('621228', '两当县', '6212');
INSERT INTO "public"."base_area" VALUES ('622901', '临夏市', '6229');
INSERT INTO "public"."base_area" VALUES ('622921', '临夏县', '6229');
INSERT INTO "public"."base_area" VALUES ('622922', '康乐县', '6229');
INSERT INTO "public"."base_area" VALUES ('622923', '永靖县', '6229');
INSERT INTO "public"."base_area" VALUES ('622924', '广河县', '6229');
INSERT INTO "public"."base_area" VALUES ('622925', '和政县', '6229');
INSERT INTO "public"."base_area" VALUES ('622926', '东乡族自治县', '6229');
INSERT INTO "public"."base_area" VALUES ('622927', '积石山保安族东乡族撒拉族自治县', '6229');
INSERT INTO "public"."base_area" VALUES ('623001', '合作市', '6230');
INSERT INTO "public"."base_area" VALUES ('623021', '临潭县', '6230');
INSERT INTO "public"."base_area" VALUES ('623022', '卓尼县', '6230');
INSERT INTO "public"."base_area" VALUES ('623023', '舟曲县', '6230');
INSERT INTO "public"."base_area" VALUES ('623024', '迭部县', '6230');
INSERT INTO "public"."base_area" VALUES ('623025', '玛曲县', '6230');
INSERT INTO "public"."base_area" VALUES ('623026', '碌曲县', '6230');
INSERT INTO "public"."base_area" VALUES ('623027', '夏河县', '6230');
INSERT INTO "public"."base_area" VALUES ('630102', '城东区', '6301');
INSERT INTO "public"."base_area" VALUES ('630103', '城中区', '6301');
INSERT INTO "public"."base_area" VALUES ('630104', '城西区', '6301');
INSERT INTO "public"."base_area" VALUES ('630105', '城北区', '6301');
INSERT INTO "public"."base_area" VALUES ('630106', '湟中区', '6301');
INSERT INTO "public"."base_area" VALUES ('630121', '大通回族土族自治县', '6301');
INSERT INTO "public"."base_area" VALUES ('630123', '湟源县', '6301');
INSERT INTO "public"."base_area" VALUES ('630202', '乐都区', '6302');
INSERT INTO "public"."base_area" VALUES ('630203', '平安区', '6302');
INSERT INTO "public"."base_area" VALUES ('630222', '民和回族土族自治县', '6302');
INSERT INTO "public"."base_area" VALUES ('630223', '互助土族自治县', '6302');
INSERT INTO "public"."base_area" VALUES ('630224', '化隆回族自治县', '6302');
INSERT INTO "public"."base_area" VALUES ('630225', '循化撒拉族自治县', '6302');
INSERT INTO "public"."base_area" VALUES ('632221', '门源回族自治县', '6322');
INSERT INTO "public"."base_area" VALUES ('632222', '祁连县', '6322');
INSERT INTO "public"."base_area" VALUES ('632223', '海晏县', '6322');
INSERT INTO "public"."base_area" VALUES ('632224', '刚察县', '6322');
INSERT INTO "public"."base_area" VALUES ('632301', '同仁市', '6323');
INSERT INTO "public"."base_area" VALUES ('632322', '尖扎县', '6323');
INSERT INTO "public"."base_area" VALUES ('632323', '泽库县', '6323');
INSERT INTO "public"."base_area" VALUES ('632324', '河南蒙古族自治县', '6323');
INSERT INTO "public"."base_area" VALUES ('632521', '共和县', '6325');
INSERT INTO "public"."base_area" VALUES ('632522', '同德县', '6325');
INSERT INTO "public"."base_area" VALUES ('632523', '贵德县', '6325');
INSERT INTO "public"."base_area" VALUES ('632524', '兴海县', '6325');
INSERT INTO "public"."base_area" VALUES ('632525', '贵南县', '6325');
INSERT INTO "public"."base_area" VALUES ('632621', '玛沁县', '6326');
INSERT INTO "public"."base_area" VALUES ('632622', '班玛县', '6326');
INSERT INTO "public"."base_area" VALUES ('632623', '甘德县', '6326');
INSERT INTO "public"."base_area" VALUES ('632624', '达日县', '6326');
INSERT INTO "public"."base_area" VALUES ('632625', '久治县', '6326');
INSERT INTO "public"."base_area" VALUES ('632626', '玛多县', '6326');
INSERT INTO "public"."base_area" VALUES ('632701', '玉树市', '6327');
INSERT INTO "public"."base_area" VALUES ('632722', '杂多县', '6327');
INSERT INTO "public"."base_area" VALUES ('632723', '称多县', '6327');
INSERT INTO "public"."base_area" VALUES ('632724', '治多县', '6327');
INSERT INTO "public"."base_area" VALUES ('632725', '囊谦县', '6327');
INSERT INTO "public"."base_area" VALUES ('632726', '曲麻莱县', '6327');
INSERT INTO "public"."base_area" VALUES ('632801', '格尔木市', '6328');
INSERT INTO "public"."base_area" VALUES ('632802', '德令哈市', '6328');
INSERT INTO "public"."base_area" VALUES ('632803', '茫崖市', '6328');
INSERT INTO "public"."base_area" VALUES ('632821', '乌兰县', '6328');
INSERT INTO "public"."base_area" VALUES ('632822', '都兰县', '6328');
INSERT INTO "public"."base_area" VALUES ('632823', '天峻县', '6328');
INSERT INTO "public"."base_area" VALUES ('632857', '大柴旦行政委员会', '6328');
INSERT INTO "public"."base_area" VALUES ('640104', '兴庆区', '6401');
INSERT INTO "public"."base_area" VALUES ('640105', '西夏区', '6401');
INSERT INTO "public"."base_area" VALUES ('640106', '金凤区', '6401');
INSERT INTO "public"."base_area" VALUES ('640121', '永宁县', '6401');
INSERT INTO "public"."base_area" VALUES ('640122', '贺兰县', '6401');
INSERT INTO "public"."base_area" VALUES ('640181', '灵武市', '6401');
INSERT INTO "public"."base_area" VALUES ('640202', '大武口区', '6402');
INSERT INTO "public"."base_area" VALUES ('640205', '惠农区', '6402');
INSERT INTO "public"."base_area" VALUES ('640221', '平罗县', '6402');
INSERT INTO "public"."base_area" VALUES ('640302', '利通区', '6403');
INSERT INTO "public"."base_area" VALUES ('640303', '红寺堡区', '6403');
INSERT INTO "public"."base_area" VALUES ('640323', '盐池县', '6403');
INSERT INTO "public"."base_area" VALUES ('640324', '同心县', '6403');
INSERT INTO "public"."base_area" VALUES ('640381', '青铜峡市', '6403');
INSERT INTO "public"."base_area" VALUES ('640402', '原州区', '6404');
INSERT INTO "public"."base_area" VALUES ('640422', '西吉县', '6404');
INSERT INTO "public"."base_area" VALUES ('640423', '隆德县', '6404');
INSERT INTO "public"."base_area" VALUES ('640424', '泾源县', '6404');
INSERT INTO "public"."base_area" VALUES ('640425', '彭阳县', '6404');
INSERT INTO "public"."base_area" VALUES ('640502', '沙坡头区', '6405');
INSERT INTO "public"."base_area" VALUES ('640521', '中宁县', '6405');
INSERT INTO "public"."base_area" VALUES ('640522', '海原县', '6405');
INSERT INTO "public"."base_area" VALUES ('650102', '天山区', '6501');
INSERT INTO "public"."base_area" VALUES ('650103', '沙依巴克区', '6501');
INSERT INTO "public"."base_area" VALUES ('650104', '新市区', '6501');
INSERT INTO "public"."base_area" VALUES ('650105', '水磨沟区', '6501');
INSERT INTO "public"."base_area" VALUES ('650106', '头屯河区', '6501');
INSERT INTO "public"."base_area" VALUES ('650107', '达坂城区', '6501');
INSERT INTO "public"."base_area" VALUES ('650109', '米东区', '6501');
INSERT INTO "public"."base_area" VALUES ('650121', '乌鲁木齐县', '6501');
INSERT INTO "public"."base_area" VALUES ('650202', '独山子区', '6502');
INSERT INTO "public"."base_area" VALUES ('650203', '克拉玛依区', '6502');
INSERT INTO "public"."base_area" VALUES ('650204', '白碱滩区', '6502');
INSERT INTO "public"."base_area" VALUES ('650205', '乌尔禾区', '6502');
INSERT INTO "public"."base_area" VALUES ('650402', '高昌区', '6504');
INSERT INTO "public"."base_area" VALUES ('650421', '鄯善县', '6504');
INSERT INTO "public"."base_area" VALUES ('650422', '托克逊县', '6504');
INSERT INTO "public"."base_area" VALUES ('650502', '伊州区', '6505');
INSERT INTO "public"."base_area" VALUES ('650521', '巴里坤哈萨克自治县', '6505');
INSERT INTO "public"."base_area" VALUES ('650522', '伊吾县', '6505');
INSERT INTO "public"."base_area" VALUES ('652301', '昌吉市', '6523');
INSERT INTO "public"."base_area" VALUES ('652302', '阜康市', '6523');
INSERT INTO "public"."base_area" VALUES ('652323', '呼图壁县', '6523');
INSERT INTO "public"."base_area" VALUES ('652324', '玛纳斯县', '6523');
INSERT INTO "public"."base_area" VALUES ('652325', '奇台县', '6523');
INSERT INTO "public"."base_area" VALUES ('652327', '吉木萨尔县', '6523');
INSERT INTO "public"."base_area" VALUES ('652328', '木垒哈萨克自治县', '6523');
INSERT INTO "public"."base_area" VALUES ('652701', '博乐市', '6527');
INSERT INTO "public"."base_area" VALUES ('652702', '阿拉山口市', '6527');
INSERT INTO "public"."base_area" VALUES ('652722', '精河县', '6527');
INSERT INTO "public"."base_area" VALUES ('652723', '温泉县', '6527');
INSERT INTO "public"."base_area" VALUES ('652801', '库尔勒市', '6528');
INSERT INTO "public"."base_area" VALUES ('652822', '轮台县', '6528');
INSERT INTO "public"."base_area" VALUES ('652823', '尉犁县', '6528');
INSERT INTO "public"."base_area" VALUES ('652824', '若羌县', '6528');
INSERT INTO "public"."base_area" VALUES ('652825', '且末县', '6528');
INSERT INTO "public"."base_area" VALUES ('652826', '焉耆回族自治县', '6528');
INSERT INTO "public"."base_area" VALUES ('652827', '和静县', '6528');
INSERT INTO "public"."base_area" VALUES ('652828', '和硕县', '6528');
INSERT INTO "public"."base_area" VALUES ('652829', '博湖县', '6528');
INSERT INTO "public"."base_area" VALUES ('652871', '库尔勒经济技术开发区', '6528');
INSERT INTO "public"."base_area" VALUES ('652901', '阿克苏市', '6529');
INSERT INTO "public"."base_area" VALUES ('652902', '库车市', '6529');
INSERT INTO "public"."base_area" VALUES ('652922', '温宿县', '6529');
INSERT INTO "public"."base_area" VALUES ('652924', '沙雅县', '6529');
INSERT INTO "public"."base_area" VALUES ('652925', '新和县', '6529');
INSERT INTO "public"."base_area" VALUES ('652926', '拜城县', '6529');
INSERT INTO "public"."base_area" VALUES ('652927', '乌什县', '6529');
INSERT INTO "public"."base_area" VALUES ('652928', '阿瓦提县', '6529');
INSERT INTO "public"."base_area" VALUES ('652929', '柯坪县', '6529');
INSERT INTO "public"."base_area" VALUES ('653001', '阿图什市', '6530');
INSERT INTO "public"."base_area" VALUES ('653022', '阿克陶县', '6530');
INSERT INTO "public"."base_area" VALUES ('653023', '阿合奇县', '6530');
INSERT INTO "public"."base_area" VALUES ('653024', '乌恰县', '6530');
INSERT INTO "public"."base_area" VALUES ('653101', '喀什市', '6531');
INSERT INTO "public"."base_area" VALUES ('653121', '疏附县', '6531');
INSERT INTO "public"."base_area" VALUES ('653122', '疏勒县', '6531');
INSERT INTO "public"."base_area" VALUES ('653123', '英吉沙县', '6531');
INSERT INTO "public"."base_area" VALUES ('653124', '泽普县', '6531');
INSERT INTO "public"."base_area" VALUES ('653125', '莎车县', '6531');
INSERT INTO "public"."base_area" VALUES ('653126', '叶城县', '6531');
INSERT INTO "public"."base_area" VALUES ('653127', '麦盖提县', '6531');
INSERT INTO "public"."base_area" VALUES ('653128', '岳普湖县', '6531');
INSERT INTO "public"."base_area" VALUES ('653129', '伽师县', '6531');
INSERT INTO "public"."base_area" VALUES ('653130', '巴楚县', '6531');
INSERT INTO "public"."base_area" VALUES ('653131', '塔什库尔干塔吉克自治县', '6531');
INSERT INTO "public"."base_area" VALUES ('653201', '和田市', '6532');
INSERT INTO "public"."base_area" VALUES ('653221', '和田县', '6532');
INSERT INTO "public"."base_area" VALUES ('653222', '墨玉县', '6532');
INSERT INTO "public"."base_area" VALUES ('653223', '皮山县', '6532');
INSERT INTO "public"."base_area" VALUES ('653224', '洛浦县', '6532');
INSERT INTO "public"."base_area" VALUES ('653225', '策勒县', '6532');
INSERT INTO "public"."base_area" VALUES ('653226', '于田县', '6532');
INSERT INTO "public"."base_area" VALUES ('653227', '民丰县', '6532');
INSERT INTO "public"."base_area" VALUES ('654002', '伊宁市', '6540');
INSERT INTO "public"."base_area" VALUES ('654003', '奎屯市', '6540');
INSERT INTO "public"."base_area" VALUES ('654004', '霍尔果斯市', '6540');
INSERT INTO "public"."base_area" VALUES ('654021', '伊宁县', '6540');
INSERT INTO "public"."base_area" VALUES ('654022', '察布查尔锡伯自治县', '6540');
INSERT INTO "public"."base_area" VALUES ('654023', '霍城县', '6540');
INSERT INTO "public"."base_area" VALUES ('654024', '巩留县', '6540');
INSERT INTO "public"."base_area" VALUES ('654025', '新源县', '6540');
INSERT INTO "public"."base_area" VALUES ('654026', '昭苏县', '6540');
INSERT INTO "public"."base_area" VALUES ('654027', '特克斯县', '6540');
INSERT INTO "public"."base_area" VALUES ('654028', '尼勒克县', '6540');
INSERT INTO "public"."base_area" VALUES ('654201', '塔城市', '6542');
INSERT INTO "public"."base_area" VALUES ('654202', '乌苏市', '6542');
INSERT INTO "public"."base_area" VALUES ('654203', '沙湾市', '6542');
INSERT INTO "public"."base_area" VALUES ('654221', '额敏县', '6542');
INSERT INTO "public"."base_area" VALUES ('654224', '托里县', '6542');
INSERT INTO "public"."base_area" VALUES ('654225', '裕民县', '6542');
INSERT INTO "public"."base_area" VALUES ('654226', '和布克赛尔蒙古自治县', '6542');
INSERT INTO "public"."base_area" VALUES ('654301', '阿勒泰市', '6543');
INSERT INTO "public"."base_area" VALUES ('654321', '布尔津县', '6543');
INSERT INTO "public"."base_area" VALUES ('654322', '富蕴县', '6543');
INSERT INTO "public"."base_area" VALUES ('654323', '福海县', '6543');
INSERT INTO "public"."base_area" VALUES ('654324', '哈巴河县', '6543');
INSERT INTO "public"."base_area" VALUES ('654325', '青河县', '6543');
INSERT INTO "public"."base_area" VALUES ('654326', '吉木乃县', '6543');
INSERT INTO "public"."base_area" VALUES ('659001', '石河子市', '6590');
INSERT INTO "public"."base_area" VALUES ('659002', '阿拉尔市', '6590');
INSERT INTO "public"."base_area" VALUES ('659003', '图木舒克市', '6590');
INSERT INTO "public"."base_area" VALUES ('659004', '五家渠市', '6590');
INSERT INTO "public"."base_area" VALUES ('659005', '北屯市', '6590');
INSERT INTO "public"."base_area" VALUES ('659006', '铁门关市', '6590');
INSERT INTO "public"."base_area" VALUES ('659007', '双河市', '6590');
INSERT INTO "public"."base_area" VALUES ('659008', '可克达拉市', '6590');
INSERT INTO "public"."base_area" VALUES ('659009', '昆玉市', '6590');
INSERT INTO "public"."base_area" VALUES ('659010', '胡杨河市', '6590');
INSERT INTO "public"."base_area" VALUES ('659011', '新星市', '6590');

-- ----------------------------
-- Table structure for base_city
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_city";
CREATE TABLE "public"."base_city" (
  "code" varchar(4) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(60) COLLATE "pg_catalog"."default" NOT NULL,
  "province_code" varchar(2) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."base_city"."code" IS '城市编码';
COMMENT ON COLUMN "public"."base_city"."name" IS '城市名称';
COMMENT ON COLUMN "public"."base_city"."province_code" IS '省份编码';
COMMENT ON TABLE "public"."base_city" IS '城市表';

-- ----------------------------
-- Records of base_city
-- ----------------------------
INSERT INTO "public"."base_city" VALUES ('1101', '市辖区', '11');
INSERT INTO "public"."base_city" VALUES ('1201', '市辖区', '12');
INSERT INTO "public"."base_city" VALUES ('1301', '石家庄市', '13');
INSERT INTO "public"."base_city" VALUES ('1302', '唐山市', '13');
INSERT INTO "public"."base_city" VALUES ('1303', '秦皇岛市', '13');
INSERT INTO "public"."base_city" VALUES ('1304', '邯郸市', '13');
INSERT INTO "public"."base_city" VALUES ('1305', '邢台市', '13');
INSERT INTO "public"."base_city" VALUES ('1306', '保定市', '13');
INSERT INTO "public"."base_city" VALUES ('1307', '张家口市', '13');
INSERT INTO "public"."base_city" VALUES ('1308', '承德市', '13');
INSERT INTO "public"."base_city" VALUES ('1309', '沧州市', '13');
INSERT INTO "public"."base_city" VALUES ('1310', '廊坊市', '13');
INSERT INTO "public"."base_city" VALUES ('1311', '衡水市', '13');
INSERT INTO "public"."base_city" VALUES ('1401', '太原市', '14');
INSERT INTO "public"."base_city" VALUES ('1402', '大同市', '14');
INSERT INTO "public"."base_city" VALUES ('1403', '阳泉市', '14');
INSERT INTO "public"."base_city" VALUES ('1404', '长治市', '14');
INSERT INTO "public"."base_city" VALUES ('1405', '晋城市', '14');
INSERT INTO "public"."base_city" VALUES ('1406', '朔州市', '14');
INSERT INTO "public"."base_city" VALUES ('1407', '晋中市', '14');
INSERT INTO "public"."base_city" VALUES ('1408', '运城市', '14');
INSERT INTO "public"."base_city" VALUES ('1409', '忻州市', '14');
INSERT INTO "public"."base_city" VALUES ('1410', '临汾市', '14');
INSERT INTO "public"."base_city" VALUES ('1411', '吕梁市', '14');
INSERT INTO "public"."base_city" VALUES ('1501', '呼和浩特市', '15');
INSERT INTO "public"."base_city" VALUES ('1502', '包头市', '15');
INSERT INTO "public"."base_city" VALUES ('1503', '乌海市', '15');
INSERT INTO "public"."base_city" VALUES ('1504', '赤峰市', '15');
INSERT INTO "public"."base_city" VALUES ('1505', '通辽市', '15');
INSERT INTO "public"."base_city" VALUES ('1506', '鄂尔多斯市', '15');
INSERT INTO "public"."base_city" VALUES ('1507', '呼伦贝尔市', '15');
INSERT INTO "public"."base_city" VALUES ('1508', '巴彦淖尔市', '15');
INSERT INTO "public"."base_city" VALUES ('1509', '乌兰察布市', '15');
INSERT INTO "public"."base_city" VALUES ('1522', '兴安盟', '15');
INSERT INTO "public"."base_city" VALUES ('1525', '锡林郭勒盟', '15');
INSERT INTO "public"."base_city" VALUES ('1529', '阿拉善盟', '15');
INSERT INTO "public"."base_city" VALUES ('2101', '沈阳市', '21');
INSERT INTO "public"."base_city" VALUES ('2102', '大连市', '21');
INSERT INTO "public"."base_city" VALUES ('2103', '鞍山市', '21');
INSERT INTO "public"."base_city" VALUES ('2104', '抚顺市', '21');
INSERT INTO "public"."base_city" VALUES ('2105', '本溪市', '21');
INSERT INTO "public"."base_city" VALUES ('2106', '丹东市', '21');
INSERT INTO "public"."base_city" VALUES ('2107', '锦州市', '21');
INSERT INTO "public"."base_city" VALUES ('2108', '营口市', '21');
INSERT INTO "public"."base_city" VALUES ('2109', '阜新市', '21');
INSERT INTO "public"."base_city" VALUES ('2110', '辽阳市', '21');
INSERT INTO "public"."base_city" VALUES ('2111', '盘锦市', '21');
INSERT INTO "public"."base_city" VALUES ('2112', '铁岭市', '21');
INSERT INTO "public"."base_city" VALUES ('2113', '朝阳市', '21');
INSERT INTO "public"."base_city" VALUES ('2114', '葫芦岛市', '21');
INSERT INTO "public"."base_city" VALUES ('2201', '长春市', '22');
INSERT INTO "public"."base_city" VALUES ('2202', '吉林市', '22');
INSERT INTO "public"."base_city" VALUES ('2203', '四平市', '22');
INSERT INTO "public"."base_city" VALUES ('2204', '辽源市', '22');
INSERT INTO "public"."base_city" VALUES ('2205', '通化市', '22');
INSERT INTO "public"."base_city" VALUES ('2206', '白山市', '22');
INSERT INTO "public"."base_city" VALUES ('2207', '松原市', '22');
INSERT INTO "public"."base_city" VALUES ('2208', '白城市', '22');
INSERT INTO "public"."base_city" VALUES ('2224', '延边朝鲜族自治州', '22');
INSERT INTO "public"."base_city" VALUES ('2301', '哈尔滨市', '23');
INSERT INTO "public"."base_city" VALUES ('2302', '齐齐哈尔市', '23');
INSERT INTO "public"."base_city" VALUES ('2303', '鸡西市', '23');
INSERT INTO "public"."base_city" VALUES ('2304', '鹤岗市', '23');
INSERT INTO "public"."base_city" VALUES ('2305', '双鸭山市', '23');
INSERT INTO "public"."base_city" VALUES ('2306', '大庆市', '23');
INSERT INTO "public"."base_city" VALUES ('2307', '伊春市', '23');
INSERT INTO "public"."base_city" VALUES ('2308', '佳木斯市', '23');
INSERT INTO "public"."base_city" VALUES ('2309', '七台河市', '23');
INSERT INTO "public"."base_city" VALUES ('2310', '牡丹江市', '23');
INSERT INTO "public"."base_city" VALUES ('2311', '黑河市', '23');
INSERT INTO "public"."base_city" VALUES ('2312', '绥化市', '23');
INSERT INTO "public"."base_city" VALUES ('2327', '大兴安岭地区', '23');
INSERT INTO "public"."base_city" VALUES ('3101', '市辖区', '31');
INSERT INTO "public"."base_city" VALUES ('3201', '南京市', '32');
INSERT INTO "public"."base_city" VALUES ('3202', '无锡市', '32');
INSERT INTO "public"."base_city" VALUES ('3203', '徐州市', '32');
INSERT INTO "public"."base_city" VALUES ('3204', '常州市', '32');
INSERT INTO "public"."base_city" VALUES ('3205', '苏州市', '32');
INSERT INTO "public"."base_city" VALUES ('3206', '南通市', '32');
INSERT INTO "public"."base_city" VALUES ('3207', '连云港市', '32');
INSERT INTO "public"."base_city" VALUES ('3208', '淮安市', '32');
INSERT INTO "public"."base_city" VALUES ('3209', '盐城市', '32');
INSERT INTO "public"."base_city" VALUES ('3210', '扬州市', '32');
INSERT INTO "public"."base_city" VALUES ('3211', '镇江市', '32');
INSERT INTO "public"."base_city" VALUES ('3212', '泰州市', '32');
INSERT INTO "public"."base_city" VALUES ('3213', '宿迁市', '32');
INSERT INTO "public"."base_city" VALUES ('3301', '杭州市', '33');
INSERT INTO "public"."base_city" VALUES ('3302', '宁波市', '33');
INSERT INTO "public"."base_city" VALUES ('3303', '温州市', '33');
INSERT INTO "public"."base_city" VALUES ('3304', '嘉兴市', '33');
INSERT INTO "public"."base_city" VALUES ('3305', '湖州市', '33');
INSERT INTO "public"."base_city" VALUES ('3306', '绍兴市', '33');
INSERT INTO "public"."base_city" VALUES ('3307', '金华市', '33');
INSERT INTO "public"."base_city" VALUES ('3308', '衢州市', '33');
INSERT INTO "public"."base_city" VALUES ('3309', '舟山市', '33');
INSERT INTO "public"."base_city" VALUES ('3310', '台州市', '33');
INSERT INTO "public"."base_city" VALUES ('3311', '丽水市', '33');
INSERT INTO "public"."base_city" VALUES ('3401', '合肥市', '34');
INSERT INTO "public"."base_city" VALUES ('3402', '芜湖市', '34');
INSERT INTO "public"."base_city" VALUES ('3403', '蚌埠市', '34');
INSERT INTO "public"."base_city" VALUES ('3404', '淮南市', '34');
INSERT INTO "public"."base_city" VALUES ('3405', '马鞍山市', '34');
INSERT INTO "public"."base_city" VALUES ('3406', '淮北市', '34');
INSERT INTO "public"."base_city" VALUES ('3407', '铜陵市', '34');
INSERT INTO "public"."base_city" VALUES ('3408', '安庆市', '34');
INSERT INTO "public"."base_city" VALUES ('3410', '黄山市', '34');
INSERT INTO "public"."base_city" VALUES ('3411', '滁州市', '34');
INSERT INTO "public"."base_city" VALUES ('3412', '阜阳市', '34');
INSERT INTO "public"."base_city" VALUES ('3413', '宿州市', '34');
INSERT INTO "public"."base_city" VALUES ('3415', '六安市', '34');
INSERT INTO "public"."base_city" VALUES ('3416', '亳州市', '34');
INSERT INTO "public"."base_city" VALUES ('3417', '池州市', '34');
INSERT INTO "public"."base_city" VALUES ('3418', '宣城市', '34');
INSERT INTO "public"."base_city" VALUES ('3501', '福州市', '35');
INSERT INTO "public"."base_city" VALUES ('3502', '厦门市', '35');
INSERT INTO "public"."base_city" VALUES ('3503', '莆田市', '35');
INSERT INTO "public"."base_city" VALUES ('3504', '三明市', '35');
INSERT INTO "public"."base_city" VALUES ('3505', '泉州市', '35');
INSERT INTO "public"."base_city" VALUES ('3506', '漳州市', '35');
INSERT INTO "public"."base_city" VALUES ('3507', '南平市', '35');
INSERT INTO "public"."base_city" VALUES ('3508', '龙岩市', '35');
INSERT INTO "public"."base_city" VALUES ('3509', '宁德市', '35');
INSERT INTO "public"."base_city" VALUES ('3601', '南昌市', '36');
INSERT INTO "public"."base_city" VALUES ('3602', '景德镇市', '36');
INSERT INTO "public"."base_city" VALUES ('3603', '萍乡市', '36');
INSERT INTO "public"."base_city" VALUES ('3604', '九江市', '36');
INSERT INTO "public"."base_city" VALUES ('3605', '新余市', '36');
INSERT INTO "public"."base_city" VALUES ('3606', '鹰潭市', '36');
INSERT INTO "public"."base_city" VALUES ('3607', '赣州市', '36');
INSERT INTO "public"."base_city" VALUES ('3608', '吉安市', '36');
INSERT INTO "public"."base_city" VALUES ('3609', '宜春市', '36');
INSERT INTO "public"."base_city" VALUES ('3610', '抚州市', '36');
INSERT INTO "public"."base_city" VALUES ('3611', '上饶市', '36');
INSERT INTO "public"."base_city" VALUES ('3701', '济南市', '37');
INSERT INTO "public"."base_city" VALUES ('3702', '青岛市', '37');
INSERT INTO "public"."base_city" VALUES ('3703', '淄博市', '37');
INSERT INTO "public"."base_city" VALUES ('3704', '枣庄市', '37');
INSERT INTO "public"."base_city" VALUES ('3705', '东营市', '37');
INSERT INTO "public"."base_city" VALUES ('3706', '烟台市', '37');
INSERT INTO "public"."base_city" VALUES ('3707', '潍坊市', '37');
INSERT INTO "public"."base_city" VALUES ('3708', '济宁市', '37');
INSERT INTO "public"."base_city" VALUES ('3709', '泰安市', '37');
INSERT INTO "public"."base_city" VALUES ('3710', '威海市', '37');
INSERT INTO "public"."base_city" VALUES ('3711', '日照市', '37');
INSERT INTO "public"."base_city" VALUES ('3713', '临沂市', '37');
INSERT INTO "public"."base_city" VALUES ('3714', '德州市', '37');
INSERT INTO "public"."base_city" VALUES ('3715', '聊城市', '37');
INSERT INTO "public"."base_city" VALUES ('3716', '滨州市', '37');
INSERT INTO "public"."base_city" VALUES ('3717', '菏泽市', '37');
INSERT INTO "public"."base_city" VALUES ('4101', '郑州市', '41');
INSERT INTO "public"."base_city" VALUES ('4102', '开封市', '41');
INSERT INTO "public"."base_city" VALUES ('4103', '洛阳市', '41');
INSERT INTO "public"."base_city" VALUES ('4104', '平顶山市', '41');
INSERT INTO "public"."base_city" VALUES ('4105', '安阳市', '41');
INSERT INTO "public"."base_city" VALUES ('4106', '鹤壁市', '41');
INSERT INTO "public"."base_city" VALUES ('4107', '新乡市', '41');
INSERT INTO "public"."base_city" VALUES ('4108', '焦作市', '41');
INSERT INTO "public"."base_city" VALUES ('4109', '濮阳市', '41');
INSERT INTO "public"."base_city" VALUES ('4110', '许昌市', '41');
INSERT INTO "public"."base_city" VALUES ('4111', '漯河市', '41');
INSERT INTO "public"."base_city" VALUES ('4112', '三门峡市', '41');
INSERT INTO "public"."base_city" VALUES ('4113', '南阳市', '41');
INSERT INTO "public"."base_city" VALUES ('4114', '商丘市', '41');
INSERT INTO "public"."base_city" VALUES ('4115', '信阳市', '41');
INSERT INTO "public"."base_city" VALUES ('4116', '周口市', '41');
INSERT INTO "public"."base_city" VALUES ('4117', '驻马店市', '41');
INSERT INTO "public"."base_city" VALUES ('4190', '省直辖县级行政区划', '41');
INSERT INTO "public"."base_city" VALUES ('4201', '武汉市', '42');
INSERT INTO "public"."base_city" VALUES ('4202', '黄石市', '42');
INSERT INTO "public"."base_city" VALUES ('4203', '十堰市', '42');
INSERT INTO "public"."base_city" VALUES ('4205', '宜昌市', '42');
INSERT INTO "public"."base_city" VALUES ('4206', '襄阳市', '42');
INSERT INTO "public"."base_city" VALUES ('4207', '鄂州市', '42');
INSERT INTO "public"."base_city" VALUES ('4208', '荆门市', '42');
INSERT INTO "public"."base_city" VALUES ('4209', '孝感市', '42');
INSERT INTO "public"."base_city" VALUES ('4210', '荆州市', '42');
INSERT INTO "public"."base_city" VALUES ('4211', '黄冈市', '42');
INSERT INTO "public"."base_city" VALUES ('4212', '咸宁市', '42');
INSERT INTO "public"."base_city" VALUES ('4213', '随州市', '42');
INSERT INTO "public"."base_city" VALUES ('4228', '恩施土家族苗族自治州', '42');
INSERT INTO "public"."base_city" VALUES ('4290', '省直辖县级行政区划', '42');
INSERT INTO "public"."base_city" VALUES ('4301', '长沙市', '43');
INSERT INTO "public"."base_city" VALUES ('4302', '株洲市', '43');
INSERT INTO "public"."base_city" VALUES ('4303', '湘潭市', '43');
INSERT INTO "public"."base_city" VALUES ('4304', '衡阳市', '43');
INSERT INTO "public"."base_city" VALUES ('4305', '邵阳市', '43');
INSERT INTO "public"."base_city" VALUES ('4306', '岳阳市', '43');
INSERT INTO "public"."base_city" VALUES ('4307', '常德市', '43');
INSERT INTO "public"."base_city" VALUES ('4308', '张家界市', '43');
INSERT INTO "public"."base_city" VALUES ('4309', '益阳市', '43');
INSERT INTO "public"."base_city" VALUES ('4310', '郴州市', '43');
INSERT INTO "public"."base_city" VALUES ('4311', '永州市', '43');
INSERT INTO "public"."base_city" VALUES ('4312', '怀化市', '43');
INSERT INTO "public"."base_city" VALUES ('4313', '娄底市', '43');
INSERT INTO "public"."base_city" VALUES ('4331', '湘西土家族苗族自治州', '43');
INSERT INTO "public"."base_city" VALUES ('4401', '广州市', '44');
INSERT INTO "public"."base_city" VALUES ('4402', '韶关市', '44');
INSERT INTO "public"."base_city" VALUES ('4403', '深圳市', '44');
INSERT INTO "public"."base_city" VALUES ('4404', '珠海市', '44');
INSERT INTO "public"."base_city" VALUES ('4405', '汕头市', '44');
INSERT INTO "public"."base_city" VALUES ('4406', '佛山市', '44');
INSERT INTO "public"."base_city" VALUES ('4407', '江门市', '44');
INSERT INTO "public"."base_city" VALUES ('4408', '湛江市', '44');
INSERT INTO "public"."base_city" VALUES ('4409', '茂名市', '44');
INSERT INTO "public"."base_city" VALUES ('4412', '肇庆市', '44');
INSERT INTO "public"."base_city" VALUES ('4413', '惠州市', '44');
INSERT INTO "public"."base_city" VALUES ('4414', '梅州市', '44');
INSERT INTO "public"."base_city" VALUES ('4415', '汕尾市', '44');
INSERT INTO "public"."base_city" VALUES ('4416', '河源市', '44');
INSERT INTO "public"."base_city" VALUES ('4417', '阳江市', '44');
INSERT INTO "public"."base_city" VALUES ('4418', '清远市', '44');
INSERT INTO "public"."base_city" VALUES ('4419', '东莞市', '44');
INSERT INTO "public"."base_city" VALUES ('4420', '中山市', '44');
INSERT INTO "public"."base_city" VALUES ('4451', '潮州市', '44');
INSERT INTO "public"."base_city" VALUES ('4452', '揭阳市', '44');
INSERT INTO "public"."base_city" VALUES ('4453', '云浮市', '44');
INSERT INTO "public"."base_city" VALUES ('4501', '南宁市', '45');
INSERT INTO "public"."base_city" VALUES ('4502', '柳州市', '45');
INSERT INTO "public"."base_city" VALUES ('4503', '桂林市', '45');
INSERT INTO "public"."base_city" VALUES ('4504', '梧州市', '45');
INSERT INTO "public"."base_city" VALUES ('4505', '北海市', '45');
INSERT INTO "public"."base_city" VALUES ('4506', '防城港市', '45');
INSERT INTO "public"."base_city" VALUES ('4507', '钦州市', '45');
INSERT INTO "public"."base_city" VALUES ('4508', '贵港市', '45');
INSERT INTO "public"."base_city" VALUES ('4509', '玉林市', '45');
INSERT INTO "public"."base_city" VALUES ('4510', '百色市', '45');
INSERT INTO "public"."base_city" VALUES ('4511', '贺州市', '45');
INSERT INTO "public"."base_city" VALUES ('4512', '河池市', '45');
INSERT INTO "public"."base_city" VALUES ('4513', '来宾市', '45');
INSERT INTO "public"."base_city" VALUES ('4514', '崇左市', '45');
INSERT INTO "public"."base_city" VALUES ('4601', '海口市', '46');
INSERT INTO "public"."base_city" VALUES ('4602', '三亚市', '46');
INSERT INTO "public"."base_city" VALUES ('4603', '三沙市', '46');
INSERT INTO "public"."base_city" VALUES ('4604', '儋州市', '46');
INSERT INTO "public"."base_city" VALUES ('4690', '省直辖县级行政区划', '46');
INSERT INTO "public"."base_city" VALUES ('5001', '市辖区', '50');
INSERT INTO "public"."base_city" VALUES ('5002', '县', '50');
INSERT INTO "public"."base_city" VALUES ('5101', '成都市', '51');
INSERT INTO "public"."base_city" VALUES ('5103', '自贡市', '51');
INSERT INTO "public"."base_city" VALUES ('5104', '攀枝花市', '51');
INSERT INTO "public"."base_city" VALUES ('5105', '泸州市', '51');
INSERT INTO "public"."base_city" VALUES ('5106', '德阳市', '51');
INSERT INTO "public"."base_city" VALUES ('5107', '绵阳市', '51');
INSERT INTO "public"."base_city" VALUES ('5108', '广元市', '51');
INSERT INTO "public"."base_city" VALUES ('5109', '遂宁市', '51');
INSERT INTO "public"."base_city" VALUES ('5110', '内江市', '51');
INSERT INTO "public"."base_city" VALUES ('5111', '乐山市', '51');
INSERT INTO "public"."base_city" VALUES ('5113', '南充市', '51');
INSERT INTO "public"."base_city" VALUES ('5114', '眉山市', '51');
INSERT INTO "public"."base_city" VALUES ('5115', '宜宾市', '51');
INSERT INTO "public"."base_city" VALUES ('5116', '广安市', '51');
INSERT INTO "public"."base_city" VALUES ('5117', '达州市', '51');
INSERT INTO "public"."base_city" VALUES ('5118', '雅安市', '51');
INSERT INTO "public"."base_city" VALUES ('5119', '巴中市', '51');
INSERT INTO "public"."base_city" VALUES ('5120', '资阳市', '51');
INSERT INTO "public"."base_city" VALUES ('5132', '阿坝藏族羌族自治州', '51');
INSERT INTO "public"."base_city" VALUES ('5133', '甘孜藏族自治州', '51');
INSERT INTO "public"."base_city" VALUES ('5134', '凉山彝族自治州', '51');
INSERT INTO "public"."base_city" VALUES ('5201', '贵阳市', '52');
INSERT INTO "public"."base_city" VALUES ('5202', '六盘水市', '52');
INSERT INTO "public"."base_city" VALUES ('5203', '遵义市', '52');
INSERT INTO "public"."base_city" VALUES ('5204', '安顺市', '52');
INSERT INTO "public"."base_city" VALUES ('5205', '毕节市', '52');
INSERT INTO "public"."base_city" VALUES ('5206', '铜仁市', '52');
INSERT INTO "public"."base_city" VALUES ('5223', '黔西南布依族苗族自治州', '52');
INSERT INTO "public"."base_city" VALUES ('5226', '黔东南苗族侗族自治州', '52');
INSERT INTO "public"."base_city" VALUES ('5227', '黔南布依族苗族自治州', '52');
INSERT INTO "public"."base_city" VALUES ('5301', '昆明市', '53');
INSERT INTO "public"."base_city" VALUES ('5303', '曲靖市', '53');
INSERT INTO "public"."base_city" VALUES ('5304', '玉溪市', '53');
INSERT INTO "public"."base_city" VALUES ('5305', '保山市', '53');
INSERT INTO "public"."base_city" VALUES ('5306', '昭通市', '53');
INSERT INTO "public"."base_city" VALUES ('5307', '丽江市', '53');
INSERT INTO "public"."base_city" VALUES ('5308', '普洱市', '53');
INSERT INTO "public"."base_city" VALUES ('5309', '临沧市', '53');
INSERT INTO "public"."base_city" VALUES ('5323', '楚雄彝族自治州', '53');
INSERT INTO "public"."base_city" VALUES ('5325', '红河哈尼族彝族自治州', '53');
INSERT INTO "public"."base_city" VALUES ('5326', '文山壮族苗族自治州', '53');
INSERT INTO "public"."base_city" VALUES ('5328', '西双版纳傣族自治州', '53');
INSERT INTO "public"."base_city" VALUES ('5329', '大理白族自治州', '53');
INSERT INTO "public"."base_city" VALUES ('5331', '德宏傣族景颇族自治州', '53');
INSERT INTO "public"."base_city" VALUES ('5333', '怒江傈僳族自治州', '53');
INSERT INTO "public"."base_city" VALUES ('5334', '迪庆藏族自治州', '53');
INSERT INTO "public"."base_city" VALUES ('5401', '拉萨市', '54');
INSERT INTO "public"."base_city" VALUES ('5402', '日喀则市', '54');
INSERT INTO "public"."base_city" VALUES ('5403', '昌都市', '54');
INSERT INTO "public"."base_city" VALUES ('5404', '林芝市', '54');
INSERT INTO "public"."base_city" VALUES ('5405', '山南市', '54');
INSERT INTO "public"."base_city" VALUES ('5406', '那曲市', '54');
INSERT INTO "public"."base_city" VALUES ('5425', '阿里地区', '54');
INSERT INTO "public"."base_city" VALUES ('6101', '西安市', '61');
INSERT INTO "public"."base_city" VALUES ('6102', '铜川市', '61');
INSERT INTO "public"."base_city" VALUES ('6103', '宝鸡市', '61');
INSERT INTO "public"."base_city" VALUES ('6104', '咸阳市', '61');
INSERT INTO "public"."base_city" VALUES ('6105', '渭南市', '61');
INSERT INTO "public"."base_city" VALUES ('6106', '延安市', '61');
INSERT INTO "public"."base_city" VALUES ('6107', '汉中市', '61');
INSERT INTO "public"."base_city" VALUES ('6108', '榆林市', '61');
INSERT INTO "public"."base_city" VALUES ('6109', '安康市', '61');
INSERT INTO "public"."base_city" VALUES ('6110', '商洛市', '61');
INSERT INTO "public"."base_city" VALUES ('6201', '兰州市', '62');
INSERT INTO "public"."base_city" VALUES ('6202', '嘉峪关市', '62');
INSERT INTO "public"."base_city" VALUES ('6203', '金昌市', '62');
INSERT INTO "public"."base_city" VALUES ('6204', '白银市', '62');
INSERT INTO "public"."base_city" VALUES ('6205', '天水市', '62');
INSERT INTO "public"."base_city" VALUES ('6206', '武威市', '62');
INSERT INTO "public"."base_city" VALUES ('6207', '张掖市', '62');
INSERT INTO "public"."base_city" VALUES ('6208', '平凉市', '62');
INSERT INTO "public"."base_city" VALUES ('6209', '酒泉市', '62');
INSERT INTO "public"."base_city" VALUES ('6210', '庆阳市', '62');
INSERT INTO "public"."base_city" VALUES ('6211', '定西市', '62');
INSERT INTO "public"."base_city" VALUES ('6212', '陇南市', '62');
INSERT INTO "public"."base_city" VALUES ('6229', '临夏回族自治州', '62');
INSERT INTO "public"."base_city" VALUES ('6230', '甘南藏族自治州', '62');
INSERT INTO "public"."base_city" VALUES ('6301', '西宁市', '63');
INSERT INTO "public"."base_city" VALUES ('6302', '海东市', '63');
INSERT INTO "public"."base_city" VALUES ('6322', '海北藏族自治州', '63');
INSERT INTO "public"."base_city" VALUES ('6323', '黄南藏族自治州', '63');
INSERT INTO "public"."base_city" VALUES ('6325', '海南藏族自治州', '63');
INSERT INTO "public"."base_city" VALUES ('6326', '果洛藏族自治州', '63');
INSERT INTO "public"."base_city" VALUES ('6327', '玉树藏族自治州', '63');
INSERT INTO "public"."base_city" VALUES ('6328', '海西蒙古族藏族自治州', '63');
INSERT INTO "public"."base_city" VALUES ('6401', '银川市', '64');
INSERT INTO "public"."base_city" VALUES ('6402', '石嘴山市', '64');
INSERT INTO "public"."base_city" VALUES ('6403', '吴忠市', '64');
INSERT INTO "public"."base_city" VALUES ('6404', '固原市', '64');
INSERT INTO "public"."base_city" VALUES ('6405', '中卫市', '64');
INSERT INTO "public"."base_city" VALUES ('6501', '乌鲁木齐市', '65');
INSERT INTO "public"."base_city" VALUES ('6502', '克拉玛依市', '65');
INSERT INTO "public"."base_city" VALUES ('6504', '吐鲁番市', '65');
INSERT INTO "public"."base_city" VALUES ('6505', '哈密市', '65');
INSERT INTO "public"."base_city" VALUES ('6523', '昌吉回族自治州', '65');
INSERT INTO "public"."base_city" VALUES ('6527', '博尔塔拉蒙古自治州', '65');
INSERT INTO "public"."base_city" VALUES ('6528', '巴音郭楞蒙古自治州', '65');
INSERT INTO "public"."base_city" VALUES ('6529', '阿克苏地区', '65');
INSERT INTO "public"."base_city" VALUES ('6530', '克孜勒苏柯尔克孜自治州', '65');
INSERT INTO "public"."base_city" VALUES ('6531', '喀什地区', '65');
INSERT INTO "public"."base_city" VALUES ('6532', '和田地区', '65');
INSERT INTO "public"."base_city" VALUES ('6540', '伊犁哈萨克自治州', '65');
INSERT INTO "public"."base_city" VALUES ('6542', '塔城地区', '65');
INSERT INTO "public"."base_city" VALUES ('6543', '阿勒泰地区', '65');
INSERT INTO "public"."base_city" VALUES ('6590', '自治区直辖县级行政区划', '65');

-- ----------------------------
-- Table structure for base_dict
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_dict";
CREATE TABLE "public"."base_dict" (
  "id" int8 NOT NULL,
  "name" varchar(50) COLLATE "pg_catalog"."default",
  "group_tag" varchar(50) COLLATE "pg_catalog"."default",
  "code" varchar(50) COLLATE "pg_catalog"."default",
  "remark" varchar(50) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "enable" bool,
  "deleted" bool NOT NULL
)
;
COMMENT ON COLUMN "public"."base_dict"."id" IS '主键';
COMMENT ON COLUMN "public"."base_dict"."name" IS '名称';
COMMENT ON COLUMN "public"."base_dict"."group_tag" IS '分类标签';
COMMENT ON COLUMN "public"."base_dict"."code" IS '编码';
COMMENT ON COLUMN "public"."base_dict"."remark" IS '备注';
COMMENT ON COLUMN "public"."base_dict"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."base_dict"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."base_dict"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."base_dict"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."base_dict"."version" IS '版本号';
COMMENT ON COLUMN "public"."base_dict"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."base_dict"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."base_dict" IS '字典表';

-- ----------------------------
-- Records of base_dict
-- ----------------------------
INSERT INTO "public"."base_dict" VALUES (1823688893108133888, '支付订单关闭类型', '支付', 'close_type', '', 1811365615815487488, '2024-08-14 19:51:29.275426', 1811365615815487488, '2024-08-14 19:51:29.277016', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823688398549360640, '支付通道', '支付', 'channel', '', 1811365615815487488, '2024-08-14 19:49:31.363417', 1811365615815487488, '2024-08-14 19:56:44.841238', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823696159936946176, '证件类型', '基础信息', 'id_type', '', 1811365615815487488, '2024-08-14 20:20:21.822676', 1811365615815487488, '2024-08-14 20:20:21.824229', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823708720229117952, '签名方式', '支付', 'sign_type', '', 1811365615815487488, '2024-08-14 21:10:16.430774', 1811365615815487488, '2024-08-14 21:10:16.433984', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823928239337771008, '商户状态', '支付', 'merchant_status', '', 1811365615815487488, '2024-08-15 11:42:33.863751', 1811365615815487488, '2024-08-15 11:42:33.865868', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823928536063807488, '商户应用状态', '支付', 'mch_app_status', '', 1811365615815487488, '2024-08-15 11:43:44.608309', 1811365615815487488, '2024-08-15 11:43:44.60988', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823969170598400000, '客户通知内容类型', '支付', 'notify_content_type', '', 1811365615815487488, '2024-08-15 14:25:12.636886', 1811365615815487488, '2024-08-15 15:48:34.51576', 2, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823969966421446656, '商户消息通知类型', '支付', 'merchant_notify_type', '', 1811365615815487488, '2024-08-15 14:28:22.375016', 1811365615815487488, '2024-08-15 15:48:43.031616', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823991280205041664, '业务操作类型', '审计日志', 'log_business_type', '', 0, '2024-08-15 15:53:03.977005', 0, '2024-08-15 15:53:03.978035', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824277928167870464, '支付订单的退款状态', '支付', 'pay_refund_status', '', 1811365615815487488, '2024-08-16 10:52:06.177341', 1811365615815487488, '2024-08-16 10:52:06.179417', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824285303885008896, '支付方式', '支付', 'pay_method', '', 1811365615815487488, '2024-08-16 11:21:24.685429', 1811365615815487488, '2024-08-16 11:21:24.686997', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824050661785407488, '支付订单状态', '支付', 'pay_status', '', 0, '2024-08-15 19:49:01.649012', 1811365615815487488, '2024-08-16 11:24:05.396307', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824277760341184512, '支付订单分账状态', '支付', 'pay_alloc_status', '支付订单的分账状态', 1811365615815487488, '2024-08-16 10:51:26.166066', 1811365615815487488, '2024-08-16 14:42:42.689952', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824287281285435392, '退款状态枚举', '支付', 'refund_status', '', 1811365615815487488, '2024-08-16 11:29:16.134278', 1811365615815487488, '2024-08-16 15:03:53.495862', 7, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824342698652971008, '交易流水记录类型', '支付', 'trade_flow_type', '', 1811365615815487488, '2024-08-16 15:09:28.666504', 1811365615815487488, '2024-08-16 15:09:34.017606', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824791500966486016, '交易类型', '支付', 'trade_type', '', 1811365615815487488, '2024-08-17 20:52:51.47629', 1811365615815487488, '2024-08-17 20:52:51.532979', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1825134996013670400, '转账状态', '支付', 'transfer_status', '', 1811365615815487488, '2024-08-18 19:37:47.076212', 1811365615815487488, '2024-08-18 19:37:47.137228', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1825135068277334016, '转账接收方类型', '支付', 'transfer_payee_type', '', 1811365615815487488, '2024-08-18 19:38:04.304349', 1811365615815487488, '2024-08-18 19:38:04.306948', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1825174702763966464, '支付回调处理状态', '支付', 'callback_status', '', 1811365615815487488, '2024-08-18 22:15:33.902812', 1811365615815487488, '2024-08-18 22:15:33.90384', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1825408039604842496, '消息发送类型', '支付', 'notice_send_type', '', 1811365615815487488, '2024-08-19 13:42:45.740594', 1811365615815487488, '2024-08-19 13:42:45.742689', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1826059944274382848, '对账差异类型', '支付', 'reconcile_discrepancy_type', '', 1811365615815487488, '2024-08-21 08:53:11.927052', 1811365615815487488, '2024-08-21 08:53:11.931319', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1826061072412135424, '对账结果', '支付', 'reconcile_result', '', 1811365615815487488, '2024-08-21 08:57:40.895007', 1811365615815487488, '2024-08-21 08:57:40.897049', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1826143914542350336, '交易状态', '支付', 'trade_status', '', 1811365615815487488, '2024-08-21 14:26:52.000272', 1811365615815487488, '2024-08-21 14:26:52.005675', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1843536439208931328, '分账接收方类型', '支付', 'alloc_receiver_type', '', 1811365615815487488, '2024-10-08 14:18:33.016439', 1811365615815487488, '2024-10-08 14:18:33.017572', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1866464725509451776, '分账订单处理结果', '支付', 'allocation_result', '', 1811365615815487488, '2024-12-10 20:47:22.458399', 1811365615815487488, '2024-12-10 20:47:22.512079', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1866861372363571200, '分账明细处理结果', '支付', 'alloc_detail_result', '', 1811365615815487488, '2024-12-11 23:03:30.437127', 1811365615815487488, '2024-12-11 23:03:30.629127', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1866464929205825536, '分账状态', '支付', 'allocation_status', '', 1811365615815487488, '2024-12-10 20:48:11.021238', 1811365615815487488, '2024-12-10 20:48:11.071674', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1875419885631762432, '商户类型', '支付', 'merchant_type', '', 1811365615815487488, '2025-01-04 13:51:59.008711', 1811365615815487488, '2025-01-04 13:51:59.008711', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1889586857340620800, '用户状态', '基础信息', 'user_status', '', 1811365615815487488, '2025-02-12 16:06:28.275604', 1811365615815487488, '2025-02-12 16:06:28.275604', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1889977324553109504, '乐刷分账能力开通状态', '支付通道', 'ls_alloc_apply_status', '', 1811365615815487488, '2025-02-13 17:58:02.913465', 1811365615815487488, '2025-02-13 17:58:02.914463', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1891378565338431488, '分账关系类型', '支付', 'alloc_relation_type', '', 1811365615815487488, '2025-02-17 14:46:04.748953', 1811365615815487488, '2025-02-17 14:46:04.748953', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1898988643046494208, '收款终端设备类型', '支付', 'terminal_device_type', '', 1811365615815487488, '2025-03-10 14:45:48.663842', 1811365615815487488, '2025-03-10 14:45:48.664841', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1899630183427186688, '汇付企业分账接收方状态', '支付', 'ada_pay_corp_status', '', 1811365615815487488, '2025-03-12 09:15:03.810762', 1811365615815487488, '2025-03-12 09:15:03.811761', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1899771077857976320, '终端报送类型', '支付', 'terminal_type', '', 1811365615815487488, '2025-03-12 18:34:55.659749', 1811365615815487488, '2025-03-12 18:34:55.660754', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1899038420995997696, '终端设备通道报备状态', '支付', 'channel_terminal_status', '', 1811365615815487488, '2025-03-10 18:03:36.650965', 1811365615815487488, '2025-03-12 22:41:08.934094', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1894013347259527168, '随行付分账能力开通状态枚举', '支付通道', 'vbill_alloc_apply_status', '', 1811365615815487488, '2025-02-24 21:15:45.713406', 1811365615815487488, '2025-03-19 17:07:18.083376', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1894015467740246016, '随行付分账能力开通类型枚举', '支付通道', 'vbill_alloc_sign_type', '', 1811365615815487488, '2025-02-24 21:24:11.272869', 1811365615815487488, '2025-03-19 17:07:25.297913', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1902286175247896576, '网关支付类型', '支付', 'gateway_pay_type', '', 1811365615815487488, '2025-03-19 17:09:01.604929', 1811365615815487488, '2025-03-19 17:09:01.604929', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1905238363176165376, '网关收银台类型', '支付', 'gateway_cashier_type', '', 1811365615815487488, '2025-03-27 20:39:58.057782', 1811365615815487488, '2025-03-27 20:39:58.058781', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1901976184890114048, '汇付结算账户状态', '支付通道', 'adapay_settle_account_status', '', 1811365615815487488, '2025-03-18 20:37:14.145553', 1811365615815487488, '2025-03-24 16:49:38.559604', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1902285549071863808, '聚合支付类型', '支付', 'aggregate_pay_type', '', 1811365615815487488, '2025-03-19 17:06:32.314075', 1811365615815487488, '2025-03-24 17:20:41.459492', 2, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1904092914741948416, '付款码支付类型', '支付', 'aggregate_bar_pay_type', '', 1811365615815487488, '2025-03-24 16:48:21.87', 1811365615815487488, '2025-04-10 15:07:38.127186', 2, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1889681841244356608, '服务商状态', '支付', 'isv_status', '', 1811365615815487488, '2025-02-12 22:23:54.201303', 1811365615815487488, '2025-02-12 22:23:54.202303', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1909136633497235456, '通道认证类型', '支付通道', 'channel_auth_type', '', 1811365615815487488, '2025-04-07 14:50:18.126778', 1811365615815487488, '2025-04-07 14:50:18.126778', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1930152051594498048, '汇付支付方式', '支付通道', 'ada_pay_method', '', 1811365615815487488, '2025-06-04 14:38:04.250594', 1811365615815487488, '2025-06-04 14:38:04.251587', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1930168171781165056, '随行付支付方式', '支付通道', 'vbill_pay_method', '', 1811365615815487488, '2025-06-04 15:42:07.600554', 1811365615815487488, '2025-06-04 15:42:07.600554', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1840650057641713664, '支付场景类型', '支付', 'cashier_scene', '', 1811365615815487488, '2024-09-30 15:09:06.025286', 1811365615815487488, '2025-07-03 14:30:23.444221', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1942417671570812928, '音响设备绑定类型', '支付设备', 'audio_bind_type', '', 1811365615815487488, '2025-07-08 10:57:15.935522', 1811365615815487488, '2025-07-08 10:57:23.58743', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1943940109484339200, '提现方式', '支付', 'agent_cashouts_type', '', 1811365615815487488, '2025-07-12 15:46:53.418485', 1811365615815487488, '2025-07-12 15:46:53.418519', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1943941556863152128, '代理商提现手续费计算类型', '支付', 'agent_cashouts_fee_formula', '', 1811365615815487488, '2025-07-12 15:52:38.501134', 1811365615815487488, '2025-07-12 15:52:38.501229', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1944657858338680832, '代理商提现申请状态', '支付', 'agent_cashouts_status', '', 1811365615815487488, '2025-07-14 15:18:58.082588', 1811365615815487488, '2025-07-14 15:18:58.083589', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1947829752568721408, '结算状态', '支付', 'settle_status', '', 1811365615815487488, '2025-07-23 09:22:56.596316', 1811365615815487488, '2025-07-23 09:22:56.5975', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1926978062638395392, '进件商户商户类型', '支付', 'onb_apply_mch_type', '', 1811365615815487488, '2025-05-26 20:25:46.314188', 1811365615815487488, '2025-07-31 22:07:05.299733', 5, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1951173999276052480, '进件商户来源', '支付', 'onb_mch_source', '', 1811365615815487488, '2025-08-01 14:51:47.147707', 1811365615815487488, '2025-08-01 14:51:47.148083', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1856269612988506112, '进件申请状态', '支付', 'onb_apply_status', '', 1811365615815487488, '2024-11-12 17:35:38.217929', 1811365615815487488, '2025-10-16 21:06:34.492519', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1856210887078592512, '服务商进件类型', '支付', 'onb_apply_type', '', 1811365615815487488, '2024-11-12 13:42:16.868901', 1811365615815487488, '2025-10-16 21:14:06.664386', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1977603752893566976, '短信厂商', '基础信息', 'sms_provider', '', 1811365615815487488, '2025-10-13 13:14:11.45038', 1811365615815487488, '2025-10-13 13:14:24.345199', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1902285697764134912, '支付调起方式', '支付', 'pay_call_type', '', 1811365615815487488, '2025-03-19 17:07:07.763773', 1811365615815487488, '2025-12-10 18:35:31.407805', 3, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1996158235021885440, '支付厂商', '支付', 'payment_vendor', '', 1811365615815487488, '2025-12-03 18:03:04.713544', 1811365615815487488, '2025-12-03 18:03:04.714549', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1947837590728847360, '钱包流水类型', '支付', 'wallet_flow_type', '', 1811365615815487488, '2025-07-23 09:54:05.359041', 1811365615815487488, '2026-01-16 12:17:06.219707', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1994679881710104576, '分账绑定状态', '支付', 'alloc_receiver_bind_status', '', 1811365615815487488, '2025-11-29 16:08:37.818439', 1811365615815487488, '2026-01-16 12:16:56.588122', 1, 't', 'f');

-- ----------------------------
-- Table structure for base_province
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_province";
CREATE TABLE "public"."base_province" (
  "code" varchar(2) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."base_province"."code" IS '省份编码';
COMMENT ON COLUMN "public"."base_province"."name" IS '省份名称';
COMMENT ON TABLE "public"."base_province" IS '省份表';

-- ----------------------------
-- Records of base_province
-- ----------------------------
INSERT INTO "public"."base_province" VALUES ('11', '北京市');
INSERT INTO "public"."base_province" VALUES ('12', '天津市');
INSERT INTO "public"."base_province" VALUES ('13', '河北省');
INSERT INTO "public"."base_province" VALUES ('14', '山西省');
INSERT INTO "public"."base_province" VALUES ('15', '内蒙古自治区');
INSERT INTO "public"."base_province" VALUES ('21', '辽宁省');
INSERT INTO "public"."base_province" VALUES ('22', '吉林省');
INSERT INTO "public"."base_province" VALUES ('23', '黑龙江省');
INSERT INTO "public"."base_province" VALUES ('31', '上海市');
INSERT INTO "public"."base_province" VALUES ('32', '江苏省');
INSERT INTO "public"."base_province" VALUES ('33', '浙江省');
INSERT INTO "public"."base_province" VALUES ('34', '安徽省');
INSERT INTO "public"."base_province" VALUES ('35', '福建省');
INSERT INTO "public"."base_province" VALUES ('36', '江西省');
INSERT INTO "public"."base_province" VALUES ('37', '山东省');
INSERT INTO "public"."base_province" VALUES ('41', '河南省');
INSERT INTO "public"."base_province" VALUES ('42', '湖北省');
INSERT INTO "public"."base_province" VALUES ('43', '湖南省');
INSERT INTO "public"."base_province" VALUES ('44', '广东省');
INSERT INTO "public"."base_province" VALUES ('45', '广西壮族自治区');
INSERT INTO "public"."base_province" VALUES ('46', '海南省');
INSERT INTO "public"."base_province" VALUES ('50', '重庆市');
INSERT INTO "public"."base_province" VALUES ('51', '四川省');
INSERT INTO "public"."base_province" VALUES ('52', '贵州省');
INSERT INTO "public"."base_province" VALUES ('53', '云南省');
INSERT INTO "public"."base_province" VALUES ('54', '西藏自治区');
INSERT INTO "public"."base_province" VALUES ('61', '陕西省');
INSERT INTO "public"."base_province" VALUES ('62', '甘肃省');
INSERT INTO "public"."base_province" VALUES ('63', '青海省');
INSERT INTO "public"."base_province" VALUES ('64', '宁夏回族自治区');
INSERT INTO "public"."base_province" VALUES ('65', '新疆维吾尔自治区');

-- ----------------------------
-- Table structure for base_street
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_street";
CREATE TABLE "public"."base_street" (
  "code" varchar(9) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(60) COLLATE "pg_catalog"."default" NOT NULL,
  "area_code" varchar(6) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."base_street"."code" IS '编码';
COMMENT ON COLUMN "public"."base_street"."name" IS '街道名称';
COMMENT ON COLUMN "public"."base_street"."area_code" IS '县区编码';
COMMENT ON TABLE "public"."base_street" IS '街道表';

-- ----------------------------
-- Records of base_street
-- ----------------------------

-- ----------------------------
-- Table structure for iam_perm_code
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_perm_code";
CREATE TABLE "public"."iam_perm_code" (
  "id" int8 NOT NULL,
  "code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "name_cn" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "name_en" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_code" varchar(100) COLLATE "pg_catalog"."default",
  "internal" bool DEFAULT false,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."iam_perm_code"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_perm_code"."code" IS '权限码编码';
COMMENT ON COLUMN "public"."iam_perm_code"."name_cn" IS '中文名称';
COMMENT ON COLUMN "public"."iam_perm_code"."name_en" IS '英文名称';
COMMENT ON COLUMN "public"."iam_perm_code"."menu_code" IS '菜单编码';
COMMENT ON COLUMN "public"."iam_perm_code"."internal" IS '是否系统内置';
COMMENT ON COLUMN "public"."iam_perm_code"."remark" IS '备注';
COMMENT ON COLUMN "public"."iam_perm_code"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_perm_code"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_perm_code"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_perm_code"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_perm_code"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_perm_code"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."iam_perm_code" IS '权限码';

-- ----------------------------
-- Records of iam_perm_code
-- ----------------------------
INSERT INTO "public"."iam_perm_code" VALUES (2038784667368542208, 'iam:perm:menu:manage', '菜单管理', 'Menu Manage', 'iam:perm:menu', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.147865', 1, '2026-03-31 09:05:18.216159', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038784667741835264, 'iam:perm:menu:view', '菜单查看', 'Menu View', 'iam:perm:menu', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.234051', 1, '2026-03-31 09:05:18.234051', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038784667741835265, 'iam:role:manage', '角色管理', 'Role Manage', 'iam:role', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.235563', 1, '2026-03-31 09:05:18.235563', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038784667750223872, 'iam:role:view', '角色查看', 'Role View', 'iam:role', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.23611', 1, '2026-03-31 09:05:18.23611', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038784667750223873, 'starter:log:login:manage', '登录日志管理', 'Login Log Manage', 'starter:log:login', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.23662', 1, '2026-03-31 09:05:18.23662', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038784667754418176, 'starter:log:login:view', '登录日志查看', 'Login Log View', 'starter:log:login', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.237131', 1, '2026-03-31 09:05:18.237131', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038784667754418177, 'starter:log:operate:manage', '操作日志管理', 'Operate Log Manage', 'starter:log:operate', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.237745', 1, '2026-03-31 09:05:18.237745', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038784667754418178, 'starter:log:operate:view', '操作日志查看', 'Operate Log View', 'starter:log:operate', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.237745', 1, '2026-03-31 09:05:18.237745', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038784667762806784, 'system:dict:dict:manage', '字典管理', 'Dict Manage', 'system:dict', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.23928', 1, '2026-03-31 09:05:18.23928', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038784667762806785, 'system:dict:dict:view', '字典查看', 'Dict View', 'system:dict', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.23928', 1, '2026-03-31 09:05:18.23928', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038784667767001088, 'system:dict:item:manage', '字典项管理', 'Dict Item Manage', 'system:dict', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.2408', 1, '2026-03-31 09:05:18.2408', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038784667767001089, 'system:dict:item:view', '字典项查看', 'Dict Item View', 'system:dict', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 09:05:18.2408', 1, '2026-03-31 09:05:18.2408', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038990840427347968, 'iam:user:manager:add', '用户新增', 'User Add', 'iam:user:manager', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 22:44:33.635707', 1, '2026-03-31 22:44:33.726596', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038990840934858752, 'iam:user:manager:edit', '用户编辑', 'User Edit', 'iam:user:manager', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 22:44:33.753261', 1, '2026-03-31 22:44:33.753261', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038990840934858753, 'iam:user:manager:resetPassword', '重置密码', 'Reset Password', 'iam:user:manager', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 22:44:33.753261', 1, '2026-03-31 22:44:33.753261', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038990840939053056, 'iam:user:manager:status', '用户状态管理', 'User Status Manage', 'iam:user:manager', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 22:44:33.754788', 1, '2026-03-31 22:44:33.754788', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038990840939053057, 'iam:user:manager:view', '用户查看', 'User View', 'iam:user:manager', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 22:44:33.754788', 1, '2026-03-31 22:44:33.754788', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038973370039693312, 'iam:user:add', '用户新增', 'User Add', 'iam:user', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 21:35:08.370501', 1, '2026-03-31 22:44:33.796637', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2038973370148745216, 'iam:user:edit', '用户编辑', 'User Edit', 'iam:user', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 21:35:08.393941', 1, '2026-03-31 22:44:33.807621', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2038973370148745217, 'iam:user:resetPassword', '重置密码', 'Reset Password', 'iam:user', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 21:35:08.393941', 1, '2026-03-31 22:44:33.808124', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2038973370152939520, 'iam:user:status', '用户状态管理', 'User Status Manage', 'iam:user', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 21:35:08.394945', 1, '2026-03-31 22:44:33.809129', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2039563690460745728, 'iam:user:manager:assignRole', '分配角色', 'Assign Role', 'iam:user:manager', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-02 12:40:51.723419', 1, '2026-04-02 12:40:51.729061', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2038973370144550912, 'iam:user:assignRole', '分配角色', 'Assign Role', 'iam:user', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 21:35:08.392432', 1, '2026-04-02 12:40:51.765811', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2038973370157133824, 'iam:user:view', '用户查看', 'User View', 'iam:user', 't', '由 @PermCode 扫描同步生成', 1, '2026-03-31 21:35:08.395451', 1, '2026-04-02 12:40:51.77459', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2041511649003667456, 'system:security:config:security:manage', '安全配置管理', 'Security Manage', 'system:security:config', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-07 21:41:21.233008', 1, '2026-04-07 21:41:21.238724', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2041511649108525056, 'system:security:config:security:view', '安全配置查看', 'Security View', 'system:security:config', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-07 21:41:21.256196', 1, '2026-04-07 21:41:21.256196', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2042852672892219393, 'system:file:platform:view', '文件查看', 'File View', 'system:file:platform', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-11 14:30:06.24119', 1, '2026-04-11 14:30:06.24119', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2042852672393097216, 'system:file:platform:add', '文件新增', 'File Add', 'system:file:platform', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-11 14:30:06.125997', 1, '2026-04-12 21:17:39.378296', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2042852672888025088, 'system:file:platform:delete', '文件删除', 'File Delete', 'system:file:platform', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-11 14:30:06.240183', 1, '2026-04-12 21:17:39.380799', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2042852672892219392, 'system:file:platform:edit', '文件编辑', 'File Edit', 'system:file:platform', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-11 14:30:06.24119', 1, '2026-04-12 21:17:39.381804', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2043636490955845632, 'iam:online:user:kickout', '强制下线', 'Kickout', 'iam:online:user', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-13 18:24:43.033351', 1, '2026-04-13 18:24:43.048432', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2043636491127812096, 'iam:online:user:view', '在线用户查看', 'Online User View', 'iam:online:user', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-13 18:24:43.071177', 1, '2026-04-13 18:24:43.071177', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2043317624413253632, 'kickout', '强制下线', 'Kickout', '', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-12 21:17:39.326006', 1, '2026-04-13 18:24:43.10157', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2043317624505528320, 'view', '在线用户查看', 'Online User View', '', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-12 21:17:39.345066', 1, '2026-04-13 18:24:43.108181', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2043957827775696896, 'payment:merchant:edit', '商户编辑', 'Merchant Edit', 'payment:merchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 15:41:35.702149', 1, '2026-04-14 15:41:35.754281', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2043970654305435648, 'payment:merchant:add', '商户新增', 'Merchant Add', 'payment:merchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 16:32:33.783962', 1, '2026-04-14 16:32:33.788495', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2043970654385127424, 'payment:merchant:delete', '商户删除', 'Merchant Delete', 'payment:merchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 16:32:33.800236', 1, '2026-04-14 16:32:33.800236', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2043970654389321728, 'payment:merchant:view', '商户查看', 'Merchant View', 'payment:merchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 16:32:33.801236', 1, '2026-04-14 16:32:33.801236', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2044069839163809792, 'payment:channel:merchant:edit', '通道商户编辑', 'Channel Merchant Edit', 'payment:channel:merchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 23:06:41.297287', 1, '2026-04-14 23:06:41.301702', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2044069839272861696, 'payment:channel:merchant:view', '通道商户查看', 'Channel Merchant View', 'payment:channel:merchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 23:06:41.320845', 1, '2026-04-14 23:06:41.320845', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2047628312069742592, 'payment:product:view', '产品查看', 'Product View', 'payment:product', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-24 18:46:47.351239', 1, '2026-04-24 18:46:47.355289', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2053674960713592832, 'payment:merchant:channelMerchant:edit', '商户通道商户编辑', 'Merchant Channel Merchant Edit', 'payment:merchant:channelMerchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:14:00.726525', 1, '2026-05-11 11:14:00.726525', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2053674960717787136, 'payment:merchant:channelMerchant:view', '商户通道商户查看', 'Merchant Channel Merchant View', 'payment:merchant:channelMerchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:14:00.727075', 1, '2026-05-11 11:14:00.727075', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2053674960717787137, 'payment:merchant:credential_config_update', '对接配置更新', 'Credential Config Update', 'payment:merchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:14:00.727075', 1, '2026-05-11 11:14:00.727075', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2053674960721981440, 'payment:merchant:product_config_update', '产品配置更新', 'Product Config Update', 'payment:merchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:14:00.728081', 1, '2026-05-11 11:14:00.728081', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2044069839272861697, 'payment:merchant:channelMch:add', '商户通道商户新增', 'Merchant Channel Merchant Add', 'payment:merchant:channelMch', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 23:06:41.321844', 1, '2026-05-11 11:14:00.770981', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2044069839277056000, 'payment:merchant:channelMch:delete', '商户通道商户删除', 'Merchant Channel Merchant Delete', 'payment:merchant:channelMch', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 23:06:41.321844', 1, '2026-05-11 11:14:00.778013', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2044069839277056001, 'payment:merchant:channelMch:edit', '商户通道商户编辑', 'Merchant Channel Merchant Edit', 'payment:merchant:channelMch', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 23:06:41.321844', 1, '2026-05-11 11:14:00.779021', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2044069839277056002, 'payment:merchant:channelMch:view', '商户通道商户查看', 'Merchant Channel Merchant View', 'payment:merchant:channelMch', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 23:06:41.323347', 1, '2026-05-11 11:14:00.780021', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2053684256197308416, 'risk:model:add', '风控新增', 'Risk Add', 'risk:model', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:50:56.945594', 1, '2026-05-24 20:31:47.22471', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2053684256348303360, 'risk:model:delete', '风控删除', 'Risk Delete', 'risk:model', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:50:56.978073', 1, '2026-05-24 20:31:47.239364', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2053684256356691968, 'risk:model:disable', '风控禁用', 'Risk Disable', 'risk:model', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:50:56.980096', 1, '2026-05-24 20:31:47.240365', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2053684256356691969, 'risk:model:edit', '风控编辑', 'Risk Edit', 'risk:model', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:50:56.980096', 1, '2026-05-24 20:31:47.240365', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2053684256365080576, 'risk:model:effect', '风控生效', 'Risk Effect', 'risk:model', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:50:56.982275', 1, '2026-05-24 20:31:47.240365', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2053684256365080577, 'risk:model:view', '风控查看', 'Risk View', 'risk:model', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:50:56.982275', 1, '2026-05-24 20:31:47.240365', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2053674960709398528, 'payment:merchant:channelMerchant:delete', '商户通道商户删除', 'Merchant Channel Merchant Delete', 'payment:merchant:channelMerchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:14:00.725518', 1, '2026-06-02 14:37:57.250624', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058847929878401024, 'payment:merchant:app:payRoute:edit', '通道路由编辑', 'Pay Route Edit', 'payment:merchant:app:payRoute', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:49:32.687076', 1, '2026-05-25 17:49:32.691155', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2058847930004230144, 'payment:merchant:app:payRoute:view', '通道路由查看', 'Pay Route View', 'payment:merchant:app:payRoute', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:49:32.714899', 1, '2026-05-25 17:49:32.714899', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2061698817202528256, 'payment:capability:view', '支付能力查看', 'Pay Capability View', 'payment:capability', 't', '由 @PermCode 扫描同步生成', 1, '2026-06-02 14:37:57.192615', 1, '2026-06-02 14:37:57.196832', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2061698817328357376, 'payment:platform:channel:view', '支付通道查看', 'Pay Channel View', 'payment:platform:channel', 't', '由 @PermCode 扫描同步生成', 1, '2026-06-02 14:37:57.219858', 1, '2026-06-02 14:37:57.219858', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2061698817332551680, 'payment:platform:provider:view', '品牌目录查看', 'Brand Method Directory View', 'payment:platform:provider', 't', '由 @PermCode 扫描同步生成', 1, '2026-06-02 14:37:57.220859', 1, '2026-06-02 14:37:57.220859', 0, 'f');
INSERT INTO "public"."iam_perm_code" VALUES (2043889460729552896, 'payment:agent:add', '代理商新增', 'Agent Add', 'payment:agent', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 11:09:55.727999', 1, '2026-06-13 08:28:42.097472', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2043889461165760512, 'payment:agent:edit', '代理商编辑', 'Agent Edit', 'payment:agent', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 11:09:55.828023', 1, '2026-06-13 08:28:42.120531', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2043889461169954816, 'payment:agent:view', '代理商查看', 'Agent View', 'payment:agent', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-14 11:09:55.829023', 1, '2026-06-13 08:28:42.12153', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2061735391835377664, 'payment:alipay:isv:add', '支付宝服务商新增', 'Alipay ISV Add', 'payment:alipay:isv', 't', '由 @PermCode 扫描同步生成', 1, '2026-06-02 17:03:17.264745', 1, '2026-06-13 08:28:42.122531', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2061735392422580224, 'payment:alipay:isv:edit', '支付宝服务商编辑', 'Alipay ISV Edit', 'payment:alipay:isv', 't', '由 @PermCode 扫描同步生成', 1, '2026-06-02 17:03:17.401016', 1, '2026-06-13 08:28:42.122531', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2061735392426774528, 'payment:alipay:isv:view', '支付宝服务商查看', 'Alipay ISV View', 'payment:alipay:isv', 't', '由 @PermCode 扫描同步生成', 1, '2026-06-02 17:03:17.402015', 1, '2026-06-13 08:28:42.122531', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2042169995620990976, 'payment:isv:add', '服务商新增', 'ISV Add', 'payment:isv', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-09 17:17:23.298936', 1, '2026-06-13 08:28:42.123531', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2053674960134778880, 'payment:isv:credential_config_update', '对接配置更新', 'Credential Config Update', 'payment:isv', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:14:00.591315', 1, '2026-06-13 08:28:42.123531', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2042169995730042880, 'payment:isv:delete', '服务商删除', 'ISV Delete', 'payment:isv', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-09 17:17:23.322919', 1, '2026-06-13 08:28:42.123531', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2042169995734237184, 'payment:isv:edit', '服务商编辑', 'ISV Edit', 'payment:isv', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-09 17:17:23.323926', 1, '2026-06-13 08:28:42.125035', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2042169995738431488, 'payment:isv:view', '服务商查看', 'ISV View', 'payment:isv', 't', '由 @PermCode 扫描同步生成', 1, '2026-04-09 17:17:23.324429', 1, '2026-06-13 08:28:42.125035', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2053674960705204224, 'payment:merchant:channelMerchant:add', '商户通道商户新增', 'Merchant Channel Merchant Add', 'payment:merchant:channelMerchant', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-11 11:14:00.724511', 1, '2026-06-13 08:28:42.126042', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2063120580130033664, 'payment:wechat:isv:add', '微信服务商新增', 'WeChat ISV Add', 'payment:wechat:isv', 't', '由 @PermCode 扫描同步生成', 1, '2026-06-06 12:47:31.890138', 1, '2026-06-13 08:28:42.127042', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2063120580222308352, 'payment:wechat:isv:edit', '微信服务商编辑', 'WeChat ISV Edit', 'payment:wechat:isv', 't', '由 @PermCode 扫描同步生成', 1, '2026-06-06 12:47:31.90849', 1, '2026-06-13 08:28:42.127042', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2063120580222308353, 'payment:wechat:isv:view', '微信服务商查看', 'WeChat ISV View', 'payment:wechat:isv', 't', '由 @PermCode 扫描同步生成', 1, '2026-06-06 12:47:31.90849', 1, '2026-06-13 08:28:42.127042', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630502879232, 'risk:dataSourceDef:add', '数据源定义新增', 'Data Source Def Add', 'risk:dataSourceDef', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.637768', 1, '2026-06-13 08:28:42.127042', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630666457088, 'risk:dataSourceDef:delete', '数据源定义删除', 'Data Source Def Delete', 'risk:dataSourceDef', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.672772', 1, '2026-06-13 08:28:42.128547', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630666457089, 'risk:dataSourceDef:edit', '数据源定义编辑', 'Data Source Def Edit', 'risk:dataSourceDef', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.672772', 1, '2026-06-13 08:28:42.128547', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630670651392, 'risk:dataSourceDef:manage', '数据源定义同步', 'Data Source Def Sync', 'risk:dataSourceDef', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.673772', 1, '2026-06-13 08:28:42.129553', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630670651393, 'risk:dataSourceDef:view', '数据源定义查看', 'Data Source Def View', 'risk:dataSourceDef', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.673772', 1, '2026-06-13 08:28:42.129553', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630674845696, 'risk:inputParamSet:add', '参数集新增', 'Input Param Set Add', 'risk:inputParamSet', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.674772', 1, '2026-06-13 08:28:42.130552', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630679040000, 'risk:inputParamSet:delete', '参数集删除', 'Input Param Set Delete', 'risk:inputParamSet', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.675276', 1, '2026-06-13 08:28:42.130552', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630679040001, 'risk:inputParamSet:edit', '参数集编辑', 'Input Param Set Edit', 'risk:inputParamSet', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.675276', 1, '2026-06-13 08:28:42.131554', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630683234304, 'risk:inputParamSet:view', '参数集查看', 'Input Param Set View', 'risk:inputParamSet', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.676279', 1, '2026-06-13 08:28:42.131554', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630683234305, 'risk:model:add', '模型新增', 'Model Add', 'risk:model', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.676279', 1, '2026-06-13 08:28:42.131554', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630687428608, 'risk:model:delete', '模型删除', 'Model Delete', 'risk:model', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.677278', 1, '2026-06-13 08:28:42.132553', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630687428609, 'risk:model:edit', '模型编辑', 'Model Edit', 'risk:model', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.677278', 1, '2026-06-13 08:28:42.132553', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058528386563891200, 'risk:model:instance:add', '新增实例', 'Add Instance', 'risk:model:instance', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-24 20:39:47.625347', 1, '2026-06-13 08:28:42.133553', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058528386681331712, 'risk:model:instance:delete', '删除实例', 'Delete Instance', 'risk:model:instance', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-24 20:39:47.649403', 1, '2026-06-13 08:28:42.133553', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058528386681331713, 'risk:model:instance:edit', '修改实例', 'Edit Instance', 'risk:model:instance', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-24 20:39:47.649403', 1, '2026-06-13 08:28:42.133553', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058528386685526016, 'risk:model:instance:view', '实例查看', 'Instance View', 'risk:model:instance', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-24 20:39:47.650405', 1, '2026-06-13 08:28:42.135058', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630691622912, 'risk:model:manage:add', '模型配置新增', 'Model Config Add', 'risk:model:manage', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.67828', 1, '2026-06-13 08:28:42.135058', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630691622913, 'risk:model:manage:delete', '模型配置删除', 'Model Config Delete', 'risk:model:manage', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.67828', 1, '2026-06-13 08:28:42.136067', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630691622914, 'risk:model:manage:edit', '模型配置编辑', 'Model Config Edit', 'risk:model:manage', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.679279', 1, '2026-06-13 08:28:42.136067', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630695817216, 'risk:model:manage:strategyNode:add', '策略节点新增', 'Strategy Node Add', 'risk:model:manage:strategyNode', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.679279', 1, '2026-06-13 08:28:42.137066', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630700011520, 'risk:model:manage:strategyNode:delete', '策略节点删除', 'Strategy Node Delete', 'risk:model:manage:strategyNode', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.68028', 1, '2026-06-13 08:28:42.137066', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630700011521, 'risk:model:manage:strategyNode:edit', '策略节点编辑', 'Strategy Node Edit', 'risk:model:manage:strategyNode', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.68028', 1, '2026-06-13 08:28:42.138065', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630704205824, 'risk:model:manage:strategyNode:view', '策略节点查看', 'Strategy Node View', 'risk:model:manage:strategyNode', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.681283', 1, '2026-06-13 08:28:42.138065', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630704205825, 'risk:model:manage:strategyRule:add', '规则配置新增', 'Rule Config Add', 'risk:model:manage:strategyRule', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.681283', 1, '2026-06-13 08:28:42.139066', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630704205826, 'risk:model:manage:strategyRule:delete', '规则配置删除', 'Rule Config Delete', 'risk:model:manage:strategyRule', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.681283', 1, '2026-06-13 08:28:42.139572', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630708400128, 'risk:model:manage:strategyRule:edit', '规则配置编辑', 'Rule Config Edit', 'risk:model:manage:strategyRule', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.682278', 1, '2026-06-13 08:28:42.140579', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630708400129, 'risk:model:manage:strategyRule:view', '规则配置查看', 'Rule Config View', 'risk:model:manage:strategyRule', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.682788', 1, '2026-06-13 08:28:42.14158', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630708400130, 'risk:model:manage:view', '模型配置查看', 'Model Config View', 'risk:model:manage', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.682788', 1, '2026-06-13 08:28:42.14258', 0, 't');
INSERT INTO "public"."iam_perm_code" VALUES (2058843630708400131, 'risk:model:view', '模型查看', 'Model View', 'risk:model', 't', '由 @PermCode 扫描同步生成', 1, '2026-05-25 17:32:27.682788', 1, '2026-06-13 08:28:42.14258', 0, 't');

-- ----------------------------
-- Table structure for iam_perm_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_perm_menu";
CREATE TABLE "public"."iam_perm_menu" (
  "id" int8 NOT NULL,
  "pid" int8,
  "menu_code" varchar(100) COLLATE "pg_catalog"."default",
  "client_code" varchar(100) COLLATE "pg_catalog"."default",
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "title_cn" varchar(200) COLLATE "pg_catalog"."default",
  "title_en" varchar(200) COLLATE "pg_catalog"."default",
  "i18n_key" varchar(200) COLLATE "pg_catalog"."default",
  "icon" varchar(200) COLLATE "pg_catalog"."default",
  "hidden" bool DEFAULT false,
  "hide_children_menu" bool DEFAULT false,
  "component" varchar(500) COLLATE "pg_catalog"."default",
  "path" varchar(500) COLLATE "pg_catalog"."default",
  "redirect" varchar(500) COLLATE "pg_catalog"."default",
  "sort_no" float8,
  "root" bool DEFAULT false,
  "keep_alive" bool DEFAULT false,
  "affix_tab" bool DEFAULT false,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "menu_type" varchar(20) COLLATE "pg_catalog"."default",
  "active_icon" varchar(100) COLLATE "pg_catalog"."default",
  "badge" varchar(50) COLLATE "pg_catalog"."default",
  "badge_type" varchar(20) COLLATE "pg_catalog"."default",
  "badge_variants" varchar(50) COLLATE "pg_catalog"."default",
  "iframe_src" varchar(500) COLLATE "pg_catalog"."default",
  "link" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."iam_perm_menu"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_perm_menu"."pid" IS '父菜单ID,0表示根菜单';
COMMENT ON COLUMN "public"."iam_perm_menu"."menu_code" IS '菜单编码';
COMMENT ON COLUMN "public"."iam_perm_menu"."client_code" IS '关联终端code';
COMMENT ON COLUMN "public"."iam_perm_menu"."name" IS '路由名称，建议唯一';
COMMENT ON COLUMN "public"."iam_perm_menu"."title_cn" IS '菜单标题-中文';
COMMENT ON COLUMN "public"."iam_perm_menu"."title_en" IS '菜单标题-英文';
COMMENT ON COLUMN "public"."iam_perm_menu"."i18n_key" IS '国际化key';
COMMENT ON COLUMN "public"."iam_perm_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "public"."iam_perm_menu"."hidden" IS '是否隐藏';
COMMENT ON COLUMN "public"."iam_perm_menu"."hide_children_menu" IS '是否隐藏子菜单';
COMMENT ON COLUMN "public"."iam_perm_menu"."component" IS '组件';
COMMENT ON COLUMN "public"."iam_perm_menu"."path" IS '访问路径';
COMMENT ON COLUMN "public"."iam_perm_menu"."redirect" IS '菜单跳转地址(重定向)';
COMMENT ON COLUMN "public"."iam_perm_menu"."sort_no" IS '菜单排序';
COMMENT ON COLUMN "public"."iam_perm_menu"."root" IS '是否是一级菜单';
COMMENT ON COLUMN "public"."iam_perm_menu"."keep_alive" IS '是否开启页面缓存';
COMMENT ON COLUMN "public"."iam_perm_menu"."affix_tab" IS '是否固定标签页';
COMMENT ON COLUMN "public"."iam_perm_menu"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_perm_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_perm_menu"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_perm_menu"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_perm_menu"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_perm_menu"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_perm_menu"."menu_type" IS '菜单类型: catalog-目录, menu-菜单, embedded-内嵌, link-外链';
COMMENT ON COLUMN "public"."iam_perm_menu"."active_icon" IS '激活状态图标';
COMMENT ON COLUMN "public"."iam_perm_menu"."badge" IS '徽章显示文本';
COMMENT ON COLUMN "public"."iam_perm_menu"."badge_type" IS '徽章类型: dot-圆点, normal-文本';
COMMENT ON COLUMN "public"."iam_perm_menu"."badge_variants" IS '徽章样式变体';
COMMENT ON COLUMN "public"."iam_perm_menu"."iframe_src" IS '内嵌页面URL地址';
COMMENT ON COLUMN "public"."iam_perm_menu"."link" IS '外部链接URL地址';
COMMENT ON TABLE "public"."iam_perm_menu" IS '菜单权限配置';

-- ----------------------------
-- Records of iam_perm_menu
-- ----------------------------
INSERT INTO "public"."iam_perm_menu" VALUES (306, 3, 'system:storage', 'admin', 'StorageManagement', '存储管理', 'Storage Management', 'menu.system.storage', 'lucide:database', 'f', 'f', NULL, '/system/storage', NULL, 5, 'f', 't', 'f', 1, '2026-04-10 00:00:00', 1, '2026-04-10 00:00:00', 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (30601, 306, 'system:file:platform', 'admin', 'PlatformFile', '平台文件', 'Platform File', 'menu.system.storage.platform', 'lucide:file', 'f', 'f', '/system/file/platform/PlatformFileList', '/system/storage/platform', NULL, 1, 'f', 't', 'f', 1, '2026-04-10 00:00:00', 1, '2026-04-10 09:03:38.933496', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (202, 2, NULL, 'admin', 'FileUploadDemo', '文件上传演示', 'File Upload Demo', 'menu.demos.fileUpload', 'lucide:upload', 'f', 'f', '/demos/file-upload/FileUploadDemo', '/demos/file-upload', NULL, 2, 'f', 't', 'f', 0, '2026-04-10 00:00:00', NULL, '2026-04-10 00:00:00', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (307, 3, 'system:monitor', 'admin', 'SystemMonitor', '系统监控', 'System Monitor', 'menu.system.monitor', 'lucide:monitor', 'f', 'f', NULL, '/system/monitor', NULL, 50, 'f', 't', 'f', 1, '2026-04-11 00:00:00', 1, '2026-04-12 20:53:45.790453', 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (101, 1, NULL, 'admin', 'Analytics', '分析页', 'Analytics', 'menu.dashboard.analytics', 'lucide:area-chart', 'f', 'f', '/dashboard/analytics/index', '/analytics', NULL, 1, 'f', 'f', 't', 0, '2026-03-20 11:11:13.134079', NULL, '2026-03-20 11:11:13.134079', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (102, 1, NULL, 'admin', 'Workspace', '工作台', 'Workspace', 'menu.dashboard.workspace', 'carbon:workspace', 'f', 'f', '/dashboard/workspace/index', '/workspace', NULL, 2, 'f', 'f', 'f', 0, '2026-03-20 11:11:13.134079', NULL, '2026-03-20 11:11:13.134079', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (3, NULL, NULL, 'admin', 'System', '系统管理', 'System', 'menu.system', 'carbon:settings', 'f', 'f', NULL, '/system', NULL, 0, 'f', 't', 'f', 0, '2026-03-20 11:11:13.134079', 1, '2026-03-30 21:54:20.444713', 2, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (301, 3, NULL, 'admin', 'SystemBasic', '基础数据', 'Basic Data', 'menu.system.basic', 'carbon:data-base', 'f', 'f', NULL, '/system/basic', NULL, 1, 'f', 't', 'f', 0, '2026-03-20 11:11:13.134079', NULL, '2026-03-20 11:11:13.134079', 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (30101, 301, 'system:dict', 'admin', 'SystemDict', '字典管理', 'Dictionary', 'menu.system.basic.dict', 'carbon:book', 'f', 'f', '/system/basic/dict/DictList', '/system/basic/dict', NULL, 1, 'f', 't', 'f', 0, '2026-03-20 11:11:13.134079', 1, '2026-03-31 21:40:01.132798', 2, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (30103, 305, 'iam:role', 'admin', 'SystemRole', '角色管理', 'Role Management', 'menu.system.basic.role', 'carbon:user-role', 'f', 'f', '/iam/perm/role/RoleList', '/iam/perm/role', NULL, 3, 'f', 't', 'f', 0, '2026-03-20 11:11:13.134079', 1, '2026-04-09 22:42:57.371619', 2, 'f', 'menu', NULL, NULL, NULL, NULL, '', '');
INSERT INTO "public"."iam_perm_menu" VALUES (302, 3, NULL, 'admin', 'SystemLog', '日志管理', 'Log Management', 'menu.system.log', 'lucide:file-text', 'f', 'f', NULL, '/system/log', NULL, 99, 'f', 't', 'f', 0, '2026-03-20 11:11:13.134079', 1, '2026-04-05 16:56:11.97756', 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (30102, 301, 'iam:perm:menu', 'admin', 'SystemMenu', '菜单管理', 'Menu Management', 'menu.system.basic.menu', 'carbon:menu', 'f', 'f', '/iam/perm/menu/MenuList', '/system/basic/menu', NULL, 0, 'f', 't', 'f', 0, '2026-03-20 11:11:13.134079', 1, '2026-03-31 21:39:34.260955', 2, 'f', 'menu', NULL, NULL, NULL, NULL, '', '');
INSERT INTO "public"."iam_perm_menu" VALUES (30201, 302, 'starter:log:login', 'admin', 'SystemLoginLog', '登录日志', 'Login Log', 'menu.system.log.login', 'lucide:log-in', 'f', 'f', '/system/log/login/LoginLogList', '/system/log/login', NULL, 1, 'f', 't', 'f', 0, '2026-03-20 11:11:13.134079', 1, '2026-03-30 23:25:09.855555', 1, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (2, NULL, NULL, 'admin', 'Demos', '演示', 'Demos', 'menu.demos', 'ic:baseline-view-in-ar', 'f', 'f', NULL, '/demos', NULL, 1000, 'f', 't', 'f', 0, '2026-03-20 11:11:13.134079', NULL, '2026-03-20 11:11:13.134079', 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (201, 2, NULL, 'admin', 'AntDesignDemos', 'Antd Next演示', 'Antd Next Demo', 'menu.demos.antd', 'lucide:box', 'f', 'f', '/demos/antd/AntdDemo', '/demos/ant-design-next', NULL, 1, 'f', 'f', 'f', 0, '2026-03-20 11:11:13.134079', NULL, '2026-03-20 11:11:13.134079', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (206, 2, NULL, 'admin', 'DescriptionsDemo', '描述列表演示', 'Descriptions Demo', 'menu.demos.descriptions', 'lucide:list', 'f', 'f', '/demos/descriptions/DescriptionsDemo', '/demos/descriptions', NULL, 6, 'f', 't', 'f', 0, '2026-03-20 11:11:13.134079', NULL, '2026-03-20 11:11:13.134079', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (30202, 302, 'starter:log:operate', 'admin', 'SystemOperateLog', '操作日志', 'Operate Log', 'menu.system.log.operate', 'lucide:activity', 'f', 'f', '/system/log/operate/OperateLogList', '/system/log/operate', NULL, 2, 'f', 't', 'f', 0, '2026-03-20 11:11:13.134079', 1, '2026-03-30 23:24:57.076166', 1, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1, NULL, NULL, 'admin', 'Dashboard', '仪表板', 'Dashboard', 'menu.dashboard', 'lucide:layout-dashboard', 'f', 'f', NULL, '/dashboard', '/analytics', -1, 'f', 'f', 'f', 0, '2026-03-20 11:11:13.134079', NULL, '2026-03-20 11:11:13.134079', 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (402, 4, 'payment:isv', 'admin', 'PaymentISV', '服务商管理', 'ISV Management', 'menu.payment.isv', 'lucide:server', 'f', 'f', NULL, '/payment/isv', NULL, 2, 'f', 't', 'f', 0, '2026-04-06 00:00:00', NULL, '2026-04-06 00:00:00', 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (404, 4, 'payment:merchant', 'admin', 'PaymentMerchant', '商户管理', 'Merchant Management', 'menu.payment.merchant', 'lucide:store', 'f', 'f', NULL, '/payment/merchant', NULL, 4, 'f', 't', 'f', 0, '2026-04-06 00:00:00', NULL, '2026-04-06 00:00:00', 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4, NULL, 'payment:system', 'admin', 'PaymentSystem', '支付系统', 'Payment System', 'menu.payment', 'lucide:credit-card', 'f', 'f', NULL, '/payment', NULL, 3, 'f', 't', 'f', 0, '2026-04-06 00:00:00', 1, '2026-04-07 21:07:11.194433', 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (30402, 304, 'system:platform:config', 'admin', 'PlatformConfig', '平台配置', 'Platform Config', 'menu.system.config.platform', 'lucide:settings', 'f', 'f', '/system/config/platform/PlatformConfig', '/system/config/platform', NULL, 2, 'f', 't', 'f', 1, '2026-04-08 00:00:00', 1, '2026-04-08 00:00:00', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (30401, 304, 'system:security:config', 'admin', 'SecurityConfig', '安全配置', 'Security Config', 'menu.system.config.security', 'lucide:shield-check', 'f', 'f', '/system/config/security/SecurityConfig', '/system/config/security', NULL, 1, 'f', 't', 'f', 1, '2026-04-05 10:00:00', 1, '2026-04-05 10:00:00', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (30501, 305, 'iam:user:manager', 'admin', 'UserList', '用户管理', 'User Management', 'iam.user.title', 'ant-design:team-outlined', 'f', 'f', 'views/iam/user/UserList', '/iam/user', NULL, 10, 'f', 't', 'f', 1, '2026-03-31 08:23:04.37507', 1, '2026-04-09 22:43:11.814198', 4, 'f', 'menu', NULL, NULL, NULL, NULL, '', '');
INSERT INTO "public"."iam_perm_menu" VALUES (305, 3, 'iam:perm', 'admin', 'SystemPerm', '权限管理', 'Permission Management', 'menu.system.perm', 'lucide:shield', 'f', 'f', NULL, '/system/perm', NULL, 2, 'f', 't', 'f', 1, '2026-04-09 00:00:00', 1, '2026-04-09 23:10:44.651238', 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (304, 3, 'system:config', 'admin', 'SystemConfig', '系统配置', 'System Config', 'menu.system.config', 'lucide:settings-2', 'f', 'f', NULL, '/system/config', NULL, 10, 'f', 't', 'f', 0, '2026-04-05 00:00:00', 1, '2026-04-09 23:11:00.840153', 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (30701, 307, 'iam:online:user', 'admin', 'OnlineUser', '在线用户', 'Online User', 'menu.system.monitor.online', 'lucide:users', 'f', 'f', '/system/monitor/online/OnlineUserList', '/system/monitor/online', NULL, 1, 'f', 't', 'f', 1, '2026-04-12 00:00:00', 1, '2026-04-12 00:00:00', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (40201, 402, 'payment:isv:info', 'admin', 'IsvInfo', '服务商信息', 'ISV Info', 'menu.payment.isv.info', 'lucide:building-2', 'f', 'f', '/payment/isv/info/IsvList', '/payment/isv/info', NULL, 1, 'f', 't', 'f', 1, '2026-04-09 00:00:00', NULL, '2026-04-09 00:00:00', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (40401, 404, 'payment:merchant:info', 'admin', 'MerchantInfo', '商户信息', 'Merchant Info', 'menu.payment.merchant.info', 'ant-design:shop-twotone', 'f', 'f', '/payment/merchant/info/MerchantList', '/payment/merchant/info', NULL, 1, 'f', 't', 'f', 1, '2026-04-14 00:00:00', 1, '2026-05-02 14:57:49.044189', 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '');
INSERT INTO "public"."iam_perm_menu" VALUES (401, 4, 'payment:platform', 'admin', 'PaymentPlatform', '支付主数据', 'Payment Master Data', 'menu.payment.platform', 'lucide:building', 'f', 'f', NULL, '/payment/platform', NULL, 1, 'f', 't', 'f', 0, '2026-04-06 00:00:00', 1, '2026-05-28 14:25:42.880461', 3, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020106, 40201, 'payment:isv:manage:security', 'admin', 'IsvSecurityConfig', '安全配置', 'Security Config', 'menu.payment.isv.manage.security', NULL, 't', 'f', '/payment/isv/security/IsvSecurityConfig', '/payment/isv/security', NULL, 6, 'f', 't', 'f', 1, '2026-04-29 00:00:00', 1, '2026-04-29 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (203, 2, 'demos:region', 'admin', 'RegionCascaderDemo', '行政区划选择器', 'Region Cascader', 'menu.demos.region', 'lucide:map-pin', 'f', 'f', '/demos/region/RegionCascaderDemo', '/demos/region', NULL, 3, 'f', 't', 'f', 0, '2026-04-25 00:00:00', NULL, '2026-04-25 00:00:00', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020101, 40201, 'payment:isv:manage', 'admin', 'IsvManage', '服务商管理', 'ISV Management', 'menu.payment.isv.manage', NULL, 't', 'f', '/payment/isv/manage/workbench/IsvManage', '/payment/isv/manage', NULL, 2, 'f', 't', 'f', 1, '2026-04-13 03:31:51.860709', 1, '2026-04-13 03:31:51.860709', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020102, 40201, 'payment:isv:manage:info', 'admin', 'IsvInfoManage', '服务商信息', 'ISV Info', 'menu.payment.isv.manage.info', NULL, 't', 'f', '/payment/isv/manage/info/IsvInfoManage', '/payment/isv/manage/info', NULL, 3, 'f', 't', 'f', 1, '2026-04-24 00:00:00', 1, '2026-04-24 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020103, 40201, 'payment:isv:manage:entity', 'admin', 'IsvEntityManage', '主体信息', 'Entity Info', 'menu.payment.isv.manage.entity', NULL, 't', 'f', '/payment/isv/manage/entity/IsvEntityManage', '/payment/isv/manage/entity', NULL, 4, 'f', 't', 'f', 1, '2026-04-24 00:00:00', 1, '2026-04-24 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020104, 40201, 'payment:isv:manage:settle', 'admin', 'IsvSettleManage', '结算账户', 'Settle Account', 'menu.payment.isv.manage.settle', NULL, 't', 'f', '/payment/isv/manage/settle/IsvSettleManage', '/payment/isv/manage/settle', NULL, 5, 'f', 't', 'f', 1, '2026-04-25 00:00:00', 1, '2026-04-25 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020105, 40201, 'payment:isv:manage:product', 'admin', 'IsvProductConfig', '服务商产品配置', 'ISV Product Config', 'menu.payment.isv.manage.product', NULL, 't', 'f', '/payment/isv/manage/product/IsvProductConfig', '/payment/isv/product', NULL, 5, 'f', 't', 'f', 1, '2026-04-27 00:00:00', 1, '2026-04-27 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040103, 40401, 'payment:merchant:manage:info', 'admin', 'MchInfoManage', '商户信息', 'Merchant Info', 'menu.payment.merchant.manage.info', NULL, 't', 'f', '/payment/merchant/manage/info/MchInfoManage', '/payment/merchant/manage/info', NULL, 3, 'f', 't', 'f', 1, '2026-05-02 00:00:00', 1, '2026-05-02 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040104, 40401, 'payment:merchant:manage:entity', 'admin', 'MchEntityManage', '主体信息', 'Entity Info', 'menu.payment.merchant.manage.entity', NULL, 't', 'f', '/payment/merchant/manage/entity/MchEntityManage', '/payment/merchant/manage/entity', NULL, 4, 'f', 't', 'f', 1, '2026-05-02 00:00:00', 1, '2026-05-02 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040105, 40401, 'payment:merchant:manage:settle', 'admin', 'MchSettleManage', '结算账户', 'Settle Account', 'menu.payment.merchant.manage.settle', NULL, 't', 'f', '/payment/merchant/manage/settle/MchSettleManage', '/payment/merchant/manage/settle', NULL, 5, 'f', 't', 'f', 1, '2026-05-02 00:00:00', 1, '2026-05-02 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020108, 40201, 'payment:isv:manage:credentialConfig', 'admin', 'IsvCredentialConfig', '对接配置', 'Credential Config', 'menu.payment.isv.manage.credentialConfig', '', 't', 'f', '/payment/isv/manage/credential/IsvCredentialConfig', '/payment/isv/manage/credential', NULL, 8, 'f', 't', 'f', 1, '2026-05-02 00:00:00', 1, '2026-05-04 23:13:17.189339', 2, 'f', 'subpage', NULL, NULL, NULL, NULL, '', '');
INSERT INTO "public"."iam_perm_menu" VALUES (4040102, 40401, 'payment:merchant:manage:credentialConfig', 'admin', 'MerchantCredentialConfig', '对接配置', 'Credential Config', 'menu.payment.merchant.manage.credentialConfig', NULL, 't', 'f', '/payment/merchant/manage/credential/MerchantCredentialConfig', '/payment/merchant/manage/credential', NULL, 3, 'f', 't', 'f', 1, '2026-05-02 00:00:00', 1, '2026-05-02 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040101, 40401, 'payment:merchant:manage', 'admin', 'MerchantManage', '商户管理', 'Merchant Management', 'menu.payment.merchant.manage', '', 't', 'f', '/payment/merchant/manage/workbench/MerchantManage', '/payment/merchant/manage', NULL, 2, 'f', 't', 'f', 1, '2026-04-14 00:00:00', 1, '2026-05-02 14:57:49.052168', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020109, 40201, 'payment:isv:user', 'admin', 'IsvUser', '服务商用户', 'ISV User', 'menu.payment.isv.user', '', 't', 'f', '/payment/isv/user/IsvUserList', '/payment/isv/user', NULL, 9, 'f', 't', 'f', 1, '2026-05-04 00:00:00', 1, '2026-05-04 23:07:02.62964', 1, 'f', 'subpage', NULL, NULL, NULL, NULL, '', '');
INSERT INTO "public"."iam_perm_menu" VALUES (4040106, 40401, 'payment:merchant:channelMerchant', 'admin', 'ChannelMerchant', '通道商户', 'Channel Merchant', 'menu.payment.merchant.channelMerchant', NULL, 't', 'f', '/payment/merchant/channel-merchant/ChannelMerchantList', '/payment/merchant/channel-merchant', NULL, 6, 'f', 't', 'f', 1, '2026-05-04 00:00:00', 1, '2026-05-04 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040107, 40401, 'payment:merchant:product', 'admin', 'MchProductConfig', '支付产品', 'Payment Product', 'menu.payment.merchant.product', NULL, 't', 'f', '/payment/merchant/product/MchProductConfig', '/payment/merchant/product', NULL, 7, 'f', 't', 'f', 1, '2026-05-04 00:00:00', 1, '2026-05-04 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040108, 40401, 'payment:merchant:user', 'admin', 'MerchantUser', '商户用户', 'Merchant User', 'menu.payment.merchant.user', '', 't', 'f', '/payment/merchant/user/MerchantUserList', '/payment/merchant/user', NULL, 8, 'f', 't', 'f', 1, '2026-05-05 00:00:00', 1, '2026-05-05 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020107, 40201, 'payment:isv:manage:productPayConfig', 'admin', 'IsvProductPayConfig', '支付产品配置', 'Product Pay Config', 'menu.payment.isv.manage.productPayConfig', NULL, 't', 'f', '/payment/isv/manage/pay-config/IsvProductPayConfig', '/payment/isv/product-pay-config', NULL, 7, 'f', 't', 'f', 1, '2026-04-28 00:00:00', 1, '2026-04-28 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040109, 40401, 'payment:merchant:channelMerchant:create', 'admin', 'ChannelMerchantCreate', '创建通道商户', 'Create Channel Merchant', 'menu.payment.merchant.channelMerchant.create', NULL, 't', 'f', '/payment/merchant/channel-merchant/ChannelMerchantCreate', '/payment/merchant/channel-merchant/create', NULL, 9, 'f', 't', 'f', 1, '2026-05-06 00:00:00', 1, '2026-05-06 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040111, 4040110, 'payment:merchant:app:payRoute', 'admin', 'PayRouteConfig', '通道路由', 'Channel Routing', 'menu.payment.merchant.app.payRoute', NULL, 't', 'f', '/payment/merchant/route/PayRouteConfig', '/payment/merchant/route', NULL, 2, 'f', 't', 'f', 1, '2026-05-25 06:23:46.483985', 1, '2026-05-27 04:03:06.445803', 1, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040110, 40401, 'payment:merchant:app', 'admin', 'MchAppInfoList', '应用管理', 'App Management', 'menu.payment.merchant.app', NULL, 't', 'f', '/payment/merchant/app/MchAppInfoList', '/payment/merchant/app', NULL, 10, 'f', 't', 'f', 1, '2026-05-25 00:00:00', 1, '2026-05-25 03:54:00.095239', 1, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (6, NULL, 'trade:system', 'admin', 'TransactionManagement', '交易管理', 'Transaction Management', 'menu.trade', 'lucide:arrow-left-right', 'f', 'f', NULL, '/trade', '/trade/index', 3.5, 'f', 't', 'f', 1, '2026-05-25 00:00:00', 1, '2026-05-25 00:00:00', 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (601, 6, NULL, 'admin', 'TransactionIndex', '功能开发中', 'Coming Soon', 'menu.trade.index', 'lucide:construction', 'f', 'f', '/_core/fallback/coming-soon', '/trade/index', NULL, 1, 'f', 'f', 'f', 1, '2026-05-25 00:00:00', 1, '2026-05-25 00:00:00', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (7, NULL, 'terminal:system', 'admin', 'TerminalManagement', '终端管理', 'Terminal Management', 'menu.terminal', 'lucide:smartphone', 'f', 'f', NULL, '/terminal', '/terminal/index', 4.5, 'f', 't', 'f', 1, '2026-05-25 00:00:00', 1, '2026-05-25 00:00:00', 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (701, 7, NULL, 'admin', 'TerminalIndex', '功能开发中', 'Coming Soon', 'menu.terminal.index', 'lucide:construction', 'f', 'f', '/_core/fallback/coming-soon', '/terminal/index', NULL, 1, 'f', 'f', 'f', 1, '2026-05-25 00:00:00', 1, '2026-05-25 00:00:00', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (40103, 401, 'payment:platform:channel', 'admin', 'PayChannelList', '支付通道', 'Payment Channel', 'menu.payment.platform.channel', 'lucide:radio-tower', 'f', 'f', '/payment/masterdata/channel/PayChannelList', '/payment/platform/pay-channel', NULL, 0, 'f', 't', 'f', 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (40102, 401, 'payment:platform:provider', 'admin', 'PayProviderList', '支付渠道/方式', 'Pay Channel / Method', 'menu.payment.platform.provider', 'lucide:list-tree', 'f', 'f', '/payment/masterdata/provider/PayProviderList', '/payment/platform/pay-provider', NULL, 1, 'f', 't', 'f', 1, '2026-05-28 04:18:01.383008', 1, '2026-05-30 16:21:19.464024', 5, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (40104, 401, 'payment:platform:capability', 'admin', 'PayCapabilityList', '支付能力', 'Payment Capability', 'menu.payment.platform.capability', 'lucide:zap', 'f', 'f', '/payment/masterdata/capability/PayCapabilityList', '/payment/platform/pay-capability', NULL, 3, 'f', 't', 'f', 1, '2026-05-27 00:00:00', 1, '2026-05-28 14:43:27.505831', 4, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (40101, 401, 'payment:platform:product', 'admin', 'ProductList', '支付产品', 'Payment Product', 'menu.payment.platform.product', 'lucide:package', 'f', 'f', '/payment/masterdata/product/PayProductList', '/payment/platform/product', NULL, 2, 'f', 't', 'f', 1, '2026-04-24 00:00:00', 1, '2026-05-28 14:25:42.892396', 3, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020121, 40201, 'payment:isv:manage:alipayIsvApp', 'admin', 'AlipayIsvAppManage', '服务商应用', 'ISV Application', 'menu.payment.isv.manage.alipayIsvApp', NULL, 't', 'f', '/payment/channel/alipay/manage/app/AlipayIsvAppManage', '/payment/isv/alipay-app-manage', NULL, 11, 'f', 't', 'f', 1, '2026-06-02 00:00:00', 1, '2026-06-02 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020125, 40201, 'payment:wechat:isv:manage:wechatIsvApp', 'admin', 'WechatIsvAppManage', '微信服务商应用', 'WeChat ISV Application', 'menu.payment.isv.manage.wechatIsvApp', NULL, 't', 'f', '/payment/channel/wechat/manage/app/WechatIsvAppManage', '/payment/isv/wechat-app-manage', NULL, 14, 'f', 't', 'f', 1, '2026-06-06 00:00:00', 1, '2026-06-06 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4020120, 40201, 'payment:isv:manage:productDetail', 'admin', 'IsvProductDetailDispatch', '服务商产品配置', 'ISV Product Configuration', 'menu.payment.isv.manage.productDetail', NULL, 't', 'f', '/payment/isv/manage/pay-config/detail/IsvProductDetailDispatch', '/payment/isv/product-detail', NULL, 10, 'f', 't', 'f', 1, '2026-06-02 00:00:00', 1, '2026-06-02 00:00:00', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040112, 40401, 'payment:merchant:channelMerchant:detail', 'admin', 'ChannelMerchantDetailDispatch', '通道商户详情', 'Channel Merchant Detail', 'menu.payment.merchant.channelMerchant.detail', NULL, 't', 'f', '/payment/merchant/channel-merchant/detail/ChannelMerchantDetailDispatch', '/payment/merchant/channel-merchant/detail', NULL, 10, 'f', 't', 'f', 1, '2026-06-08 03:48:34.936565', 1, '2026-06-08 03:48:34.936565', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040113, 40401, 'payment:merchant:channelMerchant:wechatApp', 'admin', 'WechatMchAppManage', '微信通道商户应用', 'WeChat Channel Merchant App', 'menu.payment.merchant.channelMerchant.wechatApp', NULL, 't', 'f', '/payment/channel/wechat/manage/mch/app/WechatMchAppManage', '/payment/merchant/channel-merchant/wechat-app-manage', NULL, 11, 'f', 't', 'f', 1, '2026-06-08 07:57:13.620205', 1, '2026-06-08 07:57:13.620205', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040114, 40401, 'payment:merchant:channelMerchant:alipayApp', 'admin', 'AlipayMchAppManage', '支付宝通道商户应用', 'Alipay Channel Merchant App', 'menu.payment.merchant.channelMerchant.alipayApp', NULL, 't', 'f', '/payment/channel/alipay/manage/mch/app/AlipayMchAppManage', '/payment/merchant/channel-merchant/alipay-app-manage', NULL, 12, 'f', 't', 'f', 1, '2026-06-12 06:28:11.274785', 1, '2026-06-12 06:28:11.274785', 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for iam_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_role";
CREATE TABLE "public"."iam_role" (
  "id" int8 NOT NULL,
  "code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "name_cn" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "name_en" varchar(200) COLLATE "pg_catalog"."default",
  "client_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "data_scope" varchar(50) COLLATE "pg_catalog"."default",
  "internal" bool DEFAULT false,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."iam_role"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_role"."code" IS '角色编码';
COMMENT ON COLUMN "public"."iam_role"."name_cn" IS '中文名称';
COMMENT ON COLUMN "public"."iam_role"."name_en" IS '英文名称';
COMMENT ON COLUMN "public"."iam_role"."client_code" IS '终端编码';
COMMENT ON COLUMN "public"."iam_role"."data_scope" IS '数据权限范围';
COMMENT ON COLUMN "public"."iam_role"."internal" IS '是否系统内置';
COMMENT ON COLUMN "public"."iam_role"."remark" IS '备注';
COMMENT ON COLUMN "public"."iam_role"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_role"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_role"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_role"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_role"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."iam_role" IS '角色';

-- ----------------------------
-- Records of iam_role
-- ----------------------------
INSERT INTO "public"."iam_role" VALUES (2039658239578222592, 'cs2', '测试2', 'cs2', 'admin', NULL, 'f', NULL, 1, '2026-04-02 18:56:33.993631', 1, '2026-04-08 23:07:50.050605', 3, 'f');
INSERT INTO "public"."iam_role" VALUES (2034473713154596864, 'cs', '中文', 'en', 'admin', NULL, 'f', '123', 0, '2026-03-19 11:35:06.567793', 1, '2026-04-08 23:07:53.55479', 2, 'f');
INSERT INTO "public"."iam_role" VALUES (1928374650192837465, 'agent_admin', '代理商管理员', 'Agent Admin', 'agent', NULL, 't', '系统内置代理商管理员角色', 1, '2026-04-09 13:33:34.754947', 1, '2026-04-09 13:33:34.754947', 0, 'f');
INSERT INTO "public"."iam_role" VALUES (1928374650192837466, 'merchant_admin', '商户管理员', 'Merchant Admin', 'merchant', NULL, 't', '系统内置商户管理员角色', 1, '2026-04-09 13:33:34.754947', 1, '2026-04-09 13:33:34.754947', 0, 'f');
INSERT INTO "public"."iam_role" VALUES (1928374650192837467, 'isv_admin', '服务商管理员', 'ISV Admin', 'isv', NULL, 't', '系统内置服务商管理员角色', 1, '2026-04-09 13:33:34.754947', 1, '2026-04-09 13:33:34.754947', 0, 'f');

-- ----------------------------
-- Table structure for iam_role_code
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_role_code";
CREATE TABLE "public"."iam_role_code" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "code_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."iam_role_code"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_role_code"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."iam_role_code"."code_id" IS '权限码ID';
COMMENT ON TABLE "public"."iam_role_code" IS '角色权限码关系';

-- ----------------------------
-- Records of iam_role_code
-- ----------------------------

-- ----------------------------
-- Table structure for iam_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_role_menu";
CREATE TABLE "public"."iam_role_menu" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "client_code" varchar(50) COLLATE "pg_catalog"."default",
  "menu_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."iam_role_menu"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_role_menu"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."iam_role_menu"."client_code" IS '终端编码: ADMIN/ISV/AGENT/MCH';
COMMENT ON COLUMN "public"."iam_role_menu"."menu_id" IS '菜单ID';
COMMENT ON TABLE "public"."iam_role_menu" IS '角色-菜单关联表';

-- ----------------------------
-- Records of iam_role_menu
-- ----------------------------
INSERT INTO "public"."iam_role_menu" VALUES (2038571556120207360, 2034473713154596864, NULL, 30102);

-- ----------------------------
-- Table structure for iam_user_expand_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_expand_info";
CREATE TABLE "public"."iam_user_expand_info" (
  "id" int8 NOT NULL,
  "sex" varchar(10) COLLATE "pg_catalog"."default",
  "avatar" varchar(500) COLLATE "pg_catalog"."default",
  "birthday" date,
  "last_login_time" timestamp(6),
  "register_time" timestamp(6),
  "current_login_time" timestamp(6),
  "last_login_ip" varchar(100) COLLATE "pg_catalog"."default",
  "login_count" int4,
  "register_source" varchar(100) COLLATE "pg_catalog"."default",
  "register_channel" varchar(100) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."iam_user_expand_info"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_expand_info"."sex" IS '性别';
COMMENT ON COLUMN "public"."iam_user_expand_info"."avatar" IS '头像图片ID';
COMMENT ON COLUMN "public"."iam_user_expand_info"."birthday" IS '生日';
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_login_time" IS '上次登录时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."register_time" IS '注册时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."current_login_time" IS '本次登录时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_login_ip" IS '最后登录IP';
COMMENT ON COLUMN "public"."iam_user_expand_info"."login_count" IS '登录次数';
COMMENT ON COLUMN "public"."iam_user_expand_info"."register_source" IS '注册来源';
COMMENT ON COLUMN "public"."iam_user_expand_info"."register_channel" IS '注册渠道';
COMMENT ON COLUMN "public"."iam_user_expand_info"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_user_expand_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_user_expand_info"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."iam_user_expand_info" IS '用户扩展信息';

-- ----------------------------
-- Records of iam_user_expand_info
-- ----------------------------
INSERT INTO "public"."iam_user_expand_info" VALUES (1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '2026-03-28 22:26:08', 0, '2026-03-28 22:26:00', 0, 'f');
INSERT INTO "public"."iam_user_expand_info" VALUES (2039557567951310848, NULL, NULL, NULL, NULL, '2026-04-02 12:16:32.012305', NULL, NULL, NULL, NULL, NULL, 1, '2026-04-02 12:16:32.015326', 1, '2026-04-02 12:16:32.015326', 0, 'f');
INSERT INTO "public"."iam_user_expand_info" VALUES (2042248310121746432, NULL, NULL, NULL, NULL, '2026-04-09 22:28:34.954599', NULL, NULL, NULL, NULL, NULL, 1, '2026-04-09 22:28:34.957113', 1, '2026-04-09 22:28:34.957113', 0, 'f');
INSERT INTO "public"."iam_user_expand_info" VALUES (2043895568185643008, NULL, NULL, NULL, NULL, '2026-04-14 11:34:11.961945', NULL, NULL, NULL, NULL, NULL, 1, '2026-04-14 11:34:11.963458', 1, '2026-04-14 11:34:11.963963', 0, 'f');
INSERT INTO "public"."iam_user_expand_info" VALUES (2043897038620545024, NULL, NULL, NULL, NULL, '2026-04-14 11:40:02.455235', NULL, NULL, NULL, NULL, NULL, 1, '2026-04-14 11:40:02.455235', 1, '2026-04-14 11:40:02.455235', 0, 'f');
INSERT INTO "public"."iam_user_expand_info" VALUES (2043973456939393024, NULL, NULL, NULL, NULL, '2026-04-14 16:43:42.124115', NULL, NULL, NULL, NULL, NULL, 1, '2026-04-14 16:43:42.126121', 1, '2026-04-14 16:43:42.126121', 0, 'f');
INSERT INTO "public"."iam_user_expand_info" VALUES (2047692453195206656, NULL, NULL, NULL, NULL, '2026-04-24 23:01:39.836945', NULL, NULL, NULL, NULL, NULL, 1, '2026-04-24 23:01:39.839802', 1, '2026-04-24 23:01:39.839802', 0, 'f');
INSERT INTO "public"."iam_user_expand_info" VALUES (2048608994656964608, NULL, NULL, NULL, NULL, '2026-04-27 11:43:40.357126', NULL, NULL, NULL, NULL, NULL, 1, '2026-04-27 11:43:40.359637', 1, '2026-04-27 11:43:40.359637', 0, 'f');
INSERT INTO "public"."iam_user_expand_info" VALUES (2048950207985635328, NULL, NULL, NULL, NULL, '2026-04-28 10:19:31.922097', NULL, NULL, NULL, NULL, NULL, 1, '2026-04-28 10:19:31.924149', 1, '2026-04-28 10:19:31.924149', 0, 'f');
INSERT INTO "public"."iam_user_expand_info" VALUES (2050845636545449984, NULL, NULL, NULL, NULL, '2026-05-03 15:51:17.411961', NULL, NULL, NULL, NULL, NULL, 1, '2026-05-03 15:51:17.413963', 1, '2026-05-03 15:51:17.413963', 0, 'f');
INSERT INTO "public"."iam_user_expand_info" VALUES (2050857563292131328, NULL, NULL, NULL, NULL, '2026-05-03 16:38:40.885302', NULL, NULL, NULL, NULL, NULL, 1, '2026-05-03 16:38:40.88781', 1, '2026-05-03 16:38:40.88781', 0, 'f');
INSERT INTO "public"."iam_user_expand_info" VALUES (2051222393719070720, NULL, NULL, NULL, NULL, '2026-05-04 16:48:23.227008', NULL, NULL, NULL, NULL, NULL, 1, '2026-05-04 16:48:23.22952', 1, '2026-05-04 16:48:23.22952', 0, 'f');

-- ----------------------------
-- Table structure for iam_user_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_info";
CREATE TABLE "public"."iam_user_info" (
  "id" int8 NOT NULL,
  "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "client_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "account" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "phone" varchar(50) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "administrator" bool DEFAULT false,
  "status" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'normal'::character varying,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."iam_user_info"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_info"."name" IS '名称';
COMMENT ON COLUMN "public"."iam_user_info"."client_code" IS '终端编码';
COMMENT ON COLUMN "public"."iam_user_info"."account" IS '账号';
COMMENT ON COLUMN "public"."iam_user_info"."password" IS '密码';
COMMENT ON COLUMN "public"."iam_user_info"."phone" IS '手机号';
COMMENT ON COLUMN "public"."iam_user_info"."email" IS '邮箱';
COMMENT ON COLUMN "public"."iam_user_info"."administrator" IS '是否管理员';
COMMENT ON COLUMN "public"."iam_user_info"."status" IS '账号状态';
COMMENT ON COLUMN "public"."iam_user_info"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_user_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_info"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_user_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_info"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_user_info"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."iam_user_info" IS '用户核心信息';

-- ----------------------------
-- Records of iam_user_info
-- ----------------------------
INSERT INTO "public"."iam_user_info" VALUES (2051222393719070720, '测试企业代理商管理员', 'agent', 'csqydls', '$2a$10$adE2SwUw7dRkghvMcGQplupneWyb0F8YXXnkQRhED4kmrD.NrZeVu', '', '', 'f', 'normal', 1, '2026-05-04 16:48:23.191626', 1, '2026-05-06 11:31:51.844318', 2, 'f');
INSERT INTO "public"."iam_user_info" VALUES (2039557567951310848, 'cs111', 'admin', 'csadmin', '$2a$10$2YU69WcXx86BHe.fvycrRuuC6QmFE21DCyKcB65sgmkCI9mjZMnuK', NULL, NULL, 'f', 'normal', 1, '2026-04-02 12:16:32.003451', 1, '2026-04-13 14:45:56.265724', 3, 'f');
INSERT INTO "public"."iam_user_info" VALUES (2043895568185643008, '测试管理员', 'agent', 'csagent', '$2a$10$KQtswyZldFrx18hsB3hnDevZXAHhtym23eszDr4lZ34FjCJgggiDK', NULL, NULL, 'f', 'normal', 1, '2026-04-14 11:34:11.858064', 1, '2026-04-14 11:34:11.918498', 0, 'f');
INSERT INTO "public"."iam_user_info" VALUES (2043897038620545024, '二级代理管理员', 'agent', 'cserji', '$2a$10$2pE6H4Z8TOjNRMMV2Axuw.p/AcTFA4HKc6T.1PYXnLGOA.FO9ESN6', NULL, NULL, 'f', 'normal', 1, '2026-04-14 11:40:02.434562', 1, '2026-04-14 11:40:02.4375', 0, 'f');
INSERT INTO "public"."iam_user_info" VALUES (2043973456939393024, '测试商户管理员', 'merchant', 'cssh001', '$2a$10$5E8KeFArn.ptHv.kiL44jeHWJRFR6BXDGRBe7glrmIICKIGFAJUHu', NULL, NULL, 'f', 'normal', 1, '2026-04-14 16:43:41.984147', 1, '2026-04-14 16:43:42.042642', 0, 'f');
INSERT INTO "public"."iam_user_info" VALUES (2047692453195206656, '测试商户1管理员', 'merchant', 'cssh01', '$2a$10$fTr6Jp73WNunXMt7gVdK.ukC0dz9PgBZAd9d9gT82apMHtPVoqgmy', NULL, NULL, 'f', 'normal', 1, '2026-04-24 23:01:39.787363', 1, '2026-04-24 23:01:39.791925', 0, 'f');
INSERT INTO "public"."iam_user_info" VALUES (2048608994656964608, 'cs002', 'admin', 'cs0002', '$2a$10$6rfJaOkcKhdhzyBZxODCVuJa4CWezY3eCcGTsSkM7sfRgdWeP9mcK', NULL, NULL, 'f', 'normal', 1, '2026-04-27 11:43:40.299805', 1, '2026-04-27 11:43:40.30584', 0, 'f');
INSERT INTO "public"."iam_user_info" VALUES (2048950207985635328, '测试运营商2管理员', 'isv', 'csyys2', '$2a$10$t4XrcQy3QTWs/oO02mhagejSV1fwaYKNjBfMLFq2UsZnYlcsMkGiK', NULL, NULL, 'f', 'normal', 1, '2026-04-28 10:19:31.894011', 1, '2026-04-28 10:19:31.894011', 0, 'f');
INSERT INTO "public"."iam_user_info" VALUES (2050845636545449984, '测试小微管理员', 'merchant', 'csxwsh', '$2a$10$hzbsBWvWSRvPaR574QCL8euffLjvvmw0Cg.pGKcmaYQvZXhVlIUTK', NULL, NULL, 'f', 'normal', 1, '2026-05-03 15:51:17.281075', 1, '2026-05-03 15:51:17.36229', 0, 'f');
INSERT INTO "public"."iam_user_info" VALUES (2050857563292131328, '测试个体管理员', 'merchant', 'csgtgsh', '$2a$10$JC15YWz17aPG12jp.LiXi.evQo346LGXsEq0m9ozSf4V0HVDB794S', NULL, NULL, 'f', 'normal', 1, '2026-05-03 16:38:40.838459', 1, '2026-05-03 16:38:40.842616', 0, 'f');
INSERT INTO "public"."iam_user_info" VALUES (1, '超级管理员', 'admin', 'bootx', '$2a$10$b1ODwTps4YY0Tu7oR0LaAOlSTaxXmT1VGs2ge9ISEmbF3tN0nknLW', NULL, NULL, 't', 'normal', 0, '2026-03-28 22:26:08', 1, '2026-05-04 22:41:40.947361', 2, 'f');
INSERT INTO "public"."iam_user_info" VALUES (2042248310121746432, 'isv管理员', 'isv', 'bootx1', '$2a$10$AR4YfJJ3zmKG9fsd1lbQG.vGwjTk/qL0auGQt.5tEpviy52jmvp4K', '', '', 'f', 'normal', 1, '2026-04-09 22:28:34.92874', 1, '2026-05-04 23:02:28.601522', 4, 'f');

-- ----------------------------
-- Table structure for iam_user_password_history
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_password_history";
CREATE TABLE "public"."iam_user_password_history" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "password" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."iam_user_password_history"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_password_history"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."iam_user_password_history"."password" IS '密码';
COMMENT ON COLUMN "public"."iam_user_password_history"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_user_password_history"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."iam_user_password_history" IS '用户密码历史表';

-- ----------------------------
-- Records of iam_user_password_history
-- ----------------------------
INSERT INTO "public"."iam_user_password_history" VALUES (2042248310180466688, 2042248310121746432, '$2a$10$AR4YfJJ3zmKG9fsd1lbQG.vGwjTk/qL0auGQt.5tEpviy52jmvp4K', 1, '2026-04-09 22:28:34.941069');
INSERT INTO "public"."iam_user_password_history" VALUES (2043581433371631616, 2039557567951310848, '$2a$10$2YU69WcXx86BHe.fvycrRuuC6QmFE21DCyKcB65sgmkCI9mjZMnuK', 1, '2026-04-13 14:45:56.278278');
INSERT INTO "public"."iam_user_password_history" VALUES (2043895568558936064, 2043895568185643008, '$2a$10$KQtswyZldFrx18hsB3hnDevZXAHhtym23eszDr4lZ34FjCJgggiDK', 1, '2026-04-14 11:34:11.943292');
INSERT INTO "public"."iam_user_password_history" VALUES (2043897038675070976, 2043897038620545024, '$2a$10$2pE6H4Z8TOjNRMMV2Axuw.p/AcTFA4HKc6T.1PYXnLGOA.FO9ESN6', 1, '2026-04-14 11:40:02.447096');
INSERT INTO "public"."iam_user_password_history" VALUES (2043973457346240512, 2043973456939393024, '$2a$10$5E8KeFArn.ptHv.kiL44jeHWJRFR6BXDGRBe7glrmIICKIGFAJUHu', 1, '2026-04-14 16:43:42.078521');
INSERT INTO "public"."iam_user_password_history" VALUES (2047692453325230080, 2047692453195206656, '$2a$10$fTr6Jp73WNunXMt7gVdK.ukC0dz9PgBZAd9d9gT82apMHtPVoqgmy', 1, '2026-04-24 23:01:39.815264');
INSERT INTO "public"."iam_user_password_history" VALUES (2048608994803765248, 2048608994656964608, '$2a$10$6rfJaOkcKhdhzyBZxODCVuJa4CWezY3eCcGTsSkM7sfRgdWeP9mcK', 1, '2026-04-27 11:43:40.331787');
INSERT INTO "public"."iam_user_password_history" VALUES (2048950208040161280, 2048950207985635328, '$2a$10$t4XrcQy3QTWs/oO02mhagejSV1fwaYKNjBfMLFq2UsZnYlcsMkGiK', 1, '2026-04-28 10:19:31.907856');
INSERT INTO "public"."iam_user_password_history" VALUES (2050845636994240512, 2050845636545449984, '$2a$10$hzbsBWvWSRvPaR574QCL8euffLjvvmw0Cg.pGKcmaYQvZXhVlIUTK', 1, '2026-05-03 15:51:17.384249');
INSERT INTO "public"."iam_user_password_history" VALUES (2050857563417960448, 2050857563292131328, '$2a$10$JC15YWz17aPG12jp.LiXi.evQo346LGXsEq0m9ozSf4V0HVDB794S', 1, '2026-05-03 16:38:40.864548');
INSERT INTO "public"."iam_user_password_history" VALUES (2051222393840705536, 2051222393719070720, '$2a$10$adE2SwUw7dRkghvMcGQplupneWyb0F8YXXnkQRhED4kmrD.NrZeVu', 1, '2026-05-04 16:48:23.215499');
INSERT INTO "public"."iam_user_password_history" VALUES (2051311303631495168, 1, '$2a$10$b1ODwTps4YY0Tu7oR0LaAOlSTaxXmT1VGs2ge9ISEmbF3tN0nknLW', 1, '2026-05-04 22:41:40.963064');

-- ----------------------------
-- Table structure for iam_user_password_security
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_password_security";
CREATE TABLE "public"."iam_user_password_security" (
  "id" int8 NOT NULL,
  "password_error_count" int4,
  "lock_time" timestamp(6),
  "password_expire_time" timestamp(6),
  "last_change_password_time" timestamp(6),
  "initial_password" bool DEFAULT false,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "last_failure_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."iam_user_password_security"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_password_security"."password_error_count" IS '密码错误次数';
COMMENT ON COLUMN "public"."iam_user_password_security"."lock_time" IS '锁定结束时间';
COMMENT ON COLUMN "public"."iam_user_password_security"."password_expire_time" IS '密码过期时间';
COMMENT ON COLUMN "public"."iam_user_password_security"."last_change_password_time" IS '上次修改密码时间';
COMMENT ON COLUMN "public"."iam_user_password_security"."initial_password" IS '是否初始密码';
COMMENT ON COLUMN "public"."iam_user_password_security"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_user_password_security"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_password_security"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_user_password_security"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_password_security"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_user_password_security"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_user_password_security"."last_failure_time" IS '上次登录失败时间';
COMMENT ON TABLE "public"."iam_user_password_security" IS '用户密码安全信息';

-- ----------------------------
-- Records of iam_user_password_security
-- ----------------------------
INSERT INTO "public"."iam_user_password_security" VALUES (2043973456939393024, 0, NULL, '2026-07-13 16:43:42.105304', '2026-04-14 16:43:42.105304', 't', 1, '2026-04-14 16:43:42.106304', 1, '2026-04-14 16:43:42.107305', 0, 'f', NULL);
INSERT INTO "public"."iam_user_password_security" VALUES (2047692453195206656, 0, NULL, '2026-07-23 23:01:39.830891', '2026-04-24 23:01:39.831896', 't', 1, '2026-04-24 23:01:39.833428', 1, '2026-04-24 23:01:39.833428', 0, 'f', NULL);
INSERT INTO "public"."iam_user_password_security" VALUES (2048608994656964608, 0, NULL, '2026-07-26 11:43:40.349573', '2026-04-27 11:43:40.350572', 't', 1, '2026-04-27 11:43:40.353079', 1, '2026-04-27 11:43:40.353079', 0, 'f', NULL);
INSERT INTO "public"."iam_user_password_security" VALUES (2050845636545449984, 0, NULL, '2026-08-01 15:51:17.404422', '2026-05-03 15:51:17.404422', 't', 1, '2026-05-03 15:51:17.40643', 1, '2026-05-03 15:51:17.406933', 0, 'f', NULL);
INSERT INTO "public"."iam_user_password_security" VALUES (2050857563292131328, 0, NULL, '2026-08-01 16:38:40.879201', '2026-05-03 16:38:40.880706', 't', 1, '2026-05-03 16:38:40.882214', 1, '2026-05-03 16:38:40.882214', 0, 'f', NULL);
INSERT INTO "public"."iam_user_password_security" VALUES (1, 0, NULL, '2026-08-02 22:41:40.978408', '2026-05-04 22:41:40.982139', 'f', 0, '2026-03-28 22:26:08', 0, '2026-03-28 22:26:00', 0, 'f', NULL);

-- ----------------------------
-- Table structure for iam_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_role";
CREATE TABLE "public"."iam_user_role" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."iam_user_role"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_role"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."iam_user_role"."role_id" IS '角色ID';
COMMENT ON TABLE "public"."iam_user_role" IS '用户角色关系';

-- ----------------------------
-- Records of iam_user_role
-- ----------------------------
INSERT INTO "public"."iam_user_role" VALUES (2039668364800045056, 2039557567951310848, 2034473713154596864);
INSERT INTO "public"."iam_user_role" VALUES (2042248310406959104, 2042248310121746432, 1928374650192837467);
INSERT INTO "public"."iam_user_role" VALUES (2043895578373607424, 2043895568185643008, 1928374650192837465);
INSERT INTO "public"."iam_user_role" VALUES (2043897038830260224, 2043897038620545024, 1928374650192837465);
INSERT INTO "public"."iam_user_role" VALUES (2043973457748893696, 2043973456939393024, 1928374650192837466);
INSERT INTO "public"."iam_user_role" VALUES (2047692453635608576, 2047692453195206656, 1928374650192837466);
INSERT INTO "public"."iam_user_role" VALUES (2048950208354734080, 2048950207985635328, 1928374650192837467);
INSERT INTO "public"."iam_user_role" VALUES (2050845637350756352, 2050845636545449984, 1928374650192837466);
INSERT INTO "public"."iam_user_role" VALUES (2050857563698978816, 2050857563292131328, 1928374650192837466);
INSERT INTO "public"."iam_user_role" VALUES (2051222394050420736, 2051222393719070720, 1928374650192837465);

-- ----------------------------
-- Table structure for mch_alipay_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_alipay_channel_merchant";
CREATE TABLE "public"."mch_alipay_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "alipay_user_id" varchar(32) COLLATE "pg_catalog"."default",
  "agent_no" varchar(32) COLLATE "pg_catalog"."default",
  "isv_no" varchar(32) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "app_auth_token" varchar(128) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "isv_app_id" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."alipay_user_id" IS '支付宝商家唯一识别码(2088开头的16位数字)';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."app_auth_token" IS '应用授权令牌';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."mch_alipay_channel_merchant"."isv_app_id" IS '支付宝服务商应用ID（aliAppId）';
COMMENT ON TABLE "public"."mch_alipay_channel_merchant" IS '支付宝通道商户配置';

-- ----------------------------
-- Records of mch_alipay_channel_merchant
-- ----------------------------
INSERT INTO "public"."mch_alipay_channel_merchant" VALUES (2052586166228807680, 'M1777797520668', 'alipay_isv', '1123123123', NULL, NULL, 1, '2026-05-08 11:07:31.892896', 1, '2026-05-08 11:07:31.892896', 0, 'f', NULL, NULL, NULL);
INSERT INTO "public"."mch_alipay_channel_merchant" VALUES (2061276885198024704, 'M1777797520668', 'alipay_isv', '11123', NULL, NULL, 1, '2026-06-01 10:41:20.762436', 1, '2026-06-01 10:41:20.762436', 0, 'f', '1111111', NULL, NULL);
INSERT INTO "public"."mch_alipay_channel_merchant" VALUES (2063822922399784960, 'M1777797520668', 'alipay_isv', '123', NULL, NULL, 1, '2026-06-08 11:18:23.333338', 1, '2026-06-08 11:18:23.333338', 0, 'f', '12312123', '2063822922345259008', '2021004161670629');
INSERT INTO "public"."mch_alipay_channel_merchant" VALUES (2063884528299151360, 'M1777797520668', 'alipay_isv', '123', NULL, NULL, 1, '2026-06-08 15:23:11.325412', 1, '2026-06-08 15:23:11.325412', 0, 'f', '123', '2063884527858749440', '2021004161670629');
INSERT INTO "public"."mch_alipay_channel_merchant" VALUES (2065319805467107328, 'M1777797520668', 'alipay', '111111', NULL, NULL, 1, '2026-06-12 14:26:28.068347', 1, '2026-06-12 14:26:28.068347', 0, 'f', NULL, '111111', NULL);

-- ----------------------------
-- Table structure for mch_app_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_app_info";
CREATE TABLE "public"."mch_app_info" (
  "id" int8 NOT NULL,
  "isv_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "agent_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "default_app" bool NOT NULL DEFAULT false,
  "notify_url" varchar(512) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_app_info"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_app_info"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."mch_app_info"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."mch_app_info"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_app_info"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."mch_app_info"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."mch_app_info"."status" IS '应用状态，字典 mch_app_status';
COMMENT ON COLUMN "public"."mch_app_info"."default_app" IS '是否默认应用';
COMMENT ON COLUMN "public"."mch_app_info"."notify_url" IS '通知地址';
COMMENT ON COLUMN "public"."mch_app_info"."creator" IS '创建者';
COMMENT ON COLUMN "public"."mch_app_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_app_info"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."mch_app_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_app_info"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_app_info"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."mch_app_info" IS '商户应用信息';

-- ----------------------------
-- Records of mch_app_info
-- ----------------------------
INSERT INTO "public"."mch_app_info" VALUES (2058848500786089984, 'ISV1215972714557722', 'AGENT1776138002345', 'M1777797520668', 'A0187406187594024', 'cs1', 'enable', 'f', NULL, 1, '2026-05-25 17:51:48.799839', 1, '2026-05-25 17:51:48.803277', 0, 'f');
INSERT INTO "public"."mch_app_info" VALUES (2058848213685981184, 'ISV1215972714557722', 'AGENT1776138002345', 'M1777797520668', 'A4322396916898690', 'cs', 'enable', 'f', NULL, 1, '2026-05-25 17:50:40.349128', 1, '2026-05-25 18:07:39.317252', 5, 'f');
INSERT INTO "public"."mch_app_info" VALUES (2058848487011995648, 'ISV1215972714557722', 'AGENT1776138002345', 'M1777797520668', 'A1046452901837665', 'cs2', 'enable', 't', NULL, 1, '2026-05-25 17:51:45.515197', 1, '2026-05-25 23:28:55.847292', 3, 'f');
INSERT INTO "public"."mch_app_info" VALUES (2058765412206825472, 'ISV1215972714557722', 'AGENT1776137651692', 'M1777794676556', 'A3068473540233729', '123', 'enable', 'f', NULL, 1, '2026-05-25 12:21:38.938132', 1, '2026-05-25 12:21:38.941131', 0, 'f');
INSERT INTO "public"."mch_app_info" VALUES (2058765489356853248, 'ISV1215972714557722', 'AGENT1776137651692', 'M1777794676556', 'A2922692313523250', '111', 'enable', 'f', NULL, 1, '2026-05-25 12:21:57.332558', 1, '2026-05-25 12:22:28.371514', 1, 'f');
INSERT INTO "public"."mch_app_info" VALUES (2058765499112804352, 'ISV1215972714557722', 'AGENT1776137651692', 'M1777794676556', 'A1334212725027809', '你好', 'enable', 'f', NULL, 1, '2026-05-25 12:21:59.65871', 1, '2026-05-25 12:27:18.865084', 3, 'f');
INSERT INTO "public"."mch_app_info" VALUES (2058765509552427008, 'ISV1215972714557722', 'AGENT1776137651692', 'M1777794676556', 'A3585792974162061', '222', 'enable', 'f', NULL, 1, '2026-05-25 12:22:02.147453', 1, '2026-05-25 12:22:02.150963', 0, 'f');
INSERT INTO "public"."mch_app_info" VALUES (2058766322685366272, 'ISV1215972714557722', 'AGENT1776137651692', 'M1777794676556', 'A1691594098578122', 'xxxx', 'enable', 'f', NULL, 1, '2026-05-25 12:25:16.01326', 1, '2026-05-25 12:25:16.017463', 0, 'f');
INSERT INTO "public"."mch_app_info" VALUES (2058766338959265792, 'ISV1215972714557722', 'AGENT1776137651692', 'M1777794676556', 'A6012816229287609', 'aaaa', 'enable', 'f', NULL, 1, '2026-05-25 12:25:19.89303', 1, '2026-05-25 12:25:19.896257', 0, 'f');
INSERT INTO "public"."mch_app_info" VALUES (2058762757141090304, 'ISV1215972714557722', 'AGENT1776137651692', 'M1777794676556', 'A6787850981390709', '123', 'enable', 't', NULL, 1, '2026-05-25 12:11:05.925183', 1, '2026-05-25 17:32:41.590375', 5, 'f');

-- ----------------------------
-- Table structure for mch_bank_card_profile
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_bank_card_profile";
CREATE TABLE "public"."mch_bank_card_profile" (
  "id" int8 NOT NULL,
  "isv_no" varchar(32) COLLATE "pg_catalog"."default",
  "agent_no" varchar(32) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "account_type" varchar(32) COLLATE "pg_catalog"."default",
  "account_name" varchar(64) COLLATE "pg_catalog"."default",
  "card_no" varchar(32) COLLATE "pg_catalog"."default",
  "bank_name" varchar(128) COLLATE "pg_catalog"."default",
  "branch_no" varchar(32) COLLATE "pg_catalog"."default",
  "bank_phone" varchar(32) COLLATE "pg_catalog"."default",
  "card_front_pic" varchar(256) COLLATE "pg_catalog"."default",
  "card_back_pic" varchar(256) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_bank_card_profile"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."account_type" IS '银行账户类型';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."account_name" IS '银行卡账户名';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."card_no" IS '银行卡号';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."bank_name" IS '银行卡开户行名称';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."branch_no" IS '银行卡开户行联行号';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."bank_phone" IS '银行预留手机号';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."card_front_pic" IS '银行卡正面照片';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."card_back_pic" IS '银行卡反面照片';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_bank_card_profile"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."mch_bank_card_profile" IS '商户结算卡信息';

-- ----------------------------
-- Records of mch_bank_card_profile
-- ----------------------------

-- ----------------------------
-- Table structure for mch_base_profile
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_base_profile";
CREATE TABLE "public"."mch_base_profile" (
  "id" int8 NOT NULL,
  "isv_no" varchar(32) COLLATE "pg_catalog"."default",
  "agent_no" varchar(32) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "contact_name" varchar(64) COLLATE "pg_catalog"."default",
  "contact_phone" varchar(32) COLLATE "pg_catalog"."default",
  "contact_email" varchar(128) COLLATE "pg_catalog"."default",
  "province_code" varchar(16) COLLATE "pg_catalog"."default",
  "city_code" varchar(16) COLLATE "pg_catalog"."default",
  "address" varchar(256) COLLATE "pg_catalog"."default",
  "remark" varchar(512) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_base_profile"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_base_profile"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."mch_base_profile"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."mch_base_profile"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_base_profile"."contact_name" IS '联系人姓名';
COMMENT ON COLUMN "public"."mch_base_profile"."contact_phone" IS '联系电话';
COMMENT ON COLUMN "public"."mch_base_profile"."contact_email" IS '联系邮箱';
COMMENT ON COLUMN "public"."mch_base_profile"."province_code" IS '省份编码';
COMMENT ON COLUMN "public"."mch_base_profile"."city_code" IS '城市编码';
COMMENT ON COLUMN "public"."mch_base_profile"."address" IS '详细地址';
COMMENT ON COLUMN "public"."mch_base_profile"."remark" IS '备注';
COMMENT ON COLUMN "public"."mch_base_profile"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_base_profile"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_base_profile"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."mch_base_profile"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_base_profile"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_base_profile"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."mch_base_profile" IS '商户基础资料';

-- ----------------------------
-- Records of mch_base_profile
-- ----------------------------

-- ----------------------------
-- Table structure for mch_credential
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_credential";
CREATE TABLE "public"."mch_credential" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "isv_no" varchar(32) COLLATE "pg_catalog"."default",
  "agent_no" varchar(32) COLLATE "pg_catalog"."default",
  "public_key" text COLLATE "pg_catalog"."default",
  "secret_key" text COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_credential"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_credential"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_credential"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."mch_credential"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."mch_credential"."public_key" IS '商户公钥(加密存储)';
COMMENT ON COLUMN "public"."mch_credential"."secret_key" IS '通信密钥(加密存储)';
COMMENT ON COLUMN "public"."mch_credential"."creator" IS '创建者';
COMMENT ON COLUMN "public"."mch_credential"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_credential"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."mch_credential"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_credential"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_credential"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."mch_credential" IS '商户对接配置';

-- ----------------------------
-- Records of mch_credential
-- ----------------------------
INSERT INTO "public"."mch_credential" VALUES (2050846465423806464, 'M1777794676556', 'ISV1215972714557722', 'AGENT1776137651692', 'v1:kl/Hg0D92VZH0LR9aalAiyZKfJczlHhGQsA6979SNMCEXALhOndbUP8YlmNBHlQSxIcyx4LXEW/GQiJ6QMkoVNrZBt0/bAlfOMSwPQ/z1zZHDyazuvwH65qFoFrUwbx76E6K3CLyC2YPBCYSQfdViyKEsmMzjNO4yQ1KafqXm14xWkKzSumn/qHZutFbwf3albCkcbI7HnV4QBI+ld2QC8sj/TgIQbFmilpl9vh8lPTCS/m+RfAwIosTSc4x6lX9lhwSaVT4imhWS1p2ShUrwOZXcQwX6gC/izKfK775VTSb+WCFc2vffvjazmuwDBCYffJH/JgkGUJhLIEiQYpBC4Exad7q3e52nDF/vRDOFgUOBPd/BuZaFKAQRa7F2+uDSVWGuQEu1dw6tAXFjb1zvvjrr1/Q0pmBYbaZpy45CD1rrFd1UlSFmhgUxfkZbuApDdYXa1k0CUsmqxRFBW2SVsgLiJ/VVetGJVi0sUP4gQ9/her0AzC9OLYkXNegP724WQZI0XPP/lt7SyVRGh5ysbidEbVzQsOIRtLPcYWWNRHVqMq7hnkegbbffFNwTJNP+E1yYnAxjHTj3Y3lVX8It2h59DNQVxurnoAfg0GDUhxlpglvOXJjwrCNZydfACE=', 'v1:UH6cUi6T55pbdUe+2riu2523iNoOnhzuBRJBcWARqP/FqrHprDha2bFFlVGnFLgj+R2M5oE3Nu/AGmPs', 1, '2026-05-03 15:54:34.898449', 1, '2026-05-03 22:23:32.23013', 1, 'f');
INSERT INTO "public"."mch_credential" VALUES (2058739653031489536, 'M1777797520668', 'ISV1215972714557722', 'AGENT1776138002345', NULL, NULL, 1, '2026-05-25 10:39:17.476296', 1, '2026-05-25 10:39:17.481813', 0, 'f');

-- ----------------------------
-- Table structure for mch_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_info";
CREATE TABLE "public"."mch_info" (
  "id" int8 NOT NULL,
  "isv_no" varchar(32) COLLATE "pg_catalog"."default",
  "agent_no" varchar(32) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "mch_name" varchar(128) COLLATE "pg_catalog"."default",
  "mch_short_name" varchar(64) COLLATE "pg_catalog"."default",
  "admin_user_id" int8,
  "status" varchar(32) COLLATE "pg_catalog"."default",
  "subject_type" varchar(32) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."mch_info"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_info"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."mch_info"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."mch_info"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_info"."mch_name" IS '商户名称';
COMMENT ON COLUMN "public"."mch_info"."mch_short_name" IS '商户简称';
COMMENT ON COLUMN "public"."mch_info"."admin_user_id" IS '关联管理员用户ID';
COMMENT ON COLUMN "public"."mch_info"."status" IS '状态';
COMMENT ON COLUMN "public"."mch_info"."subject_type" IS '主体类型';
COMMENT ON COLUMN "public"."mch_info"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."mch_info"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_info"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."mch_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_info"."version" IS '版本号(乐观锁)';
COMMENT ON TABLE "public"."mch_info" IS '商户信息表';

-- ----------------------------
-- Records of mch_info
-- ----------------------------
INSERT INTO "public"."mch_info" VALUES (2050857563791253504, 'ISV1215972714557722', 'AGENT1776138002345', 'M1777797520668', '测试个体', '测试个体', 2050857563292131328, 'enable', 'individual', 'f', 1, '2026-05-03 16:38:40.95515', 1, '2026-05-03 16:38:40.944756', 1);
INSERT INTO "public"."mch_info" VALUES (2050845637472391168, 'ISV1215972714557722', 'AGENT1776137651692', 'M1777794676556', '测试小微', '测试小微', 2050845636545449984, 'disabled', 'micro', 'f', 1, '2026-05-03 15:51:17.499964', 1, '2026-05-03 23:46:10.811457', 2);

-- ----------------------------
-- Table structure for mch_lakala_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_lakala_channel_merchant";
CREATE TABLE "public"."mch_lakala_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "term_no" varchar(32) COLLATE "pg_catalog"."default",
  "agent_no" varchar(32) COLLATE "pg_catalog"."default",
  "isv_no" varchar(32) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."term_no" IS '终端号';
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_lakala_channel_merchant"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."mch_lakala_channel_merchant" IS '拉卡拉通道商户配置';

-- ----------------------------
-- Records of mch_lakala_channel_merchant
-- ----------------------------

-- ----------------------------
-- Table structure for mch_product_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_product_config";
CREATE TABLE "public"."mch_product_config" (
  "id" int8 NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "enable" bool DEFAULT false,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "agent_no" varchar(32) COLLATE "pg_catalog"."default",
  "isv_no" varchar(32) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_product_config"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_product_config"."product" IS '产品编码';
COMMENT ON COLUMN "public"."mch_product_config"."channel" IS '通道编码';
COMMENT ON COLUMN "public"."mch_product_config"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."mch_product_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_product_config"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."mch_product_config"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."mch_product_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_product_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_product_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."mch_product_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_product_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_product_config"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."mch_product_config" IS '商户产品配置';

-- ----------------------------
-- Records of mch_product_config
-- ----------------------------
INSERT INTO "public"."mch_product_config" VALUES (2051331183856517120, 'alipay_isv', 'alipay', 't', 'M1777797520668', NULL, NULL, 1, '2026-05-05 00:00:40.782649', 1, '2026-05-05 00:00:40.838356', 0, 'f');
INSERT INTO "public"."mch_product_config" VALUES (2051331192400314368, 'wechat_isv', 'wechat', 't', 'M1777797520668', NULL, NULL, 1, '2026-05-05 00:00:42.815935', 1, '2026-05-05 00:00:42.818831', 0, 'f');
INSERT INTO "public"."mch_product_config" VALUES (2052045455767011328, 'ums_qrcode', 'ums_pay', 't', 'M1777797520668', NULL, NULL, 1, '2026-05-06 23:18:56.472518', 1, '2026-05-06 23:18:56.475549', 0, 'f');
INSERT INTO "public"."mch_product_config" VALUES (2052045463513890816, 'lakala_pay', 'lakala_pay', 't', 'M1777797520668', NULL, NULL, 1, '2026-05-06 23:18:58.319149', 1, '2026-05-06 23:18:58.322184', 0, 'f');
INSERT INTO "public"."mch_product_config" VALUES (2052045473706049536, 'ums_barcode', 'ums_pay', 't', 'M1777797520668', NULL, NULL, 1, '2026-05-06 23:19:00.749365', 1, '2026-05-06 23:19:00.755911', 0, 'f');
INSERT INTO "public"."mch_product_config" VALUES (2052248784778547200, 'ums_jsapi', 'ums_pay', 't', 'M1777797520668', NULL, NULL, 1, '2026-05-07 12:46:53.891993', 1, '2026-05-07 12:46:53.896511', 0, 'f');
INSERT INTO "public"."mch_product_config" VALUES (2058917881834586112, 'ums_app', 'ums_pay', 't', 'M1777797520668', NULL, NULL, 1, '2026-05-25 22:27:30.53259', 1, '2026-05-25 22:27:30.536595', 0, 'f');
INSERT INTO "public"."mch_product_config" VALUES (2058917892488118272, 'ums_mini', 'ums_pay', 't', 'M1777797520668', NULL, NULL, 1, '2026-05-25 22:27:33.070513', 1, '2026-05-25 22:27:33.074832', 0, 'f');
INSERT INTO "public"."mch_product_config" VALUES (2058917899496800256, 'ums_h5', 'ums_pay', 't', 'M1777797520668', NULL, NULL, 1, '2026-05-25 22:27:34.741507', 1, '2026-05-25 22:27:34.745509', 0, 'f');
INSERT INTO "public"."mch_product_config" VALUES (2065272719199625216, 'alipay', 'alipay', 't', 'M1777797520668', NULL, NULL, 1, '2026-06-12 11:19:21.827692', 1, '2026-06-12 11:19:21.832894', 0, 'f');
INSERT INTO "public"."mch_product_config" VALUES (2065272733338624000, 'douyin_pay', 'douyin_pay', 't', 'M1777797520668', NULL, NULL, 1, '2026-06-12 11:19:25.196981', 1, '2026-06-12 11:19:25.200013', 0, 'f');
INSERT INTO "public"."mch_product_config" VALUES (2065272744742936576, 'wechat_pay', 'wechat', 't', 'M1777797520668', NULL, NULL, 1, '2026-06-12 11:19:27.915758', 1, '2026-06-12 11:19:27.919452', 0, 'f');

-- ----------------------------
-- Table structure for mch_ums_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_ums_channel_merchant";
CREATE TABLE "public"."mch_ums_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "ums_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "app_key" varchar(256) COLLATE "pg_catalog"."default",
  "merchant_no" varchar(32) COLLATE "pg_catalog"."default",
  "terminal_no" varchar(32) COLLATE "pg_catalog"."default",
  "order_prefix" varchar(16) COLLATE "pg_catalog"."default",
  "secret_key" varchar(256) COLLATE "pg_catalog"."default",
  "sandbox" bool DEFAULT false,
  "agent_no" varchar(32) COLLATE "pg_catalog"."default",
  "isv_no" varchar(32) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."ums_app_id" IS '银联商务应用ID';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."app_key" IS '银联商务应用密钥(加密存储)';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."merchant_no" IS '银联商务商户号';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."terminal_no" IS '银联商务终端号';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."order_prefix" IS '订单号前缀';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."secret_key" IS '通信密钥(加密存储)';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."sandbox" IS '是否沙箱环境';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_ums_channel_merchant"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."mch_ums_channel_merchant" IS '银联商务通道商户配置';

-- ----------------------------
-- Records of mch_ums_channel_merchant
-- ----------------------------

-- ----------------------------
-- Table structure for mch_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_user";
CREATE TABLE "public"."mch_user" (
  "id" int8 NOT NULL DEFAULT nextval('mch_user_id_seq'::regclass),
  "user_id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "administrator" bool DEFAULT false,
  "creator" int8,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."mch_user"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."mch_user"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_user"."administrator" IS '是否管理员';
COMMENT ON COLUMN "public"."mch_user"."creator" IS '创建者';
COMMENT ON COLUMN "public"."mch_user"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."mch_user" IS '商户用户关联表';

-- ----------------------------
-- Records of mch_user
-- ----------------------------

-- ----------------------------
-- Table structure for mch_wechat_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_wechat_channel_merchant";
CREATE TABLE "public"."mch_wechat_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "sub_mch_id" varchar(32) COLLATE "pg_catalog"."default",
  "sub_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "agent_no" varchar(32) COLLATE "pg_catalog"."default",
  "isv_no" varchar(32) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."sub_mch_id" IS '微信特约商户号/二级商户号';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."sub_app_id" IS '微信特约商户/二级商户AppId';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."mch_wechat_channel_merchant"."channel_mch_no" IS '通道商户号';
COMMENT ON TABLE "public"."mch_wechat_channel_merchant" IS '微信通道商户配置';

-- ----------------------------
-- Records of mch_wechat_channel_merchant
-- ----------------------------
INSERT INTO "public"."mch_wechat_channel_merchant" VALUES (2063824872902139904, 'M1777797520668', 'wechat_isv', '123123', NULL, NULL, NULL, 1, '2026-06-08 11:26:08.369594', 1, '2026-06-08 11:26:08.369594', 0, 'f', NULL);
INSERT INTO "public"."mch_wechat_channel_merchant" VALUES (2063884572645527552, 'M1777797520668', 'wechat_isv', '111111', NULL, NULL, NULL, 1, '2026-06-08 15:23:21.898463', 1, '2026-06-08 15:23:21.898463', 0, 'f', '111111');

-- ----------------------------
-- Table structure for pay_capability
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_capability";
CREATE TABLE "public"."pay_capability" (
  "id" int8 NOT NULL,
  "code" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4 NOT NULL DEFAULT 0,
  "enabled" bool NOT NULL DEFAULT true,
  "description" varchar(512) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."pay_capability"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_capability"."code" IS '支付能力编码（全局唯一）';
COMMENT ON COLUMN "public"."pay_capability"."sort_no" IS '全局排序';
COMMENT ON COLUMN "public"."pay_capability"."enabled" IS '是否启用';
COMMENT ON COLUMN "public"."pay_capability"."description" IS '说明';
COMMENT ON COLUMN "public"."pay_capability"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_capability"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_capability"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_capability"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."pay_capability"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_capability"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."pay_capability" IS '支付能力主数据（code 对齐 PayCapabilityEnum，展示名走 enum i18n）';

-- ----------------------------
-- Records of pay_capability
-- ----------------------------
INSERT INTO "public"."pay_capability" VALUES (5001, 'aggregate_pay_qrcode', 0, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5002, 'aggregate_pay_barcode', 1, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5003, 'wechat_cashier', 2, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5004, 'wechat_jsapi', 3, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5005, 'wechat_app', 4, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5006, 'wechat_h5', 5, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5007, 'wechat_qr', 6, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5008, 'wechat_mini', 7, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5009, 'wechat_barcode', 8, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5010, 'alipay_barcode', 9, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5011, 'alipay_order_qr', 10, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5012, 'alipay_app', 11, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5013, 'alipay_h5', 12, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5014, 'alipay_pc', 13, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5015, 'alipay_jsapi', 14, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5016, 'union_pay_qr', 15, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5017, 'union_pay_barcode', 16, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5018, 'union_pay_h5', 17, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5019, 'union_pay_jsapi', 18, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5020, 'visa_card_gateway', 19, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5021, 'visa_card_present', 20, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5022, 'mastercard_card_gateway', 21, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_capability" VALUES (5023, 'mastercard_card_present', 22, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');

-- ----------------------------
-- Table structure for pay_channel
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_channel";
CREATE TABLE "public"."pay_channel" (
  "id" int8 NOT NULL,
  "code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4 DEFAULT 0,
  "description" varchar(500) COLLATE "pg_catalog"."default",
  "icon" varchar(200) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_channel"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_channel"."code" IS '通道编码';
COMMENT ON COLUMN "public"."pay_channel"."sort_no" IS '排序';
COMMENT ON COLUMN "public"."pay_channel"."description" IS '通道介绍';
COMMENT ON COLUMN "public"."pay_channel"."icon" IS '图标';
COMMENT ON COLUMN "public"."pay_channel"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_channel"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_channel"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_channel"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_channel"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_channel"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."pay_channel" IS '支付通道';

-- ----------------------------
-- Records of pay_channel
-- ----------------------------
INSERT INTO "public"."pay_channel" VALUES (1, 'alipay', 1, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');
INSERT INTO "public"."pay_channel" VALUES (3, 'wechat', 3, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');
INSERT INTO "public"."pay_channel" VALUES (6, 'leshua_pay', 6, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');
INSERT INTO "public"."pay_channel" VALUES (7, 'vbill_pay', 7, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');
INSERT INTO "public"."pay_channel" VALUES (8, 'lakala_pay', 8, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');
INSERT INTO "public"."pay_channel" VALUES (5, 'ums_pay', 5, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');

-- ----------------------------
-- Table structure for pay_method
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_method";
CREATE TABLE "public"."pay_method" (
  "id" int8 NOT NULL,
  "code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4 NOT NULL DEFAULT 0,
  "description" varchar(512) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."pay_method"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_method"."code" IS '支付方式编码（全局唯一）';
COMMENT ON COLUMN "public"."pay_method"."sort_no" IS '全局排序';
COMMENT ON COLUMN "public"."pay_method"."description" IS '说明';
COMMENT ON COLUMN "public"."pay_method"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_method"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_method"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_method"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."pay_method"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_method"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."pay_method" IS '支付方式';

-- ----------------------------
-- Records of pay_method
-- ----------------------------
INSERT INTO "public"."pay_method" VALUES (502003001, 'aggregate_pay_qrcode', 1, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003002, 'aggregate_pay_barcode', 2, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003003, 'wechat_cashier', 3, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003004, 'wechat_qr', 4, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003005, 'wechat_jsapi', 5, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003006, 'wechat_mini', 6, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003007, 'wechat_h5', 7, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003008, 'wechat_app', 8, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003009, 'wechat_barcode', 9, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003010, 'alipay_qr', 10, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003012, 'alipay_jsapi', 12, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003013, 'alipay_mini', 13, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003014, 'alipay_pc', 14, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003015, 'alipay_h5', 15, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003016, 'alipay_app', 16, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003017, 'alipay_barcode', 17, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003018, 'union_qr', 18, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003019, 'union_jsapi', 19, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003020, 'union_h5', 20, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003021, 'union_pay_barcode', 21, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003022, 'visa_card_gateway', 22, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003023, 'visa_card_present', 23, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003024, 'mastercard_card_gateway', 24, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003025, 'mastercard_card_present', 25, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003026, 'other', 26, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_method" VALUES (502003011, 'alipay_order_qr', 12, '支付宝订单码', 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');

-- ----------------------------
-- Table structure for pay_product
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_product";
CREATE TABLE "public"."pay_product" (
  "id" int8 NOT NULL,
  "code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "channel" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "description" text COLLATE "pg_catalog"."default",
  "icon" varchar(256) COLLATE "pg_catalog"."default",
  "settle_periods" jsonb,
  "sort_no" int4 DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "sandbox" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_product"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_product"."code" IS '产品编码';
COMMENT ON COLUMN "public"."pay_product"."name" IS '产品名称';
COMMENT ON COLUMN "public"."pay_product"."channel" IS '关联通道编码';
COMMENT ON COLUMN "public"."pay_product"."description" IS '产品介绍';
COMMENT ON COLUMN "public"."pay_product"."icon" IS '图标';
COMMENT ON COLUMN "public"."pay_product"."settle_periods" IS '支持的结算周期列表';
COMMENT ON COLUMN "public"."pay_product"."sort_no" IS '排序';
COMMENT ON COLUMN "public"."pay_product"."creator" IS '创建者';
COMMENT ON COLUMN "public"."pay_product"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_product"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."pay_product"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_product"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_product"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_product"."sandbox" IS '是否支持沙箱环境';
COMMENT ON TABLE "public"."pay_product" IS '支付产品';

-- ----------------------------
-- Records of pay_product
-- ----------------------------
INSERT INTO "public"."pay_product" VALUES (1001, 'alipay_isv', '支付宝(服务商)', 'alipay', '支付宝服务商模式，支持多种支付方式、进件申请和分账', 'alipay', '["T+1", "D+1", "T+0", "D+0"]', 10, NULL, NULL, NULL, NULL, 0, 'f', 'f');
INSERT INTO "public"."pay_product" VALUES (10008, 'alipay', '支付宝(直连)', 'alipay', '支付宝直连商户模式', NULL, '["T0", "T1"]', 3, NULL, NULL, NULL, NULL, 0, 'f', 't');
INSERT INTO "public"."pay_product" VALUES (10009, 'wechat_pay', '微信支付(直连)', 'wechat', '微信支付直连商户模式', NULL, '["T0", "T1"]', 4, NULL, NULL, NULL, NULL, 0, 'f', 'f');
INSERT INTO "public"."pay_product" VALUES (10010, 'douyin_pay', '抖音支付(直连)', 'douyin_pay', '抖音支付直连商户模式', NULL, '["T0", "T1"]', 5, NULL, NULL, NULL, NULL, 0, 'f', 'f');
INSERT INTO "public"."pay_product" VALUES (1003, 'wechat_isv', '微信支付(服务商)', 'wechat', '微信支付服务商模式，支持多种支付方式、进件申请和分账', 'wechat', '["T+1", "D+1", "T+0", "D+0"]', 20, NULL, NULL, NULL, NULL, 0, 'f', 'f');
INSERT INTO "public"."pay_product" VALUES (10001, 'ums_qrcode', '银联商务(C扫B)', 'ums_pay', '银联商务C扫B支付（主扫）', NULL, '["T0", "T1"]', 30, NULL, NULL, NULL, NULL, 0, 'f', 't');
INSERT INTO "public"."pay_product" VALUES (10002, 'ums_jsapi', '银联商务(公众号)', 'ums_pay', '银联商务公众号支付', NULL, '["T0", "T1"]', 30, NULL, NULL, NULL, NULL, 0, 'f', 't');
INSERT INTO "public"."pay_product" VALUES (10003, 'ums_app', '银联商务(APP)', 'ums_pay', '银联商务APP支付', NULL, '["T0", "T1"]', 30, NULL, NULL, NULL, NULL, 0, 'f', 't');
INSERT INTO "public"."pay_product" VALUES (10004, 'ums_mini', '银联商务(小程序)', 'ums_pay', '银联商务小程序支付', NULL, '["T0", "T1"]', 30, NULL, NULL, NULL, NULL, 0, 'f', 't');
INSERT INTO "public"."pay_product" VALUES (10005, 'ums_h5', '银联商务(H5)', 'ums_pay', '银联商务H5支付', NULL, '["T0", "T1"]', 30, NULL, NULL, NULL, NULL, 0, 'f', 't');
INSERT INTO "public"."pay_product" VALUES (10006, 'ums_barcode', '银联商务(B扫C)', 'ums_pay', '银联商务B扫C支付（被扫）', NULL, '["T0", "T1"]', 30, NULL, NULL, NULL, NULL, 0, 'f', 't');
INSERT INTO "public"."pay_product" VALUES (10007, 'lakala_pay', '拉卡拉支付', 'lakala_pay', '拉卡拉支付', NULL, '["T0", "T1"]', 40, NULL, NULL, NULL, NULL, 0, 'f', 't');

-- ----------------------------
-- Table structure for pay_product_capability
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_product_capability";
CREATE TABLE "public"."pay_product_capability" (
  "id" int8 NOT NULL,
  "product_code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "capability_code" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4 NOT NULL DEFAULT 0,
  "enabled" bool NOT NULL DEFAULT true,
  "remark" varchar(512) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."pay_product_capability"."product_code" IS '支付产品编码';
COMMENT ON COLUMN "public"."pay_product_capability"."capability_code" IS '支付能力编码';
COMMENT ON COLUMN "public"."pay_product_capability"."sort_no" IS '排序';
COMMENT ON COLUMN "public"."pay_product_capability"."enabled" IS '是否启用';
COMMENT ON COLUMN "public"."pay_product_capability"."remark" IS '备注';
COMMENT ON TABLE "public"."pay_product_capability" IS '支付产品与支付能力关联';

-- ----------------------------
-- Records of pay_product_capability
-- ----------------------------
INSERT INTO "public"."pay_product_capability" VALUES (6001, 'alipay_isv', 'alipay_barcode', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6002, 'alipay_isv', 'alipay_order_qr', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6003, 'alipay_isv', 'alipay_jsapi', 2, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6004, 'alipay_isv', 'alipay_pc', 3, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6005, 'alipay_isv', 'alipay_h5', 4, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6006, 'alipay_isv', 'alipay_app', 5, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6010, 'wechat_isv', 'wechat_qr', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6011, 'wechat_isv', 'wechat_app', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6012, 'wechat_isv', 'wechat_h5', 2, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6013, 'wechat_isv', 'wechat_barcode', 3, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6014, 'wechat_isv', 'wechat_jsapi', 4, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6015, 'wechat_isv', 'wechat_mini', 5, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6020, 'ums_qrcode', 'aggregate_pay_qrcode', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6021, 'ums_qrcode', 'union_pay_qr', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6022, 'ums_qrcode', 'alipay_order_qr', 2, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6023, 'ums_qrcode', 'wechat_qr', 3, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6030, 'ums_jsapi', 'wechat_jsapi', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6031, 'ums_jsapi', 'alipay_jsapi', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6040, 'ums_app', 'wechat_app', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6041, 'ums_app', 'alipay_app', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6050, 'ums_mini', 'wechat_mini', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6060, 'ums_h5', 'wechat_h5', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6061, 'ums_h5', 'alipay_h5', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6062, 'ums_h5', 'union_pay_h5', 2, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6070, 'ums_barcode', 'union_pay_barcode', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6080, 'lakala_pay', 'wechat_barcode', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6081, 'lakala_pay', 'alipay_barcode', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6082, 'lakala_pay', 'union_pay_barcode', 2, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6083, 'lakala_pay', 'wechat_jsapi', 3, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6084, 'lakala_pay', 'wechat_app', 4, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6085, 'lakala_pay', 'wechat_mini', 5, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6086, 'lakala_pay', 'alipay_order_qr', 6, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6087, 'lakala_pay', 'alipay_jsapi', 7, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6088, 'lakala_pay', 'union_pay_qr', 8, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (6089, 'lakala_pay', 'union_pay_jsapi', 9, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_product_capability" VALUES (20081, 'alipay', 'alipay_barcode', 1, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20082, 'alipay', 'alipay_order_qr', 2, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20083, 'alipay', 'alipay_jsapi', 3, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20084, 'alipay', 'alipay_mini', 4, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20085, 'alipay', 'alipay_pc', 5, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20086, 'alipay', 'alipay_h5', 6, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20087, 'alipay', 'alipay_app', 7, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20091, 'wechat_pay', 'wechat_qr', 1, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20092, 'wechat_pay', 'wechat_app', 2, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20093, 'wechat_pay', 'wechat_h5', 3, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20094, 'wechat_pay', 'wechat_barcode', 4, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20095, 'wechat_pay', 'wechat_jsapi', 5, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_product_capability" VALUES (20096, 'wechat_pay', 'wechat_mini', 6, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);

-- ----------------------------
-- Table structure for pay_provider
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_provider";
CREATE TABLE "public"."pay_provider" (
  "id" int8 NOT NULL,
  "code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "icon" varchar(255) COLLATE "pg_catalog"."default",
  "sort_no" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."pay_provider"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_provider"."code" IS '支付渠道编码（PayProviderEnum.code：aggregate_pay/wechat/alipay/union_pay/visa/mastercard）';
COMMENT ON COLUMN "public"."pay_provider"."icon" IS '图标（可选，覆盖前端默认展示）';
COMMENT ON COLUMN "public"."pay_provider"."sort_no" IS '排序（管理端 Tab/列表顺序）';
COMMENT ON COLUMN "public"."pay_provider"."deleted" IS '删除标志（逻辑删除）';
COMMENT ON COLUMN "public"."pay_provider"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_provider"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_provider"."version" IS '版本号（乐观锁）';
COMMENT ON COLUMN "public"."pay_provider"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_provider"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."pay_provider" IS '支付渠道';

-- ----------------------------
-- Records of pay_provider
-- ----------------------------
INSERT INTO "public"."pay_provider" VALUES (502001000, 'aggregate_pay', NULL, 0, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441');
INSERT INTO "public"."pay_provider" VALUES (502001001, 'wechat', NULL, 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441');
INSERT INTO "public"."pay_provider" VALUES (502001002, 'alipay', NULL, 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441');
INSERT INTO "public"."pay_provider" VALUES (502001003, 'union_pay', NULL, 3, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441');
INSERT INTO "public"."pay_provider" VALUES (502001004, 'visa', NULL, 4, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441');
INSERT INTO "public"."pay_provider" VALUES (502001005, 'mastercard', NULL, 5, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441');

-- ----------------------------
-- Table structure for pay_provider_method
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_provider_method";
CREATE TABLE "public"."pay_provider_method" (
  "id" int8 NOT NULL,
  "provider" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "method" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6),
  "description" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_provider_method"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_provider_method"."provider" IS '支付渠道编码（对应 PayProviderMethod.provider / PayProviderEnum.code）';
COMMENT ON COLUMN "public"."pay_provider_method"."method" IS '支付方式编码（PayMethodEnum.code）';
COMMENT ON COLUMN "public"."pay_provider_method"."sort_no" IS '渠道内排序';
COMMENT ON COLUMN "public"."pay_provider_method"."deleted" IS '删除标志（逻辑删除）';
COMMENT ON COLUMN "public"."pay_provider_method"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_provider_method"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_provider_method"."version" IS '版本号（乐观锁）';
COMMENT ON COLUMN "public"."pay_provider_method"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_provider_method"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_provider_method"."description" IS '目录项说明';
COMMENT ON TABLE "public"."pay_provider_method" IS '支付渠道和方式关联';

-- ----------------------------
-- Records of pay_provider_method
-- ----------------------------
INSERT INTO "public"."pay_provider_method" VALUES (502001901, 'aggregate_pay', 'aggregate_pay_qrcode', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502001902, 'aggregate_pay', 'aggregate_pay_barcode', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002001, 'wechat', 'wechat_jsapi', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002002, 'wechat', 'wechat_app', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002003, 'wechat', 'wechat_h5', 3, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002004, 'wechat', 'wechat_qr', 4, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002005, 'wechat', 'wechat_mini', 5, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002006, 'wechat', 'wechat_barcode', 6, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002007, 'wechat', 'wechat_cashier', 7, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002008, 'alipay', 'alipay_barcode', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002010, 'alipay', 'alipay_app', 3, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002011, 'alipay', 'alipay_h5', 4, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002012, 'alipay', 'alipay_pc', 5, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002013, 'alipay', 'alipay_jsapi', 6, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002014, 'union_pay', 'union_qr', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002015, 'union_pay', 'union_pay_barcode', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002016, 'union_pay', 'union_h5', 3, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002017, 'union_pay', 'union_jsapi', 4, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002018, 'visa', 'visa_card_gateway', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002019, 'visa', 'visa_card_present', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002020, 'mastercard', 'mastercard_card_gateway', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002021, 'mastercard', 'mastercard_card_present', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_provider_method" VALUES (502002009, 'alipay', 'alipay_order_qr', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);

-- ----------------------------
-- Table structure for starter_audit_login_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."starter_audit_login_log";
CREATE TABLE "public"."starter_audit_login_log" (
  "id" int8 NOT NULL,
  "user_id" int8,
  "account" varchar(200) COLLATE "pg_catalog"."default",
  "login" bool DEFAULT false,
  "client" varchar(100) COLLATE "pg_catalog"."default",
  "login_type" varchar(100) COLLATE "pg_catalog"."default",
  "ip" varchar(100) COLLATE "pg_catalog"."default",
  "login_location" varchar(200) COLLATE "pg_catalog"."default",
  "browser" varchar(200) COLLATE "pg_catalog"."default",
  "os" varchar(200) COLLATE "pg_catalog"."default",
  "msg" varchar(500) COLLATE "pg_catalog"."default",
  "login_time" timestamp(6),
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."starter_audit_login_log"."id" IS '主键';
COMMENT ON COLUMN "public"."starter_audit_login_log"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."starter_audit_login_log"."account" IS '用户账号';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login" IS '登录成功状态';
COMMENT ON COLUMN "public"."starter_audit_login_log"."client" IS '登录终端';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login_type" IS '登录方式';
COMMENT ON COLUMN "public"."starter_audit_login_log"."ip" IS '登录IP地址';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login_location" IS '登录地点';
COMMENT ON COLUMN "public"."starter_audit_login_log"."browser" IS '浏览器类型';
COMMENT ON COLUMN "public"."starter_audit_login_log"."os" IS '操作系统';
COMMENT ON COLUMN "public"."starter_audit_login_log"."msg" IS '提示消息';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login_time" IS '访问时间';
COMMENT ON COLUMN "public"."starter_audit_login_log"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."starter_audit_login_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."starter_audit_login_log"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."starter_audit_login_log"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."starter_audit_login_log"."version" IS '版本号';
COMMENT ON COLUMN "public"."starter_audit_login_log"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."starter_audit_login_log" IS '登录日志';

-- ----------------------------
-- Records of starter_audit_login_log
-- ----------------------------
INSERT INTO "public"."starter_audit_login_log" VALUES (2037808450413707264, NULL, '未知', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', '用户未找到', '2026-03-28 16:26:08.970318', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2037808471750131712, NULL, '未知', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', '用户未找到', '2026-03-28 16:26:14.601796', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2037808505614942208, NULL, '未知', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', '用户未找到', '2026-03-28 16:26:22.840154', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2037817913807912960, NULL, '未知', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', '用户未找到', '2026-03-28 17:03:45.897829', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2037898883751919616, NULL, '未知', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', '用户未找到', '2026-03-28 22:25:30.110086', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2037899171481174016, NULL, 'bootx', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', '用户状态异常', '2026-03-28 22:26:39.275098', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2037899264653443072, NULL, 'bootx', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', '账号或密码不正确', '2026-03-28 22:27:00.908316', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2037900030071980032, NULL, 'bootx', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', '账号或密码不正确', '2026-03-28 22:30:03.606724', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2037900449653374976, NULL, 'bootx', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', '账号或密码不正确', '2026-03-28 22:31:43.35749', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2037901685874147328, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-28 22:36:37.966962', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2037902735154790400, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-28 22:40:48.600193', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038539833777029120, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 16:52:24.273755', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038540802229239808, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 16:56:16.100079', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038540819153256448, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 16:56:19.326612', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038540996593287168, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 16:56:59.456317', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038541241993625600, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 16:57:56.970748', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038541977636798464, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 17:00:56.142059', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038543133377265664, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 17:05:31.247486', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038543306908205056, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 17:06:13.181829', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038545442270314496, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 17:14:41.486836', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038553326324514816, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 17:46:01.567013', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038553634664579072, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 17:47:15.20004', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038554120872427520, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 17:49:11.364687', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038554142393401344, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 17:49:16.032777', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038554214573178880, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 17:49:33.408465', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038555075990945792, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-30 17:52:56.795415', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038993607032209408, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-31 22:55:32.792073', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038998166282575872, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-31 23:13:39.703074', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038998347866578944, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-31 23:14:23.238038', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2038998555321049088, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-31 23:15:12.667626', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2039004059539742720, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-31 23:37:04.68331', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2039004406538706944, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-03-31 23:38:27.487041', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2039010922905870336, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-01 00:04:20.824903', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2040704990039990272, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-05 16:15:58.013393', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2041501563447353344, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-07 21:01:16.405249', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2041756440052547584, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-08 13:54:03.369681', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2041881733622849536, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-08 22:11:55.874263', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2041891911604822016, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-08 22:52:22.512181', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2042238907188432896, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-09 21:51:12.085684', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2042513881950191616, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-10 16:03:51.149781', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2042514006579740672, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-10 16:04:20.893234', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2042514025617686528, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-10 16:04:25.4294', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2042910461412958208, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-11 18:19:43.103124', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2042980274130853888, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-11 22:57:08.434096', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2042980346197385216, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-11 22:57:25.912124', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2042980954530848768, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-11 22:59:50.462905', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2042981077033885696, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-11 23:00:19.548739', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2042982471379918848, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-11 23:05:51.968071', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043001289456164864, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-12 00:20:38.557701', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043006287422365696, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-12 00:40:30.109913', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043007895916978176, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-12 00:46:53.398394', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043161747374911488, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-12 10:58:15.197825', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043161794091069440, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-12 10:58:25.801886', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043161967475208192, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-12 10:59:07.135546', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043167798002364416, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-12 11:22:17.603613', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043236296724492288, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-12 15:54:29.214381', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043579393832591360, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-13 14:37:49.078815', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043581227083177984, NULL, '未知', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', '用户未找到', '2026-04-13 14:45:06.466434', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043581243973640192, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-13 14:45:11.101979', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043581320133812224, NULL, 'csadmin', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', '账号或密码不正确', '2026-04-13 14:45:28.743268', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043581337078800384, NULL, 'csadmin', 'f', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', '账号或密码不正确', '2026-04-13 14:45:32.716177', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043581366715752448, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-13 14:45:40.291544', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043581489256538112, 2039557567951310848, 'csadmin', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-13 14:46:09.350657', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043581738050068480, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-13 14:47:08.710846', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043639963516641280, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-13 18:38:30.830974', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043686231072747520, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-13 21:42:21.642409', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2043686290543783936, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 146.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-13 21:42:35.45175', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2046939724231405568, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-22 21:10:34.253731', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2046945097852055552, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-22 21:31:56.029017', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2048071717774077952, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-26 00:08:43.018429', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2049160441970769920, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-29 00:14:54.951108', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2049160476141764608, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-04-29 00:15:03.245406', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2050400881487323136, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-02 10:23:58.596345', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2051504094059888640, 2039557567951310848, 'csadmin', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-05 11:27:44.825885', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2051524835555868672, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-05 12:50:10.229927', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2051889056856633344, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-06 12:57:27.738683', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2051889069598928896, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-06 12:57:30.224123', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2051889171042365440, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-06 12:57:54.585906', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2051894382658023424, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-06 13:18:37.294346', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2051900879400038400, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-06 13:44:25.917892', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2053499330520879104, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-10 23:36:06.197414', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2057003263100899328, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 148.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-20 15:39:28.876785', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2058094461353914368, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 148.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-23 15:55:31.086184', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2059181926584070144, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 148.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-26 15:56:43.115715', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2060302777836564480, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 148.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-05-29 18:10:34.791012', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2061449165693759488, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 148.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-06-01 22:05:55.262357', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2063075053652905984, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 148.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-06-06 09:46:37.432804', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2065057528398675968, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'Chrome 148.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-06-11 21:04:15.994807', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_login_log" VALUES (2065439670140108800, 1, 'bootx', 't', 'admin', 'password', '0:0:0:0:0:0:0:1', '未知', 'MSEdge 149.0.0.0', 'Windows 10 or Windows Server 2016', NULL, '2026-06-12 22:22:45.97808', NULL, NULL, NULL, NULL, 0, 'f');

-- ----------------------------
-- Table structure for starter_audit_operate_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."starter_audit_operate_log";
CREATE TABLE "public"."starter_audit_operate_log" (
  "id" int8 NOT NULL,
  "title" varchar(200) COLLATE "pg_catalog"."default",
  "operate_id" int8,
  "account" varchar(200) COLLATE "pg_catalog"."default",
  "client" varchar(100) COLLATE "pg_catalog"."default",
  "browser" varchar(200) COLLATE "pg_catalog"."default",
  "os" varchar(200) COLLATE "pg_catalog"."default",
  "business_type" varchar(100) COLLATE "pg_catalog"."default",
  "method" varchar(200) COLLATE "pg_catalog"."default",
  "request_method" varchar(20) COLLATE "pg_catalog"."default",
  "operate_url" varchar(500) COLLATE "pg_catalog"."default",
  "operate_ip" varchar(100) COLLATE "pg_catalog"."default",
  "operate_location" varchar(200) COLLATE "pg_catalog"."default",
  "operate_param" jsonb,
  "operate_return" jsonb,
  "success" bool DEFAULT false,
  "error_msg" varchar(1000) COLLATE "pg_catalog"."default",
  "operate_time" timestamp(6),
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."starter_audit_operate_log"."id" IS '主键';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."title" IS '操作模块';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_id" IS '操作人员ID';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."account" IS '操作人员账号';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."client" IS '终端编码';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."browser" IS '浏览器类型';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."os" IS '操作系统';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."business_type" IS '业务类型';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."method" IS '请求方法';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."request_method" IS '请求方式';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_url" IS '请求URL';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_ip" IS '操作IP';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_location" IS '操作地点';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_param" IS '请求参数';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_return" IS '返回参数';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."success" IS '操作状态';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."error_msg" IS '错误消息';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_time" IS '操作时间';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."version" IS '版本号';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."starter_audit_operate_log" IS '操作日志';

-- ----------------------------
-- Records of starter_audit_operate_log
-- ----------------------------
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051311123842654208, '重置服务商用户密码', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.isv.controller.info.IsvUserController#restartPassword', 'POST', '/isv/user/restart-password', '0:0:0:0:0:0:0:1', '未知', '[{"userId": "2042248310121746432", "newPassword": "WCC/96lkAiepTY6mfoOs4XvbJocbcIwI63I9jrQB8mSOada9vEC2kb/hEJ7BjXHcBACVweebwdQ8BbbMqtxDLsaoUlZpkYNe4ekkQ0xoAKnCoXnoSQsm9N7iPXnVonFottS/tHPbofjjqFN/3ZIQ8Zkq72ysusLXNwcRv+q87/goHJpJ21TrvtPsv1egSzM9x/tj39E3CXzhgKDHg1pM5WCtD6n4JnCHRVbn0AxASUXITfIzT84mG30adizyEL6I+h1zfqXAQE6hMxiBwc4puCaAU/bh56y6Besn9wpR8XdNQLDFVK70k63wvxAf/Pk9zrhR7UvloKlAlofRSa8qaQ=="}]', NULL, 'f', 'error.iam.password.historyDuplicate', '2026-05-04 22:40:58.011521', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051313207530938368, '封禁服务商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.isv.controller.info.IsvUserController#ban', 'POST', '/isv/user/ban', '0:0:0:0:0:0:0:1', '未知', '["2042248310121746432"]', NULL, 't', NULL, '2026-05-04 22:49:14.164735', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051313220336148480, '解锁服务商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.isv.controller.info.IsvUserController#unlock', 'POST', '/isv/user/unlock', '0:0:0:0:0:0:0:1', '未知', '["2042248310121746432"]', NULL, 't', NULL, '2026-05-04 22:49:17.72841', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051313812492181504, '修改服务商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.isv.controller.info.IsvUserController#update', 'POST', '/isv/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2042248310121746432", "name": "123管理员1", "email": "", "phone": ""}]', NULL, 'f', 'error.iam.user.cannot_edit_non_admin', '2026-05-04 22:51:38.349791', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051313842162688000, '修改服务商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.isv.controller.info.IsvUserController#update', 'POST', '/isv/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2042248310121746432", "name": "123管理员1", "email": "", "phone": ""}]', NULL, 'f', 'error.iam.user.cannot_edit_non_admin', '2026-05-04 22:51:45.846931', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051316472129720320, '修改服务商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.isv.controller.info.IsvUserController#update', 'POST', '/isv/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2042248310121746432", "name": "123管理员1", "email": "", "phone": "", "clientCode": "isv"}]', NULL, 't', NULL, '2026-05-04 23:02:12.651943', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051316540148748288, '修改服务商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.isv.controller.info.IsvUserController#update', 'POST', '/isv/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2042248310121746432", "name": "isv管理员", "email": "", "phone": "", "clientCode": "isv"}]', NULL, 't', NULL, '2026-05-04 23:02:28.619904', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051865182148427776, '修改代理商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.agent.controller.info.AgentUserController#update', 'POST', '/agent/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2051222393719070720", "name": "测试企业代理管理员", "email": "", "phone": ""}]', NULL, 'f', 'error.iam.user.cannot_edit_non_admin', '2026-05-06 11:22:35.172611', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051865355062804480, '修改代理商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.agent.controller.info.AgentUserController#update', 'POST', '/agent/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2051222393719070720", "name": "测试企业代理管理员", "email": "", "phone": ""}]', NULL, 'f', 'error.iam.user.cannot_edit_non_admin', '2026-05-06 11:23:16.171302', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051865359332605952, '修改代理商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.agent.controller.info.AgentUserController#update', 'POST', '/agent/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2051222393719070720", "name": "测试企业代理管理员", "email": "", "phone": ""}]', NULL, 'f', 'error.iam.user.cannot_edit_non_admin', '2026-05-06 11:23:17.462638', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051865359340994560, '修改代理商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.agent.controller.info.AgentUserController#update', 'POST', '/agent/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2051222393719070720", "name": "测试企业代理管理员", "email": "", "phone": ""}]', NULL, 'f', 'error.iam.user.cannot_edit_non_admin', '2026-05-06 11:23:18.08561', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051865363619184640, '修改代理商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.agent.controller.info.AgentUserController#update', 'POST', '/agent/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2051222393719070720", "name": "测试企业代理管理员", "email": "", "phone": ""}]', NULL, 'f', 'error.iam.user.cannot_edit_non_admin', '2026-05-06 11:23:19.054126', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051865367842848768, '修改代理商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.agent.controller.info.AgentUserController#update', 'POST', '/agent/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2051222393719070720", "name": "测试企业代理管理员", "email": "", "phone": ""}]', NULL, 'f', 'error.iam.user.cannot_edit_non_admin', '2026-05-06 11:23:19.789895', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051865380585144320, '修改代理商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.agent.controller.info.AgentUserController#update', 'POST', '/agent/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2051222393719070720", "name": "测试企业代理管理员", "email": "", "phone": ""}]', NULL, 'f', 'error.iam.user.cannotEditNonAdmin', '2026-05-06 11:23:22.295329', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051865384875917312, '修改代理商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.agent.controller.info.AgentUserController#update', 'POST', '/agent/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2051222393719070720", "name": "测试企业代理管理员", "email": "", "phone": ""}]', NULL, 'f', 'error.iam.user.cannotEditNonAdmin', '2026-05-06 11:23:23.219827', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051867495709741056, '修改代理商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.agent.controller.info.AgentUserController#update', 'POST', '/agent/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2051222393719070720", "name": "测试企业代理商管理员1", "email": "", "phone": "", "clientCode": "agent"}]', NULL, 't', NULL, '2026-05-06 11:31:46.47378', NULL, NULL, NULL, NULL, 0, 'f');
INSERT INTO "public"."starter_audit_operate_log" VALUES (2051867516970668032, '修改代理商用户', 1, 'bootx', 'admin', 'Chrome 147.0.0.0', 'Windows 10 or Windows Server 2016', 'update', 'cn.daxpay.payment.agent.controller.info.AgentUserController#update', 'POST', '/agent/user/update', '0:0:0:0:0:0:0:1', '未知', '[{"id": "2051222393719070720", "name": "测试企业代理商管理员", "email": "", "phone": "", "clientCode": "agent"}]', NULL, 't', NULL, '2026-05-06 11:31:51.861881', NULL, NULL, NULL, NULL, 0, 'f');

-- ----------------------------
-- Table structure for system_dict
-- ----------------------------
DROP TABLE IF EXISTS "public"."system_dict";
CREATE TABLE "public"."system_dict" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "name_cn" varchar(255) COLLATE "pg_catalog"."default",
  "name_en" varchar(255) COLLATE "pg_catalog"."default",
  "dict_type" varchar(255) COLLATE "pg_catalog"."default",
  "code" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "enable" bool,
  "internal" bool
)
;
COMMENT ON COLUMN "public"."system_dict"."id" IS '主键';
COMMENT ON COLUMN "public"."system_dict"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."system_dict"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."system_dict"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."system_dict"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."system_dict"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."system_dict"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."system_dict"."name" IS '名称';
COMMENT ON COLUMN "public"."system_dict"."name_cn" IS '中文名称';
COMMENT ON COLUMN "public"."system_dict"."name_en" IS '英文名称';
COMMENT ON COLUMN "public"."system_dict"."dict_type" IS '字典类型';
COMMENT ON COLUMN "public"."system_dict"."code" IS '编码';
COMMENT ON COLUMN "public"."system_dict"."remark" IS '备注';
COMMENT ON COLUMN "public"."system_dict"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."system_dict"."internal" IS '是否内置';
COMMENT ON TABLE "public"."system_dict" IS '字典表';

-- ----------------------------
-- Records of system_dict
-- ----------------------------
INSERT INTO "public"."system_dict" VALUES (2034597186006867968, 0, '2026-03-19 19:45:44.788062', 0, '2026-03-19 22:06:35.468374', 7, 'f', '123', '中文', '123', NULL, 'cs', NULL, 't', 'f');
INSERT INTO "public"."system_dict" VALUES (308196335536967680, 1, '2026-04-30 11:15:28.565006', 1, '2026-04-30 11:15:28.565006', 0, 'f', '支付宝认证方式', '支付宝认证方式', 'Alipay Auth Type', 'common', 'alipay_auth_type', '支付宝接口认证方式', 't', 't');

-- ----------------------------
-- Table structure for system_dict_item
-- ----------------------------
DROP TABLE IF EXISTS "public"."system_dict_item";
CREATE TABLE "public"."system_dict_item" (
  "id" int8 NOT NULL,
  "dict_id" int8 NOT NULL,
  "dict_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "name_cn" varchar(200) COLLATE "pg_catalog"."default",
  "name_en" varchar(200) COLLATE "pg_catalog"."default",
  "sort_no" int4,
  "enable" bool DEFAULT true,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."system_dict_item"."id" IS '主键';
COMMENT ON COLUMN "public"."system_dict_item"."dict_id" IS '字典ID';
COMMENT ON COLUMN "public"."system_dict_item"."dict_code" IS '字典编码';
COMMENT ON COLUMN "public"."system_dict_item"."code" IS '字典项编码';
COMMENT ON COLUMN "public"."system_dict_item"."name_cn" IS '中文名称';
COMMENT ON COLUMN "public"."system_dict_item"."name_en" IS '英文名称';
COMMENT ON COLUMN "public"."system_dict_item"."sort_no" IS '字典项排序';
COMMENT ON COLUMN "public"."system_dict_item"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."system_dict_item"."remark" IS '备注';
COMMENT ON COLUMN "public"."system_dict_item"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."system_dict_item"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."system_dict_item"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."system_dict_item"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."system_dict_item"."version" IS '版本号';
COMMENT ON COLUMN "public"."system_dict_item"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."system_dict_item" IS '字典项';

-- ----------------------------
-- Records of system_dict_item
-- ----------------------------
INSERT INTO "public"."system_dict_item" VALUES (2034632666501005312, 2034597186006867968, 'cs', 'cs', '中文', '123', 0, 't', NULL, 0, '2026-03-19 22:06:44.064918', 0, '2026-03-19 22:08:50.053875', 3, 'f');
INSERT INTO "public"."system_dict_item" VALUES (308196335536967681, 308196335536967680, 'alipay_auth_type', 'public_key', '公钥模式', 'Public Key Mode', 0, 't', '使用公钥进行签名验证', 1, '2026-04-30 11:15:28.567585', 1, '2026-04-30 11:15:28.567585', 0, 'f');
INSERT INTO "public"."system_dict_item" VALUES (308196335536967682, 308196335536967680, 'alipay_auth_type', 'cert', '证书模式', 'Certificate Mode', 1, 't', '使用证书进行签名验证', 1, '2026-04-30 11:15:28.570843', 1, '2026-04-30 11:15:28.570843', 0, 'f');

-- ----------------------------
-- Table structure for system_platform_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."system_platform_config";
CREATE TABLE "public"."system_platform_config" (
  "id" int8 NOT NULL,
  "config_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "config_name" varchar(100) COLLATE "pg_catalog"."default",
  "config_data" jsonb,
  "description" varchar(500) COLLATE "pg_catalog"."default",
  "enabled" bool DEFAULT true,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."system_platform_config"."id" IS '主键';
COMMENT ON COLUMN "public"."system_platform_config"."config_type" IS '配置类型';
COMMENT ON COLUMN "public"."system_platform_config"."config_name" IS '配置名称';
COMMENT ON COLUMN "public"."system_platform_config"."config_data" IS '配置数据JSON格式';
COMMENT ON COLUMN "public"."system_platform_config"."description" IS '配置描述';
COMMENT ON COLUMN "public"."system_platform_config"."enabled" IS '是否启用';
COMMENT ON COLUMN "public"."system_platform_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."system_platform_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."system_platform_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."system_platform_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."system_platform_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."system_platform_config"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."system_platform_config" IS '系统平台统一配置';

-- ----------------------------
-- Records of system_platform_config
-- ----------------------------
INSERT INTO "public"."system_platform_config" VALUES (2040811212160327680, 'security_two_factor_auth', '双因素认证配置', '{"enabled": true, "algorithm": "HmacSHA1"}', NULL, 't', 1, '2026-04-05 23:18:04.074627', 1, '2026-04-05 23:21:47.073617', 1, 'f');
INSERT INTO "public"."system_platform_config" VALUES (2040728696825012224, 'security_session', '会话管理配置', '{"enabled": true, "maxOnlineHours": 3, "concurrentStrategy": "NEW_SESSION", "maxConcurrentSessions": 2}', NULL, 't', 1, '2026-04-05 17:50:10.889395', 1, '2026-04-05 20:25:36.288686', 2, 'f');
INSERT INTO "public"."system_platform_config" VALUES (2040986103044034560, 'anomaly_detection', '异常登录检测配置', '{}', NULL, 't', 1, '2026-04-06 10:53:01.313398', 1, '2026-04-06 10:53:01.318629', 0, 'f');
INSERT INTO "public"."system_platform_config" VALUES (2037801810901790720, 'security_login', '登录安全配置', '{"lockoutEnabled": true, "maxFailedAttempts": 7, "failureResetMinutes": 15, "lockoutDurationMinutes": 30}', NULL, 't', 0, '2026-03-28 15:59:46.902957', 1, '2026-04-08 21:40:19.345563', 29, 'f');
INSERT INTO "public"."system_platform_config" VALUES (2041873119185149952, 'oss', '对象存储配置', '{"bucket": "123", "region": "123", "remark": "", "endpoint": "123", "accessKey": "123", "secretKey": "123", "publicBaseUrl": "123"}', NULL, 't', 1, '2026-04-08 21:37:42.438334', 1, '2026-04-09 00:42:35.047283', 5, 'f');
INSERT INTO "public"."system_platform_config" VALUES (2039557567431217152, 'security_password_policy', '密码策略配置', '{"enabled": true, "maxLength": 32, "minLength": 6, "historyCount": 3, "requireDigit": false, "rotationDays": 90, "specialChars": "!@#$%^&*()_+-=[]{}|;:,.<>?", "requireLowercase": false, "requireUppercase": false, "requireSpecialChar": false}', NULL, 't', 1, '2026-04-02 12:16:31.879282', 1, '2026-04-09 20:31:05.239573', 19, 'f');

-- ----------------------------
-- Table structure for system_platform_encrypt_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."system_platform_encrypt_config";
CREATE TABLE "public"."system_platform_encrypt_config" (
  "id" int8 NOT NULL,
  "config_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "config_name" varchar(100) COLLATE "pg_catalog"."default",
  "config_data" text COLLATE "pg_catalog"."default",
  "description" varchar(500) COLLATE "pg_catalog"."default",
  "enabled" bool DEFAULT true,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."id" IS '主键';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."config_type" IS '配置类型';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."config_name" IS '配置名称';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."config_data" IS '配置数据(加密存储)';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."description" IS '配置描述';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."enabled" IS '是否启用';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."creator" IS '创建者';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."deleted" IS '是否删除';
COMMENT ON TABLE "public"."system_platform_encrypt_config" IS '系统平台加密配置表';

-- ----------------------------
-- Records of system_platform_encrypt_config
-- ----------------------------
INSERT INTO "public"."system_platform_encrypt_config" VALUES (2042089743590817792, 'oss', '对象存储配置', 'v1:7BMULO7UdBVTuWm5Xqs5d2xeK4/gURRIGkHc+2Pw9QU66zQ5W2iBjRzhKOzex6pxWmgAqG4+bTySa87evT0/WkUH11Fwz+j7pP7km1Z53hA/2ulEqsEwQ5ImRI/dxhHASB/tDw7CAD/1i9QRdneBQr6Rvhw8OTCzY93xcQ16/Ew91rxFa21a21WUwJdFmOaZBLpxv6y9bo8YGkutfU1RuuS8/19ud88YoEhP6aFKIi47tBrElWO7VMDfBOrUQxzRRpjv/IOEiWGo6sSg502/Ax6MBoyqhilRsxaa/kz5iIAIEUcbOmR7vWhdBZEA3HfR9C1BxreIufkBRsh0E0yGMbJMT3sZBqDGCDSaXdkM0jholrd7yrCzxoU6GpjYdiAh+wdxyFc1jOyr0qdJL+JrJXyiUv+pgbuN5xly5ReucraIyvJi/bPRqdlUM+O6ADMFYDqdA7SDAiunfihxqfJwR4w=', NULL, 't', 1, '2026-04-09 11:58:29.724897', 1, '2026-04-11 13:10:03.427086', 14, 'f');

-- ----------------------------
-- Table structure for wechat_mch_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."wechat_mch_app";
CREATE TABLE "public"."wechat_mch_app" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "agent_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "isv_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "app_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "wx_app_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."wechat_mch_app"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_mch_app"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_mch_app"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."wechat_mch_app"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."wechat_mch_app"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."wechat_mch_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."wechat_mch_app"."app_type" IS '应用类型：official_account-公众号，mini_program-小程序，mobile_app-移动应用';
COMMENT ON COLUMN "public"."wechat_mch_app"."wx_app_id" IS '微信应用AppId';
COMMENT ON COLUMN "public"."wechat_mch_app"."creator" IS '创建者';
COMMENT ON COLUMN "public"."wechat_mch_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_mch_app"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."wechat_mch_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_mch_app"."version" IS '版本号';
COMMENT ON COLUMN "public"."wechat_mch_app"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."wechat_mch_app" IS '微信通道商户应用';

-- ----------------------------
-- Records of wechat_mch_app
-- ----------------------------
INSERT INTO "public"."wechat_mch_app" VALUES (2063963629836029952, 'M1777797520668', 'AGENT1776138002345', 'ISV1215972714557722', '111111', '123', 'official_account', '123', 1, '2026-06-08 20:37:30.602962', 1, '2026-06-08 20:37:30.712132', 0, 'f');
INSERT INTO "public"."wechat_mch_app" VALUES (2063963720168755200, 'M1777797520668', 'AGENT1776138002345', 'ISV1215972714557722', '111111', 'cs', 'mini_program', '12312', 1, '2026-06-08 20:37:52.136572', 1, '2026-06-08 20:37:52.140083', 0, 'f');

-- ----------------------------
-- Table structure for wechat_mch_app_auth_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."wechat_mch_app_auth_config";
CREATE TABLE "public"."wechat_mch_app_auth_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "agent_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "isv_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" int8 NOT NULL,
  "app_secret" varchar(512) COLLATE "pg_catalog"."default",
  "auth_callback_url" varchar(512) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."agent_no" IS '代理商号';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."isv_no" IS '服务商号';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."app_id" IS '微信通道商户应用ID';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."app_secret" IS '应用密钥(加密存储)';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."auth_callback_url" IS '授权回调地址';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."creator" IS '创建者';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."wechat_mch_app_auth_config"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."wechat_mch_app_auth_config" IS '微信通道商户应用授权认证配置';

-- ----------------------------
-- Records of wechat_mch_app_auth_config
-- ----------------------------

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."mch_user_id_seq"
OWNED BY "public"."mch_user"."id";
SELECT setval('"public"."mch_user_id_seq"', 1, false);

-- ----------------------------
-- Indexes structure for table alipay_mch_app
-- ----------------------------
CREATE INDEX "idx_alipay_mch_app_channel_mch_no" ON "public"."alipay_mch_app" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_alipay_mch_app_mch_no" ON "public"."alipay_mch_app" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "uk_alipay_mch_app_channel_ali_app_id" ON "public"."alipay_mch_app" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "ali_app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_alipay_mch_app_channel_ali_app_id" IS '同一通道商户下支付宝应用ID不可重复';

-- ----------------------------
-- Primary Key structure for table alipay_mch_app
-- ----------------------------
ALTER TABLE "public"."alipay_mch_app" ADD CONSTRAINT "pk_alipay_mch_app" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table base_area
-- ----------------------------
ALTER TABLE "public"."base_area" ADD CONSTRAINT "base_area_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Primary Key structure for table base_city
-- ----------------------------
ALTER TABLE "public"."base_city" ADD CONSTRAINT "base_city_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Primary Key structure for table base_dict
-- ----------------------------
ALTER TABLE "public"."base_dict" ADD CONSTRAINT "base_dict_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table base_province
-- ----------------------------
ALTER TABLE "public"."base_province" ADD CONSTRAINT "base_province_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Indexes structure for table base_street
-- ----------------------------
CREATE INDEX "inx_area_code" ON "public"."base_street" USING btree (
  "area_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."inx_area_code" IS '县区';

-- ----------------------------
-- Primary Key structure for table base_street
-- ----------------------------
ALTER TABLE "public"."base_street" ADD CONSTRAINT "base_street_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Indexes structure for table iam_perm_code
-- ----------------------------
CREATE UNIQUE INDEX "uk_iam_perm_code_code" ON "public"."iam_perm_code" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_iam_perm_code_code" IS '权限码表编码唯一索引';

-- ----------------------------
-- Primary Key structure for table iam_perm_code
-- ----------------------------
ALTER TABLE "public"."iam_perm_code" ADD CONSTRAINT "iam_perm_code_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_perm_menu
-- ----------------------------
ALTER TABLE "public"."iam_perm_menu" ADD CONSTRAINT "iam_perm_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_role
-- ----------------------------
ALTER TABLE "public"."iam_role" ADD CONSTRAINT "iam_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_role_code
-- ----------------------------
CREATE INDEX "idx_iam_role_code_role_id" ON "public"."iam_role_code" USING btree (
  "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_iam_role_code_role_id" IS '角色权限码关联表角色ID索引';

-- ----------------------------
-- Primary Key structure for table iam_role_code
-- ----------------------------
ALTER TABLE "public"."iam_role_code" ADD CONSTRAINT "iam_role_code_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_role_menu
-- ----------------------------
CREATE INDEX "idx_role_menu_role_client" ON "public"."iam_role_menu" USING btree (
  "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "client_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_role_menu_role_client" IS '角色菜单表按角色ID和终端编码的普通索引';

-- ----------------------------
-- Primary Key structure for table iam_role_menu
-- ----------------------------
ALTER TABLE "public"."iam_role_menu" ADD CONSTRAINT "iam_role_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_user_expand_info
-- ----------------------------
ALTER TABLE "public"."iam_user_expand_info" ADD CONSTRAINT "iam_user_expand_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_user_info
-- ----------------------------
CREATE INDEX "idx_iam_user_info_client_account" ON "public"."iam_user_info" USING btree (
  "client_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "account" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_iam_user_info_client_account" IS '用户信息表终端账号索引';
CREATE INDEX "idx_iam_user_info_client_email" ON "public"."iam_user_info" USING btree (
  "client_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "email" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false AND email IS NOT NULL AND email::text <> ''::text;
COMMENT ON INDEX "public"."idx_iam_user_info_client_email" IS '用户信息表终端邮箱索引';
CREATE INDEX "idx_iam_user_info_client_phone" ON "public"."iam_user_info" USING btree (
  "client_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "phone" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false AND phone IS NOT NULL AND phone::text <> ''::text;
COMMENT ON INDEX "public"."idx_iam_user_info_client_phone" IS '用户信息表终端手机号索引';

-- ----------------------------
-- Primary Key structure for table iam_user_info
-- ----------------------------
ALTER TABLE "public"."iam_user_info" ADD CONSTRAINT "iam_user_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_user_password_history
-- ----------------------------
CREATE INDEX "idx_password_history_user_id" ON "public"."iam_user_password_history" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_password_history_user_id" IS '用户ID索引';

-- ----------------------------
-- Primary Key structure for table iam_user_password_history
-- ----------------------------
ALTER TABLE "public"."iam_user_password_history" ADD CONSTRAINT "iam_user_password_history_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_user_password_security
-- ----------------------------
ALTER TABLE "public"."iam_user_password_security" ADD CONSTRAINT "iam_user_password_security_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_user_role
-- ----------------------------
CREATE INDEX "idx_iam_user_role_user_id" ON "public"."iam_user_role" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_iam_user_role_user_id" IS '用户角色关联表用户ID索引';

-- ----------------------------
-- Primary Key structure for table iam_user_role
-- ----------------------------
ALTER TABLE "public"."iam_user_role" ADD CONSTRAINT "iam_user_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_alipay_channel_merchant
-- ----------------------------
CREATE INDEX "idx_mch_alipay_channel_merchant_mch_no" ON "public"."mch_alipay_channel_merchant" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_alipay_channel_merchant_mch_no" IS '商户号索引';

-- ----------------------------
-- Primary Key structure for table mch_alipay_channel_merchant
-- ----------------------------
ALTER TABLE "public"."mch_alipay_channel_merchant" ADD CONSTRAINT "mch_alipay_channel_merchant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_app_info
-- ----------------------------
CREATE INDEX "idx_mch_app_info_mch_no" ON "public"."mch_app_info" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_app_info_mch_no" IS '商户号索引';
CREATE UNIQUE INDEX "uk_mch_app_info_app_id" ON "public"."mch_app_info" USING btree (
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."uk_mch_app_info_app_id" IS '应用号唯一索引';

-- ----------------------------
-- Primary Key structure for table mch_app_info
-- ----------------------------
ALTER TABLE "public"."mch_app_info" ADD CONSTRAINT "mch_app_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_bank_card_profile
-- ----------------------------
CREATE INDEX "idx_mch_bank_card_profile_mch_no" ON "public"."mch_bank_card_profile" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_bank_card_profile_mch_no" IS '商户号索引';

-- ----------------------------
-- Primary Key structure for table mch_bank_card_profile
-- ----------------------------
ALTER TABLE "public"."mch_bank_card_profile" ADD CONSTRAINT "mch_bank_card_profile_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_base_profile
-- ----------------------------
CREATE INDEX "idx_mch_base_profile_mch_no" ON "public"."mch_base_profile" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_base_profile_mch_no" IS '商户号索引';

-- ----------------------------
-- Primary Key structure for table mch_base_profile
-- ----------------------------
ALTER TABLE "public"."mch_base_profile" ADD CONSTRAINT "mch_base_profile_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_credential
-- ----------------------------
CREATE INDEX "idx_mch_credential_mch_no" ON "public"."mch_credential" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_credential_mch_no" IS '商户号索引';

-- ----------------------------
-- Primary Key structure for table mch_credential
-- ----------------------------
ALTER TABLE "public"."mch_credential" ADD CONSTRAINT "mch_credential_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_info
-- ----------------------------
CREATE INDEX "idx_mch_info_agent_no" ON "public"."mch_info" USING btree (
  "agent_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_mch_info_isv_no" ON "public"."mch_info" USING btree (
  "isv_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_mch_info_mch_no" ON "public"."mch_info" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_mch_info_status" ON "public"."mch_info" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table mch_info
-- ----------------------------
ALTER TABLE "public"."mch_info" ADD CONSTRAINT "mch_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_lakala_channel_merchant
-- ----------------------------
CREATE INDEX "idx_mch_lakala_channel_merchant_mch_no" ON "public"."mch_lakala_channel_merchant" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_lakala_channel_merchant_mch_no" IS '商户号索引';

-- ----------------------------
-- Primary Key structure for table mch_lakala_channel_merchant
-- ----------------------------
ALTER TABLE "public"."mch_lakala_channel_merchant" ADD CONSTRAINT "mch_lakala_channel_merchant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_product_config
-- ----------------------------
CREATE INDEX "idx_mch_product_config_mch_no" ON "public"."mch_product_config" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_product_config_mch_no" IS '商户号索引';
CREATE INDEX "idx_mch_product_config_product" ON "public"."mch_product_config" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_product_config_product" IS '产品编码索引';

-- ----------------------------
-- Primary Key structure for table mch_product_config
-- ----------------------------
ALTER TABLE "public"."mch_product_config" ADD CONSTRAINT "mch_product_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_ums_channel_merchant
-- ----------------------------
CREATE INDEX "idx_mch_ums_channel_merchant_mch_no" ON "public"."mch_ums_channel_merchant" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_ums_channel_merchant_mch_no" IS '商户号索引';

-- ----------------------------
-- Primary Key structure for table mch_ums_channel_merchant
-- ----------------------------
ALTER TABLE "public"."mch_ums_channel_merchant" ADD CONSTRAINT "mch_ums_channel_merchant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table mch_user
-- ----------------------------
ALTER TABLE "public"."mch_user" ADD CONSTRAINT "mch_user_mch_no_user_id_key" UNIQUE ("mch_no", "user_id");

-- ----------------------------
-- Primary Key structure for table mch_user
-- ----------------------------
ALTER TABLE "public"."mch_user" ADD CONSTRAINT "mch_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_wechat_channel_merchant
-- ----------------------------
CREATE INDEX "idx_mch_wechat_channel_merchant_mch_no" ON "public"."mch_wechat_channel_merchant" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_wechat_channel_merchant_mch_no" IS '商户号索引';

-- ----------------------------
-- Primary Key structure for table mch_wechat_channel_merchant
-- ----------------------------
ALTER TABLE "public"."mch_wechat_channel_merchant" ADD CONSTRAINT "mch_wechat_channel_merchant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_capability
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_capability_code" ON "public"."pay_capability" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_capability_code" IS '支付能力编码唯一（未删除）';

-- ----------------------------
-- Primary Key structure for table pay_capability
-- ----------------------------
ALTER TABLE "public"."pay_capability" ADD CONSTRAINT "pay_capability_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_channel
-- ----------------------------
ALTER TABLE "public"."pay_channel" ADD CONSTRAINT "pay_channel_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_method
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_method_code" ON "public"."pay_method" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_method_code" IS '支付方式编码唯一（未删除）';

-- ----------------------------
-- Primary Key structure for table pay_method
-- ----------------------------
ALTER TABLE "public"."pay_method" ADD CONSTRAINT "pay_method_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_product
-- ----------------------------
CREATE UNIQUE INDEX "idx_pay_product_code" ON "public"."pay_product" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_pay_product_code" IS '产品编码唯一索引';

-- ----------------------------
-- Primary Key structure for table pay_product
-- ----------------------------
ALTER TABLE "public"."pay_product" ADD CONSTRAINT "pay_product_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_product_capability
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_product_capability_pair" ON "public"."pay_product_capability" USING btree (
  "product_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "capability_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_product_capability_pair" IS '产品+能力唯一（未删除）';

-- ----------------------------
-- Primary Key structure for table pay_product_capability
-- ----------------------------
ALTER TABLE "public"."pay_product_capability" ADD CONSTRAINT "pay_product_capability_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_provider
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_provider_code" ON "public"."pay_provider" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_provider_code" IS '支付渠道编码唯一（未删除）';

-- ----------------------------
-- Primary Key structure for table pay_provider
-- ----------------------------
ALTER TABLE "public"."pay_provider" ADD CONSTRAINT "pay_provider_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_provider_method
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_provider_method_pair" ON "public"."pay_provider_method" USING btree (
  "provider" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "method" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_provider_method_pair" IS '支付渠道+支付方式唯一（未删除）';

-- ----------------------------
-- Primary Key structure for table pay_provider_method
-- ----------------------------
ALTER TABLE "public"."pay_provider_method" ADD CONSTRAINT "pay_provider_method_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table wechat_mch_app
-- ----------------------------
CREATE INDEX "idx_wechat_mch_app_mch" ON "public"."wechat_mch_app" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_wechat_mch_app_mch" IS '商户号与通道商户号索引';

-- ----------------------------
-- Uniques structure for table wechat_mch_app
-- ----------------------------
ALTER TABLE "public"."wechat_mch_app" ADD CONSTRAINT "uk_wechat_mch_app" UNIQUE ("channel_mch_no", "wx_app_id");

-- ----------------------------
-- Primary Key structure for table wechat_mch_app
-- ----------------------------
ALTER TABLE "public"."wechat_mch_app" ADD CONSTRAINT "pk_wechat_mch_app" PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table wechat_mch_app_auth_config
-- ----------------------------
ALTER TABLE "public"."wechat_mch_app_auth_config" ADD CONSTRAINT "uk_wechat_mch_app_auth_config" UNIQUE ("app_id");

-- ----------------------------
-- Primary Key structure for table wechat_mch_app_auth_config
-- ----------------------------
ALTER TABLE "public"."wechat_mch_app_auth_config" ADD CONSTRAINT "pk_wechat_mch_app_auth_config" PRIMARY KEY ("id");
