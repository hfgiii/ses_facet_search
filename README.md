## ses_facet_search

The main purpose of this project is to explore implementations of faceted search with [elasticsearch](elasticsearch.org) in scala using the [elastic4s](https://github.com/sksamuel/elastic4s) elasticsearch DSL for indexing, querying, searching. etc.

### Loading CSV Data into PostgresSql

There are two CSV files used a data source:

 1. [iso_3166_2_countries.csv](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/resources/iso_3166_2_countries.csv), which is a source for the [countries](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/resources/countries.sql) database table .
 2. [traffic_accidents.csv](https://github.com/hfgiii/ses_facet_search/tree/master/core/src/main/resources), which is the source for the [traffic_accidents](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/resources/traffic_acidents.sql) database table
 
The is another database table [nghbr_demographics_2011](https://github.com/hfgiii/ses_facet_search/blob/master/core/src/main/resources/n_demo.sql) included in the repo, but there is no corresponding CSV file included in the project, mainly because of its size

