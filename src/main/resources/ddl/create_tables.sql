CREATE TABLE HOUSE.INVENTORY (
    ID BIGINT NOT NULL GENERATED by default AS IDENTITY PRIMARY KEY,
    BRAND VARCHAR(255),
    LAST_BOUGHT TIMESTAMP,
    LAST_UPD_BY VARCHAR(255),
    USE_BY TIMESTAMP,
    QUANTITY DECIMAL(8, 2),
    TYPE VARCHAR(255),
    NOTES VARCHAR(1000),
    ITEM_ID BIGINT,
    MEASUREMENT VARCHAR(100),
    RATING INTEGER,
    LOCK_MEASUREMENT BOOLEAN,
    CONSTRAINT INVENTORY_ITEM_FK FOREIGN KEY (ITEM_ID) REFERENCES HOUSE.ITEM(ID)
);


CREATE TABLE HOUSE.INVENTORY_HIST (
    ID BIGINT NOT NULL GENERATED by default AS IDENTITY PRIMARY KEY,
    CREATION_DTTM TIMESTAMP,
    INVEN_ID INTEGER,
    NEW VARCHAR(255),
    PREVIOUS VARCHAR(255),
    UPDATE_TYPE VARCHAR(255)
);

CREATE TABLE HOUSE.CHORES (
    ID BIGINT NOT NULL GENERATED by default AS IDENTITY PRIMARY KEY,
    FREQUENCY VARCHAR(255),
    FREQUENCY_PERIOD VARCHAR(20),
    NAME VARCHAR(255),
    TYPE VARCHAR(255),
    LAST_COMPLETED_DATE TIMESTAMP,
    LAST_COMPLETED_BY VARCHAR(20),
    DUE_DATE TIMESTAMP,
    DIFFICULTY INTEGER,
    ASSIGNEE VARCHAR(100)
);
    
CREATE TABLE HOUSE.CHORE_HIST (
    ID BIGINT NOT NULL GENERATED by default AS IDENTITY PRIMARY KEY,
    CHORE_ID BIGINT NOT NULL,
    LAST_DONE_BY VARCHAR(100) NOT NULL,
    LAST_DONE TIMESTAMP NOT NULL,
    DATE_ENTERED TIMESTAMP NOT NULL,
    CONSTRAINT SQL150419183622531 FOREIGN KEY (CHORE_ID) REFERENCES HOUSE.CHORE(ID)
);

CREATE TABLE HOUSE.RECIPES (
    ID BIGINT NOT NULL GENERATED by default AS IDENTITY PRIMARY KEY,
    NAME VARCHAR(100) NOT NULL,
    TYPE VARCHAR(100) NOT NULL,
    STYLE VARCHAR(100) NOT NULL,
    SERVING_SIZE BIGINT,
    LAST_MADE TIMESTAMP,
    TIMES_MADE INTEGER NOT NULL,
    RATING INTEGER NOT NULL,
    NOTES VARCHAR(1000),
    IMAGE VARCHAR(100),
    COOK_TIME VARCHAR(100)
);

CREATE TABLE HOUSE.INGREDIENTS (
    ID BIGINT NOT NULL GENERATED by default AS IDENTITY PRIMARY KEY,
    RECIPE_ID BIGINT NOT NULL,
    ITEM_ID BIGINT NOT NULL,
    AMOUNT DECIMAL(8, 2),
    MEASUREMENT VARCHAR(100),
    FORM VARCHAR(100),
    CONSTRAINT SQL150419183622531 FOREIGN KEY (RECIPE_ID) REFERENCES HOUSE.RECIPES(ID),
    CONSTRAINT SQL150419183622540 FOREIGN KEY (ITEM_ID) REFERENCES HOUSE.ITEMS(ID)
);

CREATE TABLE HOUSE.ITEMS (
    ID BIGINT NOT NULL GENERATED by default AS IDENTITY PRIMARY KEY,
    NAME VARCHAR(100),
    FAVORITE_BRAND VARCHAR(100),
    ESSENTIAL BOOLEAN,
    THRESHOLD DECIMAL(8, 2),
    MEASUREMENT VARCHAR(100),
    CONSTRAINT SQL150419181428830 PRIMARY KEY (ID)
);

CREATE TABLE HOUSE.TODO (
    ID BIGINT NOT NULL GENERATED by default AS IDENTITY PRIMARY KEY,
    NAME VARCHAR(100),
    LOCATION VARCHAR(100),
    DATE_ENTERED TIMESTAMP,
    ENTERED_BY VARCHAR(20),
    NOTES VARCHAR(1000),
    COMPLETED_BY VARCHAR(20),
    COMPLETED_ON TIMESTAMP,
    DUE_DATE TIMESTAMP,
    IMPORTANCE INTEGER	
);

CREATE TABLE HOUSE.SCRATCH (
    ID BIGINT NOT NULL GENERATED by default AS IDENTITY PRIMARY KEY,
    NOTE VARCHAR(2000)
);

create table USER_INFO (
	ID BIGINT NOT NULL GENERATED by default AS IDENTITY PRIMARY KEY,
	USER_NAME VARCHAR(20) NOT NULL,
	PASSWORD VARCHAR(50) NOT NULL,
	ROLE VARCHAR(20) NOT NULL
);

CREATE TABLE HOUSE.GROCERY_ITEM (
    ID BIGINT NOT NULL GENERATED by default AS IDENTITY PRIMARY KEY,
    ITEM_ID BIGINT,
    INVENTORY_ID BIGINT,
    SPECIFIC_BRAND VARCHAR(255),
    QUANTITY DECIMAL(8, 2),
    MEASUREMENT VARCHAR(100),
    CONSTRAINT GROCERY_INVENTORY_FK FOREIGN KEY (ITEM_ID) REFERENCES HOUSE.INVENTORY(ID),
    CONSTRAINT GROCERY_ITEM_FK FOREIGN KEY (ITEM_ID) REFERENCES HOUSE.ITEMS(ID)
);

--changes from previous 
--rename table item_name to ITEMS;
--rename column inventory.name_id to item_id;
--rename column ingredients.name_id to item_id;
--rename column items.item_name to name;
--create grocery_item