<?xml version='1.0' encoding='iso-8859-1'?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" version="1.0" encoding="utf-8" indent="yes"/>

  <xsl:template match="/project">
    digraph PeopleWare_Libraries_Dependencies {

      graph [	fontname = "Helvetica-Oblique",
        fontsize = 36,
        label = "PeopleWare Libraries Dependencies, NOW",
        size = "10,50"];

      nslimit=10.0;
      mclimit=10.0;

      node [shape = rectangle, fillcolor=aquamarine2,
        fontname = "Helvetica", fontsize = 11];

      edge [];
      /* optional = blue
         only needed for the tests = chocolate */

      subgraph cluster_PPW_Libraries {
        color = aquamarine4;
        fontcolor = aquamarine4;
        label = "Peopleware Java Libraries";
        
        node [style=filled, fillcolor=aquamarine2];
        edge [style=bold];
        
        /* this node */
        <xsl:value-of select="translate(groupId, '-', '_')"/>_<xsl:value-of select="translate(artifactId, '-', '_')"/>
          [label="<xsl:value-of select="name"/>\nv <xsl:value-of select="currentVersion"/>"];
      }
    
      subgraph cluster_web_apis {
        label = "Standard Web API's";
        
        servlet_api       [label="Servlet API\nv2.4"];
        jsp_api           [label="JSP API\nv2.0"];
        jstl_api          [label="JSTL\nv1.1.2"];
        mail_api          [label="Mail API\nv1.3.2"];
      }
    
      subgraph cluster_Apache_jakarta {
        label = "Apache Jakarta";
      
        jstl_apache       [label="standard\n(Apache JSTL Impl.)"];
        struts            [label="Struts"];
        
        subgraph cluster_commons {
          label = "Apache Jakarta Commons";
          
          commons_logging_commons_logging   [label="Commons logging"];
          commons_beanutils_commons_beanutils [label="Commons beanutils"];
        }
      
      }
    
    <!--     
      /* nodes */
<xsl:apply-templates select="dependencies" mode="nodes"/> -->
    
      /* dependencies */
      <xsl:value-of select="translate(groupId, '-', '_')"/>_<xsl:value-of select="translate(artifactId, '-', '_')"/>
        -> {<xsl:apply-templates select="dependencies" mode="arcs"/>           };

    }
  </xsl:template>


  <xsl:template match="/project/dependencies/dependency" mode="nodes">
    <xsl:value-of select="translate(groupId, '-', '_')"/>_<xsl:value-of select="translate(artifactId, '-', '_')"/>_<xsl:value-of
      select="translate(version, '.-', '_')"/> [label="<xsl:apply-templates select="$libraries" mode="name">
                                                                      <xsl:with-param name="artifactId" select="artifactId" />
                                                                    </xsl:apply-templates>\nv <xsl:value-of select="version"/>"];</xsl:template>

  <xsl:key name="libraryLookup" match="library" use="artifactId" />

  <xsl:variable name="libraries" select="document('libraries.xml')/libraries" />

  <xsl:template match="libraries" mode="name">
    <xsl:param name="artifactId" />
    <xsl:value-of select="key('libraryLookup', $artifactId)/name"/>
  </xsl:template>
  
  <xsl:template match="/project/dependencies/dependency" mode="arcs">
    <xsl:value-of select="translate(groupId, '-', '_')"/>_<xsl:value-of select="translate(artifactId, '-', '_')"/>;</xsl:template>
  
  
  <!-- main layout
  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ++>

  <xsl:template match="/project">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>About this application</title>
        <link rel="stylesheet" href="../css/list.css" type="text/css"/>
        <link rel="stylesheet" href="../css/about.css" type="text/css"/>
      </head>
      <body>
        <div class="header">
          <div class="status">
            <span class="item"><a href="../">Back to the application</a></span>
          </div>
        </div>
        <xsl:apply-templates select="name"/>
         <xsl:apply-templates select="currentVersion"/>
         <xsl:apply-templates select="inceptionYear"/>
         <xsl:apply-templates select="shortDescription"/>
         <xsl:apply-templates select="description"/>
         <xsl:apply-templates select="developers"/>
        <xsl:apply-templates select="dependencies"/>
        <div class="attribution">© <a href="http://www.fvb.be/">FVB/FFC</a> - <a
            href="http://www.peopleware.be/">PeopleWare</a>, 2005</div>
      </body>
    </html>
  </xsl:template>



  <!++ title: project name
  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ++>

  <xsl:template match="/project/name">
    <p class="name"><xsl:value-of select="."/></p>
  </xsl:template>



  <!++ subtitle: project version and copyright
  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ++>

  <xsl:template match="/project/currentVersion">
    <p class="version"> Version: <xsl:value-of select="."/></p>
  </xsl:template>

  <xsl:template match="/project/inceptionYear">
    <p class="copyright">
      <xsl:text>© PeopleWare, FVB/FFC </xsl:text>
      <xsl:value-of select="."/>
      <xsl:text> - 2005</xsl:text>
    </p>
  </xsl:template>



  <!++ project description
  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ++>

  <xsl:template match="/project/shortDescription">
    <p class="shortDescription"><xsl:value-of select="."/></p>
  </xsl:template>

  <xsl:template match="/project/description">
    <p class="description"><xsl:value-of select="."/></p>
  </xsl:template>



  <!++ project dependencies
  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ++>
  <!++ table with link to site and license for each dependency ++>

  <xsl:template match="/project/dependencies">
    <div class="dependencies">
      <p>This application is build using the following Open Source technology:</p>
      <table>
        <tr>
          <th>Technology</th>
          <th>Version</th>
          <th>License</th>
          <th>Library</th>
        </tr>
        <xsl:for-each select="dependency">
          <xsl:sort select="artifactId" />
          <tr>
            <xsl:attribute name="class">
              <xsl:choose>
                <xsl:when test="position() mod 2 = 1">
                  <xsl:value-of select="'odd'" />
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="'even'" />
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <xsl:apply-templates select="." />
          </tr>
        </xsl:for-each>
      </table>
    </div>
  </xsl:template>

  <!++ dependency ++>
  <xsl:template match="/project/dependencies/dependency">
      <td class="technologyName">
        <xsl:apply-templates select="$libraries" mode="name">
          <xsl:with-param name="artifactId" select="artifactId" />
        </xsl:apply-templates>
      </td>
      <td class="technologyVersion">
        <xsl:value-of select="version"/>
      </td>
      <td class="technologyLicense">
        <xsl:apply-templates select="$libraries" mode="license">
          <xsl:with-param name="artifactId" select="artifactId" />
        </xsl:apply-templates>
      </td>
      <td class="library">
        <xsl:value-of select="artifactId"/>
        <xsl:text>-</xsl:text>
        <xsl:value-of select="version"/>
        <xsl:text>.jar</xsl:text>
      </td>
  </xsl:template>

  <xsl:key name="libraryLookup" match="library" use="artifactId" />

  <xsl:variable name="libraries" select="document('META-INF/libraries.xml')/libraries" />

  <xsl:template match="libraries" mode="name">
    <xsl:param name="artifactId" />
    <a>
      <xsl:attribute name="href">
        <xsl:value-of select="key('libraryLookup', $artifactId)/url"/>
      </xsl:attribute>
      <xsl:if test="key('libraryLookup', $artifactId)/logoUrl">
         <img>
          <xsl:attribute name="src">
            <xsl:value-of select="key('libraryLookup', $artifactId)/logoUrl"/>
          </xsl:attribute>
        </img>
      </xsl:if>
      <br />
      <xsl:value-of select="key('libraryLookup', $artifactId)/name"/>
    </a>
  </xsl:template>

  <xsl:template match="libraries" mode="license">
    <xsl:param name="artifactId" />
    <xsl:apply-templates select="$licenses">
      <xsl:with-param name="licenseRef" select="key('libraryLookup', $artifactId)/license" />
    </xsl:apply-templates>
  </xsl:template>

  <xsl:key name="licenseLookup" match="license" use="ref" />

  <xsl:variable name="licenses" select="document('META-INF/licenses.xml')/licenses" />

  <xsl:template match="licenses">
    <xsl:param name="licenseRef" />
    <a>
      <xsl:attribute name="href">
        <xsl:value-of select="key('licenseLookup', $licenseRef)/url"/>
      </xsl:attribute>
      <xsl:value-of select="key('licenseLookup', $licenseRef)/name"/>
    </a>
  </xsl:template>



  <!++ developers
  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ++>
  <xsl:template match="/project/developers">
    <div class="developers">
      <p>This application was developed by the following people:</p>
      <table>
        <tr>
          <th>Name</th>
          <th>Organization</th>
          <th>Roles</th>
        </tr>
        <xsl:for-each select="developer">
          <tr>
            <xsl:attribute name="class">
              <xsl:choose>
                <xsl:when test="position() mod 2 = 1">
                  <xsl:value-of select="'odd'" />
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="'even'" />
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <xsl:apply-templates select="." />
          </tr>
        </xsl:for-each>
      </table>
    </div>
  </xsl:template>

  <xsl:template match="/project/developers/developer">
      <td class="developerName">
        <a>
          <xsl:attribute name="href">
            <xsl:text>mailto:</xsl:text>
            <xsl:value-of select="email"/>
          </xsl:attribute>
          <xsl:value-of select="name"/>
        </a>
      </td>
      <td class="developerOrganization">
        <xsl:value-of select="organization"/>
      </td>
      <td class="developerRoles" colspan="2">
          <xsl:apply-templates select="roles" />
      </td>
  </xsl:template>

  <xsl:template match="/project/developers/developer/roles">
      <xsl:for-each select="role">
        <span class="role">
          <xsl:value-of select="."/>
        </span>
      </xsl:for-each>
  </xsl:template>

  -->

</xsl:stylesheet>
