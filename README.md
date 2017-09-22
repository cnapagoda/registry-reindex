#JMX Bean to Re-index WSO2 Registry Solr data. Edit


This project provides a JMX bean to reindex WSO2 Registry Solr data.

Configuration Steps:

1). Clone the Repo and compiled the project

2). Add compiled JAR file into <CARBOn_HOME>/repository/components/dropins directory

3). Restart the server.

Using JMX

4). Connect to the server using jconsole and you can find related operation under object path of "org.wso2.registry.reindex"


Using WebService call

4). In addition to the JMX bean, this bundle registers an AdminService as well. You can invoke it by calling below URL : https://<SERVER_HOST>:<SERVER_PORT>/services/ReindexAdminService/reindexSolrData
