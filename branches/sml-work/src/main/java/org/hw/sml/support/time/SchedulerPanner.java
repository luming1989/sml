package org.hw.sml.support.time;

import java.util.Enumeration;
import java.util.Map;

import org.hw.sml.FrameworkConstant;
import org.hw.sml.support.LoggerHelper;
import org.hw.sml.support.ioc.BeanHelper;
import org.hw.sml.support.queue.ManagedQuene;
import org.hw.sml.support.queue.Task;
import org.hw.sml.tools.ClassUtil;
import org.hw.sml.tools.MapUtils;

public class SchedulerPanner extends ManagedQuene{
	private static Map<String,String> taskMapContain=MapUtils.newHashMap();
	public void init(){
		Enumeration<Object> keys=FrameworkConstant.otherProperties.keys();
		while(keys.hasMoreElements()){
			String key=(String) keys.nextElement();
			if(key.startsWith("task-")&&key.contains("-")){
				String beanMethod=key.replace("task-","");
				if(beanMethod.split("\\.").length==1){
					LoggerHelper.warn(getClass(),key+" is error!");
					continue;
				}
				taskMapContain.put(beanMethod,BeanHelper.getValue(key));
			}
		}
		LoggerHelper.info(getClass(),"task["+taskMapContain+"]");
		if(taskMapContain.size()>0){
			super.init();
			Scheduler sd=new Scheduler();
			sd.setTask(new TimerTask() {
				public void execute() {
					task();
				}
			});
			sd.setDelay(60);
			sd.init();
		}
	}
	public void task(){
		for(Map.Entry<String,String> entry:taskMapContain.entrySet()){
			try {
				final String key=entry.getKey();
				String value=entry.getValue();
				TaskModel tm=new TaskModel();
				tm.setElp(value);
				tm.init();
				if(!tm.isExecuteNow()){
					continue;
				}
				super.add(new Task() {
					String[] bm=key.split("-")[1].split("\\.");
					String bn=bm[0];
					String mn=bm[1];
					Object bean=BeanHelper.getBean(bn);
					Class c=bean.getClass();
					public void execute() throws Exception {
						ClassUtil.getMethod(c,mn).invoke(bean,new Object[]{});
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
}
