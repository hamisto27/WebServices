<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes" encoding="utf-8" />
  
<xsl:template match="/">

	<xsl:apply-templates select="collection/items" /> 
	<xsl:apply-templates select="item" />
	<xsl:apply-templates select="error" />  

</xsl:template>
  
<xsl:template match="item" >

	<div class="item-wrapper">
		<xsl:element name="ul">
			<xsl:attribute name="class">item</xsl:attribute>
			<xsl:element name="li">
				<xsl:if test="data/id">
					<xsl:attribute name="id">
						<xsl:value-of select="data/id"/>
					</xsl:attribute>
				<xsl:if test="data/@type">
					<xsl:attribute name="data-type">
						<xsl:value-of select="data/@type"/>
					</xsl:attribute>
				</xsl:if>
				</xsl:if>
				<xsl:apply-templates select="current()/data" /> 
				<xsl:apply-templates select="current()/links" />
			</xsl:element>
		</xsl:element>
	</div>

</xsl:template> 

<xsl:template match="items" >

	<div class="collection-wrapper">
		<xsl:element name="ul">
			<xsl:attribute name="class">collection</xsl:attribute>
			<xsl:for-each select="current()/item" >
				<xsl:element name="li">
					<xsl:if test="data/id">
						<xsl:attribute name="id">
							<xsl:value-of select="data/id"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:if test="data/@type">
						<xsl:attribute name="data-type">
							<xsl:value-of select="data/@type"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:apply-templates select="current()/data" /> 
					<xsl:apply-templates select="current()/links" />
				</xsl:element>
			</xsl:for-each>
		</xsl:element>
	</div>
</xsl:template>

<xsl:template match="data[@type = 'user']">
	
	<xsl:for-each select="*" >
		<xsl:if test="current() and name(current()) != 'id'">
			<xsl:element name="span">
				<xsl:attribute name="class">
					<xsl:value-of select="name(current())"/>
				</xsl:attribute>
				<xsl:value-of select="current()"/>
			</xsl:element>
		</xsl:if>
	</xsl:for-each>
	
</xsl:template>

<xsl:template match="data[@type = 'friend']">
	
	<xsl:element name="div">
		<xsl:attribute name="class">friendship</xsl:attribute>
		<xsl:element name="span">
			<xsl:attribute name="class">friend-one</xsl:attribute>
			<xsl:value-of select="friendship/friendOne"/>
		</xsl:element>
		<xsl:element name="span">
			<xsl:attribute name="class">friend-two</xsl:attribute>
			<xsl:value-of select="friendship/friendTwo"/>
		</xsl:element>
	</xsl:element>
	
  	<xsl:element name="span">
		<xsl:attribute name="class">
			<xsl:value-of select="name(status)"/>
		</xsl:attribute>
		<xsl:value-of select="status"/>
	</xsl:element>
	
	<xsl:element name="span">
		<xsl:attribute name="class">
			<xsl:value-of select="name(since)"/>
		</xsl:attribute>
		<xsl:value-of select="since"/>
	</xsl:element>
	
</xsl:template>

<xsl:template match="data[@type = 'userSeries']">

	<xsl:element name="span">
		<xsl:attribute name="data-id">
			<xsl:value-of select="series/id"/>
		</xsl:attribute>
		<xsl:attribute name="class">
			<xsl:value-of select="name(series/name)"/>
		</xsl:attribute>
		<xsl:value-of select="series/name"/>
	</xsl:element>
	
	<xsl:for-each select="*" >
		<xsl:if test="current() and name(current()) != 'series'">
			<xsl:element name="span">
				<xsl:attribute name="class">
					<xsl:value-of select="name(current())"/>
				</xsl:attribute>
				<xsl:value-of select="current()"/>
			</xsl:element>
		</xsl:if>
	</xsl:for-each>
	
</xsl:template>

<xsl:template match="data[@type = 'series']">
	
	<xsl:for-each select="*" >
		<xsl:if test="current() and name(current()) != 'id'">
			<xsl:element name="span">
				<xsl:attribute name="class">
					<xsl:value-of select="name(current())"/>
				</xsl:attribute>
				<xsl:value-of select="current()"/>
			</xsl:element>
		</xsl:if>
	</xsl:for-each>
			
</xsl:template>

<xsl:template match="data[@type = 'season']">
	
	<xsl:if test="number">
		 <xsl:element name="span">
			<xsl:attribute name="class">
				<xsl:value-of select="name(number)"/>
			</xsl:attribute>
			<xsl:value-of select="number"/>
		</xsl:element>
	</xsl:if>
		
	<xsl:if test="episodes">
		<xsl:element name="ul">
			<xsl:attribute name="class">episodes</xsl:attribute>
			<xsl:for-each select="episodes/episode" >
			 	<xsl:element name="li">
					<xsl:attribute name="class">
						<xsl:value-of select="name(current())"/>
					</xsl:attribute>
					<xsl:attribute name="data-number">
						<xsl:value-of select="current()/number"/>
					</xsl:attribute>
					<xsl:value-of select="current()/name"/>
				</xsl:element>
			</xsl:for-each>
		</xsl:element> 
	</xsl:if>
			
</xsl:template>

<xsl:template match="data[@type = 'episode']">
	
	<xsl:for-each select="*" >
		<xsl:if test="current() and name(current()) != 'id'">
			<xsl:element name="span">
				<xsl:attribute name="class">
					<xsl:value-of select="name(current())"/>
				</xsl:attribute>
				<xsl:value-of select="current()"/>
			</xsl:element>
		</xsl:if>
	</xsl:for-each>
	
</xsl:template>


<xsl:template match="error" >

	<div class="wrapper-error">
		<xsl:for-each select="*" >
			<xsl:if test="current()">
				<xsl:element name="span">
					<xsl:attribute name="class">
						<xsl:value-of select="name(current())"/>
					</xsl:attribute>
					<xsl:value-of select="current()"/>
				</xsl:element>
			</xsl:if>
		</xsl:for-each> 
	</div>
	
</xsl:template>

<xsl:template match="links">
	<!-- <xsl:for-each select="link" >
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
	</xsl:for-each>  -->
</xsl:template>


</xsl:stylesheet> 