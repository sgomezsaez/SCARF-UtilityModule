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
//The 'csvFileWrite.jsp' file uploads a csv file (behavModelName+".csv") to the location: repositoryPath+"\\csv\\"+serviceTemplate+"\\"+sampleName.

String tmpArray=request.getParameter("tmpArray");
String repositoryPath=request.getParameter("repositoryPath");
String serviceTemplate=request.getParameter("serviceTemplate");
String sampleName=request.getParameter("sampleName");
String behavModelName=request.getParameter("behavModelName");
String[] arr=tmpArray.split(",End Here,");
String textFileDirectory = repositoryPath+"/csv/"+serviceTemplate+"/"+sampleName;
new File(textFileDirectory).mkdirs();
String nameOfTextFile = textFileDirectory+"/"+behavModelName+".csv";

try {   
	
    PrintWriter pw = new PrintWriter(new FileOutputStream(nameOfTextFile));
 
    for(int i=0;i<arr.length;i++){
    	if(arr[i].equals(",End Here")){
       	 arr[i]="";
        }	
    	else if(arr[i].indexOf(",End Here") > 0){
    	 arr[i]=arr[i].replace(",End Here", "");
    	 pw.print(arr[i]);
    	}
    	else{
    		pw.println(arr[i]);
    	}
    	
    }

out.println("The csv file has been successfully uploaded");
    pw.close();
} catch(IOException e) {
   out.println(e.getMessage());
}

%>
