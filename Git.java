import java.net.*;
import java.io.*;
import java.sql.*;
import java.text.*;

public class Git
{

	/*
	 *
	 * DB Connection Parameters
	 * Needless to say, you will have to modified these parameters otherwise nothing will work.
	 *
	 */
	 
	String db_hostname = "localhost", db_username = "root", db_password = "root", db_database = "git";
	Connection db_connect;

	// Dates
	String[] dates = {
		"2001-01-01", "2001-02-01", "2001-03-01", "2001-04-01", "2001-05-01", "2001-06-01", "2001-07-01", "2001-08-01", "2001-09-01", "2001-10-01", "2001-11-01", "2001-12-01", 
		"2002-01-01", "2002-02-01", "2002-03-01", "2002-04-01", "2002-05-01", "2002-06-01", "2002-07-01", "2002-08-01", "2002-09-01", "2002-10-01", "2002-11-01", "2002-12-01", 
		"2003-01-01", "2003-02-01", "2003-03-01", "2003-04-01", "2003-05-01", "2003-06-01", "2003-07-01", "2003-08-01", "2003-09-01", "2003-10-01", "2003-11-01", "2003-12-01", 
		"2004-01-01", "2004-02-01", "2004-03-01", "2004-04-01", "2004-05-01", "2004-06-01", "2004-07-01", "2004-08-01", "2004-09-01", "2004-10-01", "2004-11-01", "2004-12-01", 
		"2005-01-01", "2005-02-01", "2005-03-01", "2005-04-01", "2005-05-01", "2005-06-01", "2005-07-01", "2005-08-01", "2005-09-01", "2005-10-01", "2005-11-01", "2005-12-01", 
		"2006-01-01", "2006-02-01", "2006-03-01", "2006-04-01", "2006-05-01", "2006-06-01", "2006-07-01", "2006-08-01", "2006-09-01", "2006-10-01", "2006-11-01", "2006-12-01", 
		"2007-01-01", "2007-02-01", "2007-03-01", "2007-04-01", "2007-05-01", "2007-06-01", "2007-07-01", "2007-08-01", "2007-09-01", "2007-10-01", "2007-11-01", "2007-12-01", 
		"2008-01-01", "2008-02-01", "2008-03-01", "2008-04-01", "2008-05-01", "2008-06-01", "2008-07-01", "2008-08-01", "2008-09-01", "2008-10-01", "2008-11-01", "2008-12-01", 
		"2009-01-01", "2009-02-01", "2009-03-01", "2009-04-01", "2009-05-01", "2009-06-01", "2009-07-01", "2009-08-01", "2009-09-01", "2009-10-01", "2009-11-01", "2009-12-01", 
		"2010-01-01", "2010-02-01", "2010-03-01", "2010-04-01", "2010-05-01", "2010-06-01", "2010-07-01", "2010-08-01", "2010-09-01", "2010-10-01", "2010-11-01", "2010-12-01", 
		"2011-01-01", "2011-02-01", "2011-03-01", "2011-04-01", "2011-05-01", "2011-06-01", "2011-07-01", "2011-08-01", "2011-09-01", "2011-10-01", "2011-11-01", "2011-12-01", 
		"2012-01-01", "2012-02-01", "2012-03-01", "2012-04-01", "2012-05-01", "2012-06-01", "2012-07-01", "2012-08-01", "2012-09-01", "2012-10-01", "2012-11-01", "2012-12-01",
		"2013-01-01", "2013-02-01", "2013-03-01", "2013-04-01", "2013-05-01", "2013-06-01", "2013-07-01", "2013-08-01", "2013-09-01", "2013-10-01", "2013-11-01", "2013-12-01",
		"2014-01-01", "2014-02-01", "2014-03-01", "2014-04-01", "2014-05-01", "2014-06-01", "2014-07-01", "2014-08-01", "2014-09-01", "2014-10-01", "2014-11-01", "2014-12-01",
		"2015-01-01", "2015-02-01", "2015-03-01", "2015-04-01", "2015-05-01"};

