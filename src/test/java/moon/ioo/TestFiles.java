package moon.ioo;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import io.qameta.allure.Owner;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static moon.ioo.TestData.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.qameta.allure.selenide.AllureSelenide;
import com.codeborne.selenide.logevents.SelenideLogger;

public class TestFiles {

    @Test
    @Owner("ioomoon")
    @DisplayName("Filename should appear after upload")
    void uploadFile() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        open(UPLOAD_PAGE);
        $("#uploadFile").uploadFromClasspath(SAMPLE_FILE_NAME);
        $("#uploadedFilePath").shouldHave(text(SAMPLE_FILE_NAME));
    }

    @Test
    @Owner("ioomoon")
    @DisplayName("Download TXT file and check it's content")
    void downloadTXTFile() throws IOException {
        SelenideLogger.addListener("allure", new AllureSelenide());

        open(DOWNLOAD_TXT_PAGE);
        File download = $("#downloadButton").download();
        String fileContent = IOUtils.toString(new FileReader(download));
        assertTrue(fileContent.contains("Оператор if-else существенно расширил наши возможности программирования"));
    }

    @Test
    @Owner("ioomoon")
    @DisplayName("Download PDF file and check it's content")
    void downloadPDFFile() throws IOException {
        SelenideLogger.addListener("allure", new AllureSelenide());

        open(DOWNLOAD_PDF_PAGE);

        File pdf = $(byText("Download sample pdf file")).download();
        PDF parsedPdf = new PDF(pdf);
        Assertions.assertEquals(4, parsedPdf.numberOfPages);
    }

    @Test
    @Owner("ioomoon")
    @DisplayName("Download XLS file and check it's content")
    void downloadXLSFile() throws IOException {
        SelenideLogger.addListener("allure", new AllureSelenide());

        open(DOWNLOAD_XLS_PAGE);

        File file = $(byText("Download sample xls file")).download();

        XLS parsedXls = new XLS(file);
        boolean checkPassed = parsedXls.excel
                .getSheetAt(0)
                .getRow(1)
                .getCell(1)
                .getStringCellValue()
                .contains(XLS_CHECK_RESULT);

        assertTrue(checkPassed);
    }
}
