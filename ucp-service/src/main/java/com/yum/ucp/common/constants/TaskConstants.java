package com.yum.ucp.common.constants;

public class TaskConstants {
	
	public enum TaskType {
		CONFIG, CHECK;
	}
	
	public enum ModuleActRowStatus{
		PENDING("0","未完成"), COMPLETED("1","已完成");

		ModuleActRowStatus(String abbr, String value) {
			this.abbr = abbr;
			this.value = value;
		}

		public String abbr;
		public String value;
		
		public String getAbbr() {
			return abbr;
		}

		public void setAbbr(String abbr) {
			this.abbr = abbr;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