	// OpenStack Projects		 
	String[] openstack_projects = {"openstack", "glance", "ceilometer", "cinder-specs", "heat", "gnocchi", "keystone",  "nova", "horizon", "python-neutronclient", "nova-specs", "trove", "oslo.messaging",  "openstackdocstheme", "magnum", "os-brick", "python-openstackclient", "neutron", "trove-integration",  "sahara", "security-doc", "api-site", "ironic", "barbican", "openstack-manuals",  "django_openstack_auth", "cinder", "python-magnumclient", "tripleo-heat-templates", "swift", "neutron-lbaas",  "diskimage-builder", "tempest", "neutron-specs", "manila", "oslo.middleware", "oslo-specs",  "zaqar", "ha-guide", "designate", "operations-guide", "defcore", "tempest-lib",  "requirements", "os-testr", "ironic-python-agent", "taskflow", "python-ironicclient", "governance",  "python-novaclient", "keystonemiddleware", "os-net-config", "sahara-image-elements", "tuskar-ui", "ceilometer-specs",  "tripleo-puppet-elements", "python-glanceclient", "python-cinderclient", "python-manilaclient", "tripleo-image-elements", "openstack-doc-tools",  "oslo-incubator", "python-ceilometerclient", "heat-specs", "ironic-specs", "python-heatclient", "zaqar-specs",  "tripleo-incubator", "heat-templates", "python-saharaclient", "python-keystoneclient", "oslo.policy", "tooz",  "oslo.rootwrap", "python-barbicanclient", "glance_store", "heat-translator", "training-guides", "keystone-specs",  "designate-specs", "ironic-lib", "glance-specs", "oslo.db", "trove-specs", "neutron-fwaas",  "python-swiftclient", "oslo.serialization", "api-wg", "openstack-specs", "gantt", "neutron-vpnaas",  "tuskar", "ceilometermiddleware", "oslo.config", "tripleo-specs", "sahara-extra", "oslo.vmware",  "qa-specs", "castellan", "oslo.versionedobjects",  "swift-specs", "os-client-config", "oslo.utils", "oslo.concurrency", "oslotest", "oslo.log",  "coreos-image-builder", "docs-specs", "oslo.i18n", "python-zaqarclient", "oslo.context", "python-troveclient",  "sahara-specs", "python-designateclient", "openstack-planet", "sahara-dashboard", "pycadf", "debtcollector",  "stevedore", "python-keystoneclient-kerberos", "python-keystoneclient-federation", "os-collect-config", "os-cloud-config", "cliff",  "python-tuskarclient", "dib-utils", "os-apply-config", "ossa", "oslosphinx", "barbican-specs",  "heat-cfntools", "pylockfile", "python-kiteclient", "kite", "os-refresh-config", "swift-bench",  "python-ganttclient"};
	// OpenStack Infrastructure Projects
	String[] openstack_infra_projects = {"gerrit", "project-config", "jenkins-job-builder", "system-config", "zuul",  "os-loganalyze", "git-review", "nodepool", "puppet-openstackci", "elastic-recheck", "bindep",  "shade", "devstack-gate", "reviewstats", "groups-static-pages", "puppet-zuul", "tripleo-ci",  "subunit2sql", "storyboard-webclient", "infra-manual", "puppet-elasticsearch", "activity-board",  "jeepyb", "puppet-nodepool", "storyboard", "groups", "puppet-zanata", "python-storyboardclient",  "puppet-jenkins", "puppet-pip", "puppet-cgit", "infra-specs", "gear", "puppet-jeepyb",  "puppet-ssh", "puppet-graphite", "openstackid",  "ansible-puppet", "puppet-askbot", "puppet-bandersnatch", "puppet-puppet", "puppet-kibana", "puppet-subunit2sql",  "puppet-user", "gerritlib", "puppet-gerrit", "puppet-httpd", "release-tools", "puppet-etherpad_lite",  "puppet-storyboard", "zmq-event-publisher", "puppet-openstackid", "puppet-logstash", "puppet-project_config", "puppet-ansible",  "publications", "puppet-log_processor", "puppet-drupal", "lodgeit", "puppet-mailman", "yaml2ical",  "gerritbot", "irc-meetings", "gitdm", "pypi-mirror", "puppet-apparmor", "puppet-bup",  "puppet-meetbot", "puppet-unattended_upgrades", "puppet-unbound", "puppet-ulimit", "puppet-vinz", "puppet-snmpd",  "puppet-sudoers", "puppet-statusbot", "puppet-asterisk", "puppet-elastic_recheck", "puppet-tmpreaper", "puppet-redis",  "puppet-yum", "puppet-vcsrepo", "puppet-openafs", "puppet-mysql_backup", "puppet-mediawiki", "puppet-logrotate",  "puppet-lodgeit", "puppet-kerberos", "puppet-iptables", "puppet-haveged", "puppet-github", "puppet-gerritbot",  "puppet-exim", "puppet-bugdaystats", "puppet-dashboard", "puppet-accessbot", "puppet-packagekit",  "puppet-reviewday", "puppet-releasestatus", "puppet-ssl_cert_check", "puppet-planet", "puppet-simpleproxy", "gearman-plugin",  "openstackweb", "askbot-theme", "nose-html-output", "reviewday", "releasestatus", "vinz-webclient",  "vinz", "bugdaystats", "statusbot",  "odsreg", "meetbot", "zuul-packaging"};
	// OpenNebula, Eucalyptus, CloudStack projects
	String[] opennebula_projects = {"one"};
	String[] eucalyptus_projects = {"eucalyptus"};
	String[] cloudstack_projects = {"cloudstack"};
	

