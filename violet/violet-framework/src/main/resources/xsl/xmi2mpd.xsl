<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:UML = 'org.omg.xmi.namespace.UML'
	xmlns:violet = 'http://www.violet.org/'>
	<xsl:output 
  		encoding="ISO-8859-15"
  		method="xml"
  		indent="yes" />

	<xsl:template match="/">
		<Violet>
		   	<newFile path="mpd.class.violet" name="mpd.class.violet">
		   	<java version="1.5.0" class="java.beans.XMLDecoder"> 
 			<object class="com.horstmann.violet.ClassDiagramGraph">
			<xsl:apply-templates select="//UML:Class[@name]" />
			<xsl:apply-templates select="//UML:Association" />
			<xsl:apply-templates select="//UML:Namespace.ownedElement/UML:Generalization" />
			<xsl:apply-templates select="//UML:Abstraction" />
			<xsl:call-template name="GetAssociationNN"/>
			</object> 
			</java> 
			</newFile>
		</Violet>
	</xsl:template>
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create class ********************************************* -->
	<!-- ************************************************************************************** -->
	<xsl:template match="UML:Class">
	<void method="addNode"> 
   		<object>
		   <xsl:attribute name="id"><xsl:value-of select="@xmi.id"/></xsl:attribute>
		   <xsl:attribute name="class">com.horstmann.violet.ClassNode</xsl:attribute>
    		<void property="attributes"> 
     			<void property="text"> 
      				<string>
						<!-- add column private -->
						<xsl:for-each select=".//UML:Attribute[@visibility='private']">
							<xsl:if test="@visibility='public'">+ </xsl:if>
							<xsl:if test="@visibility='private'">+ </xsl:if><!-- modification for mpd2sql -->
							<xsl:if test="@visibility='protected'"># </xsl:if>
							<xsl:value-of select="@name" />
							<xsl:text> : </xsl:text>
							<xsl:value-of select="substring-before(.//UML:DataType/@xmi.idref,'-')"/>
							<xsl:if test="string-length(.//UML:Expression/@body) &gt; 0">
								<xsl:text> = </xsl:text>
								<xsl:value-of select=".//UML:Expression/@body" />
							</xsl:if>
							<xsl:text>&#xa;</xsl:text>
						</xsl:for-each>
						<!-- add column for association -->
						<xsl:call-template name="GetAssociation">
							<xsl:with-param name="node" select="./@xmi.id" />
							<xsl:with-param name="type">attribute</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="GetGeneralization">
							<xsl:with-param name="node" select="./@xmi.id" />
							<xsl:with-param name="type">attribute</xsl:with-param>
						</xsl:call-template>
						<!-- add column no private -->
						<xsl:for-each select=".//UML:Attribute[@visibility != 'private']">
							<xsl:if test="@visibility='public'">+ </xsl:if>
							<xsl:if test="@visibility='private'">+ </xsl:if><!-- modification for mpd2sql -->
							<xsl:if test="@visibility='protected'"># </xsl:if>
							<xsl:value-of select="@name" />
							<xsl:text> : </xsl:text>
							<xsl:value-of select="substring-before(.//UML:DataType/@xmi.idref,'-')"/>
							<xsl:if test="string-length(.//UML:Expression/@body) &gt; 0">
								<xsl:text> = </xsl:text>
								<xsl:value-of select=".//UML:Expression/@body" />
							</xsl:if>
							<xsl:if test="not(position()=last())">
								<xsl:text>&#xa;</xsl:text>
							</xsl:if>
						</xsl:for-each>
      				</string> 
     			</void> 
    		</void> 
    		<void property="methods"> 
     			<void property="text"> 
      				<string>
						<!-- add pk -->
						<xsl:if test="count(.//UML:Attribute[@visibility='private']) &gt; 0">
							<xsl:text>getPK(</xsl:text>
							<xsl:for-each select=".//UML:Attribute[@visibility='private']">
								<xsl:value-of select="@name" />
								<xsl:text> : </xsl:text>
								<xsl:apply-templates select=".//UML:DataType"/>
								<xsl:if test="string-length(.//UML:Expression/@body) &gt; 0">
									<xsl:text> = </xsl:text>
									<xsl:value-of select=".//UML:Expression/@body" />
								</xsl:if>
								<xsl:if test="not(position()=last())">
									<xsl:text> , </xsl:text>
								</xsl:if>
							</xsl:for-each>
							<xsl:text>)&#xa;</xsl:text>
						</xsl:if>						
						<!-- add column for association -->
						<xsl:call-template name="GetAssociation">
							<xsl:with-param name="node" select="./@xmi.id" />
							<xsl:with-param name="type">method</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="GetGeneralization">
							<xsl:with-param name="node" select="./@xmi.id" />
							<xsl:with-param name="type">method</xsl:with-param>
						</xsl:call-template>
						<xsl:apply-templates select=".//UML:Operation"/>
      				</string>      				
      			</void>
      		</void>
    		<void property="name"> 
     			<void property="text"> 
      				<string><xsl:value-of select='@name'/></string> 
     			</void> 
    		</void> 
   		</object> 
   		<object class="java.awt.geom.Point2D$Double"> 
    		<void method="setLocation">
    			<xsl:for-each select = ".//violet:void[@method='setLocation']/violet:double">
     				<double><xsl:value-of select='.'/></double>
    			</xsl:for-each>
    		</void> 
   		</object> 
  	</void>
  	</xsl:template>
	
	<xsl:template match="UML:Operation">
		<xsl:text>&#xa;</xsl:text>
		<xsl:if test="@visibility='public'">+ </xsl:if>
		<xsl:if test="@visibility='private'">- </xsl:if>
		<xsl:if test="@visibility='protected'"># </xsl:if>
		<xsl:value-of select="@name" />
		<xsl:text> ( </xsl:text>
		<xsl:for-each select=".//UML:Parameter[@name != 'return']">
			<xsl:value-of select="./@name"/>
			<xsl:if test="not(position()=last())">
				<xsl:text> , </xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>)</xsl:text>
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
	
	<xsl:variable name="test1" select="($startUpper = 1 and $endUpper &lt; 1) or ($startUpper &lt; 1 and $endUpper = 1)"/>
	<xsl:variable name="test2" select="($startUpper = 1 and $endUpper = 1)"/>
	<xsl:if test="$test1 or $test2">
	<void method="connect"> 
   		<object class="com.horstmann.violet.ClassRelationshipEdge"> 
    		<void property="bentStyle"> 
     			<object class="com.horstmann.violet.BentStyle" field="HVH"/> 
    		</void>
    		<xsl:if test=".//UML:AssociationEnd[1]/@aggregation='composite'">
    			<void property="startArrowHead">
    				<object class="com.horstmann.violet.ArrowHead">
   						<xsl:attribute name="field">BLACK_DIAMOND</xsl:attribute>    			
    				</object>
    			</void>    			
    		</xsl:if>
    		<xsl:if test=".//UML:AssociationEnd[1]/@aggregation='aggregate'">
    			<void property="startArrowHead">
    				<object class="com.horstmann.violet.ArrowHead">
   						<xsl:attribute name="field">DIAMOND</xsl:attribute>    			
    				</object></void>    			
    		</xsl:if>
    		<xsl:if test=".//UML:AssociationEnd[2]/@aggregation='composite'">
    			<void property="endArrowHead">
    				<object class="com.horstmann.violet.ArrowHead">
   						<xsl:attribute name="field">BLACK_DIAMOND</xsl:attribute>    			
    				</object></void>    			
    		</xsl:if>
    		<xsl:if test=".//UML:AssociationEnd[2]/@aggregation='aggregate'">
    			<void property="endArrowHead">
    				<object class="com.horstmann.violet.ArrowHead">
   						<xsl:attribute name="field">DIAMOND</xsl:attribute>    			
    				</object></void>    			
    		</xsl:if>
    		<xsl:if test=".//UML:AssociationEnd[1]/@isNavigable='true' and .//UML:AssociationEnd[2]/@isNavigable='false' ">
    			<void property="startArrowHead">
    				<object class="com.horstmann.violet.ArrowHead">
   						<xsl:attribute name="field">V</xsl:attribute>    			
    				</object></void>    			
    		</xsl:if>
    		<xsl:if test=".//UML:AssociationEnd[2]/@isNavigable='true' and .//UML:AssociationEnd[1]/@isNavigable='false' ">
    			<void property="endArrowHead">
    				<object class="com.horstmann.violet.ArrowHead">
   						<xsl:attribute name="field">V</xsl:attribute>    			
    				</object></void>    			
    		</xsl:if>    		
    		<void property="endLabel"><string></string></void> 
    		<void property="startLabel"><string></string></void> 
    		<void property="middleLabel"><string><xsl:value-of select="@name"/></string></void> 
   		</object>
   		<xsl:for-each select=".//UML:Class">
   			<object>
   				<xsl:attribute name="idref"><xsl:value-of select="@xmi.idref"/></xsl:attribute>
   			</object> 
   		</xsl:for-each>
  	</void> 
  	</xsl:if>
	</xsl:template>	
	

	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Generalization ************************************ -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Generalization">
	<void method="connect"> 
   		<object class="com.horstmann.violet.ClassRelationshipEdge"> 
    		<void property="bentStyle"> 
     			<object class="com.horstmann.violet.BentStyle" field="HVH"/> 
    		</void>
    		<void property='endArrowHead'>
     			<object class="com.horstmann.violet.ArrowHead" field="TRIANGLE"/> 
    		</void>
    		<void property="endLabel"><string></string></void> 
    		<void property="startLabel"><string></string></void> 
    		<void property="middleLabel"><string><xsl:value-of select="@name"/></string></void> 
   		</object>
   		<object>
   			<xsl:attribute name="idref"><xsl:value-of select=".//UML:Generalization.child/UML:Class/@xmi.idref"/></xsl:attribute>
   		</object>
   		<object>
   			<xsl:attribute name="idref"><xsl:value-of select=".//UML:Generalization.parent/UML:Class/@xmi.idref"/></xsl:attribute>
   		</object>
  	</void> 
	</xsl:template>	
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Abstraction *************************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Abstraction">
	<void method="connect"> 
   		<object class="com.horstmann.violet.ClassRelationshipEdge"> 
    		<void property="bentStyle"> 
     			<object class="com.horstmann.violet.BentStyle" field="HVH"/> 
    		</void>
    		<void property='endArrowHead'>
     			<object class="com.horstmann.violet.ArrowHead" field="TRIANGLE"/> 
    		</void>
    		<void property="lineStyle"> 
    			<object class="com.horstmann.violet.LineStyle" field="DOTTED"/> 
    		</void> 
    		<void property="endLabel"><string></string></void> 
    		<void property="startLabel"><string></string></void> 
    		<void property="middleLabel"><string><xsl:value-of select="@name"/></string></void> 
   		</object>
   		<object>
   			<xsl:attribute name="idref"><xsl:value-of select=".//UML:Dependency.client/UML:Class/@xmi.idref"/></xsl:attribute>
   		</object>
   		<object>
   			<xsl:attribute name="idref"><xsl:value-of select=".//UML:Dependency.supplier/UML:Class/@xmi.idref"/></xsl:attribute>
   		</object>
  	</void> 
	</xsl:template>	
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Dependency **************************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Dependency">
	<void method="connect"> 
   		<object class="com.horstmann.violet.ClassRelationshipEdge"> 
    		<void property="bentStyle"> 
     			<object class="com.horstmann.violet.BentStyle" field="HVH"/> 
    		</void>
    		<void property="endArrowHead"> 
     			<object class="com.horstmann.violet.ArrowHead" field="V"/> 
    		</void> 
    		<void property="lineStyle"> 
     			<object class="com.horstmann.violet.LineStyle" field="DOTTED"/> 
   			</void> 
    		<void property="endLabel"><string></string></void> 
    		<void property="startLabel"><string></string></void> 
    		<void property="middleLabel"><string><xsl:value-of select="@name"/></string></void> 
   		</object>
   		<object>
   			<xsl:attribute name="idref"><xsl:value-of select=".//UML:Dependency.client/UML:Class/@xmi.idref"/></xsl:attribute>
   		</object>
   		<object>
   			<xsl:attribute name="idref"><xsl:value-of select=".//UML:Dependency.supplier/UML:Class/@xmi.idref"/></xsl:attribute>
   		</object>
  	</void> 
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
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Attribute and method by Association *************** -->
	<!-- ************************************************************************************** -->
	
	
	<xsl:template name="GetAssociation">
		<xsl:param name="node" />
		<xsl:param name="type" />
		
		<xsl:for-each select="//UML:Association[//UML:AssociationEnd//UML:Class/@xmi.idref = $node]">
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
					
					<xsl:if test="$idPere = $node">
						<xsl:if test="count(//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']) &gt; 0">
				  			<xsl:if test="$type = 'attribute'">
								<xsl:text>+ </xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
								<xsl:text>_</xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
								<xsl:text> : </xsl:text>
								<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']//UML:DataType"/>
								<xsl:text>&#xa;</xsl:text>
							</xsl:if>
					
				  			<xsl:if test="$type = 'method'">
								<xsl:text>getFK(</xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
								<xsl:text> , </xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
								<xsl:text> : </xsl:text>
								<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']//UML:DataType"/>
								<xsl:text> , </xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
								<xsl:text>_</xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
								<xsl:text> : </xsl:text>
								<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']//UML:DataType"/>
								<xsl:text>)&#xa;</xsl:text>
							</xsl:if>
						</xsl:if>
					</xsl:if>
				
				</xsl:when>
				<xsl:when test="($startUpper = 1 and $endUpper = 1)  and ($startLower = $endLower)">
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
				
					<xsl:if test="$idPere = $node">
						<xsl:if test="count(//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']) &gt; 0">				
					 		<xsl:if test="$type = 'attribute'">
								<xsl:text>+ </xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
								<xsl:text>_</xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
								<xsl:text> : </xsl:text>
								<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']//UML:DataType"/>
								<xsl:text> &#xa;</xsl:text>
       				 		</xsl:if>
				
				
					  		<xsl:if test="$type = 'method'">				
								<xsl:text>getFK(</xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
								<xsl:text> , </xsl:text>		
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
								<xsl:text> : </xsl:text>
								<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']//UML:DataType"/>
								<xsl:text> , </xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
								<xsl:text>_</xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
								<xsl:text> : </xsl:text>
								<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']//UML:DataType"/>
								<xsl:text>)&#xa;</xsl:text>		
							</xsl:if>
						</xsl:if>
					</xsl:if>
				</xsl:when>
				<xsl:when test="($startUpper = 1 and $endUpper = 1)  and ($startLower != $endLower)">
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
				
					<xsl:if test="$idPere = $node">
						<xsl:if test="count(//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']) &gt; 0">				
					 		<xsl:if test="$type = 'attribute'">
								<xsl:text>+ </xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
								<xsl:text>_</xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
								<xsl:text> : </xsl:text>
								<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']//UML:DataType"/>
								<xsl:text> &#xa;</xsl:text>
       				 		</xsl:if>
				
				
					  		<xsl:if test="$type = 'method'">				
								<xsl:text>getFK(</xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
								<xsl:text> , </xsl:text>		
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
								<xsl:text> : </xsl:text>
								<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']//UML:DataType"/>
								<xsl:text> , </xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
								<xsl:text>_</xsl:text>
								<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
								<xsl:text> : </xsl:text>
								<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']//UML:DataType"/>
								<xsl:text>)&#xa;</xsl:text>		
							</xsl:if>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>		
		</xsl:for-each>
	</xsl:template>
	
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Attribute and method by AssociationNN ************* -->
	<!-- ************************************************************************************** -->
	
	
	<xsl:template name="GetAssociationNN">		
		<xsl:for-each select="//UML:Association">
			<xsl:variable name="startId"><xsl:value-of select=".//UML:AssociationEnd[1]//UML:Class/@xmi.idref"/></xsl:variable>
			<xsl:variable name="startLower"><xsl:value-of select=".//UML:AssociationEnd[1]//UML:MultiplicityRange[@xmi.id]/@lower"/></xsl:variable>
			<xsl:variable name="startUpper"><xsl:value-of select=".//UML:AssociationEnd[1]//UML:MultiplicityRange[@xmi.id]/@upper"/></xsl:variable>
		
			<xsl:variable name="endId"><xsl:value-of select=".//UML:AssociationEnd[2]//UML:Class/@xmi.idref"/></xsl:variable>
			<xsl:variable name="endLower"><xsl:value-of select=".//UML:AssociationEnd[2]//UML:MultiplicityRange[@xmi.id]/@lower"/></xsl:variable>
			<xsl:variable name="endUpper"><xsl:value-of select=".//UML:AssociationEnd[2]//UML:MultiplicityRange[@xmi.id]/@upper"/></xsl:variable>
					
			<xsl:choose>
				<xsl:when test="($startUpper = 1 and $endUpper &lt; 1) or ($startUpper &lt; 1 and $endUpper = 1)"/>
				<xsl:when test="$startUpper = 1 and $endUpper = 1"/>
			
				<xsl:otherwise>
				<!-- relation N vers N -->
					<xsl:variable name="idPere"><xsl:value-of select="$endId"/></xsl:variable>
					<xsl:variable name="idFils"><xsl:value-of select="$startId"/></xsl:variable>
					<xsl:variable name="name">
						<xsl:if test="string-length(@name) &gt; 0"><xsl:value-of select="@name"/></xsl:if>
						<xsl:if test="string-length(@name) = 0"><xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>_<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/></xsl:if>
					</xsl:variable>
					<void method="addNode"> 
   						<object>
		   					<xsl:attribute name="id"><xsl:value-of select="$idPere"/>-1</xsl:attribute>
		   					<xsl:attribute name="class">com.horstmann.violet.ClassNode</xsl:attribute>
    						<void property="attributes"> 
     							<void property="text"> 
      								<string>
      								<xsl:for-each select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']">
      									<xsl:text>+ </xsl:text>
										<xsl:value-of select="@name"/>
										<xsl:text>_</xsl:text>
										<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
										<xsl:text> : </xsl:text>
										<xsl:apply-templates select=".//UML:DataType"/>
										<xsl:text>&#xa;</xsl:text>
									</xsl:for-each>
									<xsl:for-each select="//UML:Class[@xmi.id=$idPere]//UML:Attribute[@visibility='private']">
      									<xsl:text>+ </xsl:text>
										<xsl:value-of select="@name"/>
										<xsl:text>_</xsl:text>
										<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
										<xsl:text> : </xsl:text>
										<xsl:apply-templates select=".//UML:DataType"/>
										<xsl:text>&#xa;</xsl:text>
									</xsl:for-each>
									</string> 
     							</void> 
    						</void> 
    						<void property="methods"> 
     							<void property="text"> 
      								<string>
      									<xsl:text>getFK(</xsl:text>
										<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
										<xsl:text> , </xsl:text>		
										<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
										<xsl:text> : </xsl:text>
										<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']//UML:DataType"/>
										<xsl:text> , </xsl:text>
										<xsl:value-of select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']/@name"/>
										<xsl:text>_</xsl:text>
										<xsl:value-of select="//UML:Class[@xmi.id=$idFils]/@name"/>
										<xsl:text> : </xsl:text>
										<xsl:apply-templates select="//UML:Class[@xmi.id=$idFils]//UML:Attribute[@visibility='private']//UML:DataType"/>
										<xsl:text>)&#xa;</xsl:text>	
										
      									<xsl:text>getFK(</xsl:text>
										<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
										<xsl:text> , </xsl:text>		
										<xsl:value-of select="//UML:Class[@xmi.id=$idPere]//UML:Attribute[@visibility='private']/@name"/>
										<xsl:text> : </xsl:text>
										<xsl:apply-templates select="//UML:Class[@xmi.id=$idPere]//UML:Attribute[@visibility='private']//UML:DataType"/>
										<xsl:text> , </xsl:text>
										<xsl:value-of select="//UML:Class[@xmi.id=$idPere]//UML:Attribute[@visibility='private']/@name"/>
										<xsl:text>_</xsl:text>
										<xsl:value-of select="//UML:Class[@xmi.id=$idPere]/@name"/>
										<xsl:text> : </xsl:text>
										<xsl:apply-templates select="//UML:Class[@xmi.id=$idPere]//UML:Attribute[@visibility='private']//UML:DataType"/>
										<xsl:text>)&#xa;</xsl:text>	
      								</string>      				
      							</void>
      						</void>
    						<void property="name"> 
     							<void property="text"> 
      								<string><xsl:value-of select="$name"/></string> 
     							</void> 
    						</void> 
   						</object> 
   						<object class="java.awt.geom.Point2D$Double"> 
    						<void method="setLocation">
     							<double><xsl:value-of select="(//UML:Class[@xmi.id=$idPere]//violet:void[@method='setLocation']/violet:double[1] + //UML:Class[@xmi.id=$idFils]//violet:void[@method='setLocation']/violet:double[1]) div 2"/></double>
     							<double><xsl:value-of select="(//UML:Class[@xmi.id=$idPere]//violet:void[@method='setLocation']/violet:double[2] + //UML:Class[@xmi.id=$idFils]//violet:void[@method='setLocation']/violet:double[2]) div 2"/></double>
    						</void> 
   						</object> 
  					</void>
					<void method="connect"> 
   						<object class="com.horstmann.violet.ClassRelationshipEdge"> 
    						<void property="bentStyle"> 
     							<object class="com.horstmann.violet.BentStyle" field="HVH"/> 
    						</void>		
    						<void property="endLabel"><string></string></void> 
    						<void property="startLabel"><string></string></void> 
    						<void property="middleLabel"><string></string></void> 
   						</object>
   						<object>
   							<xsl:attribute name="idref"><xsl:value-of select="$idPere"/>-1</xsl:attribute>
   						</object>
   						<object>
   							<xsl:attribute name="idref"><xsl:value-of select="$idPere"/></xsl:attribute>
   						</object>
  					</void> 
					<void method="connect"> 
   						<object class="com.horstmann.violet.ClassRelationshipEdge"> 
    						<void property="bentStyle"> 
     							<object class="com.horstmann.violet.BentStyle" field="HVH"/> 
    						</void>		
    						<void property="endLabel"><string></string></void> 
    						<void property="startLabel"><string></string></void> 
    						<void property="middleLabel"><string></string></void> 
   						</object>
   						<object>
   							<xsl:attribute name="idref"><xsl:value-of select="$idPere"/>-1</xsl:attribute>
   						</object>
   						<object>
   							<xsl:attribute name="idref"><xsl:value-of select="$idFils"/></xsl:attribute>
   						</object>
  					</void> 
				</xsl:otherwise>
			</xsl:choose>		
		</xsl:for-each>
	</xsl:template>
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Attribute and method by Generalization ************ -->
	<!-- ************************************************************************************** -->
	
	<xsl:template name="GetGeneralization">
		<xsl:param name="node" />
		<xsl:param name="type" />
		
		<xsl:for-each select="//UML:Generalization[//UML:Generalization.child//UML:Class/@xmi.idref = $node]">
		
			<xsl:variable name="idChild"><xsl:value-of select=".//UML:Generalization.child//UML:Class/@xmi.idref"/></xsl:variable>
			<xsl:variable name="idParent"><xsl:value-of select=".//UML:Generalization.parent//UML:Class/@xmi.idref"/></xsl:variable>
			<xsl:if test="$type = 'attribute'">	
				<xsl:for-each select="//UML:Class[@xmi.id=$idParent]//UML:Attribute">
					<xsl:text>+ </xsl:text>
					<xsl:value-of select="@name"/>
					<xsl:text> : </xsl:text>
					<xsl:apply-templates select=".//UML:DataType"/>
					<xsl:if test="string-length(.//UML:Expression/@body) &gt; 0">
						<xsl:text> = </xsl:text>
						<xsl:value-of select=".//UML:Expression/@body" />
					</xsl:if>
					<xsl:text>&#xa;</xsl:text>
				</xsl:for-each>
			</xsl:if>
						
			<xsl:if test="$type = 'method'">
				<xsl:if test="count(//UML:Class[@xmi.id=$idParent]//UML:Attribute[@visibility= 'private']) &gt; 0">
					<xsl:text>getPK(</xsl:text>
					<xsl:for-each select="//UML:Class[@xmi.id=$idParent]//UML:Attribute[@visibility='private']">
						<xsl:value-of select="@name" />
						<xsl:text> : </xsl:text>
						<xsl:apply-templates select=".//UML:DataType"/>
						<xsl:if test="string-length(.//UML:Expression/@body) &gt; 0">
							<xsl:text> = </xsl:text>
							<xsl:value-of select=".//UML:Expression/@body" />
						</xsl:if>
						<xsl:if test="not(position()=last())">
							<xsl:text> , </xsl:text>
						</xsl:if>
					</xsl:for-each>
					<xsl:text>)&#xa;</xsl:text>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	
</xsl:stylesheet>

