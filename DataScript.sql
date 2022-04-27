use barmgmt;

insert into User (UserFirstName, UserLastName, UserName, UserPassword) values
("Robert", "Smith", "Bob", "bob");
insert into User (UserFirstName, UserLastName, UserName, UserPassword) values
("Amanda", "Williams", "aPanda", "1234abc");
insert into User (UserFirstName, UserLastName, UserName, UserPassword) values
("Samantha", "Morgan", "sam1989", "sm1989");

call addFood("Hamburger", 40, 100, "Dinner");
call addFood("Sliders", 70, 200, "Appetizer");
call addFood("Grilled Cheese", 20, 75, "Lunch");
call addFood("Ice Cream Sandwich", 20, 50, "Dessert");

call addDrink("Figi Mineral Water", 50, 200, "Water");
call addDrink("Rootbeer", 40, 150, "SoftDrink");
call addDrink("Corona Lite", 70, 100, "Alcoholic");
call addDrink("Fruit Punch", 20, 50, "Other");
update InventoryItem set ItemQuantityInStock = ItemLowAmt * 1.231;

update InventoryItem set ItemQuantityInStock = ItemLowAmt * 0.674 where InventoryItemID in (1, 7);

insert into inventoryorder (OrderDate, OrderPricePerUnit, OrderQuantity, CompleteDate, InventoryItemID, UserID)
	values('2022-04-14', 4.12, 200, null, 2, 1);
insert into inventoryorder (OrderDate, OrderPricePerUnit, OrderQuantity, CompleteDate, InventoryItemID, UserID)
	values('2022-03-29', 7.41, 200, null, 7, 2);
insert into inventoryorder (OrderDate, OrderPricePerUnit, OrderQuantity, CompleteDate, InventoryItemID, UserID)
	values('2021-12-18', 5.25, 50, '2022-02-24', 8, 3);
insert into inventoryorder (OrderDate, OrderPricePerUnit, OrderQuantity, CompleteDate, InventoryItemID, UserID)
	values('2022-01-09', 4.51, 50, '2022-03-08', 4, 2);
