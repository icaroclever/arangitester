<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output method="html"/>
    <xsl:template match="/">
    <html>
       <head>
        <script type="text/javascript">
            function openObs(obs){
                janela=window.open(NULL,'popup');
                janela.write(
                );
            }
        </script>
    	<!-- JavaScript Function used to open a pop-up -->
    	<script type="text/javascript">
            function popup(path){
                janela=window.open(path,'popup')
                <!-- width 450 height 400  -->	
            }
        </script>
	
        <!-- JavaScript Function used to open a display screen to detail the test case -->
        <script type="text/javascript">
            function toggleDisplay(elementId) {
                var elm = document.getElementById(elementId + 'error');
                if (elm &amp;&amp; typeof elm.style != "undefined") {
                    if (elm.style.display == "none") {
                        elm.style.display = "";
                        document.getElementById(elementId + 'off').style.display = "none";
                        document.getElementById(elementId + 'on').style.display = "inline";
                    }
                    else if (elm.style.display == "") {
                        elm.style.display = "none";
                        document.getElementById(elementId + 'off').style.display = "inline";
                        document.getElementById(elementId + 'on').style.display = "none";
                    }
                }
            }
        </script>
        <script type="text/javascript">
            function toggleVisibility(idElement) {
                document.getElementById(idElement).style.display = "";
                if(document.getElementById(idElement).style.visibility == "hidden" ) {
                    document.getElementById(idElement).style.visibility = "visible";
                }
                else {
                    document.getElementById(idElement).style.visibility = "hidden";
                }
            }

            function toggleDisplay(idElement) {
                document.getElementById(idElement).style.visibility = "visible";
                if(document.getElementById(idElement).style.display == "none" ) {
                    document.getElementById(idElement).style.display = "";
                }
                else {
                    document.getElementById(idElement).style.display = "none";
                } 
            }
      
            function show(id){
                var e = document.getElementById(id);
                var t = document.getElementById("name_"+id)
                if(e.style.display == 'block'){
                    e.style.display = 'none';
                    t.style.fontWeight = 'normal';
                }
                else
                {
                    e.style.display = 'block';
                    t.style.fontWeight = 'bold';
                }
            }
         </script>
	
       </head>
       <body style="margin-left:50px;   font-family: Arial; font-size: medium;">    
        <div id="general_info">
            <h3>General Information</h3>
            Tempo total:                     <xsl:value-of select="LccFunctionalSuite/@totalTime"/>min<br/>
            Quantidade de Testes:            <xsl:value-of select="LccFunctionalSuite/@total"/><br/>
            Testes com Sucesso:              <xsl:value-of select="LccFunctionalSuite/@successful"/><br/>
            Testes Falhos:                   <xsl:value-of select="LccFunctionalSuite/@fail"/><br/>
            Testes Nao Executados (Skipped): <xsl:value-of select="LccFunctionalSuite/@skip"/><br/>
            Percentual de Testes com Sucesso:<xsl:value-of select="LccFunctionalSuite/@percent"/>%<br/>
        </div>

        <div id="use_cases">
            <h3>Use Cases</h3>
            <ol> 
                <xsl:for-each select="LccFunctionalSuite/UseCase">
                    <xsl:variable name="nomeusecase" select="@name"/>
                    <xsl:variable name="totalerrors" select="count(.//error)"/>
                    <xsl:variable name="obs" select="count(./Obs)"/>
                    <xsl:variable name="annotation.list" select="./Obs"/>
                    <xsl:variable name="ann" select="count($annotation.list)"/>

                    <li>
                        <xsl:if test="$totalerrors > 0">
                            <a style="color:red;" href='javascript: show("{$nomeusecase}")'>
                                <span id="name_{$nomeusecase}"><xsl:value-of select="$nomeusecase" /></span>
                            </a>
			            </xsl:if>
                        <xsl:if test="$totalerrors = 0">
                            <a style="color:green;" href='javascript: show("{$nomeusecase}")'>
                                <span id="name_{$nomeusecase}"><xsl:value-of select="$nomeusecase" /></span>
                            </a>
			            </xsl:if>
			            <xsl:if test="$obs > 0">
                            <a title="Click here to see the {$nomeusecase}'s observations" href='javascript: show("{$nomeusecase}_obs")'><img hspace="10" height="15" src="/plugin/arangitesterhp/icons/balao_48x48.png"/></a>
                        </xsl:if>
                    </li>
                    <xsl:if test="$obs > 0">
                        <div id="{$nomeusecase}_obs" style=" font-size:xxsmall;border: 0.5px solid #000000;height: 100px;width: 700px;overflow: auto;background-color:#ffffff;padding:5px;display:none;">
                            <ol>
                                <xsl:for-each select="$annotation.list">
                                    <li><xsl:value-of select="current()"/></li>
                                </xsl:for-each>
                            </ol>
                        </div>
                    </xsl:if>
                    <div id="{$nomeusecase}" style="display:none">
                        <blockquote>
                            <xsl:for-each select="info">
                                <xsl:value-of select="." /><br/>
                            </xsl:for-each>       
                        </blockquote>
                        <xsl:for-each select="error">
                            <blockquote>
                                <span class="error">Cause: <xsl:value-of select="@cause"/></span>
                                <xsl:if test="@screenshot">
                                     <xsl:variable name="time" select="/info/property[@name='cctimestamp']/@value"/>
                                     <xsl:variable name="project" select="/info/property[@name='projectname']/@value"/>
                                     <xsl:variable name="fotopath" select="@screenshot"/><a title="Click here to open the screenshot" href='javascript: popup("http://argonio.lcc.ufmg.br/{$project}/{$time}/{$fotopath}")'><img hspace="10" height="30" src="../images/snapshot.png"/></a> 
                                </xsl:if>
                                <xsl:if test="text()">
                                    <br/>
                                    <span class="error">Stack Trace: <xsl:value-of select="."/></span>
                                </xsl:if>
                                <hr/>
                            </blockquote>
                        </xsl:for-each>
 
                        <xsl:for-each select="TestCase">
                            <xsl:variable name="testcases" select="@name"/>
                            <xsl:variable name="skipped" select="@skip"/>
                            <xsl:variable name="descricao" select="@description" />
                            <xsl:if test="not(error)">
                                <a href='javascript: show("{$testcases}_{$nomeusecase}")'>
                                    <span id="name_{$testcases}_{$nomeusecase}" style="color:green;margin-left:40px;"><xsl:value-of select="$descricao"/></span>
                                </a><br/>
                            </xsl:if>
                            <xsl:if test="error">
                                <a href='javascript: show("{$testcases}_{$nomeusecase}")'>
                                    <span id="name_{$testcases}_{$nomeusecase}" style="color:red;margin-left:40px;"><xsl:value-of select="$descricao"/></span>
                                </a><br/>
                            </xsl:if>
                            <div id="{$testcases}_{$nomeusecase}" style="display:none">
                                <pre style="margin-left:50px;  font-size:small;">
                                    <u>Method</u>: <xsl:value-of select="$testcases" />
                                </pre>
                                <pre  style="margin-left:50px;  font-size:small;">
                                    <u>Start</u>: <xsl:value-of select="@startTime"/><br/>
                                    <u>End</u>: <xsl:value-of select="@endTime" />
                                </pre>
                                <xsl:if test="$skipped = 'true'">
                                    <pre><u><b>SKIPED</b></u></pre>
                                </xsl:if>
                                <blockquote>
                                    <ol>
                                        <xsl:for-each select="info">
                                            <li><xsl:value-of select="." /></li>
                                        </xsl:for-each>
                                    </ol>
                                </blockquote>
                                <xsl:for-each select="error">
                                    <blockquote>
                                        <span class="error">Cause: <xsl:value-of select="@cause"/></span>
                                        <xsl:if test="@screenshot">
                                            <xsl:variable name="time" select="/info/property[@name='cctimestamp']/@value"/>
                                            <xsl:variable name="project" select="/info/property[@name='projectname']/@value"/>
                                            <xsl:variable name="fotopath" select="@screenshot"/>
                                            <a title="Click here to open the screenshot" href='javascript: popup("%%SCREENSHOT%%/{$fotopath}")'>
                                                <img hspace="10" height="30" src="/plugin/arangitesterhp/icons/lupa_24x24.png"/>
                                            </a> 
                                 
                                            <a title="Click here to open the screenshot" href='javascript: popup("%%SCREENSHOT%%/{$fotopath}.html")'>
                                                <img hspace="10" height="30" src="/plugin/arangitesterhp/icons/lupa_24x24_html.png"/>
                                            </a>
                                        </xsl:if>
                                        <xsl:if test="text()">
                                            <br/>
                                            <span class="error">Stack Trace: <xsl:value-of select="." /></span>
                                        </xsl:if>
                                    <hr/>
                                    </blockquote>
                                </xsl:for-each>
                            </div>
                        </xsl:for-each>
                    </div>
                </xsl:for-each>
            </ol>
         </div> 
        </body>
    </html>
    </xsl:template>
</xsl:stylesheet>

