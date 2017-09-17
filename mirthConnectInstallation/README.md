# Mirth Connect Workshop - Installation

## What we need

Mirth Connect could be installed in a wide variety of platforms (Windows, Linux, OSx,...). For this tutorial, we will install, configure and tuneup it using [Linux Centos 7 64bit](https://www.centos.org) / [PostgreSQL](https://www.postgresql.org) platform.

## Installing basic standalone Mirth Connect Server

This installation process describes the minimum steps to create an standalone Mirth Connect server, using last available versions. Bear in mind that if you need to install older Mirth Connect versions, you need to be sure to use a compatible JRE environment. For more information, please refer to the last [Mirth Connect Manual](https://bridge.nextgen.com/media/3244/mirth-data-sheet-mirth-connect-3-4-user-guide.pdf).
This example uses Centos 7 64bit distro and PostgreSQL, but it can be applied to other similar linux distros and compatible databases with minimum changes.

### Install base software

 1. Get a physical or virtual machine with Centos 7 64bit installed on it:
  * For performance and security reasons, it's highly recommended to install a minimal linux distribution, adding only the services that are needed.
  * SSH service could be needed for remote server access.
  * After installing, update the server: `sudo yum update`.

 2. Installing Java:
  * Bear in mind that only Oracle JRE is supported. Using last Java version is recommended.
  * Download last Java JRE available [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html):
      * For RedHat/Centos based distributions, the Linux rpm package file can be used.
  * Install the downloaded RPM package: e.g. `sudo rpm -Uvh jre-8u144-linux-x64.rpm`.
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
  * Create a new PostgreSQL database cluster:
      * `sudo postgresql-setup initdb`.
  * By default, PostgreSQL does not allow password authentication. To change this, open the HBA configuration with your favorite text editor:
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
