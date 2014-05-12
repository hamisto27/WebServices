<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
<xsl:output method="xml" 
  doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" 
  doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes"   encoding="UTF-8" />
  
<xsl:template match="/">
  	<html>
  		<body>
  			<xsl:apply-templates select="collection/items" /> 
  			<xsl:apply-templates select="item" /> 
  		</body>
	</html>
</xsl:template>
  
<xsl:template match="item" >

<div> COCKSUCKER!!</div>
</xsl:template> 

<xsl:template match="items" >
	
	<xsl:choose>		
		<xsl:when test="not(compare(., $currentAuthor))">
		 	<xsl:value-of select="." />
		</xsl:when>
		<xsl:otherwise>
			<a href="../{functx:getURL(current())}.html"><xsl:value-of select="." /></a>
		</xsl:otherwise>
	</xsl:choose>
	<div id="users">
	
		<ul class="all">
			<xsl:for-each select="current()/item" >
				<li id="{data/id}">
				<xsl:apply-templates select="current()/data" /> 
				
					<xsl:for-each select="current()/links/link" >
						<xsl:if test=".">
							<xsl:element name="a">
								<xsl:attribute name="href">
									<xsl:value-of select="@href"/>
								</xsl:attribute>
								<xsl:attribute name="rel">
									<xsl:value-of select="@rel"/>
								</xsl:attribute>
								<xsl:value-of select="@rel"/>
							</xsl:element>
						</xsl:if>
					</xsl:for-each>
				</li>
			</xsl:for-each>
		</ul>
	</div>
</xsl:template>

<xsl:template match="data[@type = 'user']">

  	<xsl:element name="span">
		<xsl:attribute name="class">
			<xsl:value-of select="name(name)"/>
		</xsl:attribute>
		<xsl:value-of select="name"/>
	</xsl:element>
	
	<xsl:if test="email">
		<xsl:element name="span">
			<xsl:attribute name="class">
				<xsl:value-of select="name(email)"/>
			</xsl:attribute>
			<xsl:value-of select="email"/>
		</xsl:element>
	</xsl:if>
	
	<xsl:element name="span">
		<xsl:attribute name="class">
			<xsl:value-of select="name(friend_count)"/>
		</xsl:attribute>
		<xsl:value-of select="friend_count"/>
	</xsl:element>
	
	<xsl:element name="span">
		<xsl:attribute name="class">
			<xsl:value-of select="name(created)"/>
		</xsl:attribute>
		<xsl:value-of select="created"/>
	</xsl:element>
	
</xsl:template>



</xsl:stylesheet> 