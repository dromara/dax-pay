-- ============================================================
-- 敏感数据已清除(白名单模式) | 工具 redact-data.mjs | 时间 2026-08-23T01:39:47.123Z
-- 策略：保留 18 张系统种子表 + bootx 超管(id=1)，其余整表清除
-- 用途：干净安装包/演示数据
-- ============================================================



-- REDACTED: adapay_direct_key_config (整表清除)


--
-- Data for Name: alipay_direct_alloc_receiver; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: alipay_direct_alloc_receiver (整表清除)


--
-- Data for Name: alipay_direct_app; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: alipay_direct_app (整表清除)


--
-- Data for Name: alipay_direct_app_auth_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: alipay_direct_app_auth_config (整表清除)


--
-- Data for Name: alipay_direct_app_capability; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: alipay_direct_app_key_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: alipay_direct_app_key_config (整表清除)


--
-- Data for Name: alipay_direct_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: alipay_direct_channel_merchant (整表清除)


--
-- Data for Name: alipay_isv_alloc_receiver; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: alipay_isv_app; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: alipay_isv_app_auth_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: alipay_isv_app_key_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: alipay_isv_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: alipay_transfer_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: alipay_transfer_config (整表清除)


--
-- Data for Name: alipay_transfer_scene_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: alipay_transfer_scene_config (整表清除)


--
-- Data for Name: base_area; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.base_area VALUES ('110101', '东城区', '1101');
INSERT INTO public.base_area VALUES ('110102', '西城区', '1101');
INSERT INTO public.base_area VALUES ('110105', '朝阳区', '1101');
INSERT INTO public.base_area VALUES ('110106', '丰台区', '1101');
INSERT INTO public.base_area VALUES ('110107', '石景山区', '1101');
INSERT INTO public.base_area VALUES ('110108', '海淀区', '1101');
INSERT INTO public.base_area VALUES ('110109', '门头沟区', '1101');
INSERT INTO public.base_area VALUES ('110111', '房山区', '1101');
INSERT INTO public.base_area VALUES ('110112', '通州区', '1101');
INSERT INTO public.base_area VALUES ('110113', '顺义区', '1101');
INSERT INTO public.base_area VALUES ('110114', '昌平区', '1101');
INSERT INTO public.base_area VALUES ('110115', '大兴区', '1101');
INSERT INTO public.base_area VALUES ('110116', '怀柔区', '1101');
INSERT INTO public.base_area VALUES ('110117', '平谷区', '1101');
INSERT INTO public.base_area VALUES ('110118', '密云区', '1101');
INSERT INTO public.base_area VALUES ('110119', '延庆区', '1101');
INSERT INTO public.base_area VALUES ('120101', '和平区', '1201');
INSERT INTO public.base_area VALUES ('120102', '河东区', '1201');
INSERT INTO public.base_area VALUES ('120103', '河西区', '1201');
INSERT INTO public.base_area VALUES ('120104', '南开区', '1201');
INSERT INTO public.base_area VALUES ('120105', '河北区', '1201');
INSERT INTO public.base_area VALUES ('120106', '红桥区', '1201');
INSERT INTO public.base_area VALUES ('120110', '东丽区', '1201');
INSERT INTO public.base_area VALUES ('120111', '西青区', '1201');
INSERT INTO public.base_area VALUES ('120112', '津南区', '1201');
INSERT INTO public.base_area VALUES ('120113', '北辰区', '1201');
INSERT INTO public.base_area VALUES ('120114', '武清区', '1201');
INSERT INTO public.base_area VALUES ('120115', '宝坻区', '1201');
INSERT INTO public.base_area VALUES ('120116', '滨海新区', '1201');
INSERT INTO public.base_area VALUES ('120117', '宁河区', '1201');
INSERT INTO public.base_area VALUES ('120118', '静海区', '1201');
INSERT INTO public.base_area VALUES ('120119', '蓟州区', '1201');
INSERT INTO public.base_area VALUES ('130102', '长安区', '1301');
INSERT INTO public.base_area VALUES ('130104', '桥西区', '1301');
INSERT INTO public.base_area VALUES ('130105', '新华区', '1301');
INSERT INTO public.base_area VALUES ('130107', '井陉矿区', '1301');
INSERT INTO public.base_area VALUES ('130108', '裕华区', '1301');
INSERT INTO public.base_area VALUES ('130109', '藁城区', '1301');
INSERT INTO public.base_area VALUES ('130110', '鹿泉区', '1301');
INSERT INTO public.base_area VALUES ('130111', '栾城区', '1301');
INSERT INTO public.base_area VALUES ('130121', '井陉县', '1301');
INSERT INTO public.base_area VALUES ('130123', '正定县', '1301');
INSERT INTO public.base_area VALUES ('130125', '行唐县', '1301');
INSERT INTO public.base_area VALUES ('130126', '灵寿县', '1301');
INSERT INTO public.base_area VALUES ('130127', '高邑县', '1301');
INSERT INTO public.base_area VALUES ('130128', '深泽县', '1301');
INSERT INTO public.base_area VALUES ('130129', '赞皇县', '1301');
INSERT INTO public.base_area VALUES ('130130', '无极县', '1301');
INSERT INTO public.base_area VALUES ('130131', '平山县', '1301');
INSERT INTO public.base_area VALUES ('130132', '元氏县', '1301');
INSERT INTO public.base_area VALUES ('130133', '赵县', '1301');
INSERT INTO public.base_area VALUES ('130171', '石家庄高新技术产业开发区', '1301');
INSERT INTO public.base_area VALUES ('130172', '石家庄循环化工园区', '1301');
INSERT INTO public.base_area VALUES ('130181', '辛集市', '1301');
INSERT INTO public.base_area VALUES ('130183', '晋州市', '1301');
INSERT INTO public.base_area VALUES ('130184', '新乐市', '1301');
INSERT INTO public.base_area VALUES ('130202', '路南区', '1302');
INSERT INTO public.base_area VALUES ('130203', '路北区', '1302');
INSERT INTO public.base_area VALUES ('130204', '古冶区', '1302');
INSERT INTO public.base_area VALUES ('130205', '开平区', '1302');
INSERT INTO public.base_area VALUES ('130207', '丰南区', '1302');
INSERT INTO public.base_area VALUES ('130208', '丰润区', '1302');
INSERT INTO public.base_area VALUES ('130209', '曹妃甸区', '1302');
INSERT INTO public.base_area VALUES ('130224', '滦南县', '1302');
INSERT INTO public.base_area VALUES ('130225', '乐亭县', '1302');
INSERT INTO public.base_area VALUES ('130227', '迁西县', '1302');
INSERT INTO public.base_area VALUES ('130229', '玉田县', '1302');
INSERT INTO public.base_area VALUES ('130271', '河北唐山芦台经济开发区', '1302');
INSERT INTO public.base_area VALUES ('130272', '唐山市汉沽管理区', '1302');
INSERT INTO public.base_area VALUES ('130273', '唐山高新技术产业开发区', '1302');
INSERT INTO public.base_area VALUES ('130274', '河北唐山海港经济开发区', '1302');
INSERT INTO public.base_area VALUES ('130281', '遵化市', '1302');
INSERT INTO public.base_area VALUES ('130283', '迁安市', '1302');
INSERT INTO public.base_area VALUES ('130284', '滦州市', '1302');
INSERT INTO public.base_area VALUES ('130302', '海港区', '1303');
INSERT INTO public.base_area VALUES ('130303', '山海关区', '1303');
INSERT INTO public.base_area VALUES ('130304', '北戴河区', '1303');
INSERT INTO public.base_area VALUES ('130306', '抚宁区', '1303');
INSERT INTO public.base_area VALUES ('130321', '青龙满族自治县', '1303');
INSERT INTO public.base_area VALUES ('130322', '昌黎县', '1303');
INSERT INTO public.base_area VALUES ('130324', '卢龙县', '1303');
INSERT INTO public.base_area VALUES ('130371', '秦皇岛市经济技术开发区', '1303');
INSERT INTO public.base_area VALUES ('130372', '北戴河新区', '1303');
INSERT INTO public.base_area VALUES ('130402', '邯山区', '1304');
INSERT INTO public.base_area VALUES ('130403', '丛台区', '1304');
INSERT INTO public.base_area VALUES ('130404', '复兴区', '1304');
INSERT INTO public.base_area VALUES ('130406', '峰峰矿区', '1304');
INSERT INTO public.base_area VALUES ('130407', '肥乡区', '1304');
INSERT INTO public.base_area VALUES ('130408', '永年区', '1304');
INSERT INTO public.base_area VALUES ('130423', '临漳县', '1304');
INSERT INTO public.base_area VALUES ('130424', '成安县', '1304');
INSERT INTO public.base_area VALUES ('130425', '大名县', '1304');
INSERT INTO public.base_area VALUES ('130426', '涉县', '1304');
INSERT INTO public.base_area VALUES ('130427', '磁县', '1304');
INSERT INTO public.base_area VALUES ('130430', '邱县', '1304');
INSERT INTO public.base_area VALUES ('130431', '鸡泽县', '1304');
INSERT INTO public.base_area VALUES ('130432', '广平县', '1304');
INSERT INTO public.base_area VALUES ('130433', '馆陶县', '1304');
INSERT INTO public.base_area VALUES ('130434', '魏县', '1304');
INSERT INTO public.base_area VALUES ('130435', '曲周县', '1304');
INSERT INTO public.base_area VALUES ('130471', '邯郸经济技术开发区', '1304');
INSERT INTO public.base_area VALUES ('130473', '邯郸冀南新区', '1304');
INSERT INTO public.base_area VALUES ('130481', '武安市', '1304');
INSERT INTO public.base_area VALUES ('130502', '襄都区', '1305');
INSERT INTO public.base_area VALUES ('130503', '信都区', '1305');
INSERT INTO public.base_area VALUES ('130505', '任泽区', '1305');
INSERT INTO public.base_area VALUES ('130506', '南和区', '1305');
INSERT INTO public.base_area VALUES ('130522', '临城县', '1305');
INSERT INTO public.base_area VALUES ('130523', '内丘县', '1305');
INSERT INTO public.base_area VALUES ('130524', '柏乡县', '1305');
INSERT INTO public.base_area VALUES ('130525', '隆尧县', '1305');
INSERT INTO public.base_area VALUES ('130528', '宁晋县', '1305');
INSERT INTO public.base_area VALUES ('130529', '巨鹿县', '1305');
INSERT INTO public.base_area VALUES ('130530', '新河县', '1305');
INSERT INTO public.base_area VALUES ('130531', '广宗县', '1305');
INSERT INTO public.base_area VALUES ('130532', '平乡县', '1305');
INSERT INTO public.base_area VALUES ('130533', '威县', '1305');
INSERT INTO public.base_area VALUES ('130534', '清河县', '1305');
INSERT INTO public.base_area VALUES ('130535', '临西县', '1305');
INSERT INTO public.base_area VALUES ('130571', '河北邢台经济开发区', '1305');
INSERT INTO public.base_area VALUES ('130581', '南宫市', '1305');
INSERT INTO public.base_area VALUES ('130582', '沙河市', '1305');
INSERT INTO public.base_area VALUES ('130602', '竞秀区', '1306');
INSERT INTO public.base_area VALUES ('130606', '莲池区', '1306');
INSERT INTO public.base_area VALUES ('130607', '满城区', '1306');
INSERT INTO public.base_area VALUES ('130608', '清苑区', '1306');
INSERT INTO public.base_area VALUES ('130609', '徐水区', '1306');
INSERT INTO public.base_area VALUES ('130623', '涞水县', '1306');
INSERT INTO public.base_area VALUES ('130624', '阜平县', '1306');
INSERT INTO public.base_area VALUES ('130626', '定兴县', '1306');
INSERT INTO public.base_area VALUES ('130627', '唐县', '1306');
INSERT INTO public.base_area VALUES ('130628', '高阳县', '1306');
INSERT INTO public.base_area VALUES ('130629', '容城县', '1306');
INSERT INTO public.base_area VALUES ('130630', '涞源县', '1306');
INSERT INTO public.base_area VALUES ('130631', '望都县', '1306');
INSERT INTO public.base_area VALUES ('130632', '安新县', '1306');
INSERT INTO public.base_area VALUES ('130633', '易县', '1306');
INSERT INTO public.base_area VALUES ('130634', '曲阳县', '1306');
INSERT INTO public.base_area VALUES ('130635', '蠡县', '1306');
INSERT INTO public.base_area VALUES ('130636', '顺平县', '1306');
INSERT INTO public.base_area VALUES ('130637', '博野县', '1306');
INSERT INTO public.base_area VALUES ('130638', '雄县', '1306');
INSERT INTO public.base_area VALUES ('130671', '保定高新技术产业开发区', '1306');
INSERT INTO public.base_area VALUES ('130672', '保定白沟新城', '1306');
INSERT INTO public.base_area VALUES ('130681', '涿州市', '1306');
INSERT INTO public.base_area VALUES ('130682', '定州市', '1306');
INSERT INTO public.base_area VALUES ('130683', '安国市', '1306');
INSERT INTO public.base_area VALUES ('130684', '高碑店市', '1306');
INSERT INTO public.base_area VALUES ('130702', '桥东区', '1307');
INSERT INTO public.base_area VALUES ('130703', '桥西区', '1307');
INSERT INTO public.base_area VALUES ('130705', '宣化区', '1307');
INSERT INTO public.base_area VALUES ('130706', '下花园区', '1307');
INSERT INTO public.base_area VALUES ('130708', '万全区', '1307');
INSERT INTO public.base_area VALUES ('130709', '崇礼区', '1307');
INSERT INTO public.base_area VALUES ('130722', '张北县', '1307');
INSERT INTO public.base_area VALUES ('130723', '康保县', '1307');
INSERT INTO public.base_area VALUES ('130724', '沽源县', '1307');
INSERT INTO public.base_area VALUES ('130725', '尚义县', '1307');
INSERT INTO public.base_area VALUES ('130726', '蔚县', '1307');
INSERT INTO public.base_area VALUES ('130727', '阳原县', '1307');
INSERT INTO public.base_area VALUES ('130728', '怀安县', '1307');
INSERT INTO public.base_area VALUES ('130730', '怀来县', '1307');
INSERT INTO public.base_area VALUES ('130731', '涿鹿县', '1307');
INSERT INTO public.base_area VALUES ('130732', '赤城县', '1307');
INSERT INTO public.base_area VALUES ('130771', '张家口经济开发区', '1307');
INSERT INTO public.base_area VALUES ('130772', '张家口市察北管理区', '1307');
INSERT INTO public.base_area VALUES ('130773', '张家口市塞北管理区', '1307');
INSERT INTO public.base_area VALUES ('130802', '双桥区', '1308');
INSERT INTO public.base_area VALUES ('130803', '双滦区', '1308');
INSERT INTO public.base_area VALUES ('130804', '鹰手营子矿区', '1308');
INSERT INTO public.base_area VALUES ('130821', '承德县', '1308');
INSERT INTO public.base_area VALUES ('130822', '兴隆县', '1308');
INSERT INTO public.base_area VALUES ('130824', '滦平县', '1308');
INSERT INTO public.base_area VALUES ('130825', '隆化县', '1308');
INSERT INTO public.base_area VALUES ('130826', '丰宁满族自治县', '1308');
INSERT INTO public.base_area VALUES ('130827', '宽城满族自治县', '1308');
INSERT INTO public.base_area VALUES ('130828', '围场满族蒙古族自治县', '1308');
INSERT INTO public.base_area VALUES ('130871', '承德高新技术产业开发区', '1308');
INSERT INTO public.base_area VALUES ('130881', '平泉市', '1308');
INSERT INTO public.base_area VALUES ('130902', '新华区', '1309');
INSERT INTO public.base_area VALUES ('130903', '运河区', '1309');
INSERT INTO public.base_area VALUES ('130921', '沧县', '1309');
INSERT INTO public.base_area VALUES ('130922', '青县', '1309');
INSERT INTO public.base_area VALUES ('130923', '东光县', '1309');
INSERT INTO public.base_area VALUES ('130924', '海兴县', '1309');
INSERT INTO public.base_area VALUES ('130925', '盐山县', '1309');
INSERT INTO public.base_area VALUES ('130926', '肃宁县', '1309');
INSERT INTO public.base_area VALUES ('130927', '南皮县', '1309');
INSERT INTO public.base_area VALUES ('130928', '吴桥县', '1309');
INSERT INTO public.base_area VALUES ('130929', '献县', '1309');
INSERT INTO public.base_area VALUES ('130930', '孟村回族自治县', '1309');
INSERT INTO public.base_area VALUES ('130971', '河北沧州经济开发区', '1309');
INSERT INTO public.base_area VALUES ('130972', '沧州高新技术产业开发区', '1309');
INSERT INTO public.base_area VALUES ('130973', '沧州渤海新区', '1309');
INSERT INTO public.base_area VALUES ('130981', '泊头市', '1309');
INSERT INTO public.base_area VALUES ('130982', '任丘市', '1309');
INSERT INTO public.base_area VALUES ('130983', '黄骅市', '1309');
INSERT INTO public.base_area VALUES ('130984', '河间市', '1309');
INSERT INTO public.base_area VALUES ('131002', '安次区', '1310');
INSERT INTO public.base_area VALUES ('131003', '广阳区', '1310');
INSERT INTO public.base_area VALUES ('131022', '固安县', '1310');
INSERT INTO public.base_area VALUES ('131023', '永清县', '1310');
INSERT INTO public.base_area VALUES ('131024', '香河县', '1310');
INSERT INTO public.base_area VALUES ('131025', '大城县', '1310');
INSERT INTO public.base_area VALUES ('131026', '文安县', '1310');
INSERT INTO public.base_area VALUES ('131028', '大厂回族自治县', '1310');
INSERT INTO public.base_area VALUES ('131071', '廊坊经济技术开发区', '1310');
INSERT INTO public.base_area VALUES ('131081', '霸州市', '1310');
INSERT INTO public.base_area VALUES ('131082', '三河市', '1310');
INSERT INTO public.base_area VALUES ('131102', '桃城区', '1311');
INSERT INTO public.base_area VALUES ('131103', '冀州区', '1311');
INSERT INTO public.base_area VALUES ('131121', '枣强县', '1311');
INSERT INTO public.base_area VALUES ('131122', '武邑县', '1311');
INSERT INTO public.base_area VALUES ('131123', '武强县', '1311');
INSERT INTO public.base_area VALUES ('131124', '饶阳县', '1311');
INSERT INTO public.base_area VALUES ('131125', '安平县', '1311');
INSERT INTO public.base_area VALUES ('131126', '故城县', '1311');
INSERT INTO public.base_area VALUES ('131127', '景县', '1311');
INSERT INTO public.base_area VALUES ('131128', '阜城县', '1311');
INSERT INTO public.base_area VALUES ('131171', '河北衡水高新技术产业开发区', '1311');
INSERT INTO public.base_area VALUES ('131172', '衡水滨湖新区', '1311');
INSERT INTO public.base_area VALUES ('131182', '深州市', '1311');
INSERT INTO public.base_area VALUES ('140105', '小店区', '1401');
INSERT INTO public.base_area VALUES ('140106', '迎泽区', '1401');
INSERT INTO public.base_area VALUES ('140107', '杏花岭区', '1401');
INSERT INTO public.base_area VALUES ('140108', '尖草坪区', '1401');
INSERT INTO public.base_area VALUES ('140109', '万柏林区', '1401');
INSERT INTO public.base_area VALUES ('140110', '晋源区', '1401');
INSERT INTO public.base_area VALUES ('140121', '清徐县', '1401');
INSERT INTO public.base_area VALUES ('140122', '阳曲县', '1401');
INSERT INTO public.base_area VALUES ('140123', '娄烦县', '1401');
INSERT INTO public.base_area VALUES ('140171', '山西转型综合改革示范区', '1401');
INSERT INTO public.base_area VALUES ('140181', '古交市', '1401');
INSERT INTO public.base_area VALUES ('140212', '新荣区', '1402');
INSERT INTO public.base_area VALUES ('140213', '平城区', '1402');
INSERT INTO public.base_area VALUES ('140214', '云冈区', '1402');
INSERT INTO public.base_area VALUES ('140215', '云州区', '1402');
INSERT INTO public.base_area VALUES ('140221', '阳高县', '1402');
INSERT INTO public.base_area VALUES ('140222', '天镇县', '1402');
INSERT INTO public.base_area VALUES ('140223', '广灵县', '1402');
INSERT INTO public.base_area VALUES ('140224', '灵丘县', '1402');
INSERT INTO public.base_area VALUES ('140225', '浑源县', '1402');
INSERT INTO public.base_area VALUES ('140226', '左云县', '1402');
INSERT INTO public.base_area VALUES ('140271', '山西大同经济开发区', '1402');
INSERT INTO public.base_area VALUES ('140302', '城区', '1403');
INSERT INTO public.base_area VALUES ('140303', '矿区', '1403');
INSERT INTO public.base_area VALUES ('140311', '郊区', '1403');
INSERT INTO public.base_area VALUES ('140321', '平定县', '1403');
INSERT INTO public.base_area VALUES ('140322', '盂县', '1403');
INSERT INTO public.base_area VALUES ('140403', '潞州区', '1404');
INSERT INTO public.base_area VALUES ('140404', '上党区', '1404');
INSERT INTO public.base_area VALUES ('140405', '屯留区', '1404');
INSERT INTO public.base_area VALUES ('140406', '潞城区', '1404');
INSERT INTO public.base_area VALUES ('140423', '襄垣县', '1404');
INSERT INTO public.base_area VALUES ('140425', '平顺县', '1404');
INSERT INTO public.base_area VALUES ('140426', '黎城县', '1404');
INSERT INTO public.base_area VALUES ('140427', '壶关县', '1404');
INSERT INTO public.base_area VALUES ('140428', '长子县', '1404');
INSERT INTO public.base_area VALUES ('140429', '武乡县', '1404');
INSERT INTO public.base_area VALUES ('140430', '沁县', '1404');
INSERT INTO public.base_area VALUES ('140431', '沁源县', '1404');
INSERT INTO public.base_area VALUES ('140471', '山西长治高新技术产业园区', '1404');
INSERT INTO public.base_area VALUES ('140502', '城区', '1405');
INSERT INTO public.base_area VALUES ('140521', '沁水县', '1405');
INSERT INTO public.base_area VALUES ('140522', '阳城县', '1405');
INSERT INTO public.base_area VALUES ('140524', '陵川县', '1405');
INSERT INTO public.base_area VALUES ('140525', '泽州县', '1405');
INSERT INTO public.base_area VALUES ('140581', '高平市', '1405');
INSERT INTO public.base_area VALUES ('140602', '朔城区', '1406');
INSERT INTO public.base_area VALUES ('140603', '平鲁区', '1406');
INSERT INTO public.base_area VALUES ('140621', '山阴县', '1406');
INSERT INTO public.base_area VALUES ('140622', '应县', '1406');
INSERT INTO public.base_area VALUES ('140623', '右玉县', '1406');
INSERT INTO public.base_area VALUES ('140671', '山西朔州经济开发区', '1406');
INSERT INTO public.base_area VALUES ('140681', '怀仁市', '1406');
INSERT INTO public.base_area VALUES ('140702', '榆次区', '1407');
INSERT INTO public.base_area VALUES ('140703', '太谷区', '1407');
INSERT INTO public.base_area VALUES ('140721', '榆社县', '1407');
INSERT INTO public.base_area VALUES ('140722', '左权县', '1407');
INSERT INTO public.base_area VALUES ('140723', '和顺县', '1407');
INSERT INTO public.base_area VALUES ('140724', '昔阳县', '1407');
INSERT INTO public.base_area VALUES ('140725', '寿阳县', '1407');
INSERT INTO public.base_area VALUES ('140727', '祁县', '1407');
INSERT INTO public.base_area VALUES ('140728', '平遥县', '1407');
INSERT INTO public.base_area VALUES ('140729', '灵石县', '1407');
INSERT INTO public.base_area VALUES ('140781', '介休市', '1407');
INSERT INTO public.base_area VALUES ('140802', '盐湖区', '1408');
INSERT INTO public.base_area VALUES ('140821', '临猗县', '1408');
INSERT INTO public.base_area VALUES ('140822', '万荣县', '1408');
INSERT INTO public.base_area VALUES ('140823', '闻喜县', '1408');
INSERT INTO public.base_area VALUES ('140824', '稷山县', '1408');
INSERT INTO public.base_area VALUES ('140825', '新绛县', '1408');
INSERT INTO public.base_area VALUES ('140826', '绛县', '1408');
INSERT INTO public.base_area VALUES ('140827', '垣曲县', '1408');
INSERT INTO public.base_area VALUES ('140828', '夏县', '1408');
INSERT INTO public.base_area VALUES ('140829', '平陆县', '1408');
INSERT INTO public.base_area VALUES ('140830', '芮城县', '1408');
INSERT INTO public.base_area VALUES ('140881', '永济市', '1408');
INSERT INTO public.base_area VALUES ('140882', '河津市', '1408');
INSERT INTO public.base_area VALUES ('140902', '忻府区', '1409');
INSERT INTO public.base_area VALUES ('140921', '定襄县', '1409');
INSERT INTO public.base_area VALUES ('140922', '五台县', '1409');
INSERT INTO public.base_area VALUES ('140923', '代县', '1409');
INSERT INTO public.base_area VALUES ('140924', '繁峙县', '1409');
INSERT INTO public.base_area VALUES ('140925', '宁武县', '1409');
INSERT INTO public.base_area VALUES ('140926', '静乐县', '1409');
INSERT INTO public.base_area VALUES ('140927', '神池县', '1409');
INSERT INTO public.base_area VALUES ('140928', '五寨县', '1409');
INSERT INTO public.base_area VALUES ('140929', '岢岚县', '1409');
INSERT INTO public.base_area VALUES ('140930', '河曲县', '1409');
INSERT INTO public.base_area VALUES ('140931', '保德县', '1409');
INSERT INTO public.base_area VALUES ('140932', '偏关县', '1409');
INSERT INTO public.base_area VALUES ('140971', '五台山风景名胜区', '1409');
INSERT INTO public.base_area VALUES ('140981', '原平市', '1409');
INSERT INTO public.base_area VALUES ('141002', '尧都区', '1410');
INSERT INTO public.base_area VALUES ('141021', '曲沃县', '1410');
INSERT INTO public.base_area VALUES ('141022', '翼城县', '1410');
INSERT INTO public.base_area VALUES ('141023', '襄汾县', '1410');
INSERT INTO public.base_area VALUES ('141024', '洪洞县', '1410');
INSERT INTO public.base_area VALUES ('141025', '古县', '1410');
INSERT INTO public.base_area VALUES ('141026', '安泽县', '1410');
INSERT INTO public.base_area VALUES ('141027', '浮山县', '1410');
INSERT INTO public.base_area VALUES ('141028', '吉县', '1410');
INSERT INTO public.base_area VALUES ('141029', '乡宁县', '1410');
INSERT INTO public.base_area VALUES ('141030', '大宁县', '1410');
INSERT INTO public.base_area VALUES ('141031', '隰县', '1410');
INSERT INTO public.base_area VALUES ('141032', '永和县', '1410');
INSERT INTO public.base_area VALUES ('141033', '蒲县', '1410');
INSERT INTO public.base_area VALUES ('141034', '汾西县', '1410');
INSERT INTO public.base_area VALUES ('141081', '侯马市', '1410');
INSERT INTO public.base_area VALUES ('141082', '霍州市', '1410');
INSERT INTO public.base_area VALUES ('141102', '离石区', '1411');
INSERT INTO public.base_area VALUES ('141121', '文水县', '1411');
INSERT INTO public.base_area VALUES ('141122', '交城县', '1411');
INSERT INTO public.base_area VALUES ('141123', '兴县', '1411');
INSERT INTO public.base_area VALUES ('141124', '临县', '1411');
INSERT INTO public.base_area VALUES ('141125', '柳林县', '1411');
INSERT INTO public.base_area VALUES ('141126', '石楼县', '1411');
INSERT INTO public.base_area VALUES ('141127', '岚县', '1411');
INSERT INTO public.base_area VALUES ('141128', '方山县', '1411');
INSERT INTO public.base_area VALUES ('141129', '中阳县', '1411');
INSERT INTO public.base_area VALUES ('141130', '交口县', '1411');
INSERT INTO public.base_area VALUES ('141181', '孝义市', '1411');
INSERT INTO public.base_area VALUES ('141182', '汾阳市', '1411');
INSERT INTO public.base_area VALUES ('150102', '新城区', '1501');
INSERT INTO public.base_area VALUES ('150103', '回民区', '1501');
INSERT INTO public.base_area VALUES ('150104', '玉泉区', '1501');
INSERT INTO public.base_area VALUES ('150105', '赛罕区', '1501');
INSERT INTO public.base_area VALUES ('150121', '土默特左旗', '1501');
INSERT INTO public.base_area VALUES ('150122', '托克托县', '1501');
INSERT INTO public.base_area VALUES ('150123', '和林格尔县', '1501');
INSERT INTO public.base_area VALUES ('150124', '清水河县', '1501');
INSERT INTO public.base_area VALUES ('150125', '武川县', '1501');
INSERT INTO public.base_area VALUES ('150172', '呼和浩特经济技术开发区', '1501');
INSERT INTO public.base_area VALUES ('150202', '东河区', '1502');
INSERT INTO public.base_area VALUES ('150203', '昆都仑区', '1502');
INSERT INTO public.base_area VALUES ('150204', '青山区', '1502');
INSERT INTO public.base_area VALUES ('150205', '石拐区', '1502');
INSERT INTO public.base_area VALUES ('150206', '白云鄂博矿区', '1502');
INSERT INTO public.base_area VALUES ('150207', '九原区', '1502');
INSERT INTO public.base_area VALUES ('150221', '土默特右旗', '1502');
INSERT INTO public.base_area VALUES ('150222', '固阳县', '1502');
INSERT INTO public.base_area VALUES ('150223', '达尔罕茂明安联合旗', '1502');
INSERT INTO public.base_area VALUES ('150271', '包头稀土高新技术产业开发区', '1502');
INSERT INTO public.base_area VALUES ('150302', '海勃湾区', '1503');
INSERT INTO public.base_area VALUES ('150303', '海南区', '1503');
INSERT INTO public.base_area VALUES ('150304', '乌达区', '1503');
INSERT INTO public.base_area VALUES ('150402', '红山区', '1504');
INSERT INTO public.base_area VALUES ('150403', '元宝山区', '1504');
INSERT INTO public.base_area VALUES ('150404', '松山区', '1504');
INSERT INTO public.base_area VALUES ('150421', '阿鲁科尔沁旗', '1504');
INSERT INTO public.base_area VALUES ('150422', '巴林左旗', '1504');
INSERT INTO public.base_area VALUES ('150423', '巴林右旗', '1504');
INSERT INTO public.base_area VALUES ('150424', '林西县', '1504');
INSERT INTO public.base_area VALUES ('150425', '克什克腾旗', '1504');
INSERT INTO public.base_area VALUES ('150426', '翁牛特旗', '1504');
INSERT INTO public.base_area VALUES ('150428', '喀喇沁旗', '1504');
INSERT INTO public.base_area VALUES ('150429', '宁城县', '1504');
INSERT INTO public.base_area VALUES ('150430', '敖汉旗', '1504');
INSERT INTO public.base_area VALUES ('150502', '科尔沁区', '1505');
INSERT INTO public.base_area VALUES ('150521', '科尔沁左翼中旗', '1505');
INSERT INTO public.base_area VALUES ('150522', '科尔沁左翼后旗', '1505');
INSERT INTO public.base_area VALUES ('150523', '开鲁县', '1505');
INSERT INTO public.base_area VALUES ('150524', '库伦旗', '1505');
INSERT INTO public.base_area VALUES ('150525', '奈曼旗', '1505');
INSERT INTO public.base_area VALUES ('150526', '扎鲁特旗', '1505');
INSERT INTO public.base_area VALUES ('150571', '通辽经济技术开发区', '1505');
INSERT INTO public.base_area VALUES ('150581', '霍林郭勒市', '1505');
INSERT INTO public.base_area VALUES ('150602', '东胜区', '1506');
INSERT INTO public.base_area VALUES ('150603', '康巴什区', '1506');
INSERT INTO public.base_area VALUES ('150621', '达拉特旗', '1506');
INSERT INTO public.base_area VALUES ('150622', '准格尔旗', '1506');
INSERT INTO public.base_area VALUES ('150623', '鄂托克前旗', '1506');
INSERT INTO public.base_area VALUES ('150624', '鄂托克旗', '1506');
INSERT INTO public.base_area VALUES ('150625', '杭锦旗', '1506');
INSERT INTO public.base_area VALUES ('150626', '乌审旗', '1506');
INSERT INTO public.base_area VALUES ('150627', '伊金霍洛旗', '1506');
INSERT INTO public.base_area VALUES ('150702', '海拉尔区', '1507');
INSERT INTO public.base_area VALUES ('150703', '扎赉诺尔区', '1507');
INSERT INTO public.base_area VALUES ('150721', '阿荣旗', '1507');
INSERT INTO public.base_area VALUES ('150722', '莫力达瓦达斡尔族自治旗', '1507');
INSERT INTO public.base_area VALUES ('150723', '鄂伦春自治旗', '1507');
INSERT INTO public.base_area VALUES ('150724', '鄂温克族自治旗', '1507');
INSERT INTO public.base_area VALUES ('150725', '陈巴尔虎旗', '1507');
INSERT INTO public.base_area VALUES ('150726', '新巴尔虎左旗', '1507');
INSERT INTO public.base_area VALUES ('150727', '新巴尔虎右旗', '1507');
INSERT INTO public.base_area VALUES ('150781', '满洲里市', '1507');
INSERT INTO public.base_area VALUES ('150782', '牙克石市', '1507');
INSERT INTO public.base_area VALUES ('150783', '扎兰屯市', '1507');
INSERT INTO public.base_area VALUES ('150784', '额尔古纳市', '1507');
INSERT INTO public.base_area VALUES ('150785', '根河市', '1507');
INSERT INTO public.base_area VALUES ('150802', '临河区', '1508');
INSERT INTO public.base_area VALUES ('150821', '五原县', '1508');
INSERT INTO public.base_area VALUES ('150822', '磴口县', '1508');
INSERT INTO public.base_area VALUES ('150823', '乌拉特前旗', '1508');
INSERT INTO public.base_area VALUES ('150824', '乌拉特中旗', '1508');
INSERT INTO public.base_area VALUES ('150825', '乌拉特后旗', '1508');
INSERT INTO public.base_area VALUES ('150826', '杭锦后旗', '1508');
INSERT INTO public.base_area VALUES ('150902', '集宁区', '1509');
INSERT INTO public.base_area VALUES ('150921', '卓资县', '1509');
INSERT INTO public.base_area VALUES ('150922', '化德县', '1509');
INSERT INTO public.base_area VALUES ('150923', '商都县', '1509');
INSERT INTO public.base_area VALUES ('150924', '兴和县', '1509');
INSERT INTO public.base_area VALUES ('150925', '凉城县', '1509');
INSERT INTO public.base_area VALUES ('150926', '察哈尔右翼前旗', '1509');
INSERT INTO public.base_area VALUES ('150927', '察哈尔右翼中旗', '1509');
INSERT INTO public.base_area VALUES ('150928', '察哈尔右翼后旗', '1509');
INSERT INTO public.base_area VALUES ('150929', '四子王旗', '1509');
INSERT INTO public.base_area VALUES ('150981', '丰镇市', '1509');
INSERT INTO public.base_area VALUES ('152201', '乌兰浩特市', '1522');
INSERT INTO public.base_area VALUES ('152202', '阿尔山市', '1522');
INSERT INTO public.base_area VALUES ('152221', '科尔沁右翼前旗', '1522');
INSERT INTO public.base_area VALUES ('152222', '科尔沁右翼中旗', '1522');
INSERT INTO public.base_area VALUES ('152223', '扎赉特旗', '1522');
INSERT INTO public.base_area VALUES ('152224', '突泉县', '1522');
INSERT INTO public.base_area VALUES ('152501', '二连浩特市', '1525');
INSERT INTO public.base_area VALUES ('152502', '锡林浩特市', '1525');
INSERT INTO public.base_area VALUES ('152522', '阿巴嘎旗', '1525');
INSERT INTO public.base_area VALUES ('152523', '苏尼特左旗', '1525');
INSERT INTO public.base_area VALUES ('152524', '苏尼特右旗', '1525');
INSERT INTO public.base_area VALUES ('152525', '东乌珠穆沁旗', '1525');
INSERT INTO public.base_area VALUES ('152526', '西乌珠穆沁旗', '1525');
INSERT INTO public.base_area VALUES ('152527', '太仆寺旗', '1525');
INSERT INTO public.base_area VALUES ('152528', '镶黄旗', '1525');
INSERT INTO public.base_area VALUES ('152529', '正镶白旗', '1525');
INSERT INTO public.base_area VALUES ('152530', '正蓝旗', '1525');
INSERT INTO public.base_area VALUES ('152531', '多伦县', '1525');
INSERT INTO public.base_area VALUES ('152571', '乌拉盖管委会', '1525');
INSERT INTO public.base_area VALUES ('152921', '阿拉善左旗', '1529');
INSERT INTO public.base_area VALUES ('152922', '阿拉善右旗', '1529');
INSERT INTO public.base_area VALUES ('152923', '额济纳旗', '1529');
INSERT INTO public.base_area VALUES ('152971', '内蒙古阿拉善高新技术产业开发区', '1529');
INSERT INTO public.base_area VALUES ('210102', '和平区', '2101');
INSERT INTO public.base_area VALUES ('210103', '沈河区', '2101');
INSERT INTO public.base_area VALUES ('210104', '大东区', '2101');
INSERT INTO public.base_area VALUES ('210105', '皇姑区', '2101');
INSERT INTO public.base_area VALUES ('210106', '铁西区', '2101');
INSERT INTO public.base_area VALUES ('210111', '苏家屯区', '2101');
INSERT INTO public.base_area VALUES ('210112', '浑南区', '2101');
INSERT INTO public.base_area VALUES ('210113', '沈北新区', '2101');
INSERT INTO public.base_area VALUES ('210114', '于洪区', '2101');
INSERT INTO public.base_area VALUES ('210115', '辽中区', '2101');
INSERT INTO public.base_area VALUES ('210123', '康平县', '2101');
INSERT INTO public.base_area VALUES ('210124', '法库县', '2101');
INSERT INTO public.base_area VALUES ('210181', '新民市', '2101');
INSERT INTO public.base_area VALUES ('210202', '中山区', '2102');
INSERT INTO public.base_area VALUES ('210203', '西岗区', '2102');
INSERT INTO public.base_area VALUES ('210204', '沙河口区', '2102');
INSERT INTO public.base_area VALUES ('210211', '甘井子区', '2102');
INSERT INTO public.base_area VALUES ('210212', '旅顺口区', '2102');
INSERT INTO public.base_area VALUES ('210213', '金州区', '2102');
INSERT INTO public.base_area VALUES ('210214', '普兰店区', '2102');
INSERT INTO public.base_area VALUES ('210224', '长海县', '2102');
INSERT INTO public.base_area VALUES ('210281', '瓦房店市', '2102');
INSERT INTO public.base_area VALUES ('210283', '庄河市', '2102');
INSERT INTO public.base_area VALUES ('210302', '铁东区', '2103');
INSERT INTO public.base_area VALUES ('210303', '铁西区', '2103');
INSERT INTO public.base_area VALUES ('210304', '立山区', '2103');
INSERT INTO public.base_area VALUES ('210311', '千山区', '2103');
INSERT INTO public.base_area VALUES ('210321', '台安县', '2103');
INSERT INTO public.base_area VALUES ('210323', '岫岩满族自治县', '2103');
INSERT INTO public.base_area VALUES ('210381', '海城市', '2103');
INSERT INTO public.base_area VALUES ('210402', '新抚区', '2104');
INSERT INTO public.base_area VALUES ('210403', '东洲区', '2104');
INSERT INTO public.base_area VALUES ('210404', '望花区', '2104');
INSERT INTO public.base_area VALUES ('210411', '顺城区', '2104');
INSERT INTO public.base_area VALUES ('210421', '抚顺县', '2104');
INSERT INTO public.base_area VALUES ('210422', '新宾满族自治县', '2104');
INSERT INTO public.base_area VALUES ('210423', '清原满族自治县', '2104');
INSERT INTO public.base_area VALUES ('210502', '平山区', '2105');
INSERT INTO public.base_area VALUES ('210503', '溪湖区', '2105');
INSERT INTO public.base_area VALUES ('210504', '明山区', '2105');
INSERT INTO public.base_area VALUES ('210505', '南芬区', '2105');
INSERT INTO public.base_area VALUES ('210521', '本溪满族自治县', '2105');
INSERT INTO public.base_area VALUES ('210522', '桓仁满族自治县', '2105');
INSERT INTO public.base_area VALUES ('210602', '元宝区', '2106');
INSERT INTO public.base_area VALUES ('210603', '振兴区', '2106');
INSERT INTO public.base_area VALUES ('210604', '振安区', '2106');
INSERT INTO public.base_area VALUES ('210624', '宽甸满族自治县', '2106');
INSERT INTO public.base_area VALUES ('210681', '东港市', '2106');
INSERT INTO public.base_area VALUES ('210682', '凤城市', '2106');
INSERT INTO public.base_area VALUES ('210702', '古塔区', '2107');
INSERT INTO public.base_area VALUES ('210703', '凌河区', '2107');
INSERT INTO public.base_area VALUES ('210711', '太和区', '2107');
INSERT INTO public.base_area VALUES ('210726', '黑山县', '2107');
INSERT INTO public.base_area VALUES ('210727', '义县', '2107');
INSERT INTO public.base_area VALUES ('210781', '凌海市', '2107');
INSERT INTO public.base_area VALUES ('210782', '北镇市', '2107');
INSERT INTO public.base_area VALUES ('210802', '站前区', '2108');
INSERT INTO public.base_area VALUES ('210803', '西市区', '2108');
INSERT INTO public.base_area VALUES ('210804', '鲅鱼圈区', '2108');
INSERT INTO public.base_area VALUES ('210811', '老边区', '2108');
INSERT INTO public.base_area VALUES ('210881', '盖州市', '2108');
INSERT INTO public.base_area VALUES ('210882', '大石桥市', '2108');
INSERT INTO public.base_area VALUES ('210902', '海州区', '2109');
INSERT INTO public.base_area VALUES ('210903', '新邱区', '2109');
INSERT INTO public.base_area VALUES ('210904', '太平区', '2109');
INSERT INTO public.base_area VALUES ('210905', '清河门区', '2109');
INSERT INTO public.base_area VALUES ('210911', '细河区', '2109');
INSERT INTO public.base_area VALUES ('210921', '阜新蒙古族自治县', '2109');
INSERT INTO public.base_area VALUES ('210922', '彰武县', '2109');
INSERT INTO public.base_area VALUES ('211002', '白塔区', '2110');
INSERT INTO public.base_area VALUES ('211003', '文圣区', '2110');
INSERT INTO public.base_area VALUES ('211004', '宏伟区', '2110');
INSERT INTO public.base_area VALUES ('211005', '弓长岭区', '2110');
INSERT INTO public.base_area VALUES ('211011', '太子河区', '2110');
INSERT INTO public.base_area VALUES ('211021', '辽阳县', '2110');
INSERT INTO public.base_area VALUES ('211081', '灯塔市', '2110');
INSERT INTO public.base_area VALUES ('211102', '双台子区', '2111');
INSERT INTO public.base_area VALUES ('211103', '兴隆台区', '2111');
INSERT INTO public.base_area VALUES ('211104', '大洼区', '2111');
INSERT INTO public.base_area VALUES ('211122', '盘山县', '2111');
INSERT INTO public.base_area VALUES ('211202', '银州区', '2112');
INSERT INTO public.base_area VALUES ('211204', '清河区', '2112');
INSERT INTO public.base_area VALUES ('211221', '铁岭县', '2112');
INSERT INTO public.base_area VALUES ('211223', '西丰县', '2112');
INSERT INTO public.base_area VALUES ('211224', '昌图县', '2112');
INSERT INTO public.base_area VALUES ('211281', '调兵山市', '2112');
INSERT INTO public.base_area VALUES ('211282', '开原市', '2112');
INSERT INTO public.base_area VALUES ('211302', '双塔区', '2113');
INSERT INTO public.base_area VALUES ('211303', '龙城区', '2113');
INSERT INTO public.base_area VALUES ('211321', '朝阳县', '2113');
INSERT INTO public.base_area VALUES ('211322', '建平县', '2113');
INSERT INTO public.base_area VALUES ('211324', '喀喇沁左翼蒙古族自治县', '2113');
INSERT INTO public.base_area VALUES ('211381', '北票市', '2113');
INSERT INTO public.base_area VALUES ('211382', '凌源市', '2113');
INSERT INTO public.base_area VALUES ('211402', '连山区', '2114');
INSERT INTO public.base_area VALUES ('211403', '龙港区', '2114');
INSERT INTO public.base_area VALUES ('211404', '南票区', '2114');
INSERT INTO public.base_area VALUES ('211421', '绥中县', '2114');
INSERT INTO public.base_area VALUES ('211422', '建昌县', '2114');
INSERT INTO public.base_area VALUES ('211481', '兴城市', '2114');
INSERT INTO public.base_area VALUES ('220102', '南关区', '2201');
INSERT INTO public.base_area VALUES ('220103', '宽城区', '2201');
INSERT INTO public.base_area VALUES ('220104', '朝阳区', '2201');
INSERT INTO public.base_area VALUES ('220105', '二道区', '2201');
INSERT INTO public.base_area VALUES ('220106', '绿园区', '2201');
INSERT INTO public.base_area VALUES ('220112', '双阳区', '2201');
INSERT INTO public.base_area VALUES ('220113', '九台区', '2201');
INSERT INTO public.base_area VALUES ('220122', '农安县', '2201');
INSERT INTO public.base_area VALUES ('220171', '长春经济技术开发区', '2201');
INSERT INTO public.base_area VALUES ('220172', '长春净月高新技术产业开发区', '2201');
INSERT INTO public.base_area VALUES ('220173', '长春高新技术产业开发区', '2201');
INSERT INTO public.base_area VALUES ('220174', '长春汽车经济技术开发区', '2201');
INSERT INTO public.base_area VALUES ('220182', '榆树市', '2201');
INSERT INTO public.base_area VALUES ('220183', '德惠市', '2201');
INSERT INTO public.base_area VALUES ('220184', '公主岭市', '2201');
INSERT INTO public.base_area VALUES ('220202', '昌邑区', '2202');
INSERT INTO public.base_area VALUES ('220203', '龙潭区', '2202');
INSERT INTO public.base_area VALUES ('220204', '船营区', '2202');
INSERT INTO public.base_area VALUES ('220211', '丰满区', '2202');
INSERT INTO public.base_area VALUES ('220221', '永吉县', '2202');
INSERT INTO public.base_area VALUES ('220271', '吉林经济开发区', '2202');
INSERT INTO public.base_area VALUES ('220272', '吉林高新技术产业开发区', '2202');
INSERT INTO public.base_area VALUES ('220273', '吉林中国新加坡食品区', '2202');
INSERT INTO public.base_area VALUES ('220281', '蛟河市', '2202');
INSERT INTO public.base_area VALUES ('220282', '桦甸市', '2202');
INSERT INTO public.base_area VALUES ('220283', '舒兰市', '2202');
INSERT INTO public.base_area VALUES ('220284', '磐石市', '2202');
INSERT INTO public.base_area VALUES ('220302', '铁西区', '2203');
INSERT INTO public.base_area VALUES ('220303', '铁东区', '2203');
INSERT INTO public.base_area VALUES ('220322', '梨树县', '2203');
INSERT INTO public.base_area VALUES ('220323', '伊通满族自治县', '2203');
INSERT INTO public.base_area VALUES ('220382', '双辽市', '2203');
INSERT INTO public.base_area VALUES ('220402', '龙山区', '2204');
INSERT INTO public.base_area VALUES ('220403', '西安区', '2204');
INSERT INTO public.base_area VALUES ('220421', '东丰县', '2204');
INSERT INTO public.base_area VALUES ('220422', '东辽县', '2204');
INSERT INTO public.base_area VALUES ('220502', '东昌区', '2205');
INSERT INTO public.base_area VALUES ('220503', '二道江区', '2205');
INSERT INTO public.base_area VALUES ('220521', '通化县', '2205');
INSERT INTO public.base_area VALUES ('220523', '辉南县', '2205');
INSERT INTO public.base_area VALUES ('220524', '柳河县', '2205');
INSERT INTO public.base_area VALUES ('220581', '梅河口市', '2205');
INSERT INTO public.base_area VALUES ('220582', '集安市', '2205');
INSERT INTO public.base_area VALUES ('220602', '浑江区', '2206');
INSERT INTO public.base_area VALUES ('220605', '江源区', '2206');
INSERT INTO public.base_area VALUES ('220621', '抚松县', '2206');
INSERT INTO public.base_area VALUES ('220622', '靖宇县', '2206');
INSERT INTO public.base_area VALUES ('220623', '长白朝鲜族自治县', '2206');
INSERT INTO public.base_area VALUES ('220681', '临江市', '2206');
INSERT INTO public.base_area VALUES ('220702', '宁江区', '2207');
INSERT INTO public.base_area VALUES ('220721', '前郭尔罗斯蒙古族自治县', '2207');
INSERT INTO public.base_area VALUES ('220722', '长岭县', '2207');
INSERT INTO public.base_area VALUES ('220723', '乾安县', '2207');
INSERT INTO public.base_area VALUES ('220771', '吉林松原经济开发区', '2207');
INSERT INTO public.base_area VALUES ('220781', '扶余市', '2207');
INSERT INTO public.base_area VALUES ('220802', '洮北区', '2208');
INSERT INTO public.base_area VALUES ('220821', '镇赉县', '2208');
INSERT INTO public.base_area VALUES ('220822', '通榆县', '2208');
INSERT INTO public.base_area VALUES ('220871', '吉林白城经济开发区', '2208');
INSERT INTO public.base_area VALUES ('220881', '洮南市', '2208');
INSERT INTO public.base_area VALUES ('220882', '大安市', '2208');
INSERT INTO public.base_area VALUES ('222401', '延吉市', '2224');
INSERT INTO public.base_area VALUES ('222402', '图们市', '2224');
INSERT INTO public.base_area VALUES ('222403', '敦化市', '2224');
INSERT INTO public.base_area VALUES ('222404', '珲春市', '2224');
INSERT INTO public.base_area VALUES ('222405', '龙井市', '2224');
INSERT INTO public.base_area VALUES ('222406', '和龙市', '2224');
INSERT INTO public.base_area VALUES ('222424', '汪清县', '2224');
INSERT INTO public.base_area VALUES ('222426', '安图县', '2224');
INSERT INTO public.base_area VALUES ('230102', '道里区', '2301');
INSERT INTO public.base_area VALUES ('230103', '南岗区', '2301');
INSERT INTO public.base_area VALUES ('230104', '道外区', '2301');
INSERT INTO public.base_area VALUES ('230108', '平房区', '2301');
INSERT INTO public.base_area VALUES ('230109', '松北区', '2301');
INSERT INTO public.base_area VALUES ('230110', '香坊区', '2301');
INSERT INTO public.base_area VALUES ('230111', '呼兰区', '2301');
INSERT INTO public.base_area VALUES ('230112', '阿城区', '2301');
INSERT INTO public.base_area VALUES ('230113', '双城区', '2301');
INSERT INTO public.base_area VALUES ('230123', '依兰县', '2301');
INSERT INTO public.base_area VALUES ('230124', '方正县', '2301');
INSERT INTO public.base_area VALUES ('230125', '宾县', '2301');
INSERT INTO public.base_area VALUES ('230126', '巴彦县', '2301');
INSERT INTO public.base_area VALUES ('230127', '木兰县', '2301');
INSERT INTO public.base_area VALUES ('230128', '通河县', '2301');
INSERT INTO public.base_area VALUES ('230129', '延寿县', '2301');
INSERT INTO public.base_area VALUES ('230183', '尚志市', '2301');
INSERT INTO public.base_area VALUES ('230184', '五常市', '2301');
INSERT INTO public.base_area VALUES ('230202', '龙沙区', '2302');
INSERT INTO public.base_area VALUES ('230203', '建华区', '2302');
INSERT INTO public.base_area VALUES ('230204', '铁锋区', '2302');
INSERT INTO public.base_area VALUES ('230205', '昂昂溪区', '2302');
INSERT INTO public.base_area VALUES ('230206', '富拉尔基区', '2302');
INSERT INTO public.base_area VALUES ('230207', '碾子山区', '2302');
INSERT INTO public.base_area VALUES ('230208', '梅里斯达斡尔族区', '2302');
INSERT INTO public.base_area VALUES ('230221', '龙江县', '2302');
INSERT INTO public.base_area VALUES ('230223', '依安县', '2302');
INSERT INTO public.base_area VALUES ('230224', '泰来县', '2302');
INSERT INTO public.base_area VALUES ('230225', '甘南县', '2302');
INSERT INTO public.base_area VALUES ('230227', '富裕县', '2302');
INSERT INTO public.base_area VALUES ('230229', '克山县', '2302');
INSERT INTO public.base_area VALUES ('230230', '克东县', '2302');
INSERT INTO public.base_area VALUES ('230231', '拜泉县', '2302');
INSERT INTO public.base_area VALUES ('230281', '讷河市', '2302');
INSERT INTO public.base_area VALUES ('230302', '鸡冠区', '2303');
INSERT INTO public.base_area VALUES ('230303', '恒山区', '2303');
INSERT INTO public.base_area VALUES ('230304', '滴道区', '2303');
INSERT INTO public.base_area VALUES ('230305', '梨树区', '2303');
INSERT INTO public.base_area VALUES ('230306', '城子河区', '2303');
INSERT INTO public.base_area VALUES ('230307', '麻山区', '2303');
INSERT INTO public.base_area VALUES ('230321', '鸡东县', '2303');
INSERT INTO public.base_area VALUES ('230381', '虎林市', '2303');
INSERT INTO public.base_area VALUES ('230382', '密山市', '2303');
INSERT INTO public.base_area VALUES ('230402', '向阳区', '2304');
INSERT INTO public.base_area VALUES ('230403', '工农区', '2304');
INSERT INTO public.base_area VALUES ('230404', '南山区', '2304');
INSERT INTO public.base_area VALUES ('230405', '兴安区', '2304');
INSERT INTO public.base_area VALUES ('230406', '东山区', '2304');
INSERT INTO public.base_area VALUES ('230407', '兴山区', '2304');
INSERT INTO public.base_area VALUES ('230421', '萝北县', '2304');
INSERT INTO public.base_area VALUES ('230422', '绥滨县', '2304');
INSERT INTO public.base_area VALUES ('230502', '尖山区', '2305');
INSERT INTO public.base_area VALUES ('230503', '岭东区', '2305');
INSERT INTO public.base_area VALUES ('230505', '四方台区', '2305');
INSERT INTO public.base_area VALUES ('230506', '宝山区', '2305');
INSERT INTO public.base_area VALUES ('230521', '集贤县', '2305');
INSERT INTO public.base_area VALUES ('230522', '友谊县', '2305');
INSERT INTO public.base_area VALUES ('230523', '宝清县', '2305');
INSERT INTO public.base_area VALUES ('230524', '饶河县', '2305');
INSERT INTO public.base_area VALUES ('230602', '萨尔图区', '2306');
INSERT INTO public.base_area VALUES ('230603', '龙凤区', '2306');
INSERT INTO public.base_area VALUES ('230604', '让胡路区', '2306');
INSERT INTO public.base_area VALUES ('230605', '红岗区', '2306');
INSERT INTO public.base_area VALUES ('230606', '大同区', '2306');
INSERT INTO public.base_area VALUES ('230621', '肇州县', '2306');
INSERT INTO public.base_area VALUES ('230622', '肇源县', '2306');
INSERT INTO public.base_area VALUES ('230623', '林甸县', '2306');
INSERT INTO public.base_area VALUES ('230624', '杜尔伯特蒙古族自治县', '2306');
INSERT INTO public.base_area VALUES ('230671', '大庆高新技术产业开发区', '2306');
INSERT INTO public.base_area VALUES ('230717', '伊美区', '2307');
INSERT INTO public.base_area VALUES ('230718', '乌翠区', '2307');
INSERT INTO public.base_area VALUES ('230719', '友好区', '2307');
INSERT INTO public.base_area VALUES ('230722', '嘉荫县', '2307');
INSERT INTO public.base_area VALUES ('230723', '汤旺县', '2307');
INSERT INTO public.base_area VALUES ('230724', '丰林县', '2307');
INSERT INTO public.base_area VALUES ('230725', '大箐山县', '2307');
INSERT INTO public.base_area VALUES ('230726', '南岔县', '2307');
INSERT INTO public.base_area VALUES ('230751', '金林区', '2307');
INSERT INTO public.base_area VALUES ('230781', '铁力市', '2307');
INSERT INTO public.base_area VALUES ('230803', '向阳区', '2308');
INSERT INTO public.base_area VALUES ('230804', '前进区', '2308');
INSERT INTO public.base_area VALUES ('230805', '东风区', '2308');
INSERT INTO public.base_area VALUES ('230811', '郊区', '2308');
INSERT INTO public.base_area VALUES ('230822', '桦南县', '2308');
INSERT INTO public.base_area VALUES ('230826', '桦川县', '2308');
INSERT INTO public.base_area VALUES ('230828', '汤原县', '2308');
INSERT INTO public.base_area VALUES ('230881', '同江市', '2308');
INSERT INTO public.base_area VALUES ('230882', '富锦市', '2308');
INSERT INTO public.base_area VALUES ('230883', '抚远市', '2308');
INSERT INTO public.base_area VALUES ('230902', '新兴区', '2309');
INSERT INTO public.base_area VALUES ('230903', '桃山区', '2309');
INSERT INTO public.base_area VALUES ('230904', '茄子河区', '2309');
INSERT INTO public.base_area VALUES ('230921', '勃利县', '2309');
INSERT INTO public.base_area VALUES ('231002', '东安区', '2310');
INSERT INTO public.base_area VALUES ('231003', '阳明区', '2310');
INSERT INTO public.base_area VALUES ('231004', '爱民区', '2310');
INSERT INTO public.base_area VALUES ('231005', '西安区', '2310');
INSERT INTO public.base_area VALUES ('231025', '林口县', '2310');
INSERT INTO public.base_area VALUES ('231071', '牡丹江经济技术开发区', '2310');
INSERT INTO public.base_area VALUES ('231081', '绥芬河市', '2310');
INSERT INTO public.base_area VALUES ('231083', '海林市', '2310');
INSERT INTO public.base_area VALUES ('231084', '宁安市', '2310');
INSERT INTO public.base_area VALUES ('231085', '穆棱市', '2310');
INSERT INTO public.base_area VALUES ('231086', '东宁市', '2310');
INSERT INTO public.base_area VALUES ('231102', '爱辉区', '2311');
INSERT INTO public.base_area VALUES ('231123', '逊克县', '2311');
INSERT INTO public.base_area VALUES ('231124', '孙吴县', '2311');
INSERT INTO public.base_area VALUES ('231181', '北安市', '2311');
INSERT INTO public.base_area VALUES ('231182', '五大连池市', '2311');
INSERT INTO public.base_area VALUES ('231183', '嫩江市', '2311');
INSERT INTO public.base_area VALUES ('231202', '北林区', '2312');
INSERT INTO public.base_area VALUES ('231221', '望奎县', '2312');
INSERT INTO public.base_area VALUES ('231222', '兰西县', '2312');
INSERT INTO public.base_area VALUES ('231223', '青冈县', '2312');
INSERT INTO public.base_area VALUES ('231224', '庆安县', '2312');
INSERT INTO public.base_area VALUES ('231225', '明水县', '2312');
INSERT INTO public.base_area VALUES ('231226', '绥棱县', '2312');
INSERT INTO public.base_area VALUES ('231281', '安达市', '2312');
INSERT INTO public.base_area VALUES ('231282', '肇东市', '2312');
INSERT INTO public.base_area VALUES ('231283', '海伦市', '2312');
INSERT INTO public.base_area VALUES ('232701', '漠河市', '2327');
INSERT INTO public.base_area VALUES ('232721', '呼玛县', '2327');
INSERT INTO public.base_area VALUES ('232722', '塔河县', '2327');
INSERT INTO public.base_area VALUES ('232761', '加格达奇区', '2327');
INSERT INTO public.base_area VALUES ('232762', '松岭区', '2327');
INSERT INTO public.base_area VALUES ('232763', '新林区', '2327');
INSERT INTO public.base_area VALUES ('232764', '呼中区', '2327');
INSERT INTO public.base_area VALUES ('310101', '黄浦区', '3101');
INSERT INTO public.base_area VALUES ('310104', '徐汇区', '3101');
INSERT INTO public.base_area VALUES ('310105', '长宁区', '3101');
INSERT INTO public.base_area VALUES ('310106', '静安区', '3101');
INSERT INTO public.base_area VALUES ('310107', '普陀区', '3101');
INSERT INTO public.base_area VALUES ('310109', '虹口区', '3101');
INSERT INTO public.base_area VALUES ('310110', '杨浦区', '3101');
INSERT INTO public.base_area VALUES ('310112', '闵行区', '3101');
INSERT INTO public.base_area VALUES ('310113', '宝山区', '3101');
INSERT INTO public.base_area VALUES ('310114', '嘉定区', '3101');
INSERT INTO public.base_area VALUES ('310115', '浦东新区', '3101');
INSERT INTO public.base_area VALUES ('310116', '金山区', '3101');
INSERT INTO public.base_area VALUES ('310117', '松江区', '3101');
INSERT INTO public.base_area VALUES ('310118', '青浦区', '3101');
INSERT INTO public.base_area VALUES ('310120', '奉贤区', '3101');
INSERT INTO public.base_area VALUES ('310151', '崇明区', '3101');
INSERT INTO public.base_area VALUES ('320102', '玄武区', '3201');
INSERT INTO public.base_area VALUES ('320104', '秦淮区', '3201');
INSERT INTO public.base_area VALUES ('320105', '建邺区', '3201');
INSERT INTO public.base_area VALUES ('320106', '鼓楼区', '3201');
INSERT INTO public.base_area VALUES ('320111', '浦口区', '3201');
INSERT INTO public.base_area VALUES ('320113', '栖霞区', '3201');
INSERT INTO public.base_area VALUES ('320114', '雨花台区', '3201');
INSERT INTO public.base_area VALUES ('320115', '江宁区', '3201');
INSERT INTO public.base_area VALUES ('320116', '六合区', '3201');
INSERT INTO public.base_area VALUES ('320117', '溧水区', '3201');
INSERT INTO public.base_area VALUES ('320118', '高淳区', '3201');
INSERT INTO public.base_area VALUES ('320205', '锡山区', '3202');
INSERT INTO public.base_area VALUES ('320206', '惠山区', '3202');
INSERT INTO public.base_area VALUES ('320211', '滨湖区', '3202');
INSERT INTO public.base_area VALUES ('320213', '梁溪区', '3202');
INSERT INTO public.base_area VALUES ('320214', '新吴区', '3202');
INSERT INTO public.base_area VALUES ('320281', '江阴市', '3202');
INSERT INTO public.base_area VALUES ('320282', '宜兴市', '3202');
INSERT INTO public.base_area VALUES ('320302', '鼓楼区', '3203');
INSERT INTO public.base_area VALUES ('320303', '云龙区', '3203');
INSERT INTO public.base_area VALUES ('320305', '贾汪区', '3203');
INSERT INTO public.base_area VALUES ('320311', '泉山区', '3203');
INSERT INTO public.base_area VALUES ('320312', '铜山区', '3203');
INSERT INTO public.base_area VALUES ('320321', '丰县', '3203');
INSERT INTO public.base_area VALUES ('320322', '沛县', '3203');
INSERT INTO public.base_area VALUES ('320324', '睢宁县', '3203');
INSERT INTO public.base_area VALUES ('320371', '徐州经济技术开发区', '3203');
INSERT INTO public.base_area VALUES ('320381', '新沂市', '3203');
INSERT INTO public.base_area VALUES ('320382', '邳州市', '3203');
INSERT INTO public.base_area VALUES ('320402', '天宁区', '3204');
INSERT INTO public.base_area VALUES ('320404', '钟楼区', '3204');
INSERT INTO public.base_area VALUES ('320411', '新北区', '3204');
INSERT INTO public.base_area VALUES ('320412', '武进区', '3204');
INSERT INTO public.base_area VALUES ('320413', '金坛区', '3204');
INSERT INTO public.base_area VALUES ('320481', '溧阳市', '3204');
INSERT INTO public.base_area VALUES ('320505', '虎丘区', '3205');
INSERT INTO public.base_area VALUES ('320506', '吴中区', '3205');
INSERT INTO public.base_area VALUES ('320507', '相城区', '3205');
INSERT INTO public.base_area VALUES ('320508', '姑苏区', '3205');
INSERT INTO public.base_area VALUES ('320509', '吴江区', '3205');
INSERT INTO public.base_area VALUES ('320571', '苏州工业园区', '3205');
INSERT INTO public.base_area VALUES ('320581', '常熟市', '3205');
INSERT INTO public.base_area VALUES ('320582', '张家港市', '3205');
INSERT INTO public.base_area VALUES ('320583', '昆山市', '3205');
INSERT INTO public.base_area VALUES ('320585', '太仓市', '3205');
INSERT INTO public.base_area VALUES ('320612', '通州区', '3206');
INSERT INTO public.base_area VALUES ('320613', '崇川区', '3206');
INSERT INTO public.base_area VALUES ('320614', '海门区', '3206');
INSERT INTO public.base_area VALUES ('320623', '如东县', '3206');
INSERT INTO public.base_area VALUES ('320671', '南通经济技术开发区', '3206');
INSERT INTO public.base_area VALUES ('320681', '启东市', '3206');
INSERT INTO public.base_area VALUES ('320682', '如皋市', '3206');
INSERT INTO public.base_area VALUES ('320685', '海安市', '3206');
INSERT INTO public.base_area VALUES ('320703', '连云区', '3207');
INSERT INTO public.base_area VALUES ('320706', '海州区', '3207');
INSERT INTO public.base_area VALUES ('320707', '赣榆区', '3207');
INSERT INTO public.base_area VALUES ('320722', '东海县', '3207');
INSERT INTO public.base_area VALUES ('320723', '灌云县', '3207');
INSERT INTO public.base_area VALUES ('320724', '灌南县', '3207');
INSERT INTO public.base_area VALUES ('320771', '连云港经济技术开发区', '3207');
INSERT INTO public.base_area VALUES ('320772', '连云港高新技术产业开发区', '3207');
INSERT INTO public.base_area VALUES ('320803', '淮安区', '3208');
INSERT INTO public.base_area VALUES ('320804', '淮阴区', '3208');
INSERT INTO public.base_area VALUES ('320812', '清江浦区', '3208');
INSERT INTO public.base_area VALUES ('320813', '洪泽区', '3208');
INSERT INTO public.base_area VALUES ('320826', '涟水县', '3208');
INSERT INTO public.base_area VALUES ('320830', '盱眙县', '3208');
INSERT INTO public.base_area VALUES ('320831', '金湖县', '3208');
INSERT INTO public.base_area VALUES ('320871', '淮安经济技术开发区', '3208');
INSERT INTO public.base_area VALUES ('320902', '亭湖区', '3209');
INSERT INTO public.base_area VALUES ('320903', '盐都区', '3209');
INSERT INTO public.base_area VALUES ('320904', '大丰区', '3209');
INSERT INTO public.base_area VALUES ('320921', '响水县', '3209');
INSERT INTO public.base_area VALUES ('320922', '滨海县', '3209');
INSERT INTO public.base_area VALUES ('320923', '阜宁县', '3209');
INSERT INTO public.base_area VALUES ('320924', '射阳县', '3209');
INSERT INTO public.base_area VALUES ('320925', '建湖县', '3209');
INSERT INTO public.base_area VALUES ('320971', '盐城经济技术开发区', '3209');
INSERT INTO public.base_area VALUES ('320981', '东台市', '3209');
INSERT INTO public.base_area VALUES ('321002', '广陵区', '3210');
INSERT INTO public.base_area VALUES ('321003', '邗江区', '3210');
INSERT INTO public.base_area VALUES ('321012', '江都区', '3210');
INSERT INTO public.base_area VALUES ('321023', '宝应县', '3210');
INSERT INTO public.base_area VALUES ('321071', '扬州经济技术开发区', '3210');
INSERT INTO public.base_area VALUES ('321081', '仪征市', '3210');
INSERT INTO public.base_area VALUES ('321084', '高邮市', '3210');
INSERT INTO public.base_area VALUES ('321102', '京口区', '3211');
INSERT INTO public.base_area VALUES ('321111', '润州区', '3211');
INSERT INTO public.base_area VALUES ('321112', '丹徒区', '3211');
INSERT INTO public.base_area VALUES ('321171', '镇江新区', '3211');
INSERT INTO public.base_area VALUES ('321181', '丹阳市', '3211');
INSERT INTO public.base_area VALUES ('321182', '扬中市', '3211');
INSERT INTO public.base_area VALUES ('321183', '句容市', '3211');
INSERT INTO public.base_area VALUES ('321202', '海陵区', '3212');
INSERT INTO public.base_area VALUES ('321203', '高港区', '3212');
INSERT INTO public.base_area VALUES ('321204', '姜堰区', '3212');
INSERT INTO public.base_area VALUES ('321271', '泰州医药高新技术产业开发区', '3212');
INSERT INTO public.base_area VALUES ('321281', '兴化市', '3212');
INSERT INTO public.base_area VALUES ('321282', '靖江市', '3212');
INSERT INTO public.base_area VALUES ('321283', '泰兴市', '3212');
INSERT INTO public.base_area VALUES ('321302', '宿城区', '3213');
INSERT INTO public.base_area VALUES ('321311', '宿豫区', '3213');
INSERT INTO public.base_area VALUES ('321322', '沭阳县', '3213');
INSERT INTO public.base_area VALUES ('321323', '泗阳县', '3213');
INSERT INTO public.base_area VALUES ('321324', '泗洪县', '3213');
INSERT INTO public.base_area VALUES ('321371', '宿迁经济技术开发区', '3213');
INSERT INTO public.base_area VALUES ('330102', '上城区', '3301');
INSERT INTO public.base_area VALUES ('330105', '拱墅区', '3301');
INSERT INTO public.base_area VALUES ('330106', '西湖区', '3301');
INSERT INTO public.base_area VALUES ('330108', '滨江区', '3301');
INSERT INTO public.base_area VALUES ('330109', '萧山区', '3301');
INSERT INTO public.base_area VALUES ('330110', '余杭区', '3301');
INSERT INTO public.base_area VALUES ('330111', '富阳区', '3301');
INSERT INTO public.base_area VALUES ('330112', '临安区', '3301');
INSERT INTO public.base_area VALUES ('330113', '临平区', '3301');
INSERT INTO public.base_area VALUES ('330114', '钱塘区', '3301');
INSERT INTO public.base_area VALUES ('330122', '桐庐县', '3301');
INSERT INTO public.base_area VALUES ('330127', '淳安县', '3301');
INSERT INTO public.base_area VALUES ('330182', '建德市', '3301');
INSERT INTO public.base_area VALUES ('330203', '海曙区', '3302');
INSERT INTO public.base_area VALUES ('330205', '江北区', '3302');
INSERT INTO public.base_area VALUES ('330206', '北仑区', '3302');
INSERT INTO public.base_area VALUES ('330211', '镇海区', '3302');
INSERT INTO public.base_area VALUES ('330212', '鄞州区', '3302');
INSERT INTO public.base_area VALUES ('330213', '奉化区', '3302');
INSERT INTO public.base_area VALUES ('330225', '象山县', '3302');
INSERT INTO public.base_area VALUES ('330226', '宁海县', '3302');
INSERT INTO public.base_area VALUES ('330281', '余姚市', '3302');
INSERT INTO public.base_area VALUES ('330282', '慈溪市', '3302');
INSERT INTO public.base_area VALUES ('330302', '鹿城区', '3303');
INSERT INTO public.base_area VALUES ('330303', '龙湾区', '3303');
INSERT INTO public.base_area VALUES ('330304', '瓯海区', '3303');
INSERT INTO public.base_area VALUES ('330305', '洞头区', '3303');
INSERT INTO public.base_area VALUES ('330324', '永嘉县', '3303');
INSERT INTO public.base_area VALUES ('330326', '平阳县', '3303');
INSERT INTO public.base_area VALUES ('330327', '苍南县', '3303');
INSERT INTO public.base_area VALUES ('330328', '文成县', '3303');
INSERT INTO public.base_area VALUES ('330329', '泰顺县', '3303');
INSERT INTO public.base_area VALUES ('330381', '瑞安市', '3303');
INSERT INTO public.base_area VALUES ('330382', '乐清市', '3303');
INSERT INTO public.base_area VALUES ('330383', '龙港市', '3303');
INSERT INTO public.base_area VALUES ('330402', '南湖区', '3304');
INSERT INTO public.base_area VALUES ('330411', '秀洲区', '3304');
INSERT INTO public.base_area VALUES ('330421', '嘉善县', '3304');
INSERT INTO public.base_area VALUES ('330424', '海盐县', '3304');
INSERT INTO public.base_area VALUES ('330481', '海宁市', '3304');
INSERT INTO public.base_area VALUES ('330482', '平湖市', '3304');
INSERT INTO public.base_area VALUES ('330483', '桐乡市', '3304');
INSERT INTO public.base_area VALUES ('330502', '吴兴区', '3305');
INSERT INTO public.base_area VALUES ('330503', '南浔区', '3305');
INSERT INTO public.base_area VALUES ('330521', '德清县', '3305');
INSERT INTO public.base_area VALUES ('330522', '长兴县', '3305');
INSERT INTO public.base_area VALUES ('330523', '安吉县', '3305');
INSERT INTO public.base_area VALUES ('330602', '越城区', '3306');
INSERT INTO public.base_area VALUES ('330603', '柯桥区', '3306');
INSERT INTO public.base_area VALUES ('330604', '上虞区', '3306');
INSERT INTO public.base_area VALUES ('330624', '新昌县', '3306');
INSERT INTO public.base_area VALUES ('330681', '诸暨市', '3306');
INSERT INTO public.base_area VALUES ('330683', '嵊州市', '3306');
INSERT INTO public.base_area VALUES ('330702', '婺城区', '3307');
INSERT INTO public.base_area VALUES ('330703', '金东区', '3307');
INSERT INTO public.base_area VALUES ('330723', '武义县', '3307');
INSERT INTO public.base_area VALUES ('330726', '浦江县', '3307');
INSERT INTO public.base_area VALUES ('330727', '磐安县', '3307');
INSERT INTO public.base_area VALUES ('330781', '兰溪市', '3307');
INSERT INTO public.base_area VALUES ('330782', '义乌市', '3307');
INSERT INTO public.base_area VALUES ('330783', '东阳市', '3307');
INSERT INTO public.base_area VALUES ('330784', '永康市', '3307');
INSERT INTO public.base_area VALUES ('330802', '柯城区', '3308');
INSERT INTO public.base_area VALUES ('330803', '衢江区', '3308');
INSERT INTO public.base_area VALUES ('330822', '常山县', '3308');
INSERT INTO public.base_area VALUES ('330824', '开化县', '3308');
INSERT INTO public.base_area VALUES ('330825', '龙游县', '3308');
INSERT INTO public.base_area VALUES ('330881', '江山市', '3308');
INSERT INTO public.base_area VALUES ('330902', '定海区', '3309');
INSERT INTO public.base_area VALUES ('330903', '普陀区', '3309');
INSERT INTO public.base_area VALUES ('330921', '岱山县', '3309');
INSERT INTO public.base_area VALUES ('330922', '嵊泗县', '3309');
INSERT INTO public.base_area VALUES ('331002', '椒江区', '3310');
INSERT INTO public.base_area VALUES ('331003', '黄岩区', '3310');
INSERT INTO public.base_area VALUES ('331004', '路桥区', '3310');
INSERT INTO public.base_area VALUES ('331022', '三门县', '3310');
INSERT INTO public.base_area VALUES ('331023', '天台县', '3310');
INSERT INTO public.base_area VALUES ('331024', '仙居县', '3310');
INSERT INTO public.base_area VALUES ('331081', '温岭市', '3310');
INSERT INTO public.base_area VALUES ('331082', '临海市', '3310');
INSERT INTO public.base_area VALUES ('331083', '玉环市', '3310');
INSERT INTO public.base_area VALUES ('331102', '莲都区', '3311');
INSERT INTO public.base_area VALUES ('331121', '青田县', '3311');
INSERT INTO public.base_area VALUES ('331122', '缙云县', '3311');
INSERT INTO public.base_area VALUES ('331123', '遂昌县', '3311');
INSERT INTO public.base_area VALUES ('331124', '松阳县', '3311');
INSERT INTO public.base_area VALUES ('331125', '云和县', '3311');
INSERT INTO public.base_area VALUES ('331126', '庆元县', '3311');
INSERT INTO public.base_area VALUES ('331127', '景宁畲族自治县', '3311');
INSERT INTO public.base_area VALUES ('331181', '龙泉市', '3311');
INSERT INTO public.base_area VALUES ('340102', '瑶海区', '3401');
INSERT INTO public.base_area VALUES ('340103', '庐阳区', '3401');
INSERT INTO public.base_area VALUES ('340104', '蜀山区', '3401');
INSERT INTO public.base_area VALUES ('340111', '包河区', '3401');
INSERT INTO public.base_area VALUES ('340121', '长丰县', '3401');
INSERT INTO public.base_area VALUES ('340122', '肥东县', '3401');
INSERT INTO public.base_area VALUES ('340123', '肥西县', '3401');
INSERT INTO public.base_area VALUES ('340124', '庐江县', '3401');
INSERT INTO public.base_area VALUES ('340171', '合肥高新技术产业开发区', '3401');
INSERT INTO public.base_area VALUES ('340172', '合肥经济技术开发区', '3401');
INSERT INTO public.base_area VALUES ('340173', '合肥新站高新技术产业开发区', '3401');
INSERT INTO public.base_area VALUES ('340181', '巢湖市', '3401');
INSERT INTO public.base_area VALUES ('340202', '镜湖区', '3402');
INSERT INTO public.base_area VALUES ('340207', '鸠江区', '3402');
INSERT INTO public.base_area VALUES ('340209', '弋江区', '3402');
INSERT INTO public.base_area VALUES ('340210', '湾沚区', '3402');
INSERT INTO public.base_area VALUES ('340212', '繁昌区', '3402');
INSERT INTO public.base_area VALUES ('340223', '南陵县', '3402');
INSERT INTO public.base_area VALUES ('340271', '芜湖经济技术开发区', '3402');
INSERT INTO public.base_area VALUES ('340272', '安徽芜湖三山经济开发区', '3402');
INSERT INTO public.base_area VALUES ('340281', '无为市', '3402');
INSERT INTO public.base_area VALUES ('340302', '龙子湖区', '3403');
INSERT INTO public.base_area VALUES ('340303', '蚌山区', '3403');
INSERT INTO public.base_area VALUES ('340304', '禹会区', '3403');
INSERT INTO public.base_area VALUES ('340311', '淮上区', '3403');
INSERT INTO public.base_area VALUES ('340321', '怀远县', '3403');
INSERT INTO public.base_area VALUES ('340322', '五河县', '3403');
INSERT INTO public.base_area VALUES ('340323', '固镇县', '3403');
INSERT INTO public.base_area VALUES ('340371', '蚌埠市高新技术开发区', '3403');
INSERT INTO public.base_area VALUES ('340372', '蚌埠市经济开发区', '3403');
INSERT INTO public.base_area VALUES ('340402', '大通区', '3404');
INSERT INTO public.base_area VALUES ('340403', '田家庵区', '3404');
INSERT INTO public.base_area VALUES ('340404', '谢家集区', '3404');
INSERT INTO public.base_area VALUES ('340405', '八公山区', '3404');
INSERT INTO public.base_area VALUES ('340406', '潘集区', '3404');
INSERT INTO public.base_area VALUES ('340421', '凤台县', '3404');
INSERT INTO public.base_area VALUES ('340422', '寿县', '3404');
INSERT INTO public.base_area VALUES ('340503', '花山区', '3405');
INSERT INTO public.base_area VALUES ('340504', '雨山区', '3405');
INSERT INTO public.base_area VALUES ('340506', '博望区', '3405');
INSERT INTO public.base_area VALUES ('340521', '当涂县', '3405');
INSERT INTO public.base_area VALUES ('340522', '含山县', '3405');
INSERT INTO public.base_area VALUES ('340523', '和县', '3405');
INSERT INTO public.base_area VALUES ('340602', '杜集区', '3406');
INSERT INTO public.base_area VALUES ('340603', '相山区', '3406');
INSERT INTO public.base_area VALUES ('340604', '烈山区', '3406');
INSERT INTO public.base_area VALUES ('340621', '濉溪县', '3406');
INSERT INTO public.base_area VALUES ('340705', '铜官区', '3407');
INSERT INTO public.base_area VALUES ('340706', '义安区', '3407');
INSERT INTO public.base_area VALUES ('340711', '郊区', '3407');
INSERT INTO public.base_area VALUES ('340722', '枞阳县', '3407');
INSERT INTO public.base_area VALUES ('340802', '迎江区', '3408');
INSERT INTO public.base_area VALUES ('340803', '大观区', '3408');
INSERT INTO public.base_area VALUES ('340811', '宜秀区', '3408');
INSERT INTO public.base_area VALUES ('340822', '怀宁县', '3408');
INSERT INTO public.base_area VALUES ('340825', '太湖县', '3408');
INSERT INTO public.base_area VALUES ('340826', '宿松县', '3408');
INSERT INTO public.base_area VALUES ('340827', '望江县', '3408');
INSERT INTO public.base_area VALUES ('340828', '岳西县', '3408');
INSERT INTO public.base_area VALUES ('340871', '安徽安庆经济开发区', '3408');
INSERT INTO public.base_area VALUES ('340881', '桐城市', '3408');
INSERT INTO public.base_area VALUES ('340882', '潜山市', '3408');
INSERT INTO public.base_area VALUES ('341002', '屯溪区', '3410');
INSERT INTO public.base_area VALUES ('341003', '黄山区', '3410');
INSERT INTO public.base_area VALUES ('341004', '徽州区', '3410');
INSERT INTO public.base_area VALUES ('341021', '歙县', '3410');
INSERT INTO public.base_area VALUES ('341022', '休宁县', '3410');
INSERT INTO public.base_area VALUES ('341023', '黟县', '3410');
INSERT INTO public.base_area VALUES ('341024', '祁门县', '3410');
INSERT INTO public.base_area VALUES ('341102', '琅琊区', '3411');
INSERT INTO public.base_area VALUES ('341103', '南谯区', '3411');
INSERT INTO public.base_area VALUES ('341122', '来安县', '3411');
INSERT INTO public.base_area VALUES ('341124', '全椒县', '3411');
INSERT INTO public.base_area VALUES ('341125', '定远县', '3411');
INSERT INTO public.base_area VALUES ('341126', '凤阳县', '3411');
INSERT INTO public.base_area VALUES ('341171', '中新苏滁高新技术产业开发区', '3411');
INSERT INTO public.base_area VALUES ('341172', '滁州经济技术开发区', '3411');
INSERT INTO public.base_area VALUES ('341181', '天长市', '3411');
INSERT INTO public.base_area VALUES ('341182', '明光市', '3411');
INSERT INTO public.base_area VALUES ('341202', '颍州区', '3412');
INSERT INTO public.base_area VALUES ('341203', '颍东区', '3412');
INSERT INTO public.base_area VALUES ('341204', '颍泉区', '3412');
INSERT INTO public.base_area VALUES ('341221', '临泉县', '3412');
INSERT INTO public.base_area VALUES ('341222', '太和县', '3412');
INSERT INTO public.base_area VALUES ('341225', '阜南县', '3412');
INSERT INTO public.base_area VALUES ('341226', '颍上县', '3412');
INSERT INTO public.base_area VALUES ('341271', '阜阳合肥现代产业园区', '3412');
INSERT INTO public.base_area VALUES ('341272', '阜阳经济技术开发区', '3412');
INSERT INTO public.base_area VALUES ('341282', '界首市', '3412');
INSERT INTO public.base_area VALUES ('341302', '埇桥区', '3413');
INSERT INTO public.base_area VALUES ('341321', '砀山县', '3413');
INSERT INTO public.base_area VALUES ('341322', '萧县', '3413');
INSERT INTO public.base_area VALUES ('341323', '灵璧县', '3413');
INSERT INTO public.base_area VALUES ('341324', '泗县', '3413');
INSERT INTO public.base_area VALUES ('341371', '宿州马鞍山现代产业园区', '3413');
INSERT INTO public.base_area VALUES ('341372', '宿州经济技术开发区', '3413');
INSERT INTO public.base_area VALUES ('341502', '金安区', '3415');
INSERT INTO public.base_area VALUES ('341503', '裕安区', '3415');
INSERT INTO public.base_area VALUES ('341504', '叶集区', '3415');
INSERT INTO public.base_area VALUES ('341522', '霍邱县', '3415');
INSERT INTO public.base_area VALUES ('341523', '舒城县', '3415');
INSERT INTO public.base_area VALUES ('341524', '金寨县', '3415');
INSERT INTO public.base_area VALUES ('341525', '霍山县', '3415');
INSERT INTO public.base_area VALUES ('341602', '谯城区', '3416');
INSERT INTO public.base_area VALUES ('341621', '涡阳县', '3416');
INSERT INTO public.base_area VALUES ('341622', '蒙城县', '3416');
INSERT INTO public.base_area VALUES ('341623', '利辛县', '3416');
INSERT INTO public.base_area VALUES ('341702', '贵池区', '3417');
INSERT INTO public.base_area VALUES ('341721', '东至县', '3417');
INSERT INTO public.base_area VALUES ('341722', '石台县', '3417');
INSERT INTO public.base_area VALUES ('341723', '青阳县', '3417');
INSERT INTO public.base_area VALUES ('341802', '宣州区', '3418');
INSERT INTO public.base_area VALUES ('341821', '郎溪县', '3418');
INSERT INTO public.base_area VALUES ('341823', '泾县', '3418');
INSERT INTO public.base_area VALUES ('341824', '绩溪县', '3418');
INSERT INTO public.base_area VALUES ('341825', '旌德县', '3418');
INSERT INTO public.base_area VALUES ('341871', '宣城市经济开发区', '3418');
INSERT INTO public.base_area VALUES ('341881', '宁国市', '3418');
INSERT INTO public.base_area VALUES ('341882', '广德市', '3418');
INSERT INTO public.base_area VALUES ('350102', '鼓楼区', '3501');
INSERT INTO public.base_area VALUES ('350103', '台江区', '3501');
INSERT INTO public.base_area VALUES ('350104', '仓山区', '3501');
INSERT INTO public.base_area VALUES ('350105', '马尾区', '3501');
INSERT INTO public.base_area VALUES ('350111', '晋安区', '3501');
INSERT INTO public.base_area VALUES ('350112', '长乐区', '3501');
INSERT INTO public.base_area VALUES ('350121', '闽侯县', '3501');
INSERT INTO public.base_area VALUES ('350122', '连江县', '3501');
INSERT INTO public.base_area VALUES ('350123', '罗源县', '3501');
INSERT INTO public.base_area VALUES ('350124', '闽清县', '3501');
INSERT INTO public.base_area VALUES ('350125', '永泰县', '3501');
INSERT INTO public.base_area VALUES ('350128', '平潭县', '3501');
INSERT INTO public.base_area VALUES ('350181', '福清市', '3501');
INSERT INTO public.base_area VALUES ('350203', '思明区', '3502');
INSERT INTO public.base_area VALUES ('350205', '海沧区', '3502');
INSERT INTO public.base_area VALUES ('350206', '湖里区', '3502');
INSERT INTO public.base_area VALUES ('350211', '集美区', '3502');
INSERT INTO public.base_area VALUES ('350212', '同安区', '3502');
INSERT INTO public.base_area VALUES ('350213', '翔安区', '3502');
INSERT INTO public.base_area VALUES ('350302', '城厢区', '3503');
INSERT INTO public.base_area VALUES ('350303', '涵江区', '3503');
INSERT INTO public.base_area VALUES ('350304', '荔城区', '3503');
INSERT INTO public.base_area VALUES ('350305', '秀屿区', '3503');
INSERT INTO public.base_area VALUES ('350322', '仙游县', '3503');
INSERT INTO public.base_area VALUES ('350404', '三元区', '3504');
INSERT INTO public.base_area VALUES ('350405', '沙县区', '3504');
INSERT INTO public.base_area VALUES ('350421', '明溪县', '3504');
INSERT INTO public.base_area VALUES ('350423', '清流县', '3504');
INSERT INTO public.base_area VALUES ('350424', '宁化县', '3504');
INSERT INTO public.base_area VALUES ('350425', '大田县', '3504');
INSERT INTO public.base_area VALUES ('350426', '尤溪县', '3504');
INSERT INTO public.base_area VALUES ('350428', '将乐县', '3504');
INSERT INTO public.base_area VALUES ('350429', '泰宁县', '3504');
INSERT INTO public.base_area VALUES ('350430', '建宁县', '3504');
INSERT INTO public.base_area VALUES ('350481', '永安市', '3504');
INSERT INTO public.base_area VALUES ('350502', '鲤城区', '3505');
INSERT INTO public.base_area VALUES ('350503', '丰泽区', '3505');
INSERT INTO public.base_area VALUES ('350504', '洛江区', '3505');
INSERT INTO public.base_area VALUES ('350505', '泉港区', '3505');
INSERT INTO public.base_area VALUES ('350521', '惠安县', '3505');
INSERT INTO public.base_area VALUES ('350524', '安溪县', '3505');
INSERT INTO public.base_area VALUES ('350525', '永春县', '3505');
INSERT INTO public.base_area VALUES ('350526', '德化县', '3505');
INSERT INTO public.base_area VALUES ('350527', '金门县', '3505');
INSERT INTO public.base_area VALUES ('350581', '石狮市', '3505');
INSERT INTO public.base_area VALUES ('350582', '晋江市', '3505');
INSERT INTO public.base_area VALUES ('350583', '南安市', '3505');
INSERT INTO public.base_area VALUES ('350602', '芗城区', '3506');
INSERT INTO public.base_area VALUES ('350603', '龙文区', '3506');
INSERT INTO public.base_area VALUES ('350604', '龙海区', '3506');
INSERT INTO public.base_area VALUES ('350605', '长泰区', '3506');
INSERT INTO public.base_area VALUES ('350622', '云霄县', '3506');
INSERT INTO public.base_area VALUES ('350623', '漳浦县', '3506');
INSERT INTO public.base_area VALUES ('350624', '诏安县', '3506');
INSERT INTO public.base_area VALUES ('350626', '东山县', '3506');
INSERT INTO public.base_area VALUES ('350627', '南靖县', '3506');
INSERT INTO public.base_area VALUES ('350628', '平和县', '3506');
INSERT INTO public.base_area VALUES ('350629', '华安县', '3506');
INSERT INTO public.base_area VALUES ('350702', '延平区', '3507');
INSERT INTO public.base_area VALUES ('350703', '建阳区', '3507');
INSERT INTO public.base_area VALUES ('350721', '顺昌县', '3507');
INSERT INTO public.base_area VALUES ('350722', '浦城县', '3507');
INSERT INTO public.base_area VALUES ('350723', '光泽县', '3507');
INSERT INTO public.base_area VALUES ('350724', '松溪县', '3507');
INSERT INTO public.base_area VALUES ('350725', '政和县', '3507');
INSERT INTO public.base_area VALUES ('350781', '邵武市', '3507');
INSERT INTO public.base_area VALUES ('350782', '武夷山市', '3507');
INSERT INTO public.base_area VALUES ('350783', '建瓯市', '3507');
INSERT INTO public.base_area VALUES ('350802', '新罗区', '3508');
INSERT INTO public.base_area VALUES ('350803', '永定区', '3508');
INSERT INTO public.base_area VALUES ('350821', '长汀县', '3508');
INSERT INTO public.base_area VALUES ('350823', '上杭县', '3508');
INSERT INTO public.base_area VALUES ('350824', '武平县', '3508');
INSERT INTO public.base_area VALUES ('350825', '连城县', '3508');
INSERT INTO public.base_area VALUES ('350881', '漳平市', '3508');
INSERT INTO public.base_area VALUES ('350902', '蕉城区', '3509');
INSERT INTO public.base_area VALUES ('350921', '霞浦县', '3509');
INSERT INTO public.base_area VALUES ('350922', '古田县', '3509');
INSERT INTO public.base_area VALUES ('350923', '屏南县', '3509');
INSERT INTO public.base_area VALUES ('350924', '寿宁县', '3509');
INSERT INTO public.base_area VALUES ('350925', '周宁县', '3509');
INSERT INTO public.base_area VALUES ('350926', '柘荣县', '3509');
INSERT INTO public.base_area VALUES ('350981', '福安市', '3509');
INSERT INTO public.base_area VALUES ('350982', '福鼎市', '3509');
INSERT INTO public.base_area VALUES ('360102', '东湖区', '3601');
INSERT INTO public.base_area VALUES ('360103', '西湖区', '3601');
INSERT INTO public.base_area VALUES ('360104', '青云谱区', '3601');
INSERT INTO public.base_area VALUES ('360111', '青山湖区', '3601');
INSERT INTO public.base_area VALUES ('360112', '新建区', '3601');
INSERT INTO public.base_area VALUES ('360113', '红谷滩区', '3601');
INSERT INTO public.base_area VALUES ('360121', '南昌县', '3601');
INSERT INTO public.base_area VALUES ('360123', '安义县', '3601');
INSERT INTO public.base_area VALUES ('360124', '进贤县', '3601');
INSERT INTO public.base_area VALUES ('360202', '昌江区', '3602');
INSERT INTO public.base_area VALUES ('360203', '珠山区', '3602');
INSERT INTO public.base_area VALUES ('360222', '浮梁县', '3602');
INSERT INTO public.base_area VALUES ('360281', '乐平市', '3602');
INSERT INTO public.base_area VALUES ('360302', '安源区', '3603');
INSERT INTO public.base_area VALUES ('360313', '湘东区', '3603');
INSERT INTO public.base_area VALUES ('360321', '莲花县', '3603');
INSERT INTO public.base_area VALUES ('360322', '上栗县', '3603');
INSERT INTO public.base_area VALUES ('360323', '芦溪县', '3603');
INSERT INTO public.base_area VALUES ('360402', '濂溪区', '3604');
INSERT INTO public.base_area VALUES ('360403', '浔阳区', '3604');
INSERT INTO public.base_area VALUES ('360404', '柴桑区', '3604');
INSERT INTO public.base_area VALUES ('360423', '武宁县', '3604');
INSERT INTO public.base_area VALUES ('360424', '修水县', '3604');
INSERT INTO public.base_area VALUES ('360425', '永修县', '3604');
INSERT INTO public.base_area VALUES ('360426', '德安县', '3604');
INSERT INTO public.base_area VALUES ('360428', '都昌县', '3604');
INSERT INTO public.base_area VALUES ('360429', '湖口县', '3604');
INSERT INTO public.base_area VALUES ('360430', '彭泽县', '3604');
INSERT INTO public.base_area VALUES ('360481', '瑞昌市', '3604');
INSERT INTO public.base_area VALUES ('360482', '共青城市', '3604');
INSERT INTO public.base_area VALUES ('360483', '庐山市', '3604');
INSERT INTO public.base_area VALUES ('360502', '渝水区', '3605');
INSERT INTO public.base_area VALUES ('360521', '分宜县', '3605');
INSERT INTO public.base_area VALUES ('360602', '月湖区', '3606');
INSERT INTO public.base_area VALUES ('360603', '余江区', '3606');
INSERT INTO public.base_area VALUES ('360681', '贵溪市', '3606');
INSERT INTO public.base_area VALUES ('360702', '章贡区', '3607');
INSERT INTO public.base_area VALUES ('360703', '南康区', '3607');
INSERT INTO public.base_area VALUES ('360704', '赣县区', '3607');
INSERT INTO public.base_area VALUES ('360722', '信丰县', '3607');
INSERT INTO public.base_area VALUES ('360723', '大余县', '3607');
INSERT INTO public.base_area VALUES ('360724', '上犹县', '3607');
INSERT INTO public.base_area VALUES ('360725', '崇义县', '3607');
INSERT INTO public.base_area VALUES ('360726', '安远县', '3607');
INSERT INTO public.base_area VALUES ('360728', '定南县', '3607');
INSERT INTO public.base_area VALUES ('360729', '全南县', '3607');
INSERT INTO public.base_area VALUES ('360730', '宁都县', '3607');
INSERT INTO public.base_area VALUES ('360731', '于都县', '3607');
INSERT INTO public.base_area VALUES ('360732', '兴国县', '3607');
INSERT INTO public.base_area VALUES ('360733', '会昌县', '3607');
INSERT INTO public.base_area VALUES ('360734', '寻乌县', '3607');
INSERT INTO public.base_area VALUES ('360735', '石城县', '3607');
INSERT INTO public.base_area VALUES ('360781', '瑞金市', '3607');
INSERT INTO public.base_area VALUES ('360783', '龙南市', '3607');
INSERT INTO public.base_area VALUES ('360802', '吉州区', '3608');
INSERT INTO public.base_area VALUES ('360803', '青原区', '3608');
INSERT INTO public.base_area VALUES ('360821', '吉安县', '3608');
INSERT INTO public.base_area VALUES ('360822', '吉水县', '3608');
INSERT INTO public.base_area VALUES ('360823', '峡江县', '3608');
INSERT INTO public.base_area VALUES ('360824', '新干县', '3608');
INSERT INTO public.base_area VALUES ('360825', '永丰县', '3608');
INSERT INTO public.base_area VALUES ('360826', '泰和县', '3608');
INSERT INTO public.base_area VALUES ('360827', '遂川县', '3608');
INSERT INTO public.base_area VALUES ('360828', '万安县', '3608');
INSERT INTO public.base_area VALUES ('360829', '安福县', '3608');
INSERT INTO public.base_area VALUES ('360830', '永新县', '3608');
INSERT INTO public.base_area VALUES ('360881', '井冈山市', '3608');
INSERT INTO public.base_area VALUES ('360902', '袁州区', '3609');
INSERT INTO public.base_area VALUES ('360921', '奉新县', '3609');
INSERT INTO public.base_area VALUES ('360922', '万载县', '3609');
INSERT INTO public.base_area VALUES ('360923', '上高县', '3609');
INSERT INTO public.base_area VALUES ('360924', '宜丰县', '3609');
INSERT INTO public.base_area VALUES ('360925', '靖安县', '3609');
INSERT INTO public.base_area VALUES ('360926', '铜鼓县', '3609');
INSERT INTO public.base_area VALUES ('360981', '丰城市', '3609');
INSERT INTO public.base_area VALUES ('360982', '樟树市', '3609');
INSERT INTO public.base_area VALUES ('360983', '高安市', '3609');
INSERT INTO public.base_area VALUES ('361002', '临川区', '3610');
INSERT INTO public.base_area VALUES ('361003', '东乡区', '3610');
INSERT INTO public.base_area VALUES ('361021', '南城县', '3610');
INSERT INTO public.base_area VALUES ('361022', '黎川县', '3610');
INSERT INTO public.base_area VALUES ('361023', '南丰县', '3610');
INSERT INTO public.base_area VALUES ('361024', '崇仁县', '3610');
INSERT INTO public.base_area VALUES ('361025', '乐安县', '3610');
INSERT INTO public.base_area VALUES ('361026', '宜黄县', '3610');
INSERT INTO public.base_area VALUES ('361027', '金溪县', '3610');
INSERT INTO public.base_area VALUES ('361028', '资溪县', '3610');
INSERT INTO public.base_area VALUES ('361030', '广昌县', '3610');
INSERT INTO public.base_area VALUES ('361102', '信州区', '3611');
INSERT INTO public.base_area VALUES ('361103', '广丰区', '3611');
INSERT INTO public.base_area VALUES ('361104', '广信区', '3611');
INSERT INTO public.base_area VALUES ('361123', '玉山县', '3611');
INSERT INTO public.base_area VALUES ('361124', '铅山县', '3611');
INSERT INTO public.base_area VALUES ('361125', '横峰县', '3611');
INSERT INTO public.base_area VALUES ('361126', '弋阳县', '3611');
INSERT INTO public.base_area VALUES ('361127', '余干县', '3611');
INSERT INTO public.base_area VALUES ('361128', '鄱阳县', '3611');
INSERT INTO public.base_area VALUES ('361129', '万年县', '3611');
INSERT INTO public.base_area VALUES ('361130', '婺源县', '3611');
INSERT INTO public.base_area VALUES ('361181', '德兴市', '3611');
INSERT INTO public.base_area VALUES ('370102', '历下区', '3701');
INSERT INTO public.base_area VALUES ('370103', '市中区', '3701');
INSERT INTO public.base_area VALUES ('370104', '槐荫区', '3701');
INSERT INTO public.base_area VALUES ('370105', '天桥区', '3701');
INSERT INTO public.base_area VALUES ('370112', '历城区', '3701');
INSERT INTO public.base_area VALUES ('370113', '长清区', '3701');
INSERT INTO public.base_area VALUES ('370114', '章丘区', '3701');
INSERT INTO public.base_area VALUES ('370115', '济阳区', '3701');
INSERT INTO public.base_area VALUES ('370116', '莱芜区', '3701');
INSERT INTO public.base_area VALUES ('370117', '钢城区', '3701');
INSERT INTO public.base_area VALUES ('370124', '平阴县', '3701');
INSERT INTO public.base_area VALUES ('370126', '商河县', '3701');
INSERT INTO public.base_area VALUES ('370171', '济南高新技术产业开发区', '3701');
INSERT INTO public.base_area VALUES ('370202', '市南区', '3702');
INSERT INTO public.base_area VALUES ('370203', '市北区', '3702');
INSERT INTO public.base_area VALUES ('370211', '黄岛区', '3702');
INSERT INTO public.base_area VALUES ('370212', '崂山区', '3702');
INSERT INTO public.base_area VALUES ('370213', '李沧区', '3702');
INSERT INTO public.base_area VALUES ('370214', '城阳区', '3702');
INSERT INTO public.base_area VALUES ('370215', '即墨区', '3702');
INSERT INTO public.base_area VALUES ('370271', '青岛高新技术产业开发区', '3702');
INSERT INTO public.base_area VALUES ('370281', '胶州市', '3702');
INSERT INTO public.base_area VALUES ('370283', '平度市', '3702');
INSERT INTO public.base_area VALUES ('370285', '莱西市', '3702');
INSERT INTO public.base_area VALUES ('370302', '淄川区', '3703');
INSERT INTO public.base_area VALUES ('370303', '张店区', '3703');
INSERT INTO public.base_area VALUES ('370304', '博山区', '3703');
INSERT INTO public.base_area VALUES ('370305', '临淄区', '3703');
INSERT INTO public.base_area VALUES ('370306', '周村区', '3703');
INSERT INTO public.base_area VALUES ('370321', '桓台县', '3703');
INSERT INTO public.base_area VALUES ('370322', '高青县', '3703');
INSERT INTO public.base_area VALUES ('370323', '沂源县', '3703');
INSERT INTO public.base_area VALUES ('370402', '市中区', '3704');
INSERT INTO public.base_area VALUES ('370403', '薛城区', '3704');
INSERT INTO public.base_area VALUES ('370404', '峄城区', '3704');
INSERT INTO public.base_area VALUES ('370405', '台儿庄区', '3704');
INSERT INTO public.base_area VALUES ('370406', '山亭区', '3704');
INSERT INTO public.base_area VALUES ('370481', '滕州市', '3704');
INSERT INTO public.base_area VALUES ('370502', '东营区', '3705');
INSERT INTO public.base_area VALUES ('370503', '河口区', '3705');
INSERT INTO public.base_area VALUES ('370505', '垦利区', '3705');
INSERT INTO public.base_area VALUES ('370522', '利津县', '3705');
INSERT INTO public.base_area VALUES ('370523', '广饶县', '3705');
INSERT INTO public.base_area VALUES ('370571', '东营经济技术开发区', '3705');
INSERT INTO public.base_area VALUES ('370572', '东营港经济开发区', '3705');
INSERT INTO public.base_area VALUES ('370602', '芝罘区', '3706');
INSERT INTO public.base_area VALUES ('370611', '福山区', '3706');
INSERT INTO public.base_area VALUES ('370612', '牟平区', '3706');
INSERT INTO public.base_area VALUES ('370613', '莱山区', '3706');
INSERT INTO public.base_area VALUES ('370614', '蓬莱区', '3706');
INSERT INTO public.base_area VALUES ('370671', '烟台高新技术产业开发区', '3706');
INSERT INTO public.base_area VALUES ('370672', '烟台经济技术开发区', '3706');
INSERT INTO public.base_area VALUES ('370681', '龙口市', '3706');
INSERT INTO public.base_area VALUES ('370682', '莱阳市', '3706');
INSERT INTO public.base_area VALUES ('370683', '莱州市', '3706');
INSERT INTO public.base_area VALUES ('370685', '招远市', '3706');
INSERT INTO public.base_area VALUES ('370686', '栖霞市', '3706');
INSERT INTO public.base_area VALUES ('370687', '海阳市', '3706');
INSERT INTO public.base_area VALUES ('370702', '潍城区', '3707');
INSERT INTO public.base_area VALUES ('370703', '寒亭区', '3707');
INSERT INTO public.base_area VALUES ('370704', '坊子区', '3707');
INSERT INTO public.base_area VALUES ('370705', '奎文区', '3707');
INSERT INTO public.base_area VALUES ('370724', '临朐县', '3707');
INSERT INTO public.base_area VALUES ('370725', '昌乐县', '3707');
INSERT INTO public.base_area VALUES ('370772', '潍坊滨海经济技术开发区', '3707');
INSERT INTO public.base_area VALUES ('370781', '青州市', '3707');
INSERT INTO public.base_area VALUES ('370782', '诸城市', '3707');
INSERT INTO public.base_area VALUES ('370783', '寿光市', '3707');
INSERT INTO public.base_area VALUES ('370784', '安丘市', '3707');
INSERT INTO public.base_area VALUES ('370785', '高密市', '3707');
INSERT INTO public.base_area VALUES ('370786', '昌邑市', '3707');
INSERT INTO public.base_area VALUES ('370811', '任城区', '3708');
INSERT INTO public.base_area VALUES ('370812', '兖州区', '3708');
INSERT INTO public.base_area VALUES ('370826', '微山县', '3708');
INSERT INTO public.base_area VALUES ('370827', '鱼台县', '3708');
INSERT INTO public.base_area VALUES ('370828', '金乡县', '3708');
INSERT INTO public.base_area VALUES ('370829', '嘉祥县', '3708');
INSERT INTO public.base_area VALUES ('370830', '汶上县', '3708');
INSERT INTO public.base_area VALUES ('370831', '泗水县', '3708');
INSERT INTO public.base_area VALUES ('370832', '梁山县', '3708');
INSERT INTO public.base_area VALUES ('370871', '济宁高新技术产业开发区', '3708');
INSERT INTO public.base_area VALUES ('370881', '曲阜市', '3708');
INSERT INTO public.base_area VALUES ('370883', '邹城市', '3708');
INSERT INTO public.base_area VALUES ('370902', '泰山区', '3709');
INSERT INTO public.base_area VALUES ('370911', '岱岳区', '3709');
INSERT INTO public.base_area VALUES ('370921', '宁阳县', '3709');
INSERT INTO public.base_area VALUES ('370923', '东平县', '3709');
INSERT INTO public.base_area VALUES ('370982', '新泰市', '3709');
INSERT INTO public.base_area VALUES ('370983', '肥城市', '3709');
INSERT INTO public.base_area VALUES ('371002', '环翠区', '3710');
INSERT INTO public.base_area VALUES ('371003', '文登区', '3710');
INSERT INTO public.base_area VALUES ('371071', '威海火炬高技术产业开发区', '3710');
INSERT INTO public.base_area VALUES ('371072', '威海经济技术开发区', '3710');
INSERT INTO public.base_area VALUES ('371073', '威海临港经济技术开发区', '3710');
INSERT INTO public.base_area VALUES ('371082', '荣成市', '3710');
INSERT INTO public.base_area VALUES ('371083', '乳山市', '3710');
INSERT INTO public.base_area VALUES ('371102', '东港区', '3711');
INSERT INTO public.base_area VALUES ('371103', '岚山区', '3711');
INSERT INTO public.base_area VALUES ('371121', '五莲县', '3711');
INSERT INTO public.base_area VALUES ('371122', '莒县', '3711');
INSERT INTO public.base_area VALUES ('371171', '日照经济技术开发区', '3711');
INSERT INTO public.base_area VALUES ('371302', '兰山区', '3713');
INSERT INTO public.base_area VALUES ('371311', '罗庄区', '3713');
INSERT INTO public.base_area VALUES ('371312', '河东区', '3713');
INSERT INTO public.base_area VALUES ('371321', '沂南县', '3713');
INSERT INTO public.base_area VALUES ('371322', '郯城县', '3713');
INSERT INTO public.base_area VALUES ('371323', '沂水县', '3713');
INSERT INTO public.base_area VALUES ('371324', '兰陵县', '3713');
INSERT INTO public.base_area VALUES ('371325', '费县', '3713');
INSERT INTO public.base_area VALUES ('371326', '平邑县', '3713');
INSERT INTO public.base_area VALUES ('371327', '莒南县', '3713');
INSERT INTO public.base_area VALUES ('371328', '蒙阴县', '3713');
INSERT INTO public.base_area VALUES ('371329', '临沭县', '3713');
INSERT INTO public.base_area VALUES ('371371', '临沂高新技术产业开发区', '3713');
INSERT INTO public.base_area VALUES ('371402', '德城区', '3714');
INSERT INTO public.base_area VALUES ('371403', '陵城区', '3714');
INSERT INTO public.base_area VALUES ('371422', '宁津县', '3714');
INSERT INTO public.base_area VALUES ('371423', '庆云县', '3714');
INSERT INTO public.base_area VALUES ('371424', '临邑县', '3714');
INSERT INTO public.base_area VALUES ('371425', '齐河县', '3714');
INSERT INTO public.base_area VALUES ('371426', '平原县', '3714');
INSERT INTO public.base_area VALUES ('371427', '夏津县', '3714');
INSERT INTO public.base_area VALUES ('371428', '武城县', '3714');
INSERT INTO public.base_area VALUES ('371471', '德州天衢新区', '3714');
INSERT INTO public.base_area VALUES ('371481', '乐陵市', '3714');
INSERT INTO public.base_area VALUES ('371482', '禹城市', '3714');
INSERT INTO public.base_area VALUES ('371502', '东昌府区', '3715');
INSERT INTO public.base_area VALUES ('371503', '茌平区', '3715');
INSERT INTO public.base_area VALUES ('371521', '阳谷县', '3715');
INSERT INTO public.base_area VALUES ('371522', '莘县', '3715');
INSERT INTO public.base_area VALUES ('371524', '东阿县', '3715');
INSERT INTO public.base_area VALUES ('371525', '冠县', '3715');
INSERT INTO public.base_area VALUES ('371526', '高唐县', '3715');
INSERT INTO public.base_area VALUES ('371581', '临清市', '3715');
INSERT INTO public.base_area VALUES ('371602', '滨城区', '3716');
INSERT INTO public.base_area VALUES ('371603', '沾化区', '3716');
INSERT INTO public.base_area VALUES ('371621', '惠民县', '3716');
INSERT INTO public.base_area VALUES ('371622', '阳信县', '3716');
INSERT INTO public.base_area VALUES ('371623', '无棣县', '3716');
INSERT INTO public.base_area VALUES ('371625', '博兴县', '3716');
INSERT INTO public.base_area VALUES ('371681', '邹平市', '3716');
INSERT INTO public.base_area VALUES ('371702', '牡丹区', '3717');
INSERT INTO public.base_area VALUES ('371703', '定陶区', '3717');
INSERT INTO public.base_area VALUES ('371721', '曹县', '3717');
INSERT INTO public.base_area VALUES ('371722', '单县', '3717');
INSERT INTO public.base_area VALUES ('371723', '成武县', '3717');
INSERT INTO public.base_area VALUES ('371724', '巨野县', '3717');
INSERT INTO public.base_area VALUES ('371725', '郓城县', '3717');
INSERT INTO public.base_area VALUES ('371726', '鄄城县', '3717');
INSERT INTO public.base_area VALUES ('371728', '东明县', '3717');
INSERT INTO public.base_area VALUES ('371771', '菏泽经济技术开发区', '3717');
INSERT INTO public.base_area VALUES ('371772', '菏泽高新技术开发区', '3717');
INSERT INTO public.base_area VALUES ('410102', '中原区', '4101');
INSERT INTO public.base_area VALUES ('410103', '二七区', '4101');
INSERT INTO public.base_area VALUES ('410104', '管城回族区', '4101');
INSERT INTO public.base_area VALUES ('410105', '金水区', '4101');
INSERT INTO public.base_area VALUES ('410106', '上街区', '4101');
INSERT INTO public.base_area VALUES ('410108', '惠济区', '4101');
INSERT INTO public.base_area VALUES ('410122', '中牟县', '4101');
INSERT INTO public.base_area VALUES ('410171', '郑州经济技术开发区', '4101');
INSERT INTO public.base_area VALUES ('410172', '郑州高新技术产业开发区', '4101');
INSERT INTO public.base_area VALUES ('410173', '郑州航空港经济综合实验区', '4101');
INSERT INTO public.base_area VALUES ('410181', '巩义市', '4101');
INSERT INTO public.base_area VALUES ('410182', '荥阳市', '4101');
INSERT INTO public.base_area VALUES ('410183', '新密市', '4101');
INSERT INTO public.base_area VALUES ('410184', '新郑市', '4101');
INSERT INTO public.base_area VALUES ('410185', '登封市', '4101');
INSERT INTO public.base_area VALUES ('410202', '龙亭区', '4102');
INSERT INTO public.base_area VALUES ('410203', '顺河回族区', '4102');
INSERT INTO public.base_area VALUES ('410204', '鼓楼区', '4102');
INSERT INTO public.base_area VALUES ('410205', '禹王台区', '4102');
INSERT INTO public.base_area VALUES ('410212', '祥符区', '4102');
INSERT INTO public.base_area VALUES ('410221', '杞县', '4102');
INSERT INTO public.base_area VALUES ('410222', '通许县', '4102');
INSERT INTO public.base_area VALUES ('410223', '尉氏县', '4102');
INSERT INTO public.base_area VALUES ('410225', '兰考县', '4102');
INSERT INTO public.base_area VALUES ('410302', '老城区', '4103');
INSERT INTO public.base_area VALUES ('410303', '西工区', '4103');
INSERT INTO public.base_area VALUES ('410304', '瀍河回族区', '4103');
INSERT INTO public.base_area VALUES ('410305', '涧西区', '4103');
INSERT INTO public.base_area VALUES ('410307', '偃师区', '4103');
INSERT INTO public.base_area VALUES ('410308', '孟津区', '4103');
INSERT INTO public.base_area VALUES ('410311', '洛龙区', '4103');
INSERT INTO public.base_area VALUES ('410323', '新安县', '4103');
INSERT INTO public.base_area VALUES ('410324', '栾川县', '4103');
INSERT INTO public.base_area VALUES ('410325', '嵩县', '4103');
INSERT INTO public.base_area VALUES ('410326', '汝阳县', '4103');
INSERT INTO public.base_area VALUES ('410327', '宜阳县', '4103');
INSERT INTO public.base_area VALUES ('410328', '洛宁县', '4103');
INSERT INTO public.base_area VALUES ('410329', '伊川县', '4103');
INSERT INTO public.base_area VALUES ('410371', '洛阳高新技术产业开发区', '4103');
INSERT INTO public.base_area VALUES ('410402', '新华区', '4104');
INSERT INTO public.base_area VALUES ('410403', '卫东区', '4104');
INSERT INTO public.base_area VALUES ('410404', '石龙区', '4104');
INSERT INTO public.base_area VALUES ('410411', '湛河区', '4104');
INSERT INTO public.base_area VALUES ('410421', '宝丰县', '4104');
INSERT INTO public.base_area VALUES ('410422', '叶县', '4104');
INSERT INTO public.base_area VALUES ('410423', '鲁山县', '4104');
INSERT INTO public.base_area VALUES ('410425', '郏县', '4104');
INSERT INTO public.base_area VALUES ('410471', '平顶山高新技术产业开发区', '4104');
INSERT INTO public.base_area VALUES ('410472', '平顶山市城乡一体化示范区', '4104');
INSERT INTO public.base_area VALUES ('410481', '舞钢市', '4104');
INSERT INTO public.base_area VALUES ('410482', '汝州市', '4104');
INSERT INTO public.base_area VALUES ('410502', '文峰区', '4105');
INSERT INTO public.base_area VALUES ('410503', '北关区', '4105');
INSERT INTO public.base_area VALUES ('410505', '殷都区', '4105');
INSERT INTO public.base_area VALUES ('410506', '龙安区', '4105');
INSERT INTO public.base_area VALUES ('410522', '安阳县', '4105');
INSERT INTO public.base_area VALUES ('410523', '汤阴县', '4105');
INSERT INTO public.base_area VALUES ('410526', '滑县', '4105');
INSERT INTO public.base_area VALUES ('410527', '内黄县', '4105');
INSERT INTO public.base_area VALUES ('410571', '安阳高新技术产业开发区', '4105');
INSERT INTO public.base_area VALUES ('410581', '林州市', '4105');
INSERT INTO public.base_area VALUES ('410602', '鹤山区', '4106');
INSERT INTO public.base_area VALUES ('410603', '山城区', '4106');
INSERT INTO public.base_area VALUES ('410611', '淇滨区', '4106');
INSERT INTO public.base_area VALUES ('410621', '浚县', '4106');
INSERT INTO public.base_area VALUES ('410622', '淇县', '4106');
INSERT INTO public.base_area VALUES ('410671', '鹤壁经济技术开发区', '4106');
INSERT INTO public.base_area VALUES ('410702', '红旗区', '4107');
INSERT INTO public.base_area VALUES ('410703', '卫滨区', '4107');
INSERT INTO public.base_area VALUES ('410704', '凤泉区', '4107');
INSERT INTO public.base_area VALUES ('410711', '牧野区', '4107');
INSERT INTO public.base_area VALUES ('410721', '新乡县', '4107');
INSERT INTO public.base_area VALUES ('410724', '获嘉县', '4107');
INSERT INTO public.base_area VALUES ('410725', '原阳县', '4107');
INSERT INTO public.base_area VALUES ('410726', '延津县', '4107');
INSERT INTO public.base_area VALUES ('410727', '封丘县', '4107');
INSERT INTO public.base_area VALUES ('410771', '新乡高新技术产业开发区', '4107');
INSERT INTO public.base_area VALUES ('410772', '新乡经济技术开发区', '4107');
INSERT INTO public.base_area VALUES ('410773', '新乡市平原城乡一体化示范区', '4107');
INSERT INTO public.base_area VALUES ('410781', '卫辉市', '4107');
INSERT INTO public.base_area VALUES ('410782', '辉县市', '4107');
INSERT INTO public.base_area VALUES ('410783', '长垣市', '4107');
INSERT INTO public.base_area VALUES ('410802', '解放区', '4108');
INSERT INTO public.base_area VALUES ('410803', '中站区', '4108');
INSERT INTO public.base_area VALUES ('410804', '马村区', '4108');
INSERT INTO public.base_area VALUES ('410811', '山阳区', '4108');
INSERT INTO public.base_area VALUES ('410821', '修武县', '4108');
INSERT INTO public.base_area VALUES ('410822', '博爱县', '4108');
INSERT INTO public.base_area VALUES ('410823', '武陟县', '4108');
INSERT INTO public.base_area VALUES ('410825', '温县', '4108');
INSERT INTO public.base_area VALUES ('410871', '焦作城乡一体化示范区', '4108');
INSERT INTO public.base_area VALUES ('410882', '沁阳市', '4108');
INSERT INTO public.base_area VALUES ('410883', '孟州市', '4108');
INSERT INTO public.base_area VALUES ('410902', '华龙区', '4109');
INSERT INTO public.base_area VALUES ('410922', '清丰县', '4109');
INSERT INTO public.base_area VALUES ('410923', '南乐县', '4109');
INSERT INTO public.base_area VALUES ('410926', '范县', '4109');
INSERT INTO public.base_area VALUES ('410927', '台前县', '4109');
INSERT INTO public.base_area VALUES ('410928', '濮阳县', '4109');
INSERT INTO public.base_area VALUES ('410971', '河南濮阳工业园区', '4109');
INSERT INTO public.base_area VALUES ('410972', '濮阳经济技术开发区', '4109');
INSERT INTO public.base_area VALUES ('411002', '魏都区', '4110');
INSERT INTO public.base_area VALUES ('411003', '建安区', '4110');
INSERT INTO public.base_area VALUES ('411024', '鄢陵县', '4110');
INSERT INTO public.base_area VALUES ('411025', '襄城县', '4110');
INSERT INTO public.base_area VALUES ('411071', '许昌经济技术开发区', '4110');
INSERT INTO public.base_area VALUES ('411081', '禹州市', '4110');
INSERT INTO public.base_area VALUES ('411082', '长葛市', '4110');
INSERT INTO public.base_area VALUES ('411102', '源汇区', '4111');
INSERT INTO public.base_area VALUES ('411103', '郾城区', '4111');
INSERT INTO public.base_area VALUES ('411104', '召陵区', '4111');
INSERT INTO public.base_area VALUES ('411121', '舞阳县', '4111');
INSERT INTO public.base_area VALUES ('411122', '临颍县', '4111');
INSERT INTO public.base_area VALUES ('411171', '漯河经济技术开发区', '4111');
INSERT INTO public.base_area VALUES ('411202', '湖滨区', '4112');
INSERT INTO public.base_area VALUES ('411203', '陕州区', '4112');
INSERT INTO public.base_area VALUES ('411221', '渑池县', '4112');
INSERT INTO public.base_area VALUES ('411224', '卢氏县', '4112');
INSERT INTO public.base_area VALUES ('411271', '河南三门峡经济开发区', '4112');
INSERT INTO public.base_area VALUES ('411281', '义马市', '4112');
INSERT INTO public.base_area VALUES ('411282', '灵宝市', '4112');
INSERT INTO public.base_area VALUES ('411302', '宛城区', '4113');
INSERT INTO public.base_area VALUES ('411303', '卧龙区', '4113');
INSERT INTO public.base_area VALUES ('411321', '南召县', '4113');
INSERT INTO public.base_area VALUES ('411322', '方城县', '4113');
INSERT INTO public.base_area VALUES ('411323', '西峡县', '4113');
INSERT INTO public.base_area VALUES ('411324', '镇平县', '4113');
INSERT INTO public.base_area VALUES ('411325', '内乡县', '4113');
INSERT INTO public.base_area VALUES ('411326', '淅川县', '4113');
INSERT INTO public.base_area VALUES ('411327', '社旗县', '4113');
INSERT INTO public.base_area VALUES ('411328', '唐河县', '4113');
INSERT INTO public.base_area VALUES ('411329', '新野县', '4113');
INSERT INTO public.base_area VALUES ('411330', '桐柏县', '4113');
INSERT INTO public.base_area VALUES ('411371', '南阳高新技术产业开发区', '4113');
INSERT INTO public.base_area VALUES ('411372', '南阳市城乡一体化示范区', '4113');
INSERT INTO public.base_area VALUES ('411381', '邓州市', '4113');
INSERT INTO public.base_area VALUES ('411402', '梁园区', '4114');
INSERT INTO public.base_area VALUES ('411403', '睢阳区', '4114');
INSERT INTO public.base_area VALUES ('411421', '民权县', '4114');
INSERT INTO public.base_area VALUES ('411422', '睢县', '4114');
INSERT INTO public.base_area VALUES ('411423', '宁陵县', '4114');
INSERT INTO public.base_area VALUES ('411424', '柘城县', '4114');
INSERT INTO public.base_area VALUES ('411425', '虞城县', '4114');
INSERT INTO public.base_area VALUES ('411426', '夏邑县', '4114');
INSERT INTO public.base_area VALUES ('411471', '豫东综合物流产业聚集区', '4114');
INSERT INTO public.base_area VALUES ('411472', '河南商丘经济开发区', '4114');
INSERT INTO public.base_area VALUES ('411481', '永城市', '4114');
INSERT INTO public.base_area VALUES ('411502', '浉河区', '4115');
INSERT INTO public.base_area VALUES ('411503', '平桥区', '4115');
INSERT INTO public.base_area VALUES ('411521', '罗山县', '4115');
INSERT INTO public.base_area VALUES ('411522', '光山县', '4115');
INSERT INTO public.base_area VALUES ('411523', '新县', '4115');
INSERT INTO public.base_area VALUES ('411524', '商城县', '4115');
INSERT INTO public.base_area VALUES ('411525', '固始县', '4115');
INSERT INTO public.base_area VALUES ('411526', '潢川县', '4115');
INSERT INTO public.base_area VALUES ('411527', '淮滨县', '4115');
INSERT INTO public.base_area VALUES ('411528', '息县', '4115');
INSERT INTO public.base_area VALUES ('411571', '信阳高新技术产业开发区', '4115');
INSERT INTO public.base_area VALUES ('411602', '川汇区', '4116');
INSERT INTO public.base_area VALUES ('411603', '淮阳区', '4116');
INSERT INTO public.base_area VALUES ('411621', '扶沟县', '4116');
INSERT INTO public.base_area VALUES ('411622', '西华县', '4116');
INSERT INTO public.base_area VALUES ('411623', '商水县', '4116');
INSERT INTO public.base_area VALUES ('411624', '沈丘县', '4116');
INSERT INTO public.base_area VALUES ('411625', '郸城县', '4116');
INSERT INTO public.base_area VALUES ('411627', '太康县', '4116');
INSERT INTO public.base_area VALUES ('411628', '鹿邑县', '4116');
INSERT INTO public.base_area VALUES ('411671', '河南周口经济开发区', '4116');
INSERT INTO public.base_area VALUES ('411681', '项城市', '4116');
INSERT INTO public.base_area VALUES ('411702', '驿城区', '4117');
INSERT INTO public.base_area VALUES ('411721', '西平县', '4117');
INSERT INTO public.base_area VALUES ('411722', '上蔡县', '4117');
INSERT INTO public.base_area VALUES ('411723', '平舆县', '4117');
INSERT INTO public.base_area VALUES ('411724', '正阳县', '4117');
INSERT INTO public.base_area VALUES ('411725', '确山县', '4117');
INSERT INTO public.base_area VALUES ('411726', '泌阳县', '4117');
INSERT INTO public.base_area VALUES ('411727', '汝南县', '4117');
INSERT INTO public.base_area VALUES ('411728', '遂平县', '4117');
INSERT INTO public.base_area VALUES ('411729', '新蔡县', '4117');
INSERT INTO public.base_area VALUES ('411771', '河南驻马店经济开发区', '4117');
INSERT INTO public.base_area VALUES ('419001', '济源市', '4190');
INSERT INTO public.base_area VALUES ('420102', '江岸区', '4201');
INSERT INTO public.base_area VALUES ('420103', '江汉区', '4201');
INSERT INTO public.base_area VALUES ('420104', '硚口区', '4201');
INSERT INTO public.base_area VALUES ('420105', '汉阳区', '4201');
INSERT INTO public.base_area VALUES ('420106', '武昌区', '4201');
INSERT INTO public.base_area VALUES ('420107', '青山区', '4201');
INSERT INTO public.base_area VALUES ('420111', '洪山区', '4201');
INSERT INTO public.base_area VALUES ('420112', '东西湖区', '4201');
INSERT INTO public.base_area VALUES ('420113', '汉南区', '4201');
INSERT INTO public.base_area VALUES ('420114', '蔡甸区', '4201');
INSERT INTO public.base_area VALUES ('420115', '江夏区', '4201');
INSERT INTO public.base_area VALUES ('420116', '黄陂区', '4201');
INSERT INTO public.base_area VALUES ('420117', '新洲区', '4201');
INSERT INTO public.base_area VALUES ('420202', '黄石港区', '4202');
INSERT INTO public.base_area VALUES ('420203', '西塞山区', '4202');
INSERT INTO public.base_area VALUES ('420204', '下陆区', '4202');
INSERT INTO public.base_area VALUES ('420205', '铁山区', '4202');
INSERT INTO public.base_area VALUES ('420222', '阳新县', '4202');
INSERT INTO public.base_area VALUES ('420281', '大冶市', '4202');
INSERT INTO public.base_area VALUES ('420302', '茅箭区', '4203');
INSERT INTO public.base_area VALUES ('420303', '张湾区', '4203');
INSERT INTO public.base_area VALUES ('420304', '郧阳区', '4203');
INSERT INTO public.base_area VALUES ('420322', '郧西县', '4203');
INSERT INTO public.base_area VALUES ('420323', '竹山县', '4203');
INSERT INTO public.base_area VALUES ('420324', '竹溪县', '4203');
INSERT INTO public.base_area VALUES ('420325', '房县', '4203');
INSERT INTO public.base_area VALUES ('420381', '丹江口市', '4203');
INSERT INTO public.base_area VALUES ('420502', '西陵区', '4205');
INSERT INTO public.base_area VALUES ('420503', '伍家岗区', '4205');
INSERT INTO public.base_area VALUES ('420504', '点军区', '4205');
INSERT INTO public.base_area VALUES ('420505', '猇亭区', '4205');
INSERT INTO public.base_area VALUES ('420506', '夷陵区', '4205');
INSERT INTO public.base_area VALUES ('420525', '远安县', '4205');
INSERT INTO public.base_area VALUES ('420526', '兴山县', '4205');
INSERT INTO public.base_area VALUES ('420527', '秭归县', '4205');
INSERT INTO public.base_area VALUES ('420528', '长阳土家族自治县', '4205');
INSERT INTO public.base_area VALUES ('420529', '五峰土家族自治县', '4205');
INSERT INTO public.base_area VALUES ('420581', '宜都市', '4205');
INSERT INTO public.base_area VALUES ('420582', '当阳市', '4205');
INSERT INTO public.base_area VALUES ('420583', '枝江市', '4205');
INSERT INTO public.base_area VALUES ('420602', '襄城区', '4206');
INSERT INTO public.base_area VALUES ('420606', '樊城区', '4206');
INSERT INTO public.base_area VALUES ('420607', '襄州区', '4206');
INSERT INTO public.base_area VALUES ('420624', '南漳县', '4206');
INSERT INTO public.base_area VALUES ('420625', '谷城县', '4206');
INSERT INTO public.base_area VALUES ('420626', '保康县', '4206');
INSERT INTO public.base_area VALUES ('420682', '老河口市', '4206');
INSERT INTO public.base_area VALUES ('420683', '枣阳市', '4206');
INSERT INTO public.base_area VALUES ('420684', '宜城市', '4206');
INSERT INTO public.base_area VALUES ('420702', '梁子湖区', '4207');
INSERT INTO public.base_area VALUES ('420703', '华容区', '4207');
INSERT INTO public.base_area VALUES ('420704', '鄂城区', '4207');
INSERT INTO public.base_area VALUES ('420802', '东宝区', '4208');
INSERT INTO public.base_area VALUES ('420804', '掇刀区', '4208');
INSERT INTO public.base_area VALUES ('420822', '沙洋县', '4208');
INSERT INTO public.base_area VALUES ('420881', '钟祥市', '4208');
INSERT INTO public.base_area VALUES ('420882', '京山市', '4208');
INSERT INTO public.base_area VALUES ('420902', '孝南区', '4209');
INSERT INTO public.base_area VALUES ('420921', '孝昌县', '4209');
INSERT INTO public.base_area VALUES ('420922', '大悟县', '4209');
INSERT INTO public.base_area VALUES ('420923', '云梦县', '4209');
INSERT INTO public.base_area VALUES ('420981', '应城市', '4209');
INSERT INTO public.base_area VALUES ('420982', '安陆市', '4209');
INSERT INTO public.base_area VALUES ('420984', '汉川市', '4209');
INSERT INTO public.base_area VALUES ('421002', '沙市区', '4210');
INSERT INTO public.base_area VALUES ('421003', '荆州区', '4210');
INSERT INTO public.base_area VALUES ('421022', '公安县', '4210');
INSERT INTO public.base_area VALUES ('421024', '江陵县', '4210');
INSERT INTO public.base_area VALUES ('421071', '荆州经济技术开发区', '4210');
INSERT INTO public.base_area VALUES ('421081', '石首市', '4210');
INSERT INTO public.base_area VALUES ('421083', '洪湖市', '4210');
INSERT INTO public.base_area VALUES ('421087', '松滋市', '4210');
INSERT INTO public.base_area VALUES ('421088', '监利市', '4210');
INSERT INTO public.base_area VALUES ('421102', '黄州区', '4211');
INSERT INTO public.base_area VALUES ('421121', '团风县', '4211');
INSERT INTO public.base_area VALUES ('421122', '红安县', '4211');
INSERT INTO public.base_area VALUES ('421123', '罗田县', '4211');
INSERT INTO public.base_area VALUES ('421124', '英山县', '4211');
INSERT INTO public.base_area VALUES ('421125', '浠水县', '4211');
INSERT INTO public.base_area VALUES ('421126', '蕲春县', '4211');
INSERT INTO public.base_area VALUES ('421127', '黄梅县', '4211');
INSERT INTO public.base_area VALUES ('421171', '龙感湖管理区', '4211');
INSERT INTO public.base_area VALUES ('421181', '麻城市', '4211');
INSERT INTO public.base_area VALUES ('421182', '武穴市', '4211');
INSERT INTO public.base_area VALUES ('421202', '咸安区', '4212');
INSERT INTO public.base_area VALUES ('421221', '嘉鱼县', '4212');
INSERT INTO public.base_area VALUES ('421222', '通城县', '4212');
INSERT INTO public.base_area VALUES ('421223', '崇阳县', '4212');
INSERT INTO public.base_area VALUES ('421224', '通山县', '4212');
INSERT INTO public.base_area VALUES ('421281', '赤壁市', '4212');
INSERT INTO public.base_area VALUES ('421303', '曾都区', '4213');
INSERT INTO public.base_area VALUES ('421321', '随县', '4213');
INSERT INTO public.base_area VALUES ('421381', '广水市', '4213');
INSERT INTO public.base_area VALUES ('422801', '恩施市', '4228');
INSERT INTO public.base_area VALUES ('422802', '利川市', '4228');
INSERT INTO public.base_area VALUES ('422822', '建始县', '4228');
INSERT INTO public.base_area VALUES ('422823', '巴东县', '4228');
INSERT INTO public.base_area VALUES ('422825', '宣恩县', '4228');
INSERT INTO public.base_area VALUES ('422826', '咸丰县', '4228');
INSERT INTO public.base_area VALUES ('422827', '来凤县', '4228');
INSERT INTO public.base_area VALUES ('422828', '鹤峰县', '4228');
INSERT INTO public.base_area VALUES ('429004', '仙桃市', '4290');
INSERT INTO public.base_area VALUES ('429005', '潜江市', '4290');
INSERT INTO public.base_area VALUES ('429006', '天门市', '4290');
INSERT INTO public.base_area VALUES ('429021', '神农架林区', '4290');
INSERT INTO public.base_area VALUES ('430102', '芙蓉区', '4301');
INSERT INTO public.base_area VALUES ('430103', '天心区', '4301');
INSERT INTO public.base_area VALUES ('430104', '岳麓区', '4301');
INSERT INTO public.base_area VALUES ('430105', '开福区', '4301');
INSERT INTO public.base_area VALUES ('430111', '雨花区', '4301');
INSERT INTO public.base_area VALUES ('430112', '望城区', '4301');
INSERT INTO public.base_area VALUES ('430121', '长沙县', '4301');
INSERT INTO public.base_area VALUES ('430181', '浏阳市', '4301');
INSERT INTO public.base_area VALUES ('430182', '宁乡市', '4301');
INSERT INTO public.base_area VALUES ('430202', '荷塘区', '4302');
INSERT INTO public.base_area VALUES ('430203', '芦淞区', '4302');
INSERT INTO public.base_area VALUES ('430204', '石峰区', '4302');
INSERT INTO public.base_area VALUES ('430211', '天元区', '4302');
INSERT INTO public.base_area VALUES ('430212', '渌口区', '4302');
INSERT INTO public.base_area VALUES ('430223', '攸县', '4302');
INSERT INTO public.base_area VALUES ('430224', '茶陵县', '4302');
INSERT INTO public.base_area VALUES ('430225', '炎陵县', '4302');
INSERT INTO public.base_area VALUES ('430281', '醴陵市', '4302');
INSERT INTO public.base_area VALUES ('430302', '雨湖区', '4303');
INSERT INTO public.base_area VALUES ('430304', '岳塘区', '4303');
INSERT INTO public.base_area VALUES ('430321', '湘潭县', '4303');
INSERT INTO public.base_area VALUES ('430371', '湖南湘潭高新技术产业园区', '4303');
INSERT INTO public.base_area VALUES ('430372', '湘潭昭山示范区', '4303');
INSERT INTO public.base_area VALUES ('430373', '湘潭九华示范区', '4303');
INSERT INTO public.base_area VALUES ('430381', '湘乡市', '4303');
INSERT INTO public.base_area VALUES ('430382', '韶山市', '4303');
INSERT INTO public.base_area VALUES ('430405', '珠晖区', '4304');
INSERT INTO public.base_area VALUES ('430406', '雁峰区', '4304');
INSERT INTO public.base_area VALUES ('430407', '石鼓区', '4304');
INSERT INTO public.base_area VALUES ('430408', '蒸湘区', '4304');
INSERT INTO public.base_area VALUES ('430412', '南岳区', '4304');
INSERT INTO public.base_area VALUES ('430421', '衡阳县', '4304');
INSERT INTO public.base_area VALUES ('430422', '衡南县', '4304');
INSERT INTO public.base_area VALUES ('430423', '衡山县', '4304');
INSERT INTO public.base_area VALUES ('430424', '衡东县', '4304');
INSERT INTO public.base_area VALUES ('430426', '祁东县', '4304');
INSERT INTO public.base_area VALUES ('430471', '衡阳综合保税区', '4304');
INSERT INTO public.base_area VALUES ('430472', '湖南衡阳高新技术产业园区', '4304');
INSERT INTO public.base_area VALUES ('430473', '湖南衡阳松木经济开发区', '4304');
INSERT INTO public.base_area VALUES ('430481', '耒阳市', '4304');
INSERT INTO public.base_area VALUES ('430482', '常宁市', '4304');
INSERT INTO public.base_area VALUES ('430502', '双清区', '4305');
INSERT INTO public.base_area VALUES ('430503', '大祥区', '4305');
INSERT INTO public.base_area VALUES ('430511', '北塔区', '4305');
INSERT INTO public.base_area VALUES ('430522', '新邵县', '4305');
INSERT INTO public.base_area VALUES ('430523', '邵阳县', '4305');
INSERT INTO public.base_area VALUES ('430524', '隆回县', '4305');
INSERT INTO public.base_area VALUES ('430525', '洞口县', '4305');
INSERT INTO public.base_area VALUES ('430527', '绥宁县', '4305');
INSERT INTO public.base_area VALUES ('430528', '新宁县', '4305');
INSERT INTO public.base_area VALUES ('430529', '城步苗族自治县', '4305');
INSERT INTO public.base_area VALUES ('430581', '武冈市', '4305');
INSERT INTO public.base_area VALUES ('430582', '邵东市', '4305');
INSERT INTO public.base_area VALUES ('430602', '岳阳楼区', '4306');
INSERT INTO public.base_area VALUES ('430603', '云溪区', '4306');
INSERT INTO public.base_area VALUES ('430611', '君山区', '4306');
INSERT INTO public.base_area VALUES ('430621', '岳阳县', '4306');
INSERT INTO public.base_area VALUES ('430623', '华容县', '4306');
INSERT INTO public.base_area VALUES ('430624', '湘阴县', '4306');
INSERT INTO public.base_area VALUES ('430626', '平江县', '4306');
INSERT INTO public.base_area VALUES ('430671', '岳阳市屈原管理区', '4306');
INSERT INTO public.base_area VALUES ('430681', '汨罗市', '4306');
INSERT INTO public.base_area VALUES ('430682', '临湘市', '4306');
INSERT INTO public.base_area VALUES ('430702', '武陵区', '4307');
INSERT INTO public.base_area VALUES ('430703', '鼎城区', '4307');
INSERT INTO public.base_area VALUES ('430721', '安乡县', '4307');
INSERT INTO public.base_area VALUES ('430722', '汉寿县', '4307');
INSERT INTO public.base_area VALUES ('430723', '澧县', '4307');
INSERT INTO public.base_area VALUES ('430724', '临澧县', '4307');
INSERT INTO public.base_area VALUES ('430725', '桃源县', '4307');
INSERT INTO public.base_area VALUES ('430726', '石门县', '4307');
INSERT INTO public.base_area VALUES ('430771', '常德市西洞庭管理区', '4307');
INSERT INTO public.base_area VALUES ('430781', '津市市', '4307');
INSERT INTO public.base_area VALUES ('430802', '永定区', '4308');
INSERT INTO public.base_area VALUES ('430811', '武陵源区', '4308');
INSERT INTO public.base_area VALUES ('430821', '慈利县', '4308');
INSERT INTO public.base_area VALUES ('430822', '桑植县', '4308');
INSERT INTO public.base_area VALUES ('430902', '资阳区', '4309');
INSERT INTO public.base_area VALUES ('430903', '赫山区', '4309');
INSERT INTO public.base_area VALUES ('430921', '南县', '4309');
INSERT INTO public.base_area VALUES ('430922', '桃江县', '4309');
INSERT INTO public.base_area VALUES ('430923', '安化县', '4309');
INSERT INTO public.base_area VALUES ('430971', '益阳市大通湖管理区', '4309');
INSERT INTO public.base_area VALUES ('430972', '湖南益阳高新技术产业园区', '4309');
INSERT INTO public.base_area VALUES ('430981', '沅江市', '4309');
INSERT INTO public.base_area VALUES ('431002', '北湖区', '4310');
INSERT INTO public.base_area VALUES ('431003', '苏仙区', '4310');
INSERT INTO public.base_area VALUES ('431021', '桂阳县', '4310');
INSERT INTO public.base_area VALUES ('431022', '宜章县', '4310');
INSERT INTO public.base_area VALUES ('431023', '永兴县', '4310');
INSERT INTO public.base_area VALUES ('431024', '嘉禾县', '4310');
INSERT INTO public.base_area VALUES ('431025', '临武县', '4310');
INSERT INTO public.base_area VALUES ('431026', '汝城县', '4310');
INSERT INTO public.base_area VALUES ('431027', '桂东县', '4310');
INSERT INTO public.base_area VALUES ('431028', '安仁县', '4310');
INSERT INTO public.base_area VALUES ('431081', '资兴市', '4310');
INSERT INTO public.base_area VALUES ('431102', '零陵区', '4311');
INSERT INTO public.base_area VALUES ('431103', '冷水滩区', '4311');
INSERT INTO public.base_area VALUES ('431122', '东安县', '4311');
INSERT INTO public.base_area VALUES ('431123', '双牌县', '4311');
INSERT INTO public.base_area VALUES ('431124', '道县', '4311');
INSERT INTO public.base_area VALUES ('431125', '江永县', '4311');
INSERT INTO public.base_area VALUES ('431126', '宁远县', '4311');
INSERT INTO public.base_area VALUES ('431127', '蓝山县', '4311');
INSERT INTO public.base_area VALUES ('431128', '新田县', '4311');
INSERT INTO public.base_area VALUES ('431129', '江华瑶族自治县', '4311');
INSERT INTO public.base_area VALUES ('431171', '永州经济技术开发区', '4311');
INSERT INTO public.base_area VALUES ('431173', '永州市回龙圩管理区', '4311');
INSERT INTO public.base_area VALUES ('431181', '祁阳市', '4311');
INSERT INTO public.base_area VALUES ('431202', '鹤城区', '4312');
INSERT INTO public.base_area VALUES ('431221', '中方县', '4312');
INSERT INTO public.base_area VALUES ('431222', '沅陵县', '4312');
INSERT INTO public.base_area VALUES ('431223', '辰溪县', '4312');
INSERT INTO public.base_area VALUES ('431224', '溆浦县', '4312');
INSERT INTO public.base_area VALUES ('431225', '会同县', '4312');
INSERT INTO public.base_area VALUES ('431226', '麻阳苗族自治县', '4312');
INSERT INTO public.base_area VALUES ('431227', '新晃侗族自治县', '4312');
INSERT INTO public.base_area VALUES ('431228', '芷江侗族自治县', '4312');
INSERT INTO public.base_area VALUES ('431229', '靖州苗族侗族自治县', '4312');
INSERT INTO public.base_area VALUES ('431230', '通道侗族自治县', '4312');
INSERT INTO public.base_area VALUES ('431271', '怀化市洪江管理区', '4312');
INSERT INTO public.base_area VALUES ('431281', '洪江市', '4312');
INSERT INTO public.base_area VALUES ('431302', '娄星区', '4313');
INSERT INTO public.base_area VALUES ('431321', '双峰县', '4313');
INSERT INTO public.base_area VALUES ('431322', '新化县', '4313');
INSERT INTO public.base_area VALUES ('431381', '冷水江市', '4313');
INSERT INTO public.base_area VALUES ('431382', '涟源市', '4313');
INSERT INTO public.base_area VALUES ('433101', '吉首市', '4331');
INSERT INTO public.base_area VALUES ('433122', '泸溪县', '4331');
INSERT INTO public.base_area VALUES ('433123', '凤凰县', '4331');
INSERT INTO public.base_area VALUES ('433124', '花垣县', '4331');
INSERT INTO public.base_area VALUES ('433125', '保靖县', '4331');
INSERT INTO public.base_area VALUES ('433126', '古丈县', '4331');
INSERT INTO public.base_area VALUES ('433127', '永顺县', '4331');
INSERT INTO public.base_area VALUES ('433130', '龙山县', '4331');
INSERT INTO public.base_area VALUES ('440103', '荔湾区', '4401');
INSERT INTO public.base_area VALUES ('440104', '越秀区', '4401');
INSERT INTO public.base_area VALUES ('440105', '海珠区', '4401');
INSERT INTO public.base_area VALUES ('440106', '天河区', '4401');
INSERT INTO public.base_area VALUES ('440111', '白云区', '4401');
INSERT INTO public.base_area VALUES ('440112', '黄埔区', '4401');
INSERT INTO public.base_area VALUES ('440113', '番禺区', '4401');
INSERT INTO public.base_area VALUES ('440114', '花都区', '4401');
INSERT INTO public.base_area VALUES ('440115', '南沙区', '4401');
INSERT INTO public.base_area VALUES ('440117', '从化区', '4401');
INSERT INTO public.base_area VALUES ('440118', '增城区', '4401');
INSERT INTO public.base_area VALUES ('440203', '武江区', '4402');
INSERT INTO public.base_area VALUES ('440204', '浈江区', '4402');
INSERT INTO public.base_area VALUES ('440205', '曲江区', '4402');
INSERT INTO public.base_area VALUES ('440222', '始兴县', '4402');
INSERT INTO public.base_area VALUES ('440224', '仁化县', '4402');
INSERT INTO public.base_area VALUES ('440229', '翁源县', '4402');
INSERT INTO public.base_area VALUES ('440232', '乳源瑶族自治县', '4402');
INSERT INTO public.base_area VALUES ('440233', '新丰县', '4402');
INSERT INTO public.base_area VALUES ('440281', '乐昌市', '4402');
INSERT INTO public.base_area VALUES ('440282', '南雄市', '4402');
INSERT INTO public.base_area VALUES ('440303', '罗湖区', '4403');
INSERT INTO public.base_area VALUES ('440304', '福田区', '4403');
INSERT INTO public.base_area VALUES ('440305', '南山区', '4403');
INSERT INTO public.base_area VALUES ('440306', '宝安区', '4403');
INSERT INTO public.base_area VALUES ('440307', '龙岗区', '4403');
INSERT INTO public.base_area VALUES ('440308', '盐田区', '4403');
INSERT INTO public.base_area VALUES ('440309', '龙华区', '4403');
INSERT INTO public.base_area VALUES ('440310', '坪山区', '4403');
INSERT INTO public.base_area VALUES ('440311', '光明区', '4403');
INSERT INTO public.base_area VALUES ('440402', '香洲区', '4404');
INSERT INTO public.base_area VALUES ('440403', '斗门区', '4404');
INSERT INTO public.base_area VALUES ('440404', '金湾区', '4404');
INSERT INTO public.base_area VALUES ('440507', '龙湖区', '4405');
INSERT INTO public.base_area VALUES ('440511', '金平区', '4405');
INSERT INTO public.base_area VALUES ('440512', '濠江区', '4405');
INSERT INTO public.base_area VALUES ('440513', '潮阳区', '4405');
INSERT INTO public.base_area VALUES ('440514', '潮南区', '4405');
INSERT INTO public.base_area VALUES ('440515', '澄海区', '4405');
INSERT INTO public.base_area VALUES ('440523', '南澳县', '4405');
INSERT INTO public.base_area VALUES ('440604', '禅城区', '4406');
INSERT INTO public.base_area VALUES ('440605', '南海区', '4406');
INSERT INTO public.base_area VALUES ('440606', '顺德区', '4406');
INSERT INTO public.base_area VALUES ('440607', '三水区', '4406');
INSERT INTO public.base_area VALUES ('440608', '高明区', '4406');
INSERT INTO public.base_area VALUES ('440703', '蓬江区', '4407');
INSERT INTO public.base_area VALUES ('440704', '江海区', '4407');
INSERT INTO public.base_area VALUES ('440705', '新会区', '4407');
INSERT INTO public.base_area VALUES ('440781', '台山市', '4407');
INSERT INTO public.base_area VALUES ('440783', '开平市', '4407');
INSERT INTO public.base_area VALUES ('440784', '鹤山市', '4407');
INSERT INTO public.base_area VALUES ('440785', '恩平市', '4407');
INSERT INTO public.base_area VALUES ('440802', '赤坎区', '4408');
INSERT INTO public.base_area VALUES ('440803', '霞山区', '4408');
INSERT INTO public.base_area VALUES ('440804', '坡头区', '4408');
INSERT INTO public.base_area VALUES ('440811', '麻章区', '4408');
INSERT INTO public.base_area VALUES ('440823', '遂溪县', '4408');
INSERT INTO public.base_area VALUES ('440825', '徐闻县', '4408');
INSERT INTO public.base_area VALUES ('440881', '廉江市', '4408');
INSERT INTO public.base_area VALUES ('440882', '雷州市', '4408');
INSERT INTO public.base_area VALUES ('440883', '吴川市', '4408');
INSERT INTO public.base_area VALUES ('440902', '茂南区', '4409');
INSERT INTO public.base_area VALUES ('440904', '电白区', '4409');
INSERT INTO public.base_area VALUES ('440981', '高州市', '4409');
INSERT INTO public.base_area VALUES ('440982', '化州市', '4409');
INSERT INTO public.base_area VALUES ('440983', '信宜市', '4409');
INSERT INTO public.base_area VALUES ('441202', '端州区', '4412');
INSERT INTO public.base_area VALUES ('441203', '鼎湖区', '4412');
INSERT INTO public.base_area VALUES ('441204', '高要区', '4412');
INSERT INTO public.base_area VALUES ('441223', '广宁县', '4412');
INSERT INTO public.base_area VALUES ('441224', '怀集县', '4412');
INSERT INTO public.base_area VALUES ('441225', '封开县', '4412');
INSERT INTO public.base_area VALUES ('441226', '德庆县', '4412');
INSERT INTO public.base_area VALUES ('441284', '四会市', '4412');
INSERT INTO public.base_area VALUES ('441302', '惠城区', '4413');
INSERT INTO public.base_area VALUES ('441303', '惠阳区', '4413');
INSERT INTO public.base_area VALUES ('441322', '博罗县', '4413');
INSERT INTO public.base_area VALUES ('441323', '惠东县', '4413');
INSERT INTO public.base_area VALUES ('441324', '龙门县', '4413');
INSERT INTO public.base_area VALUES ('441402', '梅江区', '4414');
INSERT INTO public.base_area VALUES ('441403', '梅县区', '4414');
INSERT INTO public.base_area VALUES ('441422', '大埔县', '4414');
INSERT INTO public.base_area VALUES ('441423', '丰顺县', '4414');
INSERT INTO public.base_area VALUES ('441424', '五华县', '4414');
INSERT INTO public.base_area VALUES ('441426', '平远县', '4414');
INSERT INTO public.base_area VALUES ('441427', '蕉岭县', '4414');
INSERT INTO public.base_area VALUES ('441481', '兴宁市', '4414');
INSERT INTO public.base_area VALUES ('441502', '城区', '4415');
INSERT INTO public.base_area VALUES ('441521', '海丰县', '4415');
INSERT INTO public.base_area VALUES ('441523', '陆河县', '4415');
INSERT INTO public.base_area VALUES ('441581', '陆丰市', '4415');
INSERT INTO public.base_area VALUES ('441602', '源城区', '4416');
INSERT INTO public.base_area VALUES ('441621', '紫金县', '4416');
INSERT INTO public.base_area VALUES ('441622', '龙川县', '4416');
INSERT INTO public.base_area VALUES ('441623', '连平县', '4416');
INSERT INTO public.base_area VALUES ('441624', '和平县', '4416');
INSERT INTO public.base_area VALUES ('441625', '东源县', '4416');
INSERT INTO public.base_area VALUES ('441702', '江城区', '4417');
INSERT INTO public.base_area VALUES ('441704', '阳东区', '4417');
INSERT INTO public.base_area VALUES ('441721', '阳西县', '4417');
INSERT INTO public.base_area VALUES ('441781', '阳春市', '4417');
INSERT INTO public.base_area VALUES ('441802', '清城区', '4418');
INSERT INTO public.base_area VALUES ('441803', '清新区', '4418');
INSERT INTO public.base_area VALUES ('441821', '佛冈县', '4418');
INSERT INTO public.base_area VALUES ('441823', '阳山县', '4418');
INSERT INTO public.base_area VALUES ('441825', '连山壮族瑶族自治县', '4418');
INSERT INTO public.base_area VALUES ('441826', '连南瑶族自治县', '4418');
INSERT INTO public.base_area VALUES ('441881', '英德市', '4418');
INSERT INTO public.base_area VALUES ('441882', '连州市', '4418');
INSERT INTO public.base_area VALUES ('441900', '东莞市', '4419');
INSERT INTO public.base_area VALUES ('442000', '中山市', '4420');
INSERT INTO public.base_area VALUES ('445102', '湘桥区', '4451');
INSERT INTO public.base_area VALUES ('445103', '潮安区', '4451');
INSERT INTO public.base_area VALUES ('445122', '饶平县', '4451');
INSERT INTO public.base_area VALUES ('445202', '榕城区', '4452');
INSERT INTO public.base_area VALUES ('445203', '揭东区', '4452');
INSERT INTO public.base_area VALUES ('445222', '揭西县', '4452');
INSERT INTO public.base_area VALUES ('445224', '惠来县', '4452');
INSERT INTO public.base_area VALUES ('445281', '普宁市', '4452');
INSERT INTO public.base_area VALUES ('445302', '云城区', '4453');
INSERT INTO public.base_area VALUES ('445303', '云安区', '4453');
INSERT INTO public.base_area VALUES ('445321', '新兴县', '4453');
INSERT INTO public.base_area VALUES ('445322', '郁南县', '4453');
INSERT INTO public.base_area VALUES ('445381', '罗定市', '4453');
INSERT INTO public.base_area VALUES ('450102', '兴宁区', '4501');
INSERT INTO public.base_area VALUES ('450103', '青秀区', '4501');
INSERT INTO public.base_area VALUES ('450105', '江南区', '4501');
INSERT INTO public.base_area VALUES ('450107', '西乡塘区', '4501');
INSERT INTO public.base_area VALUES ('450108', '良庆区', '4501');
INSERT INTO public.base_area VALUES ('450109', '邕宁区', '4501');
INSERT INTO public.base_area VALUES ('450110', '武鸣区', '4501');
INSERT INTO public.base_area VALUES ('450123', '隆安县', '4501');
INSERT INTO public.base_area VALUES ('450124', '马山县', '4501');
INSERT INTO public.base_area VALUES ('450125', '上林县', '4501');
INSERT INTO public.base_area VALUES ('450126', '宾阳县', '4501');
INSERT INTO public.base_area VALUES ('450181', '横州市', '4501');
INSERT INTO public.base_area VALUES ('450202', '城中区', '4502');
INSERT INTO public.base_area VALUES ('450203', '鱼峰区', '4502');
INSERT INTO public.base_area VALUES ('450204', '柳南区', '4502');
INSERT INTO public.base_area VALUES ('450205', '柳北区', '4502');
INSERT INTO public.base_area VALUES ('450206', '柳江区', '4502');
INSERT INTO public.base_area VALUES ('450222', '柳城县', '4502');
INSERT INTO public.base_area VALUES ('450223', '鹿寨县', '4502');
INSERT INTO public.base_area VALUES ('450224', '融安县', '4502');
INSERT INTO public.base_area VALUES ('450225', '融水苗族自治县', '4502');
INSERT INTO public.base_area VALUES ('450226', '三江侗族自治县', '4502');
INSERT INTO public.base_area VALUES ('450302', '秀峰区', '4503');
INSERT INTO public.base_area VALUES ('450303', '叠彩区', '4503');
INSERT INTO public.base_area VALUES ('450304', '象山区', '4503');
INSERT INTO public.base_area VALUES ('450305', '七星区', '4503');
INSERT INTO public.base_area VALUES ('450311', '雁山区', '4503');
INSERT INTO public.base_area VALUES ('450312', '临桂区', '4503');
INSERT INTO public.base_area VALUES ('450321', '阳朔县', '4503');
INSERT INTO public.base_area VALUES ('450323', '灵川县', '4503');
INSERT INTO public.base_area VALUES ('450324', '全州县', '4503');
INSERT INTO public.base_area VALUES ('450325', '兴安县', '4503');
INSERT INTO public.base_area VALUES ('450326', '永福县', '4503');
INSERT INTO public.base_area VALUES ('450327', '灌阳县', '4503');
INSERT INTO public.base_area VALUES ('450328', '龙胜各族自治县', '4503');
INSERT INTO public.base_area VALUES ('450329', '资源县', '4503');
INSERT INTO public.base_area VALUES ('450330', '平乐县', '4503');
INSERT INTO public.base_area VALUES ('450332', '恭城瑶族自治县', '4503');
INSERT INTO public.base_area VALUES ('450381', '荔浦市', '4503');
INSERT INTO public.base_area VALUES ('450403', '万秀区', '4504');
INSERT INTO public.base_area VALUES ('450405', '长洲区', '4504');
INSERT INTO public.base_area VALUES ('450406', '龙圩区', '4504');
INSERT INTO public.base_area VALUES ('450421', '苍梧县', '4504');
INSERT INTO public.base_area VALUES ('450422', '藤县', '4504');
INSERT INTO public.base_area VALUES ('450423', '蒙山县', '4504');
INSERT INTO public.base_area VALUES ('450481', '岑溪市', '4504');
INSERT INTO public.base_area VALUES ('450502', '海城区', '4505');
INSERT INTO public.base_area VALUES ('450503', '银海区', '4505');
INSERT INTO public.base_area VALUES ('450512', '铁山港区', '4505');
INSERT INTO public.base_area VALUES ('450521', '合浦县', '4505');
INSERT INTO public.base_area VALUES ('450602', '港口区', '4506');
INSERT INTO public.base_area VALUES ('450603', '防城区', '4506');
INSERT INTO public.base_area VALUES ('450621', '上思县', '4506');
INSERT INTO public.base_area VALUES ('450681', '东兴市', '4506');
INSERT INTO public.base_area VALUES ('450702', '钦南区', '4507');
INSERT INTO public.base_area VALUES ('450703', '钦北区', '4507');
INSERT INTO public.base_area VALUES ('450721', '灵山县', '4507');
INSERT INTO public.base_area VALUES ('450722', '浦北县', '4507');
INSERT INTO public.base_area VALUES ('450802', '港北区', '4508');
INSERT INTO public.base_area VALUES ('450803', '港南区', '4508');
INSERT INTO public.base_area VALUES ('450804', '覃塘区', '4508');
INSERT INTO public.base_area VALUES ('450821', '平南县', '4508');
INSERT INTO public.base_area VALUES ('450881', '桂平市', '4508');
INSERT INTO public.base_area VALUES ('450902', '玉州区', '4509');
INSERT INTO public.base_area VALUES ('450903', '福绵区', '4509');
INSERT INTO public.base_area VALUES ('450921', '容县', '4509');
INSERT INTO public.base_area VALUES ('450922', '陆川县', '4509');
INSERT INTO public.base_area VALUES ('450923', '博白县', '4509');
INSERT INTO public.base_area VALUES ('450924', '兴业县', '4509');
INSERT INTO public.base_area VALUES ('450981', '北流市', '4509');
INSERT INTO public.base_area VALUES ('451002', '右江区', '4510');
INSERT INTO public.base_area VALUES ('451003', '田阳区', '4510');
INSERT INTO public.base_area VALUES ('451022', '田东县', '4510');
INSERT INTO public.base_area VALUES ('451024', '德保县', '4510');
INSERT INTO public.base_area VALUES ('451026', '那坡县', '4510');
INSERT INTO public.base_area VALUES ('451027', '凌云县', '4510');
INSERT INTO public.base_area VALUES ('451028', '乐业县', '4510');
INSERT INTO public.base_area VALUES ('451029', '田林县', '4510');
INSERT INTO public.base_area VALUES ('451030', '西林县', '4510');
INSERT INTO public.base_area VALUES ('451031', '隆林各族自治县', '4510');
INSERT INTO public.base_area VALUES ('451081', '靖西市', '4510');
INSERT INTO public.base_area VALUES ('451082', '平果市', '4510');
INSERT INTO public.base_area VALUES ('451102', '八步区', '4511');
INSERT INTO public.base_area VALUES ('451103', '平桂区', '4511');
INSERT INTO public.base_area VALUES ('451121', '昭平县', '4511');
INSERT INTO public.base_area VALUES ('451122', '钟山县', '4511');
INSERT INTO public.base_area VALUES ('451123', '富川瑶族自治县', '4511');
INSERT INTO public.base_area VALUES ('451202', '金城江区', '4512');
INSERT INTO public.base_area VALUES ('451203', '宜州区', '4512');
INSERT INTO public.base_area VALUES ('451221', '南丹县', '4512');
INSERT INTO public.base_area VALUES ('451222', '天峨县', '4512');
INSERT INTO public.base_area VALUES ('451223', '凤山县', '4512');
INSERT INTO public.base_area VALUES ('451224', '东兰县', '4512');
INSERT INTO public.base_area VALUES ('451225', '罗城仫佬族自治县', '4512');
INSERT INTO public.base_area VALUES ('451226', '环江毛南族自治县', '4512');
INSERT INTO public.base_area VALUES ('451227', '巴马瑶族自治县', '4512');
INSERT INTO public.base_area VALUES ('451228', '都安瑶族自治县', '4512');
INSERT INTO public.base_area VALUES ('451229', '大化瑶族自治县', '4512');
INSERT INTO public.base_area VALUES ('451302', '兴宾区', '4513');
INSERT INTO public.base_area VALUES ('451321', '忻城县', '4513');
INSERT INTO public.base_area VALUES ('451322', '象州县', '4513');
INSERT INTO public.base_area VALUES ('451323', '武宣县', '4513');
INSERT INTO public.base_area VALUES ('451324', '金秀瑶族自治县', '4513');
INSERT INTO public.base_area VALUES ('451381', '合山市', '4513');
INSERT INTO public.base_area VALUES ('451402', '江州区', '4514');
INSERT INTO public.base_area VALUES ('451421', '扶绥县', '4514');
INSERT INTO public.base_area VALUES ('451422', '宁明县', '4514');
INSERT INTO public.base_area VALUES ('451423', '龙州县', '4514');
INSERT INTO public.base_area VALUES ('451424', '大新县', '4514');
INSERT INTO public.base_area VALUES ('451425', '天等县', '4514');
INSERT INTO public.base_area VALUES ('451481', '凭祥市', '4514');
INSERT INTO public.base_area VALUES ('460105', '秀英区', '4601');
INSERT INTO public.base_area VALUES ('460106', '龙华区', '4601');
INSERT INTO public.base_area VALUES ('460107', '琼山区', '4601');
INSERT INTO public.base_area VALUES ('460108', '美兰区', '4601');
INSERT INTO public.base_area VALUES ('460202', '海棠区', '4602');
INSERT INTO public.base_area VALUES ('460203', '吉阳区', '4602');
INSERT INTO public.base_area VALUES ('460204', '天涯区', '4602');
INSERT INTO public.base_area VALUES ('460205', '崖州区', '4602');
INSERT INTO public.base_area VALUES ('460321', '西沙群岛', '4603');
INSERT INTO public.base_area VALUES ('460322', '南沙群岛', '4603');
INSERT INTO public.base_area VALUES ('460323', '中沙群岛的岛礁及其海域', '4603');
INSERT INTO public.base_area VALUES ('460400', '儋州市', '4604');
INSERT INTO public.base_area VALUES ('469001', '五指山市', '4690');
INSERT INTO public.base_area VALUES ('469002', '琼海市', '4690');
INSERT INTO public.base_area VALUES ('469005', '文昌市', '4690');
INSERT INTO public.base_area VALUES ('469006', '万宁市', '4690');
INSERT INTO public.base_area VALUES ('469007', '东方市', '4690');
INSERT INTO public.base_area VALUES ('469021', '定安县', '4690');
INSERT INTO public.base_area VALUES ('469022', '屯昌县', '4690');
INSERT INTO public.base_area VALUES ('469023', '澄迈县', '4690');
INSERT INTO public.base_area VALUES ('469024', '临高县', '4690');
INSERT INTO public.base_area VALUES ('469025', '白沙黎族自治县', '4690');
INSERT INTO public.base_area VALUES ('469026', '昌江黎族自治县', '4690');
INSERT INTO public.base_area VALUES ('469027', '乐东黎族自治县', '4690');
INSERT INTO public.base_area VALUES ('469028', '陵水黎族自治县', '4690');
INSERT INTO public.base_area VALUES ('469029', '保亭黎族苗族自治县', '4690');
INSERT INTO public.base_area VALUES ('469030', '琼中黎族苗族自治县', '4690');
INSERT INTO public.base_area VALUES ('500101', '万州区', '5001');
INSERT INTO public.base_area VALUES ('500102', '涪陵区', '5001');
INSERT INTO public.base_area VALUES ('500103', '渝中区', '5001');
INSERT INTO public.base_area VALUES ('500104', '大渡口区', '5001');
INSERT INTO public.base_area VALUES ('500105', '江北区', '5001');
INSERT INTO public.base_area VALUES ('500106', '沙坪坝区', '5001');
INSERT INTO public.base_area VALUES ('500107', '九龙坡区', '5001');
INSERT INTO public.base_area VALUES ('500108', '南岸区', '5001');
INSERT INTO public.base_area VALUES ('500109', '北碚区', '5001');
INSERT INTO public.base_area VALUES ('500110', '綦江区', '5001');
INSERT INTO public.base_area VALUES ('500111', '大足区', '5001');
INSERT INTO public.base_area VALUES ('500112', '渝北区', '5001');
INSERT INTO public.base_area VALUES ('500113', '巴南区', '5001');
INSERT INTO public.base_area VALUES ('500114', '黔江区', '5001');
INSERT INTO public.base_area VALUES ('500115', '长寿区', '5001');
INSERT INTO public.base_area VALUES ('500116', '江津区', '5001');
INSERT INTO public.base_area VALUES ('500117', '合川区', '5001');
INSERT INTO public.base_area VALUES ('500118', '永川区', '5001');
INSERT INTO public.base_area VALUES ('500119', '南川区', '5001');
INSERT INTO public.base_area VALUES ('500120', '璧山区', '5001');
INSERT INTO public.base_area VALUES ('500151', '铜梁区', '5001');
INSERT INTO public.base_area VALUES ('500152', '潼南区', '5001');
INSERT INTO public.base_area VALUES ('500153', '荣昌区', '5001');
INSERT INTO public.base_area VALUES ('500154', '开州区', '5001');
INSERT INTO public.base_area VALUES ('500155', '梁平区', '5001');
INSERT INTO public.base_area VALUES ('500156', '武隆区', '5001');
INSERT INTO public.base_area VALUES ('500229', '城口县', '5002');
INSERT INTO public.base_area VALUES ('500230', '丰都县', '5002');
INSERT INTO public.base_area VALUES ('500231', '垫江县', '5002');
INSERT INTO public.base_area VALUES ('500233', '忠县', '5002');
INSERT INTO public.base_area VALUES ('500235', '云阳县', '5002');
INSERT INTO public.base_area VALUES ('500236', '奉节县', '5002');
INSERT INTO public.base_area VALUES ('500237', '巫山县', '5002');
INSERT INTO public.base_area VALUES ('500238', '巫溪县', '5002');
INSERT INTO public.base_area VALUES ('500240', '石柱土家族自治县', '5002');
INSERT INTO public.base_area VALUES ('500241', '秀山土家族苗族自治县', '5002');
INSERT INTO public.base_area VALUES ('500242', '酉阳土家族苗族自治县', '5002');
INSERT INTO public.base_area VALUES ('500243', '彭水苗族土家族自治县', '5002');
INSERT INTO public.base_area VALUES ('510104', '锦江区', '5101');
INSERT INTO public.base_area VALUES ('510105', '青羊区', '5101');
INSERT INTO public.base_area VALUES ('510106', '金牛区', '5101');
INSERT INTO public.base_area VALUES ('510107', '武侯区', '5101');
INSERT INTO public.base_area VALUES ('510108', '成华区', '5101');
INSERT INTO public.base_area VALUES ('510112', '龙泉驿区', '5101');
INSERT INTO public.base_area VALUES ('510113', '青白江区', '5101');
INSERT INTO public.base_area VALUES ('510114', '新都区', '5101');
INSERT INTO public.base_area VALUES ('510115', '温江区', '5101');
INSERT INTO public.base_area VALUES ('510116', '双流区', '5101');
INSERT INTO public.base_area VALUES ('510117', '郫都区', '5101');
INSERT INTO public.base_area VALUES ('510118', '新津区', '5101');
INSERT INTO public.base_area VALUES ('510121', '金堂县', '5101');
INSERT INTO public.base_area VALUES ('510129', '大邑县', '5101');
INSERT INTO public.base_area VALUES ('510131', '蒲江县', '5101');
INSERT INTO public.base_area VALUES ('510181', '都江堰市', '5101');
INSERT INTO public.base_area VALUES ('510182', '彭州市', '5101');
INSERT INTO public.base_area VALUES ('510183', '邛崃市', '5101');
INSERT INTO public.base_area VALUES ('510184', '崇州市', '5101');
INSERT INTO public.base_area VALUES ('510185', '简阳市', '5101');
INSERT INTO public.base_area VALUES ('510302', '自流井区', '5103');
INSERT INTO public.base_area VALUES ('510303', '贡井区', '5103');
INSERT INTO public.base_area VALUES ('510304', '大安区', '5103');
INSERT INTO public.base_area VALUES ('510311', '沿滩区', '5103');
INSERT INTO public.base_area VALUES ('510321', '荣县', '5103');
INSERT INTO public.base_area VALUES ('510322', '富顺县', '5103');
INSERT INTO public.base_area VALUES ('510402', '东区', '5104');
INSERT INTO public.base_area VALUES ('510403', '西区', '5104');
INSERT INTO public.base_area VALUES ('510411', '仁和区', '5104');
INSERT INTO public.base_area VALUES ('510421', '米易县', '5104');
INSERT INTO public.base_area VALUES ('510422', '盐边县', '5104');
INSERT INTO public.base_area VALUES ('510502', '江阳区', '5105');
INSERT INTO public.base_area VALUES ('510503', '纳溪区', '5105');
INSERT INTO public.base_area VALUES ('510504', '龙马潭区', '5105');
INSERT INTO public.base_area VALUES ('510521', '泸县', '5105');
INSERT INTO public.base_area VALUES ('510522', '合江县', '5105');
INSERT INTO public.base_area VALUES ('510524', '叙永县', '5105');
INSERT INTO public.base_area VALUES ('510525', '古蔺县', '5105');
INSERT INTO public.base_area VALUES ('510603', '旌阳区', '5106');
INSERT INTO public.base_area VALUES ('510604', '罗江区', '5106');
INSERT INTO public.base_area VALUES ('510623', '中江县', '5106');
INSERT INTO public.base_area VALUES ('510681', '广汉市', '5106');
INSERT INTO public.base_area VALUES ('510682', '什邡市', '5106');
INSERT INTO public.base_area VALUES ('510683', '绵竹市', '5106');
INSERT INTO public.base_area VALUES ('510703', '涪城区', '5107');
INSERT INTO public.base_area VALUES ('510704', '游仙区', '5107');
INSERT INTO public.base_area VALUES ('510705', '安州区', '5107');
INSERT INTO public.base_area VALUES ('510722', '三台县', '5107');
INSERT INTO public.base_area VALUES ('510723', '盐亭县', '5107');
INSERT INTO public.base_area VALUES ('510725', '梓潼县', '5107');
INSERT INTO public.base_area VALUES ('510726', '北川羌族自治县', '5107');
INSERT INTO public.base_area VALUES ('510727', '平武县', '5107');
INSERT INTO public.base_area VALUES ('510781', '江油市', '5107');
INSERT INTO public.base_area VALUES ('510802', '利州区', '5108');
INSERT INTO public.base_area VALUES ('510811', '昭化区', '5108');
INSERT INTO public.base_area VALUES ('510812', '朝天区', '5108');
INSERT INTO public.base_area VALUES ('510821', '旺苍县', '5108');
INSERT INTO public.base_area VALUES ('510822', '青川县', '5108');
INSERT INTO public.base_area VALUES ('510823', '剑阁县', '5108');
INSERT INTO public.base_area VALUES ('510824', '苍溪县', '5108');
INSERT INTO public.base_area VALUES ('510903', '船山区', '5109');
INSERT INTO public.base_area VALUES ('510904', '安居区', '5109');
INSERT INTO public.base_area VALUES ('510921', '蓬溪县', '5109');
INSERT INTO public.base_area VALUES ('510923', '大英县', '5109');
INSERT INTO public.base_area VALUES ('510981', '射洪市', '5109');
INSERT INTO public.base_area VALUES ('511002', '市中区', '5110');
INSERT INTO public.base_area VALUES ('511011', '东兴区', '5110');
INSERT INTO public.base_area VALUES ('511024', '威远县', '5110');
INSERT INTO public.base_area VALUES ('511025', '资中县', '5110');
INSERT INTO public.base_area VALUES ('511083', '隆昌市', '5110');
INSERT INTO public.base_area VALUES ('511102', '市中区', '5111');
INSERT INTO public.base_area VALUES ('511111', '沙湾区', '5111');
INSERT INTO public.base_area VALUES ('511112', '五通桥区', '5111');
INSERT INTO public.base_area VALUES ('511113', '金口河区', '5111');
INSERT INTO public.base_area VALUES ('511123', '犍为县', '5111');
INSERT INTO public.base_area VALUES ('511124', '井研县', '5111');
INSERT INTO public.base_area VALUES ('511126', '夹江县', '5111');
INSERT INTO public.base_area VALUES ('511129', '沐川县', '5111');
INSERT INTO public.base_area VALUES ('511132', '峨边彝族自治县', '5111');
INSERT INTO public.base_area VALUES ('511133', '马边彝族自治县', '5111');
INSERT INTO public.base_area VALUES ('511181', '峨眉山市', '5111');
INSERT INTO public.base_area VALUES ('511302', '顺庆区', '5113');
INSERT INTO public.base_area VALUES ('511303', '高坪区', '5113');
INSERT INTO public.base_area VALUES ('511304', '嘉陵区', '5113');
INSERT INTO public.base_area VALUES ('511321', '南部县', '5113');
INSERT INTO public.base_area VALUES ('511322', '营山县', '5113');
INSERT INTO public.base_area VALUES ('511323', '蓬安县', '5113');
INSERT INTO public.base_area VALUES ('511324', '仪陇县', '5113');
INSERT INTO public.base_area VALUES ('511325', '西充县', '5113');
INSERT INTO public.base_area VALUES ('511381', '阆中市', '5113');
INSERT INTO public.base_area VALUES ('511402', '东坡区', '5114');
INSERT INTO public.base_area VALUES ('511403', '彭山区', '5114');
INSERT INTO public.base_area VALUES ('511421', '仁寿县', '5114');
INSERT INTO public.base_area VALUES ('511423', '洪雅县', '5114');
INSERT INTO public.base_area VALUES ('511424', '丹棱县', '5114');
INSERT INTO public.base_area VALUES ('511425', '青神县', '5114');
INSERT INTO public.base_area VALUES ('511502', '翠屏区', '5115');
INSERT INTO public.base_area VALUES ('511503', '南溪区', '5115');
INSERT INTO public.base_area VALUES ('511504', '叙州区', '5115');
INSERT INTO public.base_area VALUES ('511523', '江安县', '5115');
INSERT INTO public.base_area VALUES ('511524', '长宁县', '5115');
INSERT INTO public.base_area VALUES ('511525', '高县', '5115');
INSERT INTO public.base_area VALUES ('511526', '珙县', '5115');
INSERT INTO public.base_area VALUES ('511527', '筠连县', '5115');
INSERT INTO public.base_area VALUES ('511528', '兴文县', '5115');
INSERT INTO public.base_area VALUES ('511529', '屏山县', '5115');
INSERT INTO public.base_area VALUES ('511602', '广安区', '5116');
INSERT INTO public.base_area VALUES ('511603', '前锋区', '5116');
INSERT INTO public.base_area VALUES ('511621', '岳池县', '5116');
INSERT INTO public.base_area VALUES ('511622', '武胜县', '5116');
INSERT INTO public.base_area VALUES ('511623', '邻水县', '5116');
INSERT INTO public.base_area VALUES ('511681', '华蓥市', '5116');
INSERT INTO public.base_area VALUES ('511702', '通川区', '5117');
INSERT INTO public.base_area VALUES ('511703', '达川区', '5117');
INSERT INTO public.base_area VALUES ('511722', '宣汉县', '5117');
INSERT INTO public.base_area VALUES ('511723', '开江县', '5117');
INSERT INTO public.base_area VALUES ('511724', '大竹县', '5117');
INSERT INTO public.base_area VALUES ('511725', '渠县', '5117');
INSERT INTO public.base_area VALUES ('511781', '万源市', '5117');
INSERT INTO public.base_area VALUES ('511802', '雨城区', '5118');
INSERT INTO public.base_area VALUES ('511803', '名山区', '5118');
INSERT INTO public.base_area VALUES ('511822', '荥经县', '5118');
INSERT INTO public.base_area VALUES ('511823', '汉源县', '5118');
INSERT INTO public.base_area VALUES ('511824', '石棉县', '5118');
INSERT INTO public.base_area VALUES ('511825', '天全县', '5118');
INSERT INTO public.base_area VALUES ('511826', '芦山县', '5118');
INSERT INTO public.base_area VALUES ('511827', '宝兴县', '5118');
INSERT INTO public.base_area VALUES ('511902', '巴州区', '5119');
INSERT INTO public.base_area VALUES ('511903', '恩阳区', '5119');
INSERT INTO public.base_area VALUES ('511921', '通江县', '5119');
INSERT INTO public.base_area VALUES ('511922', '南江县', '5119');
INSERT INTO public.base_area VALUES ('511923', '平昌县', '5119');
INSERT INTO public.base_area VALUES ('512002', '雁江区', '5120');
INSERT INTO public.base_area VALUES ('512021', '安岳县', '5120');
INSERT INTO public.base_area VALUES ('512022', '乐至县', '5120');
INSERT INTO public.base_area VALUES ('513201', '马尔康市', '5132');
INSERT INTO public.base_area VALUES ('513221', '汶川县', '5132');
INSERT INTO public.base_area VALUES ('513222', '理县', '5132');
INSERT INTO public.base_area VALUES ('513223', '茂县', '5132');
INSERT INTO public.base_area VALUES ('513224', '松潘县', '5132');
INSERT INTO public.base_area VALUES ('513225', '九寨沟县', '5132');
INSERT INTO public.base_area VALUES ('513226', '金川县', '5132');
INSERT INTO public.base_area VALUES ('513227', '小金县', '5132');
INSERT INTO public.base_area VALUES ('513228', '黑水县', '5132');
INSERT INTO public.base_area VALUES ('513230', '壤塘县', '5132');
INSERT INTO public.base_area VALUES ('513231', '阿坝县', '5132');
INSERT INTO public.base_area VALUES ('513232', '若尔盖县', '5132');
INSERT INTO public.base_area VALUES ('513233', '红原县', '5132');
INSERT INTO public.base_area VALUES ('513301', '康定市', '5133');
INSERT INTO public.base_area VALUES ('513322', '泸定县', '5133');
INSERT INTO public.base_area VALUES ('513323', '丹巴县', '5133');
INSERT INTO public.base_area VALUES ('513324', '九龙县', '5133');
INSERT INTO public.base_area VALUES ('513325', '雅江县', '5133');
INSERT INTO public.base_area VALUES ('513326', '道孚县', '5133');
INSERT INTO public.base_area VALUES ('513327', '炉霍县', '5133');
INSERT INTO public.base_area VALUES ('513328', '甘孜县', '5133');
INSERT INTO public.base_area VALUES ('513329', '新龙县', '5133');
INSERT INTO public.base_area VALUES ('513330', '德格县', '5133');
INSERT INTO public.base_area VALUES ('513331', '白玉县', '5133');
INSERT INTO public.base_area VALUES ('513332', '石渠县', '5133');
INSERT INTO public.base_area VALUES ('513333', '色达县', '5133');
INSERT INTO public.base_area VALUES ('513334', '理塘县', '5133');
INSERT INTO public.base_area VALUES ('513335', '巴塘县', '5133');
INSERT INTO public.base_area VALUES ('513336', '乡城县', '5133');
INSERT INTO public.base_area VALUES ('513337', '稻城县', '5133');
INSERT INTO public.base_area VALUES ('513338', '得荣县', '5133');
INSERT INTO public.base_area VALUES ('513401', '西昌市', '5134');
INSERT INTO public.base_area VALUES ('513402', '会理市', '5134');
INSERT INTO public.base_area VALUES ('513422', '木里藏族自治县', '5134');
INSERT INTO public.base_area VALUES ('513423', '盐源县', '5134');
INSERT INTO public.base_area VALUES ('513424', '德昌县', '5134');
INSERT INTO public.base_area VALUES ('513426', '会东县', '5134');
INSERT INTO public.base_area VALUES ('513427', '宁南县', '5134');
INSERT INTO public.base_area VALUES ('513428', '普格县', '5134');
INSERT INTO public.base_area VALUES ('513429', '布拖县', '5134');
INSERT INTO public.base_area VALUES ('513430', '金阳县', '5134');
INSERT INTO public.base_area VALUES ('513431', '昭觉县', '5134');
INSERT INTO public.base_area VALUES ('513432', '喜德县', '5134');
INSERT INTO public.base_area VALUES ('513433', '冕宁县', '5134');
INSERT INTO public.base_area VALUES ('513434', '越西县', '5134');
INSERT INTO public.base_area VALUES ('513435', '甘洛县', '5134');
INSERT INTO public.base_area VALUES ('513436', '美姑县', '5134');
INSERT INTO public.base_area VALUES ('513437', '雷波县', '5134');
INSERT INTO public.base_area VALUES ('520102', '南明区', '5201');
INSERT INTO public.base_area VALUES ('520103', '云岩区', '5201');
INSERT INTO public.base_area VALUES ('520111', '花溪区', '5201');
INSERT INTO public.base_area VALUES ('520112', '乌当区', '5201');
INSERT INTO public.base_area VALUES ('520113', '白云区', '5201');
INSERT INTO public.base_area VALUES ('520115', '观山湖区', '5201');
INSERT INTO public.base_area VALUES ('520121', '开阳县', '5201');
INSERT INTO public.base_area VALUES ('520122', '息烽县', '5201');
INSERT INTO public.base_area VALUES ('520123', '修文县', '5201');
INSERT INTO public.base_area VALUES ('520181', '清镇市', '5201');
INSERT INTO public.base_area VALUES ('520201', '钟山区', '5202');
INSERT INTO public.base_area VALUES ('520203', '六枝特区', '5202');
INSERT INTO public.base_area VALUES ('520204', '水城区', '5202');
INSERT INTO public.base_area VALUES ('520281', '盘州市', '5202');
INSERT INTO public.base_area VALUES ('520302', '红花岗区', '5203');
INSERT INTO public.base_area VALUES ('520303', '汇川区', '5203');
INSERT INTO public.base_area VALUES ('520304', '播州区', '5203');
INSERT INTO public.base_area VALUES ('520322', '桐梓县', '5203');
INSERT INTO public.base_area VALUES ('520323', '绥阳县', '5203');
INSERT INTO public.base_area VALUES ('520324', '正安县', '5203');
INSERT INTO public.base_area VALUES ('520325', '道真仡佬族苗族自治县', '5203');
INSERT INTO public.base_area VALUES ('520326', '务川仡佬族苗族自治县', '5203');
INSERT INTO public.base_area VALUES ('520327', '凤冈县', '5203');
INSERT INTO public.base_area VALUES ('520328', '湄潭县', '5203');
INSERT INTO public.base_area VALUES ('520329', '余庆县', '5203');
INSERT INTO public.base_area VALUES ('520330', '习水县', '5203');
INSERT INTO public.base_area VALUES ('520381', '赤水市', '5203');
INSERT INTO public.base_area VALUES ('520382', '仁怀市', '5203');
INSERT INTO public.base_area VALUES ('520402', '西秀区', '5204');
INSERT INTO public.base_area VALUES ('520403', '平坝区', '5204');
INSERT INTO public.base_area VALUES ('520422', '普定县', '5204');
INSERT INTO public.base_area VALUES ('520423', '镇宁布依族苗族自治县', '5204');
INSERT INTO public.base_area VALUES ('520424', '关岭布依族苗族自治县', '5204');
INSERT INTO public.base_area VALUES ('520425', '紫云苗族布依族自治县', '5204');
INSERT INTO public.base_area VALUES ('520502', '七星关区', '5205');
INSERT INTO public.base_area VALUES ('520521', '大方县', '5205');
INSERT INTO public.base_area VALUES ('520523', '金沙县', '5205');
INSERT INTO public.base_area VALUES ('520524', '织金县', '5205');
INSERT INTO public.base_area VALUES ('520525', '纳雍县', '5205');
INSERT INTO public.base_area VALUES ('520526', '威宁彝族回族苗族自治县', '5205');
INSERT INTO public.base_area VALUES ('520527', '赫章县', '5205');
INSERT INTO public.base_area VALUES ('520581', '黔西市', '5205');
INSERT INTO public.base_area VALUES ('520602', '碧江区', '5206');
INSERT INTO public.base_area VALUES ('520603', '万山区', '5206');
INSERT INTO public.base_area VALUES ('520621', '江口县', '5206');
INSERT INTO public.base_area VALUES ('520622', '玉屏侗族自治县', '5206');
INSERT INTO public.base_area VALUES ('520623', '石阡县', '5206');
INSERT INTO public.base_area VALUES ('520624', '思南县', '5206');
INSERT INTO public.base_area VALUES ('520625', '印江土家族苗族自治县', '5206');
INSERT INTO public.base_area VALUES ('520626', '德江县', '5206');
INSERT INTO public.base_area VALUES ('520627', '沿河土家族自治县', '5206');
INSERT INTO public.base_area VALUES ('520628', '松桃苗族自治县', '5206');
INSERT INTO public.base_area VALUES ('522301', '兴义市', '5223');
INSERT INTO public.base_area VALUES ('522302', '兴仁市', '5223');
INSERT INTO public.base_area VALUES ('522323', '普安县', '5223');
INSERT INTO public.base_area VALUES ('522324', '晴隆县', '5223');
INSERT INTO public.base_area VALUES ('522325', '贞丰县', '5223');
INSERT INTO public.base_area VALUES ('522326', '望谟县', '5223');
INSERT INTO public.base_area VALUES ('522327', '册亨县', '5223');
INSERT INTO public.base_area VALUES ('522328', '安龙县', '5223');
INSERT INTO public.base_area VALUES ('522601', '凯里市', '5226');
INSERT INTO public.base_area VALUES ('522622', '黄平县', '5226');
INSERT INTO public.base_area VALUES ('522623', '施秉县', '5226');
INSERT INTO public.base_area VALUES ('522624', '三穗县', '5226');
INSERT INTO public.base_area VALUES ('522625', '镇远县', '5226');
INSERT INTO public.base_area VALUES ('522626', '岑巩县', '5226');
INSERT INTO public.base_area VALUES ('522627', '天柱县', '5226');
INSERT INTO public.base_area VALUES ('522628', '锦屏县', '5226');
INSERT INTO public.base_area VALUES ('522629', '剑河县', '5226');
INSERT INTO public.base_area VALUES ('522630', '台江县', '5226');
INSERT INTO public.base_area VALUES ('522631', '黎平县', '5226');
INSERT INTO public.base_area VALUES ('522632', '榕江县', '5226');
INSERT INTO public.base_area VALUES ('522633', '从江县', '5226');
INSERT INTO public.base_area VALUES ('522634', '雷山县', '5226');
INSERT INTO public.base_area VALUES ('522635', '麻江县', '5226');
INSERT INTO public.base_area VALUES ('522636', '丹寨县', '5226');
INSERT INTO public.base_area VALUES ('522701', '都匀市', '5227');
INSERT INTO public.base_area VALUES ('522702', '福泉市', '5227');
INSERT INTO public.base_area VALUES ('522722', '荔波县', '5227');
INSERT INTO public.base_area VALUES ('522723', '贵定县', '5227');
INSERT INTO public.base_area VALUES ('522725', '瓮安县', '5227');
INSERT INTO public.base_area VALUES ('522726', '独山县', '5227');
INSERT INTO public.base_area VALUES ('522727', '平塘县', '5227');
INSERT INTO public.base_area VALUES ('522728', '罗甸县', '5227');
INSERT INTO public.base_area VALUES ('522729', '长顺县', '5227');
INSERT INTO public.base_area VALUES ('522730', '龙里县', '5227');
INSERT INTO public.base_area VALUES ('522731', '惠水县', '5227');
INSERT INTO public.base_area VALUES ('522732', '三都水族自治县', '5227');
INSERT INTO public.base_area VALUES ('530102', '五华区', '5301');
INSERT INTO public.base_area VALUES ('530103', '盘龙区', '5301');
INSERT INTO public.base_area VALUES ('530111', '官渡区', '5301');
INSERT INTO public.base_area VALUES ('530112', '西山区', '5301');
INSERT INTO public.base_area VALUES ('530113', '东川区', '5301');
INSERT INTO public.base_area VALUES ('530114', '呈贡区', '5301');
INSERT INTO public.base_area VALUES ('530115', '晋宁区', '5301');
INSERT INTO public.base_area VALUES ('530124', '富民县', '5301');
INSERT INTO public.base_area VALUES ('530125', '宜良县', '5301');
INSERT INTO public.base_area VALUES ('530126', '石林彝族自治县', '5301');
INSERT INTO public.base_area VALUES ('530127', '嵩明县', '5301');
INSERT INTO public.base_area VALUES ('530128', '禄劝彝族苗族自治县', '5301');
INSERT INTO public.base_area VALUES ('530129', '寻甸回族彝族自治县', '5301');
INSERT INTO public.base_area VALUES ('530181', '安宁市', '5301');
INSERT INTO public.base_area VALUES ('530302', '麒麟区', '5303');
INSERT INTO public.base_area VALUES ('530303', '沾益区', '5303');
INSERT INTO public.base_area VALUES ('530304', '马龙区', '5303');
INSERT INTO public.base_area VALUES ('530322', '陆良县', '5303');
INSERT INTO public.base_area VALUES ('530323', '师宗县', '5303');
INSERT INTO public.base_area VALUES ('530324', '罗平县', '5303');
INSERT INTO public.base_area VALUES ('530325', '富源县', '5303');
INSERT INTO public.base_area VALUES ('530326', '会泽县', '5303');
INSERT INTO public.base_area VALUES ('530381', '宣威市', '5303');
INSERT INTO public.base_area VALUES ('530402', '红塔区', '5304');
INSERT INTO public.base_area VALUES ('530403', '江川区', '5304');
INSERT INTO public.base_area VALUES ('530423', '通海县', '5304');
INSERT INTO public.base_area VALUES ('530424', '华宁县', '5304');
INSERT INTO public.base_area VALUES ('530425', '易门县', '5304');
INSERT INTO public.base_area VALUES ('530426', '峨山彝族自治县', '5304');
INSERT INTO public.base_area VALUES ('530427', '新平彝族傣族自治县', '5304');
INSERT INTO public.base_area VALUES ('530428', '元江哈尼族彝族傣族自治县', '5304');
INSERT INTO public.base_area VALUES ('530481', '澄江市', '5304');
INSERT INTO public.base_area VALUES ('530502', '隆阳区', '5305');
INSERT INTO public.base_area VALUES ('530521', '施甸县', '5305');
INSERT INTO public.base_area VALUES ('530523', '龙陵县', '5305');
INSERT INTO public.base_area VALUES ('530524', '昌宁县', '5305');
INSERT INTO public.base_area VALUES ('530581', '腾冲市', '5305');
INSERT INTO public.base_area VALUES ('530602', '昭阳区', '5306');
INSERT INTO public.base_area VALUES ('530621', '鲁甸县', '5306');
INSERT INTO public.base_area VALUES ('530622', '巧家县', '5306');
INSERT INTO public.base_area VALUES ('530623', '盐津县', '5306');
INSERT INTO public.base_area VALUES ('530624', '大关县', '5306');
INSERT INTO public.base_area VALUES ('530625', '永善县', '5306');
INSERT INTO public.base_area VALUES ('530626', '绥江县', '5306');
INSERT INTO public.base_area VALUES ('530627', '镇雄县', '5306');
INSERT INTO public.base_area VALUES ('530628', '彝良县', '5306');
INSERT INTO public.base_area VALUES ('530629', '威信县', '5306');
INSERT INTO public.base_area VALUES ('530681', '水富市', '5306');
INSERT INTO public.base_area VALUES ('530702', '古城区', '5307');
INSERT INTO public.base_area VALUES ('530721', '玉龙纳西族自治县', '5307');
INSERT INTO public.base_area VALUES ('530722', '永胜县', '5307');
INSERT INTO public.base_area VALUES ('530723', '华坪县', '5307');
INSERT INTO public.base_area VALUES ('530724', '宁蒗彝族自治县', '5307');
INSERT INTO public.base_area VALUES ('530802', '思茅区', '5308');
INSERT INTO public.base_area VALUES ('530821', '宁洱哈尼族彝族自治县', '5308');
INSERT INTO public.base_area VALUES ('530822', '墨江哈尼族自治县', '5308');
INSERT INTO public.base_area VALUES ('530823', '景东彝族自治县', '5308');
INSERT INTO public.base_area VALUES ('530824', '景谷傣族彝族自治县', '5308');
INSERT INTO public.base_area VALUES ('530825', '镇沅彝族哈尼族拉祜族自治县', '5308');
INSERT INTO public.base_area VALUES ('530826', '江城哈尼族彝族自治县', '5308');
INSERT INTO public.base_area VALUES ('530827', '孟连傣族拉祜族佤族自治县', '5308');
INSERT INTO public.base_area VALUES ('530828', '澜沧拉祜族自治县', '5308');
INSERT INTO public.base_area VALUES ('530829', '西盟佤族自治县', '5308');
INSERT INTO public.base_area VALUES ('530902', '临翔区', '5309');
INSERT INTO public.base_area VALUES ('530921', '凤庆县', '5309');
INSERT INTO public.base_area VALUES ('530922', '云县', '5309');
INSERT INTO public.base_area VALUES ('530923', '永德县', '5309');
INSERT INTO public.base_area VALUES ('530924', '镇康县', '5309');
INSERT INTO public.base_area VALUES ('530925', '双江拉祜族佤族布朗族傣族自治县', '5309');
INSERT INTO public.base_area VALUES ('530926', '耿马傣族佤族自治县', '5309');
INSERT INTO public.base_area VALUES ('530927', '沧源佤族自治县', '5309');
INSERT INTO public.base_area VALUES ('532301', '楚雄市', '5323');
INSERT INTO public.base_area VALUES ('532302', '禄丰市', '5323');
INSERT INTO public.base_area VALUES ('532322', '双柏县', '5323');
INSERT INTO public.base_area VALUES ('532323', '牟定县', '5323');
INSERT INTO public.base_area VALUES ('532324', '南华县', '5323');
INSERT INTO public.base_area VALUES ('532325', '姚安县', '5323');
INSERT INTO public.base_area VALUES ('532326', '大姚县', '5323');
INSERT INTO public.base_area VALUES ('532327', '永仁县', '5323');
INSERT INTO public.base_area VALUES ('532328', '元谋县', '5323');
INSERT INTO public.base_area VALUES ('532329', '武定县', '5323');
INSERT INTO public.base_area VALUES ('532501', '个旧市', '5325');
INSERT INTO public.base_area VALUES ('532502', '开远市', '5325');
INSERT INTO public.base_area VALUES ('532503', '蒙自市', '5325');
INSERT INTO public.base_area VALUES ('532504', '弥勒市', '5325');
INSERT INTO public.base_area VALUES ('532523', '屏边苗族自治县', '5325');
INSERT INTO public.base_area VALUES ('532524', '建水县', '5325');
INSERT INTO public.base_area VALUES ('532525', '石屏县', '5325');
INSERT INTO public.base_area VALUES ('532527', '泸西县', '5325');
INSERT INTO public.base_area VALUES ('532528', '元阳县', '5325');
INSERT INTO public.base_area VALUES ('532529', '红河县', '5325');
INSERT INTO public.base_area VALUES ('532530', '金平苗族瑶族傣族自治县', '5325');
INSERT INTO public.base_area VALUES ('532531', '绿春县', '5325');
INSERT INTO public.base_area VALUES ('532532', '河口瑶族自治县', '5325');
INSERT INTO public.base_area VALUES ('532601', '文山市', '5326');
INSERT INTO public.base_area VALUES ('532622', '砚山县', '5326');
INSERT INTO public.base_area VALUES ('532623', '西畴县', '5326');
INSERT INTO public.base_area VALUES ('532624', '麻栗坡县', '5326');
INSERT INTO public.base_area VALUES ('532625', '马关县', '5326');
INSERT INTO public.base_area VALUES ('532626', '丘北县', '5326');
INSERT INTO public.base_area VALUES ('532627', '广南县', '5326');
INSERT INTO public.base_area VALUES ('532628', '富宁县', '5326');
INSERT INTO public.base_area VALUES ('532801', '景洪市', '5328');
INSERT INTO public.base_area VALUES ('532822', '勐海县', '5328');
INSERT INTO public.base_area VALUES ('532823', '勐腊县', '5328');
INSERT INTO public.base_area VALUES ('532901', '大理市', '5329');
INSERT INTO public.base_area VALUES ('532922', '漾濞彝族自治县', '5329');
INSERT INTO public.base_area VALUES ('532923', '祥云县', '5329');
INSERT INTO public.base_area VALUES ('532924', '宾川县', '5329');
INSERT INTO public.base_area VALUES ('532925', '弥渡县', '5329');
INSERT INTO public.base_area VALUES ('532926', '南涧彝族自治县', '5329');
INSERT INTO public.base_area VALUES ('532927', '巍山彝族回族自治县', '5329');
INSERT INTO public.base_area VALUES ('532928', '永平县', '5329');
INSERT INTO public.base_area VALUES ('532929', '云龙县', '5329');
INSERT INTO public.base_area VALUES ('532930', '洱源县', '5329');
INSERT INTO public.base_area VALUES ('532931', '剑川县', '5329');
INSERT INTO public.base_area VALUES ('532932', '鹤庆县', '5329');
INSERT INTO public.base_area VALUES ('533102', '瑞丽市', '5331');
INSERT INTO public.base_area VALUES ('533103', '芒市', '5331');
INSERT INTO public.base_area VALUES ('533122', '梁河县', '5331');
INSERT INTO public.base_area VALUES ('533123', '盈江县', '5331');
INSERT INTO public.base_area VALUES ('533124', '陇川县', '5331');
INSERT INTO public.base_area VALUES ('533301', '泸水市', '5333');
INSERT INTO public.base_area VALUES ('533323', '福贡县', '5333');
INSERT INTO public.base_area VALUES ('533324', '贡山独龙族怒族自治县', '5333');
INSERT INTO public.base_area VALUES ('533325', '兰坪白族普米族自治县', '5333');
INSERT INTO public.base_area VALUES ('533401', '香格里拉市', '5334');
INSERT INTO public.base_area VALUES ('533422', '德钦县', '5334');
INSERT INTO public.base_area VALUES ('533423', '维西傈僳族自治县', '5334');
INSERT INTO public.base_area VALUES ('540102', '城关区', '5401');
INSERT INTO public.base_area VALUES ('540103', '堆龙德庆区', '5401');
INSERT INTO public.base_area VALUES ('540104', '达孜区', '5401');
INSERT INTO public.base_area VALUES ('540121', '林周县', '5401');
INSERT INTO public.base_area VALUES ('540122', '当雄县', '5401');
INSERT INTO public.base_area VALUES ('540123', '尼木县', '5401');
INSERT INTO public.base_area VALUES ('540124', '曲水县', '5401');
INSERT INTO public.base_area VALUES ('540127', '墨竹工卡县', '5401');
INSERT INTO public.base_area VALUES ('540171', '格尔木藏青工业园区', '5401');
INSERT INTO public.base_area VALUES ('540172', '拉萨经济技术开发区', '5401');
INSERT INTO public.base_area VALUES ('540173', '西藏文化旅游创意园区', '5401');
INSERT INTO public.base_area VALUES ('540174', '达孜工业园区', '5401');
INSERT INTO public.base_area VALUES ('540202', '桑珠孜区', '5402');
INSERT INTO public.base_area VALUES ('540221', '南木林县', '5402');
INSERT INTO public.base_area VALUES ('540222', '江孜县', '5402');
INSERT INTO public.base_area VALUES ('540223', '定日县', '5402');
INSERT INTO public.base_area VALUES ('540224', '萨迦县', '5402');
INSERT INTO public.base_area VALUES ('540225', '拉孜县', '5402');
INSERT INTO public.base_area VALUES ('540226', '昂仁县', '5402');
INSERT INTO public.base_area VALUES ('540227', '谢通门县', '5402');
INSERT INTO public.base_area VALUES ('540228', '白朗县', '5402');
INSERT INTO public.base_area VALUES ('540229', '仁布县', '5402');
INSERT INTO public.base_area VALUES ('540230', '康马县', '5402');
INSERT INTO public.base_area VALUES ('540231', '定结县', '5402');
INSERT INTO public.base_area VALUES ('540232', '仲巴县', '5402');
INSERT INTO public.base_area VALUES ('540233', '亚东县', '5402');
INSERT INTO public.base_area VALUES ('540234', '吉隆县', '5402');
INSERT INTO public.base_area VALUES ('540235', '聂拉木县', '5402');
INSERT INTO public.base_area VALUES ('540236', '萨嘎县', '5402');
INSERT INTO public.base_area VALUES ('540237', '岗巴县', '5402');
INSERT INTO public.base_area VALUES ('540302', '卡若区', '5403');
INSERT INTO public.base_area VALUES ('540321', '江达县', '5403');
INSERT INTO public.base_area VALUES ('540322', '贡觉县', '5403');
INSERT INTO public.base_area VALUES ('540323', '类乌齐县', '5403');
INSERT INTO public.base_area VALUES ('540324', '丁青县', '5403');
INSERT INTO public.base_area VALUES ('540325', '察雅县', '5403');
INSERT INTO public.base_area VALUES ('540326', '八宿县', '5403');
INSERT INTO public.base_area VALUES ('540327', '左贡县', '5403');
INSERT INTO public.base_area VALUES ('540328', '芒康县', '5403');
INSERT INTO public.base_area VALUES ('540329', '洛隆县', '5403');
INSERT INTO public.base_area VALUES ('540330', '边坝县', '5403');
INSERT INTO public.base_area VALUES ('540402', '巴宜区', '5404');
INSERT INTO public.base_area VALUES ('540421', '工布江达县', '5404');
INSERT INTO public.base_area VALUES ('540422', '米林县', '5404');
INSERT INTO public.base_area VALUES ('540423', '墨脱县', '5404');
INSERT INTO public.base_area VALUES ('540424', '波密县', '5404');
INSERT INTO public.base_area VALUES ('540425', '察隅县', '5404');
INSERT INTO public.base_area VALUES ('540426', '朗县', '5404');
INSERT INTO public.base_area VALUES ('540502', '乃东区', '5405');
INSERT INTO public.base_area VALUES ('540521', '扎囊县', '5405');
INSERT INTO public.base_area VALUES ('540522', '贡嘎县', '5405');
INSERT INTO public.base_area VALUES ('540523', '桑日县', '5405');
INSERT INTO public.base_area VALUES ('540524', '琼结县', '5405');
INSERT INTO public.base_area VALUES ('540525', '曲松县', '5405');
INSERT INTO public.base_area VALUES ('540526', '措美县', '5405');
INSERT INTO public.base_area VALUES ('540527', '洛扎县', '5405');
INSERT INTO public.base_area VALUES ('540528', '加查县', '5405');
INSERT INTO public.base_area VALUES ('540529', '隆子县', '5405');
INSERT INTO public.base_area VALUES ('540530', '错那县', '5405');
INSERT INTO public.base_area VALUES ('540531', '浪卡子县', '5405');
INSERT INTO public.base_area VALUES ('540602', '色尼区', '5406');
INSERT INTO public.base_area VALUES ('540621', '嘉黎县', '5406');
INSERT INTO public.base_area VALUES ('540622', '比如县', '5406');
INSERT INTO public.base_area VALUES ('540623', '聂荣县', '5406');
INSERT INTO public.base_area VALUES ('540624', '安多县', '5406');
INSERT INTO public.base_area VALUES ('540625', '申扎县', '5406');
INSERT INTO public.base_area VALUES ('540626', '索县', '5406');
INSERT INTO public.base_area VALUES ('540627', '班戈县', '5406');
INSERT INTO public.base_area VALUES ('540628', '巴青县', '5406');
INSERT INTO public.base_area VALUES ('540629', '尼玛县', '5406');
INSERT INTO public.base_area VALUES ('540630', '双湖县', '5406');
INSERT INTO public.base_area VALUES ('542521', '普兰县', '5425');
INSERT INTO public.base_area VALUES ('542522', '札达县', '5425');
INSERT INTO public.base_area VALUES ('542523', '噶尔县', '5425');
INSERT INTO public.base_area VALUES ('542524', '日土县', '5425');
INSERT INTO public.base_area VALUES ('542525', '革吉县', '5425');
INSERT INTO public.base_area VALUES ('542526', '改则县', '5425');
INSERT INTO public.base_area VALUES ('542527', '措勤县', '5425');
INSERT INTO public.base_area VALUES ('610102', '新城区', '6101');
INSERT INTO public.base_area VALUES ('610103', '碑林区', '6101');
INSERT INTO public.base_area VALUES ('610104', '莲湖区', '6101');
INSERT INTO public.base_area VALUES ('610111', '灞桥区', '6101');
INSERT INTO public.base_area VALUES ('610112', '未央区', '6101');
INSERT INTO public.base_area VALUES ('610113', '雁塔区', '6101');
INSERT INTO public.base_area VALUES ('610114', '阎良区', '6101');
INSERT INTO public.base_area VALUES ('610115', '临潼区', '6101');
INSERT INTO public.base_area VALUES ('610116', '长安区', '6101');
INSERT INTO public.base_area VALUES ('610117', '高陵区', '6101');
INSERT INTO public.base_area VALUES ('610118', '鄠邑区', '6101');
INSERT INTO public.base_area VALUES ('610122', '蓝田县', '6101');
INSERT INTO public.base_area VALUES ('610124', '周至县', '6101');
INSERT INTO public.base_area VALUES ('610202', '王益区', '6102');
INSERT INTO public.base_area VALUES ('610203', '印台区', '6102');
INSERT INTO public.base_area VALUES ('610204', '耀州区', '6102');
INSERT INTO public.base_area VALUES ('610222', '宜君县', '6102');
INSERT INTO public.base_area VALUES ('610302', '渭滨区', '6103');
INSERT INTO public.base_area VALUES ('610303', '金台区', '6103');
INSERT INTO public.base_area VALUES ('610304', '陈仓区', '6103');
INSERT INTO public.base_area VALUES ('610305', '凤翔区', '6103');
INSERT INTO public.base_area VALUES ('610323', '岐山县', '6103');
INSERT INTO public.base_area VALUES ('610324', '扶风县', '6103');
INSERT INTO public.base_area VALUES ('610326', '眉县', '6103');
INSERT INTO public.base_area VALUES ('610327', '陇县', '6103');
INSERT INTO public.base_area VALUES ('610328', '千阳县', '6103');
INSERT INTO public.base_area VALUES ('610329', '麟游县', '6103');
INSERT INTO public.base_area VALUES ('610330', '凤县', '6103');
INSERT INTO public.base_area VALUES ('610331', '太白县', '6103');
INSERT INTO public.base_area VALUES ('610402', '秦都区', '6104');
INSERT INTO public.base_area VALUES ('610403', '杨陵区', '6104');
INSERT INTO public.base_area VALUES ('610404', '渭城区', '6104');
INSERT INTO public.base_area VALUES ('610422', '三原县', '6104');
INSERT INTO public.base_area VALUES ('610423', '泾阳县', '6104');
INSERT INTO public.base_area VALUES ('610424', '乾县', '6104');
INSERT INTO public.base_area VALUES ('610425', '礼泉县', '6104');
INSERT INTO public.base_area VALUES ('610426', '永寿县', '6104');
INSERT INTO public.base_area VALUES ('610428', '长武县', '6104');
INSERT INTO public.base_area VALUES ('610429', '旬邑县', '6104');
INSERT INTO public.base_area VALUES ('610430', '淳化县', '6104');
INSERT INTO public.base_area VALUES ('610431', '武功县', '6104');
INSERT INTO public.base_area VALUES ('610481', '兴平市', '6104');
INSERT INTO public.base_area VALUES ('610482', '彬州市', '6104');
INSERT INTO public.base_area VALUES ('610502', '临渭区', '6105');
INSERT INTO public.base_area VALUES ('610503', '华州区', '6105');
INSERT INTO public.base_area VALUES ('610522', '潼关县', '6105');
INSERT INTO public.base_area VALUES ('610523', '大荔县', '6105');
INSERT INTO public.base_area VALUES ('610524', '合阳县', '6105');
INSERT INTO public.base_area VALUES ('610525', '澄城县', '6105');
INSERT INTO public.base_area VALUES ('610526', '蒲城县', '6105');
INSERT INTO public.base_area VALUES ('610527', '白水县', '6105');
INSERT INTO public.base_area VALUES ('610528', '富平县', '6105');
INSERT INTO public.base_area VALUES ('610581', '韩城市', '6105');
INSERT INTO public.base_area VALUES ('610582', '华阴市', '6105');
INSERT INTO public.base_area VALUES ('610602', '宝塔区', '6106');
INSERT INTO public.base_area VALUES ('610603', '安塞区', '6106');
INSERT INTO public.base_area VALUES ('610621', '延长县', '6106');
INSERT INTO public.base_area VALUES ('610622', '延川县', '6106');
INSERT INTO public.base_area VALUES ('610625', '志丹县', '6106');
INSERT INTO public.base_area VALUES ('610626', '吴起县', '6106');
INSERT INTO public.base_area VALUES ('610627', '甘泉县', '6106');
INSERT INTO public.base_area VALUES ('610628', '富县', '6106');
INSERT INTO public.base_area VALUES ('610629', '洛川县', '6106');
INSERT INTO public.base_area VALUES ('610630', '宜川县', '6106');
INSERT INTO public.base_area VALUES ('610631', '黄龙县', '6106');
INSERT INTO public.base_area VALUES ('610632', '黄陵县', '6106');
INSERT INTO public.base_area VALUES ('610681', '子长市', '6106');
INSERT INTO public.base_area VALUES ('610702', '汉台区', '6107');
INSERT INTO public.base_area VALUES ('610703', '南郑区', '6107');
INSERT INTO public.base_area VALUES ('610722', '城固县', '6107');
INSERT INTO public.base_area VALUES ('610723', '洋县', '6107');
INSERT INTO public.base_area VALUES ('610724', '西乡县', '6107');
INSERT INTO public.base_area VALUES ('610725', '勉县', '6107');
INSERT INTO public.base_area VALUES ('610726', '宁强县', '6107');
INSERT INTO public.base_area VALUES ('610727', '略阳县', '6107');
INSERT INTO public.base_area VALUES ('610728', '镇巴县', '6107');
INSERT INTO public.base_area VALUES ('610729', '留坝县', '6107');
INSERT INTO public.base_area VALUES ('610730', '佛坪县', '6107');
INSERT INTO public.base_area VALUES ('610802', '榆阳区', '6108');
INSERT INTO public.base_area VALUES ('610803', '横山区', '6108');
INSERT INTO public.base_area VALUES ('610822', '府谷县', '6108');
INSERT INTO public.base_area VALUES ('610824', '靖边县', '6108');
INSERT INTO public.base_area VALUES ('610825', '定边县', '6108');
INSERT INTO public.base_area VALUES ('610826', '绥德县', '6108');
INSERT INTO public.base_area VALUES ('610827', '米脂县', '6108');
INSERT INTO public.base_area VALUES ('610828', '佳县', '6108');
INSERT INTO public.base_area VALUES ('610829', '吴堡县', '6108');
INSERT INTO public.base_area VALUES ('610830', '清涧县', '6108');
INSERT INTO public.base_area VALUES ('610831', '子洲县', '6108');
INSERT INTO public.base_area VALUES ('610881', '神木市', '6108');
INSERT INTO public.base_area VALUES ('610902', '汉滨区', '6109');
INSERT INTO public.base_area VALUES ('610921', '汉阴县', '6109');
INSERT INTO public.base_area VALUES ('610922', '石泉县', '6109');
INSERT INTO public.base_area VALUES ('610923', '宁陕县', '6109');
INSERT INTO public.base_area VALUES ('610924', '紫阳县', '6109');
INSERT INTO public.base_area VALUES ('610925', '岚皋县', '6109');
INSERT INTO public.base_area VALUES ('610926', '平利县', '6109');
INSERT INTO public.base_area VALUES ('610927', '镇坪县', '6109');
INSERT INTO public.base_area VALUES ('610929', '白河县', '6109');
INSERT INTO public.base_area VALUES ('610981', '旬阳市', '6109');
INSERT INTO public.base_area VALUES ('611002', '商州区', '6110');
INSERT INTO public.base_area VALUES ('611021', '洛南县', '6110');
INSERT INTO public.base_area VALUES ('611022', '丹凤县', '6110');
INSERT INTO public.base_area VALUES ('611023', '商南县', '6110');
INSERT INTO public.base_area VALUES ('611024', '山阳县', '6110');
INSERT INTO public.base_area VALUES ('611025', '镇安县', '6110');
INSERT INTO public.base_area VALUES ('611026', '柞水县', '6110');
INSERT INTO public.base_area VALUES ('620102', '城关区', '6201');
INSERT INTO public.base_area VALUES ('620103', '七里河区', '6201');
INSERT INTO public.base_area VALUES ('620104', '西固区', '6201');
INSERT INTO public.base_area VALUES ('620105', '安宁区', '6201');
INSERT INTO public.base_area VALUES ('620111', '红古区', '6201');
INSERT INTO public.base_area VALUES ('620121', '永登县', '6201');
INSERT INTO public.base_area VALUES ('620122', '皋兰县', '6201');
INSERT INTO public.base_area VALUES ('620123', '榆中县', '6201');
INSERT INTO public.base_area VALUES ('620171', '兰州新区', '6201');
INSERT INTO public.base_area VALUES ('620201', '嘉峪关市', '6202');
INSERT INTO public.base_area VALUES ('620302', '金川区', '6203');
INSERT INTO public.base_area VALUES ('620321', '永昌县', '6203');
INSERT INTO public.base_area VALUES ('620402', '白银区', '6204');
INSERT INTO public.base_area VALUES ('620403', '平川区', '6204');
INSERT INTO public.base_area VALUES ('620421', '靖远县', '6204');
INSERT INTO public.base_area VALUES ('620422', '会宁县', '6204');
INSERT INTO public.base_area VALUES ('620423', '景泰县', '6204');
INSERT INTO public.base_area VALUES ('620502', '秦州区', '6205');
INSERT INTO public.base_area VALUES ('620503', '麦积区', '6205');
INSERT INTO public.base_area VALUES ('620521', '清水县', '6205');
INSERT INTO public.base_area VALUES ('620522', '秦安县', '6205');
INSERT INTO public.base_area VALUES ('620523', '甘谷县', '6205');
INSERT INTO public.base_area VALUES ('620524', '武山县', '6205');
INSERT INTO public.base_area VALUES ('620525', '张家川回族自治县', '6205');
INSERT INTO public.base_area VALUES ('620602', '凉州区', '6206');
INSERT INTO public.base_area VALUES ('620621', '民勤县', '6206');
INSERT INTO public.base_area VALUES ('620622', '古浪县', '6206');
INSERT INTO public.base_area VALUES ('620623', '天祝藏族自治县', '6206');
INSERT INTO public.base_area VALUES ('620702', '甘州区', '6207');
INSERT INTO public.base_area VALUES ('620721', '肃南裕固族自治县', '6207');
INSERT INTO public.base_area VALUES ('620722', '民乐县', '6207');
INSERT INTO public.base_area VALUES ('620723', '临泽县', '6207');
INSERT INTO public.base_area VALUES ('620724', '高台县', '6207');
INSERT INTO public.base_area VALUES ('620725', '山丹县', '6207');
INSERT INTO public.base_area VALUES ('620802', '崆峒区', '6208');
INSERT INTO public.base_area VALUES ('620821', '泾川县', '6208');
INSERT INTO public.base_area VALUES ('620822', '灵台县', '6208');
INSERT INTO public.base_area VALUES ('620823', '崇信县', '6208');
INSERT INTO public.base_area VALUES ('620825', '庄浪县', '6208');
INSERT INTO public.base_area VALUES ('620826', '静宁县', '6208');
INSERT INTO public.base_area VALUES ('620881', '华亭市', '6208');
INSERT INTO public.base_area VALUES ('620902', '肃州区', '6209');
INSERT INTO public.base_area VALUES ('620921', '金塔县', '6209');
INSERT INTO public.base_area VALUES ('620922', '瓜州县', '6209');
INSERT INTO public.base_area VALUES ('620923', '肃北蒙古族自治县', '6209');
INSERT INTO public.base_area VALUES ('620924', '阿克塞哈萨克族自治县', '6209');
INSERT INTO public.base_area VALUES ('620981', '玉门市', '6209');
INSERT INTO public.base_area VALUES ('620982', '敦煌市', '6209');
INSERT INTO public.base_area VALUES ('621002', '西峰区', '6210');
INSERT INTO public.base_area VALUES ('621021', '庆城县', '6210');
INSERT INTO public.base_area VALUES ('621022', '环县', '6210');
INSERT INTO public.base_area VALUES ('621023', '华池县', '6210');
INSERT INTO public.base_area VALUES ('621024', '合水县', '6210');
INSERT INTO public.base_area VALUES ('621025', '正宁县', '6210');
INSERT INTO public.base_area VALUES ('621026', '宁县', '6210');
INSERT INTO public.base_area VALUES ('621027', '镇原县', '6210');
INSERT INTO public.base_area VALUES ('621102', '安定区', '6211');
INSERT INTO public.base_area VALUES ('621121', '通渭县', '6211');
INSERT INTO public.base_area VALUES ('621122', '陇西县', '6211');
INSERT INTO public.base_area VALUES ('621123', '渭源县', '6211');
INSERT INTO public.base_area VALUES ('621124', '临洮县', '6211');
INSERT INTO public.base_area VALUES ('621125', '漳县', '6211');
INSERT INTO public.base_area VALUES ('621126', '岷县', '6211');
INSERT INTO public.base_area VALUES ('621202', '武都区', '6212');
INSERT INTO public.base_area VALUES ('621221', '成县', '6212');
INSERT INTO public.base_area VALUES ('621222', '文县', '6212');
INSERT INTO public.base_area VALUES ('621223', '宕昌县', '6212');
INSERT INTO public.base_area VALUES ('621224', '康县', '6212');
INSERT INTO public.base_area VALUES ('621225', '西和县', '6212');
INSERT INTO public.base_area VALUES ('621226', '礼县', '6212');
INSERT INTO public.base_area VALUES ('621227', '徽县', '6212');
INSERT INTO public.base_area VALUES ('621228', '两当县', '6212');
INSERT INTO public.base_area VALUES ('622901', '临夏市', '6229');
INSERT INTO public.base_area VALUES ('622921', '临夏县', '6229');
INSERT INTO public.base_area VALUES ('622922', '康乐县', '6229');
INSERT INTO public.base_area VALUES ('622923', '永靖县', '6229');
INSERT INTO public.base_area VALUES ('622924', '广河县', '6229');
INSERT INTO public.base_area VALUES ('622925', '和政县', '6229');
INSERT INTO public.base_area VALUES ('622926', '东乡族自治县', '6229');
INSERT INTO public.base_area VALUES ('622927', '积石山保安族东乡族撒拉族自治县', '6229');
INSERT INTO public.base_area VALUES ('623001', '合作市', '6230');
INSERT INTO public.base_area VALUES ('623021', '临潭县', '6230');
INSERT INTO public.base_area VALUES ('623022', '卓尼县', '6230');
INSERT INTO public.base_area VALUES ('623023', '舟曲县', '6230');
INSERT INTO public.base_area VALUES ('623024', '迭部县', '6230');
INSERT INTO public.base_area VALUES ('623025', '玛曲县', '6230');
INSERT INTO public.base_area VALUES ('623026', '碌曲县', '6230');
INSERT INTO public.base_area VALUES ('623027', '夏河县', '6230');
INSERT INTO public.base_area VALUES ('630102', '城东区', '6301');
INSERT INTO public.base_area VALUES ('630103', '城中区', '6301');
INSERT INTO public.base_area VALUES ('630104', '城西区', '6301');
INSERT INTO public.base_area VALUES ('630105', '城北区', '6301');
INSERT INTO public.base_area VALUES ('630106', '湟中区', '6301');
INSERT INTO public.base_area VALUES ('630121', '大通回族土族自治县', '6301');
INSERT INTO public.base_area VALUES ('630123', '湟源县', '6301');
INSERT INTO public.base_area VALUES ('630202', '乐都区', '6302');
INSERT INTO public.base_area VALUES ('630203', '平安区', '6302');
INSERT INTO public.base_area VALUES ('630222', '民和回族土族自治县', '6302');
INSERT INTO public.base_area VALUES ('630223', '互助土族自治县', '6302');
INSERT INTO public.base_area VALUES ('630224', '化隆回族自治县', '6302');
INSERT INTO public.base_area VALUES ('630225', '循化撒拉族自治县', '6302');
INSERT INTO public.base_area VALUES ('632221', '门源回族自治县', '6322');
INSERT INTO public.base_area VALUES ('632222', '祁连县', '6322');
INSERT INTO public.base_area VALUES ('632223', '海晏县', '6322');
INSERT INTO public.base_area VALUES ('632224', '刚察县', '6322');
INSERT INTO public.base_area VALUES ('632301', '同仁市', '6323');
INSERT INTO public.base_area VALUES ('632322', '尖扎县', '6323');
INSERT INTO public.base_area VALUES ('632323', '泽库县', '6323');
INSERT INTO public.base_area VALUES ('632324', '河南蒙古族自治县', '6323');
INSERT INTO public.base_area VALUES ('632521', '共和县', '6325');
INSERT INTO public.base_area VALUES ('632522', '同德县', '6325');
INSERT INTO public.base_area VALUES ('632523', '贵德县', '6325');
INSERT INTO public.base_area VALUES ('632524', '兴海县', '6325');
INSERT INTO public.base_area VALUES ('632525', '贵南县', '6325');
INSERT INTO public.base_area VALUES ('632621', '玛沁县', '6326');
INSERT INTO public.base_area VALUES ('632622', '班玛县', '6326');
INSERT INTO public.base_area VALUES ('632623', '甘德县', '6326');
INSERT INTO public.base_area VALUES ('632624', '达日县', '6326');
INSERT INTO public.base_area VALUES ('632625', '久治县', '6326');
INSERT INTO public.base_area VALUES ('632626', '玛多县', '6326');
INSERT INTO public.base_area VALUES ('632701', '玉树市', '6327');
INSERT INTO public.base_area VALUES ('632722', '杂多县', '6327');
INSERT INTO public.base_area VALUES ('632723', '称多县', '6327');
INSERT INTO public.base_area VALUES ('632724', '治多县', '6327');
INSERT INTO public.base_area VALUES ('632725', '囊谦县', '6327');
INSERT INTO public.base_area VALUES ('632726', '曲麻莱县', '6327');
INSERT INTO public.base_area VALUES ('632801', '格尔木市', '6328');
INSERT INTO public.base_area VALUES ('632802', '德令哈市', '6328');
INSERT INTO public.base_area VALUES ('632803', '茫崖市', '6328');
INSERT INTO public.base_area VALUES ('632821', '乌兰县', '6328');
INSERT INTO public.base_area VALUES ('632822', '都兰县', '6328');
INSERT INTO public.base_area VALUES ('632823', '天峻县', '6328');
INSERT INTO public.base_area VALUES ('632857', '大柴旦行政委员会', '6328');
INSERT INTO public.base_area VALUES ('640104', '兴庆区', '6401');
INSERT INTO public.base_area VALUES ('640105', '西夏区', '6401');
INSERT INTO public.base_area VALUES ('640106', '金凤区', '6401');
INSERT INTO public.base_area VALUES ('640121', '永宁县', '6401');
INSERT INTO public.base_area VALUES ('640122', '贺兰县', '6401');
INSERT INTO public.base_area VALUES ('640181', '灵武市', '6401');
INSERT INTO public.base_area VALUES ('640202', '大武口区', '6402');
INSERT INTO public.base_area VALUES ('640205', '惠农区', '6402');
INSERT INTO public.base_area VALUES ('640221', '平罗县', '6402');
INSERT INTO public.base_area VALUES ('640302', '利通区', '6403');
INSERT INTO public.base_area VALUES ('640303', '红寺堡区', '6403');
INSERT INTO public.base_area VALUES ('640323', '盐池县', '6403');
INSERT INTO public.base_area VALUES ('640324', '同心县', '6403');
INSERT INTO public.base_area VALUES ('640381', '青铜峡市', '6403');
INSERT INTO public.base_area VALUES ('640402', '原州区', '6404');
INSERT INTO public.base_area VALUES ('640422', '西吉县', '6404');
INSERT INTO public.base_area VALUES ('640423', '隆德县', '6404');
INSERT INTO public.base_area VALUES ('640424', '泾源县', '6404');
INSERT INTO public.base_area VALUES ('640425', '彭阳县', '6404');
INSERT INTO public.base_area VALUES ('640502', '沙坡头区', '6405');
INSERT INTO public.base_area VALUES ('640521', '中宁县', '6405');
INSERT INTO public.base_area VALUES ('640522', '海原县', '6405');
INSERT INTO public.base_area VALUES ('650102', '天山区', '6501');
INSERT INTO public.base_area VALUES ('650103', '沙依巴克区', '6501');
INSERT INTO public.base_area VALUES ('650104', '新市区', '6501');
INSERT INTO public.base_area VALUES ('650105', '水磨沟区', '6501');
INSERT INTO public.base_area VALUES ('650106', '头屯河区', '6501');
INSERT INTO public.base_area VALUES ('650107', '达坂城区', '6501');
INSERT INTO public.base_area VALUES ('650109', '米东区', '6501');
INSERT INTO public.base_area VALUES ('650121', '乌鲁木齐县', '6501');
INSERT INTO public.base_area VALUES ('650202', '独山子区', '6502');
INSERT INTO public.base_area VALUES ('650203', '克拉玛依区', '6502');
INSERT INTO public.base_area VALUES ('650204', '白碱滩区', '6502');
INSERT INTO public.base_area VALUES ('650205', '乌尔禾区', '6502');
INSERT INTO public.base_area VALUES ('650402', '高昌区', '6504');
INSERT INTO public.base_area VALUES ('650421', '鄯善县', '6504');
INSERT INTO public.base_area VALUES ('650422', '托克逊县', '6504');
INSERT INTO public.base_area VALUES ('650502', '伊州区', '6505');
INSERT INTO public.base_area VALUES ('650521', '巴里坤哈萨克自治县', '6505');
INSERT INTO public.base_area VALUES ('650522', '伊吾县', '6505');
INSERT INTO public.base_area VALUES ('652301', '昌吉市', '6523');
INSERT INTO public.base_area VALUES ('652302', '阜康市', '6523');
INSERT INTO public.base_area VALUES ('652323', '呼图壁县', '6523');
INSERT INTO public.base_area VALUES ('652324', '玛纳斯县', '6523');
INSERT INTO public.base_area VALUES ('652325', '奇台县', '6523');
INSERT INTO public.base_area VALUES ('652327', '吉木萨尔县', '6523');
INSERT INTO public.base_area VALUES ('652328', '木垒哈萨克自治县', '6523');
INSERT INTO public.base_area VALUES ('652701', '博乐市', '6527');
INSERT INTO public.base_area VALUES ('652702', '阿拉山口市', '6527');
INSERT INTO public.base_area VALUES ('652722', '精河县', '6527');
INSERT INTO public.base_area VALUES ('652723', '温泉县', '6527');
INSERT INTO public.base_area VALUES ('652801', '库尔勒市', '6528');
INSERT INTO public.base_area VALUES ('652822', '轮台县', '6528');
INSERT INTO public.base_area VALUES ('652823', '尉犁县', '6528');
INSERT INTO public.base_area VALUES ('652824', '若羌县', '6528');
INSERT INTO public.base_area VALUES ('652825', '且末县', '6528');
INSERT INTO public.base_area VALUES ('652826', '焉耆回族自治县', '6528');
INSERT INTO public.base_area VALUES ('652827', '和静县', '6528');
INSERT INTO public.base_area VALUES ('652828', '和硕县', '6528');
INSERT INTO public.base_area VALUES ('652829', '博湖县', '6528');
INSERT INTO public.base_area VALUES ('652871', '库尔勒经济技术开发区', '6528');
INSERT INTO public.base_area VALUES ('652901', '阿克苏市', '6529');
INSERT INTO public.base_area VALUES ('652902', '库车市', '6529');
INSERT INTO public.base_area VALUES ('652922', '温宿县', '6529');
INSERT INTO public.base_area VALUES ('652924', '沙雅县', '6529');
INSERT INTO public.base_area VALUES ('652925', '新和县', '6529');
INSERT INTO public.base_area VALUES ('652926', '拜城县', '6529');
INSERT INTO public.base_area VALUES ('652927', '乌什县', '6529');
INSERT INTO public.base_area VALUES ('652928', '阿瓦提县', '6529');
INSERT INTO public.base_area VALUES ('652929', '柯坪县', '6529');
INSERT INTO public.base_area VALUES ('653001', '阿图什市', '6530');
INSERT INTO public.base_area VALUES ('653022', '阿克陶县', '6530');
INSERT INTO public.base_area VALUES ('653023', '阿合奇县', '6530');
INSERT INTO public.base_area VALUES ('653024', '乌恰县', '6530');
INSERT INTO public.base_area VALUES ('653101', '喀什市', '6531');
INSERT INTO public.base_area VALUES ('653121', '疏附县', '6531');
INSERT INTO public.base_area VALUES ('653122', '疏勒县', '6531');
INSERT INTO public.base_area VALUES ('653123', '英吉沙县', '6531');
INSERT INTO public.base_area VALUES ('653124', '泽普县', '6531');
INSERT INTO public.base_area VALUES ('653125', '莎车县', '6531');
INSERT INTO public.base_area VALUES ('653126', '叶城县', '6531');
INSERT INTO public.base_area VALUES ('653127', '麦盖提县', '6531');
INSERT INTO public.base_area VALUES ('653128', '岳普湖县', '6531');
INSERT INTO public.base_area VALUES ('653129', '伽师县', '6531');
INSERT INTO public.base_area VALUES ('653130', '巴楚县', '6531');
INSERT INTO public.base_area VALUES ('653131', '塔什库尔干塔吉克自治县', '6531');
INSERT INTO public.base_area VALUES ('653201', '和田市', '6532');
INSERT INTO public.base_area VALUES ('653221', '和田县', '6532');
INSERT INTO public.base_area VALUES ('653222', '墨玉县', '6532');
INSERT INTO public.base_area VALUES ('653223', '皮山县', '6532');
INSERT INTO public.base_area VALUES ('653224', '洛浦县', '6532');
INSERT INTO public.base_area VALUES ('653225', '策勒县', '6532');
INSERT INTO public.base_area VALUES ('653226', '于田县', '6532');
INSERT INTO public.base_area VALUES ('653227', '民丰县', '6532');
INSERT INTO public.base_area VALUES ('654002', '伊宁市', '6540');
INSERT INTO public.base_area VALUES ('654003', '奎屯市', '6540');
INSERT INTO public.base_area VALUES ('654004', '霍尔果斯市', '6540');
INSERT INTO public.base_area VALUES ('654021', '伊宁县', '6540');
INSERT INTO public.base_area VALUES ('654022', '察布查尔锡伯自治县', '6540');
INSERT INTO public.base_area VALUES ('654023', '霍城县', '6540');
INSERT INTO public.base_area VALUES ('654024', '巩留县', '6540');
INSERT INTO public.base_area VALUES ('654025', '新源县', '6540');
INSERT INTO public.base_area VALUES ('654026', '昭苏县', '6540');
INSERT INTO public.base_area VALUES ('654027', '特克斯县', '6540');
INSERT INTO public.base_area VALUES ('654028', '尼勒克县', '6540');
INSERT INTO public.base_area VALUES ('654201', '塔城市', '6542');
INSERT INTO public.base_area VALUES ('654202', '乌苏市', '6542');
INSERT INTO public.base_area VALUES ('654203', '沙湾市', '6542');
INSERT INTO public.base_area VALUES ('654221', '额敏县', '6542');
INSERT INTO public.base_area VALUES ('654224', '托里县', '6542');
INSERT INTO public.base_area VALUES ('654225', '裕民县', '6542');
INSERT INTO public.base_area VALUES ('654226', '和布克赛尔蒙古自治县', '6542');
INSERT INTO public.base_area VALUES ('654301', '阿勒泰市', '6543');
INSERT INTO public.base_area VALUES ('654321', '布尔津县', '6543');
INSERT INTO public.base_area VALUES ('654322', '富蕴县', '6543');
INSERT INTO public.base_area VALUES ('654323', '福海县', '6543');
INSERT INTO public.base_area VALUES ('654324', '哈巴河县', '6543');
INSERT INTO public.base_area VALUES ('654325', '青河县', '6543');
INSERT INTO public.base_area VALUES ('654326', '吉木乃县', '6543');
INSERT INTO public.base_area VALUES ('659001', '石河子市', '6590');
INSERT INTO public.base_area VALUES ('659002', '阿拉尔市', '6590');
INSERT INTO public.base_area VALUES ('659003', '图木舒克市', '6590');
INSERT INTO public.base_area VALUES ('659004', '五家渠市', '6590');
INSERT INTO public.base_area VALUES ('659005', '北屯市', '6590');
INSERT INTO public.base_area VALUES ('659006', '铁门关市', '6590');
INSERT INTO public.base_area VALUES ('659007', '双河市', '6590');
INSERT INTO public.base_area VALUES ('659008', '可克达拉市', '6590');
INSERT INTO public.base_area VALUES ('659009', '昆玉市', '6590');
INSERT INTO public.base_area VALUES ('659010', '胡杨河市', '6590');
INSERT INTO public.base_area VALUES ('659011', '新星市', '6590');


--
-- Data for Name: base_city; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.base_city VALUES ('1101', '市辖区', '11');
INSERT INTO public.base_city VALUES ('1201', '市辖区', '12');
INSERT INTO public.base_city VALUES ('1301', '石家庄市', '13');
INSERT INTO public.base_city VALUES ('1302', '唐山市', '13');
INSERT INTO public.base_city VALUES ('1303', '秦皇岛市', '13');
INSERT INTO public.base_city VALUES ('1304', '邯郸市', '13');
INSERT INTO public.base_city VALUES ('1305', '邢台市', '13');
INSERT INTO public.base_city VALUES ('1306', '保定市', '13');
INSERT INTO public.base_city VALUES ('1307', '张家口市', '13');
INSERT INTO public.base_city VALUES ('1308', '承德市', '13');
INSERT INTO public.base_city VALUES ('1309', '沧州市', '13');
INSERT INTO public.base_city VALUES ('1310', '廊坊市', '13');
INSERT INTO public.base_city VALUES ('1311', '衡水市', '13');
INSERT INTO public.base_city VALUES ('1401', '太原市', '14');
INSERT INTO public.base_city VALUES ('1402', '大同市', '14');
INSERT INTO public.base_city VALUES ('1403', '阳泉市', '14');
INSERT INTO public.base_city VALUES ('1404', '长治市', '14');
INSERT INTO public.base_city VALUES ('1405', '晋城市', '14');
INSERT INTO public.base_city VALUES ('1406', '朔州市', '14');
INSERT INTO public.base_city VALUES ('1407', '晋中市', '14');
INSERT INTO public.base_city VALUES ('1408', '运城市', '14');
INSERT INTO public.base_city VALUES ('1409', '忻州市', '14');
INSERT INTO public.base_city VALUES ('1410', '临汾市', '14');
INSERT INTO public.base_city VALUES ('1411', '吕梁市', '14');
INSERT INTO public.base_city VALUES ('1501', '呼和浩特市', '15');
INSERT INTO public.base_city VALUES ('1502', '包头市', '15');
INSERT INTO public.base_city VALUES ('1503', '乌海市', '15');
INSERT INTO public.base_city VALUES ('1504', '赤峰市', '15');
INSERT INTO public.base_city VALUES ('1505', '通辽市', '15');
INSERT INTO public.base_city VALUES ('1506', '鄂尔多斯市', '15');
INSERT INTO public.base_city VALUES ('1507', '呼伦贝尔市', '15');
INSERT INTO public.base_city VALUES ('1508', '巴彦淖尔市', '15');
INSERT INTO public.base_city VALUES ('1509', '乌兰察布市', '15');
INSERT INTO public.base_city VALUES ('1522', '兴安盟', '15');
INSERT INTO public.base_city VALUES ('1525', '锡林郭勒盟', '15');
INSERT INTO public.base_city VALUES ('1529', '阿拉善盟', '15');
INSERT INTO public.base_city VALUES ('2101', '沈阳市', '21');
INSERT INTO public.base_city VALUES ('2102', '大连市', '21');
INSERT INTO public.base_city VALUES ('2103', '鞍山市', '21');
INSERT INTO public.base_city VALUES ('2104', '抚顺市', '21');
INSERT INTO public.base_city VALUES ('2105', '本溪市', '21');
INSERT INTO public.base_city VALUES ('2106', '丹东市', '21');
INSERT INTO public.base_city VALUES ('2107', '锦州市', '21');
INSERT INTO public.base_city VALUES ('2108', '营口市', '21');
INSERT INTO public.base_city VALUES ('2109', '阜新市', '21');
INSERT INTO public.base_city VALUES ('2110', '辽阳市', '21');
INSERT INTO public.base_city VALUES ('2111', '盘锦市', '21');
INSERT INTO public.base_city VALUES ('2112', '铁岭市', '21');
INSERT INTO public.base_city VALUES ('2113', '朝阳市', '21');
INSERT INTO public.base_city VALUES ('2114', '葫芦岛市', '21');
INSERT INTO public.base_city VALUES ('2201', '长春市', '22');
INSERT INTO public.base_city VALUES ('2202', '吉林市', '22');
INSERT INTO public.base_city VALUES ('2203', '四平市', '22');
INSERT INTO public.base_city VALUES ('2204', '辽源市', '22');
INSERT INTO public.base_city VALUES ('2205', '通化市', '22');
INSERT INTO public.base_city VALUES ('2206', '白山市', '22');
INSERT INTO public.base_city VALUES ('2207', '松原市', '22');
INSERT INTO public.base_city VALUES ('2208', '白城市', '22');
INSERT INTO public.base_city VALUES ('2224', '延边朝鲜族自治州', '22');
INSERT INTO public.base_city VALUES ('2301', '哈尔滨市', '23');
INSERT INTO public.base_city VALUES ('2302', '齐齐哈尔市', '23');
INSERT INTO public.base_city VALUES ('2303', '鸡西市', '23');
INSERT INTO public.base_city VALUES ('2304', '鹤岗市', '23');
INSERT INTO public.base_city VALUES ('2305', '双鸭山市', '23');
INSERT INTO public.base_city VALUES ('2306', '大庆市', '23');
INSERT INTO public.base_city VALUES ('2307', '伊春市', '23');
INSERT INTO public.base_city VALUES ('2308', '佳木斯市', '23');
INSERT INTO public.base_city VALUES ('2309', '七台河市', '23');
INSERT INTO public.base_city VALUES ('2310', '牡丹江市', '23');
INSERT INTO public.base_city VALUES ('2311', '黑河市', '23');
INSERT INTO public.base_city VALUES ('2312', '绥化市', '23');
INSERT INTO public.base_city VALUES ('2327', '大兴安岭地区', '23');
INSERT INTO public.base_city VALUES ('3101', '市辖区', '31');
INSERT INTO public.base_city VALUES ('3201', '南京市', '32');
INSERT INTO public.base_city VALUES ('3202', '无锡市', '32');
INSERT INTO public.base_city VALUES ('3203', '徐州市', '32');
INSERT INTO public.base_city VALUES ('3204', '常州市', '32');
INSERT INTO public.base_city VALUES ('3205', '苏州市', '32');
INSERT INTO public.base_city VALUES ('3206', '南通市', '32');
INSERT INTO public.base_city VALUES ('3207', '连云港市', '32');
INSERT INTO public.base_city VALUES ('3208', '淮安市', '32');
INSERT INTO public.base_city VALUES ('3209', '盐城市', '32');
INSERT INTO public.base_city VALUES ('3210', '扬州市', '32');
INSERT INTO public.base_city VALUES ('3211', '镇江市', '32');
INSERT INTO public.base_city VALUES ('3212', '泰州市', '32');
INSERT INTO public.base_city VALUES ('3213', '宿迁市', '32');
INSERT INTO public.base_city VALUES ('3301', '杭州市', '33');
INSERT INTO public.base_city VALUES ('3302', '宁波市', '33');
INSERT INTO public.base_city VALUES ('3303', '温州市', '33');
INSERT INTO public.base_city VALUES ('3304', '嘉兴市', '33');
INSERT INTO public.base_city VALUES ('3305', '湖州市', '33');
INSERT INTO public.base_city VALUES ('3306', '绍兴市', '33');
INSERT INTO public.base_city VALUES ('3307', '金华市', '33');
INSERT INTO public.base_city VALUES ('3308', '衢州市', '33');
INSERT INTO public.base_city VALUES ('3309', '舟山市', '33');
INSERT INTO public.base_city VALUES ('3310', '台州市', '33');
INSERT INTO public.base_city VALUES ('3311', '丽水市', '33');
INSERT INTO public.base_city VALUES ('3401', '合肥市', '34');
INSERT INTO public.base_city VALUES ('3402', '芜湖市', '34');
INSERT INTO public.base_city VALUES ('3403', '蚌埠市', '34');
INSERT INTO public.base_city VALUES ('3404', '淮南市', '34');
INSERT INTO public.base_city VALUES ('3405', '马鞍山市', '34');
INSERT INTO public.base_city VALUES ('3406', '淮北市', '34');
INSERT INTO public.base_city VALUES ('3407', '铜陵市', '34');
INSERT INTO public.base_city VALUES ('3408', '安庆市', '34');
INSERT INTO public.base_city VALUES ('3410', '黄山市', '34');
INSERT INTO public.base_city VALUES ('3411', '滁州市', '34');
INSERT INTO public.base_city VALUES ('3412', '阜阳市', '34');
INSERT INTO public.base_city VALUES ('3413', '宿州市', '34');
INSERT INTO public.base_city VALUES ('3415', '六安市', '34');
INSERT INTO public.base_city VALUES ('3416', '亳州市', '34');
INSERT INTO public.base_city VALUES ('3417', '池州市', '34');
INSERT INTO public.base_city VALUES ('3418', '宣城市', '34');
INSERT INTO public.base_city VALUES ('3501', '福州市', '35');
INSERT INTO public.base_city VALUES ('3502', '厦门市', '35');
INSERT INTO public.base_city VALUES ('3503', '莆田市', '35');
INSERT INTO public.base_city VALUES ('3504', '三明市', '35');
INSERT INTO public.base_city VALUES ('3505', '泉州市', '35');
INSERT INTO public.base_city VALUES ('3506', '漳州市', '35');
INSERT INTO public.base_city VALUES ('3507', '南平市', '35');
INSERT INTO public.base_city VALUES ('3508', '龙岩市', '35');
INSERT INTO public.base_city VALUES ('3509', '宁德市', '35');
INSERT INTO public.base_city VALUES ('3601', '南昌市', '36');
INSERT INTO public.base_city VALUES ('3602', '景德镇市', '36');
INSERT INTO public.base_city VALUES ('3603', '萍乡市', '36');
INSERT INTO public.base_city VALUES ('3604', '九江市', '36');
INSERT INTO public.base_city VALUES ('3605', '新余市', '36');
INSERT INTO public.base_city VALUES ('3606', '鹰潭市', '36');
INSERT INTO public.base_city VALUES ('3607', '赣州市', '36');
INSERT INTO public.base_city VALUES ('3608', '吉安市', '36');
INSERT INTO public.base_city VALUES ('3609', '宜春市', '36');
INSERT INTO public.base_city VALUES ('3610', '抚州市', '36');
INSERT INTO public.base_city VALUES ('3611', '上饶市', '36');
INSERT INTO public.base_city VALUES ('3701', '济南市', '37');
INSERT INTO public.base_city VALUES ('3702', '青岛市', '37');
INSERT INTO public.base_city VALUES ('3703', '淄博市', '37');
INSERT INTO public.base_city VALUES ('3704', '枣庄市', '37');
INSERT INTO public.base_city VALUES ('3705', '东营市', '37');
INSERT INTO public.base_city VALUES ('3706', '烟台市', '37');
INSERT INTO public.base_city VALUES ('3707', '潍坊市', '37');
INSERT INTO public.base_city VALUES ('3708', '济宁市', '37');
INSERT INTO public.base_city VALUES ('3709', '泰安市', '37');
INSERT INTO public.base_city VALUES ('3710', '威海市', '37');
INSERT INTO public.base_city VALUES ('3711', '日照市', '37');
INSERT INTO public.base_city VALUES ('3713', '临沂市', '37');
INSERT INTO public.base_city VALUES ('3714', '德州市', '37');
INSERT INTO public.base_city VALUES ('3715', '聊城市', '37');
INSERT INTO public.base_city VALUES ('3716', '滨州市', '37');
INSERT INTO public.base_city VALUES ('3717', '菏泽市', '37');
INSERT INTO public.base_city VALUES ('4101', '郑州市', '41');
INSERT INTO public.base_city VALUES ('4102', '开封市', '41');
INSERT INTO public.base_city VALUES ('4103', '洛阳市', '41');
INSERT INTO public.base_city VALUES ('4104', '平顶山市', '41');
INSERT INTO public.base_city VALUES ('4105', '安阳市', '41');
INSERT INTO public.base_city VALUES ('4106', '鹤壁市', '41');
INSERT INTO public.base_city VALUES ('4107', '新乡市', '41');
INSERT INTO public.base_city VALUES ('4108', '焦作市', '41');
INSERT INTO public.base_city VALUES ('4109', '濮阳市', '41');
INSERT INTO public.base_city VALUES ('4110', '许昌市', '41');
INSERT INTO public.base_city VALUES ('4111', '漯河市', '41');
INSERT INTO public.base_city VALUES ('4112', '三门峡市', '41');
INSERT INTO public.base_city VALUES ('4113', '南阳市', '41');
INSERT INTO public.base_city VALUES ('4114', '商丘市', '41');
INSERT INTO public.base_city VALUES ('4115', '信阳市', '41');
INSERT INTO public.base_city VALUES ('4116', '周口市', '41');
INSERT INTO public.base_city VALUES ('4117', '驻马店市', '41');
INSERT INTO public.base_city VALUES ('4190', '省直辖县级行政区划', '41');
INSERT INTO public.base_city VALUES ('4201', '武汉市', '42');
INSERT INTO public.base_city VALUES ('4202', '黄石市', '42');
INSERT INTO public.base_city VALUES ('4203', '十堰市', '42');
INSERT INTO public.base_city VALUES ('4205', '宜昌市', '42');
INSERT INTO public.base_city VALUES ('4206', '襄阳市', '42');
INSERT INTO public.base_city VALUES ('4207', '鄂州市', '42');
INSERT INTO public.base_city VALUES ('4208', '荆门市', '42');
INSERT INTO public.base_city VALUES ('4209', '孝感市', '42');
INSERT INTO public.base_city VALUES ('4210', '荆州市', '42');
INSERT INTO public.base_city VALUES ('4211', '黄冈市', '42');
INSERT INTO public.base_city VALUES ('4212', '咸宁市', '42');
INSERT INTO public.base_city VALUES ('4213', '随州市', '42');
INSERT INTO public.base_city VALUES ('4228', '恩施土家族苗族自治州', '42');
INSERT INTO public.base_city VALUES ('4290', '省直辖县级行政区划', '42');
INSERT INTO public.base_city VALUES ('4301', '长沙市', '43');
INSERT INTO public.base_city VALUES ('4302', '株洲市', '43');
INSERT INTO public.base_city VALUES ('4303', '湘潭市', '43');
INSERT INTO public.base_city VALUES ('4304', '衡阳市', '43');
INSERT INTO public.base_city VALUES ('4305', '邵阳市', '43');
INSERT INTO public.base_city VALUES ('4306', '岳阳市', '43');
INSERT INTO public.base_city VALUES ('4307', '常德市', '43');
INSERT INTO public.base_city VALUES ('4308', '张家界市', '43');
INSERT INTO public.base_city VALUES ('4309', '益阳市', '43');
INSERT INTO public.base_city VALUES ('4310', '郴州市', '43');
INSERT INTO public.base_city VALUES ('4311', '永州市', '43');
INSERT INTO public.base_city VALUES ('4312', '怀化市', '43');
INSERT INTO public.base_city VALUES ('4313', '娄底市', '43');
INSERT INTO public.base_city VALUES ('4331', '湘西土家族苗族自治州', '43');
INSERT INTO public.base_city VALUES ('4401', '广州市', '44');
INSERT INTO public.base_city VALUES ('4402', '韶关市', '44');
INSERT INTO public.base_city VALUES ('4403', '深圳市', '44');
INSERT INTO public.base_city VALUES ('4404', '珠海市', '44');
INSERT INTO public.base_city VALUES ('4405', '汕头市', '44');
INSERT INTO public.base_city VALUES ('4406', '佛山市', '44');
INSERT INTO public.base_city VALUES ('4407', '江门市', '44');
INSERT INTO public.base_city VALUES ('4408', '湛江市', '44');
INSERT INTO public.base_city VALUES ('4409', '茂名市', '44');
INSERT INTO public.base_city VALUES ('4412', '肇庆市', '44');
INSERT INTO public.base_city VALUES ('4413', '惠州市', '44');
INSERT INTO public.base_city VALUES ('4414', '梅州市', '44');
INSERT INTO public.base_city VALUES ('4415', '汕尾市', '44');
INSERT INTO public.base_city VALUES ('4416', '河源市', '44');
INSERT INTO public.base_city VALUES ('4417', '阳江市', '44');
INSERT INTO public.base_city VALUES ('4418', '清远市', '44');
INSERT INTO public.base_city VALUES ('4419', '东莞市', '44');
INSERT INTO public.base_city VALUES ('4420', '中山市', '44');
INSERT INTO public.base_city VALUES ('4451', '潮州市', '44');
INSERT INTO public.base_city VALUES ('4452', '揭阳市', '44');
INSERT INTO public.base_city VALUES ('4453', '云浮市', '44');
INSERT INTO public.base_city VALUES ('4501', '南宁市', '45');
INSERT INTO public.base_city VALUES ('4502', '柳州市', '45');
INSERT INTO public.base_city VALUES ('4503', '桂林市', '45');
INSERT INTO public.base_city VALUES ('4504', '梧州市', '45');
INSERT INTO public.base_city VALUES ('4505', '北海市', '45');
INSERT INTO public.base_city VALUES ('4506', '防城港市', '45');
INSERT INTO public.base_city VALUES ('4507', '钦州市', '45');
INSERT INTO public.base_city VALUES ('4508', '贵港市', '45');
INSERT INTO public.base_city VALUES ('4509', '玉林市', '45');
INSERT INTO public.base_city VALUES ('4510', '百色市', '45');
INSERT INTO public.base_city VALUES ('4511', '贺州市', '45');
INSERT INTO public.base_city VALUES ('4512', '河池市', '45');
INSERT INTO public.base_city VALUES ('4513', '来宾市', '45');
INSERT INTO public.base_city VALUES ('4514', '崇左市', '45');
INSERT INTO public.base_city VALUES ('4601', '海口市', '46');
INSERT INTO public.base_city VALUES ('4602', '三亚市', '46');
INSERT INTO public.base_city VALUES ('4603', '三沙市', '46');
INSERT INTO public.base_city VALUES ('4604', '儋州市', '46');
INSERT INTO public.base_city VALUES ('4690', '省直辖县级行政区划', '46');
INSERT INTO public.base_city VALUES ('5001', '市辖区', '50');
INSERT INTO public.base_city VALUES ('5002', '县', '50');
INSERT INTO public.base_city VALUES ('5101', '成都市', '51');
INSERT INTO public.base_city VALUES ('5103', '自贡市', '51');
INSERT INTO public.base_city VALUES ('5104', '攀枝花市', '51');
INSERT INTO public.base_city VALUES ('5105', '泸州市', '51');
INSERT INTO public.base_city VALUES ('5106', '德阳市', '51');
INSERT INTO public.base_city VALUES ('5107', '绵阳市', '51');
INSERT INTO public.base_city VALUES ('5108', '广元市', '51');
INSERT INTO public.base_city VALUES ('5109', '遂宁市', '51');
INSERT INTO public.base_city VALUES ('5110', '内江市', '51');
INSERT INTO public.base_city VALUES ('5111', '乐山市', '51');
INSERT INTO public.base_city VALUES ('5113', '南充市', '51');
INSERT INTO public.base_city VALUES ('5114', '眉山市', '51');
INSERT INTO public.base_city VALUES ('5115', '宜宾市', '51');
INSERT INTO public.base_city VALUES ('5116', '广安市', '51');
INSERT INTO public.base_city VALUES ('5117', '达州市', '51');
INSERT INTO public.base_city VALUES ('5118', '雅安市', '51');
INSERT INTO public.base_city VALUES ('5119', '巴中市', '51');
INSERT INTO public.base_city VALUES ('5120', '资阳市', '51');
INSERT INTO public.base_city VALUES ('5132', '阿坝藏族羌族自治州', '51');
INSERT INTO public.base_city VALUES ('5133', '甘孜藏族自治州', '51');
INSERT INTO public.base_city VALUES ('5134', '凉山彝族自治州', '51');
INSERT INTO public.base_city VALUES ('5201', '贵阳市', '52');
INSERT INTO public.base_city VALUES ('5202', '六盘水市', '52');
INSERT INTO public.base_city VALUES ('5203', '遵义市', '52');
INSERT INTO public.base_city VALUES ('5204', '安顺市', '52');
INSERT INTO public.base_city VALUES ('5205', '毕节市', '52');
INSERT INTO public.base_city VALUES ('5206', '铜仁市', '52');
INSERT INTO public.base_city VALUES ('5223', '黔西南布依族苗族自治州', '52');
INSERT INTO public.base_city VALUES ('5226', '黔东南苗族侗族自治州', '52');
INSERT INTO public.base_city VALUES ('5227', '黔南布依族苗族自治州', '52');
INSERT INTO public.base_city VALUES ('5301', '昆明市', '53');
INSERT INTO public.base_city VALUES ('5303', '曲靖市', '53');
INSERT INTO public.base_city VALUES ('5304', '玉溪市', '53');
INSERT INTO public.base_city VALUES ('5305', '保山市', '53');
INSERT INTO public.base_city VALUES ('5306', '昭通市', '53');
INSERT INTO public.base_city VALUES ('5307', '丽江市', '53');
INSERT INTO public.base_city VALUES ('5308', '普洱市', '53');
INSERT INTO public.base_city VALUES ('5309', '临沧市', '53');
INSERT INTO public.base_city VALUES ('5323', '楚雄彝族自治州', '53');
INSERT INTO public.base_city VALUES ('5325', '红河哈尼族彝族自治州', '53');
INSERT INTO public.base_city VALUES ('5326', '文山壮族苗族自治州', '53');
INSERT INTO public.base_city VALUES ('5328', '西双版纳傣族自治州', '53');
INSERT INTO public.base_city VALUES ('5329', '大理白族自治州', '53');
INSERT INTO public.base_city VALUES ('5331', '德宏傣族景颇族自治州', '53');
INSERT INTO public.base_city VALUES ('5333', '怒江傈僳族自治州', '53');
INSERT INTO public.base_city VALUES ('5334', '迪庆藏族自治州', '53');
INSERT INTO public.base_city VALUES ('5401', '拉萨市', '54');
INSERT INTO public.base_city VALUES ('5402', '日喀则市', '54');
INSERT INTO public.base_city VALUES ('5403', '昌都市', '54');
INSERT INTO public.base_city VALUES ('5404', '林芝市', '54');
INSERT INTO public.base_city VALUES ('5405', '山南市', '54');
INSERT INTO public.base_city VALUES ('5406', '那曲市', '54');
INSERT INTO public.base_city VALUES ('5425', '阿里地区', '54');
INSERT INTO public.base_city VALUES ('6101', '西安市', '61');
INSERT INTO public.base_city VALUES ('6102', '铜川市', '61');
INSERT INTO public.base_city VALUES ('6103', '宝鸡市', '61');
INSERT INTO public.base_city VALUES ('6104', '咸阳市', '61');
INSERT INTO public.base_city VALUES ('6105', '渭南市', '61');
INSERT INTO public.base_city VALUES ('6106', '延安市', '61');
INSERT INTO public.base_city VALUES ('6107', '汉中市', '61');
INSERT INTO public.base_city VALUES ('6108', '榆林市', '61');
INSERT INTO public.base_city VALUES ('6109', '安康市', '61');
INSERT INTO public.base_city VALUES ('6110', '商洛市', '61');
INSERT INTO public.base_city VALUES ('6201', '兰州市', '62');
INSERT INTO public.base_city VALUES ('6202', '嘉峪关市', '62');
INSERT INTO public.base_city VALUES ('6203', '金昌市', '62');
INSERT INTO public.base_city VALUES ('6204', '白银市', '62');
INSERT INTO public.base_city VALUES ('6205', '天水市', '62');
INSERT INTO public.base_city VALUES ('6206', '武威市', '62');
INSERT INTO public.base_city VALUES ('6207', '张掖市', '62');
INSERT INTO public.base_city VALUES ('6208', '平凉市', '62');
INSERT INTO public.base_city VALUES ('6209', '酒泉市', '62');
INSERT INTO public.base_city VALUES ('6210', '庆阳市', '62');
INSERT INTO public.base_city VALUES ('6211', '定西市', '62');
INSERT INTO public.base_city VALUES ('6212', '陇南市', '62');
INSERT INTO public.base_city VALUES ('6229', '临夏回族自治州', '62');
INSERT INTO public.base_city VALUES ('6230', '甘南藏族自治州', '62');
INSERT INTO public.base_city VALUES ('6301', '西宁市', '63');
INSERT INTO public.base_city VALUES ('6302', '海东市', '63');
INSERT INTO public.base_city VALUES ('6322', '海北藏族自治州', '63');
INSERT INTO public.base_city VALUES ('6323', '黄南藏族自治州', '63');
INSERT INTO public.base_city VALUES ('6325', '海南藏族自治州', '63');
INSERT INTO public.base_city VALUES ('6326', '果洛藏族自治州', '63');
INSERT INTO public.base_city VALUES ('6327', '玉树藏族自治州', '63');
INSERT INTO public.base_city VALUES ('6328', '海西蒙古族藏族自治州', '63');
INSERT INTO public.base_city VALUES ('6401', '银川市', '64');
INSERT INTO public.base_city VALUES ('6402', '石嘴山市', '64');
INSERT INTO public.base_city VALUES ('6403', '吴忠市', '64');
INSERT INTO public.base_city VALUES ('6404', '固原市', '64');
INSERT INTO public.base_city VALUES ('6405', '中卫市', '64');
INSERT INTO public.base_city VALUES ('6501', '乌鲁木齐市', '65');
INSERT INTO public.base_city VALUES ('6502', '克拉玛依市', '65');
INSERT INTO public.base_city VALUES ('6504', '吐鲁番市', '65');
INSERT INTO public.base_city VALUES ('6505', '哈密市', '65');
INSERT INTO public.base_city VALUES ('6523', '昌吉回族自治州', '65');
INSERT INTO public.base_city VALUES ('6527', '博尔塔拉蒙古自治州', '65');
INSERT INTO public.base_city VALUES ('6528', '巴音郭楞蒙古自治州', '65');
INSERT INTO public.base_city VALUES ('6529', '阿克苏地区', '65');
INSERT INTO public.base_city VALUES ('6530', '克孜勒苏柯尔克孜自治州', '65');
INSERT INTO public.base_city VALUES ('6531', '喀什地区', '65');
INSERT INTO public.base_city VALUES ('6532', '和田地区', '65');
INSERT INTO public.base_city VALUES ('6540', '伊犁哈萨克自治州', '65');
INSERT INTO public.base_city VALUES ('6542', '塔城地区', '65');
INSERT INTO public.base_city VALUES ('6543', '阿勒泰地区', '65');
INSERT INTO public.base_city VALUES ('6590', '自治区直辖县级行政区划', '65');


--
-- Data for Name: base_city_adjacent; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.base_city_adjacent VALUES ('1101', '1201', 1);
INSERT INTO public.base_city_adjacent VALUES ('1201', '1101', 2);
INSERT INTO public.base_city_adjacent VALUES ('1101', '1306', 3);
INSERT INTO public.base_city_adjacent VALUES ('1306', '1101', 4);
INSERT INTO public.base_city_adjacent VALUES ('1101', '1307', 5);
INSERT INTO public.base_city_adjacent VALUES ('1307', '1101', 6);
INSERT INTO public.base_city_adjacent VALUES ('1101', '1308', 7);
INSERT INTO public.base_city_adjacent VALUES ('1308', '1101', 8);
INSERT INTO public.base_city_adjacent VALUES ('1101', '1310', 9);
INSERT INTO public.base_city_adjacent VALUES ('1310', '1101', 10);
INSERT INTO public.base_city_adjacent VALUES ('1201', '1302', 11);
INSERT INTO public.base_city_adjacent VALUES ('1302', '1201', 12);
INSERT INTO public.base_city_adjacent VALUES ('1201', '1308', 13);
INSERT INTO public.base_city_adjacent VALUES ('1308', '1201', 14);
INSERT INTO public.base_city_adjacent VALUES ('1201', '1309', 15);
INSERT INTO public.base_city_adjacent VALUES ('1309', '1201', 16);
INSERT INTO public.base_city_adjacent VALUES ('1201', '1310', 17);
INSERT INTO public.base_city_adjacent VALUES ('1310', '1201', 18);
INSERT INTO public.base_city_adjacent VALUES ('1301', '1305', 19);
INSERT INTO public.base_city_adjacent VALUES ('1305', '1301', 20);
INSERT INTO public.base_city_adjacent VALUES ('1301', '1306', 21);
INSERT INTO public.base_city_adjacent VALUES ('1306', '1301', 22);
INSERT INTO public.base_city_adjacent VALUES ('1301', '1311', 23);
INSERT INTO public.base_city_adjacent VALUES ('1311', '1301', 24);
INSERT INTO public.base_city_adjacent VALUES ('1301', '1403', 25);
INSERT INTO public.base_city_adjacent VALUES ('1403', '1301', 26);
INSERT INTO public.base_city_adjacent VALUES ('1301', '1407', 27);
INSERT INTO public.base_city_adjacent VALUES ('1407', '1301', 28);
INSERT INTO public.base_city_adjacent VALUES ('1301', '1409', 29);
INSERT INTO public.base_city_adjacent VALUES ('1409', '1301', 30);
INSERT INTO public.base_city_adjacent VALUES ('1302', '1303', 31);
INSERT INTO public.base_city_adjacent VALUES ('1303', '1302', 32);
INSERT INTO public.base_city_adjacent VALUES ('1302', '1308', 33);
INSERT INTO public.base_city_adjacent VALUES ('1308', '1302', 34);
INSERT INTO public.base_city_adjacent VALUES ('1303', '1308', 35);
INSERT INTO public.base_city_adjacent VALUES ('1308', '1303', 36);
INSERT INTO public.base_city_adjacent VALUES ('1303', '2113', 37);
INSERT INTO public.base_city_adjacent VALUES ('2113', '1303', 38);
INSERT INTO public.base_city_adjacent VALUES ('1303', '2114', 39);
INSERT INTO public.base_city_adjacent VALUES ('2114', '1303', 40);
INSERT INTO public.base_city_adjacent VALUES ('1304', '1305', 41);
INSERT INTO public.base_city_adjacent VALUES ('1305', '1304', 42);
INSERT INTO public.base_city_adjacent VALUES ('1304', '1404', 43);
INSERT INTO public.base_city_adjacent VALUES ('1404', '1304', 44);
INSERT INTO public.base_city_adjacent VALUES ('1304', '1407', 45);
INSERT INTO public.base_city_adjacent VALUES ('1407', '1304', 46);
INSERT INTO public.base_city_adjacent VALUES ('1304', '3715', 47);
INSERT INTO public.base_city_adjacent VALUES ('3715', '1304', 48);
INSERT INTO public.base_city_adjacent VALUES ('1304', '4105', 49);
INSERT INTO public.base_city_adjacent VALUES ('4105', '1304', 50);
INSERT INTO public.base_city_adjacent VALUES ('1304', '4109', 51);
INSERT INTO public.base_city_adjacent VALUES ('4109', '1304', 52);
INSERT INTO public.base_city_adjacent VALUES ('1305', '1311', 53);
INSERT INTO public.base_city_adjacent VALUES ('1311', '1305', 54);
INSERT INTO public.base_city_adjacent VALUES ('1305', '1407', 55);
INSERT INTO public.base_city_adjacent VALUES ('1407', '1305', 56);
INSERT INTO public.base_city_adjacent VALUES ('1305', '3714', 57);
INSERT INTO public.base_city_adjacent VALUES ('3714', '1305', 58);
INSERT INTO public.base_city_adjacent VALUES ('1305', '3715', 59);
INSERT INTO public.base_city_adjacent VALUES ('3715', '1305', 60);
INSERT INTO public.base_city_adjacent VALUES ('1306', '1307', 61);
INSERT INTO public.base_city_adjacent VALUES ('1307', '1306', 62);
INSERT INTO public.base_city_adjacent VALUES ('1306', '1309', 63);
INSERT INTO public.base_city_adjacent VALUES ('1309', '1306', 64);
INSERT INTO public.base_city_adjacent VALUES ('1306', '1310', 65);
INSERT INTO public.base_city_adjacent VALUES ('1310', '1306', 66);
INSERT INTO public.base_city_adjacent VALUES ('1306', '1311', 67);
INSERT INTO public.base_city_adjacent VALUES ('1311', '1306', 68);
INSERT INTO public.base_city_adjacent VALUES ('1306', '1402', 69);
INSERT INTO public.base_city_adjacent VALUES ('1402', '1306', 70);
INSERT INTO public.base_city_adjacent VALUES ('1306', '1409', 71);
INSERT INTO public.base_city_adjacent VALUES ('1409', '1306', 72);
INSERT INTO public.base_city_adjacent VALUES ('1307', '1308', 73);
INSERT INTO public.base_city_adjacent VALUES ('1308', '1307', 74);
INSERT INTO public.base_city_adjacent VALUES ('1307', '1402', 75);
INSERT INTO public.base_city_adjacent VALUES ('1402', '1307', 76);
INSERT INTO public.base_city_adjacent VALUES ('1307', '1509', 77);
INSERT INTO public.base_city_adjacent VALUES ('1509', '1307', 78);
INSERT INTO public.base_city_adjacent VALUES ('1307', '1525', 79);
INSERT INTO public.base_city_adjacent VALUES ('1525', '1307', 80);
INSERT INTO public.base_city_adjacent VALUES ('1308', '1504', 81);
INSERT INTO public.base_city_adjacent VALUES ('1504', '1308', 82);
INSERT INTO public.base_city_adjacent VALUES ('1308', '1525', 83);
INSERT INTO public.base_city_adjacent VALUES ('1525', '1308', 84);
INSERT INTO public.base_city_adjacent VALUES ('1308', '2113', 85);
INSERT INTO public.base_city_adjacent VALUES ('2113', '1308', 86);
INSERT INTO public.base_city_adjacent VALUES ('1309', '1310', 87);
INSERT INTO public.base_city_adjacent VALUES ('1310', '1309', 88);
INSERT INTO public.base_city_adjacent VALUES ('1309', '1311', 89);
INSERT INTO public.base_city_adjacent VALUES ('1311', '1309', 90);
INSERT INTO public.base_city_adjacent VALUES ('1309', '3714', 91);
INSERT INTO public.base_city_adjacent VALUES ('3714', '1309', 92);
INSERT INTO public.base_city_adjacent VALUES ('1309', '3716', 93);
INSERT INTO public.base_city_adjacent VALUES ('3716', '1309', 94);
INSERT INTO public.base_city_adjacent VALUES ('1311', '3714', 95);
INSERT INTO public.base_city_adjacent VALUES ('3714', '1311', 96);
INSERT INTO public.base_city_adjacent VALUES ('1401', '1403', 97);
INSERT INTO public.base_city_adjacent VALUES ('1403', '1401', 98);
INSERT INTO public.base_city_adjacent VALUES ('1401', '1407', 99);
INSERT INTO public.base_city_adjacent VALUES ('1407', '1401', 100);
INSERT INTO public.base_city_adjacent VALUES ('1401', '1409', 101);
INSERT INTO public.base_city_adjacent VALUES ('1409', '1401', 102);
INSERT INTO public.base_city_adjacent VALUES ('1401', '1411', 103);
INSERT INTO public.base_city_adjacent VALUES ('1411', '1401', 104);
INSERT INTO public.base_city_adjacent VALUES ('1402', '1406', 105);
INSERT INTO public.base_city_adjacent VALUES ('1406', '1402', 106);
INSERT INTO public.base_city_adjacent VALUES ('1402', '1409', 107);
INSERT INTO public.base_city_adjacent VALUES ('1409', '1402', 108);
INSERT INTO public.base_city_adjacent VALUES ('1402', '1509', 109);
INSERT INTO public.base_city_adjacent VALUES ('1509', '1402', 110);
INSERT INTO public.base_city_adjacent VALUES ('1403', '1407', 111);
INSERT INTO public.base_city_adjacent VALUES ('1407', '1403', 112);
INSERT INTO public.base_city_adjacent VALUES ('1403', '1409', 113);
INSERT INTO public.base_city_adjacent VALUES ('1409', '1403', 114);
INSERT INTO public.base_city_adjacent VALUES ('1404', '1405', 115);
INSERT INTO public.base_city_adjacent VALUES ('1405', '1404', 116);
INSERT INTO public.base_city_adjacent VALUES ('1404', '1407', 117);
INSERT INTO public.base_city_adjacent VALUES ('1407', '1404', 118);
INSERT INTO public.base_city_adjacent VALUES ('1404', '1410', 119);
INSERT INTO public.base_city_adjacent VALUES ('1410', '1404', 120);
INSERT INTO public.base_city_adjacent VALUES ('1404', '4105', 121);
INSERT INTO public.base_city_adjacent VALUES ('4105', '1404', 122);
INSERT INTO public.base_city_adjacent VALUES ('1404', '4107', 123);
INSERT INTO public.base_city_adjacent VALUES ('4107', '1404', 124);
INSERT INTO public.base_city_adjacent VALUES ('1405', '1408', 125);
INSERT INTO public.base_city_adjacent VALUES ('1408', '1405', 126);
INSERT INTO public.base_city_adjacent VALUES ('1405', '1410', 127);
INSERT INTO public.base_city_adjacent VALUES ('1410', '1405', 128);
INSERT INTO public.base_city_adjacent VALUES ('1405', '4107', 129);
INSERT INTO public.base_city_adjacent VALUES ('4107', '1405', 130);
INSERT INTO public.base_city_adjacent VALUES ('1405', '4108', 131);
INSERT INTO public.base_city_adjacent VALUES ('4108', '1405', 132);
INSERT INTO public.base_city_adjacent VALUES ('1405', '4190', 133);
INSERT INTO public.base_city_adjacent VALUES ('4190', '1405', 134);
INSERT INTO public.base_city_adjacent VALUES ('1406', '1409', 135);
INSERT INTO public.base_city_adjacent VALUES ('1409', '1406', 136);
INSERT INTO public.base_city_adjacent VALUES ('1406', '1501', 137);
INSERT INTO public.base_city_adjacent VALUES ('1501', '1406', 138);
INSERT INTO public.base_city_adjacent VALUES ('1406', '1509', 139);
INSERT INTO public.base_city_adjacent VALUES ('1509', '1406', 140);
INSERT INTO public.base_city_adjacent VALUES ('1407', '1410', 141);
INSERT INTO public.base_city_adjacent VALUES ('1410', '1407', 142);
INSERT INTO public.base_city_adjacent VALUES ('1407', '1411', 143);
INSERT INTO public.base_city_adjacent VALUES ('1411', '1407', 144);
INSERT INTO public.base_city_adjacent VALUES ('1408', '1410', 145);
INSERT INTO public.base_city_adjacent VALUES ('1410', '1408', 146);
INSERT INTO public.base_city_adjacent VALUES ('1408', '4103', 147);
INSERT INTO public.base_city_adjacent VALUES ('4103', '1408', 148);
INSERT INTO public.base_city_adjacent VALUES ('1408', '4112', 149);
INSERT INTO public.base_city_adjacent VALUES ('4112', '1408', 150);
INSERT INTO public.base_city_adjacent VALUES ('1408', '4190', 151);
INSERT INTO public.base_city_adjacent VALUES ('4190', '1408', 152);
INSERT INTO public.base_city_adjacent VALUES ('1408', '6105', 153);
INSERT INTO public.base_city_adjacent VALUES ('6105', '1408', 154);
INSERT INTO public.base_city_adjacent VALUES ('1409', '1411', 155);
INSERT INTO public.base_city_adjacent VALUES ('1411', '1409', 156);
INSERT INTO public.base_city_adjacent VALUES ('1409', '1501', 157);
INSERT INTO public.base_city_adjacent VALUES ('1501', '1409', 158);
INSERT INTO public.base_city_adjacent VALUES ('1409', '1506', 159);
INSERT INTO public.base_city_adjacent VALUES ('1506', '1409', 160);
INSERT INTO public.base_city_adjacent VALUES ('1409', '6108', 161);
INSERT INTO public.base_city_adjacent VALUES ('6108', '1409', 162);
INSERT INTO public.base_city_adjacent VALUES ('1410', '1411', 163);
INSERT INTO public.base_city_adjacent VALUES ('1411', '1410', 164);
INSERT INTO public.base_city_adjacent VALUES ('1410', '6105', 165);
INSERT INTO public.base_city_adjacent VALUES ('6105', '1410', 166);
INSERT INTO public.base_city_adjacent VALUES ('1410', '6106', 167);
INSERT INTO public.base_city_adjacent VALUES ('6106', '1410', 168);
INSERT INTO public.base_city_adjacent VALUES ('1411', '6106', 169);
INSERT INTO public.base_city_adjacent VALUES ('6106', '1411', 170);
INSERT INTO public.base_city_adjacent VALUES ('1411', '6108', 171);
INSERT INTO public.base_city_adjacent VALUES ('6108', '1411', 172);
INSERT INTO public.base_city_adjacent VALUES ('1501', '1502', 173);
INSERT INTO public.base_city_adjacent VALUES ('1502', '1501', 174);
INSERT INTO public.base_city_adjacent VALUES ('1501', '1506', 175);
INSERT INTO public.base_city_adjacent VALUES ('1506', '1501', 176);
INSERT INTO public.base_city_adjacent VALUES ('1501', '1509', 177);
INSERT INTO public.base_city_adjacent VALUES ('1509', '1501', 178);
INSERT INTO public.base_city_adjacent VALUES ('1502', '1506', 179);
INSERT INTO public.base_city_adjacent VALUES ('1506', '1502', 180);
INSERT INTO public.base_city_adjacent VALUES ('1502', '1508', 181);
INSERT INTO public.base_city_adjacent VALUES ('1508', '1502', 182);
INSERT INTO public.base_city_adjacent VALUES ('1502', '1509', 183);
INSERT INTO public.base_city_adjacent VALUES ('1509', '1502', 184);
INSERT INTO public.base_city_adjacent VALUES ('1503', '1506', 185);
INSERT INTO public.base_city_adjacent VALUES ('1506', '1503', 186);
INSERT INTO public.base_city_adjacent VALUES ('1503', '1529', 187);
INSERT INTO public.base_city_adjacent VALUES ('1529', '1503', 188);
INSERT INTO public.base_city_adjacent VALUES ('1503', '6402', 189);
INSERT INTO public.base_city_adjacent VALUES ('6402', '1503', 190);
INSERT INTO public.base_city_adjacent VALUES ('1504', '1505', 191);
INSERT INTO public.base_city_adjacent VALUES ('1505', '1504', 192);
INSERT INTO public.base_city_adjacent VALUES ('1504', '1525', 193);
INSERT INTO public.base_city_adjacent VALUES ('1525', '1504', 194);
INSERT INTO public.base_city_adjacent VALUES ('1504', '2113', 195);
INSERT INTO public.base_city_adjacent VALUES ('2113', '1504', 196);
INSERT INTO public.base_city_adjacent VALUES ('1505', '1522', 197);
INSERT INTO public.base_city_adjacent VALUES ('1522', '1505', 198);
INSERT INTO public.base_city_adjacent VALUES ('1505', '1525', 199);
INSERT INTO public.base_city_adjacent VALUES ('1525', '1505', 200);
INSERT INTO public.base_city_adjacent VALUES ('1505', '2101', 201);
INSERT INTO public.base_city_adjacent VALUES ('2101', '1505', 202);
INSERT INTO public.base_city_adjacent VALUES ('1505', '2109', 203);
INSERT INTO public.base_city_adjacent VALUES ('2109', '1505', 204);
INSERT INTO public.base_city_adjacent VALUES ('1505', '2112', 205);
INSERT INTO public.base_city_adjacent VALUES ('2112', '1505', 206);
INSERT INTO public.base_city_adjacent VALUES ('1505', '2113', 207);
INSERT INTO public.base_city_adjacent VALUES ('2113', '1505', 208);
INSERT INTO public.base_city_adjacent VALUES ('1505', '2203', 209);
INSERT INTO public.base_city_adjacent VALUES ('2203', '1505', 210);
INSERT INTO public.base_city_adjacent VALUES ('1505', '2207', 211);
INSERT INTO public.base_city_adjacent VALUES ('2207', '1505', 212);
INSERT INTO public.base_city_adjacent VALUES ('1505', '2208', 213);
INSERT INTO public.base_city_adjacent VALUES ('2208', '1505', 214);
INSERT INTO public.base_city_adjacent VALUES ('1506', '1508', 215);
INSERT INTO public.base_city_adjacent VALUES ('1508', '1506', 216);
INSERT INTO public.base_city_adjacent VALUES ('1506', '1529', 217);
INSERT INTO public.base_city_adjacent VALUES ('1529', '1506', 218);
INSERT INTO public.base_city_adjacent VALUES ('1506', '6108', 219);
INSERT INTO public.base_city_adjacent VALUES ('6108', '1506', 220);
INSERT INTO public.base_city_adjacent VALUES ('1506', '6401', 221);
INSERT INTO public.base_city_adjacent VALUES ('6401', '1506', 222);
INSERT INTO public.base_city_adjacent VALUES ('1506', '6402', 223);
INSERT INTO public.base_city_adjacent VALUES ('6402', '1506', 224);
INSERT INTO public.base_city_adjacent VALUES ('1506', '6403', 225);
INSERT INTO public.base_city_adjacent VALUES ('6403', '1506', 226);
INSERT INTO public.base_city_adjacent VALUES ('1507', '1522', 227);
INSERT INTO public.base_city_adjacent VALUES ('1522', '1507', 228);
INSERT INTO public.base_city_adjacent VALUES ('1507', '2302', 229);
INSERT INTO public.base_city_adjacent VALUES ('2302', '1507', 230);
INSERT INTO public.base_city_adjacent VALUES ('1507', '2311', 231);
INSERT INTO public.base_city_adjacent VALUES ('2311', '1507', 232);
INSERT INTO public.base_city_adjacent VALUES ('1507', '2327', 233);
INSERT INTO public.base_city_adjacent VALUES ('2327', '1507', 234);
INSERT INTO public.base_city_adjacent VALUES ('1508', '1529', 235);
INSERT INTO public.base_city_adjacent VALUES ('1529', '1508', 236);
INSERT INTO public.base_city_adjacent VALUES ('1509', '1525', 237);
INSERT INTO public.base_city_adjacent VALUES ('1525', '1509', 238);
INSERT INTO public.base_city_adjacent VALUES ('1522', '1525', 239);
INSERT INTO public.base_city_adjacent VALUES ('1525', '1522', 240);
INSERT INTO public.base_city_adjacent VALUES ('1522', '2208', 241);
INSERT INTO public.base_city_adjacent VALUES ('2208', '1522', 242);
INSERT INTO public.base_city_adjacent VALUES ('1522', '2302', 243);
INSERT INTO public.base_city_adjacent VALUES ('2302', '1522', 244);
INSERT INTO public.base_city_adjacent VALUES ('1529', '6203', 245);
INSERT INTO public.base_city_adjacent VALUES ('6203', '1529', 246);
INSERT INTO public.base_city_adjacent VALUES ('1529', '6204', 247);
INSERT INTO public.base_city_adjacent VALUES ('6204', '1529', 248);
INSERT INTO public.base_city_adjacent VALUES ('1529', '6206', 249);
INSERT INTO public.base_city_adjacent VALUES ('6206', '1529', 250);
INSERT INTO public.base_city_adjacent VALUES ('1529', '6207', 251);
INSERT INTO public.base_city_adjacent VALUES ('6207', '1529', 252);
INSERT INTO public.base_city_adjacent VALUES ('1529', '6209', 253);
INSERT INTO public.base_city_adjacent VALUES ('6209', '1529', 254);
INSERT INTO public.base_city_adjacent VALUES ('1529', '6401', 255);
INSERT INTO public.base_city_adjacent VALUES ('6401', '1529', 256);
INSERT INTO public.base_city_adjacent VALUES ('1529', '6402', 257);
INSERT INTO public.base_city_adjacent VALUES ('6402', '1529', 258);
INSERT INTO public.base_city_adjacent VALUES ('1529', '6403', 259);
INSERT INTO public.base_city_adjacent VALUES ('6403', '1529', 260);
INSERT INTO public.base_city_adjacent VALUES ('1529', '6405', 261);
INSERT INTO public.base_city_adjacent VALUES ('6405', '1529', 262);
INSERT INTO public.base_city_adjacent VALUES ('2101', '2103', 263);
INSERT INTO public.base_city_adjacent VALUES ('2103', '2101', 264);
INSERT INTO public.base_city_adjacent VALUES ('2101', '2104', 265);
INSERT INTO public.base_city_adjacent VALUES ('2104', '2101', 266);
INSERT INTO public.base_city_adjacent VALUES ('2101', '2105', 267);
INSERT INTO public.base_city_adjacent VALUES ('2105', '2101', 268);
INSERT INTO public.base_city_adjacent VALUES ('2101', '2107', 269);
INSERT INTO public.base_city_adjacent VALUES ('2107', '2101', 270);
INSERT INTO public.base_city_adjacent VALUES ('2101', '2109', 271);
INSERT INTO public.base_city_adjacent VALUES ('2109', '2101', 272);
INSERT INTO public.base_city_adjacent VALUES ('2101', '2110', 273);
INSERT INTO public.base_city_adjacent VALUES ('2110', '2101', 274);
INSERT INTO public.base_city_adjacent VALUES ('2101', '2112', 275);
INSERT INTO public.base_city_adjacent VALUES ('2112', '2101', 276);
INSERT INTO public.base_city_adjacent VALUES ('2102', '2103', 277);
INSERT INTO public.base_city_adjacent VALUES ('2103', '2102', 278);
INSERT INTO public.base_city_adjacent VALUES ('2102', '2106', 279);
INSERT INTO public.base_city_adjacent VALUES ('2106', '2102', 280);
INSERT INTO public.base_city_adjacent VALUES ('2102', '2108', 281);
INSERT INTO public.base_city_adjacent VALUES ('2108', '2102', 282);
INSERT INTO public.base_city_adjacent VALUES ('2103', '2106', 283);
INSERT INTO public.base_city_adjacent VALUES ('2106', '2103', 284);
INSERT INTO public.base_city_adjacent VALUES ('2103', '2107', 285);
INSERT INTO public.base_city_adjacent VALUES ('2107', '2103', 286);
INSERT INTO public.base_city_adjacent VALUES ('2103', '2108', 287);
INSERT INTO public.base_city_adjacent VALUES ('2108', '2103', 288);
INSERT INTO public.base_city_adjacent VALUES ('2103', '2110', 289);
INSERT INTO public.base_city_adjacent VALUES ('2110', '2103', 290);
INSERT INTO public.base_city_adjacent VALUES ('2103', '2111', 291);
INSERT INTO public.base_city_adjacent VALUES ('2111', '2103', 292);
INSERT INTO public.base_city_adjacent VALUES ('2104', '2105', 293);
INSERT INTO public.base_city_adjacent VALUES ('2105', '2104', 294);
INSERT INTO public.base_city_adjacent VALUES ('2104', '2112', 295);
INSERT INTO public.base_city_adjacent VALUES ('2112', '2104', 296);
INSERT INTO public.base_city_adjacent VALUES ('2104', '2204', 297);
INSERT INTO public.base_city_adjacent VALUES ('2204', '2104', 298);
INSERT INTO public.base_city_adjacent VALUES ('2104', '2205', 299);
INSERT INTO public.base_city_adjacent VALUES ('2205', '2104', 300);
INSERT INTO public.base_city_adjacent VALUES ('2105', '2106', 301);
INSERT INTO public.base_city_adjacent VALUES ('2106', '2105', 302);
INSERT INTO public.base_city_adjacent VALUES ('2105', '2110', 303);
INSERT INTO public.base_city_adjacent VALUES ('2110', '2105', 304);
INSERT INTO public.base_city_adjacent VALUES ('2105', '2205', 305);
INSERT INTO public.base_city_adjacent VALUES ('2205', '2105', 306);
INSERT INTO public.base_city_adjacent VALUES ('2106', '2110', 307);
INSERT INTO public.base_city_adjacent VALUES ('2110', '2106', 308);
INSERT INTO public.base_city_adjacent VALUES ('2106', '2205', 309);
INSERT INTO public.base_city_adjacent VALUES ('2205', '2106', 310);
INSERT INTO public.base_city_adjacent VALUES ('2107', '2109', 311);
INSERT INTO public.base_city_adjacent VALUES ('2109', '2107', 312);
INSERT INTO public.base_city_adjacent VALUES ('2107', '2111', 313);
INSERT INTO public.base_city_adjacent VALUES ('2111', '2107', 314);
INSERT INTO public.base_city_adjacent VALUES ('2107', '2113', 315);
INSERT INTO public.base_city_adjacent VALUES ('2113', '2107', 316);
INSERT INTO public.base_city_adjacent VALUES ('2107', '2114', 317);
INSERT INTO public.base_city_adjacent VALUES ('2114', '2107', 318);
INSERT INTO public.base_city_adjacent VALUES ('2108', '2111', 319);
INSERT INTO public.base_city_adjacent VALUES ('2111', '2108', 320);
INSERT INTO public.base_city_adjacent VALUES ('2109', '2113', 321);
INSERT INTO public.base_city_adjacent VALUES ('2113', '2109', 322);
INSERT INTO public.base_city_adjacent VALUES ('2112', '2203', 323);
INSERT INTO public.base_city_adjacent VALUES ('2203', '2112', 324);
INSERT INTO public.base_city_adjacent VALUES ('2112', '2204', 325);
INSERT INTO public.base_city_adjacent VALUES ('2204', '2112', 326);
INSERT INTO public.base_city_adjacent VALUES ('2113', '2114', 327);
INSERT INTO public.base_city_adjacent VALUES ('2114', '2113', 328);
INSERT INTO public.base_city_adjacent VALUES ('2201', '2202', 329);
INSERT INTO public.base_city_adjacent VALUES ('2202', '2201', 330);
INSERT INTO public.base_city_adjacent VALUES ('2201', '2203', 331);
INSERT INTO public.base_city_adjacent VALUES ('2203', '2201', 332);
INSERT INTO public.base_city_adjacent VALUES ('2201', '2207', 333);
INSERT INTO public.base_city_adjacent VALUES ('2207', '2201', 334);
INSERT INTO public.base_city_adjacent VALUES ('2201', '2301', 335);
INSERT INTO public.base_city_adjacent VALUES ('2301', '2201', 336);
INSERT INTO public.base_city_adjacent VALUES ('2202', '2203', 337);
INSERT INTO public.base_city_adjacent VALUES ('2203', '2202', 338);
INSERT INTO public.base_city_adjacent VALUES ('2202', '2204', 339);
INSERT INTO public.base_city_adjacent VALUES ('2204', '2202', 340);
INSERT INTO public.base_city_adjacent VALUES ('2202', '2205', 341);
INSERT INTO public.base_city_adjacent VALUES ('2205', '2202', 342);
INSERT INTO public.base_city_adjacent VALUES ('2202', '2206', 343);
INSERT INTO public.base_city_adjacent VALUES ('2206', '2202', 344);
INSERT INTO public.base_city_adjacent VALUES ('2202', '2224', 345);
INSERT INTO public.base_city_adjacent VALUES ('2224', '2202', 346);
INSERT INTO public.base_city_adjacent VALUES ('2202', '2301', 347);
INSERT INTO public.base_city_adjacent VALUES ('2301', '2202', 348);
INSERT INTO public.base_city_adjacent VALUES ('2203', '2204', 349);
INSERT INTO public.base_city_adjacent VALUES ('2204', '2203', 350);
INSERT INTO public.base_city_adjacent VALUES ('2203', '2207', 351);
INSERT INTO public.base_city_adjacent VALUES ('2207', '2203', 352);
INSERT INTO public.base_city_adjacent VALUES ('2204', '2205', 353);
INSERT INTO public.base_city_adjacent VALUES ('2205', '2204', 354);
INSERT INTO public.base_city_adjacent VALUES ('2205', '2206', 355);
INSERT INTO public.base_city_adjacent VALUES ('2206', '2205', 356);
INSERT INTO public.base_city_adjacent VALUES ('2206', '2224', 357);
INSERT INTO public.base_city_adjacent VALUES ('2224', '2206', 358);
INSERT INTO public.base_city_adjacent VALUES ('2207', '2208', 359);
INSERT INTO public.base_city_adjacent VALUES ('2208', '2207', 360);
INSERT INTO public.base_city_adjacent VALUES ('2207', '2301', 361);
INSERT INTO public.base_city_adjacent VALUES ('2301', '2207', 362);
INSERT INTO public.base_city_adjacent VALUES ('2207', '2306', 363);
INSERT INTO public.base_city_adjacent VALUES ('2306', '2207', 364);
INSERT INTO public.base_city_adjacent VALUES ('2208', '2302', 365);
INSERT INTO public.base_city_adjacent VALUES ('2302', '2208', 366);
INSERT INTO public.base_city_adjacent VALUES ('2208', '2306', 367);
INSERT INTO public.base_city_adjacent VALUES ('2306', '2208', 368);
INSERT INTO public.base_city_adjacent VALUES ('2224', '2301', 369);
INSERT INTO public.base_city_adjacent VALUES ('2301', '2224', 370);
INSERT INTO public.base_city_adjacent VALUES ('2224', '2310', 371);
INSERT INTO public.base_city_adjacent VALUES ('2310', '2224', 372);
INSERT INTO public.base_city_adjacent VALUES ('2301', '2306', 373);
INSERT INTO public.base_city_adjacent VALUES ('2306', '2301', 374);
INSERT INTO public.base_city_adjacent VALUES ('2301', '2307', 375);
INSERT INTO public.base_city_adjacent VALUES ('2307', '2301', 376);
INSERT INTO public.base_city_adjacent VALUES ('2301', '2308', 377);
INSERT INTO public.base_city_adjacent VALUES ('2308', '2301', 378);
INSERT INTO public.base_city_adjacent VALUES ('2301', '2309', 379);
INSERT INTO public.base_city_adjacent VALUES ('2309', '2301', 380);
INSERT INTO public.base_city_adjacent VALUES ('2301', '2310', 381);
INSERT INTO public.base_city_adjacent VALUES ('2310', '2301', 382);
INSERT INTO public.base_city_adjacent VALUES ('2301', '2312', 383);
INSERT INTO public.base_city_adjacent VALUES ('2312', '2301', 384);
INSERT INTO public.base_city_adjacent VALUES ('2302', '2306', 385);
INSERT INTO public.base_city_adjacent VALUES ('2306', '2302', 386);
INSERT INTO public.base_city_adjacent VALUES ('2302', '2311', 387);
INSERT INTO public.base_city_adjacent VALUES ('2311', '2302', 388);
INSERT INTO public.base_city_adjacent VALUES ('2302', '2312', 389);
INSERT INTO public.base_city_adjacent VALUES ('2312', '2302', 390);
INSERT INTO public.base_city_adjacent VALUES ('2303', '2305', 391);
INSERT INTO public.base_city_adjacent VALUES ('2305', '2303', 392);
INSERT INTO public.base_city_adjacent VALUES ('2303', '2309', 393);
INSERT INTO public.base_city_adjacent VALUES ('2309', '2303', 394);
INSERT INTO public.base_city_adjacent VALUES ('2303', '2310', 395);
INSERT INTO public.base_city_adjacent VALUES ('2310', '2303', 396);
INSERT INTO public.base_city_adjacent VALUES ('2304', '2307', 397);
INSERT INTO public.base_city_adjacent VALUES ('2307', '2304', 398);
INSERT INTO public.base_city_adjacent VALUES ('2304', '2308', 399);
INSERT INTO public.base_city_adjacent VALUES ('2308', '2304', 400);
INSERT INTO public.base_city_adjacent VALUES ('2305', '2308', 401);
INSERT INTO public.base_city_adjacent VALUES ('2308', '2305', 402);
INSERT INTO public.base_city_adjacent VALUES ('2305', '2309', 403);
INSERT INTO public.base_city_adjacent VALUES ('2309', '2305', 404);
INSERT INTO public.base_city_adjacent VALUES ('2306', '2312', 405);
INSERT INTO public.base_city_adjacent VALUES ('2312', '2306', 406);
INSERT INTO public.base_city_adjacent VALUES ('2307', '2308', 407);
INSERT INTO public.base_city_adjacent VALUES ('2308', '2307', 408);
INSERT INTO public.base_city_adjacent VALUES ('2307', '2311', 409);
INSERT INTO public.base_city_adjacent VALUES ('2311', '2307', 410);
INSERT INTO public.base_city_adjacent VALUES ('2307', '2312', 411);
INSERT INTO public.base_city_adjacent VALUES ('2312', '2307', 412);
INSERT INTO public.base_city_adjacent VALUES ('2308', '2309', 413);
INSERT INTO public.base_city_adjacent VALUES ('2309', '2308', 414);
INSERT INTO public.base_city_adjacent VALUES ('2309', '2310', 415);
INSERT INTO public.base_city_adjacent VALUES ('2310', '2309', 416);
INSERT INTO public.base_city_adjacent VALUES ('2311', '2312', 417);
INSERT INTO public.base_city_adjacent VALUES ('2312', '2311', 418);
INSERT INTO public.base_city_adjacent VALUES ('2311', '2327', 419);
INSERT INTO public.base_city_adjacent VALUES ('2327', '2311', 420);
INSERT INTO public.base_city_adjacent VALUES ('3101', '3205', 421);
INSERT INTO public.base_city_adjacent VALUES ('3205', '3101', 422);
INSERT INTO public.base_city_adjacent VALUES ('3101', '3206', 423);
INSERT INTO public.base_city_adjacent VALUES ('3206', '3101', 424);
INSERT INTO public.base_city_adjacent VALUES ('3101', '3304', 425);
INSERT INTO public.base_city_adjacent VALUES ('3304', '3101', 426);
INSERT INTO public.base_city_adjacent VALUES ('3101', '3309', 427);
INSERT INTO public.base_city_adjacent VALUES ('3309', '3101', 428);
INSERT INTO public.base_city_adjacent VALUES ('3201', '3204', 429);
INSERT INTO public.base_city_adjacent VALUES ('3204', '3201', 430);
INSERT INTO public.base_city_adjacent VALUES ('3201', '3210', 431);
INSERT INTO public.base_city_adjacent VALUES ('3210', '3201', 432);
INSERT INTO public.base_city_adjacent VALUES ('3201', '3211', 433);
INSERT INTO public.base_city_adjacent VALUES ('3211', '3201', 434);
INSERT INTO public.base_city_adjacent VALUES ('3201', '3405', 435);
INSERT INTO public.base_city_adjacent VALUES ('3405', '3201', 436);
INSERT INTO public.base_city_adjacent VALUES ('3201', '3411', 437);
INSERT INTO public.base_city_adjacent VALUES ('3411', '3201', 438);
INSERT INTO public.base_city_adjacent VALUES ('3201', '3418', 439);
INSERT INTO public.base_city_adjacent VALUES ('3418', '3201', 440);
INSERT INTO public.base_city_adjacent VALUES ('3202', '3204', 441);
INSERT INTO public.base_city_adjacent VALUES ('3204', '3202', 442);
INSERT INTO public.base_city_adjacent VALUES ('3202', '3205', 443);
INSERT INTO public.base_city_adjacent VALUES ('3205', '3202', 444);
INSERT INTO public.base_city_adjacent VALUES ('3202', '3212', 445);
INSERT INTO public.base_city_adjacent VALUES ('3212', '3202', 446);
INSERT INTO public.base_city_adjacent VALUES ('3202', '3305', 447);
INSERT INTO public.base_city_adjacent VALUES ('3305', '3202', 448);
INSERT INTO public.base_city_adjacent VALUES ('3202', '3418', 449);
INSERT INTO public.base_city_adjacent VALUES ('3418', '3202', 450);
INSERT INTO public.base_city_adjacent VALUES ('3203', '3207', 451);
INSERT INTO public.base_city_adjacent VALUES ('3207', '3203', 452);
INSERT INTO public.base_city_adjacent VALUES ('3203', '3213', 453);
INSERT INTO public.base_city_adjacent VALUES ('3213', '3203', 454);
INSERT INTO public.base_city_adjacent VALUES ('3203', '3406', 455);
INSERT INTO public.base_city_adjacent VALUES ('3406', '3203', 456);
INSERT INTO public.base_city_adjacent VALUES ('3203', '3413', 457);
INSERT INTO public.base_city_adjacent VALUES ('3413', '3203', 458);
INSERT INTO public.base_city_adjacent VALUES ('3203', '3704', 459);
INSERT INTO public.base_city_adjacent VALUES ('3704', '3203', 460);
INSERT INTO public.base_city_adjacent VALUES ('3203', '3708', 461);
INSERT INTO public.base_city_adjacent VALUES ('3708', '3203', 462);
INSERT INTO public.base_city_adjacent VALUES ('3203', '3713', 463);
INSERT INTO public.base_city_adjacent VALUES ('3713', '3203', 464);
INSERT INTO public.base_city_adjacent VALUES ('3203', '3717', 465);
INSERT INTO public.base_city_adjacent VALUES ('3717', '3203', 466);
INSERT INTO public.base_city_adjacent VALUES ('3204', '3211', 467);
INSERT INTO public.base_city_adjacent VALUES ('3211', '3204', 468);
INSERT INTO public.base_city_adjacent VALUES ('3204', '3212', 469);
INSERT INTO public.base_city_adjacent VALUES ('3212', '3204', 470);
INSERT INTO public.base_city_adjacent VALUES ('3204', '3418', 471);
INSERT INTO public.base_city_adjacent VALUES ('3418', '3204', 472);
INSERT INTO public.base_city_adjacent VALUES ('3205', '3206', 473);
INSERT INTO public.base_city_adjacent VALUES ('3206', '3205', 474);
INSERT INTO public.base_city_adjacent VALUES ('3205', '3212', 475);
INSERT INTO public.base_city_adjacent VALUES ('3212', '3205', 476);
INSERT INTO public.base_city_adjacent VALUES ('3205', '3304', 477);
INSERT INTO public.base_city_adjacent VALUES ('3304', '3205', 478);
INSERT INTO public.base_city_adjacent VALUES ('3205', '3305', 479);
INSERT INTO public.base_city_adjacent VALUES ('3305', '3205', 480);
INSERT INTO public.base_city_adjacent VALUES ('3206', '3209', 481);
INSERT INTO public.base_city_adjacent VALUES ('3209', '3206', 482);
INSERT INTO public.base_city_adjacent VALUES ('3206', '3212', 483);
INSERT INTO public.base_city_adjacent VALUES ('3212', '3206', 484);
INSERT INTO public.base_city_adjacent VALUES ('3207', '3208', 485);
INSERT INTO public.base_city_adjacent VALUES ('3208', '3207', 486);
INSERT INTO public.base_city_adjacent VALUES ('3207', '3209', 487);
INSERT INTO public.base_city_adjacent VALUES ('3209', '3207', 488);
INSERT INTO public.base_city_adjacent VALUES ('3207', '3213', 489);
INSERT INTO public.base_city_adjacent VALUES ('3213', '3207', 490);
INSERT INTO public.base_city_adjacent VALUES ('3207', '3711', 491);
INSERT INTO public.base_city_adjacent VALUES ('3711', '3207', 492);
INSERT INTO public.base_city_adjacent VALUES ('3207', '3713', 493);
INSERT INTO public.base_city_adjacent VALUES ('3713', '3207', 494);
INSERT INTO public.base_city_adjacent VALUES ('3208', '3209', 495);
INSERT INTO public.base_city_adjacent VALUES ('3209', '3208', 496);
INSERT INTO public.base_city_adjacent VALUES ('3208', '3210', 497);
INSERT INTO public.base_city_adjacent VALUES ('3210', '3208', 498);
INSERT INTO public.base_city_adjacent VALUES ('3208', '3213', 499);
INSERT INTO public.base_city_adjacent VALUES ('3213', '3208', 500);
INSERT INTO public.base_city_adjacent VALUES ('3208', '3411', 501);
INSERT INTO public.base_city_adjacent VALUES ('3411', '3208', 502);
INSERT INTO public.base_city_adjacent VALUES ('3209', '3210', 503);
INSERT INTO public.base_city_adjacent VALUES ('3210', '3209', 504);
INSERT INTO public.base_city_adjacent VALUES ('3209', '3212', 505);
INSERT INTO public.base_city_adjacent VALUES ('3212', '3209', 506);
INSERT INTO public.base_city_adjacent VALUES ('3210', '3211', 507);
INSERT INTO public.base_city_adjacent VALUES ('3211', '3210', 508);
INSERT INTO public.base_city_adjacent VALUES ('3210', '3212', 509);
INSERT INTO public.base_city_adjacent VALUES ('3212', '3210', 510);
INSERT INTO public.base_city_adjacent VALUES ('3210', '3411', 511);
INSERT INTO public.base_city_adjacent VALUES ('3411', '3210', 512);
INSERT INTO public.base_city_adjacent VALUES ('3211', '3212', 513);
INSERT INTO public.base_city_adjacent VALUES ('3212', '3211', 514);
INSERT INTO public.base_city_adjacent VALUES ('3213', '3403', 515);
INSERT INTO public.base_city_adjacent VALUES ('3403', '3213', 516);
INSERT INTO public.base_city_adjacent VALUES ('3213', '3411', 517);
INSERT INTO public.base_city_adjacent VALUES ('3411', '3213', 518);
INSERT INTO public.base_city_adjacent VALUES ('3213', '3413', 519);
INSERT INTO public.base_city_adjacent VALUES ('3413', '3213', 520);
INSERT INTO public.base_city_adjacent VALUES ('3301', '3304', 521);
INSERT INTO public.base_city_adjacent VALUES ('3304', '3301', 522);
INSERT INTO public.base_city_adjacent VALUES ('3301', '3305', 523);
INSERT INTO public.base_city_adjacent VALUES ('3305', '3301', 524);
INSERT INTO public.base_city_adjacent VALUES ('3301', '3306', 525);
INSERT INTO public.base_city_adjacent VALUES ('3306', '3301', 526);
INSERT INTO public.base_city_adjacent VALUES ('3301', '3307', 527);
INSERT INTO public.base_city_adjacent VALUES ('3307', '3301', 528);
INSERT INTO public.base_city_adjacent VALUES ('3301', '3308', 529);
INSERT INTO public.base_city_adjacent VALUES ('3308', '3301', 530);
INSERT INTO public.base_city_adjacent VALUES ('3301', '3410', 531);
INSERT INTO public.base_city_adjacent VALUES ('3410', '3301', 532);
INSERT INTO public.base_city_adjacent VALUES ('3301', '3418', 533);
INSERT INTO public.base_city_adjacent VALUES ('3418', '3301', 534);
INSERT INTO public.base_city_adjacent VALUES ('3302', '3304', 535);
INSERT INTO public.base_city_adjacent VALUES ('3304', '3302', 536);
INSERT INTO public.base_city_adjacent VALUES ('3302', '3306', 537);
INSERT INTO public.base_city_adjacent VALUES ('3306', '3302', 538);
INSERT INTO public.base_city_adjacent VALUES ('3302', '3310', 539);
INSERT INTO public.base_city_adjacent VALUES ('3310', '3302', 540);
INSERT INTO public.base_city_adjacent VALUES ('3303', '3310', 541);
INSERT INTO public.base_city_adjacent VALUES ('3310', '3303', 542);
INSERT INTO public.base_city_adjacent VALUES ('3303', '3311', 543);
INSERT INTO public.base_city_adjacent VALUES ('3311', '3303', 544);
INSERT INTO public.base_city_adjacent VALUES ('3303', '3509', 545);
INSERT INTO public.base_city_adjacent VALUES ('3509', '3303', 546);
INSERT INTO public.base_city_adjacent VALUES ('3304', '3305', 547);
INSERT INTO public.base_city_adjacent VALUES ('3305', '3304', 548);
INSERT INTO public.base_city_adjacent VALUES ('3304', '3306', 549);
INSERT INTO public.base_city_adjacent VALUES ('3306', '3304', 550);
INSERT INTO public.base_city_adjacent VALUES ('3305', '3418', 551);
INSERT INTO public.base_city_adjacent VALUES ('3418', '3305', 552);
INSERT INTO public.base_city_adjacent VALUES ('3306', '3307', 553);
INSERT INTO public.base_city_adjacent VALUES ('3307', '3306', 554);
INSERT INTO public.base_city_adjacent VALUES ('3306', '3310', 555);
INSERT INTO public.base_city_adjacent VALUES ('3310', '3306', 556);
INSERT INTO public.base_city_adjacent VALUES ('3307', '3308', 557);
INSERT INTO public.base_city_adjacent VALUES ('3308', '3307', 558);
INSERT INTO public.base_city_adjacent VALUES ('3307', '3310', 559);
INSERT INTO public.base_city_adjacent VALUES ('3310', '3307', 560);
INSERT INTO public.base_city_adjacent VALUES ('3307', '3311', 561);
INSERT INTO public.base_city_adjacent VALUES ('3311', '3307', 562);
INSERT INTO public.base_city_adjacent VALUES ('3308', '3311', 563);
INSERT INTO public.base_city_adjacent VALUES ('3311', '3308', 564);
INSERT INTO public.base_city_adjacent VALUES ('3308', '3410', 565);
INSERT INTO public.base_city_adjacent VALUES ('3410', '3308', 566);
INSERT INTO public.base_city_adjacent VALUES ('3308', '3507', 567);
INSERT INTO public.base_city_adjacent VALUES ('3507', '3308', 568);
INSERT INTO public.base_city_adjacent VALUES ('3308', '3611', 569);
INSERT INTO public.base_city_adjacent VALUES ('3611', '3308', 570);
INSERT INTO public.base_city_adjacent VALUES ('3310', '3311', 571);
INSERT INTO public.base_city_adjacent VALUES ('3311', '3310', 572);
INSERT INTO public.base_city_adjacent VALUES ('3311', '3507', 573);
INSERT INTO public.base_city_adjacent VALUES ('3507', '3311', 574);
INSERT INTO public.base_city_adjacent VALUES ('3311', '3509', 575);
INSERT INTO public.base_city_adjacent VALUES ('3509', '3311', 576);
INSERT INTO public.base_city_adjacent VALUES ('3401', '3402', 577);
INSERT INTO public.base_city_adjacent VALUES ('3402', '3401', 578);
INSERT INTO public.base_city_adjacent VALUES ('3401', '3404', 579);
INSERT INTO public.base_city_adjacent VALUES ('3404', '3401', 580);
INSERT INTO public.base_city_adjacent VALUES ('3401', '3405', 581);
INSERT INTO public.base_city_adjacent VALUES ('3405', '3401', 582);
INSERT INTO public.base_city_adjacent VALUES ('3401', '3407', 583);
INSERT INTO public.base_city_adjacent VALUES ('3407', '3401', 584);
INSERT INTO public.base_city_adjacent VALUES ('3401', '3408', 585);
INSERT INTO public.base_city_adjacent VALUES ('3408', '3401', 586);
INSERT INTO public.base_city_adjacent VALUES ('3401', '3411', 587);
INSERT INTO public.base_city_adjacent VALUES ('3411', '3401', 588);
INSERT INTO public.base_city_adjacent VALUES ('3401', '3415', 589);
INSERT INTO public.base_city_adjacent VALUES ('3415', '3401', 590);
INSERT INTO public.base_city_adjacent VALUES ('3402', '3405', 591);
INSERT INTO public.base_city_adjacent VALUES ('3405', '3402', 592);
INSERT INTO public.base_city_adjacent VALUES ('3402', '3407', 593);
INSERT INTO public.base_city_adjacent VALUES ('3407', '3402', 594);
INSERT INTO public.base_city_adjacent VALUES ('3402', '3417', 595);
INSERT INTO public.base_city_adjacent VALUES ('3417', '3402', 596);
INSERT INTO public.base_city_adjacent VALUES ('3402', '3418', 597);
INSERT INTO public.base_city_adjacent VALUES ('3418', '3402', 598);
INSERT INTO public.base_city_adjacent VALUES ('3403', '3404', 599);
INSERT INTO public.base_city_adjacent VALUES ('3404', '3403', 600);
INSERT INTO public.base_city_adjacent VALUES ('3403', '3406', 601);
INSERT INTO public.base_city_adjacent VALUES ('3406', '3403', 602);
INSERT INTO public.base_city_adjacent VALUES ('3403', '3411', 603);
INSERT INTO public.base_city_adjacent VALUES ('3411', '3403', 604);
INSERT INTO public.base_city_adjacent VALUES ('3403', '3413', 605);
INSERT INTO public.base_city_adjacent VALUES ('3413', '3403', 606);
INSERT INTO public.base_city_adjacent VALUES ('3403', '3416', 607);
INSERT INTO public.base_city_adjacent VALUES ('3416', '3403', 608);
INSERT INTO public.base_city_adjacent VALUES ('3404', '3411', 609);
INSERT INTO public.base_city_adjacent VALUES ('3411', '3404', 610);
INSERT INTO public.base_city_adjacent VALUES ('3404', '3412', 611);
INSERT INTO public.base_city_adjacent VALUES ('3412', '3404', 612);
INSERT INTO public.base_city_adjacent VALUES ('3404', '3415', 613);
INSERT INTO public.base_city_adjacent VALUES ('3415', '3404', 614);
INSERT INTO public.base_city_adjacent VALUES ('3404', '3416', 615);
INSERT INTO public.base_city_adjacent VALUES ('3416', '3404', 616);
INSERT INTO public.base_city_adjacent VALUES ('3405', '3411', 617);
INSERT INTO public.base_city_adjacent VALUES ('3411', '3405', 618);
INSERT INTO public.base_city_adjacent VALUES ('3405', '3418', 619);
INSERT INTO public.base_city_adjacent VALUES ('3418', '3405', 620);
INSERT INTO public.base_city_adjacent VALUES ('3406', '3413', 621);
INSERT INTO public.base_city_adjacent VALUES ('3413', '3406', 622);
INSERT INTO public.base_city_adjacent VALUES ('3406', '3416', 623);
INSERT INTO public.base_city_adjacent VALUES ('3416', '3406', 624);
INSERT INTO public.base_city_adjacent VALUES ('3406', '4114', 625);
INSERT INTO public.base_city_adjacent VALUES ('4114', '3406', 626);
INSERT INTO public.base_city_adjacent VALUES ('3407', '3408', 627);
INSERT INTO public.base_city_adjacent VALUES ('3408', '3407', 628);
INSERT INTO public.base_city_adjacent VALUES ('3407', '3417', 629);
INSERT INTO public.base_city_adjacent VALUES ('3417', '3407', 630);
INSERT INTO public.base_city_adjacent VALUES ('3408', '3415', 631);
INSERT INTO public.base_city_adjacent VALUES ('3415', '3408', 632);
INSERT INTO public.base_city_adjacent VALUES ('3408', '3417', 633);
INSERT INTO public.base_city_adjacent VALUES ('3417', '3408', 634);
INSERT INTO public.base_city_adjacent VALUES ('3408', '3604', 635);
INSERT INTO public.base_city_adjacent VALUES ('3604', '3408', 636);
INSERT INTO public.base_city_adjacent VALUES ('3408', '4211', 637);
INSERT INTO public.base_city_adjacent VALUES ('4211', '3408', 638);
INSERT INTO public.base_city_adjacent VALUES ('3410', '3417', 639);
INSERT INTO public.base_city_adjacent VALUES ('3417', '3410', 640);
INSERT INTO public.base_city_adjacent VALUES ('3410', '3418', 641);
INSERT INTO public.base_city_adjacent VALUES ('3418', '3410', 642);
INSERT INTO public.base_city_adjacent VALUES ('3410', '3602', 643);
INSERT INTO public.base_city_adjacent VALUES ('3602', '3410', 644);
INSERT INTO public.base_city_adjacent VALUES ('3410', '3611', 645);
INSERT INTO public.base_city_adjacent VALUES ('3611', '3410', 646);
INSERT INTO public.base_city_adjacent VALUES ('3412', '3415', 647);
INSERT INTO public.base_city_adjacent VALUES ('3415', '3412', 648);
INSERT INTO public.base_city_adjacent VALUES ('3412', '3416', 649);
INSERT INTO public.base_city_adjacent VALUES ('3416', '3412', 650);
INSERT INTO public.base_city_adjacent VALUES ('3412', '4115', 651);
INSERT INTO public.base_city_adjacent VALUES ('4115', '3412', 652);
INSERT INTO public.base_city_adjacent VALUES ('3412', '4116', 653);
INSERT INTO public.base_city_adjacent VALUES ('4116', '3412', 654);
INSERT INTO public.base_city_adjacent VALUES ('3412', '4117', 655);
INSERT INTO public.base_city_adjacent VALUES ('4117', '3412', 656);
INSERT INTO public.base_city_adjacent VALUES ('3413', '3717', 657);
INSERT INTO public.base_city_adjacent VALUES ('3717', '3413', 658);
INSERT INTO public.base_city_adjacent VALUES ('3413', '4114', 659);
INSERT INTO public.base_city_adjacent VALUES ('4114', '3413', 660);
INSERT INTO public.base_city_adjacent VALUES ('3415', '4115', 661);
INSERT INTO public.base_city_adjacent VALUES ('4115', '3415', 662);
INSERT INTO public.base_city_adjacent VALUES ('3415', '4211', 663);
INSERT INTO public.base_city_adjacent VALUES ('4211', '3415', 664);
INSERT INTO public.base_city_adjacent VALUES ('3416', '4114', 665);
INSERT INTO public.base_city_adjacent VALUES ('4114', '3416', 666);
INSERT INTO public.base_city_adjacent VALUES ('3416', '4116', 667);
INSERT INTO public.base_city_adjacent VALUES ('4116', '3416', 668);
INSERT INTO public.base_city_adjacent VALUES ('3417', '3418', 669);
INSERT INTO public.base_city_adjacent VALUES ('3418', '3417', 670);
INSERT INTO public.base_city_adjacent VALUES ('3417', '3602', 671);
INSERT INTO public.base_city_adjacent VALUES ('3602', '3417', 672);
INSERT INTO public.base_city_adjacent VALUES ('3417', '3604', 673);
INSERT INTO public.base_city_adjacent VALUES ('3604', '3417', 674);
INSERT INTO public.base_city_adjacent VALUES ('3417', '3611', 675);
INSERT INTO public.base_city_adjacent VALUES ('3611', '3417', 676);
INSERT INTO public.base_city_adjacent VALUES ('3501', '3503', 677);
INSERT INTO public.base_city_adjacent VALUES ('3503', '3501', 678);
INSERT INTO public.base_city_adjacent VALUES ('3501', '3504', 679);
INSERT INTO public.base_city_adjacent VALUES ('3504', '3501', 680);
INSERT INTO public.base_city_adjacent VALUES ('3501', '3505', 681);
INSERT INTO public.base_city_adjacent VALUES ('3505', '3501', 682);
INSERT INTO public.base_city_adjacent VALUES ('3501', '3507', 683);
INSERT INTO public.base_city_adjacent VALUES ('3507', '3501', 684);
INSERT INTO public.base_city_adjacent VALUES ('3501', '3509', 685);
INSERT INTO public.base_city_adjacent VALUES ('3509', '3501', 686);
INSERT INTO public.base_city_adjacent VALUES ('3502', '3505', 687);
INSERT INTO public.base_city_adjacent VALUES ('3505', '3502', 688);
INSERT INTO public.base_city_adjacent VALUES ('3502', '3506', 689);
INSERT INTO public.base_city_adjacent VALUES ('3506', '3502', 690);
INSERT INTO public.base_city_adjacent VALUES ('3503', '3505', 691);
INSERT INTO public.base_city_adjacent VALUES ('3505', '3503', 692);
INSERT INTO public.base_city_adjacent VALUES ('3504', '3505', 693);
INSERT INTO public.base_city_adjacent VALUES ('3505', '3504', 694);
INSERT INTO public.base_city_adjacent VALUES ('3504', '3507', 695);
INSERT INTO public.base_city_adjacent VALUES ('3507', '3504', 696);
INSERT INTO public.base_city_adjacent VALUES ('3504', '3508', 697);
INSERT INTO public.base_city_adjacent VALUES ('3508', '3504', 698);
INSERT INTO public.base_city_adjacent VALUES ('3504', '3607', 699);
INSERT INTO public.base_city_adjacent VALUES ('3607', '3504', 700);
INSERT INTO public.base_city_adjacent VALUES ('3504', '3610', 701);
INSERT INTO public.base_city_adjacent VALUES ('3610', '3504', 702);
INSERT INTO public.base_city_adjacent VALUES ('3505', '3506', 703);
INSERT INTO public.base_city_adjacent VALUES ('3506', '3505', 704);
INSERT INTO public.base_city_adjacent VALUES ('3505', '3508', 705);
INSERT INTO public.base_city_adjacent VALUES ('3508', '3505', 706);
INSERT INTO public.base_city_adjacent VALUES ('3506', '3508', 707);
INSERT INTO public.base_city_adjacent VALUES ('3508', '3506', 708);
INSERT INTO public.base_city_adjacent VALUES ('3506', '4414', 709);
INSERT INTO public.base_city_adjacent VALUES ('4414', '3506', 710);
INSERT INTO public.base_city_adjacent VALUES ('3506', '4451', 711);
INSERT INTO public.base_city_adjacent VALUES ('4451', '3506', 712);
INSERT INTO public.base_city_adjacent VALUES ('3507', '3509', 713);
INSERT INTO public.base_city_adjacent VALUES ('3509', '3507', 714);
INSERT INTO public.base_city_adjacent VALUES ('3507', '3606', 715);
INSERT INTO public.base_city_adjacent VALUES ('3606', '3507', 716);
INSERT INTO public.base_city_adjacent VALUES ('3507', '3610', 717);
INSERT INTO public.base_city_adjacent VALUES ('3610', '3507', 718);
INSERT INTO public.base_city_adjacent VALUES ('3507', '3611', 719);
INSERT INTO public.base_city_adjacent VALUES ('3611', '3507', 720);
INSERT INTO public.base_city_adjacent VALUES ('3508', '3607', 721);
INSERT INTO public.base_city_adjacent VALUES ('3607', '3508', 722);
INSERT INTO public.base_city_adjacent VALUES ('3508', '4414', 723);
INSERT INTO public.base_city_adjacent VALUES ('4414', '3508', 724);
INSERT INTO public.base_city_adjacent VALUES ('3601', '3604', 725);
INSERT INTO public.base_city_adjacent VALUES ('3604', '3601', 726);
INSERT INTO public.base_city_adjacent VALUES ('3601', '3609', 727);
INSERT INTO public.base_city_adjacent VALUES ('3609', '3601', 728);
INSERT INTO public.base_city_adjacent VALUES ('3601', '3610', 729);
INSERT INTO public.base_city_adjacent VALUES ('3610', '3601', 730);
INSERT INTO public.base_city_adjacent VALUES ('3601', '3611', 731);
INSERT INTO public.base_city_adjacent VALUES ('3611', '3601', 732);
INSERT INTO public.base_city_adjacent VALUES ('3602', '3611', 733);
INSERT INTO public.base_city_adjacent VALUES ('3611', '3602', 734);
INSERT INTO public.base_city_adjacent VALUES ('3603', '3608', 735);
INSERT INTO public.base_city_adjacent VALUES ('3608', '3603', 736);
INSERT INTO public.base_city_adjacent VALUES ('3603', '3609', 737);
INSERT INTO public.base_city_adjacent VALUES ('3609', '3603', 738);
INSERT INTO public.base_city_adjacent VALUES ('3603', '4301', 739);
INSERT INTO public.base_city_adjacent VALUES ('4301', '3603', 740);
INSERT INTO public.base_city_adjacent VALUES ('3603', '4302', 741);
INSERT INTO public.base_city_adjacent VALUES ('4302', '3603', 742);
INSERT INTO public.base_city_adjacent VALUES ('3604', '3609', 743);
INSERT INTO public.base_city_adjacent VALUES ('3609', '3604', 744);
INSERT INTO public.base_city_adjacent VALUES ('3604', '3611', 745);
INSERT INTO public.base_city_adjacent VALUES ('3611', '3604', 746);
INSERT INTO public.base_city_adjacent VALUES ('3604', '4202', 747);
INSERT INTO public.base_city_adjacent VALUES ('4202', '3604', 748);
INSERT INTO public.base_city_adjacent VALUES ('3604', '4211', 749);
INSERT INTO public.base_city_adjacent VALUES ('4211', '3604', 750);
INSERT INTO public.base_city_adjacent VALUES ('3604', '4212', 751);
INSERT INTO public.base_city_adjacent VALUES ('4212', '3604', 752);
INSERT INTO public.base_city_adjacent VALUES ('3604', '4306', 753);
INSERT INTO public.base_city_adjacent VALUES ('4306', '3604', 754);
INSERT INTO public.base_city_adjacent VALUES ('3605', '3608', 755);
INSERT INTO public.base_city_adjacent VALUES ('3608', '3605', 756);
INSERT INTO public.base_city_adjacent VALUES ('3605', '3609', 757);
INSERT INTO public.base_city_adjacent VALUES ('3609', '3605', 758);
INSERT INTO public.base_city_adjacent VALUES ('3606', '3610', 759);
INSERT INTO public.base_city_adjacent VALUES ('3610', '3606', 760);
INSERT INTO public.base_city_adjacent VALUES ('3606', '3611', 761);
INSERT INTO public.base_city_adjacent VALUES ('3611', '3606', 762);
INSERT INTO public.base_city_adjacent VALUES ('3607', '3608', 763);
INSERT INTO public.base_city_adjacent VALUES ('3608', '3607', 764);
INSERT INTO public.base_city_adjacent VALUES ('3607', '3610', 765);
INSERT INTO public.base_city_adjacent VALUES ('3610', '3607', 766);
INSERT INTO public.base_city_adjacent VALUES ('3607', '4310', 767);
INSERT INTO public.base_city_adjacent VALUES ('4310', '3607', 768);
INSERT INTO public.base_city_adjacent VALUES ('3607', '4402', 769);
INSERT INTO public.base_city_adjacent VALUES ('4402', '3607', 770);
INSERT INTO public.base_city_adjacent VALUES ('3607', '4414', 771);
INSERT INTO public.base_city_adjacent VALUES ('4414', '3607', 772);
INSERT INTO public.base_city_adjacent VALUES ('3607', '4416', 773);
INSERT INTO public.base_city_adjacent VALUES ('4416', '3607', 774);
INSERT INTO public.base_city_adjacent VALUES ('3608', '3609', 775);
INSERT INTO public.base_city_adjacent VALUES ('3609', '3608', 776);
INSERT INTO public.base_city_adjacent VALUES ('3608', '3610', 777);
INSERT INTO public.base_city_adjacent VALUES ('3610', '3608', 778);
INSERT INTO public.base_city_adjacent VALUES ('3608', '4302', 779);
INSERT INTO public.base_city_adjacent VALUES ('4302', '3608', 780);
INSERT INTO public.base_city_adjacent VALUES ('3608', '4310', 781);
INSERT INTO public.base_city_adjacent VALUES ('4310', '3608', 782);
INSERT INTO public.base_city_adjacent VALUES ('3609', '3610', 783);
INSERT INTO public.base_city_adjacent VALUES ('3610', '3609', 784);
INSERT INTO public.base_city_adjacent VALUES ('3609', '4301', 785);
INSERT INTO public.base_city_adjacent VALUES ('4301', '3609', 786);
INSERT INTO public.base_city_adjacent VALUES ('3609', '4306', 787);
INSERT INTO public.base_city_adjacent VALUES ('4306', '3609', 788);
INSERT INTO public.base_city_adjacent VALUES ('3610', '3611', 789);
INSERT INTO public.base_city_adjacent VALUES ('3611', '3610', 790);
INSERT INTO public.base_city_adjacent VALUES ('3701', '3703', 791);
INSERT INTO public.base_city_adjacent VALUES ('3703', '3701', 792);
INSERT INTO public.base_city_adjacent VALUES ('3701', '3709', 793);
INSERT INTO public.base_city_adjacent VALUES ('3709', '3701', 794);
INSERT INTO public.base_city_adjacent VALUES ('3701', '3714', 795);
INSERT INTO public.base_city_adjacent VALUES ('3714', '3701', 796);
INSERT INTO public.base_city_adjacent VALUES ('3701', '3715', 797);
INSERT INTO public.base_city_adjacent VALUES ('3715', '3701', 798);
INSERT INTO public.base_city_adjacent VALUES ('3701', '3716', 799);
INSERT INTO public.base_city_adjacent VALUES ('3716', '3701', 800);
INSERT INTO public.base_city_adjacent VALUES ('3702', '3706', 801);
INSERT INTO public.base_city_adjacent VALUES ('3706', '3702', 802);
INSERT INTO public.base_city_adjacent VALUES ('3702', '3707', 803);
INSERT INTO public.base_city_adjacent VALUES ('3707', '3702', 804);
INSERT INTO public.base_city_adjacent VALUES ('3702', '3711', 805);
INSERT INTO public.base_city_adjacent VALUES ('3711', '3702', 806);
INSERT INTO public.base_city_adjacent VALUES ('3703', '3705', 807);
INSERT INTO public.base_city_adjacent VALUES ('3705', '3703', 808);
INSERT INTO public.base_city_adjacent VALUES ('3703', '3707', 809);
INSERT INTO public.base_city_adjacent VALUES ('3707', '3703', 810);
INSERT INTO public.base_city_adjacent VALUES ('3703', '3709', 811);
INSERT INTO public.base_city_adjacent VALUES ('3709', '3703', 812);
INSERT INTO public.base_city_adjacent VALUES ('3703', '3713', 813);
INSERT INTO public.base_city_adjacent VALUES ('3713', '3703', 814);
INSERT INTO public.base_city_adjacent VALUES ('3703', '3716', 815);
INSERT INTO public.base_city_adjacent VALUES ('3716', '3703', 816);
INSERT INTO public.base_city_adjacent VALUES ('3704', '3708', 817);
INSERT INTO public.base_city_adjacent VALUES ('3708', '3704', 818);
INSERT INTO public.base_city_adjacent VALUES ('3704', '3713', 819);
INSERT INTO public.base_city_adjacent VALUES ('3713', '3704', 820);
INSERT INTO public.base_city_adjacent VALUES ('3705', '3707', 821);
INSERT INTO public.base_city_adjacent VALUES ('3707', '3705', 822);
INSERT INTO public.base_city_adjacent VALUES ('3705', '3716', 823);
INSERT INTO public.base_city_adjacent VALUES ('3716', '3705', 824);
INSERT INTO public.base_city_adjacent VALUES ('3706', '3707', 825);
INSERT INTO public.base_city_adjacent VALUES ('3707', '3706', 826);
INSERT INTO public.base_city_adjacent VALUES ('3706', '3710', 827);
INSERT INTO public.base_city_adjacent VALUES ('3710', '3706', 828);
INSERT INTO public.base_city_adjacent VALUES ('3707', '3711', 829);
INSERT INTO public.base_city_adjacent VALUES ('3711', '3707', 830);
INSERT INTO public.base_city_adjacent VALUES ('3707', '3713', 831);
INSERT INTO public.base_city_adjacent VALUES ('3713', '3707', 832);
INSERT INTO public.base_city_adjacent VALUES ('3708', '3709', 833);
INSERT INTO public.base_city_adjacent VALUES ('3709', '3708', 834);
INSERT INTO public.base_city_adjacent VALUES ('3708', '3713', 835);
INSERT INTO public.base_city_adjacent VALUES ('3713', '3708', 836);
INSERT INTO public.base_city_adjacent VALUES ('3708', '3717', 837);
INSERT INTO public.base_city_adjacent VALUES ('3717', '3708', 838);
INSERT INTO public.base_city_adjacent VALUES ('3708', '4109', 839);
INSERT INTO public.base_city_adjacent VALUES ('4109', '3708', 840);
INSERT INTO public.base_city_adjacent VALUES ('3709', '3713', 841);
INSERT INTO public.base_city_adjacent VALUES ('3713', '3709', 842);
INSERT INTO public.base_city_adjacent VALUES ('3709', '3715', 843);
INSERT INTO public.base_city_adjacent VALUES ('3715', '3709', 844);
INSERT INTO public.base_city_adjacent VALUES ('3709', '4109', 845);
INSERT INTO public.base_city_adjacent VALUES ('4109', '3709', 846);
INSERT INTO public.base_city_adjacent VALUES ('3711', '3713', 847);
INSERT INTO public.base_city_adjacent VALUES ('3713', '3711', 848);
INSERT INTO public.base_city_adjacent VALUES ('3714', '3715', 849);
INSERT INTO public.base_city_adjacent VALUES ('3715', '3714', 850);
INSERT INTO public.base_city_adjacent VALUES ('3714', '3716', 851);
INSERT INTO public.base_city_adjacent VALUES ('3716', '3714', 852);
INSERT INTO public.base_city_adjacent VALUES ('3715', '4109', 853);
INSERT INTO public.base_city_adjacent VALUES ('4109', '3715', 854);
INSERT INTO public.base_city_adjacent VALUES ('3717', '4102', 855);
INSERT INTO public.base_city_adjacent VALUES ('4102', '3717', 856);
INSERT INTO public.base_city_adjacent VALUES ('3717', '4107', 857);
INSERT INTO public.base_city_adjacent VALUES ('4107', '3717', 858);
INSERT INTO public.base_city_adjacent VALUES ('3717', '4109', 859);
INSERT INTO public.base_city_adjacent VALUES ('4109', '3717', 860);
INSERT INTO public.base_city_adjacent VALUES ('3717', '4114', 861);
INSERT INTO public.base_city_adjacent VALUES ('4114', '3717', 862);
INSERT INTO public.base_city_adjacent VALUES ('4101', '4102', 863);
INSERT INTO public.base_city_adjacent VALUES ('4102', '4101', 864);
INSERT INTO public.base_city_adjacent VALUES ('4101', '4103', 865);
INSERT INTO public.base_city_adjacent VALUES ('4103', '4101', 866);
INSERT INTO public.base_city_adjacent VALUES ('4101', '4104', 867);
INSERT INTO public.base_city_adjacent VALUES ('4104', '4101', 868);
INSERT INTO public.base_city_adjacent VALUES ('4101', '4107', 869);
INSERT INTO public.base_city_adjacent VALUES ('4107', '4101', 870);
INSERT INTO public.base_city_adjacent VALUES ('4101', '4108', 871);
INSERT INTO public.base_city_adjacent VALUES ('4108', '4101', 872);
INSERT INTO public.base_city_adjacent VALUES ('4101', '4110', 873);
INSERT INTO public.base_city_adjacent VALUES ('4110', '4101', 874);
INSERT INTO public.base_city_adjacent VALUES ('4102', '4107', 875);
INSERT INTO public.base_city_adjacent VALUES ('4107', '4102', 876);
INSERT INTO public.base_city_adjacent VALUES ('4102', '4110', 877);
INSERT INTO public.base_city_adjacent VALUES ('4110', '4102', 878);
INSERT INTO public.base_city_adjacent VALUES ('4102', '4114', 879);
INSERT INTO public.base_city_adjacent VALUES ('4114', '4102', 880);
INSERT INTO public.base_city_adjacent VALUES ('4102', '4116', 881);
INSERT INTO public.base_city_adjacent VALUES ('4116', '4102', 882);
INSERT INTO public.base_city_adjacent VALUES ('4103', '4104', 883);
INSERT INTO public.base_city_adjacent VALUES ('4104', '4103', 884);
INSERT INTO public.base_city_adjacent VALUES ('4103', '4108', 885);
INSERT INTO public.base_city_adjacent VALUES ('4108', '4103', 886);
INSERT INTO public.base_city_adjacent VALUES ('4103', '4112', 887);
INSERT INTO public.base_city_adjacent VALUES ('4112', '4103', 888);
INSERT INTO public.base_city_adjacent VALUES ('4103', '4113', 889);
INSERT INTO public.base_city_adjacent VALUES ('4113', '4103', 890);
INSERT INTO public.base_city_adjacent VALUES ('4103', '4190', 891);
INSERT INTO public.base_city_adjacent VALUES ('4190', '4103', 892);
INSERT INTO public.base_city_adjacent VALUES ('4104', '4110', 893);
INSERT INTO public.base_city_adjacent VALUES ('4110', '4104', 894);
INSERT INTO public.base_city_adjacent VALUES ('4104', '4111', 895);
INSERT INTO public.base_city_adjacent VALUES ('4111', '4104', 896);
INSERT INTO public.base_city_adjacent VALUES ('4104', '4113', 897);
INSERT INTO public.base_city_adjacent VALUES ('4113', '4104', 898);
INSERT INTO public.base_city_adjacent VALUES ('4104', '4117', 899);
INSERT INTO public.base_city_adjacent VALUES ('4117', '4104', 900);
INSERT INTO public.base_city_adjacent VALUES ('4105', '4106', 901);
INSERT INTO public.base_city_adjacent VALUES ('4106', '4105', 902);
INSERT INTO public.base_city_adjacent VALUES ('4105', '4107', 903);
INSERT INTO public.base_city_adjacent VALUES ('4107', '4105', 904);
INSERT INTO public.base_city_adjacent VALUES ('4105', '4109', 905);
INSERT INTO public.base_city_adjacent VALUES ('4109', '4105', 906);
INSERT INTO public.base_city_adjacent VALUES ('4106', '4107', 907);
INSERT INTO public.base_city_adjacent VALUES ('4107', '4106', 908);
INSERT INTO public.base_city_adjacent VALUES ('4107', '4108', 909);
INSERT INTO public.base_city_adjacent VALUES ('4108', '4107', 910);
INSERT INTO public.base_city_adjacent VALUES ('4107', '4109', 911);
INSERT INTO public.base_city_adjacent VALUES ('4109', '4107', 912);
INSERT INTO public.base_city_adjacent VALUES ('4108', '4190', 913);
INSERT INTO public.base_city_adjacent VALUES ('4190', '4108', 914);
INSERT INTO public.base_city_adjacent VALUES ('4110', '4111', 915);
INSERT INTO public.base_city_adjacent VALUES ('4111', '4110', 916);
INSERT INTO public.base_city_adjacent VALUES ('4110', '4116', 917);
INSERT INTO public.base_city_adjacent VALUES ('4116', '4110', 918);
INSERT INTO public.base_city_adjacent VALUES ('4111', '4116', 919);
INSERT INTO public.base_city_adjacent VALUES ('4116', '4111', 920);
INSERT INTO public.base_city_adjacent VALUES ('4111', '4117', 921);
INSERT INTO public.base_city_adjacent VALUES ('4117', '4111', 922);
INSERT INTO public.base_city_adjacent VALUES ('4112', '4113', 923);
INSERT INTO public.base_city_adjacent VALUES ('4113', '4112', 924);
INSERT INTO public.base_city_adjacent VALUES ('4112', '6105', 925);
INSERT INTO public.base_city_adjacent VALUES ('6105', '4112', 926);
INSERT INTO public.base_city_adjacent VALUES ('4112', '6110', 927);
INSERT INTO public.base_city_adjacent VALUES ('6110', '4112', 928);
INSERT INTO public.base_city_adjacent VALUES ('4113', '4115', 929);
INSERT INTO public.base_city_adjacent VALUES ('4115', '4113', 930);
INSERT INTO public.base_city_adjacent VALUES ('4113', '4117', 931);
INSERT INTO public.base_city_adjacent VALUES ('4117', '4113', 932);
INSERT INTO public.base_city_adjacent VALUES ('4113', '4203', 933);
INSERT INTO public.base_city_adjacent VALUES ('4203', '4113', 934);
INSERT INTO public.base_city_adjacent VALUES ('4113', '4206', 935);
INSERT INTO public.base_city_adjacent VALUES ('4206', '4113', 936);
INSERT INTO public.base_city_adjacent VALUES ('4113', '4213', 937);
INSERT INTO public.base_city_adjacent VALUES ('4213', '4113', 938);
INSERT INTO public.base_city_adjacent VALUES ('4113', '6110', 939);
INSERT INTO public.base_city_adjacent VALUES ('6110', '4113', 940);
INSERT INTO public.base_city_adjacent VALUES ('4114', '4116', 941);
INSERT INTO public.base_city_adjacent VALUES ('4116', '4114', 942);
INSERT INTO public.base_city_adjacent VALUES ('4115', '4117', 943);
INSERT INTO public.base_city_adjacent VALUES ('4117', '4115', 944);
INSERT INTO public.base_city_adjacent VALUES ('4115', '4209', 945);
INSERT INTO public.base_city_adjacent VALUES ('4209', '4115', 946);
INSERT INTO public.base_city_adjacent VALUES ('4115', '4211', 947);
INSERT INTO public.base_city_adjacent VALUES ('4211', '4115', 948);
INSERT INTO public.base_city_adjacent VALUES ('4115', '4213', 949);
INSERT INTO public.base_city_adjacent VALUES ('4213', '4115', 950);
INSERT INTO public.base_city_adjacent VALUES ('4116', '4117', 951);
INSERT INTO public.base_city_adjacent VALUES ('4117', '4116', 952);
INSERT INTO public.base_city_adjacent VALUES ('4201', '4202', 953);
INSERT INTO public.base_city_adjacent VALUES ('4202', '4201', 954);
INSERT INTO public.base_city_adjacent VALUES ('4201', '4207', 955);
INSERT INTO public.base_city_adjacent VALUES ('4207', '4201', 956);
INSERT INTO public.base_city_adjacent VALUES ('4201', '4209', 957);
INSERT INTO public.base_city_adjacent VALUES ('4209', '4201', 958);
INSERT INTO public.base_city_adjacent VALUES ('4201', '4210', 959);
INSERT INTO public.base_city_adjacent VALUES ('4210', '4201', 960);
INSERT INTO public.base_city_adjacent VALUES ('4201', '4211', 961);
INSERT INTO public.base_city_adjacent VALUES ('4211', '4201', 962);
INSERT INTO public.base_city_adjacent VALUES ('4201', '4212', 963);
INSERT INTO public.base_city_adjacent VALUES ('4212', '4201', 964);
INSERT INTO public.base_city_adjacent VALUES ('4201', '4290', 965);
INSERT INTO public.base_city_adjacent VALUES ('4290', '4201', 966);
INSERT INTO public.base_city_adjacent VALUES ('4202', '4207', 967);
INSERT INTO public.base_city_adjacent VALUES ('4207', '4202', 968);
INSERT INTO public.base_city_adjacent VALUES ('4202', '4211', 969);
INSERT INTO public.base_city_adjacent VALUES ('4211', '4202', 970);
INSERT INTO public.base_city_adjacent VALUES ('4202', '4212', 971);
INSERT INTO public.base_city_adjacent VALUES ('4212', '4202', 972);
INSERT INTO public.base_city_adjacent VALUES ('4203', '4206', 973);
INSERT INTO public.base_city_adjacent VALUES ('4206', '4203', 974);
INSERT INTO public.base_city_adjacent VALUES ('4203', '5001', 975);
INSERT INTO public.base_city_adjacent VALUES ('5001', '4203', 976);
INSERT INTO public.base_city_adjacent VALUES ('4203', '6109', 977);
INSERT INTO public.base_city_adjacent VALUES ('6109', '4203', 978);
INSERT INTO public.base_city_adjacent VALUES ('4203', '6110', 979);
INSERT INTO public.base_city_adjacent VALUES ('6110', '4203', 980);
INSERT INTO public.base_city_adjacent VALUES ('4205', '4206', 981);
INSERT INTO public.base_city_adjacent VALUES ('4206', '4205', 982);
INSERT INTO public.base_city_adjacent VALUES ('4205', '4208', 983);
INSERT INTO public.base_city_adjacent VALUES ('4208', '4205', 984);
INSERT INTO public.base_city_adjacent VALUES ('4205', '4210', 985);
INSERT INTO public.base_city_adjacent VALUES ('4210', '4205', 986);
INSERT INTO public.base_city_adjacent VALUES ('4205', '4228', 987);
INSERT INTO public.base_city_adjacent VALUES ('4228', '4205', 988);
INSERT INTO public.base_city_adjacent VALUES ('4205', '4307', 989);
INSERT INTO public.base_city_adjacent VALUES ('4307', '4205', 990);
INSERT INTO public.base_city_adjacent VALUES ('4206', '4208', 991);
INSERT INTO public.base_city_adjacent VALUES ('4208', '4206', 992);
INSERT INTO public.base_city_adjacent VALUES ('4206', '4213', 993);
INSERT INTO public.base_city_adjacent VALUES ('4213', '4206', 994);
INSERT INTO public.base_city_adjacent VALUES ('4207', '4211', 995);
INSERT INTO public.base_city_adjacent VALUES ('4211', '4207', 996);
INSERT INTO public.base_city_adjacent VALUES ('4208', '4209', 997);
INSERT INTO public.base_city_adjacent VALUES ('4209', '4208', 998);
INSERT INTO public.base_city_adjacent VALUES ('4208', '4210', 999);
INSERT INTO public.base_city_adjacent VALUES ('4210', '4208', 1000);
INSERT INTO public.base_city_adjacent VALUES ('4208', '4213', 1001);
INSERT INTO public.base_city_adjacent VALUES ('4213', '4208', 1002);
INSERT INTO public.base_city_adjacent VALUES ('4209', '4211', 1003);
INSERT INTO public.base_city_adjacent VALUES ('4211', '4209', 1004);
INSERT INTO public.base_city_adjacent VALUES ('4209', '4213', 1005);
INSERT INTO public.base_city_adjacent VALUES ('4213', '4209', 1006);
INSERT INTO public.base_city_adjacent VALUES ('4209', '4290', 1007);
INSERT INTO public.base_city_adjacent VALUES ('4290', '4209', 1008);
INSERT INTO public.base_city_adjacent VALUES ('4210', '4212', 1009);
INSERT INTO public.base_city_adjacent VALUES ('4212', '4210', 1010);
INSERT INTO public.base_city_adjacent VALUES ('4210', '4290', 1011);
INSERT INTO public.base_city_adjacent VALUES ('4290', '4210', 1012);
INSERT INTO public.base_city_adjacent VALUES ('4210', '4306', 1013);
INSERT INTO public.base_city_adjacent VALUES ('4306', '4210', 1014);
INSERT INTO public.base_city_adjacent VALUES ('4210', '4307', 1015);
INSERT INTO public.base_city_adjacent VALUES ('4307', '4210', 1016);
INSERT INTO public.base_city_adjacent VALUES ('4210', '4309', 1017);
INSERT INTO public.base_city_adjacent VALUES ('4309', '4210', 1018);
INSERT INTO public.base_city_adjacent VALUES ('4212', '4306', 1019);
INSERT INTO public.base_city_adjacent VALUES ('4306', '4212', 1020);
INSERT INTO public.base_city_adjacent VALUES ('4228', '4307', 1021);
INSERT INTO public.base_city_adjacent VALUES ('4307', '4228', 1022);
INSERT INTO public.base_city_adjacent VALUES ('4228', '4308', 1023);
INSERT INTO public.base_city_adjacent VALUES ('4308', '4228', 1024);
INSERT INTO public.base_city_adjacent VALUES ('4228', '4331', 1025);
INSERT INTO public.base_city_adjacent VALUES ('4331', '4228', 1026);
INSERT INTO public.base_city_adjacent VALUES ('4228', '5001', 1027);
INSERT INTO public.base_city_adjacent VALUES ('5001', '4228', 1028);
INSERT INTO public.base_city_adjacent VALUES ('4301', '4302', 1029);
INSERT INTO public.base_city_adjacent VALUES ('4302', '4301', 1030);
INSERT INTO public.base_city_adjacent VALUES ('4301', '4303', 1031);
INSERT INTO public.base_city_adjacent VALUES ('4303', '4301', 1032);
INSERT INTO public.base_city_adjacent VALUES ('4301', '4306', 1033);
INSERT INTO public.base_city_adjacent VALUES ('4306', '4301', 1034);
INSERT INTO public.base_city_adjacent VALUES ('4301', '4309', 1035);
INSERT INTO public.base_city_adjacent VALUES ('4309', '4301', 1036);
INSERT INTO public.base_city_adjacent VALUES ('4301', '4313', 1037);
INSERT INTO public.base_city_adjacent VALUES ('4313', '4301', 1038);
INSERT INTO public.base_city_adjacent VALUES ('4302', '4303', 1039);
INSERT INTO public.base_city_adjacent VALUES ('4303', '4302', 1040);
INSERT INTO public.base_city_adjacent VALUES ('4302', '4304', 1041);
INSERT INTO public.base_city_adjacent VALUES ('4304', '4302', 1042);
INSERT INTO public.base_city_adjacent VALUES ('4302', '4310', 1043);
INSERT INTO public.base_city_adjacent VALUES ('4310', '4302', 1044);
INSERT INTO public.base_city_adjacent VALUES ('4303', '4304', 1045);
INSERT INTO public.base_city_adjacent VALUES ('4304', '4303', 1046);
INSERT INTO public.base_city_adjacent VALUES ('4303', '4313', 1047);
INSERT INTO public.base_city_adjacent VALUES ('4313', '4303', 1048);
INSERT INTO public.base_city_adjacent VALUES ('4304', '4305', 1049);
INSERT INTO public.base_city_adjacent VALUES ('4305', '4304', 1050);
INSERT INTO public.base_city_adjacent VALUES ('4304', '4310', 1051);
INSERT INTO public.base_city_adjacent VALUES ('4310', '4304', 1052);
INSERT INTO public.base_city_adjacent VALUES ('4304', '4311', 1053);
INSERT INTO public.base_city_adjacent VALUES ('4311', '4304', 1054);
INSERT INTO public.base_city_adjacent VALUES ('4304', '4313', 1055);
INSERT INTO public.base_city_adjacent VALUES ('4313', '4304', 1056);
INSERT INTO public.base_city_adjacent VALUES ('4305', '4311', 1057);
INSERT INTO public.base_city_adjacent VALUES ('4311', '4305', 1058);
INSERT INTO public.base_city_adjacent VALUES ('4305', '4312', 1059);
INSERT INTO public.base_city_adjacent VALUES ('4312', '4305', 1060);
INSERT INTO public.base_city_adjacent VALUES ('4305', '4313', 1061);
INSERT INTO public.base_city_adjacent VALUES ('4313', '4305', 1062);
INSERT INTO public.base_city_adjacent VALUES ('4305', '4503', 1063);
INSERT INTO public.base_city_adjacent VALUES ('4503', '4305', 1064);
INSERT INTO public.base_city_adjacent VALUES ('4306', '4309', 1065);
INSERT INTO public.base_city_adjacent VALUES ('4309', '4306', 1066);
INSERT INTO public.base_city_adjacent VALUES ('4307', '4308', 1067);
INSERT INTO public.base_city_adjacent VALUES ('4308', '4307', 1068);
INSERT INTO public.base_city_adjacent VALUES ('4307', '4309', 1069);
INSERT INTO public.base_city_adjacent VALUES ('4309', '4307', 1070);
INSERT INTO public.base_city_adjacent VALUES ('4307', '4312', 1071);
INSERT INTO public.base_city_adjacent VALUES ('4312', '4307', 1072);
INSERT INTO public.base_city_adjacent VALUES ('4308', '4312', 1073);
INSERT INTO public.base_city_adjacent VALUES ('4312', '4308', 1074);
INSERT INTO public.base_city_adjacent VALUES ('4308', '4331', 1075);
INSERT INTO public.base_city_adjacent VALUES ('4331', '4308', 1076);
INSERT INTO public.base_city_adjacent VALUES ('4309', '4312', 1077);
INSERT INTO public.base_city_adjacent VALUES ('4312', '4309', 1078);
INSERT INTO public.base_city_adjacent VALUES ('4309', '4313', 1079);
INSERT INTO public.base_city_adjacent VALUES ('4313', '4309', 1080);
INSERT INTO public.base_city_adjacent VALUES ('4310', '4311', 1081);
INSERT INTO public.base_city_adjacent VALUES ('4311', '4310', 1082);
INSERT INTO public.base_city_adjacent VALUES ('4310', '4402', 1083);
INSERT INTO public.base_city_adjacent VALUES ('4402', '4310', 1084);
INSERT INTO public.base_city_adjacent VALUES ('4310', '4418', 1085);
INSERT INTO public.base_city_adjacent VALUES ('4418', '4310', 1086);
INSERT INTO public.base_city_adjacent VALUES ('4311', '4418', 1087);
INSERT INTO public.base_city_adjacent VALUES ('4418', '4311', 1088);
INSERT INTO public.base_city_adjacent VALUES ('4311', '4503', 1089);
INSERT INTO public.base_city_adjacent VALUES ('4503', '4311', 1090);
INSERT INTO public.base_city_adjacent VALUES ('4311', '4511', 1091);
INSERT INTO public.base_city_adjacent VALUES ('4511', '4311', 1092);
INSERT INTO public.base_city_adjacent VALUES ('4312', '4313', 1093);
INSERT INTO public.base_city_adjacent VALUES ('4313', '4312', 1094);
INSERT INTO public.base_city_adjacent VALUES ('4312', '4331', 1095);
INSERT INTO public.base_city_adjacent VALUES ('4331', '4312', 1096);
INSERT INTO public.base_city_adjacent VALUES ('4312', '4502', 1097);
INSERT INTO public.base_city_adjacent VALUES ('4502', '4312', 1098);
INSERT INTO public.base_city_adjacent VALUES ('4312', '4503', 1099);
INSERT INTO public.base_city_adjacent VALUES ('4503', '4312', 1100);
INSERT INTO public.base_city_adjacent VALUES ('4312', '5206', 1101);
INSERT INTO public.base_city_adjacent VALUES ('5206', '4312', 1102);
INSERT INTO public.base_city_adjacent VALUES ('4312', '5226', 1103);
INSERT INTO public.base_city_adjacent VALUES ('5226', '4312', 1104);
INSERT INTO public.base_city_adjacent VALUES ('4331', '5001', 1105);
INSERT INTO public.base_city_adjacent VALUES ('5001', '4331', 1106);
INSERT INTO public.base_city_adjacent VALUES ('4331', '5206', 1107);
INSERT INTO public.base_city_adjacent VALUES ('5206', '4331', 1108);
INSERT INTO public.base_city_adjacent VALUES ('4401', '4402', 1109);
INSERT INTO public.base_city_adjacent VALUES ('4402', '4401', 1110);
INSERT INTO public.base_city_adjacent VALUES ('4401', '4406', 1111);
INSERT INTO public.base_city_adjacent VALUES ('4406', '4401', 1112);
INSERT INTO public.base_city_adjacent VALUES ('4401', '4413', 1113);
INSERT INTO public.base_city_adjacent VALUES ('4413', '4401', 1114);
INSERT INTO public.base_city_adjacent VALUES ('4401', '4418', 1115);
INSERT INTO public.base_city_adjacent VALUES ('4418', '4401', 1116);
INSERT INTO public.base_city_adjacent VALUES ('4401', '4419', 1117);
INSERT INTO public.base_city_adjacent VALUES ('4419', '4401', 1118);
INSERT INTO public.base_city_adjacent VALUES ('4401', '4420', 1119);
INSERT INTO public.base_city_adjacent VALUES ('4420', '4401', 1120);
INSERT INTO public.base_city_adjacent VALUES ('4402', '4413', 1121);
INSERT INTO public.base_city_adjacent VALUES ('4413', '4402', 1122);
INSERT INTO public.base_city_adjacent VALUES ('4402', '4416', 1123);
INSERT INTO public.base_city_adjacent VALUES ('4416', '4402', 1124);
INSERT INTO public.base_city_adjacent VALUES ('4402', '4418', 1125);
INSERT INTO public.base_city_adjacent VALUES ('4418', '4402', 1126);
INSERT INTO public.base_city_adjacent VALUES ('4403', '4413', 1127);
INSERT INTO public.base_city_adjacent VALUES ('4413', '4403', 1128);
INSERT INTO public.base_city_adjacent VALUES ('4403', '4419', 1129);
INSERT INTO public.base_city_adjacent VALUES ('4419', '4403', 1130);
INSERT INTO public.base_city_adjacent VALUES ('4404', '4407', 1131);
INSERT INTO public.base_city_adjacent VALUES ('4407', '4404', 1132);
INSERT INTO public.base_city_adjacent VALUES ('4404', '4420', 1133);
INSERT INTO public.base_city_adjacent VALUES ('4420', '4404', 1134);
INSERT INTO public.base_city_adjacent VALUES ('4405', '4451', 1135);
INSERT INTO public.base_city_adjacent VALUES ('4451', '4405', 1136);
INSERT INTO public.base_city_adjacent VALUES ('4405', '4452', 1137);
INSERT INTO public.base_city_adjacent VALUES ('4452', '4405', 1138);
INSERT INTO public.base_city_adjacent VALUES ('4406', '4407', 1139);
INSERT INTO public.base_city_adjacent VALUES ('4407', '4406', 1140);
INSERT INTO public.base_city_adjacent VALUES ('4406', '4412', 1141);
INSERT INTO public.base_city_adjacent VALUES ('4412', '4406', 1142);
INSERT INTO public.base_city_adjacent VALUES ('4406', '4418', 1143);
INSERT INTO public.base_city_adjacent VALUES ('4418', '4406', 1144);
INSERT INTO public.base_city_adjacent VALUES ('4406', '4420', 1145);
INSERT INTO public.base_city_adjacent VALUES ('4420', '4406', 1146);
INSERT INTO public.base_city_adjacent VALUES ('4406', '4453', 1147);
INSERT INTO public.base_city_adjacent VALUES ('4453', '4406', 1148);
INSERT INTO public.base_city_adjacent VALUES ('4407', '4417', 1149);
INSERT INTO public.base_city_adjacent VALUES ('4417', '4407', 1150);
INSERT INTO public.base_city_adjacent VALUES ('4407', '4420', 1151);
INSERT INTO public.base_city_adjacent VALUES ('4420', '4407', 1152);
INSERT INTO public.base_city_adjacent VALUES ('4407', '4453', 1153);
INSERT INTO public.base_city_adjacent VALUES ('4453', '4407', 1154);
INSERT INTO public.base_city_adjacent VALUES ('4408', '4409', 1155);
INSERT INTO public.base_city_adjacent VALUES ('4409', '4408', 1156);
INSERT INTO public.base_city_adjacent VALUES ('4408', '4505', 1157);
INSERT INTO public.base_city_adjacent VALUES ('4505', '4408', 1158);
INSERT INTO public.base_city_adjacent VALUES ('4408', '4509', 1159);
INSERT INTO public.base_city_adjacent VALUES ('4509', '4408', 1160);
INSERT INTO public.base_city_adjacent VALUES ('4409', '4417', 1161);
INSERT INTO public.base_city_adjacent VALUES ('4417', '4409', 1162);
INSERT INTO public.base_city_adjacent VALUES ('4409', '4453', 1163);
INSERT INTO public.base_city_adjacent VALUES ('4453', '4409', 1164);
INSERT INTO public.base_city_adjacent VALUES ('4409', '4504', 1165);
INSERT INTO public.base_city_adjacent VALUES ('4504', '4409', 1166);
INSERT INTO public.base_city_adjacent VALUES ('4409', '4509', 1167);
INSERT INTO public.base_city_adjacent VALUES ('4509', '4409', 1168);
INSERT INTO public.base_city_adjacent VALUES ('4412', '4418', 1169);
INSERT INTO public.base_city_adjacent VALUES ('4418', '4412', 1170);
INSERT INTO public.base_city_adjacent VALUES ('4412', '4453', 1171);
INSERT INTO public.base_city_adjacent VALUES ('4453', '4412', 1172);
INSERT INTO public.base_city_adjacent VALUES ('4412', '4504', 1173);
INSERT INTO public.base_city_adjacent VALUES ('4504', '4412', 1174);
INSERT INTO public.base_city_adjacent VALUES ('4412', '4511', 1175);
INSERT INTO public.base_city_adjacent VALUES ('4511', '4412', 1176);
INSERT INTO public.base_city_adjacent VALUES ('4413', '4415', 1177);
INSERT INTO public.base_city_adjacent VALUES ('4415', '4413', 1178);
INSERT INTO public.base_city_adjacent VALUES ('4413', '4416', 1179);
INSERT INTO public.base_city_adjacent VALUES ('4416', '4413', 1180);
INSERT INTO public.base_city_adjacent VALUES ('4413', '4419', 1181);
INSERT INTO public.base_city_adjacent VALUES ('4419', '4413', 1182);
INSERT INTO public.base_city_adjacent VALUES ('4414', '4415', 1183);
INSERT INTO public.base_city_adjacent VALUES ('4415', '4414', 1184);
INSERT INTO public.base_city_adjacent VALUES ('4414', '4416', 1185);
INSERT INTO public.base_city_adjacent VALUES ('4416', '4414', 1186);
INSERT INTO public.base_city_adjacent VALUES ('4414', '4451', 1187);
INSERT INTO public.base_city_adjacent VALUES ('4451', '4414', 1188);
INSERT INTO public.base_city_adjacent VALUES ('4414', '4452', 1189);
INSERT INTO public.base_city_adjacent VALUES ('4452', '4414', 1190);
INSERT INTO public.base_city_adjacent VALUES ('4415', '4416', 1191);
INSERT INTO public.base_city_adjacent VALUES ('4416', '4415', 1192);
INSERT INTO public.base_city_adjacent VALUES ('4415', '4452', 1193);
INSERT INTO public.base_city_adjacent VALUES ('4452', '4415', 1194);
INSERT INTO public.base_city_adjacent VALUES ('4417', '4453', 1195);
INSERT INTO public.base_city_adjacent VALUES ('4453', '4417', 1196);
INSERT INTO public.base_city_adjacent VALUES ('4418', '4511', 1197);
INSERT INTO public.base_city_adjacent VALUES ('4511', '4418', 1198);
INSERT INTO public.base_city_adjacent VALUES ('4451', '4452', 1199);
INSERT INTO public.base_city_adjacent VALUES ('4452', '4451', 1200);
INSERT INTO public.base_city_adjacent VALUES ('4453', '4504', 1201);
INSERT INTO public.base_city_adjacent VALUES ('4504', '4453', 1202);
INSERT INTO public.base_city_adjacent VALUES ('4501', '4506', 1203);
INSERT INTO public.base_city_adjacent VALUES ('4506', '4501', 1204);
INSERT INTO public.base_city_adjacent VALUES ('4501', '4507', 1205);
INSERT INTO public.base_city_adjacent VALUES ('4507', '4501', 1206);
INSERT INTO public.base_city_adjacent VALUES ('4501', '4508', 1207);
INSERT INTO public.base_city_adjacent VALUES ('4508', '4501', 1208);
INSERT INTO public.base_city_adjacent VALUES ('4501', '4510', 1209);
INSERT INTO public.base_city_adjacent VALUES ('4510', '4501', 1210);
INSERT INTO public.base_city_adjacent VALUES ('4501', '4512', 1211);
INSERT INTO public.base_city_adjacent VALUES ('4512', '4501', 1212);
INSERT INTO public.base_city_adjacent VALUES ('4501', '4513', 1213);
INSERT INTO public.base_city_adjacent VALUES ('4513', '4501', 1214);
INSERT INTO public.base_city_adjacent VALUES ('4501', '4514', 1215);
INSERT INTO public.base_city_adjacent VALUES ('4514', '4501', 1216);
INSERT INTO public.base_city_adjacent VALUES ('4502', '4503', 1217);
INSERT INTO public.base_city_adjacent VALUES ('4503', '4502', 1218);
INSERT INTO public.base_city_adjacent VALUES ('4502', '4512', 1219);
INSERT INTO public.base_city_adjacent VALUES ('4512', '4502', 1220);
INSERT INTO public.base_city_adjacent VALUES ('4502', '4513', 1221);
INSERT INTO public.base_city_adjacent VALUES ('4513', '4502', 1222);
INSERT INTO public.base_city_adjacent VALUES ('4502', '5226', 1223);
INSERT INTO public.base_city_adjacent VALUES ('5226', '4502', 1224);
INSERT INTO public.base_city_adjacent VALUES ('4503', '4504', 1225);
INSERT INTO public.base_city_adjacent VALUES ('4504', '4503', 1226);
INSERT INTO public.base_city_adjacent VALUES ('4503', '4511', 1227);
INSERT INTO public.base_city_adjacent VALUES ('4511', '4503', 1228);
INSERT INTO public.base_city_adjacent VALUES ('4503', '4513', 1229);
INSERT INTO public.base_city_adjacent VALUES ('4513', '4503', 1230);
INSERT INTO public.base_city_adjacent VALUES ('4504', '4508', 1231);
INSERT INTO public.base_city_adjacent VALUES ('4508', '4504', 1232);
INSERT INTO public.base_city_adjacent VALUES ('4504', '4509', 1233);
INSERT INTO public.base_city_adjacent VALUES ('4509', '4504', 1234);
INSERT INTO public.base_city_adjacent VALUES ('4504', '4511', 1235);
INSERT INTO public.base_city_adjacent VALUES ('4511', '4504', 1236);
INSERT INTO public.base_city_adjacent VALUES ('4504', '4513', 1237);
INSERT INTO public.base_city_adjacent VALUES ('4513', '4504', 1238);
INSERT INTO public.base_city_adjacent VALUES ('4505', '4507', 1239);
INSERT INTO public.base_city_adjacent VALUES ('4507', '4505', 1240);
INSERT INTO public.base_city_adjacent VALUES ('4505', '4509', 1241);
INSERT INTO public.base_city_adjacent VALUES ('4509', '4505', 1242);
INSERT INTO public.base_city_adjacent VALUES ('4506', '4507', 1243);
INSERT INTO public.base_city_adjacent VALUES ('4507', '4506', 1244);
INSERT INTO public.base_city_adjacent VALUES ('4506', '4514', 1245);
INSERT INTO public.base_city_adjacent VALUES ('4514', '4506', 1246);
INSERT INTO public.base_city_adjacent VALUES ('4507', '4508', 1247);
INSERT INTO public.base_city_adjacent VALUES ('4508', '4507', 1248);
INSERT INTO public.base_city_adjacent VALUES ('4507', '4509', 1249);
INSERT INTO public.base_city_adjacent VALUES ('4509', '4507', 1250);
INSERT INTO public.base_city_adjacent VALUES ('4508', '4509', 1251);
INSERT INTO public.base_city_adjacent VALUES ('4509', '4508', 1252);
INSERT INTO public.base_city_adjacent VALUES ('4508', '4513', 1253);
INSERT INTO public.base_city_adjacent VALUES ('4513', '4508', 1254);
INSERT INTO public.base_city_adjacent VALUES ('4510', '4512', 1255);
INSERT INTO public.base_city_adjacent VALUES ('4512', '4510', 1256);
INSERT INTO public.base_city_adjacent VALUES ('4510', '4514', 1257);
INSERT INTO public.base_city_adjacent VALUES ('4514', '4510', 1258);
INSERT INTO public.base_city_adjacent VALUES ('4510', '5223', 1259);
INSERT INTO public.base_city_adjacent VALUES ('5223', '4510', 1260);
INSERT INTO public.base_city_adjacent VALUES ('4510', '5227', 1261);
INSERT INTO public.base_city_adjacent VALUES ('5227', '4510', 1262);
INSERT INTO public.base_city_adjacent VALUES ('4510', '5303', 1263);
INSERT INTO public.base_city_adjacent VALUES ('5303', '4510', 1264);
INSERT INTO public.base_city_adjacent VALUES ('4510', '5326', 1265);
INSERT INTO public.base_city_adjacent VALUES ('5326', '4510', 1266);
INSERT INTO public.base_city_adjacent VALUES ('4512', '4513', 1267);
INSERT INTO public.base_city_adjacent VALUES ('4513', '4512', 1268);
INSERT INTO public.base_city_adjacent VALUES ('4512', '5226', 1269);
INSERT INTO public.base_city_adjacent VALUES ('5226', '4512', 1270);
INSERT INTO public.base_city_adjacent VALUES ('4512', '5227', 1271);
INSERT INTO public.base_city_adjacent VALUES ('5227', '4512', 1272);
INSERT INTO public.base_city_adjacent VALUES ('5001', '5105', 1273);
INSERT INTO public.base_city_adjacent VALUES ('5105', '5001', 1274);
INSERT INTO public.base_city_adjacent VALUES ('5001', '5109', 1275);
INSERT INTO public.base_city_adjacent VALUES ('5109', '5001', 1276);
INSERT INTO public.base_city_adjacent VALUES ('5001', '5110', 1277);
INSERT INTO public.base_city_adjacent VALUES ('5110', '5001', 1278);
INSERT INTO public.base_city_adjacent VALUES ('5001', '5116', 1279);
INSERT INTO public.base_city_adjacent VALUES ('5116', '5001', 1280);
INSERT INTO public.base_city_adjacent VALUES ('5001', '5117', 1281);
INSERT INTO public.base_city_adjacent VALUES ('5117', '5001', 1282);
INSERT INTO public.base_city_adjacent VALUES ('5001', '5120', 1283);
INSERT INTO public.base_city_adjacent VALUES ('5120', '5001', 1284);
INSERT INTO public.base_city_adjacent VALUES ('5001', '5203', 1285);
INSERT INTO public.base_city_adjacent VALUES ('5203', '5001', 1286);
INSERT INTO public.base_city_adjacent VALUES ('5001', '5206', 1287);
INSERT INTO public.base_city_adjacent VALUES ('5206', '5001', 1288);
INSERT INTO public.base_city_adjacent VALUES ('5001', '6109', 1289);
INSERT INTO public.base_city_adjacent VALUES ('6109', '5001', 1290);
INSERT INTO public.base_city_adjacent VALUES ('5101', '5106', 1291);
INSERT INTO public.base_city_adjacent VALUES ('5106', '5101', 1292);
INSERT INTO public.base_city_adjacent VALUES ('5101', '5114', 1293);
INSERT INTO public.base_city_adjacent VALUES ('5114', '5101', 1294);
INSERT INTO public.base_city_adjacent VALUES ('5101', '5118', 1295);
INSERT INTO public.base_city_adjacent VALUES ('5118', '5101', 1296);
INSERT INTO public.base_city_adjacent VALUES ('5101', '5120', 1297);
INSERT INTO public.base_city_adjacent VALUES ('5120', '5101', 1298);
INSERT INTO public.base_city_adjacent VALUES ('5101', '5132', 1299);
INSERT INTO public.base_city_adjacent VALUES ('5132', '5101', 1300);
INSERT INTO public.base_city_adjacent VALUES ('5103', '5105', 1301);
INSERT INTO public.base_city_adjacent VALUES ('5105', '5103', 1302);
INSERT INTO public.base_city_adjacent VALUES ('5103', '5110', 1303);
INSERT INTO public.base_city_adjacent VALUES ('5110', '5103', 1304);
INSERT INTO public.base_city_adjacent VALUES ('5103', '5111', 1305);
INSERT INTO public.base_city_adjacent VALUES ('5111', '5103', 1306);
INSERT INTO public.base_city_adjacent VALUES ('5103', '5114', 1307);
INSERT INTO public.base_city_adjacent VALUES ('5114', '5103', 1308);
INSERT INTO public.base_city_adjacent VALUES ('5103', '5115', 1309);
INSERT INTO public.base_city_adjacent VALUES ('5115', '5103', 1310);
INSERT INTO public.base_city_adjacent VALUES ('5104', '5134', 1311);
INSERT INTO public.base_city_adjacent VALUES ('5134', '5104', 1312);
INSERT INTO public.base_city_adjacent VALUES ('5104', '5307', 1313);
INSERT INTO public.base_city_adjacent VALUES ('5307', '5104', 1314);
INSERT INTO public.base_city_adjacent VALUES ('5104', '5323', 1315);
INSERT INTO public.base_city_adjacent VALUES ('5323', '5104', 1316);
INSERT INTO public.base_city_adjacent VALUES ('5105', '5110', 1317);
INSERT INTO public.base_city_adjacent VALUES ('5110', '5105', 1318);
INSERT INTO public.base_city_adjacent VALUES ('5105', '5115', 1319);
INSERT INTO public.base_city_adjacent VALUES ('5115', '5105', 1320);
INSERT INTO public.base_city_adjacent VALUES ('5105', '5203', 1321);
INSERT INTO public.base_city_adjacent VALUES ('5203', '5105', 1322);
INSERT INTO public.base_city_adjacent VALUES ('5105', '5205', 1323);
INSERT INTO public.base_city_adjacent VALUES ('5205', '5105', 1324);
INSERT INTO public.base_city_adjacent VALUES ('5105', '5306', 1325);
INSERT INTO public.base_city_adjacent VALUES ('5306', '5105', 1326);
INSERT INTO public.base_city_adjacent VALUES ('5106', '5107', 1327);
INSERT INTO public.base_city_adjacent VALUES ('5107', '5106', 1328);
INSERT INTO public.base_city_adjacent VALUES ('5106', '5109', 1329);
INSERT INTO public.base_city_adjacent VALUES ('5109', '5106', 1330);
INSERT INTO public.base_city_adjacent VALUES ('5106', '5120', 1331);
INSERT INTO public.base_city_adjacent VALUES ('5120', '5106', 1332);
INSERT INTO public.base_city_adjacent VALUES ('5106', '5132', 1333);
INSERT INTO public.base_city_adjacent VALUES ('5132', '5106', 1334);
INSERT INTO public.base_city_adjacent VALUES ('5107', '5108', 1335);
INSERT INTO public.base_city_adjacent VALUES ('5108', '5107', 1336);
INSERT INTO public.base_city_adjacent VALUES ('5107', '5109', 1337);
INSERT INTO public.base_city_adjacent VALUES ('5109', '5107', 1338);
INSERT INTO public.base_city_adjacent VALUES ('5107', '5113', 1339);
INSERT INTO public.base_city_adjacent VALUES ('5113', '5107', 1340);
INSERT INTO public.base_city_adjacent VALUES ('5107', '5132', 1341);
INSERT INTO public.base_city_adjacent VALUES ('5132', '5107', 1342);
INSERT INTO public.base_city_adjacent VALUES ('5107', '6212', 1343);
INSERT INTO public.base_city_adjacent VALUES ('6212', '5107', 1344);
INSERT INTO public.base_city_adjacent VALUES ('5108', '5113', 1345);
INSERT INTO public.base_city_adjacent VALUES ('5113', '5108', 1346);
INSERT INTO public.base_city_adjacent VALUES ('5108', '5119', 1347);
INSERT INTO public.base_city_adjacent VALUES ('5119', '5108', 1348);
INSERT INTO public.base_city_adjacent VALUES ('5108', '6107', 1349);
INSERT INTO public.base_city_adjacent VALUES ('6107', '5108', 1350);
INSERT INTO public.base_city_adjacent VALUES ('5108', '6212', 1351);
INSERT INTO public.base_city_adjacent VALUES ('6212', '5108', 1352);
INSERT INTO public.base_city_adjacent VALUES ('5109', '5113', 1353);
INSERT INTO public.base_city_adjacent VALUES ('5113', '5109', 1354);
INSERT INTO public.base_city_adjacent VALUES ('5109', '5116', 1355);
INSERT INTO public.base_city_adjacent VALUES ('5116', '5109', 1356);
INSERT INTO public.base_city_adjacent VALUES ('5109', '5120', 1357);
INSERT INTO public.base_city_adjacent VALUES ('5120', '5109', 1358);
INSERT INTO public.base_city_adjacent VALUES ('5110', '5114', 1359);
INSERT INTO public.base_city_adjacent VALUES ('5114', '5110', 1360);
INSERT INTO public.base_city_adjacent VALUES ('5110', '5120', 1361);
INSERT INTO public.base_city_adjacent VALUES ('5120', '5110', 1362);
INSERT INTO public.base_city_adjacent VALUES ('5111', '5114', 1363);
INSERT INTO public.base_city_adjacent VALUES ('5114', '5111', 1364);
INSERT INTO public.base_city_adjacent VALUES ('5111', '5115', 1365);
INSERT INTO public.base_city_adjacent VALUES ('5115', '5111', 1366);
INSERT INTO public.base_city_adjacent VALUES ('5111', '5118', 1367);
INSERT INTO public.base_city_adjacent VALUES ('5118', '5111', 1368);
INSERT INTO public.base_city_adjacent VALUES ('5111', '5134', 1369);
INSERT INTO public.base_city_adjacent VALUES ('5134', '5111', 1370);
INSERT INTO public.base_city_adjacent VALUES ('5113', '5116', 1371);
INSERT INTO public.base_city_adjacent VALUES ('5116', '5113', 1372);
INSERT INTO public.base_city_adjacent VALUES ('5113', '5117', 1373);
INSERT INTO public.base_city_adjacent VALUES ('5117', '5113', 1374);
INSERT INTO public.base_city_adjacent VALUES ('5113', '5119', 1375);
INSERT INTO public.base_city_adjacent VALUES ('5119', '5113', 1376);
INSERT INTO public.base_city_adjacent VALUES ('5114', '5118', 1377);
INSERT INTO public.base_city_adjacent VALUES ('5118', '5114', 1378);
INSERT INTO public.base_city_adjacent VALUES ('5114', '5120', 1379);
INSERT INTO public.base_city_adjacent VALUES ('5120', '5114', 1380);
INSERT INTO public.base_city_adjacent VALUES ('5115', '5134', 1381);
INSERT INTO public.base_city_adjacent VALUES ('5134', '5115', 1382);
INSERT INTO public.base_city_adjacent VALUES ('5115', '5306', 1383);
INSERT INTO public.base_city_adjacent VALUES ('5306', '5115', 1384);
INSERT INTO public.base_city_adjacent VALUES ('5116', '5117', 1385);
INSERT INTO public.base_city_adjacent VALUES ('5117', '5116', 1386);
INSERT INTO public.base_city_adjacent VALUES ('5117', '5119', 1387);
INSERT INTO public.base_city_adjacent VALUES ('5119', '5117', 1388);
INSERT INTO public.base_city_adjacent VALUES ('5117', '6107', 1389);
INSERT INTO public.base_city_adjacent VALUES ('6107', '5117', 1390);
INSERT INTO public.base_city_adjacent VALUES ('5117', '6109', 1391);
INSERT INTO public.base_city_adjacent VALUES ('6109', '5117', 1392);
INSERT INTO public.base_city_adjacent VALUES ('5118', '5132', 1393);
INSERT INTO public.base_city_adjacent VALUES ('5132', '5118', 1394);
INSERT INTO public.base_city_adjacent VALUES ('5118', '5133', 1395);
INSERT INTO public.base_city_adjacent VALUES ('5133', '5118', 1396);
INSERT INTO public.base_city_adjacent VALUES ('5118', '5134', 1397);
INSERT INTO public.base_city_adjacent VALUES ('5134', '5118', 1398);
INSERT INTO public.base_city_adjacent VALUES ('5119', '6107', 1399);
INSERT INTO public.base_city_adjacent VALUES ('6107', '5119', 1400);
INSERT INTO public.base_city_adjacent VALUES ('5132', '5133', 1401);
INSERT INTO public.base_city_adjacent VALUES ('5133', '5132', 1402);
INSERT INTO public.base_city_adjacent VALUES ('5132', '6212', 1403);
INSERT INTO public.base_city_adjacent VALUES ('6212', '5132', 1404);
INSERT INTO public.base_city_adjacent VALUES ('5132', '6230', 1405);
INSERT INTO public.base_city_adjacent VALUES ('6230', '5132', 1406);
INSERT INTO public.base_city_adjacent VALUES ('5132', '6326', 1407);
INSERT INTO public.base_city_adjacent VALUES ('6326', '5132', 1408);
INSERT INTO public.base_city_adjacent VALUES ('5133', '5134', 1409);
INSERT INTO public.base_city_adjacent VALUES ('5134', '5133', 1410);
INSERT INTO public.base_city_adjacent VALUES ('5133', '5334', 1411);
INSERT INTO public.base_city_adjacent VALUES ('5334', '5133', 1412);
INSERT INTO public.base_city_adjacent VALUES ('5133', '5403', 1413);
INSERT INTO public.base_city_adjacent VALUES ('5403', '5133', 1414);
INSERT INTO public.base_city_adjacent VALUES ('5133', '6326', 1415);
INSERT INTO public.base_city_adjacent VALUES ('6326', '5133', 1416);
INSERT INTO public.base_city_adjacent VALUES ('5133', '6327', 1417);
INSERT INTO public.base_city_adjacent VALUES ('6327', '5133', 1418);
INSERT INTO public.base_city_adjacent VALUES ('5134', '5301', 1419);
INSERT INTO public.base_city_adjacent VALUES ('5301', '5134', 1420);
INSERT INTO public.base_city_adjacent VALUES ('5134', '5306', 1421);
INSERT INTO public.base_city_adjacent VALUES ('5306', '5134', 1422);
INSERT INTO public.base_city_adjacent VALUES ('5134', '5307', 1423);
INSERT INTO public.base_city_adjacent VALUES ('5307', '5134', 1424);
INSERT INTO public.base_city_adjacent VALUES ('5134', '5323', 1425);
INSERT INTO public.base_city_adjacent VALUES ('5323', '5134', 1426);
INSERT INTO public.base_city_adjacent VALUES ('5134', '5334', 1427);
INSERT INTO public.base_city_adjacent VALUES ('5334', '5134', 1428);
INSERT INTO public.base_city_adjacent VALUES ('5201', '5203', 1429);
INSERT INTO public.base_city_adjacent VALUES ('5203', '5201', 1430);
INSERT INTO public.base_city_adjacent VALUES ('5201', '5204', 1431);
INSERT INTO public.base_city_adjacent VALUES ('5204', '5201', 1432);
INSERT INTO public.base_city_adjacent VALUES ('5201', '5205', 1433);
INSERT INTO public.base_city_adjacent VALUES ('5205', '5201', 1434);
INSERT INTO public.base_city_adjacent VALUES ('5201', '5227', 1435);
INSERT INTO public.base_city_adjacent VALUES ('5227', '5201', 1436);
INSERT INTO public.base_city_adjacent VALUES ('5202', '5204', 1437);
INSERT INTO public.base_city_adjacent VALUES ('5204', '5202', 1438);
INSERT INTO public.base_city_adjacent VALUES ('5202', '5205', 1439);
INSERT INTO public.base_city_adjacent VALUES ('5205', '5202', 1440);
INSERT INTO public.base_city_adjacent VALUES ('5202', '5223', 1441);
INSERT INTO public.base_city_adjacent VALUES ('5223', '5202', 1442);
INSERT INTO public.base_city_adjacent VALUES ('5202', '5303', 1443);
INSERT INTO public.base_city_adjacent VALUES ('5303', '5202', 1444);
INSERT INTO public.base_city_adjacent VALUES ('5203', '5205', 1445);
INSERT INTO public.base_city_adjacent VALUES ('5205', '5203', 1446);
INSERT INTO public.base_city_adjacent VALUES ('5203', '5206', 1447);
INSERT INTO public.base_city_adjacent VALUES ('5206', '5203', 1448);
INSERT INTO public.base_city_adjacent VALUES ('5203', '5226', 1449);
INSERT INTO public.base_city_adjacent VALUES ('5226', '5203', 1450);
INSERT INTO public.base_city_adjacent VALUES ('5203', '5227', 1451);
INSERT INTO public.base_city_adjacent VALUES ('5227', '5203', 1452);
INSERT INTO public.base_city_adjacent VALUES ('5204', '5205', 1453);
INSERT INTO public.base_city_adjacent VALUES ('5205', '5204', 1454);
INSERT INTO public.base_city_adjacent VALUES ('5204', '5223', 1455);
INSERT INTO public.base_city_adjacent VALUES ('5223', '5204', 1456);
INSERT INTO public.base_city_adjacent VALUES ('5204', '5227', 1457);
INSERT INTO public.base_city_adjacent VALUES ('5227', '5204', 1458);
INSERT INTO public.base_city_adjacent VALUES ('5205', '5303', 1459);
INSERT INTO public.base_city_adjacent VALUES ('5303', '5205', 1460);
INSERT INTO public.base_city_adjacent VALUES ('5205', '5306', 1461);
INSERT INTO public.base_city_adjacent VALUES ('5306', '5205', 1462);
INSERT INTO public.base_city_adjacent VALUES ('5206', '5226', 1463);
INSERT INTO public.base_city_adjacent VALUES ('5226', '5206', 1464);
INSERT INTO public.base_city_adjacent VALUES ('5223', '5227', 1465);
INSERT INTO public.base_city_adjacent VALUES ('5227', '5223', 1466);
INSERT INTO public.base_city_adjacent VALUES ('5223', '5303', 1467);
INSERT INTO public.base_city_adjacent VALUES ('5303', '5223', 1468);
INSERT INTO public.base_city_adjacent VALUES ('5226', '5227', 1469);
INSERT INTO public.base_city_adjacent VALUES ('5227', '5226', 1470);
INSERT INTO public.base_city_adjacent VALUES ('5301', '5303', 1471);
INSERT INTO public.base_city_adjacent VALUES ('5303', '5301', 1472);
INSERT INTO public.base_city_adjacent VALUES ('5301', '5304', 1473);
INSERT INTO public.base_city_adjacent VALUES ('5304', '5301', 1474);
INSERT INTO public.base_city_adjacent VALUES ('5301', '5306', 1475);
INSERT INTO public.base_city_adjacent VALUES ('5306', '5301', 1476);
INSERT INTO public.base_city_adjacent VALUES ('5301', '5323', 1477);
INSERT INTO public.base_city_adjacent VALUES ('5323', '5301', 1478);
INSERT INTO public.base_city_adjacent VALUES ('5301', '5325', 1479);
INSERT INTO public.base_city_adjacent VALUES ('5325', '5301', 1480);
INSERT INTO public.base_city_adjacent VALUES ('5303', '5306', 1481);
INSERT INTO public.base_city_adjacent VALUES ('5306', '5303', 1482);
INSERT INTO public.base_city_adjacent VALUES ('5303', '5325', 1483);
INSERT INTO public.base_city_adjacent VALUES ('5325', '5303', 1484);
INSERT INTO public.base_city_adjacent VALUES ('5303', '5326', 1485);
INSERT INTO public.base_city_adjacent VALUES ('5326', '5303', 1486);
INSERT INTO public.base_city_adjacent VALUES ('5304', '5308', 1487);
INSERT INTO public.base_city_adjacent VALUES ('5308', '5304', 1488);
INSERT INTO public.base_city_adjacent VALUES ('5304', '5323', 1489);
INSERT INTO public.base_city_adjacent VALUES ('5323', '5304', 1490);
INSERT INTO public.base_city_adjacent VALUES ('5304', '5325', 1491);
INSERT INTO public.base_city_adjacent VALUES ('5325', '5304', 1492);
INSERT INTO public.base_city_adjacent VALUES ('5305', '5309', 1493);
INSERT INTO public.base_city_adjacent VALUES ('5309', '5305', 1494);
INSERT INTO public.base_city_adjacent VALUES ('5305', '5329', 1495);
INSERT INTO public.base_city_adjacent VALUES ('5329', '5305', 1496);
INSERT INTO public.base_city_adjacent VALUES ('5305', '5331', 1497);
INSERT INTO public.base_city_adjacent VALUES ('5331', '5305', 1498);
INSERT INTO public.base_city_adjacent VALUES ('5305', '5333', 1499);
INSERT INTO public.base_city_adjacent VALUES ('5333', '5305', 1500);
INSERT INTO public.base_city_adjacent VALUES ('5307', '5323', 1501);
INSERT INTO public.base_city_adjacent VALUES ('5323', '5307', 1502);
INSERT INTO public.base_city_adjacent VALUES ('5307', '5329', 1503);
INSERT INTO public.base_city_adjacent VALUES ('5329', '5307', 1504);
INSERT INTO public.base_city_adjacent VALUES ('5307', '5333', 1505);
INSERT INTO public.base_city_adjacent VALUES ('5333', '5307', 1506);
INSERT INTO public.base_city_adjacent VALUES ('5307', '5334', 1507);
INSERT INTO public.base_city_adjacent VALUES ('5334', '5307', 1508);
INSERT INTO public.base_city_adjacent VALUES ('5308', '5309', 1509);
INSERT INTO public.base_city_adjacent VALUES ('5309', '5308', 1510);
INSERT INTO public.base_city_adjacent VALUES ('5308', '5323', 1511);
INSERT INTO public.base_city_adjacent VALUES ('5323', '5308', 1512);
INSERT INTO public.base_city_adjacent VALUES ('5308', '5325', 1513);
INSERT INTO public.base_city_adjacent VALUES ('5325', '5308', 1514);
INSERT INTO public.base_city_adjacent VALUES ('5308', '5328', 1515);
INSERT INTO public.base_city_adjacent VALUES ('5328', '5308', 1516);
INSERT INTO public.base_city_adjacent VALUES ('5308', '5329', 1517);
INSERT INTO public.base_city_adjacent VALUES ('5329', '5308', 1518);
INSERT INTO public.base_city_adjacent VALUES ('5309', '5329', 1519);
INSERT INTO public.base_city_adjacent VALUES ('5329', '5309', 1520);
INSERT INTO public.base_city_adjacent VALUES ('5323', '5329', 1521);
INSERT INTO public.base_city_adjacent VALUES ('5329', '5323', 1522);
INSERT INTO public.base_city_adjacent VALUES ('5325', '5326', 1523);
INSERT INTO public.base_city_adjacent VALUES ('5326', '5325', 1524);
INSERT INTO public.base_city_adjacent VALUES ('5329', '5333', 1525);
INSERT INTO public.base_city_adjacent VALUES ('5333', '5329', 1526);
INSERT INTO public.base_city_adjacent VALUES ('5333', '5334', 1527);
INSERT INTO public.base_city_adjacent VALUES ('5334', '5333', 1528);
INSERT INTO public.base_city_adjacent VALUES ('5333', '5404', 1529);
INSERT INTO public.base_city_adjacent VALUES ('5404', '5333', 1530);
INSERT INTO public.base_city_adjacent VALUES ('5334', '5403', 1531);
INSERT INTO public.base_city_adjacent VALUES ('5403', '5334', 1532);
INSERT INTO public.base_city_adjacent VALUES ('5334', '5404', 1533);
INSERT INTO public.base_city_adjacent VALUES ('5404', '5334', 1534);
INSERT INTO public.base_city_adjacent VALUES ('5401', '5402', 1535);
INSERT INTO public.base_city_adjacent VALUES ('5402', '5401', 1536);
INSERT INTO public.base_city_adjacent VALUES ('5401', '5404', 1537);
INSERT INTO public.base_city_adjacent VALUES ('5404', '5401', 1538);
INSERT INTO public.base_city_adjacent VALUES ('5401', '5405', 1539);
INSERT INTO public.base_city_adjacent VALUES ('5405', '5401', 1540);
INSERT INTO public.base_city_adjacent VALUES ('5401', '5406', 1541);
INSERT INTO public.base_city_adjacent VALUES ('5406', '5401', 1542);
INSERT INTO public.base_city_adjacent VALUES ('5402', '5405', 1543);
INSERT INTO public.base_city_adjacent VALUES ('5405', '5402', 1544);
INSERT INTO public.base_city_adjacent VALUES ('5402', '5406', 1545);
INSERT INTO public.base_city_adjacent VALUES ('5406', '5402', 1546);
INSERT INTO public.base_city_adjacent VALUES ('5402', '5425', 1547);
INSERT INTO public.base_city_adjacent VALUES ('5425', '5402', 1548);
INSERT INTO public.base_city_adjacent VALUES ('5403', '5404', 1549);
INSERT INTO public.base_city_adjacent VALUES ('5404', '5403', 1550);
INSERT INTO public.base_city_adjacent VALUES ('5403', '5406', 1551);
INSERT INTO public.base_city_adjacent VALUES ('5406', '5403', 1552);
INSERT INTO public.base_city_adjacent VALUES ('5403', '6327', 1553);
INSERT INTO public.base_city_adjacent VALUES ('6327', '5403', 1554);
INSERT INTO public.base_city_adjacent VALUES ('5404', '5405', 1555);
INSERT INTO public.base_city_adjacent VALUES ('5405', '5404', 1556);
INSERT INTO public.base_city_adjacent VALUES ('5404', '5406', 1557);
INSERT INTO public.base_city_adjacent VALUES ('5406', '5404', 1558);
INSERT INTO public.base_city_adjacent VALUES ('5406', '5425', 1559);
INSERT INTO public.base_city_adjacent VALUES ('5425', '5406', 1560);
INSERT INTO public.base_city_adjacent VALUES ('5406', '6327', 1561);
INSERT INTO public.base_city_adjacent VALUES ('6327', '5406', 1562);
INSERT INTO public.base_city_adjacent VALUES ('5406', '6328', 1563);
INSERT INTO public.base_city_adjacent VALUES ('6328', '5406', 1564);
INSERT INTO public.base_city_adjacent VALUES ('5406', '6528', 1565);
INSERT INTO public.base_city_adjacent VALUES ('6528', '5406', 1566);
INSERT INTO public.base_city_adjacent VALUES ('5425', '6528', 1567);
INSERT INTO public.base_city_adjacent VALUES ('6528', '5425', 1568);
INSERT INTO public.base_city_adjacent VALUES ('5425', '6532', 1569);
INSERT INTO public.base_city_adjacent VALUES ('6532', '5425', 1570);
INSERT INTO public.base_city_adjacent VALUES ('6101', '6103', 1571);
INSERT INTO public.base_city_adjacent VALUES ('6103', '6101', 1572);
INSERT INTO public.base_city_adjacent VALUES ('6101', '6104', 1573);
INSERT INTO public.base_city_adjacent VALUES ('6104', '6101', 1574);
INSERT INTO public.base_city_adjacent VALUES ('6101', '6105', 1575);
INSERT INTO public.base_city_adjacent VALUES ('6105', '6101', 1576);
INSERT INTO public.base_city_adjacent VALUES ('6101', '6107', 1577);
INSERT INTO public.base_city_adjacent VALUES ('6107', '6101', 1578);
INSERT INTO public.base_city_adjacent VALUES ('6101', '6109', 1579);
INSERT INTO public.base_city_adjacent VALUES ('6109', '6101', 1580);
INSERT INTO public.base_city_adjacent VALUES ('6101', '6110', 1581);
INSERT INTO public.base_city_adjacent VALUES ('6110', '6101', 1582);
INSERT INTO public.base_city_adjacent VALUES ('6102', '6104', 1583);
INSERT INTO public.base_city_adjacent VALUES ('6104', '6102', 1584);
INSERT INTO public.base_city_adjacent VALUES ('6102', '6105', 1585);
INSERT INTO public.base_city_adjacent VALUES ('6105', '6102', 1586);
INSERT INTO public.base_city_adjacent VALUES ('6102', '6106', 1587);
INSERT INTO public.base_city_adjacent VALUES ('6106', '6102', 1588);
INSERT INTO public.base_city_adjacent VALUES ('6103', '6104', 1589);
INSERT INTO public.base_city_adjacent VALUES ('6104', '6103', 1590);
INSERT INTO public.base_city_adjacent VALUES ('6103', '6107', 1591);
INSERT INTO public.base_city_adjacent VALUES ('6107', '6103', 1592);
INSERT INTO public.base_city_adjacent VALUES ('6103', '6205', 1593);
INSERT INTO public.base_city_adjacent VALUES ('6205', '6103', 1594);
INSERT INTO public.base_city_adjacent VALUES ('6103', '6208', 1595);
INSERT INTO public.base_city_adjacent VALUES ('6208', '6103', 1596);
INSERT INTO public.base_city_adjacent VALUES ('6103', '6212', 1597);
INSERT INTO public.base_city_adjacent VALUES ('6212', '6103', 1598);
INSERT INTO public.base_city_adjacent VALUES ('6104', '6105', 1599);
INSERT INTO public.base_city_adjacent VALUES ('6105', '6104', 1600);
INSERT INTO public.base_city_adjacent VALUES ('6104', '6106', 1601);
INSERT INTO public.base_city_adjacent VALUES ('6106', '6104', 1602);
INSERT INTO public.base_city_adjacent VALUES ('6104', '6208', 1603);
INSERT INTO public.base_city_adjacent VALUES ('6208', '6104', 1604);
INSERT INTO public.base_city_adjacent VALUES ('6104', '6210', 1605);
INSERT INTO public.base_city_adjacent VALUES ('6210', '6104', 1606);
INSERT INTO public.base_city_adjacent VALUES ('6105', '6106', 1607);
INSERT INTO public.base_city_adjacent VALUES ('6106', '6105', 1608);
INSERT INTO public.base_city_adjacent VALUES ('6105', '6110', 1609);
INSERT INTO public.base_city_adjacent VALUES ('6110', '6105', 1610);
INSERT INTO public.base_city_adjacent VALUES ('6106', '6108', 1611);
INSERT INTO public.base_city_adjacent VALUES ('6108', '6106', 1612);
INSERT INTO public.base_city_adjacent VALUES ('6106', '6210', 1613);
INSERT INTO public.base_city_adjacent VALUES ('6210', '6106', 1614);
INSERT INTO public.base_city_adjacent VALUES ('6107', '6109', 1615);
INSERT INTO public.base_city_adjacent VALUES ('6109', '6107', 1616);
INSERT INTO public.base_city_adjacent VALUES ('6107', '6212', 1617);
INSERT INTO public.base_city_adjacent VALUES ('6212', '6107', 1618);
INSERT INTO public.base_city_adjacent VALUES ('6108', '6210', 1619);
INSERT INTO public.base_city_adjacent VALUES ('6210', '6108', 1620);
INSERT INTO public.base_city_adjacent VALUES ('6108', '6403', 1621);
INSERT INTO public.base_city_adjacent VALUES ('6403', '6108', 1622);
INSERT INTO public.base_city_adjacent VALUES ('6109', '6110', 1623);
INSERT INTO public.base_city_adjacent VALUES ('6110', '6109', 1624);
INSERT INTO public.base_city_adjacent VALUES ('6201', '6204', 1625);
INSERT INTO public.base_city_adjacent VALUES ('6204', '6201', 1626);
INSERT INTO public.base_city_adjacent VALUES ('6201', '6206', 1627);
INSERT INTO public.base_city_adjacent VALUES ('6206', '6201', 1628);
INSERT INTO public.base_city_adjacent VALUES ('6201', '6211', 1629);
INSERT INTO public.base_city_adjacent VALUES ('6211', '6201', 1630);
INSERT INTO public.base_city_adjacent VALUES ('6201', '6229', 1631);
INSERT INTO public.base_city_adjacent VALUES ('6229', '6201', 1632);
INSERT INTO public.base_city_adjacent VALUES ('6201', '6302', 1633);
INSERT INTO public.base_city_adjacent VALUES ('6302', '6201', 1634);
INSERT INTO public.base_city_adjacent VALUES ('6202', '6207', 1635);
INSERT INTO public.base_city_adjacent VALUES ('6207', '6202', 1636);
INSERT INTO public.base_city_adjacent VALUES ('6202', '6209', 1637);
INSERT INTO public.base_city_adjacent VALUES ('6209', '6202', 1638);
INSERT INTO public.base_city_adjacent VALUES ('6203', '6206', 1639);
INSERT INTO public.base_city_adjacent VALUES ('6206', '6203', 1640);
INSERT INTO public.base_city_adjacent VALUES ('6203', '6207', 1641);
INSERT INTO public.base_city_adjacent VALUES ('6207', '6203', 1642);
INSERT INTO public.base_city_adjacent VALUES ('6204', '6206', 1643);
INSERT INTO public.base_city_adjacent VALUES ('6206', '6204', 1644);
INSERT INTO public.base_city_adjacent VALUES ('6204', '6208', 1645);
INSERT INTO public.base_city_adjacent VALUES ('6208', '6204', 1646);
INSERT INTO public.base_city_adjacent VALUES ('6204', '6211', 1647);
INSERT INTO public.base_city_adjacent VALUES ('6211', '6204', 1648);
INSERT INTO public.base_city_adjacent VALUES ('6204', '6404', 1649);
INSERT INTO public.base_city_adjacent VALUES ('6404', '6204', 1650);
INSERT INTO public.base_city_adjacent VALUES ('6204', '6405', 1651);
INSERT INTO public.base_city_adjacent VALUES ('6405', '6204', 1652);
INSERT INTO public.base_city_adjacent VALUES ('6205', '6208', 1653);
INSERT INTO public.base_city_adjacent VALUES ('6208', '6205', 1654);
INSERT INTO public.base_city_adjacent VALUES ('6205', '6211', 1655);
INSERT INTO public.base_city_adjacent VALUES ('6211', '6205', 1656);
INSERT INTO public.base_city_adjacent VALUES ('6205', '6212', 1657);
INSERT INTO public.base_city_adjacent VALUES ('6212', '6205', 1658);
INSERT INTO public.base_city_adjacent VALUES ('6206', '6207', 1659);
INSERT INTO public.base_city_adjacent VALUES ('6207', '6206', 1660);
INSERT INTO public.base_city_adjacent VALUES ('6206', '6302', 1661);
INSERT INTO public.base_city_adjacent VALUES ('6302', '6206', 1662);
INSERT INTO public.base_city_adjacent VALUES ('6206', '6322', 1663);
INSERT INTO public.base_city_adjacent VALUES ('6322', '6206', 1664);
INSERT INTO public.base_city_adjacent VALUES ('6207', '6209', 1665);
INSERT INTO public.base_city_adjacent VALUES ('6209', '6207', 1666);
INSERT INTO public.base_city_adjacent VALUES ('6207', '6322', 1667);
INSERT INTO public.base_city_adjacent VALUES ('6322', '6207', 1668);
INSERT INTO public.base_city_adjacent VALUES ('6207', '6328', 1669);
INSERT INTO public.base_city_adjacent VALUES ('6328', '6207', 1670);
INSERT INTO public.base_city_adjacent VALUES ('6208', '6210', 1671);
INSERT INTO public.base_city_adjacent VALUES ('6210', '6208', 1672);
INSERT INTO public.base_city_adjacent VALUES ('6208', '6211', 1673);
INSERT INTO public.base_city_adjacent VALUES ('6211', '6208', 1674);
INSERT INTO public.base_city_adjacent VALUES ('6208', '6404', 1675);
INSERT INTO public.base_city_adjacent VALUES ('6404', '6208', 1676);
INSERT INTO public.base_city_adjacent VALUES ('6209', '6328', 1677);
INSERT INTO public.base_city_adjacent VALUES ('6328', '6209', 1678);
INSERT INTO public.base_city_adjacent VALUES ('6209', '6505', 1679);
INSERT INTO public.base_city_adjacent VALUES ('6505', '6209', 1680);
INSERT INTO public.base_city_adjacent VALUES ('6209', '6528', 1681);
INSERT INTO public.base_city_adjacent VALUES ('6528', '6209', 1682);
INSERT INTO public.base_city_adjacent VALUES ('6210', '6403', 1683);
INSERT INTO public.base_city_adjacent VALUES ('6403', '6210', 1684);
INSERT INTO public.base_city_adjacent VALUES ('6210', '6404', 1685);
INSERT INTO public.base_city_adjacent VALUES ('6404', '6210', 1686);
INSERT INTO public.base_city_adjacent VALUES ('6210', '6405', 1687);
INSERT INTO public.base_city_adjacent VALUES ('6405', '6210', 1688);
INSERT INTO public.base_city_adjacent VALUES ('6211', '6212', 1689);
INSERT INTO public.base_city_adjacent VALUES ('6212', '6211', 1690);
INSERT INTO public.base_city_adjacent VALUES ('6211', '6229', 1691);
INSERT INTO public.base_city_adjacent VALUES ('6229', '6211', 1692);
INSERT INTO public.base_city_adjacent VALUES ('6211', '6230', 1693);
INSERT INTO public.base_city_adjacent VALUES ('6230', '6211', 1694);
INSERT INTO public.base_city_adjacent VALUES ('6212', '6230', 1695);
INSERT INTO public.base_city_adjacent VALUES ('6230', '6212', 1696);
INSERT INTO public.base_city_adjacent VALUES ('6229', '6230', 1697);
INSERT INTO public.base_city_adjacent VALUES ('6230', '6229', 1698);
INSERT INTO public.base_city_adjacent VALUES ('6229', '6302', 1699);
INSERT INTO public.base_city_adjacent VALUES ('6302', '6229', 1700);
INSERT INTO public.base_city_adjacent VALUES ('6230', '6302', 1701);
INSERT INTO public.base_city_adjacent VALUES ('6302', '6230', 1702);
INSERT INTO public.base_city_adjacent VALUES ('6230', '6323', 1703);
INSERT INTO public.base_city_adjacent VALUES ('6323', '6230', 1704);
INSERT INTO public.base_city_adjacent VALUES ('6230', '6326', 1705);
INSERT INTO public.base_city_adjacent VALUES ('6326', '6230', 1706);
INSERT INTO public.base_city_adjacent VALUES ('6301', '6302', 1707);
INSERT INTO public.base_city_adjacent VALUES ('6302', '6301', 1708);
INSERT INTO public.base_city_adjacent VALUES ('6301', '6322', 1709);
INSERT INTO public.base_city_adjacent VALUES ('6322', '6301', 1710);
INSERT INTO public.base_city_adjacent VALUES ('6301', '6325', 1711);
INSERT INTO public.base_city_adjacent VALUES ('6325', '6301', 1712);
INSERT INTO public.base_city_adjacent VALUES ('6302', '6322', 1713);
INSERT INTO public.base_city_adjacent VALUES ('6322', '6302', 1714);
INSERT INTO public.base_city_adjacent VALUES ('6302', '6323', 1715);
INSERT INTO public.base_city_adjacent VALUES ('6323', '6302', 1716);
INSERT INTO public.base_city_adjacent VALUES ('6302', '6325', 1717);
INSERT INTO public.base_city_adjacent VALUES ('6325', '6302', 1718);
INSERT INTO public.base_city_adjacent VALUES ('6322', '6325', 1719);
INSERT INTO public.base_city_adjacent VALUES ('6325', '6322', 1720);
INSERT INTO public.base_city_adjacent VALUES ('6322', '6328', 1721);
INSERT INTO public.base_city_adjacent VALUES ('6328', '6322', 1722);
INSERT INTO public.base_city_adjacent VALUES ('6323', '6325', 1723);
INSERT INTO public.base_city_adjacent VALUES ('6325', '6323', 1724);
INSERT INTO public.base_city_adjacent VALUES ('6323', '6326', 1725);
INSERT INTO public.base_city_adjacent VALUES ('6326', '6323', 1726);
INSERT INTO public.base_city_adjacent VALUES ('6325', '6326', 1727);
INSERT INTO public.base_city_adjacent VALUES ('6326', '6325', 1728);
INSERT INTO public.base_city_adjacent VALUES ('6325', '6328', 1729);
INSERT INTO public.base_city_adjacent VALUES ('6328', '6325', 1730);
INSERT INTO public.base_city_adjacent VALUES ('6326', '6327', 1731);
INSERT INTO public.base_city_adjacent VALUES ('6327', '6326', 1732);
INSERT INTO public.base_city_adjacent VALUES ('6326', '6328', 1733);
INSERT INTO public.base_city_adjacent VALUES ('6328', '6326', 1734);
INSERT INTO public.base_city_adjacent VALUES ('6327', '6328', 1735);
INSERT INTO public.base_city_adjacent VALUES ('6328', '6327', 1736);
INSERT INTO public.base_city_adjacent VALUES ('6327', '6528', 1737);
INSERT INTO public.base_city_adjacent VALUES ('6528', '6327', 1738);
INSERT INTO public.base_city_adjacent VALUES ('6328', '6528', 1739);
INSERT INTO public.base_city_adjacent VALUES ('6528', '6328', 1740);
INSERT INTO public.base_city_adjacent VALUES ('6401', '6402', 1741);
INSERT INTO public.base_city_adjacent VALUES ('6402', '6401', 1742);
INSERT INTO public.base_city_adjacent VALUES ('6401', '6403', 1743);
INSERT INTO public.base_city_adjacent VALUES ('6403', '6401', 1744);
INSERT INTO public.base_city_adjacent VALUES ('6403', '6405', 1745);
INSERT INTO public.base_city_adjacent VALUES ('6405', '6403', 1746);
INSERT INTO public.base_city_adjacent VALUES ('6404', '6405', 1747);
INSERT INTO public.base_city_adjacent VALUES ('6405', '6404', 1748);
INSERT INTO public.base_city_adjacent VALUES ('6501', '6504', 1749);
INSERT INTO public.base_city_adjacent VALUES ('6504', '6501', 1750);
INSERT INTO public.base_city_adjacent VALUES ('6501', '6523', 1751);
INSERT INTO public.base_city_adjacent VALUES ('6523', '6501', 1752);
INSERT INTO public.base_city_adjacent VALUES ('6501', '6528', 1753);
INSERT INTO public.base_city_adjacent VALUES ('6528', '6501', 1754);
INSERT INTO public.base_city_adjacent VALUES ('6501', '6543', 1755);
INSERT INTO public.base_city_adjacent VALUES ('6543', '6501', 1756);
INSERT INTO public.base_city_adjacent VALUES ('6502', '6540', 1757);
INSERT INTO public.base_city_adjacent VALUES ('6540', '6502', 1758);
INSERT INTO public.base_city_adjacent VALUES ('6502', '6542', 1759);
INSERT INTO public.base_city_adjacent VALUES ('6542', '6502', 1760);
INSERT INTO public.base_city_adjacent VALUES ('6504', '6505', 1761);
INSERT INTO public.base_city_adjacent VALUES ('6505', '6504', 1762);
INSERT INTO public.base_city_adjacent VALUES ('6504', '6523', 1763);
INSERT INTO public.base_city_adjacent VALUES ('6523', '6504', 1764);
INSERT INTO public.base_city_adjacent VALUES ('6504', '6528', 1765);
INSERT INTO public.base_city_adjacent VALUES ('6528', '6504', 1766);
INSERT INTO public.base_city_adjacent VALUES ('6505', '6523', 1767);
INSERT INTO public.base_city_adjacent VALUES ('6523', '6505', 1768);
INSERT INTO public.base_city_adjacent VALUES ('6505', '6528', 1769);
INSERT INTO public.base_city_adjacent VALUES ('6528', '6505', 1770);
INSERT INTO public.base_city_adjacent VALUES ('6523', '6528', 1771);
INSERT INTO public.base_city_adjacent VALUES ('6528', '6523', 1772);
INSERT INTO public.base_city_adjacent VALUES ('6523', '6542', 1773);
INSERT INTO public.base_city_adjacent VALUES ('6542', '6523', 1774);
INSERT INTO public.base_city_adjacent VALUES ('6523', '6543', 1775);
INSERT INTO public.base_city_adjacent VALUES ('6543', '6523', 1776);
INSERT INTO public.base_city_adjacent VALUES ('6523', '6590', 1777);
INSERT INTO public.base_city_adjacent VALUES ('6590', '6523', 1778);
INSERT INTO public.base_city_adjacent VALUES ('6527', '6540', 1779);
INSERT INTO public.base_city_adjacent VALUES ('6540', '6527', 1780);
INSERT INTO public.base_city_adjacent VALUES ('6527', '6542', 1781);
INSERT INTO public.base_city_adjacent VALUES ('6542', '6527', 1782);
INSERT INTO public.base_city_adjacent VALUES ('6528', '6529', 1783);
INSERT INTO public.base_city_adjacent VALUES ('6529', '6528', 1784);
INSERT INTO public.base_city_adjacent VALUES ('6528', '6532', 1785);
INSERT INTO public.base_city_adjacent VALUES ('6532', '6528', 1786);
INSERT INTO public.base_city_adjacent VALUES ('6528', '6540', 1787);
INSERT INTO public.base_city_adjacent VALUES ('6540', '6528', 1788);
INSERT INTO public.base_city_adjacent VALUES ('6528', '6542', 1789);
INSERT INTO public.base_city_adjacent VALUES ('6542', '6528', 1790);
INSERT INTO public.base_city_adjacent VALUES ('6529', '6530', 1791);
INSERT INTO public.base_city_adjacent VALUES ('6530', '6529', 1792);
INSERT INTO public.base_city_adjacent VALUES ('6529', '6531', 1793);
INSERT INTO public.base_city_adjacent VALUES ('6531', '6529', 1794);
INSERT INTO public.base_city_adjacent VALUES ('6529', '6532', 1795);
INSERT INTO public.base_city_adjacent VALUES ('6532', '6529', 1796);
INSERT INTO public.base_city_adjacent VALUES ('6529', '6540', 1797);
INSERT INTO public.base_city_adjacent VALUES ('6540', '6529', 1798);
INSERT INTO public.base_city_adjacent VALUES ('6530', '6531', 1799);
INSERT INTO public.base_city_adjacent VALUES ('6531', '6530', 1800);
INSERT INTO public.base_city_adjacent VALUES ('6531', '6532', 1801);
INSERT INTO public.base_city_adjacent VALUES ('6532', '6531', 1802);
INSERT INTO public.base_city_adjacent VALUES ('6540', '6542', 1803);
INSERT INTO public.base_city_adjacent VALUES ('6542', '6540', 1804);
INSERT INTO public.base_city_adjacent VALUES ('6542', '6543', 1805);
INSERT INTO public.base_city_adjacent VALUES ('6543', '6542', 1806);
INSERT INTO public.base_city_adjacent VALUES ('6542', '6590', 1807);
INSERT INTO public.base_city_adjacent VALUES ('6590', '6542', 1808);


--
-- Data for Name: base_province; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.base_province VALUES ('11', '北京市');
INSERT INTO public.base_province VALUES ('12', '天津市');
INSERT INTO public.base_province VALUES ('13', '河北省');
INSERT INTO public.base_province VALUES ('14', '山西省');
INSERT INTO public.base_province VALUES ('15', '内蒙古自治区');
INSERT INTO public.base_province VALUES ('21', '辽宁省');
INSERT INTO public.base_province VALUES ('22', '吉林省');
INSERT INTO public.base_province VALUES ('23', '黑龙江省');
INSERT INTO public.base_province VALUES ('31', '上海市');
INSERT INTO public.base_province VALUES ('32', '江苏省');
INSERT INTO public.base_province VALUES ('33', '浙江省');
INSERT INTO public.base_province VALUES ('34', '安徽省');
INSERT INTO public.base_province VALUES ('35', '福建省');
INSERT INTO public.base_province VALUES ('36', '江西省');
INSERT INTO public.base_province VALUES ('37', '山东省');
INSERT INTO public.base_province VALUES ('41', '河南省');
INSERT INTO public.base_province VALUES ('42', '湖北省');
INSERT INTO public.base_province VALUES ('43', '湖南省');
INSERT INTO public.base_province VALUES ('44', '广东省');
INSERT INTO public.base_province VALUES ('45', '广西壮族自治区');
INSERT INTO public.base_province VALUES ('46', '海南省');
INSERT INTO public.base_province VALUES ('50', '重庆市');
INSERT INTO public.base_province VALUES ('51', '四川省');
INSERT INTO public.base_province VALUES ('52', '贵州省');
INSERT INTO public.base_province VALUES ('53', '云南省');
INSERT INTO public.base_province VALUES ('54', '西藏自治区');
INSERT INTO public.base_province VALUES ('61', '陕西省');
INSERT INTO public.base_province VALUES ('62', '甘肃省');
INSERT INTO public.base_province VALUES ('63', '青海省');
INSERT INTO public.base_province VALUES ('64', '宁夏回族自治区');
INSERT INTO public.base_province VALUES ('65', '新疆维吾尔自治区');


--
-- Data for Name: base_street; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: base_user_protocol; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: base_user_protocol (整表清除)


--
-- Data for Name: base_user_protocol_version; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: base_user_protocol_version (整表清除)


--
-- Data for Name: device_qr_code; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: device_qr_code (整表清除)


--
-- Data for Name: douyin_direct_alloc_receiver; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: douyin_direct_alloc_receiver (整表清除)


--
-- Data for Name: douyin_direct_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: douyin_direct_channel_merchant (整表清除)


--
-- Data for Name: douyin_direct_key_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: douyin_direct_key_config (整表清除)


--
-- Data for Name: douyin_transfer_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: douyin_transfer_config (整表清除)


--
-- Data for Name: dy_channel_app_capability; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: dy_mch_app; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: dy_mch_app (整表清除)


--
-- Data for Name: dy_platform_app; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: dy_platform_app_capability; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: fuyou_isv_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: fuyou_isv_key_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: hkrt_isv_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: hkrt_isv_key_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: hmpay_isv_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: hmpay_isv_key_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: iam_perm_code; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.iam_perm_code VALUES (2080304418086137856, 'payment:wx:mch-app:manage', 'payment:wx:mch-app', true, NULL, 1, 1, 0, false, '2026-07-23 14:49:58.118592+00', '2026-07-23 14:49:58.125608+00', 'perm.payment:wx:mch-app:manage');
INSERT INTO public.iam_perm_code VALUES (2080304418371350528, 'payment:wx:mch-app:view', 'payment:wx:mch-app', true, NULL, 1, 1, 0, false, '2026-07-23 14:49:58.183146+00', '2026-07-23 14:49:58.183146+00', 'perm.payment:wx:mch-app:view');
INSERT INTO public.iam_perm_code VALUES (2080304418371350529, 'payment:wx:platform-app:manage', 'payment:wx:platform-app', true, NULL, 1, 1, 0, false, '2026-07-23 14:49:58.183662+00', '2026-07-23 14:49:58.183662+00', 'perm.payment:wx:platform-app:manage');
INSERT INTO public.iam_perm_code VALUES (2080304418371350530, 'payment:wx:platform-app:view', 'payment:wx:platform-app', true, NULL, 1, 1, 0, false, '2026-07-23 14:49:58.184182+00', '2026-07-23 14:49:58.184182+00', 'perm.payment:wx:platform-app:view');
INSERT INTO public.iam_perm_code VALUES (2083000000000000001, 'trade:transfer:view', 'trade:transfer', true, NULL, 1, 1, 0, false, '2026-08-05 12:13:20.611224+00', '2026-08-05 12:13:20.611224+00', 'perm.trade:transfer:view');
INSERT INTO public.iam_perm_code VALUES (2083000000000000002, 'trade:transfer:manage', 'trade:transfer', true, NULL, 1, 1, 0, false, '2026-08-05 12:13:20.613805+00', '2026-08-05 12:13:20.613805+00', 'perm.trade:transfer:manage');
INSERT INTO public.iam_perm_code VALUES (2076548114998755328, 'channel:app:manage', 'channel:app', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-13 06:03:45.674956+00', '2026-07-15 03:59:23.658548+00', 'perm.channel:app:manage');
INSERT INTO public.iam_perm_code VALUES (2076548115791478784, 'channel:app:view', 'channel:app', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-13 06:03:45.8603+00', '2026-07-15 03:59:23.675708+00', 'perm.channel:app:view');
INSERT INTO public.iam_perm_code VALUES (2070862264909631489, 'channel:merchant:manage', 'channel:merchant', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.37159+00', '2026-07-15 03:59:23.675708+00', 'perm.channel:merchant:manage');
INSERT INTO public.iam_perm_code VALUES (2070862264913825792, 'channel:merchant:view', 'channel:merchant', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.372587+00', '2026-07-15 03:59:23.675708+00', 'perm.channel:merchant:view');
INSERT INTO public.iam_perm_code VALUES (2070862264922214401, 'develop:sign:view', 'develop:sign', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.374587+00', '2026-07-15 03:59:23.677216+00', 'perm.develop:sign:view');
INSERT INTO public.iam_perm_code VALUES (2070862264930603008, 'develop:trade:sign', 'develop:trade', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.37609+00', '2026-07-15 03:59:23.678223+00', 'perm.develop:trade:sign');
INSERT INTO public.iam_perm_code VALUES (2070862264934797312, 'develop:trade:view', 'develop:trade', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.377094+00', '2026-07-15 03:59:23.678223+00', 'perm.develop:trade:view');
INSERT INTO public.iam_perm_code VALUES (2070862264964157440, 'iam:menu:manage', 'iam:menu', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.384096+00', '2026-07-15 03:59:23.680223+00', 'perm.iam:menu:manage');
INSERT INTO public.iam_perm_code VALUES (2070862264964157441, 'iam:menu:view', 'iam:menu', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.384096+00', '2026-07-15 03:59:23.680223+00', 'perm.iam:menu:view');
INSERT INTO public.iam_perm_code VALUES (2070862264955768832, 'iam:online:kickout', 'iam:online', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.382096+00', '2026-07-15 03:59:23.680223+00', 'perm.iam:online:kickout');
INSERT INTO public.iam_perm_code VALUES (2070862264955768833, 'iam:online:view', 'iam:online', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.383096+00', '2026-07-15 03:59:23.681729+00', 'perm.iam:online:view');
INSERT INTO public.iam_perm_code VALUES (2070862264968351744, 'iam:role:manage', 'iam:role', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.385095+00', '2026-07-15 03:59:23.681729+00', 'perm.iam:role:manage');
INSERT INTO public.iam_perm_code VALUES (2077568466990313472, 'payment:risk:blacklist:manage', 'payment:risk:blacklist', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-16 01:38:16.546135+00', '2026-07-16 01:38:16.553159+00', 'perm.payment:risk:blacklist:manage');
INSERT INTO public.iam_perm_code VALUES (2077568467208417280, 'payment:risk:blacklist:view', 'payment:risk:blacklist', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-16 01:38:16.59627+00', '2026-07-16 01:38:16.59627+00', 'perm.payment:risk:blacklist:view');
INSERT INTO public.iam_perm_code VALUES (2077568467216805888, 'payment:risk:hit:view', 'payment:risk:hit', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-16 01:38:16.597278+00', '2026-07-16 01:38:16.597278+00', 'perm.payment:risk:hit:view');
INSERT INTO public.iam_perm_code VALUES (2077568467221000192, 'system:sensitive-word-hit:view', 'system:sensitive-word-hit', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-16 01:38:16.598278+00', '2026-07-16 01:38:16.598278+00', 'perm.system:sensitive-word-hit:view');
INSERT INTO public.iam_perm_code VALUES (2077568467221000193, 'system:sensitive-word:manage', 'system:sensitive-word', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-16 01:38:16.598278+00', '2026-07-16 01:38:16.598278+00', 'perm.system:sensitive-word:manage');
INSERT INTO public.iam_perm_code VALUES (2077568467225194496, 'system:sensitive-word:view', 'system:sensitive-word', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-16 01:38:16.599276+00', '2026-07-16 01:38:16.599276+00', 'perm.system:sensitive-word:view');
INSERT INTO public.iam_perm_code VALUES (2080921585542385664, 'merchant:alipay-isv-auth:manage', 'merchant:alipay-isv-auth', true, NULL, 1, 1, 0, false, '2026-07-25 07:42:22.305327+00', '2026-07-25 07:42:22.309867+00', 'perm.merchant:alipay-isv-auth:manage');
INSERT INTO public.iam_perm_code VALUES (2080921585609494528, 'merchant:alipay-isv-auth:view', 'merchant:alipay-isv-auth', true, NULL, 1, 1, 0, false, '2026-07-25 07:42:22.31989+00', '2026-07-25 07:42:22.31989+00', 'perm.merchant:alipay-isv-auth:view');
INSERT INTO public.iam_perm_code VALUES (2077568468000000001, 'payment:risk:security:manage', 'payment:risk:security', true, '', 1, 1, 0, false, '2026-08-06 01:05:45.386753+00', '2026-08-06 01:05:45.386753+00', 'perm.payment:risk:security:manage');
INSERT INTO public.iam_perm_code VALUES (2077568468000000002, 'payment:risk:security:view', 'payment:risk:security', true, '', 1, 1, 0, false, '2026-08-06 01:05:45.389564+00', '2026-08-06 01:05:45.389564+00', 'perm.payment:risk:security:view');
INSERT INTO public.iam_perm_code VALUES (2072990657125986304, 'merchant:notify-config:view', 'merchant:notify-config', true, '由 @PermCode 扫描同步生成', 1, 1, 2, false, '2026-07-03 10:27:41.619775+00', '2026-07-15 03:59:23.693424+00', 'perm.merchant:notify-config:view');
INSERT INTO public.iam_perm_code VALUES (2070862265018683392, 'payment:config:product-config:manage', 'payment:config:product-config', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.397109+00', '2026-07-15 03:59:23.695427+00', 'perm.payment:config:product-config:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265022877696, 'payment:config:product-config:view', 'payment:config:product-config', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.398114+00', '2026-07-15 03:59:23.696424+00', 'perm.payment:config:product-config:view');
INSERT INTO public.iam_perm_code VALUES (2078479091642855424, 'develop:gateway:sign', 'develop:gateway', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-18 13:56:46.379702+00', '2026-07-18 13:56:46.383702+00', 'perm.develop:gateway:sign');
INSERT INTO public.iam_perm_code VALUES (2078479091684798466, 'system:log:unipay:manage', 'system:log:unipay', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-18 13:56:46.388212+00', '2026-07-18 13:56:46.388212+00', 'perm.system:log:unipay:manage');
INSERT INTO public.iam_perm_code VALUES (2078479091688992768, 'system:log:unipay:view', 'system:log:unipay', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-18 13:56:46.389213+00', '2026-07-18 13:56:46.389213+00', 'perm.system:log:unipay:view');
INSERT INTO public.iam_perm_code VALUES (2075452237802512384, 'develop:auth:view', 'develop:auth', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-10 05:29:08.191101+00', '2026-07-15 03:59:23.677216+00', 'perm.develop:auth:view');
INSERT INTO public.iam_perm_code VALUES (2075452238549098496, 'device:qrcode:manage', 'device:qrcode', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-10 05:29:08.365787+00', '2026-07-15 03:59:23.679227+00', 'perm.device:qrcode:manage');
INSERT INTO public.iam_perm_code VALUES (2075452238553292800, 'device:qrcode:view', 'device:qrcode', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-10 05:29:08.367792+00', '2026-07-15 03:59:23.679227+00', 'perm.device:qrcode:view');
INSERT INTO public.iam_perm_code VALUES (2075452238557487104, 'iam:social:manage', 'iam:social', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-10 05:29:08.367792+00', '2026-07-15 03:59:23.682738+00', 'perm.iam:social:manage');
INSERT INTO public.iam_perm_code VALUES (2075452238561681408, 'iam:social:view', 'iam:social', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-10 05:29:08.368793+00', '2026-07-15 03:59:23.683738+00', 'perm.iam:social:view');
INSERT INTO public.iam_perm_code VALUES (2082124980798255104, 'payment:douyin:mch-app:manage', 'payment:douyin:mch-app', true, NULL, 1, 1, 0, false, '2026-07-28 15:24:14.093134+00', '2026-07-28 15:24:14.099644+00', 'perm.payment:douyin:mch-app:manage');
INSERT INTO public.iam_perm_code VALUES (2082124981007970304, 'payment:douyin:mch-app:view', 'payment:douyin:mch-app', true, NULL, 1, 1, 0, false, '2026-07-28 15:24:14.140188+00', '2026-07-28 15:24:14.140188+00', 'perm.payment:douyin:mch-app:view');
INSERT INTO public.iam_perm_code VALUES (2082124981007970305, 'payment:douyin:platform-app:manage', 'payment:douyin:platform-app', true, NULL, 1, 1, 0, false, '2026-07-28 15:24:14.140188+00', '2026-07-28 15:24:14.140188+00', 'perm.payment:douyin:platform-app:manage');
INSERT INTO public.iam_perm_code VALUES (2075051483223699456, 'merchant:wx-verify:manage', 'merchant:wx-verify', true, '由 @PermCode 扫描同步生成', 1, 1, 2, false, '2026-07-09 02:56:40.852181+00', '2026-07-15 03:59:23.694425+00', 'perm.merchant:wx-verify:manage');
INSERT INTO public.iam_perm_code VALUES (2082124981012164608, 'payment:douyin:platform-app:view', 'payment:douyin:platform-app', true, NULL, 1, 1, 0, false, '2026-07-28 15:24:14.141188+00', '2026-07-28 15:24:14.141188+00', 'perm.payment:douyin:platform-app:view');
INSERT INTO public.iam_perm_code VALUES (2082124981012164609, 'plugin:easypay-order:manage', 'plugin:easypay-order', true, NULL, 1, 1, 0, false, '2026-07-28 15:24:14.141188+00', '2026-07-28 15:24:14.141188+00', 'perm.plugin:easypay-order:manage');
INSERT INTO public.iam_perm_code VALUES (2082124981012164610, 'plugin:easypay-order:view', 'plugin:easypay-order', true, NULL, 1, 1, 0, false, '2026-07-28 15:24:14.141188+00', '2026-07-28 15:24:14.141188+00', 'perm.plugin:easypay-order:view');
INSERT INTO public.iam_perm_code VALUES (2082124981016358912, 'plugin:easypay-refund:manage', 'plugin:easypay-refund', true, NULL, 1, 1, 0, false, '2026-07-28 15:24:14.142188+00', '2026-07-28 15:24:14.142188+00', 'perm.plugin:easypay-refund:manage');
INSERT INTO public.iam_perm_code VALUES (2082124981016358913, 'plugin:easypay-refund:view', 'plugin:easypay-refund', true, NULL, 1, 1, 0, false, '2026-07-28 15:24:14.142188+00', '2026-07-28 15:24:14.142188+00', 'perm.plugin:easypay-refund:view');
INSERT INTO public.iam_perm_code VALUES (2085254519598546944, 'payment:risk:mch-config:manage', 'payment:risk:mch-config', true, NULL, 1, 1, 0, false, '2026-08-06 06:39:54.28434+00', '2026-08-06 06:39:54.28434+00', 'perm.payment:risk:mch-config:manage');
INSERT INTO public.iam_perm_code VALUES (2085254519619518464, 'payment:risk:mch-config:view', 'payment:risk:mch-config', true, NULL, 1, 1, 0, false, '2026-08-06 06:39:54.289335+00', '2026-08-06 06:39:54.289335+00', 'perm.payment:risk:mch-config:view');
INSERT INTO public.iam_perm_code VALUES (2078486217727614976, 'merchant:terminal:manage', 'merchant:terminal', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-18 14:25:05.372949+00', '2026-07-18 14:25:05.380456+00', 'perm.merchant:terminal:manage');
INSERT INTO public.iam_perm_code VALUES (2078486217954107392, 'merchant:terminal:view', 'merchant:terminal', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-18 14:25:05.423491+00', '2026-07-18 14:25:05.423491+00', 'perm.merchant:terminal:view');
INSERT INTO public.iam_perm_code VALUES (2082636699320639488, 'payment:config:mobile-app:manage', 'payment:config:mobile-app', true, NULL, 1, 1, 0, false, '2026-07-30 01:17:37.296213+00', '2026-07-30 01:17:37.304726+00', 'perm.payment:config:mobile-app:manage');
INSERT INTO public.iam_perm_code VALUES (2082636699480023040, 'payment:config:mobile-app:view', 'payment:config:mobile-app', true, NULL, 1, 1, 0, false, '2026-07-30 01:17:37.331264+00', '2026-07-30 01:17:37.331264+00', 'perm.payment:config:mobile-app:view');
INSERT INTO public.iam_perm_code VALUES (2089898876290093056, 'trade:alloc:manage', 'trade:alloc', true, NULL, 1, 1, 0, false, '2026-08-19 02:14:55.208677+00', '2026-08-19 02:14:55.208677+00', 'perm.trade:alloc:manage');
INSERT INTO public.iam_perm_code VALUES (2077241592518934528, 'merchant:easypay:manage', 'merchant:easypay', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-15 03:59:23.599356+00', '2026-07-15 03:59:23.604361+00', 'perm.merchant:easypay:manage');
INSERT INTO public.iam_perm_code VALUES (2077241592707678208, 'merchant:easypay:view', 'merchant:easypay', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-15 03:59:23.641499+00', '2026-07-15 03:59:23.6425+00', 'perm.merchant:easypay:view');
INSERT INTO public.iam_perm_code VALUES (2070862264968351745, 'iam:role:view', 'iam:role', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.385095+00', '2026-07-15 03:59:23.682738+00', 'perm.iam:role:view');
INSERT INTO public.iam_perm_code VALUES (2070862264976740352, 'iam:user:assign-role', 'iam:user', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.387603+00', '2026-07-15 03:59:23.683738+00', 'perm.iam:user:assign-role');
INSERT INTO public.iam_perm_code VALUES (2070862264976740353, 'iam:user:manage', 'iam:user', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.387603+00', '2026-07-15 03:59:23.684738+00', 'perm.iam:user:manage');
INSERT INTO public.iam_perm_code VALUES (2070862264980934656, 'iam:user:reset-password', 'iam:user', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.388603+00', '2026-07-15 03:59:23.684738+00', 'perm.iam:user:reset-password');
INSERT INTO public.iam_perm_code VALUES (2070862264980934657, 'iam:user:status', 'iam:user', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.388603+00', '2026-07-15 03:59:23.684738+00', 'perm.iam:user:status');
INSERT INTO public.iam_perm_code VALUES (2070862264985128960, 'iam:user:view', 'iam:user', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.389603+00', '2026-07-15 03:59:23.686245+00', 'perm.iam:user:view');
INSERT INTO public.iam_perm_code VALUES (2070862264985128961, 'merchant:app:manage', 'merchant:app', true, '由 @PermCode 扫描同步生成', 1, 1, 2, false, '2026-06-27 13:30:13.389603+00', '2026-07-15 03:59:23.686883+00', 'perm.merchant:app:manage');
INSERT INTO public.iam_perm_code VALUES (2070862264989323264, 'merchant:app:route:manage', 'merchant:app:route', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.390603+00', '2026-07-15 03:59:23.687398+00', 'perm.merchant:app:route:manage');
INSERT INTO public.iam_perm_code VALUES (2070862264989323265, 'merchant:app:route:view', 'merchant:app:route', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.390603+00', '2026-07-15 03:59:23.687398+00', 'perm.merchant:app:route:view');
INSERT INTO public.iam_perm_code VALUES (2070862264993517568, 'merchant:app:view', 'merchant:app', true, '由 @PermCode 扫描同步生成', 1, 1, 2, false, '2026-06-27 13:30:13.391603+00', '2026-07-15 03:59:23.687398+00', 'perm.merchant:app:view');
INSERT INTO public.iam_perm_code VALUES (2070862264993517569, 'merchant:credential:manage', 'merchant:credential', true, '由 @PermCode 扫描同步生成', 1, 1, 2, false, '2026-06-27 13:30:13.391603+00', '2026-07-15 03:59:23.688908+00', 'perm.merchant:credential:manage');
INSERT INTO public.iam_perm_code VALUES (2070862264997711872, 'merchant:credential:view', 'merchant:credential', true, '由 @PermCode 扫描同步生成', 1, 1, 2, false, '2026-06-27 13:30:13.392603+00', '2026-07-15 03:59:23.688908+00', 'perm.merchant:credential:view');
INSERT INTO public.iam_perm_code VALUES (2075845892346347520, 'merchant:gateway-cashier:manage', 'merchant:gateway-cashier', true, '由 @PermCode 扫描同步生成', 1, 1, 2, false, '2026-07-11 07:33:22.747963+00', '2026-07-15 03:59:23.690915+00', 'perm.merchant:gateway-cashier:manage');
INSERT INTO public.iam_perm_code VALUES (2075845892476370944, 'merchant:gateway-cashier:view', 'merchant:gateway-cashier', true, '由 @PermCode 扫描同步生成', 1, 1, 2, false, '2026-07-11 07:33:22.775546+00', '2026-07-15 03:59:23.690915+00', 'perm.merchant:gateway-cashier:view');
INSERT INTO public.iam_perm_code VALUES (2070862264997711873, 'merchant:info:manage', 'merchant:info', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.392603+00', '2026-07-15 03:59:23.691914+00', 'perm.merchant:info:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265001906176, 'merchant:info:view', 'merchant:info', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.393603+00', '2026-07-15 03:59:23.692419+00', 'perm.merchant:info:view');
INSERT INTO public.iam_perm_code VALUES (2072990657067266048, 'merchant:notify-config:manage', 'merchant:notify-config', true, '由 @PermCode 扫描同步生成', 1, 1, 2, false, '2026-07-03 10:27:41.608254+00', '2026-07-15 03:59:23.692419+00', 'perm.merchant:notify-config:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265001906177, 'merchant:store:manage', 'merchant:store', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.393603+00', '2026-07-15 03:59:23.693424+00', 'perm.merchant:store:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265001906178, 'merchant:store:view', 'merchant:store', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.393603+00', '2026-07-15 03:59:23.694425+00', 'perm.merchant:store:view');
INSERT INTO public.iam_perm_code VALUES (2075051483370500096, 'merchant:wx-verify:view', 'merchant:wx-verify', true, '由 @PermCode 扫描同步生成', 1, 1, 2, false, '2026-07-09 02:56:40.884332+00', '2026-07-15 03:59:23.695427+00', 'perm.merchant:wx-verify:view');
INSERT INTO public.iam_perm_code VALUES (2075051483374694400, 'payment:config:wx-verify:manage', 'payment:config:wx-verify', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-09 02:56:40.885336+00', '2026-07-15 03:59:23.696424+00', 'perm.payment:config:wx-verify:manage');
INSERT INTO public.iam_perm_code VALUES (2075051483374694401, 'payment:config:wx-verify:view', 'payment:config:wx-verify', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-09 02:56:40.885839+00', '2026-07-15 03:59:23.697425+00', 'perm.payment:config:wx-verify:view');
INSERT INTO public.iam_perm_code VALUES (2076548115795673088, 'payment:isv:manage', 'payment:isv', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-13 06:03:45.8613+00', '2026-07-15 03:59:23.697931+00', 'perm.payment:isv:manage');
INSERT INTO public.iam_perm_code VALUES (2076548115795673089, 'payment:isv:view', 'payment:isv', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-13 06:03:45.8613+00', '2026-07-15 03:59:23.697931+00', 'perm.payment:isv:view');
INSERT INTO public.iam_perm_code VALUES (2070862265022877697, 'payment:platform:capability:view', 'payment:platform:capability', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.398114+00', '2026-07-15 03:59:23.698937+00', 'perm.payment:platform:capability:view');
INSERT INTO public.iam_perm_code VALUES (2070862265027072000, 'payment:platform:pay-channel:view', 'payment:platform:pay-channel', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.399113+00', '2026-07-15 03:59:23.698937+00', 'perm.payment:platform:pay-channel:view');
INSERT INTO public.iam_perm_code VALUES (2070862265027072001, 'payment:platform:product:manage', 'payment:platform:product', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.399113+00', '2026-07-15 03:59:23.698937+00', 'perm.payment:platform:product:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265031266304, 'payment:platform:product:view', 'payment:platform:product', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.400113+00', '2026-07-15 03:59:23.699945+00', 'perm.payment:platform:product:view');
INSERT INTO public.iam_perm_code VALUES (2070862265031266305, 'payment:platform:provider:manage', 'payment:platform:provider', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.400113+00', '2026-07-15 03:59:23.699945+00', 'perm.payment:platform:provider:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265035460608, 'payment:platform:provider:view', 'payment:platform:provider', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.401117+00', '2026-07-15 03:59:23.700944+00', 'perm.payment:platform:provider:view');
INSERT INTO public.iam_perm_code VALUES (2070862265039654913, 'system:dict:manage', 'system:dict', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.402115+00', '2026-07-15 03:59:23.702448+00', 'perm.system:dict:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265043849216, 'system:dict:view', 'system:dict', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.403114+00', '2026-07-15 03:59:23.702448+00', 'perm.system:dict:view');
INSERT INTO public.iam_perm_code VALUES (2070862265043849217, 'system:file:view', 'system:file', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.403114+00', '2026-07-15 03:59:23.703455+00', 'perm.system:file:view');
INSERT INTO public.iam_perm_code VALUES (2070862265048043520, 'system:log:login:manage', 'system:log:login', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.404113+00', '2026-07-15 03:59:23.703455+00', 'perm.system:log:login:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265048043521, 'system:log:login:view', 'system:log:login', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.404113+00', '2026-07-15 03:59:23.704455+00', 'perm.system:log:login:view');
INSERT INTO public.iam_perm_code VALUES (2070862265048043522, 'system:log:operate:manage', 'system:log:operate', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.404113+00', '2026-07-15 03:59:23.704455+00', 'perm.system:log:operate:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265052237824, 'system:log:operate:view', 'system:log:operate', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.405113+00', '2026-07-15 03:59:23.706454+00', 'perm.system:log:operate:view');
INSERT INTO public.iam_perm_code VALUES (2072232741347454976, 'system:notify:notice:manage', 'system:notify:notice', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-01 08:16:00.41608+00', '2026-07-15 03:59:23.706454+00', 'perm.system:notify:notice:manage');
INSERT INTO public.iam_perm_code VALUES (2072232741381009408, 'system:notify:notice:publish', 'system:notify:notice', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-01 08:16:00.424595+00', '2026-07-15 03:59:23.707963+00', 'perm.system:notify:notice:publish');
INSERT INTO public.iam_perm_code VALUES (2072232741385203712, 'system:notify:notice:view', 'system:notify:notice', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-01 08:16:00.425103+00', '2026-07-15 03:59:23.707963+00', 'perm.system:notify:notice:view');
INSERT INTO public.iam_perm_code VALUES (2079866296000000001, 'system:notify:mail-record:view', 'system:notify:mail-record', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-08-27 16:00:00+00', '2026-08-27 16:00:00+00', 'perm.system:notify:mail-record:view');
INSERT INTO public.iam_perm_code VALUES (2079866296000000002, 'system:notify:mail-record:manage', 'system:notify:mail-record', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-08-27 16:00:00+00', '2026-08-27 16:00:00+00', 'perm.system:notify:mail-record:manage');
INSERT INTO public.iam_perm_code VALUES (2079866296000000003, 'system:notify:mail-record:resend', 'system:notify:mail-record', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-08-27 16:00:00+00', '2026-08-27 16:00:00+00', 'perm.system:notify:mail-record:resend');
INSERT INTO public.iam_perm_code VALUES (2079866296000000004, 'system:platform-config:test', 'system:platform-config', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-08-27 16:00:00+00', '2026-08-27 16:00:00+00', 'perm.system:platform-config:test');
INSERT INTO public.iam_perm_code VALUES (2070862265056432129, 'system:oss-config:manage', 'system:oss-config', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.406113+00', '2026-07-15 03:59:23.70998+00', 'perm.system:oss-config:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265064820736, 'system:oss-config:view', 'system:oss-config', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.408624+00', '2026-07-15 03:59:23.710979+00', 'perm.system:oss-config:view');
INSERT INTO public.iam_perm_code VALUES (2070862265069015040, 'system:platform-config:manage', 'system:platform-config', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.409623+00', '2026-07-15 03:59:23.710979+00', 'perm.system:platform-config:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265069015041, 'system:platform-config:view', 'system:platform-config', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.409623+00', '2026-07-15 03:59:23.71198+00', 'perm.system:platform-config:view');
INSERT INTO public.iam_perm_code VALUES (2072232741389398016, 'system:protocol:manage', 'system:protocol', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-01 08:16:00.42611+00', '2026-07-15 03:59:23.71198+00', 'perm.system:protocol:manage');
INSERT INTO public.iam_perm_code VALUES (2072232741389398017, 'system:protocol:publish', 'system:protocol', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-01 08:16:00.42611+00', '2026-07-15 03:59:23.71298+00', 'perm.system:protocol:publish');
INSERT INTO public.iam_perm_code VALUES (2072232741389398018, 'system:protocol:view', 'system:protocol', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-01 08:16:00.42611+00', '2026-07-15 03:59:23.713487+00', 'perm.system:protocol:view');
INSERT INTO public.iam_perm_code VALUES (2070862265073209344, 'system:security-config:manage', 'system:security-config', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.410622+00', '2026-07-15 03:59:23.713487+00', 'perm.system:security-config:manage');
INSERT INTO public.iam_perm_code VALUES (2070862265073209345, 'system:security-config:view', 'system:security-config', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-06-27 13:30:13.410622+00', '2026-07-15 03:59:23.713992+00', 'perm.system:security-config:view');
INSERT INTO public.iam_perm_code VALUES (2072377871723388929, 'trade:fund:manage', 'trade:fund', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-01 17:52:42.194097+00', '2026-07-15 03:59:23.713992+00', 'perm.trade:fund:manage');
INSERT INTO public.iam_perm_code VALUES (2072377871727583232, 'trade:fund:view', 'trade:fund', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-01 17:52:42.195094+00', '2026-07-15 03:59:23.715001+00', 'perm.trade:fund:view');
INSERT INTO public.iam_perm_code VALUES (2075769305903063040, 'trade:gateway-order:manage', 'trade:gateway-order', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-11 02:29:03.113055+00', '2026-07-15 03:59:23.715001+00', 'perm.trade:gateway-order:manage');
INSERT INTO public.iam_perm_code VALUES (2075769305903063041, 'trade:gateway-order:view', 'trade:gateway-order', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-11 02:29:03.113565+00', '2026-07-15 03:59:23.715001+00', 'perm.trade:gateway-order:view');
INSERT INTO public.iam_perm_code VALUES (2072377871668862976, 'trade:order:manage', 'trade:order', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-01 17:52:42.183588+00', '2026-07-15 03:59:23.715999+00', 'perm.trade:order:manage');
INSERT INTO public.iam_perm_code VALUES (2072377871723388928, 'trade:order:view', 'trade:order', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-01 17:52:42.194097+00', '2026-07-15 03:59:23.715999+00', 'perm.trade:order:view');
INSERT INTO public.iam_perm_code VALUES (2072990657130180608, 'trade:refund:manage', 'trade:refund', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-03 10:27:41.620782+00', '2026-07-15 03:59:23.715999+00', 'perm.trade:refund:manage');
INSERT INTO public.iam_perm_code VALUES (2072990657130180609, 'trade:refund:view', 'trade:refund', true, '由 @PermCode 扫描同步生成', 1, 1, 1, false, '2026-07-03 10:27:41.620782+00', '2026-07-15 03:59:23.717505+00', 'perm.trade:refund:view');
INSERT INTO public.iam_perm_code VALUES (2079866295577419776, 'trade:callback-record:view', 'trade:callback-record', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-22 09:49:01.56951+00', '2026-07-22 09:49:01.575017+00', 'perm.trade:callback-record:view');
INSERT INTO public.iam_perm_code VALUES (2079866295615168512, 'trade:mch-notice:manage', 'trade:mch-notice', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-22 09:49:01.578524+00', '2026-07-22 09:49:01.578524+00', 'perm.trade:mch-notice:manage');
INSERT INTO public.iam_perm_code VALUES (2079866295619362816, 'trade:mch-notice:view', 'trade:mch-notice', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-07-22 09:49:01.579524+00', '2026-07-22 09:49:01.579524+00', 'perm.trade:mch-notice:view');
INSERT INTO public.iam_perm_code VALUES (2082811057154473984, 'merchant:gateway-pay-config:manage', 'merchant:gateway-pay-config', true, NULL, 1, 1, 0, false, '2026-07-30 12:50:27.440009+00', '2026-07-30 12:50:27.44601+00', 'perm.merchant:gateway-pay-config:manage');
INSERT INTO public.iam_perm_code VALUES (2082811057204805632, 'merchant:gateway-pay-config:view', 'merchant:gateway-pay-config', true, NULL, 1, 1, 0, false, '2026-07-30 12:50:27.452008+00', '2026-07-30 12:50:27.452008+00', 'perm.merchant:gateway-pay-config:view');
INSERT INTO public.iam_perm_code VALUES (2089898876302675968, 'trade:alloc:view', 'trade:alloc', true, NULL, 1, 1, 0, false, '2026-08-19 02:14:55.211906+00', '2026-08-19 02:14:55.211906+00', 'perm.trade:alloc:view');
INSERT INTO public.iam_perm_code VALUES (2079866296000000101, 'merchant:user:view', 'merchant:user', true, NULL, 1, 1, 0, false, '2026-08-27 00:00:00+00', '2026-08-27 00:00:00+00', 'perm.merchant:user:view');
INSERT INTO public.iam_perm_code VALUES (2079866296000000102, 'merchant:user:manage', 'merchant:user', true, NULL, 1, 1, 0, false, '2026-08-27 00:00:00+00', '2026-08-27 00:00:00+00', 'perm.merchant:user:manage');
INSERT INTO public.iam_perm_code VALUES (2079866296000000103, 'merchant:user:assign-role', 'merchant:user', true, NULL, 1, 1, 0, false, '2026-08-27 00:00:00+00', '2026-08-27 00:00:00+00', 'perm.merchant:user:assign-role');
INSERT INTO public.iam_perm_code VALUES (2079866296000000104, 'merchant:user:status', 'merchant:user', true, NULL, 1, 1, 0, false, '2026-08-27 00:00:00+00', '2026-08-27 00:00:00+00', 'perm.merchant:user:status');
INSERT INTO public.iam_perm_code VALUES (2079866296000000105, 'merchant:user:reset-password', 'merchant:user', true, NULL, 1, 1, 0, false, '2026-08-27 00:00:00+00', '2026-08-27 00:00:00+00', 'perm.merchant:user:reset-password');


--
-- Data for Name: iam_perm_menu; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.iam_perm_menu VALUES (40603, 406, 'payment:risk:security', 'admin', 'ApiSecurityConfig', 'menu.payment.security.api', 'lucide:lock-keyhole', false, false, '/payment/risk/security/ApiSecurityConfig', '/payment/risk/api-security', NULL, 0, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-06 01:05:45.367566+00', '2026-08-06 01:05:45.367566+00');
INSERT INTO public.iam_perm_menu VALUES (202, 2, NULL, 'admin', 'FileUploadDemo', 'menu.demos.fileUpload', 'lucide:upload', false, false, '/demos/file-upload/FileUploadDemo', '/demos/file-upload', NULL, 2, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-09 16:00:00+00', '2026-04-09 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (307, 3, 'system:monitor', 'admin', 'SystemMonitor', 'menu.system.monitor', 'lucide:monitor', false, false, NULL, '/system/monitor', NULL, 50, false, true, false, 1, 1, 1, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-10 16:00:00+00', '2026-04-12 12:53:45.790453+00');
INSERT INTO public.iam_perm_menu VALUES (305, 3, 'iam:perm', 'admin', 'SystemPerm', 'menu.system.perm', 'lucide:shield', false, false, NULL, '/system/perm', NULL, 2, false, true, false, 1, 1, 1, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-08 16:00:00+00', '2026-04-09 15:10:44.651238+00');
INSERT INTO public.iam_perm_menu VALUES (302, 3, NULL, 'admin', 'SystemLog', 'menu.system.log', 'lucide:file-text', false, false, NULL, '/system/log', NULL, 99, false, true, false, 0, 1, 1, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-04-05 08:56:11.97756+00');
INSERT INTO public.iam_perm_menu VALUES (40604, 406, 'payment:risk:security', 'admin', 'RiskStrategy', 'menu.payment.security.riskStrategy', 'lucide:shield-alert', false, false, '/payment/risk/security/RiskStrategy', '/payment/risk/strategy', NULL, 0.5, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-06 01:05:45.376446+00', '2026-08-06 01:05:45.376446+00');
INSERT INTO public.iam_perm_menu VALUES (304, 3, 'system:config', 'admin', 'SystemConfig', 'menu.system.config', 'lucide:settings-2', false, false, NULL, '/system/config', NULL, 10, false, true, false, 0, 1, 1, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-04 16:00:00+00', '2026-04-09 15:11:00.840153+00');
INSERT INTO public.iam_perm_menu VALUES (203, 2, 'demos:region', 'admin', 'RegionCascaderDemo', 'menu.demos.region', 'lucide:map-pin', false, false, '/demos/region/RegionCascaderDemo', '/demos/region', NULL, 3, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-24 16:00:00+00', '2026-04-24 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (401, 4, 'payment:platform', 'admin', 'PaymentPlatform', 'menu.payment.platform', 'lucide:building', false, false, NULL, '/payment/platform', NULL, 10, false, true, false, 0, 1, 3, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-05 16:00:00+00', '2026-07-31 03:18:17.020454+00');
INSERT INTO public.iam_perm_menu VALUES (3, NULL, NULL, 'admin', 'System', 'menu.system', 'lucide:sliders-horizontal', false, false, NULL, '/system', NULL, 0, false, true, false, 0, 1, 2, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.22348+00');
INSERT INTO public.iam_perm_menu VALUES (30102, 301, 'iam:menu', 'admin', 'SystemMenu', 'menu.system.perm.menu', 'lucide:panel-top', false, false, '/iam/perm/menu/MenuList', '/system/basic/menu', NULL, 0, false, true, false, 0, 1, 3, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-27 10:29:51.371435+00');
INSERT INTO public.iam_perm_menu VALUES (301, 3, NULL, 'admin', 'SystemBasic', 'menu.system.basic', 'lucide:boxes', false, false, NULL, '/system/basic', NULL, 1, false, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.236321+00');
INSERT INTO public.iam_perm_menu VALUES (30101, 301, 'system:dict', 'admin', 'SystemDict', 'menu.system.basic.dict', 'lucide:book-open', false, false, '/system/basic/dict/DictList', '/system/basic/dict', NULL, 1, false, true, false, 0, 1, 2, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.243488+00');
INSERT INTO public.iam_perm_menu VALUES (30601, 307, 'system:file', 'admin', 'StorageFile', 'menu.system.monitor.file', 'lucide:files', false, false, '/system/monitor/file/PlatformFileList', '/system/monitor/file', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-09 16:00:00+00', '2026-06-25 02:43:43.71511+00');
INSERT INTO public.iam_perm_menu VALUES (30501, 305, 'iam:user', 'admin', 'UserList', 'menu.system.perm.user', 'lucide:users-round', false, false, '/iam/user/UserList', '/iam/user', NULL, 10, false, true, false, 1, 1, 4, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-31 00:23:04.37507+00', '2026-07-13 02:15:41.494775+00');
INSERT INTO public.iam_perm_menu VALUES (2, NULL, NULL, 'admin', 'Demos', 'menu.demos', 'lucide:blocks', false, false, NULL, '/demos', NULL, 1000, false, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.27727+00');
INSERT INTO public.iam_perm_menu VALUES (1, NULL, NULL, 'admin', 'Dashboard', 'menu.dashboard', 'lucide:layout-dashboard', false, false, NULL, '/dashboard', '/workspace', -1, false, false, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-27 13:46:52.151771+00');
INSERT INTO public.iam_perm_menu VALUES (30202, 302, 'system:log:operate', 'admin', 'SystemOperateLog', 'menu.system.log.operate', 'lucide:activity', false, false, '/system/log/operate/OperateLogList', '/system/log/operate', NULL, 2, false, true, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-03-30 15:24:57.076166+00');
INSERT INTO public.iam_perm_menu VALUES (4, NULL, 'payment', 'admin', 'PaymentSystem', 'menu.platform', 'lucide:credit-card', false, false, NULL, '/payment', NULL, 3, false, true, false, 0, 1, 1, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-05 16:00:00+00', '2026-07-13 13:02:20.694464+00');
INSERT INTO public.iam_perm_menu VALUES (30201, 302, 'system:log:login', 'admin', 'SystemLoginLog', 'menu.system.log.login', 'lucide:log-in', false, false, '/system/log/login/LoginLogList', '/system/log/login', NULL, 1, false, true, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-03-30 15:25:09.855555+00');
INSERT INTO public.iam_perm_menu VALUES (102, 1, 'dashboard:workspace', 'admin', 'Workspace', 'menu.dashboard.workspace', 'lucide:panels-top-left', false, false, '/dashboard/workspace/index', '/workspace', NULL, 1, false, false, true, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-28 02:50:37.055128+00');
INSERT INTO public.iam_perm_menu VALUES (101, 1, 'dashboard:analytics', 'admin', 'Analytics', 'menu.dashboard.analytics', 'lucide:area-chart', false, false, '/dashboard/analytics/index', '/analytics', NULL, 2, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-28 02:50:43.072618+00');
INSERT INTO public.iam_perm_menu VALUES (30103, 305, 'iam:role', 'admin', 'SystemRole', 'menu.system.perm.role', 'lucide:shield-user', false, false, '/iam/perm/role/RoleList', '/iam/perm/role', NULL, 3, false, true, false, 0, 1, 2, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.246504+00');
INSERT INTO public.iam_perm_menu VALUES (30701, 307, 'iam:online', 'admin', 'OnlineUser', 'menu.system.monitor.online', 'lucide:users', false, false, '/system/monitor/online/OnlineUserList', '/system/monitor/online', NULL, 1, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-11 16:00:00+00', '2026-04-11 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (6, NULL, 'trade', 'admin', 'TransactionManagement', 'menu.trade', 'lucide:arrow-left-right', false, false, NULL, '/trade', '/trade/pay-trade', 4, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 16:00:00+00', '2026-07-17 06:18:15.479497+00');
INSERT INTO public.iam_perm_menu VALUES (40401, 404, 'merchant:info', 'admin', 'MerchantInfo', 'menu.payment.merchant.list', 'lucide:shopping-bag', false, false, '/payment/merchant/info/MerchantList', '/payment/merchant/info', NULL, 1, false, true, false, 1, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-04-13 16:00:00+00', '2026-06-25 02:00:30.290399+00');
INSERT INTO public.iam_perm_menu VALUES (30402, 304, 'system:platform-config', 'admin', 'PlatformConfig', 'menu.system.config.platform', 'lucide:settings', false, false, '/system/config/platform/PlatformConfig', '/system/config/platform', NULL, 1, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-07 16:00:00+00', '2026-07-13 13:17:21.921261+00');
INSERT INTO public.iam_perm_menu VALUES (3040201, 30402, 'system:oss-config', 'admin', 'OssConfigPermission', 'menu.system.config.platform', NULL, true, false, NULL, NULL, NULL, 1, false, false, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-27 09:30:00+00', '2026-08-27 09:30:00+00');
INSERT INTO public.iam_perm_menu VALUES (4040125, 4040130, 'payment:risk:mch-config', 'admin', 'MchRiskConfigManage', 'menu.payment.merchant.riskConfig', NULL, true, false, '/payment/merchant/manage/risk-config/MchRiskConfigManage', '/payment/merchant/manage/risk-config', NULL, 25, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-06 00:00:00+00', '2026-08-06 00:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (207, 2, 'demos:city-adjacent', 'admin', 'CityAdjacentDemo', 'menu.demos.cityAdjacent', 'lucide:map-pinned', false, false, '/demos/city-adjacent/CityAdjacentDemo', '/demos/city-adjacent', NULL, 6, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-06 16:00:00+00', '2026-08-06 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (40104, 401, 'payment:platform:capability', 'admin', 'PayCapabilityList', 'menu.payment.platform.capability', 'lucide:zap', false, false, '/payment/masterdata/capability/PayCapabilityList', '/payment/platform/pay-capability', NULL, 3, false, true, false, 1, 1, 4, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-26 16:00:00+00', '2026-05-28 06:43:27.505831+00');
INSERT INTO public.iam_perm_menu VALUES (4040102, 4040130, 'merchant:credential', 'admin', 'MerchantCredentialConfig', 'menu.payment.merchant.credential', NULL, true, false, '/payment/merchant/manage/credential/MerchantCredentialConfig', '/payment/merchant/manage/credential', NULL, 3, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-01 16:00:00+00', '2026-07-13 10:13:20.809377+00');
INSERT INTO public.iam_perm_menu VALUES (404, NULL, 'merchant', 'admin', 'PaymentMerchant', 'menu.merchant', 'lucide:store', false, false, NULL, '/payment/merchant', NULL, 3.5, true, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-05 16:00:00+00', '2026-07-13 13:12:49.514288+00');
INSERT INTO public.iam_perm_menu VALUES (601, 610, 'trade:order', 'admin', 'NormalOrderList', 'menu.trade.normalPay', 'lucide:credit-card', false, false, '/payment/order/NormalOrderList', '/trade/pay-order/normal', NULL, 1, false, false, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 16:00:00+00', '2026-07-17 06:18:15.466267+00');
INSERT INTO public.iam_perm_menu VALUES (4040111, 4040130, 'merchant:app:route', 'admin', 'PayRouteConfig', 'menu.payment.merchant.payRoute', NULL, true, false, '/payment/route/PayRouteConfig', '/payment/route', NULL, 2, false, true, false, 1, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 22:23:46.483985+00', '2026-07-22 13:50:46.613056+00');
INSERT INTO public.iam_perm_menu VALUES (40101, 401, 'payment:platform:product', 'admin', 'ProductList', 'menu.payment.platform.product', 'lucide:package', false, false, '/payment/masterdata/product/PayProductList', '/payment/platform/product', NULL, 2, false, true, false, 1, 1, 3, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-23 16:00:00+00', '2026-05-28 06:25:42.892396+00');
INSERT INTO public.iam_perm_menu VALUES (4040109, 4040131, 'channel:merchant:create', 'admin', 'ChannelMerchantCreate', 'menu.payment.merchant.channelMerchant.create', NULL, true, false, '/payment/global/channel-merchant/ChannelMerchantCreate', '/payment/global/channel-merchant/create', NULL, 9, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-05 16:00:00+00', '2026-07-22 13:50:46.619325+00');
INSERT INTO public.iam_perm_menu VALUES (4040101, 4040130, 'merchant:workbench', 'admin', 'MerchantManage', 'menu.payment.merchant.workbench', '', true, false, '/payment/merchant/manage/workbench/MerchantManage', '/payment/merchant/manage', NULL, 2, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-13 16:00:00+00', '2026-07-11 03:37:15.424776+00');
INSERT INTO public.iam_perm_menu VALUES (4040112, 4040131, 'channel:merchant:detail', 'admin', 'ChannelMerchantDetailDispatch', 'menu.payment.merchant.channelMerchant.detail', NULL, true, false, '/payment/global/channel-merchant/detail/ChannelMerchantDetailDispatch', '/payment/global/channel-merchant/detail', NULL, 10, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-07 19:48:34.936565+00', '2026-07-22 13:50:46.623111+00');
INSERT INTO public.iam_perm_menu VALUES (40103, 401, 'payment:platform:pay-channel', 'admin', 'PayChannelList', 'menu.payment.platform.channel', 'lucide:radio-tower', false, false, '/payment/masterdata/channel/PayChannelList', '/payment/platform/pay-channel', NULL, 0, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-28 16:00:00+00', '2026-06-27 13:01:24.315365+00');
INSERT INTO public.iam_perm_menu VALUES (40102, 401, 'payment:platform:pay-method', 'admin', 'PayProviderList', 'menu.payment.platform.provider', 'lucide:list-tree', false, false, '/payment/masterdata/provider/PayMethodList', '/payment/platform/pay-method', NULL, 1, false, true, false, 1, 1, 5, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-27 20:18:01.383008+00', '2026-05-30 08:21:19.464024+00');
INSERT INTO public.iam_perm_menu VALUES (4040114, 4040132, 'channel:app', 'admin', 'AlipayMchAppManage', 'menu.payment.merchant.channelMerchant.alipayApp', NULL, true, false, '/payment/channel/alipay/manage/mch/app/AlipayMchAppManage', '/payment/global/channel-merchant/alipay-app-manage', NULL, 12, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-11 22:28:11.274785+00', '2026-07-22 13:50:46.635605+00');
INSERT INTO public.iam_perm_menu VALUES (4040108, 4040130, 'merchant:user', 'admin', 'MerchantUser', 'menu.payment.merchant.user', '', true, false, '/payment/merchant/user/MerchantUserList', '/payment/merchant/user', NULL, 8, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-04 16:00:00+00', '2026-07-11 03:37:15.424776+00');
INSERT INTO public.iam_perm_menu VALUES (4040110, 4040130, 'merchant:app', 'admin', 'MchAppInfoList', 'menu.payment.merchant.app', NULL, true, false, '/payment/merchant/app/MchAppInfoList', '/payment/merchant/app', NULL, 10, false, true, false, 1, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 16:00:00+00', '2026-07-11 03:37:15.424776+00');
INSERT INTO public.iam_perm_menu VALUES (91127, 91100, 'trade:alloc', 'merchant', 'AllocOrderList', 'menu.trade.allocOrder', 'lucide:split', false, false, '/payment/order/AllocOrderList', '/trade/alloc-order', NULL, 2.5, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.iam_perm_menu VALUES (4040117, 4040130, 'merchant:store', 'admin', 'MchStoreInfoList', 'menu.payment.merchant.store', NULL, true, false, '/payment/merchant/store/MchStoreInfoList', '/payment/merchant/store', NULL, 15, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-24 16:00:00+00', '2026-07-11 03:37:15.424776+00');
INSERT INTO public.iam_perm_menu VALUES (602, 6, 'trade:fund', 'admin', 'PayTradeList', 'menu.trade.payTrade', 'lucide:arrow-left-right', false, false, '/payment/order/PayTradeList', '/trade/pay-trade', NULL, 1, false, true, false, NULL, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-01 15:09:04.769906+00', '2026-07-17 06:18:15.474015+00');
INSERT INTO public.iam_perm_menu VALUES (8, NULL, 'develop', 'admin', 'Develop', 'menu.develop', 'lucide:wrench', false, false, NULL, '/develop', NULL, 5, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-23 16:00:00+00', '2026-06-23 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (204, 2, 'demos:artemis', 'admin', 'ArtemisDemo', 'menu.demos.artemis', 'lucide:send', false, false, '/demos/artemis/ArtemisDemo', '/demos/artemis', NULL, 4, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-18 00:00:00+00', '2026-06-18 00:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (900001, 304, 'iam:social', 'admin', 'ThirdPlatform', 'menu.system.config.thirdPlatform', 'lucide:share-2', false, false, '/system/config/third-platform/ThirdPlatform', '/system/config/third-platform', NULL, 5, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-13 02:15:41.505142+00');
INSERT INTO public.iam_perm_menu VALUES (4040103, 4040130, 'merchant:profile', 'admin', 'MchInfoManage', 'menu.payment.merchant.profile', NULL, true, false, '/payment/merchant/manage/info/MchInfoManage', '/payment/merchant/manage/info', NULL, 1, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:37:15.424776+00');
INSERT INTO public.iam_perm_menu VALUES (801, 8, 'develop:trade', 'admin', 'DevelopTrade', 'menu.develop.trade', 'lucide:credit-card', false, false, '/payment/develop/trade/DevelopTrade', '/develop/trade', NULL, 1, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-23 16:00:00+00', '2026-06-23 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (405, 4, 'payment:config', 'admin', 'PaymentConfig', 'menu.payment.config', 'lucide:settings-2', false, false, NULL, '/payment/config', NULL, 50, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-14 00:00:00+00', '2026-07-31 03:18:17.016499+00');
INSERT INTO public.iam_perm_menu VALUES (901, 4, 'device:qrcode', 'admin', 'DeviceQrCode', 'menu.device.qrcode', 'lucide:qr-code', false, false, '/payment/device/qrcode/DeviceQrCode', '/device/qrcode', NULL, 40, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-25 01:21:34.634639+00', '2026-07-31 03:18:17.02516+00');
INSERT INTO public.iam_perm_menu VALUES (40105, 4, 'payment:config:product-config', 'admin', 'ProductConfig', 'menu.payment.config.productConfig', 'lucide:layers', false, false, '/payment/config/product/ProductConfig', '/payment/config/product', NULL, 20, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-13 00:00:00+00', '2026-07-31 03:18:17.022986+00');
INSERT INTO public.iam_perm_menu VALUES (4040116, 401, 'payment:platform:provider', 'admin', 'PayProviderManage', 'menu.payment.platform.provider.manage', 'lucide:wallet', false, false, '/payment/masterdata/provider/PayProviderList', '/payment/platform/pay-provider', NULL, 0.5, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-17 10:00:31.640482+00', '2026-06-27 13:01:24.318649+00');
INSERT INTO public.iam_perm_menu VALUES (603, 6, 'trade:refund', 'admin', 'RefundOrderList', 'menu.trade.refundOrder', 'lucide:rotate-ccw', false, false, '/payment/order/RefundOrderList', '/trade/refund-order', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-03 16:00:00+00', '2026-07-17 06:18:15.476169+00');
INSERT INTO public.iam_perm_menu VALUES (309, 308, 'system:notify:notice', 'admin', 'SystemNotify', 'menu.system.notify.notice', 'lucide:megaphone', false, false, '/system/notify/notice/NoticeList', '/system/notify/notice', NULL, 20, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, '2026-06-24 16:00:00+00', '2026-06-24 16:00:00+00', NULL);
INSERT INTO public.iam_perm_menu VALUES (310, 308, 'system:notify:mail-record', 'admin', 'SystemMailRecord', 'menu.system.notify.mailRecord', 'lucide:mail', false, false, '/system/notify/mail/MailRecordList', '/system/notify/mail-record', NULL, 30, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-27 16:00:00+00', '2026-08-27 16:00:00+00', NULL);
INSERT INTO public.iam_perm_menu VALUES (308, 3, 'system:notify', 'admin', 'SystemNotice', 'menu.system.notify', 'lucide:bell', false, false, NULL, '/system/notify', '/system/notify/notice', 20, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-01 07:16:30.927645+00', '2026-07-01 07:16:30.927645+00');
INSERT INTO public.iam_perm_menu VALUES (40502, 40508, 'payment:isv', 'admin', 'AlipayIsvAppManage', 'menu.payment.config.alipayApp', NULL, true, false, '/payment/channel/alipay/manage/app/AlipayIsvAppManage', '/payment/config/product/app-manage', NULL, 3, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-14 00:00:00+00', '2026-07-11 03:37:15.43231+00');
INSERT INTO public.iam_perm_menu VALUES (4040106, 4040131, 'channel:merchant', 'admin', 'ChannelMerchant', 'menu.payment.merchant.channelMerchant', NULL, true, false, '/payment/global/channel-merchant/ChannelMerchantList', '/payment/global/channel-merchant', NULL, 6, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-03 16:00:00+00', '2026-07-22 13:50:46.61698+00');
INSERT INTO public.iam_perm_menu VALUES (30403, 301, 'system:protocol', 'admin', 'UserProtocol', 'menu.system.basic.protocol', 'lucide:file-text', false, false, '/system/basic/protocol/UserProtocolList', '/system/basic/protocol', NULL, 2, false, true, false, 1, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-24 10:32:13.183371+00', '2026-07-16 08:43:35.038937+00');
INSERT INTO public.iam_perm_menu VALUES (802, 8, 'develop:sign', 'admin', 'DevelopSign', 'menu.develop.sign', 'lucide:pen-tool', false, false, '/payment/develop/sign/DevelopSign', '/develop/sign', NULL, 3, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-23 16:00:00+00', '2026-06-23 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (3040101, 304, 'system:security-config', 'admin', 'SystemSecurityConfig', 'menu.system.security.system', 'lucide:lock', false, false, '/system/config/security/SystemSecurityConfig', '/system/config/security/system', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-16 07:31:19.446929+00', '2026-07-16 07:31:19.446929+00');
INSERT INTO public.iam_perm_menu VALUES (406, 4, 'payment:risk', 'admin', 'PaymentSecurity', 'menu.payment.security', 'lucide:shield-alert', false, false, NULL, '/payment/risk', NULL, 60, false, true, false, 0, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-15 00:00:00+00', '2026-07-31 03:18:17.027286+00');
INSERT INTO public.iam_perm_menu VALUES (40601, 406, 'payment:risk:blacklist', 'admin', 'PayBlacklistList', 'menu.payment.security.blacklist', 'lucide:ban', false, false, '/payment/risk/blacklist/PayBlacklistList', '/payment/risk/blacklist', NULL, 1, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-15 00:00:00+00', '2026-07-15 00:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (40602, 406, 'payment:risk:hit', 'admin', 'PayRiskHitList', 'menu.payment.security.hit', 'lucide:triangle-alert', false, false, '/payment/risk/hit/PayRiskHitList', '/payment/risk/hit', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-15 00:00:00+00', '2026-07-15 00:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (608, 6, 'trade:alloc', 'admin', 'AllocOrderList', 'menu.trade.allocOrder', 'lucide:split', false, false, '/payment/order/AllocOrderList', '/trade/alloc-order', NULL, 2.5, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.iam_perm_menu VALUES (40504, 405, 'payment:config:mobile-app', 'admin', 'MobileAppConfig', 'menu.payment.config.mobileApp', 'lucide:smartphone', false, false, '/payment/config/mobile-app/MobileAppConfig', '/payment/config/mobile-app', NULL, 10, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-31 03:18:17.013352+00');
INSERT INTO public.iam_perm_menu VALUES (4040130, 40401, NULL, 'admin', 'MchManageGroup', 'menu.payment.merchant.group.manage', 'lucide:settings-2', true, false, NULL, NULL, NULL, 1, false, false, false, 1, 1, 0, false, 'subpage_group', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:37:15.421585+00', '2026-07-11 03:37:15.421585+00');
INSERT INTO public.iam_perm_menu VALUES (4040131, 40401, NULL, 'admin', 'ChannelMerchantGroup', 'menu.payment.merchant.group.channelMerchant', 'lucide:repeat', true, false, NULL, NULL, NULL, 2, false, false, false, 1, 1, 0, false, 'subpage_group', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:37:15.421585+00', '2026-07-11 03:37:15.421585+00');
INSERT INTO public.iam_perm_menu VALUES (4040120, 4040130, 'merchant:wx-verify', 'admin', 'MchWxDomainVerifyList', 'menu.payment.merchant.wxVerify', NULL, true, false, '/payment/merchant/manage/wx-verify/MchWxDomainVerifyList', '/payment/merchant/manage/wx-verify', NULL, 20, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-09 16:00:00+00', '2026-07-11 03:37:15.424776+00');
INSERT INTO public.iam_perm_menu VALUES (40501, 40105, 'payment:config:product-detail', 'admin', 'ProductDetailDispatch', 'menu.payment.config.detail', NULL, true, false, '/payment/config/product/detail/ProductDetailDispatch', '/payment/product-detail', NULL, 2, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-14 00:00:00+00', '2026-06-27 13:01:24.321883+00');
INSERT INTO public.iam_perm_menu VALUES (40507, 405, 'payment:config:wx-verify', 'admin', 'PlatformWxDomainVerifyList', 'menu.payment.config.wxVerify', 'lucide:shield-check', false, false, '/payment/config/wx-verify/PlatformWxDomainVerifyList', '/payment/config/wx-verify', NULL, 20, false, true, false, 1, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-09 16:00:00+00', '2026-07-31 03:18:17.018417+00');
INSERT INTO public.iam_perm_menu VALUES (803, 8, 'develop:auth', 'admin', 'ChannelAuth', 'menu.develop.auth', 'lucide:shield-check', false, false, '/payment/develop/auth/ChannelAuth', '/develop/auth', NULL, 4, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-09 16:00:00+00', '2026-07-09 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (4040123, 4040130, 'merchant:app:workbench', 'admin', 'MchAppWorkbench', 'menu.payment.merchant.appWorkbench', NULL, true, false, '/payment/merchant/app/MchAppWorkbench', '/payment/merchant/app/manage', NULL, 11, false, true, false, 1, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 08:00:00+00', '2026-07-11 08:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (4040126, 4040130, 'merchant:notify-config', 'admin', 'MchAppNotifyConfigPermission', 'menu.payment.merchant.appWorkbench', NULL, true, false, NULL, NULL, NULL, 12, false, false, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-27 09:30:00+00', '2026-08-27 09:30:00+00');
INSERT INTO public.iam_perm_menu VALUES (40506, 40508, 'payment:isv', 'admin', 'LakalaManage', 'menu.payment.lakala.config', NULL, true, false, '/payment/channel/lakala/manage/LakalaManage', '/payment/config/product/lakala-manage', NULL, 5, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-11 03:37:15.43231+00');
INSERT INTO public.iam_perm_menu VALUES (4040132, 40401, NULL, 'admin', 'ChannelAppGroup', 'menu.payment.merchant.group.channelApp', 'lucide:layout-grid', true, false, NULL, NULL, NULL, 3, false, false, false, 1, 1, 0, false, 'subpage_group', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:37:15.421585+00', '2026-07-13 10:13:20.804554+00');
INSERT INTO public.iam_perm_menu VALUES (40508, 40105, NULL, 'admin', 'ChannelIsvConfigGroup', 'menu.payment.config.group.channelIsv', 'lucide:server', true, false, NULL, NULL, NULL, 1, false, false, false, 1, 1, 0, false, 'subpage_group', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:37:15.421585+00', '2026-07-13 10:13:20.806797+00');
INSERT INTO public.iam_perm_menu VALUES (4040122, 4040130, 'merchant:gateway-cashier', 'admin', 'CashierConfig', 'menu.payment.merchant.cashierConfig', NULL, true, false, '/payment/merchant/cashier/CashierConfig', '/payment/merchant/cashier', NULL, 5, false, true, false, 1, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:00:13.374496+00', '2026-07-15 09:03:23.614722+00');
INSERT INTO public.iam_perm_menu VALUES (4040124, 4040130, 'merchant:easypay', 'admin', 'EasyPayConfig', 'menu.payment.merchant.easypay', NULL, true, false, '/payment/merchant/easypay/EasyPayConfig', '/payment/merchant/easypay', NULL, 6, false, true, false, 1, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-15 00:00:00+00', '2026-07-15 09:03:23.618+00');
INSERT INTO public.iam_perm_menu VALUES (311, 3, 'system:sensitive', 'admin', 'SensitiveWord', 'menu.system.sensitive', 'lucide:shield-ban', false, false, NULL, '/system/sensitive', NULL, 15, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-16 00:40:06.501938+00', '2026-07-16 00:40:06.501938+00');
INSERT INTO public.iam_perm_menu VALUES (31101, 311, 'system:sensitive-word', 'admin', 'SensitiveWordList', 'menu.system.sensitive.word', 'lucide:book-x', false, false, '/system/sensitive-word/SensitiveWordList', '/system/sensitive/word', NULL, 1, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-16 00:40:06.511319+00', '2026-07-16 00:40:06.511319+00');
INSERT INTO public.iam_perm_menu VALUES (31102, 311, 'system:sensitive-word-hit', 'admin', 'SensitiveWordHitList', 'menu.system.sensitive.hit', 'lucide:scan-search', false, false, '/system/sensitive-word/SensitiveWordHitList', '/system/sensitive/hit', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-16 00:40:06.51532+00', '2026-07-16 00:40:06.51532+00');
INSERT INTO public.iam_perm_menu VALUES (40505, 40504, 'payment:config:mobile-app-detail', 'admin', 'MobileAppDetail', 'menu.payment.config.mobileAppDetail', NULL, true, false, '/payment/config/mobile-app/detail/MobileAppDetail', '/payment/config/mobile-app/detail/:appType', NULL, 1, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-22 13:50:46.655058+00');
INSERT INTO public.iam_perm_menu VALUES (804, 8, 'develop:gateway', 'admin', 'DevelopGateway', 'menu.develop.gateway', 'lucide:globe', false, false, '/payment/develop/gateway/DevelopGateway', '/develop/gateway', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-16 16:00:00+00', '2026-07-16 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (610, 6, 'trade:pay-order', 'admin', 'PayOrderCatalog', 'menu.trade.payOrder', 'lucide:receipt', false, false, NULL, '/trade/pay-order', '/trade/pay-order/normal', 3, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-17 06:18:15.454191+00', '2026-07-17 06:18:15.463823+00');
INSERT INTO public.iam_perm_menu VALUES (604, 610, 'trade:gateway-order', 'admin', 'GatewayOrderList', 'menu.trade.gatewayOrder', 'lucide:store', false, false, '/payment/order/GatewayOrderList', '/trade/pay-order/gateway', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-17 06:18:15.468829+00', '2026-07-17 06:18:15.47158+00');
INSERT INTO public.iam_perm_menu VALUES (30203, 302, 'system:log:unipay', 'admin', 'SystemUnipayApiLog', 'menu.system.log.unipay', 'lucide:webhook', false, false, '/system/log/unipay/UnipayApiLogList', '/system/log/unipay', NULL, 3, false, true, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-17 00:00:00+00', '2026-07-17 00:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (208, 2, 'demos:cache', 'admin', 'CacheDemo', 'menu.demos.cache', 'lucide:database-backup', false, false, '/demos/cache/CacheDemo', '/demos/cache', NULL, 7, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-14 16:00:00+00', '2026-08-14 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (91200, NULL, 'trade:record', 'merchant', 'TradeRecordCatalog', 'menu.trade.record', 'lucide:scroll-text', false, false, NULL, '/trade/record', '/trade/record/mch-notice', 4, false, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-23 09:32:12.651967+00', '2026-07-23 09:32:12.651967+00');
INSERT INTO public.iam_perm_menu VALUES (91101, 91100, 'trade:fund', 'merchant', 'PayTradeList', 'menu.trade.payTrade', 'lucide:arrow-left-right', false, false, '/payment/order/PayTradeList', '/trade/pay-trade', NULL, 1, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-23 03:48:14.896997+00', '2026-07-23 06:44:16.735074+00');
INSERT INTO public.iam_perm_menu VALUES (91103, 91100, 'trade:refund', 'merchant', 'RefundOrderList', 'menu.trade.refundOrder', 'lucide:rotate-ccw', false, false, '/payment/order/RefundOrderList', '/trade/refund-order', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-23 03:48:14.903907+00', '2026-07-23 06:44:16.737803+00');
INSERT INTO public.iam_perm_menu VALUES (607, NULL, 'trade:record', 'admin', 'TradeRecordCatalog', 'menu.trade.record', 'lucide:scroll-text', false, false, NULL, '/trade/record', '/trade/record/mch-notice', 4.5, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-21 06:30:00+00', '2026-07-21 06:45:00+00');
INSERT INTO public.iam_perm_menu VALUES (605, 607, 'trade:mch-notice', 'admin', 'MchNoticeTaskList', 'menu.trade.mchNotice', 'lucide:bell-ring', false, false, '/payment/notice/MchNoticeTaskList', '/trade/record/mch-notice', NULL, 1, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-21 03:00:00+00', '2026-07-21 06:30:00+00');
INSERT INTO public.iam_perm_menu VALUES (606, 607, 'trade:callback-record', 'admin', 'PayCallbackRecordList', 'menu.trade.callbackRecord', 'lucide:inbox', false, false, '/payment/record/PayCallbackRecordList', '/trade/record/callback-record', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-21 06:00:00+00', '2026-07-21 06:30:00+00');
INSERT INTO public.iam_perm_menu VALUES (905, 4040130, 'merchant:terminal', 'admin', 'SystemTerminalList', 'menu.payment.merchant.terminal', NULL, true, false, '/payment/device/terminal/system/SystemTerminalList', '/payment/device/terminal/system', NULL, 16, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-17 00:00:00+00', '2026-07-22 13:50:46.597022+00');
INSERT INTO public.iam_perm_menu VALUES (40402, 404, 'channel:merchant', 'admin', 'ChannelMerchantGlobal', 'menu.payment.merchant.channelMerchant.global', 'lucide:repeat', false, false, '/payment/global/channel-merchant-global/ChannelMerchantGlobalList', '/payment/global/channel-merchants', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-13 13:12:31.180398+00', '2026-07-22 13:50:46.629357+00');
INSERT INTO public.iam_perm_menu VALUES (91001, NULL, NULL, 'merchant', 'Dashboard', 'menu.dashboard', 'lucide:layout-dashboard', false, false, NULL, '/dashboard', '/workspace', -1, false, false, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-23 02:30:21.982361+00', '2026-07-23 02:30:21.982361+00');
INSERT INTO public.iam_perm_menu VALUES (91002, 91001, 'dashboard:workspace', 'merchant', 'Workspace', 'menu.dashboard.workspace', 'lucide:panels-top-left', false, false, '/dashboard/workspace/index', '/workspace', NULL, 1, false, false, true, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 02:30:21.991345+00', '2026-07-23 02:30:21.991345+00');
INSERT INTO public.iam_perm_menu VALUES (91003, 91001, 'dashboard:analytics', 'merchant', 'Analytics', 'menu.dashboard.analytics', 'lucide:area-chart', false, false, '/dashboard/analytics/index', '/analytics', NULL, 2, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 02:30:21.994392+00', '2026-07-23 02:30:21.994392+00');
-- 商户端支付单目录旧副本(91104/91102/91105)与记录目录旧副本(91106/91107/91108)已于 2026-08-23 清理,
-- 正式树为 91110→91111/91112 与 91200→91201/91202, 旧副本从未绑定角色
INSERT INTO public.iam_perm_menu VALUES (91111, 91110, 'trade:order', 'merchant', 'NormalOrderList', 'menu.trade.normalPay', 'lucide:file-text', false, false, '/payment/order/NormalOrderList', '/trade/pay-order/normal', NULL, 1, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.645988+00', '2026-07-23 09:32:12.645988+00');
INSERT INTO public.iam_perm_menu VALUES (91112, 91110, 'trade:gateway-order', 'merchant', 'GatewayOrderList', 'menu.trade.gatewayOrder', 'lucide:globe', false, false, '/payment/order/GatewayOrderList', '/trade/pay-order/gateway', NULL, 2, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.648789+00', '2026-07-23 09:32:12.648789+00');
INSERT INTO public.iam_perm_menu VALUES (91100, NULL, 'trade', 'merchant', 'TransactionManagement', 'menu.trade', 'lucide:arrow-left-right', false, false, NULL, '/trade', '/trade/pay-trade', 3, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-23 03:48:14.886531+00', '2026-07-23 06:44:16.725707+00');
INSERT INTO public.iam_perm_menu VALUES (91201, 91200, 'trade:mch-notice', 'merchant', 'MchNoticeTaskList', 'menu.trade.mchNotice', 'lucide:bell-ring', false, false, '/payment/notice/MchNoticeTaskList', '/trade/record/mch-notice', NULL, 1, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.655107+00', '2026-07-23 09:32:12.655107+00');
INSERT INTO public.iam_perm_menu VALUES (91301, 91300, 'merchant:info', 'merchant', 'MchInfoManage', 'menu.payment.merchant.profile', 'lucide:badge-info', false, false, '/payment/merchant/info/MchInfoManage', '/mch/info', NULL, 1, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.665593+00', '2026-07-23 09:32:12.665593+00');
INSERT INTO public.iam_perm_menu VALUES (91302, 91300, 'merchant:user', 'merchant', 'MerchantUserList', 'menu.payment.merchant.user', 'lucide:users', false, false, '/payment/merchant/user/MerchantUserList', '/mch/user', NULL, 2, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.669009+00', '2026-07-23 09:32:12.669009+00');
INSERT INTO public.iam_perm_menu VALUES (91303, 91300, 'merchant:store', 'merchant', 'MchStoreInfoList', 'menu.payment.merchant.store', 'lucide:store', false, false, '/payment/merchant/store/MchStoreInfoList', '/mch/store', NULL, 3, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.671534+00', '2026-07-23 09:32:12.671534+00');
INSERT INTO public.iam_perm_menu VALUES (91304, 91300, 'merchant:terminal', 'merchant', 'SystemTerminalList', 'menu.payment.merchant.terminal', 'lucide:monitor', false, false, '/payment/device/terminal/system/SystemTerminalList', '/mch/terminal', NULL, 4, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.67447+00', '2026-07-23 09:32:12.67447+00');
INSERT INTO public.iam_perm_menu VALUES (91401, 91400, 'merchant:app', 'merchant', 'MchAppInfoList', 'menu.payment.merchant.app', 'lucide:app-window', false, false, '/payment/merchant/app/MchAppInfoList', '/mch/app', NULL, 1, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.681008+00', '2026-07-23 09:32:12.681008+00');
INSERT INTO public.iam_perm_menu VALUES (91402, 91400, 'channel:merchant', 'merchant', 'ChannelMerchantList', 'menu.payment.merchant.channelMerchant', 'lucide:store', false, false, '/payment/channel-merchant/ChannelMerchantList', '/mch/channel-merchant', NULL, 2, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.684068+00', '2026-07-23 09:32:12.684068+00');
INSERT INTO public.iam_perm_menu VALUES (91411, 91402, 'channel:merchant:detail', 'merchant', 'ChannelMerchantDetail', 'menu.payment.merchant.channelMerchant.detail', NULL, true, false, '/payment/channel-merchant/detail/ChannelMerchantDetailDispatch', '/mch/channel-merchant/detail', NULL, 2, false, false, false, 0, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.689613+00', '2026-07-23 09:32:12.689613+00');
INSERT INTO public.iam_perm_menu VALUES (91415, 91402, 'merchant:alipay-isv-auth', 'merchant', 'AlipayIsvAuthPermission', 'menu.payment.merchant.channelMerchant.detail', NULL, true, false, NULL, NULL, NULL, 3, false, false, false, 0, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, '', '', '2026-08-27 09:30:00+00', '2026-08-27 09:30:00+00');
INSERT INTO public.iam_perm_menu VALUES (91403, 91401, NULL, 'merchant', 'AppConfigDir', 'menu.payment.merchant.appWorkbench', NULL, true, false, NULL, '/mch/app-config', NULL, 4, false, false, false, 0, NULL, 0, false, 'subpage_group', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-23 09:32:12.692738+00', '2026-07-23 09:32:12.692738+00');
INSERT INTO public.iam_perm_menu VALUES (40107, 40106, 'payment:wx:mch-app', 'admin', 'WxMchApp', 'menu.payment.wx.mchApp', NULL, true, false, NULL, NULL, NULL, 1, false, false, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-23 13:44:05.963399+00', '2026-07-23 14:32:14.494862+00');
INSERT INTO public.iam_perm_menu VALUES (91412, 91400, 'payment:wx:mch-app', 'merchant', 'MchWxAppList', 'menu.payment.wx.mchApp', 'lucide:message-circle', false, false, '/payment/wx/mch/MchWxAppList', '/mch/wx-app', NULL, 3, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-24 01:58:27.304104+00', '2026-07-24 01:58:27.304104+00');
INSERT INTO public.iam_perm_menu VALUES (91413, 91403, 'merchant:app:workbench', 'merchant', 'MchAppWorkbench', 'menu.payment.merchant.appWorkbench', NULL, true, false, '/payment/merchant/app/MchAppWorkbench', '/mch/app/manage', NULL, 0, false, false, false, 0, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-24 10:20:36.109679+00', '2026-07-24 13:08:44.946319+00');
INSERT INTO public.iam_perm_menu VALUES (91414, 91403, 'merchant:notify-config', 'merchant', 'MchAppNotifyConfigPermission', 'menu.payment.merchant.appWorkbench', NULL, true, false, NULL, NULL, NULL, 5, false, false, false, 0, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, '', '', '2026-08-27 09:30:00+00', '2026-08-27 09:30:00+00');
INSERT INTO public.iam_perm_menu VALUES (91300, NULL, NULL, 'merchant', 'MchCenter', 'menu.mch.center', 'lucide:building-2', false, false, NULL, '/mch-center', '/mch/info', 1, false, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-23 09:32:12.662617+00', '2026-07-23 09:32:12.662617+00');
INSERT INTO public.iam_perm_menu VALUES (91400, NULL, NULL, 'merchant', 'PaymentConfig', 'menu.payment.config', 'lucide:settings-2', false, false, NULL, '/mch-payment', '/mch/app', 2, false, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-23 09:32:12.677882+00', '2026-07-23 09:32:12.677882+00');
INSERT INTO public.iam_perm_menu VALUES (91202, 91200, 'trade:callback-record', 'merchant', 'PayCallbackRecordList', 'menu.trade.callbackRecord', 'lucide:inbox', false, false, '/payment/record/PayCallbackRecordList', '/trade/record/callback-record', NULL, 2, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.658982+00', '2026-07-23 09:32:12.658982+00');
INSERT INTO public.iam_perm_menu VALUES (40106, 409, 'payment:wx:platform-app', 'admin', 'WxAppHub', 'menu.payment.wx.app', 'lucide:message-circle', false, false, '/payment/wx/WxAppHub', '/payment/wx/app', NULL, 10, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-23 13:44:05.953634+00', '2026-07-31 03:18:17.00789+00');
INSERT INTO public.iam_perm_menu VALUES (91404, 91403, 'merchant:app:route', 'merchant', 'PayRouteConfig', 'menu.payment.merchant.payRoute', 'lucide:git-branch', true, false, '/payment/route/PayRouteConfig', '/mch/route', NULL, 1, false, false, false, 0, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.695589+00', '2026-07-23 09:32:12.695589+00');
INSERT INTO public.iam_perm_menu VALUES (91407, 91403, 'merchant:gateway-cashier', 'merchant', 'CashierConfig', 'menu.payment.merchant.cashierConfig', 'lucide:monitor-smartphone', true, false, '/payment/merchant/cashier/CashierConfig', '/mch/cashier', NULL, 4, false, false, false, 0, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.704415+00', '2026-07-23 09:32:12.704415+00');
INSERT INTO public.iam_perm_menu VALUES (91409, 91403, 'merchant:easypay', 'merchant', 'EasyPayConfig', 'menu.payment.merchant.easypay', 'lucide:plug', true, false, '/payment/merchant/easypay/EasyPayConfig', '/mch/easypay', NULL, 6, false, false, false, 0, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, '', '', '2026-07-23 09:32:12.710238+00', '2026-07-23 09:32:12.710238+00');
INSERT INTO public.iam_perm_menu VALUES (91501, 91300, 'merchant:credential', 'merchant', 'MerchantCredentialConfig', 'menu.payment.merchant.credential', 'lucide:key', false, false, '/payment/merchant/credential/MerchantCredentialConfig', '/mch/credential', NULL, 5, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-24 13:08:44.950986+00', '2026-07-24 13:08:44.950986+00');
INSERT INTO public.iam_perm_menu VALUES (91305, 91300, 'merchant:wx-verify', 'merchant', 'MchWxDomainVerifyList', 'menu.payment.merchant.wxVerify', 'lucide:shield-check', false, false, '/payment/merchant/wx-verify/MchWxDomainVerifyList', '/mch/wx-verify', NULL, 6, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-24 14:17:59.251093+00', '2026-07-24 14:17:59.251093+00');
INSERT INTO public.iam_perm_menu VALUES (91421, 91402, 'channel:app', 'merchant', 'AlipayMchAppManage', 'menu.payment.merchant.channelMerchant.alipayApp', NULL, true, false, '/payment/channel/alipay/manage/mch/app/AlipayMchAppManage', '/mch/channel-merchant/alipay-app-manage', NULL, 4, false, false, false, 0, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-25 01:53:14.928869+00', '2026-07-25 01:53:14.928869+00');
INSERT INTO public.iam_perm_menu VALUES (91601, 91600, 'develop:trade', 'merchant', 'DevelopTrade', 'menu.develop.trade', 'lucide:credit-card', false, false, '/payment/develop/trade/DevelopTrade', '/develop/trade', NULL, 1, false, false, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-25 15:10:00+00', '2026-07-25 15:10:00+00');
INSERT INTO public.iam_perm_menu VALUES (91602, 91600, 'develop:gateway', 'merchant', 'DevelopGateway', 'menu.develop.gateway', 'lucide:globe', false, false, '/payment/develop/gateway/DevelopGateway', '/develop/gateway', NULL, 2, false, false, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-25 15:10:00+00', '2026-07-25 15:10:00+00');
INSERT INTO public.iam_perm_menu VALUES (91603, 91600, 'develop:sign', 'merchant', 'DevelopSign', 'menu.develop.sign', 'lucide:pen-tool', false, false, '/payment/develop/sign/DevelopSign', '/develop/sign', NULL, 3, false, false, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-25 15:10:00+00', '2026-07-25 15:10:00+00');
INSERT INTO public.iam_perm_menu VALUES (91604, 91600, 'develop:auth', 'merchant', 'ChannelAuth', 'menu.develop.auth', 'lucide:shield-check', false, false, '/payment/develop/auth/ChannelAuth', '/develop/auth', NULL, 4, false, false, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-25 15:10:00+00', '2026-07-25 15:10:00+00');
INSERT INTO public.iam_perm_menu VALUES (91600, NULL, NULL, 'merchant', 'Develop', 'menu.develop', 'lucide:wrench', false, false, NULL, '/develop', '/develop/trade', 9, false, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-25 15:10:00+00', '2026-07-25 15:10:00+00');
INSERT INTO public.iam_perm_menu VALUES (701, 7, NULL, 'admin', 'EasyPayPlugin', 'menu.plugin.easypay', 'lucide:plug', false, false, NULL, '/plugin/easypay', '/plugin/easypay/order', 1, false, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-26 02:00:00+00', '2026-07-26 02:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (70101, 701, 'plugin:easypay-order', 'admin', 'EasyPayOrderList', 'menu.plugin.easypay.order', 'lucide:credit-card', false, false, '/plugin/easypay/EasyPayOrderList', '/plugin/easypay/order', NULL, 1, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-26 02:00:00+00', '2026-07-26 02:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (70102, 701, 'plugin:easypay-refund', 'admin', 'EasyPayRefundOrderList', 'menu.plugin.easypay.refund', 'lucide:undo-2', false, false, '/plugin/easypay/EasyPayRefundOrderList', '/plugin/easypay/refund-order', NULL, 2, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-26 02:00:00+00', '2026-07-26 02:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (91701, 91700, NULL, 'merchant', 'EasyPayPlugin', 'menu.plugin.easypay', 'lucide:plug', false, false, NULL, '/plugin/easypay', '/plugin/easypay/order', 1, false, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-26 02:00:00+00', '2026-07-26 02:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (91702, 91701, 'plugin:easypay-order', 'merchant', 'EasyPayOrderList', 'menu.plugin.easypay.order', 'lucide:credit-card', false, false, '/plugin/easypay/EasyPayOrderList', '/plugin/easypay/order', NULL, 1, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-26 02:00:00+00', '2026-07-26 02:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (91703, 91701, 'plugin:easypay-refund', 'merchant', 'EasyPayRefundOrderList', 'menu.plugin.easypay.refund', 'lucide:undo-2', false, false, '/plugin/easypay/EasyPayRefundOrderList', '/plugin/easypay/refund-order', NULL, 2, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-26 02:00:00+00', '2026-07-26 02:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (7, NULL, 'plugin', 'admin', 'PluginManagement', 'menu.plugin', 'lucide:puzzle', false, false, NULL, '/plugin', '/plugin/easypay/order', 6, false, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-26 02:00:00+00', '2026-07-26 02:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (91700, NULL, 'plugin', 'merchant', 'PluginManagement', 'menu.plugin', 'lucide:puzzle', false, false, NULL, '/plugin', '/plugin/easypay/order', 10, false, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-26 02:00:00+00', '2026-07-26 02:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (205, 2, 'demos:ip-region', 'admin', 'IpRegionDemo', 'menu.demos.ipRegion', 'lucide:locate-fixed', false, false, '/demos/ip-region/IpRegionDemo', '/demos/ip-region', NULL, 5, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-27 16:00:00+00', '2026-07-27 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (40109, 40108, 'payment:douyin:mch-app', 'admin', 'DyMchApp', 'menu.payment.douyin.mchApp', NULL, true, false, NULL, NULL, NULL, 1, false, false, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-28 14:00:00+00', '2026-07-28 14:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (4040121, 4040130, 'merchant:gateway-pay-config', 'admin', 'GatewayPayConfig', 'menu.payment.merchant.gatewayPayConfig', NULL, true, false, '/payment/merchant/gateway-config/GatewayPayConfig', '/payment/merchant/gateway-config', NULL, 3, false, true, false, 1, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-30 12:24:51.675237+00', '2026-07-30 12:24:51.675237+00');
INSERT INTO public.iam_perm_menu VALUES (91405, 91403, 'merchant:gateway-pay-config', 'merchant', 'GatewayPayConfig', 'menu.payment.merchant.gatewayPayConfig', 'lucide:qr-code', true, false, '/payment/merchant/gateway-config/GatewayPayConfig', '/mch/gateway-config', NULL, 2, false, false, false, 0, 1, 1, false, 'subpage', NULL, NULL, NULL, NULL, '', '', '2026-07-30 12:24:51.676811+00', '2026-07-30 12:24:51.676811+00');
INSERT INTO public.iam_perm_menu VALUES (409, 4, 'payment:app', 'admin', 'PaymentApp', 'menu.payment.app', 'lucide:layout-grid', false, false, NULL, '/payment/app', NULL, 30, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-31 03:18:16.99898+00', '2026-07-31 03:18:16.99898+00');
INSERT INTO public.iam_perm_menu VALUES (611, NULL, 'trade:transfer', 'admin', 'TransferCatalog', 'menu.trade.transfer', 'lucide:send', false, false, NULL, '/trade/transfer', '/trade/transfer/wechat', 4.3, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-04 16:00:00+00', '2026-08-04 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (91110, 91100, 'trade:pay-order', 'merchant', 'PayOrderCatalog', 'menu.trade.payOrder', 'lucide:receipt', false, false, NULL, '/trade/pay-order', '/trade/pay-order/normal', 3, false, true, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-01 01:15:14.072785+00', '2026-08-01 01:15:14.072785+00');
INSERT INTO public.iam_perm_menu VALUES (206, 2, 'demos:callback', 'admin', 'CallbackDemo', 'menu.demos.callback', 'lucide:webhook', false, false, '/demos/callback/CallbackDemo', '/demos/callback', NULL, 5, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-01 16:00:00+00', '2026-08-01 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (40108, 409, 'payment:douyin:platform-app', 'admin', 'DyAppHub', 'menu.payment.douyin.app', 'lucide:music-2', false, false, '/payment/douyin/DyAppHub', '/payment/douyin/app', NULL, 20, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-28 14:00:00+00', '2026-07-31 03:18:17.0111+00');
INSERT INTO public.iam_perm_menu VALUES (91420, 91400, 'payment:douyin:mch-app', 'merchant', 'MchDyAppList', 'menu.payment.douyin.mchApp', 'lucide:music-2', false, false, '/payment/douyin/mch/MchDyAppList', '/mch/douyin-app', NULL, 4, false, false, false, 0, 1, 1, false, 'menu', NULL, NULL, NULL, NULL, '', '', '2026-07-28 14:00:00+00', '2026-07-28 14:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (61101, 61106, 'trade:transfer:wechat', 'admin', 'WechatTransferList', 'menu.trade.transfer.wechat', 'lucide:message-circle', false, true, '/payment/transfer/WechatTransferList', '/trade/transfer/wechat', NULL, 1, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-04 16:00:00+00', '2026-08-04 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (61102, 61106, 'trade:transfer:alipay', 'admin', 'AlipayTransferList', 'menu.trade.transfer.alipay', 'lucide:credit-card', false, true, '/payment/transfer/AlipayTransferList', '/trade/transfer/alipay', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-04 16:00:00+00', '2026-08-04 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (91120, NULL, 'trade:transfer', 'merchant', 'TransferCatalog', 'menu.trade.transfer', 'lucide:send', false, false, NULL, '/trade/transfer', '/trade/transfer/wechat', 3.5, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-04 16:00:00+00', '2026-08-04 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (61106, 611, 'trade:transfer:channel', 'admin', 'TransferChannelCatalog', 'menu.trade.transfer.channel', 'lucide:layers', false, false, NULL, '/trade/transfer/channel', '/trade/transfer/channel/wechat', 3, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-05 12:50:41.670351+00', '2026-08-05 12:50:41.670351+00');
INSERT INTO public.iam_perm_menu VALUES (91126, 91120, 'trade:transfer:channel', 'merchant', 'TransferChannelCatalog', 'menu.trade.transfer.channel', 'lucide:layers', false, false, NULL, '/trade/transfer/channel', '/trade/transfer/channel/wechat', 3, false, true, false, 1, 1, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-05 12:50:41.679227+00', '2026-08-05 12:50:41.679227+00');
INSERT INTO public.iam_perm_menu VALUES (61103, 61106, 'trade:transfer:douyin', 'admin', 'DouyinTransferList', 'menu.trade.transfer.douyin', 'lucide:clapperboard', false, true, '/payment/transfer/DouyinTransferList', '/trade/transfer/douyin', NULL, 3, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-04 16:00:00+00', '2026-08-04 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (91121, 91126, 'trade:transfer:wechat', 'merchant', 'WechatTransferList', 'menu.trade.transfer.wechat', 'lucide:message-circle', false, true, '/payment/transfer/WechatTransferList', '/trade/transfer/wechat', NULL, 1, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-04 16:00:00+00', '2026-08-04 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (91122, 91126, 'trade:transfer:alipay', 'merchant', 'AlipayTransferList', 'menu.trade.transfer.alipay', 'lucide:credit-card', false, true, '/payment/transfer/AlipayTransferList', '/trade/transfer/alipay', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-04 16:00:00+00', '2026-08-04 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (91123, 91126, 'trade:transfer:douyin', 'merchant', 'DouyinTransferList', 'menu.trade.transfer.douyin', 'lucide:clapperboard', false, true, '/payment/transfer/DouyinTransferList', '/trade/transfer/douyin', NULL, 3, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-04 16:00:00+00', '2026-08-04 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (61105, 611, 'trade:transfer:create', 'admin', 'TransferCreate', 'menu.trade.transfer.create', 'lucide:plus-circle', false, false, '/payment/transfer/TransferCreate', '/trade/transfer/create', NULL, 1, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-05 12:13:20.606538+00', '2026-08-05 12:13:20.606538+00');
INSERT INTO public.iam_perm_menu VALUES (91125, 91120, 'trade:transfer:create', 'merchant', 'TransferCreate', 'menu.trade.transfer.create', 'lucide:plus-circle', false, false, '/payment/transfer/TransferCreate', '/trade/transfer/create', NULL, 1, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-05 12:13:20.608919+00', '2026-08-05 12:13:20.608919+00');
INSERT INTO public.iam_perm_menu VALUES (61104, 611, 'trade:transfer:trade', 'admin', 'TransferTradeList', 'menu.trade.transfer.trade', 'lucide:list-ordered', false, true, '/payment/transfer/TransferTradeList', '/trade/transfer/trade', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-04 16:00:00+00', '2026-08-04 16:00:00+00');
INSERT INTO public.iam_perm_menu VALUES (91124, 91120, 'trade:transfer:trade', 'merchant', 'TransferTradeList', 'menu.trade.transfer.trade', 'lucide:list-ordered', false, true, '/payment/transfer/TransferTradeList', '/trade/transfer/trade', NULL, 2, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-04 16:00:00+00', '2026-08-04 16:00:00+00');


--
-- Data for Name: iam_role; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.iam_role VALUES (1, 'admin_admin', 'admin', NULL, true, '系统内置运营管理员角色', 1, 1, 0, false, '2026-07-14 07:08:13.608733+00', '2026-07-14 07:08:13.608733+00', 'role.admin_admin');
INSERT INTO public.iam_role VALUES (2, 'merchant_admin', 'merchant', NULL, true, '系统内置商户管理员角色', 1, 1, 0, false, '2026-07-14 07:08:13.608733+00', '2026-07-14 07:08:13.608733+00', 'role.merchant_admin');


--
-- Data for Name: iam_role_code; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.iam_role_code VALUES (2070864264909631489, 1, 2070862264909631489);
INSERT INTO public.iam_role_code VALUES (2070864264913825792, 1, 2070862264913825792);
INSERT INTO public.iam_role_code VALUES (2076550114998755328, 1, 2076548114998755328);
INSERT INTO public.iam_role_code VALUES (2070864264922214401, 1, 2070862264922214401);
INSERT INTO public.iam_role_code VALUES (2070864264930603008, 1, 2070862264930603008);
INSERT INTO public.iam_role_code VALUES (2070864264934797312, 1, 2070862264934797312);
INSERT INTO public.iam_role_code VALUES (2076550115791478784, 1, 2076548115791478784);
INSERT INTO public.iam_role_code VALUES (2076550115795673088, 1, 2076548115795673088);
INSERT INTO public.iam_role_code VALUES (2076550115795673089, 1, 2076548115795673089);
INSERT INTO public.iam_role_code VALUES (2070864264985128961, 1, 2070862264985128961);
INSERT INTO public.iam_role_code VALUES (2070864264993517568, 1, 2070862264993517568);
INSERT INTO public.iam_role_code VALUES (2070864264993517569, 1, 2070862264993517569);
INSERT INTO public.iam_role_code VALUES (2070864264997711872, 1, 2070862264997711872);
INSERT INTO public.iam_role_code VALUES (2070864264968351744, 1, 2070862264968351744);
INSERT INTO public.iam_role_code VALUES (2070864264968351745, 1, 2070862264968351745);
INSERT INTO public.iam_role_code VALUES (2070864264989323264, 1, 2070862264989323264);
INSERT INTO public.iam_role_code VALUES (2070864264989323265, 1, 2070862264989323265);
INSERT INTO public.iam_role_code VALUES (2070864264997711873, 1, 2070862264997711873);
INSERT INTO public.iam_role_code VALUES (2070864265001906176, 1, 2070862265001906176);
INSERT INTO public.iam_role_code VALUES (2070864265001906177, 1, 2070862265001906177);
INSERT INTO public.iam_role_code VALUES (2070864265001906178, 1, 2070862265001906178);
INSERT INTO public.iam_role_code VALUES (2070864264955768832, 1, 2070862264955768832);
INSERT INTO public.iam_role_code VALUES (2070864264955768833, 1, 2070862264955768833);
INSERT INTO public.iam_role_code VALUES (2070864264964157440, 1, 2070862264964157440);
INSERT INTO public.iam_role_code VALUES (2070864264964157441, 1, 2070862264964157441);
INSERT INTO public.iam_role_code VALUES (2070864264976740353, 1, 2070862264976740353);
INSERT INTO public.iam_role_code VALUES (2070864264980934657, 1, 2070862264980934657);
INSERT INTO public.iam_role_code VALUES (2070864264985128960, 1, 2070862264985128960);
INSERT INTO public.iam_role_code VALUES (2070864264980934656, 1, 2070862264980934656);
INSERT INTO public.iam_role_code VALUES (2070864264976740352, 1, 2070862264976740352);
INSERT INTO public.iam_role_code VALUES (2072992657125986304, 1, 2072990657125986304);
INSERT INTO public.iam_role_code VALUES (2070864265022877697, 1, 2070862265022877697);
INSERT INTO public.iam_role_code VALUES (2070864265027072001, 1, 2070862265027072001);
INSERT INTO public.iam_role_code VALUES (2070864265031266304, 1, 2070862265031266304);
INSERT INTO public.iam_role_code VALUES (2070864265031266305, 1, 2070862265031266305);
INSERT INTO public.iam_role_code VALUES (2070864265035460608, 1, 2070862265035460608);
INSERT INTO public.iam_role_code VALUES (2070864265039654913, 1, 2070862265039654913);
INSERT INTO public.iam_role_code VALUES (2070864265043849216, 1, 2070862265043849216);
INSERT INTO public.iam_role_code VALUES (2070864265048043520, 1, 2070862265048043520);
INSERT INTO public.iam_role_code VALUES (2070864265048043521, 1, 2070862265048043521);
INSERT INTO public.iam_role_code VALUES (2070864265048043522, 1, 2070862265048043522);
INSERT INTO public.iam_role_code VALUES (2070864265052237824, 1, 2070862265052237824);
INSERT INTO public.iam_role_code VALUES (2072234741347454976, 1, 2072232741347454976);
INSERT INTO public.iam_role_code VALUES (2072234741381009408, 1, 2072232741381009408);
INSERT INTO public.iam_role_code VALUES (2072234741385203712, 1, 2072232741385203712);
INSERT INTO public.iam_role_code VALUES (2072234741389398016, 1, 2072232741389398016);
INSERT INTO public.iam_role_code VALUES (2072234741389398017, 1, 2072232741389398017);
INSERT INTO public.iam_role_code VALUES (2072234741389398018, 1, 2072232741389398018);
INSERT INTO public.iam_role_code VALUES (2070864265069015040, 1, 2070862265069015040);
INSERT INTO public.iam_role_code VALUES (2070864265056432129, 1, 2070862265056432129);
INSERT INTO public.iam_role_code VALUES (2070864265018683392, 1, 2070862265018683392);
INSERT INTO public.iam_role_code VALUES (2070864265022877696, 1, 2070862265022877696);
INSERT INTO public.iam_role_code VALUES (2070864265027072000, 1, 2070862265027072000);
INSERT INTO public.iam_role_code VALUES (2070864265073209344, 1, 2070862265073209344);
INSERT INTO public.iam_role_code VALUES (2072992657130180608, 1, 2072990657130180608);
INSERT INTO public.iam_role_code VALUES (2072992657130180609, 1, 2072990657130180609);
INSERT INTO public.iam_role_code VALUES (2072379871723388929, 1, 2072377871723388929);
INSERT INTO public.iam_role_code VALUES (2072379871727583232, 1, 2072377871727583232);
INSERT INTO public.iam_role_code VALUES (2070864265043849217, 1, 2070862265043849217);
INSERT INTO public.iam_role_code VALUES (2070864265064820736, 1, 2070862265064820736);
INSERT INTO public.iam_role_code VALUES (2072379871668862976, 1, 2072377871668862976);
INSERT INTO public.iam_role_code VALUES (2072379871723388928, 1, 2072377871723388928);
INSERT INTO public.iam_role_code VALUES (2075053483223699456, 1, 2075051483223699456);
INSERT INTO public.iam_role_code VALUES (2075053483370500096, 1, 2075051483370500096);
INSERT INTO public.iam_role_code VALUES (2075454237802512384, 1, 2075452237802512384);
INSERT INTO public.iam_role_code VALUES (2075454238549098496, 1, 2075452238549098496);
INSERT INTO public.iam_role_code VALUES (2075454238553292800, 1, 2075452238553292800);
INSERT INTO public.iam_role_code VALUES (2075053483374694400, 1, 2075051483374694400);
INSERT INTO public.iam_role_code VALUES (2075053483374694401, 1, 2075051483374694401);
INSERT INTO public.iam_role_code VALUES (2075454238557487104, 1, 2075452238557487104);
INSERT INTO public.iam_role_code VALUES (2075454238561681408, 1, 2075452238561681408);
INSERT INTO public.iam_role_code VALUES (2075771305903063040, 1, 2075769305903063040);
INSERT INTO public.iam_role_code VALUES (2075771305903063041, 1, 2075769305903063041);
INSERT INTO public.iam_role_code VALUES (2075847892346347520, 1, 2075845892346347520);
INSERT INTO public.iam_role_code VALUES (2075847892476370944, 1, 2075845892476370944);
INSERT INTO public.iam_role_code VALUES (2072992657067266048, 1, 2072990657067266048);
INSERT INTO public.iam_role_code VALUES (2070864265073209345, 1, 2070862265073209345);
INSERT INTO public.iam_role_code VALUES (2070864265069015041, 1, 2070862265069015041);
INSERT INTO public.iam_role_code VALUES (2080584149952053248, 2, 2079866295619362816);
INSERT INTO public.iam_role_code VALUES (2080584149964636160, 2, 2079866295615168512);
INSERT INTO public.iam_role_code VALUES (2080584149964636161, 2, 2079866295577419776);
INSERT INTO public.iam_role_code VALUES (2080584149968830464, 2, 2072377871727583232);
INSERT INTO public.iam_role_code VALUES (2080584149968830465, 2, 2072377871723388929);
INSERT INTO public.iam_role_code VALUES (2080584149968830466, 2, 2072990657130180609);
INSERT INTO public.iam_role_code VALUES (2080584149968830467, 2, 2072990657130180608);
INSERT INTO public.iam_role_code VALUES (2080584149973024771, 2, 2070862265001906178);
INSERT INTO public.iam_role_code VALUES (2080584149973024772, 2, 2070862265001906177);
INSERT INTO public.iam_role_code VALUES (2080584149977219072, 2, 2078486217954107392);
INSERT INTO public.iam_role_code VALUES (2080584149977219073, 2, 2078486217727614976);
INSERT INTO public.iam_role_code VALUES (2080584149977219074, 2, 2070862264993517568);
INSERT INTO public.iam_role_code VALUES (2080584149977219075, 2, 2070862264985128961);
INSERT INTO public.iam_role_code VALUES (2080584149977219076, 2, 2070862264913825792);
INSERT INTO public.iam_role_code VALUES (2080584149977219077, 2, 2070862264909631489);
INSERT INTO public.iam_role_code VALUES (2080584149977219078, 2, 2080304418371350528);
INSERT INTO public.iam_role_code VALUES (2080584149981413376, 2, 2080304418086137856);
INSERT INTO public.iam_role_code VALUES (2080584149981413377, 2, 2070862264989323265);
INSERT INTO public.iam_role_code VALUES (2080584149981413378, 2, 2070862264989323264);
INSERT INTO public.iam_role_code VALUES (2080584149981413383, 2, 2075845892476370944);
INSERT INTO public.iam_role_code VALUES (2080584149985607680, 2, 2075845892346347520);
INSERT INTO public.iam_role_code VALUES (2080584149985607683, 2, 2077241592707678208);
INSERT INTO public.iam_role_code VALUES (2080584149989801984, 2, 2077241592518934528);
INSERT INTO public.iam_role_code VALUES (2080641499132018688, 2, 2070862264997711872);
INSERT INTO public.iam_role_code VALUES (2080641499140407296, 2, 2070862264993517569);
INSERT INTO public.iam_role_code VALUES (2080662590080622592, 2, 2075051483370500096);
INSERT INTO public.iam_role_code VALUES (2080662590093205504, 2, 2075051483223699456);
INSERT INTO public.iam_role_code VALUES (2080835059898798080, 2, 2076548115791478784);
INSERT INTO public.iam_role_code VALUES (2080835059907186688, 2, 2076548114998755328);
INSERT INTO public.iam_role_code VALUES (2080921659357941760, 2, 2070862264934797312);
INSERT INTO public.iam_role_code VALUES (2080921659366330368, 2, 2070862264930603008);
INSERT INTO public.iam_role_code VALUES (2080921659366330369, 2, 2078479091642855424);
INSERT INTO public.iam_role_code VALUES (2080921659366330370, 2, 2070862264922214401);
INSERT INTO public.iam_role_code VALUES (2080921659366330371, 2, 2075452237802512384);
INSERT INTO public.iam_role_code VALUES (2083360959894851584, 2, 2082811057204805632);
INSERT INTO public.iam_role_code VALUES (2083360959907434496, 2, 2082811057154473984);
INSERT INTO public.iam_role_code VALUES (2083360959907434497, 2, 2082124981007970304);
INSERT INTO public.iam_role_code VALUES (2083360959907434498, 2, 2082124980798255104);
INSERT INTO public.iam_role_code VALUES (2083360959907434499, 2, 2072377871723388928);
INSERT INTO public.iam_role_code VALUES (2083360959907434500, 2, 2072377871668862976);
INSERT INTO public.iam_role_code VALUES (2083360959907434501, 2, 2075769305903063041);
INSERT INTO public.iam_role_code VALUES (2083360959911628800, 2, 2075769305903063040);
INSERT INTO public.iam_role_code VALUES (2083360959911628801, 2, 2082124981012164610);
INSERT INTO public.iam_role_code VALUES (2083360959911628802, 2, 2082124981012164609);
INSERT INTO public.iam_role_code VALUES (2083360959911628803, 2, 2082124981016358913);
INSERT INTO public.iam_role_code VALUES (2083360959915823104, 2, 2082124981016358912);
INSERT INTO public.iam_role_code VALUES (2077568468000000101, 1, 2077568468000000001);
INSERT INTO public.iam_role_code VALUES (2077568468000000102, 1, 2077568468000000002);
INSERT INTO public.iam_role_code VALUES (2079866296000000201, 1, 2079866296000000101);
INSERT INTO public.iam_role_code VALUES (2079866296000000211, 2, 2079866296000000101);
INSERT INTO public.iam_role_code VALUES (2079866296000000202, 1, 2079866296000000102);
INSERT INTO public.iam_role_code VALUES (2079866296000000212, 2, 2079866296000000102);
INSERT INTO public.iam_role_code VALUES (2079866296000000203, 1, 2079866296000000103);
INSERT INTO public.iam_role_code VALUES (2079866296000000213, 2, 2079866296000000103);
INSERT INTO public.iam_role_code VALUES (2079866296000000204, 1, 2079866296000000104);
INSERT INTO public.iam_role_code VALUES (2079866296000000214, 2, 2079866296000000104);
INSERT INTO public.iam_role_code VALUES (2079866296000000205, 1, 2079866296000000105);
INSERT INTO public.iam_role_code VALUES (2079866296000000215, 2, 2079866296000000105);
INSERT INTO public.iam_role_code VALUES (2079866296000000301, 2, 2070862264997711873);
INSERT INTO public.iam_role_code VALUES (2079866296000000302, 2, 2070862265001906176);
INSERT INTO public.iam_role_code VALUES (2079866296000000303, 2, 2089898876290093056);
INSERT INTO public.iam_role_code VALUES (2079866296000000304, 2, 2089898876302675968);
INSERT INTO public.iam_role_code VALUES (2079866296000000305, 2, 2083000000000000001);
INSERT INTO public.iam_role_code VALUES (2079866296000000306, 2, 2083000000000000002);


--
-- Data for Name: iam_role_menu; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.iam_role_menu VALUES (1000000000202, 1, NULL, 202);
INSERT INTO public.iam_role_menu VALUES (1000000000307, 1, NULL, 307);
INSERT INTO public.iam_role_menu VALUES (1000000000305, 1, NULL, 305);
INSERT INTO public.iam_role_menu VALUES (1000000000302, 1, NULL, 302);
INSERT INTO public.iam_role_menu VALUES (1000000000304, 1, NULL, 304);
INSERT INTO public.iam_role_menu VALUES (1000000000401, 1, NULL, 401);
INSERT INTO public.iam_role_menu VALUES (1000000000203, 1, NULL, 203);
INSERT INTO public.iam_role_menu VALUES (1000000000003, 1, NULL, 3);
INSERT INTO public.iam_role_menu VALUES (1000000030102, 1, NULL, 30102);
INSERT INTO public.iam_role_menu VALUES (1000000000301, 1, NULL, 301);
INSERT INTO public.iam_role_menu VALUES (1000000030101, 1, NULL, 30101);
INSERT INTO public.iam_role_menu VALUES (1000000030601, 1, NULL, 30601);
INSERT INTO public.iam_role_menu VALUES (1000000030501, 1, NULL, 30501);
INSERT INTO public.iam_role_menu VALUES (1000000000002, 1, NULL, 2);
INSERT INTO public.iam_role_menu VALUES (1000000000001, 1, NULL, 1);
INSERT INTO public.iam_role_menu VALUES (1000000030202, 1, NULL, 30202);
INSERT INTO public.iam_role_menu VALUES (1000000000004, 1, NULL, 4);
INSERT INTO public.iam_role_menu VALUES (1000000030201, 1, NULL, 30201);
INSERT INTO public.iam_role_menu VALUES (1000000000102, 1, NULL, 102);
INSERT INTO public.iam_role_menu VALUES (1000000000101, 1, NULL, 101);
INSERT INTO public.iam_role_menu VALUES (1000000030103, 1, NULL, 30103);
INSERT INTO public.iam_role_menu VALUES (1000000030701, 1, NULL, 30701);
INSERT INTO public.iam_role_menu VALUES (1000000040401, 1, NULL, 40401);
INSERT INTO public.iam_role_menu VALUES (1000000000006, 1, NULL, 6);
INSERT INTO public.iam_role_menu VALUES (1000000030402, 1, NULL, 30402);
INSERT INTO public.iam_role_menu VALUES (1000000040104, 1, NULL, 40104);
INSERT INTO public.iam_role_menu VALUES (1000004040102, 1, NULL, 4040102);
INSERT INTO public.iam_role_menu VALUES (1000000000404, 1, NULL, 404);
INSERT INTO public.iam_role_menu VALUES (1000000040101, 1, NULL, 40101);
INSERT INTO public.iam_role_menu VALUES (1000004040101, 1, NULL, 4040101);
INSERT INTO public.iam_role_menu VALUES (1000000040103, 1, NULL, 40103);
INSERT INTO public.iam_role_menu VALUES (1000000040102, 1, NULL, 40102);
INSERT INTO public.iam_role_menu VALUES (1000004040108, 1, NULL, 4040108);
INSERT INTO public.iam_role_menu VALUES (1000004040110, 1, NULL, 4040110);
INSERT INTO public.iam_role_menu VALUES (1000000000601, 1, NULL, 601);
INSERT INTO public.iam_role_menu VALUES (1000004040114, 1, NULL, 4040114);
INSERT INTO public.iam_role_menu VALUES (1000004040112, 1, NULL, 4040112);
INSERT INTO public.iam_role_menu VALUES (1000004040111, 1, NULL, 4040111);
INSERT INTO public.iam_role_menu VALUES (1000004040109, 1, NULL, 4040109);
INSERT INTO public.iam_role_menu VALUES (1000004040117, 1, NULL, 4040117);
INSERT INTO public.iam_role_menu VALUES (1000000000603, 1, NULL, 603);
INSERT INTO public.iam_role_menu VALUES (1000000000008, 1, NULL, 8);
INSERT INTO public.iam_role_menu VALUES (1000000000204, 1, NULL, 204);
INSERT INTO public.iam_role_menu VALUES (1000000900001, 1, NULL, 900001);
INSERT INTO public.iam_role_menu VALUES (1000000030403, 1, NULL, 30403);
INSERT INTO public.iam_role_menu VALUES (1000004040103, 1, NULL, 4040103);
INSERT INTO public.iam_role_menu VALUES (1000000000801, 1, NULL, 801);
INSERT INTO public.iam_role_menu VALUES (1000000000802, 1, NULL, 802);
INSERT INTO public.iam_role_menu VALUES (1000004040106, 1, NULL, 4040106);
INSERT INTO public.iam_role_menu VALUES (1000000040105, 1, NULL, 40105);
INSERT INTO public.iam_role_menu VALUES (1000004040116, 1, NULL, 4040116);
INSERT INTO public.iam_role_menu VALUES (1000000000602, 1, NULL, 602);
INSERT INTO public.iam_role_menu VALUES (1000000000309, 1, NULL, 309);
INSERT INTO public.iam_role_menu VALUES (1000000000310, 1, NULL, 310);
INSERT INTO public.iam_role_menu VALUES (1000000000308, 1, NULL, 308);
INSERT INTO public.iam_role_menu VALUES (1000000040502, 1, NULL, 40502);
INSERT INTO public.iam_role_menu VALUES (1000000000901, 1, NULL, 901);
INSERT INTO public.iam_role_menu VALUES (1000000000803, 1, NULL, 803);
INSERT INTO public.iam_role_menu VALUES (1000004040121, 1, NULL, 4040121);
INSERT INTO public.iam_role_menu VALUES (1000000040507, 1, NULL, 40507);
INSERT INTO public.iam_role_menu VALUES (1000004040130, 1, NULL, 4040130);
INSERT INTO public.iam_role_menu VALUES (1000004040131, 1, NULL, 4040131);
INSERT INTO public.iam_role_menu VALUES (1000000040402, 1, NULL, 40402);
INSERT INTO public.iam_role_menu VALUES (1000004040122, 1, NULL, 4040122);
INSERT INTO public.iam_role_menu VALUES (1000004040120, 1, NULL, 4040120);
INSERT INTO public.iam_role_menu VALUES (1000000040501, 1, NULL, 40501);
INSERT INTO public.iam_role_menu VALUES (1000000040504, 1, NULL, 40504);
INSERT INTO public.iam_role_menu VALUES (1000000040505, 1, NULL, 40505);
INSERT INTO public.iam_role_menu VALUES (1000004040123, 1, NULL, 4040123);
INSERT INTO public.iam_role_menu VALUES (1000000040506, 1, NULL, 40506);
INSERT INTO public.iam_role_menu VALUES (1000004040132, 1, NULL, 4040132);
INSERT INTO public.iam_role_menu VALUES (1000000040508, 1, NULL, 40508);
INSERT INTO public.iam_role_menu VALUES (9200000000000000001, 1, NULL, 604);
INSERT INTO public.iam_role_menu VALUES (1784773822002001, 2, NULL, 91001);
INSERT INTO public.iam_role_menu VALUES (1784773822003002, 2, NULL, 91002);
INSERT INTO public.iam_role_menu VALUES (1784773822003003, 2, NULL, 91003);
INSERT INTO public.iam_role_menu VALUES (1784778494911002, 2, NULL, 91103);
INSERT INTO public.iam_role_menu VALUES (1784778494911003, 2, NULL, 91100);
INSERT INTO public.iam_role_menu VALUES (1784778494911004, 2, NULL, 91101);
INSERT INTO public.iam_role_menu VALUES (401060000001, 1, NULL, 40106);
INSERT INTO public.iam_role_menu VALUES (401060000002, 1, NULL, 40107);
INSERT INTO public.iam_role_menu VALUES (920000000001, 2, NULL, 91412);
INSERT INTO public.iam_role_menu VALUES (2080584149880750080, 2, NULL, 91200);
INSERT INTO public.iam_role_menu VALUES (2080584149889138688, 2, NULL, 91201);
INSERT INTO public.iam_role_menu VALUES (2080584149893332992, 2, NULL, 91202);
INSERT INTO public.iam_role_menu VALUES (2080584149897527296, 2, NULL, 91300);
INSERT INTO public.iam_role_menu VALUES (2080584149897527297, 2, NULL, 91301);
INSERT INTO public.iam_role_menu VALUES (2080584149897527298, 2, NULL, 91302);
INSERT INTO public.iam_role_menu VALUES (2080584149897527299, 2, NULL, 91303);
INSERT INTO public.iam_role_menu VALUES (2080584149897527300, 2, NULL, 91304);
INSERT INTO public.iam_role_menu VALUES (2080584149897527301, 2, NULL, 91400);
INSERT INTO public.iam_role_menu VALUES (2080584149897527302, 2, NULL, 91401);
INSERT INTO public.iam_role_menu VALUES (2080584149901721600, 2, NULL, 91402);
INSERT INTO public.iam_role_menu VALUES (2080584149901721602, 2, NULL, 91411);
INSERT INTO public.iam_role_menu VALUES (2080584149901721603, 2, NULL, 91403);
INSERT INTO public.iam_role_menu VALUES (2080584149901721604, 2, NULL, 91404);
INSERT INTO public.iam_role_menu VALUES (2080584149905915904, 2, NULL, 91405);
INSERT INTO public.iam_role_menu VALUES (2080584149905915906, 2, NULL, 91407);
INSERT INTO public.iam_role_menu VALUES (2080584149910110208, 2, NULL, 91409);
INSERT INTO public.iam_role_menu VALUES (2080600725350727680, 2, NULL, 91413);
INSERT INTO public.iam_role_menu VALUES (2080641499102658560, 2, NULL, 91501);
INSERT INTO public.iam_role_menu VALUES (2080662589891878912, 2, NULL, 91305);
INSERT INTO public.iam_role_menu VALUES (2080835059550670848, 2, NULL, 91421);
INSERT INTO public.iam_role_menu VALUES (2080921659299221504, 2, NULL, 91600);
INSERT INTO public.iam_role_menu VALUES (2080921659303415808, 2, NULL, 91601);
INSERT INTO public.iam_role_menu VALUES (2080921659303415809, 2, NULL, 91602);
INSERT INTO public.iam_role_menu VALUES (2080921659311804416, 2, NULL, 91603);
INSERT INTO public.iam_role_menu VALUES (2080921659311804417, 2, NULL, 91604);
INSERT INTO public.iam_role_menu VALUES (2081221795145486336, 2, NULL, 91700);
INSERT INTO public.iam_role_menu VALUES (2081221795359395840, 2, NULL, 91701);
INSERT INTO public.iam_role_menu VALUES (2081221795363590144, 2, NULL, 91702);
INSERT INTO public.iam_role_menu VALUES (2081221795363590145, 2, NULL, 91703);
INSERT INTO public.iam_role_menu VALUES (2083360959840325632, 2, NULL, 91420);
INSERT INTO public.iam_role_menu VALUES (2083360959848714240, 2, NULL, 91110);
INSERT INTO public.iam_role_menu VALUES (2083360959857102848, 2, NULL, 91111);
INSERT INTO public.iam_role_menu VALUES (2083360959857102849, 2, NULL, 91112);
-- 商户端转账菜单树(91120~91126)补绑内置商户管理员角色(2026-08-23, 此前整棵未绑定导致内置角色不可见)
INSERT INTO public.iam_role_menu VALUES (920000091120, 2, NULL, 91120);
INSERT INTO public.iam_role_menu VALUES (920000091126, 2, NULL, 91126);
INSERT INTO public.iam_role_menu VALUES (920000091121, 2, NULL, 91121);
INSERT INTO public.iam_role_menu VALUES (920000091122, 2, NULL, 91122);
INSERT INTO public.iam_role_menu VALUES (920000091123, 2, NULL, 91123);
INSERT INTO public.iam_role_menu VALUES (920000091124, 2, NULL, 91124);
INSERT INTO public.iam_role_menu VALUES (920000091125, 2, NULL, 91125);
INSERT INTO public.iam_role_menu VALUES (1000000040603, 1, NULL, 40603);
INSERT INTO public.iam_role_menu VALUES (1000000040604, 1, NULL, 40604);
INSERT INTO public.iam_role_menu VALUES (1000000091127, 2, NULL, 91127);
INSERT INTO public.iam_role_menu VALUES (1000000000608, 1, NULL, 608);
INSERT INTO public.iam_role_menu VALUES (1000000000208, 1, NULL, 208);


--
-- Data for Name: iam_social_login_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: iam_social_login_config (整表清除)


--
-- Data for Name: iam_user_dashboard_preference; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: iam_user_dashboard_preference (整表清除)


--
-- Data for Name: iam_user_expand_info; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: iam_user_expand_info (整表清除)
INSERT INTO public.iam_user_expand_info VALUES (1, 'female', NULL, NULL, NULL, NULL, NULL, NULL, 0, 1, 12, false, '2026-03-28 14:26:08+00', '2026-07-16 05:09:46.145161+00', NULL, NULL, NULL);


--
-- Data for Name: iam_user_info; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: iam_user_info (整表清除)
INSERT INTO public.iam_user_info VALUES (1, '超级管理员', 'admin', 'bootx', '$2a$10$pMvjAHI8RSDrTXSLrUBueeESg5Y9XNXWA5A96z8583PKkwBF53hyq', NULL, NULL, true, 'normal', 0, 1, 16, false, '2026-03-28 14:26:08+00', '2026-07-16 05:09:46.154179+00');


--
-- Data for Name: iam_user_password_history; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: iam_user_password_history (整表清除)


--
-- Data for Name: iam_user_password_security; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: iam_user_password_security (整表清除)


--
-- Data for Name: iam_user_role; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: iam_user_role (整表清除)


--
-- Data for Name: iam_user_social; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: iam_user_social (整表清除)


--
-- Data for Name: iam_user_two_factor; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: iam_user_two_factor (整表清除)


--
-- Data for Name: lakala_isv_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: lakala_isv_key_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: leshua_isv_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: leshua_isv_key_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: mch_app_info; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: mch_app_info (整表清除)


--
-- Data for Name: mch_app_notify_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: mch_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: mch_channel_merchant (整表清除)


--
-- Data for Name: mch_credential; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: mch_credential (整表清除)


--
-- Data for Name: mch_info; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: mch_info (整表清除)


--
-- Data for Name: mch_notice_record; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: mch_notice_record (整表清除)


--
-- Data for Name: mch_notice_task; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: mch_notice_task (整表清除)


--
-- Data for Name: mch_risk_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: mch_risk_config (整表清除)


--
-- Data for Name: mch_store_info; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: mch_store_info (整表清除)


--
-- Data for Name: mch_user; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: mch_user (整表清除)


--
-- Data for Name: mch_wx_domain_verify; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: notify_message; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: notify_notice; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: notify_notice (整表清除)


--
-- Data for Name: notify_notice_read; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: notify_notice_read (整表清除)


--
-- Data for Name: pay_alloc_detail; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_alloc_detail (整表清除)


--
-- Data for Name: pay_alloc_order; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_alloc_order (整表清除)


--
-- Data for Name: pay_blacklist; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_blacklist (整表清除)


--
-- Data for Name: pay_callback_record; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_callback_record (整表清除)


--
-- Data for Name: pay_channel_terminal; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: pay_close_record; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_close_record (整表清除)


--
-- Data for Name: pay_easy_pay_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_easy_pay_config (整表清除)


--
-- Data for Name: pay_easy_pay_credential; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_easy_pay_credential (整表清除)


--
-- Data for Name: pay_easy_pay_order; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: pay_easy_pay_refund_order; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: pay_gateway_cashier_item; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_gateway_cashier_item (整表清除)


--
-- Data for Name: pay_gateway_order; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_gateway_order (整表清除)


--
-- Data for Name: pay_gateway_pay_client_env; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_gateway_pay_client_env (整表清除)


--
-- Data for Name: pay_gateway_pay_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_gateway_pay_config (整表清除)


--
-- Data for Name: pay_md_capability; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.pay_md_capability VALUES (5001, 'aggregate_pay_qrcode', 0, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5003, 'wechat_cashier', 2, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5004, 'wechat_jsapi', 3, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5005, 'wechat_app', 4, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5006, 'wechat_h5', 5, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5007, 'wechat_qr', 6, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5008, 'wechat_mini', 7, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5009, 'wechat_barcode', 8, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5010, 'alipay_barcode', 9, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5012, 'alipay_app', 11, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5013, 'alipay_h5', 12, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5014, 'alipay_pc', 13, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5015, 'alipay_jsapi', 14, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5020, 'visa_card_gateway', 19, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5021, 'visa_card_present', 20, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5022, 'mastercard_card_gateway', 21, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5023, 'mastercard_card_present', 22, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (7001, 'douyin_qr', 1, true, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_capability VALUES (7002, 'douyin_jsapi', 2, true, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_capability VALUES (7003, 'douyin_h5', 3, true, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_capability VALUES (7004, 'douyin_app', 4, true, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_capability VALUES (5011, 'alipay_qr', 10, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5016, 'union_qr', 15, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5019, 'union_jsapi', 18, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5018, 'union_h5', 17, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO public.pay_md_capability VALUES (5017, 'union_barcode', 16, true, NULL, false, 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');


--
-- Data for Name: pay_md_channel; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.pay_md_channel VALUES (1, 'alipay', 1, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, false);
INSERT INTO public.pay_md_channel VALUES (3, 'wechat', 2, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, false);
INSERT INTO public.pay_md_channel VALUES (13, 'hkrt_pay', 9, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, false);
INSERT INTO public.pay_md_channel VALUES (14, 'fuyou_pay', 13, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, false);
INSERT INTO public.pay_md_channel VALUES (15, 'sheng_pay', 14, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, false);
INSERT INTO public.pay_md_channel VALUES (16, 'ysep_pay', 15, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, false);
INSERT INTO public.pay_md_channel VALUES (17, 'quick_pay', 16, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, false);
INSERT INTO public.pay_md_channel VALUES (18, 'sand_pay', 12, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, false);
INSERT INTO public.pay_md_channel VALUES (19, 'yee_pay', 10, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, false);
INSERT INTO public.pay_md_channel VALUES (20, 'jee_pay', 17, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, false);
INSERT INTO public.pay_md_channel VALUES (9, 'douyin', 3, 'douyinPay', 1, '2026-06-15 00:00:00', 1, '2026-06-15 00:00:00', 0, false);
INSERT INTO public.pay_md_channel VALUES (11, 'huifu', 11, 'huifu', 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, false);
INSERT INTO public.pay_md_channel VALUES (92001, 'stripe', 18, 'stripe', 1, '2026-08-02 13:04:19.050804', 1, '2026-08-02 13:04:19.050804', 0, false);
INSERT INTO public.pay_md_channel VALUES (91001, 'union_pay', 4, NULL, 1, '2026-08-02 08:38:10.079913', 1, '2026-08-02 08:38:10.079913', 0, false);
INSERT INTO public.pay_md_channel VALUES (5, 'ums_pay', 5, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, false);
INSERT INTO public.pay_md_channel VALUES (8, 'lakala_pay', 6, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, false);
INSERT INTO public.pay_md_channel VALUES (6, 'leshua_pay', 7, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, false);
INSERT INTO public.pay_md_channel VALUES (7, 'vbill_pay', 8, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, false);


--
-- Data for Name: pay_md_method; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.pay_md_method VALUES (502003001, 'aggregate_pay_qrcode', 1, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003003, 'wechat_cashier', 3, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003004, 'wechat_qr', 4, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003005, 'wechat_jsapi', 5, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003006, 'wechat_mini', 6, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003007, 'wechat_h5', 7, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003008, 'wechat_app', 8, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003009, 'wechat_barcode', 9, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003010, 'alipay_qr', 10, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003012, 'alipay_jsapi', 12, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003014, 'alipay_pc', 14, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003015, 'alipay_h5', 15, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003016, 'alipay_app', 16, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003017, 'alipay_barcode', 17, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003018, 'union_qr', 18, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003019, 'union_jsapi', 19, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003020, 'union_h5', 20, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003022, 'visa_card_gateway', 22, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003023, 'visa_card_present', 23, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003024, 'mastercard_card_gateway', 24, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003025, 'mastercard_card_present', 25, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003026, 'other', 26, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO public.pay_md_method VALUES (502003027, 'douyin_qr', 1, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_method VALUES (502003028, 'douyin_jsapi', 2, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_method VALUES (502003029, 'douyin_h5', 3, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_method VALUES (502003030, 'douyin_app', 4, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_method VALUES (502003021, 'union_barcode', 21, NULL, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');


--
-- Data for Name: pay_md_product; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.pay_md_product VALUES (1001, 'alipay_isv', '支付宝(服务商)', 'alipay', 11, NULL, NULL, NULL, NULL, 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10009, 'wechat_pay', '微信支付(直连)', 'wechat', 20, NULL, NULL, NULL, NULL, 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (1003, 'wechat_isv', '微信支付(服务商)', 'wechat', 21, NULL, NULL, NULL, NULL, 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10008, 'alipay', '支付宝(直连)', 'alipay', 10, NULL, NULL, 1, '2026-06-17 09:53:02.203443', 4, false, true, true);
INSERT INTO public.pay_md_product VALUES (96001, 'union_pay', '云闪付', 'union_pay', 40, 1, '2026-08-02 09:33:13.989427', 1, '2026-08-02 09:33:13.989427', 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (10001, 'ums_qrcode', '银联商务(C扫B)', 'ums_pay', 50, NULL, NULL, NULL, NULL, 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (10002, 'ums_jsapi', '银联商务(公众号)', 'ums_pay', 51, NULL, NULL, NULL, NULL, 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (10003, 'ums_app', '银联商务(APP)', 'ums_pay', 52, NULL, NULL, NULL, NULL, 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (10004, 'ums_mini', '银联商务(小程序)', 'ums_pay', 53, NULL, NULL, NULL, NULL, 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (10005, 'ums_h5', '银联商务(H5)', 'ums_pay', 54, NULL, NULL, NULL, NULL, 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (10006, 'ums_barcode', '银联商务(B扫C)', 'ums_pay', 55, NULL, NULL, NULL, NULL, 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (10007, 'lakala_pay', '拉卡拉支付', 'lakala_pay', 60, NULL, NULL, NULL, NULL, 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (92001, 'stripe_pay', 'Stripe 支付', 'stripe', 190, 1, '2026-08-02 13:04:19.05231', 1, '2026-08-02 13:04:19.05231', 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (10012, 'ada_pay', 'Adapay', 'huifu', 120, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (10010, 'douyin_pay', '抖音支付(直连)', 'douyin', 30, NULL, NULL, NULL, NULL, 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10022, 'leshua_pay', '乐刷支付', 'leshua_pay', 70, 1, '2026-07-06 00:00:00', 1, '2026-07-06 00:00:00', 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (10023, 'vbill_pay', '随行付', 'vbill_pay', 80, 1, '2026-07-06 00:00:00', 1, '2026-07-06 00:00:00', 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (10024, 'hm_pay', '河马付', 'sand_pay', 90, 1, '2026-07-07 00:00:00', 1, '2026-07-07 14:08:28.223227', 1, false, true, true);
INSERT INTO public.pay_md_product VALUES (10013, 'dougong_pay', '斗拱支付', 'huifu', 91, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-26 12:07:40.114398', 4, false, false, true);
INSERT INTO public.pay_md_product VALUES (10014, 'hkrt_pay', '海科融通', 'hkrt_pay', 100, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10020, 'yee_pay', '易宝支付', 'yee_pay', 110, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10015, 'fuyou_pay', '富友支付', 'fuyou_pay', 140, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10016, 'sheng_pay', '盛付通', 'sheng_pay', 150, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10017, 'ysep_pay', '银盛支付', 'ysep_pay', 160, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10018, 'quick_pay', '快钱支付', 'quick_pay', 170, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10021, 'jee_pay', 'Jeepay', 'jee_pay', 180, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, false, false, true);


--
-- Data for Name: pay_md_product_capability; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.pay_md_product_capability VALUES (6001, 'alipay_isv', 'alipay_barcode', 0, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (92001, 'stripe_pay', 'visa_card_gateway', 1, true, NULL, false, 1, '2026-08-02 13:04:19.055745', 0, 1, '2026-08-02 13:04:19.055745');
INSERT INTO public.pay_md_product_capability VALUES (6003, 'alipay_isv', 'alipay_jsapi', 2, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6004, 'alipay_isv', 'alipay_pc', 3, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6005, 'alipay_isv', 'alipay_h5', 4, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6006, 'alipay_isv', 'alipay_app', 5, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6010, 'wechat_isv', 'wechat_qr', 0, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6011, 'wechat_isv', 'wechat_app', 1, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6012, 'wechat_isv', 'wechat_h5', 2, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6013, 'wechat_isv', 'wechat_barcode', 3, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6014, 'wechat_isv', 'wechat_jsapi', 4, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6015, 'wechat_isv', 'wechat_mini', 5, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6020, 'ums_qrcode', 'aggregate_pay_qrcode', 0, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (92002, 'stripe_pay', 'mastercard_card_gateway', 2, true, NULL, false, 1, '2026-08-02 13:04:19.057452', 0, 1, '2026-08-02 13:04:19.057452');
INSERT INTO public.pay_md_product_capability VALUES (6023, 'ums_qrcode', 'wechat_qr', 3, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6030, 'ums_jsapi', 'wechat_jsapi', 0, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6031, 'ums_jsapi', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6040, 'ums_app', 'wechat_app', 0, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6041, 'ums_app', 'alipay_app', 1, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6050, 'ums_mini', 'wechat_mini', 0, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6060, 'ums_h5', 'wechat_h5', 0, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6061, 'ums_h5', 'alipay_h5', 1, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6080, 'lakala_pay', 'wechat_barcode', 0, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6081, 'lakala_pay', 'alipay_barcode', 1, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6083, 'lakala_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6084, 'lakala_pay', 'wechat_app', 4, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6085, 'lakala_pay', 'wechat_mini', 5, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6087, 'lakala_pay', 'alipay_jsapi', 7, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (20081, 'alipay', 'alipay_barcode', 1, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (20083, 'alipay', 'alipay_jsapi', 3, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (20085, 'alipay', 'alipay_pc', 5, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (20086, 'alipay', 'alipay_h5', 6, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (20087, 'alipay', 'alipay_app', 7, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (20091, 'wechat_pay', 'wechat_qr', 1, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (20092, 'wechat_pay', 'wechat_app', 2, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (20093, 'wechat_pay', 'wechat_h5', 3, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (20094, 'wechat_pay', 'wechat_barcode', 4, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (20095, 'wechat_pay', 'wechat_jsapi', 5, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (20096, 'wechat_pay', 'wechat_mini', 6, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (20101, 'douyin_pay', 'douyin_qr', 1, true, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (20102, 'douyin_pay', 'douyin_jsapi', 2, true, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (20103, 'douyin_pay', 'douyin_h5', 3, true, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (20104, 'douyin_pay', 'douyin_app', 4, true, NULL, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (21017, 'ada_pay', 'wechat_qr', 0, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21018, 'ada_pay', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21019, 'ada_pay', 'wechat_app', 2, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21020, 'ada_pay', 'wechat_h5', 3, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21021, 'ada_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21022, 'ada_pay', 'wechat_barcode', 5, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (6002, 'alipay_isv', 'alipay_qr', 1, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6022, 'ums_qrcode', 'alipay_qr', 2, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6086, 'lakala_pay', 'alipay_qr', 6, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (20082, 'alipay', 'alipay_qr', 2, true, NULL, false, NULL, NULL, 0, NULL, NULL);
INSERT INTO public.pay_md_product_capability VALUES (6021, 'ums_qrcode', 'union_qr', 1, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6088, 'lakala_pay', 'union_qr', 8, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6089, 'lakala_pay', 'union_jsapi', 9, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6062, 'ums_h5', 'union_h5', 2, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6070, 'ums_barcode', 'union_barcode', 0, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (6082, 'lakala_pay', 'union_barcode', 2, true, NULL, false, 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO public.pay_md_product_capability VALUES (21024, 'ada_pay', 'alipay_jsapi', 7, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21025, 'ada_pay', 'alipay_app', 8, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21026, 'ada_pay', 'alipay_h5', 9, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21027, 'ada_pay', 'alipay_pc', 10, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21028, 'ada_pay', 'alipay_barcode', 11, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21033, 'dougong_pay', 'wechat_qr', 0, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21034, 'dougong_pay', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21035, 'dougong_pay', 'wechat_app', 2, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21036, 'dougong_pay', 'wechat_h5', 3, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21037, 'dougong_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21038, 'dougong_pay', 'wechat_barcode', 5, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21040, 'dougong_pay', 'alipay_jsapi', 7, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21041, 'dougong_pay', 'alipay_app', 8, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21042, 'dougong_pay', 'alipay_h5', 9, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21043, 'dougong_pay', 'alipay_pc', 10, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21044, 'dougong_pay', 'alipay_barcode', 11, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21049, 'hkrt_pay', 'wechat_qr', 0, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21050, 'hkrt_pay', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21051, 'hkrt_pay', 'wechat_app', 2, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21052, 'hkrt_pay', 'wechat_h5', 3, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21053, 'hkrt_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21054, 'hkrt_pay', 'wechat_barcode', 5, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21056, 'hkrt_pay', 'alipay_jsapi', 7, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21057, 'hkrt_pay', 'alipay_app', 8, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21058, 'hkrt_pay', 'alipay_h5', 9, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21059, 'hkrt_pay', 'alipay_pc', 10, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21060, 'hkrt_pay', 'alipay_barcode', 11, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21065, 'fuyou_pay', 'wechat_qr', 0, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21066, 'fuyou_pay', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21067, 'fuyou_pay', 'wechat_app', 2, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21068, 'fuyou_pay', 'wechat_h5', 3, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21069, 'fuyou_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21070, 'fuyou_pay', 'wechat_barcode', 5, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21072, 'fuyou_pay', 'alipay_jsapi', 7, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21073, 'fuyou_pay', 'alipay_app', 8, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21074, 'fuyou_pay', 'alipay_h5', 9, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21075, 'fuyou_pay', 'alipay_pc', 10, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21076, 'fuyou_pay', 'alipay_barcode', 11, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21081, 'sheng_pay', 'wechat_qr', 0, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21082, 'sheng_pay', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21083, 'sheng_pay', 'wechat_app', 2, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21084, 'sheng_pay', 'wechat_h5', 3, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21085, 'sheng_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21086, 'sheng_pay', 'wechat_barcode', 5, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21088, 'sheng_pay', 'alipay_jsapi', 7, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21089, 'sheng_pay', 'alipay_app', 8, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21090, 'sheng_pay', 'alipay_h5', 9, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21091, 'sheng_pay', 'alipay_pc', 10, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21029, 'ada_pay', 'union_qr', 12, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21045, 'dougong_pay', 'union_qr', 12, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21061, 'hkrt_pay', 'union_qr', 12, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21077, 'fuyou_pay', 'union_qr', 12, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21030, 'ada_pay', 'union_jsapi', 13, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21046, 'dougong_pay', 'union_jsapi', 13, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21062, 'hkrt_pay', 'union_jsapi', 13, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21078, 'fuyou_pay', 'union_jsapi', 13, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21031, 'ada_pay', 'union_h5', 14, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21047, 'dougong_pay', 'union_h5', 14, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21063, 'hkrt_pay', 'union_h5', 14, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21079, 'fuyou_pay', 'union_h5', 14, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21032, 'ada_pay', 'union_barcode', 15, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21048, 'dougong_pay', 'union_barcode', 15, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21064, 'hkrt_pay', 'union_barcode', 15, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21080, 'fuyou_pay', 'union_barcode', 15, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21092, 'sheng_pay', 'alipay_barcode', 11, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21097, 'ysep_pay', 'wechat_qr', 0, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21098, 'ysep_pay', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21099, 'ysep_pay', 'wechat_app', 2, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21100, 'ysep_pay', 'wechat_h5', 3, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21101, 'ysep_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21102, 'ysep_pay', 'wechat_barcode', 5, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21104, 'ysep_pay', 'alipay_jsapi', 7, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21105, 'ysep_pay', 'alipay_app', 8, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21106, 'ysep_pay', 'alipay_h5', 9, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21107, 'ysep_pay', 'alipay_pc', 10, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21108, 'ysep_pay', 'alipay_barcode', 11, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21113, 'quick_pay', 'wechat_qr', 0, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21114, 'quick_pay', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21115, 'quick_pay', 'wechat_app', 2, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21116, 'quick_pay', 'wechat_h5', 3, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21117, 'quick_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21118, 'quick_pay', 'wechat_barcode', 5, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21120, 'quick_pay', 'alipay_jsapi', 7, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21121, 'quick_pay', 'alipay_app', 8, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21122, 'quick_pay', 'alipay_h5', 9, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21123, 'quick_pay', 'alipay_pc', 10, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21124, 'quick_pay', 'alipay_barcode', 11, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21145, 'yee_pay', 'wechat_qr', 0, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21146, 'yee_pay', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21147, 'yee_pay', 'wechat_app', 2, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21148, 'yee_pay', 'wechat_h5', 3, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21149, 'yee_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21150, 'yee_pay', 'wechat_barcode', 5, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21152, 'yee_pay', 'alipay_jsapi', 7, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21153, 'yee_pay', 'alipay_app', 8, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21154, 'yee_pay', 'alipay_h5', 9, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21155, 'yee_pay', 'alipay_pc', 10, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21156, 'yee_pay', 'alipay_barcode', 11, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21093, 'sheng_pay', 'union_qr', 12, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21109, 'ysep_pay', 'union_qr', 12, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21125, 'quick_pay', 'union_qr', 12, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21157, 'yee_pay', 'union_qr', 12, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21094, 'sheng_pay', 'union_jsapi', 13, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21110, 'ysep_pay', 'union_jsapi', 13, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21126, 'quick_pay', 'union_jsapi', 13, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21158, 'yee_pay', 'union_jsapi', 13, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21095, 'sheng_pay', 'union_h5', 14, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21111, 'ysep_pay', 'union_h5', 14, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21127, 'quick_pay', 'union_h5', 14, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21159, 'yee_pay', 'union_h5', 14, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21096, 'sheng_pay', 'union_barcode', 15, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21112, 'ysep_pay', 'union_barcode', 15, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21128, 'quick_pay', 'union_barcode', 15, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21160, 'yee_pay', 'union_barcode', 15, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21161, 'jee_pay', 'wechat_qr', 0, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21162, 'jee_pay', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21163, 'jee_pay', 'wechat_app', 2, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21164, 'jee_pay', 'wechat_h5', 3, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21165, 'jee_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21167, 'jee_pay', 'alipay_jsapi', 6, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21168, 'jee_pay', 'alipay_app', 7, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21169, 'jee_pay', 'alipay_h5', 8, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21170, 'jee_pay', 'alipay_pc', 9, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21023, 'ada_pay', 'alipay_qr', 6, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21039, 'dougong_pay', 'alipay_qr', 6, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21055, 'hkrt_pay', 'alipay_qr', 6, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21071, 'fuyou_pay', 'alipay_qr', 6, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21087, 'sheng_pay', 'alipay_qr', 6, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21103, 'ysep_pay', 'alipay_qr', 6, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21119, 'quick_pay', 'alipay_qr', 6, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21151, 'yee_pay', 'alipay_qr', 6, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (21166, 'jee_pay', 'alipay_qr', 5, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (6100, 'leshua_pay', 'wechat_barcode', 0, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6101, 'leshua_pay', 'alipay_barcode', 1, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6103, 'leshua_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6104, 'leshua_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6105, 'leshua_pay', 'alipay_qr', 5, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6106, 'leshua_pay', 'alipay_jsapi', 6, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6200, 'hm_pay', 'aggregate_pay_qrcode', 0, true, NULL, false, 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6202, 'hm_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6203, 'hm_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6204, 'hm_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6205, 'hm_pay', 'alipay_qr', 5, true, NULL, false, 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6206, 'hm_pay', 'alipay_jsapi', 6, true, NULL, false, 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6300, 'vbill_pay', 'wechat_jsapi', 0, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6301, 'vbill_pay', 'wechat_mini', 1, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6302, 'vbill_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6303, 'vbill_pay', 'wechat_barcode', 3, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6304, 'vbill_pay', 'wechat_cashier', 4, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6305, 'vbill_pay', 'alipay_jsapi', 5, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6307, 'vbill_pay', 'alipay_qr', 7, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6308, 'vbill_pay', 'alipay_barcode', 8, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (97001, 'union_pay', 'union_qr', 1, true, NULL, false, 1, '2026-08-02 09:33:13.993374', 0, 1, '2026-08-02 09:33:13.993374');
INSERT INTO public.pay_md_product_capability VALUES (97002, 'union_pay', 'union_h5', 2, true, NULL, false, 1, '2026-08-02 09:33:13.993374', 0, 1, '2026-08-02 09:33:13.993374');
INSERT INTO public.pay_md_product_capability VALUES (97003, 'union_pay', 'union_barcode', 3, true, NULL, false, 1, '2026-08-02 09:33:13.993374', 0, 1, '2026-08-02 09:33:13.993374');
INSERT INTO public.pay_md_product_capability VALUES (21171, 'jee_pay', 'union_qr', 10, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (6108, 'leshua_pay', 'union_qr', 8, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6310, 'vbill_pay', 'union_qr', 10, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (21172, 'jee_pay', 'union_jsapi', 11, true, NULL, false, 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO public.pay_md_product_capability VALUES (6109, 'leshua_pay', 'union_jsapi', 9, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6309, 'vbill_pay', 'union_jsapi', 9, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6102, 'leshua_pay', 'union_barcode', 2, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO public.pay_md_product_capability VALUES (6311, 'vbill_pay', 'union_barcode', 11, true, NULL, false, 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');


--
-- Data for Name: pay_md_product_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_md_product_config (整表清除)


--
-- Data for Name: pay_md_provider; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.pay_md_provider VALUES (502001000, 'aggregate_pay', NULL, 0, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', true, NULL);
INSERT INTO public.pay_md_provider VALUES (502001001, 'wechat', NULL, 1, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', true, NULL);
INSERT INTO public.pay_md_provider VALUES (502001002, 'alipay', NULL, 2, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', true, NULL);
INSERT INTO public.pay_md_provider VALUES (502001003, 'union_pay', NULL, 3, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', true, NULL);
INSERT INTO public.pay_md_provider VALUES (502001004, 'visa', NULL, 4, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', true, NULL);
INSERT INTO public.pay_md_provider VALUES (502001005, 'mastercard', NULL, 5, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', true, NULL);
INSERT INTO public.pay_md_provider VALUES (502001006, 'douyin', 'douyinPay', 60, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00', true, NULL);


--
-- Data for Name: pay_md_provider_method; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.pay_md_provider_method VALUES (502001901, 'aggregate_pay', 'aggregate_pay_qrcode', 1, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002001, 'wechat', 'wechat_jsapi', 1, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002002, 'wechat', 'wechat_app', 2, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002003, 'wechat', 'wechat_h5', 3, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002004, 'wechat', 'wechat_qr', 4, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002005, 'wechat', 'wechat_mini', 5, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002006, 'wechat', 'wechat_barcode', 6, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002007, 'wechat', 'wechat_cashier', 7, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002008, 'alipay', 'alipay_barcode', 1, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002010, 'alipay', 'alipay_app', 3, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002011, 'alipay', 'alipay_h5', 4, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002012, 'alipay', 'alipay_pc', 5, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002013, 'alipay', 'alipay_jsapi', 6, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002014, 'union_pay', 'union_qr', 1, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002016, 'union_pay', 'union_h5', 3, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002017, 'union_pay', 'union_jsapi', 4, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002018, 'visa', 'visa_card_gateway', 1, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002019, 'visa', 'visa_card_present', 2, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002020, 'mastercard', 'mastercard_card_gateway', 1, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002021, 'mastercard', 'mastercard_card_present', 2, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002022, 'douyin', 'douyin_qr', 1, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002023, 'douyin', 'douyin_jsapi', 2, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002024, 'douyin', 'douyin_h5', 3, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002025, 'douyin', 'douyin_app', 4, false, NULL, NULL, 0, NULL, '2026-06-15 00:00:00', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002009, 'alipay', 'alipay_qr', 2, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO public.pay_md_provider_method VALUES (502002015, 'union_pay', 'union_barcode', 2, false, NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);


--
-- Data for Name: pay_normal_order; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_normal_order (整表清除)


--
-- Data for Name: pay_platform_mobile_app; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_platform_mobile_app (整表清除)


--
-- Data for Name: pay_refund_order; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_refund_order (整表清除)


--
-- Data for Name: pay_risk_hit; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_risk_hit (整表清除)


--
-- Data for Name: pay_route_basic_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_route_basic_config (整表清除)


--
-- Data for Name: pay_route_scene_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: pay_route_strategy; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_route_strategy (整表清除)


--
-- Data for Name: pay_sync_record; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_sync_record (整表清除)


--
-- Data for Name: pay_terminal_channel_bind; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: pay_terminal_device; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_terminal_device (整表清除)


--
-- Data for Name: pay_trade; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_trade (整表清除)


--
-- Data for Name: pay_transfer_order_alipay; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_transfer_order_alipay (整表清除)


--
-- Data for Name: pay_transfer_order_douyin; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_transfer_order_douyin (整表清除)


--
-- Data for Name: pay_transfer_order_wechat; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_transfer_order_wechat (整表清除)


--
-- Data for Name: pay_transfer_trade; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: pay_transfer_trade (整表清除)


--
-- Data for Name: starter_audit_login_log; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: starter_audit_login_log (整表清除)


--
-- Data for Name: starter_audit_operate_log; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: starter_audit_operate_log (整表清除)


--
-- Data for Name: starter_audit_unipay_log; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: starter_audit_unipay_log (整表清除)


--
-- Data for Name: starter_platform_file_record; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: starter_platform_file_record (整表清除)


--
-- Data for Name: stripe_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: stripe_channel_merchant (整表清除)


--
-- Data for Name: stripe_key_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: stripe_key_config (整表清除)


--
-- Data for Name: system_dict; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.system_dict VALUES (308196335536967680, 1, '2026-04-30 11:15:28.565006', 1, '2026-04-30 11:15:28.565006', 0, false, '支付宝认证方式', 'common', 'alipay_auth_type', '支付宝接口认证方式', true, true, NULL);
INSERT INTO public.system_dict VALUES (2034597186006867968, 0, '2026-03-19 19:45:44.788062', 1, '2026-07-14 07:20:39.360411', 7, true, '123', NULL, 'cs', NULL, true, false, NULL);


--
-- Data for Name: system_dict_item; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.system_dict_item VALUES (308196335536967681, 308196335536967680, 'alipay_auth_type', 'public_key', 0, true, '使用公钥进行签名验证', 1, '2026-04-30 11:15:28.567585', 1, '2026-07-14 02:47:08.934408', 0, false, 'dict.alipay_auth_type.public_key');
INSERT INTO public.system_dict_item VALUES (308196335536967682, 308196335536967680, 'alipay_auth_type', 'cert', 1, true, '使用证书进行签名验证', 1, '2026-04-30 11:15:28.570843', 1, '2026-07-14 02:47:08.934408', 0, false, 'dict.alipay_auth_type.cert');
INSERT INTO public.system_dict_item VALUES (2034632666501005312, 2034597186006867968, 'cs', 'cs', 0, true, NULL, 0, '2026-03-19 22:06:44.064918', 1, '2026-07-14 07:20:36.33256', 3, true, 'dict.cs.cs');


--
-- Data for Name: system_platform_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: system_platform_config (整表清除)


--
-- Data for Name: system_platform_encrypt_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: system_platform_encrypt_config (整表清除)


--
-- Data for Name: system_sensitive_word; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.system_sensitive_word VALUES (2077569027575820288, '测试敏感词', 'custom', 'contains', 'reject', 'enable', '1111', 1, '2026-07-16 01:40:30.198619+00', 1, '2026-07-16 03:58:13.005826+00', 1, false);


--
-- Data for Name: system_sensitive_word_hit; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: system_sensitive_word_hit (整表清除)


--
-- Data for Name: ums_direct_key_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: union_key_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: vbill_isv_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: vbill_isv_key_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: wechat_direct_alloc_receiver; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: wechat_direct_alloc_receiver (整表清除)


--
-- Data for Name: wechat_direct_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: wechat_direct_channel_merchant (整表清除)


--
-- Data for Name: wechat_direct_key_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: wechat_direct_key_config (整表清除)


--
-- Data for Name: wechat_isv_alloc_receiver; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: wechat_isv_channel_merchant; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: wechat_isv_channel_merchant (整表清除)


--
-- Data for Name: wechat_isv_key_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: wechat_isv_key_config (整表清除)


--
-- Data for Name: wechat_transfer_config; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: wechat_transfer_config (整表清除)


--
-- Data for Name: wx_channel_app_capability; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: wx_channel_app_capability (整表清除)


--
-- Data for Name: wx_mch_app; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: wx_mch_app (整表清除)


--
-- Data for Name: wx_platform_app; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: wx_platform_app (整表清除)


--
-- Data for Name: wx_platform_app_capability; Type: TABLE DATA; Schema: public; Owner: -
--

-- REDACTED: wx_platform_app_capability (整表清除)


--
-- Data for Name: yeepay_direct_key_config; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Name: adapay_direct_key_config_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.adapay_direct_key_config_id_seq', 1, false);


--
-- Name: alipay_direct_app_capability_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.alipay_direct_app_capability_id_seq', 1, false);


--
-- Name: base_city_adjacent_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.base_city_adjacent_id_seq', 1808, true);


--
-- Name: hmpay_isv_channel_merchant_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.hmpay_isv_channel_merchant_id_seq', 1, false);


--
-- Name: hmpay_isv_key_config_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.hmpay_isv_key_config_id_seq', 1, false);


--
-- Name: mch_app_notify_config_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.mch_app_notify_config_id_seq', 1, false);


--
-- Name: mch_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.mch_user_id_seq', 1, false);


--
-- Name: pay_close_record_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.pay_close_record_id_seq', 1, false);


--
-- Name: pay_sync_record_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.pay_sync_record_id_seq', 1, false);


--
-- PostgreSQL database dump complete
--
