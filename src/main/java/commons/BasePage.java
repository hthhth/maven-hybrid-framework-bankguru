package commons;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageObjects.hrm.DashboardPO;
import pageObjects.hrm.LoginPO;
import pageObjects.hrm.PageGenerator;
import pageObjects.user.nopCommerce.MyAccountPageObject;
import pageObjects.user.nopCommerce.OrderPageObject;
import pageObjects.user.nopCommerce.PageGeneratorManager;
import pageObjects.user.nopCommerce.SearchPageObject;
import pageUIs.admin.nopCommerce.AdminBasePageUI;
import pageUIs.hrm.BasePageUI;
import pageUIs.user.nopCommerce.UserBasePageUI;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BasePage {

    public static BasePage getBasePage(){
        return new BasePage();
    }

    public void openPageURL(WebDriver driver, String pageURL){
        driver.get(pageURL);
    }

    public String getPageTitle(WebDriver driver){
        return driver.getTitle();
    }

    public String getPageURL(WebDriver driver){
        return driver.getCurrentUrl();
    }

    public String getPageSource(WebDriver driver){
        return driver.getPageSource();
    }

    public Set<Cookie> getAllCookies(WebDriver driver){
        return driver.manage().getCookies();
    }
    public void setAllCookies(WebDriver driver, Set<Cookie> allCookies){
        for (Cookie cookie : allCookies) {
            driver.manage().addCookie(cookie);
        }
    }

    public Alert waitForAlertPresence(WebDriver driver){
        explicitWait = new WebDriverWait(driver, shortTimeout);
        return explicitWait.until(ExpectedConditions.alertIsPresent());
    }

    public void acceptAlert(WebDriver driver){
        alert = waitForAlertPresence(driver);
        alert.accept();
        sleepInSeconds(2);
    }

    public void cancelAlert(WebDriver driver){
        alert = waitForAlertPresence(driver);
        alert.dismiss();
    }

    public void sendKeyToAlert(WebDriver driver, String value){
        alert = waitForAlertPresence(driver);
        alert.sendKeys(value);
    }

    public String getAlertText(WebDriver driver){
        alert = waitForAlertPresence(driver);
        return alert.getText();
    }

    public void switchToWindowByID(WebDriver driver, String parentID){
        Set<String> allWindowIDs = driver.getWindowHandles();
        for (String windowID : allWindowIDs) {
            if (!windowID.equals(parentID)){
                driver.switchTo().window(windowID);
                break;
            }
        }
    }

    public void switchToWindowByTitle(WebDriver driver, String expectedWindowTitle){
        Set<String> allWindowIDs = driver.getWindowHandles();
        for(String windowID : allWindowIDs){
            driver.switchTo().window(windowID);
            String windowTitle = driver.getTitle();
            if (windowTitle.equals(expectedWindowTitle)){
                break;
            }
        }
    }

    public void closeAllWindowExceptParent(WebDriver driver, String parentID){
        Set<String> allWindowIDs = driver.getWindowHandles();
        for(String windowID : allWindowIDs) {
            if (!windowID.equals(parentID)) {
                driver.switchTo().window(windowID);
                driver.close();
                sleepInSeconds(1);
            }
        }
        driver.switchTo().window(parentID);
    }

    public void sleepInSeconds(int time){
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void backToPage(WebDriver driver){
        driver.navigate().back();
    }

    public void refreshCurrentPage(WebDriver driver){
        driver.navigate().refresh();
    }

    public void forwardToPage(WebDriver driver){
        driver.navigate().forward();
    }

    public WebElement getElement(WebDriver driver, String locator){
        return driver.findElement(getByXpath(locator));
    }

    public WebElement getElement(WebDriver driver, String locator, String... params){
        return driver.findElement(getByXpath(getDynamicLocator(locator, params)));
    }

    public List<WebElement> getElements(WebDriver driver, String locator){
        return driver.findElements(getByXpath(locator));
    }

    public By getByXpath(String locator){
        return By.xpath(locator);
    }

    public String getDynamicLocator(String locator, String... params){
        return String.format(locator, params);
    }

    public void clickToElement(WebDriver driver, String locator){
        if (driver.toString().contains("internet explorer")) {
            clickToElementByJS(driver, locator);
            sleepInSeconds(2);
        } else {
            getElement(driver, locator).click();
        }
    }

    public void clickToElement(WebDriver driver, String locator, String... params){
        if (driver.toString().contains("internet explorer")) {
            clickToElementByJS(driver, getDynamicLocator(locator, params));
            sleepInSeconds(2);
        } else {
            getElement(driver, getDynamicLocator(locator, params)).click();
        }
    }
    public void sendKeyToElement(WebDriver driver, String locator, String value){
        getElement(driver, locator).clear();
        getElement(driver, locator).sendKeys(value);
    }
    public void sendKeyToElement(WebDriver driver, String locator, String value, String... params){
        locator = getDynamicLocator(locator, params);
        getElement(driver, locator).clear();
        getElement(driver, locator).sendKeys(value);
    }

    public int getElementSize(WebDriver driver, String locator){
        return getElements(driver, locator).size();
    }

    public int getElementSize(WebDriver driver, String locator, String... params){
        locator = getDynamicLocator(locator, params);
        return getElements(driver, locator).size();
    }

    public void selectDropdownByText(WebDriver driver, String locator, String itemText){
        select = new Select(getElement(driver, locator));
        select.selectByVisibleText(itemText);
    }

    public void selectDropdownByText(WebDriver driver, String locator, String itemText, String... params){
        locator = getDynamicLocator(locator, params);
        select = new Select(getElement(driver, locator));
        select.selectByVisibleText(itemText);
    }

    public String getSelectedItemDropdown(WebDriver driver, String locator){
        select = new Select(getElement(driver, locator));
        return select.getFirstSelectedOption().getText();
    }

    public String getSelectedItemDropdown(WebDriver driver, String locator, String... params){
        locator = getDynamicLocator(locator, params);
        select = new Select(getElement(driver, locator));
        return select.getFirstSelectedOption().getText();
    }

    public boolean isDropdownMultiple(WebDriver driver, String locator){
        select = new Select(getElement(driver, locator));
        return select.isMultiple();
    }

    public void selectItemInCustomDropdown(WebDriver driver, String parentLocator, String childItemsLocator, String expectedItem) {
        clickToElement(driver, parentLocator);
        explicitWait = new WebDriverWait(driver, shortTimeout);
        List<WebElement> allItems = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByXpath(childItemsLocator)));

        for (WebElement item : allItems){
            if (item.getText().trim().equals(expectedItem)){
                jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", item);
                sleepInSeconds(1);
                item.click();
                sleepInSeconds(1);
                break;
            }
        }
    }

    public String getElementAttribute(WebDriver driver, String locator, String attributeName){
        return getElement(driver, locator).getAttribute(attributeName);
    }

    public String getElementAttribute(WebDriver driver, String locator, String attributeName, String...  params){
        locator = getDynamicLocator(locator, params);
        return getElement(driver, locator).getAttribute(attributeName);
    }

    public String getElementText(WebDriver driver, String locator){
        return getElement(driver, locator).getText();
    }

    public String getElementText(WebDriver driver, String locator, String... params){
        return getElement(driver, getDynamicLocator(locator, params)).getText().trim();
    }

    public void checkToCheckboxOrRadio(WebDriver driver, String locator){
        if (!isElementSelected(driver, locator)) {
            clickToElement(driver, locator);
        }
    }

    public void checkToCheckboxOrRadio(WebDriver driver, String locator, String... params){
        locator = getDynamicLocator(locator, params);
        if (!isElementSelected(driver, locator)) {
            clickToElement(driver, locator);
        }
    }

    public void uncheckToRadio(WebDriver driver, String locator){
        if (isElementSelected(driver, locator)) {
            clickToElement(driver, locator);
        }
    }

    public boolean isElementDisplayed(WebDriver driver, String locator){
        try {
            return getElement(driver, locator).isDisplayed();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isElementUndisplayed(WebDriver driver, String locator){
        overrideGlobalTimeout(driver, shortTimeout);
        List<WebElement> elements = getElements(driver, locator);
        overrideGlobalTimeout(driver, longTimeout);
        if (elements.size() == 0){
            System.out.println("End 1: " + new Date().toString());
            return true;
        } else if (elements.size() > 0 && !elements.get(0).isDisplayed()){
            return true;
        } else {
            return false;
        }
    }

    public void overrideGlobalTimeout(WebDriver driver, long timeout){
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }

    public boolean isElementDisplayed(WebDriver driver, String locator, String... params){
        return getElement(driver, getDynamicLocator(locator, params)).isDisplayed();
    }

    public boolean isElementEnabled(WebDriver driver, String locator){
        return getElement(driver, locator).isEnabled();
    }

    public boolean isElementEnabled(WebDriver driver, String locator, String... params){
        locator = getDynamicLocator(locator, params);
        return getElement(driver, locator).isEnabled();
    }

    public boolean isElementSelected(WebDriver driver, String locator){
        return getElement(driver, locator).isSelected();
    }

    public boolean isElementSelected(WebDriver driver, String locator, String... params){
        locator = getDynamicLocator(locator, params);
        return getElement(driver, locator).isSelected();
    }

    public WebDriver switchToIframeByElement(WebDriver driver, String locator){
        return driver.switchTo().frame(getElement(driver, locator));
    }

    public WebDriver switchToDefaultContent(WebDriver driver){
        return driver.switchTo().defaultContent();
    }

    public void hoverToElement(WebDriver driver, String locator){
        action = new Actions(driver);
        action.moveToElement(getElement(driver, locator)).perform();
    }

    public void hoverToElement(WebDriver driver, String locator, String... params){
        action = new Actions(driver);
        locator = getDynamicLocator(locator, params);
        action.moveToElement(getElement(driver, locator)).perform();
    }

    public void doubleClickToElement(WebDriver driver, String locator){
        action = new Actions(driver);
        action.doubleClick(getElement(driver, locator)).perform();
    }

    public void rightClickToElement(WebDriver driver, String locator){
        action = new Actions(driver);
        action.contextClick(getElement(driver, locator)).perform();
    }

    public void dragAndDropElement(WebDriver driver, String sourceLocator, String targetLocator){
        action = new Actions(driver);
        action.dragAndDrop(getElement(driver, sourceLocator), getElement(driver, targetLocator)).perform();
    }

    public void pressKeyToElement(WebDriver driver, String locator, Keys key){
        action = new Actions(driver);
        action.sendKeys(getElement(driver, locator), key).perform();
    }

    public void pressKeyToElement(WebDriver driver, String locator, Keys key, String... params){
        action = new Actions(driver);
        locator = getDynamicLocator(locator, params);
        action.sendKeys(getElement(driver, locator), key).perform();
    }

    public void submitWithoutInputData(WebElement buttonSubmit, WebElement element, String message){
        buttonSubmit.click();
        sleepInSeconds(2);
        Assert.assertEquals(jsExecutor.executeScript("return arguments[0].validationMessage", element), message);
    }

    public Object executeForBrowser(WebDriver driver, String javaScript) {
        jsExecutor = (JavascriptExecutor) driver;
        return jsExecutor.executeScript(javaScript);
    }

    public String getInnerText(WebDriver driver) {
        jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript("return document.documentElement.innerText;");
    }

    public boolean areExpectedTextInInnerText(WebDriver driver, String textExpected) {
        jsExecutor = (JavascriptExecutor) driver;
        String textActual = (String) jsExecutor.executeScript("return document.documentElement.innerText.match('" + textExpected + "')[0]");
        return textActual.equals(textExpected);
    }

    public void scrollToBottomPage(WebDriver driver) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public void navigateToUrlByJS(WebDriver driver, String url) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.location = '" + url + "'");
    }

    public void highlightElement(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        WebElement element= driver.findElement(By.xpath(locator));
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed; background-color: yellow;");
        sleepInSeconds(1);
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
//        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//        WebElement element = getElement(driver, locator);
//        String originalStyle = element.getAttribute("style");
//        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
//        sleepInSecond(1);
//        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    public void clickToElementByJS(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].click();", getElement(driver, locator));
    }

    public void scrollToElement(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getElement(driver, locator));
    }

    public void sendkeyToElementByJS(WebDriver driver, String locator, String value) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].setAttribute('value', '" + value + "')", getElement(driver, locator));
    }

    public void removeAttributeInDOM(WebDriver driver, String locator, String attributeRemove) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", getElement(driver, locator));
    }

    public boolean isJQueryAJAXLoadedSuccess(WebDriver driver) {
        explicitWait = new WebDriverWait(driver, longTimeout);
        jsExecutor = (JavascriptExecutor) driver;
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) ((JavascriptExecutor) driver).executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
            }
        };
        return explicitWait.until(jQueryLoad);
    }

    public boolean areJQueryAndJSLoadedSuccess(WebDriver driver) {
        explicitWait = new WebDriverWait(driver, longTimeout);
        jsExecutor = (JavascriptExecutor) driver;

        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) jsExecutor.executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };

        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return jsExecutor.executeScript("return document.readyState").toString().equals("complete");
            }
        };

        return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
    }

    // HTML 5 validation message (browser's validation message)
    public String getElementValidationMessage(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", getElement(driver, locator));
    }

    public boolean isImageLoaded(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", getElement(driver, locator));
        if (status) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isImageLoadedSuccess(WebDriver driver, String javaScript, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(By.xpath(locator));
        return (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0", element);
    }

    public void waitForElementVisible(WebDriver driver, String locator){
        explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(locator)));
    }

    public void waitForElementVisible(WebDriver driver, String locator, String... params){
        explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(getDynamicLocator(locator, params))));
    }

    public void waitForAllElementsVisible(WebDriver driver, String locator){
        explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(locator)));
    }

    public void waitForElementClickable(WebDriver driver, String locator){
        explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(locator)));
    }

    public void waitForElementClickable(WebDriver driver, String locator, String... params){
        explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(getDynamicLocator(locator, params))));
    }

    public void waitForElementInvisible(WebDriver driver, String locator){
        explicitWait = new WebDriverWait(driver, shortTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(locator)));
    }

    public void waitForElementInvisible(WebDriver driver, String locator, String... params){
        explicitWait = new WebDriverWait(driver, shortTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(getDynamicLocator(locator, params))));
    }

    // User - NopCommerce
    public SearchPageObject openSearchPage(WebDriver driver) {
        waitForElementClickable(driver, UserBasePageUI.SEARCH_PAGE_FOOTER);
        clickToElement(driver, UserBasePageUI.SEARCH_PAGE_FOOTER);
        return PageGeneratorManager.getSearchPage(driver);
    }
    public MyAccountPageObject openMyAccountPage(WebDriver driver) {
        waitForElementClickable(driver, UserBasePageUI.MY_ACCOUNT_PAGE_FOOTER);
        clickToElement(driver, UserBasePageUI.MY_ACCOUNT_PAGE_FOOTER);
        return PageGeneratorManager.getMyAccountPage(driver);
    }
    public OrderPageObject openOrderPage(WebDriver driver) {
        waitForElementClickable(driver, UserBasePageUI.ORDER_PAGE_FOOTER);
        clickToElement(driver, UserBasePageUI.ORDER_PAGE_FOOTER);
        return PageGeneratorManager.getOrderPage(driver);
    }

    public BasePage getFooterPageByName(WebDriver driver, String pageName){
        waitForElementClickable(driver, UserBasePageUI.DYNAMIC_PAGE_FOOTER, pageName);
        clickToElement(driver, UserBasePageUI.DYNAMIC_PAGE_FOOTER, pageName);

        if (pageName.equals("Search")){
            return PageGeneratorManager.getSearchPage(driver);
        } else if (pageName.equals("My account")){
            return PageGeneratorManager.getMyAccountPage(driver);
        } else {
            return PageGeneratorManager.getOrderPage(driver);
        }
    }

    public void openFooterPageByName(WebDriver driver, String pageName) {
        waitForElementClickable(driver, UserBasePageUI.DYNAMIC_PAGE_FOOTER, pageName);
        clickToElement(driver, UserBasePageUI.DYNAMIC_PAGE_FOOTER, pageName);
    }

    // Admin - NopCommerce
    public void openSubMenuPageByName(WebDriver driver, String menuPageName, String submenuPageName) {
        waitForElementClickable(driver, AdminBasePageUI.MENU_LINK_BY_NAME, menuPageName);
        clickToElement(driver, AdminBasePageUI.MENU_LINK_BY_NAME, menuPageName);

        waitForElementClickable(driver, AdminBasePageUI.SUB_MENU_LINK_BY_NAME, submenuPageName);
        clickToElement(driver, AdminBasePageUI.SUB_MENU_LINK_BY_NAME, submenuPageName);
    }

    public void uploadMultipleFilesAtCardName(WebDriver driver, String cardName, String... fileNames){
        String fullFileName = "";
        for (String file:fileNames){
            fullFileName = fullFileName + GlobalConstants.UPLOAD_FOLDER_PATH + file + "\n";
        }
        fullFileName = fullFileName.trim();
        getElement(driver, AdminBasePageUI.UPLOAD_FILE_BY_CARD_NAME, cardName).sendKeys(fullFileName);
    }

    public boolean isMessageDisplayedInEmptyTable(WebDriver driver, String tableName) {
        waitForElementVisible(driver, AdminBasePageUI.NO_DATA_MESSAGE_BY_TABLE_NAME, tableName);
        return isElementDisplayed(driver, AdminBasePageUI.NO_DATA_MESSAGE_BY_TABLE_NAME, tableName);
    }

    // Pattern object - NopCommerce
    public void enterToTextboxByID(WebDriver driver,String textboxID, String value) {
        waitForElementVisible(driver, UserBasePageUI.DYNAMIC_TEXTBOX_BY_ID, textboxID);
        sendKeyToElement(driver, UserBasePageUI.DYNAMIC_TEXTBOX_BY_ID, value, textboxID);
    }

    public void openHeaderPageByName(WebDriver driver, String pageName) {
        waitForElementClickable(driver, UserBasePageUI.DYNAMIC_PAGE_HEADER, pageName);
        clickToElement(driver, UserBasePageUI.DYNAMIC_PAGE_HEADER, pageName);
    }

    public void clickToRadioButtonByText(WebDriver driver, String radioButtonText) {
        waitForElementClickable(driver, UserBasePageUI.DYNAMIC_RADIO_BY_NAME, radioButtonText);
        clickToElement(driver, UserBasePageUI.DYNAMIC_RADIO_BY_NAME, radioButtonText);
    }
    public void selectDropdownByName(WebDriver driver, String dropdownName, String itemText) {
        selectDropdownByText(driver, UserBasePageUI.DYNAMIC_DROPDOWN_BY_NAME, itemText, dropdownName);
    }

    public void clickToButtonByText(WebDriver driver, String buttonText) {
        waitForElementClickable(driver, UserBasePageUI.DYNAMIC_BUTTON_BY_TEXT, buttonText);
        clickToElement(driver, UserBasePageUI.DYNAMIC_BUTTON_BY_TEXT, buttonText);
    }

    // HRM - Menu
    public void openMenuPage(WebDriver driver, String menuPageName){
        waitForElementClickable(driver, BasePageUI.MENU_BY_PAGE_NAME, menuPageName);
        clickToElement(driver, BasePageUI.MENU_BY_PAGE_NAME, menuPageName);

        isJQueryAJAXLoadedSuccess(driver);
    }

    // HRM - SubMenu
    public void openSubMenuPage(WebDriver driver, String menuPageName, String subMenuPageName){
        waitForElementClickable(driver, BasePageUI.MENU_BY_PAGE_NAME, menuPageName);
        if (driver.toString().contains("internet explorer")) {
            openPageURL(driver, getElementAttribute(driver, BasePageUI.MENU_BY_PAGE_NAME, "href", menuPageName));
        } else {
            clickToElement(driver, BasePageUI.MENU_BY_PAGE_NAME, menuPageName);
        }

        waitForElementClickable(driver, BasePageUI.MENU_BY_PAGE_NAME, subMenuPageName);
        if (driver.toString().contains("internet explorer")) {
            openPageURL(driver, getElementAttribute(driver, BasePageUI.MENU_BY_PAGE_NAME, "href", subMenuPageName));
        } else {
            clickToElement(driver, BasePageUI.MENU_BY_PAGE_NAME, subMenuPageName);
        }

        isJQueryAJAXLoadedSuccess(driver);

        if (driver.toString().contains("internet explorer")){
            sleepInSeconds(3);
        }
    }

    // HRM - ChildSubMenu
    public void openChildSubMenuPage(WebDriver driver, String menuPageName, String subMenuPageName, String childSubMenuPageName){
        waitForElementClickable(driver, BasePageUI.MENU_BY_PAGE_NAME, menuPageName);
        clickToElement(driver, BasePageUI.MENU_BY_PAGE_NAME, menuPageName);

        waitForElementVisible(driver, BasePageUI.MENU_BY_PAGE_NAME, subMenuPageName);
        hoverToElement(driver, BasePageUI.MENU_BY_PAGE_NAME, subMenuPageName);

        waitForElementClickable(driver, BasePageUI.MENU_BY_PAGE_NAME, childSubMenuPageName);
        clickToElement(driver, BasePageUI.MENU_BY_PAGE_NAME, childSubMenuPageName);

        isJQueryAJAXLoadedSuccess(driver);

        if (driver.toString().contains("internet explorer")){
            sleepInSeconds(3);
        }
    }

    // HRM - Button component
    public void clickToButtonByID(WebDriver driver, String buttonIDName){
        waitForElementClickable(driver, BasePageUI.BUTTON_BY_ID, buttonIDName);
        clickToElement(driver, BasePageUI.BUTTON_BY_ID, buttonIDName);
        if (driver.toString().contains("internet explorer")){
            sleepInSeconds(3);
        }
    }
    // HRM - Textbox component
    public void enterToTextboxByID_HRM(WebDriver driver, String textBoxIDName, String value){
        waitForElementVisible(driver, BasePageUI.TEXTBOX_BY_ID, textBoxIDName);
        sendKeyToElement(driver, BasePageUI.TEXTBOX_BY_ID, value, textBoxIDName );
    }

    // HRM - Get Textbox value
    /**
     * Get textbox value by textbox ID
     * @param driver
     * @param textBoxIDName
     * @return Attribute value
     */
    public String getTextboxValueByID(WebDriver driver, String textBoxIDName){
        waitForElementVisible(driver, BasePageUI.TEXTBOX_BY_ID, textBoxIDName);
        return getElementAttribute(driver, BasePageUI.TEXTBOX_BY_ID, "value", textBoxIDName);
    }

    /**
     * HRM - Select item dropdown by id
     * @param driver
     * @param dropdownID
     * @param valueItem
     */
    public void selectItemInDropdownByID(WebDriver driver, String dropdownID, String valueItem){
        waitForElementClickable(driver, BasePageUI.DROPDOWN_BY_ID, dropdownID);
        selectDropdownByText(driver, BasePageUI.DROPDOWN_BY_ID, valueItem, dropdownID);
    }

    /**
     * HRM - Get selected text item in dropdown
     * @param driver
     * @param dropdownID
     * @return Selected text in dropdown
     */
    public String getSelectedValueInDropdownByID(WebDriver driver, String dropdownID){
        waitForElementVisible(driver, BasePageUI.DROPDOWN_BY_ID, dropdownID);
        return getSelectedItemDropdown(driver, BasePageUI.DROPDOWN_BY_ID, dropdownID);
    }

    /**
     * HRM - Click to checkbox by label text
     * @param driver
     * @param checkboxLabelName
     */
    public void clickToCheckboxByLabel(WebDriver driver, String checkboxLabelName){
        waitForElementClickable(driver, BasePageUI.CHECKBOX_BY_LABEL, checkboxLabelName);
        checkToCheckboxOrRadio(driver, BasePageUI.CHECKBOX_BY_LABEL, checkboxLabelName);
    }

    /**
     * HRM - Click to radio by label text
     * @param driver
     * @param radioLabelName
     */
    public void clickToRadioByLabel(WebDriver driver, String radioLabelName){
        waitForElementClickable(driver, BasePageUI.RADIO_BY_LABEL, radioLabelName);
        checkToCheckboxOrRadio(driver, BasePageUI.RADIO_BY_LABEL, radioLabelName);
    }

    public boolean isRadioButtonSelectedByLabel(WebDriver driver, String labelName){
        waitForElementVisible(driver, BasePageUI.RADIO_BY_LABEL, labelName);
        return isElementSelected(driver, BasePageUI.RADIO_BY_LABEL, labelName);
    }

    public String getValueInTableIDAtColumnNameAndRowIndex(WebDriver driver, String tableID, String headerName, String rowIndex) {
        int columnIndex = getElementSize(driver, BasePageUI.TABLE_HEADER_BY_ID_AND_NAME, tableID, headerName) + 1;
        waitForElementVisible(driver, BasePageUI.TABLE_ROW_BY_COLUMN_INDEX_AND_ROW_INDEX, tableID, rowIndex, String.valueOf(columnIndex));
        return getElementText(driver, BasePageUI.TABLE_ROW_BY_COLUMN_INDEX_AND_ROW_INDEX, tableID, rowIndex, String.valueOf(columnIndex));
    }

    public DashboardPO loginToSystem(WebDriver driver, String userName, String password){
        waitForElementVisible(driver, BasePageUI.USER_LOGIN_TEXTBOX);
        sendKeyToElement(driver, BasePageUI.USER_LOGIN_TEXTBOX, userName);
        sendKeyToElement(driver, BasePageUI.PASSWORD_LOGIN_TEXTBOX, password);
        clickToElement(driver, BasePageUI.LOGIN_BUTTON);
        if (driver.toString().contains("internet explorer")){
            sleepInSeconds(3);
        }
        return PageGenerator.getDashboardPage(driver);
    }

    public LoginPO logoutSystem(WebDriver driver){
        waitForElementClickable(driver, BasePageUI.WELCOME_USER_LINK);
        clickToElement(driver, BasePageUI.WELCOME_USER_LINK);
        waitForElementClickable(driver, BasePageUI.LOGOUT_LINK);
        clickToElement(driver, BasePageUI.LOGOUT_LINK);

        return PageGenerator.getLoginPage(driver);
    }

    public void uploadImage(WebDriver driver, String filePath){
        getElement(driver, BasePageUI.UPLOAD_FILE).sendKeys(filePath);
    }

