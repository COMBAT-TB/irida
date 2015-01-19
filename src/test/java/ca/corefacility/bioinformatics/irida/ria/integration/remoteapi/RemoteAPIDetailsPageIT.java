package ca.corefacility.bioinformatics.irida.ria.integration.remoteapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import ca.corefacility.bioinformatics.irida.config.data.IridaApiJdbcDataSourceConfig;
import ca.corefacility.bioinformatics.irida.config.services.IridaApiPropertyPlaceholderConfig;
import ca.corefacility.bioinformatics.irida.ria.integration.pages.LoginPage;
import ca.corefacility.bioinformatics.irida.ria.integration.pages.remoteapi.RemoteAPIDetailsPage;
import ca.corefacility.bioinformatics.irida.ria.integration.pages.remoteapi.RemoteAPIDetailsPage.ApiStatus;
import ca.corefacility.bioinformatics.irida.ria.integration.utilities.TestUtilities;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * IT for the client details page
 *
 * @author Thomas Matthews <thomas.matthews@phac-aspc.gc.ca>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { IridaApiJdbcDataSourceConfig.class,
		IridaApiPropertyPlaceholderConfig.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
@ActiveProfiles("it")
@DatabaseSetup("/ca/corefacility/bioinformatics/irida/ria/web/oauth/RemoteApisIT.xml")
@DatabaseTearDown("classpath:/ca/corefacility/bioinformatics/irida/test/integration/TableReset.xml")
public class RemoteAPIDetailsPageIT {
	private WebDriver driver;
	private RemoteAPIDetailsPage page;

	Long id = 1l;
	String apiName = "a client";
	String apiClient = "client";

	@Before
	public void setup() {
		driver = TestUtilities.setDriverDefaults(new ChromeDriver());
		LoginPage.loginAsAdmin(driver);

		page = new RemoteAPIDetailsPage(driver, id);
	}

	@After
	public void destroy() {
		if (driver != null) {
			driver.close();
			driver.quit();
		}
	}

	@Test
	public void testVerifyClient() {
		String clientName = page.getClientName();
		assertEquals(apiName, clientName);

		String clientId = page.getClientId();
		assertEquals(apiClient, clientId);

		ApiStatus remoteApiStatus = page.getRemoteApiStatus();
		assertEquals(ApiStatus.INVALID, remoteApiStatus);

	}

	@Test
	public void testDeleteClient() {
		page.clickDeleteButton();
		page.confirmDelete();
		assertTrue(page.checkDeleteSuccess());
	}

}
