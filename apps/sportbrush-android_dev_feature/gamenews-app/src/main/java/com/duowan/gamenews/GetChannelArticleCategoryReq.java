// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 2.1.5.5
// Generated from `gamenews.jce'
// **********************************************************************

package com.duowan.gamenews;

public final class GetChannelArticleCategoryReq extends com.duowan.taf.jce.JceStruct implements java.lang.Cloneable
{
    public String className()
    {
        return "gamenews.GetChannelArticleCategoryReq";
    }

    public String fullClassName()
    {
        return "com.duowan.gamenews.GetChannelArticleCategoryReq";
    }

    public int channelId = 0;

    public int getChannelId()
    {
        return channelId;
    }

    public void  setChannelId(int channelId)
    {
        this.channelId = channelId;
    }

    public GetChannelArticleCategoryReq()
    {
        setChannelId(channelId);
    }

    public GetChannelArticleCategoryReq(int channelId)
    {
        setChannelId(channelId);
    }

    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }

        GetChannelArticleCategoryReq t = (GetChannelArticleCategoryReq) o;
        return (
            com.duowan.taf.jce.JceUtil.equals(channelId, t.channelId) );
    }

    public int hashCode()
    {
        try
        {
            throw new Exception("Need define key first!");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }
    public java.lang.Object clone()
    {
        java.lang.Object o = null;
        try
        {
            o = super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return o;
    }

    public void writeTo(com.duowan.taf.jce.JceOutputStream _os)
    {
        _os.write(channelId, 0);
    }


    public void readFrom(com.duowan.taf.jce.JceInputStream _is)
    {
        setChannelId((int) _is.read(channelId, 0, false));

    }

    public void display(java.lang.StringBuilder _os, int _level)
    {
        com.duowan.taf.jce.JceDisplayer _ds = new com.duowan.taf.jce.JceDisplayer(_os, _level);
        _ds.display(channelId, "channelId");
    }

}

