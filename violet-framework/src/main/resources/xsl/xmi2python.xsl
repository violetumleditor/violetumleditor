<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:UML="org.omg.xmi.namespace.UML">
	<xsl:output encoding="ISO-8859-15" method="xml" indent="yes" />
	<xsl:strip-space elements="*" />
	
	<xsl:param name="nameDir" select="/" />
	<xsl:variable name = "lowercase" select = "'abcdefghijklmnopqrstuvwxyz_'"  />
	<xsl:variable name = "uppercase" select= "'ABCDEFGHIJKLMNOPQRSTUVWXYZ-'" />
	
	
	<xsl:template match="/">
		<Violet>
		<xsl:apply-templates select="//UML:Class[@name]" />
		</Violet>
	</xsl:template>
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create class ********************************************* -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Class">
		<xsl:variable name="name">
			<xsl:value-of select="@name" />
		</xsl:variable>
		<xsl:variable name="id">
			<xsl:value-of select="@xmi.id" />
		</xsl:variable>
		<xsl:variable name="file">
			<xsl:value-of select="translate($name, $uppercase, $lowercase)" />
			<xsl:text>.py</xsl:text>
		</xsl:variable>
		   <newFile>
		   <xsl:attribute name="path"><xsl:value-of select="$file"/></xsl:attribute>
		   <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:text>#!/usr/bin/env python&#xa;</xsl:text>
			<xsl:text>&#xa;</xsl:text>
			<xsl:text># generate by Violet Uml &#xa;</xsl:text>
			<xsl:text># </xsl:text>
			<xsl:call-template name="getClassType">
				<xsl:with-param name="node" select="." />
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="translate($name, $uppercase, $lowercase)" />
			<xsl:text>&#xa;</xsl:text>
			<xsl:text># More info here &#xa;</xsl:text>
			<xsl:text>&#xa;</xsl:text>
			
			<xsl:call-template name="getImport">
				<xsl:with-param name="node" select="." />
			</xsl:call-template>
			
			
			<xsl:text>&#xa;</xsl:text>		
			<xsl:text>__version__='$Revision: 1.1 $'&#xa;</xsl:text>
			<xsl:text>__author__=''&#xa;</xsl:text>
			<xsl:text>__date__=''&#xa;</xsl:text>
			<xsl:text>&#xa;</xsl:text>
			
			<xsl:call-template name="getClassType">
				<xsl:with-param name="node" select="." />
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="$name" />
			<xsl:text> </xsl:text>
			<xsl:variable name="compter">
				<xsl:value-of select="count(.//UML:GeneralizableElement.generalization//UML:Generalization)+count(.//UML:ModelElement.clientDependency//UML:Abstraction)" />
			</xsl:variable>
			<xsl:if test="$compter &gt; 0">
				<xsl:text>(</xsl:text>
    		</xsl:if>
			<xsl:apply-templates select=".//UML:GeneralizableElement.generalization" />
			<xsl:if test="count(.//UML:ModelElement.clientDependency//UML:Abstraction) &gt; 0">
				<xsl:text>,</xsl:text>
    		</xsl:if>
			<xsl:apply-templates select=".//UML:ModelElement.clientDependency" />
			<xsl:if test="$compter &gt; 0">
				<xsl:text>)</xsl:text>
    		</xsl:if>
			<xsl:text> :&#xa;&#xa;</xsl:text>
		    <xsl:text>&#x09;def __init__(self):&#xa;</xsl:text>
		    <xsl:text>&#x09;&#x09;"""&#xa;</xsl:text>
		    <xsl:text>&#x09;&#x09;Add description&#xa;</xsl:text>
		    <xsl:text>&#x09;&#x09;@since 1.0&#xa;</xsl:text>
		    <xsl:text>&#x09;&#x09;@author&#xa;</xsl:text>
		    <xsl:text>&#x09;&#x09;"""&#xa;</xsl:text>
			<xsl:for-each select=".//UML:Attribute">
				<xsl:text>&#x09;</xsl:text>
				<xsl:apply-templates select="."/>
				<xsl:text>&#xa;</xsl:text>
			</xsl:for-each>
			<xsl:for-each select="//UML:Association[.//UML:Class/@xmi.idref=$id]">
				<xsl:text>&#x09;</xsl:text>
				<xsl:call-template name="Assoc">
					<xsl:with-param name="node" select="." />
					<xsl:with-param name="id" select="$id" />
				</xsl:call-template>
				<xsl:text>&#xa;</xsl:text>
			</xsl:for-each>
			<xsl:text>&#xa;</xsl:text>
			<xsl:for-each select=".//UML:Operation">
				<xsl:text>&#x09;</xsl:text>
				<xsl:apply-templates select="."/>
				<xsl:text>&#xa;&#xa;</xsl:text>
			</xsl:for-each>
			
		</newFile>
	</xsl:template>
	
	<xsl:template name="getImport">
		<xsl:param name="node" />
		<xsl:for-each select="$node//UML:GeneralizableElement.generalization//UML:Generalization">
			<xsl:text>from </xsl:text>
			<xsl:variable name="name">
				<xsl:apply-templates select="."/>
			</xsl:variable>
			<xsl:value-of select="translate($name, $uppercase, $lowercase)" />			
			<xsl:text> import </xsl:text>
			<xsl:value-of select="$name" />
			<xsl:text>&#xa;</xsl:text>
		</xsl:for-each>
		<xsl:for-each select="$node//UML:ModelElement.clientDependency//UML:Abstraction">
			<xsl:text>from </xsl:text>
			<xsl:variable name="name">
				<xsl:apply-templates select="."/>
			</xsl:variable>
			<xsl:value-of select="translate($name, $uppercase, $lowercase)" />			
			<xsl:text> import </xsl:text>
			<xsl:value-of select="$name" />
			<xsl:text>&#xa;</xsl:text>
		</xsl:for-each>
		
	</xsl:template>
	
	<xsl:template name="getClassType">
		<xsl:param name="node" />
		<xsl:if test='count($node//UML:Stereotype) = 0' >
			<xsl:text>class</xsl:text>
		</xsl:if>
		<xsl:if test='count($node//UML:Stereotype) &gt; 0' >
			<xsl:variable name="id">
				<xsl:value-of select="$node//UML:Stereotype/@xmi.idref" />
			</xsl:variable>
			<xsl:value-of select="//UML:Stereotype[@xmi.id=$id]/UML:Stereotype.baseClass" />
		</xsl:if> 
	</xsl:template>
	
	<xsl:template match="UML:GeneralizableElement.generalization">
		<xsl:if test='count(./UML:Generalization) &gt; 0' >
			<xsl:for-each select="./UML:Generalization">
				<xsl:apply-templates select="." />
				<xsl:if test="position() = last()">
					<xsl:text> </xsl:text>
    			</xsl:if>
				<xsl:if test="position() != last()">
					<xsl:text>,</xsl:text>
    			</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="UML:Generalization">
			<xsl:variable name="idref">
				<xsl:value-of select="@xmi.idref" />
			</xsl:variable>
			<xsl:variable name="idrefParent">
				<xsl:value-of select="//UML:Generalization[@xmi.id=$idref]/UML:Generalization.parent/UML:Class/@xmi.idref" />
			</xsl:variable>
			<xsl:value-of select="//UML:Class[@xmi.id=$idrefParent]/@name" />
	</xsl:template>
	
	<xsl:template match="UML:ModelElement.clientDependency">
		<xsl:if test='count(./UML:Abstraction) &gt; 0' >
			<xsl:for-each select="./UML:Abstraction">
				<xsl:apply-templates select="." />
				<xsl:if test="position() = last()">
					<xsl:text> </xsl:text>
    			</xsl:if>
				<xsl:if test="position() != last()">
					<xsl:text>,</xsl:text>
    			</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="UML:Abstraction">
			<xsl:variable name="idref">
				<xsl:value-of select="@xmi.idref" />
			</xsl:variable>
			<xsl:variable name="idrefParent">
				<xsl:value-of select="//UML:Abstraction[@xmi.id=$idref]/UML:Dependency.supplier/UML:Class/@xmi.idref" />
			</xsl:variable>
			<xsl:value-of select="//UML:Class[@xmi.id=$idrefParent]/@name" />
	</xsl:template>
	
	<xsl:template match="UML:Attribute">
			<xsl:text>&#x09;self.</xsl:text>
			<xsl:if test="@visibility = 'private'">
				<xsl:text>__</xsl:text>
			</xsl:if>
			<xsl:if test="@visibility = 'protected'">
				<xsl:text>_</xsl:text>
			</xsl:if>
			<xsl:value-of select="@name" />
			<xsl:text> = </xsl:text>
			<xsl:apply-templates select=".//UML:Attribute.initialValue"/>
	</xsl:template>
	
	<xsl:template match="UML:DataType">
			<xsl:value-of select="substring-before(@xmi.idref,'-')"/>
			<!-- <xsl:variable name="idref">
				<xsl:value-of select="@xmi.idref" />
			</xsl:variable>
			<xsl:value-of select="//UML:DataType[@xmi.id=$idref]/@name" /> -->
	</xsl:template>
	
	<xsl:template match="UML:Attribute.initialValue">
			<xsl:value-of select="./UML:Expression/@body" />
			<xsl:if test="./UML:Expression/@body = ''">
				<xsl:text>None</xsl:text>
    		</xsl:if>
	</xsl:template>
	
	<xsl:template match="UML:Operation">
			<xsl:text>def </xsl:text>
			<xsl:if test="@visibility = 'private'">
				<xsl:text>__</xsl:text>
			</xsl:if>
			<xsl:if test="@visibility = 'protected'">
				<xsl:text>_</xsl:text>
			</xsl:if>
			<xsl:value-of select="@name" />
			<xsl:text>(self</xsl:text>
			<xsl:if test="count(.//UML:Parameter[@name != 'return']) &gt; 0">
				<xsl:text>,</xsl:text>
			</xsl:if>
			<xsl:for-each select=".//UML:Parameter[@name != 'return']">
				<xsl:apply-templates select="." />
				<xsl:if test="position() != last()">
					<xsl:text>,</xsl:text>
    			</xsl:if>
			</xsl:for-each>
			<xsl:text>)&#xa;</xsl:text>
			<xsl:text>&#x09;&#x09;"""&#xa;</xsl:text>
		    <xsl:text>&#x09;&#x09;Add description&#xa;</xsl:text>
		    <xsl:text>&#x09;&#x09;@since 1.0&#xa;</xsl:text>
		    <xsl:text>&#x09;&#x09;@author&#xa;</xsl:text>
		    <xsl:text>&#x09;&#x09;"""&#xa;</xsl:text>
		    <xsl:text>&#xa;&#x09;&#x09;pass&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="UML:Parameter">
			<xsl:value-of select="@name" />
			<xsl:if test=".//UML:Expression/@body != ''">
				<xsl:text> = </xsl:text>
				<xsl:value-of select=".//UML:Expression/@body" />
    		</xsl:if>
			<xsl:if test="count(.//UML:Expression/@body) = 0">
				<xsl:text> = None</xsl:text>
    		</xsl:if>
	</xsl:template>
	
	<xsl:template name="Assoc">
		<xsl:param name="node" />
		<xsl:param name="id" />
		<xsl:for-each select="$node//UML:AssociationEnd[.//UML:Class/@xmi.idref != $id]">
				<xsl:apply-templates select="." />
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="UML:AssociationEnd">
		<xsl:if test="@aggregation = 'none' and @isNavigable='true'"> 
			<xsl:text>&#x09;self._</xsl:text>
			<xsl:variable name="idref">
				<xsl:value-of select=".//UML:Class/@xmi.idref" />
			</xsl:variable>
			<xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" />
			<xsl:if test=".//UML:MultiplicityRange/@upper = '-1'">
				<xsl:text> = []</xsl:text>
    		</xsl:if>
			<xsl:if test=".//UML:MultiplicityRange/@upper != '-1'">
				<xsl:text> = None</xsl:text>
    		</xsl:if>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
