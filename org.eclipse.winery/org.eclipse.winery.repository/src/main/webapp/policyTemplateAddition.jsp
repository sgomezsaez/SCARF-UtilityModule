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

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Performance Policy Type Addition</title>
</head>
<body>
	<%@ page import="java.io.*"%>
	<%@ page import="java.util.List"%>
	<%@ page import="org.apache.commons.io.FileUtils"%>

	<%
//The 'policyTemplateAddition.jsp' file creates the new performance policy template tosca file to the location: repositoryPath+"\\policytemplates\\"+policyTemplateNamespace+"\\"+name(policy template name)+"\\PolicyTemplate.tosca" 
// and writes the performance specification information to 'PolicyTemplate.tosca' file. 

String name=request.getParameter("name");
String perfPolicytype=request.getParameter("perfPolicytype");
String propDef=request.getParameter("propDef");
String metricsAsString=request.getParameter("metricsAsString");
String repositoryPath=request.getParameter("repositoryPath");
String policyTemplateNamespace=request.getParameter("policyTemplateNamespace");
String xmlAsString="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><tosca:Definitions id=\"winery-defs-for_expte-"+name+"\" targetNamespace=\"http://www.example.org/PolicyTemplates\" xmlns:tosca=\"http://docs.oasis-open.org/tosca/ns/2011/12\" xmlns:winery=\"http://www.opentosca.org/winery/extensions/tosca/2013/02/12\" xmlns:ns2=\"http://www.eclipse.org/winery/model/selfservice\"><tosca:PolicyTemplate name=\""+name+"\" id=\""+name+"\" type=\"ns6:"+perfPolicytype+"\" xmlns:ns6=\"http://example.com/PolicyTypes\"><tosca:Properties><winery:"+propDef+" xmlns:ns1=\"http://www.eclipse.org/winery/model/selfservice\">"+metricsAsString+"</winery:"+propDef+"></tosca:Properties></tosca:PolicyTemplate></tosca:Definitions>";
String textFileDirectory = repositoryPath+"/policytemplates/"+policyTemplateNamespace+"/";

new File(textFileDirectory+name).mkdir();
String nameOfTextFile = textFileDirectory+name+"/PolicyTemplate.tosca";

try {   
	
    PrintWriter pw = new PrintWriter(new FileOutputStream(nameOfTextFile));
    pw.println(xmlAsString);
    pw.close();
} catch(IOException e) {
   out.println(e.getMessage());
}

String redirectURL="/winery/policytemplates/";
response.sendRedirect(redirectURL);

%>
</body>
</html>