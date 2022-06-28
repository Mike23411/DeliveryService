-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema DeliveryService
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema DeliveryService
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `DeliveryService` DEFAULT CHARACTER SET utf8 ;
USE `DeliveryService` ;

-- -----------------------------------------------------
-- Table `DeliveryService`.`DeliveryServiceUser`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DeliveryService`.`DeliveryServiceUser` (
  `account` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`account`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DeliveryService`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DeliveryService`.`Customer` (
  `account` VARCHAR(45) NOT NULL,
  `customerCredit` INT NOT NULL,
  `rating` INT NOT NULL,
  `firstName` VARCHAR(45) NOT NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `phoneNumber` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`account`),
  INDEX `fk_Customer_User1_idx` (`account` ASC) VISIBLE,
  CONSTRAINT `fk_Customer_User1`
    FOREIGN KEY (`account`)
    REFERENCES `DeliveryService`.`DeliveryServiceUser` (`account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DeliveryService`.`Store`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DeliveryService`.`Store` (
  `storeName` VARCHAR(45) NOT NULL,
  `revenue` INT NOT NULL,
  PRIMARY KEY (`storeName`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DeliveryService`.`Pilot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DeliveryService`.`Pilot` (
  `account` VARCHAR(45) NOT NULL,
  `numberSuccessfulDeliveries` INT NOT NULL,
  `tax` VARCHAR(45) NOT NULL,
  `license` VARCHAR(45) NOT NULL,
  `firstName` VARCHAR(45) NOT NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `phoneNumber` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`account`),
  UNIQUE INDEX `license_UNIQUE` (`license` ASC) VISIBLE,
  CONSTRAINT `fk_Pilot_User1`
    FOREIGN KEY (`account`)
    REFERENCES `DeliveryService`.`DeliveryServiceUser` (`account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DeliveryService`.`Drone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DeliveryService`.`Drone` (
  `droneId` INT NOT NULL,
  `fuel` INT NOT NULL,
  `maximumLiftCapacity` INT NOT NULL,
  `store` VARCHAR(45) NOT NULL,
  `pilotAccount` VARCHAR(45) NULL,
  PRIMARY KEY (`droneId`, `store`),
  INDEX `fk_Drone_Store1_idx` (`store` ASC) VISIBLE,
  INDEX `fk_Drone_Pilot1_idx` (`pilotAccount` ASC) VISIBLE,
  UNIQUE INDEX `pilotAccount_UNIQUE` (`pilotAccount` ASC) VISIBLE,
  CONSTRAINT `fk_Drone_Store1`
    FOREIGN KEY (`store`)
    REFERENCES `DeliveryService`.`Store` (`storeName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Drone_Pilot1`
    FOREIGN KEY (`pilotAccount`)
    REFERENCES `DeliveryService`.`Pilot` (`account`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DeliveryService`.`CustomerOrder`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DeliveryService`.`CustomerOrder` (
  `orderId` VARCHAR(45) NOT NULL,
  `Drone_droneId` INT NOT NULL,
  `Store_storeName` VARCHAR(45) NOT NULL,
  `customerAccount` VARCHAR(45) NOT NULL,
  `orderDate` TIMESTAMP DEFAULT NOW(),
  PRIMARY KEY (`orderId`, `Store_storeName`),
  INDEX `fk_Order_Drone1_idx` (`Drone_droneId` ASC) VISIBLE,
  INDEX `fk_Order_Store1_idx` (`Store_storeName` ASC) VISIBLE,
  INDEX `fk_CustomerOrder_Customer1_idx` (`customerAccount` ASC) VISIBLE,
  CONSTRAINT `fk_Order_Drone1`
    FOREIGN KEY (`Drone_droneId`)
    REFERENCES `DeliveryService`.`Drone` (`droneId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Order_Store1`
    FOREIGN KEY (`Store_storeName`)
    REFERENCES `DeliveryService`.`Store` (`storeName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CustomerOrder_Customer1`
    FOREIGN KEY (`customerAccount`)
    REFERENCES `DeliveryService`.`Customer` (`account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DeliveryService`.`StoreItem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DeliveryService`.`StoreItem` (
  `itemName` VARCHAR(45) NOT NULL,
  `itemWeight` INT NOT NULL,
  `storeName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`itemName`, `storeName`),
  INDEX `fk_Item_Store1_idx` (`storeName` ASC) VISIBLE,
  CONSTRAINT `fk_Item_Store1`
    FOREIGN KEY (`storeName`)
    REFERENCES `DeliveryService`.`Store` (`storeName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DeliveryService`.`Line`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DeliveryService`.`Line` (
  `itemPrice` INT NOT NULL,
  `itemQuantity` INT NOT NULL,
  `itemId` VARCHAR(45) NOT NULL,
  `orderId` VARCHAR(45) NOT NULL,
  `storeName` VARCHAR(45) NOT NULL,
  `orderDate` TIMESTAMP DEFAULT NOW(),
  PRIMARY KEY (`itemId`, `orderId`, `storeName`),
  INDEX `fk_Line_Item1_idx` (`itemId` ASC) VISIBLE,
  INDEX `fk_Line_CustomerOrder1_idx` (`orderId` ASC, `storeName` ASC) VISIBLE,
  CONSTRAINT `fk_Line_Item1`
    FOREIGN KEY (`itemId`)
    REFERENCES `DeliveryService`.`StoreItem` (`itemName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Line_CustomerOrder1`
    FOREIGN KEY (`orderId` , `storeName`)
    REFERENCES `DeliveryService`.`CustomerOrder` (`orderId` , `Store_storeName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DeliveryService`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DeliveryService`.`user` (
  `username` VARCHAR(16) NOT NULL,
  `email` VARCHAR(255) NULL,
  `password` VARCHAR(32) NOT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP);


-- -----------------------------------------------------
-- Table `DeliveryService`.`user_1`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DeliveryService`.`user_1` (
  `username` VARCHAR(16) NOT NULL,
  `email` VARCHAR(255) NULL,
  `password` VARCHAR(32) NOT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