	/**
	 *
	 * Create DB Connection
	 *
	 */
	
	public void open_db_connection()
	{
		if (db_connect == null)
		{
			try
			{
				// MySQL Driver
				Class.forName("com.mysql.jdbc.Driver");
				// DB URI
				String conn_string = "jdbc:mysql://" + db_hostname + "/" + db_database 
					+ "?user=" + db_username + "&password=" + db_password;
				// Create DB Connection
				db_connect = DriverManager.getConnection(conn_string);
			} catch (Exception e)
			{
				// Exception Handling
				db_connect = null;
				
				System.out.println(e.getMessage());
			}
		}
	}

	/*
	 *
	 * Clean up the whole git table
	 *
	 */

	public void clean_up_table()
	{
		// Check DB Connection 
		if (db_connect == null) 
		{
			open_db_connection();
		}
		// Truncate Table
		if (db_connect != null)
		{
			try
			{
				String command;
				PreparedStatement statement;
					
				command = "TRUNCATE TABLE logs";
				statement = db_connect.prepareStatement(command);
				statement.executeUpdate();
			} catch (Exception e)
			{
				db_connect = null;
			}
		}				
	}
	
	
	/*
	 *
	 * Clean up record for a main project or a sub project inside a main project. 
	 *
	 */

	public void clean_up_record(String main_project, String sub_project)
	{
		// Check DB Connection 
		if (db_connect == null) 
		{
			open_db_connection();
		}
		// Truncate Table
		if (db_connect != null)
		{
			try
			{
				String command;
				PreparedStatement statement;
					
				if (main_project.equals(sub_project))
				{
					command = "DELETE FROM logs WHERE main_project = '" + main_project + "'";
					statement = db_connect.prepareStatement(command);
					statement.executeUpdate();
				}
				else
				{
					command = "DELETE FROM logs WHERE main_project = '" + main_project + "' AND sub_project = '" + sub_project + "'";
					statement = db_connect.prepareStatement(command);
					statement.executeUpdate();					
				}
			} catch (Exception e)
			{
				db_connect = null;
			}
		}				
	}
		
	public void insert_data(String main_project, String sub_project, String commit, String author, String email, String domain, String date)
	{
		// Check DB Connection 
		if (db_connect == null) 
		{
			open_db_connection();
		}
		// Truncate Table
		if (db_connect != null)
		{
			try
			{
				PreparedStatement statement;
					
				// Prepare SQL Commands
				String sql = "INSERT INTO logs (main_project, sub_project, commit, author, email, domain, date) VALUES ("
					+ "'" + main_project + "', " + "'" + sub_project + "', '" + commit + "', '" + author + "', '" + email + "', '" + domain + "', '" + date + "'" + ")";
				// Execute SQL Commands
				statement = db_connect.prepareStatement(sql);
				statement.executeUpdate();
			} catch (Exception e)
			{
				db_connect = null;
			}
		}		
		
	}
	
	
	/**
	 * 
	 * IBM contributors have email addresses with subdomains, such as cn.ibm.cn, us.ibm.com.
	 * Need to fix them and treat as ibm.com.
	 *
	 * Also fix the same issue for other projects.
	 */
	 
