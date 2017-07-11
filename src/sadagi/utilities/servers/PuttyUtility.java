package sadagi.utilities.servers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import sadagi.utilities.properties.Config;
import sadagi.utilities.validators.PuttyUtilityValidator;

/**
 * <p>
 * PuttyUtility class is a business class of api. 
 * It provides all functionality for getting transaction between user application and server. 
 * 
 * @author Chandan Singh Sroniyan (Contact : +91 7827711464, Email : sadagi.chandan@gmail.com).
 */
public class PuttyUtility implements Config{
	
	/**
	 * Global variable for process.
	 */
	private Session session = null;
	private String channeltype = null;
	
	/**
	  * <p>
	  * It is parameterized constructor. Which initialize session of server transaction.
	  *  
	  * @param USER It is a String variable for username of server access.
	  * @param PASS It is a String variable for password of server access.
	  * @param IP It is a String variable for ip of server access.
	  * @param PORT It is a Integer variable for port number of server access.
	  * @param CHANNELTYPE It is a String variable for channel type of server access.
	  * @throws JSchException This method throw JSchException.
	  */
	public PuttyUtility(String USER, String PASS, String IP, int PORT, String CHANNELTYPE) throws JSchException{
		
		this.ConstructorValidator(USER, PASS,IP, PORT,CHANNELTYPE);
		
		this.channeltype = CHANNELTYPE;
		
		JSch jsch = new JSch();
		this.session = jsch.getSession(USER, IP, PORT);
		session.setPassword(PASS);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
		session.connect();
	    
	}
	
	/**
	  * <p>
	  * It is a public method. It provide text file result for offline process.
	  *  
	  * @param SITE It is a String variable which contains site info.
	  * @param CLIST It is a String variable which contains command list info for running on server.
	  * @param OUTPATH It is a String variable which contains output file info.
	  * @param wait It is a Integer variable which contain wait time (in milliseconds) for applying wait between commands.
	  * @param commandPB It is a JProgressBar variable which contains JProgressBar object for capturing progress.
	  * @param commandL It is a JLabel variable which contains JLabel object for capturing progress name.
	  * @throws JSchException This method throw JSchException.
	  * @throws IOException This method throw IOException.
	  * @throws InterruptedException This method throw InterruptedException.
	  * 
	  */
	public void saveLogs(String SITE, ArrayList<String> CLIST, String OUTPATH, int wait, JProgressBar commandPB, JLabel commandL) throws IOException, JSchException, InterruptedException
	{
		this.saveLogsValidator(SITE, CLIST, OUTPATH, wait, commandPB, commandL);
		
		BufferedReader brLTECheck = null;
		BufferedWriter bwLTECheck = null;
		String msg=null;
		
		Channel channel = null;
		OutputStream ops = null;
		PrintStream ps = null;
		
		int pIdex = commandPB.getValue();
		int sIndex = 100/CLIST.size();
		
		try
		{

			channel = this.session.openChannel(this.channeltype);
			ops = channel.getOutputStream();
			ps = new PrintStream(ops, true);
			channel.connect();
			
			brLTECheck=new BufferedReader(new InputStreamReader(channel.getInputStream()));
			
			//commands
			for(String COMMAND : CLIST)
			{
				ps.println(COMMAND); 
				
				if(wait > 0)
				{
					Thread.sleep(wait);
				}
				
				commandL.setText(COMMAND);
				pIdex = pIdex+sIndex;
				commandPB.setValue(pIdex);
			}
			ps.close();
			
			bwLTECheck = new BufferedWriter(new FileWriter(new File(OUTPATH)));
			while((msg=brLTECheck.readLine())!=null)
			{
				bwLTECheck.write(msg.replace("", "").replace("", ""));
				bwLTECheck.newLine();
			}
			bwLTECheck.flush();
			bwLTECheck.close();
			channel.disconnect();
		}
		finally{
			
			 msg = null;
			 channel = null;
			 ops = null;
			 ps = null;
			 
			 System.runFinalization();
			 System.gc();
		}
		
		}

	/**
	  * <p>
	  * It is a public method. It provide text file result for online process.
	  *  
	  * @param SITE It is a String variable which contains site info.
	  * @param CLIST It is a String variable which contains command list info for running on server.
	  * @param OUTPATH It is a String variable which contains output file info.
	  * @param wait It is a Integer variable which contain wait time (in milliseconds) for applying wait between commands.
	  * @throws JSchException This method throw JSchException.
	  * @throws IOException This method throw IOException.
	  * @throws InterruptedException This method throw InterruptedException.
	  */
	
