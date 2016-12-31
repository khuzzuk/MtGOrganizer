package pl.khuzzuk.mtg.organizer;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FileNameManagerTest {
    @DataProvider
    public static Object[][] validNumbers() {
        return new Integer[][]{{1}, {9}, {10}, {999_999}};
    }

    @Test(groups = "fast", dataProvider = "validNumbers")
    public void checkGeneratedName(Integer number) throws Exception {
        String name = FileNameManager.getFileName(number);
        Assert.assertTrue(name.length() == 18);
    }
}