## ses_facet_search

The main purpose of this project is to explore implementations of faceted search with [elasticsearch](elasticsearch.org) in scala using the [elastic4s](https://github.com/sksamuel/elastic4s) elasticsearch DSL for indexing, querying, searching. etc.

### Before Compilation or ETL Execution

This project depends on the [ses_common](https://github.com/hfgiii/ses_common). Before you can compile and execute any code in this project, you must clone the __ses_common__ project and execute the following __sbt__ command in the directory containing the __ses_common__ clone:

 ```
     sbt publish-local
 ```
For more detail, check the [README.md](https://github.com/hfgiii/ses_common/blob/master/README.md) in the __ses_common__ project.

### Loading CSV Data into PostgresSql

There are two CSV files used a data source:

 1. [iso_3166_2_countries.csv](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/resources/iso_3166_2_countries.csv), which is a source for the [countries](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/resources/countries.sql) database table.
 2. [traffic_accidents.csv](https://github.com/hfgiii/ses_facet_search/tree/master/core/src/main/resources), which is the source for the [traffic_accidents](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/resources/traffic_acidents.sql) database table.
 3. [census_neighborhood_demographics_2010.csv](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/resources/census_neighborhood_demographics_2010.csv), which is the source for the [nghbr_demographics_2011](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/resources/n_demo.sql) database table. 
 
Download [PostgreSQL](http://www.postgresql.org/download/) and install it and start the PostgreSQL server. Download and install [pgAdmin](http://www.pgadmin.org/), the stand-alone Postgre Client. From pgAdmin login into the PostgreSQL server and create the __essource__ database. You can use any user id and password you want, but the __essource__ in the project uses:

```
   userid = rush
   password = manga927
```

If you use a different userid and password, you have to make changes to to [application.conf](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/resources/application.conf) and the following declarations in the _SlickConfig_ object in [Build.scala](https://github.com/hfgiii/ses_facet_search/blob/master/project/Build.scala).

``` scala

  val FS_SQL_URL = Properties.envOrElse("FS_SQL_URL", "jdbc:postgresql://localhost:5432/essource")
  val FS_SQL_USER = Properties.envOrElse("FS_SQL_USER", "rush")
  val FS_SQL_PASS = Properties.envOrElse("FS_SQL_PASS", "manga927")
  
```

Create the _countries_, _traffic_accidents_, and _nghbr_demographics_2011_ tables in the __essource__ database. Note: At this time there is no ETL implemented for _nghbr_demographics_2011_; creating this table is optional. 

In the directory where you pulled the repo, run __sbt compile__; __sbt__ generates scala [slick](https://github.com/slick/slick) classes used to populate the PosgesSQL tables

There are two CSV-PostgreSQL ETL processes implemented:

  1. [CountriesCsvToPostgres](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/scala/org/hfgiii/ses/fs/core/etl/csvtordb/CountriesCsvToPostgres.scala), which populates the __countries__ table.
  2. [TrafficAccidentsCsvToPostgres](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/scala/org/hfgiii/ses/fs/core/etl/csvtordb/TrafficAccidentsCsvToPostgres.scala), which populates the _traffic_accidents_ table.
  

### Loading PostgreSQL Tables into Elasticsearch

There are two PostgrSQL-Elasticsearch ETL Processes implemented:

1. [CountriesPostgresToES](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/scala/org/hfgiii/ses/fs/core/etl/rdbtoes/CountriesPostgresToES.scala), which loads the elasticsearch __countries__ index from the __countries__ PostgreSQL table.
2. [TrafficAccidentsPostgreToES](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/scala/org/hfgiii/ses/fs/core/etl/rdbtoes/TrafficAccidentsPostgreToES.scala), which loads the elasticsearch _traffic_accidents_ from th _traffic_accidents_ PostgreSQL table.

Note: The elasticsearch node in these process is running locally. Once the process finishes, it shuts down the  elasticsearch embedded node and all loaded data is lost. If you wish to load the data into the an external elasticsearch node, replace [initLocalEs4sClient](https://github.com/hfgiii/ses_common/blob/master/core/src/main/scala/org/hfgiii/ses/common/admin/EsAdmin.scala) with [initRemoteEs4sClient](https://github.com/hfgiii/ses_common/blob/master/core/src/main/scala/org/hfgiii/ses/common/admin/EsAdmin.scala) int the implementation of [org.hfgiii.ses.fs.core.edbtoes.ES](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/scala/org/hfgiii/ses/fs/core/etl/rdbtoes/ES.scala),


### Running ETL Processes

You can run the ETL processes by issuing the following __sbt__ command for each process:


 ```
     sbt core/run
 ```
 
 The response will be:
 
 ```
    Multiple main classes detected, select one to run:

    [1] org.hfgiii.ses.fs.core.etl.rdbtoes.CountriesPostgresToES
    [2] org.hfgiii.ses.fs.core.etl.csvtordb.CountriesCsvToPostgres
    [3] org.hfgiii.ses.fs.core.etl.csvtordb.TrafficAccidentsCsvToPostgres
    [4] org.hfgiii.ses.fs.core.etl.rdbtoes.TrafficAccidentsPostgreToES

    Enter number: 

 ```
 
 The [2] and [3] processes should be run before the [1] and [4] processes. 

  

 

