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

<%
//The 'policyTypeRead.jsp' file reads and provides the properties definition name of the selected performance policy type.

	String typeName=request.getParameter("typeName");
	String repositoryPath=request.getParameter("repositoryPath");
	String policyTypeNamespace=request.getParameter("policyTypeNamespace");
	String textFileDirectory = repositoryPath+"/policytypes/"+policyTypeNamespace+"/";
	String txtFilePath = textFileDirectory+typeName+"/PolicyType.tosca";
	BufferedReader reader = new BufferedReader(new FileReader(txtFilePath));
    StringBuilder sb = new StringBuilder();
    String line;

    while((line = reader.readLine())!= null){
        sb.append(line);
    }
    out.println(sb.toString()); 

    reader.close();
%>