		public void saveLogs(String SITE, ArrayList<String> CLIST, String OUTPATH, int wait) throws IOException, JSchException, InterruptedException
		{
			this.saveLogsValidator(SITE, CLIST, OUTPATH, wait);
			
			BufferedReader brLTECheck = null;
			BufferedWriter bwLTECheck = null;
			String msg=null;
			
			Channel channel = null;
			OutputStream ops = null;
			PrintStream ps = null;
			
			try
			{

				channel = this.session.openChannel(this.channeltype);
				ops = channel.getOutputStream();
				ps = new PrintStream(ops, true);
				channel.connect();
				
				brLTECheck=new BufferedReader(new InputStreamReader(channel.getInputStream()));
				
				//commands
				for(String COMMAND : CLIST)
				{
					ps.println(COMMAND); 
					
					if(wait > 0)
					{
						Thread.sleep(wait);
					}
				}
				ps.close();
				
				bwLTECheck = new BufferedWriter(new FileWriter(new File(OUTPATH)));
				while((msg=brLTECheck.readLine())!=null)
				{
					bwLTECheck.write(msg.replace("", "").replace("", ""));
					bwLTECheck.newLine();
				}
				bwLTECheck.flush();
				bwLTECheck.close();
				channel.disconnect();
			}
			finally{
				
				 msg = null;
				 channel = null;
				 ops = null;
				 ps = null;
				 
				 System.runFinalization();
				 System.gc();
			}
			
			}
		
		/**
		  * <p>
		  * It is a public method. It provide result as collection object for offline process.
		  *  
		  * @param SITE It is a String variable which contains site info.
		  * @param CLIST It is a String variable which contains command list info for running on server.
		  * @param OUTPATH It is a String variable which contains output file info.
		  * @param wait It is a Integer variable which contain wait time (in milliseconds) for applying wait between commands.
		  * @param commandPB It is a JProgressBar variable which contains JProgressBar object for capturing progress.
		  * @param commandL It is a JLabel variable which contains JLabel object for capturing progress name.
	      * @throws JSchException This method throw JSchException.
	      * @return <code>ArrayList class with String generic type</code>
	      * @throws IOException This method throw IOException.
	      * @throws InterruptedException This method throw InterruptedException.
		  */
		
	public ArrayList<String> captureLogs(String SITE, ArrayList<String> CLIST, String OUTPATH, int wait, JProgressBar commandPB, JLabel commandL) throws IOException, JSchException, InterruptedException
	{
		this.saveLogsValidator(SITE, CLIST, OUTPATH, wait, commandPB, commandL);
		
		BufferedReader brLTECheck = null;
		String msg=null;
		
		ArrayList<String> shareLog = new ArrayList<String>();
		
		Channel channel = null;
		OutputStream ops = null;
		PrintStream ps = null;
		
		int pIdex = commandPB.getValue();
		int sIndex = 100/CLIST.size();
		
		try
		{

			channel = this.session.openChannel(channeltype);
			ops = channel.getOutputStream();
			ps = new PrintStream(ops, true);
			channel.connect();
			
			brLTECheck=new BufferedReader(new InputStreamReader(channel.getInputStream()));
			
			//commands
			for(String COMMAND : CLIST)
			{
				ps.println(COMMAND); 
				
				if(wait > 0)
				{
					Thread.sleep(wait);
				}
				
				commandL.setText(COMMAND);
				pIdex = pIdex+sIndex;
				commandPB.setValue(pIdex);
			}
			ps.close();
			
			while((msg=brLTECheck.readLine())!=null)
			{
				shareLog.add(msg.replace("", "").replace("", "\n"));
			}
			channel.disconnect();
		}
		finally{
			
			 msg = null;
			 channel = null;
			 ops = null;
			 ps = null;
			 
			 System.runFinalization();
			 System.gc();
		}
		
		return shareLog;
		}
	
	
	/**
	  * <p>
	  * It is a public method. It provide result as collection object for online process.
	  *  
	  * @param SITE It is a String variable which contains site info.
	  * @param CLIST It is a String variable which contains command list info for running on server.
	  * @param OUTPATH It is a String variable which contains output file info.
	  * @param wait It is a Integer variable which contain wait time (in milliseconds) for applying wait between commands.
	  * @return <code>ArrayList class with String generic type</code>
	  * @throws JSchException This method throw JSchException.
	  * @throws IOException This method throw IOException.
	  * @throws InterruptedException This method throw InterruptedException.
	  */
		public ArrayList<String> captureLogs(String SITE, ArrayList<String> CLIST, String OUTPATH, int wait) throws IOException, JSchException, InterruptedException
		{
			this.saveLogsValidator(SITE, CLIST, OUTPATH, wait);
			
			BufferedReader brLTECheck = null;
			String msg=null;
			
			ArrayList<String> shareLog = new ArrayList<String>();
			
			Channel channel = null;
			OutputStream ops = null;
			PrintStream ps = null;
			
			try
			{

				channel = this.session.openChannel(channeltype);
				ops = channel.getOutputStream();
				ps = new PrintStream(ops, true);
				channel.connect();
				
				brLTECheck=new BufferedReader(new InputStreamReader(channel.getInputStream()));
				
				//commands
				for(String COMMAND : CLIST)
				{
					ps.println(COMMAND); 
					
					if(wait > 0)
					{
						Thread.sleep(wait);
					}
				}
				ps.close();
				
				while((msg=brLTECheck.readLine())!=null)
				{
					shareLog.add(msg.replace("", "").replace("", "\n"));
				}
				channel.disconnect();
			}
			finally{
				
				 msg = null;
				 channel = null;
				 ops = null;
				 ps = null;
				 
				 System.runFinalization();
				 System.gc();
			}
			
			return shareLog;
			}
		

	
		/**
		  * <p>
		  * It is a method. Which close session of server transaction.
		  */
	public void closeSession()
	{
		this.session.disconnect();
	}
	
