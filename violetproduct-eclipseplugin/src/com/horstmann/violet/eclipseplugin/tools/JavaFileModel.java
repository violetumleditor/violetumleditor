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

package com.horstmann.violet.eclipseplugin.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.ws.jaxme.js.JavaField;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.JavaSourceFactory;
import org.apache.ws.jaxme.js.util.JavaParser;

import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;
import com.horstmann.violet.product.diagram.communication.node.ClassNode;
import com.horstmann.violet.product.diagram.sequence.LifelineNode;

public class JavaFileModel
{

    private String className;

    private String[] classMethods;

    private String[] classAttributes;

    public JavaFileModel(File pFile)
    {
        ArrayList<String> methodsArray = new ArrayList<String>();
        ArrayList<String> attributsArray = new ArrayList<String>();
        try
        {
            JavaSourceFactory jsf = new JavaSourceFactory();
            JavaParser jp = new JavaParser(jsf);
            jp.parse(pFile);
            Iterator<?> iter = jsf.getJavaSources();
            if (iter.hasNext())
            {
                // Get main class (no inner)
                JavaSource js = (JavaSource) iter.next();
                // Get class name
                this.setClassName(js.getClassName());
                // Get methods names
                JavaMethod[] methods = js.getMethods();
                for (int i = 0; i < methods.length; i++)
                {
                    methodsArray.add(methods[i].getName());
                }
                this.setClassMethods((String[]) methodsArray.toArray(new String[methodsArray.size()]));
                // Get attributs names
                JavaField[] attributs = js.getFields();
                for (int i = 0; i < attributs.length; i++)
                {
                    attributsArray.add(attributs[i].getName());
                }
                this.setClassAttributes((String[]) attributsArray.toArray(new String[attributsArray.size()]));
            }
        }
        catch (Exception e)
        {
            this.setClassName("");
            this.setClassAttributes(new String[]
            {
                ""
            });
            this.setClassMethods(new String[]
            {
                ""
            });
            e.printStackTrace();
        }
    }

    public String[] getClassAttributes()
    {
        return classAttributes;
    }

    public void setClassAttributes(String[] classAttributes)
    {
        this.classAttributes = classAttributes;
    }

    public String[] getClassMethods()
    {
        return classMethods;
    }

    public void setClassMethods(String[] classMethods)
    {
        this.classMethods = classMethods;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public LifelineNode getLifelineNode()
    {
        MultiLineString mls;
        mls = new MultiLineString();
        mls.setText(this.getClassName());
        LifelineNode seqNode = new LifelineNode();
        seqNode.setName(mls);
        return seqNode;
    }

    public ClassNode getClassNode()
    {
        ClassNode classNode = new ClassNode();
        // Set mathods
        MultiLineString mls = new MultiLineString();
        StringBuffer text = new StringBuffer();
        for (int i = 0; i < this.getClassMethods().length; i++)
        {
            text.append(this.getClassMethods()[i]);
            text.append("\n");
        }
        mls.setText(text.toString());
        mls.setJustification(MultiLineString.LEFT);
        classNode.setMethods(mls);
        // Set attributs
        mls = new MultiLineString();
        text = new StringBuffer();
        for (int i = 0; i < this.getClassAttributes().length; i++)
        {
            text.append(this.getClassAttributes()[i]);
            text.append("\n");
        }
        mls.setText(text.toString());
        mls.setJustification(MultiLineString.LEFT);
        classNode.setAttributes(mls);
        // Set class name
        mls = new MultiLineString();
        mls.setText(this.getClassName());
        mls.setSize(MultiLineString.LARGE);
        classNode.setName(mls);
        return classNode;
    }

}
