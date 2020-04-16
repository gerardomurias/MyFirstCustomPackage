import com.automationanywhere.bot.service.ExternalEnvironment;
import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import commands.ExecuteFunction;
import helper.AttributeValueUtil;
import commands.OpenScript;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Test(enabled=true)
public class ExecuteInlinePythonTest {

    @DataProvider(name="testValues")
    public Object[][] testValues() {
        return new Object[][] {
                {"Default", "script", "3"}
        };
    }

    private String _expectedResult = "expectedResult";

    private GlobalSessionContext getGlobalSessionContextMock(){
        ExternalEnvironment externalEnvironment = mock(ExternalEnvironment.class);
        doNothing().when(externalEnvironment).openFunction(any(), any());
        when(externalEnvironment.callFunction(any(), any())).thenReturn(_expectedResult);
        doNothing().when(externalEnvironment).closeFunction(any());

        GlobalSessionContext globalSessionContext = mock(GlobalSessionContext.class);
        when(globalSessionContext.getExternalEnvironment()).thenReturn(externalEnvironment);
        return globalSessionContext;
    }

    @Test(dataProvider = "testValues")
    public void MyFirstTest(String pythonSession, String scriptOption, String pythonVersion){

        // Arrange
        OpenScript openScript = new OpenScript();
        ExecuteFunction executeFunction = new ExecuteFunction();

        Map<String, Value> parameters = new HashMap<String, Value>();
        parameters.put("uuid", new StringValue("defa3a1b-f3b2-4961-a88e-378aef143965"));
        openScript.setParameters(parameters);

        AttributeValueUtil attributeValueUtil = new AttributeValueUtil();
        openScript.setAttributeValueUtil(attributeValueUtil);

        openScript.setScript("def hello_world():\nprint \"hello\"");

        GlobalSessionContext globalSessionContextMock = getGlobalSessionContextMock();
        openScript.setGlobalSessionContext(globalSessionContextMock);
        executeFunction.setGlobalSessionContext(globalSessionContextMock);

        HashMap<String,Object> mySession = new HashMap<String,Object>();
        mySession.put("Default", null);
        openScript.setSessionMap(mySession);
        executeFunction.setSessionMap(mySession);


        // Act
        openScript.open(pythonSession, scriptOption, pythonVersion);
        Value returnedResult = executeFunction.execute(pythonSession, "hello_world", null);


        // Assert
        assertEquals(returnedResult.toString() , _expectedResult);
    }
}