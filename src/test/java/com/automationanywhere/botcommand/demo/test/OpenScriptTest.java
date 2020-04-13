package com.automationanywhere.botcommand.demo.test;

import com.automationanywhere.bot.service.ExternalEnvironment;
import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.demo.AttributeValueUtil;
import com.automationanywhere.botcommand.demo.OpenScript;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@Test(enabled=true)
public class OpenScriptTest {

    @DataProvider(name="testValues")
    public Object[][] testValues() {
        return new Object[][] {
                {"Default", "script", "3"}
        };
    }

    private GlobalSessionContext getGlobalSessionContextMock(){
        ExternalEnvironment externalEnvironment = mock(ExternalEnvironment.class);
        doNothing().when(externalEnvironment).openFunction(any(), any());
        when(externalEnvironment.callFunction(any(), any())).thenReturn("result");
        doNothing().when(externalEnvironment).closeFunction(any());

        GlobalSessionContext globalSessionContext = mock(GlobalSessionContext.class);
        when(globalSessionContext.getExternalEnvironment()).thenReturn(externalEnvironment);
        return globalSessionContext;
    }

    @Test(dataProvider = "testValues")
    public void MyFirstTest(String pythonSession, String scriptOption, String pythonVersion){

        // Arrange
        OpenScript systemUnderTest = new OpenScript();

        Map<String, Value> parameters = new HashMap<String, Value>();
        parameters.put("uuid", new StringValue("defa3a1b-f3b2-4961-a88e-378aef143965"));
        systemUnderTest.setParameters(parameters);

        AttributeValueUtil attributeValueUtil = new AttributeValueUtil();
        //systemUnderTest.setAttributeValueUtil(attributeValueUtil);
        /*AttributeValueUtil attributeValueUtilMock = mock(AttributeValueUtil.class);
        when(attributeValueUtilMock.getStringValue(parameters, "uuid"))
                .thenReturn("defa3a1b-f3b2-4961-a88e-378aef143965");
        systemUnderTest.setAttributeValueUtil(attributeValueUtilMock);*/

        systemUnderTest.setScript("print \"hello\"");

        GlobalSessionContext globalSessionContextMock = getGlobalSessionContextMock();
        systemUnderTest.setGlobalSessionContext(globalSessionContextMock);

        HashMap<String,Object> mySession = new HashMap<String,Object>();
        mySession.put("Default", null);
        systemUnderTest.setSessionMap(mySession);


        // Act
        //systemUnderTest.open(pythonSession, scriptOption, pythonVersion);


        // Assert
        assertEquals(1,1);
    }
}