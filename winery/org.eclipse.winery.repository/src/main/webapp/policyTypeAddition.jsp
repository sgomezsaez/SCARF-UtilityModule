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
//The 'policyTypeAddition.jsp' file creates a new performance policy type tosca file to location: repositoryPath+"\\policytypes\\"+policyTypeNamespace+"\\"+name(policy type name)+"\\PolicyType.tosca" and adds policy type information to it.	

String name=request.getParameter("name");
String propDef=request.getParameter("propDef");
String repositoryPath=request.getParameter("repositoryPath");
String policyTypeNamespace=request.getParameter("policyTypeNamespace");
String xmlAsString="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><tosca:Definitions xmlns:tosca=\"http://docs.oasis-open.org/tosca/ns/2011/12\" xmlns:winery=\"http://www.opentosca.org/winery/extensions/tosca/2013/02/12\" xmlns:ns2=\"http://www.eclipse.org/winery/model/selfservice\" id=\"winery-defs-for_ns6-"+name+"\" targetNamespace=\"http://example.com/PolicyTypes\"><tosca:PolicyType name=\""+name+"\" abstract=\"no\" final=\"no\" targetNamespace=\"http://example.com/PolicyTypes\"><tosca:PropertiesDefinition element=\"winery:"+propDef+"\"/></tosca:PolicyType></tosca:Definitions>";
String textFileDirectory = repositoryPath+"/policytypes/"+policyTypeNamespace+"/";

new File(textFileDirectory+name).mkdir();
String nameOfTextFile = textFileDirectory+name+"/PolicyType.tosca";

try {   
	
    PrintWriter pw = new PrintWriter(new FileOutputStream(nameOfTextFile));
    pw.println(xmlAsString);
    //clean up
    pw.close();
} catch(IOException e) {
   out.println(e.getMessage());
}

String redirectURL="/winery/policytypes/";
response.sendRedirect(redirectURL);

%>
</body>
</html>