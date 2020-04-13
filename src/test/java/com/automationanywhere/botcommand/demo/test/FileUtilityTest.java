package com.automationanywhere.botcommand.demo.test;

import com.automationanywhere.bot.service.ExternalEnvironment;
import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.BooleanValue;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.NumberValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.demo.FileUtility;
import com.automationanywhere.botcommand.exception.BotCommandException;
import org.testng.Assert.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.junit.Assert.*;


public class FileUtilityTest {
/*
    FileUtility command = new FileUtility();

    @DataProvider(name = "strings")
    public Object[][] dataTobeTested(){
        return new Object[][]{
                { "C:\\Users\\Gerado Murias\\Desktop\\A2019 - Building Packages - Level 1.mp4" }
        };
    }

    @Test(dataProvider = "strings")
    public void CheckFileUtilityMethod(String filePath){

        Value<String> d = command.action(filePath);
        String output = d.get();
        System.out.println(output);
        assertEquals(output,"0");
    }*/
}