package it.fabryprog.mysql.statistics;

import static org.mockito.Mockito.mock;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

import org.junit.Test;

/**
 * Unit test for simple App.
 */

public class MainTest {

	private static final Logger log = Logger.getLogger(MainTest.class.getName());
	
	@Test
	public void runTest() throws Exception {
		PreparedStatement ps = mock(PreparedStatement.class);
		
		ps.executeQuery();
	}
}
