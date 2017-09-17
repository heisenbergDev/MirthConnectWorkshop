# Mirth Connect Workshop - Installation

## What we need

Mirth Connect could be installed in a wide variety of platforms (Windows, Linux, OSx,...). For this tutorial, we will install, configure and tuneup it using [Linux Centos 7 64bit](https://www.centos.org) / [PostgreSQL](https://www.postgresql.org) platform.

## Installing basic standalone Mirth Connect Server

This installation process describes the minimum steps to create an standalone Mirth Connect server, using last available versions. Bear in mind that if you need to install older Mirth Connect versions, you need to be sure to use a compatible JRE environment. For more information, please refer to the last [Mirth Connect Manual](https://bridge.nextgen.com/media/3244/mirth-data-sheet-mirth-connect-3-4-user-guide.pdf).
This example uses Centos 7 64bit distro and PostgreSQL, but it can be applied to other similar linux distros and compatible databases with minimum changes.

### Install base software

 1. Get a physical or virtual machine with Centos 7 64bit installed on it:
  * For performance and security reasons, it's highly recommended to install a minimal linux distribution, adding only the services that are really needed.
  * SSH service could be needed for remote server access.
  * Creating a non-superuser user to run Mirth Connect is recommended (you can do it in installation process. e.g. mirth). This user could have "sudoer" privileges to simplify installation and configuration process (instead of using root). `visudo` command can do the trick.
  * After installing, update the server: `sudo yum update`.

 2. Installing Java:
  * Bear in mind that only Oracle JRE is supported. Using last Java version is recommended.
  * Download last Java JRE available [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html):
      * For RedHat/Centos based distributions, the Linux rpm package file can be used.
  * Install the downloaded rpm package: e.g. `sudo rpm -Uvh jre-8u144-linux-x64.rpm`.
  * You can check the installed Java version:
      * `java -version` must show something like this:

        ```
        java version "1.8.0_144"
        Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
        Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
        ```

 3. Installing PostgreSQL:
  * Install the postgresql-server package and the "contrib" package, that adds some additional utilities and functionality:
      * `sudo yum install postgresql-server postgresql-contrib`.
  * Initialize PostgreSQL database:
      * `sudo postgresql-setup initdb`.
  * By default, PostgreSQL does not allow password authentication. To change this, open the HBA configuration with a text editor:
      * `sudo vi /var/lib/pgsql/data/pg_hba.conf`.
      * Change from this:

        ```
        host    all    all     127.0.0.1/32     ident
        host    all    all     ::1/128          ident
        ```

        * To this:

        ```
        host    all    all     127.0.0.1/32     md5
        host    all    all     ::1/128          md5
        ```

  * Now start and enable PostgreSQL:
      * `sudo systemctl start postgresql`.
      * `sudo systemctl enable postgresql`.

 4. Install Mirth Connect:
  * Download last Mirth Connect version from [here](https://www.mirth.com/Downloads). For RedHat/Centos based distributions, the Linux rpm package file can be used.
  * Install rpm Mirth connect package with a command like this:
      * `sudo rpm -Uvh mirthconnect-3.5.0.8232.b2153-linux.rpm`
  * Open firewall rules for Mirth Connect default ports (this default ports could be changed in mirth.properties file):

      ```
      sudo firewall-cmd --zone=public --add-port=8080/tcp --permanent
      sudo firewall-cmd --zone=public --add-port=8443/tcp --permanent
      sudo firewall-cmd --reload
      ```
  * After this, a new Mirth Connect instance is available in /opt/mirthconnect folder; but it's not ready to work (we want to use it with PostgreSQL database, not default Apache Derby database).

### Configuring Mirth Connect instance

 1. Creating Mirth database (using PostgreSQL DBMS). Mirth Connect needs a database to store its data and messages. In this example, we're going to use PostgreSQL database; but other DBMS are ready to work with Mirth Connect (for more information, refer to [Mirth Connect Manual](https://bridge.nextgen.com/media/3244/mirth-data-sheet-mirth-connect-3-4-user-guide.pdf)).
    * We are going to create a "mirthdb" user on the database:

        ```
        sudo -u postgres createuser -d -R -P mirthdb
        Ingrese la contraseña para el nuevo rol:
        Ingrésela nuevamente:
        ```

    * Create a database named "mirthdb":
        * `sudo -u postgres createdb -O mirthdb mirthdb`.

    * Tuneup PostgreSQL; edit `sudo vi /var/lib/pgsql/data/postgresql.conf` and add the following parameters:

        ```
        autovacuum=on
        autovacuum_vacuum_threshold=1000
        ```

 2. Configure database connection in Mirth Connect.
    * Edit "mirth.properties": `sudo vi /opt/mirthconnect/conf/mirth.properties`.
        * Change from this:

        ```
        # options: derby, mysql, postgres, oracle, sqlserver
        database = derby

        database.url = jdbc:derby:${dir.appdata}/mirthdb;create=true

        # database credentials
        database.username =
        database.password =
        ```

        * To this:

        ```
        # options: derby, mysql, postgres, oracle, sqlserver
        database = postgres

        database.url = jdbc:postgresql://localhost:5432/mirthdb

        # database credentials
        database.username = mirthdb
        database.password = xxxxxxx
        ```
 3. Tuneup Mirth Connect parameters. This configuration process will depend of the amount of RAM available on the server, other process running on it, etc...this example is calcualted with a 2Gb RAM server and PostgreSQL in the same machine.
    * Edit "mirth.properties": `sudo vi /opt/mirthconnect/mcservice.vmoptions`.
        * Change from this:

        ```
        -server
        -Xmx256m
        -Djava.awt.headless=true
        -Dapple.awt.UIElement=true
        ```

        * To this:
        ```
        -server
        -Xms512m
        -Xmx1024m
        -Djava.awt.headless=true
        -Dapple.awt.UIElement=true
        ```

 Restart the server (after all this changes to load new configuration parameters):
    * To restart all the server: `sudo restart` (recommended).
    * To restart only Postgresql service: `sudo systemctl restart postgresql`.
    * To restart only Mirth Connect: `sudo systemctl restart mcservice`.