//    public void waitImageLoad(WebDriver driver, String locator){
//        explicitWait = new WebDriverWait(driver, GlobalConstants.LONG_TIMEOUT);
//        jsExecutor = (JavascriptExecutor) driver;
//        explicitWait.until(new ExpectedCondition<Boolean>() {
//            @Override
//            public Boolean apply(WebDriver driver) {
//                return (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].complete", getElement(driver, locator));
//            }
//        }
//        );
//    }

    public boolean isSuccessMessageDisplayed(WebDriver driver, String messageValue) {
        waitForElementVisible(driver, BasePageUI.SUCCESS_MESSAGE_VALUE, messageValue);
        return isElementDisplayed(driver, BasePageUI.SUCCESS_MESSAGE_VALUE, messageValue);
    }

    public boolean isFieldEnabledByName(WebDriver driver, String fieldID){
        waitForElementVisible(driver, BasePageUI.ANY_FIELD_BY_ID, fieldID);
        return isElementEnabled(driver, BasePageUI.ANY_FIELD_BY_ID, fieldID);
    }

    private Alert alert;
    private Select select;
    private Actions action;
    private long shortTimeout = GlobalConstants.SHORT_TIMEOUT;
    private long longTimeout = GlobalConstants.LONG_TIMEOUT;
    private WebDriverWait explicitWait;
    private JavascriptExecutor jsExecutor;


}
