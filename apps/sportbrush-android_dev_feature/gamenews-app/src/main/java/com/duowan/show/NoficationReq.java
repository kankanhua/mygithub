// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 2.1.5.5
// Generated from `show.jce'
// **********************************************************************

package com.duowan.show;

public final class NoficationReq extends com.duowan.taf.jce.JceStruct implements java.lang.Cloneable
{
    public String className()
    {
        return "show.NoficationReq";
    }

    public String fullClassName()
    {
        return "com.duowan.show.NoficationReq";
    }

    public int count = 0;

    public int refreshType = 0;

    public String attachInfo = "";

    public int getCount()
    {
        return count;
    }

    public void  setCount(int count)
    {
        this.count = count;
    }

    public int getRefreshType()
    {
        return refreshType;
    }

    public void  setRefreshType(int refreshType)
    {
        this.refreshType = refreshType;
    }

    public String getAttachInfo()
    {
        return attachInfo;
    }

    public void  setAttachInfo(String attachInfo)
    {
        this.attachInfo = attachInfo;
    }

    public NoficationReq()
    {
        setCount(count);
        setRefreshType(refreshType);
        setAttachInfo(attachInfo);
    }

    public NoficationReq(int count, int refreshType, String attachInfo)
    {
        setCount(count);
        setRefreshType(refreshType);
        setAttachInfo(attachInfo);
    }

    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }

        NoficationReq t = (NoficationReq) o;
        return (
            com.duowan.taf.jce.JceUtil.equals(count, t.count) && 
            com.duowan.taf.jce.JceUtil.equals(refreshType, t.refreshType) && 
            com.duowan.taf.jce.JceUtil.equals(attachInfo, t.attachInfo) );
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
        _os.write(count, 0);
        _os.write(refreshType, 1);
        if (null != attachInfo)
        {
            _os.write(attachInfo, 2);
        }
    }


    public void readFrom(com.duowan.taf.jce.JceInputStream _is)
    {
        setCount((int) _is.read(count, 0, false));

        setRefreshType((int) _is.read(refreshType, 1, false));

        setAttachInfo( _is.readString(2, false));

    }

    public void display(java.lang.StringBuilder _os, int _level)
    {
        com.duowan.taf.jce.JceDisplayer _ds = new com.duowan.taf.jce.JceDisplayer(_os, _level);
        _ds.display(count, "count");
        _ds.display(refreshType, "refreshType");
        _ds.display(attachInfo, "attachInfo");
    }

}

