USE `navigation` ;

-- -----------------------------------------------------
-- Table `navigation`.`persons`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `navigation`.`persons` (
  `person_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NULL,
  `last_name` VARCHAR(45) NULL,
  PRIMARY KEY (`person_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `navigation`.`locations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `navigation`.`locations` (
  `location_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `coordinate_x` FLOAT NULL,
  `coordinate_y` FLOAT NULL,
  PRIMARY KEY (`location_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `navigation`.`storages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `navigation`.`storages` (
  `storage_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `location_id` INT UNSIGNED NULL,
  PRIMARY KEY (`storage_id`),
  INDEX `fk_storage_location_idx` (`location_id` ASC) VISIBLE,
  CONSTRAINT `fk_storage_location`
    FOREIGN KEY (`location_id`)
    REFERENCES `navigation`.`locations` (`location_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `navigation`.`employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `navigation`.`employees` (
  `employee_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(45) NULL,
  `person_id` INT UNSIGNED NULL,
  PRIMARY KEY (`employee_id`),
  INDEX `fk_person_employee_idx` (`person_id` ASC) VISIBLE,
  CONSTRAINT `fk_person_employee`
    FOREIGN KEY (`person_id`)
    REFERENCES `navigation`.`persons` (`person_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `navigation`.`vehicles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `navigation`.`vehicles` (
  `vehicle_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `year` VARCHAR(45) NULL,
  `make` VARCHAR(45) NULL,
  `model` VARCHAR(45) NULL,
  `trim_level` VARCHAR(45) NULL,
  `license_plate_number` VARCHAR(45) NULL,
  PRIMARY KEY (`vehicle_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `navigation`.`drivers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `navigation`.`drivers` (
  `driver_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `employee_id` INT NULL,
  `vehicle_id` INT UNSIGNED NULL,
  PRIMARY KEY (`driver_id`),
  INDEX `fk_employee_driver_idx` (`employee_id` ASC) VISIBLE,
  INDEX `fk_vehicle_driver_idx` (`vehicle_id` ASC) VISIBLE,
  CONSTRAINT `fk_employee_driver`
    FOREIGN KEY (`employee_id`)
    REFERENCES `navigation`.`employees` (`employee_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_vehicle_driver`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `navigation`.`vehicles` (`vehicle_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `navigation`.`order_recipients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `navigation`.`order_recipients` (
  `order_recipient_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `person_id` INT UNSIGNED NULL,
  `location_id` INT UNSIGNED NULL,
  PRIMARY KEY (`order_recipient_id`),
  INDEX `fk_person_order_recipient_idx` (`person_id` ASC) VISIBLE,
  INDEX `fk_location_order_recipient_idx` (`location_id` ASC) VISIBLE,
  CONSTRAINT `fk_person_order_recipient`
    FOREIGN KEY (`person_id`)
    REFERENCES `navigation`.`persons` (`person_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_location_order_recipient`
    FOREIGN KEY (`location_id`)
    REFERENCES `navigation`.`locations` (`location_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `navigation`.`items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `navigation`.`items` (
  `item_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `item_code` VARCHAR(45) NULL,
  `item_name` VARCHAR(45) NULL,
  `item_description` VARCHAR(45) NULL,
  PRIMARY KEY (`item_id`),
  UNIQUE INDEX `item_code_UNIQUE` (`item_code` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `navigation`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `navigation`.`orders` (
  `order_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_number` VARCHAR(75) NULL,
  `order_status_id` VARCHAR(45) NULL,
  `order_date` DATETIME NULL,
  `delivery_date` DATETIME NULL,
  `storage_id` INT UNSIGNED NULL COMMENT 'The origin location of the order.',
  `order_recipient_id` INT UNSIGNED NULL COMMENT 'The destination location of the order.',
  `driver_id` INT UNSIGNED NULL,
  PRIMARY KEY (`order_id`),
  INDEX `fk_storage_order_idx` (`order_recipient_id` ASC) VISIBLE,
  INDEX `fk_order_order_recipient_idx` (`storage_id` ASC) VISIBLE,
  INDEX `fk_driver_order_idx` (`driver_id` ASC) VISIBLE,
  CONSTRAINT `fk_storage_order`
    FOREIGN KEY (`order_recipient_id`)
    REFERENCES `navigation`.`storages` (`storage_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_recipient_order`
    FOREIGN KEY (`storage_id`)
    REFERENCES `navigation`.`order_recipients` (`order_recipient_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_driver_order`
    FOREIGN KEY (`driver_id`)
    REFERENCES `navigation`.`drivers` (`driver_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `navigation`.`order_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `navigation`.`order_items` (
  `order_item_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `item_quantity` INT NULL,
  `order_id` INT UNSIGNED NULL,
  `item_id` INT UNSIGNED NULL,
  PRIMARY KEY (`order_item_id`),
  INDEX `fk_item_order_item_idx` (`item_id` ASC) VISIBLE,
  INDEX `fk_order_order_item_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `fk_item_order_item`
    FOREIGN KEY (`item_id`)
    REFERENCES `navigation`.`items` (`item_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_order_item`
    FOREIGN KEY (`order_id`)
    REFERENCES `navigation`.`orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
