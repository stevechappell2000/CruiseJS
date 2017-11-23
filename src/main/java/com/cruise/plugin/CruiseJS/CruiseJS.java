package com.cruise.plugin.CruiseJS;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import com.corecruise.cruise.SessionObject;
import com.corecruise.cruise.logging.Clog;
import com.corecruise.cruise.services.interfaces.PluginInterface;
import com.corecruise.cruise.services.utils.GenericSessionResp;
import com.corecruise.cruise.services.utils.Services;
import com.cruise.plugins.Action;
import com.cruise.plugins.ActionParameter;
import com.cruise.plugins.PlugInMetaData;

public class CruiseJS implements PluginInterface{
	PlugInMetaData pmd = null;
	private ScriptEngineManager sem = null;
	private ScriptEngine se = null;
	public CruiseJS() {
    	pmd = new PlugInMetaData("CruiseJS","0.0.1","SJC","Serverside scripting engine");
    	
    	pmd.getActions().add(new Action("info", "getPlugin Information"));
    	pmd.getActions().get(0).getActionParams().add(new ActionParameter("service","true","CruiseJSGetInfo","Unique name defaults to a GUID. You can override."));
    	pmd.getActions().get(0).getActionParams().add(new ActionParameter("None","false","unknown","Unused Sample Parameter"));
    	
    	pmd.getActions().add(new Action("CruiseTest", "Test API Call"));
    	pmd.getActions().get(1).getActionParams().add(new ActionParameter("service","true","*UUID","Unique name defaults to a GUID. You can override."));
		pmd.getActions().get(1).getActionParams().add(new ActionParameter("Sample","false","unknown","Unused Sample Parameter"));
		
    	pmd.getActions().add(new Action("RunScript", "get information about the pluging"));
    	pmd.getActions().get(2).getActionParams().add(new ActionParameter("service","true","*UUID","Unique name defaults to a GUID. You can override."));
    	pmd.getActions().get(2).getActionParams().add(new ActionParameter("Script","true","unknown","Java Script code to execute on the server."));
 
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
		case "info":
			so.appendToResponse(pmd);
			ret = true;
			break;
		case "CruiseTest":
			gsr.addParmeter("PluginEnabled", "true");
			so.appendToResponse(service.Service()+"."+service.Action(),gsr);
			break;
		case "RunScript":
			String script = service.Parameter("Script");
			if(null != script) {
				gsr.addParmeter("Results", runScript(so, gsr, script).toString());
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
	private Boolean runScript(SessionObject so, GenericSessionResp gsr, String script) {
		boolean ret = false;
		if(null == sem ) {
			sem = new ScriptEngineManager();
			se = sem.getEngineByName("nashorn");
		}
		try {
			if(null != script) {
				se.put("sessionObject", so);
				se.put("responseObject", gsr);
				se.eval(script);
			}else {
				gsr.addParmeter("Results", "200 No Script Found.");
			}
		}catch(Exception e) {
			Clog.Error(so, "service", "14001", e.getMessage());
			gsr.addParmeter("Results", "300 "+e.getMessage());
		}
		return ret;
	}

}
