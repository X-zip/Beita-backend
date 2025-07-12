/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : beita_test

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2025-07-12 21:41:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for accesscode
-- ----------------------------
DROP TABLE IF EXISTS `accesscode`;
CREATE TABLE `accesscode` (
  `id` int(11) NOT NULL,
  `accessCode` varchar(255) DEFAULT NULL,
  `c_time` bigint(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for avatar
-- ----------------------------
DROP TABLE IF EXISTS `avatar`;
CREATE TABLE `avatar` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `add_time` varchar(255) DEFAULT NULL,
  `campus` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `imageUrl` varchar(255) DEFAULT NULL,
  `navUrl` varchar(255) DEFAULT NULL,
  `weight` int(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for bitrank
-- ----------------------------
DROP TABLE IF EXISTS `bitrank`;
CREATE TABLE `bitrank` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `nickName` varchar(255) DEFAULT NULL,
  `score` int(255) DEFAULT NULL,
  `openid` varchar(255) DEFAULT NULL,
  `c_time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5480 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for blacklist
-- ----------------------------
DROP TABLE IF EXISTS `blacklist`;
CREATE TABLE `blacklist` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `openid` varchar(200) DEFAULT NULL,
  `period` varchar(255) DEFAULT NULL,
  `start` varchar(255) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `campus` varchar(255) DEFAULT NULL,
  `uid` int(11) DEFAULT '0',
  `time` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2523 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for campus
-- ----------------------------
DROP TABLE IF EXISTS `campus`;
CREATE TABLE `campus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `region` varchar(255) DEFAULT NULL,
  `campus` varchar(255) DEFAULT NULL,
  `regionName` varchar(255) DEFAULT NULL,
  `campusName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for claim
-- ----------------------------
DROP TABLE IF EXISTS `claim`;
CREATE TABLE `claim` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) DEFAULT NULL,
  `applyTo` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `comment` varchar(2000) CHARACTER SET utf8mb4 DEFAULT NULL,
  `pk` int(30) DEFAULT NULL,
  `userName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `c_time` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT '',
  `level` varchar(255) DEFAULT '1',
  `pid` int(30) DEFAULT '0',
  `like_num` int(30) DEFAULT '0',
  `is_delete` int(30) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `pk` (`pk`),
  KEY `pid` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=4548257 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dirty_list
-- ----------------------------
DROP TABLE IF EXISTS `dirty_list`;
CREATE TABLE `dirty_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for like
-- ----------------------------
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) DEFAULT NULL,
  `pk` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=208989 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for login
-- ----------------------------
DROP TABLE IF EXISTS `login`;
CREATE TABLE `login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `campus` varchar(255) DEFAULT NULL,
  `expired` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) NOT NULL,
  `au1` int(10) unsigned zerofill DEFAULT '0000000001',
  `au2` int(10) unsigned zerofill DEFAULT '0000000001',
  `au3` int(10) unsigned zerofill DEFAULT '0000000001',
  `au4` int(10) unsigned zerofill DEFAULT '0000000001',
  `au5` int(10) unsigned zerofill DEFAULT '0000000001',
  `avatar` varchar(255) DEFAULT '',
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for nickname
-- ----------------------------
DROP TABLE IF EXISTS `nickname`;
CREATE TABLE `nickname` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) DEFAULT NULL,
  `add_time` varchar(255) DEFAULT NULL,
  `campus` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for qr
-- ----------------------------
DROP TABLE IF EXISTS `qr`;
CREATE TABLE `qr` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imageUrl` varchar(255) DEFAULT NULL,
  `campus` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=344 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for radiogroupcategory
-- ----------------------------
DROP TABLE IF EXISTS `radiogroupcategory`;
CREATE TABLE `radiogroupcategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for secret
-- ----------------------------
DROP TABLE IF EXISTS `secret`;
CREATE TABLE `secret` (
  `id` int(11) NOT NULL,
  `appid` varchar(255) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for suggestion
-- ----------------------------
DROP TABLE IF EXISTS `suggestion`;
CREATE TABLE `suggestion` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `task_id` int(30) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `c_time` datetime DEFAULT NULL,
  `openid` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14422 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `content` varchar(2000) CHARACTER SET utf8mb4 DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `title` varchar(2000) CHARACTER SET utf8mb4 DEFAULT NULL,
  `wechat` varchar(255) DEFAULT NULL,
  `openid` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `campusGroup` varchar(255) DEFAULT NULL,
  `commentNum` int(30) DEFAULT '0',
  `watchNum` int(30) DEFAULT '0',
  `likeNum` int(30) DEFAULT '0',
  `radioGroup` varchar(255) DEFAULT NULL,
  `img` varchar(500) CHARACTER SET armscii8 DEFAULT '',
  `region` varchar(255) DEFAULT NULL,
  `userName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `c_time` varchar(255) DEFAULT NULL,
  `comment_time` varchar(255) DEFAULT NULL,
  `choose` int(30) DEFAULT '0',
  `cover` varchar(500) DEFAULT '',
  `is_delete` int(30) DEFAULT '0',
  `is_complaint` int(30) DEFAULT '0',
  `ip` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=660190 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for template
-- ----------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `region` varchar(255) DEFAULT NULL,
  `campusGroup` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `template_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for user_verify
-- ----------------------------
DROP TABLE IF EXISTS `user_verify`;
CREATE TABLE `user_verify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) DEFAULT NULL,
  `pic` varchar(1000) DEFAULT NULL,
  `status` int(255) DEFAULT NULL,
  `c_time` varchar(255) DEFAULT NULL,
  `campus` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT '0',
  `avatar` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `sid` varchar(255) DEFAULT '',
  `password` varchar(255) DEFAULT '',
  `motto` varchar(255) DEFAULT '',
  `identity` int(10) DEFAULT '0',
  `update_time` varchar(255) DEFAULT '',
  `delete_time` varchar(255) DEFAULT '',
  `blind_count` int(11) DEFAULT '1',
  `profile_photo` varchar(255) DEFAULT NULL,
  `bio` varchar(10000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=104154 DEFAULT CHARSET=utf8mb4;
