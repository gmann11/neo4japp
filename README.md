# Neo4J Example

Neo4J exercise

## Getting Started

Follow the instructions to get up and running.

### Prerequisites

* Apache Maven
* Neo4J Server Community Edition

### Installing

Follow these steps to install:
```
mkdir /opt/tools
cd /opt/tools
wget http://apache.spinellicreations.com/maven/maven-3/3.5.3/binaries/apache-maven-3.5.3-bin.zip
unzip apache-maven-3.5.3-bin.zip
sudo wget http://dist.neo4j.org/neo4j-community-3.3.3-unix.tar.gz
tar xvf neo4j-community-3.3.3-unix.tar.gz
sudo yum install git
sudo yum install java-1.8.0
sudo yum install java-1.8.0-openjdk-devel
sudo /usr/sbin/alternatives --config java
```
Configure Neo4J
```
cd <heo4jhome>/bin
./neo4j-admin set-initial-password password
nohup ./neo4j start &
./cypher-shell
CREATE CONSTRAINT ON (emp:Employee) ASSERT emp.emp_id IS UNIQUE;
:exit
```
Build the App
```
git clone https://github.com/gmann11/neo4japp.git
/opt/tools/apache-maven-3.5.3/bin/mvn install
nohup java -jar target/Neo4JApp-1.0.jar &
```

### Test API
Create an Employee
```
curl -i -v -H "Content-Type: application/json" -X POST -d '{"empid":1,"name":"hugo lloris"}' http://localhost:8080/employee
```

List All Employees
```
curl -i -v -H "Content-Type: application/json" http://localhost:8080/employees
```

Get a Specific Employee
```
curl -i -v -H "Content-Type: application/json" http://localhost:8080/employee/1
```

Delete an Employee
```
curl -i -v -H "Content-Type: application/json" http://localhost:8080/del/1
```
## Built With
* [SparkJava](http://sparkjava.com/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Bootstrap](https://getbootstrap.com/) - Front-end
* [JQuery](https://jquery.com/) - JS 
