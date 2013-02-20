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

/**
 * A service for storing and loading user preferences. This service uses either the standard Java preferences API or the WebStart
 * persistence service ("muffins").
 */
public abstract class PreferencesServiceFactory
{
    /**
     * Gets an instance of the service, suitable for the package of the given class.
     * 
     * @return an instance of the service
     */
    public static IUserPreferencesDao getInstance()
    {
        if (service != null) return service;
        try
        {
            service = new DefaultUserPreferencesDao();
            return service;
        }
        catch (SecurityException exception)
        {
            // that happens when we run under Web Start
        }
        try
        {
            // we load this lazily so that the JAR can load without WebStart
            service = (IUserPreferencesDao) Class.forName("com.horstmann.violet.framework.JNLPPreferencesService").newInstance();
            return service;
        }
        catch (Throwable exception)
        {
            // that happens when we are an applet
        }

        return new AppletUserPreferencesDao();
    }

    private static IUserPreferencesDao service;
}
