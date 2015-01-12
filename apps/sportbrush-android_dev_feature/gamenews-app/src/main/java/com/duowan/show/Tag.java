// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 2.1.5.5
// Generated from `show.jce'
// **********************************************************************

package com.duowan.show;

public final class Tag extends com.duowan.taf.jce.JceStruct implements java.lang.Cloneable
{
    public String className()
    {
        return "show.Tag";
    }

    public String fullClassName()
    {
        return "com.duowan.show.Tag";
    }

    public int id = 0;

    public String name = "";

    public com.duowan.show.Image iconList = null;

    public int getId()
    {
        return id;
    }

    public void  setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void  setName(String name)
    {
        this.name = name;
    }

    public com.duowan.show.Image getIconList()
    {
        return iconList;
    }

    public void  setIconList(com.duowan.show.Image iconList)
    {
        this.iconList = iconList;
    }

    public Tag()
    {
        setId(id);
        setName(name);
        setIconList(iconList);
    }

    public Tag(int id, String name, com.duowan.show.Image iconList)
    {
        setId(id);
        setName(name);
        setIconList(iconList);
    }

    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }

        Tag t = (Tag) o;
        return (
            com.duowan.taf.jce.JceUtil.equals(id, t.id) && 
            com.duowan.taf.jce.JceUtil.equals(name, t.name) && 
            com.duowan.taf.jce.JceUtil.equals(iconList, t.iconList) );
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
        _os.write(id, 0);
        if (null != name)
        {
            _os.write(name, 1);
        }
        if (null != iconList)
        {
            _os.write(iconList, 2);
        }
    }

    static com.duowan.show.Image cache_iconList;

    public void readFrom(com.duowan.taf.jce.JceInputStream _is)
    {
        setId((int) _is.read(id, 0, false));

        setName( _is.readString(1, false));

        if(null == cache_iconList)
        {
            cache_iconList = new com.duowan.show.Image();
        }
        setIconList((com.duowan.show.Image) _is.read(cache_iconList, 2, false));

    }

    public void display(java.lang.StringBuilder _os, int _level)
    {
        com.duowan.taf.jce.JceDisplayer _ds = new com.duowan.taf.jce.JceDisplayer(_os, _level);
        _ds.display(id, "id");
        _ds.display(name, "name");
        _ds.display(iconList, "iconList");
    }

}
