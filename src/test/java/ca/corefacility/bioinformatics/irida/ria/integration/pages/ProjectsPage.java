package ca.corefacility.bioinformatics.irida.ria.integration.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.corefacility.bioinformatics.irida.ria.integration.utilities.Ajax;

/**
 * <p>
 * Page Object to represent the projects page.
 * </p>
 *
 */
public class ProjectsPage extends AbstractPage {
    private static final Logger logger = LoggerFactory.getLogger(ProjectsPage.class);
	public static final String RELATIVE_URL = "projects";
	public static final String ADMIN_URL = RELATIVE_URL + "/all";

    public ProjectsPage(WebDriver driver) {
		super(driver);
	}

	public void toUserProjectsPage() {
		get(driver, RELATIVE_URL);
		waitForAjax();
	}

	public void toAdminProjectsPage() {
		get(driver, ADMIN_URL);
		waitForAjax();
	}

	public int projectsTableSize() {
		logger.trace("Getting table size");
		return driver.findElements(By.cssSelector("#projectsTable tbody tr")).size();
	}

	public void gotoProjectPage(int row) {
		submitAndWait(driver.findElements(By.cssSelector("#projectsTable .item-link")).get(row));
	}

	public List<WebElement> getProjectColumn() {
		return driver.findElements(By.cssSelector("#projectsTable tbody td:nth-child(2)"));
	}

	public void clickProjectNameHeader() {
		// Sorting row is the second one
		WebElement headerRow = driver.findElements(By.cssSelector(".dataTables_scrollHeadInner thead tr")).get(0);
		final WebElement th = headerRow.findElements(By.cssSelector("th")).get(0);
		final String originalSortOrder = th.getAttribute("aria-sort");
		th.click();
		new WebDriverWait(driver, TIME_OUT_IN_SECONDS).until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(final WebDriver input) {
				final String ariaSort = th.getAttribute("aria-sort");
				return ariaSort!= null && !ariaSort.equals(originalSortOrder);
			}
		});
	}

	private void waitForAjax() {
		Wait<WebDriver> wait = new WebDriverWait(driver, 60);
		wait.until(Ajax.waitForAjax(60000));
	}
}
