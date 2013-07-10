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

	/*
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


	public void count_contribution(String project, String start, String end)
	{
		String sql = null, sql_total=null;
		int count_total=1, count, percentage;
		String domain;
		
		if (project.equals("openstack"))
		{
			sql_total =  "SELECT count(*) AS count FROM logs WHERE (project = 'cinder' || project = 'glance' || project = 'horizon' || project = 'keystone' || project = 'nova' || project = 'quantum' || project = 'swift') AND date >= '" + start + "' AND date < '" + end + "'";	
					
			sql = "SELECT count(*) AS count, domain FROM logs WHERE (project = 'cinder' || project = 'glance' || project = 'horizon' || project = 'keystone' || project = 'nova' || project = 'quantum' || project = 'swift') AND date >= '" + start + "' AND date < '" + end + "' GROUP BY domain ORDER BY count DESC LIMIT 10";			
		}
		else
		{

			sql_total =  "SELECT count(*) AS count FROM logs WHERE project = '" + project + "' AND date >= '" + start + "' AND date < '" + end + "'";	
					
			sql = "SELECT count(*) AS count, domain FROM logs WHERE project = '" + project + "' AND date >= '" + start + "' AND date < '" + end + "' GROUP BY domain ORDER BY count DESC LIMIT 10";	
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
//				System.out.println(inputLine);
				if (inputLine.indexOf("commit")==0)
				{
					commit = inputLine.substring(7).trim();
					System.out.println(commit);
					
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
					System.out.println(author);
					System.out.println(email);
					System.out.println(domain);
					
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
					System.out.println(date);
					
					System.out.println("");
					
					insert_data(main_project, project, commit, author, email, domain, date);
				}
			}
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
			
	}
	
	public void fetch_log()
	{
		clean_up_table();
		import_log("OpenNebula", "one");
		import_log("Eucalyptus", "eucalyptus");
//		import_log("cloudstack");
//		import_log("cinder");
//		import_log("glance");
//		import_log("horizon");
//		import_log("keystone");
//		import_log("nova");
//		import_log("quantum");
//		import_log("swift");
	}
	
	public void count(String type)
	{
		String[] dates = {"2008-03-01", "2008-04-01", "2008-05-01", "2008-06-01", "2008-07-01", "2008-08-01", "2008-09-01", "2008-10-01", "2008-11-01", "2008-12-01", 
		"2009-01-01", "2009-02-01", "2009-03-01", "2009-04-01", "2009-05-01", "2009-06-01", "2009-07-01", "2009-08-01", "2009-09-01", "2009-10-01", "2009-11-01", "2009-12-01", 
		"2010-01-01", "2010-02-01", "2010-03-01", "2010-04-01", "2010-05-01", "2010-06-01", "2010-07-01", "2010-08-01", "2010-09-01", "2010-10-01", "2010-11-01", "2010-12-01", 
		"2011-01-01", "2011-02-01", "2011-03-01", "2011-04-01", "2011-05-01", "2011-06-01", "2011-07-01", "2011-08-01", "2011-09-01", "2011-10-01", "2011-11-01", "2011-12-01", 
		"2012-01-01", "2012-02-01", "2012-03-01", "2012-04-01", "2012-05-01", "2012-06-01", "2012-07-01", "2012-08-01", "2012-09-01", "2012-10-01", "2012-11-01", "2012-12-01",
		"2013-01-01", "2013-02-01", "2013-03-01", "2013-04-01", "2013-05-01", "2013-06-01", "2013-07-01", "2013-08-01"};
		
		int cs=0, eu=0, on=0, os=0;
		int cinder, glance, horizon, keystone, nova, quantum, swift;
		/*
			for (int i=dates.length - 1; i>=1; i--)
			{
				cs = count_activity(type, "cloudstack", dates[i-1], dates[i]);
				eu = count_activity(type, "eucalyptus", dates[i-1], dates[i]);
				on = count_activity(type, "one", dates[i-1], dates[i]);
				cinder = count_activity(type, "cinder", dates[i-1], dates[i]);
				glance = count_activity(type, "glance", dates[i-1], dates[i]);
				horizon = count_activity(type, "horizon", dates[i-1], dates[i]);
				keystone = count_activity(type, "keystone", dates[i-1], dates[i]);
				nova = count_activity(type, "nova", dates[i-1], dates[i]);
				quantum = count_activity(type, "quantum", dates[i-1], dates[i]);
				swift = count_activity(type, "swift", dates[i-1], dates[i]);
				os = count_activity(type, "openstack", dates[i-1], dates[i]);
	
				System.out.println(dates[i] + " " + os + " " + on + " " + eu + " " + cs + " " + cinder + " " + glance + " " + horizon + " " + keystone + " " + nova + " " + quantum + " " + swift);
				
			}	
		*/	
	}	
	
	public void print_usage_information()
	{
			System.out.println("	");
			System.out.println("    Usage: java Git action main_project sub_project [start_date] [end_date]\n");
			System.out.println("	");
			System.out.println("    action        -- clear, import, commits, authors, domains.");
			System.out.println("                     clear       - clear out the database, delete everything.");
			System.out.println("                     import      - import git records.");
			System.out.println("    main_project  -- Name of the main project, such as CloudStack, Eucalyptus, OpenNebula, OpenStack.");
			System.out.println("    sub_project   -- Name of the sub project, such as nova or cinder. Default to main_project if ommitted.");
			System.out.println("                     This is also the directory name of the sub project.");
			System.out.println("	");
			System.out.println("    Example: java Git import OpenStack nova\n");
	}
	
	public static void main(String args[])
	{
		Git git = new Git();
/*
		git.fetch_log();
		System.out.println("\n\nCommit_Analysis \n\n");
		git.count("commits");
		System.out.println("\n\nAuthor_Analysis \n\n");
		git.count("authors");
		System.out.println("\n\nDomain_Analysis \n\n");
		git.count("domains");
*/
//		git.count_contribution("swift", "2013-04-01", "2013-07-01");
		int length = args.length;
		String action, main_project, sub_project, start_date, end_date;
		
		if (length < 2)
		{
			git.print_usage_information();
		}
		else
		{
			action = args[0];
			main_project = args[1];
			if (length == 3)	
			{
				sub_project = args[2];
			}
			else
			{
				sub_project = main_project;
			}
			
			if (action.equals("clear"))
			{
				if (main_project.equals("ALL"))
				{
					git.clean_up_table();
				}	
				else
				{
					git.clean_up_record(main_project, sub_project);
				}
			} 
			else if (action.equals("import"))
			{
				git.import_log(main_project, sub_project);
			}
			else if ((action.equals("commits")) || (action.equals("authors")) || (action.equals("domains")))
			{
				int count;
				if (length == 3)
				{
					start_date = "1000-01-01";	// A very very early date
					end_date	= "NOW()";
					count = git.count_activity(action, main_project, sub_project, start_date, end_date);
					System.out.println("Count: " + count);					
				}
				else if (length == 4)
				{
					start_date = args[3];
					end_date	= "NOW()";
					count = git.count_activity(action, main_project, sub_project, start_date, end_date);
					System.out.println("Count: " + count);					
				}	
				else if (length == 5)
				{
					start_date = args[3];
					end_date	= args[4];
					count = git.count_activity(action, main_project, sub_project, start_date, end_date);
					System.out.println("Count: " + count);					
				}
				else
				{
					git.print_usage_information();
				}
			}
			else
			{
				git.print_usage_information();
			}
		}
	}
}
