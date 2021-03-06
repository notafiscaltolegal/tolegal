package gov.goias.persistencia.util;

/**
 * Created by Remisson Silva on 23/09/2014.
 */
public class HqlTools
{
    private String hql = "";
    private Boolean setAndKeyword = false;

    public void appendHql(String valor)
    {
        this.hql += valor;
    }

    public void appendHqlIfNotNull(Object parametro, String valor)
    {
        if(parametro != null)
        {
            if(!setAndKeyword)
            {
                appendHql(valor);
                setAndKeyword = true;
            }
            else
                appendHql(" and " + valor);
        }
    }

    public void appendHqlIfNotNullAndNotEmpty(String parametro, String valor)
    {
        if(parametro != null && parametro.length() > 0)
        {
            if(!setAndKeyword)
            {
                appendHql(valor);
                setAndKeyword = true;
            }
            else
                appendHql(" and " + valor);
        }
    }


    public String getHql()
    {
        return this.hql;
    }
}
