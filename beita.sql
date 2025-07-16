/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : beita_backend

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2025-07-16 20:21:40
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
-- Records of accesscode
-- ----------------------------
INSERT INTO `accesscode` VALUES ('1', '123abc', '1');

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
-- Records of avatar
-- ----------------------------
INSERT INTO `avatar` VALUES ('1', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fm-2.duitang.com%2Falbum%2F%3Fid%3D113132652&psig=AOvVaw1I8ygSZDuukyrXGTwadLsh&ust=1752753945119000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCOCY87OrwY4DFQAAAAAdAAAAABAE', '1', '1');

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
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES ('1', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fm-2.duitang.com%2Falbum%2F%3Fid%3D113132652&psig=AOvVaw1I8ygSZDuukyrXGTwadLsh&ust=1752753945119000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCOCY87OrwY4DFQAAAAAdAAAAABAE', '/page', '1');

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
-- Records of bitrank
-- ----------------------------
INSERT INTO `bitrank` VALUES ('1', '123', 'abc', '1', '123qweasd', '1');

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
-- Records of blacklist
-- ----------------------------
INSERT INTO `blacklist` VALUES ('1', '123qaz', '永久', '2025-01-01 00:00:00', '广告', '1', '0', '');

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
-- Records of campus
-- ----------------------------
INSERT INTO `campus` VALUES ('1', '1', '1', '测试region', '测试campus');

-- ----------------------------
-- Table structure for claim
-- ----------------------------
DROP TABLE IF EXISTS `claim`;
CREATE TABLE `claim` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of claim
-- ----------------------------
INSERT INTO `claim` VALUES ('0', '未声明');

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
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES ('1', '123qwe', '1qaz2wsx', '%3D113132652&psig=AOvVaw1I8ygSZDuukyrXGTwadLsh&ust=1752753945119000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCOCY87OrwY4DFQAAAAAdAAAAABAE', '你好', '1', '匿名', '2025/00/00 00:00:00', '', '1', '0', '0', '0');

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
-- Records of dirty_list
-- ----------------------------
INSERT INTO `dirty_list` VALUES ('1', '代写');

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
-- Records of like
-- ----------------------------
INSERT INTO `like` VALUES ('1', '123qwe', '1');

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
-- Records of login
-- ----------------------------
INSERT INTO `login` VALUES ('1', 'ceshi', 'ceshi000', '1', '2030-11-28', '测试校园账号');

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
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES ('0000000001', '1234qwer', '0000000001', '0000000001', '0000000001', '0000000001', '0000000001', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.qiubiaoqing.com%2Fimg_detail%2F764174823814335090.html&psig=AOvVaw1I8ygSZDuukyrXGTwadLsh&ust=1752753945119000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCOCY87OrwY4DFQAAAAAdAAAAABAK', 'ceshi');

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
-- Records of nickname
-- ----------------------------
INSERT INTO `nickname` VALUES ('1', '123123123', '', 'ceshi');

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
-- Records of qr
-- ----------------------------
INSERT INTO `qr` VALUES ('1', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Ftouxiangkong.com%2Flist%2Fweixintouxiang298_1.html&psig=AOvVaw1I8ygSZDuukyrXGTwadLsh&ust=1752753945119000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCOCY87OrwY4DFQAAAAAdAAAAABAW', '1', '1');

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
-- Records of radiogroupcategory
-- ----------------------------
INSERT INTO `radiogroupcategory` VALUES ('1', '闲置');

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
-- Records of secret
-- ----------------------------
INSERT INTO `secret` VALUES ('1', '123456', 'qwerasdf');

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
-- Records of suggestion
-- ----------------------------
INSERT INTO `suggestion` VALUES ('1', '1', '{\"Content\":\"广告\"}', '2022-06-15 17:43:10', '12345qwert');

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
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('1', '嗨呀\n', '', '嗨呀\n', '', '', 'https://p5.itc.cn/q_70/images03/20210120/1d2080d4fc5a422ea6238cee9bbed8c4.jpeg', '1', '0', '1', '0', 'radio41', '', '1', '匿名', '2022/05/14 10:52:34', '2022/05/14 10:52:34', '0', '', '0', '0', '');

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
-- Records of template
-- ----------------------------
INSERT INTO `template` VALUES ('1', '1', '1', 'ceshi', 'ceshi1234565');

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

-- ----------------------------
-- Records of user_verify
-- ----------------------------
INSERT INTO `user_verify` VALUES ('1', '123qwe', 'https://thirdwx.qlogo.cn/mmopen/vi_32/wEcLGFrHG9qbIM2UMxEWNwfjUfF3AwU3aUbL5Iz6smvBdwyDLGkWIAvWibohZQPDZ9riaOd9TzA0zV46cK64jYog/132', '0', '2025-07-16 00:00:00', '1', '', '1', '', '', '', '', '', '', '0', '', '', '1', '', '');
