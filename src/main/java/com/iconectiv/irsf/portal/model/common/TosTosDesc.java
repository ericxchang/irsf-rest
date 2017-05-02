package com.iconectiv.irsf.portal.model.common;

import java.util.Objects;

public class TosTosDesc {
	
	private String tos;
	private String tosdesc;
	
	public TosTosDesc(String tos, String tosdesc) {
		this.tos = tos;
		this.tosdesc = tosdesc;
	}
	
	
	public String getTos() {
		return tos;
	}


	public void setTos(String tos) {
		this.tos = tos;
	}


	public String getTosdesc() {
		return tosdesc;
	}


	public void setTosdesc(String tosdesc) {
		this.tosdesc = tosdesc;
	}


	@Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof TosTosDesc)) {
            return false;
        }
        TosTosDesc instance = (TosTosDesc) o;
        return this.tos.equals(instance.tos) && this.tosdesc.equals(instance.tosdesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tos, tosdesc);
    }
	


}
