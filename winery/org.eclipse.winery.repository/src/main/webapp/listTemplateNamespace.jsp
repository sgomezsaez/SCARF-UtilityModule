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
// The 'listTemplateNamespace.jsp' file provides all service template namespace lists.

String repositoryPath=request.getParameter("repositoryPath");
String path = repositoryPath+"/servicetemplates/";
File file1 = new File(path);
String[] allNamespace = file1.list(new FilenameFilter() {
  @Override
  public boolean accept(File current, String name) {
    return new File(current, name).isDirectory();
  }
});
for(int i=0;i<allNamespace.length;i++){
	out.print(allNamespace[i]);	
	out.print(",End Here,");
}
%>