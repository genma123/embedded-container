package us.magicuser.standaloneejb.ejb;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.ejb.embeddable.EJBContainer;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import us.magicuser.standaloneejb.ejb.view.StandaloneEJBWithDBUnitLocal;
import us.magicuser.standaloneejb.jpa.entity.Employee;

public class StandaloneEJBWithDBUnitTest {
	
	public static final String driver = "org.h2.Driver";
	public static final String protocol = "jdbc:h2:";

	public static final String driver_DERBY = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String protocol_DERBY = "jdbc:derby:";

	// private IDatabaseTester databaseTester;

	private static Connection connection;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// NOTE because this is the test, it runs outside the container, so connect
		// via the usual JDBC way to populate the data.
		connection =
			DriverManager.getConnection(protocol + "mem:test;DB_CLOSE_DELAY=-1", new Properties());
				Class.forName(driver).newInstance();
			/* DriverManager.getConnection(protocol_DERBY + "derbyDB;user=EJB_TEST;create=true", new Properties());
		Class.forName(driver_DERBY).newInstance();*/
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		connection.close();
	}

	@Before
	public void setUp() throws Exception {
		setUpDBUnit();
	}

	@After
	public void tearDown() throws Exception {
		tearDownDBUnit();
	}

	@Test
	public void testAddEmployee() throws Exception {
	      // Create a properties map to pass to the embeddable container:
	      Map<String,Object> properties = new HashMap<String,Object>();

	      // Specify that you want to use the WebSphere embeddable container:
	      properties.put(EJBContainer.PROVIDER, 
	          "com.ibm.websphere.ejbcontainer.EmbeddableContainerProvider");
	      properties.put(EJBContainer.MODULES, new File("StandaloneEJBWithDBUnitEJB.jar"));
	      
	      EJBContainer ec = EJBContainer.createEJBContainer(properties);

	      StandaloneEJBWithDBUnitLocal ejb = (StandaloneEJBWithDBUnitLocal)
	    	ec.getContext().lookup(
				"java:global/StandaloneEJBWithDBUnitEJB/StandaloneEJBWithDBUnit!us.magicuser.standaloneejb.ejb.view.StandaloneEJBWithDBUnitLocal");
	      
	      Employee employee = ejb.addEmployee("George", "Washington");
	      System.err.println("Emp ln,fn->[" + employee.getLastName() + "," + employee.getFirstName() +"]");
	}
	
	private void loadSchema() throws Exception {
		InputStream is = getStreamReader("testData.sql");
		InputStreamReader reader = new InputStreamReader(is);
		
		RunScript.execute(connection, reader);
	}
	
	private void loadSchemaDerby() throws Exception {
		InputStream is = getStreamReader("testDataDerby.sql");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		while (true) {
			String line = br.readLine();
			if (line == null) break;
			
			sb.append(line);
			sb.append("\n");
		}
		
		String sql = sb.toString();
		StringTokenizer tokenizer = new StringTokenizer(sql, ";");
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			token = token.trim();
			System.err.println("token: " + token);
			if (!token.startsWith("CREATE") &&
					!token.startsWith("DROP")) continue;
			
			Statement statement = connection.createStatement();
			try {
				statement.execute(token);
				System.err.println("success!");
			} catch (Exception exc) {
				System.err.println("exception: " + exc.getMessage());
			}
		}		
	}
	
	private InputStream getStreamReader(String resourcePath) throws Exception {
		return StandaloneEJBWithDBUnitTest.class
				.getClassLoader().getResourceAsStream(resourcePath);
	}
	
	private void setUpDBUnit() throws Exception {
		loadSchema();
		// loadSchemaDerby();

		InputStream is = getStreamReader("testData.xml");		
		
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(is);
		
	    IDatabaseConnection dbConn=new DatabaseConnection(connection);
	    DatabaseConfig config=dbConn.getConfig();
	    // NOT NEEDED FOR DERBY
	    config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,new H2DataTypeFactory());
	    DatabaseOperation.CLEAN_INSERT.execute(dbConn,dataSet);


		/*String url = protocol + "mem:test;DB_CLOSE_DELAY=-1";
		System.err.println("URL: " + url);
		
		databaseTester = new JdbcDatabaseTester(driver, url);
		DatabaseConfig config = databaseTester.getConnection().getConfig();
		Object dtfprop = config.getProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY);
		System.err.println("dtfprop is " + dtfprop.getClass().getCanonicalName() + ", " + dtfprop);
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
		dtfprop = config.getProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY);
		System.err.println("dtfprop is " + dtfprop.getClass().getCanonicalName() + ", " + dtfprop);
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();*/
	}
	
	private void tearDownDBUnit() throws Exception {
		// databaseTester.onTearDown();
	}

}
