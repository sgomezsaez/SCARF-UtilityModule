1. The source code is located under the directory: "org.eclipse.winery". 

2. The performance specification interface of the 'TopologyModeler' component is developed by modifying the 'policydiag.tag' file
under the directory: '\org.eclipse.winery.topologymodeler\src\main\webapp\WEBINF\tags\common\policies'.

3. The workload specification interface is developed by modifying the 'genericcomponentpage.jsp' and 'otherElements.jsp' files under the
directory: '\org.eclipse.winery.repository\src\main\webapp\jsp'.

4. Several jsp files have been created during implementing the topology enrichment such as csvFileWrite.jsp, csvTable.jsp, displayWorkload.jsp,
listServiceTemplates.jsp, listTemplateNamespace.jsp, listWorkloads.jsp, policyTemplateAddition.jsp, policyTemplateRead.jsp, policyTypeAddition.jsp, 
policyTypeRead.jsp, readPropertiesFile.jsp and workloadAddition.jsp located under the directory: '\org.eclipse.winery.repository\src\main\webapp'.

5. The 'winery.properties' file contains user-defined values of the repository path, performance policy type namespace and performance policy template namespace variables.
   The property file is located in the source code under the following path: "\org.eclipse.winery\org.eclipse.winery.repository\src\main\resources".

	The performance policy type namespace 'http%3A%2F%2Fexample.com%2FPolicyTypes' is common for all performance policy types.
	Similarly the performance policy template namespace 'http%3A%2F%2Fwww.example.org%2FPolicyTemplates' is common for all performance policy templates.
	They can be manually changed from the 'winery.properties' file. The repository path can also be changed from the properties file.

	Each service template contains only a single workload specification XML file. Multiple workload samples are added to the single workload specification file.

6. The detailed steps of installing, deploying and running the source code are mentioned in the 'README' file under the directory: "org.eclipse.winery".






