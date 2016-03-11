package com.supernet.api.client.instantdex;

public class orderBookBean {

	private String tag;

    private Bids[] bids;

    private String numasks;

    private String inverted;

    private String timestamp;

    private String time;

    private String numbids;

    private Asks[] asks;

    private String base;

    private String maxdepth;

    private String highbid;

    private String rel;

    private String lowask;

    private String exchange;
    
    private String error;

    public String getTag ()
    {
        return tag;
    }

    public void setTag (String tag)
    {
        this.tag = tag;
    }

    public Bids[] getBids ()
    {
        return bids;
    }

    public void setBids (Bids[] bids)
    {
        this.bids = bids;
    }

    public String getNumasks ()
    {
        return numasks;
    }

    public void setNumasks (String numasks)
    {
        this.numasks = numasks;
    }

    public String getInverted ()
    {
        return inverted;
    }

    public void setInverted (String inverted)
    {
        this.inverted = inverted;
    }

    public String getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getTime ()
    {
        return time;
    }

    public void setTime (String time)
    {
        this.time = time;
    }

    public String getNumbids ()
    {
        return numbids;
    }

    public void setNumbids (String numbids)
    {
        this.numbids = numbids;
    }

    public Asks[] getAsks ()
    {
        return asks;
    }

    public void setAsks (Asks[] asks)
    {
        this.asks = asks;
    }

    public String getBase ()
    {
        return base;
    }

    public void setBase (String base)
    {
        this.base = base;
    }

    public String getMaxdepth ()
    {
        return maxdepth;
    }

    public void setMaxdepth (String maxdepth)
    {
        this.maxdepth = maxdepth;
    }

    public String getHighbid ()
    {
        return highbid;
    }

    public void setHighbid (String highbid)
    {
        this.highbid = highbid;
    }

    public String getRel ()
    {
        return rel;
    }

    public void setRel (String rel)
    {
        this.rel = rel;
    }

    public String getLowask ()
    {
        return lowask;
    }

    public void setLowask (String lowask)
    {
        this.lowask = lowask;
    }

    public String getExchange ()
    {
        return exchange;
    }

    public void setExchange (String exchange)
    {
        this.exchange = exchange;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tag = "+tag+", bids = "+bids+", numasks = "+numasks+", inverted = "+inverted+", timestamp = "+timestamp+", time = "+time+", numbids = "+numbids+", asks = "+asks+", base = "+base+", maxdepth = "+maxdepth+", highbid = "+highbid+", rel = "+rel+", lowask = "+lowask+", exchange = "+exchange+"]";
    }

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
			
			
