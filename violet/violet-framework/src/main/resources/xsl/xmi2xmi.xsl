<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:UML = 'org.omg.xmi.namespace.UML'
	xmlns:violet = 'http://www.violet.org/'>
	<xsl:output 
  		encoding="ISO-8859-15"
  		method="xml"
  		indent="yes" />

	<xsl:template match="UML:Namespace.ownedElement">
		<UML:Namespace.ownedElement>
			<xsl:apply-templates select="@* | node()"/>
			<xsl:call-template name="DataType" />
			<xsl:call-template name="Stereotype" />
		</UML:Namespace.ownedElement>
	</xsl:template>
	<!-- ************************************************************************************** -->
	<!-- ********************************** UML:DataType *********************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template name="DataType">
		<xsl:for-each select=".//UML:DataType">	    
	    <xsl:if test="count(@xmi.idref)  &gt;  0">
  		<UML:DataType>
 			<xsl:attribute name="xmi.id"><xsl:value-of select="@xmi.idref"/></xsl:attribute>
    	    <xsl:attribute name="name"><xsl:value-of select="substring-before(@xmi.idref,'-')"/></xsl:attribute>
    	    <xsl:attribute name="isSpecification">false</xsl:attribute>
       		<xsl:attribute name="isRoot">false</xsl:attribute>
       		<xsl:attribute name="isLeaf">false</xsl:attribute>
      		<xsl:attribute name="isAbstract">false</xsl:attribute>
     	</UML:DataType>
	    </xsl:if>
	    </xsl:for-each>
	</xsl:template>
	

	<!-- ************************************************************************************** -->
	<!-- ********************************** UML:Stereotype *********************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template name="Stereotype">
		<xsl:for-each select=".//UML:Stereotype">	  
	    <xsl:if test="count(@xmi.idref)  &gt;  0">
  		<UML:Stereotype>
 			<xsl:attribute name="xmi.id"><xsl:value-of select="@xmi.idref"/></xsl:attribute>
    	    <xsl:attribute name="name"><xsl:value-of select="substring-before(@xmi.idref,'-')"/></xsl:attribute>
    	    <xsl:attribute name="isSpecification">false</xsl:attribute>
       		<xsl:attribute name="isRoot">false</xsl:attribute>
       		<xsl:attribute name="isLeaf">false</xsl:attribute>
      		<xsl:attribute name="isAbstract">false</xsl:attribute>
            <UML:Stereotype.baseClass>Class</UML:Stereotype.baseClass>
            	<!-- <xsl:value-of select="substring-before(@xmi.idref,'-')"/> 
            </UML:Stereotype.baseClass>-->
     	</UML:Stereotype>
	    </xsl:if>
	    </xsl:for-each>
	</xsl:template>
	
	<xsl:template match="node() | @*">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="*[namespace-uri()='http://www.violet.org/']"/>
</xsl:stylesheet>

