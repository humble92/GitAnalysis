GitAnalysis
===========

GitAnalysis is a general-purpose git log analysis program to compare the git activity between multiple projects with sub projects. Initially this program was written as part of the effort to compare the activeness of various open source IaaS projects, including CloudStack, Eucalyptus, OpenNebula, and OpenStack. However, it can be used to carry out git log analysis for any other git projects.

The GitAnalysis program includes the following three parts:

(1) git.sql

This is the SQL script used to create a MySQL database to store the git logs. The best way to use this script is to create a database "git" in MySQL, and then import this script into the "git" database, which will result in a "log" table in the "git" database.

Each record in the "log" table includes the folloiwng information:

  -- main_project, the name of the main project (a main project might include multiple sub-projects)
  -- sub_project, the name of the sub-project
  -- commit, the ID of the commit action
  -- author, the name of the committer
  -- email, the email address of the committer
  -- date, the date of the commit action
  -- domain, which is the domain name of the email address
  
(2) Git.java

This is a Java program that performs the following tasks:

  -- import git logs into the MySQL database from a log file (text format)
  -- carry out statistics within the MySQL database for the number of commits, authors, domains in a certain period

(3) run.sh

This is the script being used to carry of git log analysis for the various open source IaaS projects. It can be used as a piece of sample script, which can be modified and used for ohter git analysis projects.


