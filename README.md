#JMX Bean to Re-index WSO2 Registry Solr data. Edit


This project provides a JMX bean to reindex WSO2 Registry Solr data.

Configuration Steps:

1). Clone the Repo and compiled the project

2). Add compiled JAR file into <CARBOn_HOME>/repository/components/dropins directory

3). Restart the server.

4). Connect to the server using jconsole and you can find related operation under object path of "org.wso2.registry.reindex"
