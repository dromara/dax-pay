/*
 Navicat Premium Data Transfer

 Source Server         : bootx
 Source Server Type    : MySQL
 Source Server Version : 50735
 Source Schema         : bootx-platform

 Target Server Type    : MySQL
 Target Server Version : 50735
 File Encoding         : 65001

 Date: 10/05/2023 09:31:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_area
-- ----------------------------
DROP TABLE IF EXISTS `base_area`;
CREATE TABLE `base_area`  (
  `code` char(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '区域名称',
  `city_code` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '城市编码',
  PRIMARY KEY (`code`) USING BTREE,
  INDEX `inx_city_code`(`city_code`) USING BTREE COMMENT '城市'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '县区表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_area
-- ----------------------------
INSERT INTO `base_area` VALUES ('110101', '东城区', '1101');
INSERT INTO `base_area` VALUES ('110102', '西城区', '1101');
INSERT INTO `base_area` VALUES ('110105', '朝阳区', '1101');
INSERT INTO `base_area` VALUES ('110106', '丰台区', '1101');
INSERT INTO `base_area` VALUES ('110107', '石景山区', '1101');
INSERT INTO `base_area` VALUES ('110108', '海淀区', '1101');
INSERT INTO `base_area` VALUES ('110109', '门头沟区', '1101');
INSERT INTO `base_area` VALUES ('110111', '房山区', '1101');
INSERT INTO `base_area` VALUES ('110112', '通州区', '1101');
INSERT INTO `base_area` VALUES ('110113', '顺义区', '1101');
INSERT INTO `base_area` VALUES ('110114', '昌平区', '1101');
INSERT INTO `base_area` VALUES ('110115', '大兴区', '1101');
INSERT INTO `base_area` VALUES ('110116', '怀柔区', '1101');
INSERT INTO `base_area` VALUES ('110117', '平谷区', '1101');
INSERT INTO `base_area` VALUES ('110118', '密云区', '1101');
INSERT INTO `base_area` VALUES ('110119', '延庆区', '1101');
INSERT INTO `base_area` VALUES ('120101', '和平区', '1201');
INSERT INTO `base_area` VALUES ('120102', '河东区', '1201');
INSERT INTO `base_area` VALUES ('120103', '河西区', '1201');
INSERT INTO `base_area` VALUES ('120104', '南开区', '1201');
INSERT INTO `base_area` VALUES ('120105', '河北区', '1201');
INSERT INTO `base_area` VALUES ('120106', '红桥区', '1201');
INSERT INTO `base_area` VALUES ('120110', '东丽区', '1201');
INSERT INTO `base_area` VALUES ('120111', '西青区', '1201');
INSERT INTO `base_area` VALUES ('120112', '津南区', '1201');
INSERT INTO `base_area` VALUES ('120113', '北辰区', '1201');
INSERT INTO `base_area` VALUES ('120114', '武清区', '1201');
INSERT INTO `base_area` VALUES ('120115', '宝坻区', '1201');
INSERT INTO `base_area` VALUES ('120116', '滨海新区', '1201');
INSERT INTO `base_area` VALUES ('120117', '宁河区', '1201');
INSERT INTO `base_area` VALUES ('120118', '静海区', '1201');
INSERT INTO `base_area` VALUES ('120119', '蓟州区', '1201');
INSERT INTO `base_area` VALUES ('130102', '长安区', '1301');
INSERT INTO `base_area` VALUES ('130104', '桥西区', '1301');
INSERT INTO `base_area` VALUES ('130105', '新华区', '1301');
INSERT INTO `base_area` VALUES ('130107', '井陉矿区', '1301');
INSERT INTO `base_area` VALUES ('130108', '裕华区', '1301');
INSERT INTO `base_area` VALUES ('130109', '藁城区', '1301');
INSERT INTO `base_area` VALUES ('130110', '鹿泉区', '1301');
INSERT INTO `base_area` VALUES ('130111', '栾城区', '1301');
INSERT INTO `base_area` VALUES ('130121', '井陉县', '1301');
INSERT INTO `base_area` VALUES ('130123', '正定县', '1301');
INSERT INTO `base_area` VALUES ('130125', '行唐县', '1301');
INSERT INTO `base_area` VALUES ('130126', '灵寿县', '1301');
INSERT INTO `base_area` VALUES ('130127', '高邑县', '1301');
INSERT INTO `base_area` VALUES ('130128', '深泽县', '1301');
INSERT INTO `base_area` VALUES ('130129', '赞皇县', '1301');
INSERT INTO `base_area` VALUES ('130130', '无极县', '1301');
INSERT INTO `base_area` VALUES ('130131', '平山县', '1301');
INSERT INTO `base_area` VALUES ('130132', '元氏县', '1301');
INSERT INTO `base_area` VALUES ('130133', '赵县', '1301');
INSERT INTO `base_area` VALUES ('130171', '石家庄高新技术产业开发区', '1301');
INSERT INTO `base_area` VALUES ('130172', '石家庄循环化工园区', '1301');
INSERT INTO `base_area` VALUES ('130181', '辛集市', '1301');
INSERT INTO `base_area` VALUES ('130183', '晋州市', '1301');
INSERT INTO `base_area` VALUES ('130184', '新乐市', '1301');
INSERT INTO `base_area` VALUES ('130202', '路南区', '1302');
INSERT INTO `base_area` VALUES ('130203', '路北区', '1302');
INSERT INTO `base_area` VALUES ('130204', '古冶区', '1302');
INSERT INTO `base_area` VALUES ('130205', '开平区', '1302');
INSERT INTO `base_area` VALUES ('130207', '丰南区', '1302');
INSERT INTO `base_area` VALUES ('130208', '丰润区', '1302');
INSERT INTO `base_area` VALUES ('130209', '曹妃甸区', '1302');
INSERT INTO `base_area` VALUES ('130224', '滦南县', '1302');
INSERT INTO `base_area` VALUES ('130225', '乐亭县', '1302');
INSERT INTO `base_area` VALUES ('130227', '迁西县', '1302');
INSERT INTO `base_area` VALUES ('130229', '玉田县', '1302');
INSERT INTO `base_area` VALUES ('130271', '河北唐山芦台经济开发区', '1302');
INSERT INTO `base_area` VALUES ('130272', '唐山市汉沽管理区', '1302');
INSERT INTO `base_area` VALUES ('130273', '唐山高新技术产业开发区', '1302');
INSERT INTO `base_area` VALUES ('130274', '河北唐山海港经济开发区', '1302');
INSERT INTO `base_area` VALUES ('130281', '遵化市', '1302');
INSERT INTO `base_area` VALUES ('130283', '迁安市', '1302');
INSERT INTO `base_area` VALUES ('130284', '滦州市', '1302');
INSERT INTO `base_area` VALUES ('130302', '海港区', '1303');
INSERT INTO `base_area` VALUES ('130303', '山海关区', '1303');
INSERT INTO `base_area` VALUES ('130304', '北戴河区', '1303');
INSERT INTO `base_area` VALUES ('130306', '抚宁区', '1303');
INSERT INTO `base_area` VALUES ('130321', '青龙满族自治县', '1303');
INSERT INTO `base_area` VALUES ('130322', '昌黎县', '1303');
INSERT INTO `base_area` VALUES ('130324', '卢龙县', '1303');
INSERT INTO `base_area` VALUES ('130371', '秦皇岛市经济技术开发区', '1303');
INSERT INTO `base_area` VALUES ('130372', '北戴河新区', '1303');
INSERT INTO `base_area` VALUES ('130402', '邯山区', '1304');
INSERT INTO `base_area` VALUES ('130403', '丛台区', '1304');
INSERT INTO `base_area` VALUES ('130404', '复兴区', '1304');
INSERT INTO `base_area` VALUES ('130406', '峰峰矿区', '1304');
INSERT INTO `base_area` VALUES ('130407', '肥乡区', '1304');
INSERT INTO `base_area` VALUES ('130408', '永年区', '1304');
INSERT INTO `base_area` VALUES ('130423', '临漳县', '1304');
INSERT INTO `base_area` VALUES ('130424', '成安县', '1304');
INSERT INTO `base_area` VALUES ('130425', '大名县', '1304');
INSERT INTO `base_area` VALUES ('130426', '涉县', '1304');
INSERT INTO `base_area` VALUES ('130427', '磁县', '1304');
INSERT INTO `base_area` VALUES ('130430', '邱县', '1304');
INSERT INTO `base_area` VALUES ('130431', '鸡泽县', '1304');
INSERT INTO `base_area` VALUES ('130432', '广平县', '1304');
INSERT INTO `base_area` VALUES ('130433', '馆陶县', '1304');
INSERT INTO `base_area` VALUES ('130434', '魏县', '1304');
INSERT INTO `base_area` VALUES ('130435', '曲周县', '1304');
INSERT INTO `base_area` VALUES ('130471', '邯郸经济技术开发区', '1304');
INSERT INTO `base_area` VALUES ('130473', '邯郸冀南新区', '1304');
INSERT INTO `base_area` VALUES ('130481', '武安市', '1304');
INSERT INTO `base_area` VALUES ('130502', '襄都区', '1305');
INSERT INTO `base_area` VALUES ('130503', '信都区', '1305');
INSERT INTO `base_area` VALUES ('130505', '任泽区', '1305');
INSERT INTO `base_area` VALUES ('130506', '南和区', '1305');
INSERT INTO `base_area` VALUES ('130522', '临城县', '1305');
INSERT INTO `base_area` VALUES ('130523', '内丘县', '1305');
INSERT INTO `base_area` VALUES ('130524', '柏乡县', '1305');
INSERT INTO `base_area` VALUES ('130525', '隆尧县', '1305');
INSERT INTO `base_area` VALUES ('130528', '宁晋县', '1305');
INSERT INTO `base_area` VALUES ('130529', '巨鹿县', '1305');
INSERT INTO `base_area` VALUES ('130530', '新河县', '1305');
INSERT INTO `base_area` VALUES ('130531', '广宗县', '1305');
INSERT INTO `base_area` VALUES ('130532', '平乡县', '1305');
INSERT INTO `base_area` VALUES ('130533', '威县', '1305');
INSERT INTO `base_area` VALUES ('130534', '清河县', '1305');
INSERT INTO `base_area` VALUES ('130535', '临西县', '1305');
INSERT INTO `base_area` VALUES ('130571', '河北邢台经济开发区', '1305');
INSERT INTO `base_area` VALUES ('130581', '南宫市', '1305');
INSERT INTO `base_area` VALUES ('130582', '沙河市', '1305');
INSERT INTO `base_area` VALUES ('130602', '竞秀区', '1306');
INSERT INTO `base_area` VALUES ('130606', '莲池区', '1306');
INSERT INTO `base_area` VALUES ('130607', '满城区', '1306');
INSERT INTO `base_area` VALUES ('130608', '清苑区', '1306');
INSERT INTO `base_area` VALUES ('130609', '徐水区', '1306');
INSERT INTO `base_area` VALUES ('130623', '涞水县', '1306');
INSERT INTO `base_area` VALUES ('130624', '阜平县', '1306');
INSERT INTO `base_area` VALUES ('130626', '定兴县', '1306');
INSERT INTO `base_area` VALUES ('130627', '唐县', '1306');
INSERT INTO `base_area` VALUES ('130628', '高阳县', '1306');
INSERT INTO `base_area` VALUES ('130629', '容城县', '1306');
INSERT INTO `base_area` VALUES ('130630', '涞源县', '1306');
INSERT INTO `base_area` VALUES ('130631', '望都县', '1306');
INSERT INTO `base_area` VALUES ('130632', '安新县', '1306');
INSERT INTO `base_area` VALUES ('130633', '易县', '1306');
INSERT INTO `base_area` VALUES ('130634', '曲阳县', '1306');
INSERT INTO `base_area` VALUES ('130635', '蠡县', '1306');
INSERT INTO `base_area` VALUES ('130636', '顺平县', '1306');
INSERT INTO `base_area` VALUES ('130637', '博野县', '1306');
INSERT INTO `base_area` VALUES ('130638', '雄县', '1306');
INSERT INTO `base_area` VALUES ('130671', '保定高新技术产业开发区', '1306');
INSERT INTO `base_area` VALUES ('130672', '保定白沟新城', '1306');
INSERT INTO `base_area` VALUES ('130681', '涿州市', '1306');
INSERT INTO `base_area` VALUES ('130682', '定州市', '1306');
INSERT INTO `base_area` VALUES ('130683', '安国市', '1306');
INSERT INTO `base_area` VALUES ('130684', '高碑店市', '1306');
INSERT INTO `base_area` VALUES ('130702', '桥东区', '1307');
INSERT INTO `base_area` VALUES ('130703', '桥西区', '1307');
INSERT INTO `base_area` VALUES ('130705', '宣化区', '1307');
INSERT INTO `base_area` VALUES ('130706', '下花园区', '1307');
INSERT INTO `base_area` VALUES ('130708', '万全区', '1307');
INSERT INTO `base_area` VALUES ('130709', '崇礼区', '1307');
INSERT INTO `base_area` VALUES ('130722', '张北县', '1307');
INSERT INTO `base_area` VALUES ('130723', '康保县', '1307');
INSERT INTO `base_area` VALUES ('130724', '沽源县', '1307');
INSERT INTO `base_area` VALUES ('130725', '尚义县', '1307');
INSERT INTO `base_area` VALUES ('130726', '蔚县', '1307');
INSERT INTO `base_area` VALUES ('130727', '阳原县', '1307');
INSERT INTO `base_area` VALUES ('130728', '怀安县', '1307');
INSERT INTO `base_area` VALUES ('130730', '怀来县', '1307');
INSERT INTO `base_area` VALUES ('130731', '涿鹿县', '1307');
INSERT INTO `base_area` VALUES ('130732', '赤城县', '1307');
INSERT INTO `base_area` VALUES ('130771', '张家口经济开发区', '1307');
INSERT INTO `base_area` VALUES ('130772', '张家口市察北管理区', '1307');
INSERT INTO `base_area` VALUES ('130773', '张家口市塞北管理区', '1307');
INSERT INTO `base_area` VALUES ('130802', '双桥区', '1308');
INSERT INTO `base_area` VALUES ('130803', '双滦区', '1308');
INSERT INTO `base_area` VALUES ('130804', '鹰手营子矿区', '1308');
INSERT INTO `base_area` VALUES ('130821', '承德县', '1308');
INSERT INTO `base_area` VALUES ('130822', '兴隆县', '1308');
INSERT INTO `base_area` VALUES ('130824', '滦平县', '1308');
INSERT INTO `base_area` VALUES ('130825', '隆化县', '1308');
INSERT INTO `base_area` VALUES ('130826', '丰宁满族自治县', '1308');
INSERT INTO `base_area` VALUES ('130827', '宽城满族自治县', '1308');
INSERT INTO `base_area` VALUES ('130828', '围场满族蒙古族自治县', '1308');
INSERT INTO `base_area` VALUES ('130871', '承德高新技术产业开发区', '1308');
INSERT INTO `base_area` VALUES ('130881', '平泉市', '1308');
INSERT INTO `base_area` VALUES ('130902', '新华区', '1309');
INSERT INTO `base_area` VALUES ('130903', '运河区', '1309');
INSERT INTO `base_area` VALUES ('130921', '沧县', '1309');
INSERT INTO `base_area` VALUES ('130922', '青县', '1309');
INSERT INTO `base_area` VALUES ('130923', '东光县', '1309');
INSERT INTO `base_area` VALUES ('130924', '海兴县', '1309');
INSERT INTO `base_area` VALUES ('130925', '盐山县', '1309');
INSERT INTO `base_area` VALUES ('130926', '肃宁县', '1309');
INSERT INTO `base_area` VALUES ('130927', '南皮县', '1309');
INSERT INTO `base_area` VALUES ('130928', '吴桥县', '1309');
INSERT INTO `base_area` VALUES ('130929', '献县', '1309');
INSERT INTO `base_area` VALUES ('130930', '孟村回族自治县', '1309');
INSERT INTO `base_area` VALUES ('130971', '河北沧州经济开发区', '1309');
INSERT INTO `base_area` VALUES ('130972', '沧州高新技术产业开发区', '1309');
INSERT INTO `base_area` VALUES ('130973', '沧州渤海新区', '1309');
INSERT INTO `base_area` VALUES ('130981', '泊头市', '1309');
INSERT INTO `base_area` VALUES ('130982', '任丘市', '1309');
INSERT INTO `base_area` VALUES ('130983', '黄骅市', '1309');
INSERT INTO `base_area` VALUES ('130984', '河间市', '1309');
INSERT INTO `base_area` VALUES ('131002', '安次区', '1310');
INSERT INTO `base_area` VALUES ('131003', '广阳区', '1310');
INSERT INTO `base_area` VALUES ('131022', '固安县', '1310');
INSERT INTO `base_area` VALUES ('131023', '永清县', '1310');
INSERT INTO `base_area` VALUES ('131024', '香河县', '1310');
INSERT INTO `base_area` VALUES ('131025', '大城县', '1310');
INSERT INTO `base_area` VALUES ('131026', '文安县', '1310');
INSERT INTO `base_area` VALUES ('131028', '大厂回族自治县', '1310');
INSERT INTO `base_area` VALUES ('131071', '廊坊经济技术开发区', '1310');
INSERT INTO `base_area` VALUES ('131081', '霸州市', '1310');
INSERT INTO `base_area` VALUES ('131082', '三河市', '1310');
INSERT INTO `base_area` VALUES ('131102', '桃城区', '1311');
INSERT INTO `base_area` VALUES ('131103', '冀州区', '1311');
INSERT INTO `base_area` VALUES ('131121', '枣强县', '1311');
INSERT INTO `base_area` VALUES ('131122', '武邑县', '1311');
INSERT INTO `base_area` VALUES ('131123', '武强县', '1311');
INSERT INTO `base_area` VALUES ('131124', '饶阳县', '1311');
INSERT INTO `base_area` VALUES ('131125', '安平县', '1311');
INSERT INTO `base_area` VALUES ('131126', '故城县', '1311');
INSERT INTO `base_area` VALUES ('131127', '景县', '1311');
INSERT INTO `base_area` VALUES ('131128', '阜城县', '1311');
INSERT INTO `base_area` VALUES ('131171', '河北衡水高新技术产业开发区', '1311');
INSERT INTO `base_area` VALUES ('131172', '衡水滨湖新区', '1311');
INSERT INTO `base_area` VALUES ('131182', '深州市', '1311');
INSERT INTO `base_area` VALUES ('140105', '小店区', '1401');
INSERT INTO `base_area` VALUES ('140106', '迎泽区', '1401');
INSERT INTO `base_area` VALUES ('140107', '杏花岭区', '1401');
INSERT INTO `base_area` VALUES ('140108', '尖草坪区', '1401');
INSERT INTO `base_area` VALUES ('140109', '万柏林区', '1401');
INSERT INTO `base_area` VALUES ('140110', '晋源区', '1401');
INSERT INTO `base_area` VALUES ('140121', '清徐县', '1401');
INSERT INTO `base_area` VALUES ('140122', '阳曲县', '1401');
INSERT INTO `base_area` VALUES ('140123', '娄烦县', '1401');
INSERT INTO `base_area` VALUES ('140171', '山西转型综合改革示范区', '1401');
INSERT INTO `base_area` VALUES ('140181', '古交市', '1401');
INSERT INTO `base_area` VALUES ('140212', '新荣区', '1402');
INSERT INTO `base_area` VALUES ('140213', '平城区', '1402');
INSERT INTO `base_area` VALUES ('140214', '云冈区', '1402');
INSERT INTO `base_area` VALUES ('140215', '云州区', '1402');
INSERT INTO `base_area` VALUES ('140221', '阳高县', '1402');
INSERT INTO `base_area` VALUES ('140222', '天镇县', '1402');
INSERT INTO `base_area` VALUES ('140223', '广灵县', '1402');
INSERT INTO `base_area` VALUES ('140224', '灵丘县', '1402');
INSERT INTO `base_area` VALUES ('140225', '浑源县', '1402');
INSERT INTO `base_area` VALUES ('140226', '左云县', '1402');
INSERT INTO `base_area` VALUES ('140271', '山西大同经济开发区', '1402');
INSERT INTO `base_area` VALUES ('140302', '城区', '1403');
INSERT INTO `base_area` VALUES ('140303', '矿区', '1403');
INSERT INTO `base_area` VALUES ('140311', '郊区', '1403');
INSERT INTO `base_area` VALUES ('140321', '平定县', '1403');
INSERT INTO `base_area` VALUES ('140322', '盂县', '1403');
INSERT INTO `base_area` VALUES ('140403', '潞州区', '1404');
INSERT INTO `base_area` VALUES ('140404', '上党区', '1404');
INSERT INTO `base_area` VALUES ('140405', '屯留区', '1404');
INSERT INTO `base_area` VALUES ('140406', '潞城区', '1404');
INSERT INTO `base_area` VALUES ('140423', '襄垣县', '1404');
INSERT INTO `base_area` VALUES ('140425', '平顺县', '1404');
INSERT INTO `base_area` VALUES ('140426', '黎城县', '1404');
INSERT INTO `base_area` VALUES ('140427', '壶关县', '1404');
INSERT INTO `base_area` VALUES ('140428', '长子县', '1404');
INSERT INTO `base_area` VALUES ('140429', '武乡县', '1404');
INSERT INTO `base_area` VALUES ('140430', '沁县', '1404');
INSERT INTO `base_area` VALUES ('140431', '沁源县', '1404');
INSERT INTO `base_area` VALUES ('140471', '山西长治高新技术产业园区', '1404');
INSERT INTO `base_area` VALUES ('140502', '城区', '1405');
INSERT INTO `base_area` VALUES ('140521', '沁水县', '1405');
INSERT INTO `base_area` VALUES ('140522', '阳城县', '1405');
INSERT INTO `base_area` VALUES ('140524', '陵川县', '1405');
INSERT INTO `base_area` VALUES ('140525', '泽州县', '1405');
INSERT INTO `base_area` VALUES ('140581', '高平市', '1405');
INSERT INTO `base_area` VALUES ('140602', '朔城区', '1406');
INSERT INTO `base_area` VALUES ('140603', '平鲁区', '1406');
INSERT INTO `base_area` VALUES ('140621', '山阴县', '1406');
INSERT INTO `base_area` VALUES ('140622', '应县', '1406');
INSERT INTO `base_area` VALUES ('140623', '右玉县', '1406');
INSERT INTO `base_area` VALUES ('140671', '山西朔州经济开发区', '1406');
INSERT INTO `base_area` VALUES ('140681', '怀仁市', '1406');
INSERT INTO `base_area` VALUES ('140702', '榆次区', '1407');
INSERT INTO `base_area` VALUES ('140703', '太谷区', '1407');
INSERT INTO `base_area` VALUES ('140721', '榆社县', '1407');
INSERT INTO `base_area` VALUES ('140722', '左权县', '1407');
INSERT INTO `base_area` VALUES ('140723', '和顺县', '1407');
INSERT INTO `base_area` VALUES ('140724', '昔阳县', '1407');
INSERT INTO `base_area` VALUES ('140725', '寿阳县', '1407');
INSERT INTO `base_area` VALUES ('140727', '祁县', '1407');
INSERT INTO `base_area` VALUES ('140728', '平遥县', '1407');
INSERT INTO `base_area` VALUES ('140729', '灵石县', '1407');
INSERT INTO `base_area` VALUES ('140781', '介休市', '1407');
INSERT INTO `base_area` VALUES ('140802', '盐湖区', '1408');
INSERT INTO `base_area` VALUES ('140821', '临猗县', '1408');
INSERT INTO `base_area` VALUES ('140822', '万荣县', '1408');
INSERT INTO `base_area` VALUES ('140823', '闻喜县', '1408');
INSERT INTO `base_area` VALUES ('140824', '稷山县', '1408');
INSERT INTO `base_area` VALUES ('140825', '新绛县', '1408');
INSERT INTO `base_area` VALUES ('140826', '绛县', '1408');
INSERT INTO `base_area` VALUES ('140827', '垣曲县', '1408');
INSERT INTO `base_area` VALUES ('140828', '夏县', '1408');
INSERT INTO `base_area` VALUES ('140829', '平陆县', '1408');
INSERT INTO `base_area` VALUES ('140830', '芮城县', '1408');
INSERT INTO `base_area` VALUES ('140881', '永济市', '1408');
INSERT INTO `base_area` VALUES ('140882', '河津市', '1408');
INSERT INTO `base_area` VALUES ('140902', '忻府区', '1409');
INSERT INTO `base_area` VALUES ('140921', '定襄县', '1409');
INSERT INTO `base_area` VALUES ('140922', '五台县', '1409');
INSERT INTO `base_area` VALUES ('140923', '代县', '1409');
INSERT INTO `base_area` VALUES ('140924', '繁峙县', '1409');
INSERT INTO `base_area` VALUES ('140925', '宁武县', '1409');
INSERT INTO `base_area` VALUES ('140926', '静乐县', '1409');
INSERT INTO `base_area` VALUES ('140927', '神池县', '1409');
INSERT INTO `base_area` VALUES ('140928', '五寨县', '1409');
INSERT INTO `base_area` VALUES ('140929', '岢岚县', '1409');
INSERT INTO `base_area` VALUES ('140930', '河曲县', '1409');
INSERT INTO `base_area` VALUES ('140931', '保德县', '1409');
INSERT INTO `base_area` VALUES ('140932', '偏关县', '1409');
INSERT INTO `base_area` VALUES ('140971', '五台山风景名胜区', '1409');
INSERT INTO `base_area` VALUES ('140981', '原平市', '1409');
INSERT INTO `base_area` VALUES ('141002', '尧都区', '1410');
INSERT INTO `base_area` VALUES ('141021', '曲沃县', '1410');
INSERT INTO `base_area` VALUES ('141022', '翼城县', '1410');
INSERT INTO `base_area` VALUES ('141023', '襄汾县', '1410');
INSERT INTO `base_area` VALUES ('141024', '洪洞县', '1410');
INSERT INTO `base_area` VALUES ('141025', '古县', '1410');
INSERT INTO `base_area` VALUES ('141026', '安泽县', '1410');
INSERT INTO `base_area` VALUES ('141027', '浮山县', '1410');
INSERT INTO `base_area` VALUES ('141028', '吉县', '1410');
INSERT INTO `base_area` VALUES ('141029', '乡宁县', '1410');
INSERT INTO `base_area` VALUES ('141030', '大宁县', '1410');
INSERT INTO `base_area` VALUES ('141031', '隰县', '1410');
INSERT INTO `base_area` VALUES ('141032', '永和县', '1410');
INSERT INTO `base_area` VALUES ('141033', '蒲县', '1410');
INSERT INTO `base_area` VALUES ('141034', '汾西县', '1410');
INSERT INTO `base_area` VALUES ('141081', '侯马市', '1410');
INSERT INTO `base_area` VALUES ('141082', '霍州市', '1410');
INSERT INTO `base_area` VALUES ('141102', '离石区', '1411');
INSERT INTO `base_area` VALUES ('141121', '文水县', '1411');
INSERT INTO `base_area` VALUES ('141122', '交城县', '1411');
INSERT INTO `base_area` VALUES ('141123', '兴县', '1411');
INSERT INTO `base_area` VALUES ('141124', '临县', '1411');
INSERT INTO `base_area` VALUES ('141125', '柳林县', '1411');
INSERT INTO `base_area` VALUES ('141126', '石楼县', '1411');
INSERT INTO `base_area` VALUES ('141127', '岚县', '1411');
INSERT INTO `base_area` VALUES ('141128', '方山县', '1411');
INSERT INTO `base_area` VALUES ('141129', '中阳县', '1411');
INSERT INTO `base_area` VALUES ('141130', '交口县', '1411');
INSERT INTO `base_area` VALUES ('141181', '孝义市', '1411');
INSERT INTO `base_area` VALUES ('141182', '汾阳市', '1411');
INSERT INTO `base_area` VALUES ('150102', '新城区', '1501');
INSERT INTO `base_area` VALUES ('150103', '回民区', '1501');
INSERT INTO `base_area` VALUES ('150104', '玉泉区', '1501');
INSERT INTO `base_area` VALUES ('150105', '赛罕区', '1501');
INSERT INTO `base_area` VALUES ('150121', '土默特左旗', '1501');
INSERT INTO `base_area` VALUES ('150122', '托克托县', '1501');
INSERT INTO `base_area` VALUES ('150123', '和林格尔县', '1501');
INSERT INTO `base_area` VALUES ('150124', '清水河县', '1501');
INSERT INTO `base_area` VALUES ('150125', '武川县', '1501');
INSERT INTO `base_area` VALUES ('150172', '呼和浩特经济技术开发区', '1501');
INSERT INTO `base_area` VALUES ('150202', '东河区', '1502');
INSERT INTO `base_area` VALUES ('150203', '昆都仑区', '1502');
INSERT INTO `base_area` VALUES ('150204', '青山区', '1502');
INSERT INTO `base_area` VALUES ('150205', '石拐区', '1502');
INSERT INTO `base_area` VALUES ('150206', '白云鄂博矿区', '1502');
INSERT INTO `base_area` VALUES ('150207', '九原区', '1502');
INSERT INTO `base_area` VALUES ('150221', '土默特右旗', '1502');
INSERT INTO `base_area` VALUES ('150222', '固阳县', '1502');
INSERT INTO `base_area` VALUES ('150223', '达尔罕茂明安联合旗', '1502');
INSERT INTO `base_area` VALUES ('150271', '包头稀土高新技术产业开发区', '1502');
INSERT INTO `base_area` VALUES ('150302', '海勃湾区', '1503');
INSERT INTO `base_area` VALUES ('150303', '海南区', '1503');
INSERT INTO `base_area` VALUES ('150304', '乌达区', '1503');
INSERT INTO `base_area` VALUES ('150402', '红山区', '1504');
INSERT INTO `base_area` VALUES ('150403', '元宝山区', '1504');
INSERT INTO `base_area` VALUES ('150404', '松山区', '1504');
INSERT INTO `base_area` VALUES ('150421', '阿鲁科尔沁旗', '1504');
INSERT INTO `base_area` VALUES ('150422', '巴林左旗', '1504');
INSERT INTO `base_area` VALUES ('150423', '巴林右旗', '1504');
INSERT INTO `base_area` VALUES ('150424', '林西县', '1504');
INSERT INTO `base_area` VALUES ('150425', '克什克腾旗', '1504');
INSERT INTO `base_area` VALUES ('150426', '翁牛特旗', '1504');
INSERT INTO `base_area` VALUES ('150428', '喀喇沁旗', '1504');
INSERT INTO `base_area` VALUES ('150429', '宁城县', '1504');
INSERT INTO `base_area` VALUES ('150430', '敖汉旗', '1504');
INSERT INTO `base_area` VALUES ('150502', '科尔沁区', '1505');
INSERT INTO `base_area` VALUES ('150521', '科尔沁左翼中旗', '1505');
INSERT INTO `base_area` VALUES ('150522', '科尔沁左翼后旗', '1505');
INSERT INTO `base_area` VALUES ('150523', '开鲁县', '1505');
INSERT INTO `base_area` VALUES ('150524', '库伦旗', '1505');
INSERT INTO `base_area` VALUES ('150525', '奈曼旗', '1505');
INSERT INTO `base_area` VALUES ('150526', '扎鲁特旗', '1505');
INSERT INTO `base_area` VALUES ('150571', '通辽经济技术开发区', '1505');
INSERT INTO `base_area` VALUES ('150581', '霍林郭勒市', '1505');
INSERT INTO `base_area` VALUES ('150602', '东胜区', '1506');
INSERT INTO `base_area` VALUES ('150603', '康巴什区', '1506');
INSERT INTO `base_area` VALUES ('150621', '达拉特旗', '1506');
INSERT INTO `base_area` VALUES ('150622', '准格尔旗', '1506');
INSERT INTO `base_area` VALUES ('150623', '鄂托克前旗', '1506');
INSERT INTO `base_area` VALUES ('150624', '鄂托克旗', '1506');
INSERT INTO `base_area` VALUES ('150625', '杭锦旗', '1506');
INSERT INTO `base_area` VALUES ('150626', '乌审旗', '1506');
INSERT INTO `base_area` VALUES ('150627', '伊金霍洛旗', '1506');
INSERT INTO `base_area` VALUES ('150702', '海拉尔区', '1507');
INSERT INTO `base_area` VALUES ('150703', '扎赉诺尔区', '1507');
INSERT INTO `base_area` VALUES ('150721', '阿荣旗', '1507');
INSERT INTO `base_area` VALUES ('150722', '莫力达瓦达斡尔族自治旗', '1507');
INSERT INTO `base_area` VALUES ('150723', '鄂伦春自治旗', '1507');
INSERT INTO `base_area` VALUES ('150724', '鄂温克族自治旗', '1507');
INSERT INTO `base_area` VALUES ('150725', '陈巴尔虎旗', '1507');
INSERT INTO `base_area` VALUES ('150726', '新巴尔虎左旗', '1507');
INSERT INTO `base_area` VALUES ('150727', '新巴尔虎右旗', '1507');
INSERT INTO `base_area` VALUES ('150781', '满洲里市', '1507');
INSERT INTO `base_area` VALUES ('150782', '牙克石市', '1507');
INSERT INTO `base_area` VALUES ('150783', '扎兰屯市', '1507');
INSERT INTO `base_area` VALUES ('150784', '额尔古纳市', '1507');
INSERT INTO `base_area` VALUES ('150785', '根河市', '1507');
INSERT INTO `base_area` VALUES ('150802', '临河区', '1508');
INSERT INTO `base_area` VALUES ('150821', '五原县', '1508');
INSERT INTO `base_area` VALUES ('150822', '磴口县', '1508');
INSERT INTO `base_area` VALUES ('150823', '乌拉特前旗', '1508');
INSERT INTO `base_area` VALUES ('150824', '乌拉特中旗', '1508');
INSERT INTO `base_area` VALUES ('150825', '乌拉特后旗', '1508');
INSERT INTO `base_area` VALUES ('150826', '杭锦后旗', '1508');
INSERT INTO `base_area` VALUES ('150902', '集宁区', '1509');
INSERT INTO `base_area` VALUES ('150921', '卓资县', '1509');
INSERT INTO `base_area` VALUES ('150922', '化德县', '1509');
INSERT INTO `base_area` VALUES ('150923', '商都县', '1509');
INSERT INTO `base_area` VALUES ('150924', '兴和县', '1509');
INSERT INTO `base_area` VALUES ('150925', '凉城县', '1509');
INSERT INTO `base_area` VALUES ('150926', '察哈尔右翼前旗', '1509');
INSERT INTO `base_area` VALUES ('150927', '察哈尔右翼中旗', '1509');
INSERT INTO `base_area` VALUES ('150928', '察哈尔右翼后旗', '1509');
INSERT INTO `base_area` VALUES ('150929', '四子王旗', '1509');
INSERT INTO `base_area` VALUES ('150981', '丰镇市', '1509');
INSERT INTO `base_area` VALUES ('152201', '乌兰浩特市', '1522');
INSERT INTO `base_area` VALUES ('152202', '阿尔山市', '1522');
INSERT INTO `base_area` VALUES ('152221', '科尔沁右翼前旗', '1522');
INSERT INTO `base_area` VALUES ('152222', '科尔沁右翼中旗', '1522');
INSERT INTO `base_area` VALUES ('152223', '扎赉特旗', '1522');
INSERT INTO `base_area` VALUES ('152224', '突泉县', '1522');
INSERT INTO `base_area` VALUES ('152501', '二连浩特市', '1525');
INSERT INTO `base_area` VALUES ('152502', '锡林浩特市', '1525');
INSERT INTO `base_area` VALUES ('152522', '阿巴嘎旗', '1525');
INSERT INTO `base_area` VALUES ('152523', '苏尼特左旗', '1525');
INSERT INTO `base_area` VALUES ('152524', '苏尼特右旗', '1525');
INSERT INTO `base_area` VALUES ('152525', '东乌珠穆沁旗', '1525');
INSERT INTO `base_area` VALUES ('152526', '西乌珠穆沁旗', '1525');
INSERT INTO `base_area` VALUES ('152527', '太仆寺旗', '1525');
INSERT INTO `base_area` VALUES ('152528', '镶黄旗', '1525');
INSERT INTO `base_area` VALUES ('152529', '正镶白旗', '1525');
INSERT INTO `base_area` VALUES ('152530', '正蓝旗', '1525');
INSERT INTO `base_area` VALUES ('152531', '多伦县', '1525');
INSERT INTO `base_area` VALUES ('152571', '乌拉盖管委会', '1525');
INSERT INTO `base_area` VALUES ('152921', '阿拉善左旗', '1529');
INSERT INTO `base_area` VALUES ('152922', '阿拉善右旗', '1529');
INSERT INTO `base_area` VALUES ('152923', '额济纳旗', '1529');
INSERT INTO `base_area` VALUES ('152971', '内蒙古阿拉善高新技术产业开发区', '1529');
INSERT INTO `base_area` VALUES ('210102', '和平区', '2101');
INSERT INTO `base_area` VALUES ('210103', '沈河区', '2101');
INSERT INTO `base_area` VALUES ('210104', '大东区', '2101');
INSERT INTO `base_area` VALUES ('210105', '皇姑区', '2101');
INSERT INTO `base_area` VALUES ('210106', '铁西区', '2101');
INSERT INTO `base_area` VALUES ('210111', '苏家屯区', '2101');
INSERT INTO `base_area` VALUES ('210112', '浑南区', '2101');
INSERT INTO `base_area` VALUES ('210113', '沈北新区', '2101');
INSERT INTO `base_area` VALUES ('210114', '于洪区', '2101');
INSERT INTO `base_area` VALUES ('210115', '辽中区', '2101');
INSERT INTO `base_area` VALUES ('210123', '康平县', '2101');
INSERT INTO `base_area` VALUES ('210124', '法库县', '2101');
INSERT INTO `base_area` VALUES ('210181', '新民市', '2101');
INSERT INTO `base_area` VALUES ('210202', '中山区', '2102');
INSERT INTO `base_area` VALUES ('210203', '西岗区', '2102');
INSERT INTO `base_area` VALUES ('210204', '沙河口区', '2102');
INSERT INTO `base_area` VALUES ('210211', '甘井子区', '2102');
INSERT INTO `base_area` VALUES ('210212', '旅顺口区', '2102');
INSERT INTO `base_area` VALUES ('210213', '金州区', '2102');
INSERT INTO `base_area` VALUES ('210214', '普兰店区', '2102');
INSERT INTO `base_area` VALUES ('210224', '长海县', '2102');
INSERT INTO `base_area` VALUES ('210281', '瓦房店市', '2102');
INSERT INTO `base_area` VALUES ('210283', '庄河市', '2102');
INSERT INTO `base_area` VALUES ('210302', '铁东区', '2103');
INSERT INTO `base_area` VALUES ('210303', '铁西区', '2103');
INSERT INTO `base_area` VALUES ('210304', '立山区', '2103');
INSERT INTO `base_area` VALUES ('210311', '千山区', '2103');
INSERT INTO `base_area` VALUES ('210321', '台安县', '2103');
INSERT INTO `base_area` VALUES ('210323', '岫岩满族自治县', '2103');
INSERT INTO `base_area` VALUES ('210381', '海城市', '2103');
INSERT INTO `base_area` VALUES ('210402', '新抚区', '2104');
INSERT INTO `base_area` VALUES ('210403', '东洲区', '2104');
INSERT INTO `base_area` VALUES ('210404', '望花区', '2104');
INSERT INTO `base_area` VALUES ('210411', '顺城区', '2104');
INSERT INTO `base_area` VALUES ('210421', '抚顺县', '2104');
INSERT INTO `base_area` VALUES ('210422', '新宾满族自治县', '2104');
INSERT INTO `base_area` VALUES ('210423', '清原满族自治县', '2104');
INSERT INTO `base_area` VALUES ('210502', '平山区', '2105');
INSERT INTO `base_area` VALUES ('210503', '溪湖区', '2105');
INSERT INTO `base_area` VALUES ('210504', '明山区', '2105');
INSERT INTO `base_area` VALUES ('210505', '南芬区', '2105');
INSERT INTO `base_area` VALUES ('210521', '本溪满族自治县', '2105');
INSERT INTO `base_area` VALUES ('210522', '桓仁满族自治县', '2105');
INSERT INTO `base_area` VALUES ('210602', '元宝区', '2106');
INSERT INTO `base_area` VALUES ('210603', '振兴区', '2106');
INSERT INTO `base_area` VALUES ('210604', '振安区', '2106');
INSERT INTO `base_area` VALUES ('210624', '宽甸满族自治县', '2106');
INSERT INTO `base_area` VALUES ('210681', '东港市', '2106');
INSERT INTO `base_area` VALUES ('210682', '凤城市', '2106');
INSERT INTO `base_area` VALUES ('210702', '古塔区', '2107');
INSERT INTO `base_area` VALUES ('210703', '凌河区', '2107');
INSERT INTO `base_area` VALUES ('210711', '太和区', '2107');
INSERT INTO `base_area` VALUES ('210726', '黑山县', '2107');
INSERT INTO `base_area` VALUES ('210727', '义县', '2107');
INSERT INTO `base_area` VALUES ('210781', '凌海市', '2107');
INSERT INTO `base_area` VALUES ('210782', '北镇市', '2107');
INSERT INTO `base_area` VALUES ('210802', '站前区', '2108');
INSERT INTO `base_area` VALUES ('210803', '西市区', '2108');
INSERT INTO `base_area` VALUES ('210804', '鲅鱼圈区', '2108');
INSERT INTO `base_area` VALUES ('210811', '老边区', '2108');
INSERT INTO `base_area` VALUES ('210881', '盖州市', '2108');
INSERT INTO `base_area` VALUES ('210882', '大石桥市', '2108');
INSERT INTO `base_area` VALUES ('210902', '海州区', '2109');
INSERT INTO `base_area` VALUES ('210903', '新邱区', '2109');
INSERT INTO `base_area` VALUES ('210904', '太平区', '2109');
INSERT INTO `base_area` VALUES ('210905', '清河门区', '2109');
INSERT INTO `base_area` VALUES ('210911', '细河区', '2109');
INSERT INTO `base_area` VALUES ('210921', '阜新蒙古族自治县', '2109');
INSERT INTO `base_area` VALUES ('210922', '彰武县', '2109');
INSERT INTO `base_area` VALUES ('211002', '白塔区', '2110');
INSERT INTO `base_area` VALUES ('211003', '文圣区', '2110');
INSERT INTO `base_area` VALUES ('211004', '宏伟区', '2110');
INSERT INTO `base_area` VALUES ('211005', '弓长岭区', '2110');
INSERT INTO `base_area` VALUES ('211011', '太子河区', '2110');
INSERT INTO `base_area` VALUES ('211021', '辽阳县', '2110');
INSERT INTO `base_area` VALUES ('211081', '灯塔市', '2110');
INSERT INTO `base_area` VALUES ('211102', '双台子区', '2111');
INSERT INTO `base_area` VALUES ('211103', '兴隆台区', '2111');
INSERT INTO `base_area` VALUES ('211104', '大洼区', '2111');
INSERT INTO `base_area` VALUES ('211122', '盘山县', '2111');
INSERT INTO `base_area` VALUES ('211202', '银州区', '2112');
INSERT INTO `base_area` VALUES ('211204', '清河区', '2112');
INSERT INTO `base_area` VALUES ('211221', '铁岭县', '2112');
INSERT INTO `base_area` VALUES ('211223', '西丰县', '2112');
INSERT INTO `base_area` VALUES ('211224', '昌图县', '2112');
INSERT INTO `base_area` VALUES ('211281', '调兵山市', '2112');
INSERT INTO `base_area` VALUES ('211282', '开原市', '2112');
INSERT INTO `base_area` VALUES ('211302', '双塔区', '2113');
INSERT INTO `base_area` VALUES ('211303', '龙城区', '2113');
INSERT INTO `base_area` VALUES ('211321', '朝阳县', '2113');
INSERT INTO `base_area` VALUES ('211322', '建平县', '2113');
INSERT INTO `base_area` VALUES ('211324', '喀喇沁左翼蒙古族自治县', '2113');
INSERT INTO `base_area` VALUES ('211381', '北票市', '2113');
INSERT INTO `base_area` VALUES ('211382', '凌源市', '2113');
INSERT INTO `base_area` VALUES ('211402', '连山区', '2114');
INSERT INTO `base_area` VALUES ('211403', '龙港区', '2114');
INSERT INTO `base_area` VALUES ('211404', '南票区', '2114');
INSERT INTO `base_area` VALUES ('211421', '绥中县', '2114');
INSERT INTO `base_area` VALUES ('211422', '建昌县', '2114');
INSERT INTO `base_area` VALUES ('211481', '兴城市', '2114');
INSERT INTO `base_area` VALUES ('220102', '南关区', '2201');
INSERT INTO `base_area` VALUES ('220103', '宽城区', '2201');
INSERT INTO `base_area` VALUES ('220104', '朝阳区', '2201');
INSERT INTO `base_area` VALUES ('220105', '二道区', '2201');
INSERT INTO `base_area` VALUES ('220106', '绿园区', '2201');
INSERT INTO `base_area` VALUES ('220112', '双阳区', '2201');
INSERT INTO `base_area` VALUES ('220113', '九台区', '2201');
INSERT INTO `base_area` VALUES ('220122', '农安县', '2201');
INSERT INTO `base_area` VALUES ('220171', '长春经济技术开发区', '2201');
INSERT INTO `base_area` VALUES ('220172', '长春净月高新技术产业开发区', '2201');
INSERT INTO `base_area` VALUES ('220173', '长春高新技术产业开发区', '2201');
INSERT INTO `base_area` VALUES ('220174', '长春汽车经济技术开发区', '2201');
INSERT INTO `base_area` VALUES ('220182', '榆树市', '2201');
INSERT INTO `base_area` VALUES ('220183', '德惠市', '2201');
INSERT INTO `base_area` VALUES ('220184', '公主岭市', '2201');
INSERT INTO `base_area` VALUES ('220202', '昌邑区', '2202');
INSERT INTO `base_area` VALUES ('220203', '龙潭区', '2202');
INSERT INTO `base_area` VALUES ('220204', '船营区', '2202');
INSERT INTO `base_area` VALUES ('220211', '丰满区', '2202');
INSERT INTO `base_area` VALUES ('220221', '永吉县', '2202');
INSERT INTO `base_area` VALUES ('220271', '吉林经济开发区', '2202');
INSERT INTO `base_area` VALUES ('220272', '吉林高新技术产业开发区', '2202');
INSERT INTO `base_area` VALUES ('220273', '吉林中国新加坡食品区', '2202');
INSERT INTO `base_area` VALUES ('220281', '蛟河市', '2202');
INSERT INTO `base_area` VALUES ('220282', '桦甸市', '2202');
INSERT INTO `base_area` VALUES ('220283', '舒兰市', '2202');
INSERT INTO `base_area` VALUES ('220284', '磐石市', '2202');
INSERT INTO `base_area` VALUES ('220302', '铁西区', '2203');
INSERT INTO `base_area` VALUES ('220303', '铁东区', '2203');
INSERT INTO `base_area` VALUES ('220322', '梨树县', '2203');
INSERT INTO `base_area` VALUES ('220323', '伊通满族自治县', '2203');
INSERT INTO `base_area` VALUES ('220382', '双辽市', '2203');
INSERT INTO `base_area` VALUES ('220402', '龙山区', '2204');
INSERT INTO `base_area` VALUES ('220403', '西安区', '2204');
INSERT INTO `base_area` VALUES ('220421', '东丰县', '2204');
INSERT INTO `base_area` VALUES ('220422', '东辽县', '2204');
INSERT INTO `base_area` VALUES ('220502', '东昌区', '2205');
INSERT INTO `base_area` VALUES ('220503', '二道江区', '2205');
INSERT INTO `base_area` VALUES ('220521', '通化县', '2205');
INSERT INTO `base_area` VALUES ('220523', '辉南县', '2205');
INSERT INTO `base_area` VALUES ('220524', '柳河县', '2205');
INSERT INTO `base_area` VALUES ('220581', '梅河口市', '2205');
INSERT INTO `base_area` VALUES ('220582', '集安市', '2205');
INSERT INTO `base_area` VALUES ('220602', '浑江区', '2206');
INSERT INTO `base_area` VALUES ('220605', '江源区', '2206');
INSERT INTO `base_area` VALUES ('220621', '抚松县', '2206');
INSERT INTO `base_area` VALUES ('220622', '靖宇县', '2206');
INSERT INTO `base_area` VALUES ('220623', '长白朝鲜族自治县', '2206');
INSERT INTO `base_area` VALUES ('220681', '临江市', '2206');
INSERT INTO `base_area` VALUES ('220702', '宁江区', '2207');
INSERT INTO `base_area` VALUES ('220721', '前郭尔罗斯蒙古族自治县', '2207');
INSERT INTO `base_area` VALUES ('220722', '长岭县', '2207');
INSERT INTO `base_area` VALUES ('220723', '乾安县', '2207');
INSERT INTO `base_area` VALUES ('220771', '吉林松原经济开发区', '2207');
INSERT INTO `base_area` VALUES ('220781', '扶余市', '2207');
INSERT INTO `base_area` VALUES ('220802', '洮北区', '2208');
INSERT INTO `base_area` VALUES ('220821', '镇赉县', '2208');
INSERT INTO `base_area` VALUES ('220822', '通榆县', '2208');
INSERT INTO `base_area` VALUES ('220871', '吉林白城经济开发区', '2208');
INSERT INTO `base_area` VALUES ('220881', '洮南市', '2208');
INSERT INTO `base_area` VALUES ('220882', '大安市', '2208');
INSERT INTO `base_area` VALUES ('222401', '延吉市', '2224');
INSERT INTO `base_area` VALUES ('222402', '图们市', '2224');
INSERT INTO `base_area` VALUES ('222403', '敦化市', '2224');
INSERT INTO `base_area` VALUES ('222404', '珲春市', '2224');
INSERT INTO `base_area` VALUES ('222405', '龙井市', '2224');
INSERT INTO `base_area` VALUES ('222406', '和龙市', '2224');
INSERT INTO `base_area` VALUES ('222424', '汪清县', '2224');
INSERT INTO `base_area` VALUES ('222426', '安图县', '2224');
INSERT INTO `base_area` VALUES ('230102', '道里区', '2301');
INSERT INTO `base_area` VALUES ('230103', '南岗区', '2301');
INSERT INTO `base_area` VALUES ('230104', '道外区', '2301');
INSERT INTO `base_area` VALUES ('230108', '平房区', '2301');
INSERT INTO `base_area` VALUES ('230109', '松北区', '2301');
INSERT INTO `base_area` VALUES ('230110', '香坊区', '2301');
INSERT INTO `base_area` VALUES ('230111', '呼兰区', '2301');
INSERT INTO `base_area` VALUES ('230112', '阿城区', '2301');
INSERT INTO `base_area` VALUES ('230113', '双城区', '2301');
INSERT INTO `base_area` VALUES ('230123', '依兰县', '2301');
INSERT INTO `base_area` VALUES ('230124', '方正县', '2301');
INSERT INTO `base_area` VALUES ('230125', '宾县', '2301');
INSERT INTO `base_area` VALUES ('230126', '巴彦县', '2301');
INSERT INTO `base_area` VALUES ('230127', '木兰县', '2301');
INSERT INTO `base_area` VALUES ('230128', '通河县', '2301');
INSERT INTO `base_area` VALUES ('230129', '延寿县', '2301');
INSERT INTO `base_area` VALUES ('230183', '尚志市', '2301');
INSERT INTO `base_area` VALUES ('230184', '五常市', '2301');
INSERT INTO `base_area` VALUES ('230202', '龙沙区', '2302');
INSERT INTO `base_area` VALUES ('230203', '建华区', '2302');
INSERT INTO `base_area` VALUES ('230204', '铁锋区', '2302');
INSERT INTO `base_area` VALUES ('230205', '昂昂溪区', '2302');
INSERT INTO `base_area` VALUES ('230206', '富拉尔基区', '2302');
INSERT INTO `base_area` VALUES ('230207', '碾子山区', '2302');
INSERT INTO `base_area` VALUES ('230208', '梅里斯达斡尔族区', '2302');
INSERT INTO `base_area` VALUES ('230221', '龙江县', '2302');
INSERT INTO `base_area` VALUES ('230223', '依安县', '2302');
INSERT INTO `base_area` VALUES ('230224', '泰来县', '2302');
INSERT INTO `base_area` VALUES ('230225', '甘南县', '2302');
INSERT INTO `base_area` VALUES ('230227', '富裕县', '2302');
INSERT INTO `base_area` VALUES ('230229', '克山县', '2302');
INSERT INTO `base_area` VALUES ('230230', '克东县', '2302');
INSERT INTO `base_area` VALUES ('230231', '拜泉县', '2302');
INSERT INTO `base_area` VALUES ('230281', '讷河市', '2302');
INSERT INTO `base_area` VALUES ('230302', '鸡冠区', '2303');
INSERT INTO `base_area` VALUES ('230303', '恒山区', '2303');
INSERT INTO `base_area` VALUES ('230304', '滴道区', '2303');
INSERT INTO `base_area` VALUES ('230305', '梨树区', '2303');
INSERT INTO `base_area` VALUES ('230306', '城子河区', '2303');
INSERT INTO `base_area` VALUES ('230307', '麻山区', '2303');
INSERT INTO `base_area` VALUES ('230321', '鸡东县', '2303');
INSERT INTO `base_area` VALUES ('230381', '虎林市', '2303');
INSERT INTO `base_area` VALUES ('230382', '密山市', '2303');
INSERT INTO `base_area` VALUES ('230402', '向阳区', '2304');
INSERT INTO `base_area` VALUES ('230403', '工农区', '2304');
INSERT INTO `base_area` VALUES ('230404', '南山区', '2304');
INSERT INTO `base_area` VALUES ('230405', '兴安区', '2304');
INSERT INTO `base_area` VALUES ('230406', '东山区', '2304');
INSERT INTO `base_area` VALUES ('230407', '兴山区', '2304');
INSERT INTO `base_area` VALUES ('230421', '萝北县', '2304');
INSERT INTO `base_area` VALUES ('230422', '绥滨县', '2304');
INSERT INTO `base_area` VALUES ('230502', '尖山区', '2305');
INSERT INTO `base_area` VALUES ('230503', '岭东区', '2305');
INSERT INTO `base_area` VALUES ('230505', '四方台区', '2305');
INSERT INTO `base_area` VALUES ('230506', '宝山区', '2305');
INSERT INTO `base_area` VALUES ('230521', '集贤县', '2305');
INSERT INTO `base_area` VALUES ('230522', '友谊县', '2305');
INSERT INTO `base_area` VALUES ('230523', '宝清县', '2305');
INSERT INTO `base_area` VALUES ('230524', '饶河县', '2305');
INSERT INTO `base_area` VALUES ('230602', '萨尔图区', '2306');
INSERT INTO `base_area` VALUES ('230603', '龙凤区', '2306');
INSERT INTO `base_area` VALUES ('230604', '让胡路区', '2306');
INSERT INTO `base_area` VALUES ('230605', '红岗区', '2306');
INSERT INTO `base_area` VALUES ('230606', '大同区', '2306');
INSERT INTO `base_area` VALUES ('230621', '肇州县', '2306');
INSERT INTO `base_area` VALUES ('230622', '肇源县', '2306');
INSERT INTO `base_area` VALUES ('230623', '林甸县', '2306');
INSERT INTO `base_area` VALUES ('230624', '杜尔伯特蒙古族自治县', '2306');
INSERT INTO `base_area` VALUES ('230671', '大庆高新技术产业开发区', '2306');
INSERT INTO `base_area` VALUES ('230717', '伊美区', '2307');
INSERT INTO `base_area` VALUES ('230718', '乌翠区', '2307');
INSERT INTO `base_area` VALUES ('230719', '友好区', '2307');
INSERT INTO `base_area` VALUES ('230722', '嘉荫县', '2307');
INSERT INTO `base_area` VALUES ('230723', '汤旺县', '2307');
INSERT INTO `base_area` VALUES ('230724', '丰林县', '2307');
INSERT INTO `base_area` VALUES ('230725', '大箐山县', '2307');
INSERT INTO `base_area` VALUES ('230726', '南岔县', '2307');
INSERT INTO `base_area` VALUES ('230751', '金林区', '2307');
INSERT INTO `base_area` VALUES ('230781', '铁力市', '2307');
INSERT INTO `base_area` VALUES ('230803', '向阳区', '2308');
INSERT INTO `base_area` VALUES ('230804', '前进区', '2308');
INSERT INTO `base_area` VALUES ('230805', '东风区', '2308');
INSERT INTO `base_area` VALUES ('230811', '郊区', '2308');
INSERT INTO `base_area` VALUES ('230822', '桦南县', '2308');
INSERT INTO `base_area` VALUES ('230826', '桦川县', '2308');
INSERT INTO `base_area` VALUES ('230828', '汤原县', '2308');
INSERT INTO `base_area` VALUES ('230881', '同江市', '2308');
INSERT INTO `base_area` VALUES ('230882', '富锦市', '2308');
INSERT INTO `base_area` VALUES ('230883', '抚远市', '2308');
INSERT INTO `base_area` VALUES ('230902', '新兴区', '2309');
INSERT INTO `base_area` VALUES ('230903', '桃山区', '2309');
INSERT INTO `base_area` VALUES ('230904', '茄子河区', '2309');
INSERT INTO `base_area` VALUES ('230921', '勃利县', '2309');
INSERT INTO `base_area` VALUES ('231002', '东安区', '2310');
INSERT INTO `base_area` VALUES ('231003', '阳明区', '2310');
INSERT INTO `base_area` VALUES ('231004', '爱民区', '2310');
INSERT INTO `base_area` VALUES ('231005', '西安区', '2310');
INSERT INTO `base_area` VALUES ('231025', '林口县', '2310');
INSERT INTO `base_area` VALUES ('231071', '牡丹江经济技术开发区', '2310');
INSERT INTO `base_area` VALUES ('231081', '绥芬河市', '2310');
INSERT INTO `base_area` VALUES ('231083', '海林市', '2310');
INSERT INTO `base_area` VALUES ('231084', '宁安市', '2310');
INSERT INTO `base_area` VALUES ('231085', '穆棱市', '2310');
INSERT INTO `base_area` VALUES ('231086', '东宁市', '2310');
INSERT INTO `base_area` VALUES ('231102', '爱辉区', '2311');
INSERT INTO `base_area` VALUES ('231123', '逊克县', '2311');
INSERT INTO `base_area` VALUES ('231124', '孙吴县', '2311');
INSERT INTO `base_area` VALUES ('231181', '北安市', '2311');
INSERT INTO `base_area` VALUES ('231182', '五大连池市', '2311');
INSERT INTO `base_area` VALUES ('231183', '嫩江市', '2311');
INSERT INTO `base_area` VALUES ('231202', '北林区', '2312');
INSERT INTO `base_area` VALUES ('231221', '望奎县', '2312');
INSERT INTO `base_area` VALUES ('231222', '兰西县', '2312');
INSERT INTO `base_area` VALUES ('231223', '青冈县', '2312');
INSERT INTO `base_area` VALUES ('231224', '庆安县', '2312');
INSERT INTO `base_area` VALUES ('231225', '明水县', '2312');
INSERT INTO `base_area` VALUES ('231226', '绥棱县', '2312');
INSERT INTO `base_area` VALUES ('231281', '安达市', '2312');
INSERT INTO `base_area` VALUES ('231282', '肇东市', '2312');
INSERT INTO `base_area` VALUES ('231283', '海伦市', '2312');
INSERT INTO `base_area` VALUES ('232701', '漠河市', '2327');
INSERT INTO `base_area` VALUES ('232721', '呼玛县', '2327');
INSERT INTO `base_area` VALUES ('232722', '塔河县', '2327');
INSERT INTO `base_area` VALUES ('232761', '加格达奇区', '2327');
INSERT INTO `base_area` VALUES ('232762', '松岭区', '2327');
INSERT INTO `base_area` VALUES ('232763', '新林区', '2327');
INSERT INTO `base_area` VALUES ('232764', '呼中区', '2327');
INSERT INTO `base_area` VALUES ('310101', '黄浦区', '3101');
INSERT INTO `base_area` VALUES ('310104', '徐汇区', '3101');
INSERT INTO `base_area` VALUES ('310105', '长宁区', '3101');
INSERT INTO `base_area` VALUES ('310106', '静安区', '3101');
INSERT INTO `base_area` VALUES ('310107', '普陀区', '3101');
INSERT INTO `base_area` VALUES ('310109', '虹口区', '3101');
INSERT INTO `base_area` VALUES ('310110', '杨浦区', '3101');
INSERT INTO `base_area` VALUES ('310112', '闵行区', '3101');
INSERT INTO `base_area` VALUES ('310113', '宝山区', '3101');
INSERT INTO `base_area` VALUES ('310114', '嘉定区', '3101');
INSERT INTO `base_area` VALUES ('310115', '浦东新区', '3101');
INSERT INTO `base_area` VALUES ('310116', '金山区', '3101');
INSERT INTO `base_area` VALUES ('310117', '松江区', '3101');
INSERT INTO `base_area` VALUES ('310118', '青浦区', '3101');
INSERT INTO `base_area` VALUES ('310120', '奉贤区', '3101');
INSERT INTO `base_area` VALUES ('310151', '崇明区', '3101');
INSERT INTO `base_area` VALUES ('320102', '玄武区', '3201');
INSERT INTO `base_area` VALUES ('320104', '秦淮区', '3201');
INSERT INTO `base_area` VALUES ('320105', '建邺区', '3201');
INSERT INTO `base_area` VALUES ('320106', '鼓楼区', '3201');
INSERT INTO `base_area` VALUES ('320111', '浦口区', '3201');
INSERT INTO `base_area` VALUES ('320113', '栖霞区', '3201');
INSERT INTO `base_area` VALUES ('320114', '雨花台区', '3201');
INSERT INTO `base_area` VALUES ('320115', '江宁区', '3201');
INSERT INTO `base_area` VALUES ('320116', '六合区', '3201');
INSERT INTO `base_area` VALUES ('320117', '溧水区', '3201');
INSERT INTO `base_area` VALUES ('320118', '高淳区', '3201');
INSERT INTO `base_area` VALUES ('320205', '锡山区', '3202');
INSERT INTO `base_area` VALUES ('320206', '惠山区', '3202');
INSERT INTO `base_area` VALUES ('320211', '滨湖区', '3202');
INSERT INTO `base_area` VALUES ('320213', '梁溪区', '3202');
INSERT INTO `base_area` VALUES ('320214', '新吴区', '3202');
INSERT INTO `base_area` VALUES ('320281', '江阴市', '3202');
INSERT INTO `base_area` VALUES ('320282', '宜兴市', '3202');
INSERT INTO `base_area` VALUES ('320302', '鼓楼区', '3203');
INSERT INTO `base_area` VALUES ('320303', '云龙区', '3203');
INSERT INTO `base_area` VALUES ('320305', '贾汪区', '3203');
INSERT INTO `base_area` VALUES ('320311', '泉山区', '3203');
INSERT INTO `base_area` VALUES ('320312', '铜山区', '3203');
INSERT INTO `base_area` VALUES ('320321', '丰县', '3203');
INSERT INTO `base_area` VALUES ('320322', '沛县', '3203');
INSERT INTO `base_area` VALUES ('320324', '睢宁县', '3203');
INSERT INTO `base_area` VALUES ('320371', '徐州经济技术开发区', '3203');
INSERT INTO `base_area` VALUES ('320381', '新沂市', '3203');
INSERT INTO `base_area` VALUES ('320382', '邳州市', '3203');
INSERT INTO `base_area` VALUES ('320402', '天宁区', '3204');
INSERT INTO `base_area` VALUES ('320404', '钟楼区', '3204');
INSERT INTO `base_area` VALUES ('320411', '新北区', '3204');
INSERT INTO `base_area` VALUES ('320412', '武进区', '3204');
INSERT INTO `base_area` VALUES ('320413', '金坛区', '3204');
INSERT INTO `base_area` VALUES ('320481', '溧阳市', '3204');
INSERT INTO `base_area` VALUES ('320505', '虎丘区', '3205');
INSERT INTO `base_area` VALUES ('320506', '吴中区', '3205');
INSERT INTO `base_area` VALUES ('320507', '相城区', '3205');
INSERT INTO `base_area` VALUES ('320508', '姑苏区', '3205');
INSERT INTO `base_area` VALUES ('320509', '吴江区', '3205');
INSERT INTO `base_area` VALUES ('320571', '苏州工业园区', '3205');
INSERT INTO `base_area` VALUES ('320581', '常熟市', '3205');
INSERT INTO `base_area` VALUES ('320582', '张家港市', '3205');
INSERT INTO `base_area` VALUES ('320583', '昆山市', '3205');
INSERT INTO `base_area` VALUES ('320585', '太仓市', '3205');
INSERT INTO `base_area` VALUES ('320612', '通州区', '3206');
INSERT INTO `base_area` VALUES ('320613', '崇川区', '3206');
INSERT INTO `base_area` VALUES ('320614', '海门区', '3206');
INSERT INTO `base_area` VALUES ('320623', '如东县', '3206');
INSERT INTO `base_area` VALUES ('320671', '南通经济技术开发区', '3206');
INSERT INTO `base_area` VALUES ('320681', '启东市', '3206');
INSERT INTO `base_area` VALUES ('320682', '如皋市', '3206');
INSERT INTO `base_area` VALUES ('320685', '海安市', '3206');
INSERT INTO `base_area` VALUES ('320703', '连云区', '3207');
INSERT INTO `base_area` VALUES ('320706', '海州区', '3207');
INSERT INTO `base_area` VALUES ('320707', '赣榆区', '3207');
INSERT INTO `base_area` VALUES ('320722', '东海县', '3207');
INSERT INTO `base_area` VALUES ('320723', '灌云县', '3207');
INSERT INTO `base_area` VALUES ('320724', '灌南县', '3207');
INSERT INTO `base_area` VALUES ('320771', '连云港经济技术开发区', '3207');
INSERT INTO `base_area` VALUES ('320772', '连云港高新技术产业开发区', '3207');
INSERT INTO `base_area` VALUES ('320803', '淮安区', '3208');
INSERT INTO `base_area` VALUES ('320804', '淮阴区', '3208');
INSERT INTO `base_area` VALUES ('320812', '清江浦区', '3208');
INSERT INTO `base_area` VALUES ('320813', '洪泽区', '3208');
INSERT INTO `base_area` VALUES ('320826', '涟水县', '3208');
INSERT INTO `base_area` VALUES ('320830', '盱眙县', '3208');
INSERT INTO `base_area` VALUES ('320831', '金湖县', '3208');
INSERT INTO `base_area` VALUES ('320871', '淮安经济技术开发区', '3208');
INSERT INTO `base_area` VALUES ('320902', '亭湖区', '3209');
INSERT INTO `base_area` VALUES ('320903', '盐都区', '3209');
INSERT INTO `base_area` VALUES ('320904', '大丰区', '3209');
INSERT INTO `base_area` VALUES ('320921', '响水县', '3209');
INSERT INTO `base_area` VALUES ('320922', '滨海县', '3209');
INSERT INTO `base_area` VALUES ('320923', '阜宁县', '3209');
INSERT INTO `base_area` VALUES ('320924', '射阳县', '3209');
INSERT INTO `base_area` VALUES ('320925', '建湖县', '3209');
INSERT INTO `base_area` VALUES ('320971', '盐城经济技术开发区', '3209');
INSERT INTO `base_area` VALUES ('320981', '东台市', '3209');
INSERT INTO `base_area` VALUES ('321002', '广陵区', '3210');
INSERT INTO `base_area` VALUES ('321003', '邗江区', '3210');
INSERT INTO `base_area` VALUES ('321012', '江都区', '3210');
INSERT INTO `base_area` VALUES ('321023', '宝应县', '3210');
INSERT INTO `base_area` VALUES ('321071', '扬州经济技术开发区', '3210');
INSERT INTO `base_area` VALUES ('321081', '仪征市', '3210');
INSERT INTO `base_area` VALUES ('321084', '高邮市', '3210');
INSERT INTO `base_area` VALUES ('321102', '京口区', '3211');
INSERT INTO `base_area` VALUES ('321111', '润州区', '3211');
INSERT INTO `base_area` VALUES ('321112', '丹徒区', '3211');
INSERT INTO `base_area` VALUES ('321171', '镇江新区', '3211');
INSERT INTO `base_area` VALUES ('321181', '丹阳市', '3211');
INSERT INTO `base_area` VALUES ('321182', '扬中市', '3211');
INSERT INTO `base_area` VALUES ('321183', '句容市', '3211');
INSERT INTO `base_area` VALUES ('321202', '海陵区', '3212');
INSERT INTO `base_area` VALUES ('321203', '高港区', '3212');
INSERT INTO `base_area` VALUES ('321204', '姜堰区', '3212');
INSERT INTO `base_area` VALUES ('321271', '泰州医药高新技术产业开发区', '3212');
INSERT INTO `base_area` VALUES ('321281', '兴化市', '3212');
INSERT INTO `base_area` VALUES ('321282', '靖江市', '3212');
INSERT INTO `base_area` VALUES ('321283', '泰兴市', '3212');
INSERT INTO `base_area` VALUES ('321302', '宿城区', '3213');
INSERT INTO `base_area` VALUES ('321311', '宿豫区', '3213');
INSERT INTO `base_area` VALUES ('321322', '沭阳县', '3213');
INSERT INTO `base_area` VALUES ('321323', '泗阳县', '3213');
INSERT INTO `base_area` VALUES ('321324', '泗洪县', '3213');
INSERT INTO `base_area` VALUES ('321371', '宿迁经济技术开发区', '3213');
INSERT INTO `base_area` VALUES ('330102', '上城区', '3301');
INSERT INTO `base_area` VALUES ('330105', '拱墅区', '3301');
INSERT INTO `base_area` VALUES ('330106', '西湖区', '3301');
INSERT INTO `base_area` VALUES ('330108', '滨江区', '3301');
INSERT INTO `base_area` VALUES ('330109', '萧山区', '3301');
INSERT INTO `base_area` VALUES ('330110', '余杭区', '3301');
INSERT INTO `base_area` VALUES ('330111', '富阳区', '3301');
INSERT INTO `base_area` VALUES ('330112', '临安区', '3301');
INSERT INTO `base_area` VALUES ('330113', '临平区', '3301');
INSERT INTO `base_area` VALUES ('330114', '钱塘区', '3301');
INSERT INTO `base_area` VALUES ('330122', '桐庐县', '3301');
INSERT INTO `base_area` VALUES ('330127', '淳安县', '3301');
INSERT INTO `base_area` VALUES ('330182', '建德市', '3301');
INSERT INTO `base_area` VALUES ('330203', '海曙区', '3302');
INSERT INTO `base_area` VALUES ('330205', '江北区', '3302');
INSERT INTO `base_area` VALUES ('330206', '北仑区', '3302');
INSERT INTO `base_area` VALUES ('330211', '镇海区', '3302');
INSERT INTO `base_area` VALUES ('330212', '鄞州区', '3302');
INSERT INTO `base_area` VALUES ('330213', '奉化区', '3302');
INSERT INTO `base_area` VALUES ('330225', '象山县', '3302');
INSERT INTO `base_area` VALUES ('330226', '宁海县', '3302');
INSERT INTO `base_area` VALUES ('330281', '余姚市', '3302');
INSERT INTO `base_area` VALUES ('330282', '慈溪市', '3302');
INSERT INTO `base_area` VALUES ('330302', '鹿城区', '3303');
INSERT INTO `base_area` VALUES ('330303', '龙湾区', '3303');
INSERT INTO `base_area` VALUES ('330304', '瓯海区', '3303');
INSERT INTO `base_area` VALUES ('330305', '洞头区', '3303');
INSERT INTO `base_area` VALUES ('330324', '永嘉县', '3303');
INSERT INTO `base_area` VALUES ('330326', '平阳县', '3303');
INSERT INTO `base_area` VALUES ('330327', '苍南县', '3303');
INSERT INTO `base_area` VALUES ('330328', '文成县', '3303');
INSERT INTO `base_area` VALUES ('330329', '泰顺县', '3303');
INSERT INTO `base_area` VALUES ('330381', '瑞安市', '3303');
INSERT INTO `base_area` VALUES ('330382', '乐清市', '3303');
INSERT INTO `base_area` VALUES ('330383', '龙港市', '3303');
INSERT INTO `base_area` VALUES ('330402', '南湖区', '3304');
INSERT INTO `base_area` VALUES ('330411', '秀洲区', '3304');
INSERT INTO `base_area` VALUES ('330421', '嘉善县', '3304');
INSERT INTO `base_area` VALUES ('330424', '海盐县', '3304');
INSERT INTO `base_area` VALUES ('330481', '海宁市', '3304');
INSERT INTO `base_area` VALUES ('330482', '平湖市', '3304');
INSERT INTO `base_area` VALUES ('330483', '桐乡市', '3304');
INSERT INTO `base_area` VALUES ('330502', '吴兴区', '3305');
INSERT INTO `base_area` VALUES ('330503', '南浔区', '3305');
INSERT INTO `base_area` VALUES ('330521', '德清县', '3305');
INSERT INTO `base_area` VALUES ('330522', '长兴县', '3305');
INSERT INTO `base_area` VALUES ('330523', '安吉县', '3305');
INSERT INTO `base_area` VALUES ('330602', '越城区', '3306');
INSERT INTO `base_area` VALUES ('330603', '柯桥区', '3306');
INSERT INTO `base_area` VALUES ('330604', '上虞区', '3306');
INSERT INTO `base_area` VALUES ('330624', '新昌县', '3306');
INSERT INTO `base_area` VALUES ('330681', '诸暨市', '3306');
INSERT INTO `base_area` VALUES ('330683', '嵊州市', '3306');
INSERT INTO `base_area` VALUES ('330702', '婺城区', '3307');
INSERT INTO `base_area` VALUES ('330703', '金东区', '3307');
INSERT INTO `base_area` VALUES ('330723', '武义县', '3307');
INSERT INTO `base_area` VALUES ('330726', '浦江县', '3307');
INSERT INTO `base_area` VALUES ('330727', '磐安县', '3307');
INSERT INTO `base_area` VALUES ('330781', '兰溪市', '3307');
INSERT INTO `base_area` VALUES ('330782', '义乌市', '3307');
INSERT INTO `base_area` VALUES ('330783', '东阳市', '3307');
INSERT INTO `base_area` VALUES ('330784', '永康市', '3307');
INSERT INTO `base_area` VALUES ('330802', '柯城区', '3308');
INSERT INTO `base_area` VALUES ('330803', '衢江区', '3308');
INSERT INTO `base_area` VALUES ('330822', '常山县', '3308');
INSERT INTO `base_area` VALUES ('330824', '开化县', '3308');
INSERT INTO `base_area` VALUES ('330825', '龙游县', '3308');
INSERT INTO `base_area` VALUES ('330881', '江山市', '3308');
INSERT INTO `base_area` VALUES ('330902', '定海区', '3309');
INSERT INTO `base_area` VALUES ('330903', '普陀区', '3309');
INSERT INTO `base_area` VALUES ('330921', '岱山县', '3309');
INSERT INTO `base_area` VALUES ('330922', '嵊泗县', '3309');
INSERT INTO `base_area` VALUES ('331002', '椒江区', '3310');
INSERT INTO `base_area` VALUES ('331003', '黄岩区', '3310');
INSERT INTO `base_area` VALUES ('331004', '路桥区', '3310');
INSERT INTO `base_area` VALUES ('331022', '三门县', '3310');
INSERT INTO `base_area` VALUES ('331023', '天台县', '3310');
INSERT INTO `base_area` VALUES ('331024', '仙居县', '3310');
INSERT INTO `base_area` VALUES ('331081', '温岭市', '3310');
INSERT INTO `base_area` VALUES ('331082', '临海市', '3310');
INSERT INTO `base_area` VALUES ('331083', '玉环市', '3310');
INSERT INTO `base_area` VALUES ('331102', '莲都区', '3311');
INSERT INTO `base_area` VALUES ('331121', '青田县', '3311');
INSERT INTO `base_area` VALUES ('331122', '缙云县', '3311');
INSERT INTO `base_area` VALUES ('331123', '遂昌县', '3311');
INSERT INTO `base_area` VALUES ('331124', '松阳县', '3311');
INSERT INTO `base_area` VALUES ('331125', '云和县', '3311');
INSERT INTO `base_area` VALUES ('331126', '庆元县', '3311');
INSERT INTO `base_area` VALUES ('331127', '景宁畲族自治县', '3311');
INSERT INTO `base_area` VALUES ('331181', '龙泉市', '3311');
INSERT INTO `base_area` VALUES ('340102', '瑶海区', '3401');
INSERT INTO `base_area` VALUES ('340103', '庐阳区', '3401');
INSERT INTO `base_area` VALUES ('340104', '蜀山区', '3401');
INSERT INTO `base_area` VALUES ('340111', '包河区', '3401');
INSERT INTO `base_area` VALUES ('340121', '长丰县', '3401');
INSERT INTO `base_area` VALUES ('340122', '肥东县', '3401');
INSERT INTO `base_area` VALUES ('340123', '肥西县', '3401');
INSERT INTO `base_area` VALUES ('340124', '庐江县', '3401');
INSERT INTO `base_area` VALUES ('340171', '合肥高新技术产业开发区', '3401');
INSERT INTO `base_area` VALUES ('340172', '合肥经济技术开发区', '3401');
INSERT INTO `base_area` VALUES ('340173', '合肥新站高新技术产业开发区', '3401');
INSERT INTO `base_area` VALUES ('340181', '巢湖市', '3401');
INSERT INTO `base_area` VALUES ('340202', '镜湖区', '3402');
INSERT INTO `base_area` VALUES ('340207', '鸠江区', '3402');
INSERT INTO `base_area` VALUES ('340209', '弋江区', '3402');
INSERT INTO `base_area` VALUES ('340210', '湾沚区', '3402');
INSERT INTO `base_area` VALUES ('340212', '繁昌区', '3402');
INSERT INTO `base_area` VALUES ('340223', '南陵县', '3402');
INSERT INTO `base_area` VALUES ('340271', '芜湖经济技术开发区', '3402');
INSERT INTO `base_area` VALUES ('340272', '安徽芜湖三山经济开发区', '3402');
INSERT INTO `base_area` VALUES ('340281', '无为市', '3402');
INSERT INTO `base_area` VALUES ('340302', '龙子湖区', '3403');
INSERT INTO `base_area` VALUES ('340303', '蚌山区', '3403');
INSERT INTO `base_area` VALUES ('340304', '禹会区', '3403');
INSERT INTO `base_area` VALUES ('340311', '淮上区', '3403');
INSERT INTO `base_area` VALUES ('340321', '怀远县', '3403');
INSERT INTO `base_area` VALUES ('340322', '五河县', '3403');
INSERT INTO `base_area` VALUES ('340323', '固镇县', '3403');
INSERT INTO `base_area` VALUES ('340371', '蚌埠市高新技术开发区', '3403');
INSERT INTO `base_area` VALUES ('340372', '蚌埠市经济开发区', '3403');
INSERT INTO `base_area` VALUES ('340402', '大通区', '3404');
INSERT INTO `base_area` VALUES ('340403', '田家庵区', '3404');
INSERT INTO `base_area` VALUES ('340404', '谢家集区', '3404');
INSERT INTO `base_area` VALUES ('340405', '八公山区', '3404');
INSERT INTO `base_area` VALUES ('340406', '潘集区', '3404');
INSERT INTO `base_area` VALUES ('340421', '凤台县', '3404');
INSERT INTO `base_area` VALUES ('340422', '寿县', '3404');
INSERT INTO `base_area` VALUES ('340503', '花山区', '3405');
INSERT INTO `base_area` VALUES ('340504', '雨山区', '3405');
INSERT INTO `base_area` VALUES ('340506', '博望区', '3405');
INSERT INTO `base_area` VALUES ('340521', '当涂县', '3405');
INSERT INTO `base_area` VALUES ('340522', '含山县', '3405');
INSERT INTO `base_area` VALUES ('340523', '和县', '3405');
INSERT INTO `base_area` VALUES ('340602', '杜集区', '3406');
INSERT INTO `base_area` VALUES ('340603', '相山区', '3406');
INSERT INTO `base_area` VALUES ('340604', '烈山区', '3406');
INSERT INTO `base_area` VALUES ('340621', '濉溪县', '3406');
INSERT INTO `base_area` VALUES ('340705', '铜官区', '3407');
INSERT INTO `base_area` VALUES ('340706', '义安区', '3407');
INSERT INTO `base_area` VALUES ('340711', '郊区', '3407');
INSERT INTO `base_area` VALUES ('340722', '枞阳县', '3407');
INSERT INTO `base_area` VALUES ('340802', '迎江区', '3408');
INSERT INTO `base_area` VALUES ('340803', '大观区', '3408');
INSERT INTO `base_area` VALUES ('340811', '宜秀区', '3408');
INSERT INTO `base_area` VALUES ('340822', '怀宁县', '3408');
INSERT INTO `base_area` VALUES ('340825', '太湖县', '3408');
INSERT INTO `base_area` VALUES ('340826', '宿松县', '3408');
INSERT INTO `base_area` VALUES ('340827', '望江县', '3408');
INSERT INTO `base_area` VALUES ('340828', '岳西县', '3408');
INSERT INTO `base_area` VALUES ('340871', '安徽安庆经济开发区', '3408');
INSERT INTO `base_area` VALUES ('340881', '桐城市', '3408');
INSERT INTO `base_area` VALUES ('340882', '潜山市', '3408');
INSERT INTO `base_area` VALUES ('341002', '屯溪区', '3410');
INSERT INTO `base_area` VALUES ('341003', '黄山区', '3410');
INSERT INTO `base_area` VALUES ('341004', '徽州区', '3410');
INSERT INTO `base_area` VALUES ('341021', '歙县', '3410');
INSERT INTO `base_area` VALUES ('341022', '休宁县', '3410');
INSERT INTO `base_area` VALUES ('341023', '黟县', '3410');
INSERT INTO `base_area` VALUES ('341024', '祁门县', '3410');
INSERT INTO `base_area` VALUES ('341102', '琅琊区', '3411');
INSERT INTO `base_area` VALUES ('341103', '南谯区', '3411');
INSERT INTO `base_area` VALUES ('341122', '来安县', '3411');
INSERT INTO `base_area` VALUES ('341124', '全椒县', '3411');
INSERT INTO `base_area` VALUES ('341125', '定远县', '3411');
INSERT INTO `base_area` VALUES ('341126', '凤阳县', '3411');
INSERT INTO `base_area` VALUES ('341171', '中新苏滁高新技术产业开发区', '3411');
INSERT INTO `base_area` VALUES ('341172', '滁州经济技术开发区', '3411');
INSERT INTO `base_area` VALUES ('341181', '天长市', '3411');
INSERT INTO `base_area` VALUES ('341182', '明光市', '3411');
INSERT INTO `base_area` VALUES ('341202', '颍州区', '3412');
INSERT INTO `base_area` VALUES ('341203', '颍东区', '3412');
INSERT INTO `base_area` VALUES ('341204', '颍泉区', '3412');
INSERT INTO `base_area` VALUES ('341221', '临泉县', '3412');
INSERT INTO `base_area` VALUES ('341222', '太和县', '3412');
INSERT INTO `base_area` VALUES ('341225', '阜南县', '3412');
INSERT INTO `base_area` VALUES ('341226', '颍上县', '3412');
INSERT INTO `base_area` VALUES ('341271', '阜阳合肥现代产业园区', '3412');
INSERT INTO `base_area` VALUES ('341272', '阜阳经济技术开发区', '3412');
INSERT INTO `base_area` VALUES ('341282', '界首市', '3412');
INSERT INTO `base_area` VALUES ('341302', '埇桥区', '3413');
INSERT INTO `base_area` VALUES ('341321', '砀山县', '3413');
INSERT INTO `base_area` VALUES ('341322', '萧县', '3413');
INSERT INTO `base_area` VALUES ('341323', '灵璧县', '3413');
INSERT INTO `base_area` VALUES ('341324', '泗县', '3413');
INSERT INTO `base_area` VALUES ('341371', '宿州马鞍山现代产业园区', '3413');
INSERT INTO `base_area` VALUES ('341372', '宿州经济技术开发区', '3413');
INSERT INTO `base_area` VALUES ('341502', '金安区', '3415');
INSERT INTO `base_area` VALUES ('341503', '裕安区', '3415');
INSERT INTO `base_area` VALUES ('341504', '叶集区', '3415');
INSERT INTO `base_area` VALUES ('341522', '霍邱县', '3415');
INSERT INTO `base_area` VALUES ('341523', '舒城县', '3415');
INSERT INTO `base_area` VALUES ('341524', '金寨县', '3415');
INSERT INTO `base_area` VALUES ('341525', '霍山县', '3415');
INSERT INTO `base_area` VALUES ('341602', '谯城区', '3416');
INSERT INTO `base_area` VALUES ('341621', '涡阳县', '3416');
INSERT INTO `base_area` VALUES ('341622', '蒙城县', '3416');
INSERT INTO `base_area` VALUES ('341623', '利辛县', '3416');
INSERT INTO `base_area` VALUES ('341702', '贵池区', '3417');
INSERT INTO `base_area` VALUES ('341721', '东至县', '3417');
INSERT INTO `base_area` VALUES ('341722', '石台县', '3417');
INSERT INTO `base_area` VALUES ('341723', '青阳县', '3417');
INSERT INTO `base_area` VALUES ('341802', '宣州区', '3418');
INSERT INTO `base_area` VALUES ('341821', '郎溪县', '3418');
INSERT INTO `base_area` VALUES ('341823', '泾县', '3418');
INSERT INTO `base_area` VALUES ('341824', '绩溪县', '3418');
INSERT INTO `base_area` VALUES ('341825', '旌德县', '3418');
INSERT INTO `base_area` VALUES ('341871', '宣城市经济开发区', '3418');
INSERT INTO `base_area` VALUES ('341881', '宁国市', '3418');
INSERT INTO `base_area` VALUES ('341882', '广德市', '3418');
INSERT INTO `base_area` VALUES ('350102', '鼓楼区', '3501');
INSERT INTO `base_area` VALUES ('350103', '台江区', '3501');
INSERT INTO `base_area` VALUES ('350104', '仓山区', '3501');
INSERT INTO `base_area` VALUES ('350105', '马尾区', '3501');
INSERT INTO `base_area` VALUES ('350111', '晋安区', '3501');
INSERT INTO `base_area` VALUES ('350112', '长乐区', '3501');
INSERT INTO `base_area` VALUES ('350121', '闽侯县', '3501');
INSERT INTO `base_area` VALUES ('350122', '连江县', '3501');
INSERT INTO `base_area` VALUES ('350123', '罗源县', '3501');
INSERT INTO `base_area` VALUES ('350124', '闽清县', '3501');
INSERT INTO `base_area` VALUES ('350125', '永泰县', '3501');
INSERT INTO `base_area` VALUES ('350128', '平潭县', '3501');
INSERT INTO `base_area` VALUES ('350181', '福清市', '3501');
INSERT INTO `base_area` VALUES ('350203', '思明区', '3502');
INSERT INTO `base_area` VALUES ('350205', '海沧区', '3502');
INSERT INTO `base_area` VALUES ('350206', '湖里区', '3502');
INSERT INTO `base_area` VALUES ('350211', '集美区', '3502');
INSERT INTO `base_area` VALUES ('350212', '同安区', '3502');
INSERT INTO `base_area` VALUES ('350213', '翔安区', '3502');
INSERT INTO `base_area` VALUES ('350302', '城厢区', '3503');
INSERT INTO `base_area` VALUES ('350303', '涵江区', '3503');
INSERT INTO `base_area` VALUES ('350304', '荔城区', '3503');
INSERT INTO `base_area` VALUES ('350305', '秀屿区', '3503');
INSERT INTO `base_area` VALUES ('350322', '仙游县', '3503');
INSERT INTO `base_area` VALUES ('350404', '三元区', '3504');
INSERT INTO `base_area` VALUES ('350405', '沙县区', '3504');
INSERT INTO `base_area` VALUES ('350421', '明溪县', '3504');
INSERT INTO `base_area` VALUES ('350423', '清流县', '3504');
INSERT INTO `base_area` VALUES ('350424', '宁化县', '3504');
INSERT INTO `base_area` VALUES ('350425', '大田县', '3504');
INSERT INTO `base_area` VALUES ('350426', '尤溪县', '3504');
INSERT INTO `base_area` VALUES ('350428', '将乐县', '3504');
INSERT INTO `base_area` VALUES ('350429', '泰宁县', '3504');
INSERT INTO `base_area` VALUES ('350430', '建宁县', '3504');
INSERT INTO `base_area` VALUES ('350481', '永安市', '3504');
INSERT INTO `base_area` VALUES ('350502', '鲤城区', '3505');
INSERT INTO `base_area` VALUES ('350503', '丰泽区', '3505');
INSERT INTO `base_area` VALUES ('350504', '洛江区', '3505');
INSERT INTO `base_area` VALUES ('350505', '泉港区', '3505');
INSERT INTO `base_area` VALUES ('350521', '惠安县', '3505');
INSERT INTO `base_area` VALUES ('350524', '安溪县', '3505');
INSERT INTO `base_area` VALUES ('350525', '永春县', '3505');
INSERT INTO `base_area` VALUES ('350526', '德化县', '3505');
INSERT INTO `base_area` VALUES ('350527', '金门县', '3505');
INSERT INTO `base_area` VALUES ('350581', '石狮市', '3505');
INSERT INTO `base_area` VALUES ('350582', '晋江市', '3505');
INSERT INTO `base_area` VALUES ('350583', '南安市', '3505');
INSERT INTO `base_area` VALUES ('350602', '芗城区', '3506');
INSERT INTO `base_area` VALUES ('350603', '龙文区', '3506');
INSERT INTO `base_area` VALUES ('350604', '龙海区', '3506');
INSERT INTO `base_area` VALUES ('350605', '长泰区', '3506');
INSERT INTO `base_area` VALUES ('350622', '云霄县', '3506');
INSERT INTO `base_area` VALUES ('350623', '漳浦县', '3506');
INSERT INTO `base_area` VALUES ('350624', '诏安县', '3506');
INSERT INTO `base_area` VALUES ('350626', '东山县', '3506');
INSERT INTO `base_area` VALUES ('350627', '南靖县', '3506');
INSERT INTO `base_area` VALUES ('350628', '平和县', '3506');
INSERT INTO `base_area` VALUES ('350629', '华安县', '3506');
INSERT INTO `base_area` VALUES ('350702', '延平区', '3507');
INSERT INTO `base_area` VALUES ('350703', '建阳区', '3507');
INSERT INTO `base_area` VALUES ('350721', '顺昌县', '3507');
INSERT INTO `base_area` VALUES ('350722', '浦城县', '3507');
INSERT INTO `base_area` VALUES ('350723', '光泽县', '3507');
INSERT INTO `base_area` VALUES ('350724', '松溪县', '3507');
INSERT INTO `base_area` VALUES ('350725', '政和县', '3507');
INSERT INTO `base_area` VALUES ('350781', '邵武市', '3507');
INSERT INTO `base_area` VALUES ('350782', '武夷山市', '3507');
INSERT INTO `base_area` VALUES ('350783', '建瓯市', '3507');
INSERT INTO `base_area` VALUES ('350802', '新罗区', '3508');
INSERT INTO `base_area` VALUES ('350803', '永定区', '3508');
INSERT INTO `base_area` VALUES ('350821', '长汀县', '3508');
INSERT INTO `base_area` VALUES ('350823', '上杭县', '3508');
INSERT INTO `base_area` VALUES ('350824', '武平县', '3508');
INSERT INTO `base_area` VALUES ('350825', '连城县', '3508');
INSERT INTO `base_area` VALUES ('350881', '漳平市', '3508');
INSERT INTO `base_area` VALUES ('350902', '蕉城区', '3509');
INSERT INTO `base_area` VALUES ('350921', '霞浦县', '3509');
INSERT INTO `base_area` VALUES ('350922', '古田县', '3509');
INSERT INTO `base_area` VALUES ('350923', '屏南县', '3509');
INSERT INTO `base_area` VALUES ('350924', '寿宁县', '3509');
INSERT INTO `base_area` VALUES ('350925', '周宁县', '3509');
INSERT INTO `base_area` VALUES ('350926', '柘荣县', '3509');
INSERT INTO `base_area` VALUES ('350981', '福安市', '3509');
INSERT INTO `base_area` VALUES ('350982', '福鼎市', '3509');
INSERT INTO `base_area` VALUES ('360102', '东湖区', '3601');
INSERT INTO `base_area` VALUES ('360103', '西湖区', '3601');
INSERT INTO `base_area` VALUES ('360104', '青云谱区', '3601');
INSERT INTO `base_area` VALUES ('360111', '青山湖区', '3601');
INSERT INTO `base_area` VALUES ('360112', '新建区', '3601');
INSERT INTO `base_area` VALUES ('360113', '红谷滩区', '3601');
INSERT INTO `base_area` VALUES ('360121', '南昌县', '3601');
INSERT INTO `base_area` VALUES ('360123', '安义县', '3601');
INSERT INTO `base_area` VALUES ('360124', '进贤县', '3601');
INSERT INTO `base_area` VALUES ('360202', '昌江区', '3602');
INSERT INTO `base_area` VALUES ('360203', '珠山区', '3602');
INSERT INTO `base_area` VALUES ('360222', '浮梁县', '3602');
INSERT INTO `base_area` VALUES ('360281', '乐平市', '3602');
INSERT INTO `base_area` VALUES ('360302', '安源区', '3603');
INSERT INTO `base_area` VALUES ('360313', '湘东区', '3603');
INSERT INTO `base_area` VALUES ('360321', '莲花县', '3603');
INSERT INTO `base_area` VALUES ('360322', '上栗县', '3603');
INSERT INTO `base_area` VALUES ('360323', '芦溪县', '3603');
INSERT INTO `base_area` VALUES ('360402', '濂溪区', '3604');
INSERT INTO `base_area` VALUES ('360403', '浔阳区', '3604');
INSERT INTO `base_area` VALUES ('360404', '柴桑区', '3604');
INSERT INTO `base_area` VALUES ('360423', '武宁县', '3604');
INSERT INTO `base_area` VALUES ('360424', '修水县', '3604');
INSERT INTO `base_area` VALUES ('360425', '永修县', '3604');
INSERT INTO `base_area` VALUES ('360426', '德安县', '3604');
INSERT INTO `base_area` VALUES ('360428', '都昌县', '3604');
INSERT INTO `base_area` VALUES ('360429', '湖口县', '3604');
INSERT INTO `base_area` VALUES ('360430', '彭泽县', '3604');
INSERT INTO `base_area` VALUES ('360481', '瑞昌市', '3604');
INSERT INTO `base_area` VALUES ('360482', '共青城市', '3604');
INSERT INTO `base_area` VALUES ('360483', '庐山市', '3604');
INSERT INTO `base_area` VALUES ('360502', '渝水区', '3605');
INSERT INTO `base_area` VALUES ('360521', '分宜县', '3605');
INSERT INTO `base_area` VALUES ('360602', '月湖区', '3606');
INSERT INTO `base_area` VALUES ('360603', '余江区', '3606');
INSERT INTO `base_area` VALUES ('360681', '贵溪市', '3606');
INSERT INTO `base_area` VALUES ('360702', '章贡区', '3607');
INSERT INTO `base_area` VALUES ('360703', '南康区', '3607');
INSERT INTO `base_area` VALUES ('360704', '赣县区', '3607');
INSERT INTO `base_area` VALUES ('360722', '信丰县', '3607');
INSERT INTO `base_area` VALUES ('360723', '大余县', '3607');
INSERT INTO `base_area` VALUES ('360724', '上犹县', '3607');
INSERT INTO `base_area` VALUES ('360725', '崇义县', '3607');
INSERT INTO `base_area` VALUES ('360726', '安远县', '3607');
INSERT INTO `base_area` VALUES ('360728', '定南县', '3607');
INSERT INTO `base_area` VALUES ('360729', '全南县', '3607');
INSERT INTO `base_area` VALUES ('360730', '宁都县', '3607');
INSERT INTO `base_area` VALUES ('360731', '于都县', '3607');
INSERT INTO `base_area` VALUES ('360732', '兴国县', '3607');
INSERT INTO `base_area` VALUES ('360733', '会昌县', '3607');
INSERT INTO `base_area` VALUES ('360734', '寻乌县', '3607');
INSERT INTO `base_area` VALUES ('360735', '石城县', '3607');
INSERT INTO `base_area` VALUES ('360781', '瑞金市', '3607');
INSERT INTO `base_area` VALUES ('360783', '龙南市', '3607');
INSERT INTO `base_area` VALUES ('360802', '吉州区', '3608');
INSERT INTO `base_area` VALUES ('360803', '青原区', '3608');
INSERT INTO `base_area` VALUES ('360821', '吉安县', '3608');
INSERT INTO `base_area` VALUES ('360822', '吉水县', '3608');
INSERT INTO `base_area` VALUES ('360823', '峡江县', '3608');
INSERT INTO `base_area` VALUES ('360824', '新干县', '3608');
INSERT INTO `base_area` VALUES ('360825', '永丰县', '3608');
INSERT INTO `base_area` VALUES ('360826', '泰和县', '3608');
INSERT INTO `base_area` VALUES ('360827', '遂川县', '3608');
INSERT INTO `base_area` VALUES ('360828', '万安县', '3608');
INSERT INTO `base_area` VALUES ('360829', '安福县', '3608');
INSERT INTO `base_area` VALUES ('360830', '永新县', '3608');
INSERT INTO `base_area` VALUES ('360881', '井冈山市', '3608');
INSERT INTO `base_area` VALUES ('360902', '袁州区', '3609');
INSERT INTO `base_area` VALUES ('360921', '奉新县', '3609');
INSERT INTO `base_area` VALUES ('360922', '万载县', '3609');
INSERT INTO `base_area` VALUES ('360923', '上高县', '3609');
INSERT INTO `base_area` VALUES ('360924', '宜丰县', '3609');
INSERT INTO `base_area` VALUES ('360925', '靖安县', '3609');
INSERT INTO `base_area` VALUES ('360926', '铜鼓县', '3609');
INSERT INTO `base_area` VALUES ('360981', '丰城市', '3609');
INSERT INTO `base_area` VALUES ('360982', '樟树市', '3609');
INSERT INTO `base_area` VALUES ('360983', '高安市', '3609');
INSERT INTO `base_area` VALUES ('361002', '临川区', '3610');
INSERT INTO `base_area` VALUES ('361003', '东乡区', '3610');
INSERT INTO `base_area` VALUES ('361021', '南城县', '3610');
INSERT INTO `base_area` VALUES ('361022', '黎川县', '3610');
INSERT INTO `base_area` VALUES ('361023', '南丰县', '3610');
INSERT INTO `base_area` VALUES ('361024', '崇仁县', '3610');
INSERT INTO `base_area` VALUES ('361025', '乐安县', '3610');
INSERT INTO `base_area` VALUES ('361026', '宜黄县', '3610');
INSERT INTO `base_area` VALUES ('361027', '金溪县', '3610');
INSERT INTO `base_area` VALUES ('361028', '资溪县', '3610');
INSERT INTO `base_area` VALUES ('361030', '广昌县', '3610');
INSERT INTO `base_area` VALUES ('361102', '信州区', '3611');
INSERT INTO `base_area` VALUES ('361103', '广丰区', '3611');
INSERT INTO `base_area` VALUES ('361104', '广信区', '3611');
INSERT INTO `base_area` VALUES ('361123', '玉山县', '3611');
INSERT INTO `base_area` VALUES ('361124', '铅山县', '3611');
INSERT INTO `base_area` VALUES ('361125', '横峰县', '3611');
INSERT INTO `base_area` VALUES ('361126', '弋阳县', '3611');
INSERT INTO `base_area` VALUES ('361127', '余干县', '3611');
INSERT INTO `base_area` VALUES ('361128', '鄱阳县', '3611');
INSERT INTO `base_area` VALUES ('361129', '万年县', '3611');
INSERT INTO `base_area` VALUES ('361130', '婺源县', '3611');
INSERT INTO `base_area` VALUES ('361181', '德兴市', '3611');
INSERT INTO `base_area` VALUES ('370102', '历下区', '3701');
INSERT INTO `base_area` VALUES ('370103', '市中区', '3701');
INSERT INTO `base_area` VALUES ('370104', '槐荫区', '3701');
INSERT INTO `base_area` VALUES ('370105', '天桥区', '3701');
INSERT INTO `base_area` VALUES ('370112', '历城区', '3701');
INSERT INTO `base_area` VALUES ('370113', '长清区', '3701');
INSERT INTO `base_area` VALUES ('370114', '章丘区', '3701');
INSERT INTO `base_area` VALUES ('370115', '济阳区', '3701');
INSERT INTO `base_area` VALUES ('370116', '莱芜区', '3701');
INSERT INTO `base_area` VALUES ('370117', '钢城区', '3701');
INSERT INTO `base_area` VALUES ('370124', '平阴县', '3701');
INSERT INTO `base_area` VALUES ('370126', '商河县', '3701');
INSERT INTO `base_area` VALUES ('370171', '济南高新技术产业开发区', '3701');
INSERT INTO `base_area` VALUES ('370202', '市南区', '3702');
INSERT INTO `base_area` VALUES ('370203', '市北区', '3702');
INSERT INTO `base_area` VALUES ('370211', '黄岛区', '3702');
INSERT INTO `base_area` VALUES ('370212', '崂山区', '3702');
INSERT INTO `base_area` VALUES ('370213', '李沧区', '3702');
INSERT INTO `base_area` VALUES ('370214', '城阳区', '3702');
INSERT INTO `base_area` VALUES ('370215', '即墨区', '3702');
INSERT INTO `base_area` VALUES ('370271', '青岛高新技术产业开发区', '3702');
INSERT INTO `base_area` VALUES ('370281', '胶州市', '3702');
INSERT INTO `base_area` VALUES ('370283', '平度市', '3702');
INSERT INTO `base_area` VALUES ('370285', '莱西市', '3702');
INSERT INTO `base_area` VALUES ('370302', '淄川区', '3703');
INSERT INTO `base_area` VALUES ('370303', '张店区', '3703');
INSERT INTO `base_area` VALUES ('370304', '博山区', '3703');
INSERT INTO `base_area` VALUES ('370305', '临淄区', '3703');
INSERT INTO `base_area` VALUES ('370306', '周村区', '3703');
INSERT INTO `base_area` VALUES ('370321', '桓台县', '3703');
INSERT INTO `base_area` VALUES ('370322', '高青县', '3703');
INSERT INTO `base_area` VALUES ('370323', '沂源县', '3703');
INSERT INTO `base_area` VALUES ('370402', '市中区', '3704');
INSERT INTO `base_area` VALUES ('370403', '薛城区', '3704');
INSERT INTO `base_area` VALUES ('370404', '峄城区', '3704');
INSERT INTO `base_area` VALUES ('370405', '台儿庄区', '3704');
INSERT INTO `base_area` VALUES ('370406', '山亭区', '3704');
INSERT INTO `base_area` VALUES ('370481', '滕州市', '3704');
INSERT INTO `base_area` VALUES ('370502', '东营区', '3705');
INSERT INTO `base_area` VALUES ('370503', '河口区', '3705');
INSERT INTO `base_area` VALUES ('370505', '垦利区', '3705');
INSERT INTO `base_area` VALUES ('370522', '利津县', '3705');
INSERT INTO `base_area` VALUES ('370523', '广饶县', '3705');
INSERT INTO `base_area` VALUES ('370571', '东营经济技术开发区', '3705');
INSERT INTO `base_area` VALUES ('370572', '东营港经济开发区', '3705');
INSERT INTO `base_area` VALUES ('370602', '芝罘区', '3706');
INSERT INTO `base_area` VALUES ('370611', '福山区', '3706');
INSERT INTO `base_area` VALUES ('370612', '牟平区', '3706');
INSERT INTO `base_area` VALUES ('370613', '莱山区', '3706');
INSERT INTO `base_area` VALUES ('370614', '蓬莱区', '3706');
INSERT INTO `base_area` VALUES ('370671', '烟台高新技术产业开发区', '3706');
INSERT INTO `base_area` VALUES ('370672', '烟台经济技术开发区', '3706');
INSERT INTO `base_area` VALUES ('370681', '龙口市', '3706');
INSERT INTO `base_area` VALUES ('370682', '莱阳市', '3706');
INSERT INTO `base_area` VALUES ('370683', '莱州市', '3706');
INSERT INTO `base_area` VALUES ('370685', '招远市', '3706');
INSERT INTO `base_area` VALUES ('370686', '栖霞市', '3706');
INSERT INTO `base_area` VALUES ('370687', '海阳市', '3706');
INSERT INTO `base_area` VALUES ('370702', '潍城区', '3707');
INSERT INTO `base_area` VALUES ('370703', '寒亭区', '3707');
INSERT INTO `base_area` VALUES ('370704', '坊子区', '3707');
INSERT INTO `base_area` VALUES ('370705', '奎文区', '3707');
INSERT INTO `base_area` VALUES ('370724', '临朐县', '3707');
INSERT INTO `base_area` VALUES ('370725', '昌乐县', '3707');
INSERT INTO `base_area` VALUES ('370772', '潍坊滨海经济技术开发区', '3707');
INSERT INTO `base_area` VALUES ('370781', '青州市', '3707');
INSERT INTO `base_area` VALUES ('370782', '诸城市', '3707');
INSERT INTO `base_area` VALUES ('370783', '寿光市', '3707');
INSERT INTO `base_area` VALUES ('370784', '安丘市', '3707');
INSERT INTO `base_area` VALUES ('370785', '高密市', '3707');
INSERT INTO `base_area` VALUES ('370786', '昌邑市', '3707');
INSERT INTO `base_area` VALUES ('370811', '任城区', '3708');
INSERT INTO `base_area` VALUES ('370812', '兖州区', '3708');
INSERT INTO `base_area` VALUES ('370826', '微山县', '3708');
INSERT INTO `base_area` VALUES ('370827', '鱼台县', '3708');
INSERT INTO `base_area` VALUES ('370828', '金乡县', '3708');
INSERT INTO `base_area` VALUES ('370829', '嘉祥县', '3708');
INSERT INTO `base_area` VALUES ('370830', '汶上县', '3708');
INSERT INTO `base_area` VALUES ('370831', '泗水县', '3708');
INSERT INTO `base_area` VALUES ('370832', '梁山县', '3708');
INSERT INTO `base_area` VALUES ('370871', '济宁高新技术产业开发区', '3708');
INSERT INTO `base_area` VALUES ('370881', '曲阜市', '3708');
INSERT INTO `base_area` VALUES ('370883', '邹城市', '3708');
INSERT INTO `base_area` VALUES ('370902', '泰山区', '3709');
INSERT INTO `base_area` VALUES ('370911', '岱岳区', '3709');
INSERT INTO `base_area` VALUES ('370921', '宁阳县', '3709');
INSERT INTO `base_area` VALUES ('370923', '东平县', '3709');
INSERT INTO `base_area` VALUES ('370982', '新泰市', '3709');
INSERT INTO `base_area` VALUES ('370983', '肥城市', '3709');
INSERT INTO `base_area` VALUES ('371002', '环翠区', '3710');
INSERT INTO `base_area` VALUES ('371003', '文登区', '3710');
INSERT INTO `base_area` VALUES ('371071', '威海火炬高技术产业开发区', '3710');
INSERT INTO `base_area` VALUES ('371072', '威海经济技术开发区', '3710');
INSERT INTO `base_area` VALUES ('371073', '威海临港经济技术开发区', '3710');
INSERT INTO `base_area` VALUES ('371082', '荣成市', '3710');
INSERT INTO `base_area` VALUES ('371083', '乳山市', '3710');
INSERT INTO `base_area` VALUES ('371102', '东港区', '3711');
INSERT INTO `base_area` VALUES ('371103', '岚山区', '3711');
INSERT INTO `base_area` VALUES ('371121', '五莲县', '3711');
INSERT INTO `base_area` VALUES ('371122', '莒县', '3711');
INSERT INTO `base_area` VALUES ('371171', '日照经济技术开发区', '3711');
INSERT INTO `base_area` VALUES ('371302', '兰山区', '3713');
INSERT INTO `base_area` VALUES ('371311', '罗庄区', '3713');
INSERT INTO `base_area` VALUES ('371312', '河东区', '3713');
INSERT INTO `base_area` VALUES ('371321', '沂南县', '3713');
INSERT INTO `base_area` VALUES ('371322', '郯城县', '3713');
INSERT INTO `base_area` VALUES ('371323', '沂水县', '3713');
INSERT INTO `base_area` VALUES ('371324', '兰陵县', '3713');
INSERT INTO `base_area` VALUES ('371325', '费县', '3713');
INSERT INTO `base_area` VALUES ('371326', '平邑县', '3713');
INSERT INTO `base_area` VALUES ('371327', '莒南县', '3713');
INSERT INTO `base_area` VALUES ('371328', '蒙阴县', '3713');
INSERT INTO `base_area` VALUES ('371329', '临沭县', '3713');
INSERT INTO `base_area` VALUES ('371371', '临沂高新技术产业开发区', '3713');
INSERT INTO `base_area` VALUES ('371402', '德城区', '3714');
INSERT INTO `base_area` VALUES ('371403', '陵城区', '3714');
INSERT INTO `base_area` VALUES ('371422', '宁津县', '3714');
INSERT INTO `base_area` VALUES ('371423', '庆云县', '3714');
INSERT INTO `base_area` VALUES ('371424', '临邑县', '3714');
INSERT INTO `base_area` VALUES ('371425', '齐河县', '3714');
INSERT INTO `base_area` VALUES ('371426', '平原县', '3714');
INSERT INTO `base_area` VALUES ('371427', '夏津县', '3714');
INSERT INTO `base_area` VALUES ('371428', '武城县', '3714');
INSERT INTO `base_area` VALUES ('371471', '德州天衢新区', '3714');
INSERT INTO `base_area` VALUES ('371481', '乐陵市', '3714');
INSERT INTO `base_area` VALUES ('371482', '禹城市', '3714');
INSERT INTO `base_area` VALUES ('371502', '东昌府区', '3715');
INSERT INTO `base_area` VALUES ('371503', '茌平区', '3715');
INSERT INTO `base_area` VALUES ('371521', '阳谷县', '3715');
INSERT INTO `base_area` VALUES ('371522', '莘县', '3715');
INSERT INTO `base_area` VALUES ('371524', '东阿县', '3715');
INSERT INTO `base_area` VALUES ('371525', '冠县', '3715');
INSERT INTO `base_area` VALUES ('371526', '高唐县', '3715');
INSERT INTO `base_area` VALUES ('371581', '临清市', '3715');
INSERT INTO `base_area` VALUES ('371602', '滨城区', '3716');
INSERT INTO `base_area` VALUES ('371603', '沾化区', '3716');
INSERT INTO `base_area` VALUES ('371621', '惠民县', '3716');
INSERT INTO `base_area` VALUES ('371622', '阳信县', '3716');
INSERT INTO `base_area` VALUES ('371623', '无棣县', '3716');
INSERT INTO `base_area` VALUES ('371625', '博兴县', '3716');
INSERT INTO `base_area` VALUES ('371681', '邹平市', '3716');
INSERT INTO `base_area` VALUES ('371702', '牡丹区', '3717');
INSERT INTO `base_area` VALUES ('371703', '定陶区', '3717');
INSERT INTO `base_area` VALUES ('371721', '曹县', '3717');
INSERT INTO `base_area` VALUES ('371722', '单县', '3717');
INSERT INTO `base_area` VALUES ('371723', '成武县', '3717');
INSERT INTO `base_area` VALUES ('371724', '巨野县', '3717');
INSERT INTO `base_area` VALUES ('371725', '郓城县', '3717');
INSERT INTO `base_area` VALUES ('371726', '鄄城县', '3717');
INSERT INTO `base_area` VALUES ('371728', '东明县', '3717');
INSERT INTO `base_area` VALUES ('371771', '菏泽经济技术开发区', '3717');
INSERT INTO `base_area` VALUES ('371772', '菏泽高新技术开发区', '3717');
INSERT INTO `base_area` VALUES ('410102', '中原区', '4101');
INSERT INTO `base_area` VALUES ('410103', '二七区', '4101');
INSERT INTO `base_area` VALUES ('410104', '管城回族区', '4101');
INSERT INTO `base_area` VALUES ('410105', '金水区', '4101');
INSERT INTO `base_area` VALUES ('410106', '上街区', '4101');
INSERT INTO `base_area` VALUES ('410108', '惠济区', '4101');
INSERT INTO `base_area` VALUES ('410122', '中牟县', '4101');
INSERT INTO `base_area` VALUES ('410171', '郑州经济技术开发区', '4101');
INSERT INTO `base_area` VALUES ('410172', '郑州高新技术产业开发区', '4101');
INSERT INTO `base_area` VALUES ('410173', '郑州航空港经济综合实验区', '4101');
INSERT INTO `base_area` VALUES ('410181', '巩义市', '4101');
INSERT INTO `base_area` VALUES ('410182', '荥阳市', '4101');
INSERT INTO `base_area` VALUES ('410183', '新密市', '4101');
INSERT INTO `base_area` VALUES ('410184', '新郑市', '4101');
INSERT INTO `base_area` VALUES ('410185', '登封市', '4101');
INSERT INTO `base_area` VALUES ('410202', '龙亭区', '4102');
INSERT INTO `base_area` VALUES ('410203', '顺河回族区', '4102');
INSERT INTO `base_area` VALUES ('410204', '鼓楼区', '4102');
INSERT INTO `base_area` VALUES ('410205', '禹王台区', '4102');
INSERT INTO `base_area` VALUES ('410212', '祥符区', '4102');
INSERT INTO `base_area` VALUES ('410221', '杞县', '4102');
INSERT INTO `base_area` VALUES ('410222', '通许县', '4102');
INSERT INTO `base_area` VALUES ('410223', '尉氏县', '4102');
INSERT INTO `base_area` VALUES ('410225', '兰考县', '4102');
INSERT INTO `base_area` VALUES ('410302', '老城区', '4103');
INSERT INTO `base_area` VALUES ('410303', '西工区', '4103');
INSERT INTO `base_area` VALUES ('410304', '瀍河回族区', '4103');
INSERT INTO `base_area` VALUES ('410305', '涧西区', '4103');
INSERT INTO `base_area` VALUES ('410307', '偃师区', '4103');
INSERT INTO `base_area` VALUES ('410308', '孟津区', '4103');
INSERT INTO `base_area` VALUES ('410311', '洛龙区', '4103');
INSERT INTO `base_area` VALUES ('410323', '新安县', '4103');
INSERT INTO `base_area` VALUES ('410324', '栾川县', '4103');
INSERT INTO `base_area` VALUES ('410325', '嵩县', '4103');
INSERT INTO `base_area` VALUES ('410326', '汝阳县', '4103');
INSERT INTO `base_area` VALUES ('410327', '宜阳县', '4103');
INSERT INTO `base_area` VALUES ('410328', '洛宁县', '4103');
INSERT INTO `base_area` VALUES ('410329', '伊川县', '4103');
INSERT INTO `base_area` VALUES ('410371', '洛阳高新技术产业开发区', '4103');
INSERT INTO `base_area` VALUES ('410402', '新华区', '4104');
INSERT INTO `base_area` VALUES ('410403', '卫东区', '4104');
INSERT INTO `base_area` VALUES ('410404', '石龙区', '4104');
INSERT INTO `base_area` VALUES ('410411', '湛河区', '4104');
INSERT INTO `base_area` VALUES ('410421', '宝丰县', '4104');
INSERT INTO `base_area` VALUES ('410422', '叶县', '4104');
INSERT INTO `base_area` VALUES ('410423', '鲁山县', '4104');
INSERT INTO `base_area` VALUES ('410425', '郏县', '4104');
INSERT INTO `base_area` VALUES ('410471', '平顶山高新技术产业开发区', '4104');
INSERT INTO `base_area` VALUES ('410472', '平顶山市城乡一体化示范区', '4104');
INSERT INTO `base_area` VALUES ('410481', '舞钢市', '4104');
INSERT INTO `base_area` VALUES ('410482', '汝州市', '4104');
INSERT INTO `base_area` VALUES ('410502', '文峰区', '4105');
INSERT INTO `base_area` VALUES ('410503', '北关区', '4105');
INSERT INTO `base_area` VALUES ('410505', '殷都区', '4105');
INSERT INTO `base_area` VALUES ('410506', '龙安区', '4105');
INSERT INTO `base_area` VALUES ('410522', '安阳县', '4105');
INSERT INTO `base_area` VALUES ('410523', '汤阴县', '4105');
INSERT INTO `base_area` VALUES ('410526', '滑县', '4105');
INSERT INTO `base_area` VALUES ('410527', '内黄县', '4105');
INSERT INTO `base_area` VALUES ('410571', '安阳高新技术产业开发区', '4105');
INSERT INTO `base_area` VALUES ('410581', '林州市', '4105');
INSERT INTO `base_area` VALUES ('410602', '鹤山区', '4106');
INSERT INTO `base_area` VALUES ('410603', '山城区', '4106');
INSERT INTO `base_area` VALUES ('410611', '淇滨区', '4106');
INSERT INTO `base_area` VALUES ('410621', '浚县', '4106');
INSERT INTO `base_area` VALUES ('410622', '淇县', '4106');
INSERT INTO `base_area` VALUES ('410671', '鹤壁经济技术开发区', '4106');
INSERT INTO `base_area` VALUES ('410702', '红旗区', '4107');
INSERT INTO `base_area` VALUES ('410703', '卫滨区', '4107');
INSERT INTO `base_area` VALUES ('410704', '凤泉区', '4107');
INSERT INTO `base_area` VALUES ('410711', '牧野区', '4107');
INSERT INTO `base_area` VALUES ('410721', '新乡县', '4107');
INSERT INTO `base_area` VALUES ('410724', '获嘉县', '4107');
INSERT INTO `base_area` VALUES ('410725', '原阳县', '4107');
INSERT INTO `base_area` VALUES ('410726', '延津县', '4107');
INSERT INTO `base_area` VALUES ('410727', '封丘县', '4107');
INSERT INTO `base_area` VALUES ('410771', '新乡高新技术产业开发区', '4107');
INSERT INTO `base_area` VALUES ('410772', '新乡经济技术开发区', '4107');
INSERT INTO `base_area` VALUES ('410773', '新乡市平原城乡一体化示范区', '4107');
INSERT INTO `base_area` VALUES ('410781', '卫辉市', '4107');
INSERT INTO `base_area` VALUES ('410782', '辉县市', '4107');
INSERT INTO `base_area` VALUES ('410783', '长垣市', '4107');
INSERT INTO `base_area` VALUES ('410802', '解放区', '4108');
INSERT INTO `base_area` VALUES ('410803', '中站区', '4108');
INSERT INTO `base_area` VALUES ('410804', '马村区', '4108');
INSERT INTO `base_area` VALUES ('410811', '山阳区', '4108');
INSERT INTO `base_area` VALUES ('410821', '修武县', '4108');
INSERT INTO `base_area` VALUES ('410822', '博爱县', '4108');
INSERT INTO `base_area` VALUES ('410823', '武陟县', '4108');
INSERT INTO `base_area` VALUES ('410825', '温县', '4108');
INSERT INTO `base_area` VALUES ('410871', '焦作城乡一体化示范区', '4108');
INSERT INTO `base_area` VALUES ('410882', '沁阳市', '4108');
INSERT INTO `base_area` VALUES ('410883', '孟州市', '4108');
INSERT INTO `base_area` VALUES ('410902', '华龙区', '4109');
INSERT INTO `base_area` VALUES ('410922', '清丰县', '4109');
INSERT INTO `base_area` VALUES ('410923', '南乐县', '4109');
INSERT INTO `base_area` VALUES ('410926', '范县', '4109');
INSERT INTO `base_area` VALUES ('410927', '台前县', '4109');
INSERT INTO `base_area` VALUES ('410928', '濮阳县', '4109');
INSERT INTO `base_area` VALUES ('410971', '河南濮阳工业园区', '4109');
INSERT INTO `base_area` VALUES ('410972', '濮阳经济技术开发区', '4109');
INSERT INTO `base_area` VALUES ('411002', '魏都区', '4110');
INSERT INTO `base_area` VALUES ('411003', '建安区', '4110');
INSERT INTO `base_area` VALUES ('411024', '鄢陵县', '4110');
INSERT INTO `base_area` VALUES ('411025', '襄城县', '4110');
INSERT INTO `base_area` VALUES ('411071', '许昌经济技术开发区', '4110');
INSERT INTO `base_area` VALUES ('411081', '禹州市', '4110');
INSERT INTO `base_area` VALUES ('411082', '长葛市', '4110');
INSERT INTO `base_area` VALUES ('411102', '源汇区', '4111');
INSERT INTO `base_area` VALUES ('411103', '郾城区', '4111');
INSERT INTO `base_area` VALUES ('411104', '召陵区', '4111');
INSERT INTO `base_area` VALUES ('411121', '舞阳县', '4111');
INSERT INTO `base_area` VALUES ('411122', '临颍县', '4111');
INSERT INTO `base_area` VALUES ('411171', '漯河经济技术开发区', '4111');
INSERT INTO `base_area` VALUES ('411202', '湖滨区', '4112');
INSERT INTO `base_area` VALUES ('411203', '陕州区', '4112');
INSERT INTO `base_area` VALUES ('411221', '渑池县', '4112');
INSERT INTO `base_area` VALUES ('411224', '卢氏县', '4112');
INSERT INTO `base_area` VALUES ('411271', '河南三门峡经济开发区', '4112');
INSERT INTO `base_area` VALUES ('411281', '义马市', '4112');
INSERT INTO `base_area` VALUES ('411282', '灵宝市', '4112');
INSERT INTO `base_area` VALUES ('411302', '宛城区', '4113');
INSERT INTO `base_area` VALUES ('411303', '卧龙区', '4113');
INSERT INTO `base_area` VALUES ('411321', '南召县', '4113');
INSERT INTO `base_area` VALUES ('411322', '方城县', '4113');
INSERT INTO `base_area` VALUES ('411323', '西峡县', '4113');
INSERT INTO `base_area` VALUES ('411324', '镇平县', '4113');
INSERT INTO `base_area` VALUES ('411325', '内乡县', '4113');
INSERT INTO `base_area` VALUES ('411326', '淅川县', '4113');
INSERT INTO `base_area` VALUES ('411327', '社旗县', '4113');
INSERT INTO `base_area` VALUES ('411328', '唐河县', '4113');
INSERT INTO `base_area` VALUES ('411329', '新野县', '4113');
INSERT INTO `base_area` VALUES ('411330', '桐柏县', '4113');
INSERT INTO `base_area` VALUES ('411371', '南阳高新技术产业开发区', '4113');
INSERT INTO `base_area` VALUES ('411372', '南阳市城乡一体化示范区', '4113');
INSERT INTO `base_area` VALUES ('411381', '邓州市', '4113');
INSERT INTO `base_area` VALUES ('411402', '梁园区', '4114');
INSERT INTO `base_area` VALUES ('411403', '睢阳区', '4114');
INSERT INTO `base_area` VALUES ('411421', '民权县', '4114');
INSERT INTO `base_area` VALUES ('411422', '睢县', '4114');
INSERT INTO `base_area` VALUES ('411423', '宁陵县', '4114');
INSERT INTO `base_area` VALUES ('411424', '柘城县', '4114');
INSERT INTO `base_area` VALUES ('411425', '虞城县', '4114');
INSERT INTO `base_area` VALUES ('411426', '夏邑县', '4114');
INSERT INTO `base_area` VALUES ('411471', '豫东综合物流产业聚集区', '4114');
INSERT INTO `base_area` VALUES ('411472', '河南商丘经济开发区', '4114');
INSERT INTO `base_area` VALUES ('411481', '永城市', '4114');
INSERT INTO `base_area` VALUES ('411502', '浉河区', '4115');
INSERT INTO `base_area` VALUES ('411503', '平桥区', '4115');
INSERT INTO `base_area` VALUES ('411521', '罗山县', '4115');
INSERT INTO `base_area` VALUES ('411522', '光山县', '4115');
INSERT INTO `base_area` VALUES ('411523', '新县', '4115');
INSERT INTO `base_area` VALUES ('411524', '商城县', '4115');
INSERT INTO `base_area` VALUES ('411525', '固始县', '4115');
INSERT INTO `base_area` VALUES ('411526', '潢川县', '4115');
INSERT INTO `base_area` VALUES ('411527', '淮滨县', '4115');
INSERT INTO `base_area` VALUES ('411528', '息县', '4115');
INSERT INTO `base_area` VALUES ('411571', '信阳高新技术产业开发区', '4115');
INSERT INTO `base_area` VALUES ('411602', '川汇区', '4116');
INSERT INTO `base_area` VALUES ('411603', '淮阳区', '4116');
INSERT INTO `base_area` VALUES ('411621', '扶沟县', '4116');
INSERT INTO `base_area` VALUES ('411622', '西华县', '4116');
INSERT INTO `base_area` VALUES ('411623', '商水县', '4116');
INSERT INTO `base_area` VALUES ('411624', '沈丘县', '4116');
INSERT INTO `base_area` VALUES ('411625', '郸城县', '4116');
INSERT INTO `base_area` VALUES ('411627', '太康县', '4116');
INSERT INTO `base_area` VALUES ('411628', '鹿邑县', '4116');
INSERT INTO `base_area` VALUES ('411671', '河南周口经济开发区', '4116');
INSERT INTO `base_area` VALUES ('411681', '项城市', '4116');
INSERT INTO `base_area` VALUES ('411702', '驿城区', '4117');
INSERT INTO `base_area` VALUES ('411721', '西平县', '4117');
INSERT INTO `base_area` VALUES ('411722', '上蔡县', '4117');
INSERT INTO `base_area` VALUES ('411723', '平舆县', '4117');
INSERT INTO `base_area` VALUES ('411724', '正阳县', '4117');
INSERT INTO `base_area` VALUES ('411725', '确山县', '4117');
INSERT INTO `base_area` VALUES ('411726', '泌阳县', '4117');
INSERT INTO `base_area` VALUES ('411727', '汝南县', '4117');
INSERT INTO `base_area` VALUES ('411728', '遂平县', '4117');
INSERT INTO `base_area` VALUES ('411729', '新蔡县', '4117');
INSERT INTO `base_area` VALUES ('411771', '河南驻马店经济开发区', '4117');
INSERT INTO `base_area` VALUES ('419001', '济源市', '4190');
INSERT INTO `base_area` VALUES ('420102', '江岸区', '4201');
INSERT INTO `base_area` VALUES ('420103', '江汉区', '4201');
INSERT INTO `base_area` VALUES ('420104', '硚口区', '4201');
INSERT INTO `base_area` VALUES ('420105', '汉阳区', '4201');
INSERT INTO `base_area` VALUES ('420106', '武昌区', '4201');
INSERT INTO `base_area` VALUES ('420107', '青山区', '4201');
INSERT INTO `base_area` VALUES ('420111', '洪山区', '4201');
INSERT INTO `base_area` VALUES ('420112', '东西湖区', '4201');
INSERT INTO `base_area` VALUES ('420113', '汉南区', '4201');
INSERT INTO `base_area` VALUES ('420114', '蔡甸区', '4201');
INSERT INTO `base_area` VALUES ('420115', '江夏区', '4201');
INSERT INTO `base_area` VALUES ('420116', '黄陂区', '4201');
INSERT INTO `base_area` VALUES ('420117', '新洲区', '4201');
INSERT INTO `base_area` VALUES ('420202', '黄石港区', '4202');
INSERT INTO `base_area` VALUES ('420203', '西塞山区', '4202');
INSERT INTO `base_area` VALUES ('420204', '下陆区', '4202');
INSERT INTO `base_area` VALUES ('420205', '铁山区', '4202');
INSERT INTO `base_area` VALUES ('420222', '阳新县', '4202');
INSERT INTO `base_area` VALUES ('420281', '大冶市', '4202');
INSERT INTO `base_area` VALUES ('420302', '茅箭区', '4203');
INSERT INTO `base_area` VALUES ('420303', '张湾区', '4203');
INSERT INTO `base_area` VALUES ('420304', '郧阳区', '4203');
INSERT INTO `base_area` VALUES ('420322', '郧西县', '4203');
INSERT INTO `base_area` VALUES ('420323', '竹山县', '4203');
INSERT INTO `base_area` VALUES ('420324', '竹溪县', '4203');
INSERT INTO `base_area` VALUES ('420325', '房县', '4203');
INSERT INTO `base_area` VALUES ('420381', '丹江口市', '4203');
INSERT INTO `base_area` VALUES ('420502', '西陵区', '4205');
INSERT INTO `base_area` VALUES ('420503', '伍家岗区', '4205');
INSERT INTO `base_area` VALUES ('420504', '点军区', '4205');
INSERT INTO `base_area` VALUES ('420505', '猇亭区', '4205');
INSERT INTO `base_area` VALUES ('420506', '夷陵区', '4205');
INSERT INTO `base_area` VALUES ('420525', '远安县', '4205');
INSERT INTO `base_area` VALUES ('420526', '兴山县', '4205');
INSERT INTO `base_area` VALUES ('420527', '秭归县', '4205');
INSERT INTO `base_area` VALUES ('420528', '长阳土家族自治县', '4205');
INSERT INTO `base_area` VALUES ('420529', '五峰土家族自治县', '4205');
INSERT INTO `base_area` VALUES ('420581', '宜都市', '4205');
INSERT INTO `base_area` VALUES ('420582', '当阳市', '4205');
INSERT INTO `base_area` VALUES ('420583', '枝江市', '4205');
INSERT INTO `base_area` VALUES ('420602', '襄城区', '4206');
INSERT INTO `base_area` VALUES ('420606', '樊城区', '4206');
INSERT INTO `base_area` VALUES ('420607', '襄州区', '4206');
INSERT INTO `base_area` VALUES ('420624', '南漳县', '4206');
INSERT INTO `base_area` VALUES ('420625', '谷城县', '4206');
INSERT INTO `base_area` VALUES ('420626', '保康县', '4206');
INSERT INTO `base_area` VALUES ('420682', '老河口市', '4206');
INSERT INTO `base_area` VALUES ('420683', '枣阳市', '4206');
INSERT INTO `base_area` VALUES ('420684', '宜城市', '4206');
INSERT INTO `base_area` VALUES ('420702', '梁子湖区', '4207');
INSERT INTO `base_area` VALUES ('420703', '华容区', '4207');
INSERT INTO `base_area` VALUES ('420704', '鄂城区', '4207');
INSERT INTO `base_area` VALUES ('420802', '东宝区', '4208');
INSERT INTO `base_area` VALUES ('420804', '掇刀区', '4208');
INSERT INTO `base_area` VALUES ('420822', '沙洋县', '4208');
INSERT INTO `base_area` VALUES ('420881', '钟祥市', '4208');
INSERT INTO `base_area` VALUES ('420882', '京山市', '4208');
INSERT INTO `base_area` VALUES ('420902', '孝南区', '4209');
INSERT INTO `base_area` VALUES ('420921', '孝昌县', '4209');
INSERT INTO `base_area` VALUES ('420922', '大悟县', '4209');
INSERT INTO `base_area` VALUES ('420923', '云梦县', '4209');
INSERT INTO `base_area` VALUES ('420981', '应城市', '4209');
INSERT INTO `base_area` VALUES ('420982', '安陆市', '4209');
INSERT INTO `base_area` VALUES ('420984', '汉川市', '4209');
INSERT INTO `base_area` VALUES ('421002', '沙市区', '4210');
INSERT INTO `base_area` VALUES ('421003', '荆州区', '4210');
INSERT INTO `base_area` VALUES ('421022', '公安县', '4210');
INSERT INTO `base_area` VALUES ('421024', '江陵县', '4210');
INSERT INTO `base_area` VALUES ('421071', '荆州经济技术开发区', '4210');
INSERT INTO `base_area` VALUES ('421081', '石首市', '4210');
INSERT INTO `base_area` VALUES ('421083', '洪湖市', '4210');
INSERT INTO `base_area` VALUES ('421087', '松滋市', '4210');
INSERT INTO `base_area` VALUES ('421088', '监利市', '4210');
INSERT INTO `base_area` VALUES ('421102', '黄州区', '4211');
INSERT INTO `base_area` VALUES ('421121', '团风县', '4211');
INSERT INTO `base_area` VALUES ('421122', '红安县', '4211');
INSERT INTO `base_area` VALUES ('421123', '罗田县', '4211');
INSERT INTO `base_area` VALUES ('421124', '英山县', '4211');
INSERT INTO `base_area` VALUES ('421125', '浠水县', '4211');
INSERT INTO `base_area` VALUES ('421126', '蕲春县', '4211');
INSERT INTO `base_area` VALUES ('421127', '黄梅县', '4211');
INSERT INTO `base_area` VALUES ('421171', '龙感湖管理区', '4211');
INSERT INTO `base_area` VALUES ('421181', '麻城市', '4211');
INSERT INTO `base_area` VALUES ('421182', '武穴市', '4211');
INSERT INTO `base_area` VALUES ('421202', '咸安区', '4212');
INSERT INTO `base_area` VALUES ('421221', '嘉鱼县', '4212');
INSERT INTO `base_area` VALUES ('421222', '通城县', '4212');
INSERT INTO `base_area` VALUES ('421223', '崇阳县', '4212');
INSERT INTO `base_area` VALUES ('421224', '通山县', '4212');
INSERT INTO `base_area` VALUES ('421281', '赤壁市', '4212');
INSERT INTO `base_area` VALUES ('421303', '曾都区', '4213');
INSERT INTO `base_area` VALUES ('421321', '随县', '4213');
INSERT INTO `base_area` VALUES ('421381', '广水市', '4213');
INSERT INTO `base_area` VALUES ('422801', '恩施市', '4228');
INSERT INTO `base_area` VALUES ('422802', '利川市', '4228');
INSERT INTO `base_area` VALUES ('422822', '建始县', '4228');
INSERT INTO `base_area` VALUES ('422823', '巴东县', '4228');
INSERT INTO `base_area` VALUES ('422825', '宣恩县', '4228');
INSERT INTO `base_area` VALUES ('422826', '咸丰县', '4228');
INSERT INTO `base_area` VALUES ('422827', '来凤县', '4228');
INSERT INTO `base_area` VALUES ('422828', '鹤峰县', '4228');
INSERT INTO `base_area` VALUES ('429004', '仙桃市', '4290');
INSERT INTO `base_area` VALUES ('429005', '潜江市', '4290');
INSERT INTO `base_area` VALUES ('429006', '天门市', '4290');
INSERT INTO `base_area` VALUES ('429021', '神农架林区', '4290');
INSERT INTO `base_area` VALUES ('430102', '芙蓉区', '4301');
INSERT INTO `base_area` VALUES ('430103', '天心区', '4301');
INSERT INTO `base_area` VALUES ('430104', '岳麓区', '4301');
INSERT INTO `base_area` VALUES ('430105', '开福区', '4301');
INSERT INTO `base_area` VALUES ('430111', '雨花区', '4301');
INSERT INTO `base_area` VALUES ('430112', '望城区', '4301');
INSERT INTO `base_area` VALUES ('430121', '长沙县', '4301');
INSERT INTO `base_area` VALUES ('430181', '浏阳市', '4301');
INSERT INTO `base_area` VALUES ('430182', '宁乡市', '4301');
INSERT INTO `base_area` VALUES ('430202', '荷塘区', '4302');
INSERT INTO `base_area` VALUES ('430203', '芦淞区', '4302');
INSERT INTO `base_area` VALUES ('430204', '石峰区', '4302');
INSERT INTO `base_area` VALUES ('430211', '天元区', '4302');
INSERT INTO `base_area` VALUES ('430212', '渌口区', '4302');
INSERT INTO `base_area` VALUES ('430223', '攸县', '4302');
INSERT INTO `base_area` VALUES ('430224', '茶陵县', '4302');
INSERT INTO `base_area` VALUES ('430225', '炎陵县', '4302');
INSERT INTO `base_area` VALUES ('430281', '醴陵市', '4302');
INSERT INTO `base_area` VALUES ('430302', '雨湖区', '4303');
INSERT INTO `base_area` VALUES ('430304', '岳塘区', '4303');
INSERT INTO `base_area` VALUES ('430321', '湘潭县', '4303');
INSERT INTO `base_area` VALUES ('430371', '湖南湘潭高新技术产业园区', '4303');
INSERT INTO `base_area` VALUES ('430372', '湘潭昭山示范区', '4303');
INSERT INTO `base_area` VALUES ('430373', '湘潭九华示范区', '4303');
INSERT INTO `base_area` VALUES ('430381', '湘乡市', '4303');
INSERT INTO `base_area` VALUES ('430382', '韶山市', '4303');
INSERT INTO `base_area` VALUES ('430405', '珠晖区', '4304');
INSERT INTO `base_area` VALUES ('430406', '雁峰区', '4304');
INSERT INTO `base_area` VALUES ('430407', '石鼓区', '4304');
INSERT INTO `base_area` VALUES ('430408', '蒸湘区', '4304');
INSERT INTO `base_area` VALUES ('430412', '南岳区', '4304');
INSERT INTO `base_area` VALUES ('430421', '衡阳县', '4304');
INSERT INTO `base_area` VALUES ('430422', '衡南县', '4304');
INSERT INTO `base_area` VALUES ('430423', '衡山县', '4304');
INSERT INTO `base_area` VALUES ('430424', '衡东县', '4304');
INSERT INTO `base_area` VALUES ('430426', '祁东县', '4304');
INSERT INTO `base_area` VALUES ('430471', '衡阳综合保税区', '4304');
INSERT INTO `base_area` VALUES ('430472', '湖南衡阳高新技术产业园区', '4304');
INSERT INTO `base_area` VALUES ('430473', '湖南衡阳松木经济开发区', '4304');
INSERT INTO `base_area` VALUES ('430481', '耒阳市', '4304');
INSERT INTO `base_area` VALUES ('430482', '常宁市', '4304');
INSERT INTO `base_area` VALUES ('430502', '双清区', '4305');
INSERT INTO `base_area` VALUES ('430503', '大祥区', '4305');
INSERT INTO `base_area` VALUES ('430511', '北塔区', '4305');
INSERT INTO `base_area` VALUES ('430522', '新邵县', '4305');
INSERT INTO `base_area` VALUES ('430523', '邵阳县', '4305');
INSERT INTO `base_area` VALUES ('430524', '隆回县', '4305');
INSERT INTO `base_area` VALUES ('430525', '洞口县', '4305');
INSERT INTO `base_area` VALUES ('430527', '绥宁县', '4305');
INSERT INTO `base_area` VALUES ('430528', '新宁县', '4305');
INSERT INTO `base_area` VALUES ('430529', '城步苗族自治县', '4305');
INSERT INTO `base_area` VALUES ('430581', '武冈市', '4305');
INSERT INTO `base_area` VALUES ('430582', '邵东市', '4305');
INSERT INTO `base_area` VALUES ('430602', '岳阳楼区', '4306');
INSERT INTO `base_area` VALUES ('430603', '云溪区', '4306');
INSERT INTO `base_area` VALUES ('430611', '君山区', '4306');
INSERT INTO `base_area` VALUES ('430621', '岳阳县', '4306');
INSERT INTO `base_area` VALUES ('430623', '华容县', '4306');
INSERT INTO `base_area` VALUES ('430624', '湘阴县', '4306');
INSERT INTO `base_area` VALUES ('430626', '平江县', '4306');
INSERT INTO `base_area` VALUES ('430671', '岳阳市屈原管理区', '4306');
INSERT INTO `base_area` VALUES ('430681', '汨罗市', '4306');
INSERT INTO `base_area` VALUES ('430682', '临湘市', '4306');
INSERT INTO `base_area` VALUES ('430702', '武陵区', '4307');
INSERT INTO `base_area` VALUES ('430703', '鼎城区', '4307');
INSERT INTO `base_area` VALUES ('430721', '安乡县', '4307');
INSERT INTO `base_area` VALUES ('430722', '汉寿县', '4307');
INSERT INTO `base_area` VALUES ('430723', '澧县', '4307');
INSERT INTO `base_area` VALUES ('430724', '临澧县', '4307');
INSERT INTO `base_area` VALUES ('430725', '桃源县', '4307');
INSERT INTO `base_area` VALUES ('430726', '石门县', '4307');
INSERT INTO `base_area` VALUES ('430771', '常德市西洞庭管理区', '4307');
INSERT INTO `base_area` VALUES ('430781', '津市市', '4307');
INSERT INTO `base_area` VALUES ('430802', '永定区', '4308');
INSERT INTO `base_area` VALUES ('430811', '武陵源区', '4308');
INSERT INTO `base_area` VALUES ('430821', '慈利县', '4308');
INSERT INTO `base_area` VALUES ('430822', '桑植县', '4308');
INSERT INTO `base_area` VALUES ('430902', '资阳区', '4309');
INSERT INTO `base_area` VALUES ('430903', '赫山区', '4309');
INSERT INTO `base_area` VALUES ('430921', '南县', '4309');
INSERT INTO `base_area` VALUES ('430922', '桃江县', '4309');
INSERT INTO `base_area` VALUES ('430923', '安化县', '4309');
INSERT INTO `base_area` VALUES ('430971', '益阳市大通湖管理区', '4309');
INSERT INTO `base_area` VALUES ('430972', '湖南益阳高新技术产业园区', '4309');
INSERT INTO `base_area` VALUES ('430981', '沅江市', '4309');
INSERT INTO `base_area` VALUES ('431002', '北湖区', '4310');
INSERT INTO `base_area` VALUES ('431003', '苏仙区', '4310');
INSERT INTO `base_area` VALUES ('431021', '桂阳县', '4310');
INSERT INTO `base_area` VALUES ('431022', '宜章县', '4310');
INSERT INTO `base_area` VALUES ('431023', '永兴县', '4310');
INSERT INTO `base_area` VALUES ('431024', '嘉禾县', '4310');
INSERT INTO `base_area` VALUES ('431025', '临武县', '4310');
INSERT INTO `base_area` VALUES ('431026', '汝城县', '4310');
INSERT INTO `base_area` VALUES ('431027', '桂东县', '4310');
INSERT INTO `base_area` VALUES ('431028', '安仁县', '4310');
INSERT INTO `base_area` VALUES ('431081', '资兴市', '4310');
INSERT INTO `base_area` VALUES ('431102', '零陵区', '4311');
INSERT INTO `base_area` VALUES ('431103', '冷水滩区', '4311');
INSERT INTO `base_area` VALUES ('431122', '东安县', '4311');
INSERT INTO `base_area` VALUES ('431123', '双牌县', '4311');
INSERT INTO `base_area` VALUES ('431124', '道县', '4311');
INSERT INTO `base_area` VALUES ('431125', '江永县', '4311');
INSERT INTO `base_area` VALUES ('431126', '宁远县', '4311');
INSERT INTO `base_area` VALUES ('431127', '蓝山县', '4311');
INSERT INTO `base_area` VALUES ('431128', '新田县', '4311');
INSERT INTO `base_area` VALUES ('431129', '江华瑶族自治县', '4311');
INSERT INTO `base_area` VALUES ('431171', '永州经济技术开发区', '4311');
INSERT INTO `base_area` VALUES ('431173', '永州市回龙圩管理区', '4311');
INSERT INTO `base_area` VALUES ('431181', '祁阳市', '4311');
INSERT INTO `base_area` VALUES ('431202', '鹤城区', '4312');
INSERT INTO `base_area` VALUES ('431221', '中方县', '4312');
INSERT INTO `base_area` VALUES ('431222', '沅陵县', '4312');
INSERT INTO `base_area` VALUES ('431223', '辰溪县', '4312');
INSERT INTO `base_area` VALUES ('431224', '溆浦县', '4312');
INSERT INTO `base_area` VALUES ('431225', '会同县', '4312');
INSERT INTO `base_area` VALUES ('431226', '麻阳苗族自治县', '4312');
INSERT INTO `base_area` VALUES ('431227', '新晃侗族自治县', '4312');
INSERT INTO `base_area` VALUES ('431228', '芷江侗族自治县', '4312');
INSERT INTO `base_area` VALUES ('431229', '靖州苗族侗族自治县', '4312');
INSERT INTO `base_area` VALUES ('431230', '通道侗族自治县', '4312');
INSERT INTO `base_area` VALUES ('431271', '怀化市洪江管理区', '4312');
INSERT INTO `base_area` VALUES ('431281', '洪江市', '4312');
INSERT INTO `base_area` VALUES ('431302', '娄星区', '4313');
INSERT INTO `base_area` VALUES ('431321', '双峰县', '4313');
INSERT INTO `base_area` VALUES ('431322', '新化县', '4313');
INSERT INTO `base_area` VALUES ('431381', '冷水江市', '4313');
INSERT INTO `base_area` VALUES ('431382', '涟源市', '4313');
INSERT INTO `base_area` VALUES ('433101', '吉首市', '4331');
INSERT INTO `base_area` VALUES ('433122', '泸溪县', '4331');
INSERT INTO `base_area` VALUES ('433123', '凤凰县', '4331');
INSERT INTO `base_area` VALUES ('433124', '花垣县', '4331');
INSERT INTO `base_area` VALUES ('433125', '保靖县', '4331');
INSERT INTO `base_area` VALUES ('433126', '古丈县', '4331');
INSERT INTO `base_area` VALUES ('433127', '永顺县', '4331');
INSERT INTO `base_area` VALUES ('433130', '龙山县', '4331');
INSERT INTO `base_area` VALUES ('440103', '荔湾区', '4401');
INSERT INTO `base_area` VALUES ('440104', '越秀区', '4401');
INSERT INTO `base_area` VALUES ('440105', '海珠区', '4401');
INSERT INTO `base_area` VALUES ('440106', '天河区', '4401');
INSERT INTO `base_area` VALUES ('440111', '白云区', '4401');
INSERT INTO `base_area` VALUES ('440112', '黄埔区', '4401');
INSERT INTO `base_area` VALUES ('440113', '番禺区', '4401');
INSERT INTO `base_area` VALUES ('440114', '花都区', '4401');
INSERT INTO `base_area` VALUES ('440115', '南沙区', '4401');
INSERT INTO `base_area` VALUES ('440117', '从化区', '4401');
INSERT INTO `base_area` VALUES ('440118', '增城区', '4401');
INSERT INTO `base_area` VALUES ('440203', '武江区', '4402');
INSERT INTO `base_area` VALUES ('440204', '浈江区', '4402');
INSERT INTO `base_area` VALUES ('440205', '曲江区', '4402');
INSERT INTO `base_area` VALUES ('440222', '始兴县', '4402');
INSERT INTO `base_area` VALUES ('440224', '仁化县', '4402');
INSERT INTO `base_area` VALUES ('440229', '翁源县', '4402');
INSERT INTO `base_area` VALUES ('440232', '乳源瑶族自治县', '4402');
INSERT INTO `base_area` VALUES ('440233', '新丰县', '4402');
INSERT INTO `base_area` VALUES ('440281', '乐昌市', '4402');
INSERT INTO `base_area` VALUES ('440282', '南雄市', '4402');
INSERT INTO `base_area` VALUES ('440303', '罗湖区', '4403');
INSERT INTO `base_area` VALUES ('440304', '福田区', '4403');
INSERT INTO `base_area` VALUES ('440305', '南山区', '4403');
INSERT INTO `base_area` VALUES ('440306', '宝安区', '4403');
INSERT INTO `base_area` VALUES ('440307', '龙岗区', '4403');
INSERT INTO `base_area` VALUES ('440308', '盐田区', '4403');
INSERT INTO `base_area` VALUES ('440309', '龙华区', '4403');
INSERT INTO `base_area` VALUES ('440310', '坪山区', '4403');
INSERT INTO `base_area` VALUES ('440311', '光明区', '4403');
INSERT INTO `base_area` VALUES ('440402', '香洲区', '4404');
INSERT INTO `base_area` VALUES ('440403', '斗门区', '4404');
INSERT INTO `base_area` VALUES ('440404', '金湾区', '4404');
INSERT INTO `base_area` VALUES ('440507', '龙湖区', '4405');
INSERT INTO `base_area` VALUES ('440511', '金平区', '4405');
INSERT INTO `base_area` VALUES ('440512', '濠江区', '4405');
INSERT INTO `base_area` VALUES ('440513', '潮阳区', '4405');
INSERT INTO `base_area` VALUES ('440514', '潮南区', '4405');
INSERT INTO `base_area` VALUES ('440515', '澄海区', '4405');
INSERT INTO `base_area` VALUES ('440523', '南澳县', '4405');
INSERT INTO `base_area` VALUES ('440604', '禅城区', '4406');
INSERT INTO `base_area` VALUES ('440605', '南海区', '4406');
INSERT INTO `base_area` VALUES ('440606', '顺德区', '4406');
INSERT INTO `base_area` VALUES ('440607', '三水区', '4406');
INSERT INTO `base_area` VALUES ('440608', '高明区', '4406');
INSERT INTO `base_area` VALUES ('440703', '蓬江区', '4407');
INSERT INTO `base_area` VALUES ('440704', '江海区', '4407');
INSERT INTO `base_area` VALUES ('440705', '新会区', '4407');
INSERT INTO `base_area` VALUES ('440781', '台山市', '4407');
INSERT INTO `base_area` VALUES ('440783', '开平市', '4407');
INSERT INTO `base_area` VALUES ('440784', '鹤山市', '4407');
INSERT INTO `base_area` VALUES ('440785', '恩平市', '4407');
INSERT INTO `base_area` VALUES ('440802', '赤坎区', '4408');
INSERT INTO `base_area` VALUES ('440803', '霞山区', '4408');
INSERT INTO `base_area` VALUES ('440804', '坡头区', '4408');
INSERT INTO `base_area` VALUES ('440811', '麻章区', '4408');
INSERT INTO `base_area` VALUES ('440823', '遂溪县', '4408');
INSERT INTO `base_area` VALUES ('440825', '徐闻县', '4408');
INSERT INTO `base_area` VALUES ('440881', '廉江市', '4408');
INSERT INTO `base_area` VALUES ('440882', '雷州市', '4408');
INSERT INTO `base_area` VALUES ('440883', '吴川市', '4408');
INSERT INTO `base_area` VALUES ('440902', '茂南区', '4409');
INSERT INTO `base_area` VALUES ('440904', '电白区', '4409');
INSERT INTO `base_area` VALUES ('440981', '高州市', '4409');
INSERT INTO `base_area` VALUES ('440982', '化州市', '4409');
INSERT INTO `base_area` VALUES ('440983', '信宜市', '4409');
INSERT INTO `base_area` VALUES ('441202', '端州区', '4412');
INSERT INTO `base_area` VALUES ('441203', '鼎湖区', '4412');
INSERT INTO `base_area` VALUES ('441204', '高要区', '4412');
INSERT INTO `base_area` VALUES ('441223', '广宁县', '4412');
INSERT INTO `base_area` VALUES ('441224', '怀集县', '4412');
INSERT INTO `base_area` VALUES ('441225', '封开县', '4412');
INSERT INTO `base_area` VALUES ('441226', '德庆县', '4412');
INSERT INTO `base_area` VALUES ('441284', '四会市', '4412');
INSERT INTO `base_area` VALUES ('441302', '惠城区', '4413');
INSERT INTO `base_area` VALUES ('441303', '惠阳区', '4413');
INSERT INTO `base_area` VALUES ('441322', '博罗县', '4413');
INSERT INTO `base_area` VALUES ('441323', '惠东县', '4413');
INSERT INTO `base_area` VALUES ('441324', '龙门县', '4413');
INSERT INTO `base_area` VALUES ('441402', '梅江区', '4414');
INSERT INTO `base_area` VALUES ('441403', '梅县区', '4414');
INSERT INTO `base_area` VALUES ('441422', '大埔县', '4414');
INSERT INTO `base_area` VALUES ('441423', '丰顺县', '4414');
INSERT INTO `base_area` VALUES ('441424', '五华县', '4414');
INSERT INTO `base_area` VALUES ('441426', '平远县', '4414');
INSERT INTO `base_area` VALUES ('441427', '蕉岭县', '4414');
INSERT INTO `base_area` VALUES ('441481', '兴宁市', '4414');
INSERT INTO `base_area` VALUES ('441502', '城区', '4415');
INSERT INTO `base_area` VALUES ('441521', '海丰县', '4415');
INSERT INTO `base_area` VALUES ('441523', '陆河县', '4415');
INSERT INTO `base_area` VALUES ('441581', '陆丰市', '4415');
INSERT INTO `base_area` VALUES ('441602', '源城区', '4416');
INSERT INTO `base_area` VALUES ('441621', '紫金县', '4416');
INSERT INTO `base_area` VALUES ('441622', '龙川县', '4416');
INSERT INTO `base_area` VALUES ('441623', '连平县', '4416');
INSERT INTO `base_area` VALUES ('441624', '和平县', '4416');
INSERT INTO `base_area` VALUES ('441625', '东源县', '4416');
INSERT INTO `base_area` VALUES ('441702', '江城区', '4417');
INSERT INTO `base_area` VALUES ('441704', '阳东区', '4417');
INSERT INTO `base_area` VALUES ('441721', '阳西县', '4417');
INSERT INTO `base_area` VALUES ('441781', '阳春市', '4417');
INSERT INTO `base_area` VALUES ('441802', '清城区', '4418');
INSERT INTO `base_area` VALUES ('441803', '清新区', '4418');
INSERT INTO `base_area` VALUES ('441821', '佛冈县', '4418');
INSERT INTO `base_area` VALUES ('441823', '阳山县', '4418');
INSERT INTO `base_area` VALUES ('441825', '连山壮族瑶族自治县', '4418');
INSERT INTO `base_area` VALUES ('441826', '连南瑶族自治县', '4418');
INSERT INTO `base_area` VALUES ('441881', '英德市', '4418');
INSERT INTO `base_area` VALUES ('441882', '连州市', '4418');
INSERT INTO `base_area` VALUES ('441900', '东莞市', '4419');
INSERT INTO `base_area` VALUES ('442000', '中山市', '4420');
INSERT INTO `base_area` VALUES ('445102', '湘桥区', '4451');
INSERT INTO `base_area` VALUES ('445103', '潮安区', '4451');
INSERT INTO `base_area` VALUES ('445122', '饶平县', '4451');
INSERT INTO `base_area` VALUES ('445202', '榕城区', '4452');
INSERT INTO `base_area` VALUES ('445203', '揭东区', '4452');
INSERT INTO `base_area` VALUES ('445222', '揭西县', '4452');
INSERT INTO `base_area` VALUES ('445224', '惠来县', '4452');
INSERT INTO `base_area` VALUES ('445281', '普宁市', '4452');
INSERT INTO `base_area` VALUES ('445302', '云城区', '4453');
INSERT INTO `base_area` VALUES ('445303', '云安区', '4453');
INSERT INTO `base_area` VALUES ('445321', '新兴县', '4453');
INSERT INTO `base_area` VALUES ('445322', '郁南县', '4453');
INSERT INTO `base_area` VALUES ('445381', '罗定市', '4453');
INSERT INTO `base_area` VALUES ('450102', '兴宁区', '4501');
INSERT INTO `base_area` VALUES ('450103', '青秀区', '4501');
INSERT INTO `base_area` VALUES ('450105', '江南区', '4501');
INSERT INTO `base_area` VALUES ('450107', '西乡塘区', '4501');
INSERT INTO `base_area` VALUES ('450108', '良庆区', '4501');
INSERT INTO `base_area` VALUES ('450109', '邕宁区', '4501');
INSERT INTO `base_area` VALUES ('450110', '武鸣区', '4501');
INSERT INTO `base_area` VALUES ('450123', '隆安县', '4501');
INSERT INTO `base_area` VALUES ('450124', '马山县', '4501');
INSERT INTO `base_area` VALUES ('450125', '上林县', '4501');
INSERT INTO `base_area` VALUES ('450126', '宾阳县', '4501');
INSERT INTO `base_area` VALUES ('450181', '横州市', '4501');
INSERT INTO `base_area` VALUES ('450202', '城中区', '4502');
INSERT INTO `base_area` VALUES ('450203', '鱼峰区', '4502');
INSERT INTO `base_area` VALUES ('450204', '柳南区', '4502');
INSERT INTO `base_area` VALUES ('450205', '柳北区', '4502');
INSERT INTO `base_area` VALUES ('450206', '柳江区', '4502');
INSERT INTO `base_area` VALUES ('450222', '柳城县', '4502');
INSERT INTO `base_area` VALUES ('450223', '鹿寨县', '4502');
INSERT INTO `base_area` VALUES ('450224', '融安县', '4502');
INSERT INTO `base_area` VALUES ('450225', '融水苗族自治县', '4502');
INSERT INTO `base_area` VALUES ('450226', '三江侗族自治县', '4502');
INSERT INTO `base_area` VALUES ('450302', '秀峰区', '4503');
INSERT INTO `base_area` VALUES ('450303', '叠彩区', '4503');
INSERT INTO `base_area` VALUES ('450304', '象山区', '4503');
INSERT INTO `base_area` VALUES ('450305', '七星区', '4503');
INSERT INTO `base_area` VALUES ('450311', '雁山区', '4503');
INSERT INTO `base_area` VALUES ('450312', '临桂区', '4503');
INSERT INTO `base_area` VALUES ('450321', '阳朔县', '4503');
INSERT INTO `base_area` VALUES ('450323', '灵川县', '4503');
INSERT INTO `base_area` VALUES ('450324', '全州县', '4503');
INSERT INTO `base_area` VALUES ('450325', '兴安县', '4503');
INSERT INTO `base_area` VALUES ('450326', '永福县', '4503');
INSERT INTO `base_area` VALUES ('450327', '灌阳县', '4503');
INSERT INTO `base_area` VALUES ('450328', '龙胜各族自治县', '4503');
INSERT INTO `base_area` VALUES ('450329', '资源县', '4503');
INSERT INTO `base_area` VALUES ('450330', '平乐县', '4503');
INSERT INTO `base_area` VALUES ('450332', '恭城瑶族自治县', '4503');
INSERT INTO `base_area` VALUES ('450381', '荔浦市', '4503');
INSERT INTO `base_area` VALUES ('450403', '万秀区', '4504');
INSERT INTO `base_area` VALUES ('450405', '长洲区', '4504');
INSERT INTO `base_area` VALUES ('450406', '龙圩区', '4504');
INSERT INTO `base_area` VALUES ('450421', '苍梧县', '4504');
INSERT INTO `base_area` VALUES ('450422', '藤县', '4504');
INSERT INTO `base_area` VALUES ('450423', '蒙山县', '4504');
INSERT INTO `base_area` VALUES ('450481', '岑溪市', '4504');
INSERT INTO `base_area` VALUES ('450502', '海城区', '4505');
INSERT INTO `base_area` VALUES ('450503', '银海区', '4505');
INSERT INTO `base_area` VALUES ('450512', '铁山港区', '4505');
INSERT INTO `base_area` VALUES ('450521', '合浦县', '4505');
INSERT INTO `base_area` VALUES ('450602', '港口区', '4506');
INSERT INTO `base_area` VALUES ('450603', '防城区', '4506');
INSERT INTO `base_area` VALUES ('450621', '上思县', '4506');
INSERT INTO `base_area` VALUES ('450681', '东兴市', '4506');
INSERT INTO `base_area` VALUES ('450702', '钦南区', '4507');
INSERT INTO `base_area` VALUES ('450703', '钦北区', '4507');
INSERT INTO `base_area` VALUES ('450721', '灵山县', '4507');
INSERT INTO `base_area` VALUES ('450722', '浦北县', '4507');
INSERT INTO `base_area` VALUES ('450802', '港北区', '4508');
INSERT INTO `base_area` VALUES ('450803', '港南区', '4508');
INSERT INTO `base_area` VALUES ('450804', '覃塘区', '4508');
INSERT INTO `base_area` VALUES ('450821', '平南县', '4508');
INSERT INTO `base_area` VALUES ('450881', '桂平市', '4508');
INSERT INTO `base_area` VALUES ('450902', '玉州区', '4509');
INSERT INTO `base_area` VALUES ('450903', '福绵区', '4509');
INSERT INTO `base_area` VALUES ('450921', '容县', '4509');
INSERT INTO `base_area` VALUES ('450922', '陆川县', '4509');
INSERT INTO `base_area` VALUES ('450923', '博白县', '4509');
INSERT INTO `base_area` VALUES ('450924', '兴业县', '4509');
INSERT INTO `base_area` VALUES ('450981', '北流市', '4509');
INSERT INTO `base_area` VALUES ('451002', '右江区', '4510');
INSERT INTO `base_area` VALUES ('451003', '田阳区', '4510');
INSERT INTO `base_area` VALUES ('451022', '田东县', '4510');
INSERT INTO `base_area` VALUES ('451024', '德保县', '4510');
INSERT INTO `base_area` VALUES ('451026', '那坡县', '4510');
INSERT INTO `base_area` VALUES ('451027', '凌云县', '4510');
INSERT INTO `base_area` VALUES ('451028', '乐业县', '4510');
INSERT INTO `base_area` VALUES ('451029', '田林县', '4510');
INSERT INTO `base_area` VALUES ('451030', '西林县', '4510');
INSERT INTO `base_area` VALUES ('451031', '隆林各族自治县', '4510');
INSERT INTO `base_area` VALUES ('451081', '靖西市', '4510');
INSERT INTO `base_area` VALUES ('451082', '平果市', '4510');
INSERT INTO `base_area` VALUES ('451102', '八步区', '4511');
INSERT INTO `base_area` VALUES ('451103', '平桂区', '4511');
INSERT INTO `base_area` VALUES ('451121', '昭平县', '4511');
INSERT INTO `base_area` VALUES ('451122', '钟山县', '4511');
INSERT INTO `base_area` VALUES ('451123', '富川瑶族自治县', '4511');
INSERT INTO `base_area` VALUES ('451202', '金城江区', '4512');
INSERT INTO `base_area` VALUES ('451203', '宜州区', '4512');
INSERT INTO `base_area` VALUES ('451221', '南丹县', '4512');
INSERT INTO `base_area` VALUES ('451222', '天峨县', '4512');
INSERT INTO `base_area` VALUES ('451223', '凤山县', '4512');
INSERT INTO `base_area` VALUES ('451224', '东兰县', '4512');
INSERT INTO `base_area` VALUES ('451225', '罗城仫佬族自治县', '4512');
INSERT INTO `base_area` VALUES ('451226', '环江毛南族自治县', '4512');
INSERT INTO `base_area` VALUES ('451227', '巴马瑶族自治县', '4512');
INSERT INTO `base_area` VALUES ('451228', '都安瑶族自治县', '4512');
INSERT INTO `base_area` VALUES ('451229', '大化瑶族自治县', '4512');
INSERT INTO `base_area` VALUES ('451302', '兴宾区', '4513');
INSERT INTO `base_area` VALUES ('451321', '忻城县', '4513');
INSERT INTO `base_area` VALUES ('451322', '象州县', '4513');
INSERT INTO `base_area` VALUES ('451323', '武宣县', '4513');
INSERT INTO `base_area` VALUES ('451324', '金秀瑶族自治县', '4513');
INSERT INTO `base_area` VALUES ('451381', '合山市', '4513');
INSERT INTO `base_area` VALUES ('451402', '江州区', '4514');
INSERT INTO `base_area` VALUES ('451421', '扶绥县', '4514');
INSERT INTO `base_area` VALUES ('451422', '宁明县', '4514');
INSERT INTO `base_area` VALUES ('451423', '龙州县', '4514');
INSERT INTO `base_area` VALUES ('451424', '大新县', '4514');
INSERT INTO `base_area` VALUES ('451425', '天等县', '4514');
INSERT INTO `base_area` VALUES ('451481', '凭祥市', '4514');
INSERT INTO `base_area` VALUES ('460105', '秀英区', '4601');
INSERT INTO `base_area` VALUES ('460106', '龙华区', '4601');
INSERT INTO `base_area` VALUES ('460107', '琼山区', '4601');
INSERT INTO `base_area` VALUES ('460108', '美兰区', '4601');
INSERT INTO `base_area` VALUES ('460202', '海棠区', '4602');
INSERT INTO `base_area` VALUES ('460203', '吉阳区', '4602');
INSERT INTO `base_area` VALUES ('460204', '天涯区', '4602');
INSERT INTO `base_area` VALUES ('460205', '崖州区', '4602');
INSERT INTO `base_area` VALUES ('460321', '西沙群岛', '4603');
INSERT INTO `base_area` VALUES ('460322', '南沙群岛', '4603');
INSERT INTO `base_area` VALUES ('460323', '中沙群岛的岛礁及其海域', '4603');
INSERT INTO `base_area` VALUES ('460400', '儋州市', '4604');
INSERT INTO `base_area` VALUES ('469001', '五指山市', '4690');
INSERT INTO `base_area` VALUES ('469002', '琼海市', '4690');
INSERT INTO `base_area` VALUES ('469005', '文昌市', '4690');
INSERT INTO `base_area` VALUES ('469006', '万宁市', '4690');
INSERT INTO `base_area` VALUES ('469007', '东方市', '4690');
INSERT INTO `base_area` VALUES ('469021', '定安县', '4690');
INSERT INTO `base_area` VALUES ('469022', '屯昌县', '4690');
INSERT INTO `base_area` VALUES ('469023', '澄迈县', '4690');
INSERT INTO `base_area` VALUES ('469024', '临高县', '4690');
INSERT INTO `base_area` VALUES ('469025', '白沙黎族自治县', '4690');
INSERT INTO `base_area` VALUES ('469026', '昌江黎族自治县', '4690');
INSERT INTO `base_area` VALUES ('469027', '乐东黎族自治县', '4690');
INSERT INTO `base_area` VALUES ('469028', '陵水黎族自治县', '4690');
INSERT INTO `base_area` VALUES ('469029', '保亭黎族苗族自治县', '4690');
INSERT INTO `base_area` VALUES ('469030', '琼中黎族苗族自治县', '4690');
INSERT INTO `base_area` VALUES ('500101', '万州区', '5001');
INSERT INTO `base_area` VALUES ('500102', '涪陵区', '5001');
INSERT INTO `base_area` VALUES ('500103', '渝中区', '5001');
INSERT INTO `base_area` VALUES ('500104', '大渡口区', '5001');
INSERT INTO `base_area` VALUES ('500105', '江北区', '5001');
INSERT INTO `base_area` VALUES ('500106', '沙坪坝区', '5001');
INSERT INTO `base_area` VALUES ('500107', '九龙坡区', '5001');
INSERT INTO `base_area` VALUES ('500108', '南岸区', '5001');
INSERT INTO `base_area` VALUES ('500109', '北碚区', '5001');
INSERT INTO `base_area` VALUES ('500110', '綦江区', '5001');
INSERT INTO `base_area` VALUES ('500111', '大足区', '5001');
INSERT INTO `base_area` VALUES ('500112', '渝北区', '5001');
INSERT INTO `base_area` VALUES ('500113', '巴南区', '5001');
INSERT INTO `base_area` VALUES ('500114', '黔江区', '5001');
INSERT INTO `base_area` VALUES ('500115', '长寿区', '5001');
INSERT INTO `base_area` VALUES ('500116', '江津区', '5001');
INSERT INTO `base_area` VALUES ('500117', '合川区', '5001');
INSERT INTO `base_area` VALUES ('500118', '永川区', '5001');
INSERT INTO `base_area` VALUES ('500119', '南川区', '5001');
INSERT INTO `base_area` VALUES ('500120', '璧山区', '5001');
INSERT INTO `base_area` VALUES ('500151', '铜梁区', '5001');
INSERT INTO `base_area` VALUES ('500152', '潼南区', '5001');
INSERT INTO `base_area` VALUES ('500153', '荣昌区', '5001');
INSERT INTO `base_area` VALUES ('500154', '开州区', '5001');
INSERT INTO `base_area` VALUES ('500155', '梁平区', '5001');
INSERT INTO `base_area` VALUES ('500156', '武隆区', '5001');
INSERT INTO `base_area` VALUES ('500229', '城口县', '5002');
INSERT INTO `base_area` VALUES ('500230', '丰都县', '5002');
INSERT INTO `base_area` VALUES ('500231', '垫江县', '5002');
INSERT INTO `base_area` VALUES ('500233', '忠县', '5002');
INSERT INTO `base_area` VALUES ('500235', '云阳县', '5002');
INSERT INTO `base_area` VALUES ('500236', '奉节县', '5002');
INSERT INTO `base_area` VALUES ('500237', '巫山县', '5002');
INSERT INTO `base_area` VALUES ('500238', '巫溪县', '5002');
INSERT INTO `base_area` VALUES ('500240', '石柱土家族自治县', '5002');
INSERT INTO `base_area` VALUES ('500241', '秀山土家族苗族自治县', '5002');
INSERT INTO `base_area` VALUES ('500242', '酉阳土家族苗族自治县', '5002');
INSERT INTO `base_area` VALUES ('500243', '彭水苗族土家族自治县', '5002');
INSERT INTO `base_area` VALUES ('510104', '锦江区', '5101');
INSERT INTO `base_area` VALUES ('510105', '青羊区', '5101');
INSERT INTO `base_area` VALUES ('510106', '金牛区', '5101');
INSERT INTO `base_area` VALUES ('510107', '武侯区', '5101');
INSERT INTO `base_area` VALUES ('510108', '成华区', '5101');
INSERT INTO `base_area` VALUES ('510112', '龙泉驿区', '5101');
INSERT INTO `base_area` VALUES ('510113', '青白江区', '5101');
INSERT INTO `base_area` VALUES ('510114', '新都区', '5101');
INSERT INTO `base_area` VALUES ('510115', '温江区', '5101');
INSERT INTO `base_area` VALUES ('510116', '双流区', '5101');
INSERT INTO `base_area` VALUES ('510117', '郫都区', '5101');
INSERT INTO `base_area` VALUES ('510118', '新津区', '5101');
INSERT INTO `base_area` VALUES ('510121', '金堂县', '5101');
INSERT INTO `base_area` VALUES ('510129', '大邑县', '5101');
INSERT INTO `base_area` VALUES ('510131', '蒲江县', '5101');
INSERT INTO `base_area` VALUES ('510181', '都江堰市', '5101');
INSERT INTO `base_area` VALUES ('510182', '彭州市', '5101');
INSERT INTO `base_area` VALUES ('510183', '邛崃市', '5101');
INSERT INTO `base_area` VALUES ('510184', '崇州市', '5101');
INSERT INTO `base_area` VALUES ('510185', '简阳市', '5101');
INSERT INTO `base_area` VALUES ('510302', '自流井区', '5103');
INSERT INTO `base_area` VALUES ('510303', '贡井区', '5103');
INSERT INTO `base_area` VALUES ('510304', '大安区', '5103');
INSERT INTO `base_area` VALUES ('510311', '沿滩区', '5103');
INSERT INTO `base_area` VALUES ('510321', '荣县', '5103');
INSERT INTO `base_area` VALUES ('510322', '富顺县', '5103');
INSERT INTO `base_area` VALUES ('510402', '东区', '5104');
INSERT INTO `base_area` VALUES ('510403', '西区', '5104');
INSERT INTO `base_area` VALUES ('510411', '仁和区', '5104');
INSERT INTO `base_area` VALUES ('510421', '米易县', '5104');
INSERT INTO `base_area` VALUES ('510422', '盐边县', '5104');
INSERT INTO `base_area` VALUES ('510502', '江阳区', '5105');
INSERT INTO `base_area` VALUES ('510503', '纳溪区', '5105');
INSERT INTO `base_area` VALUES ('510504', '龙马潭区', '5105');
INSERT INTO `base_area` VALUES ('510521', '泸县', '5105');
INSERT INTO `base_area` VALUES ('510522', '合江县', '5105');
INSERT INTO `base_area` VALUES ('510524', '叙永县', '5105');
INSERT INTO `base_area` VALUES ('510525', '古蔺县', '5105');
INSERT INTO `base_area` VALUES ('510603', '旌阳区', '5106');
INSERT INTO `base_area` VALUES ('510604', '罗江区', '5106');
INSERT INTO `base_area` VALUES ('510623', '中江县', '5106');
INSERT INTO `base_area` VALUES ('510681', '广汉市', '5106');
INSERT INTO `base_area` VALUES ('510682', '什邡市', '5106');
INSERT INTO `base_area` VALUES ('510683', '绵竹市', '5106');
INSERT INTO `base_area` VALUES ('510703', '涪城区', '5107');
INSERT INTO `base_area` VALUES ('510704', '游仙区', '5107');
INSERT INTO `base_area` VALUES ('510705', '安州区', '5107');
INSERT INTO `base_area` VALUES ('510722', '三台县', '5107');
INSERT INTO `base_area` VALUES ('510723', '盐亭县', '5107');
INSERT INTO `base_area` VALUES ('510725', '梓潼县', '5107');
INSERT INTO `base_area` VALUES ('510726', '北川羌族自治县', '5107');
INSERT INTO `base_area` VALUES ('510727', '平武县', '5107');
INSERT INTO `base_area` VALUES ('510781', '江油市', '5107');
INSERT INTO `base_area` VALUES ('510802', '利州区', '5108');
INSERT INTO `base_area` VALUES ('510811', '昭化区', '5108');
INSERT INTO `base_area` VALUES ('510812', '朝天区', '5108');
INSERT INTO `base_area` VALUES ('510821', '旺苍县', '5108');
INSERT INTO `base_area` VALUES ('510822', '青川县', '5108');
INSERT INTO `base_area` VALUES ('510823', '剑阁县', '5108');
INSERT INTO `base_area` VALUES ('510824', '苍溪县', '5108');
INSERT INTO `base_area` VALUES ('510903', '船山区', '5109');
INSERT INTO `base_area` VALUES ('510904', '安居区', '5109');
INSERT INTO `base_area` VALUES ('510921', '蓬溪县', '5109');
INSERT INTO `base_area` VALUES ('510923', '大英县', '5109');
INSERT INTO `base_area` VALUES ('510981', '射洪市', '5109');
INSERT INTO `base_area` VALUES ('511002', '市中区', '5110');
INSERT INTO `base_area` VALUES ('511011', '东兴区', '5110');
INSERT INTO `base_area` VALUES ('511024', '威远县', '5110');
INSERT INTO `base_area` VALUES ('511025', '资中县', '5110');
INSERT INTO `base_area` VALUES ('511083', '隆昌市', '5110');
INSERT INTO `base_area` VALUES ('511102', '市中区', '5111');
INSERT INTO `base_area` VALUES ('511111', '沙湾区', '5111');
INSERT INTO `base_area` VALUES ('511112', '五通桥区', '5111');
INSERT INTO `base_area` VALUES ('511113', '金口河区', '5111');
INSERT INTO `base_area` VALUES ('511123', '犍为县', '5111');
INSERT INTO `base_area` VALUES ('511124', '井研县', '5111');
INSERT INTO `base_area` VALUES ('511126', '夹江县', '5111');
INSERT INTO `base_area` VALUES ('511129', '沐川县', '5111');
INSERT INTO `base_area` VALUES ('511132', '峨边彝族自治县', '5111');
INSERT INTO `base_area` VALUES ('511133', '马边彝族自治县', '5111');
INSERT INTO `base_area` VALUES ('511181', '峨眉山市', '5111');
INSERT INTO `base_area` VALUES ('511302', '顺庆区', '5113');
INSERT INTO `base_area` VALUES ('511303', '高坪区', '5113');
INSERT INTO `base_area` VALUES ('511304', '嘉陵区', '5113');
INSERT INTO `base_area` VALUES ('511321', '南部县', '5113');
INSERT INTO `base_area` VALUES ('511322', '营山县', '5113');
INSERT INTO `base_area` VALUES ('511323', '蓬安县', '5113');
INSERT INTO `base_area` VALUES ('511324', '仪陇县', '5113');
INSERT INTO `base_area` VALUES ('511325', '西充县', '5113');
INSERT INTO `base_area` VALUES ('511381', '阆中市', '5113');
INSERT INTO `base_area` VALUES ('511402', '东坡区', '5114');
INSERT INTO `base_area` VALUES ('511403', '彭山区', '5114');
INSERT INTO `base_area` VALUES ('511421', '仁寿县', '5114');
INSERT INTO `base_area` VALUES ('511423', '洪雅县', '5114');
INSERT INTO `base_area` VALUES ('511424', '丹棱县', '5114');
INSERT INTO `base_area` VALUES ('511425', '青神县', '5114');
INSERT INTO `base_area` VALUES ('511502', '翠屏区', '5115');
INSERT INTO `base_area` VALUES ('511503', '南溪区', '5115');
INSERT INTO `base_area` VALUES ('511504', '叙州区', '5115');
INSERT INTO `base_area` VALUES ('511523', '江安县', '5115');
INSERT INTO `base_area` VALUES ('511524', '长宁县', '5115');
INSERT INTO `base_area` VALUES ('511525', '高县', '5115');
INSERT INTO `base_area` VALUES ('511526', '珙县', '5115');
INSERT INTO `base_area` VALUES ('511527', '筠连县', '5115');
INSERT INTO `base_area` VALUES ('511528', '兴文县', '5115');
INSERT INTO `base_area` VALUES ('511529', '屏山县', '5115');
INSERT INTO `base_area` VALUES ('511602', '广安区', '5116');
INSERT INTO `base_area` VALUES ('511603', '前锋区', '5116');
INSERT INTO `base_area` VALUES ('511621', '岳池县', '5116');
INSERT INTO `base_area` VALUES ('511622', '武胜县', '5116');
INSERT INTO `base_area` VALUES ('511623', '邻水县', '5116');
INSERT INTO `base_area` VALUES ('511681', '华蓥市', '5116');
INSERT INTO `base_area` VALUES ('511702', '通川区', '5117');
INSERT INTO `base_area` VALUES ('511703', '达川区', '5117');
INSERT INTO `base_area` VALUES ('511722', '宣汉县', '5117');
INSERT INTO `base_area` VALUES ('511723', '开江县', '5117');
INSERT INTO `base_area` VALUES ('511724', '大竹县', '5117');
INSERT INTO `base_area` VALUES ('511725', '渠县', '5117');
INSERT INTO `base_area` VALUES ('511781', '万源市', '5117');
INSERT INTO `base_area` VALUES ('511802', '雨城区', '5118');
INSERT INTO `base_area` VALUES ('511803', '名山区', '5118');
INSERT INTO `base_area` VALUES ('511822', '荥经县', '5118');
INSERT INTO `base_area` VALUES ('511823', '汉源县', '5118');
INSERT INTO `base_area` VALUES ('511824', '石棉县', '5118');
INSERT INTO `base_area` VALUES ('511825', '天全县', '5118');
INSERT INTO `base_area` VALUES ('511826', '芦山县', '5118');
INSERT INTO `base_area` VALUES ('511827', '宝兴县', '5118');
INSERT INTO `base_area` VALUES ('511902', '巴州区', '5119');
INSERT INTO `base_area` VALUES ('511903', '恩阳区', '5119');
INSERT INTO `base_area` VALUES ('511921', '通江县', '5119');
INSERT INTO `base_area` VALUES ('511922', '南江县', '5119');
INSERT INTO `base_area` VALUES ('511923', '平昌县', '5119');
INSERT INTO `base_area` VALUES ('512002', '雁江区', '5120');
INSERT INTO `base_area` VALUES ('512021', '安岳县', '5120');
INSERT INTO `base_area` VALUES ('512022', '乐至县', '5120');
INSERT INTO `base_area` VALUES ('513201', '马尔康市', '5132');
INSERT INTO `base_area` VALUES ('513221', '汶川县', '5132');
INSERT INTO `base_area` VALUES ('513222', '理县', '5132');
INSERT INTO `base_area` VALUES ('513223', '茂县', '5132');
INSERT INTO `base_area` VALUES ('513224', '松潘县', '5132');
INSERT INTO `base_area` VALUES ('513225', '九寨沟县', '5132');
INSERT INTO `base_area` VALUES ('513226', '金川县', '5132');
INSERT INTO `base_area` VALUES ('513227', '小金县', '5132');
INSERT INTO `base_area` VALUES ('513228', '黑水县', '5132');
INSERT INTO `base_area` VALUES ('513230', '壤塘县', '5132');
INSERT INTO `base_area` VALUES ('513231', '阿坝县', '5132');
INSERT INTO `base_area` VALUES ('513232', '若尔盖县', '5132');
INSERT INTO `base_area` VALUES ('513233', '红原县', '5132');
INSERT INTO `base_area` VALUES ('513301', '康定市', '5133');
INSERT INTO `base_area` VALUES ('513322', '泸定县', '5133');
INSERT INTO `base_area` VALUES ('513323', '丹巴县', '5133');
INSERT INTO `base_area` VALUES ('513324', '九龙县', '5133');
INSERT INTO `base_area` VALUES ('513325', '雅江县', '5133');
INSERT INTO `base_area` VALUES ('513326', '道孚县', '5133');
INSERT INTO `base_area` VALUES ('513327', '炉霍县', '5133');
INSERT INTO `base_area` VALUES ('513328', '甘孜县', '5133');
INSERT INTO `base_area` VALUES ('513329', '新龙县', '5133');
INSERT INTO `base_area` VALUES ('513330', '德格县', '5133');
INSERT INTO `base_area` VALUES ('513331', '白玉县', '5133');
INSERT INTO `base_area` VALUES ('513332', '石渠县', '5133');
INSERT INTO `base_area` VALUES ('513333', '色达县', '5133');
INSERT INTO `base_area` VALUES ('513334', '理塘县', '5133');
INSERT INTO `base_area` VALUES ('513335', '巴塘县', '5133');
INSERT INTO `base_area` VALUES ('513336', '乡城县', '5133');
INSERT INTO `base_area` VALUES ('513337', '稻城县', '5133');
INSERT INTO `base_area` VALUES ('513338', '得荣县', '5133');
INSERT INTO `base_area` VALUES ('513401', '西昌市', '5134');
INSERT INTO `base_area` VALUES ('513402', '会理市', '5134');
INSERT INTO `base_area` VALUES ('513422', '木里藏族自治县', '5134');
INSERT INTO `base_area` VALUES ('513423', '盐源县', '5134');
INSERT INTO `base_area` VALUES ('513424', '德昌县', '5134');
INSERT INTO `base_area` VALUES ('513426', '会东县', '5134');
INSERT INTO `base_area` VALUES ('513427', '宁南县', '5134');
INSERT INTO `base_area` VALUES ('513428', '普格县', '5134');
INSERT INTO `base_area` VALUES ('513429', '布拖县', '5134');
INSERT INTO `base_area` VALUES ('513430', '金阳县', '5134');
INSERT INTO `base_area` VALUES ('513431', '昭觉县', '5134');
INSERT INTO `base_area` VALUES ('513432', '喜德县', '5134');
INSERT INTO `base_area` VALUES ('513433', '冕宁县', '5134');
INSERT INTO `base_area` VALUES ('513434', '越西县', '5134');
INSERT INTO `base_area` VALUES ('513435', '甘洛县', '5134');
INSERT INTO `base_area` VALUES ('513436', '美姑县', '5134');
INSERT INTO `base_area` VALUES ('513437', '雷波县', '5134');
INSERT INTO `base_area` VALUES ('520102', '南明区', '5201');
INSERT INTO `base_area` VALUES ('520103', '云岩区', '5201');
INSERT INTO `base_area` VALUES ('520111', '花溪区', '5201');
INSERT INTO `base_area` VALUES ('520112', '乌当区', '5201');
INSERT INTO `base_area` VALUES ('520113', '白云区', '5201');
INSERT INTO `base_area` VALUES ('520115', '观山湖区', '5201');
INSERT INTO `base_area` VALUES ('520121', '开阳县', '5201');
INSERT INTO `base_area` VALUES ('520122', '息烽县', '5201');
INSERT INTO `base_area` VALUES ('520123', '修文县', '5201');
INSERT INTO `base_area` VALUES ('520181', '清镇市', '5201');
INSERT INTO `base_area` VALUES ('520201', '钟山区', '5202');
INSERT INTO `base_area` VALUES ('520203', '六枝特区', '5202');
INSERT INTO `base_area` VALUES ('520204', '水城区', '5202');
INSERT INTO `base_area` VALUES ('520281', '盘州市', '5202');
INSERT INTO `base_area` VALUES ('520302', '红花岗区', '5203');
INSERT INTO `base_area` VALUES ('520303', '汇川区', '5203');
INSERT INTO `base_area` VALUES ('520304', '播州区', '5203');
INSERT INTO `base_area` VALUES ('520322', '桐梓县', '5203');
INSERT INTO `base_area` VALUES ('520323', '绥阳县', '5203');
INSERT INTO `base_area` VALUES ('520324', '正安县', '5203');
INSERT INTO `base_area` VALUES ('520325', '道真仡佬族苗族自治县', '5203');
INSERT INTO `base_area` VALUES ('520326', '务川仡佬族苗族自治县', '5203');
INSERT INTO `base_area` VALUES ('520327', '凤冈县', '5203');
INSERT INTO `base_area` VALUES ('520328', '湄潭县', '5203');
INSERT INTO `base_area` VALUES ('520329', '余庆县', '5203');
INSERT INTO `base_area` VALUES ('520330', '习水县', '5203');
INSERT INTO `base_area` VALUES ('520381', '赤水市', '5203');
INSERT INTO `base_area` VALUES ('520382', '仁怀市', '5203');
INSERT INTO `base_area` VALUES ('520402', '西秀区', '5204');
INSERT INTO `base_area` VALUES ('520403', '平坝区', '5204');
INSERT INTO `base_area` VALUES ('520422', '普定县', '5204');
INSERT INTO `base_area` VALUES ('520423', '镇宁布依族苗族自治县', '5204');
INSERT INTO `base_area` VALUES ('520424', '关岭布依族苗族自治县', '5204');
INSERT INTO `base_area` VALUES ('520425', '紫云苗族布依族自治县', '5204');
INSERT INTO `base_area` VALUES ('520502', '七星关区', '5205');
INSERT INTO `base_area` VALUES ('520521', '大方县', '5205');
INSERT INTO `base_area` VALUES ('520523', '金沙县', '5205');
INSERT INTO `base_area` VALUES ('520524', '织金县', '5205');
INSERT INTO `base_area` VALUES ('520525', '纳雍县', '5205');
INSERT INTO `base_area` VALUES ('520526', '威宁彝族回族苗族自治县', '5205');
INSERT INTO `base_area` VALUES ('520527', '赫章县', '5205');
INSERT INTO `base_area` VALUES ('520581', '黔西市', '5205');
INSERT INTO `base_area` VALUES ('520602', '碧江区', '5206');
INSERT INTO `base_area` VALUES ('520603', '万山区', '5206');
INSERT INTO `base_area` VALUES ('520621', '江口县', '5206');
INSERT INTO `base_area` VALUES ('520622', '玉屏侗族自治县', '5206');
INSERT INTO `base_area` VALUES ('520623', '石阡县', '5206');
INSERT INTO `base_area` VALUES ('520624', '思南县', '5206');
INSERT INTO `base_area` VALUES ('520625', '印江土家族苗族自治县', '5206');
INSERT INTO `base_area` VALUES ('520626', '德江县', '5206');
INSERT INTO `base_area` VALUES ('520627', '沿河土家族自治县', '5206');
INSERT INTO `base_area` VALUES ('520628', '松桃苗族自治县', '5206');
INSERT INTO `base_area` VALUES ('522301', '兴义市', '5223');
INSERT INTO `base_area` VALUES ('522302', '兴仁市', '5223');
INSERT INTO `base_area` VALUES ('522323', '普安县', '5223');
INSERT INTO `base_area` VALUES ('522324', '晴隆县', '5223');
INSERT INTO `base_area` VALUES ('522325', '贞丰县', '5223');
INSERT INTO `base_area` VALUES ('522326', '望谟县', '5223');
INSERT INTO `base_area` VALUES ('522327', '册亨县', '5223');
INSERT INTO `base_area` VALUES ('522328', '安龙县', '5223');
INSERT INTO `base_area` VALUES ('522601', '凯里市', '5226');
INSERT INTO `base_area` VALUES ('522622', '黄平县', '5226');
INSERT INTO `base_area` VALUES ('522623', '施秉县', '5226');
INSERT INTO `base_area` VALUES ('522624', '三穗县', '5226');
INSERT INTO `base_area` VALUES ('522625', '镇远县', '5226');
INSERT INTO `base_area` VALUES ('522626', '岑巩县', '5226');
INSERT INTO `base_area` VALUES ('522627', '天柱县', '5226');
INSERT INTO `base_area` VALUES ('522628', '锦屏县', '5226');
INSERT INTO `base_area` VALUES ('522629', '剑河县', '5226');
INSERT INTO `base_area` VALUES ('522630', '台江县', '5226');
INSERT INTO `base_area` VALUES ('522631', '黎平县', '5226');
INSERT INTO `base_area` VALUES ('522632', '榕江县', '5226');
INSERT INTO `base_area` VALUES ('522633', '从江县', '5226');
INSERT INTO `base_area` VALUES ('522634', '雷山县', '5226');
INSERT INTO `base_area` VALUES ('522635', '麻江县', '5226');
INSERT INTO `base_area` VALUES ('522636', '丹寨县', '5226');
INSERT INTO `base_area` VALUES ('522701', '都匀市', '5227');
INSERT INTO `base_area` VALUES ('522702', '福泉市', '5227');
INSERT INTO `base_area` VALUES ('522722', '荔波县', '5227');
INSERT INTO `base_area` VALUES ('522723', '贵定县', '5227');
INSERT INTO `base_area` VALUES ('522725', '瓮安县', '5227');
INSERT INTO `base_area` VALUES ('522726', '独山县', '5227');
INSERT INTO `base_area` VALUES ('522727', '平塘县', '5227');
INSERT INTO `base_area` VALUES ('522728', '罗甸县', '5227');
INSERT INTO `base_area` VALUES ('522729', '长顺县', '5227');
INSERT INTO `base_area` VALUES ('522730', '龙里县', '5227');
INSERT INTO `base_area` VALUES ('522731', '惠水县', '5227');
INSERT INTO `base_area` VALUES ('522732', '三都水族自治县', '5227');
INSERT INTO `base_area` VALUES ('530102', '五华区', '5301');
INSERT INTO `base_area` VALUES ('530103', '盘龙区', '5301');
INSERT INTO `base_area` VALUES ('530111', '官渡区', '5301');
INSERT INTO `base_area` VALUES ('530112', '西山区', '5301');
INSERT INTO `base_area` VALUES ('530113', '东川区', '5301');
INSERT INTO `base_area` VALUES ('530114', '呈贡区', '5301');
INSERT INTO `base_area` VALUES ('530115', '晋宁区', '5301');
INSERT INTO `base_area` VALUES ('530124', '富民县', '5301');
INSERT INTO `base_area` VALUES ('530125', '宜良县', '5301');
INSERT INTO `base_area` VALUES ('530126', '石林彝族自治县', '5301');
INSERT INTO `base_area` VALUES ('530127', '嵩明县', '5301');
INSERT INTO `base_area` VALUES ('530128', '禄劝彝族苗族自治县', '5301');
INSERT INTO `base_area` VALUES ('530129', '寻甸回族彝族自治县', '5301');
INSERT INTO `base_area` VALUES ('530181', '安宁市', '5301');
INSERT INTO `base_area` VALUES ('530302', '麒麟区', '5303');
INSERT INTO `base_area` VALUES ('530303', '沾益区', '5303');
INSERT INTO `base_area` VALUES ('530304', '马龙区', '5303');
INSERT INTO `base_area` VALUES ('530322', '陆良县', '5303');
INSERT INTO `base_area` VALUES ('530323', '师宗县', '5303');
INSERT INTO `base_area` VALUES ('530324', '罗平县', '5303');
INSERT INTO `base_area` VALUES ('530325', '富源县', '5303');
INSERT INTO `base_area` VALUES ('530326', '会泽县', '5303');
INSERT INTO `base_area` VALUES ('530381', '宣威市', '5303');
INSERT INTO `base_area` VALUES ('530402', '红塔区', '5304');
INSERT INTO `base_area` VALUES ('530403', '江川区', '5304');
INSERT INTO `base_area` VALUES ('530423', '通海县', '5304');
INSERT INTO `base_area` VALUES ('530424', '华宁县', '5304');
INSERT INTO `base_area` VALUES ('530425', '易门县', '5304');
INSERT INTO `base_area` VALUES ('530426', '峨山彝族自治县', '5304');
INSERT INTO `base_area` VALUES ('530427', '新平彝族傣族自治县', '5304');
INSERT INTO `base_area` VALUES ('530428', '元江哈尼族彝族傣族自治县', '5304');
INSERT INTO `base_area` VALUES ('530481', '澄江市', '5304');
INSERT INTO `base_area` VALUES ('530502', '隆阳区', '5305');
INSERT INTO `base_area` VALUES ('530521', '施甸县', '5305');
INSERT INTO `base_area` VALUES ('530523', '龙陵县', '5305');
INSERT INTO `base_area` VALUES ('530524', '昌宁县', '5305');
INSERT INTO `base_area` VALUES ('530581', '腾冲市', '5305');
INSERT INTO `base_area` VALUES ('530602', '昭阳区', '5306');
INSERT INTO `base_area` VALUES ('530621', '鲁甸县', '5306');
INSERT INTO `base_area` VALUES ('530622', '巧家县', '5306');
INSERT INTO `base_area` VALUES ('530623', '盐津县', '5306');
INSERT INTO `base_area` VALUES ('530624', '大关县', '5306');
INSERT INTO `base_area` VALUES ('530625', '永善县', '5306');
INSERT INTO `base_area` VALUES ('530626', '绥江县', '5306');
INSERT INTO `base_area` VALUES ('530627', '镇雄县', '5306');
INSERT INTO `base_area` VALUES ('530628', '彝良县', '5306');
INSERT INTO `base_area` VALUES ('530629', '威信县', '5306');
INSERT INTO `base_area` VALUES ('530681', '水富市', '5306');
INSERT INTO `base_area` VALUES ('530702', '古城区', '5307');
INSERT INTO `base_area` VALUES ('530721', '玉龙纳西族自治县', '5307');
INSERT INTO `base_area` VALUES ('530722', '永胜县', '5307');
INSERT INTO `base_area` VALUES ('530723', '华坪县', '5307');
INSERT INTO `base_area` VALUES ('530724', '宁蒗彝族自治县', '5307');
INSERT INTO `base_area` VALUES ('530802', '思茅区', '5308');
INSERT INTO `base_area` VALUES ('530821', '宁洱哈尼族彝族自治县', '5308');
INSERT INTO `base_area` VALUES ('530822', '墨江哈尼族自治县', '5308');
INSERT INTO `base_area` VALUES ('530823', '景东彝族自治县', '5308');
INSERT INTO `base_area` VALUES ('530824', '景谷傣族彝族自治县', '5308');
INSERT INTO `base_area` VALUES ('530825', '镇沅彝族哈尼族拉祜族自治县', '5308');
INSERT INTO `base_area` VALUES ('530826', '江城哈尼族彝族自治县', '5308');
INSERT INTO `base_area` VALUES ('530827', '孟连傣族拉祜族佤族自治县', '5308');
INSERT INTO `base_area` VALUES ('530828', '澜沧拉祜族自治县', '5308');
INSERT INTO `base_area` VALUES ('530829', '西盟佤族自治县', '5308');
INSERT INTO `base_area` VALUES ('530902', '临翔区', '5309');
INSERT INTO `base_area` VALUES ('530921', '凤庆县', '5309');
INSERT INTO `base_area` VALUES ('530922', '云县', '5309');
INSERT INTO `base_area` VALUES ('530923', '永德县', '5309');
INSERT INTO `base_area` VALUES ('530924', '镇康县', '5309');
INSERT INTO `base_area` VALUES ('530925', '双江拉祜族佤族布朗族傣族自治县', '5309');
INSERT INTO `base_area` VALUES ('530926', '耿马傣族佤族自治县', '5309');
INSERT INTO `base_area` VALUES ('530927', '沧源佤族自治县', '5309');
INSERT INTO `base_area` VALUES ('532301', '楚雄市', '5323');
INSERT INTO `base_area` VALUES ('532302', '禄丰市', '5323');
INSERT INTO `base_area` VALUES ('532322', '双柏县', '5323');
INSERT INTO `base_area` VALUES ('532323', '牟定县', '5323');
INSERT INTO `base_area` VALUES ('532324', '南华县', '5323');
INSERT INTO `base_area` VALUES ('532325', '姚安县', '5323');
INSERT INTO `base_area` VALUES ('532326', '大姚县', '5323');
INSERT INTO `base_area` VALUES ('532327', '永仁县', '5323');
INSERT INTO `base_area` VALUES ('532328', '元谋县', '5323');
INSERT INTO `base_area` VALUES ('532329', '武定县', '5323');
INSERT INTO `base_area` VALUES ('532501', '个旧市', '5325');
INSERT INTO `base_area` VALUES ('532502', '开远市', '5325');
INSERT INTO `base_area` VALUES ('532503', '蒙自市', '5325');
INSERT INTO `base_area` VALUES ('532504', '弥勒市', '5325');
INSERT INTO `base_area` VALUES ('532523', '屏边苗族自治县', '5325');
INSERT INTO `base_area` VALUES ('532524', '建水县', '5325');
INSERT INTO `base_area` VALUES ('532525', '石屏县', '5325');
INSERT INTO `base_area` VALUES ('532527', '泸西县', '5325');
INSERT INTO `base_area` VALUES ('532528', '元阳县', '5325');
INSERT INTO `base_area` VALUES ('532529', '红河县', '5325');
INSERT INTO `base_area` VALUES ('532530', '金平苗族瑶族傣族自治县', '5325');
INSERT INTO `base_area` VALUES ('532531', '绿春县', '5325');
INSERT INTO `base_area` VALUES ('532532', '河口瑶族自治县', '5325');
INSERT INTO `base_area` VALUES ('532601', '文山市', '5326');
INSERT INTO `base_area` VALUES ('532622', '砚山县', '5326');
INSERT INTO `base_area` VALUES ('532623', '西畴县', '5326');
INSERT INTO `base_area` VALUES ('532624', '麻栗坡县', '5326');
INSERT INTO `base_area` VALUES ('532625', '马关县', '5326');
INSERT INTO `base_area` VALUES ('532626', '丘北县', '5326');
INSERT INTO `base_area` VALUES ('532627', '广南县', '5326');
INSERT INTO `base_area` VALUES ('532628', '富宁县', '5326');
INSERT INTO `base_area` VALUES ('532801', '景洪市', '5328');
INSERT INTO `base_area` VALUES ('532822', '勐海县', '5328');
INSERT INTO `base_area` VALUES ('532823', '勐腊县', '5328');
INSERT INTO `base_area` VALUES ('532901', '大理市', '5329');
INSERT INTO `base_area` VALUES ('532922', '漾濞彝族自治县', '5329');
INSERT INTO `base_area` VALUES ('532923', '祥云县', '5329');
INSERT INTO `base_area` VALUES ('532924', '宾川县', '5329');
INSERT INTO `base_area` VALUES ('532925', '弥渡县', '5329');
INSERT INTO `base_area` VALUES ('532926', '南涧彝族自治县', '5329');
INSERT INTO `base_area` VALUES ('532927', '巍山彝族回族自治县', '5329');
INSERT INTO `base_area` VALUES ('532928', '永平县', '5329');
INSERT INTO `base_area` VALUES ('532929', '云龙县', '5329');
INSERT INTO `base_area` VALUES ('532930', '洱源县', '5329');
INSERT INTO `base_area` VALUES ('532931', '剑川县', '5329');
INSERT INTO `base_area` VALUES ('532932', '鹤庆县', '5329');
INSERT INTO `base_area` VALUES ('533102', '瑞丽市', '5331');
INSERT INTO `base_area` VALUES ('533103', '芒市', '5331');
INSERT INTO `base_area` VALUES ('533122', '梁河县', '5331');
INSERT INTO `base_area` VALUES ('533123', '盈江县', '5331');
INSERT INTO `base_area` VALUES ('533124', '陇川县', '5331');
INSERT INTO `base_area` VALUES ('533301', '泸水市', '5333');
INSERT INTO `base_area` VALUES ('533323', '福贡县', '5333');
INSERT INTO `base_area` VALUES ('533324', '贡山独龙族怒族自治县', '5333');
INSERT INTO `base_area` VALUES ('533325', '兰坪白族普米族自治县', '5333');
INSERT INTO `base_area` VALUES ('533401', '香格里拉市', '5334');
INSERT INTO `base_area` VALUES ('533422', '德钦县', '5334');
INSERT INTO `base_area` VALUES ('533423', '维西傈僳族自治县', '5334');
INSERT INTO `base_area` VALUES ('540102', '城关区', '5401');
INSERT INTO `base_area` VALUES ('540103', '堆龙德庆区', '5401');
INSERT INTO `base_area` VALUES ('540104', '达孜区', '5401');
INSERT INTO `base_area` VALUES ('540121', '林周县', '5401');
INSERT INTO `base_area` VALUES ('540122', '当雄县', '5401');
INSERT INTO `base_area` VALUES ('540123', '尼木县', '5401');
INSERT INTO `base_area` VALUES ('540124', '曲水县', '5401');
INSERT INTO `base_area` VALUES ('540127', '墨竹工卡县', '5401');
INSERT INTO `base_area` VALUES ('540171', '格尔木藏青工业园区', '5401');
INSERT INTO `base_area` VALUES ('540172', '拉萨经济技术开发区', '5401');
INSERT INTO `base_area` VALUES ('540173', '西藏文化旅游创意园区', '5401');
INSERT INTO `base_area` VALUES ('540174', '达孜工业园区', '5401');
INSERT INTO `base_area` VALUES ('540202', '桑珠孜区', '5402');
INSERT INTO `base_area` VALUES ('540221', '南木林县', '5402');
INSERT INTO `base_area` VALUES ('540222', '江孜县', '5402');
INSERT INTO `base_area` VALUES ('540223', '定日县', '5402');
INSERT INTO `base_area` VALUES ('540224', '萨迦县', '5402');
INSERT INTO `base_area` VALUES ('540225', '拉孜县', '5402');
INSERT INTO `base_area` VALUES ('540226', '昂仁县', '5402');
INSERT INTO `base_area` VALUES ('540227', '谢通门县', '5402');
INSERT INTO `base_area` VALUES ('540228', '白朗县', '5402');
INSERT INTO `base_area` VALUES ('540229', '仁布县', '5402');
INSERT INTO `base_area` VALUES ('540230', '康马县', '5402');
INSERT INTO `base_area` VALUES ('540231', '定结县', '5402');
INSERT INTO `base_area` VALUES ('540232', '仲巴县', '5402');
INSERT INTO `base_area` VALUES ('540233', '亚东县', '5402');
INSERT INTO `base_area` VALUES ('540234', '吉隆县', '5402');
INSERT INTO `base_area` VALUES ('540235', '聂拉木县', '5402');
INSERT INTO `base_area` VALUES ('540236', '萨嘎县', '5402');
INSERT INTO `base_area` VALUES ('540237', '岗巴县', '5402');
INSERT INTO `base_area` VALUES ('540302', '卡若区', '5403');
INSERT INTO `base_area` VALUES ('540321', '江达县', '5403');
INSERT INTO `base_area` VALUES ('540322', '贡觉县', '5403');
INSERT INTO `base_area` VALUES ('540323', '类乌齐县', '5403');
INSERT INTO `base_area` VALUES ('540324', '丁青县', '5403');
INSERT INTO `base_area` VALUES ('540325', '察雅县', '5403');
INSERT INTO `base_area` VALUES ('540326', '八宿县', '5403');
INSERT INTO `base_area` VALUES ('540327', '左贡县', '5403');
INSERT INTO `base_area` VALUES ('540328', '芒康县', '5403');
INSERT INTO `base_area` VALUES ('540329', '洛隆县', '5403');
INSERT INTO `base_area` VALUES ('540330', '边坝县', '5403');
INSERT INTO `base_area` VALUES ('540402', '巴宜区', '5404');
INSERT INTO `base_area` VALUES ('540421', '工布江达县', '5404');
INSERT INTO `base_area` VALUES ('540422', '米林县', '5404');
INSERT INTO `base_area` VALUES ('540423', '墨脱县', '5404');
INSERT INTO `base_area` VALUES ('540424', '波密县', '5404');
INSERT INTO `base_area` VALUES ('540425', '察隅县', '5404');
INSERT INTO `base_area` VALUES ('540426', '朗县', '5404');
INSERT INTO `base_area` VALUES ('540502', '乃东区', '5405');
INSERT INTO `base_area` VALUES ('540521', '扎囊县', '5405');
INSERT INTO `base_area` VALUES ('540522', '贡嘎县', '5405');
INSERT INTO `base_area` VALUES ('540523', '桑日县', '5405');
INSERT INTO `base_area` VALUES ('540524', '琼结县', '5405');
INSERT INTO `base_area` VALUES ('540525', '曲松县', '5405');
INSERT INTO `base_area` VALUES ('540526', '措美县', '5405');
INSERT INTO `base_area` VALUES ('540527', '洛扎县', '5405');
INSERT INTO `base_area` VALUES ('540528', '加查县', '5405');
INSERT INTO `base_area` VALUES ('540529', '隆子县', '5405');
INSERT INTO `base_area` VALUES ('540530', '错那县', '5405');
INSERT INTO `base_area` VALUES ('540531', '浪卡子县', '5405');
INSERT INTO `base_area` VALUES ('540602', '色尼区', '5406');
INSERT INTO `base_area` VALUES ('540621', '嘉黎县', '5406');
INSERT INTO `base_area` VALUES ('540622', '比如县', '5406');
INSERT INTO `base_area` VALUES ('540623', '聂荣县', '5406');
INSERT INTO `base_area` VALUES ('540624', '安多县', '5406');
INSERT INTO `base_area` VALUES ('540625', '申扎县', '5406');
INSERT INTO `base_area` VALUES ('540626', '索县', '5406');
INSERT INTO `base_area` VALUES ('540627', '班戈县', '5406');
INSERT INTO `base_area` VALUES ('540628', '巴青县', '5406');
INSERT INTO `base_area` VALUES ('540629', '尼玛县', '5406');
INSERT INTO `base_area` VALUES ('540630', '双湖县', '5406');
INSERT INTO `base_area` VALUES ('542521', '普兰县', '5425');
INSERT INTO `base_area` VALUES ('542522', '札达县', '5425');
INSERT INTO `base_area` VALUES ('542523', '噶尔县', '5425');
INSERT INTO `base_area` VALUES ('542524', '日土县', '5425');
INSERT INTO `base_area` VALUES ('542525', '革吉县', '5425');
INSERT INTO `base_area` VALUES ('542526', '改则县', '5425');
INSERT INTO `base_area` VALUES ('542527', '措勤县', '5425');
INSERT INTO `base_area` VALUES ('610102', '新城区', '6101');
INSERT INTO `base_area` VALUES ('610103', '碑林区', '6101');
INSERT INTO `base_area` VALUES ('610104', '莲湖区', '6101');
INSERT INTO `base_area` VALUES ('610111', '灞桥区', '6101');
INSERT INTO `base_area` VALUES ('610112', '未央区', '6101');
INSERT INTO `base_area` VALUES ('610113', '雁塔区', '6101');
INSERT INTO `base_area` VALUES ('610114', '阎良区', '6101');
INSERT INTO `base_area` VALUES ('610115', '临潼区', '6101');
INSERT INTO `base_area` VALUES ('610116', '长安区', '6101');
INSERT INTO `base_area` VALUES ('610117', '高陵区', '6101');
INSERT INTO `base_area` VALUES ('610118', '鄠邑区', '6101');
INSERT INTO `base_area` VALUES ('610122', '蓝田县', '6101');
INSERT INTO `base_area` VALUES ('610124', '周至县', '6101');
INSERT INTO `base_area` VALUES ('610202', '王益区', '6102');
INSERT INTO `base_area` VALUES ('610203', '印台区', '6102');
INSERT INTO `base_area` VALUES ('610204', '耀州区', '6102');
INSERT INTO `base_area` VALUES ('610222', '宜君县', '6102');
INSERT INTO `base_area` VALUES ('610302', '渭滨区', '6103');
INSERT INTO `base_area` VALUES ('610303', '金台区', '6103');
INSERT INTO `base_area` VALUES ('610304', '陈仓区', '6103');
INSERT INTO `base_area` VALUES ('610305', '凤翔区', '6103');
INSERT INTO `base_area` VALUES ('610323', '岐山县', '6103');
INSERT INTO `base_area` VALUES ('610324', '扶风县', '6103');
INSERT INTO `base_area` VALUES ('610326', '眉县', '6103');
INSERT INTO `base_area` VALUES ('610327', '陇县', '6103');
INSERT INTO `base_area` VALUES ('610328', '千阳县', '6103');
INSERT INTO `base_area` VALUES ('610329', '麟游县', '6103');
INSERT INTO `base_area` VALUES ('610330', '凤县', '6103');
INSERT INTO `base_area` VALUES ('610331', '太白县', '6103');
INSERT INTO `base_area` VALUES ('610402', '秦都区', '6104');
INSERT INTO `base_area` VALUES ('610403', '杨陵区', '6104');
INSERT INTO `base_area` VALUES ('610404', '渭城区', '6104');
INSERT INTO `base_area` VALUES ('610422', '三原县', '6104');
INSERT INTO `base_area` VALUES ('610423', '泾阳县', '6104');
INSERT INTO `base_area` VALUES ('610424', '乾县', '6104');
INSERT INTO `base_area` VALUES ('610425', '礼泉县', '6104');
INSERT INTO `base_area` VALUES ('610426', '永寿县', '6104');
INSERT INTO `base_area` VALUES ('610428', '长武县', '6104');
INSERT INTO `base_area` VALUES ('610429', '旬邑县', '6104');
INSERT INTO `base_area` VALUES ('610430', '淳化县', '6104');
INSERT INTO `base_area` VALUES ('610431', '武功县', '6104');
INSERT INTO `base_area` VALUES ('610481', '兴平市', '6104');
INSERT INTO `base_area` VALUES ('610482', '彬州市', '6104');
INSERT INTO `base_area` VALUES ('610502', '临渭区', '6105');
INSERT INTO `base_area` VALUES ('610503', '华州区', '6105');
INSERT INTO `base_area` VALUES ('610522', '潼关县', '6105');
INSERT INTO `base_area` VALUES ('610523', '大荔县', '6105');
INSERT INTO `base_area` VALUES ('610524', '合阳县', '6105');
INSERT INTO `base_area` VALUES ('610525', '澄城县', '6105');
INSERT INTO `base_area` VALUES ('610526', '蒲城县', '6105');
INSERT INTO `base_area` VALUES ('610527', '白水县', '6105');
INSERT INTO `base_area` VALUES ('610528', '富平县', '6105');
INSERT INTO `base_area` VALUES ('610581', '韩城市', '6105');
INSERT INTO `base_area` VALUES ('610582', '华阴市', '6105');
INSERT INTO `base_area` VALUES ('610602', '宝塔区', '6106');
INSERT INTO `base_area` VALUES ('610603', '安塞区', '6106');
INSERT INTO `base_area` VALUES ('610621', '延长县', '6106');
INSERT INTO `base_area` VALUES ('610622', '延川县', '6106');
INSERT INTO `base_area` VALUES ('610625', '志丹县', '6106');
INSERT INTO `base_area` VALUES ('610626', '吴起县', '6106');
INSERT INTO `base_area` VALUES ('610627', '甘泉县', '6106');
INSERT INTO `base_area` VALUES ('610628', '富县', '6106');
INSERT INTO `base_area` VALUES ('610629', '洛川县', '6106');
INSERT INTO `base_area` VALUES ('610630', '宜川县', '6106');
INSERT INTO `base_area` VALUES ('610631', '黄龙县', '6106');
INSERT INTO `base_area` VALUES ('610632', '黄陵县', '6106');
INSERT INTO `base_area` VALUES ('610681', '子长市', '6106');
INSERT INTO `base_area` VALUES ('610702', '汉台区', '6107');
INSERT INTO `base_area` VALUES ('610703', '南郑区', '6107');
INSERT INTO `base_area` VALUES ('610722', '城固县', '6107');
INSERT INTO `base_area` VALUES ('610723', '洋县', '6107');
INSERT INTO `base_area` VALUES ('610724', '西乡县', '6107');
INSERT INTO `base_area` VALUES ('610725', '勉县', '6107');
INSERT INTO `base_area` VALUES ('610726', '宁强县', '6107');
INSERT INTO `base_area` VALUES ('610727', '略阳县', '6107');
INSERT INTO `base_area` VALUES ('610728', '镇巴县', '6107');
INSERT INTO `base_area` VALUES ('610729', '留坝县', '6107');
INSERT INTO `base_area` VALUES ('610730', '佛坪县', '6107');
INSERT INTO `base_area` VALUES ('610802', '榆阳区', '6108');
INSERT INTO `base_area` VALUES ('610803', '横山区', '6108');
INSERT INTO `base_area` VALUES ('610822', '府谷县', '6108');
INSERT INTO `base_area` VALUES ('610824', '靖边县', '6108');
INSERT INTO `base_area` VALUES ('610825', '定边县', '6108');
INSERT INTO `base_area` VALUES ('610826', '绥德县', '6108');
INSERT INTO `base_area` VALUES ('610827', '米脂县', '6108');
INSERT INTO `base_area` VALUES ('610828', '佳县', '6108');
INSERT INTO `base_area` VALUES ('610829', '吴堡县', '6108');
INSERT INTO `base_area` VALUES ('610830', '清涧县', '6108');
INSERT INTO `base_area` VALUES ('610831', '子洲县', '6108');
INSERT INTO `base_area` VALUES ('610881', '神木市', '6108');
INSERT INTO `base_area` VALUES ('610902', '汉滨区', '6109');
INSERT INTO `base_area` VALUES ('610921', '汉阴县', '6109');
INSERT INTO `base_area` VALUES ('610922', '石泉县', '6109');
INSERT INTO `base_area` VALUES ('610923', '宁陕县', '6109');
INSERT INTO `base_area` VALUES ('610924', '紫阳县', '6109');
INSERT INTO `base_area` VALUES ('610925', '岚皋县', '6109');
INSERT INTO `base_area` VALUES ('610926', '平利县', '6109');
INSERT INTO `base_area` VALUES ('610927', '镇坪县', '6109');
INSERT INTO `base_area` VALUES ('610929', '白河县', '6109');
INSERT INTO `base_area` VALUES ('610981', '旬阳市', '6109');
INSERT INTO `base_area` VALUES ('611002', '商州区', '6110');
INSERT INTO `base_area` VALUES ('611021', '洛南县', '6110');
INSERT INTO `base_area` VALUES ('611022', '丹凤县', '6110');
INSERT INTO `base_area` VALUES ('611023', '商南县', '6110');
INSERT INTO `base_area` VALUES ('611024', '山阳县', '6110');
INSERT INTO `base_area` VALUES ('611025', '镇安县', '6110');
INSERT INTO `base_area` VALUES ('611026', '柞水县', '6110');
INSERT INTO `base_area` VALUES ('620102', '城关区', '6201');
INSERT INTO `base_area` VALUES ('620103', '七里河区', '6201');
INSERT INTO `base_area` VALUES ('620104', '西固区', '6201');
INSERT INTO `base_area` VALUES ('620105', '安宁区', '6201');
INSERT INTO `base_area` VALUES ('620111', '红古区', '6201');
INSERT INTO `base_area` VALUES ('620121', '永登县', '6201');
INSERT INTO `base_area` VALUES ('620122', '皋兰县', '6201');
INSERT INTO `base_area` VALUES ('620123', '榆中县', '6201');
INSERT INTO `base_area` VALUES ('620171', '兰州新区', '6201');
INSERT INTO `base_area` VALUES ('620201', '嘉峪关市', '6202');
INSERT INTO `base_area` VALUES ('620302', '金川区', '6203');
INSERT INTO `base_area` VALUES ('620321', '永昌县', '6203');
INSERT INTO `base_area` VALUES ('620402', '白银区', '6204');
INSERT INTO `base_area` VALUES ('620403', '平川区', '6204');
INSERT INTO `base_area` VALUES ('620421', '靖远县', '6204');
INSERT INTO `base_area` VALUES ('620422', '会宁县', '6204');
INSERT INTO `base_area` VALUES ('620423', '景泰县', '6204');
INSERT INTO `base_area` VALUES ('620502', '秦州区', '6205');
INSERT INTO `base_area` VALUES ('620503', '麦积区', '6205');
INSERT INTO `base_area` VALUES ('620521', '清水县', '6205');
INSERT INTO `base_area` VALUES ('620522', '秦安县', '6205');
INSERT INTO `base_area` VALUES ('620523', '甘谷县', '6205');
INSERT INTO `base_area` VALUES ('620524', '武山县', '6205');
INSERT INTO `base_area` VALUES ('620525', '张家川回族自治县', '6205');
INSERT INTO `base_area` VALUES ('620602', '凉州区', '6206');
INSERT INTO `base_area` VALUES ('620621', '民勤县', '6206');
INSERT INTO `base_area` VALUES ('620622', '古浪县', '6206');
INSERT INTO `base_area` VALUES ('620623', '天祝藏族自治县', '6206');
INSERT INTO `base_area` VALUES ('620702', '甘州区', '6207');
INSERT INTO `base_area` VALUES ('620721', '肃南裕固族自治县', '6207');
INSERT INTO `base_area` VALUES ('620722', '民乐县', '6207');
INSERT INTO `base_area` VALUES ('620723', '临泽县', '6207');
INSERT INTO `base_area` VALUES ('620724', '高台县', '6207');
INSERT INTO `base_area` VALUES ('620725', '山丹县', '6207');
INSERT INTO `base_area` VALUES ('620802', '崆峒区', '6208');
INSERT INTO `base_area` VALUES ('620821', '泾川县', '6208');
INSERT INTO `base_area` VALUES ('620822', '灵台县', '6208');
INSERT INTO `base_area` VALUES ('620823', '崇信县', '6208');
INSERT INTO `base_area` VALUES ('620825', '庄浪县', '6208');
INSERT INTO `base_area` VALUES ('620826', '静宁县', '6208');
INSERT INTO `base_area` VALUES ('620881', '华亭市', '6208');
INSERT INTO `base_area` VALUES ('620902', '肃州区', '6209');
INSERT INTO `base_area` VALUES ('620921', '金塔县', '6209');
INSERT INTO `base_area` VALUES ('620922', '瓜州县', '6209');
INSERT INTO `base_area` VALUES ('620923', '肃北蒙古族自治县', '6209');
INSERT INTO `base_area` VALUES ('620924', '阿克塞哈萨克族自治县', '6209');
INSERT INTO `base_area` VALUES ('620981', '玉门市', '6209');
INSERT INTO `base_area` VALUES ('620982', '敦煌市', '6209');
INSERT INTO `base_area` VALUES ('621002', '西峰区', '6210');
INSERT INTO `base_area` VALUES ('621021', '庆城县', '6210');
INSERT INTO `base_area` VALUES ('621022', '环县', '6210');
INSERT INTO `base_area` VALUES ('621023', '华池县', '6210');
INSERT INTO `base_area` VALUES ('621024', '合水县', '6210');
INSERT INTO `base_area` VALUES ('621025', '正宁县', '6210');
INSERT INTO `base_area` VALUES ('621026', '宁县', '6210');
INSERT INTO `base_area` VALUES ('621027', '镇原县', '6210');
INSERT INTO `base_area` VALUES ('621102', '安定区', '6211');
INSERT INTO `base_area` VALUES ('621121', '通渭县', '6211');
INSERT INTO `base_area` VALUES ('621122', '陇西县', '6211');
INSERT INTO `base_area` VALUES ('621123', '渭源县', '6211');
INSERT INTO `base_area` VALUES ('621124', '临洮县', '6211');
INSERT INTO `base_area` VALUES ('621125', '漳县', '6211');
INSERT INTO `base_area` VALUES ('621126', '岷县', '6211');
INSERT INTO `base_area` VALUES ('621202', '武都区', '6212');
INSERT INTO `base_area` VALUES ('621221', '成县', '6212');
INSERT INTO `base_area` VALUES ('621222', '文县', '6212');
INSERT INTO `base_area` VALUES ('621223', '宕昌县', '6212');
INSERT INTO `base_area` VALUES ('621224', '康县', '6212');
INSERT INTO `base_area` VALUES ('621225', '西和县', '6212');
INSERT INTO `base_area` VALUES ('621226', '礼县', '6212');
INSERT INTO `base_area` VALUES ('621227', '徽县', '6212');
INSERT INTO `base_area` VALUES ('621228', '两当县', '6212');
INSERT INTO `base_area` VALUES ('622901', '临夏市', '6229');
INSERT INTO `base_area` VALUES ('622921', '临夏县', '6229');
INSERT INTO `base_area` VALUES ('622922', '康乐县', '6229');
INSERT INTO `base_area` VALUES ('622923', '永靖县', '6229');
INSERT INTO `base_area` VALUES ('622924', '广河县', '6229');
INSERT INTO `base_area` VALUES ('622925', '和政县', '6229');
INSERT INTO `base_area` VALUES ('622926', '东乡族自治县', '6229');
INSERT INTO `base_area` VALUES ('622927', '积石山保安族东乡族撒拉族自治县', '6229');
INSERT INTO `base_area` VALUES ('623001', '合作市', '6230');
INSERT INTO `base_area` VALUES ('623021', '临潭县', '6230');
INSERT INTO `base_area` VALUES ('623022', '卓尼县', '6230');
INSERT INTO `base_area` VALUES ('623023', '舟曲县', '6230');
INSERT INTO `base_area` VALUES ('623024', '迭部县', '6230');
INSERT INTO `base_area` VALUES ('623025', '玛曲县', '6230');
INSERT INTO `base_area` VALUES ('623026', '碌曲县', '6230');
INSERT INTO `base_area` VALUES ('623027', '夏河县', '6230');
INSERT INTO `base_area` VALUES ('630102', '城东区', '6301');
INSERT INTO `base_area` VALUES ('630103', '城中区', '6301');
INSERT INTO `base_area` VALUES ('630104', '城西区', '6301');
INSERT INTO `base_area` VALUES ('630105', '城北区', '6301');
INSERT INTO `base_area` VALUES ('630106', '湟中区', '6301');
INSERT INTO `base_area` VALUES ('630121', '大通回族土族自治县', '6301');
INSERT INTO `base_area` VALUES ('630123', '湟源县', '6301');
INSERT INTO `base_area` VALUES ('630202', '乐都区', '6302');
INSERT INTO `base_area` VALUES ('630203', '平安区', '6302');
INSERT INTO `base_area` VALUES ('630222', '民和回族土族自治县', '6302');
INSERT INTO `base_area` VALUES ('630223', '互助土族自治县', '6302');
INSERT INTO `base_area` VALUES ('630224', '化隆回族自治县', '6302');
INSERT INTO `base_area` VALUES ('630225', '循化撒拉族自治县', '6302');
INSERT INTO `base_area` VALUES ('632221', '门源回族自治县', '6322');
INSERT INTO `base_area` VALUES ('632222', '祁连县', '6322');
INSERT INTO `base_area` VALUES ('632223', '海晏县', '6322');
INSERT INTO `base_area` VALUES ('632224', '刚察县', '6322');
INSERT INTO `base_area` VALUES ('632301', '同仁市', '6323');
INSERT INTO `base_area` VALUES ('632322', '尖扎县', '6323');
INSERT INTO `base_area` VALUES ('632323', '泽库县', '6323');
INSERT INTO `base_area` VALUES ('632324', '河南蒙古族自治县', '6323');
INSERT INTO `base_area` VALUES ('632521', '共和县', '6325');
INSERT INTO `base_area` VALUES ('632522', '同德县', '6325');
INSERT INTO `base_area` VALUES ('632523', '贵德县', '6325');
INSERT INTO `base_area` VALUES ('632524', '兴海县', '6325');
INSERT INTO `base_area` VALUES ('632525', '贵南县', '6325');
INSERT INTO `base_area` VALUES ('632621', '玛沁县', '6326');
INSERT INTO `base_area` VALUES ('632622', '班玛县', '6326');
INSERT INTO `base_area` VALUES ('632623', '甘德县', '6326');
INSERT INTO `base_area` VALUES ('632624', '达日县', '6326');
INSERT INTO `base_area` VALUES ('632625', '久治县', '6326');
INSERT INTO `base_area` VALUES ('632626', '玛多县', '6326');
INSERT INTO `base_area` VALUES ('632701', '玉树市', '6327');
INSERT INTO `base_area` VALUES ('632722', '杂多县', '6327');
INSERT INTO `base_area` VALUES ('632723', '称多县', '6327');
INSERT INTO `base_area` VALUES ('632724', '治多县', '6327');
INSERT INTO `base_area` VALUES ('632725', '囊谦县', '6327');
INSERT INTO `base_area` VALUES ('632726', '曲麻莱县', '6327');
INSERT INTO `base_area` VALUES ('632801', '格尔木市', '6328');
INSERT INTO `base_area` VALUES ('632802', '德令哈市', '6328');
INSERT INTO `base_area` VALUES ('632803', '茫崖市', '6328');
INSERT INTO `base_area` VALUES ('632821', '乌兰县', '6328');
INSERT INTO `base_area` VALUES ('632822', '都兰县', '6328');
INSERT INTO `base_area` VALUES ('632823', '天峻县', '6328');
INSERT INTO `base_area` VALUES ('632857', '大柴旦行政委员会', '6328');
INSERT INTO `base_area` VALUES ('640104', '兴庆区', '6401');
INSERT INTO `base_area` VALUES ('640105', '西夏区', '6401');
INSERT INTO `base_area` VALUES ('640106', '金凤区', '6401');
INSERT INTO `base_area` VALUES ('640121', '永宁县', '6401');
INSERT INTO `base_area` VALUES ('640122', '贺兰县', '6401');
INSERT INTO `base_area` VALUES ('640181', '灵武市', '6401');
INSERT INTO `base_area` VALUES ('640202', '大武口区', '6402');
INSERT INTO `base_area` VALUES ('640205', '惠农区', '6402');
INSERT INTO `base_area` VALUES ('640221', '平罗县', '6402');
INSERT INTO `base_area` VALUES ('640302', '利通区', '6403');
INSERT INTO `base_area` VALUES ('640303', '红寺堡区', '6403');
INSERT INTO `base_area` VALUES ('640323', '盐池县', '6403');
INSERT INTO `base_area` VALUES ('640324', '同心县', '6403');
INSERT INTO `base_area` VALUES ('640381', '青铜峡市', '6403');
INSERT INTO `base_area` VALUES ('640402', '原州区', '6404');
INSERT INTO `base_area` VALUES ('640422', '西吉县', '6404');
INSERT INTO `base_area` VALUES ('640423', '隆德县', '6404');
INSERT INTO `base_area` VALUES ('640424', '泾源县', '6404');
INSERT INTO `base_area` VALUES ('640425', '彭阳县', '6404');
INSERT INTO `base_area` VALUES ('640502', '沙坡头区', '6405');
INSERT INTO `base_area` VALUES ('640521', '中宁县', '6405');
INSERT INTO `base_area` VALUES ('640522', '海原县', '6405');
INSERT INTO `base_area` VALUES ('650102', '天山区', '6501');
INSERT INTO `base_area` VALUES ('650103', '沙依巴克区', '6501');
INSERT INTO `base_area` VALUES ('650104', '新市区', '6501');
INSERT INTO `base_area` VALUES ('650105', '水磨沟区', '6501');
INSERT INTO `base_area` VALUES ('650106', '头屯河区', '6501');
INSERT INTO `base_area` VALUES ('650107', '达坂城区', '6501');
INSERT INTO `base_area` VALUES ('650109', '米东区', '6501');
INSERT INTO `base_area` VALUES ('650121', '乌鲁木齐县', '6501');
INSERT INTO `base_area` VALUES ('650202', '独山子区', '6502');
INSERT INTO `base_area` VALUES ('650203', '克拉玛依区', '6502');
INSERT INTO `base_area` VALUES ('650204', '白碱滩区', '6502');
INSERT INTO `base_area` VALUES ('650205', '乌尔禾区', '6502');
INSERT INTO `base_area` VALUES ('650402', '高昌区', '6504');
INSERT INTO `base_area` VALUES ('650421', '鄯善县', '6504');
INSERT INTO `base_area` VALUES ('650422', '托克逊县', '6504');
INSERT INTO `base_area` VALUES ('650502', '伊州区', '6505');
INSERT INTO `base_area` VALUES ('650521', '巴里坤哈萨克自治县', '6505');
INSERT INTO `base_area` VALUES ('650522', '伊吾县', '6505');
INSERT INTO `base_area` VALUES ('652301', '昌吉市', '6523');
INSERT INTO `base_area` VALUES ('652302', '阜康市', '6523');
INSERT INTO `base_area` VALUES ('652323', '呼图壁县', '6523');
INSERT INTO `base_area` VALUES ('652324', '玛纳斯县', '6523');
INSERT INTO `base_area` VALUES ('652325', '奇台县', '6523');
INSERT INTO `base_area` VALUES ('652327', '吉木萨尔县', '6523');
INSERT INTO `base_area` VALUES ('652328', '木垒哈萨克自治县', '6523');
INSERT INTO `base_area` VALUES ('652701', '博乐市', '6527');
INSERT INTO `base_area` VALUES ('652702', '阿拉山口市', '6527');
INSERT INTO `base_area` VALUES ('652722', '精河县', '6527');
INSERT INTO `base_area` VALUES ('652723', '温泉县', '6527');
INSERT INTO `base_area` VALUES ('652801', '库尔勒市', '6528');
INSERT INTO `base_area` VALUES ('652822', '轮台县', '6528');
INSERT INTO `base_area` VALUES ('652823', '尉犁县', '6528');
INSERT INTO `base_area` VALUES ('652824', '若羌县', '6528');
INSERT INTO `base_area` VALUES ('652825', '且末县', '6528');
INSERT INTO `base_area` VALUES ('652826', '焉耆回族自治县', '6528');
INSERT INTO `base_area` VALUES ('652827', '和静县', '6528');
INSERT INTO `base_area` VALUES ('652828', '和硕县', '6528');
INSERT INTO `base_area` VALUES ('652829', '博湖县', '6528');
INSERT INTO `base_area` VALUES ('652871', '库尔勒经济技术开发区', '6528');
INSERT INTO `base_area` VALUES ('652901', '阿克苏市', '6529');
INSERT INTO `base_area` VALUES ('652902', '库车市', '6529');
INSERT INTO `base_area` VALUES ('652922', '温宿县', '6529');
INSERT INTO `base_area` VALUES ('652924', '沙雅县', '6529');
INSERT INTO `base_area` VALUES ('652925', '新和县', '6529');
INSERT INTO `base_area` VALUES ('652926', '拜城县', '6529');
INSERT INTO `base_area` VALUES ('652927', '乌什县', '6529');
INSERT INTO `base_area` VALUES ('652928', '阿瓦提县', '6529');
INSERT INTO `base_area` VALUES ('652929', '柯坪县', '6529');
INSERT INTO `base_area` VALUES ('653001', '阿图什市', '6530');
INSERT INTO `base_area` VALUES ('653022', '阿克陶县', '6530');
INSERT INTO `base_area` VALUES ('653023', '阿合奇县', '6530');
INSERT INTO `base_area` VALUES ('653024', '乌恰县', '6530');
INSERT INTO `base_area` VALUES ('653101', '喀什市', '6531');
INSERT INTO `base_area` VALUES ('653121', '疏附县', '6531');
INSERT INTO `base_area` VALUES ('653122', '疏勒县', '6531');
INSERT INTO `base_area` VALUES ('653123', '英吉沙县', '6531');
INSERT INTO `base_area` VALUES ('653124', '泽普县', '6531');
INSERT INTO `base_area` VALUES ('653125', '莎车县', '6531');
INSERT INTO `base_area` VALUES ('653126', '叶城县', '6531');
INSERT INTO `base_area` VALUES ('653127', '麦盖提县', '6531');
INSERT INTO `base_area` VALUES ('653128', '岳普湖县', '6531');
INSERT INTO `base_area` VALUES ('653129', '伽师县', '6531');
INSERT INTO `base_area` VALUES ('653130', '巴楚县', '6531');
INSERT INTO `base_area` VALUES ('653131', '塔什库尔干塔吉克自治县', '6531');
INSERT INTO `base_area` VALUES ('653201', '和田市', '6532');
INSERT INTO `base_area` VALUES ('653221', '和田县', '6532');
INSERT INTO `base_area` VALUES ('653222', '墨玉县', '6532');
INSERT INTO `base_area` VALUES ('653223', '皮山县', '6532');
INSERT INTO `base_area` VALUES ('653224', '洛浦县', '6532');
INSERT INTO `base_area` VALUES ('653225', '策勒县', '6532');
INSERT INTO `base_area` VALUES ('653226', '于田县', '6532');
INSERT INTO `base_area` VALUES ('653227', '民丰县', '6532');
INSERT INTO `base_area` VALUES ('654002', '伊宁市', '6540');
INSERT INTO `base_area` VALUES ('654003', '奎屯市', '6540');
INSERT INTO `base_area` VALUES ('654004', '霍尔果斯市', '6540');
INSERT INTO `base_area` VALUES ('654021', '伊宁县', '6540');
INSERT INTO `base_area` VALUES ('654022', '察布查尔锡伯自治县', '6540');
INSERT INTO `base_area` VALUES ('654023', '霍城县', '6540');
INSERT INTO `base_area` VALUES ('654024', '巩留县', '6540');
INSERT INTO `base_area` VALUES ('654025', '新源县', '6540');
INSERT INTO `base_area` VALUES ('654026', '昭苏县', '6540');
INSERT INTO `base_area` VALUES ('654027', '特克斯县', '6540');
INSERT INTO `base_area` VALUES ('654028', '尼勒克县', '6540');
INSERT INTO `base_area` VALUES ('654201', '塔城市', '6542');
INSERT INTO `base_area` VALUES ('654202', '乌苏市', '6542');
INSERT INTO `base_area` VALUES ('654203', '沙湾市', '6542');
INSERT INTO `base_area` VALUES ('654221', '额敏县', '6542');
INSERT INTO `base_area` VALUES ('654224', '托里县', '6542');
INSERT INTO `base_area` VALUES ('654225', '裕民县', '6542');
INSERT INTO `base_area` VALUES ('654226', '和布克赛尔蒙古自治县', '6542');
INSERT INTO `base_area` VALUES ('654301', '阿勒泰市', '6543');
INSERT INTO `base_area` VALUES ('654321', '布尔津县', '6543');
INSERT INTO `base_area` VALUES ('654322', '富蕴县', '6543');
INSERT INTO `base_area` VALUES ('654323', '福海县', '6543');
INSERT INTO `base_area` VALUES ('654324', '哈巴河县', '6543');
INSERT INTO `base_area` VALUES ('654325', '青河县', '6543');
INSERT INTO `base_area` VALUES ('654326', '吉木乃县', '6543');
INSERT INTO `base_area` VALUES ('659001', '石河子市', '6590');
INSERT INTO `base_area` VALUES ('659002', '阿拉尔市', '6590');
INSERT INTO `base_area` VALUES ('659003', '图木舒克市', '6590');
INSERT INTO `base_area` VALUES ('659004', '五家渠市', '6590');
INSERT INTO `base_area` VALUES ('659005', '北屯市', '6590');
INSERT INTO `base_area` VALUES ('659006', '铁门关市', '6590');
INSERT INTO `base_area` VALUES ('659007', '双河市', '6590');
INSERT INTO `base_area` VALUES ('659008', '可克达拉市', '6590');
INSERT INTO `base_area` VALUES ('659009', '昆玉市', '6590');
INSERT INTO `base_area` VALUES ('659010', '胡杨河市', '6590');
INSERT INTO `base_area` VALUES ('659011', '新星市', '6590');

-- ----------------------------
-- Table structure for base_city
-- ----------------------------
DROP TABLE IF EXISTS `base_city`;
CREATE TABLE `base_city`  (
  `code` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '城市编码',
  `name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '城市名称',
  `province_code` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '省份编码',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '城市表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_city
-- ----------------------------
INSERT INTO `base_city` VALUES ('1101', '市辖区', '11');
INSERT INTO `base_city` VALUES ('1201', '市辖区', '12');
INSERT INTO `base_city` VALUES ('1301', '石家庄市', '13');
INSERT INTO `base_city` VALUES ('1302', '唐山市', '13');
INSERT INTO `base_city` VALUES ('1303', '秦皇岛市', '13');
INSERT INTO `base_city` VALUES ('1304', '邯郸市', '13');
INSERT INTO `base_city` VALUES ('1305', '邢台市', '13');
INSERT INTO `base_city` VALUES ('1306', '保定市', '13');
INSERT INTO `base_city` VALUES ('1307', '张家口市', '13');
INSERT INTO `base_city` VALUES ('1308', '承德市', '13');
INSERT INTO `base_city` VALUES ('1309', '沧州市', '13');
INSERT INTO `base_city` VALUES ('1310', '廊坊市', '13');
INSERT INTO `base_city` VALUES ('1311', '衡水市', '13');
INSERT INTO `base_city` VALUES ('1401', '太原市', '14');
INSERT INTO `base_city` VALUES ('1402', '大同市', '14');
INSERT INTO `base_city` VALUES ('1403', '阳泉市', '14');
INSERT INTO `base_city` VALUES ('1404', '长治市', '14');
INSERT INTO `base_city` VALUES ('1405', '晋城市', '14');
INSERT INTO `base_city` VALUES ('1406', '朔州市', '14');
INSERT INTO `base_city` VALUES ('1407', '晋中市', '14');
INSERT INTO `base_city` VALUES ('1408', '运城市', '14');
INSERT INTO `base_city` VALUES ('1409', '忻州市', '14');
INSERT INTO `base_city` VALUES ('1410', '临汾市', '14');
INSERT INTO `base_city` VALUES ('1411', '吕梁市', '14');
INSERT INTO `base_city` VALUES ('1501', '呼和浩特市', '15');
INSERT INTO `base_city` VALUES ('1502', '包头市', '15');
INSERT INTO `base_city` VALUES ('1503', '乌海市', '15');
INSERT INTO `base_city` VALUES ('1504', '赤峰市', '15');
INSERT INTO `base_city` VALUES ('1505', '通辽市', '15');
INSERT INTO `base_city` VALUES ('1506', '鄂尔多斯市', '15');
INSERT INTO `base_city` VALUES ('1507', '呼伦贝尔市', '15');
INSERT INTO `base_city` VALUES ('1508', '巴彦淖尔市', '15');
INSERT INTO `base_city` VALUES ('1509', '乌兰察布市', '15');
INSERT INTO `base_city` VALUES ('1522', '兴安盟', '15');
INSERT INTO `base_city` VALUES ('1525', '锡林郭勒盟', '15');
INSERT INTO `base_city` VALUES ('1529', '阿拉善盟', '15');
INSERT INTO `base_city` VALUES ('2101', '沈阳市', '21');
INSERT INTO `base_city` VALUES ('2102', '大连市', '21');
INSERT INTO `base_city` VALUES ('2103', '鞍山市', '21');
INSERT INTO `base_city` VALUES ('2104', '抚顺市', '21');
INSERT INTO `base_city` VALUES ('2105', '本溪市', '21');
INSERT INTO `base_city` VALUES ('2106', '丹东市', '21');
INSERT INTO `base_city` VALUES ('2107', '锦州市', '21');
INSERT INTO `base_city` VALUES ('2108', '营口市', '21');
INSERT INTO `base_city` VALUES ('2109', '阜新市', '21');
INSERT INTO `base_city` VALUES ('2110', '辽阳市', '21');
INSERT INTO `base_city` VALUES ('2111', '盘锦市', '21');
INSERT INTO `base_city` VALUES ('2112', '铁岭市', '21');
INSERT INTO `base_city` VALUES ('2113', '朝阳市', '21');
INSERT INTO `base_city` VALUES ('2114', '葫芦岛市', '21');
INSERT INTO `base_city` VALUES ('2201', '长春市', '22');
INSERT INTO `base_city` VALUES ('2202', '吉林市', '22');
INSERT INTO `base_city` VALUES ('2203', '四平市', '22');
INSERT INTO `base_city` VALUES ('2204', '辽源市', '22');
INSERT INTO `base_city` VALUES ('2205', '通化市', '22');
INSERT INTO `base_city` VALUES ('2206', '白山市', '22');
INSERT INTO `base_city` VALUES ('2207', '松原市', '22');
INSERT INTO `base_city` VALUES ('2208', '白城市', '22');
INSERT INTO `base_city` VALUES ('2224', '延边朝鲜族自治州', '22');
INSERT INTO `base_city` VALUES ('2301', '哈尔滨市', '23');
INSERT INTO `base_city` VALUES ('2302', '齐齐哈尔市', '23');
INSERT INTO `base_city` VALUES ('2303', '鸡西市', '23');
INSERT INTO `base_city` VALUES ('2304', '鹤岗市', '23');
INSERT INTO `base_city` VALUES ('2305', '双鸭山市', '23');
INSERT INTO `base_city` VALUES ('2306', '大庆市', '23');
INSERT INTO `base_city` VALUES ('2307', '伊春市', '23');
INSERT INTO `base_city` VALUES ('2308', '佳木斯市', '23');
INSERT INTO `base_city` VALUES ('2309', '七台河市', '23');
INSERT INTO `base_city` VALUES ('2310', '牡丹江市', '23');
INSERT INTO `base_city` VALUES ('2311', '黑河市', '23');
INSERT INTO `base_city` VALUES ('2312', '绥化市', '23');
INSERT INTO `base_city` VALUES ('2327', '大兴安岭地区', '23');
INSERT INTO `base_city` VALUES ('3101', '市辖区', '31');
INSERT INTO `base_city` VALUES ('3201', '南京市', '32');
INSERT INTO `base_city` VALUES ('3202', '无锡市', '32');
INSERT INTO `base_city` VALUES ('3203', '徐州市', '32');
INSERT INTO `base_city` VALUES ('3204', '常州市', '32');
INSERT INTO `base_city` VALUES ('3205', '苏州市', '32');
INSERT INTO `base_city` VALUES ('3206', '南通市', '32');
INSERT INTO `base_city` VALUES ('3207', '连云港市', '32');
INSERT INTO `base_city` VALUES ('3208', '淮安市', '32');
INSERT INTO `base_city` VALUES ('3209', '盐城市', '32');
INSERT INTO `base_city` VALUES ('3210', '扬州市', '32');
INSERT INTO `base_city` VALUES ('3211', '镇江市', '32');
INSERT INTO `base_city` VALUES ('3212', '泰州市', '32');
INSERT INTO `base_city` VALUES ('3213', '宿迁市', '32');
INSERT INTO `base_city` VALUES ('3301', '杭州市', '33');
INSERT INTO `base_city` VALUES ('3302', '宁波市', '33');
INSERT INTO `base_city` VALUES ('3303', '温州市', '33');
INSERT INTO `base_city` VALUES ('3304', '嘉兴市', '33');
INSERT INTO `base_city` VALUES ('3305', '湖州市', '33');
INSERT INTO `base_city` VALUES ('3306', '绍兴市', '33');
INSERT INTO `base_city` VALUES ('3307', '金华市', '33');
INSERT INTO `base_city` VALUES ('3308', '衢州市', '33');
INSERT INTO `base_city` VALUES ('3309', '舟山市', '33');
INSERT INTO `base_city` VALUES ('3310', '台州市', '33');
INSERT INTO `base_city` VALUES ('3311', '丽水市', '33');
INSERT INTO `base_city` VALUES ('3401', '合肥市', '34');
INSERT INTO `base_city` VALUES ('3402', '芜湖市', '34');
INSERT INTO `base_city` VALUES ('3403', '蚌埠市', '34');
INSERT INTO `base_city` VALUES ('3404', '淮南市', '34');
INSERT INTO `base_city` VALUES ('3405', '马鞍山市', '34');
INSERT INTO `base_city` VALUES ('3406', '淮北市', '34');
INSERT INTO `base_city` VALUES ('3407', '铜陵市', '34');
INSERT INTO `base_city` VALUES ('3408', '安庆市', '34');
INSERT INTO `base_city` VALUES ('3410', '黄山市', '34');
INSERT INTO `base_city` VALUES ('3411', '滁州市', '34');
INSERT INTO `base_city` VALUES ('3412', '阜阳市', '34');
INSERT INTO `base_city` VALUES ('3413', '宿州市', '34');
INSERT INTO `base_city` VALUES ('3415', '六安市', '34');
INSERT INTO `base_city` VALUES ('3416', '亳州市', '34');
INSERT INTO `base_city` VALUES ('3417', '池州市', '34');
INSERT INTO `base_city` VALUES ('3418', '宣城市', '34');
INSERT INTO `base_city` VALUES ('3501', '福州市', '35');
INSERT INTO `base_city` VALUES ('3502', '厦门市', '35');
INSERT INTO `base_city` VALUES ('3503', '莆田市', '35');
INSERT INTO `base_city` VALUES ('3504', '三明市', '35');
INSERT INTO `base_city` VALUES ('3505', '泉州市', '35');
INSERT INTO `base_city` VALUES ('3506', '漳州市', '35');
INSERT INTO `base_city` VALUES ('3507', '南平市', '35');
INSERT INTO `base_city` VALUES ('3508', '龙岩市', '35');
INSERT INTO `base_city` VALUES ('3509', '宁德市', '35');
INSERT INTO `base_city` VALUES ('3601', '南昌市', '36');
INSERT INTO `base_city` VALUES ('3602', '景德镇市', '36');
INSERT INTO `base_city` VALUES ('3603', '萍乡市', '36');
INSERT INTO `base_city` VALUES ('3604', '九江市', '36');
INSERT INTO `base_city` VALUES ('3605', '新余市', '36');
INSERT INTO `base_city` VALUES ('3606', '鹰潭市', '36');
INSERT INTO `base_city` VALUES ('3607', '赣州市', '36');
INSERT INTO `base_city` VALUES ('3608', '吉安市', '36');
INSERT INTO `base_city` VALUES ('3609', '宜春市', '36');
INSERT INTO `base_city` VALUES ('3610', '抚州市', '36');
INSERT INTO `base_city` VALUES ('3611', '上饶市', '36');
INSERT INTO `base_city` VALUES ('3701', '济南市', '37');
INSERT INTO `base_city` VALUES ('3702', '青岛市', '37');
INSERT INTO `base_city` VALUES ('3703', '淄博市', '37');
INSERT INTO `base_city` VALUES ('3704', '枣庄市', '37');
INSERT INTO `base_city` VALUES ('3705', '东营市', '37');
INSERT INTO `base_city` VALUES ('3706', '烟台市', '37');
INSERT INTO `base_city` VALUES ('3707', '潍坊市', '37');
INSERT INTO `base_city` VALUES ('3708', '济宁市', '37');
INSERT INTO `base_city` VALUES ('3709', '泰安市', '37');
INSERT INTO `base_city` VALUES ('3710', '威海市', '37');
INSERT INTO `base_city` VALUES ('3711', '日照市', '37');
INSERT INTO `base_city` VALUES ('3713', '临沂市', '37');
INSERT INTO `base_city` VALUES ('3714', '德州市', '37');
INSERT INTO `base_city` VALUES ('3715', '聊城市', '37');
INSERT INTO `base_city` VALUES ('3716', '滨州市', '37');
INSERT INTO `base_city` VALUES ('3717', '菏泽市', '37');
INSERT INTO `base_city` VALUES ('4101', '郑州市', '41');
INSERT INTO `base_city` VALUES ('4102', '开封市', '41');
INSERT INTO `base_city` VALUES ('4103', '洛阳市', '41');
INSERT INTO `base_city` VALUES ('4104', '平顶山市', '41');
INSERT INTO `base_city` VALUES ('4105', '安阳市', '41');
INSERT INTO `base_city` VALUES ('4106', '鹤壁市', '41');
INSERT INTO `base_city` VALUES ('4107', '新乡市', '41');
INSERT INTO `base_city` VALUES ('4108', '焦作市', '41');
INSERT INTO `base_city` VALUES ('4109', '濮阳市', '41');
INSERT INTO `base_city` VALUES ('4110', '许昌市', '41');
INSERT INTO `base_city` VALUES ('4111', '漯河市', '41');
INSERT INTO `base_city` VALUES ('4112', '三门峡市', '41');
INSERT INTO `base_city` VALUES ('4113', '南阳市', '41');
INSERT INTO `base_city` VALUES ('4114', '商丘市', '41');
INSERT INTO `base_city` VALUES ('4115', '信阳市', '41');
INSERT INTO `base_city` VALUES ('4116', '周口市', '41');
INSERT INTO `base_city` VALUES ('4117', '驻马店市', '41');
INSERT INTO `base_city` VALUES ('4190', '省直辖县级行政区划', '41');
INSERT INTO `base_city` VALUES ('4201', '武汉市', '42');
INSERT INTO `base_city` VALUES ('4202', '黄石市', '42');
INSERT INTO `base_city` VALUES ('4203', '十堰市', '42');
INSERT INTO `base_city` VALUES ('4205', '宜昌市', '42');
INSERT INTO `base_city` VALUES ('4206', '襄阳市', '42');
INSERT INTO `base_city` VALUES ('4207', '鄂州市', '42');
INSERT INTO `base_city` VALUES ('4208', '荆门市', '42');
INSERT INTO `base_city` VALUES ('4209', '孝感市', '42');
INSERT INTO `base_city` VALUES ('4210', '荆州市', '42');
INSERT INTO `base_city` VALUES ('4211', '黄冈市', '42');
INSERT INTO `base_city` VALUES ('4212', '咸宁市', '42');
INSERT INTO `base_city` VALUES ('4213', '随州市', '42');
INSERT INTO `base_city` VALUES ('4228', '恩施土家族苗族自治州', '42');
INSERT INTO `base_city` VALUES ('4290', '省直辖县级行政区划', '42');
INSERT INTO `base_city` VALUES ('4301', '长沙市', '43');
INSERT INTO `base_city` VALUES ('4302', '株洲市', '43');
INSERT INTO `base_city` VALUES ('4303', '湘潭市', '43');
INSERT INTO `base_city` VALUES ('4304', '衡阳市', '43');
INSERT INTO `base_city` VALUES ('4305', '邵阳市', '43');
INSERT INTO `base_city` VALUES ('4306', '岳阳市', '43');
INSERT INTO `base_city` VALUES ('4307', '常德市', '43');
INSERT INTO `base_city` VALUES ('4308', '张家界市', '43');
INSERT INTO `base_city` VALUES ('4309', '益阳市', '43');
INSERT INTO `base_city` VALUES ('4310', '郴州市', '43');
INSERT INTO `base_city` VALUES ('4311', '永州市', '43');
INSERT INTO `base_city` VALUES ('4312', '怀化市', '43');
INSERT INTO `base_city` VALUES ('4313', '娄底市', '43');
INSERT INTO `base_city` VALUES ('4331', '湘西土家族苗族自治州', '43');
INSERT INTO `base_city` VALUES ('4401', '广州市', '44');
INSERT INTO `base_city` VALUES ('4402', '韶关市', '44');
INSERT INTO `base_city` VALUES ('4403', '深圳市', '44');
INSERT INTO `base_city` VALUES ('4404', '珠海市', '44');
INSERT INTO `base_city` VALUES ('4405', '汕头市', '44');
INSERT INTO `base_city` VALUES ('4406', '佛山市', '44');
INSERT INTO `base_city` VALUES ('4407', '江门市', '44');
INSERT INTO `base_city` VALUES ('4408', '湛江市', '44');
INSERT INTO `base_city` VALUES ('4409', '茂名市', '44');
INSERT INTO `base_city` VALUES ('4412', '肇庆市', '44');
INSERT INTO `base_city` VALUES ('4413', '惠州市', '44');
INSERT INTO `base_city` VALUES ('4414', '梅州市', '44');
INSERT INTO `base_city` VALUES ('4415', '汕尾市', '44');
INSERT INTO `base_city` VALUES ('4416', '河源市', '44');
INSERT INTO `base_city` VALUES ('4417', '阳江市', '44');
INSERT INTO `base_city` VALUES ('4418', '清远市', '44');
INSERT INTO `base_city` VALUES ('4419', '东莞市', '44');
INSERT INTO `base_city` VALUES ('4420', '中山市', '44');
INSERT INTO `base_city` VALUES ('4451', '潮州市', '44');
INSERT INTO `base_city` VALUES ('4452', '揭阳市', '44');
INSERT INTO `base_city` VALUES ('4453', '云浮市', '44');
INSERT INTO `base_city` VALUES ('4501', '南宁市', '45');
INSERT INTO `base_city` VALUES ('4502', '柳州市', '45');
INSERT INTO `base_city` VALUES ('4503', '桂林市', '45');
INSERT INTO `base_city` VALUES ('4504', '梧州市', '45');
INSERT INTO `base_city` VALUES ('4505', '北海市', '45');
INSERT INTO `base_city` VALUES ('4506', '防城港市', '45');
INSERT INTO `base_city` VALUES ('4507', '钦州市', '45');
INSERT INTO `base_city` VALUES ('4508', '贵港市', '45');
INSERT INTO `base_city` VALUES ('4509', '玉林市', '45');
INSERT INTO `base_city` VALUES ('4510', '百色市', '45');
INSERT INTO `base_city` VALUES ('4511', '贺州市', '45');
INSERT INTO `base_city` VALUES ('4512', '河池市', '45');
INSERT INTO `base_city` VALUES ('4513', '来宾市', '45');
INSERT INTO `base_city` VALUES ('4514', '崇左市', '45');
INSERT INTO `base_city` VALUES ('4601', '海口市', '46');
INSERT INTO `base_city` VALUES ('4602', '三亚市', '46');
INSERT INTO `base_city` VALUES ('4603', '三沙市', '46');
INSERT INTO `base_city` VALUES ('4604', '儋州市', '46');
INSERT INTO `base_city` VALUES ('4690', '省直辖县级行政区划', '46');
INSERT INTO `base_city` VALUES ('5001', '市辖区', '50');
INSERT INTO `base_city` VALUES ('5002', '县', '50');
INSERT INTO `base_city` VALUES ('5101', '成都市', '51');
INSERT INTO `base_city` VALUES ('5103', '自贡市', '51');
INSERT INTO `base_city` VALUES ('5104', '攀枝花市', '51');
INSERT INTO `base_city` VALUES ('5105', '泸州市', '51');
INSERT INTO `base_city` VALUES ('5106', '德阳市', '51');
INSERT INTO `base_city` VALUES ('5107', '绵阳市', '51');
INSERT INTO `base_city` VALUES ('5108', '广元市', '51');
INSERT INTO `base_city` VALUES ('5109', '遂宁市', '51');
INSERT INTO `base_city` VALUES ('5110', '内江市', '51');
INSERT INTO `base_city` VALUES ('5111', '乐山市', '51');
INSERT INTO `base_city` VALUES ('5113', '南充市', '51');
INSERT INTO `base_city` VALUES ('5114', '眉山市', '51');
INSERT INTO `base_city` VALUES ('5115', '宜宾市', '51');
INSERT INTO `base_city` VALUES ('5116', '广安市', '51');
INSERT INTO `base_city` VALUES ('5117', '达州市', '51');
INSERT INTO `base_city` VALUES ('5118', '雅安市', '51');
INSERT INTO `base_city` VALUES ('5119', '巴中市', '51');
INSERT INTO `base_city` VALUES ('5120', '资阳市', '51');
INSERT INTO `base_city` VALUES ('5132', '阿坝藏族羌族自治州', '51');
INSERT INTO `base_city` VALUES ('5133', '甘孜藏族自治州', '51');
INSERT INTO `base_city` VALUES ('5134', '凉山彝族自治州', '51');
INSERT INTO `base_city` VALUES ('5201', '贵阳市', '52');
INSERT INTO `base_city` VALUES ('5202', '六盘水市', '52');
INSERT INTO `base_city` VALUES ('5203', '遵义市', '52');
INSERT INTO `base_city` VALUES ('5204', '安顺市', '52');
INSERT INTO `base_city` VALUES ('5205', '毕节市', '52');
INSERT INTO `base_city` VALUES ('5206', '铜仁市', '52');
INSERT INTO `base_city` VALUES ('5223', '黔西南布依族苗族自治州', '52');
INSERT INTO `base_city` VALUES ('5226', '黔东南苗族侗族自治州', '52');
INSERT INTO `base_city` VALUES ('5227', '黔南布依族苗族自治州', '52');
INSERT INTO `base_city` VALUES ('5301', '昆明市', '53');
INSERT INTO `base_city` VALUES ('5303', '曲靖市', '53');
INSERT INTO `base_city` VALUES ('5304', '玉溪市', '53');
INSERT INTO `base_city` VALUES ('5305', '保山市', '53');
INSERT INTO `base_city` VALUES ('5306', '昭通市', '53');
INSERT INTO `base_city` VALUES ('5307', '丽江市', '53');
INSERT INTO `base_city` VALUES ('5308', '普洱市', '53');
INSERT INTO `base_city` VALUES ('5309', '临沧市', '53');
INSERT INTO `base_city` VALUES ('5323', '楚雄彝族自治州', '53');
INSERT INTO `base_city` VALUES ('5325', '红河哈尼族彝族自治州', '53');
INSERT INTO `base_city` VALUES ('5326', '文山壮族苗族自治州', '53');
INSERT INTO `base_city` VALUES ('5328', '西双版纳傣族自治州', '53');
INSERT INTO `base_city` VALUES ('5329', '大理白族自治州', '53');
INSERT INTO `base_city` VALUES ('5331', '德宏傣族景颇族自治州', '53');
INSERT INTO `base_city` VALUES ('5333', '怒江傈僳族自治州', '53');
INSERT INTO `base_city` VALUES ('5334', '迪庆藏族自治州', '53');
INSERT INTO `base_city` VALUES ('5401', '拉萨市', '54');
INSERT INTO `base_city` VALUES ('5402', '日喀则市', '54');
INSERT INTO `base_city` VALUES ('5403', '昌都市', '54');
INSERT INTO `base_city` VALUES ('5404', '林芝市', '54');
INSERT INTO `base_city` VALUES ('5405', '山南市', '54');
INSERT INTO `base_city` VALUES ('5406', '那曲市', '54');
INSERT INTO `base_city` VALUES ('5425', '阿里地区', '54');
INSERT INTO `base_city` VALUES ('6101', '西安市', '61');
INSERT INTO `base_city` VALUES ('6102', '铜川市', '61');
INSERT INTO `base_city` VALUES ('6103', '宝鸡市', '61');
INSERT INTO `base_city` VALUES ('6104', '咸阳市', '61');
INSERT INTO `base_city` VALUES ('6105', '渭南市', '61');
INSERT INTO `base_city` VALUES ('6106', '延安市', '61');
INSERT INTO `base_city` VALUES ('6107', '汉中市', '61');
INSERT INTO `base_city` VALUES ('6108', '榆林市', '61');
INSERT INTO `base_city` VALUES ('6109', '安康市', '61');
INSERT INTO `base_city` VALUES ('6110', '商洛市', '61');
INSERT INTO `base_city` VALUES ('6201', '兰州市', '62');
INSERT INTO `base_city` VALUES ('6202', '嘉峪关市', '62');
INSERT INTO `base_city` VALUES ('6203', '金昌市', '62');
INSERT INTO `base_city` VALUES ('6204', '白银市', '62');
INSERT INTO `base_city` VALUES ('6205', '天水市', '62');
INSERT INTO `base_city` VALUES ('6206', '武威市', '62');
INSERT INTO `base_city` VALUES ('6207', '张掖市', '62');
INSERT INTO `base_city` VALUES ('6208', '平凉市', '62');
INSERT INTO `base_city` VALUES ('6209', '酒泉市', '62');
INSERT INTO `base_city` VALUES ('6210', '庆阳市', '62');
INSERT INTO `base_city` VALUES ('6211', '定西市', '62');
INSERT INTO `base_city` VALUES ('6212', '陇南市', '62');
INSERT INTO `base_city` VALUES ('6229', '临夏回族自治州', '62');
INSERT INTO `base_city` VALUES ('6230', '甘南藏族自治州', '62');
INSERT INTO `base_city` VALUES ('6301', '西宁市', '63');
INSERT INTO `base_city` VALUES ('6302', '海东市', '63');
INSERT INTO `base_city` VALUES ('6322', '海北藏族自治州', '63');
INSERT INTO `base_city` VALUES ('6323', '黄南藏族自治州', '63');
INSERT INTO `base_city` VALUES ('6325', '海南藏族自治州', '63');
INSERT INTO `base_city` VALUES ('6326', '果洛藏族自治州', '63');
INSERT INTO `base_city` VALUES ('6327', '玉树藏族自治州', '63');
INSERT INTO `base_city` VALUES ('6328', '海西蒙古族藏族自治州', '63');
INSERT INTO `base_city` VALUES ('6401', '银川市', '64');
INSERT INTO `base_city` VALUES ('6402', '石嘴山市', '64');
INSERT INTO `base_city` VALUES ('6403', '吴忠市', '64');
INSERT INTO `base_city` VALUES ('6404', '固原市', '64');
INSERT INTO `base_city` VALUES ('6405', '中卫市', '64');
INSERT INTO `base_city` VALUES ('6501', '乌鲁木齐市', '65');
INSERT INTO `base_city` VALUES ('6502', '克拉玛依市', '65');
INSERT INTO `base_city` VALUES ('6504', '吐鲁番市', '65');
INSERT INTO `base_city` VALUES ('6505', '哈密市', '65');
INSERT INTO `base_city` VALUES ('6523', '昌吉回族自治州', '65');
INSERT INTO `base_city` VALUES ('6527', '博尔塔拉蒙古自治州', '65');
INSERT INTO `base_city` VALUES ('6528', '巴音郭楞蒙古自治州', '65');
INSERT INTO `base_city` VALUES ('6529', '阿克苏地区', '65');
INSERT INTO `base_city` VALUES ('6530', '克孜勒苏柯尔克孜自治州', '65');
INSERT INTO `base_city` VALUES ('6531', '喀什地区', '65');
INSERT INTO `base_city` VALUES ('6532', '和田地区', '65');
INSERT INTO `base_city` VALUES ('6540', '伊犁哈萨克自治州', '65');
INSERT INTO `base_city` VALUES ('6542', '塔城地区', '65');
INSERT INTO `base_city` VALUES ('6543', '阿勒泰地区', '65');
INSERT INTO `base_city` VALUES ('6590', '自治区直辖县级行政区划', '65');

-- ----------------------------
-- Table structure for base_dict
-- ----------------------------
DROP TABLE IF EXISTS `base_dict`;
CREATE TABLE `base_dict`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '启用状态',
  `group_tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类标签',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  `version` int(8) NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_dict
-- ----------------------------
INSERT INTO `base_dict` VALUES (1422929378374828033, 'Sex', '性别', b'1', '基础属性', '性别', 0, '2021-08-04 22:36:15', 1399985191002447872, '2022-05-11 19:48:40', 0, 6);
INSERT INTO `base_dict` VALUES (1425744045414772737, 'MenuType', '菜单类型', b'1', '系统属性', '菜单类型', 0, '2021-08-12 17:00:44', 1399985191002447872, '2022-05-11 19:48:44', 0, 4);
INSERT INTO `base_dict` VALUES (1430063572491411456, 'loginType', '字典类型', b'1', NULL, '字典类型', 1399985191002447872, '2021-08-24 15:05:00', 1399985191002447872, '2021-08-24 15:05:00', 1, 2);
INSERT INTO `base_dict` VALUES (1435829999592759296, 'UserStatusCode', '用户状态码', b'1', '系统属性', '用户状态码', 1399985191002447872, '2021-09-09 12:58:43', 1399985191002447872, '2022-05-11 19:48:56', 0, 2);
INSERT INTO `base_dict` VALUES (1435838066191458304, 'LogBusinessType', '业务操作类型', b'1', '系统属性', '操作日志记录的业务操作类型', 1399985191002447872, '2021-09-09 13:30:46', 1399985191002447872, '2022-05-11 19:49:00', 0, 2);
INSERT INTO `base_dict` VALUES (1438078864509317120, 'MailSecurityCode', '邮箱安全方式编码', b'1', '消息服务', '邮箱安全方式编码', 1399985191002447872, '2021-09-15 17:54:54', 1399985191002447872, '2022-05-11 19:49:06', 0, 2);
INSERT INTO `base_dict` VALUES (1439961232651034624, 'MessageTemplateCode', '消息模板类型', b'1', '消息服务', '消息模板类型', 1399985191002447872, '2021-09-20 22:34:46', 1399985191002447872, '2022-05-11 19:48:34', 0, 1);
INSERT INTO `base_dict` VALUES (1452836604783845376, 'SocialType', '三方系统类型', b'1', '系统属性', '三方系统类型', 1399985191002447872, '2021-10-26 11:16:54', 1399985191002447872, '2022-05-11 19:48:28', 0, 3);
INSERT INTO `base_dict` VALUES (1452843488735621120, 'ParamType', '参数类型', b'1', '系统属性', '参数类型', 1399985191002447872, '2021-10-26 11:44:15', 1399985191002447872, '2022-05-11 19:48:21', 0, 2);
INSERT INTO `base_dict` VALUES (1496024933900169216, 'Political', '政治面貌', b'1', '基础数据', '政治面貌', 1399985191002447872, '2022-02-22 15:31:54', 1399985191002447872, '2022-05-11 19:48:04', 0, 1);
INSERT INTO `base_dict` VALUES (1496722894707728384, 'PayChannel', '支付通道', b'1', '支付服务', '支付宝, 微信, 云闪付等', 1399985191002447872, '2022-02-24 13:45:21', 1399985191002447872, '2022-05-11 19:47:51', 0, 1);
INSERT INTO `base_dict` VALUES (1496723207565058048, 'PayWay', '支付方式', b'1', '支付服务', '扫码支付、Wap、App支付等', 1399985191002447872, '2022-02-24 13:46:35', 1399985191002447872, '2022-05-11 19:47:46', 0, 1);
INSERT INTO `base_dict` VALUES (1497140849954185216, 'PayStatus', '支付状态', b'1', '支付服务', '支付中,成功,失败等', 1399985191002447872, '2022-02-25 17:26:09', 1399985191002447872, '2022-05-11 19:47:40', 0, 2);
INSERT INTO `base_dict` VALUES (1501031423232937984, 'AsyncPayChannel', '异步支付通道', b'1', '支付服务', '如微信支付宝云闪付等第三方支付', 1399985191002447872, '2022-03-08 11:05:54', 1399985191002447872, '2022-05-11 19:47:37', 0, 1);
INSERT INTO `base_dict` VALUES (1502276739978473472, 'WalletStatus', '钱包状态', b'1', '支付服务', '钱包状态', 1399985191002447872, '2022-03-11 21:34:20', 1399985191002447872, '2022-05-11 19:47:33', 0, 2);
INSERT INTO `base_dict` VALUES (1502624342339448832, 'WalletOperation', '钱包日志操作类型', b'1', NULL, '', 1399985191002447872, '2022-03-12 20:35:35', 1399985191002447872, '2022-03-12 20:35:35', 1, 0);
INSERT INTO `base_dict` VALUES (1502624515799085056, 'WalletLogType', '钱包日志类型', b'1', '支付服务', '钱包日志类型', 1399985191002447872, '2022-03-12 20:36:17', 1399985191002447872, '2022-05-11 19:47:29', 0, 1);
INSERT INTO `base_dict` VALUES (1502624632392347648, 'WalletLogOperation', '钱包日志操作类型', b'1', '支付服务', '钱包日志操作类型', 1399985191002447872, '2022-03-12 20:36:44', 1399985191002447872, '2022-05-11 19:47:21', 0, 1);
INSERT INTO `base_dict` VALUES (1503340128037212160, 'VoucherStatus', '储值卡状态', b'1', '支付服务', '储值卡状态', 1399985191002447872, '2022-03-14 19:59:52', 1399985191002447872, '2022-05-11 19:47:12', 0, 1);
INSERT INTO `base_dict` VALUES (1524356168611188736, 'input', '手工输入', b'1', '商品服务', '', 1399985191002447872, '2022-05-11 19:50:06', 1399985191002447872, '2022-05-11 19:50:06', 1, 0);
INSERT INTO `base_dict` VALUES (1524356376518643712, 'GoodsParamType', '参数类型', b'1', '商品服务', '列表/手动输入', 1399985191002447872, '2022-05-11 19:50:56', 1399985191002447872, '2022-05-14 23:05:41', 0, 1);
INSERT INTO `base_dict` VALUES (1546757092010078208, 'PayNotifyProcess', '支付回调处理状态', b'1', '支付服务', '成功/忽略/失败', 1399985191002447872, '2022-07-12 15:23:23', 1399985191002447872, '2022-07-12 15:23:53', 0, 1);
INSERT INTO `base_dict` VALUES (1556996322223968256, 'WeChatMediaType', '微信媒体类型', b'1', '微信', '微信媒体类型', 1399985191002447872, '2022-08-09 21:30:25', 1399985191002447872, '2022-08-09 21:30:26', 0, 0);
INSERT INTO `base_dict` VALUES (1561003021674987520, 'SiteMessageReceive', '消息接收类型', b'1', '站内信', '站内信接收类型', 1399985191002447872, '2022-08-20 22:51:37', 1399985191002447872, '2022-08-20 22:51:37', 0, 0);
INSERT INTO `base_dict` VALUES (1561003189111603200, 'SiteMessageState', '消息发布状态', b'1', '站内信', '站内信消息发布状态', 1399985191002447872, '2022-08-20 22:52:17', 1399985191002447872, '2022-08-20 22:52:17', 0, 0);
INSERT INTO `base_dict` VALUES (1562696107020230656, 'BpmModelPublish', '工作流模型发布状态', b'1', '工作流', '工作流模型发布状态', 1399985191002447872, '2022-08-25 14:59:20', 1399985191002447872, '2022-08-25 15:27:55', 0, 1);
INSERT INTO `base_dict` VALUES (1563083969989423104, 'BpmTaskAssignType', '工作流处理人分配类型', b'1', '工作流', '流程任务处理人分配类型', 1399985191002447872, '2022-08-26 16:40:34', 1399985191002447872, '2022-08-26 16:40:53', 0, 1);
INSERT INTO `base_dict` VALUES (1567091641298386944, 'BpmTaskState', '流程任务状态', b'1', '工作流', '流程任务状态', 1399985191002447872, '2022-09-06 18:05:37', 1399985191002447872, '2022-09-06 18:05:47', 0, 1);
INSERT INTO `base_dict` VALUES (1570343684024705024, 'BpmTaskResult', '流程任务处理结果', b'1', '工作流', '流程任务处理结果', 1399985191002447872, '2022-09-15 17:28:05', 1414143554414059520, '2022-10-19 23:13:40', 0, 1);
INSERT INTO `base_dict` VALUES (1570764395519111168, 'BpmInstanceState', '流程实例状态', b'1', '工作流', '流程实例状态', 1399985191002447872, '2022-09-16 21:19:50', 1414143554414059520, '2022-10-19 23:13:33', 0, 1);
INSERT INTO `base_dict` VALUES (1589527951317389312, 'DataScopePerm', '数据范围权限', b'1', '系统属性', '数据范围权限', 1414143554414059520, '2022-11-07 15:59:30', 1399985191002447872, '2022-12-09 22:09:25', 0, 3);
INSERT INTO `base_dict` VALUES (1633393287952257024, 'DatabaseType', '数据库类型', b'1', '开发', '数据库类型', 1414143554414059520, '2023-03-08 17:04:41', 1414143554414059520, '2023-03-08 17:04:41', 0, 0);

-- ----------------------------
-- Table structure for base_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `base_dict_item`;
CREATE TABLE `base_dict_item`  (
  `id` bigint(20) NOT NULL,
  `dict_id` bigint(20) NOT NULL COMMENT '字典id',
  `dict_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典code',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典项code',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典项名称',
  `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '启用状态',
  `sort_no` double(8, 2) NOT NULL COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  `version` int(8) NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dictionary_id`(`dict_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典项' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_dict_item
-- ----------------------------
INSERT INTO `base_dict_item` VALUES (1422931375807242241, 1422929378374828033, 'Sex', '1', '男', b'1', 0.00, '男性', 0, '2021-08-04 22:44:11', 0, '2021-08-04 22:44:11', 0, 2);
INSERT INTO `base_dict_item` VALUES (1425729455402401794, 1422929378374828033, 'Sex', '2', '女', b'1', 0.00, '女性', 0, '2021-08-12 16:02:46', 0, '2021-08-12 16:02:46', 0, 1);
INSERT INTO `base_dict_item` VALUES (1425744258544136194, 1425744045414772737, 'MenuType', '0', '顶级菜单', b'1', 0.00, '顶级菜单', 0, '2021-08-12 17:01:35', 0, '2021-08-12 17:01:35', 0, 0);
INSERT INTO `base_dict_item` VALUES (1425744436592340993, 1425744045414772737, 'MenuType', '1', '子菜单', b'1', 0.00, '子菜单', 0, '2021-08-12 17:02:17', 0, '2021-08-12 17:02:17', 0, 0);
INSERT INTO `base_dict_item` VALUES (1425744470582980610, 1425744045414772737, 'MenuType', '2', '按钮权限', b'1', 0.00, '按钮权限', 0, '2021-08-12 17:02:26', 0, '2021-08-12 17:02:26', 0, 0);
INSERT INTO `base_dict_item` VALUES (1430094707250413568, 1422929378374828033, 'Sex', '0', '未知', b'1', 0.00, '不确定性别', 1399985191002447872, '2021-08-24 17:08:43', 1399985191002447872, '2021-08-24 17:08:43', 0, 0);
INSERT INTO `base_dict_item` VALUES (1435830086406463488, 1435829999592759296, 'UserStatusCode', '1', '正常', b'1', 0.00, 'NORMAL', 1399985191002447872, '2021-09-09 12:59:04', 1399985191002447872, '2021-09-09 12:59:04', 0, 0);
INSERT INTO `base_dict_item` VALUES (1435830141855162368, 1435829999592759296, 'UserStatusCode', '2', '锁定', b'1', 0.00, 'LOCK, 多次登录失败被锁定', 1399985191002447872, '2021-09-09 12:59:17', 1399985191002447872, '2021-09-09 12:59:17', 0, 1);
INSERT INTO `base_dict_item` VALUES (1435830260503633920, 1435829999592759296, 'UserStatusCode', '3', '封禁', b'1', 0.00, 'BAN', 1399985191002447872, '2021-09-09 12:59:45', 1399985191002447872, '2021-09-09 12:59:45', 0, 0);
INSERT INTO `base_dict_item` VALUES (1435838374749626368, 1435838066191458304, 'LogBusinessType', 'other', '其它', b'1', 0.00, '', 1399985191002447872, '2021-09-09 13:32:00', 1399985191002447872, '2021-09-09 13:32:00', 0, 0);
INSERT INTO `base_dict_item` VALUES (1435838414436130816, 1435838066191458304, 'LogBusinessType', 'insert', '新增', b'1', 0.00, '', 1399985191002447872, '2021-09-09 13:32:09', 1399985191002447872, '2021-09-09 13:32:09', 0, 0);
INSERT INTO `base_dict_item` VALUES (1435838467624099840, 1435838066191458304, 'LogBusinessType', 'update', '修改', b'1', 0.00, '', 1399985191002447872, '2021-09-09 13:32:22', 1399985191002447872, '2021-09-09 13:32:22', 0, 0);
INSERT INTO `base_dict_item` VALUES (1435838502755590144, 1435838066191458304, 'LogBusinessType', 'delete', '删除', b'1', 0.00, '', 1399985191002447872, '2021-09-09 13:32:30', 1399985191002447872, '2021-09-09 13:32:30', 0, 0);
INSERT INTO `base_dict_item` VALUES (1435838546934194176, 1435838066191458304, 'LogBusinessType', 'grant', '授权', b'1', 0.00, '', 1399985191002447872, '2021-09-09 13:32:41', 1399985191002447872, '2021-09-09 13:32:41', 0, 0);
INSERT INTO `base_dict_item` VALUES (1435838605537009664, 1435838066191458304, 'LogBusinessType', 'export', '导出', b'1', 0.00, '', 1399985191002447872, '2021-09-09 13:32:55', 1399985191002447872, '2021-09-09 13:32:55', 0, 0);
INSERT INTO `base_dict_item` VALUES (1435838705457913856, 1435838066191458304, 'LogBusinessType', 'import', '导入', b'1', 0.00, '', 1399985191002447872, '2021-09-09 13:33:19', 1399985191002447872, '2021-09-09 13:33:19', 0, 0);
INSERT INTO `base_dict_item` VALUES (1435838745861644288, 1435838066191458304, 'LogBusinessType', 'force', '强退', b'1', 0.00, '', 1399985191002447872, '2021-09-09 13:33:28', 1399985191002447872, '2021-09-09 13:33:28', 0, 0);
INSERT INTO `base_dict_item` VALUES (1435838786273763328, 1435838066191458304, 'LogBusinessType', 'clean', '清空数据', b'1', 0.00, '', 1399985191002447872, '2021-09-09 13:33:38', 1399985191002447872, '2021-09-09 13:33:38', 0, 0);
INSERT INTO `base_dict_item` VALUES (1438079113630003200, 1438078864509317120, 'MailSecurityCode', '1', '普通方式', b'1', 0.00, 'SECURITY_TYPE_PLAIN', 1399985191002447872, '2021-09-15 17:55:54', 1399985191002447872, '2021-09-15 17:55:54', 0, 0);
INSERT INTO `base_dict_item` VALUES (1438080323061755904, 1438078864509317120, 'MailSecurityCode', '2', 'TLS方式', b'1', 0.00, 'SECURITY_TYPE_TLS', 1399985191002447872, '2021-09-15 18:00:42', 1399985191002447872, '2021-09-15 18:00:42', 0, 0);
INSERT INTO `base_dict_item` VALUES (1438080372231581696, 1438078864509317120, 'MailSecurityCode', '3', 'SSL方式', b'1', 0.00, 'SECURITY_TYPE_SSL', 1399985191002447872, '2021-09-15 18:00:54', 1399985191002447872, '2021-09-15 18:00:54', 0, 0);
INSERT INTO `base_dict_item` VALUES (1439961603914047488, 1439961232651034624, 'MessageTemplateCode', '5', '微信', b'1', -10.00, 'WECHAT', 1399985191002447872, '2021-09-20 22:36:14', 1399985191002447872, '2021-09-20 22:36:14', 0, 1);
INSERT INTO `base_dict_item` VALUES (1439961704321490944, 1439961232651034624, 'MessageTemplateCode', '4', 'Email', b'1', 0.00, 'EMAIL', 1399985191002447872, '2021-09-20 22:36:38', 1399985191002447872, '2021-09-20 22:36:38', 0, 0);
INSERT INTO `base_dict_item` VALUES (1439962132744478720, 1439961232651034624, 'MessageTemplateCode', '3', '短信', b'1', 0.00, 'SMS', 1399985191002447872, '2021-09-20 22:38:20', 1399985191002447872, '2021-09-20 22:38:20', 0, 0);
INSERT INTO `base_dict_item` VALUES (1439962205578567680, 1439961232651034624, 'MessageTemplateCode', '2', '钉钉机器人', b'1', 0.00, 'DING_TALK_ROBOT', 1399985191002447872, '2021-09-20 22:38:38', 1399985191002447872, '2021-09-20 22:38:38', 0, 0);
INSERT INTO `base_dict_item` VALUES (1439962267511660544, 1439961232651034624, 'MessageTemplateCode', '1', '钉钉', b'1', 0.00, 'DING_TALK', 1399985191002447872, '2021-09-20 22:38:52', 1399985191002447872, '2021-09-20 22:38:52', 0, 0);
INSERT INTO `base_dict_item` VALUES (1452836696873984000, 1452836604783845376, 'SocialType', 'WeChat', '微信', b'1', 0.00, '', 1399985191002447872, '2021-10-26 11:17:16', 1399985191002447872, '2021-10-26 11:17:16', 0, 0);
INSERT INTO `base_dict_item` VALUES (1452837435482529792, 1452836604783845376, 'SocialType', 'QQ', 'QQ', b'1', 0.00, '', 1399985191002447872, '2021-10-26 11:20:12', 1399985191002447872, '2021-10-26 11:20:12', 0, 0);
INSERT INTO `base_dict_item` VALUES (1452837523030237184, 1452836604783845376, 'SocialType', 'DingTalk', '钉钉', b'1', 0.00, '', 1399985191002447872, '2021-10-26 11:20:33', 1399985191002447872, '2021-10-26 11:20:33', 0, 0);
INSERT INTO `base_dict_item` VALUES (1452844537911406592, 1452843488735621120, 'ParamType', '1', '系统参数', b'1', 0.00, '', 1399985191002447872, '2021-10-26 11:48:25', 1399985191002447872, '2021-10-26 11:48:25', 0, 0);
INSERT INTO `base_dict_item` VALUES (1452844565031776256, 1452843488735621120, 'ParamType', '2', '用户参数', b'1', 0.00, '', 1399985191002447872, '2021-10-26 11:48:32', 1399985191002447872, '2021-10-26 11:48:32', 0, 2);
INSERT INTO `base_dict_item` VALUES (1496026946344005632, 1496024933900169216, 'Political', '1', '中共党员', b'1', 1.00, '', 1399985191002447872, '2022-02-22 15:39:54', 1399985191002447872, '2022-02-22 15:39:54', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027004560945152, 1496024933900169216, 'Political', '2', '中共预备党员', b'1', 2.00, '', 1399985191002447872, '2022-02-22 15:40:07', 1399985191002447872, '2022-02-22 15:40:07', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027039264616448, 1496024933900169216, 'Political', '3', '共青团员', b'1', 3.00, '', 1399985191002447872, '2022-02-22 15:40:16', 1399985191002447872, '2022-02-22 15:40:16', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027077550223360, 1496024933900169216, 'Political', '4', '民革党员', b'1', 4.00, '', 1399985191002447872, '2022-02-22 15:40:25', 1399985191002447872, '2022-02-22 15:40:25', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027123461074944, 1496024933900169216, 'Political', '5', '民盟盟员', b'1', 5.00, '', 1399985191002447872, '2022-02-22 15:40:36', 1399985191002447872, '2022-02-22 15:40:36', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027197566038016, 1496024933900169216, 'Political', '6', '民建会员', b'1', 6.00, '', 1399985191002447872, '2022-02-22 15:40:53', 1399985191002447872, '2022-02-22 15:40:53', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027234803068928, 1496024933900169216, 'Political', '7', '民进会员', b'1', 7.00, '', 1399985191002447872, '2022-02-22 15:41:02', 1399985191002447872, '2022-02-22 15:41:02', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027272941875200, 1496024933900169216, 'Political', '8', '农工党党员', b'1', 8.00, '', 1399985191002447872, '2022-02-22 15:41:11', 1399985191002447872, '2022-02-22 15:41:11', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027306634719232, 1496024933900169216, 'Political', '9', '致公党党员', b'1', 9.00, '', 1399985191002447872, '2022-02-22 15:41:19', 1399985191002447872, '2022-02-22 15:41:19', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027369796743168, 1496024933900169216, 'Political', '10', '九三学社社员', b'1', 10.00, '', 1399985191002447872, '2022-02-22 15:41:34', 1399985191002447872, '2022-02-22 15:41:35', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027408141070336, 1496024933900169216, 'Political', '11', '台盟盟员', b'1', 11.00, '', 1399985191002447872, '2022-02-22 15:41:44', 1399985191002447872, '2022-02-22 15:41:44', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027456849522688, 1496024933900169216, 'Political', '12', '无党派人士', b'1', 12.00, '', 1399985191002447872, '2022-02-22 15:41:55', 1399985191002447872, '2022-02-22 15:41:55', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496027516639326208, 1496024933900169216, 'Political', '13', '群众', b'1', 13.00, '', 1399985191002447872, '2022-02-22 15:42:09', 1399985191002447872, '2022-02-22 15:42:10', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496780500696539136, 1496722894707728384, 'PayChannel', '1', '支付宝', b'1', 1.00, '', 1399985191002447872, '2022-02-24 17:34:15', 1399985191002447872, '2022-03-08 11:02:59', 0, 3);
INSERT INTO `base_dict_item` VALUES (1496780576818962432, 1496722894707728384, 'PayChannel', '2', '微信', b'1', 2.00, '', 1399985191002447872, '2022-02-24 17:34:33', 1399985191002447872, '2022-03-08 11:04:00', 0, 2);
INSERT INTO `base_dict_item` VALUES (1496780712492113920, 1496723207565058048, 'PayWay', '1', 'wap支付', b'1', 0.00, '', 1399985191002447872, '2022-02-24 17:35:05', 1399985191002447872, '2022-02-24 17:35:05', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496780757647990784, 1496723207565058048, 'PayWay', '2', '应用支付', b'1', 0.00, '', 1399985191002447872, '2022-02-24 17:35:16', 1399985191002447872, '2022-02-24 17:35:16', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496780799691694080, 1496723207565058048, 'PayWay', '3', 'web支付', b'1', 0.00, '', 1399985191002447872, '2022-02-24 17:35:26', 1399985191002447872, '2022-02-24 17:35:26', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496780838451257344, 1496723207565058048, 'PayWay', '4', '二维码扫码支付', b'1', 0.00, '', 1399985191002447872, '2022-02-24 17:35:35', 1399985191002447872, '2022-02-24 17:35:35', 0, 0);
INSERT INTO `base_dict_item` VALUES (1496780876388737024, 1496723207565058048, 'PayWay', '5', '付款码支付', b'1', 0.00, '', 1399985191002447872, '2022-02-24 17:35:44', 1399985191002447872, '2022-02-24 17:35:44', 0, 0);
INSERT INTO `base_dict_item` VALUES (1497141630803566592, 1497140849954185216, 'PayStatus', '3', '支付取消', b'1', 0.00, '', 1399985191002447872, '2022-02-25 17:29:15', 1399985191002447872, '2022-02-25 17:29:15', 0, 0);
INSERT INTO `base_dict_item` VALUES (1497141652379066368, 1497140849954185216, 'PayStatus', '2', '失败', b'1', 0.00, '', 1399985191002447872, '2022-02-25 17:29:20', 1399985191002447872, '2022-02-25 17:29:20', 0, 0);
INSERT INTO `base_dict_item` VALUES (1497141681915355136, 1497140849954185216, 'PayStatus', '1', '成功', b'1', 0.00, '', 1399985191002447872, '2022-02-25 17:29:27', 1399985191002447872, '2022-02-25 17:29:27', 0, 0);
INSERT INTO `base_dict_item` VALUES (1497141712743489536, 1497140849954185216, 'PayStatus', '0', '支付中', b'1', 0.00, '', 1399985191002447872, '2022-02-25 17:29:35', 1399985191002447872, '2022-02-25 17:29:35', 0, 0);
INSERT INTO `base_dict_item` VALUES (1497506810439892992, 1497140849954185216, 'PayStatus', '4', '部分退款', b'1', 1.00, '部分退款', 1399985191002447872, '2022-02-26 17:40:21', 1399985191002447872, '2022-03-04 21:22:46', 0, 7);
INSERT INTO `base_dict_item` VALUES (1499367587857694720, 1497140849954185216, 'PayStatus', '5', '已退款', b'1', 2.00, '完全退款', 1399985191002447872, '2022-03-03 20:54:25', 1399985191002447872, '2022-03-04 21:22:49', 0, 3);
INSERT INTO `base_dict_item` VALUES (1501030031432847360, 1496722894707728384, 'PayChannel', '3', '云闪付', b'1', 3.00, '', 1399985191002447872, '2022-03-08 11:00:22', 1399985191002447872, '2022-03-08 11:04:07', 0, 2);
INSERT INTO `base_dict_item` VALUES (1501030073489133568, 1496722894707728384, 'PayChannel', '4', '现金', b'1', 4.00, '', 1399985191002447872, '2022-03-08 11:00:32', 1399985191002447872, '2022-03-08 11:04:10', 0, 2);
INSERT INTO `base_dict_item` VALUES (1501030108314439680, 1496722894707728384, 'PayChannel', '5', '钱包', b'1', 5.00, '', 1399985191002447872, '2022-03-08 11:00:40', 1399985191002447872, '2022-03-08 11:04:14', 0, 2);
INSERT INTO `base_dict_item` VALUES (1501031490513768448, 1501031423232937984, 'AsyncPayChannel', '3', '云闪付', b'1', 0.00, '', 1399985191002447872, '2022-03-08 11:06:10', 1399985191002447872, '2022-03-08 11:06:10', 0, 0);
INSERT INTO `base_dict_item` VALUES (1501031518208757760, 1501031423232937984, 'AsyncPayChannel', '2', '微信', b'1', 0.00, '', 1399985191002447872, '2022-03-08 11:06:16', 1399985191002447872, '2022-03-08 11:06:16', 0, 0);
INSERT INTO `base_dict_item` VALUES (1501031544360243200, 1501031423232937984, 'AsyncPayChannel', '1', '支付宝', b'1', 0.00, '', 1399985191002447872, '2022-03-08 11:06:23', 1399985191002447872, '2022-03-08 11:06:23', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502276841057005568, 1502276739978473472, 'WalletStatus', '2', '禁用', b'1', 0.00, '', 1399985191002447872, '2022-03-11 21:34:45', 1399985191002447872, '2022-03-11 21:34:45', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502276862108217344, 1502276739978473472, 'WalletStatus', '1', '正常', b'1', 0.00, '', 1399985191002447872, '2022-03-11 21:34:50', 1399985191002447872, '2022-03-11 21:34:50', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502624716257456128, 1502624515799085056, 'WalletLogType', '1', '开通', b'1', 0.00, '', 1399985191002447872, '2022-03-12 20:37:04', 1399985191002447872, '2022-03-12 20:37:04', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502624931978899456, 1502624515799085056, 'WalletLogType', '2', '主动充值', b'1', 0.00, '', 1399985191002447872, '2022-03-12 20:37:56', 1399985191002447872, '2022-03-12 20:37:56', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502624956209393664, 1502624515799085056, 'WalletLogType', '3', '自动充值', b'1', 0.00, '', 1399985191002447872, '2022-03-12 20:38:02', 1399985191002447872, '2022-03-12 20:38:02', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502625014719934464, 1502624515799085056, 'WalletLogType', '4', '余额变动', b'1', 0.00, '', 1399985191002447872, '2022-03-12 20:38:16', 1399985191002447872, '2022-03-12 20:38:16', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502625053097816064, 1502624515799085056, 'WalletLogType', '5', '支付', b'1', 0.00, '', 1399985191002447872, '2022-03-12 20:38:25', 1399985191002447872, '2022-03-12 20:38:25', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502625091639275520, 1502624515799085056, 'WalletLogType', '6', '系统扣除余额', b'1', 0.00, '', 1399985191002447872, '2022-03-12 20:38:34', 1399985191002447872, '2022-03-12 20:38:34', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502625123725701120, 1502624515799085056, 'WalletLogType', '7', '退款', b'1', 0.00, '', 1399985191002447872, '2022-03-12 20:38:42', 1399985191002447872, '2022-03-12 20:38:42', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502625783145787392, 1502624632392347648, 'WalletLogOperation', '1', '系统操作', b'1', 0.00, '', 1399985191002447872, '2022-03-12 20:41:19', 1399985191002447872, '2022-03-12 20:41:19', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502625814837948416, 1502624632392347648, 'WalletLogOperation', '2', '管理员操作', b'1', 0.00, '', 1399985191002447872, '2022-03-12 20:41:26', 1399985191002447872, '2022-03-12 20:41:26', 0, 0);
INSERT INTO `base_dict_item` VALUES (1502625850355314688, 1502624632392347648, 'WalletLogOperation', '3', '用户操作', b'1', 0.00, '', 1399985191002447872, '2022-03-12 20:41:35', 1399985191002447872, '2022-03-12 20:41:35', 0, 0);
INSERT INTO `base_dict_item` VALUES (1503340241493135360, 1503340128037212160, 'VoucherStatus', '1', '启用', b'1', 0.00, '', 1399985191002447872, '2022-03-14 20:00:19', 1399985191002447872, '2022-03-14 20:00:19', 0, 0);
INSERT INTO `base_dict_item` VALUES (1503340326645895168, 1503340128037212160, 'VoucherStatus', '2', '停用', b'1', 0.00, '', 1399985191002447872, '2022-03-14 20:00:39', 1399985191002447872, '2022-03-14 20:00:39', 0, 0);
INSERT INTO `base_dict_item` VALUES (1505112357976612864, 1496722894707728384, 'PayChannel', '6', '储值卡', b'1', 0.00, '', 1399985191002447872, '2022-03-19 17:22:04', 1399985191002447872, '2022-03-19 17:22:04', 0, 0);
INSERT INTO `base_dict_item` VALUES (1524356452720758784, 1524356376518643712, 'GoodsParamType', 'input', '手工录入', b'1', 0.00, '', 1399985191002447872, '2022-05-11 19:51:14', 1399985191002447872, '2022-05-11 19:51:14', 0, 0);
INSERT INTO `base_dict_item` VALUES (1524356510157557760, 1524356376518643712, 'GoodsParamType', 'select', '列表选择', b'1', 0.00, '', 1399985191002447872, '2022-05-11 19:51:28', 1399985191002447872, '2022-05-11 19:51:28', 0, 0);
INSERT INTO `base_dict_item` VALUES (1546757293592522752, 1546757092010078208, 'PayNotifyProcess', '0', '失败', b'1', 0.00, '', 1399985191002447872, '2022-07-12 15:24:11', 1399985191002447872, '2022-07-12 15:24:11', 0, 0);
INSERT INTO `base_dict_item` VALUES (1546757327901929472, 1546757092010078208, 'PayNotifyProcess', '1', '成功', b'1', -1.00, '', 1399985191002447872, '2022-07-12 15:24:19', 1399985191002447872, '2022-07-12 15:31:38', 0, 2);
INSERT INTO `base_dict_item` VALUES (1546757375637303296, 1546757092010078208, 'PayNotifyProcess', '2', '忽略', b'1', 0.00, '', 1399985191002447872, '2022-07-12 15:24:30', 1399985191002447872, '2022-07-12 15:24:30', 0, 0);
INSERT INTO `base_dict_item` VALUES (1556996422006460416, 1556996322223968256, 'WeChatMediaType', 'news', '新闻', b'1', 0.00, '', 1399985191002447872, '2022-08-09 21:30:49', 1399985191002447872, '2022-08-09 21:30:49', 1, 0);
INSERT INTO `base_dict_item` VALUES (1556996472661069824, 1556996322223968256, 'WeChatMediaType', 'voice', '语音', b'1', 0.00, '', 1399985191002447872, '2022-08-09 21:31:01', 1399985191002447872, '2022-08-09 21:31:01', 0, 0);
INSERT INTO `base_dict_item` VALUES (1556996501417218048, 1556996322223968256, 'WeChatMediaType', 'image', '图片', b'1', 0.00, '', 1399985191002447872, '2022-08-09 21:31:08', 1399985191002447872, '2022-08-09 21:31:08', 0, 0);
INSERT INTO `base_dict_item` VALUES (1556996529565192192, 1556996322223968256, 'WeChatMediaType', 'video', '视频', b'1', 0.00, '', 1399985191002447872, '2022-08-09 21:31:15', 1399985191002447872, '2022-08-09 21:31:15', 0, 0);
INSERT INTO `base_dict_item` VALUES (1561003235710320640, 1561003189111603200, 'SiteMessageState', 'user', '指定用户', b'1', 0.00, '', 1399985191002447872, '2022-08-20 22:52:28', 1399985191002447872, '2022-08-20 22:52:28', 1, 0);
INSERT INTO `base_dict_item` VALUES (1561003279322693632, 1561003189111603200, 'SiteMessageState', 'all', '全部用户', b'1', 0.00, '', 1399985191002447872, '2022-08-20 22:52:38', 1399985191002447872, '2022-08-20 22:52:39', 1, 0);
INSERT INTO `base_dict_item` VALUES (1561003368762032128, 1561003021674987520, 'SiteMessageReceive', 'user', '指定用户', b'1', 0.00, '', 1399985191002447872, '2022-08-20 22:53:00', 1399985191002447872, '2022-08-20 22:53:00', 0, 0);
INSERT INTO `base_dict_item` VALUES (1561003399778910208, 1561003021674987520, 'SiteMessageReceive', 'all', '全部用户', b'1', 0.00, '', 1399985191002447872, '2022-08-20 22:53:07', 1399985191002447872, '2022-08-20 22:53:24', 0, 1);
INSERT INTO `base_dict_item` VALUES (1561003539772194816, 1561003189111603200, 'SiteMessageState', 'sent', '已发送', b'1', 0.00, '', 1399985191002447872, '2022-08-20 22:53:41', 1399985191002447872, '2022-08-20 22:53:41', 0, 0);
INSERT INTO `base_dict_item` VALUES (1561003575608328192, 1561003189111603200, 'SiteMessageState', 'cancel', '撤销', b'1', 0.00, '', 1399985191002447872, '2022-08-20 22:53:49', 1399985191002447872, '2022-08-20 22:53:49', 0, 0);
INSERT INTO `base_dict_item` VALUES (1561245469535080448, 1561003189111603200, 'SiteMessageState', 'draft', '草稿', b'1', 0.00, '', 1399985191002447872, '2022-08-21 14:55:01', 1399985191002447872, '2022-08-21 14:55:01', 0, 0);
INSERT INTO `base_dict_item` VALUES (1562696390043475968, 1562696107020230656, 'BpmModelPublish', 'published', '已发布', b'1', 0.00, '', 1399985191002447872, '2022-08-25 15:00:28', 1399985191002447872, '2022-08-25 15:00:28', 0, 0);
INSERT INTO `base_dict_item` VALUES (1562696420561231872, 1562696107020230656, 'BpmModelPublish', 'unpublished', '未发布', b'1', 0.00, '未上传xml文档', 1399985191002447872, '2022-08-25 15:00:35', 1399985191002447872, '2022-08-25 15:28:09', 0, 1);
INSERT INTO `base_dict_item` VALUES (1562703450588028928, 1562696107020230656, 'BpmModelPublish', 'unpublishedXml', '未发布(已上传BPMN)', b'1', 0.00, '有xml文档', 1399985191002447872, '2022-08-25 15:28:31', 1399985191002447872, '2022-08-25 15:34:45', 0, 1);
INSERT INTO `base_dict_item` VALUES (1563087300157747200, 1563083969989423104, 'BpmTaskAssignType', 'user', '用户', b'1', 0.00, '', 1399985191002447872, '2022-08-26 16:53:48', 1399985191002447872, '2022-09-06 22:50:15', 0, 1);
INSERT INTO `base_dict_item` VALUES (1567091825981980672, 1567091641298386944, 'BpmTaskState', 'running', '处理中', b'1', 0.00, '', 1399985191002447872, '2022-09-06 18:06:21', 1399985191002447872, '2022-09-06 18:06:21', 0, 0);
INSERT INTO `base_dict_item` VALUES (1567091863017684992, 1567091641298386944, 'BpmTaskState', 'pass', '通过', b'1', 0.00, '', 1399985191002447872, '2022-09-06 18:06:30', 1399985191002447872, '2022-09-06 18:06:30', 0, 0);
INSERT INTO `base_dict_item` VALUES (1567091902414782464, 1567091641298386944, 'BpmTaskState', 'reject', '驳回', b'1', 0.00, '', 1399985191002447872, '2022-09-06 18:06:39', 1399985191002447872, '2022-09-06 18:06:51', 0, 1);
INSERT INTO `base_dict_item` VALUES (1567091993569591296, 1567091641298386944, 'BpmTaskState', 'back', '退回', b'1', 0.00, '', 1399985191002447872, '2022-09-06 18:07:01', 1399985191002447872, '2022-09-06 18:07:01', 0, 0);
INSERT INTO `base_dict_item` VALUES (1567092037261656064, 1567091641298386944, 'BpmTaskState', 'retrieve', '取回', b'1', 0.00, '', 1399985191002447872, '2022-09-06 18:07:12', 1399985191002447872, '2022-09-06 18:07:22', 0, 1);
INSERT INTO `base_dict_item` VALUES (1567092124226355200, 1567091641298386944, 'BpmTaskState', 'skip', '跳过', b'1', 0.00, '', 1399985191002447872, '2022-09-06 18:07:32', 1399985191002447872, '2022-09-06 18:07:32', 0, 0);
INSERT INTO `base_dict_item` VALUES (1567163310103564288, 1563083969989423104, 'BpmTaskAssignType', 'userGroup', '用户组', b'1', 0.00, '', 1399985191002447872, '2022-09-06 22:50:24', 1399985191002447872, '2022-09-06 22:50:24', 0, 0);
INSERT INTO `base_dict_item` VALUES (1567163343288897536, 1563083969989423104, 'BpmTaskAssignType', 'role', '角色', b'1', 0.00, '', 1399985191002447872, '2022-09-06 22:50:32', 1399985191002447872, '2022-09-06 22:50:32', 0, 0);
INSERT INTO `base_dict_item` VALUES (1567163380693700608, 1563083969989423104, 'BpmTaskAssignType', 'deptMember', '部门成员', b'1', 0.00, '', 1399985191002447872, '2022-09-06 22:50:41', 1399985191002447872, '2022-09-06 22:50:41', 0, 0);
INSERT INTO `base_dict_item` VALUES (1567163412960481280, 1563083969989423104, 'BpmTaskAssignType', 'deptLeader', '部门的负责人', b'1', 0.00, '', 1399985191002447872, '2022-09-06 22:50:49', 1399985191002447872, '2022-09-06 22:50:49', 0, 0);
INSERT INTO `base_dict_item` VALUES (1567175558888923136, 1563083969989423104, 'BpmTaskAssignType', 'roleGroup', '角色组', b'1', 0.00, '', 1399985191002447872, '2022-09-06 23:39:05', 1399985191002447872, '2022-09-06 23:39:05', 0, 0);
INSERT INTO `base_dict_item` VALUES (1567178994242002944, 1563083969989423104, 'BpmTaskAssignType', 'sponsor', '发起人', b'1', 0.00, '', 1399985191002447872, '2022-09-06 23:52:44', 1399985191002447872, '2022-09-06 23:52:44', 0, 0);
INSERT INTO `base_dict_item` VALUES (1567179143576002560, 1563083969989423104, 'BpmTaskAssignType', 'select', '用户手动选择', b'1', 0.00, '', 1399985191002447872, '2022-09-06 23:53:19', 1399985191002447872, '2022-09-07 00:01:22', 0, 1);
INSERT INTO `base_dict_item` VALUES (1570343731634249728, 1570343684024705024, 'BpmTaskResult', 'pass', '通过', b'1', 0.00, '', 1399985191002447872, '2022-09-15 17:28:16', 1399985191002447872, '2022-09-15 17:28:16', 0, 0);
INSERT INTO `base_dict_item` VALUES (1570343761636106240, 1570343684024705024, 'BpmTaskResult', 'notPass', '不通过', b'1', 0.00, '', 1399985191002447872, '2022-09-15 17:28:23', 1399985191002447872, '2022-09-15 17:28:23', 0, 0);
INSERT INTO `base_dict_item` VALUES (1570343788056027136, 1570343684024705024, 'BpmTaskResult', 'abstain', '弃权', b'1', 0.00, '', 1399985191002447872, '2022-09-15 17:28:29', 1399985191002447872, '2022-09-15 17:28:29', 0, 0);
INSERT INTO `base_dict_item` VALUES (1570343826018672640, 1570343684024705024, 'BpmTaskResult', 'reject', '驳回', b'1', 0.00, '', 1399985191002447872, '2022-09-15 17:28:38', 1399985191002447872, '2022-09-15 17:28:38', 0, 0);
INSERT INTO `base_dict_item` VALUES (1570343873737269248, 1570343684024705024, 'BpmTaskResult', 'back', '退回', b'1', 0.00, '', 1399985191002447872, '2022-09-15 17:28:50', 1399985191002447872, '2022-09-15 17:28:50', 0, 0);
INSERT INTO `base_dict_item` VALUES (1570343913918701568, 1570343684024705024, 'BpmTaskResult', 'retrieve', '取回', b'1', 0.00, '', 1399985191002447872, '2022-09-15 17:28:59', 1399985191002447872, '2022-09-15 17:28:59', 0, 0);
INSERT INTO `base_dict_item` VALUES (1570685888076120064, 1570343684024705024, 'BpmTaskResult', 'autoFinish', '自动完成', b'1', 0.00, '', 1399985191002447872, '2022-09-16 16:07:52', 1399985191002447872, '2022-09-16 16:08:02', 0, 1);
INSERT INTO `base_dict_item` VALUES (1570764765255397376, 1570764395519111168, 'BpmInstanceState', 'running', '运行中', b'1', 0.00, '', 1399985191002447872, '2022-09-16 21:21:18', 1399985191002447872, '2022-12-09 22:15:46', 0, 2);
INSERT INTO `base_dict_item` VALUES (1570764802047832064, 1570764395519111168, 'BpmInstanceState', 'finish', '已完成', b'1', 0.00, '', 1399985191002447872, '2022-09-16 21:21:27', 1399985191002447872, '2022-09-16 21:21:27', 0, 0);
INSERT INTO `base_dict_item` VALUES (1570764836319490048, 1570764395519111168, 'BpmInstanceState', 'cancel', '取消', b'1', 0.00, '', 1399985191002447872, '2022-09-16 21:21:35', 1399985191002447872, '2022-09-16 21:21:35', 0, 0);
INSERT INTO `base_dict_item` VALUES (1570784215744585728, 1570343684024705024, 'BpmTaskResult', 'cancel', '取消', b'1', 0.00, '', 1399985191002447872, '2022-09-16 22:38:35', 1399985191002447872, '2022-09-16 22:38:35', 0, 0);
INSERT INTO `base_dict_item` VALUES (1570784331511570432, 1567091641298386944, 'BpmTaskState', 'cancel', '取消', b'1', 0.00, '', 1399985191002447872, '2022-09-16 22:39:03', 1399985191002447872, '2022-09-16 22:39:03', 0, 0);
INSERT INTO `base_dict_item` VALUES (1573665422392098816, 1439961232651034624, 'MessageTemplateCode', '0', '站内信', b'1', -11.00, 'SITE', 1399985191002447872, '2022-09-24 21:27:29', 1399985191002447872, '2022-09-24 21:27:39', 0, 1);
INSERT INTO `base_dict_item` VALUES (1589528254477488128, 1589527951317389312, 'DataScopePerm', '7', '所在及下级部门', b'1', 0.00, '', 1414143554414059520, '2022-11-07 16:00:43', 1414143554414059520, '2022-11-07 16:00:43', 0, 0);
INSERT INTO `base_dict_item` VALUES (1589528283539820544, 1589527951317389312, 'DataScopePerm', '6', '所在部门', b'1', 0.00, '', 1414143554414059520, '2022-11-07 16:00:49', 1414143554414059520, '2022-11-07 16:00:49', 0, 0);
INSERT INTO `base_dict_item` VALUES (1589528315672383488, 1589527951317389312, 'DataScopePerm', '5', '全部数据', b'1', 0.00, '', 1414143554414059520, '2022-11-07 16:00:57', 1414143554414059520, '2022-11-07 16:00:57', 0, 0);
INSERT INTO `base_dict_item` VALUES (1589528340267782144, 1589527951317389312, 'DataScopePerm', '4', '部门和用户范围', b'1', 0.00, '', 1414143554414059520, '2022-11-07 16:01:03', 1414143554414059520, '2022-11-07 16:01:03', 0, 0);
INSERT INTO `base_dict_item` VALUES (1589528367228768256, 1589527951317389312, 'DataScopePerm', '3', '部门范围', b'1', 0.00, '', 1414143554414059520, '2022-11-07 16:01:09', 1414143554414059520, '2022-11-07 16:01:09', 0, 0);
INSERT INTO `base_dict_item` VALUES (1589528393292173312, 1589527951317389312, 'DataScopePerm', '2', '用户范围', b'1', 0.00, '', 1414143554414059520, '2022-11-07 16:01:16', 1414143554414059520, '2022-11-07 16:01:16', 0, 0);
INSERT INTO `base_dict_item` VALUES (1589528423956729856, 1589527951317389312, 'DataScopePerm', '1', '自身数据', b'1', 0.00, '', 1414143554414059520, '2022-11-07 16:01:23', 1414143554414059520, '2022-11-07 16:01:23', 0, 0);
INSERT INTO `base_dict_item` VALUES (1633403429028536320, 1633393287952257024, 'DatabaseType', 'mysql', 'MySQL', b'1', 1.00, '', 1414143554414059520, '2023-03-08 17:44:59', 1414143554414059520, '2023-03-08 17:44:59', 0, 0);
INSERT INTO `base_dict_item` VALUES (1633403459470794752, 1633393287952257024, 'DatabaseType', 'oracle', 'Oracle', b'1', 2.00, '', 1414143554414059520, '2023-03-08 17:45:07', 1414143554414059520, '2023-03-08 17:45:07', 0, 0);
INSERT INTO `base_dict_item` VALUES (1633403498695925760, 1633393287952257024, 'DatabaseType', 'mssql', 'SQLServer', b'1', 3.00, '', 1414143554414059520, '2023-03-08 17:45:16', 1414143554414059520, '2023-03-08 17:45:16', 0, 0);

-- ----------------------------
-- Table structure for base_dynamic_data_source
-- ----------------------------
DROP TABLE IF EXISTS `base_dynamic_data_source`;
CREATE TABLE `base_dynamic_data_source`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据源编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据源名称',
  `database_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据库类型',
  `db_driver` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '驱动类',
  `db_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据库地址',
  `db_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据库名称',
  `db_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
  `db_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '动态数据源管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_dynamic_data_source
-- ----------------------------

-- ----------------------------
-- Table structure for base_dynamic_form
-- ----------------------------
DROP TABLE IF EXISTS `base_dynamic_form`;
CREATE TABLE `base_dynamic_form`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单键名',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单内容',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  `version` int(8) NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '动态表单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_dynamic_form
-- ----------------------------
INSERT INTO `base_dynamic_form` VALUES (1552656018381422592, '测试表单', 'test', '{\"list\":[{\"type\":\"input\",\"label\":\"输入框\",\"options\":{\"type\":\"text\",\"width\":\"100%\",\"defaultValue\":\"\",\"placeholder\":\"请输入\",\"clearable\":true,\"maxLength\":null,\"addonBefore\":\"\",\"addonAfter\":\"\",\"hidden\":false,\"disabled\":false},\"model\":\"aa\",\"key\":\"input_1659059676533\",\"help\":\"测试\",\"rules\":[{\"required\":true,\"message\":\"必填项\"}]},{\"type\":\"textarea\",\"label\":\"文本框\",\"options\":{\"width\":\"100%\",\"minRows\":4,\"maxRows\":6,\"maxLength\":null,\"defaultValue\":\"\",\"clearable\":true,\"hidden\":false,\"disabled\":false,\"placeholder\":\"请输入\"},\"model\":\"bb\",\"key\":\"textarea_1659020414125\",\"help\":\"\",\"rules\":[{\"required\":true,\"message\":\"必填项\"}]},{\"type\":\"slider\",\"label\":\"滑动输入条\",\"options\":{\"width\":\"100%\",\"defaultValue\":34,\"disabled\":false,\"hidden\":false,\"min\":0,\"max\":100,\"step\":1,\"showInput\":false},\"model\":\"cc\",\"key\":\"slider_1659020433092\",\"help\":\"\",\"rules\":[{\"required\":false,\"message\":\"必填项\"}]}],\"config\":{\"layout\":\"horizontal\",\"labelCol\":{\"xs\":4,\"sm\":4,\"md\":4,\"lg\":4,\"xl\":4,\"xxl\":4},\"labelWidth\":100,\"labelLayout\":\"flex\",\"wrapperCol\":{\"xs\":18,\"sm\":18,\"md\":18,\"lg\":18,\"xl\":18,\"xxl\":18},\"hideRequiredMark\":false,\"customStyle\":\"\"}}', '测试动态表单', 1399985191002447872, '2022-07-28 22:03:36', 1399985191002447872, '2022-07-29 09:55:22', 0, 7);
INSERT INTO `base_dynamic_form` VALUES (1552656018381422593, '测试表单1', 'test1', '{\"list\":[{\"type\":\"input\",\"label\":\"申请人\",\"options\":{\"type\":\"text\",\"width\":\"100%\",\"defaultValue\":\"\",\"placeholder\":\"请输入\",\"clearable\":true,\"maxLength\":null,\"addonBefore\":\"\",\"addonAfter\":\"\",\"hidden\":false,\"disabled\":false},\"model\":\"apply_by\",\"key\":\"input_1659059676533\",\"help\":\"测试\",\"rules\":[{\"required\":true,\"message\":\"必填项\"}]},{\"type\":\"input\",\"label\":\"请假天数\",\"options\":{\"type\":\"text\",\"width\":\"100%\",\"defaultValue\":\"\",\"placeholder\":\"请输入\",\"clearable\":false,\"maxLength\":null,\"addonBefore\":\"\",\"addonAfter\":\"\",\"hidden\":false,\"disabled\":false},\"model\":\"leave_days\",\"key\":\"input_1662106166142\",\"help\":\"\",\"rules\":[{\"required\":false,\"message\":\"必填项\"}]},{\"type\":\"textarea\",\"label\":\"备注\",\"options\":{\"width\":\"100%\",\"minRows\":4,\"maxRows\":6,\"maxLength\":null,\"defaultValue\":\"\",\"clearable\":true,\"hidden\":false,\"disabled\":false,\"placeholder\":\"请输入\"},\"model\":\"remark\",\"key\":\"textarea_1659020414125\",\"help\":\"\",\"rules\":[{\"required\":true,\"message\":\"必填项\"}]},{\"type\":\"switch\",\"label\":\"开关\",\"options\":{\"defaultValue\":false,\"hidden\":false,\"disabled\":false},\"model\":\"switch_1662108221389\",\"key\":\"switch_1662108221389\",\"help\":\"\",\"rules\":[{\"required\":false,\"message\":\"必填项\"}]},{\"type\":\"slider\",\"label\":\"滑动输入条\",\"options\":{\"width\":\"100%\",\"defaultValue\":34,\"disabled\":false,\"hidden\":false,\"min\":0,\"max\":100,\"step\":1,\"showInput\":false},\"model\":\"cc\",\"key\":\"slider_1659020433092\",\"help\":\"\",\"rules\":[{\"required\":false,\"message\":\"必填项\"}]},{\"type\":\"table\",\"label\":\"表格布局\",\"trs\":[{\"tds\":[{\"colspan\":1,\"rowspan\":1,\"list\":[]},{\"colspan\":1,\"rowspan\":1,\"list\":[]}]},{\"tds\":[{\"colspan\":1,\"rowspan\":1,\"list\":[{\"type\":\"editor\",\"label\":\"富文本\",\"icon\":\"icon-LC_icon_edit_line_1\",\"list\":[],\"options\":{\"height\":300,\"placeholder\":\"请输入\",\"defaultValue\":\"\",\"chinesization\":true,\"hidden\":false,\"disabled\":false,\"showLabel\":false,\"width\":\"100%\"},\"model\":\"editor_1662106288134\",\"key\":\"editor_1662106288134\",\"help\":\"\",\"rules\":[{\"required\":false,\"message\":\"必填项\"}]}]},{\"colspan\":1,\"rowspan\":1,\"list\":[]}]}],\"options\":{\"width\":\"100%\",\"bordered\":true,\"bright\":false,\"small\":true,\"customStyle\":\"\"},\"key\":\"table_1662106283652\"}],\"config\":{\"layout\":\"vertical\",\"labelCol\":{\"xs\":6,\"sm\":6,\"md\":6,\"lg\":6,\"xl\":6,\"xxl\":6},\"labelWidth\":100,\"labelLayout\":\"Grid\",\"wrapperCol\":{\"xs\":18,\"sm\":18,\"md\":18,\"lg\":18,\"xl\":18,\"xxl\":18},\"hideRequiredMark\":false,\"customStyle\":\"\"}}', '测试动态表单', 1399985191002447872, '2022-07-28 22:03:36', 1414143554414059520, '2022-09-02 16:44:01', 0, 12);

-- ----------------------------
-- Table structure for base_key_value
-- ----------------------------
DROP TABLE IF EXISTS `base_key_value`;
CREATE TABLE `base_key_value`  (
  `id` bigint(20) NOT NULL,
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数键名',
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数值',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  `version` int(8) NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'kv存储' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_key_value
-- ----------------------------

-- ----------------------------
-- Table structure for base_param
-- ----------------------------
DROP TABLE IF EXISTS `base_param`;
CREATE TABLE `base_param`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `param_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数键名',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数值',
  `type` int(4) NULL DEFAULT NULL COMMENT '参数类型',
  `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '启用状态',
  `internal` bit(1) NOT NULL COMMENT '内置参数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  `version` int(8) NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统参数配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_param
-- ----------------------------
INSERT INTO `base_param` VALUES (1452842684284891136, '测试', 'test.v1', '123', 1, b'1', b'0', NULL, 1399985191002447872, '2021-10-26 11:41:03', 1399985191002447872, '2021-10-26 11:41:03', 0, 0);
INSERT INTO `base_param` VALUES (1500338438182789120, '结算台聚合支付请求地址', 'CashierAggregateUrl', 'http://127.0.0.1/api/', 1, b'1', b'1', '', 1399985191002447872, '2022-03-06 13:12:13', 1399985191002447872, '2022-05-01 15:03:03', 0, 3);
INSERT INTO `base_param` VALUES (1520668030248361984, '文件服务器地址', 'FileServerUrl', 'http://127.0.0.1:9999', 1, b'1', b'1', '', 1399985191002447872, '2022-05-01 15:34:46', 1399985191002447872, '2022-05-19 12:53:21', 0, 5);
INSERT INTO `base_param` VALUES (1529281530059161600, 'websocket服务器地址', 'WebsocketServerUrl', 'ws://127.0.0.1:9999', 1, b'1', b'1', '', 1399985191002447872, '2022-05-25 10:01:44', 1399985191002447872, '2022-05-25 10:01:44', 0, 0);
INSERT INTO `base_param` VALUES (1545765299880448000, '服务器地址', 'ServerUrl', 'http://127.0.0.1:9999', 1, b'1', b'1', '', 1399985191002447872, '2022-07-09 21:42:21', 1399985191002447872, '2022-07-09 21:42:21', 0, 0);
INSERT INTO `base_param` VALUES (1547511252795912192, '微信jsapi支付回调服务地址', 'JsapiRedirectUrl', 'http://127.0.0.1/api/', 1, b'1', b'1', '', 1414143554414059520, '2022-07-14 17:20:09', 1414143554414059520, '2022-07-14 17:20:09', 0, 0);

-- ----------------------------
-- Table structure for base_province
-- ----------------------------
DROP TABLE IF EXISTS `base_province`;
CREATE TABLE `base_province`  (
  `code` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '省份编码',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '省份名称',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '省份表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_province
-- ----------------------------
INSERT INTO `base_province` VALUES ('11', '北京市');
INSERT INTO `base_province` VALUES ('12', '天津市');
INSERT INTO `base_province` VALUES ('13', '河北省');
INSERT INTO `base_province` VALUES ('14', '山西省');
INSERT INTO `base_province` VALUES ('15', '内蒙古自治区');
INSERT INTO `base_province` VALUES ('21', '辽宁省');
INSERT INTO `base_province` VALUES ('22', '吉林省');
INSERT INTO `base_province` VALUES ('23', '黑龙江省');
INSERT INTO `base_province` VALUES ('31', '上海市');
INSERT INTO `base_province` VALUES ('32', '江苏省');
INSERT INTO `base_province` VALUES ('33', '浙江省');
INSERT INTO `base_province` VALUES ('34', '安徽省');
INSERT INTO `base_province` VALUES ('35', '福建省');
INSERT INTO `base_province` VALUES ('36', '江西省');
INSERT INTO `base_province` VALUES ('37', '山东省');
INSERT INTO `base_province` VALUES ('41', '河南省');
INSERT INTO `base_province` VALUES ('42', '湖北省');
INSERT INTO `base_province` VALUES ('43', '湖南省');
INSERT INTO `base_province` VALUES ('44', '广东省');
INSERT INTO `base_province` VALUES ('45', '广西壮族自治区');
INSERT INTO `base_province` VALUES ('46', '海南省');
INSERT INTO `base_province` VALUES ('50', '重庆市');
INSERT INTO `base_province` VALUES ('51', '四川省');
INSERT INTO `base_province` VALUES ('52', '贵州省');
INSERT INTO `base_province` VALUES ('53', '云南省');
INSERT INTO `base_province` VALUES ('54', '西藏自治区');
INSERT INTO `base_province` VALUES ('61', '陕西省');
INSERT INTO `base_province` VALUES ('62', '甘肃省');
INSERT INTO `base_province` VALUES ('63', '青海省');
INSERT INTO `base_province` VALUES ('64', '宁夏回族自治区');
INSERT INTO `base_province` VALUES ('65', '新疆维吾尔自治区');

-- ----------------------------
-- Table structure for base_street
-- ----------------------------
DROP TABLE IF EXISTS `base_street`;
CREATE TABLE `base_street`  (
  `code` char(9) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
  `name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '街道名称',
  `area_code` char(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '县区编码',
  PRIMARY KEY (`code`) USING BTREE,
  INDEX `inx_area_code`(`area_code`) USING BTREE COMMENT '县区'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '街道表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_street
-- ----------------------------

-- ----------------------------
-- Table structure for base_village
-- ----------------------------
DROP TABLE IF EXISTS `base_village`;
CREATE TABLE `base_village`  (
  `code` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '编码',
  `name` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `street_code` char(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '社区/乡镇编码',
  PRIMARY KEY (`code`) USING BTREE,
  INDEX `inx_street_code`(`street_code`) USING BTREE COMMENT '所属街道索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '村庄/社区' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_village
-- ----------------------------

-- ----------------------------
-- Table structure for bpm_instance
-- ----------------------------
DROP TABLE IF EXISTS `bpm_instance`;
CREATE TABLE `bpm_instance`  (
  `id` bigint(20) NOT NULL,
  `instance_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '流程实例的id',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程标题',
  `model_id` bigint(20) NULL DEFAULT NULL COMMENT '模型id',
  `def_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程定义ID',
  `def_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程定义名称',
  `start_user_id` bigint(20) NULL DEFAULT NULL COMMENT '发起人',
  `start_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发起人名称',
  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程实例的状态',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `form_variables` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '提交的表单值',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  `version` int(8) NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '流程实例扩展' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bpm_instance
-- ----------------------------

-- ----------------------------
-- Table structure for bpm_model
-- ----------------------------
DROP TABLE IF EXISTS `bpm_model`;
CREATE TABLE `bpm_model`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `model_type` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程类型',
  `form_id` bigint(20) NULL DEFAULT NULL COMMENT '关联表单',
  `publish` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发布状态',
  `enable` bit(1) NULL DEFAULT NULL COMMENT '启用状态',
  `deploy_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '部署id',
  `def_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程定义id',
  `def_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程key',
  `def_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程名称',
  `def_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程备注',
  `main_process` bit(1) NULL DEFAULT NULL COMMENT '是否主流程',
  `process_version` int(8) NULL DEFAULT NULL COMMENT '流程版本号',
  `model_editor_xml` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '流程xml',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  `version` int(8) NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '流程模型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bpm_model
-- ----------------------------

-- ----------------------------
-- Table structure for bpm_model_node
-- ----------------------------
DROP TABLE IF EXISTS `bpm_model_node`;
CREATE TABLE `bpm_model_node`  (
  `id` bigint(20) NOT NULL,
  `model_id` bigint(20) NOT NULL COMMENT '关联模型id',
  `def_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程定义id',
  `def_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程key',
  `node_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务节点id',
  `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务节点名称',
  `multi` bit(1) NOT NULL COMMENT '是否多任务',
  `sequential` bit(1) NULL DEFAULT NULL COMMENT '是否串签',
  `or_sign` bit(1) NULL DEFAULT NULL COMMENT '是否是或签',
  `ratio_pass` bit(1) NULL DEFAULT NULL COMMENT '是否比例通过',
  `pass_ratio` int(3) NULL DEFAULT NULL COMMENT '通过比例',
  `reject` bit(1) NOT NULL COMMENT '是否允许驳回',
  `back` bit(1) NOT NULL COMMENT '是否允许回退',
  `retrieve` bit(1) NOT NULL COMMENT '是否允许取回',
  `skip` bit(1) NOT NULL COMMENT '是否跳过当前节点',
  `form_id` bigint(20) NULL DEFAULT NULL COMMENT '关联表单',
  `assign_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分配类型',
  `assign_raw` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '分配的原始数据',
  `assign_show` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '分配的数据的展示',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `version` int(8) NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '模型任务节点配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bpm_model_node
-- ----------------------------

-- ----------------------------
-- Table structure for bpm_task
-- ----------------------------
DROP TABLE IF EXISTS `bpm_task`;
CREATE TABLE `bpm_task`  (
  `id` bigint(20) NOT NULL,
  `task_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务ID',
  `execution_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务执行 ID',
  `multi_id` bigint(20) NULL DEFAULT NULL COMMENT '多实例关联id',
  `instance_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '流程实例的id',
  `instance_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程名称(业务标题)',
  `def_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '流程定义名称',
  `node_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务节点id',
  `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务节点名称',
  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '处理状态',
  `result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '处理结果',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '处理意见',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '当前处理人',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '当前处理人',
  `start_user_id` bigint(20) NULL DEFAULT NULL COMMENT '发起人',
  `start_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发起人名称',
  `form_variables` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '提交的表单值',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `version` int(8) NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '流程任务扩展' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bpm_task
-- ----------------------------

-- ----------------------------
-- Table structure for common_sequence_range
-- ----------------------------
DROP TABLE IF EXISTS `common_sequence_range`;
CREATE TABLE `common_sequence_range`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `range_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区间key',
  `range_value` bigint(20) NOT NULL COMMENT '区间开始值',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '序列生成器队列区间管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of common_sequence_range
-- ----------------------------
INSERT INTO `common_sequence_range` VALUES (1470679520373862400, 'Sequence:cs', 2006, 0, '2021-12-14 16:58:16', 0, '2021-12-14 16:58:16', 6, 0);
INSERT INTO `common_sequence_range` VALUES (1470679955230908416, 'cs', 2020, 0, '2021-12-14 17:00:00', 0, '2021-12-14 17:00:00', 13, 0);

-- ----------------------------
-- Table structure for demo_data_encrypt
-- ----------------------------
DROP TABLE IF EXISTS `demo_data_encrypt`;
CREATE TABLE `demo_data_encrypt`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据加密解密演示' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of demo_data_encrypt
-- ----------------------------
INSERT INTO `demo_data_encrypt` VALUES (1506922411881103360, '测试加密效果', 'eI2RIrRLG+QUna3jMK+kejyJTTKdPFhaYWP4EhktJ2lkGTEsIxZesetNTzcqUA934ZN/OUdw4aj4t5Q+u1sH7A==', 1399985191002447872, '2022-03-24 17:14:35', 1399985191002447872, '2022-03-24 17:23:41', 1, 0);
INSERT INTO `demo_data_encrypt` VALUES (1506943412354408448, '测试下', 'Dgv5OSNiXuknceoZzeOUOQ==', 1399985191002447872, '2022-03-24 17:14:35', 1399985191002447872, '2022-03-24 17:23:41', 1, 0);

-- ----------------------------
-- Table structure for demo_data_perm
-- ----------------------------
DROP TABLE IF EXISTS `demo_data_perm`;
CREATE TABLE `demo_data_perm`  (
  `id` bigint(20) NOT NULL COMMENT '角色ID',
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `creator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '说明',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据权限演示' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of demo_data_perm
-- ----------------------------
INSERT INTO `demo_data_perm` VALUES (1495969849707220992, '33', 'xxm', '444', 1399985191002447872, '2022-02-22 11:53:01', 1399985191002447872, '2022-02-22 11:53:01', 0, 0);
INSERT INTO `demo_data_perm` VALUES (1506921683460521984, '测试', '小小明', NULL, 1399985191002447872, '2022-03-24 17:11:41', 1399985191002447872, '2022-03-24 17:11:41', 0, 0);
INSERT INTO `demo_data_perm` VALUES (1531547191561072640, '测试', '测试', '123', 1435967884114194432, '2022-05-31 16:04:40', 1435967884114194432, '2022-05-31 16:04:40', 0, 0);

-- ----------------------------
-- Table structure for demo_data_sensitive
-- ----------------------------
DROP TABLE IF EXISTS `demo_data_sensitive`;
CREATE TABLE `demo_data_sensitive`  (
  `id` bigint(20) NOT NULL COMMENT '角色ID',
  `chinese_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文名字',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `id_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `mobile_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `car_license` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车牌号',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `other` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其他',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据脱敏演示' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of demo_data_sensitive
-- ----------------------------
INSERT INTO `demo_data_sensitive` VALUES (1506942377435037696, '刘向东', '123456', '372921199302021125', '13324591123', '鲁A8S8866', 'bootx123@outlook.com', '测试测试测试测试测试测试测试问题', 1399985191002447872, '2022-03-24 18:33:55', 1399985191002447872, '2022-03-24 18:36:09', 2, 0);
INSERT INTO `demo_data_sensitive` VALUES (1506943326094352384, '成是非', '99885511', '101278112512107721', '18855446622', '汉S123456', 'chengshifei@foxmail.com', '这个就是就是就是就是就是就是这样的', 1399985191002447872, '2022-03-24 18:33:55', 1399985191002447872, '2022-03-24 18:35:00', 1, 0);

-- ----------------------------
-- Table structure for demo_super_query
-- ----------------------------
DROP TABLE IF EXISTS `demo_super_query`;
CREATE TABLE `demo_super_query`  (
  `id` bigint(20) NOT NULL COMMENT '角色ID',
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `age` int(5) NULL DEFAULT NULL COMMENT '年龄',
  `vip` bit(1) NULL DEFAULT NULL COMMENT '是否vip',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `work_time` time(0) NULL DEFAULT NULL COMMENT '上班时间',
  `registration_time` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
  `political` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '超级查询演示' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of demo_super_query
-- ----------------------------
INSERT INTO `demo_super_query` VALUES (1496046463434567680, '小小明', 18, b'1', '1998-01-23', '08:30:00', '2022-02-22 16:57:27', '13', '这是备注', 1399985191002447872, '2022-02-22 16:57:27', 1399985191002447872, '2022-02-22 17:03:34', 1, 0);
INSERT INTO `demo_super_query` VALUES (1496372341213433856, '关羽', 52, b'1', '2000-02-23', '14:31:36', '2022-02-23 14:32:22', '1', '', 1399985191002447872, '2022-02-23 14:32:22', 1399985191002447872, '2022-02-23 14:32:22', 0, 0);
INSERT INTO `demo_super_query` VALUES (1496372489909899264, '张飞', 54, b'0', '1996-02-11', '08:00:00', '2022-02-23 14:32:58', '7', '备注', 1399985191002447872, '2022-02-23 14:32:58', 1399985191002447872, '2022-02-23 14:32:58', 0, 0);
INSERT INTO `demo_super_query` VALUES (1496372766427779072, '梁冀', 38, b'1', '1958-02-08', '08:30:00', '2022-02-23 14:34:03', '1', '', 1399985191002447872, '2022-02-23 14:34:03', 1399985191002447872, '2022-02-23 14:34:03', 0, 0);
INSERT INTO `demo_super_query` VALUES (1496373512871284736, '刘备', 108, b'0', '1993-11-12', '09:30:10', '2022-02-23 14:37:01', '2', '刘羽禅的粑粑', 1399985191002447872, '2022-02-23 14:37:01', 1399985191002447872, '2022-02-23 14:37:01', 0, 0);

-- ----------------------------
-- Table structure for iam_client
-- ----------------------------
DROP TABLE IF EXISTS `iam_client`;
CREATE TABLE `iam_client`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(21) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `system` bit(1) NOT NULL COMMENT '是否系统内置',
  `enable` bit(1) NOT NULL COMMENT '是否可用',
  `login_type_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '关联登录方式\r\n',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '认证终端' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_client
-- ----------------------------
INSERT INTO `iam_client` VALUES (1430430071299207168, 'admin', 'pc管理端', b'1', b'1', '1430430071299207168,1435138582839009280,1430478946919653376,1542091599907115008,1542804450312122368,1543126042909016064', 'pc浏览器', 1399985191002447872, '2021-08-25 15:21:20', 1399985191002447872, '2022-07-02 14:55:11', 4, 0);
INSERT INTO `iam_client` VALUES (1430430071299207169, 'h5', 'h5端', b'1', b'1', '1430430071299207168,1435138582839009280', '手机wap', 1399985191002447872, '2021-08-25 15:21:20', 1399985191002447872, '2022-06-29 18:31:45', 1, 0);
INSERT INTO `iam_client` VALUES (1580487061605175296, 'adminv3', 'pc管理端(vue3版)', b'0', b'1', '1430430071299207168,1430478946919653376,1435138582839009280,1542091599907115008,1542804450312122368,1543126042909016064', 'vue3版本', 1399985191002447872, '2022-10-13 17:14:14', 1399985191002447872, '2022-10-13 17:14:25', 1, 0);
INSERT INTO `iam_client` VALUES (1626840094767714304, 'GoView', '可视化平台', b'0', b'1', '1430430071299207168', '', 1414143554414059520, '2023-02-18 15:04:38', 1414143554414059520, '2023-02-18 15:04:38', 0, 0);

-- ----------------------------
-- Table structure for iam_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `iam_data_scope`;
CREATE TABLE `iam_data_scope`  (
  `id` bigint(20) NOT NULL COMMENT '角色ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `type` int(4) NOT NULL COMMENT '类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '说明',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据范围权限' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_data_scope
-- ----------------------------
INSERT INTO `iam_data_scope` VALUES (1474706893178871808, 'self', '自身数据', 1, '只能查看自身范围的数据', 1399985191002447872, '2021-12-25 19:41:37', 1399985191002447872, '2021-12-25 19:41:37', 1, 0);
INSERT INTO `iam_data_scope` VALUES (1474717084985270272, 'user', '用户数据权限', 2, '用户数据权限', 1399985191002447872, '2021-12-25 20:22:07', 1399985191002447872, '2021-12-25 20:22:07', 0, 0);
INSERT INTO `iam_data_scope` VALUES (1474717160671485952, 'dept', '部门权限', 3, '', 1399985191002447872, '2021-12-25 20:22:25', 1399985191002447872, '2021-12-25 20:22:25', 0, 0);
INSERT INTO `iam_data_scope` VALUES (1474717276908232704, 'userAndDept', '用户和部门权限', 4, '', 1399985191002447872, '2021-12-25 20:22:52', 1399985191002447872, '2021-12-25 20:22:52', 0, 0);
INSERT INTO `iam_data_scope` VALUES (1474717344562356224, 'all', '全部数据', 5, '', 1399985191002447872, '2021-12-25 20:23:09', 1399985191002447872, '2021-12-25 20:23:09', 0, 0);
INSERT INTO `iam_data_scope` VALUES (1477990268903804928, 'belong_dept', '所在部门', 6, '', 1399985191002447872, '2022-01-03 21:08:34', 1399985191002447872, '2022-01-03 21:08:35', 0, 0);
INSERT INTO `iam_data_scope` VALUES (1477990290521247744, 'belong_dept', '所在部门', 6, '', 1399985191002447872, '2022-01-03 21:08:40', 1399985191002447872, '2022-01-03 21:08:40', 0, 1);
INSERT INTO `iam_data_scope` VALUES (1477990439800721408, 'belong_dept_and_sub', '所在及下级部门', 7, '', 1399985191002447872, '2022-01-03 21:09:15', 1399985191002447872, '2022-01-03 21:09:15', 0, 0);

-- ----------------------------
-- Table structure for iam_data_scope_dept
-- ----------------------------
DROP TABLE IF EXISTS `iam_data_scope_dept`;
CREATE TABLE `iam_data_scope_dept`  (
  `id` bigint(20) NOT NULL,
  `data_scope_id` bigint(20) NOT NULL COMMENT '数据范围id',
  `dept_id` bigint(20) NOT NULL COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据范围部门关联配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_data_scope_dept
-- ----------------------------
INSERT INTO `iam_data_scope_dept` VALUES (1478742690014101504, 1474717160671485952, 1477978464559484928);
INSERT INTO `iam_data_scope_dept` VALUES (1478742920071675904, 1474717160671485952, 1477977592291053568);

-- ----------------------------
-- Table structure for iam_data_scope_user
-- ----------------------------
DROP TABLE IF EXISTS `iam_data_scope_user`;
CREATE TABLE `iam_data_scope_user`  (
  `id` bigint(20) NOT NULL,
  `data_scope_id` bigint(20) NOT NULL COMMENT '数据范围id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据范围用户关联配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_data_scope_user
-- ----------------------------
INSERT INTO `iam_data_scope_user` VALUES (1478738754876149760, 1474717084985270272, 1399985191002447872);
INSERT INTO `iam_data_scope_user` VALUES (1478738766460817408, 1474717084985270272, 1414143554414059520);
INSERT INTO `iam_data_scope_user` VALUES (1478738811792855040, 1474717084985270272, 1477997602862505984);
INSERT INTO `iam_data_scope_user` VALUES (1478747304583114752, 1474717084985270272, 1435894470432456704);
INSERT INTO `iam_data_scope_user` VALUES (1478747304587309056, 1474717084985270272, 1477997391729631232);

-- ----------------------------
-- Table structure for iam_dept
-- ----------------------------
DROP TABLE IF EXISTS `iam_dept`;
CREATE TABLE `iam_dept`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父机构ID',
  `dept_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构/部门名称',
  `sort_no` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `org_category` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '机构类别 1公司 2部门 3岗位',
  `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构编码',
  `mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `fax` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '传真',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门组织机构表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_dept
-- ----------------------------
INSERT INTO `iam_dept` VALUES (1259382878857957377, NULL, 'Bootx Platform总公司', 0, '1', '1', '', NULL, '济南市高新区齐鲁软件园', NULL, -1, '2020-05-10 15:20:51', -1, '2020-05-10 17:52:15', 4, 0);
INSERT INTO `iam_dept` VALUES (1477976804995026944, NULL, 'Bootx Cloud公司', 0, '1', '1', '', NULL, '济南市高新区汉峪金谷', '', 1399985191002447872, '2022-01-03 20:15:04', 1399985191002447872, '2022-01-03 20:15:05', 0, 1);
INSERT INTO `iam_dept` VALUES (1477977184768282624, NULL, 'Bootx Cloud公司', 0, '1', '1', '', NULL, '', '', 1399985191002447872, '2022-01-03 20:16:35', 1399985191002447872, '2022-01-03 20:16:35', 0, 1);
INSERT INTO `iam_dept` VALUES (1477977301365739520, NULL, 'Bootx Cloud总公司', 0, '1', '2', '', NULL, '济南市高新区汉峪金谷', '', 1399985191002447872, '2022-01-03 20:17:03', 1399985191002447872, '2022-01-03 20:17:03', 1, 0);
INSERT INTO `iam_dept` VALUES (1477977592291053568, 1259382878857957377, 'bp济南分公司', 0, '1', '1_1', '', NULL, '', '', 1399985191002447872, '2022-01-03 20:18:12', 1399985191002447872, '2022-01-03 20:18:12', 1, 0);
INSERT INTO `iam_dept` VALUES (1477977690928500736, 1259382878857957377, '历城分公司', 0, '1', '1_2', '', NULL, '', '', 1399985191002447872, '2022-01-03 20:18:36', 1399985191002447872, '2022-01-03 20:18:36', 0, 1);
INSERT INTO `iam_dept` VALUES (1477977827897692160, 1259382878857957377, 'bp潍坊分公司', 0, '1', '1_2', '', NULL, '', '', 1399985191002447872, '2022-01-03 20:19:08', 1399985191002447872, '2022-01-03 20:19:08', 0, 0);
INSERT INTO `iam_dept` VALUES (1477977880947249152, 1477977301365739520, 'bc菏泽分公司', 0, '1', '2_1', '', NULL, '', '', 1399985191002447872, '2022-01-03 20:19:21', 1399985191002447872, '2022-01-03 20:19:21', 0, 0);
INSERT INTO `iam_dept` VALUES (1477977930175795200, 1477977301365739520, 'bc日照分公司', 0, '1', '2_2', '', NULL, '', '', 1399985191002447872, '2022-01-03 20:19:33', 1399985191002447872, '2022-01-03 20:19:33', 0, 0);
INSERT INTO `iam_dept` VALUES (1477978464559484928, 1477977592291053568, '历城办事部', 0, '2', '1_1_1', '', NULL, '', '', 1399985191002447872, '2022-01-03 20:21:40', 1399985191002447872, '2022-01-03 20:21:40', 1, 0);
INSERT INTO `iam_dept` VALUES (1477978512177418240, 1477977592291053568, '历下办事部', 0, '2', '1_1_2', '', NULL, '', '', 1399985191002447872, '2022-01-03 20:21:51', 1399985191002447872, '2022-01-03 20:21:52', 0, 0);
INSERT INTO `iam_dept` VALUES (1477978610865197056, 1477977592291053568, '高新办事部', 0, '2', '1_1_2', '', NULL, '', '', 1399985191002447872, '2022-01-03 20:22:15', 1399985191002447872, '2022-01-03 20:22:15', 0, 0);
INSERT INTO `iam_dept` VALUES (1477978810526650368, 1477977827897692160, '奎文办事部', 0, '2', '1_2_1', '', NULL, '', '', 1399985191002447872, '2022-01-03 20:23:03', 1399985191002447872, '2022-01-03 20:23:03', 0, 0);
INSERT INTO `iam_dept` VALUES (1477978883247493120, 1477977827897692160, '潍城办事部', 0, '2', '1_2_2', '', NULL, '', '', 1399985191002447872, '2022-01-03 20:23:20', 1399985191002447872, '2022-01-03 20:23:20', 0, 0);

-- ----------------------------
-- Table structure for iam_login_type
-- ----------------------------
DROP TABLE IF EXISTS `iam_login_type`;
CREATE TABLE `iam_login_type`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(21) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `system` bit(1) NOT NULL COMMENT '是否系统内置',
  `timeout` bigint(11) NULL DEFAULT NULL COMMENT '在线时长 秒',
  `captcha` bit(1) NOT NULL COMMENT '启用验证码',
  `pwd_err_num` int(8) NOT NULL COMMENT '密码错误次数',
  `enable` bit(1) NOT NULL COMMENT '是否可用',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录方式' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_login_type
-- ----------------------------
INSERT INTO `iam_login_type` VALUES (1430430071299207168, 'password', '账号密码登陆', 'password', b'1', 3600, b'0', -1, b'1', NULL, 1399985191002447872, '2021-08-25 15:21:20', 1399985191002447872, '2022-11-03 22:24:53', 20, 0);
INSERT INTO `iam_login_type` VALUES (1430478946919653376, 'miniApp', '微信小程序', 'openId', b'0', 99999, b'0', 0, b'1', NULL, 1399985191002447872, '2021-08-25 18:35:33', 1399985191002447872, '2022-07-16 12:32:26', 3, 0);
INSERT INTO `iam_login_type` VALUES (1435138582839009280, 'phone', '手机短信登录', 'openId', b'0', 3600, b'0', 0, b'1', NULL, 1399985191002447872, '2021-09-07 15:11:16', 1399985191002447872, '2022-07-16 12:32:19', 5, 0);
INSERT INTO `iam_login_type` VALUES (1542091599907115008, 'dingTalk', '钉钉', 'openId', b'0', 5, b'0', -1, b'1', '', 1399985191002447872, '2022-06-29 18:24:23', 1399985191002447872, '2022-07-02 14:55:01', 5, 0);
INSERT INTO `iam_login_type` VALUES (1542804450312122368, 'weCom', '企业微信', 'openId', b'0', 5, b'0', -1, b'1', '', 1399985191002447872, '2022-07-01 17:37:00', 1399985191002447872, '2022-07-01 17:37:00', 0, 0);
INSERT INTO `iam_login_type` VALUES (1543126042909016064, 'weChat', '微信登录', 'openId', b'0', 5, b'0', -1, b'1', '', 1399985191002447872, '2022-07-02 14:54:53', 0, '2022-10-12 22:15:05', 2, 0);
INSERT INTO `iam_login_type` VALUES (1626845524617203712, 'passwordGoView', '可视化平台登录', 'password', b'0', 3600, b'0', -1, b'1', '', 1414143554414059520, '2023-02-18 15:26:13', 1414143554414059520, '2023-02-18 15:26:13', 0, 0);

-- ----------------------------
-- Table structure for iam_perm_menu
-- ----------------------------
DROP TABLE IF EXISTS `iam_perm_menu`;
CREATE TABLE `iam_perm_menu`  (
  `id` bigint(20) NOT NULL,
  `client_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '终端code',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由名称',
  `perm_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单权限编码',
  `effect` bit(1) NULL DEFAULT NULL COMMENT '是否有效',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `hidden` bit(1) NOT NULL COMMENT '是否隐藏',
  `hide_children_in_menu` bit(1) NOT NULL COMMENT '是否隐藏子菜单',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件',
  `component_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件名字',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单跳转地址(重定向)',
  `sort_no` double NOT NULL COMMENT '菜单排序',
  `menu_type` int(5) NOT NULL COMMENT '类型（0：一级菜单；1：子菜单 ；2：按钮权限）',
  `leaf` bit(1) NULL DEFAULT NULL COMMENT '是否叶子节点',
  `keep_alive` bit(1) NULL DEFAULT NULL COMMENT '是否缓存页面',
  `target_outside` bit(1) NULL DEFAULT NULL COMMENT '是否外部打开方式',
  `hidden_header_content` bit(1) NULL DEFAULT NULL COMMENT '隐藏的标题内容',
  `admin` bit(1) NOT NULL COMMENT '系统菜单',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限_菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_perm_menu
-- ----------------------------
INSERT INTO `iam_perm_menu` VALUES (1414596052497092608, 'admin', NULL, '系统管理', 'system', '', NULL, 'desktop', b'0', b'0', 'RouteView', '', '/system', '/system/user', -99999, 0, b'0', b'1', b'0', b'0', b'1', NULL, 1399985191002447872, '2021-08-27 10:32:53', 1399985191002447872, '2021-08-27 10:02:16', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1414596647509446656, 'admin', 1452569691537256448, '用户管理', 'User', '', NULL, '', b'0', b'0', 'system/user/UserList', '', '/system/userAuth/user', '', 0, 1, b'0', b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-08-27 10:32:53', 1399985191002447872, '2021-08-27 10:17:40', 4, 0);
INSERT INTO `iam_perm_menu` VALUES (1414596773275652096, 'admin', 1414596052497092608, '菜单管理', 'Menu', '', NULL, '', b'0', b'0', 'system/menu/MenuList', '', '/system/permission/menu', '', 0, 1, b'0', b'1', b'0', b'0', b'1', NULL, 1399985191002447872, '2021-08-27 10:32:53', 1399985191002447872, '2021-08-26 23:56:16', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1414596805538238464, 'admin', 1452569339987472384, '角色管理', 'Role', '', NULL, '', b'0', b'0', 'system/role/RoleList', '', '/system/permission/role', '', 0, 1, b'1', b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-08-27 10:32:53', 1399985191002447872, '2021-08-26 23:56:04', 4, 0);
INSERT INTO `iam_perm_menu` VALUES (1414596842322284544, 'admin', 1452569691537256448, '部门管理', 'Dept', '', NULL, '', b'0', b'0', 'system/dept/DeptList', '', '/system/userAuth/dept', '', 0, 1, b'1', b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-08-27 10:32:53', 1399985191002447872, '2021-08-26 23:56:31', 7, 0);
INSERT INTO `iam_perm_menu` VALUES (1414596877617352704, 'admin', 1452571269199540224, '数据字典', 'Dict', '', b'0', '', b'0', b'0', 'system/dict/DictList', '', '/system/config/dict', '', 0, 1, b'1', b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-08-27 10:32:53', 1399985191002447872, '2022-05-19 09:04:55', 11, 0);
INSERT INTO `iam_perm_menu` VALUES (1431082258161434624, 'admin', 1452569691537256448, '在线用户管理', 'OnlineUser', '', NULL, '', b'0', b'0', 'system/online/OnlineUserList', NULL, '/system/userAuth/online', '', 0, 1, b'1', b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-08-27 10:32:53', 1399985191002447872, '2021-08-27 10:32:53', 6, 0);
INSERT INTO `iam_perm_menu` VALUES (1431083330909208576, 'admin', 1541427353886859264, '登录方式', 'LoginType', '', b'0', '', b'0', b'0', 'system/client/LoginTypeList', NULL, '/system/config/loginType', '', 9, 1, b'1', b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-08-27 10:37:09', 1399985191002447872, '2022-07-05 21:18:12', 11, 0);
INSERT INTO `iam_perm_menu` VALUES (1431089129232498688, 'admin', 1452569339987472384, '请求权限管理', 'Path', '', NULL, '', b'0', b'0', 'system/path/PathList', NULL, '/system/permission/path', '', 0, 1, b'1', b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-08-27 11:00:11', 1399985191002447872, '2021-08-27 11:00:11', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1431152689832525824, 'admin', NULL, '系统监控', 'monitor', '', b'0', 'radar-chart', b'0', b'0', 'RouteView', NULL, '/monitor', '', 0, 0, b'1', b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2021-08-27 15:12:45', 1399985191002447872, '2022-06-17 17:57:30', 21, 0);
INSERT INTO `iam_perm_menu` VALUES (1431153358157348864, 'admin', 1431152689832525824, '接口文档', 'ApiSwagger', '', b'0', '', b'0', b'0', '', NULL, 'http://127.0.0.1:9999/doc.html', '', 0, 1, b'1', b'1', b'1', b'0', b'0', NULL, 1399985191002447872, '2021-08-27 15:15:25', 1399985191002447872, '2022-10-14 17:47:12', 18, 0);
INSERT INTO `iam_perm_menu` VALUES (1435143678721236992, 'admin', 1452567897717321728, '登录日志', 'LoginLog', '', b'0', '', b'0', b'0', 'starter/log/LoginLogList', NULL, '/monitor/log/loginLog', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-09-07 15:31:31', 1399985191002447872, '2022-10-20 20:38:06', 6, 0);
INSERT INTO `iam_perm_menu` VALUES (1435476255797624832, 'admin', 1452567897717321728, '操作日志', 'OperateLog', '', b'0', '', b'0', b'0', 'starter/log/OperateLogList', NULL, '/monitor/log/OperateLog', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-09-08 13:33:04', 1399985191002447872, '2022-10-20 20:38:12', 8, 0);
INSERT INTO `iam_perm_menu` VALUES (1438061887002759168, 'admin', NULL, '通知管理', 'notice', '', NULL, 'message', b'0', b'0', 'RouteView', NULL, '/notice', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-09-15 16:47:26', 1399985191002447872, '2021-09-15 16:47:26', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1438072357281542144, 'admin', 1438061887002759168, '邮件配置', 'MailConfig', '', NULL, '', b'0', b'0', 'notice/mail/MailConfigList', NULL, '/notice/mailConfig', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-09-15 17:29:03', 1399985191002447872, '2021-09-15 17:29:03', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1439196893514031104, 'admin', 1438061887002759168, '消息模板', 'MessageTemplate', '', NULL, '', b'0', b'0', 'notice/template/TemplateList', NULL, '/notice/template', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-09-18 19:57:33', 1399985191002447872, '2021-09-18 19:57:33', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1440216178722050048, 'admin', 1438061887002759168, '钉钉', 'DingTalk', '', NULL, '', b'0', b'0', 'RouteView', NULL, '/notice/dingTalk', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-09-21 15:27:50', 1399985191002447872, '2021-09-21 15:27:50', 0, 1);
INSERT INTO `iam_perm_menu` VALUES (1440216612211757056, 'admin', 1450822511087271936, '钉钉机器人', 'DingTalkRobot', '', b'0', '', b'0', b'0', 'third/dingtalk/robot/DingRobotConfigList', NULL, '/third/dingTalk/robot', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-09-21 15:29:33', 1399985191002447872, '2022-07-26 13:34:35', 5, 0);
INSERT INTO `iam_perm_menu` VALUES (1450473063320526848, 'admin', 1452569691537256448, '第三方登录', 'Social', '', b'0', '', b'1', b'0', 'system/social/SocialList', NULL, '/system/userAuth/social', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-19 22:45:01', 1399985191002447872, '2022-07-26 13:35:47', 5, 0);
INSERT INTO `iam_perm_menu` VALUES (1450803906215886848, 'admin', 1452571269199540224, '定时任务', 'QuartzJobList', '', NULL, '', b'0', b'0', 'starter/quartz/QuartzJobList', NULL, '/system/config/quartz', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-20 20:39:41', 1399985191002447872, '2021-10-20 20:39:41', 5, 0);
INSERT INTO `iam_perm_menu` VALUES (1450819607680991232, 'admin', NULL, '第三方对接', 'third', '', b'0', 'branches', b'0', b'0', 'RouteView', NULL, '/third', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-20 21:42:04', 1399985191002447872, '2022-07-26 13:34:02', 4, 0);
INSERT INTO `iam_perm_menu` VALUES (1450821723027881984, 'admin', 1450819607680991232, '微信', 'WeChat', '', b'0', '', b'0', b'0', 'RouteView', NULL, '/third/wechat', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-20 21:50:28', 1399985191002447872, '2022-08-03 23:23:10', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1450821877831254016, 'admin', 1450819607680991232, '企业微信', 'WeCom', '', b'0', '', b'0', b'0', 'RouteView', NULL, '/third/wecom', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-20 21:51:05', 1399985191002447872, '2022-08-03 23:22:58', 4, 0);
INSERT INTO `iam_perm_menu` VALUES (1450822511087271936, 'admin', 1450819607680991232, '钉钉', 'DingTalk', '', b'0', '', b'0', b'0', 'RouteView', NULL, '/third/dingtalk', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-20 21:53:36', 1399985191002447872, '2022-08-03 23:23:29', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1450827660459458560, 'admin', 1438061887002759168, '微信', 'NoticeWeChat', '', b'0', '', b'0', b'0', 'Dev', NULL, '/notice/wechat', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-20 22:14:04', 1399985191002447872, '2022-06-21 20:53:04', 1, 1);
INSERT INTO `iam_perm_menu` VALUES (1452567897717321728, 'admin', 1431152689832525824, '审计日志', 'auditLog', '', b'0', '', b'0', b'0', 'RouteView', NULL, '/monitor/log', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-25 17:29:09', 1399985191002447872, '2022-10-20 20:37:46', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1452569339987472384, 'admin', 1414596052497092608, '权限管理', 'permission', '', NULL, '', b'0', b'0', 'RouteView', NULL, '/system/permission', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-25 17:34:53', 1399985191002447872, '2021-10-25 17:34:53', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1452569691537256448, 'admin', 1414596052497092608, '用户信息', 'userAuth', '', NULL, '', b'0', b'0', 'RouteView', NULL, '/system/userAuth', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-25 17:36:17', 1399985191002447872, '2021-10-25 17:36:17', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1452571269199540224, 'admin', 1414596052497092608, '系统配置', 'systemConfig', '', NULL, '', b'0', b'0', 'RouteView', NULL, '/system/config', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-25 17:42:33', 1399985191002447872, '2021-10-25 17:42:33', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1452638905302966272, 'admin', 1452571269199540224, '系统参数', 'SystemParam', '', NULL, '', b'0', b'0', 'system/param/SystemParamList', NULL, '/system/config/param', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-10-25 22:11:18', 1399985191002447872, '2021-10-25 22:11:18', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1474694545336676352, 'admin', 1452569339987472384, '数据范围权限', 'DataScope', '', NULL, '', b'0', b'0', 'system/scope/DataScopeList', NULL, '/system/permission/data', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-12-25 18:52:33', 1399985191002447872, '2021-12-25 18:52:33', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1480839877352476672, 'admin', 1452567897717321728, '数据版本日志', 'DataVersionLog', NULL, b'0', '', b'0', b'0', 'starter/log/DataVersionLogList', NULL, '/monitor/log/DataVersionLog', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-01-11 17:51:54', 1399985191002447872, '2022-10-20 20:38:18', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1490984296616263680, 'admin', 1552207982510706688, '文件管理', 'FIleUpLoad', NULL, b'0', '', b'0', b'0', 'develop/file/FileUploadList', NULL, '/develop/file', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-02-08 17:42:12', 1399985191002447872, '2022-07-28 09:16:56', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1495013564652429312, 'admin', 1552207982510706688, '代码生成', 'CodeGen', NULL, b'0', '', b'0', b'0', 'develop/codegen/CodeGenList', NULL, '/develop/codegen', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-02-19 20:33:04', 1399985191002447872, '2022-07-27 16:25:05', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1495968302034210816, 'admin', NULL, '功能演示', 'Demo', NULL, b'0', 'block', b'0', b'0', 'RouteView', NULL, '/demo', '', 99, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-02-22 11:46:52', 1399985191002447872, '2022-05-10 11:02:05', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1495969099987963904, 'admin', 1495968302034210816, '数据相关', 'DemoData', NULL, b'0', '', b'0', b'0', 'RouteView', NULL, '/demo/data', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-02-22 11:50:02', 1399985191002447872, '2022-03-24 16:27:46', 4, 0);
INSERT INTO `iam_perm_menu` VALUES (1496020308992143360, 'admin', 1495968302034210816, '超级查询', 'SuperQueryDemo', NULL, b'0', '', b'0', b'0', 'demo/query/SuperQueryDemoList', NULL, '/demo/query/super', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-02-22 15:13:31', 1399985191002447872, '2022-05-27 17:34:41', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1506910599819165696, 'admin', 1495969099987963904, '数据权限', 'DataPermDemoList', NULL, b'0', '', b'0', b'0', 'demo/data/perm/DataPermDemoList', NULL, '/demo/data/perm', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-03-24 16:27:39', 1399985191002447872, '2022-03-24 16:28:56', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1506910885463851008, 'admin', 1495969099987963904, '加密解密', 'DataEncryptDemo', NULL, b'0', '', b'0', b'0', 'demo/data/encrypt/DataEncryptDemoList', NULL, '/demo/data/encrypt', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-03-24 16:28:47', 1399985191002447872, '2022-03-24 16:28:47', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1506911113394913280, 'admin', 1495969099987963904, '数据脱敏', 'DataSensitiveDemo', NULL, b'0', '', b'0', b'0', 'demo/data/sensitive/DataSensitiveDemoList', NULL, '/demo/data/sensitive', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-03-24 16:29:41', 1399985191002447872, '2022-03-24 17:16:03', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1507998458886197248, 'admin', 1495968302034210816, 'WS演示', 'WebsocketDemo', NULL, b'0', '', b'0', b'0', 'demo/ws/WebsocketDemo', NULL, '/demo/ws', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-03-27 16:30:25', 1399985191002447872, '2022-03-27 16:30:25', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1509488473583562752, 'admin', 1495968302034210816, '幂等请求演示', 'IdempotentDemo', NULL, b'0', '', b'0', b'0', 'demo/idempotent/Idempotent', NULL, '/demo/idempotent', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-03-31 19:11:12', 1399985191002447872, '2022-05-27 17:35:00', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1511266086400524288, 'admin', 1450822511087271936, '钉钉配置', 'DingTalkConfig', NULL, b'0', '', b'1', b'0', 'third/dingtalk/config/DingTalkConfigList', NULL, '/third/dingtalk/config', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-04-05 16:54:48', 1399985191002447872, '2022-07-26 13:34:44', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1530120084482084864, 'admin', 1495968302034210816, '消息中间件演示', 'MQDemo', NULL, b'0', '', b'0', b'0', 'demo/mq/MqDemo', NULL, '/demo/mq', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-05-27 17:33:51', 1399985191002447872, '2022-05-31 15:12:15', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1530120684645044224, 'admin', 1530120084482084864, 'MQTT消息', 'MqttDemo', NULL, b'0', '', b'0', b'0', '', NULL, '/demo/mq/mqtt', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-05-27 17:36:14', 1399985191002447872, '2022-05-27 17:36:14', 0, 1);
INSERT INTO `iam_perm_menu` VALUES (1530120821144473600, 'admin', 1530120084482084864, 'RabbitMQ', 'RabbitDemo', NULL, b'0', '', b'0', b'0', '', NULL, '/demo/mq/rabbit', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-05-27 17:36:47', 1399985191002447872, '2022-05-27 17:36:47', 0, 1);
INSERT INTO `iam_perm_menu` VALUES (1534000136370204672, 'admin', 1431152689832525824, 'ELK日志', 'ELK', '', b'0', '', b'0', b'0', '', NULL, 'http://elk.dev.bootx.cn:5601/app/discover', '', 0, 1, NULL, b'1', b'1', b'0', b'0', NULL, 1399985191002447872, '2022-06-07 10:31:48', 1399985191002447872, '2022-06-07 10:31:48', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1534008203006652416, 'admin', 1431152689832525824, 'PlumeLog日志', 'PlumeLog', '', b'0', '', b'0', b'0', '', NULL, 'http://127.0.0.1:9999/plumelog/#/', '', 0, 1, NULL, b'1', b'1', b'0', b'0', NULL, 1399985191002447872, '2022-06-07 11:03:51', 1399985191002447872, '2022-06-07 11:06:13', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1535451167008436224, 'admin', 1431152689832525824, '系统信息', 'SysInfo', NULL, b'0', '', b'0', b'0', 'starter/monitor/SystemInfoMonitor', NULL, '/monitor/sysinfo', '', 0, 1, NULL, b'0', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-06-11 10:37:40', 1399985191002447872, '2022-06-13 13:07:46', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1535965936371085312, 'admin', 1431152689832525824, 'Redis监控', 'RedisInfoMonitor', NULL, b'0', '', b'0', b'0', 'starter/monitor/RedisInfoMonitor', NULL, '/monitor/redis', '', 0, 1, NULL, b'0', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-06-12 20:43:11', 1399985191002447872, '2022-06-13 13:07:38', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1537730140522348544, 'admin', 1438061887002759168, '站内信', 'SiteMessageSender', '', b'0', '', b'0', b'0', 'notice/site/sender/SiteMessageList', NULL, '/notice/siteMessage', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-06-17 17:33:30', 1399985191002447872, '2022-08-20 21:12:30', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1538160478872625152, 'admin', 1495968302034210816, '富文本编辑', 'WangEditorDemo', NULL, b'0', '', b'0', b'0', 'demo/wangeditor/WangEditorDemo', NULL, '/demo/wangEditor', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-06-18 22:03:30', 1399985191002447872, '2022-06-18 22:03:31', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1541355214204030976, 'admin', 1541427353886859264, '认证终端', 'Client', '', b'0', '', b'0', b'0', 'system/client/ClientList', NULL, '/system/config/client', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2021-08-27 10:37:08', 1399985191002447872, '2022-07-05 21:17:45', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1541427353886859264, 'admin', 1414596052497092608, '认证管理', 'Auth', '', b'0', '', b'0', b'0', 'RouteView', NULL, '/system/auth', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-06-27 22:24:54', 1399985191002447872, '2022-06-27 22:24:54', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1551803592828932096, 'admin', 1450821877831254016, '企微机器人', 'WeComRobot', '', b'0', '', b'0', b'0', 'third/wecom/robot/WeComRobotConfigList', NULL, '/third/wecom/robot', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-07-26 13:36:22', 1399985191002447872, '2022-10-24 11:07:27', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1552207982510706688, 'admin', NULL, '开发管理', 'develop', NULL, b'0', 'gold', b'0', b'0', 'RouteView', NULL, '/develop', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-07-27 16:23:16', 1399985191002447872, '2022-07-27 16:39:09', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1552208167664062464, 'admin', 1552207982510706688, '动态表单', 'DynamicForm', NULL, b'0', '', b'0', b'0', 'develop/dynamicform/DynamicFormList', NULL, '/develop/form', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-07-27 16:24:00', 1399985191002447872, '2022-07-27 16:38:05', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1554720980865380352, 'admin', 1495968302034210816, '消息通知', 'NoticeDemo', NULL, b'0', '', b'0', b'0', 'RouteView', NULL, '/demo/notice', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-08-03 14:49:02', 1399985191002447872, '2022-08-03 14:49:02', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1554721654336385024, 'admin', 1554720980865380352, '邮件发送', 'EmailDemo', NULL, b'0', '', b'0', b'0', 'demo/notice/email/EmailSender', NULL, '/demo/notice/email', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-08-03 14:51:42', 1399985191002447872, '2022-08-03 14:51:42', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1554850179754975232, 'admin', 1450821723027881984, '消息模板', 'WeChatTemplate', NULL, b'0', '', b'0', b'0', 'third/wechat/template/WeChatTemplateList', NULL, '/third/wechat/template', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-08-03 23:22:25', 1399985191002447872, '2022-08-03 23:23:18', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1555835229426368512, 'admin', 1450821723027881984, '自定义菜单', 'WeChatMenu', NULL, b'0', '', b'0', b'0', 'third/wechat/menu/WeChatMenuList', NULL, '/third/wechat/menu', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-08-06 16:36:39', 1399985191002447872, '2022-08-08 12:14:14', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1556997405528805376, 'admin', 1450821723027881984, '素材管理', 'WeChatMedia', NULL, b'0', '', b'0', b'0', 'third/wechat/media/WeChatMediaList', NULL, '/third/wechat/media', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-08-09 21:34:44', 1399985191002447872, '2022-08-09 21:34:44', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1573669546890297344, 'admin', 1552207982510706688, '动态数据源', 'DynamicSource', NULL, b'0', '', b'0', b'0', 'develop/dynamicsource/DynamicDataSourceList', NULL, '/develop/source', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-09-24 21:43:52', 1399985191002447872, '2022-09-24 21:43:52', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1580740450633101312, 'adminv3', NULL, '系统管理', 'System', NULL, b'0', 'ant-design:setting-outlined', b'0', b'0', 'Layout', NULL, '/system', '/system1/client', -99999, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-10-14 10:01:07', 1414143554414059520, '2022-10-18 15:32:09', 4, 0);
INSERT INTO `iam_perm_menu` VALUES (1580740637841666048, 'adminv3', 1582253306356649984, '终端管理', 'ClientList', NULL, b'0', '', b'0', b'0', '/modules/system/client/ClientList.vue', NULL, '/system/client', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-10-14 10:01:51', 1414143554414059520, '2022-10-18 14:13:27', 5, 0);
INSERT INTO `iam_perm_menu` VALUES (1580740758629232640, 'adminv3', 1582253306356649984, '登录方式', 'LoginTypeList', NULL, b'0', '', b'0', b'0', '/modules/system/loginType/LoginTypeList.vue', NULL, '/system/loginType', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-10-14 10:02:20', 1414143554414059520, '2022-10-18 14:13:40', 5, 0);
INSERT INTO `iam_perm_menu` VALUES (1580858583654051840, 'adminv3', 1580740450633101312, '测试Iframe', 'Iframe', NULL, b'0', '', b'0', b'0', 'Iframe', NULL, '/system/Iframe', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-10-14 17:50:32', 1414143554414059520, '2022-10-17 17:46:19', 5, 1);
INSERT INTO `iam_perm_menu` VALUES (1580917438227075072, 'adminv3', 1580740450633101312, '三极目录', 'hello', NULL, b'0', '', b'0', b'0', 'Layout', NULL, '/system1/a', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-10-14 21:44:24', 1399985191002447872, '2022-10-14 23:38:16', 2, 1);
INSERT INTO `iam_perm_menu` VALUES (1580917571069071360, 'adminv3', 1580917438227075072, '百度', 'baidu', NULL, b'0', '', b'0', b'0', '', NULL, 'https://www.baidu.com/', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-10-14 21:44:56', 1399985191002447872, '2022-10-14 23:37:44', 3, 1);
INSERT INTO `iam_perm_menu` VALUES (1580928436300337152, 'adminv3', 1580740450633101312, '菜单管理', 'MenuList', NULL, b'0', '', b'0', b'0', '/modules/system/menu/MenuList.vue', NULL, '/system/menu', '', -99, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2022-10-14 22:28:06', 1399985191002447872, '2022-10-14 22:28:32', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582249924602580992, 'adminv3', 1580740450633101312, '权限管理', 'Permission', NULL, b'0', '', b'0', b'0', 'Layout', NULL, '/system/permission', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 13:59:13', 1414143554414059520, '2022-10-18 13:59:13', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1582253011803262976, 'adminv3', 1580740450633101312, '用户信息', 'UserAuth', NULL, b'0', '', b'0', b'0', 'Layout', NULL, '/system/user', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 14:11:30', 1414143554414059520, '2022-10-18 14:11:30', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1582253152903843840, 'adminv3', 1580740450633101312, '系统配置', 'SystemConfig', NULL, b'0', '', b'0', b'0', 'Layout', NULL, '/system/config', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 14:12:03', 1414143554414059520, '2022-10-18 14:12:03', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1582253306356649984, 'adminv3', 1580740450633101312, '认证管理', 'Auth', NULL, b'0', '', b'0', b'0', 'Layout', NULL, '/system/auth', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 14:12:40', 1414143554414059520, '2022-10-18 14:13:13', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582275875424129024, 'adminv3', NULL, '系统监控', 'Monitor', NULL, b'0', 'ant-design:monitor-outlined', b'0', b'0', 'Layout', NULL, '/monitor', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 15:42:21', 1414143554414059520, '2022-10-19 17:29:29', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582275984849326080, 'adminv3', NULL, '通知管理', 'Notice', NULL, b'0', 'ant-design:message-outlined', b'0', b'0', 'Layout', NULL, '/notice', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 15:42:47', 1414143554414059520, '2022-10-19 17:30:06', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582276092038959104, 'adminv3', NULL, '第三方对接', 'Third', NULL, b'0', 'ant-design:api-twotone', b'0', b'0', 'Layout', NULL, '/third', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 15:43:12', 1414143554414059520, '2022-10-19 17:32:04', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582276341792985088, 'adminv3', NULL, '开发管理', 'Develop', NULL, b'0', 'ant-design:apartment-outlined', b'0', b'0', 'Layout', NULL, '/develop', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 15:44:12', 1414143554414059520, '2022-10-19 15:24:22', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582276516905177088, 'adminv3', NULL, '功能演示', 'Demo', NULL, b'0', 'ant-design:appstore-twotone', b'0', b'0', 'Layout', NULL, '/demo', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 15:44:54', 1414143554414059520, '2022-10-19 17:34:26', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582277076421136384, 'adminv3', 1582249924602580992, '角色管理', 'RoleList', NULL, b'0', '', b'0', b'0', '/modules/system/role/RoleList.vue', NULL, '/system/permission/role', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 15:47:07', 1414143554414059520, '2022-10-18 15:59:37', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582301940364308480, 'adminv3', 1582249924602580992, '请求权限管理', 'PermPathList', NULL, b'0', '', b'0', b'0', '/modules/system/path/PermPathList.vue', NULL, '/system/permission/path', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 17:25:55', 1414143554414059520, '2022-10-18 20:41:22', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582302180999917568, 'adminv3', 1582249924602580992, '数据范围权限', 'DataScopeList', NULL, b'0', '', b'0', b'0', '/modules/system/scope/DataScopeList.vue', NULL, '/system/permission/scope', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 17:26:52', 1414143554414059520, '2022-10-18 17:26:52', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1582302542955769856, 'adminv3', 1582253011803262976, '用户管理', 'UserList', NULL, b'0', '', b'0', b'0', '/modules/system/user/UserList.vue', NULL, '/system/user/info', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 17:28:19', 1414143554414059520, '2022-10-18 17:28:19', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1582302764129808384, 'adminv3', 1582253011803262976, '部门管理', 'DeptList', NULL, b'0', '', b'0', b'0', '/modules/system/dept/DeptList.vue', NULL, '/system/user/dept', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 17:29:11', 1414143554414059520, '2022-10-18 17:32:26', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582303143110340608, 'adminv3', 1582253152903843840, '数据字典', 'DictList', NULL, b'0', '', b'0', b'0', '/modules/system/dict/DictList.vue', NULL, '/system/config/dict', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 17:30:42', 1414143554414059520, '2022-10-18 17:30:42', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1582303290070364160, 'adminv3', 1582253152903843840, '定时任务', 'QuartzJobList', NULL, b'0', '', b'0', b'0', '/modules/system/quartz/QuartzJobList.vue', NULL, '/system/config/quartz', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 17:31:17', 1414143554414059520, '2022-10-18 17:32:12', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582303447428067328, 'adminv3', 1582253152903843840, '系统参数', 'SystemParamList', NULL, b'0', '', b'0', b'0', '/modules/system/param/SystemParamList.vue', NULL, '/system/config/param', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-18 17:31:54', 1414143554414059520, '2022-10-19 23:14:16', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582632873244172288, 'adminv3', 1582276341792985088, '文件管理', 'FileUploadList', NULL, b'0', '', b'0', b'0', '/modules/develop/file/FileUploadList.vue', NULL, '/develop/file', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-19 15:20:56', 1414143554414059520, '2022-10-19 15:20:56', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1582633196587261952, 'adminv3', 1582276341792985088, '代码生成', 'CodeGenList', NULL, b'0', '', b'0', b'0', '/modules/develop/codegen/CodeGenList.vue', NULL, '/develop/codegen', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-19 15:22:13', 1414143554414059520, '2022-10-19 15:23:17', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1582633307786649600, 'adminv3', 1582276341792985088, '动态表单', 'DynamicFormList', NULL, b'0', '', b'0', b'0', '/modules/develop/dynamicform/DynamicFormList.vue', NULL, '/develop/form', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-19 15:22:39', 1414143554414059520, '2022-10-19 15:22:39', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1582633620321017856, 'adminv3', 1582276341792985088, '动态数据源', 'DynamicDataSourceList', NULL, b'0', '', b'0', b'0', '/modules/develop/dynamicsource/DynamicDataSourceList.vue', NULL, '/develop/source', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-19 15:23:54', 1414143554414059520, '2022-10-19 15:23:54', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1583074308040925184, 'adminv3', 1582275875424129024, '接口文档', 'ApiSwagger', NULL, b'0', '', b'0', b'0', '', NULL, 'http://127.0.0.1:9999/doc.html', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-20 20:35:02', 1414143554414059520, '2022-11-23 13:59:09', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1583075229563068416, 'adminv3', 1582275875424129024, '审计日志', 'AuditLog', NULL, b'0', '', b'0', b'0', 'Layout', NULL, '/monitor/log', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-20 20:38:42', 1414143554414059520, '2022-10-20 20:41:38', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1583076217481043968, 'adminv3', 1583075229563068416, '登录日志', 'LoginLogList', NULL, b'0', '', b'0', b'0', '/modules/monitor/login/LoginLogList.vue', NULL, '/monitor/log/login', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-20 20:42:37', 1414143554414059520, '2022-10-20 20:43:36', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1583076424935514112, 'adminv3', 1583075229563068416, '操作日志', 'OperateLogList', NULL, b'0', '', b'0', b'0', '/modules/monitor/operate/OperateLogList.vue', NULL, '/monitor/log/operate', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-20 20:43:26', 1414143554414059520, '2022-10-20 20:43:26', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1583076670881112064, 'adminv3', 1583075229563068416, '数据版本日志', 'DataVersionLogList', NULL, b'0', '', b'0', b'0', '/modules/monitor/data/DataVersionLogList.vue', NULL, '/monitor/log/data', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-20 20:44:25', 1414143554414059520, '2022-10-20 20:44:25', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1583076878956339200, 'adminv3', 1582275875424129024, 'ELK日志', 'ELK', NULL, b'0', '', b'0', b'0', '', NULL, 'http://elk.dev.bootx.cn:5601/app/discover', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-20 20:45:15', 1414143554414059520, '2022-11-21 15:04:13', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1583077015434797056, 'adminv3', 1582275875424129024, 'PlumeLog日志', 'PlumeLog', NULL, b'0', '', b'0', b'0', '', NULL, 'http://127.0.0.1:9999/plumelog/#/', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-20 20:45:47', 1414143554414059520, '2022-10-20 20:45:47', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1583077198772019200, 'adminv3', 1582275875424129024, '系统信息', 'SystemInfoMonitor', NULL, b'0', '', b'0', b'0', '/modules/monitor/system/SystemInfoMonitor.vue', NULL, '/monitor/sysinfo', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-20 20:46:31', 1414143554414059520, '2022-10-20 20:46:31', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1583077360827342848, 'adminv3', 1582275875424129024, 'Redis监控', 'RedisInfoMonitor', NULL, b'0', '', b'0', b'0', '/modules/monitor/redis/RedisInfoMonitor.vue', NULL, '/monitor/redis', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-20 20:47:10', 1414143554414059520, '2022-10-20 20:47:10', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1584378294652051456, 'adminv3', 1582275984849326080, '邮件配置', 'MailConfigList', NULL, b'0', '', b'0', b'0', '/modules/notice/mail/MailConfigList.vue', NULL, '/notice/notice', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 10:56:36', 1414143554414059520, '2022-10-24 16:14:34', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1584378497824137216, 'adminv3', 1582275984849326080, '消息模板', 'MessageTemplateList', NULL, b'0', '', b'0', b'0', '/modules/notice/template/MessageTemplateList.vue', NULL, '/notice/template', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 10:57:25', 1414143554414059520, '2022-10-25 22:14:14', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1584378671266996224, 'adminv3', 1582275984849326080, '站内信', 'SiteMessageList', NULL, b'0', '', b'0', b'0', '/modules/notice/site/sender/SiteMessageList.vue', NULL, '/notice/siteMessage', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 10:58:06', 1414143554414059520, '2022-10-24 10:58:06', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1584379602188574720, 'adminv3', 1582276092038959104, '微信', 'WeChat', NULL, b'0', '', b'0', b'0', 'Layout', NULL, '/third/wechat', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 11:01:48', 1414143554414059520, '2022-10-24 11:01:48', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1584379704122744832, 'adminv3', 1582276092038959104, '企业微信', 'WeCom', NULL, b'0', '', b'0', b'0', 'Layout', NULL, '/third/wecom', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 11:02:12', 1414143554414059520, '2022-10-24 11:02:12', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1584380087805091840, 'adminv3', 1582276092038959104, '钉钉', 'DingTalk', NULL, b'0', '', b'0', b'0', 'Layout', NULL, '/third/dingtalk', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 11:03:44', 1414143554414059520, '2022-10-24 11:03:44', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1584380527829524480, 'adminv3', 1584379602188574720, '消息模板', 'WechatTemplateList', NULL, b'0', '', b'0', b'0', '/modules/third/wechat/template/WechatTemplateList.vue', NULL, '/third/wechat/template', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 11:05:29', 1414143554414059520, '2022-10-26 15:58:56', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1584380679478779904, 'adminv3', 1584379602188574720, '自定义菜单', 'WechatMenuList', NULL, b'0', '', b'0', b'0', '/modules/third/wechat/menu/WechatMenuList.vue', NULL, '/third/wechat/menu', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 11:06:05', 1414143554414059520, '2022-10-27 10:15:24', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1584380824308097024, 'adminv3', 1584379602188574720, '素材管理', 'WechatMediaList', NULL, b'0', '', b'0', b'0', '/modules/third/wechat/media/WechatMediaList.vue', NULL, '/third/wechat/media', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 11:06:40', 1414143554414059520, '2022-10-27 16:38:47', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1584381134950834176, 'adminv3', 1584379704122744832, '企微机器人', 'WeComRobotConfigList', NULL, b'0', '', b'0', b'0', '/modules/third/wecom/robot/WecomRobotConfigList.vue', NULL, '/third/wecom/robot', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 11:07:54', 1414143554414059520, '2022-11-12 20:58:25', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1584381322184564736, 'adminv3', 1584380087805091840, '钉钉机器人', 'DingRobotConfigList', NULL, b'0', '', b'0', b'0', '/modules/third/dingtalk/robot/DingRobotConfigList.vue', NULL, '/third/dingTalk/robot', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 11:08:38', 1414143554414059520, '2022-11-12 20:58:37', 6, 0);
INSERT INTO `iam_perm_menu` VALUES (1584381477986181120, 'adminv3', 1584380087805091840, '钉钉配置', 'DingRobotConfigList', NULL, b'0', '', b'1', b'0', '/modules/third/dingtalk/config/DingTalkConfigList.vue', NULL, '/third/dingtalk/config', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-24 11:09:15', 1414143554414059520, '2022-11-11 16:04:47', 1, 1);
INSERT INTO `iam_perm_menu` VALUES (1597044371008516096, 'adminv3', NULL, '功能演示', 'Demo', NULL, b'0', '', b'0', b'0', 'Layout', NULL, '/demo', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-11-28 09:47:04', 1414143554414059520, '2022-11-28 09:47:04', 0, 1);
INSERT INTO `iam_perm_menu` VALUES (1597102799370317824, 'adminv3', 1582276516905177088, '数据相关', 'DemoData', NULL, b'0', '', b'0', b'0', 'Layout', NULL, '/demo/data', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-11-28 13:39:15', 1414143554414059520, '2022-11-28 13:39:15', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1597210969883275264, 'adminv3', 1582276516905177088, '超级查询', 'SuperQueryDemoList', NULL, b'0', '', b'0', b'0', '/modules/demo/query/SuperQueryDemoList.vue', NULL, '/demo/query/super', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-11-28 20:49:05', 1414143554414059520, '2022-11-28 21:00:14', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1599337250200440832, 'adminv3', NULL, '关于', '', NULL, b'0', 'simple-icons:about-dot-me', b'0', b'0', '', NULL, '/about/index', '', 99, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-12-04 17:38:09', 1414143554414059520, '2022-12-04 17:43:32', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1599378494880436224, 'adminv3', 1582276516905177088, 'WS演示', 'WebsocketDemo', NULL, b'0', '', b'0', b'0', '/modules/demo/ws/WebsocketDemo', NULL, '/demo/ws', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-12-04 20:22:03', 1414143554414059520, '2023-02-08 12:17:58', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1599378579513102336, 'adminv3', 1582276516905177088, '幂等请求演示', 'IdempotentDemo', NULL, b'0', '', b'0', b'0', '/modules/demo/idempotent/IdempotentDemo', NULL, '/demo/idempotent', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-12-04 20:22:23', 1414143554414059520, '2023-02-08 11:46:00', 3, 0);
INSERT INTO `iam_perm_menu` VALUES (1599378728490586112, 'adminv3', 1582276516905177088, '消息中间件演示', 'MqDemo', NULL, b'0', '', b'0', b'0', '/modules/demo/mq/MqDemo', NULL, '/demo/mq', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-12-04 20:22:59', 1414143554414059520, '2023-02-08 22:09:04', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1599378838519762944, 'adminv3', 1582276516905177088, '富文本编辑', 'WangEditorDemo', NULL, b'0', '', b'0', b'0', '/modules/demo/wangeditor/WangEditorDemo.vue', NULL, '/demo/wangEditor', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-12-04 20:23:25', 1414143554414059520, '2023-02-10 09:05:16', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1617847577158324224, 'adminv3', 1597102799370317824, '数据权限', 'DataPermDemoList', NULL, b'0', '', b'0', b'0', '/modules/demo/data/perm/DataPermDemoList.vue', NULL, '/demo/data/perm', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-01-24 19:31:35', 1414143554414059520, '2023-01-24 20:06:04', 2, 0);
INSERT INTO `iam_perm_menu` VALUES (1617847653746315264, 'adminv3', 1597102799370317824, '加密解密', 'DataEncryptDemoList', NULL, b'0', '', b'0', b'0', '/modules/demo/data/encrypt/DataEncryptDemoList.vue', NULL, '/demo/data/encrypt', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-01-24 19:31:53', 1414143554414059520, '2023-01-24 20:05:45', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1617847747375763456, 'adminv3', 1597102799370317824, '数据脱敏', 'DataSensitiveDemoList', NULL, b'0', '', b'0', b'0', '/modules/demo/data/sensitive/DataSensitiveDemoList.vue', NULL, '/demo/data/sensitive', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-01-24 19:32:16', 1414143554414059520, '2023-01-24 20:06:58', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1621150743447965696, 'admin', 1495968302034210816, '省市区联动', 'ChinaRegionDemo', NULL, b'0', '', b'0', b'0', 'demo/chinaregion/ChinaRegionDemo', NULL, '/demo/chinaregion', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2023-02-02 22:17:11', 1399985191002447872, '2023-02-08 09:43:39', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1623156857846034432, 'adminv3', 1582276516905177088, '省市区联动', 'ChinaRegionDemo', NULL, b'0', '', b'0', b'0', '/modules/demo/chinaregion/ChinaRegionDemo', NULL, '/demo/chinaregion', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-02-08 11:08:46', 1414143554414059520, '2023-02-08 11:08:46', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1623325795944439808, 'adminv3', 1599378992811429888, '邮件通知', 'EmailSenderDemo', NULL, b'0', '', b'0', b'0', '/modules/demo/notice/email/EmailSenderDemo.vue', NULL, '/demo/notice/email', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-02-08 22:20:04', 1414143554414059520, '2023-02-20 10:38:55', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1623494586215579648, 'admin', 1552207982510706688, '行政区划', 'ChinaRegion', NULL, b'0', '', b'1', b'0', 'develop/region/ChinaRegionList', NULL, '/develop/region', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2023-02-09 09:30:47', 1399985191002447872, '2023-02-09 17:50:05', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1629039360928075776, 'adminv3', 1582276341792985088, '可视化大屏', 'ProjectInfoList', NULL, b'0', '', b'0', b'0', '/modules/develop/report/ProjectInfoList', NULL, '/develop/report', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-02-24 16:43:44', 1414143554414059520, '2023-02-24 16:44:17', 1, 0);
INSERT INTO `iam_perm_menu` VALUES (1631946120891707392, 'admin', 1552207982510706688, '可视化大屏', 'ProjectInfoList', NULL, b'0', '', b'0', b'0', 'develop/report/ProjectInfoList', NULL, '/develop/report', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2023-03-04 17:14:10', 1399985191002447872, '2023-03-04 17:14:10', 0, 0);
INSERT INTO `iam_perm_menu` VALUES (1635274568758435840, 'adminv3', 1582276341792985088, 'SQL查询语句', 'QuerySqlList', NULL, b'0', '', b'0', b'0', '/modules/develop/query/QuerySqlList', NULL, '/develop/querySql', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-03-13 21:40:14', 1414143554414059520, '2023-03-13 21:43:05', 2, 0);

-- ----------------------------
-- Table structure for iam_perm_path
-- ----------------------------
DROP TABLE IF EXISTS `iam_perm_path`;
CREATE TABLE `iam_perm_path`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限标识',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `request_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求类型',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求路径',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分组名称',
  `enable` bit(1) NOT NULL COMMENT '启用状态',
  `generate` bit(1) NOT NULL COMMENT '是否通过系统生成的权限',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `creator` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `last_modifier` bigint(20) NULL DEFAULT NULL,
  `last_modified_time` datetime(6) NULL DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限_请求' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_perm_path
-- ----------------------------

-- ----------------------------
-- Table structure for iam_role
-- ----------------------------
DROP TABLE IF EXISTS `iam_role`;
CREATE TABLE `iam_role`  (
  `id` bigint(20) NOT NULL COMMENT '角色ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `internal` bit(1) NOT NULL COMMENT '是否系统内置',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '说明',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_role
-- ----------------------------
INSERT INTO `iam_role` VALUES (1405414804771971072, 'admin', '管理员', b'1', '管理员', 1, '2021-06-17 14:39:35', 1399985191002447872, '2021-07-18 22:31:02', 6, 0);
INSERT INTO `iam_role` VALUES (1416730722714144768, 'test', '测试', b'0', '测试角色', 1399985191002447872, '2021-07-18 20:05:01', 1399985191002447872, '2021-07-18 20:16:15', 1, 0);
INSERT INTO `iam_role` VALUES (1422832797731778562, 'user', '用户', b'0', '用户角色', 0, '2021-08-04 16:12:29', 1399985191002447872, '2021-08-04 16:15:03', 7, 1);
INSERT INTO `iam_role` VALUES (1428891259564445696, 'manager', '管理者', b'0', 'manager管理者', 1399985191002447872, '2021-08-21 09:26:38', 1399985191002447872, '2021-08-21 09:26:39', 0, 1);

-- ----------------------------
-- Table structure for iam_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `iam_role_menu`;
CREATE TABLE `iam_role_menu`  (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `client_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '终端code',
  `permission_id` bigint(20) NOT NULL COMMENT '菜单权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for iam_role_path
-- ----------------------------
DROP TABLE IF EXISTS `iam_role_path`;
CREATE TABLE `iam_role_path`  (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) NOT NULL COMMENT '请求权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色请求权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_role_path
-- ----------------------------

-- ----------------------------
-- Table structure for iam_user_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `iam_user_data_scope`;
CREATE TABLE `iam_user_data_scope`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `data_scope_id` bigint(20) NOT NULL COMMENT '数据权限ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户数据范围关系\r\n' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_user_data_scope
-- ----------------------------
INSERT INTO `iam_user_data_scope` VALUES (1477617820165345280, 1414143554414059520, 1474717084985270272);
INSERT INTO `iam_user_data_scope` VALUES (1477991040840290304, 1477990832987361280, 1477990439800721408);
INSERT INTO `iam_user_data_scope` VALUES (1477997504506077184, 1477997391729631232, 1477990268903804928);
INSERT INTO `iam_user_data_scope` VALUES (1477997685993611264, 1477997602862505984, 1474706893178871808);

-- ----------------------------
-- Table structure for iam_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `iam_user_dept`;
CREATE TABLE `iam_user_dept`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `dept_id` bigint(20) NOT NULL COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_user_dept
-- ----------------------------
INSERT INTO `iam_user_dept` VALUES (1450088892861501440, 1399985191002447872, 1259383345604300802);
INSERT INTO `iam_user_dept` VALUES (1477996765012533248, 1477990832987361280, 1477977592291053568);
INSERT INTO `iam_user_dept` VALUES (1477997463997489152, 1477997391729631232, 1477978610865197056);
INSERT INTO `iam_user_dept` VALUES (1477997655618461696, 1477997602862505984, 1477978610865197056);
INSERT INTO `iam_user_dept` VALUES (1478741775446118400, 1435967884114194432, 1477978810526650368);
INSERT INTO `iam_user_dept` VALUES (1478741775450312704, 1435967884114194432, 1477978464559484928);
INSERT INTO `iam_user_dept` VALUES (1478741775450312705, 1435967884114194432, 1477978610865197056);

-- ----------------------------
-- Table structure for iam_user_expand_info
-- ----------------------------
DROP TABLE IF EXISTS `iam_user_expand_info`;
CREATE TABLE `iam_user_expand_info`  (
  `id` bigint(20) NOT NULL,
  `sex` int(4) NULL DEFAULT NULL COMMENT '性别',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '上次登录时间',
  `current_login_time` datetime(0) NULL DEFAULT NULL COMMENT '本次登录时间',
  `initial_password` bit(1) NOT NULL COMMENT '是否初始密码',
  `last_change_password_time` datetime(0) NULL DEFAULT NULL COMMENT '上次修改密码时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户扩展信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_user_expand_info
-- ----------------------------
INSERT INTO `iam_user_expand_info` VALUES (1399985191002447872, 1, '1996-12-01', '1495331905770315776', '2022-10-29 08:44:52', '2022-11-02 09:59:06', b'0', '2022-06-19 21:25:00', 1, '2021-06-02 15:04:15', 0, '2022-11-02 09:59:06', 367, 0);
INSERT INTO `iam_user_expand_info` VALUES (1414143554414059520, 1, '2022-10-31', '1586953599627546624', '2022-11-02 21:32:23', '2022-11-02 21:33:21', b'0', NULL, 1, '2021-07-11 16:44:32', 0, '2022-11-02 21:33:21', 62, 0);
INSERT INTO `iam_user_expand_info` VALUES (1435894470432456704, 1, NULL, NULL, '2022-09-16 16:48:16', '2022-09-17 17:20:58', b'0', '2022-06-29 00:39:23', 1399985191002447872, '2021-09-09 17:14:54', 0, '2022-09-17 17:20:58', 5, 0);
INSERT INTO `iam_user_expand_info` VALUES (1435967884114194432, 1, NULL, NULL, NULL, '2022-05-31 15:59:42', b'0', NULL, 1414143554414059520, '2021-09-09 22:06:37', 0, '2022-05-31 15:59:42', 1, 0);
INSERT INTO `iam_user_expand_info` VALUES (1477990832987361280, 1, NULL, NULL, NULL, NULL, b'0', NULL, 1399985191002447872, '2022-01-03 21:10:49', 1399985191002447872, '2022-01-03 21:10:49', 0, 0);
INSERT INTO `iam_user_expand_info` VALUES (1477997391729631232, 1, NULL, NULL, NULL, NULL, b'0', NULL, 1399985191002447872, '2022-01-03 21:36:53', 1399985191002447872, '2022-01-03 21:36:53', 0, 0);
INSERT INTO `iam_user_expand_info` VALUES (1477997602862505984, 1, NULL, NULL, NULL, '2022-06-01 16:51:46', b'0', NULL, 1399985191002447872, '2022-01-03 21:37:43', 0, '2022-06-01 16:51:46', 1, 0);

-- ----------------------------
-- Table structure for iam_user_info
-- ----------------------------
DROP TABLE IF EXISTS `iam_user_info`;
CREATE TABLE `iam_user_info`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `client_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联终端ds',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '注册来源',
  `admin` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否超级管理员',
  `register_time` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
  `status` tinyint(4) NOT NULL COMMENT '账号状态',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_user_info
-- ----------------------------
INSERT INTO `iam_user_info` VALUES (1399985191002447872, '小小明', 'xxm', 'f52020dca765fd3943ed40a615dc2c5c', '133****3333', 'x******@outlook.com', '1430430071299207168,1430430071299207169,1626840094767714304,1580487061605175296', NULL, NULL, b'1', '2021-06-02 15:04:12', 1, 1, '2021-06-02 15:04:15', 1414143554414059520, '2023-02-18 15:08:42', 55, 0);
INSERT INTO `iam_user_info` VALUES (1414143554414059520, '小小明1995', 'xxm1995', 'f52020dca765fd3943ed40a615dc2c5c', '130****0000', 'x******@foxmail.com', '1430430071299207168,1580487061605175296,1430430071299207169,1626840094767714304', NULL, NULL, b'1', '2021-07-11 16:44:31', 1, 1, '2021-07-11 16:44:32', 1414143554414059520, '2023-02-18 15:08:48', 9, 0);
INSERT INTO `iam_user_info` VALUES (1435894470432456704, '管理员', 'admin', 'f52020dca765fd3943ed40a615dc2c5c', '13000001111', 'admin@qq.com', '1430430071299207168', '', NULL, b'0', '2021-09-09 17:14:52', 1, 1399985191002447872, '2021-09-09 17:14:54', 1399985191002447872, '2022-09-16 16:41:51', 7, 0);
INSERT INTO `iam_user_info` VALUES (1435967884114194432, '测试', 'test', 'f52020dca765fd3943ed40a615dc2c5c', '13311111111', 'test@qq.com', '1430430071299207168', '', NULL, b'0', '2021-09-09 22:06:37', 1, 1414143554414059520, '2021-09-09 22:06:37', 1399985191002447872, '2022-05-31 15:59:37', 9, 0);
INSERT INTO `iam_user_info` VALUES (1477990832987361280, '测试用户001', 'test001', 'f52020dca765fd3943ed40a615dc2c5c', '', '', '1430430071299207168', '', NULL, b'0', '2022-01-03 21:10:49', 1, 1399985191002447872, '2022-01-03 21:10:49', 1399985191002447872, '2022-06-05 20:26:26', 1, 0);
INSERT INTO `iam_user_info` VALUES (1477997391729631232, '测试用户002', 'test002', 'f52020dca765fd3943ed40a615dc2c5c', '', '', '1430430071299207168', '', NULL, b'0', '2022-01-03 21:36:53', 1, 1399985191002447872, '2022-01-03 21:36:53', 1399985191002447872, '2022-06-05 20:26:19', 1, 0);
INSERT INTO `iam_user_info` VALUES (1477997602862505984, '测试用户003', 'test003', 'f52020dca765fd3943ed40a615dc2c5c', '', '', '1430430071299207168', '', NULL, b'0', '2022-01-03 21:37:43', 1, 1399985191002447872, '2022-01-03 21:37:43', 1399985191002447872, '2022-05-19 13:00:05', 4, 0);

-- ----------------------------
-- Table structure for iam_user_role
-- ----------------------------
DROP TABLE IF EXISTS `iam_user_role`;
CREATE TABLE `iam_user_role`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关系\r\n' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_user_role
-- ----------------------------
INSERT INTO `iam_user_role` VALUES (1533038443724980224, 1435894470432456704, 1405414804771971072);

-- ----------------------------
-- Table structure for iam_user_third
-- ----------------------------
DROP TABLE IF EXISTS `iam_user_third`;
CREATE TABLE `iam_user_third`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `we_chat_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信openId',
  `we_chat_open_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信开放平台id',
  `qq_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'qqId',
  `weibo_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微博Id',
  `gitee_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '码云唯一标识',
  `ding_talk_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '钉钉唯一标识',
  `we_com_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业微信唯一标识',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pk_user_index`(`user_id`) USING BTREE COMMENT '用户id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户三方登录绑定' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_user_third
-- ----------------------------

-- ----------------------------
-- Table structure for iam_user_third_info
-- ----------------------------
DROP TABLE IF EXISTS `iam_user_third_info`;
CREATE TABLE `iam_user_third_info`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `client_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '第三方终端类型',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户头像',
  `third_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '关联第三方平台的用户id',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pk_user_client`(`user_id`, `client_code`) USING BTREE COMMENT '用户id和终端code'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户三方登录绑定详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iam_user_third_info
-- ----------------------------

-- ----------------------------
-- Table structure for notice_mail_config
-- ----------------------------
DROP TABLE IF EXISTS `notice_mail_config`;
CREATE TABLE `notice_mail_config`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `host` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱服务器host',
  `port` int(5) NOT NULL COMMENT '邮箱服务器 port',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱服务器 username',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱服务器 password',
  `sender` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱服务器 sender',
  `from_` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱服务器 from',
  `activity` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认配置，0:否。1:是',
  `security_type` int(2) NULL DEFAULT NULL COMMENT '安全传输方式 1:plain 2:tls 3:ssl',
  `creator` bigint(18) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(18) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  `version` int(8) NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '邮件配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice_mail_config
-- ----------------------------
INSERT INTO `notice_mail_config` VALUES (1554739296333955072, 'fox', 'foxmail邮箱', 'smtp.qq.com', 465, 'sJfAJDDviYlqZ3BtdjMZF8V5jVSYCaMa9DNdVGDbe/s=', '7AtgVwObaO7wrsRpLvKkoo5O+udeEcFP1ONq4gYwOj0=', 'bootx-platform平台', 'xxm1995@foxmail.com', 1, 3, 1399985191002447872, '2022-08-03 16:01:49', 1399985191002447872, '2022-08-03 16:14:55', 0, 8);
INSERT INTO `notice_mail_config` VALUES (1584814372311744512, '11', '22', '33', 465, 'pwfAgEMJjGLjbVYEcgdXzA==', 'f3zJMwbPGmNRlNXpN5AMyA==', '666', '33333333@foxmail.com', 0, 1, 1414143554414059520, '2022-10-25 15:49:25', 1414143554414059520, '2022-10-25 16:27:12', 0, 10);

-- ----------------------------
-- Table structure for notice_message_template
-- ----------------------------
DROP TABLE IF EXISTS `notice_message_template`;
CREATE TABLE `notice_message_template`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '模板数据',
  `type` int(11) NULL DEFAULT NULL COMMENT '模板类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息模板' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice_message_template
-- ----------------------------
INSERT INTO `notice_message_template` VALUES (1424936204932169730, 'cs', '测试', 'hello ${msg}6666666666666666666666666666', 1, '测试模板', 0, '2021-08-10 11:30:40', 0, '2021-08-10 11:30:40', 0, 0);
INSERT INTO `notice_message_template` VALUES (1573951326893510656, 'BpmTaskCreated', '流程任务创建事件', '流程任务创建事件', 0, '', 1399985191002447872, '2022-09-25 16:23:34', 1399985191002447872, '2022-09-25 16:23:34', 0, 0);
INSERT INTO `notice_message_template` VALUES (1573951515616219136, 'BpmTaskAssignCreated', '流程任务更改处理人事件(新处理人)', '流程任务更改处理人事件(新处理人)', 0, '', 1399985191002447872, '2022-09-25 16:24:19', 1399985191002447872, '2022-09-25 16:27:09', 1, 0);
INSERT INTO `notice_message_template` VALUES (1573952505056727040, 'BpmTaskAssignCancel', '流程任务更改处理人事件(原处理人)', '流程任务更改处理人事件(原处理人)', 0, '', 1399985191002447872, '2022-09-25 16:28:14', 1399985191002447872, '2022-09-25 16:28:14', 0, 0);
INSERT INTO `notice_message_template` VALUES (1573952568654958592, 'BpmTaskCancel', '流程任务取消事件', '流程任务取消事件', 0, '', 1399985191002447872, '2022-09-25 16:28:30', 1399985191002447872, '2022-09-25 16:28:30', 0, 0);
INSERT INTO `notice_message_template` VALUES (1573952621826150400, 'BpmTaskReject', '流程任务驳回事件', '流程任务驳回事件', 0, '', 1399985191002447872, '2022-09-25 16:28:42', 1399985191002447872, '2022-09-25 16:28:42', 0, 0);
INSERT INTO `notice_message_template` VALUES (1573952709432578048, 'BpmInstanceCompleted', '流程完成时通知发起人', '流程完成时通知发起人', 0, '', 1399985191002447872, '2022-09-25 16:29:03', 1399985191002447872, '2022-09-25 16:29:03', 0, 0);
INSERT INTO `notice_message_template` VALUES (1573952762507300864, 'BpmInstanceCancel', '流程取消时通知发起人', '流程取消时通知发起人', 0, '', 1399985191002447872, '2022-09-25 16:29:16', 1399985191002447872, '2022-09-25 16:29:16', 0, 0);
INSERT INTO `notice_message_template` VALUES (1580083314274070528, 'BpmTaskBack', '流程任务回退事件', '流程任务驳回事件', 0, '', 1399985191002447872, '2022-10-12 14:29:53', 1399985191002447872, '2022-10-12 14:30:11', 1, 0);

-- ----------------------------
-- Table structure for notice_site_message
-- ----------------------------
DROP TABLE IF EXISTS `notice_site_message`;
CREATE TABLE `notice_site_message`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息内容',
  `sender_id` bigint(20) NULL DEFAULT NULL COMMENT '发送者id',
  `sender_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送者姓名',
  `sender_time` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `receive_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息类型',
  `send_state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布状态',
  `efficient_time` datetime(0) NULL DEFAULT NULL COMMENT '截至有效期',
  `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '撤回时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '站内信' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice_site_message
-- ----------------------------
INSERT INTO `notice_site_message` VALUES (1424212599079161857, '测试消息', '<div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >55</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled ></div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >6等非撒扥森<strong>速度扥三扥所</strong></div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >6</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >8</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >8</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5<span style=\"background-color: rgb(255, 169, 64);\">撒扥森森的</span></div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >5</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled ><span style=\"font-family: 华文楷体;\">5撒扥岁送</span></div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled ></div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled ></div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >8</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >8</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled >8</div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled ></div>', 1399985191002447872, '小小明', '2021-08-08 11:34:11', 'all', 'cancel', NULL, '2022-08-21 23:01:16', 1399985191002447872, '2021-08-08 11:35:19', 1399985191002447872, '2022-08-21 23:01:16', 1, 0);
INSERT INTO `notice_site_message` VALUES (1558781525200130048, '测试站内信', '<blockquote><span style=\"font-size: 24px;\"><strong>sdfsdfsdf</strong></span></blockquote><ol><li>333</li></ol><ul><li>4444</li><li>3443434</li></ul>', 0, '未知', '2022-08-14 19:44:11', NULL, 'sent', NULL, NULL, 0, '2022-08-14 19:44:11', 0, '2022-08-14 19:44:11', 0, 0);
INSERT INTO `notice_site_message` VALUES (1561363288741085184, '测试消息', '<p>三扥广丰和扥撒扥撒扥东方</p>', 1399985191002447872, '小小明', '2022-08-21 22:59:45', 'all', 'draft', '2022-09-20 00:00:00', NULL, 1399985191002447872, '2022-08-21 22:43:11', 1399985191002447872, '2022-08-21 23:23:03', 4, 1);
INSERT INTO `notice_site_message` VALUES (1561365894804766720, '测试数据', '<p>234554通扥广森</p>', 1399985191002447872, '小小明', '2022-08-21 23:48:55', 'all', 'sent', '2022-09-20 00:00:00', NULL, 1399985191002447872, '2022-08-21 22:53:33', 1399985191002447872, '2022-08-21 23:48:55', 1, 0);
INSERT INTO `notice_site_message` VALUES (1561368170558623744, '测试数据', '<p>234554通扥广森</p><h3>DFF</h3><p><span style=\"background-color: rgb(225, 60, 57);\">撒扥萨芬的</span></p><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled ><span style=\"background-color: rgb(225, 60, 57);\">11</span></div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled ><span style=\"background-color: rgb(225, 60, 57);\">33</span></div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled ><span style=\"background-color: rgb(225, 60, 57);\">44</span></div><div data-w-e-type=\"todo\"><input type=\"checkbox\" disabled ><span style=\"background-color: rgb(225, 60, 57);\">风很高</span></div>', 1399985191002447872, '小小明', '2022-08-21 23:33:01', 'all', 'sent', '2022-09-20 00:00:00', NULL, 1399985191002447872, '2022-08-21 23:02:35', 1399985191002447872, '2022-08-21 23:33:01', 7, 0);

-- ----------------------------
-- Table structure for notice_site_message_user
-- ----------------------------
DROP TABLE IF EXISTS `notice_site_message_user`;
CREATE TABLE `notice_site_message_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `message_id` bigint(20) NOT NULL COMMENT '消息id',
  `receive_id` bigint(20) NOT NULL COMMENT '接收者id',
  `have_read` bit(1) NOT NULL COMMENT '已读/未读',
  `read_time` datetime(0) NULL DEFAULT NULL COMMENT '已读时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_receive_message`(`receive_id`, `message_id`) USING BTREE COMMENT '接收人和消息联合索引',
  INDEX `inx_message`(`message_id`) USING BTREE COMMENT '消息索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '消息用户关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice_site_message_user
-- ----------------------------
INSERT INTO `notice_site_message_user` VALUES (1558781525422428160, 1558781525200130048, 1399985191002447872, b'1', '2022-08-14 19:47:53', 0, '2022-08-14 19:44:11', 0);
INSERT INTO `notice_site_message_user` VALUES (1559917646856540160, 1424212599079161857, 1399985191002447872, b'1', '2022-08-17 22:58:43', 1399985191002447872, '2022-08-17 22:58:44', 0);
INSERT INTO `notice_site_message_user` VALUES (1561379790089302016, 1561368170558623744, 1399985191002447872, b'1', '2022-08-21 23:48:43', 1399985191002447872, '2022-08-21 23:48:46', 0);
INSERT INTO `notice_site_message_user` VALUES (1561379841968648192, 1561365894804766720, 1399985191002447872, b'1', '2022-08-21 23:48:58', 1399985191002447872, '2022-08-21 23:48:58', 0);

-- ----------------------------
-- Table structure for notice_sms_channel_config
-- ----------------------------
DROP TABLE IF EXISTS `notice_sms_channel_config`;
CREATE TABLE `notice_sms_channel_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道编码',
  `signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信签名',
  `api_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信ApiKey',
  `api_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信Api秘钥',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调地址',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '乐观锁',
  `deleted` bit(1) NOT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信渠道配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice_sms_channel_config
-- ----------------------------

-- ----------------------------
-- Table structure for notice_sms_config
-- ----------------------------
DROP TABLE IF EXISTS `notice_sms_config`;
CREATE TABLE `notice_sms_config`  (
  `id` bigint(18) NOT NULL,
  `tid` bigint(18) NOT NULL COMMENT '租户id',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `account_sid` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `path_sid` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发送号码的唯一标识(基于twillio的命名风格)',
  `auth_token` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `from_num` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认配置，0:否。1:是',
  `creator` bigint(18) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `last_modifier` bigint(18) NULL DEFAULT NULL,
  `last_modified_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  `secret` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `isp` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `reply_msg` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '短信配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice_sms_config
-- ----------------------------

-- ----------------------------
-- Table structure for notice_wechat_config
-- ----------------------------
DROP TABLE IF EXISTS `notice_wechat_config`;
CREATE TABLE `notice_wechat_config`  (
  `id` bigint(18) NOT NULL,
  `tid` bigint(18) NOT NULL COMMENT '租户id',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `corp_id` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `corp_secret` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认配置，0:否。1:是',
  `creator` bigint(18) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `last_modifier` bigint(18) NULL DEFAULT NULL,
  `last_modified_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '微信消息配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice_wechat_config
-- ----------------------------
INSERT INTO `notice_wechat_config` VALUES (181361815405135421, 0, 'test01', 'test01', 'ww9d6247559117d202', '8n6A3SzN-DJNkw8wyCcJnr8-SOjFFWSOlBqZN8vypKM', 1, 1415, '2018-11-20 11:07:07', 1415, '2018-11-20 11:07:07', 0, 0);

-- ----------------------------
-- Table structure for pay_ali_payment
-- ----------------------------
DROP TABLE IF EXISTS `pay_ali_payment`;
CREATE TABLE `pay_ali_payment`  (
  `id` bigint(20) NOT NULL,
  `payment_id` bigint(20) NULL DEFAULT NULL COMMENT '交易记录ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `amount` decimal(19, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `refundable_balance` decimal(19, 2) NULL DEFAULT NULL COMMENT '可退款余额',
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `pay_status` int(11) NULL DEFAULT NULL COMMENT '支付状态',
  `pay_time` datetime(6) NULL DEFAULT NULL COMMENT '支付时间',
  `trade_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝关联流水号',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(6) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本',
  `deleted` bit(1) NOT NULL COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付宝支付记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_ali_payment
-- ----------------------------

-- ----------------------------
-- Table structure for pay_alipay_config
-- ----------------------------
DROP TABLE IF EXISTS `pay_alipay_config`;
CREATE TABLE `pay_alipay_config`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `app_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付宝商户appId',
  `notify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器异步通知页面路径',
  `return_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面跳转同步通知页面路径',
  `server_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求网关地址',
  `auth_type` int(4) NOT NULL COMMENT '认证方式',
  `sign_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名类型',
  `alipay_public_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '支付宝公钥',
  `private_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '私钥',
  `app_cert` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '应用公钥',
  `alipay_cert` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '支付宝公钥证书',
  `alipay_root_cert` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '支付宝CA根证书',
  `sandbox` bit(1) NOT NULL COMMENT '是否沙箱环境',
  `expire_time` int(10) NOT NULL COMMENT '超时配置',
  `pay_ways` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支持的支付类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `activity` bit(1) NOT NULL COMMENT '是否启用',
  `state` int(11) NOT NULL COMMENT '状态',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付宝配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_alipay_config
-- ----------------------------

-- ----------------------------
-- Table structure for pay_cash_payment
-- ----------------------------
DROP TABLE IF EXISTS `pay_cash_payment`;
CREATE TABLE `pay_cash_payment`  (
  `id` bigint(20) NOT NULL,
  `payment_id` bigint(20) NOT NULL COMMENT '支付id',
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `amount` decimal(19, 2) NULL DEFAULT NULL COMMENT '金额',
  `refundable_balance` decimal(19, 2) NULL DEFAULT NULL COMMENT '可退款金额',
  `pay_status` int(11) NULL DEFAULT NULL COMMENT '支付状态',
  `pay_time` datetime(6) NULL DEFAULT NULL COMMENT '支付时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '现金交易记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_cash_payment
-- ----------------------------

-- ----------------------------
-- Table structure for pay_pay_notify_record
-- ----------------------------
DROP TABLE IF EXISTS `pay_pay_notify_record`;
CREATE TABLE `pay_pay_notify_record`  (
  `id` bigint(20) NOT NULL,
  `payment_id` bigint(20) NOT NULL COMMENT '支付号',
  `notify_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知消息',
  `pay_channel` int(11) NOT NULL COMMENT '支付通道',
  `status` int(2) NOT NULL COMMENT '处理状态',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提示信息',
  `notify_time` datetime(6) NULL DEFAULT NULL COMMENT '回调时间',
  `creator` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `last_modifier` bigint(20) NULL DEFAULT NULL,
  `last_modified_time` datetime(6) NULL DEFAULT NULL,
  `version` int(11) NOT NULL,
  `deleted` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付回调记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_pay_notify_record
-- ----------------------------

-- ----------------------------
-- Table structure for pay_pay_order_log
-- ----------------------------
DROP TABLE IF EXISTS `pay_pay_order_log`;
CREATE TABLE `pay_pay_order_log`  (
  `id` bigint(20) NOT NULL,
  `payment_id` bigint(20) NOT NULL COMMENT '支付id',
  `business_pay_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '订单扩展业务参数',
  `pay_order_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '订单参数',
  `creator` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `last_modifier` bigint(20) NULL DEFAULT NULL,
  `last_modified_time` datetime(6) NULL DEFAULT NULL,
  `version` int(11) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `tid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付关联订单信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_pay_order_log
-- ----------------------------

-- ----------------------------
-- Table structure for pay_payment
-- ----------------------------
DROP TABLE IF EXISTS `pay_payment`;
CREATE TABLE `pay_payment`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `business_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `description` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `amount` decimal(19, 2) NOT NULL COMMENT '金额',
  `refundable_balance` decimal(19, 2) NULL DEFAULT NULL COMMENT '可退款余额',
  `pay_status` int(11) NOT NULL COMMENT '支付状态',
  `error_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误码',
  `error_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `pay_type_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付信息',
  `async_pay_mode` bit(1) NOT NULL COMMENT '是否是异步支付',
  `async_pay_channel` int(11) NULL DEFAULT NULL COMMENT '异步支付方式',
  `pay_channel_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '支付通道信息列表',
  `refundable_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '可退款信息',
  `pay_time` datetime(6) NULL DEFAULT NULL COMMENT '支付时间',
  `expired_time` datetime(6) NULL DEFAULT NULL COMMENT '过期时间',
  `client_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户ip',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_business_id`(`business_id`) USING BTREE COMMENT '业务编号id, 唯一ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_payment
-- ----------------------------

-- ----------------------------
-- Table structure for pay_refund_record
-- ----------------------------
DROP TABLE IF EXISTS `pay_refund_record`;
CREATE TABLE `pay_refund_record`  (
  `id` bigint(20) NOT NULL,
  `payment_id` bigint(20) NOT NULL COMMENT '支付记录id',
  `business_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联业务id',
  `refund_request_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异步方式关联退款请求号',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `amount` decimal(19, 2) NOT NULL COMMENT '金额',
  `refundable_balance` decimal(19, 2) NULL DEFAULT NULL COMMENT '剩余可退款金额',
  `refundable_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '可退款信息',
  `refund_status` int(2) NULL DEFAULT NULL COMMENT '退款状态',
  `refund_time` datetime(6) NULL DEFAULT NULL COMMENT '支付时间',
  `client_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户ip',
  `error_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误码',
  `error_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退款记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_refund_record
-- ----------------------------

-- ----------------------------
-- Table structure for pay_voucher
-- ----------------------------
DROP TABLE IF EXISTS `pay_voucher`;
CREATE TABLE `pay_voucher`  (
  `id` bigint(20) NOT NULL,
  `card_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '卡号',
  `batch_no` bigint(20) NULL DEFAULT NULL COMMENT '批次号',
  `face_value` decimal(15, 2) NULL DEFAULT NULL COMMENT '面值',
  `balance` decimal(15, 2) NULL DEFAULT NULL COMMENT '余额',
  `enduring` bit(1) NOT NULL COMMENT '是否长期有效',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `status` int(2) NOT NULL COMMENT '状态',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '储值卡' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_voucher
-- ----------------------------

-- ----------------------------
-- Table structure for pay_voucher_log
-- ----------------------------
DROP TABLE IF EXISTS `pay_voucher_log`;
CREATE TABLE `pay_voucher_log`  (
  `id` bigint(20) NOT NULL,
  `voucher_id` bigint(20) NOT NULL,
  `voucher_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `amount` decimal(19, 2) NULL DEFAULT NULL,
  `type` int(5) NOT NULL COMMENT '类型',
  `payment_id` bigint(20) NULL DEFAULT NULL COMMENT '交易记录ID',
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务ID',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '储值卡日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_voucher_log
-- ----------------------------

-- ----------------------------
-- Table structure for pay_voucher_payment
-- ----------------------------
DROP TABLE IF EXISTS `pay_voucher_payment`;
CREATE TABLE `pay_voucher_payment`  (
  `id` bigint(20) NOT NULL,
  `voucher_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '储值卡id列表',
  `payment_id` bigint(20) NOT NULL COMMENT '支付id',
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `amount` decimal(19, 2) NULL DEFAULT NULL COMMENT '金额',
  `refundable_balance` decimal(19, 2) NULL DEFAULT NULL COMMENT '可退款金额',
  `pay_status` int(11) NULL DEFAULT NULL COMMENT '支付状态',
  `pay_time` datetime(6) NULL DEFAULT NULL COMMENT '支付时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '储值卡支付记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_voucher_payment
-- ----------------------------

-- ----------------------------
-- Table structure for pay_wallet
-- ----------------------------
DROP TABLE IF EXISTS `pay_wallet`;
CREATE TABLE `pay_wallet`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '关联用户id',
  `balance` decimal(19, 2) NOT NULL COMMENT '余额',
  `status` int(11) NOT NULL COMMENT '状态',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pk_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_wallet
-- ----------------------------
INSERT INTO `pay_wallet` VALUES (1336489524259352576, 1399985191002447872, 999982.96, 1, NULL, '2022-03-11 21:37:33', 1399985191002447872, '2022-05-03 21:24:04', 29, 0);
INSERT INTO `pay_wallet` VALUES (1502554238582968320, 1414143554414059520, 1019.00, 1, 1399985191002447872, '2022-03-12 15:57:01', 1399985191002447872, '2022-03-13 11:21:10', 2, 0);
INSERT INTO `pay_wallet` VALUES (1502848353136791552, 1435894470432456704, 100.00, 1, 1399985191002447872, '2022-03-13 11:25:44', 1399985191002447872, '2022-03-24 13:22:37', 1, 0);

-- ----------------------------
-- Table structure for pay_wallet_log
-- ----------------------------
DROP TABLE IF EXISTS `pay_wallet_log`;
CREATE TABLE `pay_wallet_log`  (
  `id` bigint(20) NOT NULL,
  `wallet_id` bigint(20) NOT NULL COMMENT '钱包id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `type` int(11) NOT NULL COMMENT '类型',
  `payment_id` bigint(20) NULL DEFAULT NULL COMMENT '交易记录ID',
  `client_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作终端ip',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务ID',
  `operation_source` int(11) NOT NULL COMMENT '操作源',
  `amount` decimal(19, 2) NULL DEFAULT NULL COMMENT '金额',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_wallet_log
-- ----------------------------

-- ----------------------------
-- Table structure for pay_wallet_payment
-- ----------------------------
DROP TABLE IF EXISTS `pay_wallet_payment`;
CREATE TABLE `pay_wallet_payment`  (
  `id` bigint(20) NOT NULL,
  `payment_id` bigint(20) NOT NULL COMMENT '交易记录ID',
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `wallet_id` bigint(20) NULL DEFAULT NULL COMMENT '钱包ID',
  `amount` decimal(19, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `refundable_balance` decimal(19, 2) NULL DEFAULT NULL COMMENT '可退款金额',
  `pay_time` datetime(6) NULL DEFAULT NULL COMMENT '支付时间',
  `pay_status` int(11) NOT NULL COMMENT '支付状态',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包交易记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_wallet_payment
-- ----------------------------

-- ----------------------------
-- Table structure for pay_wechat_pay_config
-- ----------------------------
DROP TABLE IF EXISTS `pay_wechat_pay_config`;
CREATE TABLE `pay_wechat_pay_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `app_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信应用AppId',
  `mch_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `api_version` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务商应用编号',
  `api_key_v2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户平台「API安全」中的 APIv2 密钥',
  `api_key_v3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户平台「API安全」中的 APIv3 密钥',
  `app_secret` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'APPID对应的接口密码，用于获取接口调用凭证access_token时使用',
  `p12` bigint(20) NULL DEFAULT NULL COMMENT 'p12的文件id',
  `cert_pem` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'API 证书中的 cert.pem',
  `key_pem` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'API 证书中的 key.pem',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用域名，回调中会使用此参数',
  `notify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器异步通知页面路径',
  `return_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面跳转同步通知页面路径',
  `pay_ways` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '支持的支付类型',
  `sandbox` bit(1) NOT NULL COMMENT '是否沙箱环境',
  `expire_time` int(10) NOT NULL COMMENT '超时配置',
  `activity` bit(1) NOT NULL COMMENT '是否启用',
  `state` int(11) NULL DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信支付配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_wechat_pay_config
-- ----------------------------

-- ----------------------------
-- Table structure for pay_wechat_payment
-- ----------------------------
DROP TABLE IF EXISTS `pay_wechat_payment`;
CREATE TABLE `pay_wechat_payment`  (
  `id` bigint(20) NOT NULL,
  `payment_id` bigint(20) NOT NULL COMMENT '交易记录ID',
  `pay_status` int(11) NOT NULL COMMENT '支付状态',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信交易号',
  `amount` decimal(19, 2) NOT NULL COMMENT '交易金额',
  `refundable_balance` decimal(19, 2) NULL DEFAULT NULL COMMENT '可退款金额',
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务id',
  `pay_time` datetime(6) NULL DEFAULT NULL COMMENT '支付时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信支付记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_wechat_payment
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `INT_PROP_1` int(11) NULL DEFAULT NULL,
  `INT_PROP_2` int(11) NULL DEFAULT NULL,
  `LONG_PROP_1` bigint(20) NULL DEFAULT NULL,
  `LONG_PROP_2` bigint(20) NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for report_project_info
-- ----------------------------
DROP TABLE IF EXISTS `report_project_info`;
CREATE TABLE `report_project_info`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `state` int(11) NULL DEFAULT NULL COMMENT '发布状态',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '报表内容',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '乐观锁',
  `deleted` bit(1) NOT NULL COMMENT '删除标志',
  `index_image` bigint(20) NULL DEFAULT NULL COMMENT '预览图片id',
  `edit` bit(1) NULL DEFAULT NULL COMMENT '是否在编辑中',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自定义大屏信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of report_project_info
-- ----------------------------

-- ----------------------------
-- Table structure for report_project_info_publish
-- ----------------------------
DROP TABLE IF EXISTS `report_project_info_publish`;
CREATE TABLE `report_project_info_publish`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '报表内容',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '乐观锁',
  `deleted` bit(1) NOT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自定义大屏发布信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of report_project_info_publish
-- ----------------------------

-- ----------------------------
-- Table structure for starter_audit_data_version
-- ----------------------------
DROP TABLE IF EXISTS `starter_audit_data_version`;
CREATE TABLE `starter_audit_data_version`  (
  `id` bigint(20) NOT NULL,
  `table_name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据表名称',
  `data_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据名称',
  `data_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据主键',
  `data_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '数据内容',
  `change_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '数据更新内容',
  `version` int(10) NOT NULL COMMENT '版本',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据版本日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of starter_audit_data_version
-- ----------------------------
INSERT INTO `starter_audit_data_version` VALUES (1480550993828446208, '', 'client', '1', '{\"dataName\":\"client\",\"dataId\":\"1\",\"dataContent\":{\"id\":\"1\",\"creator\":null,\"createTime\":\"2022-01-10 22:43:58\",\"lastModifier\":null,\"lastModifiedTime\":null,\"deleted\":false,\"version\":0,\"code\":null,\"name\":null,\"timeout\":null,\"captcha\":false,\"enable\":false,\"description\":null}}', NULL, 1, 0, '2022-01-10 22:43:59');
INSERT INTO `starter_audit_data_version` VALUES (1480551021779288064, '', 'client', '1', '{\"dataName\":\"client\",\"dataId\":\"1\",\"dataContent\":{\"id\":\"1\",\"creator\":null,\"createTime\":\"2022-01-10 22:44:05\",\"lastModifier\":null,\"lastModifiedTime\":null,\"deleted\":false,\"version\":0,\"code\":null,\"name\":null,\"timeout\":null,\"captcha\":false,\"enable\":false,\"description\":null}}', NULL, 2, 0, '2022-01-10 22:44:06');

-- ----------------------------
-- Table structure for starter_audit_login_log
-- ----------------------------
DROP TABLE IF EXISTS `starter_audit_login_log`;
CREATE TABLE `starter_audit_login_log`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(11) NULL DEFAULT NULL COMMENT '用户id',
  `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `login` bit(1) NULL DEFAULT NULL COMMENT '登录成功状态',
  `client` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端',
  `login_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录方式',
  `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录IP地址',
  `login_location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录地点',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登陆日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of starter_audit_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for starter_audit_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `starter_audit_operate_log`;
CREATE TABLE `starter_audit_operate_log`  (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作模块',
  `operate_id` bigint(20) NULL DEFAULT NULL COMMENT '操作人员id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人员账号',
  `business_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `operate_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求url',
  `operate_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作ip',
  `operate_location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作地点',
  `operate_param` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `operate_return` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回参数',
  `success` bit(1) NULL DEFAULT NULL COMMENT '是否成功',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误提示',
  `operate_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of starter_audit_operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for starter_ding_media_md5
-- ----------------------------
DROP TABLE IF EXISTS `starter_ding_media_md5`;
CREATE TABLE `starter_ding_media_md5`  (
  `id` bigint(20) NOT NULL,
  `media_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '媒体id',
  `md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'md5值',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钉钉媒体文件MD5值关联关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of starter_ding_media_md5
-- ----------------------------

-- ----------------------------
-- Table structure for starter_ding_robot_config
-- ----------------------------
DROP TABLE IF EXISTS `starter_ding_robot_config`;
CREATE TABLE `starter_ding_robot_config`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `access_token` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '钉钉机器人访问token',
  `enable_signature_check` bit(1) NOT NULL COMMENT '是否开启验签',
  `sign_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '钉钉机器人私钥',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(6) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` bit(1) NOT NULL COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钉钉机器人配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of starter_ding_robot_config
-- ----------------------------

-- ----------------------------
-- Table structure for starter_file_upload_info
-- ----------------------------
DROP TABLE IF EXISTS `starter_file_upload_info`;
CREATE TABLE `starter_file_upload_info`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储位置',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `file_suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件后缀',
  `file_size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小',
  `external_storage_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外部关联id',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '上传文件信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for starter_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `starter_quartz_job`;
CREATE TABLE `starter_quartz_job`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务类名',
  `cron` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `parameter` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数',
  `state` int(4) NULL DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for starter_quartz_job_log
-- ----------------------------
DROP TABLE IF EXISTS `starter_quartz_job_log`;
CREATE TABLE `starter_quartz_job_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `handler_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理器名称',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理器全限定名',
  `success` bit(1) NOT NULL COMMENT '是否执行成功',
  `error_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `duration` bigint(255) NULL DEFAULT NULL COMMENT '执行时长',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务执行日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of starter_quartz_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for starter_wecom_robot_config
-- ----------------------------
DROP TABLE IF EXISTS `starter_wecom_robot_config`;
CREATE TABLE `starter_wecom_robot_config`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `webhook_key` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'webhook地址的key值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(6) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `deleted` bit(1) NOT NULL COMMENT '0:未删除。1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '企业微信机器人配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of starter_wecom_robot_config
-- ----------------------------

-- ----------------------------
-- Table structure for starter_wx_fans
-- ----------------------------
DROP TABLE IF EXISTS `starter_wx_fans`;
CREATE TABLE `starter_wx_fans`  (
  `id` bigint(20) NOT NULL,
  `openid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联OpenId',
  `subscribe_status` bit(1) NULL DEFAULT NULL COMMENT '订阅状态',
  `subscribe_time` datetime(0) NULL DEFAULT NULL COMMENT '订阅时间',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `sex` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语言',
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信公众号粉丝' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of starter_wx_fans
-- ----------------------------

-- ----------------------------
-- Table structure for starter_wx_menu
-- ----------------------------
DROP TABLE IF EXISTS `starter_wx_menu`;
CREATE TABLE `starter_wx_menu`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  `menu_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '菜单信息',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `publish` bit(1) NOT NULL COMMENT '是否发布',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未删除。1:已删除',
  `version` int(11) NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '微信自定义菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of starter_wx_menu
-- ----------------------------

-- ----------------------------
-- Table structure for starter_wx_template
-- ----------------------------
DROP TABLE IF EXISTS `starter_wx_template`;
CREATE TABLE `starter_wx_template`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `enable` bit(1) NOT NULL COMMENT '是否启用',
  `template_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板标题',
  `primary_industry` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板所属行业的一级行业',
  `deputy_industry` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板所属行业的二级行业',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板内容',
  `example` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '示例',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `inx_`(`template_id`) USING BTREE COMMENT '模板id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信消息模板' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of starter_wx_template
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
