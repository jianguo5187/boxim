/*
 Navicat Premium Data Transfer

 Source Server         : localhost(boxim)
 Source Server Type    : MySQL
 Source Server Version : 50726 (5.7.26)
 Source Host           : localhost:3306
 Source Schema         : boxim

 Target Server Type    : MySQL
 Target Server Version : 50726 (5.7.26)
 File Encoding         : 65001

 Date: 05/05/2024 00:20:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for im_friend
-- ----------------------------
DROP TABLE IF EXISTS `im_friend`;
CREATE TABLE `im_friend`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `friend_id` bigint(20) NOT NULL COMMENT '好友id',
  `friend_nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '好友昵称',
  `friend_head_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '好友头像',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `remark_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_friend_id`(`friend_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '好友' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of im_friend
-- ----------------------------
INSERT INTO `im_friend` VALUES (6, 1, 4, '测试用户', '', '2024-04-12 21:59:27', '测试21');
INSERT INTO `im_friend` VALUES (7, 4, 1, '充值客服账号1', '', '2024-04-12 21:59:27', NULL);
INSERT INTO `im_friend` VALUES (8, 2, 4, '测试用户', '', '2024-04-12 21:59:27', NULL);
INSERT INTO `im_friend` VALUES (9, 4, 2, '充值客服账号2', '', '2024-04-12 21:59:27', NULL);
INSERT INTO `im_friend` VALUES (10, 1, 5, '测试用户2', '', '2024-04-12 22:40:47', '');
INSERT INTO `im_friend` VALUES (11, 5, 1, '充值客服账号1', '', '2024-04-12 22:40:47', NULL);
INSERT INTO `im_friend` VALUES (12, 2, 5, '测试用户2', '', '2024-04-12 22:40:47', NULL);
INSERT INTO `im_friend` VALUES (13, 5, 2, '充值客服账号2', '', '2024-04-12 22:40:47', NULL);
INSERT INTO `im_friend` VALUES (14, 1, 6, 'temp_20240426235047', '', '2024-04-26 23:50:47', NULL);
INSERT INTO `im_friend` VALUES (15, 6, 1, '充值客服账号1', '', '2024-04-26 23:50:47', NULL);
INSERT INTO `im_friend` VALUES (16, 2, 6, 'temp_20240426235047', '', '2024-04-26 23:50:47', NULL);
INSERT INTO `im_friend` VALUES (17, 6, 2, '充值客服账号2', '', '2024-04-26 23:50:47', NULL);

-- ----------------------------
-- Table structure for im_group
-- ----------------------------
DROP TABLE IF EXISTS `im_group`;
CREATE TABLE `im_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '群名字',
  `owner_id` bigint(20) NOT NULL COMMENT '群主id',
  `head_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '群头像',
  `head_image_thumb` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '群头像缩略图',
  `notice` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '群公告',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '群备注',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否已删除',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '群' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of im_group
-- ----------------------------

-- ----------------------------
-- Table structure for im_group_member
-- ----------------------------
DROP TABLE IF EXISTS `im_group_member`;
CREATE TABLE `im_group_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `group_id` bigint(20) NOT NULL COMMENT '群id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `alias_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '组内显示名称',
  `head_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户头像',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  `quit` tinyint(1) NULL DEFAULT 0 COMMENT '是否已退出',
  `quit_time` datetime NULL DEFAULT NULL COMMENT '退出时间',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_group_id`(`group_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '群成员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of im_group_member
-- ----------------------------

-- ----------------------------
-- Table structure for im_group_message
-- ----------------------------
DROP TABLE IF EXISTS `im_group_message`;
CREATE TABLE `im_group_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `group_id` bigint(20) NOT NULL COMMENT '群id',
  `send_id` bigint(20) NOT NULL COMMENT '发送用户id',
  `send_nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '发送用户昵称',
  `recv_ids` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '接收用户id,逗号分隔，为空表示发给所有成员',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '发送内容',
  `at_user_ids` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被@的用户id列表，逗号分隔',
  `receipt` tinyint(4) NULL DEFAULT 0 COMMENT '是否回执消息',
  `receipt_ok` tinyint(4) NULL DEFAULT 0 COMMENT '回执消息是否完成',
  `type` tinyint(1) NOT NULL COMMENT '消息类型 0:文字 1:图片 2:文件 3:语音 4:视频 10:系统提示',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态 0:未发出 1:已送达  2:撤回 3:已读',
  `send_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '群消息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of im_group_message
-- ----------------------------

-- ----------------------------
-- Table structure for im_private_message
-- ----------------------------
DROP TABLE IF EXISTS `im_private_message`;
CREATE TABLE `im_private_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `send_id` bigint(20) NOT NULL COMMENT '发送用户id',
  `recv_id` bigint(20) NOT NULL COMMENT '接收用户id',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '发送内容',
  `type` tinyint(1) NOT NULL COMMENT '消息类型 0:文字 1:图片 2:文件 3:语音 10:系统提示',
  `status` tinyint(1) NOT NULL COMMENT '状态 0:未读 1:已读 2:撤回',
  `send_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_send_recv_id`(`send_id`, `recv_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '私聊消息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of im_private_message
-- ----------------------------
INSERT INTO `im_private_message` VALUES (5, 4, 1, '123', 0, 3, '2024-04-12 22:12:18');
INSERT INTO `im_private_message` VALUES (6, 4, 1, '23', 0, 3, '2024-04-12 22:23:34');
INSERT INTO `im_private_message` VALUES (7, 5, 1, '#可怜;', 0, 3, '2024-04-12 22:57:40');
INSERT INTO `im_private_message` VALUES (8, 5, 1, '{\"originUrl\":\"http://127.0.0.1:9000/box-im/image/20240412/1712934213059.png\",\"thumbUrl\":\"http://127.0.0.1:9000/box-im/image/20240412/1712934213059.png\"}', 1, 3, '2024-04-12 23:03:33');
INSERT INTO `im_private_message` VALUES (9, 1, 5, '{\"originUrl\":\"http://127.0.0.1:9000/box-im/image/20240412/1712934305406.jpeg\",\"thumbUrl\":\"http://127.0.0.1:9000/box-im/image/20240412/1712934305406.jpeg\"}', 1, 3, '2024-04-12 23:05:06');
INSERT INTO `im_private_message` VALUES (10, 1, 5, '{\"originUrl\":\"http://127.0.0.1:9000/box-im/image/20240412/1712934775445.jpeg\",\"thumbUrl\":\"http://127.0.0.1:9000/box-im/image/20240412/1712934775445.jpeg\"}', 1, 3, '2024-04-12 23:12:56');
INSERT INTO `im_private_message` VALUES (11, 1, 5, '{\"originUrl\":\"http://127.0.0.1:9000/box-im/image/20240412/1712934918545.png\",\"thumbUrl\":\"http://127.0.0.1:9000/box-im/image/20240412/1712934918545.png\"}', 1, 3, '2024-04-12 23:15:19');
INSERT INTO `im_private_message` VALUES (12, 1, 5, '#笑哭;', 0, 3, '2024-04-12 23:15:27');
INSERT INTO `im_private_message` VALUES (13, 5, 1, '{\"originUrl\":\"http://127.0.0.1:9000/box-im/image/20240412/1712935007823.png\",\"thumbUrl\":\"http://127.0.0.1:9000/box-im/image/20240412/1712935007823.png\"}', 1, 3, '2024-04-12 23:16:48');
INSERT INTO `im_private_message` VALUES (14, 5, 1, '#憨笑;', 0, 3, '2024-04-15 20:14:58');
INSERT INTO `im_private_message` VALUES (15, 1, 5, '123', 0, 3, '2024-04-19 23:29:08');
INSERT INTO `im_private_message` VALUES (16, 5, 1, '#憨笑;', 0, 3, '2024-04-21 09:30:17');
INSERT INTO `im_private_message` VALUES (17, 5, 1, '12312312', 0, 3, '2024-04-21 09:30:30');
INSERT INTO `im_private_message` VALUES (18, 5, 1, '12312312', 0, 3, '2024-04-21 09:30:35');
INSERT INTO `im_private_message` VALUES (19, 5, 1, '1', 0, 3, '2024-04-21 12:16:04');
INSERT INTO `im_private_message` VALUES (20, 1, 4, '#坏笑;', 0, 0, '2024-05-03 21:58:54');
INSERT INTO `im_private_message` VALUES (21, 5, 2, '123', 0, 3, '2024-05-04 23:49:57');
INSERT INTO `im_private_message` VALUES (22, 2, 5, '465465', 0, 1, '2024-05-04 23:51:25');

-- ----------------------------
-- Table structure for im_user
-- ----------------------------
DROP TABLE IF EXISTS `im_user`;
CREATE TABLE `im_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `head_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户头像',
  `head_image_thumb` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户头像缩略图',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码(明文)',
  `sex` tinyint(1) NULL DEFAULT 0 COMMENT '性别 0:男 1:女',
  `type` smallint(6) NULL DEFAULT 1 COMMENT '用户类型 1:客服用户 2:普通用户',
  `signature` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '个性签名',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `third_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方用户uuid',
  `remark_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `user_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户注册IP',
  `user_ip_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户注册IP归属地',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_name`(`user_name`) USING BTREE,
  INDEX `idx_nick_name`(`nick_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of im_user
-- ----------------------------
INSERT INTO `im_user` VALUES (1, 'kefu1', '充值客服账号1', '', '', '$2a$10$2A1h/81gTuUkP4y/ilV5XOxIG/ThmpZsl.KbpIIuU0H4mcL7uTG5a', 0, 1, '', NULL, '2024-04-09 20:18:54', NULL, NULL, NULL, '福建省:高砂3');
INSERT INTO `im_user` VALUES (2, 'kefu2', '充值客服账号2', '', '', '$2a$10$Zx.3MGTvRiwF/SVSPaNc1u91rpFpNaCl2xau5h.gOcH7H5jrH74Py', 0, 1, '', NULL, '2024-04-09 20:37:07', NULL, NULL, NULL, '福建省:高砂2');
INSERT INTO `im_user` VALUES (4, 'testUser', '测试用户', '', '', '$2a$10$uKmi.V/Xz9rMfRxZNXwvsebRzxgyrx9R37W.3NEZqHqdidsb2KmFi', 0, 2, '', NULL, '2024-04-12 21:59:27', 'testUser1', NULL, NULL, NULL);
INSERT INTO `im_user` VALUES (5, 'testUser2', '测试用户2', '', '', '$2a$10$c8X4CulalOX0DrZUePnasep4H6Ef97z8DsifrB/Eqp4koJ2HovV7G', 0, 2, '', NULL, '2024-04-12 22:40:47', 'testUser2', NULL, '120.39.192.173', '福建省:高砂');
INSERT INTO `im_user` VALUES (6, 'U20240426235047', 'temp_20240426235047', '', '', '$2a$10$hHsdYPomOXYf4BTml3XUpu9IhP4448QB6RP5lLBq9X.5RrLXUn4ay', 0, 2, '', NULL, '2024-04-26 23:50:47', '', NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
