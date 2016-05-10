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
//The 'listServiceTemplates.jsp' file provides the list of all the services templates of a corresponding service template namespace.

String selectedNamespace=request.getParameter("selectedNamespace");
String repositoryPath=request.getParameter("repositoryPath");
String path = repositoryPath+"/servicetemplates/"+selectedNamespace;
File file = new File(path);
String[] serviceNames = file.list(new FilenameFilter() {
  @Override
  public boolean accept(File current, String name) {
    return new File(current, name).isDirectory();
  }
});
for(int i=0;i<serviceNames.length;i++){
	out.print(serviceNames[i]);	
	out.print(",End Here,");
}
%>