	public void fix_multiple_domains()
	{
		// Check DB Connection 
		if (db_connect == null) 
		{
			open_db_connection();
		}
		// Truncate Table
		if (db_connect != null)
		{
			try
			{
				String command;
				PreparedStatement statement;
					
				// Consolidate many IBM domains
				command = "UPDATE logs SET domain = 'ibm.com' WHERE domain like '%ibm.com%'";
				statement = db_connect.prepareStatement(command);
				statement.executeUpdate();

				// Consolidate many OpenNebula domains
				command = "UPDATE logs SET domain = 'ucm.es' WHERE domain like '%ucm.es%' AND main_project='OpenNebula'";
				statement = db_connect.prepareStatement(command);
				statement.executeUpdate();
				command = "UPDATE logs SET domain = 'ucm.es' WHERE domain like '%.(none)' AND main_project='OpenNebula'";
				statement = db_connect.prepareStatement(command);
				statement.executeUpdate();
				command = "UPDATE logs SET domain = 'ucm.es' WHERE domain like 'Daniel-Molinas-MacBook-Pro.local' AND main_project='OpenNebula'";
				statement = db_connect.prepareStatement(command);
				statement.executeUpdate();

				// Consolidate internation workstations within Eucalyptus Systems Inc.
				command = "UPDATE logs SET domain = 'eucalyptus.com' WHERE domain like '%euca%'";
				statement = db_connect.prepareStatement(command);
				statement.executeUpdate();
				command = "UPDATE logs SET domain = 'eucalyptus.com' WHERE domain not like '%.%' AND main_project='Eucalyptus'";
				statement = db_connect.prepareStatement(command);
				statement.executeUpdate();
				command = "UPDATE logs SET domain = 'eucalyptus.com' WHERE domain='localhost.localdomain' AND main_project='Eucalyptus'";
				statement = db_connect.prepareStatement(command);
				statement.executeUpdate();
				command = "UPDATE logs SET domain = 'eucalyptus.com' WHERE domain like '%.(none)' AND main_project='Eucalyptus'";
				statement = db_connect.prepareStatement(command);
				statement.executeUpdate();
			} catch (Exception e)
			{
				db_connect = null;
			}
		}				
	}
	

	/*
	 *
	 * Count commits, authors, domains for either a main project or a sub project between start date and end date. 
	 *
	 */

	public int count_activity(String type, String main_project, String sub_project, String start, String end)
	{
		String sql = null;
		int count;
		
		if (main_project.equals(sub_project))
		{
			if (type.equals("commits"))
			{
				sql = "SELECT count(*) AS count FROM logs WHERE main_project = '" + main_project + "' AND date >= '" + start + "' AND date < '" + end + "'";
			}
			else if (type.equals("authors"))
			{
				sql = "SELECT count(DISTINCT author) AS count FROM logs WHERE main_project = '" + main_project + "' AND date >= '" + start + "' AND date < '" + end + "'";
			}
			else if (type.equals("domains"))
			{
				sql = "SELECT count(DISTINCT domain) AS count FROM logs WHERE main_project = '" + main_project + "' AND date >= '" + start + "' AND date < '" + end + "'";
			}			
		}
		else
		{
			if (type.equals("commits"))
			{
				sql = "SELECT count(*) AS count FROM logs WHERE main_project = '" + main_project + "' AND sub_project = '" + sub_project + "' AND date >= '" + start + "' AND date < '" + end + "'";
			}
			else if (type.equals("authors"))
			{
				sql = "SELECT count(DISTINCT author) AS count FROM logs WHERE main_project = '" + main_project + "' AND sub_project = '" + sub_project + "' AND date >= '" + start + "' AND date < '" + end + "'";
			}
			else if (type.equals("domains"))
			{
				sql = "SELECT count(DISTINCT domain) AS count FROM logs WHERE main_project = '" + main_project + "' AND sub_project = '" + sub_project + "' AND date >= '" + start + "' AND date < '" + end + "'";
			}
		}
		// Check DB Connection 
		if (db_connect == null) 
		{
			open_db_connection();
		}

		// Query DB
		if (db_connect != null)
		{
			try
			{
				PreparedStatement stat = db_connect.prepareStatement(sql);
				ResultSet results = stat.executeQuery();
				if (results.next())
				{
					count = results.getInt("count");
					return count;
				}
			} catch (Exception e)
			{
				System.out.println(e.getMessage());
				System.out.println(e.getStackTrace());
			}
		}
		
		return 0;
	}


	/*
	 *
	 * Count contributing domains for either a main project or a sub project between start date and end date. 
	 *
	 */

