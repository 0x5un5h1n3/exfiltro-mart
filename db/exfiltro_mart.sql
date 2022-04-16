-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.29-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             11.1.0.6116
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table exfiltro_mart.employee_attendance
DROP TABLE IF EXISTS `employee_attendance`;
CREATE TABLE IF NOT EXISTS `employee_attendance` (
  `usr_id` int(11) NOT NULL,
  `usr_username` varchar(50) NOT NULL,
  `att_month` varchar(50) NOT NULL,
  `att_year` varchar(50) NOT NULL,
  `att_date` date NOT NULL,
  `att_login_time` time NOT NULL,
  `att_logout_time` time NOT NULL,
  `att_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.gin
DROP TABLE IF EXISTS `gin`;
CREATE TABLE IF NOT EXISTS `gin` (
  `gin_id` int(11) NOT NULL AUTO_INCREMENT,
  `invoice_id` int(11) NOT NULL,
  `itm_id` int(11) NOT NULL,
  `itm_code` varchar(50) NOT NULL,
  `itm_name` varchar(50) NOT NULL,
  `stk_itm_price` int(11) NOT NULL,
  `stk_selling_price` int(11) NOT NULL,
  `gin_discount` int(11) NOT NULL,
  `gin_new_item_price` int(11) NOT NULL,
  `gin_amount` int(11) NOT NULL,
  `gin_total` int(11) NOT NULL,
  `gin_new_total` int(11) NOT NULL,
  `gin_savings` int(11) NOT NULL,
  `gin_employee` varchar(50) NOT NULL,
  `gin_profit` int(11) NOT NULL,
  `gin_date` date NOT NULL,
  `gin_month` varchar(50) NOT NULL,
  `gin_year` varchar(50) NOT NULL,
  `gin_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`gin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.grn
DROP TABLE IF EXISTS `grn`;
CREATE TABLE IF NOT EXISTS `grn` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `grn_id` int(11) NOT NULL,
  `itm_name` varchar(50) NOT NULL,
  `itm_code` varchar(50) NOT NULL,
  `grn_quantity` int(11) NOT NULL,
  `grn_item_price` int(11) NOT NULL,
  `sup_username` varchar(50) NOT NULL,
  `grn_total` int(11) NOT NULL,
  `grn_lead_time` date NOT NULL,
  `grn_status` varchar(50) NOT NULL,
  `grn_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.invoice
DROP TABLE IF EXISTS `invoice`;
CREATE TABLE IF NOT EXISTS `invoice` (
  `invoice_id` int(11) NOT NULL,
  `inv_type` varchar(50) NOT NULL,
  `inv_net_total` double NOT NULL,
  `inv_nbt` double NOT NULL,
  `inv_vat` double NOT NULL,
  `inv_grand_total` double NOT NULL,
  `gin_employee` varchar(50) NOT NULL,
  `gin_date` date NOT NULL,
  `gin_month` varchar(50) NOT NULL,
  `gin_year` varchar(50) NOT NULL,
  `inv_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`invoice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.item
DROP TABLE IF EXISTS `item`;
CREATE TABLE IF NOT EXISTS `item` (
  `itm_id` int(11) NOT NULL AUTO_INCREMENT,
  `itm_name` varchar(50) NOT NULL,
  `itm_code` varchar(50) NOT NULL,
  `itm_category` varchar(50) NOT NULL,
  `sup_username` varchar(50) NOT NULL,
  `sup_id` int(11) NOT NULL,
  `itm_stat` tinyint(4) NOT NULL,
  `itm_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`itm_id`),
  UNIQUE KEY `itm_name` (`itm_name`),
  UNIQUE KEY `itm_code` (`itm_code`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.log
DROP TABLE IF EXISTS `log`;
CREATE TABLE IF NOT EXISTS `log` (
  `usr_id` int(11) DEFAULT NULL,
  `usr_type` varchar(50) DEFAULT NULL,
  `usr_username` varchar(50) DEFAULT NULL,
  `log_activity` varchar(50) NOT NULL,
  `log_description` varchar(50) NOT NULL,
  `log_state` varchar(50) NOT NULL,
  `log_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.payroll
DROP TABLE IF EXISTS `payroll`;
CREATE TABLE IF NOT EXISTS `payroll` (
  `pay_id` int(11) NOT NULL AUTO_INCREMENT,
  `usr_id` int(11) NOT NULL,
  `usr_username` varchar(50) NOT NULL,
  `pay_month` varchar(50) NOT NULL,
  `pay_year` varchar(50) NOT NULL,
  `pay_date` date NOT NULL,
  `pay_attendance` int(11) NOT NULL,
  `pay_salary` int(11) NOT NULL,
  `pay_stat` varchar(50) NOT NULL,
  `pay_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.purchase_order
DROP TABLE IF EXISTS `purchase_order`;
CREATE TABLE IF NOT EXISTS `purchase_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `po_id` int(11) NOT NULL,
  `itm_name` varchar(50) NOT NULL,
  `itm_code` varchar(50) NOT NULL,
  `po_item_quantity` int(11) NOT NULL,
  `po_item_price` int(11) NOT NULL,
  `sup_username` varchar(50) NOT NULL,
  `po_total` int(11) NOT NULL,
  `po_lead_time` date NOT NULL,
  `po_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `po_id` (`po_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.sales
DROP TABLE IF EXISTS `sales`;
CREATE TABLE IF NOT EXISTS `sales` (
  `sal_id` int(11) NOT NULL AUTO_INCREMENT,
  `invoice_id` int(11) NOT NULL,
  `itm_id` int(11) NOT NULL,
  `itm_code` varchar(50) NOT NULL,
  `itm_name` varchar(50) NOT NULL,
  `stk_itm_price` int(11) NOT NULL,
  `stk_selling_price` int(11) NOT NULL,
  `gin_discount` int(11) NOT NULL,
  `gin_new_item_price` int(11) NOT NULL,
  `gin_amount` int(11) NOT NULL,
  `gin_total` int(11) NOT NULL,
  `gin_new_total` int(11) NOT NULL,
  `gin_savings` int(11) NOT NULL,
  `gin_employee` varchar(50) NOT NULL,
  `sal_profit` int(11) NOT NULL,
  `sal_date` date NOT NULL,
  `sal_month` varchar(50) NOT NULL,
  `sal_year` varchar(50) NOT NULL,
  `sal_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sal_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.stock
DROP TABLE IF EXISTS `stock`;
CREATE TABLE IF NOT EXISTS `stock` (
  `stk_id` int(11) NOT NULL AUTO_INCREMENT,
  `itm_code` varchar(50) NOT NULL,
  `itm_name` varchar(50) NOT NULL,
  `stk_itm_price` int(11) NOT NULL,
  `stk_selling_price` int(11) NOT NULL,
  `stk_profit` int(11) NOT NULL,
  `sup_username` varchar(50) NOT NULL,
  `stk_count` int(11) NOT NULL,
  `stk_low_stock` int(11) NOT NULL,
  `stk_status` varchar(50) NOT NULL,
  `stk_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`stk_id`),
  UNIQUE KEY `itm_code` (`itm_code`),
  UNIQUE KEY `itm_name` (`itm_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.stock_return
DROP TABLE IF EXISTS `stock_return`;
CREATE TABLE IF NOT EXISTS `stock_return` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stk_id` int(11) NOT NULL,
  `itm_code` varchar(50) NOT NULL,
  `itm_name` varchar(50) NOT NULL,
  `sup_username` varchar(50) NOT NULL,
  `ret_return_count` int(11) NOT NULL,
  `ret_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.supplier
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE IF NOT EXISTS `supplier` (
  `sup_id` int(11) NOT NULL AUTO_INCREMENT,
  `sup_username` varchar(50) NOT NULL,
  `sup_address` varchar(50) NOT NULL,
  `sup_email` varchar(50) NOT NULL,
  `sup_phone` varchar(50) NOT NULL,
  `sup_company` varchar(50) NOT NULL,
  `sup_stat` tinyint(4) NOT NULL,
  `sup_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sup_id`),
  UNIQUE KEY `sup_username` (`sup_username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table exfiltro_mart.user
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `usr_id` int(11) NOT NULL AUTO_INCREMENT,
  `usr_username` varchar(50) NOT NULL,
  `usr_password` varchar(50) NOT NULL,
  `usr_type` varchar(50) NOT NULL,
  `usr_position` varchar(50) NOT NULL,
  `usr_nic` varchar(50) NOT NULL,
  `usr_phone` varchar(50) NOT NULL,
  `usr_stat` tinyint(4) NOT NULL,
  `usr_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`usr_id`) USING BTREE,
  UNIQUE KEY `usr_username` (`usr_username`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
