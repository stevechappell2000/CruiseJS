package com.cruise.plugins;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.corecruise.core.CoreCruise;
import com.corecruise.cruise.SessionObject;
import com.corecruise.cruise.config.CruisePluginEnvironment;
import com.corecruise.cruise.logging.Clog;
import com.corecruise.cruise.services.interfaces.PluginInterface;
import com.corecruise.cruise.services.utils.GenericSessionResp;
import com.corecruise.cruise.services.utils.Services;
import com.cruise.plugins.Action;
import com.cruise.plugins.ActionParameter;
import com.cruise.plugins.PlugInMetaData;

public class CruiseJS implements PluginInterface{
	PlugInMetaData pmd = null;
	String pluginName = "CruiseJS";
	private ScriptEngineManager sem = null;
	CruisePluginEnvironment config = null;
	public CruiseJS() {
		if(null == config)
			config = CoreCruise.getCruiseConfig(pluginName);
		
    	pmd = new PlugInMetaData(pluginName,"0.0.1","SJC","Serverside scripting engine. Three core objects are created and are available to the script engine: cruRespone, cruSession, and cruService.");
    	
    	pmd.getActions().add(new Action("plugInInfo", "getPlugin Information"));
    	pmd.getActions().get(0).getActionParams().add(new ActionParameter("service","true","CruiseJSGetInfo","Unique name defaults to a GUID. You can override."));
    	pmd.getActions().get(0).getActionParams().add(new ActionParameter("None","false","unknown","Unused Sample Parameter"));
    	
    	pmd.getActions().add(new Action("CruiseTest", "Test API Call"));
    	pmd.getActions().get(1).getActionParams().add(new ActionParameter("service","true","~UUID","Unique name defaults to a GUID. You can override."));
		pmd.getActions().get(1).getActionParams().add(new ActionParameter("Sample","false","unknown","Unused Sample Parameter"));
		
    	pmd.getActions().add(new Action("RunScript", "get information about the pluging"));
    	pmd.getActions().get(2).getActionParams().add(new ActionParameter("service","true","~UUID","Unique name defaults to a GUID. You can override."));
    	pmd.getActions().get(2).getActionParams().add(new ActionParameter("Script","true","unknown","Java Script code to execute on the server."));
 //   	pmd.getActions().get(2).getActionParams().add(new ActionParameter("Script","true","unknown","Java Script code to execute on the server."));

	}
	@Override
	public PlugInMetaData getPlugInMetaData() {
		// TODO Auto-generated method stub
		return pmd;
	}

	@Override
	public void setPluginVendor(PlugInMetaData PMD) {
		pmd = PMD;
		
	}

	@Override
	public boolean executePlugin(SessionObject so, Services service) {
		boolean ret = false;
		String action = service.Action();
		GenericSessionResp gsr = new GenericSessionResp();
		switch (action) {
		case "plugininfo":
			so.appendToResponse(pmd);
			ret = true;
			break;
		case "CruiseTest":
			gsr.addParmeter("PluginEnabled", "true");
			so.appendToResponse(service.Service()+"."+service.Action(),gsr);
			ret = true;
			break;
		case "RunScript":
			String script = service.Parameter("Script");
			if(null != script) {
				gsr.addParmeter("Results", runScript(so, gsr, service, script).toString());
				ret = true;
			}else {
				gsr.addParmeter("Results", "100 No Script Found.");
			}
			so.appendToResponse(service.Service()+"."+service.Action(),gsr);
			break;
		default:
			Clog.Error(so, "service", "100.05", "Invalid Action supplied:"+action);
		}
		return ret;

	}
	private Boolean runScript(SessionObject so, GenericSessionResp gsr, Services s, String script) {
		boolean ret = false;
		if(null == sem ) {
			sem = new ScriptEngineManager();
			
		}
		
		try {
			ScriptEngine se = sem.getEngineByName("nashorn");
			if(null != script) {
				se.put("cruCore", new CoreCruise());
				se.put("cruSession", so);
				se.put("cruResponse", gsr);
				se.put("cruService", s);
				se.eval(script);
			    ret = true;
			}else {
				gsr.addParmeter("Results", "200 No Script Found.");
			}
		}catch(Exception e) {
			Clog.Error(so, "service", "14001", e.getMessage());
			gsr.addParmeter("Results", "300 "+e.getMessage());
		}
		return ret;
	}
	@Override
	public void byPass(SessionObject sessionObject) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean initPlugin() {
		// TODO Auto-generated method stub
		return false;
	}

}