	public void count_contribution(String main_project, String sub_project, String start, String end)
	{
		String sql = null, sql_total=null;
		int count_total=1, count, percentage;
		String domain;
		
		if (main_project.equals(sub_project))
		{
			sql_total =  "SELECT count(*) AS count FROM logs WHERE main_project = '" + main_project + "' AND date >= '" + start + "' AND date < '" + end + "'";	
						
			sql = "SELECT count(*) AS count, domain FROM logs WHERE main_project = '" + main_project + "' AND date >= '" + start + "' AND date < '" + end + "' GROUP BY domain ORDER BY count DESC LIMIT 80";	
		}
		else
		{
			sql_total =  "SELECT count(*) AS count FROM logs WHERE sub_project = '" + sub_project + "' AND date >= '" + start + "' AND date < '" + end + "'";	
						
			sql = "SELECT count(*) AS count, domain FROM logs WHERE sub_project = '" + sub_project + "' AND date >= '" + start + "' AND date < '" + end + "' GROUP BY domain ORDER BY count DESC LIMIT 80";				
		}

		// Check DB Connection 
		if (db_connect == null) 
		{
			open_db_connection();
		}

		// Query DB
		if (db_connect != null)
		{
			try
			{
				PreparedStatement stat_1 = db_connect.prepareStatement(sql_total);
				ResultSet results_1 = stat_1.executeQuery();
				if (results_1.next())
				{
					count_total = results_1.getInt("count");
					System.out.println("Total:    " + count_total);
				}

				PreparedStatement stat = db_connect.prepareStatement(sql);
				ResultSet results = stat.executeQuery();
				while (results.next())
				{
					count = results.getInt("count");
					percentage = (int) (100 * count / count_total);
					domain = results.getString("domain");
					System.out.println(domain + "    " + count + "    " + percentage);
				}
			} catch (Exception e)
			{
				System.out.println(e.getMessage());
				System.out.println(e.getStackTrace());
			}
		}
		
	}


	/*
	 *
	 * Import the log file into the MySQL database for a particular sub project.
	 *
	 */

