/*
Navicat MySQL Data Transfer

Source Server         : 本地连接
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : testdb

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2018-04-28 16:11:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for archiveconfig
-- ----------------------------
DROP TABLE IF EXISTS `archiveconfig`;
CREATE TABLE `archiveconfig` (
  `fid` int(11) NOT NULL AUTO_INCREMENT,
  `productType` varchar(125) DEFAULT NULL,
  `productTable` varchar(125) DEFAULT NULL,
  `fieldName` varchar(125) DEFAULT NULL,
  `nodeName` varchar(125) DEFAULT NULL,
  `nodepath` varchar(255) DEFAULT NULL,
  `xmlfile` varchar(125) DEFAULT NULL,
  PRIMARY KEY (`fid`)
) ENGINE=MyISAM AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of archiveconfig
-- ----------------------------
INSERT INTO `archiveconfig` VALUES ('1', '分景DOM', 'tb_domframe_product', 'DataType', 'FileType', '/sensor_corrected_metadata/productComponents/file/FileType', 'zy301a.xml');
INSERT INTO `archiveconfig` VALUES ('4', 'sc产品', 'tb_sc_product', 'satellite', 'SatelliteID', '/ProductMetaData/SatelliteID', 'GF1.xml');
INSERT INTO `archiveconfig` VALUES ('5', 'sc产品', 'tb_sc_product', 'sensor', 'SensorID', '/ProductMetaData/SensorID', 'GF1.xml');
INSERT INTO `archiveconfig` VALUES ('11', '分幅DOM', 'tb_domframe_product', 'BandsNumber', 'NumBands', '/metadata/Esri/DataProperties/RasterProperties/General/NumBands', 'J46D.tif.xml');
INSERT INTO `archiveconfig` VALUES ('10', '分幅DOM', 'tb_domframe_product', 'FileName', 'itemName', '/metadata/Esri/DataProperties/itemProps/itemName', 'J46D.tif.xml');
INSERT INTO `archiveconfig` VALUES ('12', '分幅DOM', 'tb_domframe_product', 'DataType', 'Format', '/metadata/Esri/DataProperties/RasterProperties/General/Format', 'J46D.tif.xml');
INSERT INTO `archiveconfig` VALUES ('13', '分幅DOM', 'tb_domframe_product', 'CellSizexy', 'value', '/metadata/spatRepInfo/Georect/axisDimension/dimResol/value', 'J46D.tif.xml');
INSERT INTO `archiveconfig` VALUES ('14', '分幅DOM', 'tb_domframe_product', 'Columns', 'dimSize', '/metadata/spatRepInfo/Georect/axisDimension/dimSize', 'J46D.tif.xml');
INSERT INTO `archiveconfig` VALUES ('15', '分幅DOM', 'tb_domframe_product', 'Rows', 'dimSize', '/metadata/spatRepInfo/Georect/axisDimension/dimSize', 'J46D.tif.xml');
INSERT INTO `archiveconfig` VALUES ('16', '分幅DOM', 'tb_domframe_product', 'ExtentLeft', 'westBL', '/metadata/dataIdInfo/dataExt/geoEle/GeoBndBox/westBL', 'J46D.tif.xml');
INSERT INTO `archiveconfig` VALUES ('17', '分幅DOM', 'tb_domframe_product', 'ExtentRight', 'eastBL', '/metadata/dataIdInfo/dataExt/geoEle/GeoBndBox/eastBL', 'J46D.tif.xml');
INSERT INTO `archiveconfig` VALUES ('18', '分幅DOM', 'tb_domframe_product', 'ExtentTop', 'northBL', '/metadata/dataIdInfo/dataExt/geoEle/GeoBndBox/northBL', 'J46D.tif.xml');
INSERT INTO `archiveconfig` VALUES ('19', '分幅DOM', 'tb_domframe_product', 'ExtentBottom', 'southBL', '/metadata/dataIdInfo/dataExt/geoEle/GeoBndBox/southBL', 'J46D.tif.xml');
INSERT INTO `archiveconfig` VALUES ('20', '分幅DOM', 'tb_domframe_product', 'acquisitionTime', 'CreaDate', '/metadata/Esri/CreaDate', 'J46D.tif.xml');
INSERT INTO `archiveconfig` VALUES ('22', 'sc产品', 'tb_sc_product', 'satellite', 'SatelliteID', '/ProductMetaData/SatelliteID', null);
INSERT INTO `archiveconfig` VALUES ('23', 'sc产品', 'tb_sc_product', 'archiveTime', 'ReceiveTime', '/ProductMetaData/ReceiveTime', null);
INSERT INTO `archiveconfig` VALUES ('24', 'sc产品', 'tb_sc_product', 'sensor', 'SensorID', '/ProductMetaData/SensorID', null);
INSERT INTO `archiveconfig` VALUES ('25', 'test03', 'tb_test03_product', 'satellite', 'SatelliteID', '/ProductMetaData/SatelliteID', null);
INSERT INTO `archiveconfig` VALUES ('26', 'sc产品', 'tb_test03_product', 'satellite', 'SatelliteID', '/ProductMetaData/SatelliteID', null);
