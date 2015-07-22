package com.horstmann.violet.web;

import java.io.IOException;

import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WtServlet;

public class UMLEditorWebServlet extends WtServlet
{

    public UMLEditorWebServlet()
    {
        super();
    }

    @Override
    public WApplication createApplication(WEnvironment env)
    {
        /*
         * You could read information from the environment to decide whether the user has permission to start a new application
         */
        try
        {
            return new UMLEditorWebApplication(env);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }



}
