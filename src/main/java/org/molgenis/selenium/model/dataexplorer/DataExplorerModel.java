package org.molgenis.selenium.model.dataexplorer;

import java.util.List;

import org.molgenis.selenium.model.AbstractModel;
import org.molgenis.selenium.model.component.Select2Model;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a model of the MOLGENIS Data Explorer user interface
 */
public class DataExplorerModel extends AbstractModel
{
	private static final Logger LOG = LoggerFactory.getLogger(DataExplorerModel.class);

	public static enum DeleteOption
	{
		DATA, DATA_AND_METADATA;
	}

	private final Select2Model entityModel;

	@FindBy(css = ".page-next")
	private WebElement next;

	@FindBy(css = ".page-prev")
	private WebElement previous;

	@FindBy(id = "entity-class-name")
	private WebElement entityClassName;

	@FindBy(id = "dropdownMenu1")
	private WebElement deleteDropdownMenu;

	@FindBy(id = "delete-data-btn")
	private WebElement deleteDataButton;

	@FindBy(id = "delete-data-metadata-btn")
	private WebElement deleteDataMetadataBtn;

	@FindBy(css = "[data-bb-handler=confirm]")
	private WebElement confirmButton;

	@FindBy(linkText = "Annotators")
	private WebElement annotatorTab;

	@FindBy(css = "a.tree-deselect-all-btn")
	private WebElement deselectAllButton;

	@FindBy(css = "div.molgenis-tree span.fancytree-has-children span.fancytree-checkbox")
	private List<WebElement> treeFolders;

	@FindBy(css = ".molgenis-table-container tbody tr")
	private List<WebElement> tableRows;

	public DataExplorerModel(WebDriver driver)
	{
		super(driver);
		entityModel = new Select2Model(driver, "dataset-select", false);
	}

	public void deleteEntity(DeleteOption deleteOption) throws InterruptedException
	{
		String selectedEntity = getSelectedEntityTitle();
		LOG.info("deleteEntity {}, mode={} ...", selectedEntity, deleteOption);
		deleteDropdownMenu.click();

		switch (deleteOption)
		{
			case DATA:
				deleteDataButton.click();
				break;
			case DATA_AND_METADATA:
				deleteDataMetadataBtn.click();
				break;
			default:
				break;
		}
		confirmButton.click();
		Wait<WebDriver> tenSecondWait = new WebDriverWait(driver, 10);
		tenSecondWait.until((d) -> !selectedEntity.equals(getSelectedEntityTitle()));
	}

	public DataExplorerModel selectEntity(String entityLabel) throws InterruptedException
	{
		LOG.info("selectEntity", entityLabel);
		entityModel.select(entityLabel);
		return this;
	}

	public String getSelectedEntityTitle()
	{
		return entityClassName.getText();
	}

	public DataExplorerModel next()
	{
		next.click();
		return this;
	}

	public DataExplorerModel previous()
	{
		previous.click();
		return this;
	}

	public AnnotatorModel selectAnnotatorTab()
	{
		annotatorTab.click();
		return PageFactory.initElements(driver, AnnotatorModel.class);
	}

	public DataExplorerModel deselectAll()
	{
		deselectAllButton.click();
		return this;
	}

	public DataExplorerModel selectCompoundAttributes()
	{
		treeFolders.forEach(WebElement::click);
		return this;
	}

	public List<List<String>> getTableData()
	{
		LOG.info("getTableData");
		return getTableData(tableRows);
	}
}
