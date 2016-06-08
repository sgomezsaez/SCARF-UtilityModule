<%--
/*******************************************************************************
* Copyright (c) 2015 University of Stuttgart.
* Licensed under the Apache License, Version 2.0.
* You may not use this file except in compliance with the License.
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

* Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and limitations under the License.
 *******************************************************************************/
--%>

<%@ page import="java.io.*"%>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.commons.io.FileUtils"%>

<%
//The 'workloadAddition.jsp' file adds workload samples to the xml file [name(service template name)+"Workload.xml"] located: repositoryPath+"\\workload\\"+name(service template name).

String name=request.getParameter("name");
String samplesArray=request.getParameter("samplesArray");
String repositoryPath=request.getParameter("repositoryPath");
String textFileDirectory = repositoryPath+"/workload/"+name;

new File(textFileDirectory).mkdirs();
String nameOfTextFile = textFileDirectory+"/"+name+"Workload.xml";

try {   
	
    PrintWriter pw = new PrintWriter(new FileOutputStream(nameOfTextFile));
    pw.println(samplesArray);
    //clean up
    pw.close();
} catch(IOException e) {
   out.println(e.getMessage());
}

String redirectURL="/winery/servicetemplates/";
response.sendRedirect(redirectURL);

%>
