/*
Navicat MySQL Data Transfer

Source Server         : docker
Source Server Version : 50724
Source Host           : 192.168.194.128:3306
Source Database       : rest

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-02-26 15:36:13
*/

SET FOREIGN_KEY_CHECKS=0;


-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `gmt_update` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `number` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES ('1', '2019-02-26 07:31:36', '2019-02-26 07:31:36', '0', '1');
