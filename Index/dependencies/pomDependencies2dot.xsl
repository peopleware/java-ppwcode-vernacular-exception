<?xml version='1.0' encoding='iso-8859-1'?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:date="http://exslt.org/dates-and-times"
  extension-element-prefixes="date">
  <!-- uses EXSLT, which is supported by Saxon and Xalan; see http://www.exslt.org/ -->
  
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
  <xsl:variable name="olts" select="document('../../OLTS/project.xml')/project" />
  
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
        label = "PeopleWare Libraries Dependencies, <xsl:value-of select="date:date()"/>"];

      concentrate = false
      rankdir = LR;
      ranksep = 2;
      nslimit=1000.0;
      mclimit=1000.0;
    

      node [shape = rectangle, fontname = "Helvetica", fontsize = 11];

      // edge [len=4.2];
      /* optional = blue
      only needed for the tests = chocolate */
      
      subgraph cluster_PPW_Libraries {
    
        ordering = in;
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

<!--    
      subgraph cluster_PPW_Projects {
      
        ordering = in;
        label = "Peopleware Projects";
        
        node [style=filled, fillcolor=palegreen4, fotcolor=white];
    
        <xsl:call-template name="ppwNode">
          <xsl:with-param name="project" select="$olts" />
        </xsl:call-template>
    
      }
-->
    
      subgraph cluster_web_apis {
    
        label = "Standard Web API's";
    
        servletapi_servletapi_2_4_20040521  [label="Servlet API\nv 2.4"];
        jspapi_jsp_api_2_0_20040521         [label="JSP API\nv 2.0"];
        jstl_jstl_1_1_2            [label="JSTL\nv 1.1.2"];
        javamail_mailapi_1_3_2     [label="Mail API\nv 1.3.2"];
        jsf_jsf_api_1_1_01         [label="JavaServer Faces API\nv 1.1.01"];
    
      }
    
        
      subgraph cluster_Apache_jakarta {
    
        label = "Apache Jakarta";
      
        subgraph cluster_Apache_jakarta_taglibs {
    
          label = "Apache JSTL Impl.";
    
          taglibs_standard_1_1_2       [label="standard\n v 1.1.2"];
          taglibs_standard_1_1_1       [label="standard\n v 1.1.1"];
    
        }
    
        struts_struts_1_2_4     [label="Struts\nv 1.2.4"];
        tomcat_catalina_4_1_9   [label="Tomcat Catalina\nv 4.1.9"];
    
        subgraph cluster_Apache_MyFaces {
    
          label = "Apache MyFaces";

          myfaces_myfaces_1_0_9   [label="MyFaces\nv 1.0.9"];
          myfaces_myfaces_api_20050804   [label="MyFaces JSF API\nnightly build 2005-08-04"];
          myfaces_myfaces_impl_20050804   [label="MyFaces JSF Impl.\nnightly build 2005-08-04"];
          myfaces_tomahawk_20050804   [label="MyFaces Tomahawk\nnightly build 2005-08-04"];
    
        }
    
        subgraph cluster_Apache_jakarta_commons {
      
          label = "Apache Jakarta Commons";
      
          commons_logging_commons_logging_1_0_4         [label="Commons logging\nv 1.0.4"];
          commons_beanutils_commons_beanutils_1_7_0     [label="Commons beanutils\nv 1.7.0"];
          commons_collections_commons_collections_3_1   [label="Commons collections\nv 3.1"];
          commons_digester_commons_digester_1_5         [label="Commons digester\nv 1.5"];
          commons_fileupload_commons_fileupload_1_0     [label="Commons fileupload\nv 1.0"];
          commons_betwixt_commons_betwixt_0_6           [label="Commons betwixt\nv 0.6"];
          commons_digester_commons_digester_1_5         [label="Commons digester\nv 1.5"];
          commons_dbcp_commons_dbcp_1_2_1               [label="Commons dbcp\nv 1.2.1"];
          commons_lang_commons_lang_2_0                 [label="Commons lang\nv 2.0"];
          commons_codec_commons_codec_1_3               [label="Commons codec\nv 1.3"];
    
        }
      
      }
    
      subgraph cluster_JBoss {
  
        label = "JBoss";
  
        subgraph cluster_Hibernate {
      
          label = "Hibernate";

          hibernate_hibernate_2_1_6 [label="Hibernate\nv 2.1.6"];
          hibernate_hibernate_2_1_8 [label="Hibernate\nv 2.1.8"];
    
        }
    
      }
    
      displaytag_displaytag_1_0_rc1     [label="displaytag\nv 1.0 rc1"];

      subgraph cluster_Toryt {
      
        label = "Toryt";
      
        toryt_toryt_I_1_0_0_1_0           [label="Toryt I\nv 1.0.0-1.0"];
        toryt_toryt_I_1_2_0_2_0           [label="Toryt I\nv 1.2.0-2.0"];
      
      }
      
      junit_junit_3_8_1                 [label="JUnit\nv 3.8.1"];
      log4j_log4j_1_2_8                 [label="log4j\nv 1.2.8"];
      
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
<!--
      <xsl:call-template name="projectDependencies">
        <xsl:with-param name="project" select="$olts" />
      </xsl:call-template>
-->
    }
  </xsl:template>

  <xsl:template name="ppwNode">
    <xsl:param name="project" />
    <xsl:value-of select="translate($project/groupId, '-', '_')"/>_<xsl:value-of
      select="translate($project/artifactId, '-', '_')"/>_<xsl:value-of
        select="translate($project/currentVersion, '-.', '__')"/>
    [label="<xsl:value-of select="$project/name"/>\nv <xsl:value-of select="$project/currentVersion"/>"];
  </xsl:template>    
    
  <xsl:template name="projectDependencies">
    <xsl:param name="project" />
    <xsl:value-of select="translate($project/groupId, '-', '_')"/>_<xsl:value-of
      select="translate($project/artifactId, '-', '_')"/>_<xsl:value-of
        select="translate($project/currentVersion, '-.', '__')"/>
    -> {<xsl:apply-templates select="$project/dependencies" mode="arcs"/>           };
  </xsl:template>
  
  <xsl:template match="/project/dependencies/dependency" mode="nodes">
    <xsl:value-of select="translate(groupId, '-', '_')"/>_<xsl:value-of select="translate(artifactId, '-', '_')"/>_<xsl:value-of
      select="translate(version, '.-', '_')"/> [label="<xsl:apply-templates select="$libraries" mode="name">
                                                                      <xsl:with-param name="artifactId" select="artifactId" />
                                                                    </xsl:apply-templates>\nv <xsl:value-of select="version"/>"];</xsl:template>

  <xsl:template match="/project/dependencies/dependency" mode="arcs">
    <xsl:value-of select="translate(groupId, '-', '_')"/>_<xsl:value-of
      select="translate(artifactId, '-', '_')"/>_<xsl:value-of select="translate(version, '-.', '__')"/>;</xsl:template>

</xsl:stylesheet>
