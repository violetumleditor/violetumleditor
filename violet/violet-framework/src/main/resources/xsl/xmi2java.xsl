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
			<xsl:value-of select="@name" />
			<xsl:text>.java</xsl:text>
		</xsl:variable>
		   <newFile>
		   <xsl:attribute name="path"><xsl:value-of select="$file"/></xsl:attribute>
		   <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:text>/**&#xa;</xsl:text>
			<xsl:text>* generate by Violet Uml &#xa;</xsl:text>
			<xsl:text>* </xsl:text>
			<xsl:call-template name="getClassType">
				<xsl:with-param name="node" select="." />
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>&#xa;</xsl:text>
			<xsl:text>* More info here &#xa;</xsl:text>
			<xsl:text>*/&#xa;&#xa;</xsl:text>
			<xsl:value-of select="@visibility" />
			<xsl:text> </xsl:text>
			<xsl:call-template name="getClassType">
				<xsl:with-param name="node" select="." />
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="$name" />
			<xsl:text> </xsl:text>
			<xsl:apply-templates select=".//UML:GeneralizableElement.generalization" />
			<xsl:apply-templates select=".//UML:ModelElement.clientDependency" />
			<xsl:text> {&#xa;&#xa;</xsl:text>
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
			<xsl:text>}</xsl:text>
			
		   </newFile>
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
			<xsl:text>extends </xsl:text>
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
			<xsl:text>implements </xsl:text>
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
			<xsl:value-of select="@visibility" />
			<xsl:text> </xsl:text>
			<xsl:apply-templates select=".//UML:DataType"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@name" />
			<xsl:if test="count(.//UML:Attribute.initialValue/UML:Expression[@body != '']) &gt; 0" >
				<xsl:text> = </xsl:text>
				<xsl:apply-templates select=".//UML:Attribute.initialValue"/>
			</xsl:if>
			<xsl:text>;</xsl:text>
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
	</xsl:template>
	
	<xsl:template match="UML:Operation">
			<xsl:value-of select="@visibility" />
			<xsl:text> </xsl:text>
			<xsl:apply-templates select=".//UML:Parameter[@name='return']//UML:DataType"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>(</xsl:text>
			<xsl:for-each select=".//UML:Parameter[@name != 'return']">
				<xsl:apply-templates select="." />
				<xsl:if test="position() != last()">
					<xsl:text>,</xsl:text>
    			</xsl:if>
			</xsl:for-each>
			<xsl:text>){&#xa;&#xa;&#x09;}</xsl:text>
	</xsl:template>
	
	<xsl:template match="UML:Parameter">
			<xsl:apply-templates select=".//UML:DataType"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@name" />	
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
			<xsl:text>private </xsl:text>
			<xsl:variable name="idref">
				<xsl:value-of select=".//UML:Class/@xmi.idref" />
			</xsl:variable>
			<xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" />
			<xsl:if test=".//UML:MultiplicityRange/@upper = '-1'">
				<xsl:text>[]</xsl:text>
    		</xsl:if>
			<xsl:text> </xsl:text>
			<xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" />
			<xsl:text>s</xsl:text>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
