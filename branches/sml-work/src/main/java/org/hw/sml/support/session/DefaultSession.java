package org.hw.sml.support.session;

public class DefaultSession extends Session{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int defaultMaxAccessCountOnline=-1;
	private int defaultMaxAccessCountPerDay=-1;
	public DefaultSession(){
		
	}
	public DefaultSession(int maxOnline,int defaultMaxAccessCountOnline,int defaultMaxAccessCountPerDay){
		super.maxOnline=maxOnline;
		this.defaultMaxAccessCountOnline=defaultMaxAccessCountOnline;
		this.defaultMaxAccessCountPerDay=defaultMaxAccessCountPerDay;
	}
	public void verify(String id) throws AccessUnAuthException {
		if(!accessContain.containsKey(id)){
			Access as=new Access();
			as.setId(id);
			as.setMaxAccessCountOnline(defaultMaxAccessCountOnline);
			as.setMaxAccessCountPerDay(defaultMaxAccessCountPerDay);
			accessContain.put(id, as);
		}
	}
	public int getDefaultMaxAccessCountOnline() {
		return defaultMaxAccessCountOnline;
	}
	public void setDefaultMaxAccessCountOnline(int defaultMaxAccessCountOnline) {
		this.defaultMaxAccessCountOnline = defaultMaxAccessCountOnline;
	}
	public int getDefaultMaxAccessCountPerDay() {
		return defaultMaxAccessCountPerDay;
	}
	public void setDefaultMaxAccessCountPerDay(int defaultMaxAccessCountPerDay) {
		this.defaultMaxAccessCountPerDay = defaultMaxAccessCountPerDay;
	}
	
	
}
