/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.framework.userpreferences;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.jnlp.BasicService;
import javax.jnlp.FileContents;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

/**
 * A preferences service that uses WebStart "muffins".
 */
@ManagedBean(registeredManually=true)
public class JNLPUserPreferencesDao implements IUserPreferencesDao
{

    public String get(PreferencesConstant key, String defval)
    {
        try
        {
            BasicService basic = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
            URL codeBase = basic.getCodeBase();

            PersistenceService service = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
            URL keyURL = new URL(codeBase, key.toString());

            FileContents contents = service.get(keyURL);
            InputStream in = contents.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String r = reader.readLine();
            if (r != null) return r;
        }
        catch (UnavailableServiceException e)
        {
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return defval;
    }

    public void put(PreferencesConstant key, String value)
    {
        try
        {
            BasicService basic = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
            URL codeBase = basic.getCodeBase();

            PersistenceService service = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
            URL keyURL = new URL(codeBase, key.toString());
            try
            {
                service.delete(keyURL);
            }
            catch (Exception ex)
            {
            }
            byte[] bytes = value.getBytes("UTF-8");
            service.create(keyURL, bytes.length);
            FileContents contents = service.get(keyURL);
            OutputStream out = contents.getOutputStream(true);
            out.write(bytes);
            out.close();
        }
        catch (UnavailableServiceException e)
        {
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void reset()
    {
        try
        {
            BasicService basic = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
            URL codeBase = basic.getCodeBase();

            PersistenceService service = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
            for (int i = 0; i < PreferencesConstant.LIST.length; i++)
            {
                URL keyURL = new URL(codeBase, PreferencesConstant.LIST[i].toString());
                try
                {
                    service.delete(keyURL);
                }
                catch (Exception ex)
                {
                }
            }
        }
        catch (UnavailableServiceException e)
        {
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

}
