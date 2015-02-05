# Host: localhost  (Version: 5.6.21-log)
# Date: 2015-02-05 11:16:27
# Generator: MySQL-Front 5.3  (Build 4.196)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "dish"
#

DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `materials` varchar(255) DEFAULT '',
  `steps` longtext,
  `dishType` varchar(255) DEFAULT NULL,
  `submaterials` varchar(255) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `quantity` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "dishmenu"
#

DROP TABLE IF EXISTS `dishmenu`;
CREATE TABLE `dishmenu` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `dishes` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "dishtype"
#

DROP TABLE IF EXISTS `dishtype`;
CREATE TABLE `dishtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `parentid` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#
# Structure for table "material"
#

DROP TABLE IF EXISTS `material`;
CREATE TABLE `material` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `materialtype` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

#
# Structure for table "materialtype"
#

DROP TABLE IF EXISTS `materialtype`;
CREATE TABLE `materialtype` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `parentid` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "source"
#

DROP TABLE IF EXISTS `source`;
CREATE TABLE `source` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  `html` longtext,
  `title` varchar(255) DEFAULT '',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=21393 DEFAULT CHARSET=utf8;
