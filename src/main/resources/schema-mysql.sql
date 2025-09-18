CREATE TABLE IF NOT EXISTS `fillin_abc` (
  `quiz_id` int NOT NULL,
  `question_id` int NOT NULL,
  `email` varchar(45) NOT NULL,
  `answer` varchar(200) DEFAULT NULL,
  `fillin_date` date DEFAULT NULL,
  PRIMARY KEY (`quiz_id`,`email`,`question_id`)
);

CREATE TABLE IF NOT EXISTS `quiz_123` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(200) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `is_published` tinyint DEFAULT '0',
  `version` int DEFAULT '0',
  PRIMARY KEY (`id`)
);

ALTER TABLE `quiz15`.`quiz_123` 
CHANGE COLUMN `description` `description` VARCHAR(500) NOT NULL ;
