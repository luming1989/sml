package com.eastcom_sw.inas.core.service.jdbc;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.hw.sml.tools.JAXBTools;

public class JdbcFFTemplate extends AbstractJdbcFTemplate {
	private String configFilePath;

	@Override
	public SqlTemplate getSqlTemplate(String id) {
		SqlTemplate stp= getSqlTemplateWithOutCache(id);
		reInitSqlTemplate(stp);
		return stp;
	}
	public String getConfigFilePath() {
		return configFilePath;
	}
	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}
	private SqlTemplate getSqlTemplateWithOutCache(String id) {
		try {
			SqlTemplate st=(SqlTemplate) JAXBTools.unmarshal(new File(configFilePath+File.separator+id+".xml"), SqlTemplate.class);
			return st;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

}
