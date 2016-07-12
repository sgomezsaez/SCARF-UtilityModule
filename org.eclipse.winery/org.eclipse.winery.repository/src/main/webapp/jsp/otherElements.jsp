<%--
/*******************************************************************************
 * Copyright (c) 2015 University of Stuttgart.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and the Apache License 2.0 which both accompany this distribution,
 * and are available at http://www.eclipse.org/legal/epl-v10.html
 * and http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *    Oliver Kopp - initial API and implementation and/or initial documentation
 *******************************************************************************/
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:genericpage windowtitle="Other Elements" selected="OtherElements"
	cssClass="otherelements">

	<p>
		The following items list TOSCA elements contained in TOSCA's
		<code>Definitions</code>
		element, which are not listed as separate tabs.
	</p>

	<h4>Artifacts</h4>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/artifacttypes/">Artifact
		Types</a>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/artifacttemplates/">Artifact
		Templates</a>

	<h4>Requirements and Capabilities</h4>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/requirementtypes/">Requirement
		Types</a>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/capabilitytypes/">Capability
		Types</a>

	<h4>Implementations</h4>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/nodetypeimplementations/">Node
		Type Implementations</a>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/relationshiptypeimplementations/">Relationship
		Type Implementations</a>

	<h4>Policies</h4>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/policytypes/">Policy
		Types</a>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/policytemplates/">Policy
		Templates</a>

	<h4>Imports</h4>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/imports/http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema">XML
		Schema Definitions</a>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/imports/http%253A%252F%252Fschemas.xmlsoap.org%252Fwsdl%252F">WSDLs</a>

	<h4>Workloads</h4>
	<button class="btn btn-default" id="workloadList"
		onclick="workloadList();">Workload Definitions</button>

	<div class="modal fade" id="ListWorkload">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">List Workloads</h4>
				</div>
				<div class="modal-body">

					<div class="form-group">

						<table class="table" id="ListWorkloadTable">
							<thead>
								<tr>
									<th>Service Template Name:</th>
									<th>View Workload</th>
								</tr>
							</thead>
							<tbody>

							</tbody>
						</table>

					</div>

				</div>
			</div>
		</div>
	</div>

	<script>
	var repositoryPath="";

	//The 'readPropertiesFile.jsp' file provides the value of repository path variable from the properties file.
	$.get("/winery/readPropertiesFile.jsp", function(data, status){
		var resArray=data.split(",End Here,");
		repositoryPath=resArray[0];
		});

	//The 'listWorkloads.jsp' file provides all the workload specification list. Each service template contains only a single workload specification XML file. Multiple workload samples are added to the single workload specification XML file.
	function workloadList(){
		$.get("/winery/listWorkloads.jsp?repositoryPath="+repositoryPath, function(data, status){
			var resArray=data.split(",End Here,");
			$("#ListWorkloadTable > tbody").html("");
	       eval(resArray).forEach(function(x) {
	    	   //The table containing all the workload specification list.
	    	   var workloadTable= "";
	    	   if(x!=""){
	   			workloadTable= "<tr id=\"trw"+ $.trim(x) + "\"><td>"+ $.trim(x) + " </td><td><button type=\"button\"  id=\"rw"+ $.trim(x) + "\" class=\"btn btn-primary\" onclick=\"displayWorkload(this);\">View</button></td></tr>";
	   	    	$("#ListWorkloadTable").append(workloadTable);
	    	   }
	        });
	       var diag = $("#ListWorkload");
			require(["winery-support-common"], function(w) {
				diag.modal("show");
			});
	});


	}

	//The 'displayWorkload.jsp' file displays the workload specification of the selected service template in xml format.
	function displayWorkload(el){
		var serviceTemplate=$(el).attr('id');
		serviceTemplate=serviceTemplate.substring(2);
		var sendparams = "/winery/displayWorkload.jsp?serviceTemplate="+serviceTemplate+"&repositoryPath="+repositoryPath;
		window.location.replace(sendparams);
	}
	</script>

</t:genericpage>
