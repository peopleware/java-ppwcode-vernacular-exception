<?xml version='1.0' encoding='iso-8859-1'?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" version="1.0" encoding="utf-8" indent="yes"/>

  <xsl:variable name="ppw-bean" select="document('../../ppw-bean/project.xml')/project" />
  <xsl:variable name="ppw-exception" select="document('../../ppw-exception/project.xml')/project" />
  <xsl:variable name="ppw-i18n" select="document('../../ppw-i18n/project.xml')/project" />
  <xsl:variable name="ppw-jsf" select="document('../../ppw-jsf/project.xml')/project" />
  <xsl:variable name="ppw-jsp" select="document('../../ppw-jsp/project.xml')/project" />
  <xsl:variable name="ppw-persistence" select="document('../../ppw-persistence/project.xml')/project" />
  <xsl:variable name="ppw-servlet" select="document('../../ppw-servlet/project.xml')/project" />
  <xsl:variable name="ppw-struts" select="document('../../ppw-struts/project.xml')/project" />
  <xsl:variable name="ppw-test" select="document('../../ppw-test/project.xml')/project" />
  <xsl:variable name="ppw-value" select="document('../../ppw-value/project.xml')/project" />
  
  <xsl:key name="libraryLookup" match="library" use="artifactId" />
  
  <xsl:variable name="libraries" select="document('libraries.xml')/libraries" />
  
  <xsl:template match="libraries" mode="name">
    <xsl:param name="artifactId" />
    <xsl:value-of select="key('libraryLookup', $artifactId)/name"/>
  </xsl:template>
  
  <xsl:template match="/project">
    digraph PeopleWare_Libraries_Dependencies {

      graph [	fontname = "Helvetica-Oblique",
        fontsize = 36,
        label = "PeopleWare Libraries Dependencies, NOW",
        size = "10,50"];

      concentrate = true;
      nslimit=10.0;
      mclimit=10.0;

      node [shape = rectangle, fillcolor=aquamarine2,
        fontname = "Helvetica", fontsize = 11];

      // edge [len=4.2];
      /* optional = blue
      only needed for the tests = chocolate */
      
      subgraph cluster_PPW_Libraries {
    
        ordering = out;
        color = aquamarine4;
        fontcolor = aquamarine4;
        label = "Peopleware Java Libraries";
        
        node [style=filled, fillcolor=aquamarine2];
    
        <xsl:call-template name="ppwNode">
          <xsl:with-param name="project" select="$ppw-bean" />
        </xsl:call-template>
        <xsl:call-template name="ppwNode">
          <xsl:with-param name="project" select="$ppw-exception" />
        </xsl:call-template>
        <xsl:call-template name="ppwNode">
          <xsl:with-param name="project" select="$ppw-i18n" />
        </xsl:call-template>
        <xsl:call-template name="ppwNode">
          <xsl:with-param name="project" select="$ppw-jsf" />
        </xsl:call-template>
        <xsl:call-template name="ppwNode">
          <xsl:with-param name="project" select="$ppw-jsp" />
        </xsl:call-template>
        <xsl:call-template name="ppwNode">
          <xsl:with-param name="project" select="$ppw-persistence" />
        </xsl:call-template>
        <xsl:call-template name="ppwNode">
          <xsl:with-param name="project" select="$ppw-servlet" />
        </xsl:call-template>
        <xsl:call-template name="ppwNode">
          <xsl:with-param name="project" select="$ppw-struts" />
        </xsl:call-template>
        <xsl:call-template name="ppwNode">
          <xsl:with-param name="project" select="$ppw-test" />
        </xsl:call-template>
        <xsl:call-template name="ppwNode">
          <xsl:with-param name="project" select="$ppw-value" />
        </xsl:call-template>
      }
    
      subgraph cluster_web_apis {
    
        label = "Standard Web API's";
    
        servletapi_servletapi      [label="Servlet API\nv 2.4"];
        jspapi_jsp_api             [label="JSP API\nv 2.0"];
        jstl_jstl                  [label="JSTL\nv 1.1.2"];
        javamail_mailapi           [label="Mail API\nv 1.3.2"];
        jsf_jsf_api                [label="JavaServer Faces API\nv 1.1.01"];
    
      }
    
        
      subgraph cluster_Apache_jakarta {
    
        label = "Apache Jakarta";
      
        taglibs_standard       [label="standard\n(Apache JSTL Impl.)"];
        struts_struts     [label="Struts"];
        myfaces_myfaces   [label="MyFaces"];
    
        subgraph cluster_Apache_jakarta_commons {
      
          label = "Apache Jakarta Commons";
      
          commons_logging_commons_logging   [label="Commons logging"];
          commons_beanutils_commons_beanutils [label="Commons beanutils"];
    
        }
      
      }
    
      subgraph cluster_JBoss {
  
        label = "JBoss";
  
        hibernate_hibernate [label="Hibernate"];
  
      }
    
      displaytag_displaytag [label="displaytag"];
      toryt_toryt [label="Toryt"];
    
      /* dependencies */
      edge [weight=1];
    
      <xsl:call-template name="projectDependencies">
        <xsl:with-param name="project" select="$ppw-bean" />
      </xsl:call-template>
      <xsl:call-template name="projectDependencies">
        <xsl:with-param name="project" select="$ppw-exception" />
      </xsl:call-template>
      <xsl:call-template name="projectDependencies">
        <xsl:with-param name="project" select="$ppw-i18n" />
      </xsl:call-template>
      <xsl:call-template name="projectDependencies">
        <xsl:with-param name="project" select="$ppw-jsf" />
      </xsl:call-template>
      <xsl:call-template name="projectDependencies">
        <xsl:with-param name="project" select="$ppw-jsp" />
      </xsl:call-template>
      <xsl:call-template name="projectDependencies">
        <xsl:with-param name="project" select="$ppw-persistence" />
      </xsl:call-template>
      <xsl:call-template name="projectDependencies">
        <xsl:with-param name="project" select="$ppw-servlet" />
      </xsl:call-template>
      <xsl:call-template name="projectDependencies">
        <xsl:with-param name="project" select="$ppw-struts" />
      </xsl:call-template>
      <xsl:call-template name="projectDependencies">
        <xsl:with-param name="project" select="$ppw-test" />
      </xsl:call-template>
      <xsl:call-template name="projectDependencies">
        <xsl:with-param name="project" select="$ppw-value" />
      </xsl:call-template>
    }
  </xsl:template>

  <xsl:template name="ppwNode">
    <xsl:param name="project" />
    <xsl:value-of select="translate($project/groupId, '-', '_')"/>_<xsl:value-of
      select="translate($project/artifactId, '-', '_')"/>
    [label="<xsl:value-of select="$project/name"/>\nv <xsl:value-of select="$project/currentVersion"/>"];
  </xsl:template>    
    
  <xsl:template name="projectDependencies">
    <xsl:param name="project" />
    <xsl:value-of select="translate($project/groupId, '-', '_')"/>_<xsl:value-of
      select="translate($project/artifactId, '-', '_')"/>
    -> {<xsl:apply-templates select="$project/dependencies" mode="arcs"/>           };
  </xsl:template>
  
  <xsl:template match="/project/dependencies/dependency" mode="nodes">
    <xsl:value-of select="translate(groupId, '-', '_')"/>_<xsl:value-of select="translate(artifactId, '-', '_')"/>_<xsl:value-of
      select="translate(version, '.-', '_')"/> [label="<xsl:apply-templates select="$libraries" mode="name">
                                                                      <xsl:with-param name="artifactId" select="artifactId" />
                                                                    </xsl:apply-templates>\nv <xsl:value-of select="version"/>"];</xsl:template>

  <xsl:template match="/project/dependencies/dependency" mode="arcs">
    <xsl:value-of select="translate(groupId, '-', '_')"/>_<xsl:value-of select="translate(artifactId, '-', '_')"/>;</xsl:template>

</xsl:stylesheet>
