package com.cruise.plugin.CruiseJS;

import com.corecruise.cruise.SessionObject;
import com.corecruise.cruise.SessionObjectJSON;
import com.corecruise.cruise.services.interfaces.PluginClientInterface;
import com.corecruise.cruise.services.utils.Services;
import com.corecruise.cruise.testharness.DummyValidator;

import junit.framework.TestCase;

public class CruiseJSTest extends TestCase implements PluginClientInterface{
	CruiseJS myPlugin = null;
	String ret = 
			"{"+
					"		  \"Application\" : {"+
					"		    \"parameters\" : {"+
					"		      \"name\" : \"Sample Web Application\","+
					"		      \"id\" : \"sampleid\""+
					"		    },"+
					"		    \"credentials\" : {"+
					"		      \"parameters\" : {"+
					"		        \"password\" : \"admin\","+
					"		        \"username\" : \"admin\""+
					"		      }"+
					"		    },"+
					"		    \"services\" : ["+
//					"		      \"Service1\" : {"+
					"                 {\"parameters\" : {"+
					"                     \"pluginName\" : \"CruiseJS\","+
					"                     \"service\" : \"CruiseJSTest\","+
					"                     \"action\" : \"RunScript\","+
					"                     \"Script\" : \"print('hello');cruResponse.putParam('this','that');print(cruResponse.getParam('this'))\","+
					"                     \"TestValue1\" : \"My Test Value 1\","+
					"                     \"TestValue2\" : \"My Test Value 2\""+
					"		              }"+
					"		         }"+  
					"		    ]"+
					"		  }"+
					"		}";
	SessionObjectJSON sowp = null;
	public CruiseJSTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		if(null == myPlugin) {
			myPlugin = new CruiseJS();
		}
		sowp = new SessionObjectJSON();
		sowp.go(this, new DummyValidator(),ret,true);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCruiseJS() {

		assertEquals(true, null != myPlugin);
	}

	public void testGetPlugInMetaData() {

		assertEquals(true, null != myPlugin.getPlugInMetaData());
	}

	public void testExecutePlugin() {
        //System.out.println(sowp.getActiveService().ParameterValue("Script"));
		assertEquals(true, null != sowp.getActiveService().ParameterValue("Script"));
	}

	@Override
	public boolean PreProcess(SessionObject sessionObject, Services service) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean PostProcess(SessionObject sessionObject, Services service) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void ProcessingError(SessionObject sessionObject) {
		// TODO Auto-generated method stub
		
	}

}
