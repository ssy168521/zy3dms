/*
Navicat MySQL Data Transfer

Source Server         : 本地连接
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : testdb

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2018-04-28 15:56:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_metamanager
-- ----------------------------
DROP TABLE IF EXISTS `tb_metamanager`;
CREATE TABLE `tb_metamanager` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `productType` varchar(32) DEFAULT NULL,
  `tablename` varchar(32) NOT NULL,
  `satellite` varchar(32) DEFAULT NULL,
  `sensor` varchar(32) DEFAULT NULL,
  `productLevel` varchar(32) DEFAULT NULL,
  `crs` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_metamanager
-- ----------------------------
INSERT INTO `tb_metamanager` VALUES ('1', 'sc产品', 'tb_sc_product', 'ZY3-1', 'BWD', 'SC', '');
INSERT INTO `tb_metamanager` VALUES ('2', 'sc产品', 'tb_sc_product', 'ZY3-1', 'FWD', 'SC', '');
INSERT INTO `tb_metamanager` VALUES ('4', 'sc产品', 'tb_sc_product', 'ZY3-1', 'NAD', 'SC', '');
INSERT INTO `tb_metamanager` VALUES ('5', 'sc产品', 'tb_sc_product', 'ZY3-1', 'MUX', 'SC', '');
INSERT INTO `tb_metamanager` VALUES ('6', 'sc产品', 'tb_sc_product', 'ZY3-2', 'MUX', 'SC', '');
INSERT INTO `tb_metamanager` VALUES ('7', 'sc产品', 'tb_sc_product', 'ZY3-2', 'NAD', 'SC', '');
INSERT INTO `tb_metamanager` VALUES ('8', 'sc产品', 'tb_sc_product', 'ZY3-2', 'BWD', 'SC', '');
INSERT INTO `tb_metamanager` VALUES ('9', 'sc产品', 'tb_sc_product', 'ZY3-2', 'FWD', 'SC', '');
INSERT INTO `tb_metamanager` VALUES ('10', 'sc产品', 'tb_sc_product', 'GF1', 'PMS1', 'LEVEL1A', '');
INSERT INTO `tb_metamanager` VALUES ('11', 'sc产品', 'tb_sc_product', 'GF1', 'PMS2', 'LEVEL1A', '');
INSERT INTO `tb_metamanager` VALUES ('12', 'sc产品', 'tb_sc_product', 'GF2', 'PMS1', 'LEVEL1A', '');
INSERT INTO `tb_metamanager` VALUES ('13', 'sc产品', 'tb_sc_product', 'GF2', 'PMS2', 'LEVEL1A', '');
INSERT INTO `tb_metamanager` VALUES ('14', '分幅DOM', 'tb_domframe_product', null, null, null, '');
INSERT INTO `tb_metamanager` VALUES ('15', '分景DOM', 'tb_domscene_product', null, null, null, '');
INSERT INTO `tb_metamanager` VALUES ('16', '镶嵌线', 'tb_seamline_1', null, null, null, '');