	/**
	  * <p>
	  * It is a private method. It validate parameterized constructor arguments.
	  *  
	  * @param USER It is a String variable for username of server access.
	  * @param PASS It is a String variable for password of server access.
	  * @param IP It is a String variable for ip of server access.
	  * @param PORT It is a Integer variable for port number of server access.
	  * @param CHANNELTYPE It is a String variable for channel type of server access.
	  */
	private void ConstructorValidator(String USER, String PASS, String IP, int PORT, String CHANNELTYPE)
	{
		if( ! PuttyUtilityValidator.textHasContent(USER) ){
		      throw new IllegalArgumentException("Error ! Invalid Username passed.");
		    }
		
		if( ! PuttyUtilityValidator.textHasContent(PASS) ){
		      throw new IllegalArgumentException("Error ! Invalid password passed.");
		    }
		
		if( ! PuttyUtilityValidator.textHasContent(CHANNELTYPE) ){
		      throw new IllegalArgumentException("Error ! Invalid channel type passed.");
		    }
		
		if( ! PuttyUtilityValidator.textHasIp(IP) ){
		      throw new IllegalArgumentException("Error ! Invalid ip passed.");
		    }
	}
	
	/**
	  * <p>
	  * It is a private method. It validate saveLogs() method arguments for online process.
	  *  
	  * @param SITE It is a String variable which contains site info.
	  * @param CLIST It is a String variable which contains command list info for running on server.
	  * @param OUTPATH It is a String variable which contains output file info.
	  * @param wait It is a Integer variable which contain wait time (in milliseconds) for applying wait between commands.
	  */
	private void saveLogsValidator(String SITE, ArrayList<String> CLIST, String OUTPATH, int wait)
	{
		if( ! PuttyUtilityValidator.textHasContent(SITE) ){
		      throw new IllegalArgumentException("Error ! Invalid Site name passed.");
		    }
		
		if( ! PuttyUtilityValidator.checkForNull(CLIST) ){
		      throw new IllegalArgumentException("Error ! null command list passed.");
		    }
		
		if( ! PuttyUtilityValidator.textHasContent(OUTPATH) ){
		      throw new IllegalArgumentException("Error ! Invalid output path passed.");
		    }
	}
	
	/**
	  * <p>
	  * It is a private method. It validate saveLogs() method arguments for offline process.
	  *  
	  * @param SITE It is a String variable which contains site info.
	  * @param CLIST It is a String variable which contains command list info for running on server.
	  * @param OUTPATH It is a String variable which contains output file info.
	  * @param wait It is a Integer variable which contain wait time (in milliseconds) for applying wait between commands.
	  * @param commandPB It is a JProgressBar variable which contains JProgressBar object for capturing progress.
      * @param commandL It is a JLabel variable which contains JLabel object for capturing progress name.
	  */
	private void saveLogsValidator(String SITE, ArrayList<String> CLIST, String OUTPATH, int wait, JProgressBar commandPB, JLabel commandL)
	{
		if( ! PuttyUtilityValidator.textHasContent(SITE) ){
		      throw new IllegalArgumentException("Error ! Invalid Site name passed.");
		    }
		
		if( ! PuttyUtilityValidator.checkForNull(CLIST) ){
		      throw new IllegalArgumentException("Error ! null command list passed.");
		    }
		
		if( ! PuttyUtilityValidator.textHasContent(OUTPATH) ){
		      throw new IllegalArgumentException("Error ! Invalid output path passed.");
		    }
		
		if( ! PuttyUtilityValidator.checkForNull(commandPB) ){
		      throw new IllegalArgumentException("Error ! null progress obj passed.");
		    }
		
		if( ! PuttyUtilityValidator.checkForNull(commandL) ){
		      throw new IllegalArgumentException("Error ! null level obj passed.");
		    }
	}

}
