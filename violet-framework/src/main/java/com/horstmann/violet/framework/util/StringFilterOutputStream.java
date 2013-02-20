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

package com.horstmann.violet.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class is an outputstream filter. It means that it will filter the given inputstream and replace all the key ocurrences
 * issued form the map by its value.
 * 
 * Very important note : due to outputstream behaviour, it can be filtered only after having retreived all data. In other words,
 * when close() method is called. So, filtering and writing to outputstream is done during close().
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class StringFilterOutputStream extends ByteArrayOutputStream
{

    /**
     * Default constructor
     * 
     * @param out outputstream to filter
     * @param replaceMap pairs of key/value to filter
     */
    public StringFilterOutputStream(OutputStream out, Map<String, String> replaceMap)
    {
        super();
        this.replaceMap = replaceMap;
        this.originalOutputStream = out;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.ByteArrayOutputStream#close()
     */
    public void close() throws IOException
    {
        String filteredContent = getFilteredContent(this.toString("UTF-8"), this.replaceMap);
        try
        {
            this.originalOutputStream.write(filteredContent.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            this.originalOutputStream.write(filteredContent.getBytes());
        }
        this.originalOutputStream.close();
        super.close();
    }

    /**
     * Filters a string and replaces all key courrences issed from the map by its valye
     * 
     * @param input string
     * @param replaceMap key = searchedString / value = replaceString
     * @return filtered string
     */
    private String getFilteredContent(String input, Map<String, String> replaceMap)
    {
        Set<String> set = replaceMap.keySet();
        for (Iterator<String> iter = set.iterator(); iter.hasNext();)
        {
            String searchedStr = iter.next();
            String replaceStr = replaceMap.get(searchedStr);
            input = input.replaceAll(searchedStr, replaceStr);
        }
        return input;
    }

    /**
     * Map containing pairs of key/value as searchedString/replaceString
     */
    private Map<String, String> replaceMap;

    /**
     * The outputstream to filter
     */
    private OutputStream originalOutputStream;

}
