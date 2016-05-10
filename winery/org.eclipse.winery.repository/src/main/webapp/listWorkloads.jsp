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
//The 'listWorkloads.jsp' file provides all the workload specification list. Each service template contains only a single workload specification XML file. Multiple workload samples are added to the single workload specification XML file.

String repositoryPath=request.getParameter("repositoryPath");
String path = repositoryPath+"/workload/";
File file = new File(path);
String[] workloads = file.list(new FilenameFilter() {
  @Override
  public boolean accept(File current, String name) {
    return new File(current, name).isDirectory();
  }
});
for(int i=0;i<workloads.length;i++){
	out.print(workloads[i]);	
	out.print(",End Here,");
}
%>