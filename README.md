# pf-springdata-elastic-search

==> Pre-Requisites to work with Spring boot + Spring batch + elastic search 

1) Spring Boot 2.1.4.RELEASE
2) Elastic Search 6.5.4.RELEASE
3) ElasticSearch-rest-high-level-client 6.5.4.RELEASE
4) Java 8
5) Maven
6) Swagger 2.9.2.RELEASE

I tried to connect the Elastic Search through Transport Client and AWS Elastic Search is not supporting right now . 
 Transport client   (we can use this - if it is local installation)
 RestHighLevel client (We can use this - if it is AWS host)

host-name : search-aws-elastic-search-p2ehrrbqxvdlbp6nzjbcpamrra.us-east-2.es.amazonaws.com (it is mentioned in application.properties file)
To connect from browser : https://search-aws-elastic-search-p2ehrrbqxvdlbp6nzjbcpamrra.us-east-2.es.amazonaws.com

The below details will appears

{
  "name" : "b5BKWbU",
  "cluster_name" : "008026978848:aws-elastic-search",
  "cluster_uuid" : "bzZfGDjqRmu0g0LFYsdYqw",
  "version" : {
    "number" : "6.5.4",       
    "build_flavor" : "oss",
    "build_type" : "zip",
    "build_hash" : "d2ef93d",
    "build_date" : "2019-04-01T17:24:35.943541Z",
    "build_snapshot" : false,
    "lucene_version" : "7.5.0",
    "minimum_wire_compatibility_version" : "5.6.0",
    "minimum_index_compatibility_version" : "5.0.0"
  },
  "tagline" : "You Know, for Search"
}



To see the data in the elastic search us the below url's (either browser or postman)

 https://search-aws-elastic-search-p2ehrrbqxvdlbp6nzjbcpamrra.us-east-2.es.amazonaws.com/_cat/count  (OR) --> this will give only documents count
 https://search-aws-elastic-search-p2ehrrbqxvdlbp6nzjbcpamrra.us-east-2.es.amazonaws.com/_search  --> this will give all documents along with total documents

Delete the indices from the elastic search using the below command
https://search-aws-elastic-search-p2ehrrbqxvdlbp6nzjbcpamrra.us-east-2.es.amazonaws.com/plan_index
Response :   "acknowledged": true

How to Run:
This is a maven project and we can build the project and run it through the commandprompt
mvn clean install to build
mvn spring-boot:run to run

I am using the Swagger API to test all the API's  and below is the URL
http://localhost:9090/swagger-ui.html#/

This application have below API's

16) GET/{plan-id}/plans   - Reading plan details by given Id
17) GET/plans             -Search Plan Details by given PLAN_NAME OR SPONSOR_NAME OR SPONSOR_STATE
18) POST /plans           -Upload plan details from csv file

I am using the Spring Batch for the upload API which will read the data from the CSV file and Upload in the elastic search instance by batch wise. I am using the Bulk Processor in the Rest High Level Client to upload the data and it has a listener which will monitor each batch and failures . So that we can upload the failure records into the elastic search.






