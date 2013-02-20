<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:redirect="http://xml.apache.org/xalan/redirect"
	xmlns:UML = 'org.omg.xmi.namespace.UML'
	xmlns:violet = 'http://www.violet.org/'
	extension-element-prefixes="redirect">
	
	<xsl:output encoding="ISO-8859-15" method="xml" indent="yes" />
	<xsl:param name="nameDir" select="/" />

	<xsl:variable name = "lowercase" select = "'abcdefghijklmnopqrstuvwxyz_'"  />
	<xsl:variable name = "uppercase" select= "'ABCDEFGHIJKLMNOPQRSTUVWXYZ-'" />	
		
	<xsl:template match="/">					
		<XMI xmi.version='1.2' xmlns:UML='org.omg.xmi.namespace.UML'
			timestamp='Thu Feb 15 15:33:51 CET 2007'>
			<XMI.header>
				<XMI.documentation>
					<XMI.exporter>Violet Plugin</XMI.exporter>
					<XMI.exporterVersion>0.1 revised on $Date: 2008/08/26 22:08:19 $</XMI.exporterVersion>
				</XMI.documentation>
				<XMI.metamodel xmi.name="UML" xmi.version="1.4" />
			</XMI.header>
			<XMI.content>
				<UML:Model>
					<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id()" /></xsl:attribute>
					<xsl:attribute name="name">ModelName</xsl:attribute>
					<xsl:attribute name="isSpecification">false</xsl:attribute>
					<xsl:attribute name="isRoot">false</xsl:attribute>
					<xsl:attribute name="isLeaf">false</xsl:attribute>
					<xsl:attribute name="isAbstract">false</xsl:attribute>
					
					<xsl:element name="UML:Namespace.ownedElement">
						<xsl:apply-templates select="/java/object/void" />
					</xsl:element>
						
					
				</UML:Model>
			</XMI.content>
		</XMI>
	</xsl:template>

	<!-- ************************************************************************************** -->
	<!-- *************************** create class ********************************************* -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="void[@method='addNode'and count(.//object[@class='com.horstmann.violet.NoteNode'])=0]">
		<xsl:variable name="name">
			<xsl:value-of select=".//void[@property='name']/void[@property='text']/string" />
		</xsl:variable>
		<UML:Class>
				<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id(./object[1])" /></xsl:attribute>
				<xsl:attribute name="name"><xsl:call-template name="getName"><xsl:with-param name="name" select="$name" /></xsl:call-template></xsl:attribute>
				<xsl:attribute name="visibility"><xsl:call-template name="getVisibility"><xsl:with-param name="name" select="$name" /></xsl:call-template></xsl:attribute>
				<xsl:attribute name="isSpecification">false</xsl:attribute>
				<xsl:attribute name="isRoot">false</xsl:attribute>
				<xsl:attribute name="isLeaf">false</xsl:attribute>
				<xsl:attribute name="isAbstract">false</xsl:attribute>
				<xsl:attribute name="isActive">false</xsl:attribute>
				<UML:ModelElement.stereotype>
					<xsl:call-template name="getStereotype"><xsl:with-param name="idref" select="./object[1]/@id" /><xsl:with-param name="name" select="$name" /></xsl:call-template>            		
          		</UML:ModelElement.stereotype>
        		<UML:GeneralizableElement.generalization>
					<xsl:call-template name="getGeneralization"><xsl:with-param name="idref" select="./object[1]/@id" /></xsl:call-template>
				</UML:GeneralizableElement.generalization>
				<UML:ModelElement.clientDependency>	
					<xsl:call-template name="getAbstraction"><xsl:with-param name="idref" select="./object[1]/@id" /></xsl:call-template>
				</UML:ModelElement.clientDependency>
				<UML:Classifier.feature>
					<xsl:apply-templates select="./object/void[@property='attributes']/void" />
					<xsl:apply-templates select="./object/void[@property='methods']/void" />
				</UML:Classifier.feature>
				<violet:void method="setLocation">
					<xsl:for-each select=".//void[@method='setLocation']/double">
						<violet:double><xsl:value-of select='.'/></violet:double>
					</xsl:for-each>
				</violet:void>
        </UML:Class>				
	</xsl:template>
	
	<xsl:template name="getName">
		<xsl:param name="name" />
		<xsl:choose >
        	<xsl:when test = "contains($name,'»&#xa;')" >
            	<xsl:value-of select="translate(substring-after($name,'»&#xa;'),'&#xa;' ,'' )" />
            </xsl:when>
        	<xsl:when test = "contains($name,'&gt;&gt;&#xa;')" >
            	<xsl:value-of select="translate(substring-after($name,'&gt;&gt;&#xa;'),'&#xa;' ,'' )" />
            </xsl:when>
            <xsl:otherwise>
            	<xsl:value-of select="translate($name,'&#xa;' ,'' )" />
			</xsl:otherwise>
        </xsl:choose> 
	</xsl:template>
	
	<xsl:template name="getVisibility">
		<xsl:param name="name" />
		<xsl:text>public</xsl:text>
	</xsl:template>
	
	<xsl:template name="getStereotype">
		<xsl:param name="idref" />
		<xsl:param name="name" />
		<xsl:choose >
        	<xsl:when test = "contains($name,'»&#xa;')" >
            	<UML:Stereotype>
            		<xsl:attribute name="xmi.idref"><xsl:value-of select="substring-after(substring-before($name,'»&#xa;'),'«')"/>-<xsl:value-of select="generate-id($idref)"/></xsl:attribute>
            	</UML:Stereotype>
            </xsl:when>
        	<xsl:when test = "contains($name,'&gt;&gt;&#xa;')" >
            	<UML:Stereotype>
            		<xsl:attribute name="xmi.idref"><xsl:value-of select="substring-after(substring-before($name,'&gt;&gt;&#xa;'),'&lt;&lt;')"/>-<xsl:value-of select="generate-id($idref)"/></xsl:attribute>
            	</UML:Stereotype>
            </xsl:when>
            <xsl:otherwise/>
        </xsl:choose> 
	</xsl:template>
	
	<xsl:template name="getGeneralization">
		<xsl:param name="idref" />
		<xsl:for-each select = "/java/object/void[@method='connect']/object[2][@idref=$idref]" >
				<xsl:variable name="value">
					<xsl:text>|</xsl:text>
					<xsl:value-of select="./parent::*/object[1]/void[@property='endArrowHead']/object/@field" /><xsl:text>|</xsl:text>
					<xsl:value-of select="./parent::*/object[1]/void[@property='lineStyle']/object/@field" />
				</xsl:variable>
				<xsl:if test="$value='|TRIANGLE|'">
					<UML:Generalization>
						<xsl:attribute name="xmi.idref"><xsl:value-of select="generate-id(./parent::*/.)"/></xsl:attribute>
					</UML:Generalization>
				</xsl:if>									
        </xsl:for-each>
	</xsl:template>
	
	<xsl:template name="getAbstraction">
		<xsl:param name="idref" />
		<xsl:for-each select = "/java/object/void[@method='connect']/object[2][@idref=$idref]" >
				<xsl:variable name="value">
					<xsl:text>|</xsl:text>
					<xsl:value-of select="./parent::*/object[1]/void[@property='endArrowHead']/object/@field" /><xsl:text>|</xsl:text>
					<xsl:value-of select="./parent::*/object[1]/void[@property='lineStyle']/object/@field" />
				</xsl:variable>
				<xsl:if test="$value='|TRIANGLE|DOTTED'">
					<UML:Abstraction>
						<xsl:attribute name="xmi.idref"><xsl:value-of select="generate-id(./parent::*/.)"/></xsl:attribute>
					</UML:Abstraction>
				</xsl:if>		
				<xsl:if test="$value='|V|DOTTED'">
					<UML:Dependency>
						<xsl:attribute name="xmi.idref"><xsl:value-of select="generate-id(./parent::*/.)"/></xsl:attribute>
					</UML:Dependency>
				</xsl:if>									
        </xsl:for-each>
	</xsl:template>
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create attributes **************************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="void[@property='attributes']/void">
		<xsl:variable name="value"><xsl:value-of select="string" /></xsl:variable>
		<xsl:call-template name="getAttr">
			<xsl:with-param name="stringSp" select="translate($value,' ','')" />
			<xsl:with-param name="node" select="." />
		</xsl:call-template>

	</xsl:template>

	<xsl:template name="getAttr">
		<xsl:param name="stringSp" />
		<xsl:param name="node" />
		<xsl:choose>
			<xsl:when test="contains($stringSp, '&#xa;')">
				<xsl:variable name="value"><xsl:value-of select="translate(substring-before($stringSp,'&#xa;'),'&#x20;&#xa;&#xd;&#x9;&#160;','')" /></xsl:variable>
				<xsl:if test="string-length($value) &gt; 0">
					<xsl:call-template name="FormatAttr">
						<xsl:with-param name="stringSp" select="substring-before($stringSp,'&#xa;')" />
						<xsl:with-param name="node" select="$node" />
					</xsl:call-template>
				</xsl:if>
				<xsl:call-template name="getAttr">
					<xsl:with-param name="stringSp" select="substring-after($stringSp,'&#xa;')" />
					<xsl:with-param name="node" select="$node" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="value"><xsl:value-of select="translate($stringSp,'&#x20;&#xa;&#xd;&#x9;&#160;','')" /></xsl:variable>
				<xsl:if test="string-length($value) &gt; 0">
					<xsl:call-template name="FormatAttr">
						<xsl:with-param name="stringSp" select="$stringSp" />
						<xsl:with-param name="node" select="$node" />
					</xsl:call-template>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="FormatAttr">
		<xsl:param name="stringSp" />
		<xsl:param name="node" />
		<UML:Attribute>
			<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-<xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:attribute>
			<xsl:attribute name="name"><xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:attribute>
			<xsl:attribute name="visibility"><xsl:call-template name="getNature"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:attribute>
			<xsl:attribute name="isSpecification">false</xsl:attribute>
			<xsl:attribute name="ownerScope">instance</xsl:attribute>
			<xsl:attribute name="changeability">changeable</xsl:attribute>
			<xsl:attribute name="targetScope">instance</xsl:attribute>
			
            <UML:StructuralFeature.multiplicity>
            	<UML:Multiplicity>
					<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-<xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template>-multiplicity</xsl:attribute>
			        <UML:Multiplicity.range>
                    	<UML:MultiplicityRange  lower = '1' upper = '1'>
							<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-<xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template>-multiplicityrange</xsl:attribute>
			        	</UML:MultiplicityRange>
                  	</UML:Multiplicity.range>
                </UML:Multiplicity>
   			</UML:StructuralFeature.multiplicity>
            <UML:StructuralFeature.type>
            	<UML:DataType>
					<xsl:attribute name="xmi.idref"><xsl:call-template name="getType"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template>-<xsl:value-of select = "generate-id($node)" />-<xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template>-multiplicity</xsl:attribute>
			    </UML:DataType>
            </UML:StructuralFeature.type>
            
            
			<xsl:variable name="initvalue"><xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:variable>
	        <xsl:if test="string-length($initvalue) &gt; 0">
            	<UML:Attribute.initialValue>
                	<UML:Expression>
                		<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-<xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template>-initvalue</xsl:attribute>
						<xsl:attribute name="language"/>
						<xsl:attribute name="body"><xsl:call-template name="getDefault"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:attribute>
                  	</UML:Expression>
              	</UML:Attribute.initialValue>
			</xsl:if>
            
            
        </UML:Attribute>
	</xsl:template>

	<xsl:template name="getNature">
		<xsl:param name="stringSp" />
		<xsl:variable name="typ"><xsl:value-of select="substring($stringSp,1,1)" /></xsl:variable>
		<xsl:choose >
                    <xsl:when test = "$typ = '-'" >
						<xsl:variable name="strtyp">private</xsl:variable>
						<xsl:value-of select="$strtyp" />
                    </xsl:when>
                    <xsl:when test = "$typ = '#'" >
						<xsl:variable name="strtyp">protected</xsl:variable>
						<xsl:value-of select="$strtyp" />
                    </xsl:when>
                    <xsl:otherwise >
						<xsl:variable name="strtyp">public</xsl:variable>
						<xsl:value-of select="$strtyp" />
                    </xsl:otherwise>
        </xsl:choose> 
	</xsl:template>

	<xsl:template name="getNameAttr">
		<xsl:param name="stringSp" />
		<xsl:choose >
			<xsl:when test="contains($stringSp,'(')">
				<xsl:variable name="short"><xsl:value-of select="substring-before($stringSp,'(')" /></xsl:variable>
				<xsl:choose >
                    <xsl:when test = "contains($short,':')" >
						<xsl:call-template name="getNameShort"><xsl:with-param name="stringSp" select="substring-before($short,':')" /></xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise >
						<xsl:call-template name="getNameShort"><xsl:with-param name="stringSp" select="$short" /></xsl:call-template>
                    </xsl:otherwise>
        	</xsl:choose> 
            </xsl:when>
			<xsl:when test="contains($stringSp,':')">
						<xsl:call-template name="getNameShort"><xsl:with-param name="stringSp" select="substring-before($stringSp,':')" /></xsl:call-template>
            </xsl:when>
			<xsl:when test="contains($stringSp,'=')">
						<xsl:call-template name="getNameShort"><xsl:with-param name="stringSp" select="substring-before($stringSp,'=')" /></xsl:call-template>
            </xsl:when>
            <xsl:otherwise >
					<xsl:call-template name="getNameShort"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template>
            </xsl:otherwise>
        </xsl:choose> 
	</xsl:template>

	<xsl:template name="getNameShort">
		<xsl:param name="stringSp" />
		<xsl:variable name="typ"><xsl:value-of select="translate(substring($stringSp,1,1),'+-#','')" /></xsl:variable>
		<xsl:choose >
        	<xsl:when test = "string-length($typ) = 0" >
		    	<xsl:value-of select="substring($stringSp,2,string-length($stringSp))" />
            </xsl:when>
            <xsl:otherwise >
				<xsl:value-of select="$stringSp" />
            </xsl:otherwise>
        </xsl:choose> 
	</xsl:template>


	<xsl:template name="getType">
		<xsl:param name="stringSp" />
		<xsl:choose>
			<xsl:when test="contains($stringSp,':')">
				<xsl:choose>
					<xsl:when test="contains($stringSp,'=')">
						<xsl:value-of select="translate(substring-before(substring-after($stringSp,':'),'='),' ','')" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="translate(substring-after($stringSp,':'),' ','')" />
					</xsl:otherwise>		
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="getChar"><xsl:with-param name="stringSp">int</xsl:with-param></xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getDefault">
		<xsl:param name="stringSp" />
		<xsl:if test="contains($stringSp,'=')">
			<xsl:value-of select="substring-after($stringSp,'=')" />
		</xsl:if>
	</xsl:template>

	<xsl:template name="getChar">
		<xsl:param name="stringSp" />
		<xsl:value-of select="$stringSp"/>
	</xsl:template>

	
	<!-- ************************************************************************************** -->
	<!-- *************************** create method ******************************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="void[@property='methods']/void">
		<xsl:variable name="value"><xsl:value-of select="string" /></xsl:variable>
		<xsl:call-template name="getMethod">
			<xsl:with-param name="stringSp" select="translate($value,' ','')" />
			<xsl:with-param name="node" select="." />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="getMethod">
		<xsl:param name="stringSp" />
		<xsl:param name="node" />
		<xsl:choose>
			<xsl:when test="contains($stringSp,'&#xa;')">
				<xsl:variable name="value"><xsl:value-of select="translate(substring-before($stringSp,'&#xa;'),'&#x20;&#xa;&#xd;&#x9;&#160;','')" /></xsl:variable>
				<xsl:if test="string-length($value) &gt; 0">
					<xsl:call-template name="FormatMethod">
						<xsl:with-param name="stringSp" select="substring-before($stringSp,'&#xa;')" />
						<xsl:with-param name="node" select="$node" />
					</xsl:call-template>
				</xsl:if>
				<xsl:call-template name="getMethod">
					<xsl:with-param name="stringSp" select="substring-after($stringSp,'&#xa;')" />
					<xsl:with-param name="node" select="$node" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="value"><xsl:value-of select="translate($stringSp,'&#x20;&#xa;&#xd;&#x9;&#160;','')" /></xsl:variable>
				<xsl:if test="string-length($value) &gt; 0">
					<xsl:call-template name="FormatMethod">
						<xsl:with-param name="stringSp" select="$stringSp" />
						<xsl:with-param name="node" select="$node" />
					</xsl:call-template>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="FormatMethod">
		<xsl:param name="stringSp" />
		<xsl:param name="node" />
		
		<UML:Operation>
			<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-<xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:attribute>
			<xsl:attribute name="name"><xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:attribute>
			<xsl:attribute name="visibility"><xsl:call-template name="getNature"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:attribute>
			<xsl:attribute name="isSpecification">false</xsl:attribute>
			<xsl:attribute name="ownerScope">instance</xsl:attribute>
			<xsl:attribute name="isQuery">false</xsl:attribute>
			<xsl:attribute name="concurrency">sequential</xsl:attribute>
			<xsl:attribute name="isRoot">false</xsl:attribute>
			<xsl:attribute name="isLeaf">false</xsl:attribute>
			<xsl:attribute name="isAbstract">false</xsl:attribute>
			
			<UML:BehavioralFeature.parameter>
                <xsl:call-template name="FormatAttrMethod">
						<xsl:with-param name="stringSp">return:<xsl:call-template name="getTypeMethod"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:with-param>
						<xsl:with-param name="node" select="$node" />
						<xsl:with-param name="namemethod"><xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:with-param>
					</xsl:call-template>
			<xsl:choose>
            	<xsl:when test = "contains($stringSp,'(')" >
					<xsl:variable name="valueAttr"><xsl:value-of select="substring-before(substring-after($stringSp,'('),')')" /></xsl:variable>
					<xsl:call-template name="getAttrMethod">
						<xsl:with-param name="stringSp" select="$valueAttr" />
						<xsl:with-param name="node" select="$node" />
						<xsl:with-param name="namemethod"><xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:with-param>
					</xsl:call-template>
           		</xsl:when>
          		<xsl:otherwise/>
        	</xsl:choose>
			</UML:BehavioralFeature.parameter>
        </UML:Operation>
	</xsl:template>


	<xsl:template name="getTypeMethod">
		<xsl:param name="stringSp" />
		<xsl:choose>
			<xsl:when test="contains($stringSp,')')">
				<xsl:choose>
					<xsl:when test="contains(substring-after($stringSp,')'),':')">
						<xsl:value-of select="translate(substring-after(substring-after($stringSp,')'),':'),' ','')" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="getChar"><xsl:with-param name="stringSp">void</xsl:with-param></xsl:call-template>
					</xsl:otherwise>		
				</xsl:choose>
			</xsl:when>
			<xsl:when test="contains($stringSp,':')">
				<xsl:value-of select="translate(substring-after($stringSp,':'),' ','')" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="getChar"><xsl:with-param name="stringSp">void</xsl:with-param></xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="getAttrMethod">
		<xsl:param name="stringSp" />
		<xsl:param name="node" />
		<xsl:param name="namemethod" />
		<xsl:choose>
			<xsl:when test="contains($stringSp, ',')">
				<xsl:variable name="value"><xsl:value-of select="translate(substring-before($stringSp,','),'&#x20;&#xa;&#xd;&#x9;&#160;','')" /></xsl:variable>
				<xsl:if test="string-length($value) &gt; 0">
					<xsl:call-template name="FormatAttrMethod">
						<xsl:with-param name="stringSp" select="substring-before($stringSp,',')" />
						<xsl:with-param name="node" select="$node" />
						<xsl:with-param name="namemethod" select="$namemethod" />
					</xsl:call-template>
				</xsl:if>
				<xsl:call-template name="getAttrMethod">
					<xsl:with-param name="stringSp" select="substring-after($stringSp,',')" />
					<xsl:with-param name="node" select="$node" />
					<xsl:with-param name="namemethod" select="$namemethod" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="value"><xsl:value-of select="translate($stringSp,'&#x20;&#xa;&#xd;&#x9;&#160;','')" /></xsl:variable>
				<xsl:if test="string-length($value) &gt; 0">
					<xsl:call-template name="FormatAttrMethod">
						<xsl:with-param name="stringSp" select="$stringSp" />
						<xsl:with-param name="node" select="$node" />
						<xsl:with-param name="namemethod" select="$namemethod" />
					</xsl:call-template>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="FormatAttrMethod">
		<xsl:param name="stringSp" />
		<xsl:param name="node" />
		<xsl:param name="namemethod" />
		<UML:Parameter>		
			<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-<xsl:value-of select = "$namemethod"/>-<xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:attribute>
			<xsl:attribute name="name"><xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:attribute>
			<xsl:attribute name="isSpecification">false</xsl:attribute>
			<UML:Parameter.type>
            	<UML:DataType>
            		<xsl:attribute name="xmi.idref"><xsl:call-template name="getType"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template>-<xsl:value-of select = "generate-id($node)" />-<xsl:value-of select = "$namemethod"/>-<xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:attribute>
			 	</UML:DataType>
            </UML:Parameter.type>
            <xsl:variable name="value"><xsl:call-template name="getDefault"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:variable>
            <xsl:if test="string-length($value) &gt; 0">
            	<UML:Parameter.defaultValue>
                    <UML:Expression>
            			<xsl:attribute name="xmi.id"><xsl:call-template name="getType"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template>-<xsl:value-of select = "generate-id($node)" />-<xsl:value-of select = "$namemethod"/>-<xsl:call-template name="getNameAttr"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template>-1</xsl:attribute>
			 			<xsl:attribute name="body"><xsl:call-template name="getDefault"><xsl:with-param name="stringSp" select="$stringSp" /></xsl:call-template></xsl:attribute>
                    </UML:Expression>
            	</UML:Parameter.defaultValue>
            </xsl:if>
		</UML:Parameter>
	</xsl:template>
	

	<!-- ************************************************************************************** -->
	<!-- ********************************** connect ******************************************* -->
	<!-- ************************************************************************************** -->
	
	
	<xsl:template match="void[@method='connect' and count(.//object[@class='com.horstmann.violet.NoteEdge'])=0]">
		<xsl:variable name="value">
			<xsl:text>|</xsl:text>
			<xsl:value-of select="./object[1]/void[@property='endArrowHead']/object/@field" /><xsl:text>|</xsl:text>
			<xsl:value-of select="./object[1]/void[@property='lineStyle']/object/@field" /><xsl:text>|</xsl:text>
			<xsl:value-of select="./object[1]/void[@property='startArrowHead']/object/@field" />
		</xsl:variable>
		<xsl:choose>
		<xsl:when test="$value='|TRIANGLE||'">			
			<xsl:call-template name="generalization">
				<xsl:with-param name="node" select="." />
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|||TRIANGLE'">			
			<xsl:call-template name="generalization">
				<xsl:with-param name="node" select="." />
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|TRIANGLE|DOTTED|'">			
			<xsl:call-template name="abstraction">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregation">none</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='||DOTTED|TRIANGLE'">			
			<xsl:call-template name="abstraction">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregation">none</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|V|DOTTED|'">			
			<xsl:call-template name="dependency">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregation">none</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='||DOTTED|V'">			
			<xsl:call-template name="dependency">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregation">none</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|||'">			
			<xsl:call-template name="association">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregationStart">none</xsl:with-param>
				<xsl:with-param name="aggregationEnd">none</xsl:with-param>
				<xsl:with-param name="isnavigableStart">true</xsl:with-param>
				<xsl:with-param name="isnavigableEnd">true</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|V||'">			
			<xsl:call-template name="association">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregationStart">none</xsl:with-param>
				<xsl:with-param name="aggregationEnd">none</xsl:with-param>
				<xsl:with-param name="isnavigableStart">false</xsl:with-param>
				<xsl:with-param name="isnavigableEnd">true</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|||DIAMOND'">	
			<xsl:call-template name="association">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregationStart">aggregate</xsl:with-param>
				<xsl:with-param name="aggregationEnd">none</xsl:with-param>
				<xsl:with-param name="isnavigableStart">true</xsl:with-param>
				<xsl:with-param name="isnavigableEnd">true</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|||BLACK_DIAMOND'">			
			<xsl:call-template name="association">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregationStart">composite</xsl:with-param>
				<xsl:with-param name="aggregationEnd">none</xsl:with-param>
				<xsl:with-param name="isnavigableStart">true</xsl:with-param>
				<xsl:with-param name="isnavigableEnd">true</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|DIAMOND||'">	
			<xsl:call-template name="association">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregationStart">none</xsl:with-param>
				<xsl:with-param name="aggregationEnd">aggregate</xsl:with-param>
				<xsl:with-param name="isnavigableStart">true</xsl:with-param>
				<xsl:with-param name="isnavigableEnd">true</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|BLACK_DIAMOND||'">			
			<xsl:call-template name="association">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregationStart">none</xsl:with-param>
				<xsl:with-param name="aggregationEnd">composite</xsl:with-param>
				<xsl:with-param name="isnavigableStart">true</xsl:with-param>
				<xsl:with-param name="isnavigableEnd">true</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|V||DIAMOND'">	
			<xsl:call-template name="association">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregationStart">aggregate</xsl:with-param>
				<xsl:with-param name="aggregationEnd">none</xsl:with-param>
				<xsl:with-param name="isnavigableStart">false</xsl:with-param>
				<xsl:with-param name="isnavigableEnd">true</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|V||BLACK_DIAMOND'">			
			<xsl:call-template name="association">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregationStart">composite</xsl:with-param>
				<xsl:with-param name="aggregationEnd">none</xsl:with-param>
				<xsl:with-param name="isnavigableStart">false</xsl:with-param>
				<xsl:with-param name="isnavigableEnd">true</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|DIAMOND||V'">	
			<xsl:call-template name="association">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregationStart">none</xsl:with-param>
				<xsl:with-param name="aggregationEnd">aggregate</xsl:with-param>
				<xsl:with-param name="isnavigableStart">false</xsl:with-param>
				<xsl:with-param name="isnavigableEnd">true</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="$value='|BLACK_DIAMOND||V'">			
			<xsl:call-template name="association">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregationStart">none</xsl:with-param>
				<xsl:with-param name="aggregationEnd">composite</xsl:with-param>
				<xsl:with-param name="isnavigableStart">false</xsl:with-param>
				<xsl:with-param name="isnavigableEnd">true</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>		
			<xsl:call-template name="association">
				<xsl:with-param name="node" select="." />
				<xsl:with-param name="aggregationStart">none</xsl:with-param>
				<xsl:with-param name="aggregationEnd">none</xsl:with-param>
				<xsl:with-param name="isnavigableStart">true</xsl:with-param>
				<xsl:with-param name="isnavigableEnd">true</xsl:with-param>
			</xsl:call-template>
		</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	

	<!-- ************************************************************************************** -->
	<!-- ********************************** UML:Generalization ******************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template name="generalization">
		<xsl:param name="node" />
		<UML:Generalization>
				<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" /></xsl:attribute>
				<xsl:attribute name="name"><xsl:value-of select = "$node/object[1]/void[@property='middleLabel']/string" /></xsl:attribute>
				<xsl:attribute name="isSpecification">false</xsl:attribute>
			<xsl:variable name="idChild">
				<xsl:if test="$node/object[1]/void[@property='endArrowHead']/object/@field='TRIANGLE'"><xsl:value-of select ="$node/object[2]/@idref"/></xsl:if>
				<xsl:if test="$node/object[1]/void[@property='startArrowHead']/object/@field='TRIANGLE'"><xsl:value-of select ="$node/object[3]/@idref"/></xsl:if>
			</xsl:variable>
			<xsl:variable name="idParent">
				<xsl:if test="$node/object[1]/void[@property='endArrowHead']/object/@field='TRIANGLE'"><xsl:value-of select ="$node/object[3]/@idref"/></xsl:if>
				<xsl:if test="$node/object[1]/void[@property='startArrowHead']/object/@field='TRIANGLE'"><xsl:value-of select ="$node/object[2]/@idref"/></xsl:if>
			</xsl:variable>
          <UML:Generalization.child>
            <UML:Class>
				<xsl:attribute name="xmi.idref"><xsl:value-of select = "generate-id(//void[@method='addNode']/object[@id=$idChild])" /></xsl:attribute>
            </UML:Class>
          </UML:Generalization.child>
          <UML:Generalization.parent>
            <UML:Class>
				<xsl:attribute name="xmi.idref"><xsl:value-of select = "generate-id(//void[@method='addNode']/object[@id=$idParent])" /></xsl:attribute>
            </UML:Class>
          </UML:Generalization.parent>
		</UML:Generalization>
	</xsl:template>
	

	<!-- ************************************************************************************** -->
	<!-- ********************************** UML:Abstraction *********************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template name="abstraction">
		<xsl:param name="node" />
		<UML:Abstraction>
				<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" /></xsl:attribute>
				<xsl:attribute name="name"><xsl:value-of select = "$node/object[1]/void[@property='middleLabel']/string" /></xsl:attribute>
				<xsl:attribute name="isSpecification">false</xsl:attribute>
			<xsl:variable name="idClient">
				<xsl:if test="$node/object[1]/void[@property='endArrowHead']/object/@field='TRIANGLE'"><xsl:value-of select ="$node/object[2]/@idref"/></xsl:if>
				<xsl:if test="$node/object[1]/void[@property='startArrowHead']/object/@field='TRIANGLE'"><xsl:value-of select ="$node/object[3]/@idref"/></xsl:if>
			</xsl:variable>
			<xsl:variable name="idSupplier">
				<xsl:if test="$node/object[1]/void[@property='endArrowHead']/object/@field='TRIANGLE'"><xsl:value-of select ="$node/object[3]/@idref"/></xsl:if>
				<xsl:if test="$node/object[1]/void[@property='startArrowHead']/object/@field='TRIANGLE'"><xsl:value-of select ="$node/object[2]/@idref"/></xsl:if>
			</xsl:variable>
          <UML:Dependency.client>
            <UML:Class>
				<xsl:attribute name="xmi.idref"><xsl:value-of select = "generate-id(//void[@method='addNode']/object[@id=$idClient])" /></xsl:attribute>
            </UML:Class>
          </UML:Dependency.client>
          <UML:Dependency.supplier>
            <UML:Class>
				<xsl:attribute name="xmi.idref"><xsl:value-of select = "generate-id(//void[@method='addNode']/object[@id=$idSupplier])" /></xsl:attribute>
            </UML:Class>
          </UML:Dependency.supplier>
		</UML:Abstraction>
	</xsl:template>
	

	<!-- ************************************************************************************** -->
	<!-- ********************************** UML:Dependency ************************************ -->
	<!-- ************************************************************************************** -->
	
	<xsl:template name="dependency">
		<xsl:param name="node" />
		<UML:Dependency>
				<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" /></xsl:attribute>
				<xsl:attribute name="name"><xsl:value-of select = "$node/object[1]/void[@property='middleLabel']/string" /></xsl:attribute>
				<xsl:attribute name="isSpecification">false</xsl:attribute>
			<xsl:variable name="idClient">
				<xsl:if test="$node/object[1]/void[@property='endArrowHead']/object/@field='V'"><xsl:value-of select ="$node/object[2]/@idref"/></xsl:if>
				<xsl:if test="$node/object[1]/void[@property='startArrowHead']/object/@field='V'"><xsl:value-of select ="$node/object[3]/@idref"/></xsl:if>
			</xsl:variable>
			<xsl:variable name="idSupplier">
				<xsl:if test="$node/object[1]/void[@property='endArrowHead']/object/@field='TRIANGLE'"><xsl:value-of select ="$node/object[3]/@idref"/></xsl:if>
				<xsl:if test="$node/object[1]/void[@property='startArrowHead']/object/@field='TRIANGLE'"><xsl:value-of select ="$node/object[2]/@idref"/></xsl:if>
			</xsl:variable>
          <UML:Dependency.client>
            <UML:Class>
				<xsl:attribute name="xmi.idref"><xsl:value-of select = "generate-id(//void[@method='addNode']/object[@id=$idClient])" /></xsl:attribute>
            </UML:Class>
          </UML:Dependency.client>
          <UML:Dependency.supplier>
            <UML:Class>
				<xsl:attribute name="xmi.idref"><xsl:value-of select = "generate-id(//void[@method='addNode']/object[@id=$idSupplier])" /></xsl:attribute>
            </UML:Class>
          </UML:Dependency.supplier>
		</UML:Dependency>
	</xsl:template>
	
	

	<!-- ************************************************************************************** -->
	<!-- ********************************** UML:Association *********************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template name="association">
		<xsl:param name="node" />
		<xsl:param name="aggregationStart" />
		<xsl:param name="aggregationEnd" />
		<xsl:param name="isnavigableStart" />
		<xsl:param name="isnavigableEnd" />
		<UML:Association>
			<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" /></xsl:attribute>
			<xsl:attribute name="name"><xsl:value-of select = "$node/object[1]/void[@property='middleLabel']/string" /></xsl:attribute>
			<xsl:attribute name="isSpecification">false</xsl:attribute>
			<xsl:attribute name="isRoot">false</xsl:attribute>
			<xsl:attribute name="isLeaf">false</xsl:attribute>
			<xsl:attribute name="isAbstract">false</xsl:attribute>
          <UML:Association.connection>
            <UML:AssociationEnd>
				<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-1</xsl:attribute>
				<xsl:attribute name="aggregation"><xsl:value-of select = "$aggregationStart" /></xsl:attribute>
				<xsl:attribute name="visibility">public</xsl:attribute>
				<xsl:attribute name="isNavigable"><xsl:value-of select = "$isnavigableStart" /></xsl:attribute>
				
				<xsl:variable name="value"><xsl:value-of select="translate($node/object[1]/void[@property='startLabel']/string,'&#x20;&#xa;&#xd;&#x9;&#160;','')" /></xsl:variable>
				<xsl:if test="not(contains($value, '..'))">
					<xsl:attribute name="name"><xsl:value-of select = "$value" /></xsl:attribute>
				</xsl:if>
				<xsl:if test="contains($value, '..')">
					<UML:AssociationEnd.multiplicity>
						<UML:Multiplicity>
							<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-1A</xsl:attribute>
							<UML:Multiplicity.range>
							<UML:MultiplicityRange>
								<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-1B</xsl:attribute>
								<xsl:attribute name="lower">		
									<xsl:call-template name="associationEnd">
										<xsl:with-param name="value" select="substring-before($value,'..')" />
									</xsl:call-template>
								</xsl:attribute>
								<xsl:attribute name="upper">		
									<xsl:call-template name="associationEnd">
										<xsl:with-param name="value" select="substring-after($value,'..')" />
									</xsl:call-template>
								</xsl:attribute>
							</UML:MultiplicityRange>
							</UML:Multiplicity.range>
						</UML:Multiplicity>
					</UML:AssociationEnd.multiplicity>
				</xsl:if>
				<xsl:if test="$value= '*'">
					<UML:AssociationEnd.multiplicity>
						<UML:Multiplicity>
							<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2A</xsl:attribute>
							<UML:Multiplicity.range>
							<UML:MultiplicityRange>
								<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2B</xsl:attribute>
								<xsl:attribute name="lower">0</xsl:attribute>
								<xsl:attribute name="upper">-1</xsl:attribute>
							</UML:MultiplicityRange>
							</UML:Multiplicity.range>
						</UML:Multiplicity>
					</UML:AssociationEnd.multiplicity>
				</xsl:if>
				<xsl:if test="$value= '1'">
					<UML:AssociationEnd.multiplicity>
						<UML:Multiplicity>
							<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2A</xsl:attribute>
							<UML:Multiplicity.range>
							<UML:MultiplicityRange>
								<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2B</xsl:attribute>
								<xsl:attribute name="lower">1</xsl:attribute>
								<xsl:attribute name="upper">1</xsl:attribute>
							</UML:MultiplicityRange>
							</UML:Multiplicity.range>
						</UML:Multiplicity>
					</UML:AssociationEnd.multiplicity>
				</xsl:if>
				<xsl:if test="string-length($value)= 0">
					<UML:AssociationEnd.multiplicity>
						<UML:Multiplicity>
							<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2A</xsl:attribute>
							<UML:Multiplicity.range>
							<UML:MultiplicityRange>
								<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2B</xsl:attribute>
								<xsl:attribute name="lower">1</xsl:attribute>
								<xsl:attribute name="upper">1</xsl:attribute>
							</UML:MultiplicityRange>
							</UML:Multiplicity.range>
						</UML:Multiplicity>
					</UML:AssociationEnd.multiplicity>
				</xsl:if>
            	<UML:AssociationEnd.participant>
            		<UML:Class>
            			<xsl:variable name ="id"><xsl:value-of select ="$node/object[2]/@idref"/></xsl:variable>
						<xsl:attribute name="xmi.idref"><xsl:value-of select = "generate-id(//void[@method='addNode']/object[@id=$id])" /></xsl:attribute>
					</UML:Class>
				</UML:AssociationEnd.participant>
            </UML:AssociationEnd>
            <UML:AssociationEnd>
				<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2</xsl:attribute>
				<xsl:attribute name="aggregation"><xsl:value-of select = "$aggregationEnd" /></xsl:attribute>
				<xsl:attribute name="visibility">public</xsl:attribute>
				<xsl:attribute name="isNavigable"><xsl:value-of select = "$isnavigableEnd" /></xsl:attribute>
								
				<xsl:variable name="value"><xsl:value-of select="translate($node/object[1]/void[@property='endLabel']/string,'&#x20;&#xa;&#xd;&#x9;&#160;','')" /></xsl:variable>
				<xsl:if test="not(contains($value, '..'))">
					<xsl:attribute name="name"><xsl:value-of select = "$value" /></xsl:attribute>
				</xsl:if>
				<xsl:if test="contains($value, '..')">
					<UML:AssociationEnd.multiplicity>
						<UML:Multiplicity>
							<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2A</xsl:attribute>
							<UML:Multiplicity.range>
							<UML:MultiplicityRange>
								<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2B</xsl:attribute>
								<xsl:attribute name="lower">		
									<xsl:call-template name="associationEnd">
										<xsl:with-param name="value" select="substring-before($value,'..')" />
									</xsl:call-template>
								</xsl:attribute>
								<xsl:attribute name="upper">		
									<xsl:call-template name="associationEnd">
										<xsl:with-param name="value" select="substring-after($value,'..')" />
									</xsl:call-template>
								</xsl:attribute>
							</UML:MultiplicityRange>
							</UML:Multiplicity.range>
						</UML:Multiplicity>
					</UML:AssociationEnd.multiplicity>
				</xsl:if>
				<xsl:if test="$value= '*'">
					<UML:AssociationEnd.multiplicity>
						<UML:Multiplicity>
							<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2A</xsl:attribute>
							<UML:Multiplicity.range>
							<UML:MultiplicityRange>
								<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2B</xsl:attribute>
								<xsl:attribute name="lower">0</xsl:attribute>
								<xsl:attribute name="upper">-1</xsl:attribute>
							</UML:MultiplicityRange>
							</UML:Multiplicity.range>
						</UML:Multiplicity>
					</UML:AssociationEnd.multiplicity>
				</xsl:if>
				<xsl:if test="$value= '1'">
					<UML:AssociationEnd.multiplicity>
						<UML:Multiplicity>
							<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2A</xsl:attribute>
							<UML:Multiplicity.range>
							<UML:MultiplicityRange>
								<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2B</xsl:attribute>
								<xsl:attribute name="lower">1</xsl:attribute>
								<xsl:attribute name="upper">1</xsl:attribute>
							</UML:MultiplicityRange>
							</UML:Multiplicity.range>
						</UML:Multiplicity>
					</UML:AssociationEnd.multiplicity>
				</xsl:if>
				<xsl:if test="string-length($value)= 0">
					<UML:AssociationEnd.multiplicity>
						<UML:Multiplicity>
							<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2A</xsl:attribute>
							<UML:Multiplicity.range>
							<UML:MultiplicityRange>
								<xsl:attribute name="xmi.id"><xsl:value-of select = "generate-id($node)" />-2B</xsl:attribute>
								<xsl:attribute name="lower">1</xsl:attribute>
								<xsl:attribute name="upper">1</xsl:attribute>
							</UML:MultiplicityRange>
							</UML:Multiplicity.range>
						</UML:Multiplicity>
					</UML:AssociationEnd.multiplicity>
				</xsl:if>
            	<UML:AssociationEnd.participant>
            		<UML:Class>
            			<xsl:variable name ="id"><xsl:value-of select ="$node/object[3]/@idref"/></xsl:variable>
						<xsl:attribute name="xmi.idref"><xsl:value-of select = "generate-id(//void[@method='addNode']/object[@id=$id])" /></xsl:attribute>
					</UML:Class>
				</UML:AssociationEnd.participant>
            </UML:AssociationEnd>
          </UML:Association.connection>
		</UML:Association>
	</xsl:template>
	
	<xsl:template name="associationEnd">
		<xsl:param name="value" />
		<xsl:if test="$value='*'">
			<xsl:variable name="value">-1</xsl:variable>
			<xsl:value-of select = "$value"/>
		</xsl:if>
		<xsl:if test="$value != '*'">
			<xsl:value-of select = "$value"/>
		</xsl:if>
	</xsl:template>
	


	<!-- ************************************************************************************** -->
	<!-- ********************************** other ********************************************* -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="void"/>

</xsl:stylesheet>
