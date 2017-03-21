CREATE DATABASE  IF NOT EXISTS `cust01` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `cust01`;
-- MySQL dump 10.13  Distrib 5.6.23, for Win64 (x86_64)
--
-- Host: nj01app5050    Database: cust01
-- ------------------------------------------------------
-- Server version	5.7.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `list_defintion`
--

DROP TABLE IF EXISTS `list_defintion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list_defintion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(45) NOT NULL,
  `list_name` varchar(25) CHARACTER SET latin1 NOT NULL,
  `type` enum('WL','BL') CHARACTER SET latin1 NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `last_updated_by` varchar(45) CHARACTER SET latin1 NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(45) NOT NULL,
  `create_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `list_name_UNIQUE` (`list_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `list_details`
--

DROP TABLE IF EXISTS `list_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `list_ref_id` int(11) NOT NULL,
  `upload_req_ref_id` int(11) DEFAULT NULL,
  `dial_pattern` varchar(15) CHARACTER SET latin1 NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `notes` varchar(100) DEFAULT NULL,
  `customer_date` date DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `list_ref_id_idx` (`list_ref_id`),
  KEY `upload_req_ref_id_idx` (`upload_req_ref_id`),
  CONSTRAINT `lis_ref_id_fk` FOREIGN KEY (`list_ref_id`) REFERENCES `list_defintion` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `upload_req_ref_id_fk` FOREIGN KEY (`upload_req_ref_id`) REFERENCES `list_upload_request` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `list_upload_request`
--

DROP TABLE IF EXISTS `list_upload_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list_upload_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `list_ref_id` int(11) NOT NULL,
  `delimiter` varchar(1) NOT NULL DEFAULT ',',
  `status` enum('process','complete','fail') NOT NULL,
  `error_data` mediumtext,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `list_ref_id_idx` (`list_ref_id`),
  CONSTRAINT `list_ref_id` FOREIGN KEY (`list_ref_id`) REFERENCES `list_defintion` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `partition_data_details`
--

DROP TABLE IF EXISTS `partition_data_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partition_data_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `partition_id` int(11) NOT NULL,
  `reference` varchar(45) NOT NULL COMMENT 'either rule_id or list_name',
  `dial_pattern` varchar(15) NOT NULL,
  `customer_date` date DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL COMMENT 'from list table',
  `notes` varchar(100) DEFAULT NULL,
  `cc` varchar(3) DEFAULT NULL,
  `ndc` varchar(15) DEFAULT NULL,
  `iso2` varchar(2) DEFAULT NULL,
  `tos` varchar(1) DEFAULT NULL,
  `tosdesc` varchar(20) DEFAULT NULL,
  `provider` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `partition_fk` (`partition_id`),
  CONSTRAINT `partition_fk` FOREIGN KEY (`partition_id`) REFERENCES `partition_defintion` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `partition_defintion`
--

DROP TABLE IF EXISTS `partition_defintion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partition_defintion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `wl_id` int(11) DEFAULT NULL,
  `bl_id` int(11) DEFAULT NULL,
  `rule_ids` varchar(250) DEFAULT NULL,
  `status` enum('draft','locked') NOT NULL DEFAULT 'draft',
  `draft_date` timestamp NULL DEFAULT NULL,
  `last_export_date` timestamp NULL DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT NULL,
  `last_updated_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `partition_export_history`
--

DROP TABLE IF EXISTS `partition_export_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partition_export_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `partition_id` int(11) NOT NULL,
  `export_file_long` blob NOT NULL,
  `export_file_short` blob NOT NULL,
  `export_whitelist` blob,
  `export_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(45) CHARACTER SET latin1 NOT NULL COMMENT 'status to send file to down stream system',
  `reason` varchar(1000) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `partion_id_fk_idx` (`partition_id`),
  CONSTRAINT `partion_id_fk` FOREIGN KEY (`partition_id`) REFERENCES `partition_defintion` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rule_definition`
--

DROP TABLE IF EXISTS `rule_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rule_definition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `partition_id` int(11) NOT NULL,
  `data_source` varchar(15) NOT NULL,
  `details` varchar(1000) CHARACTER SET latin1 NOT NULL,
  `dial_pattern_type` enum('PRIME-2','PRIME-3','PRIME-4') CHARACTER SET latin1 NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'for IPRN only',
  `create_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` varchar(45) CHARACTER SET latin1 NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`id`),
  KEY `partition_id_fk_idx` (`partition_id`),
  CONSTRAINT `partition_id_fk` FOREIGN KEY (`partition_id`) REFERENCES `partition_defintion` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'cust01'
--

--
-- Dumping routines for database 'cust01'
--
/*!50003 DROP PROCEDURE IF EXISTS `proc_list_details_all_test_vw` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = latin1 */ ;
/*!50003 SET character_set_results = latin1 */ ;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `proc_list_details_all_test_vw`(IN list_id INT)
BEGIN
SET @_list_id = list_id;
PREPARE stmt FROM "SELECT ld.*, rt.* from (select * from list_details ld WHERE ld.list_ref_id = ?) ld LEFT JOIN irsfmast.range_ndc rt on rt.cc_ndc=irsfmast.getMaxMatchCCNDCPattern(ld.dial_pattern)";
EXECUTE stmt USING @_list_id;
DEALLOCATE PREPARE stmt;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_list_details_range_all_vw` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = latin1 */ ;
/*!50003 SET character_set_results = latin1 */ ;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `proc_list_details_range_all_vw`(IN list_id INT)
BEGIN
SET @_list_id = list_id;
PREPARE stmt FROM "SELECT ld.*, rt.* from (  select irsfmast.getMaxMatchCCNDCPattern(dial_pattern) as match_cc_ndc, ld.* from list_details ld WHERE list_ref_id = ? ) ld LEFT JOIN irsfmast.range_ndc rt on rt.cc_ndc=ld.match_cc_ndc";
EXECUTE stmt USING @_list_id;
DEALLOCATE PREPARE stmt;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_list_details_range_vw` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = latin1 */ ;
/*!50003 SET character_set_results = latin1 */ ;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `proc_list_details_range_vw`(IN list_id INT, IN row_start INT, IN num_recs INT)
BEGIN
DECLARE cnt INT;
SET @_list_id = list_id;
SET @_row_start = row_start;
SET @_num_recs = num_recs;
 if (row_start is null) then
 	SET @_row_start = 0;
 end if;
 if (num_recs is null) then
 	select count(*) INTO cnt from list_details where list_ref_id=list_id;
 	SET @_num_recs = cnt;
 end if;

PREPARE stmt FROM "SELECT ld.*, rt.* from (  select irsfmast.getMaxMatchCCNDCPattern(dial_pattern) as match_cc_ndc, ld.* from list_details ld WHERE list_ref_id = ?  limit ?, ?) ld LEFT JOIN irsfmast.range_ndc rt on rt.cc_ndc=ld.match_cc_ndc";
EXECUTE stmt USING @_list_id,@_row_start,@_num_recs;
DEALLOCATE PREPARE stmt;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-21 16:19:34