	public void import_log(String main_project, String project)
	{
		String commit, author, email, domain, date;
		String year, month, day; 
		int pos_1, pos_2;
		SimpleDateFormat DF = new SimpleDateFormat("MMM d' 'HH:mm:ss yyyy");
		
		try
		{
			FileInputStream fstream = new FileInputStream(project + "/log.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
    	    String inputLine;
    	    while ((inputLine = in.readLine()) != null) 
			{
				if (inputLine.indexOf("commit")==0)
				{
					commit = inputLine.substring(7).trim();
					
					// Get Author line
					inputLine = in.readLine();
					while (inputLine.indexOf("Author:")==-1)
					{
						inputLine = in.readLine();
					}
					pos_1 = inputLine.indexOf("<");
					pos_2 = inputLine.indexOf(">");
					author = inputLine.substring(7, pos_1).trim();
					email = inputLine.substring(pos_1+1, pos_2).trim();
					domain = email.substring(email.indexOf('@')+1);
					
					// Get Date line
					inputLine = in.readLine();
					while (inputLine.indexOf("Date:")==-1)
					{
						inputLine = in.readLine();
					}
					date = inputLine.substring(12).trim();
					date = date.substring(0, date.length()-5).trim();
					month = date.substring(0, 3);
					date = date.substring(4).trim();
					day = date.substring(0, date.indexOf(' ')).trim();
					year = date.substring(date.length()-4);
					if (month.equals("Jan"))
					{
						date = year + "-01-" + day;
					}
					else if  (month.equals("Feb"))
					{
						date = year + "-02-" + day;
					}
					else if  (month.equals("Mar"))
					{
						date = year + "-03-" + day;
					}
					else if  (month.equals("Apr"))
					{
						date = year + "-04-" + day;
					}
					else if  (month.equals("May"))
					{
						date = year + "-05-" + day;
					}
					else if  (month.equals("Jun"))
					{
						date = year + "-06-" + day;
					}
					else if  (month.equals("Jul"))
					{
						date = year + "-07-" + day;
					}
					else if  (month.equals("Aug"))
					{
						date = year + "-08-" + day;
					}
					else if  (month.equals("Sep"))
					{
						date = year + "-09-" + day;
					}
					else if  (month.equals("Oct"))
					{
						date = year + "-10-" + day;
					}
					else if  (month.equals("Nov"))
					{
						date = year + "-11-" + day;
					}
					else if  (month.equals("Dec"))
					{
						date = year + "-12-" + day;
					}
					
					System.out.println(commit + " : " + author);
					insert_data(main_project, project, commit, author, email, domain, date);
				}
			}
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
			
	}
	
	
	/*
	 *
	 * Count accumulated population and accumulated domain. 
	 *
	 */

	public void count(String type)
	{
		
		int cs=0, eu=0, on=0, os=0;
		
		if (type.equals("accumulated_population"))
		{
			for (int i=dates.length - 1; i>=1; i--)
			{
				cs = count_activity("authors", "CloudStack", "CloudStack", "2009-01-01", dates[i]);
				eu = count_activity("authors", "Eucalyptus", "Eucalyptus", "2009-01-01", dates[i]);
				on = count_activity("authors", "OpenNebula", "OpenNebula", "2009-01-01", dates[i]);
				os = count_activity("authors", "OpenStack",  "OpenStack",  "2009-01-01", dates[i]);
				System.out.println(dates[i] + "   " + os + "   " + on + "   " + eu + "   " + cs);
			}				
		}
		else if (type.equals("accumulated_domain"))
		{
			for (int i=dates.length - 1; i>=1; i--)
			{
				cs = count_activity("domains", "CloudStack", "CloudStack", "2009-01-01", dates[i]);
				eu = count_activity("domains", "Eucalyptus", "Eucalyptus", "2009-01-01", dates[i]);
				on = count_activity("domains", "OpenNebula", "OpenNebula", "2009-01-01", dates[i]);
				os = count_activity("domains", "OpenStack",  "OpenStack",  "2009-01-01", dates[i]);
				System.out.println(dates[i] + "   " + os + "   " + on + "   " + eu + "   " + cs);
			}				
		}
		else
		{
			for (int i=dates.length - 1; i>=1; i--)
			{
				cs = count_activity(type, "CloudStack", "CloudStack", dates[i-1], dates[i]);
				eu = count_activity(type, "Eucalyptus", "Eucalyptus", dates[i-1], dates[i]);
				on = count_activity(type, "OpenNebula", "OpenNebula", dates[i-1], dates[i]);
				os = count_activity(type, "OpenStack",  "OpenStack",  dates[i-1], dates[i]);
				System.out.println(dates[i] + "   " + os + "   " + on + "   " + eu + "   " + cs);				
			}	
		}
	}	

	/*
	 *
	 * Count OpenStack sub project metrics (such as commits, authors, domains). 
	 *
	 */

	public void count_openstack(String type)
	{
		
		int cinder=0, glance=0, horizon=0, keystone=0, nova=0, neutron=0, swift=0;

			for (int i=dates.length - 1; i>=1; i--)
			{
				cinder 		= count_activity(type, "OpenStack", "cinder", dates[i-1], dates[i]);
				glance 		= count_activity(type, "OpenStack", "glance", dates[i-1], dates[i]);
				horizon 	= count_activity(type, "OpenStack", "horizon", dates[i-1], dates[i]);
				keystone 	= count_activity(type, "OpenStack", "keystone",  dates[i-1], dates[i]);
				nova 		= count_activity(type, "OpenStack", "nova", dates[i-1], dates[i]);
				neutron 	= count_activity(type, "OpenStack", "neutron", dates[i-1], dates[i]);
				swift 		= count_activity(type, "OpenStack", "swift",  dates[i-1], dates[i]);
				System.out.println(dates[i] + "   " + cinder + "   " + glance + "   " + horizon + "   " + keystone + "   " + nova + "   " + neutron + "   " + swift);
				
			}	
	}	
	
	
	/*
	 *
	 * Traverse the sub projects (as defined in the arrays at the very beginning) and do the following:
	 *
	 * (1) git clone the project 
	 * (2) git log to generate the log file
	 * (3) import the log file into MySQL database
	 *
	 */

	public void fetch_git_raw_data()
	{
	
		String del_command=null, git_command=null, log_command=null, line=null;
		PrintStream original = System.out;
		
		try
		{
			// Clean Up Table
			System.out.println("Cleaning up database...");
			clean_up_table();

			// OpenStack Main Projects
			Runtime rt = Runtime.getRuntime();
			Process proc;
			BufferedReader in;
			
			// OpenStack Projects			
			for (int i=0; i<openstack_projects.length; i++)
			{
				del_command = "rm -Rf " + openstack_projects[i];
				System.out.println("Executing: " + del_command);
				proc = rt.exec(del_command);
				proc.waitFor();
	
				git_command = "git clone https://github.com/openstack/" + openstack_projects[i];
				System.out.println("Executing: " + git_command);
				proc = rt.exec(git_command);
				proc.waitFor();
				
				log_command = "git --git-dir=" + openstack_projects[i] + "/.git log --no-merges";
				System.out.println("Executing: " + log_command);
				System.setOut(new PrintStream(new FileOutputStream(openstack_projects[i] + "/log.txt")));
				proc = rt.exec(log_command);
				in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while ((line = in.readLine()) != null) 
				{
					System.out.println(line);
				}
				in.close();
				System.setOut(original);
				
				System.out.println("Importing log into database for project " + openstack_projects[i]);
				import_log("OpenStack", openstack_projects[i]);
			}
	
			// OpenStack Infrastructure Projects
			for (int i=0; i<openstack_infra_projects.length; i++)
			{
				del_command = "rm -Rf " + openstack_infra_projects[i];
				System.out.println("Executing: " + del_command);
				proc = rt.exec(del_command);
				proc.waitFor();
	
				git_command = "git clone https://github.com/openstack-infra/" + openstack_infra_projects[i];
				System.out.println("Executing: " + git_command);
				proc = rt.exec(git_command);
				proc.waitFor();
				
				log_command = "git --git-dir=" + openstack_infra_projects[i] + "/.git log --no-merges";
				System.out.println("Executing: " + log_command);
				System.setOut(new PrintStream(new FileOutputStream(openstack_infra_projects[i] + "/log.txt")));
				proc = rt.exec(log_command);
				in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while ((line = in.readLine()) != null) 
				{
					System.out.println(line);
				}
				in.close();
				System.setOut(original);
				
				System.out.println("Importing log into database for project " + openstack_infra_projects[i]);
				import_log("OpenStack", openstack_infra_projects[i]);
			}
					
			// Eucalyptus Projects
			for (int i=0; i<eucalyptus_projects.length; i++)
			{
				del_command = "rm -Rf " + eucalyptus_projects[i];
				System.out.println("Executing: " + del_command);
				proc = rt.exec(del_command);
				proc.waitFor();
	
				git_command = "git clone https://github.com/eucalyptus/" + eucalyptus_projects[i];
				System.out.println("Executing: " + git_command);
				proc = rt.exec(git_command);
				proc.waitFor();
				
				log_command = "git --git-dir=" + eucalyptus_projects[i] + "/.git log --no-merges";
				System.out.println("Executing: " + log_command);
				System.setOut(new PrintStream(new FileOutputStream(eucalyptus_projects[i] + "/log.txt")));
				proc = rt.exec(log_command);
				in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while ((line = in.readLine()) != null) 
				{
					System.out.println(line);
				}
				in.close();
				System.setOut(original);
				
				System.out.println("Importing log into database for project " + eucalyptus_projects[i]);
				import_log("Eucalyptus", eucalyptus_projects[i]);
			}
			

			// CloudStack Projects
			for (int i=0; i<cloudstack_projects.length; i++)
			{
				del_command = "rm -Rf " + cloudstack_projects[i];
				System.out.println("Executing: " + del_command);
				proc = rt.exec(del_command);
				proc.waitFor();
	
				git_command = "git clone https://git-wip-us.apache.org/repos/asf/" + cloudstack_projects[i];
				System.out.println("Executing: " + git_command);
				proc = rt.exec(git_command);
				proc.waitFor();
				
				log_command = "git --git-dir=" + cloudstack_projects[i] + "/.git log --no-merges";
				System.out.println("Executing: " + log_command);
				System.setOut(new PrintStream(new FileOutputStream(cloudstack_projects[i] + "/log.txt")));
				proc = rt.exec(log_command);
				in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while ((line = in.readLine()) != null) 
				{
					System.out.println(line);
				}
				in.close();
				System.setOut(original);
				
				System.out.println("Importing log into database for project " + cloudstack_projects[i]);
				import_log("CloudStack", cloudstack_projects[i]);
			}
			
			// OpenNebula Projects
			for (int i=0; i<opennebula_projects.length; i++)
			{
				del_command = "rm -Rf " + opennebula_projects[i];
				System.out.println("Executing: " + del_command);
				proc = rt.exec(del_command);
				proc.waitFor();
	
				git_command = "git clone https://github.com/OpenNebula/" + opennebula_projects[i];
				System.out.println("Executing: " + git_command);
				proc = rt.exec(git_command);
				proc.waitFor();
				
				log_command = "git --git-dir=" + opennebula_projects[i] + "/.git log --no-merges";
				System.out.println("Executing: " + log_command);
				System.setOut(new PrintStream(new FileOutputStream(opennebula_projects[i] + "/log.txt")));
				proc = rt.exec(log_command);
				in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while ((line = in.readLine()) != null) 
				{
					System.out.println(line);
				}
				in.close();
				System.setOut(original);
				
				System.out.println("Importing log into database for project " + opennebula_projects[i]);
				import_log("OpenNebula", opennebula_projects[i]);
			}
						
		} catch (Exception e)
		{
		}
		
		fix_multiple_domains();

	}
	

	
	public static void main(String args[])
	{
		int cs=0, eu=0, on=0, os=0;
		Git git = new Git();
		git.fetch_git_raw_data();
		git.fix_multiple_domains();

		// Do some counting...
		int i = 0;
		int total, count;
		String date1, date2;		
		
		System.out.println("\n\nContributing Authors \n\n");
		while (i < (git.dates.length-1))
		{
			date1 = git.dates[i];
			date2 = git.dates[i+1];
			cs = git.count_activity("authors", "CloudStack", "CloudStack", date1, date2);
			eu = git.count_activity("authors", "Eucalyptus", "Eucalyptus", date1, date2);
			on = git.count_activity("authors", "OpenNebula", "OpenNebula", date1, date2);
			os = git.count_activity("authors", "OpenStack", "OpenStack", date1, date2);
			System.out.println(date2 + " \t" + os + " \t" + on + "\t" + eu + "\t" + cs);
			i++;
		}

		System.out.println("\n\nCommit Activities \n\n");
		while (i < (git.dates.length-1))
		{
			date1 = git.dates[i];
			date2 = git.dates[i+1];
			cs = git.count_activity("commits", "CloudStack", "CloudStack", date1, date2);
			eu = git.count_activity("commits", "Eucalyptus", "Eucalyptus", date1, date2);
			on = git.count_activity("commits", "OpenNebula", "OpenNebula", date1, date2);
			os = git.count_activity("commits", "OpenStack", "OpenStack", date1, date2);
			System.out.println(date2 + " \t" + os + " \t" + on + "\t" + eu + "\t" + cs);
			i++;
		}

		System.out.println("\n\nContributing Domains \n\n");
		while (i < (git.dates.length-1))
		{
			date1 = git.dates[i];
			date2 = git.dates[i+1];
			cs = git.count_activity("domains", "CloudStack", "CloudStack", date1, date2);
			eu = git.count_activity("domains", "Eucalyptus", "Eucalyptus", date1, date2);
			on = git.count_activity("domains", "OpenNebula", "OpenNebula", date1, date2);
			os = git.count_activity("domains", "OpenStack", "OpenStack", date1, date2);
			System.out.println(date2 + " \t" + os + " \t" + on + "\t" + eu + "\t" + cs);
			i++;
		}

		// OpenStack sub-project analysis
		System.out.println("\n\nOpenStack Commit_Analysis \n\n");
		git.count_openstack("commits");
		System.out.println("\n\nOpenStack Author_Analysis \n\n");
		git.count_openstack("authors");
		System.out.println("\n\nOpenStack Domain_Analysis \n\n");
		git.count_openstack("domains");
		
		// Accumulated population and accumulated domain
		git.count("accumulated_population");
		git.count("accumulated_domain");


		// Active Contributing organizations in the past quarter
		System.out.println("\n\nActive Organizationss During Past Quarter \n\n");
		cs = git.count_activity("domains", "CloudStack", "CloudStack", "2015-01-01", "2015-04-01");
		eu = git.count_activity("domains", "Eucalyptus", "Eucalyptus", "2015-01-01", "2015-04-01");
		on = git.count_activity("domains", "OpenNebula", "OpenNebula", "2015-01-01", "2015-04-01");
		os = git.count_activity("domains", "OpenStack",  "OpenStack",  "2015-01-01", "2015-04-01");
		System.out.println(os + "   " + on + "   " + eu + "   " + cs);


	}
}
