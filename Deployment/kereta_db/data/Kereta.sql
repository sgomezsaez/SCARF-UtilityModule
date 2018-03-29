-- MySQL dump 10.13  Distrib 5.5.58, for linux-glibc2.12 (x86_64)
--
-- Host: localhost    Database: Kereta
-- ------------------------------------------------------
-- Server version	5.5.58

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
-- Table structure for table `kereta_application`
--

DROP TABLE IF EXISTS `kereta_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_application` (
  `id` varchar(36) COLLATE utf8_bin NOT NULL DEFAULT '',
  `name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `description` text COLLATE utf8_bin,
  `application_type` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tier` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`tier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_application`
--

LOCK TABLES `kereta_application` WRITE;
/*!40000 ALTER TABLE `kereta_application` DISABLE KEYS */;
INSERT INTO `kereta_application` VALUES ('603e3430-0cee-4be4-b29c-1a9b8a0bfdbb','MediaWiki',NULL,'wiki','pt_app','mackfn','2017-12-04 07:14:14',0),('603e3430-0cee-4be4-b29c-1a9b8a0bfdbb','MediaWiki front-end',NULL,NULL,NULL,'mackfn','2017-12-04 07:14:14',1),('603e3430-0cee-4be4-b29c-1a9b8a0bfdbb','MediaWiki persistence layer',NULL,NULL,NULL,'mackfn','2017-12-04 07:14:14',2);
/*!40000 ALTER TABLE `kereta_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_applicationType`
--

DROP TABLE IF EXISTS `kereta_applicationType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_applicationType` (
  `name` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '',
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_applicationType`
--

LOCK TABLES `kereta_applicationType` WRITE;
/*!40000 ALTER TABLE `kereta_applicationType` DISABLE KEYS */;
INSERT INTO `kereta_applicationType` VALUES ('wiki','mackfn','2017-12-04 07:14:14');
/*!40000 ALTER TABLE `kereta_applicationType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_dataType`
--

DROP TABLE IF EXISTS `kereta_dataType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_dataType` (
  `name` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '',
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_dataType`
--

LOCK TABLES `kereta_dataType` WRITE;
/*!40000 ALTER TABLE `kereta_dataType` DISABLE KEYS */;
INSERT INTO `kereta_dataType` VALUES ('array of arrays','Kereta','2017-12-04 07:14:14'),('array of numbers','Kereta','2017-12-04 07:14:14'),('array of strings','Kereta','2017-12-04 07:14:14'),('number','Kereta','2017-12-04 07:14:14'),('string','Kereta','2017-12-04 07:14:14');
/*!40000 ALTER TABLE `kereta_dataType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_distribution`
--

DROP TABLE IF EXISTS `kereta_distribution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_distribution` (
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `topology` text COLLATE utf8_bin,
  `topology_language` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `topology_language_version` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `alias` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `namespace` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `application_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_distribution`
--

LOCK TABLES `kereta_distribution` WRITE;
/*!40000 ALTER TABLE `kereta_distribution` DISABLE KEYS */;
INSERT INTO `kereta_distribution` VALUES ('5831e0d0-3a8b-453c-a963-fc1ec3bfd681',' ','','','mackfn','2017-12-04 07:14:14','pt_IaaS','','603e3430-0cee-4be4-b29c-1a9b8a0bfdbb'),('6617d158-da36-4913-a60e-cb986d9461a2',' ','','','mackfn','2017-12-04 07:14:14','pt_DBaaS','','603e3430-0cee-4be4-b29c-1a9b8a0bfdbb'),('9a2194ff-eaa4-4d0e-890a-8f898de73e53','','','','mackfn','2017-12-04 07:14:14','pt_Prms','','603e3430-0cee-4be4-b29c-1a9b8a0bfdbb');
/*!40000 ALTER TABLE `kereta_distribution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_function`
--

DROP TABLE IF EXISTS `kereta_function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_function` (
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `function` text COLLATE utf8_bin,
  `description` text COLLATE utf8_bin,
  `function_type` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `alias` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_function`
--

LOCK TABLES `kereta_function` WRITE;
/*!40000 ALTER TABLE `kereta_function` DISABLE KEYS */;
INSERT INTO `kereta_function` VALUES ('2929801b-edd0-427b-bffe-5c6470af492b','IGR_t(\nIFF(\nEQU(To,1),\nIFE( <(MOD(t,12),3), 125, 42 ) \n) +\nIFF(\nEQU(To,2),\nIFE( <(MOD(t,12),3), 120, 0 )\n))',NULL,'cost','mackfn','2017-12-04 07:14:14','pt_adap'),('4053faff-1397-40ad-a825-3f2da57556c5','IFF(EQU(w,0),5) +\nIFF(EQU(w,1),12)','average transactions per user\nparms: w: workload numbering','misc','mackfn','2017-12-04 07:14:14','pt_tpu'),('4c5f5308-c1f4-49e7-b50f-1c36edd14ac4','IGR_t(\n  SUM_w\n  (\n    FCT(pt_pro, w:w) *\n    FCT(pt_users, t:t) *\n    FCT(pt_tpu, w:w) *\n    FCT(pt_rpt, t:t) *\n    FCT(pt_sat, t:t$T:To) *\n    FCT(pt_av, T:To)\n  )\n)','Prototype, revenue function;\nparms: t: time interval, To: topology numbering, w: workload numbering','revenue','mackfn','2017-12-04 07:14:14','pt_rev'),('78791dcb-b940-459f-b118-aedb9e54009f','IFF(EQU(T,0), 0.998)+\nIFF(EQU(T,1), 0.9975)+\nIFF(EQU(T,2), 0.9975)','average availability\nparms: T: topology numbering','misc','mackfn','2017-12-04 07:14:14','pt_av'),('9a7fe164-c573-4741-b25c-488f5ad1e3a2','IFF(EQU(w,0), 0.7)+\nIFF(EQU(w,1), 0.3)','probability of occurrence of workload \nparms: w: workload numbering','misc','mackfn','2017-12-04 07:14:14','pt_pro'),('9ff80cad-00d6-4c0e-aae2-e86ad54816fa','IFE(<(MOD(t,12), 10),0.18, 0.35)','average revenue per transaction\nparms: t: point in time','misc','mackfn','2017-12-04 07:14:14','pt_rpt'),('d440033b-b6c0-44e3-8c2f-834a075f5774','IFE(\n<(MOD(t,12), 1), 5000,\nIFE(<(MOD(t,12),3), 3200,2000)\n)','average number of users per month\nparms: t: time period','misc','mackfn','2017-12-04 07:14:14','pt_users'),('f0a6a7ec-3945-4333-9a47-38b346ddcb6e','IFF(\nEQU(T, 0),\nIFE(<(MOD(t,12),1), 0.71, 0.82)\n)+\nIFF(\nEQU(T, 1),\nIFE(<(MOD(t,12),1), 0.96, 0.97)\n)+\nIFF(\nEQU(T, 2),\nIFE(<(MOD(t,12),1), 0.98, 0.999)\n)','average user satisfaction\nparms: t: time interval, T: topology numbering','misc','mackfn','2017-12-04 07:14:14','pt_sat');
/*!40000 ALTER TABLE `kereta_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_functionType`
--

DROP TABLE IF EXISTS `kereta_functionType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_functionType` (
  `name` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '',
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_functionType`
--

LOCK TABLES `kereta_functionType` WRITE;
/*!40000 ALTER TABLE `kereta_functionType` DISABLE KEYS */;
INSERT INTO `kereta_functionType` VALUES ('cost','Kereta','2017-12-04 07:14:14'),('misc','Kereta','2017-12-04 07:14:14'),('revenue','Kereta','2017-12-04 07:14:14');
/*!40000 ALTER TABLE `kereta_functionType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_offering`
--

DROP TABLE IF EXISTS `kereta_offering`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_offering` (
  `number` int(11) NOT NULL DEFAULT '0',
  `nefolog_configuration_id` int(11) DEFAULT NULL,
  `nefolog_offering_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `nefolog_service_type` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `distribution_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `nefolog_provider` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `nefolog_configuration` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`number`,`distribution_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_offering`
--

LOCK TABLES `kereta_offering` WRITE;
/*!40000 ALTER TABLE `kereta_offering` DISABLE KEYS */;
INSERT INTO `kereta_offering` VALUES (1,289,'offerings/elasticComputeCloud','/serviceTypes/infrastructures','mackfn','2017-12-04 07:14:14','5831e0d0-3a8b-453c-a963-fc1ec3bfd681','/providers/AmazonWebServices','/offerings/elasticComputeCloud/configuration_289'),(1,187,'/offerings/relationalDatabaseService','/serviceTypes/sqlDatabases','mackfn','2017-12-04 07:14:14','6617d158-da36-4913-a60e-cb986d9461a2','/providers/AmazonWebServices','/offerings/relationalDatabaseService/configuration_187');
/*!40000 ALTER TABLE `kereta_offering` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_offeringTier`
--

DROP TABLE IF EXISTS `kereta_offeringTier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_offeringTier` (
  `application_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `application_tier` int(11) NOT NULL,
  `offering_number` int(11) NOT NULL,
  `distribution_id` varchar(36) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`application_id`,`application_tier`,`offering_number`,`distribution_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_offeringTier`
--

LOCK TABLES `kereta_offeringTier` WRITE;
/*!40000 ALTER TABLE `kereta_offeringTier` DISABLE KEYS */;
INSERT INTO `kereta_offeringTier` VALUES ('603e3430-0cee-4be4-b29c-1a9b8a0bfdbb',2,1,'5831e0d0-3a8b-453c-a963-fc1ec3bfd681'),('603e3430-0cee-4be4-b29c-1a9b8a0bfdbb',2,1,'6617d158-da36-4913-a60e-cb986d9461a2');
/*!40000 ALTER TABLE `kereta_offeringTier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_parameter`
--

DROP TABLE IF EXISTS `kereta_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_parameter` (
  `name` varchar(128) COLLATE utf8_bin NOT NULL,
  `function_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `data_type` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `default_value` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`name`,`function_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_parameter`
--

LOCK TABLES `kereta_parameter` WRITE;
/*!40000 ALTER TABLE `kereta_parameter` DISABLE KEYS */;
INSERT INTO `kereta_parameter` VALUES ('T','78791dcb-b940-459f-b118-aedb9e54009f','number','0','mackfn','2017-12-04 07:14:14','Topology identifier'),('To','2929801b-edd0-427b-bffe-5c6470af492b','number','0','mackfn','2017-12-04 07:14:14','topology identifier'),('To','4c5f5308-c1f4-49e7-b50f-1c36edd14ac4','number','0','mackfn','2017-12-04 07:14:14','topology identifier'),('t','2929801b-edd0-427b-bffe-5c6470af492b','array of numbers','[0,0]','mackfn','2017-12-04 07:14:14','time interval boundaries'),('t','4c5f5308-c1f4-49e7-b50f-1c36edd14ac4','array of numbers','[0,0]','mackfn','2017-12-04 07:14:14','time interval boundaries'),('t','9ff80cad-00d6-4c0e-aae2-e86ad54816fa','number','0','mackfn','2017-12-04 07:14:14','time period'),('t','d440033b-b6c0-44e3-8c2f-834a075f5774','number','0','mackfn','2017-12-04 07:14:14','time period'),('w','4053faff-1397-40ad-a825-3f2da57556c5','number','0','mackfn','2017-12-04 07:14:14','workload identifier'),('w','4c5f5308-c1f4-49e7-b50f-1c36edd14ac4','array of numbers','[0,0]','mackfn','2017-12-04 07:14:14','identifier of the first and last workload'),('w','9a7fe164-c573-4741-b25c-488f5ad1e3a2','number','0','mackfn','2017-12-04 07:14:14','workload identifier');
/*!40000 ALTER TABLE `kereta_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_performance`
--

DROP TABLE IF EXISTS `kereta_performance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_performance` (
  `name` varchar(128) COLLATE utf8_bin NOT NULL,
  `distribution_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `offering_number` int(11) NOT NULL,
  `value` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `requirement_type` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `data_type` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `demand` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`name`,`distribution_id`,`offering_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_performance`
--

LOCK TABLES `kereta_performance` WRITE;
/*!40000 ALTER TABLE `kereta_performance` DISABLE KEYS */;
INSERT INTO `kereta_performance` VALUES ('location','5831e0d0-3a8b-453c-a963-fc1ec3bfd681',1,'EU','non-functional','string','mackfn','2016-02-06 18:50:01','='),('location','6617d158-da36-4913-a60e-cb986d9461a2',1,'EU','non-functional',NULL,'mackfn','2016-02-07 18:57:06','='),('throughput','5831e0d0-3a8b-453c-a963-fc1ec3bfd681',1,'w_0: 13.3 Req/s - w_1: 3.0 Req/s','non-functional','string','mackfn','2017-12-04 07:14:14','='),('throughput','6617d158-da36-4913-a60e-cb986d9461a2',1,'w_0: 19.4 Req/s - w_1: 6.9 Req/s','non-functional',NULL,'mackfn','2017-12-04 07:14:14','=');
/*!40000 ALTER TABLE `kereta_performance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_requirement`
--

DROP TABLE IF EXISTS `kereta_requirement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_requirement` (
  `name` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '',
  `value` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `data_type` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `requirement_type` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `application_id` varchar(36) COLLATE utf8_bin NOT NULL DEFAULT '',
  `application_tier` int(11) NOT NULL,
  `demand` varchar(1) COLLATE utf8_bin DEFAULT '=',
  PRIMARY KEY (`name`,`application_id`,`application_tier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_requirement`
--

LOCK TABLES `kereta_requirement` WRITE;
/*!40000 ALTER TABLE `kereta_requirement` DISABLE KEYS */;
INSERT INTO `kereta_requirement` VALUES ('location','EU','string','mackfn','2017-12-04 07:14:14','non-functional','603e3430-0cee-4be4-b29c-1a9b8a0bfdbb',2,'='),('throughput','15','number','mackfn','2017-12-04 07:14:14','non-functional','603e3430-0cee-4be4-b29c-1a9b8a0bfdbb',0,'>');
/*!40000 ALTER TABLE `kereta_requirement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_requirementType`
--

DROP TABLE IF EXISTS `kereta_requirementType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_requirementType` (
  `name` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '',
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_requirementType`
--

LOCK TABLES `kereta_requirementType` WRITE;
/*!40000 ALTER TABLE `kereta_requirementType` DISABLE KEYS */;
INSERT INTO `kereta_requirementType` VALUES ('functional','Kereta','2017-12-04 07:14:14'),('non-functional','Kereta','2017-12-04 07:14:14');
/*!40000 ALTER TABLE `kereta_requirementType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_subfunction`
--

DROP TABLE IF EXISTS `kereta_subfunction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_subfunction` (
  `number` int(11) NOT NULL,
  `utility_function_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `function_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`number`,`utility_function_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_subfunction`
--

LOCK TABLES `kereta_subfunction` WRITE;
/*!40000 ALTER TABLE `kereta_subfunction` DISABLE KEYS */;
INSERT INTO `kereta_subfunction` VALUES (1,'282b087f-7939-4a13-bfcd-87b8a684b98b','4c5f5308-c1f4-49e7-b50f-1c36edd14ac4','mackfn','2017-12-04 07:14:14'),(1,'687b5a5f-74e1-41ac-a6c8-e6f350c14274','4c5f5308-c1f4-49e7-b50f-1c36edd14ac4','mackfn','2017-12-04 07:14:14'),(1,'a00bc6d5-6b77-4ce3-9807-c162e41be21a','4c5f5308-c1f4-49e7-b50f-1c36edd14ac4','mackfn','2017-12-04 07:14:14'),(2,'282b087f-7939-4a13-bfcd-87b8a684b98b','2929801b-edd0-427b-bffe-5c6470af492b','mackfn','2017-12-04 07:14:14'),(2,'687b5a5f-74e1-41ac-a6c8-e6f350c14274','2929801b-edd0-427b-bffe-5c6470af492b','mackfn','2017-12-04 07:14:14'),(2,'a00bc6d5-6b77-4ce3-9807-c162e41be21a','2929801b-edd0-427b-bffe-5c6470af492b','mackfn','2017-12-04 07:14:14'),(3,'687b5a5f-74e1-41ac-a6c8-e6f350c14274','nefolog$289','mackfn','2017-12-04 07:14:14'),(3,'a00bc6d5-6b77-4ce3-9807-c162e41be21a','nefolog$187','mackfn','2017-12-04 07:14:14');
/*!40000 ALTER TABLE `kereta_subfunction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kereta_utilityFunction`
--

DROP TABLE IF EXISTS `kereta_utilityFunction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kereta_utilityFunction` (
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `distribution_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `author` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `alias` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kereta_utilityFunction`
--

LOCK TABLES `kereta_utilityFunction` WRITE;
/*!40000 ALTER TABLE `kereta_utilityFunction` DISABLE KEYS */;
INSERT INTO `kereta_utilityFunction` VALUES ('282b087f-7939-4a13-bfcd-87b8a684b98b','9a2194ff-eaa4-4d0e-890a-8f898de73e53','Prototype Utility Function for pt_Prms','mackfn','2017-12-04 07:14:14','pt_uf-0'),('687b5a5f-74e1-41ac-a6c8-e6f350c14274','5831e0d0-3a8b-453c-a963-fc1ec3bfd681','Prototype Utility Function for pt_IaaS','mackfn','2017-12-04 07:14:14','pt_uf-1'),('a00bc6d5-6b77-4ce3-9807-c162e41be21a','6617d158-da36-4913-a60e-cb986d9461a2','Prototype Utility Function for pt_DBaaS','mackfn','2017-12-04 07:14:14','pt_uf-2');
/*!40000 ALTER TABLE `kereta_utilityFunction` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-04  7:23:11
