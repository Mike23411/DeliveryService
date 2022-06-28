A5 Repo for CS6310 Fall 2021
# To Install Docker go to:
```
https://docs.docker.com/get-docker/
```

#Important Information
The only real dependencies are an up to date version of docker, python3, and an internet connection.

Once docker-compose up is executed, the sql DB is reachable through port 33061.

The username is root, and the password is password. MySQL Workbench or any other mysql client
will allow you to inspect and run queries on the Database. 

The database is stored on the host system in the folder db_data via a bind mount from docker compose.
Within the db_data folder is the DeliveryService folder, which will contain archived data. In order to execute the
archivability functionality, we will need to run the command "clean_up". 

The python client can be accessed via a separate terminal and accepts all commands from the requirements either via command line,
or file as seen below. Multiple files can be fed in.

Examples of test files that can be ran as follows are inside of {PROJECT_ROOT}/test_scenarios/:
``` 
python client.py ./test_scenarios/test_file.txt
```

It is easy to run multiple clients in a script at once. An Example:

``` 
python client.py ./test_scenarios/test_file.txt &
python client.py ./test_scenarios/test_file2.txt &
python client.py ./test_scenarios/test_file3.txt &
```

# Note please run all scripts from the project root directory

### To build and run:

```
docker-compose up --build
```

### To test with the command line:

```
python3 client.py
```

### To test with a file:

```
python3 client.py file1 [file2] [file3] ...
```

### To stop docker-compose and it's containers

```
docker-compose down
```

### To reset the Database

While in project root and docker-compose is down:
```
rm db_data
```

### To clean all the containers after docker compose is down

```
docker ps -aq | % { docker stop $_ } | % { docker rm -f $_ } | docker images -f "dangling=true" -aq | % { docker rmi -f $_ } | docker images gatech/* -aq | % { docker rmi -f $_ }
```

### To access the running java container
```
docker container ls
```

Select the container that ends in _web
```
docker run -ti {CurrentDirectoryNameLowerCase}_web bash
```

### To test with a clean image & container
After running the below command you will need to run the build command again
```
docker ps -aq | % { docker stop $_ } | % { docker rm -f $_ } | docker images -f "dangling=true" -aq | % { docker rmi -f $_ } | docker images gatech/* -aq | % { docker rmi -f $_ }
```

### To kill all connections
In case the connections need to be killed / restarted the following command will need to be executed in mySQL client
```
show full processlist;
```
From here, note down the process id that is currently listening to the ports 5000 and 6000.
```
KILL <pid>;
```
As an example if pid 50 is using the above mentioned ports, we would need to run the below command to close the connections.
```
KILL 50;
```
