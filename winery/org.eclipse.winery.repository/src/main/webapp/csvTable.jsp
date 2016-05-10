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
<title>Display CSV File in Tabular Form</title>
</head>
<body>
<%@ page import="java.io.*"%>

<table width="300" border="2">
<%
//The 'csvTable.jsp' file displays the uploaded csv file in tabular format.

String repositoryPath=request.getParameter("repositoryPath");
String serviceTemplate=request.getParameter("serviceTemplate");
String sampleName=request.getParameter("sampleName");
String behavModelName=request.getParameter("behavModelName");
String textFileDirectory = repositoryPath+"/csv/"+serviceTemplate+"/"+sampleName+"/";

 String fName = textFileDirectory+behavModelName+".csv";
 String thisLine; 
 int count=0; 
 try{
 FileInputStream fis = new FileInputStream(fName);
 DataInputStream myInput = new DataInputStream(fis);
 int i=0; 

while ((thisLine = myInput.readLine()) != null)
{
String strar[] = thisLine.split(",");
%><tr>
<%
for(int j=0;j<strar.length;j++)
 {
if(i!=0) 
 {
out.print("<td> " +strar[j]+ "</td> ");
 }
else
{
out.print(" <td> " +strar[j]+ " </td> ");
}
i++;
} 
%>
		</tr>
<%
}

 } catch(IOException e) {
	   out.println(e.getMessage());
	}
%>
	</table>
</body>
</html>

