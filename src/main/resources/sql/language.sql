/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50645
 Source Host           : localhost:3306
 Source Schema         : language

 Target Server Type    : MySQL
 Target Server Version : 50645
 File Encoding         : 65001

 Date: 08/11/2019 13:37:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chinese_word
-- ----------------------------
DROP TABLE IF EXISTS `chinese_word`;
CREATE TABLE `chinese_word`  (
  `CHINESE_WORD_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CHINESE_WORD_ORIGINAL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CHINESE_WORD_LINK_NUM` int(11) NULL DEFAULT NULL,
  `CHINESE_WORD_CREATE_TIME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CHINESE_WORD_UPDATE_TIME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`CHINESE_WORD_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for word
-- ----------------------------
DROP TABLE IF EXISTS `word`;
CREATE TABLE `word`  (
  `WORD_ID` int(11) NOT NULL AUTO_INCREMENT,
  `WORD_ORIGINAL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WORD_PRONUNCIATION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WORD_TYPE` int(1) NULL DEFAULT NULL COMMENT '1和语 2汉语 3外来语 4外来语(含中文)',
  `WORD_MISTAKE_NUM` int(11) NULL DEFAULT NULL,
  `WORD_RIGHT_NUM` int(11) NULL DEFAULT NULL,
  `WORD_CREATE_TIME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WORD_CHINESE_IDS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'chinese_word的ids:|45|87|58',
  `WORD_UPDATE_TIME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`WORD_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
