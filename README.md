# cassandra-jdbc-driver
Cassandra JDBC driver that works with 2.x and above. 

## What is this?
This is nothing but a JDBC driver build on top of existing popular java clients(e.g. [DataStax Java Driver](https://github.com/datastax/java-driver/)). It can be used with [SQuirreL SQL](http://www.squirrelsql.org/) for development and [Pentaho BI Server](http://community.pentaho.com/) for data analysis and reporting.

## Where we are?
[0.1.0 Release](https://github.com/zhicwu/cassandra-jdbc-driver/releases/tag/0.1.0) - Proof of Concept

## What's next?
- Tracing support
- Write(INSERT/UPDATE/DELETE) support
- PreparedStatement support
- Advanced types(LOBs, Collections and UDTs) support
- Multiple ResultSet support
- Better SQL compatibility(e.g. Aggregation functions and probably simple table joins and sub-queries)
- More providers...

## How to use?
#### Hello World
```java
...
// Driver driver = new com.github.cassandra.jdbc.CassandraDriver();
Properties props = new Properties();
props.setProperty("user", "cassandra");
props.setProperty("password", "cassandra");

// ":datastax" in the URL is optional, it suggests to use DataStax Java driver as the provider to connect to Cassandra
Connection conn = DriverManager.connect("jdbc:c*:datastax://host1,host2/system_auth?consistencyLevel=one", props);
// change current keyspace from system_auth to system
conn.setCatalog("system");

// query peers table in current keyspace, by default the SQL below will be translated into the following CQL:
// SELECT * FROM peers LIMIT 10000
// Please be aware that the original SQL does not work in Cassandra as table alias is not supported
ResultSet rs = conn.createStatement().executeQuery("select p.* from peers p");
while (rs.next()) {
...
}
...
```
#### Connection Properties
| Property         | Description                                | Default Value |
|:-----------------|:-------------------------------------------|:--------------|
| port             | Port number                                | 9042 |
| compression      | Enable compression(snappy/lz4), which required additional libs | none |
| consistencyLevel | Default consistency level for all requests | one |
| connectTimeout   | Connect time out in milliseconds           | 5000 |
| readTimeout      | Read time out in milliseconds              | 30000 |
| fetchSize        | Default fetch size                         | 100 |
| localDc          | Name of local Datacenter, this will enable DCAwareRoundRobinPolicy in DataStax Java Driver | NULL | 
| quiet            | Whether to swollow exception for unsupported features or not | true |
| sqlFriendly      | Whether to translate given SQL to CQL      | true |
#### SQuirrel SQL
1. Configure Apache Cassandra Driver by including all required libs and set _com.github.cassandra.jdbc.CassandraDriver_ as driver
2. Create a new alias using Aapche Cassandra Driver with a valid URL like _java:c*://localhost/system_ and credentials
3. That's it! You should now be able to connect to Cassandra using this driver, issue simple queries and browse meta data(columns, indices and primary keys) like any other database in SQuirrel SQL