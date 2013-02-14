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
		   <newFile>
		   		<xsl:attribute name="path">export.sql</xsl:attribute>
		  		<xsl:attribute name="name">export.sql</xsl:attribute>
				<xsl:text>--&#xa;</xsl:text>
				<xsl:text>-- generate by Violet&#xa;</xsl:text>
				<xsl:text>--&#xa;</xsl:text>
				<xsl:apply-templates select="//UML:Class[@name]" />
				<xsl:text>&#xa;</xsl:text>
				<xsl:apply-templates select="//UML:Association" />
				<xsl:text>&#xa;</xsl:text>
				<xsl:apply-templates select="//UML:Generalization" />
		   </newFile>
		</Violet>
	</xsl:template>
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create table ********************************************* -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Class">
		<xsl:variable name="name">
			<xsl:value-of select="@name" />
		</xsl:variable>
		<xsl:variable name="id">
			<xsl:value-of select="@xmi.id" />
		</xsl:variable>		
		<xsl:text>&#xa;-- table </xsl:text>
		<xsl:value-of select="@name" />		
		<xsl:text>&#xa;</xsl:text>
		<xsl:for-each select=".//UML:Attribute">
			<xsl:call-template name="seq">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="table" select="$name" />
			</xsl:call-template>
		</xsl:for-each>
		<xsl:text>&#xa;</xsl:text>
		<xsl:text>create table </xsl:text>
		<xsl:value-of select="@name" />
		<xsl:text>(&#xa;</xsl:text>
		<xsl:for-each select=".//UML:Attribute">
			<xsl:text>&#x09;</xsl:text>
			<xsl:apply-templates select="."/>
			<xsl:if test="not(./UML:Attribute.initialValue/UML:Expression/@body = '')">
				<xsl:text> DEFAULT </xsl:text>
				<xsl:value-of select="./UML:Attribute.initialValue/UML:Expression/@body" />	
			</xsl:if>
			<xsl:if test="contains(.//UML:DataType/@xmi.idref, '++')">
				<xsl:text> DEFAULT nextval('</xsl:text>
				<xsl:value-of select="$name" />	
				<xsl:text>_</xsl:text>
				<xsl:value-of select="@name" />	
				<xsl:text>')</xsl:text>
			</xsl:if>
			<xsl:if test="not(position()=last())">
				<xsl:text>,</xsl:text>
			</xsl:if>
			<xsl:text>&#xa;</xsl:text>
		</xsl:for-each>
		<xsl:text>);&#xa;</xsl:text>
		<xsl:call-template name="pk">
			<xsl:with-param name="node" select="." />
		</xsl:call-template>
		<xsl:call-template name="pu">
			<xsl:with-param name="node" select="." />
		</xsl:call-template>
		<xsl:text>&#xa;</xsl:text>
		<xsl:apply-templates select=".//UML:Operation[@name='getPK']"/>
		<xsl:apply-templates select=".//UML:Operation[@name='getFK']"/>
		<xsl:apply-templates select=".//UML:Operation[@name='getIDX']"/>
		<xsl:apply-templates select=".//UML:Operation[@name='getIDU']"/>
		
	</xsl:template>
	
	<xsl:template match="UML:Operation[@name='getPK']">
		<xsl:variable name="pkvalue">
			<xsl:for-each select=".//UML:Parameter[@name != 'return']">
				<xsl:value-of select="./@name"/>
			</xsl:for-each>
		</xsl:variable>
		
		<xsl:text>ALTER TABLE </xsl:text>
		<xsl:value-of select="./../../@name" />
		<xsl:text> ADD CONSTRAINT PK_</xsl:text>
		<xsl:value-of select="./../../@name" />
		<xsl:text> PRIMARY KEY(</xsl:text>
		<xsl:value-of select="$pkvalue"/>
		<xsl:text>);&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="UML:Operation[@name='getFK']">
		
		<xsl:text>ALTER TABLE </xsl:text>
		<xsl:value-of select="./../../@name" />
		<xsl:text> ADD CONSTRAINT FK_</xsl:text>
		<xsl:value-of select=".//UML:Parameter[@name != 'return'][1]/@name" />
		<xsl:text> FOREIGN KEY (</xsl:text>
		<xsl:value-of select=".//UML:Parameter[@name != 'return'][3]/@name"/>
		<xsl:text>) REFERENCES </xsl:text>
		<xsl:value-of select=".//UML:Parameter[@name != 'return'][1]/@name"/>
		<xsl:text>(</xsl:text>
		<xsl:value-of select=".//UML:Parameter[@name != 'return'][2]/@name"/>
		<xsl:text>);&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="UML:Operation[@name='getIDX']">
		<xsl:variable name="idxvalue">
			<xsl:for-each select=".//UML:Parameter[@name != 'return']">
				<xsl:value-of select="./@name"/>
			</xsl:for-each>
		</xsl:variable>
		
		<xsl:text>CREATE INDEX </xsl:text>
		<xsl:value-of select="./../../@name" />
		<xsl:value-of select="position()" />
		<xsl:text>_IDX ON </xsl:text>
		<xsl:value-of select="./../../@name" />
		<xsl:text> (</xsl:text>
		<xsl:value-of select="$idxvalue"/>
		<xsl:text>);&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="UML:Operation[@name='getIDU']">
		<xsl:variable name="idxvalue">
			<xsl:for-each select=".//UML:Parameter[@name != 'return']">
				<xsl:value-of select="./@name"/>
			</xsl:for-each>
		</xsl:variable>
		
		<xsl:text>CREATE UNIQUE INDEX </xsl:text>
		<xsl:value-of select="./../../@name" />
		<xsl:value-of select="position()" />
		<xsl:text>_IDX ON </xsl:text>
		<xsl:value-of select="./../../@name" />
		<xsl:text> (</xsl:text>
		<xsl:value-of select="$idxvalue"/>
		<xsl:text>);&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="UML:Attribute">
		<xsl:value-of select="@name" />
		<xsl:text> </xsl:text>
		<xsl:apply-templates select=".//UML:DataType"/>
	</xsl:template>
	
	<xsl:template match="UML:DataType">
		<xsl:variable name="value" select="substring-before(@xmi.idref,'-')"/>
		<xsl:choose>
			<xsl:when test="contains($value, '++')">
				<xsl:value-of select="substring-before($value,'++')" />				
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$value" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="seq">
		<xsl:param name="node" />
		<xsl:param name="table" />
		<xsl:if test="contains($node//UML:DataType/@xmi.idref, '++')">
			<xsl:text>CREATE SEQUENCE </xsl:text>
			<xsl:value-of select="$table" /> 
			<xsl:text>_</xsl:text>
			<xsl:value-of select="@name" /> 
			<xsl:text> INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;&#xa;</xsl:text>
			<!-- <xsl:text> SELECT setval('</xsl:text>
			<xsl:value-of select="$table" /> 
			<xsl:text>_</xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>', 1);&#xa;</xsl:text>
			<xsl:text> use sequence by nextval('</xsl:text>
			<xsl:value-of select="$table" /> 
			<xsl:text>_</xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>')&#xa;</xsl:text> -->
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="pk">
		<xsl:param name="node" />
		<xsl:if test="count($node//UML:Attribute[@visibility='private']) &gt; 0">
			<xsl:variable name="pkvalue">
				<xsl:for-each select="$node//UML:Attribute[@visibility='private']">
					<xsl:value-of select="./@name"/>
				</xsl:for-each>
			</xsl:variable>
			
			<xsl:text>ALTER TABLE </xsl:text>
			<xsl:value-of select="$node/@name" />
			<xsl:text> ADD CONSTRAINT PK_</xsl:text>
			<xsl:value-of select="$node/@name" />
			<xsl:text> PRIMARY KEY(</xsl:text>
			<xsl:value-of select="$pkvalue"/>
			<xsl:text>);</xsl:text>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="pu">
		<xsl:param name="node" />
		<xsl:if test="count($node//UML:Attribute[@visibility='protected']) &gt; 0">
			<xsl:text>&#xa;</xsl:text>
			<xsl:variable name="idxvalue">
				<xsl:for-each select="$node//UML:Attribute[@visibility='protected']">
					<xsl:value-of select="./@name"/>
				</xsl:for-each>
			</xsl:variable>
			<xsl:text>CREATE UNIQUE INDEX </xsl:text>
			<xsl:value-of select="$node/@name" />
			<xsl:value-of select="position()" />
			<xsl:text>_IDX ON </xsl:text>
			<xsl:value-of select="$node/@name" />
			<xsl:text> (</xsl:text>
			<xsl:value-of select="$idxvalue"/>
			<xsl:text>);</xsl:text>
		</xsl:if>
	</xsl:template>
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Association *************************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Association">
		<xsl:variable name="startId"><xsl:value-of select=".//UML:AssociationEnd[1]//UML:Class/@xmi.idref"/></xsl:variable>
		<xsl:variable name="startLower"><xsl:value-of select=".//UML:AssociationEnd[1]//UML:MultiplicityRange[@xmi.id]/@lower"/></xsl:variable>
		<xsl:variable name="startUpper"><xsl:value-of select=".//UML:AssociationEnd[1]//UML:MultiplicityRange[@xmi.id]/@upper"/></xsl:variable>
		
		<xsl:variable name="endId"><xsl:value-of select=".//UML:AssociationEnd[2]//UML:Class/@xmi.idref"/></xsl:variable>
		<xsl:variable name="endLower"><xsl:value-of select=".//UML:AssociationEnd[2]//UML:MultiplicityRange[@xmi.id]/@lower"/></xsl:variable>
		<xsl:variable name="endUpper"><xsl:value-of select=".//UML:AssociationEnd[2]//UML:MultiplicityRange[@xmi.id]/@upper"/></xsl:variable>
			
		
		<xsl:choose>
			<xsl:when test="($startUpper = 1 and $endUpper &lt; 1) or ($startUpper &lt; 1 and $endUpper = 1)">
				<!-- relation 1 vers N -->
				<xsl:variable name="idPere"><xsl:if test="$endUpper &lt; 1"><xsl:value-of select="$endId"/></xsl:if><xsl:if test="$startUpper &lt; 1"><xsl:value-of select="$startId"/></xsl:if></xsl:variable>
				<xsl:variable name="idFils"><xsl:if test="$endUpper &lt; 1"><xsl:value-of select="$startId"/></xsl:if><xsl:if test="$startUpper &lt; 1"><xsl:value-of select="$endId"/></xsl:if></xsl:variable>
				
				<xsl:variable name="pkvalue"><xsl:for-each select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']"><xsl:value-of select="./@name"/></xsl:for-each></xsl:variable>
				
				<xsl:if test="count(//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']) &gt; 0">
					<xsl:text>ALTER TABLE </xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
					<xsl:text> ADD COLUMN </xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
					<xsl:text> </xsl:text>
					<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/.//UML:DataType"/>
					<xsl:text> ;&#xa;</xsl:text>
				
					<xsl:text>ALTER TABLE </xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
					<xsl:text> ADD CONSTRAINT FK_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
					<xsl:text> FOREIGN KEY (</xsl:text>		
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
					<xsl:text>) REFERENCES </xsl:text>	
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>	
					<xsl:text>(</xsl:text>		
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>) MATCH FULL;</xsl:text>
					<xsl:text>&#xa;</xsl:text>
				</xsl:if>
				
			</xsl:when>
			<xsl:when test="($startUpper = 1 and $endUpper = 1) and ($startLower = $endLower)">
				<!-- relation 1 vers 1 -->
				<xsl:variable name="idPere">
				<xsl:choose >
        			<xsl:when test = "(.//UML:AssociationEnd[2]/@aggregation='composite') or (.//UML:AssociationEnd[2]/@aggregation='aggregate')" >
						<xsl:value-of select="$endId"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:value-of select="$startId"/>
            		</xsl:otherwise>
        		</xsl:choose> 
				</xsl:variable>
				<xsl:variable name="idFils">
				<xsl:choose >
        			<xsl:when test = "(.//UML:AssociationEnd[2]/@aggregation='composite') or (.//UML:AssociationEnd[2]/@aggregation='aggregate')" >
						<xsl:value-of select="$startId"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:value-of select="$endId"/>
            		</xsl:otherwise>
        		</xsl:choose>
        		</xsl:variable>
				<xsl:variable name="pkvalue"><xsl:for-each select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']"><xsl:value-of select="./@name"/></xsl:for-each></xsl:variable>
				
				<xsl:if test="count(//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']) &gt; 0">
					<xsl:text>ALTER TABLE </xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
					<xsl:text> ADD COLUMN </xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
					<xsl:text> </xsl:text>
					<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/.//UML:DataType"/>
					<xsl:text> ;&#xa;</xsl:text>
				
					<xsl:text>ALTER TABLE </xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
					<xsl:text> ADD CONSTRAINT FK_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
					<xsl:text> FOREIGN KEY (</xsl:text>		
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
					<xsl:text>) REFERENCES </xsl:text>	
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>	
					<xsl:text>(</xsl:text>		
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>) MATCH FULL;</xsl:text>	
					<xsl:text>&#xa;</xsl:text>		
				</xsl:if>
			</xsl:when>
			<xsl:when test="($startUpper = 1 and $endUpper = 1) and ($startLower != $endLower)">
				<!-- relation 1 vers 1 -->
				<xsl:variable name="idPere">
				<xsl:choose >
        			<xsl:when test = "$startLower = 1" >
						<xsl:value-of select="$endId"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:value-of select="$startId"/>
            		</xsl:otherwise>
        		</xsl:choose> 
				</xsl:variable>
				<xsl:variable name="idFils">
				<xsl:choose >
        			<xsl:when test = "$startLower = 1" >
						<xsl:value-of select="$startId"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:value-of select="$endId"/>
            		</xsl:otherwise>
        		</xsl:choose>
        		</xsl:variable>
				<xsl:variable name="pkvalue"><xsl:for-each select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']"><xsl:value-of select="./@name"/></xsl:for-each></xsl:variable>
				
				<xsl:if test="count(//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']) &gt; 0">
					<xsl:text>ALTER TABLE </xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
					<xsl:text> ADD COLUMN </xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
					<xsl:text> </xsl:text>
					<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/.//UML:DataType"/>
					<xsl:text> ;&#xa;</xsl:text>
				
					<xsl:text>ALTER TABLE </xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
					<xsl:text> ADD CONSTRAINT FK_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
					<xsl:text> FOREIGN KEY (</xsl:text>		
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
					<xsl:text>) REFERENCES </xsl:text>	
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>	
					<xsl:text>(</xsl:text>		
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>) MATCH FULL;</xsl:text>	
					<xsl:text>&#xa;</xsl:text>		
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<!-- relation N vers N -->
				<xsl:variable name="idPere"><xsl:value-of select="$endId"/></xsl:variable>
				<xsl:variable name="idFils"><xsl:value-of select="$startId"/></xsl:variable>
				<xsl:variable name="name">
					<xsl:if test="string-length(@name) &gt; 0"><xsl:value-of select="@name"/></xsl:if>
					<xsl:if test="string-length(@name) = 0"><xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>_<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/></xsl:if>
				</xsl:variable>
				
				<xsl:if test="count(//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']) &gt; 0 and count(//UML:Class[@xmi.id=$idPere]//UML:Attribute[@visibility='private']) &gt; 0 ">
					<xsl:text>&#xa;</xsl:text>
					<xsl:text>create table </xsl:text>
					<xsl:value-of select="$name" />
					<xsl:text>(&#xa;</xsl:text>
					<xsl:for-each select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']">
						<xsl:value-of select="@name"/>
						<xsl:text>_</xsl:text>
						<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
						<xsl:text> </xsl:text>
						<xsl:apply-templates select=".//UML:DataType"/>
						<xsl:if test="not(position()=last())">
							<xsl:text>,</xsl:text>
						</xsl:if>
					</xsl:for-each>
					<xsl:text>,&#xa;</xsl:text>
					<xsl:for-each select="//UML:Class[@xmi.id=$idPere]//UML:Attribute[@visibility='private']">
						<xsl:value-of select="@name"/>
						<xsl:text>_</xsl:text>
						<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
						<xsl:text> </xsl:text>
						<xsl:apply-templates select=".//UML:DataType"/>
						<xsl:if test="not(position()=last())">
							<xsl:text>,</xsl:text>
						</xsl:if>
					</xsl:for-each>
					<xsl:text>&#xa;</xsl:text>
					<xsl:text>);&#xa;</xsl:text>
				
					<xsl:text>ALTER TABLE </xsl:text>
					<xsl:value-of select="$name"/>
					<xsl:text> ADD CONSTRAINT FK_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
					<xsl:text> FOREIGN KEY (</xsl:text>		
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
					<xsl:text>) REFERENCES </xsl:text>	
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>	
					<xsl:text>(</xsl:text>		
					<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>) MATCH FULL;</xsl:text>
					<xsl:text>&#xa;</xsl:text>
					
					<xsl:text>ALTER TABLE </xsl:text>
					<xsl:value-of select="$name"/>
					<xsl:text> ADD CONSTRAINT FK_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
					<xsl:text> FOREIGN KEY (</xsl:text>		
					<xsl:value-of select="//UML:Class[@xmi.id=$idPere]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>_</xsl:text>
					<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
					<xsl:text>) REFERENCES </xsl:text>	
					<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>	
					<xsl:text>(</xsl:text>		
					<xsl:value-of select="//UML:Class[@xmi.id=$idPere]//UML:Attribute[@visibility='private']/@name"/>
					<xsl:text>) MATCH FULL;&#xa;</xsl:text>
					
					<xsl:text>CREATE UNIQUE INDEX </xsl:text>
					<xsl:value-of select="$name" />
					<xsl:text>_IDX ON </xsl:text>
					<xsl:value-of select="$name" /> 
					<xsl:text> (</xsl:text>
					<xsl:for-each select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']">
						<xsl:value-of select="@name"/>
						<xsl:text>_</xsl:text>
						<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
						<xsl:if test="not(position()=last())">
							<xsl:text>,</xsl:text>
						</xsl:if>
					</xsl:for-each>
					<xsl:text>,</xsl:text>
					<xsl:for-each select="//UML:Class[@xmi.id=$idPere]//UML:Attribute[@visibility='private']">
						<xsl:value-of select="@name"/>
						<xsl:text>_</xsl:text>
						<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
						<xsl:if test="not(position()=last())">
							<xsl:text>,</xsl:text>
						</xsl:if>
					</xsl:for-each>
					<xsl:text>);</xsl:text>				
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text>&#xa;</xsl:text>
	</xsl:template>
	
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Generalization ************************************ -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Generalization">
		<xsl:variable name="idChild"><xsl:value-of select=".//UML:Generalization.child//UML:Class/@xmi.idref"/></xsl:variable>
		<xsl:variable name="idParent"><xsl:value-of select=".//UML:Generalization.parent//UML:Class/@xmi.idref"/></xsl:variable>
		
		<xsl:if test="count(//UML:Class[@xmi.id=$idParent]//UML:Attribute[@visibility='private']) &gt; 0">		
			<xsl:for-each select="//UML:Class[@xmi.id=$idParent]//UML:Attribute">
				<xsl:text>ALTER TABLE </xsl:text>
				<xsl:value-of select="//UML:Class[@xmi.id=$idChild]/@name"/>
				<xsl:text> ADD COLUMN </xsl:text>
				<xsl:value-of select="@name"/>
				<xsl:text> </xsl:text>
				<xsl:apply-templates select=".//UML:DataType"/>
				<xsl:text> ;&#xa;</xsl:text>
			</xsl:for-each>
			<xsl:if test="contains(//UML:Class[@xmi.id=$idParent]//UML:Attribute/@visibility, 'private')">
				<xsl:variable name="pkvalue">
					<xsl:for-each select="//UML:Class[@xmi.id=$idParent]//UML:Attribute[@visibility='private']">
						<xsl:value-of select="./@name"/>
					</xsl:for-each>
				</xsl:variable>
			
				<xsl:text>ALTER TABLE </xsl:text>
				<xsl:value-of select="//UML:Class[@xmi.id=$idChild]/@name" />
				<xsl:text> ADD CONSTRAINT PK_</xsl:text>
				<xsl:value-of select="//UML:Class[@xmi.id=$idChild]/@name" />
				<xsl:text> PRIMARY KEY(</xsl:text>
				<xsl:value-of select="$pkvalue"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
			<xsl:text>&#xa;</xsl:text>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
