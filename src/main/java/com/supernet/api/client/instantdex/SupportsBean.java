package com.supernet.api.client.instantdex;

public class SupportsBean {

	private String result;

    private String tag;
    
    private String error;

    public String getResult ()
    {
        return result;
    }

    public void setResult (String result)
    {
        this.result = result;
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
        return "ClassPojo [result = "+result+", tag = "+tag+"]";
    }

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
			
