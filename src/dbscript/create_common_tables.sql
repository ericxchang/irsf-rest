CREATE DATABASE  IF NOT EXISTS `irsfmast` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `irsfmast`;
-- MySQL dump 10.13  Distrib 5.6.23, for Win64 (x86_64)
--
-- Host: nj01app5050    Database: irsfmast
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
-- Table structure for table `audit_trail`
--

DROP TABLE IF EXISTS `audit_trail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_trail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `customer_name` varchar(45) DEFAULT NULL,
  `action` varchar(45) NOT NULL,
  `details` varchar(1000) DEFAULT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cc_ndc_index`
--

DROP TABLE IF EXISTS `cc_ndc_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cc_ndc_index` (
  `cc_ndc` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`cc_ndc`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `country` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `code` varchar(3) COLLATE utf8_unicode_ci NOT NULL,
  `iso2` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  `fips` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `iso3` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isono` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `clmin` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `clmax` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dststart` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dstend` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
  `load_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `job_id` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`code`,`iso2`),
  KEY `country_iso2_indx` (`iso2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_definition`
--

DROP TABLE IF EXISTS `customer_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_definition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(45) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `create_timestamp` timestamp NULL DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT NULL,
  `last_updated_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_name_UNIQUE` (`customer_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `domain_elements`
--

DROP TABLE IF EXISTS `domain_elements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain_elements` (
  `domain` varchar(12) NOT NULL DEFAULT '',
  `domain_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`domain`,`domain_value`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_notification`
--

DROP TABLE IF EXISTS `event_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(45) DEFAULT NULL,
  `event_type` varchar(45) NOT NULL,
  `reference_id` int(11) DEFAULT NULL COMMENT 'reference id',
  `message` varchar(250) NOT NULL,
  `status` varchar(45) NOT NULL,
  `create_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `acknowledge_timestamp` timestamp NULL DEFAULT NULL,
  `last_updated_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iprn`
--

DROP TABLE IF EXISTS `iprn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iprn` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `term_country` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `prime_minus_2` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `prime_minus_3` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `prime_minus_4` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `iso2` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `code` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tos` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tosdesc` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ndc` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `locality` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `provider` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `supplement` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lemin` int(11) DEFAULT NULL,
  `lemax` int(11) DEFAULT NULL,
  `last_update` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL,
  `filler` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
  `load_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `job_id` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `iprn_iso2_indx` (`iso2`),
  KEY `iprn_prime4_indx` (`prime_minus_4`),
  KEY `iprn_prime3_indx` (`prime_minus_3`),
  KEY `iprn_prime2_indx` (`prime_minus_2`),
  KEY `iprn_tos_indx` (`tos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iprn_special_unallocated`
--

DROP TABLE IF EXISTS `iprn_special_unallocated`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iprn_special_unallocated` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `term_country` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `prime_minus_2` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `prime_minus_3` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `prime_minus_4` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `iso2` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `code` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tos` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tosdesc` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ndc` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `locality` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `provider` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `supplement` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lemin` int(11) DEFAULT NULL,
  `lemax` int(11) DEFAULT NULL,
  `iprn` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_update` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL,
  `filler` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
  `load_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `job_id` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `iprnsa_iso2_indx` (`iso2`),
  KEY `iprnsa_tos_indx` (`tos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ndc`
--

DROP TABLE IF EXISTS `ndc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ndc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `iso2` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tos` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tosdesc` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ndc` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `locality` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `provider` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `utc` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL,
  `supplement` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `admindiv` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `billingid` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `effective_date` date DEFAULT NULL,
  `pos` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lemin` int(11) DEFAULT NULL,
  `lemax` int(11) DEFAULT NULL,
  `ocn` varchar(4) COLLATE utf8_unicode_ci DEFAULT NULL,
  `filler` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
  `load_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `job_id` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `ndc_tos_indx` (`tos`),
  KEY `ndc_iso2_indx` (`iso2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prem_rng_file_load_tracker`
--

DROP TABLE IF EXISTS `prem_rng_file_load_tracker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prem_rng_file_load_tracker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `load_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `upl_file_name` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lpn_file_name` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `notes` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spring_batch_job_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `premium`
--

DROP TABLE IF EXISTS `premium`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `premium` (
  `term_country` varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `dial_pattern` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `dial_pattern_type` varchar(7) NOT NULL DEFAULT '',
  `iso2` varchar(2) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `code` varchar(3) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `tos` varchar(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `tosdesc` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `ndc` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `locality` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `provider` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `supplement` varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_update` date DEFAULT NULL,
  `job_id` longtext CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `load_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `prm_tos_indx` (`tos`),
  KEY `prm_iso2_indx` (`iso2`),
  KEY `prm_code_indx` (`code`),
  KEY `prm_dp_indx` (`dial_pattern`),
  KEY `prn_supp_indx` (`supplement`),
  KEY `prn_prov_indx` (`provider`),
  KEY `prn_lst_ipdt` (`last_update`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `range_ndc`
--

DROP TABLE IF EXISTS `range_ndc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `range_ndc` (
  `term_country` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cc_ndc` varchar(18) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `iso2` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `code` varchar(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tos` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tosdesc` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ndc` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `locality` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `provider` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `supplement` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `job_id` longtext COLLATE utf8mb4_unicode_ci,
  `load_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `rng_ndc_iso2_indx` (`iso2`),
  KEY `rng_ndc_tos_indx` (`tos`),
  KEY `rng_ndc_cc_indx` (`code`),
  KEY `rng_ndc_ccndc_indx` (`cc_ndc`),
  KEY `rng_ndc_supp_indx` (`supplement`),
  KEY `rng_ndc_prov_indx` (`provider`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `unalloc_alloc`
--

DROP TABLE IF EXISTS `unalloc_alloc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unalloc_alloc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `iso2` varchar(2) DEFAULT NULL,
  `ndc` varchar(10) DEFAULT NULL,
  `indicator` varchar(1) DEFAULT NULL,
  `filler` varchar(12) DEFAULT NULL,
  `load_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `job_id` mediumtext,
  PRIMARY KEY (`id`),
  KEY `unalloc_iso2_indx` (`iso2`),
  KEY `unalloc_ndc_indx` (`ndc`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_definition`
--

DROP TABLE IF EXISTS `user_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_definition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `role` varchar(45) DEFAULT NULL COMMENT 'system admin, customer admin, user ',
  `password` varchar(100) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `create_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_UNIQUE` (`user_name`),
  KEY `customer_id_fk_idx` (`customer_id`),
  CONSTRAINT `customer_id_fk` FOREIGN KEY (`customer_id`) REFERENCES `customer_definition` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-15  9:54:43