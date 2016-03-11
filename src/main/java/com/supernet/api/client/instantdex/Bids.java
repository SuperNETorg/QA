package com.supernet.api.client.instantdex;

public class Bids {

	 private String cumulative;

	    private String price;

	    private String volume;

	    private String aveprice;

	    private String offerer;

	    public String getCumulative ()
	    {
	        return cumulative;
	    }

	    public void setCumulative (String cumulative)
	    {
	        this.cumulative = cumulative;
	    }

	    public String getPrice ()
	    {
	        return price;
	    }

	    public void setPrice (String price)
	    {
	        this.price = price;
	    }

	    public String getVolume ()
	    {
	        return volume;
	    }

	    public void setVolume (String volume)
	    {
	        this.volume = volume;
	    }

	    public String getAveprice ()
	    {
	        return aveprice;
	    }

	    public void setAveprice (String aveprice)
	    {
	        this.aveprice = aveprice;
	    }

	    public String getOfferer ()
	    {
	        return offerer;
	    }

	    public void setOfferer (String offerer)
	    {
	        this.offerer = offerer;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [cumulative = "+cumulative+", price = "+price+", volume = "+volume+", aveprice = "+aveprice+", offerer = "+offerer+"]";
	    }
	}
