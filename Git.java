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

	 
	String[] dates = {"2008-03-01", "2008-04-01", "2008-05-01", "2008-06-01", "2008-07-01", "2008-08-01", "2008-09-01", "2008-10-01", "2008-11-01", "2008-12-01", 
		"2009-01-01", "2009-02-01", "2009-03-01", "2009-04-01", "2009-05-01", "2009-06-01", "2009-07-01", "2009-08-01", "2009-09-01", "2009-10-01", "2009-11-01", "2009-12-01", 
		"2010-01-01", "2010-02-01", "2010-03-01", "2010-04-01", "2010-05-01", "2010-06-01", "2010-07-01", "2010-08-01", "2010-09-01", "2010-10-01", "2010-11-01", "2010-12-01", 
		"2011-01-01", "2011-02-01", "2011-03-01", "2011-04-01", "2011-05-01", "2011-06-01", "2011-07-01", "2011-08-01", "2011-09-01", "2011-10-01", "2011-11-01", "2011-12-01", 
		"2012-01-01", "2012-02-01", "2012-03-01", "2012-04-01", "2012-05-01", "2012-06-01", "2012-07-01", "2012-08-01", "2012-09-01", "2012-10-01", "2012-11-01", "2012-12-01",
		"2013-01-01", "2013-02-01", "2013-03-01", "2013-04-01", "2013-05-01", "2013-06-01", "2013-07-01", "2013-08-01", "2013-09-01", "2013-10-01", "2013-11-01", "2013-12-01"};
	 
	String[] openstack_projects = {"swift", "neutron", "nova", "tripleo-image-elements", "compute-api", "api-site", "keystone", "openstack-manuals", "tempest", "object-api", "cinder", "python-neutronclient", "horizon", "python-keystoneclient", "ceilometer", "python-marconiclient", "heat", "os-refresh-config", "os-apply-config", "os-collect-config", "glance", "trove-integration", "marconi", "requirements", "oslo-incubator", "trove", "python-swiftclient", "diskimage-builder", "django_openstack_auth", "tripleo-incubator", "python-cinderclient", "ironic", "netconn-api", "python-troveclient", "python-openstackclient", "python-novaclient", "python-ironicclient", "python-heatclient", "python-glanceclient", "python-ceilometerclient", "oslo.messaging", "oslo.config", "heat-templates", "oslo.version", "image-api", "tripleo-heat-templates", "governance", "openstack-planet", "oslo.sphinx", "identity-api", "melange", "operations-guide", "heat-cfntools", "volume-api", "database-api", "python-melangeclient", "openstack-chef"};
	
	String[] openstack_infra_projects = {"nodepool", "config", "elastic-recheck", "jenkins-job-builder", "reviewstats", "tripleo-ci", "jeepyb", "devstack-gate", "pypi-mirror", "gerritlib", "zuul", "activity-board", "gitdm", "publications", "groups", "gear", "gearman-plugin", "storyboard", "reviewday", "gerrit", "git-review", "odsreg", "gerritbot", "puppet-dashboard", "askbot-theme", "statusbot", "zmq-event-publisher", "nose-html-output", "meetbot", "releasestatus", "puppet-vcsrepo", "puppet-apparmor", "lodgeit"};
	
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
//				System.out.println(sql);
			} catch (Exception e)
			{
				db_connect = null;
			}
		}		
		
	}
	
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


	public void count_contribution(String main_project, String sub_project, String start, String end)
	{
		String sql = null, sql_total=null;
		int count_total=1, count, percentage;
		String domain;
		
		if (main_project.equals(sub_project))
		{
			sql_total =  "SELECT count(*) AS count FROM logs WHERE main_project = '" + main_project + "' AND date >= '" + start + "' AND date < '" + end + "'";	
						
			sql = "SELECT count(*) AS count, domain FROM logs WHERE main_project = '" + main_project + "' AND date >= '" + start + "' AND date < '" + end + "' GROUP BY domain ORDER BY count DESC LIMIT 10";	
		}
		else
		{
			sql_total =  "SELECT count(*) AS count FROM logs WHERE sub_project = '" + sub_project + "' AND date >= '" + start + "' AND date < '" + end + "'";	
						
			sql = "SELECT count(*) AS count, domain FROM logs WHERE sub_project = '" + sub_project + "' AND date >= '" + start + "' AND date < '" + end + "' GROUP BY domain ORDER BY count DESC LIMIT 10";				
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
					
					insert_data(main_project, project, commit, author, email, domain, date);
				}
			}
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
			
	}
	
	
	public void count(String type)
	{
		
		int cs=0, eu=0, on=0, os=0;
		
		if (type.equals("population"))
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
	
				git_command = "git clone git://git.opennebula.org/" + opennebula_projects[i];
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
		
	
	}
	
	
	public static void main(String args[])
	{
		Git git = new Git();

/*		
		// Fetch git data and save to database
		git.fetch_git_raw_data();

		// Main project analysis
		System.out.println("\n\nCommit_Analysis \n\n");
		git.count("commits");
		System.out.println("\n\nAuthor_Analysis \n\n");
		git.count("authors");
		System.out.println("\n\nDomain_Analysis \n\n");
		git.count("domains");
		System.out.println("\n\nPopulation_Analysis \n\n");
		git.count("population");


		// OpenStack sub-project analysis
		System.out.println("\n\nOpenStack Commit_Analysis \n\n");
		git.count_openstack("commits");
		System.out.println("\n\nOpenStack Author_Analysis \n\n");
		git.count_openstack("authors");
		System.out.println("\n\nOpenStack Domain_Analysis \n\n");
		git.count_openstack("domains");


		System.out.println("\n\nCloudStack Top Contributors \n\n");
		git.count_contribution("CloudStack", "CloudStack", "2013-7-01", "2013-10-01");
		System.out.println("\n\nEucalyptus Top Contributors \n\n");
		git.count_contribution("Eucalyptus", "Eucalyptus", "2013-7-01", "2013-10-01");
		System.out.println("\n\nOpenNebula Top Contributors \n\n");
		git.count_contribution("OpenNebula", "OpenNebula", "2013-7-01", "2013-10-01");
		System.out.println("\n\nOpenStack Top Contributors \n\n");
		git.count_contribution("OpenStack", "OpenStack", "2013-7-01", "2013-10-01");
		System.out.println("\n\nCinder Top Contributors \n\n");
		git.count_contribution("OpenStack", "cinder", "2013-7-01", "2013-10-01");
		System.out.println("\n\nGlance Top Contributors \n\n");
		git.count_contribution("OpenStack", "glance", "2013-7-01", "2013-10-01");
		System.out.println("\n\nHorizon Top Contributors \n\n");
		git.count_contribution("OpenStack", "horizon", "2013-7-01", "2013-10-01");
		System.out.println("\n\nKeystone Top Contributors \n\n");
		git.count_contribution("OpenStack", "keystone", "2013-7-01", "2013-10-01");
		System.out.println("\n\nNova Top Contributors \n\n");
		git.count_contribution("OpenStack", "nova", "2013-7-01", "2013-10-01");
		System.out.println("\n\nNeutron Top Contributors \n\n");
		git.count_contribution("OpenStack", "neutron", "2013-7-01", "2013-10-01");
		System.out.println("\n\nSwift Top Contributors \n\n");
		git.count_contribution("OpenStack", "swift", "2013-7-01", "2013-10-01");
*/
		System.out.println("\n\nPopulation_Analysis \n\n");
		git.count("population");

	}
}
