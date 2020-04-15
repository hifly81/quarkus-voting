Quarkus App for Voting
=============================

### Launch on OpenShift

Images are downloaded from docker hub and from https://quay.io

Images:
 - Postgres (image debezium/postgres) on port 5432
 - AMQ Streams (Zookeeper on port 2181 and Kafka on port 9092)
 - Voting Service (image quay.io/bridlos/voting-service-quarkus) on port 8080

In order to create the demo on your openshift environment, you need:
 - ocp user with cluster-admin role
 - oc client installed on your machine
 - AMQ Streams 1.x for ocp downloaded from Red Hat<br>
 https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?downloadType=distributions&product=jboss.amq.streams

Follow these instructions to create the demo:

Login to ocp, create a new project, create e new service account runasanyuid (postgres must run as root):
```bash
oc login <ocp_master_url> --token=<ocp_user_token>
oc new-project quarkus-voting
oc create serviceaccount runasanyuid
oc adm policy add-scc-to-user anyuid -z runasanyuid -n quarkus-voting
```

Create postgres, then create voting database:
```bash
oc new-app debezium/postgres
oc patch dc/postgres --patch '{"spec":{"template":{"spec":{"serviceAccountName": "runasanyuid"}}}}'
oc exec $(oc get pods | grep postgres | cut -d " " -f1) -- bash -c 'psql -h localhost -p 5432 -U postgres -c "CREATE DATABASE voting;"'
```

#### Install AMQ Streams on OpenShift 3.11

Install AMQ Streams cluster operator and a kafka cluster with 3 brokers (ephemeral and with prometheus metrics).<br>
This step requires that you've downloaded and unpacked the AMQ Streams zip archive for ocp <br>
(for more info about the installation, https://access.redhat.com/documentation/en-us/red_hat_amq/7.2/html-single/using_amq_streams_on_openshift_container_platform/index)


```bash
#replace the template namespace with quarkus-voting
sed -i 's/namespace: .*/namespace: quarkus-voting/' install/cluster-operator/*RoleBinding*.yaml
oc apply -f install/cluster-operator/020-RoleBinding-strimzi-cluster-operator.yaml -n quarkus-voting
oc apply -f install/cluster-operator/031-RoleBinding-strimzi-cluster-operator-entity-operator-delegation.yaml -n quarkus-voting
oc apply -f install/cluster-operator/032-RoleBinding-strimzi-cluster-operator-topic-operator-delegation.yaml -n quarkus-voting
oc apply -f install/cluster-operator -n quarkus-voting
oc apply -f examples/metrics/kafka-metrics.yaml
```

#### Install AMQ Streams on OpenShift 4.x

In the OpenShift 4 web console, click Operators > OperatorHub.<br>
Search or browse for the AMQ Streams Operator, in the Streaming & Messaging category. <br>
Click the AMQ Streams tile and then, in the sidebar on the right, click Install.<br>
On the Create Operator Subscription screen, choose from the following installation and update options:<br>
Installation Mode: Choose to install the AMQ Streams Operator to a specific (project) namespace <br>
Click Subscribe; the AMQ Streams Operator is installed to your OpenShift cluster.<br>


Verify that the amq-streams-cluster-operator is Running:<br>

```bash
oc get pods
NAME                                                   READY     STATUS    RESTARTS   AGE
amq-streams-cluster-operator-v1.4.0-55f4b48cc6-mhckl   1/1       Running   0          56s
```

#### Install voting microservice

```bash
oc new-app quay.io/bridlos/voting-service-quarkus
oc expose svc/voting-service-quarkus
```

#### Install prometheus and grafana

```bash
wget https://raw.githubusercontent.com/strimzi/strimzi-kafka-operator/0.10.0/metrics/examples/prometheus/kubernetes.yaml
mv kubernetes.yaml prometheus.yaml
oc apply -f prometheus.yaml -n quarkus-voting
oc adm policy add-cluster-role-to-user prometheus -z prometheus-server
wget https://raw.githubusercontent.com/strimzi/strimzi-kafka-operator/0.10.0/metrics/examples/grafana/kubernetes.yaml
mv kubernetes.yaml grafana.yaml
oc apply -f grafana.yaml -n quarkus-voting
oc expose svc/grafana
```

Download and import grafana dashboard for kafka and zookeeper, dashboard can be downloaded at:<br>
wget https://raw.githubusercontent.com/strimzi/strimzi-kafka-operator/master/metrics/examples/grafana/strimzi-kafka.json<br>
wget https://raw.githubusercontent.com/strimzi/strimzi-kafka-operator/master/metrics/examples/grafana/strimzi-zookeeper.json

Follow the instruction to import the kafka and zookeeper grafana dashboards:<br>
https://strimzi.io/docs/latest/#grafana_dashboard

Results will be available at:

```bash
http://<your-host>/voting/results
```

and on a grafana dashboard


### Launch on local env - linux and mac

Launch the bootstrap script to create the docker containers.<br>
Images are downloaded from docker hub and from quay.io.

Images:
 - Postgres (image debezium/postgres) on local port 5432
 - Zookeeper (image debezium/zookeeper) on local port 2181
 - Kafka (image debezium/kafka) on local port 9092
 - Jaeger (image jaegertracing/all-in-one) on local port 16686
 - Voting Service (image quay.io/bridlos/voting-service-quarkus) on local port 8080

```bash
./deploy-docker.sh
```

If you want to run the voting application in dev mode use the script:

```bash
./deploy-docker-no-voting.sh
```

and then run the voting application with:

```bash
cd voting/
./mvnw clean compile quarkus:dev (debug port 5005)
```

To test the application run:
```bash
cd voting/
mvn clean compile test
```


### Endpoints

Test them with swagger, available at:
```bash
http://<your-host>/swagger-ui
http://<your-host>/openapi
```


### Jaeger

See tracing at:<br>
```bash
http://<your-host>:16686
```


### Compile and Create Images

Launch the script to compile and create the native images:

```bash
./build-native-image.sh
```
