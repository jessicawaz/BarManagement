To run the program, run both the creation script and the data script once on a MySQL server. This will create everything the program needs to connect to the database as well as some starting data and user credentials. Use any of the following credentials to log in:
bob	bob
apanda	123abc
sam1989	sm1989

Also make sure to include the MySQL JDBC in the classpath. If you are using maven you can add this to the dependencies.
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>8.0.28</version>
</dependency>