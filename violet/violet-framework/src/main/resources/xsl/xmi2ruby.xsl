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
			<xsl:text>.rb</xsl:text>
		</xsl:variable>
		   <newFile>
		   <xsl:attribute name="path"><xsl:value-of select="$file"/></xsl:attribute>
		   <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:text>#!/usr/bin/env ruby&#xa;</xsl:text>
			<xsl:text>&#xa;</xsl:text>
			<xsl:text># generate by Violet Uml &#xa;</xsl:text>
			<xsl:text># class </xsl:text>
			<xsl:text> </xsl:text>
			<xsl:value-of select="translate($name, $uppercase, $lowercase)" />
			<xsl:text>&#xa;</xsl:text>
			<xsl:text># More info here &#xa;</xsl:text>
			<xsl:text>&#xa;</xsl:text>
			<xsl:text>class </xsl:text>
			<xsl:value-of select="$name" />
			<xsl:text> </xsl:text>
			<xsl:variable name="compter">
				<xsl:value-of select="count(.//UML:GeneralizableElement.generalization//UML:Generalization)+count(.//UML:ModelElement.clientDependency//UML:Abstraction)" />
			</xsl:variable>
			<xsl:if test="$compter &gt; 0">
				<xsl:text> &lt; </xsl:text>
    		</xsl:if>
			<xsl:apply-templates select=".//UML:GeneralizableElement.generalization" />
			<xsl:if test="count(.//UML:ModelElement.clientDependency//UML:Abstraction) &gt; 0">
				<xsl:text>,</xsl:text>
    		</xsl:if>
			<xsl:apply-templates select=".//UML:ModelElement.clientDependency" />
			<xsl:text>&#xa;&#xa;</xsl:text>
			
			<xsl:for-each select=".//UML:Attribute">
				<xsl:if test="@visibility= 'public'">
					<xsl:text>&#x09;attr(:</xsl:text>
					<xsl:value-of select="@name" />
					<xsl:text>,true)&#xa;</xsl:text>
				</xsl:if>
				<xsl:if test="@visibility= 'protected'">
					<xsl:text>&#x09;&#x09;attr(:</xsl:text>
					<xsl:value-of select="@name" />
					<xsl:text>,false)&#xa;</xsl:text>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="count(.//UML:Attribute) &gt; 0">
				<xsl:text>&#xa;</xsl:text>		
			</xsl:if>
			
			
		    <xsl:text>&#x09;def initialize</xsl:text>
			<xsl:if test="count(.//UML:Attribute) &gt; 0">
				<xsl:text>(</xsl:text>
    		</xsl:if>
			<xsl:for-each select=".//UML:Attribute">
				<xsl:apply-templates select="."/>
				<xsl:if test="position() != last()">
					<xsl:text>,</xsl:text>
    			</xsl:if>
			</xsl:for-each>
			
			<xsl:if test="count(.//UML:Attribute) &gt; 0">
				<xsl:text>)</xsl:text>
    		</xsl:if>
			<xsl:text>&#xa;</xsl:text>
			<xsl:text>&#x09;&#x09;# todo&#xa;</xsl:text>
			<xsl:for-each select=".//UML:Attribute">
				<xsl:text>&#x09;&#x09;@</xsl:text>
				<xsl:value-of select="@name" />
				<xsl:text> = </xsl:text>
				<xsl:value-of select="@name" />
				<xsl:text>&#xa;</xsl:text>
			</xsl:for-each>			
			<xsl:for-each select="//UML:Association[.//UML:Class/@xmi.idref=$id]">
				<xsl:call-template name="Assoc">
					<xsl:with-param name="node" select="." />
					<xsl:with-param name="id" select="$id" />
				</xsl:call-template>
			</xsl:for-each>
			<xsl:text>&#x09;end&#xa;</xsl:text>
			
			<xsl:text>&#xa;</xsl:text>
			<xsl:for-each select=".//UML:Operation">
				<xsl:apply-templates select="."/>
				<xsl:text>&#xa;&#xa;</xsl:text>
			</xsl:for-each>			
			<xsl:text>end </xsl:text>
			
		</newFile>
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
			<xsl:value-of select="@name" />
			<xsl:apply-templates select=".//UML:Attribute.initialValue"/>
	</xsl:template>
	
	<xsl:template match="UML:DataType">
			<xsl:value-of select="substring-before(@xmi.idref,'-')"/>
	</xsl:template>
	
	<xsl:template match="UML:Attribute.initialValue">
			<xsl:if test="./UML:Expression/@body != ''">
				<xsl:text>=</xsl:text>
				<xsl:value-of select="./UML:Expression/@body" />
    		</xsl:if>
	</xsl:template>
	
	<xsl:template match="UML:Operation">
			<xsl:text>&#x09;</xsl:text>
			<xsl:value-of select="@visibility"/>
			<xsl:text>&#xa;&#x09;</xsl:text>
			<xsl:text>def </xsl:text>
			<xsl:value-of select="@name" />
			<xsl:if test="count(.//UML:Parameter[@name != 'return']) &gt; 0">
				<xsl:text>(</xsl:text>
			</xsl:if>
			<xsl:for-each select=".//UML:Parameter[@name != 'return']">
				<xsl:apply-templates select="." />
				<xsl:if test="position() != last()">
					<xsl:text>,</xsl:text>
    			</xsl:if>
			</xsl:for-each>
			<xsl:if test="count(.//UML:Parameter[@name != 'return']) &gt; 0">
				<xsl:text>)</xsl:text>
			</xsl:if>
			<xsl:text>&#xa;</xsl:text>
			<xsl:text>&#x09;&#x09;# todo&#xa;</xsl:text>
			<xsl:text>&#x09;end</xsl:text>
	</xsl:template>
	
	<xsl:template match="UML:Parameter">
			<xsl:value-of select="@name" />
			<xsl:if test=".//UML:Expression/@body != ''">
				<xsl:text> = </xsl:text>
				<xsl:value-of select=".//UML:Expression/@body" />
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
			<xsl:text>&#x09;&#x09;@</xsl:text>
			<xsl:variable name="idref">
				<xsl:value-of select=".//UML:Class/@xmi.idref" />
			</xsl:variable>
			<xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" />
			<xsl:text> = </xsl:text>
			<xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" />
			<xsl:text>&#xa;</xsl:text>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
