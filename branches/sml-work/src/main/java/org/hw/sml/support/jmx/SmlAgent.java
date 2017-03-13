package org.hw.sml.support.jmx;

import java.util.Date;

import org.hw.sml.FrameworkConstant;
import org.hw.sml.support.ManagedThread;
import org.hw.sml.support.SmlAppContextUtils;
import org.hw.sml.support.security.CyptoUtils;
import org.hw.sml.tools.DateTools;

public class SmlAgent implements SmlAgentMBean {
	static{
		Thread securityMaster=new ManagedThread() {
			protected boolean prepare() {
				return true;
			}
			protected boolean extraExitCondition() {
				return false;
			}
			protected void doWorkProcess() {
				try {
					String key=CyptoUtils.decode(FrameworkConstant.AUTHOR,FrameworkConstant.AUTHKEY);
					while(DateTools.parse(key).before(new Date())){
						SmlAppContextUtils.getSqlMarkupAbstractTemplate().destroy();
					}
					//LoggerHelper.debug(SmlAgent.class,"sml is support before "+key +"...");
				} catch (Exception e) {
					SmlAppContextUtils.getSqlMarkupAbstractTemplate().destroy();
				}finally{
					try {
						Thread.sleep(DateTools.DAY_TIME_MILLS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			protected void cleanup() {
			}
		};
		securityMaster.setDaemon(true);
		securityMaster.setName("securityMaster");
		securityMaster.start();
	}
	@Override
	public int clear(String key) {
		if(key==null||key.equals("all")){
			key="";
		}
		return SmlAppContextUtils.getSmlContextUtils().clear(key);
	}

}
