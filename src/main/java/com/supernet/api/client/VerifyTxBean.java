package com.supernet.api.client;

public class VerifyTxBean {
	
	 private String error;

	    private String tag;

	    public String getError ()
	    {
	        return error;
	    }

	    public void setError (String error)
	    {
	        this.error = error;
	    }

	    public String getTag ()
	    {
	        return tag;
	    }

	    public void setTag (String tag)
	    {
	        this.tag = tag;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [error = "+error+", tag = "+tag+"]";
	    }
	}
				
