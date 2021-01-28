/*
 Navicat Premium Data Transfer

 Source Server         : Location-MySql
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : mall_medicine

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 28/01/2021 14:19:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mall_boiled
-- ----------------------------
DROP TABLE IF EXISTS `mall_boiled`;
CREATE TABLE `mall_boiled`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `count` int(0) NULL DEFAULT NULL COMMENT '服数',
  `price` int(0) NULL DEFAULT NULL COMMENT '收费',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_boiled
-- ----------------------------
INSERT INTO `mall_boiled` VALUES (1, 1, 1);
INSERT INTO `mall_boiled` VALUES (3, 2, 2);
INSERT INTO `mall_boiled` VALUES (4, 3, 3);
INSERT INTO `mall_boiled` VALUES (5, 4, 4);
INSERT INTO `mall_boiled` VALUES (6, 5, 5);
INSERT INTO `mall_boiled` VALUES (7, 6, 8);
INSERT INTO `mall_boiled` VALUES (8, 7, 8);
INSERT INTO `mall_boiled` VALUES (9, 8, 8);
INSERT INTO `mall_boiled` VALUES (10, 9, 10);
INSERT INTO `mall_boiled` VALUES (11, 10, 10);

-- ----------------------------
-- Table structure for mall_cart
-- ----------------------------
DROP TABLE IF EXISTS `mall_cart`;
CREATE TABLE `mall_cart`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '购物车id',
  `product_id` int(0) NULL DEFAULT NULL COMMENT '商品id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `quantity` int(0) NULL DEFAULT NULL COMMENT '商品数量',
  `selected` int(0) NULL DEFAULT NULL COMMENT '是否已经勾选：0：未勾选，1：已勾选',
  `formula` int(0) NULL DEFAULT 1 COMMENT '所在配方',
  `handle` int(0) NULL DEFAULT NULL COMMENT '处理类型',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_category
-- ----------------------------
DROP TABLE IF EXISTS `mall_category`;
CREATE TABLE `mall_category`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父类目录名称',
  `type` int(0) NULL DEFAULT NULL COMMENT '父类目录级别，1代表一级',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '父id',
  `order_num` int(0) NULL DEFAULT NULL COMMENT '目录展示时的排序',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_category
-- ----------------------------
INSERT INTO `mall_category` VALUES (1, '中药', 1, 0, 1, '2021-01-17 22:16:24', '2021-01-17 22:16:24');
INSERT INTO `mall_category` VALUES (2, '西药', 1, 0, 1, '2021-01-17 22:16:25', '2021-01-17 22:16:25');

-- ----------------------------
-- Table structure for mall_grasp
-- ----------------------------
DROP TABLE IF EXISTS `mall_grasp`;
CREATE TABLE `mall_grasp`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '抓药师邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_grasp
-- ----------------------------
INSERT INTO `mall_grasp` VALUES (1, '2374887017@qq.com');
INSERT INTO `mall_grasp` VALUES (4, '429741825@qq.com');

-- ----------------------------
-- Table structure for mall_order
-- ----------------------------
DROP TABLE IF EXISTS `mall_order`;
CREATE TABLE `mall_order`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号（非主键id）',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `total_price` int(0) NULL DEFAULT NULL COMMENT '订单总价格',
  `receiver_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人手机号',
  `receiver_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人地址',
  `order_status` int(0) NULL DEFAULT NULL COMMENT '订单状态，0：用户已取消，10：未付款（初始状态），20：已付款，30：已发货，40：完成交易',
  `postage` int(0) NULL DEFAULT 0 COMMENT '运费，默认为0',
  `payment_type` int(0) NULL DEFAULT 1 COMMENT '支付类型，1：在线支付',
  `delivery_time` timestamp(0) NULL DEFAULT NULL COMMENT '发货时间',
  `pay_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付时间',
  `end_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易完成时间',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注信息',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_order_item
-- ----------------------------
DROP TABLE IF EXISTS `mall_order_item`;
CREATE TABLE `mall_order_item`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归属订单id',
  `product_id` int(0) NULL DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `unit_price` int(0) NULL DEFAULT NULL COMMENT '单价',
  `quantity` int(0) NULL DEFAULT NULL COMMENT '商品数量',
  `total_price` int(0) NULL DEFAULT NULL COMMENT '商品总价',
  `formula` int(0) NULL DEFAULT 1 COMMENT '所在配方',
  `handle` int(0) NULL DEFAULT NULL COMMENT '处理类型',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_order_item
-- ----------------------------
INSERT INTO `mall_order_item` VALUES (30, '111305077893', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 7, 21, 1, 20, '2021-01-22 11:30:50', '2021-01-22 11:30:50');
INSERT INTO `mall_order_item` VALUES (31, '111305077893', 17, '常山', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 2, 12, 1, 17, '2021-01-22 11:30:50', '2021-01-22 11:30:50');
INSERT INTO `mall_order_item` VALUES (32, '111305077893', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 2, 14, 2, 21, '2021-01-22 11:30:50', '2021-01-22 11:30:50');
INSERT INTO `mall_order_item` VALUES (33, '111305077893', 21, '草乌', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 1, 7, 5, 18, '2021-01-22 11:30:50', '2021-01-22 11:30:50');
INSERT INTO `mall_order_item` VALUES (34, '111305077893', 1, '商枝', 'http://localhost:10088/api/imgs/0b080b71-dfed-46bf-a70a-3ff6dfa074e2.jpg', 1, 4, 84, 8, 13, '2021-01-22 11:30:50', '2021-01-22 11:30:50');
INSERT INTO `mall_order_item` VALUES (35, '114411647247', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 7, 21, 1, 20, '2021-01-22 14:41:16', '2021-01-22 14:41:16');
INSERT INTO `mall_order_item` VALUES (36, '114424947774', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 7, 21, 1, 20, '2021-01-22 14:42:49', '2021-01-22 14:42:49');
INSERT INTO `mall_order_item` VALUES (37, '114454247636', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 8, 24, 1, 20, '2021-01-22 14:45:42', '2021-01-22 14:45:42');
INSERT INTO `mall_order_item` VALUES (38, '114475647789', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 9, 63, 1, 21, '2021-01-22 14:47:56', '2021-01-22 14:47:56');
INSERT INTO `mall_order_item` VALUES (39, '114504947644', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 4, 12, 1, 20, '2021-01-22 14:50:49', '2021-01-22 14:50:49');
INSERT INTO `mall_order_item` VALUES (40, '114512047780', 17, '常山', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 4, 24, 1, 17, '2021-01-22 14:51:20', '2021-01-22 14:51:20');
INSERT INTO `mall_order_item` VALUES (41, '114512047780', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 6, 18, 1, 20, '2021-01-22 14:51:20', '2021-01-22 14:51:20');
INSERT INTO `mall_order_item` VALUES (42, '115191447420', 21, '草乌', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 8, 56, 1, 18, '2021-01-23 15:19:14', '2021-01-23 15:19:14');
INSERT INTO `mall_order_item` VALUES (43, '116512447148', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 4, 28, 7, 21, '2021-01-25 16:51:24', '2021-01-25 16:51:24');
INSERT INTO `mall_order_item` VALUES (44, '117132247487', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 4, 12, 1, 20, '2021-01-25 17:13:22', '2021-01-25 17:13:22');
INSERT INTO `mall_order_item` VALUES (45, '117132247487', 21, '草乌', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 1, 7, 1, 18, '2021-01-25 17:13:22', '2021-01-25 17:13:22');
INSERT INTO `mall_order_item` VALUES (46, '117132247487', 17, '常山', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 2, 12, 2, 17, '2021-01-25 17:13:22', '2021-01-25 17:13:22');
INSERT INTO `mall_order_item` VALUES (47, '117132247487', 21, '草乌', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 1, 7, 3, 18, '2021-01-25 17:13:22', '2021-01-25 17:13:22');
INSERT INTO `mall_order_item` VALUES (48, '117493447562', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 2, 6, 1, 20, '2021-01-25 17:49:34', '2021-01-25 17:49:34');
INSERT INTO `mall_order_item` VALUES (49, '117493447562', 17, '常山', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 1, 6, 1, 17, '2021-01-25 17:49:34', '2021-01-25 17:49:34');
INSERT INTO `mall_order_item` VALUES (50, '117493447562', 21, '草乌', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 2, 14, 2, 18, '2021-01-25 17:49:34', '2021-01-25 17:49:34');
INSERT INTO `mall_order_item` VALUES (51, '117493447562', 28, '沉香', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 5, 3, 45, 3, 22, '2021-01-25 17:49:34', '2021-01-25 17:49:34');
INSERT INTO `mall_order_item` VALUES (52, '117493447562', 16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', 1, 4, 12, 4, 20, '2021-01-25 17:49:34', '2021-01-25 17:49:34');

-- ----------------------------
-- Table structure for mall_product
-- ----------------------------
DROP TABLE IF EXISTS `mall_product`;
CREATE TABLE `mall_product`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '商品主键ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品图片路径地址',
  `detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品详情',
  `category_id` int(0) NULL DEFAULT NULL COMMENT '分类id',
  `price` int(0) NULL DEFAULT NULL COMMENT '价格，单位-分',
  `stock` int(0) NULL DEFAULT NULL COMMENT '库存数量',
  `status` int(0) NULL DEFAULT NULL COMMENT '商品上架状态：0：下架，1：上架',
  `py_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拼音',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_product
-- ----------------------------
INSERT INTO `mall_product` VALUES (1, '商枝', 'http://localhost:10088/api/imgs/0b080b71-dfed-46bf-a70a-3ff6dfa074e2.jpg', '非常好的药材11111111111111111', 1, 1, 496, 1, 'SHANG ZHI', '2021-01-18 22:12:22', '2021-01-18 22:12:22');
INSERT INTO `mall_product` VALUES (8, '荆芥', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', '暂无介绍1', 1, 1, 500, 1, 'JING JIE', '2021-01-23 15:18:42', '2021-01-23 15:18:42');
INSERT INTO `mall_product` VALUES (9, '降香', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'JIANG XIANG', '2021-01-22 11:24:28', '2021-01-22 11:24:28');
INSERT INTO `mall_product` VALUES (10, '细辛', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'XI XIN', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (11, '苦酒', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'KU JIU', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (12, '菘蓝', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'SONG LAN', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (13, '京墨', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'JING MO', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (14, '苡仁', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'YI REN', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (15, '斑蛰', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'BAN ZHE', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (16, '侧柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', '', 1, 1, 436, 1, 'CE BO', '2021-01-27 09:58:17', '2021-01-27 09:58:17');
INSERT INTO `mall_product` VALUES (17, '常山', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 491, 1, 'CHANG SHAN', '2021-01-22 11:24:28', '2021-01-22 11:24:28');
INSERT INTO `mall_product` VALUES (18, '紫苑', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'ZI YUAN', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (19, '绥草', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'SUI CAO', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (20, '青黛', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'QING DAI', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (21, '草乌', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 487, 1, 'CAO WU', '2021-01-22 11:24:28', '2021-01-22 11:24:28');
INSERT INTO `mall_product` VALUES (22, '郁金', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'YU JIN', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (23, '卷柏', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'JUAN BO', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (24, '苦木', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'KU MU', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (25, '陆英', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'LU YING', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (26, '官桂', 'http://localhost:10088/api/imgs/2c0fae13-0e20-4bcc-b856-2b99a4257aee.png', '暂无介绍', 1, 10, 500, 1, 'GUAN GUI', '2021-01-22 11:24:28', '2021-01-22 11:24:28');
INSERT INTO `mall_product` VALUES (27, '琥珀', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'HU PO', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (28, '沉香', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', '暂无介绍', 1, 5, 497, 1, 'CHEN XIANG', '2021-01-22 11:24:28', '2021-01-22 11:24:28');
INSERT INTO `mall_product` VALUES (29, '商陆', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'SHANG LU', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (30, '茜草', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'QIAN CAO', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (31, '空青', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'KONG QING', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (32, '续断', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'XU DUAN', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (33, '玳瑁', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'DAI MAO', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (34, '落葵', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'LUO KUI', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (35, '菖蒲', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'CHANG PU', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (36, '佩兰', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'PEI LAN', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (37, '泽兰', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'ZE LAN', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (38, '青蒿', 'http://localhost:10088/api/imgs/19376f0c-b8f0-4e76-beab-c54507a5a579.jpg', NULL, 1, 1, 500, 1, 'QING HAO', '2021-01-18 21:32:07', '2021-01-18 21:32:07');
INSERT INTO `mall_product` VALUES (40, '测试药材', 'http://localhost:10088/api/imgs/67e940de-842b-4fd5-8e91-8768919301f7.jpg', 'SADASDASDA...', 1, 2, 500, 1, 'CS', '2021-01-22 11:29:02', '2021-01-22 11:29:02');

-- ----------------------------
-- Table structure for mall_system
-- ----------------------------
DROP TABLE IF EXISTS `mall_system`;
CREATE TABLE `mall_system`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `sys_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统邮箱',
  `sys_authorization` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱授权码',
  `boided_min` int(0) NULL DEFAULT NULL COMMENT '7~10服煮药价格',
  `boided_max` int(0) NULL DEFAULT NULL COMMENT '10服药以上煮药价格',
  `grasp_count` int(0) NULL DEFAULT NULL COMMENT '抓药师上限',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_system
-- ----------------------------
INSERT INTO `mall_system` VALUES (1, '15576618671@163.com', '', 4, 10, 6);

-- ----------------------------
-- Table structure for mall_user
-- ----------------------------
DROP TABLE IF EXISTS `mall_user`;
CREATE TABLE `mall_user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码,md5加密',
  `personalized_signature` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `role` int(0) NULL DEFAULT 1 COMMENT '角色，1.普通用户，2.管理员',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `first_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'First-Name',
  `last_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Last-Name',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所在城市',
  `state` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮编',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_user
-- ----------------------------
INSERT INTO `mall_user` VALUES (1, 'admin', 'cKbJqKhe98Bp+iN3VUqKoQ==', '我是管理员', 2, '123321@qq.com', 'Mr', 'Tan', '中国慕城', '中国', '1', '15888888888', '2021-01-19 10:19:53', '2021-01-19 10:19:53');
INSERT INTO `mall_user` VALUES (19, '13395824134', 'XigFwYLFnj4UuecRe9yhTA==', NULL, 1, '2374887017@qq.com', 'Mr', 'Tang', '长沙市-岳麓区', '中国', '214577', '15576618671', '2021-01-22 11:26:57', '2021-01-22 11:26:57');

-- ----------------------------
-- Table structure for mall_variant
-- ----------------------------
DROP TABLE IF EXISTS `mall_variant`;
CREATE TABLE `mall_variant`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理类型名称',
  `product_id` int(0) NULL DEFAULT NULL COMMENT '药材id',
  `price` int(0) NULL DEFAULT NULL COMMENT '处理价格',
  `order_no` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_variant
-- ----------------------------
INSERT INTO `mall_variant` VALUES (12, '块状', 1, 10, 1, '2021-01-21 15:54:00', '2021-01-21 15:54:00');
INSERT INTO `mall_variant` VALUES (13, '粉状', 1, 20, NULL, '2021-01-21 16:09:55', '2021-01-21 16:09:55');
INSERT INTO `mall_variant` VALUES (14, '块装', 8, 5, NULL, '2021-01-21 16:13:04', '2021-01-21 16:13:04');
INSERT INTO `mall_variant` VALUES (17, '块状', 17, 5, NULL, '2021-01-22 10:31:15', '2021-01-22 10:31:15');
INSERT INTO `mall_variant` VALUES (18, '块状', 21, 6, NULL, '2021-01-22 10:31:32', '2021-01-22 10:31:32');
INSERT INTO `mall_variant` VALUES (19, '粉状', 15, 10, NULL, '2021-01-22 10:31:59', '2021-01-22 10:31:59');
INSERT INTO `mall_variant` VALUES (20, '块状', 16, 2, NULL, '2021-01-22 10:39:29', '2021-01-22 10:39:29');
INSERT INTO `mall_variant` VALUES (21, '粉状', 16, 6, NULL, '2021-01-22 10:39:37', '2021-01-22 10:39:37');
INSERT INTO `mall_variant` VALUES (22, '粉状', 28, 10, NULL, '2021-01-22 11:05:04', '2021-01-22 11:05:04');
INSERT INTO `mall_variant` VALUES (23, '块状', 26, 5, NULL, '2021-01-22 11:06:28', '2021-01-22 11:06:28');
INSERT INTO `mall_variant` VALUES (25, '块状', 40, 5, NULL, '2021-01-22 11:29:33', '2021-01-22 11:29:33');

SET FOREIGN_KEY_CHECKS = 1;
