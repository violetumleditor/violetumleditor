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
		<xsl:call-template name="index" />
		<xsl:apply-templates select="//UML:Class[@name]" />
		<xsl:apply-templates select="//UML:Generalization[@xmi.id]" />
		<xsl:apply-templates select="//UML:Abstraction[@xmi.id]" />
		<xsl:apply-templates select="//UML:Dependency[@xmi.id]" />
		<xsl:apply-templates select="//UML:Association[@xmi.id]" />
		</Violet>
	</xsl:template>
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create class ********************************************* -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Class">
		<xsl:variable name="id">
			<xsl:value-of select="@xmi.id" />
		</xsl:variable>
		<newFile>
			<xsl:attribute name="path">Class_<xsl:value-of select="@name"/>.html</xsl:attribute>
		   	<xsl:attribute name="name">Class_<xsl:value-of select="@name"/></xsl:attribute>
		   	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
     			<head>
        			<title>Documentation generate by Violet</title>
					<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        			<link rel="stylesheet" href="toc.css" type="text/css" />
    			</head>
    			<xsl:variable name="type">
    				<xsl:if test="count(.//UML:Stereotype)=0">
    					<xsl:text>Class</xsl:text>
    				</xsl:if>
    				<xsl:if test="count(.//UML:Stereotype)=1">
    					<xsl:text>Interface</xsl:text>
    				</xsl:if>    			
    			</xsl:variable>
		   		<h1><xsl:value-of select="$type"/><xsl:text> </xsl:text><xsl:value-of select="@name"/></h1>
		   		<h2><xsl:value-of select="@visibility"/><xsl:text> </xsl:text><xsl:value-of select="$type"/><xsl:text> </xsl:text><xsl:value-of select="@name"/></h2>
		   		<table class="summary">
    				<tr class="TableRowHeading"><td colspan="2">Attribute Summary</td></tr>
    				<xsl:for-each select=".//UML:Attribute">
    					<tr class="TableRow"><td class="TableCol1"><xsl:value-of select="@visibility"/><br/><xsl:value-of select="substring-before(.//UML:DataType/@xmi.idref,'-')"/></td>
    					<td class="TableCol2"><xsl:value-of select="@name"/></td></tr>    					
    				</xsl:for-each>
    			</table><br/>
		   		<table class="summary">
    				<tr class="TableRowHeading"><td colspan="2">Method Summary</td></tr>
    				<xsl:for-each select=".//UML:Operation">
    					<tr class="TableRow"><td class="TableCol1"><xsl:value-of select="@visibility"/><br/><xsl:value-of select="substring-before(.//UML:DataType/@xmi.idref,'-')"/></td>
    					<td class="TableCol2">
    						<xsl:value-of select="@name"/>
    						<xsl:text> (</xsl:text>
    						<xsl:for-each select=".//UML:Parameter">
								<xsl:value-of select="substring-before(.//UML:DataType/@xmi.idref,'-')" />
								<xsl:text> </xsl:text>
								<xsl:value-of select="@name" />
								<xsl:if test="position() != last()">
									<xsl:text>,</xsl:text>
    							</xsl:if>
							</xsl:for-each>	
    						<xsl:text>)</xsl:text>					
						</td></tr>    					
    				</xsl:for-each>
    			</table><br />
    			<table class="summary">
    				<tr class="TableRowHeading"><td colspan="2">Generalization Summary</td></tr>
    				<xsl:for-each select=".//UML:Generalization">
    					<tr class="TableRow">
    						<td class="TableCol1" colspan="2">
    						<xsl:variable name="name">
							<xsl:call-template name="getGeneralizationName">
                    			<xsl:with-param name = "id" ><xsl:value-of select="@xmi.idref" /></xsl:with-param> 
							</xsl:call-template>
							</xsl:variable>
							<a>
								<xsl:attribute name="href">Generalization<xsl:value-of select="$name" />.html</xsl:attribute>
		   						<xsl:attribute name="target">description</xsl:attribute>
		   						<xsl:value-of select="$name" />
		   					</a>
    						</td>
    					</tr>    					
    				</xsl:for-each>
    			</table><br/>
    			<table class="summary">
    				<tr class="TableRowHeading"><td colspan="2">Abstraction Summary</td></tr>
    				<xsl:for-each select=".//UML:Abstraction">
    					<tr class="TableRow">
    						<td class="TableCol1" colspan="2">
    						<xsl:variable name="name">
							<xsl:call-template name="getAbstractionName">
                    			<xsl:with-param name = "id" ><xsl:value-of select="@xmi.idref" /></xsl:with-param> 
							</xsl:call-template>
							</xsl:variable>
							<a>
								<xsl:attribute name="href">Abstraction<xsl:value-of select="$name" />.html</xsl:attribute>
		   						<xsl:attribute name="target">description</xsl:attribute>
		   						<xsl:value-of select="$name" />
		   					</a>
		   					</td>
		   				</tr>    					
    				</xsl:for-each>
    			</table><br/>
    			<table class="summary">
    				<tr class="TableRowHeading"><td colspan="2">Association Summary</td></tr>
    				<xsl:variable name="id"><xsl:value-of select="@xmi.id" /></xsl:variable>
    				<xsl:for-each select="//UML:Association[.//UML:Class/@xmi.idref=$id]">
    					<tr class="TableRow">
    						<td class="TableCol1" colspan="2">
    						<xsl:variable name="name">
							<xsl:call-template name="getAssociationName">
                    			<xsl:with-param name = "id" ><xsl:value-of select="@xmi.id" /></xsl:with-param> 
							</xsl:call-template>
							</xsl:variable>
							<a>
								<xsl:attribute name="href">Association<xsl:value-of select="$name" />.html</xsl:attribute>
		   						<xsl:attribute name="target">description</xsl:attribute>
		   						<xsl:value-of select="$name" />
		   					</a>
		   					</td>
		   				</tr>  					
    				</xsl:for-each>
    			</table><br/>
    			<table class="summary">
    				<tr class="TableRowHeading"><td colspan="2">Dependency Summary</td></tr>
    				<xsl:for-each select=".//UML:Dependency">
    					<tr class="TableRow">
    						<td class="TableCol1" colspan="2">
    						<xsl:variable name="name">
							<xsl:call-template name="getDependencyName">
                    			<xsl:with-param name = "id" ><xsl:value-of select="@xmi.idref" /></xsl:with-param> 
							</xsl:call-template>
							</xsl:variable>
							<a>
								<xsl:attribute name="href">Dependency<xsl:value-of select="$name" />.html</xsl:attribute>
		   						<xsl:attribute name="target">description</xsl:attribute>
		   						<xsl:value-of select="$name" />
		   					</a>
		   					</td>
    					</tr>    					
    				</xsl:for-each>
    			</table><br/>
    			<hr/>
		   		<table class="summary"><tr class="TableRowHeading"><td colspan="2">Attribute Detail</td></tr></table>
    			<xsl:for-each select=".//UML:Attribute">
    				<h3><xsl:value-of select="@visibility"/><xsl:text> </xsl:text><xsl:value-of select="@name"/>
    				<xsl:text> </xsl:text>
    				<xsl:value-of select="substring-before(.//UML:DataType/@xmi.idref,'-')"/>
    				</h3>
    				<xsl:apply-templates select="./@*"/>  
    				<xsl:apply-templates select=".//UML:MultiplicityRange/@*"/>	
    				<xsl:apply-templates select=".//UML:Attribute.initialValue/UML:Expression/@*"/>			
    			</xsl:for-each>
		   		
    			<hr/>
		   		<table class="summary"><tr class="TableRowHeading"><td colspan="2">Method Detail</td></tr></table>
    			<xsl:for-each select=".//UML:Operation">
    				<h3><xsl:value-of select="@visibility"/><xsl:text> </xsl:text><xsl:value-of select="@name"/></h3>
    				<xsl:apply-templates select="./@*"/> 
    				<xsl:text>Parameter</xsl:text><br/>
    				<xsl:for-each select=".//UML:Parameter">
    					<xsl:apply-templates select="./@*"/> 
    					<xsl:apply-templates select=".//UML:MultiplicityRange/@*"/>	
    					<xsl:apply-templates select=".//UML:Attribute.initialValue/UML:Expression/@*"/>	
    					<br/>		
    				</xsl:for-each>		
    			</xsl:for-each>
				<h6>Documentation HTML generate by <a href="http://alexdp.free.fr/violetumleditor/page.php">Violet</a></h6>
		   	</html>
		</newFile>
	</xsl:template>
	
	<xsl:template match="@*">
		<xsl:choose>		
			<xsl:when test="contains(name(), 'xmi.')"/>
			<xsl:when test="name() ='xmi.idref'">
				type:<xsl:value-of select="substring-before(.,'-')" /><br/>			
			</xsl:when>
			<xsl:when test="name() ='body'">
				initial value:<xsl:value-of select="." /><br/>			
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="name()" /><xsl:text> : </xsl:text><xsl:value-of select="." /><br/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Generalization ************************************ -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Generalization">
		<newFile>
			<xsl:variable name="name">
				<xsl:call-template name="getGeneralizationName">
            		<xsl:with-param name = "id" ><xsl:value-of select="@xmi.id" /></xsl:with-param> 
				</xsl:call-template>
			</xsl:variable>
			<xsl:attribute name="path">Generalization<xsl:value-of select="$name" />.html</xsl:attribute>
		   	<xsl:attribute name="name">index</xsl:attribute>
		   	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
     		<head>
        			<title>Documentation generate by Violet</title>
					<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        			<link rel="stylesheet" href="toc.css" type="text/css" />
    		</head>
    		<h1>Generalization <xsl:value-of select="$name"/></h1>
    		<table class="summary">
    			<tr class="TableRowHeading"><td colspan="2">Child</td></tr>
    			<tr class="TableRow">
    				<td class="TableCol1" colspan="2">
    				<xsl:variable name="idref"><xsl:value-of select=".//UML:Generalization.child/UML:Class/@xmi.idref" /></xsl:variable>
    				<xsl:variable name="class"><xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" /></xsl:variable>
    				<a>
						<xsl:attribute name="href">Class_<xsl:value-of select="$class" />.html</xsl:attribute>
		   				<xsl:attribute name="target">description</xsl:attribute>
		   				<xsl:value-of select="$class" />
		   			</a>
    				</td>
    			</tr>
    		</table><br/>
    		<table class="summary">
    			<tr class="TableRowHeading"><td colspan="2">Parent</td></tr>
    			<tr class="TableRow">
    				<td class="TableCol1" colspan="2">
    				<xsl:variable name="idref"><xsl:value-of select=".//UML:Generalization.parent/UML:Class/@xmi.idref" /></xsl:variable>
    				<xsl:variable name="class"><xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" /></xsl:variable>
    				<a>
						<xsl:attribute name="href">Class_<xsl:value-of select="$class" />.html</xsl:attribute>
		   				<xsl:attribute name="target">description</xsl:attribute>
		   				<xsl:value-of select="$class" />
		   			</a>
    				</td>
    			</tr>
    		</table>
			<h6>Documentation HTML generate by <a href="http://alexdp.free.fr/violetumleditor/page.php">Violet</a></h6>
			</html>
		</newFile>
	</xsl:template>
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Abstraction *************************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Abstraction">
		<newFile>
			<xsl:variable name="name">
				<xsl:call-template name="getAbstractionName">
            		<xsl:with-param name = "id" ><xsl:value-of select="@xmi.id" /></xsl:with-param> 
				</xsl:call-template>
			</xsl:variable>
			<xsl:attribute name="path">Abstraction<xsl:value-of select="$name" />.html</xsl:attribute>
		   	<xsl:attribute name="name">index</xsl:attribute>
		   	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
     		<head>
        			<title>Documentation generate by Violet</title>
					<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        			<link rel="stylesheet" href="toc.css" type="text/css" />
    		</head>
    		<h1>Abstraction <xsl:value-of select="$name"/></h1>
    		<table class="summary">
    			<tr class="TableRowHeading"><td colspan="2">Client</td></tr>
    			<tr class="TableRow">
    				<td class="TableCol1" colspan="2">
    				<xsl:variable name="idref"><xsl:value-of select=".//UML:Dependency.client/UML:Class/@xmi.idref" /></xsl:variable>
    				<xsl:variable name="class"><xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" /></xsl:variable>
    				<a>
						<xsl:attribute name="href">Class_<xsl:value-of select="$class" />.html</xsl:attribute>
		   				<xsl:attribute name="target">description</xsl:attribute>
		   				<xsl:value-of select="$class" />
		   			</a>
    				</td>
    			</tr>
    		</table><br/>
    		<table class="summary">
    			<tr class="TableRowHeading"><td colspan="2">Supplier</td></tr>
    			<tr class="TableRow">
    				<td class="TableCol1" colspan="2">
    				<xsl:variable name="idref"><xsl:value-of select=".//UML:Dependency.supplier/UML:Class/@xmi.idref" /></xsl:variable>
    				<xsl:variable name="class"><xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" /></xsl:variable>
    				<a>
						<xsl:attribute name="href">Class_<xsl:value-of select="$class" />.html</xsl:attribute>
		   				<xsl:attribute name="target">description</xsl:attribute>
		   				<xsl:value-of select="$class" />
		   			</a>
    				</td>
    			</tr>
    		</table>
			<h6>Documentation HTML generate by <a href="http://alexdp.free.fr/violetumleditor/page.php">Violet</a></h6>
			</html>
		</newFile>
	</xsl:template>
	
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Dependency *************************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Dependency">
		<newFile>
			<xsl:variable name="name">
				<xsl:call-template name="getDependencyName">
            		<xsl:with-param name = "id" ><xsl:value-of select="@xmi.id" /></xsl:with-param> 
				</xsl:call-template>
			</xsl:variable>
			<xsl:attribute name="path">Dependency<xsl:value-of select="$name" />.html</xsl:attribute>
		   	<xsl:attribute name="name">index</xsl:attribute>
		   	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
     		<head>
        			<title>Documentation generate by Violet</title>
					<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        			<link rel="stylesheet" href="toc.css" type="text/css" />
    		</head>
    		<h1>Dependency <xsl:value-of select="$name"/></h1>
    		<table class="summary">
    			<tr class="TableRowHeading"><td colspan="2">Client</td></tr>
    			<tr class="TableRow">
    				<td class="TableCol1" colspan="2">
    				<xsl:variable name="idref"><xsl:value-of select=".//UML:Dependency.client/UML:Class/@xmi.idref" /></xsl:variable>
    				<xsl:variable name="class"><xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" /></xsl:variable>
    				<a>
						<xsl:attribute name="href">Class_<xsl:value-of select="$class" />.html</xsl:attribute>
		   				<xsl:attribute name="target">description</xsl:attribute>
		   				<xsl:value-of select="$class" />
		   			</a>
    				</td>
    			</tr>
    		</table><br/>
    		<table class="summary">
    			<tr class="TableRowHeading"><td colspan="2">Supplier</td></tr>
    			<tr class="TableRow">
    				<td class="TableCol1" colspan="2">
    				<xsl:variable name="idref"><xsl:value-of select=".//UML:Dependency.supplier/UML:Class/@xmi.idref" /></xsl:variable>
    				<xsl:variable name="class"><xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" /></xsl:variable>
    				<a>
						<xsl:attribute name="href">Class_<xsl:value-of select="$class" />.html</xsl:attribute>
		   				<xsl:attribute name="target">description</xsl:attribute>
		   				<xsl:value-of select="$class" />
		   			</a>
    				</td>
    			</tr>
    		</table>
			<h6>Documentation HTML generate by <a href="http://alexdp.free.fr/violetumleditor/page.php">Violet</a></h6>
			</html>
		</newFile>
	</xsl:template>
	
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create Association *************************************** -->
	<!-- ************************************************************************************** -->
	
	<xsl:template match="UML:Association">
		<newFile>
			<xsl:variable name="name">
				<xsl:call-template name="getAssociationName">
            		<xsl:with-param name = "id" ><xsl:value-of select="@xmi.id" /></xsl:with-param> 
				</xsl:call-template>
			</xsl:variable>
			<xsl:attribute name="path">Association<xsl:value-of select="$name" />.html</xsl:attribute>
		   	<xsl:attribute name="name">index</xsl:attribute>
		   	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
     		<head>
        			<title>Documentation generate by Violet</title>
					<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        			<link rel="stylesheet" href="toc.css" type="text/css" />
    		</head>
    		<h1>Association <xsl:value-of select="$name"/></h1>
    		<table class="summary">
    			<tr class="TableRowHeading"><td colspan="2">Association with</td></tr>
    			<xsl:for-each select=".//UML:AssociationEnd">
    			<tr class="TableRow">
    				<td class="TableCol1">
    				<xsl:variable name="idref"><xsl:value-of select=".//UML:AssociationEnd.participant/UML:Class/@xmi.idref" /></xsl:variable>
    				<xsl:variable name="class"><xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" /></xsl:variable>
    				<a>
						<xsl:attribute name="href">Class_<xsl:value-of select="$class" />.html</xsl:attribute>
		   				<xsl:attribute name="target">description</xsl:attribute>
		   				<xsl:value-of select="$class" />
		   			</a>
    				</td>
    				<td class="TableCol2">
    				<xsl:text>lower: </xsl:text>
    				<xsl:value-of select=".//UML:MultiplicityRange/@lower" />
    				<br/>
    				<xsl:text>upper: </xsl:text>
    				<xsl:value-of select=".//UML:MultiplicityRange/@upper" />
    				<br/>
    				<xsl:apply-templates select="./@*[not(name()='name')]"/>
    				</td>    				 
    			</tr>
    			</xsl:for-each>
    		</table><br/>
			<h6>Documentation HTML generate by <a href="http://alexdp.free.fr/violetumleditor/page.php">Violet</a></h6>
			</html>
		</newFile>
	</xsl:template>
	
	
	<!-- ************************************************************************************** -->
	<!-- *************************** create index ********************************************* -->
	<!-- ************************************************************************************** -->
	
	<xsl:template name="index">
		<newFile>
			<xsl:attribute name="path">index.html</xsl:attribute>
		   	<xsl:attribute name="name">index</xsl:attribute>
		   	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
     		<head>
        		<title>Documentation generate by Violet</title>
				<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    		</head>
    		<frameset cols="20%,80%" frameborder="0" framespacing="0" border="0">
        		<frame src="menu.html" name="menu" scrolling="auto"/>
        		<frame src="toc.html" name="description" scrolling="auto"/>
    		</frameset>
			</html>
		</newFile>
		<newFile>
			<xsl:attribute name="path">toc.html</xsl:attribute>
		   	<xsl:attribute name="name">toc</xsl:attribute>
		   	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
     		<head>
        		<title>Documentation generate by Violet</title>
				<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        		<link rel="stylesheet" href="toc.css" type="text/css" />
    		</head>
    		<h1>Summary</h1><br/>
    		<xsl:text>Documentation HTML generate by </xsl:text><a href="http://alexdp.free.fr/violetumleditor/page.php">Violet</a>
    		<table class="summary">
    		<tr class="TableRow"><td class="TableCol1">Name</td><td class="TableCol2">...</td></tr>
    		<tr class="TableRow"><td class="TableCol1">Version</td><td class="TableCol2">...</td></tr>
    		</table>
			<h6>Documentation HTML generate by <a href="http://alexdp.free.fr/violetumleditor/page.php">Violet</a></h6>
			</html>
		</newFile>
		<newFile>
			<xsl:attribute name="path">menu.html</xsl:attribute>
		   	<xsl:attribute name="name">menu</xsl:attribute>
		   	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
     		<head>
        		<title>Documentation generate by Violet</title>
				<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        		<link rel="stylesheet" href="menu.css" type="text/css" />
        		<script type="text/javascript" src="menu.js"></script>
    		</head>
			<body>
			<xsl:text>Menu</xsl:text>
			<br/>
			<dl id="menu">
				<dt onclick="javascript:montre('smenu1');">Class</dt>
					<xsl:if test="not(count(//UML:Class[@name][count(.//UML:Stereotype)=0])=0)">
					<dd id="smenu1">
						<ul>
						<xsl:for-each select="//UML:Class[@name][count(.//UML:Stereotype)=0]">
							<li>
								<a>
									<xsl:attribute name="href">Class_<xsl:value-of select="@name" />.html</xsl:attribute>
		   							<xsl:attribute name="target">description</xsl:attribute>
		   							<xsl:value-of select="@name" />
		   						</a>
		   					</li>
						</xsl:for-each>
						</ul>
					</dd>
					</xsl:if>
				<dt onclick="javascript:montre('smenu2');">Interface</dt>
					<xsl:if test="not(count(//UML:Class[@name][count(.//UML:Stereotype)=1])=0)">
					<dd id="smenu2">
						<ul>
						<xsl:for-each select="//UML:Class[@name][count(.//UML:Stereotype)=1]">
							<li>
								<a>
									<xsl:attribute name="href">Class_<xsl:value-of select="@name" />.html</xsl:attribute>
		   							<xsl:attribute name="target">description</xsl:attribute>
		   							<xsl:value-of select="@name" />
		   						</a>
		   					</li>
						</xsl:for-each>
						</ul>
					</dd>
					</xsl:if>
				<dt onclick="javascript:montre('smenu3');">Generalization</dt>
					<xsl:if test="not(count(//UML:Generalization[@xmi.id])=0)">
					<dd id="smenu3">
						<ul>
						<xsl:for-each select="//UML:Generalization[@xmi.id]">
						<xsl:variable name="name">
							<xsl:call-template name="getGeneralizationName">
                    			<xsl:with-param name = "id" ><xsl:value-of select="@xmi.id" /></xsl:with-param> 
							</xsl:call-template>
						</xsl:variable>
							<li>
								<a>
									<xsl:attribute name="href">Generalization<xsl:value-of select="$name" />.html</xsl:attribute>
		   							<xsl:attribute name="target">description</xsl:attribute>
		   							<xsl:value-of select="$name" />
		   						</a>
		   					</li>
						</xsl:for-each>
						</ul>
					</dd>
					</xsl:if>
				<dt onclick="javascript:montre('smenu4');">Abstraction</dt>
					<xsl:if test="not(count(//UML:Abstraction[@xmi.id])=0)">
					<dd id="smenu4">
						<ul>
						<xsl:for-each select="//UML:Abstraction[@xmi.id]">
						<xsl:variable name="name">
							<xsl:call-template name="getAbstractionName">
                    			<xsl:with-param name = "id" ><xsl:value-of select="@xmi.id" /></xsl:with-param> 
							</xsl:call-template>
						</xsl:variable>
							<li>
								<a>
									<xsl:attribute name="href">Abstraction<xsl:value-of select="$name" />.html</xsl:attribute>
		   							<xsl:attribute name="target">description</xsl:attribute>
		   							<xsl:value-of select="$name" />
		   						</a>
		   					</li>
						</xsl:for-each>
						</ul>
					</dd>
					</xsl:if>
				<dt onclick="javascript:montre('smenu5');">Association</dt>
					<xsl:if test="not(count(//UML:Association[@xmi.id])=0)">
					<dd id="smenu5">
						<ul>
						<xsl:for-each select="//UML:Association[@xmi.id]">
						<xsl:variable name="name">
							<xsl:call-template name="getAssociationName">
                    			<xsl:with-param name = "id" ><xsl:value-of select="@xmi.id" /></xsl:with-param> 
							</xsl:call-template>
						</xsl:variable>
							<li>
								<a>
									<xsl:attribute name="href">Association<xsl:value-of select="$name" />.html</xsl:attribute>
		   							<xsl:attribute name="target">description</xsl:attribute>
		   							<xsl:value-of select="$name" />
		   						</a>
		   					</li>
						</xsl:for-each>
						</ul>
					</dd>
					</xsl:if>
				<dt onclick="javascript:montre('smenu6');">Dependency</dt>
					<xsl:if test="not(count(//UML:Dependency[@xmi.id])=0)">
					<dd id="smenu6">
						<ul>
						<xsl:for-each select="//UML:Dependency[@xmi.id]">
						<xsl:variable name="name">
							<xsl:call-template name="getDependencyName">
                    			<xsl:with-param name = "id" ><xsl:value-of select="@xmi.id" /></xsl:with-param> 
							</xsl:call-template>
						</xsl:variable>
							<li>
								<a>
									<xsl:attribute name="href">Dependency<xsl:value-of select="$name" />.html</xsl:attribute>
		   							<xsl:attribute name="target">description</xsl:attribute>
		   							<xsl:value-of select="$name" />
		   						</a>
		   					</li>
						</xsl:for-each>
						</ul>
					</dd>
					</xsl:if>
			</dl>
			</body>
			</html>
		</newFile>
		<newFile>
			<xsl:attribute name="path">menu.css</xsl:attribute>
		   	<xsl:attribute name="name">menu</xsl:attribute>			
		   	<xsl:text>body {&#xa;</xsl:text>
			<xsl:text>margin: 0;&#xa;</xsl:text>
			<xsl:text>padding: 0;&#xa;</xsl:text>
			<xsl:text>background: white;&#xa;</xsl:text>
			<xsl:text>font: 80% verdana, arial, sans-serif;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>dl, dt, dd, ul, li {&#xa;</xsl:text>
			<xsl:text>margin: 0;&#xa;</xsl:text>
			<xsl:text>padding: 0;&#xa;</xsl:text>
			<xsl:text>list-style-type: none;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>#menu {&#xa;</xsl:text>
			<xsl:text>position: absolute;&#xa;</xsl:text>
			<xsl:text>top: 0;&#xa;</xsl:text>
			<xsl:text>left: 0;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>dl#menu {&#xa;</xsl:text>
			<xsl:text>width: 15em;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>dl#menu dt {&#xa;</xsl:text>
			<xsl:text>cursor: pointer;&#xa;</xsl:text>
			<xsl:text>margin: 2px 0;;&#xa;</xsl:text>
			<xsl:text>height: 20px;&#xa;</xsl:text>
			<xsl:text>line-height: 20px;&#xa;</xsl:text>
			<xsl:text>text-align: center;&#xa;</xsl:text>
			<xsl:text>font-weight: bold;&#xa;</xsl:text>
			<xsl:text>border: 1px solid gray;&#xa;</xsl:text>
			<xsl:text>background: yellow;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>dl#menu dd {&#xa;</xsl:text>
			<xsl:text>border: 1px solid gray;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>dl#menu li {&#xa;</xsl:text>
			<xsl:text>text-align: center;&#xa;</xsl:text>
			<xsl:text>background: #fff;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>dl#menu li a, dl#menu dt a {&#xa;</xsl:text>
			<xsl:text>color: #000;&#xa;</xsl:text>
			<xsl:text>text-decoration: none;&#xa;</xsl:text>
			<xsl:text>display: block;&#xa;</xsl:text>
			<xsl:text>border: 0 none;&#xa;</xsl:text>
			<xsl:text>height: 100%;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>&#xa;</xsl:text>
			<xsl:text>dl#menu li a:hover, dl#menu dt a:hover {&#xa;</xsl:text>
			<xsl:text>background: #eee;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>&#xa;</xsl:text>
			<xsl:text>#mentions {&#xa;</xsl:text>
			<xsl:text>font-family: verdana, arial, sans-serif;&#xa;</xsl:text>
			<xsl:text>position: absolute;&#xa;</xsl:text>
			<xsl:text>bottom : 200px;&#xa;</xsl:text>
			<xsl:text>left : 10px;&#xa;</xsl:text>
			<xsl:text>color: #000;&#xa;</xsl:text>
			<xsl:text>background-color: #ddd;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>#mentions a {text-decoration: none;&#xa;</xsl:text>
			<xsl:text>color: #222;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>#mentions a:hover{text-decoration: underline;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
		</newFile>
		<newFile>
			<xsl:attribute name="path">menu.js</xsl:attribute>
		   	<xsl:attribute name="name">menu</xsl:attribute>
		   	<xsl:text>window.onload=montre;&#xa;</xsl:text>
		   	<xsl:text>function montre(id) {&#xa;</xsl:text>
		   	<xsl:text>var d = document.getElementById(id);&#xa;</xsl:text>
		   	<xsl:text>	for (var i = 1; i&lt;=10; i++) {&#xa;</xsl:text>
		   	<xsl:text>		if (document.getElementById('smenu'+i)) {document.getElementById('smenu'+i).style.display='none';}&#xa;</xsl:text>
		   	<xsl:text>	}&#xa;</xsl:text>
		   	<xsl:text>if (d) {d.style.display='block';}&#xa;</xsl:text>
		   	<xsl:text>}&#xa;</xsl:text>
		</newFile>
		<newFile>
			<xsl:attribute name="path">toc.css</xsl:attribute>
		   	<xsl:attribute name="name">toc.css</xsl:attribute>
		   	<xsl:text>A {&#xa;</xsl:text>
			<xsl:text>color: #003399;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>A:active {&#xa;</xsl:text>
			<xsl:text>color: #003399;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>A:visited {&#xa;</xsl:text>
			<xsl:text>color: #888888;&#xa;</xsl:text>
			<xsl:text>}TABLE.summary  {&#xa;</xsl:text>
			<xsl:text>border-style:solid;&#xa;</xsl:text>
			<xsl:text>border-width:1px;&#xa;</xsl:text>
			<xsl:text>border-color:black;&#xa;</xsl:text>
			<xsl:text>border-collapse: collapse;&#xa;</xsl:text>
			<xsl:text>border-spacing:0;&#xa;</xsl:text>
			<xsl:text>empty-cells: hide;width: 90%;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>TD  {border-style:solid;&#xa;</xsl:text>
			<xsl:text>border-width:1px;&#xa;</xsl:text>
			<xsl:text>border-color:black;&#xa;</xsl:text>
			<xsl:text>border-collapse: collapse;&#xa;</xsl:text>
			<xsl:text>border-spacing:0;&#xa;</xsl:text>
			<xsl:text>padding: 4pt;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>TD.TableCol1  {&#xa;</xsl:text>
			<xsl:text>font-size: 70%;&#xa;</xsl:text>
			<xsl:text>color: #888888;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>H1 { &#xa;</xsl:text>
			<xsl:text>font-size: 120%;&#xa;</xsl:text>
			<xsl:text>color: blue;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>H2 { &#xa;</xsl:text>
			<xsl:text>font-size: 120%;&#xa;</xsl:text>
			<xsl:text>color: orange;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>TD.TableCol1 {&#xa;</xsl:text>
			<xsl:text>width: 30%;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>TR.TableRowHeading {&#xa;</xsl:text>
			<xsl:text>background-color:yellow;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
			<xsl:text>H6 {&#xa;</xsl:text>
			<xsl:text>font-size: 60%;&#xa;</xsl:text>
			<xsl:text>color: grey;&#xa;</xsl:text>
			<xsl:text>text-align: center;&#xa;</xsl:text>
			<xsl:text>}&#xa;</xsl:text>
		</newFile>
	</xsl:template>
	
	<xsl:template name="getGeneralizationName">
        <xsl:param name = "id" /> 
		<xsl:if test="not(string-length(//UML:Generalization[@xmi.id=$id]/@name) = 0)">
			<xsl:value-of select="//UML:Generalization[@xmi.id=$id]/@name" />
		</xsl:if>
		<xsl:if test="string-length(//UML:Generalization[@xmi.id=$id]/@name) = 0">
			<xsl:variable name="idref2">
				<xsl:value-of select="//UML:Generalization[@xmi.id=$id]/UML:Generalization.child/UML:Class/@xmi.idref"/>
			</xsl:variable>
			<xsl:value-of select="//UML:Class[@xmi.id=$idref2]/@name" />
			<xsl:text>_To_</xsl:text>
			<xsl:variable name="idref">
				<xsl:value-of select="//UML:Generalization[@xmi.id=$id]/UML:Generalization.parent/UML:Class/@xmi.idref"/>
			</xsl:variable>
			<xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="getAbstractionName">
        <xsl:param name = "id" /> 
		<xsl:if test="not(string-length(//UML:Abstraction[@xmi.id=$id]/@name) = 0)">
			<xsl:value-of select="//UML:Abstraction[@xmi.id=$id]/@name" />
		</xsl:if>
		<xsl:if test="string-length(//UML:Abstraction[@xmi.id=$id]/@name) = 0">
			<xsl:variable name="idref">
				<xsl:value-of select="//UML:Abstraction[@xmi.id=$id]/UML:Dependency.client/UML:Class/@xmi.idref"/>
			</xsl:variable>
			<xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" />
			<xsl:text>_To_</xsl:text>
			<xsl:variable name="idref2">
				<xsl:value-of select="//UML:Abstraction[@xmi.id=$id]/UML:Dependency.supplier/UML:Class/@xmi.idref"/>
			</xsl:variable>
			<xsl:value-of select="//UML:Class[@xmi.id=$idref2]/@name" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="getAssociationName">
        <xsl:param name = "id" /> 
		<xsl:if test="not(string-length(//UML:Association[@xmi.id=$id]/@name) = 0)">
			<xsl:value-of select="//UML:Association[@xmi.id=$id]/@name" />
		</xsl:if>
		<xsl:if test="string-length(//UML:Association[@xmi.id=$id]/@name) = 0">
			<xsl:for-each select="//UML:Association[@xmi.id=$id]//UML:Class/@xmi.idref">
				<xsl:variable name="idref"><xsl:value-of select="."/></xsl:variable>
				<xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" />
				<xsl:if test="position() != last()">
					<xsl:text>_To_</xsl:text>
    			</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="getDependencyName">
        <xsl:param name = "id" /> 
		<xsl:if test="not(string-length(//UML:Dependency[@xmi.id=$id]/@name) = 0)">
			<xsl:value-of select="/UML:Dependency[@xmi.id=$id]/@name" />
		</xsl:if>
		<xsl:if test="string-length(//UML:Dependency[@xmi.id=$id]/@name) = 0">
			<xsl:variable name="idref">
				<xsl:value-of select="//UML:Dependency[@xmi.id=$id]/UML:Dependency.client/UML:Class/@xmi.idref"/>
			</xsl:variable>
			<xsl:value-of select="//UML:Class[@xmi.id=$idref]/@name" />
			<xsl:text>_To_</xsl:text>
			<xsl:variable name="idref2">
				<xsl:value-of select="//UML:Dependency[@xmi.id=$id]/UML:Dependency.supplier/UML:Class/@xmi.idref"/>
			</xsl:variable>
			<xsl:value-of select="//UML:Class[@xmi.id=$idref2]/@name" />
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
