CREATE TABLE IF NOT EXISTS `restaurant`.`order_has_dishes` (
  `order_id` INT NOT NULL,
  `dish_id` INT NOT NULL,
  PRIMARY KEY (`order_id`, `dish_id`),
  INDEX `fk_order_has_dishes_dish_idx` (`dish_id` ASC) VISIBLE,
  INDEX `fk_order_has_dishes_order_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_has_dishes_order`
    FOREIGN KEY (`order_id`)
    REFERENCES `restaurant`.`orders` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_order_has_dishes_dish`
    FOREIGN KEY (`dish_id`)
    REFERENCES `restaurant`.`dishes` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
ENGINE = InnoDB;
