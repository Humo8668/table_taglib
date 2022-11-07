<%@ page import="uz.app.jsp.Table.Tags.*" %>
<%@ page import="java.util.Iterator, java.util.LinkedList, java.util.List, java.util.Map" %>
<%@ page import="java.io.IOException" %>
<%
Table table = (Table)request.getAttribute("table");
String tableName = table.getName();
Integer overallRowsCount = table.getOverallRowsCount();
Integer activePage = table.getActivePageNumber(); //Integer.valueOf(request.getAttribute("active_page").toString());
Integer rowsInPage = table.getRowsNumberPerPage();//Integer.valueOf(request.getAttribute("rows_in_page").toString());
Integer pagesCount = overallRowsCount / rowsInPage + ((overallRowsCount % rowsInPage != 0)?1:0);//Integer.valueOf(request.getAttribute("pages_count").toString());
String JS_tableName = tableName + "_table";
%>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.1.min.js"></script>
<script type="text/javascript" src="<%=pageContext.getRequest().getServletContext().getContextPath()%>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=pageContext.getRequest().getServletContext().getContextPath()%>/js/table.js"></script>
<link rel="stylesheet" type="text/css" href="<%=pageContext.getRequest().getServletContext().getContextPath()%>/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="<%=pageContext.getRequest().getServletContext().getContextPath()%>/css/fa/css/all.css" />
<link rel="stylesheet" type="text/css" href="<%=pageContext.getRequest().getServletContext().getContextPath()%>/css/table.css" />

<script type="text/javascript">
	var <%=JS_tableName%> = new Table("<%=tableName%>");
</script>

<form name="table_<%=tableName%>_form" id="table_<%=tableName%>_form">
	<div class="btn-toolbar justify-content-center" role="toolbar" aria-label="Toolbar with button groups">
		<div class="btn-group mr-2" role="group">
		<%
		for(int i = 1; i <= pagesCount; i++) {
			if(i == activePage) {%>
				<button type="button" class="btn btn-secondary active" onclick="<%=JS_tableName%>.onPageChanged(<%=i%>)"><%=i%></button>
			<%} else { %>
				<button type="button" class="btn btn-secondary" onclick="<%=JS_tableName%>.onPageChanged(<%=i%>)"><%=i%></button>
			<%}
		}
		%>
		</div>
	</div>

	<table>
		<tr>
			<td>
				<div class="input-group">
					<div class="input-group-prepend">
						<span class="input-group-text" id="rows-in-page-label"><i class="fa fa-solid fa-list-ol"></i></span>
					</div>
					<input class="form-control" 
						name="rows_in_page"
						type="number"
						placeholder="Number of rows in page" 
						aria-describedby="rows-in-page-label" 
						label="Number of rows in page" 
						value="<%=rowsInPage%>"
						style="text-align: right;">
					<input type="hidden" name="active_page_number" value="<%=activePage%>">
				</div>
			</td>
			<td>
				<button id="btnRefresh" class="btn btn-dark" onclick="<%=JS_tableName%>.onRefresh();">
					<i class="fa fa-arrows-rotate"></i>&nbsp;<span>Refresh</span>
				</button>
			</td>
			<td>
				<button id="btnFilter" class="btn btn-dark" type="button" data-toggle="modal" data-target="#<%=tableName%>_filterModal">
					<i class="fa fa-filter"></i>&nbsp;<span>Filter</span>
				</button>
			</td>
		</tr>
	</table>

	<input type="hidden" name="<%=tableName%>" value="<%=activePage%>;<%=rowsInPage%>"> <!-- Table arguments carrier -->
	<%
	List<Column> showableColumns = table.getShowableColumns(); // columns list sorted by order value
	%>
	<table class="table table-striped table-bordered table-hover table-sm" id="<%=tableName%>">
		<caption>Overall: <%=overallRowsCount%> rows</caption>
		<thead class="thead-dark">
		<tr>
			<%if(table.hasNumeration()) {%>
				<th scope="col" name="#" type="number">#</th>
			<%}%>
			<%for (Column column: showableColumns) {%>
				<th scope="col" name="<%=column.getName()%>" type="<%=column.getType()%>" onclick="<%=JS_tableName%>.changeOrdering('<%=column.getName()%>')">
					<%=column.getLabel()%> &nbsp;
				</th>
			<%}%>
		</tr>
		</thead>
		<tbody>
		<%
		int rowNumeration = 1;
		Iterable<Map<String, Object>> rowsData = table.getRows();
        Iterator<Map<String, Object>> rowIterator = rowsData.iterator();
		if(!rowIterator.hasNext()) { %>
            <tr> <td align="center" colspan='100%'>(No data)</td></tr>
        <%} else { 
            Map<String,Object> rowObject;
            while(rowIterator.hasNext()) {
                rowObject = rowIterator.next();%>
				<tr onclick="<%=JS_tableName%>.onRowSelected(this)">
				<% if(table.hasNumeration()) {%> 
					<td><%=rowNumeration%></th>
				<%}%>

				<%for (Column column: showableColumns) {
                    String formattedValue = column.formatValue(rowObject.getOrDefault(column.getName(), "").toString());%>
					<td name="<%=column.getName()%>"><%=formattedValue%></td>
				<%}%>
				</tr>

				<% rowNumeration++; %>
            <%}
		}%>
		</tbody>
	</table>


	<div class="btn-toolbar justify-content-center" role="toolbar" aria-label="Toolbar with button groups">
		<div class="btn-group mr-2" role="group">
		<%
		for(int i = 1; i <= pagesCount; i++) {
			if(i == activePage) {%>
				<button type="button" class="btn btn-secondary active" onclick="<%=JS_tableName%>.onPageChanged(<%=i%>)"><%=i%></button>
			<%} else { %>
				<button type="button" class="btn btn-secondary" onclick="<%=JS_tableName%>.onPageChanged(<%=i%>)"><%=i%></button>
			<%}
		}
		%>
		</div>
	</div>
</form>


<script type="text/javascript">
	function applyFilter(modalID, table) {
		var serializedForm = $("#"+modalID+" form").serializeArray();
		var filterObj = {};
		for(var i = 0; i < serializedForm.length; i++) {
			if(typeof filterObj[serializedForm[i].name] === 'undefined')
				filterObj[serializedForm[i].name] = serializedForm[i].value;
			else {
				filterObj[serializedForm[i].name] = [filterObj[serializedForm[i].name]];
				filterObj[serializedForm[i].name].push(serializedForm[i].value);
			}
		}
		$("#"+modalID).modal('hide');
		table.filter(filterObj);
	}
</script>

<div class="modal fade" id="<%=tableName%>_filterModal" tabindex="-1" role="dialog" aria-hidden="true" >
	<div class="modal-dialog modal-dialog-centered modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Filter</h5>		
			</div>
			<div class="modal-body">

			<form name="<%=tableName%>-filter-form" id="<%=tableName%>-filter-form">

				<%
				List<uz.app.jsp.Table.Tags.Filter> filters = new LinkedList<uz.app.jsp.Table.Tags.Filter>();
				for(Column col: showableColumns) {
					if(col.getFilter() != null)
						filters.add(col.getFilter());
				}

				for(uz.app.jsp.Table.Tags.Filter filter: filters) {%>
					<div class="form-group row">
						<label for="<%=filter.getColumnName()%>" class="col-4 col-form-label"><%=filter.getLabel()%></label>
						<div class="col-8 input-group">
							<%
							switch(filter.getType()) {
								case EQUAL: {%>
									<div class="input-group-prepend">
										<div class="input-group-text">=</div>
									</div>
									<%
									switch(filter.getColumnType()) {
										case STRING: { %>
											<input name="<%=filter.getColumnName()%>" id="<%=filter.getColumnName()%>" class="form-control" type="text"/>
										<%}
										case NUMBER: { %>
											<input name="<%=filter.getColumnName()%>" id="<%=filter.getColumnName()%>" class="form-control" type="number"/>
										<%}
										case DATE: { %>
											<input name="<%=filter.getColumnName()%>" id="<%=filter.getColumnName()%>" type="date" class="form-control">
										<%}
									}
									%>
								<%}
								case LIKE: {%>
									<div class="input-group-prepend">
										<div class="input-group-text">like</div>
									</div>
									<%
									switch(filter.getColumnType()) {
										case STRING:
										case NUMBER: { %>
											<input name="<%=filter.getColumnName()%>" id="<%=filter.getColumnName()%>" class="form-control" type="text"/>
										<%}
									}%>
								<%}
								case RANGE: { %>
									<%
									switch(filter.getColumnType()) {
										case DATE: { %>
											<div class="input-group-prepend">
												<div class="input-group-text">between</div>
											</div>
											<input 	name="<%=filter.getColumnName()%>" 
													id="<%=filter.getColumnName()%>" 
													type="date" 
													class="form-control">

											<div class="input-group-prepend">
												<div class="input-group-text">and</div>
											</div>
											<input 	name="<%=filter.getColumnName()%>" 
													id="<%=filter.getColumnName()%>" 
													type="date" 
													class="form-control">
										<%}
										case NUMBER: { %>
											<div class="input-group-prepend">
												<div class="input-group-text">between</div>
											</div>
											<input 	name="<%=filter.getColumnName()%>" 
													id="<%=filter.getColumnName()%>" 
													type="number" 
													class="form-control">

											<div class="input-group-prepend">
												<div class="input-group-text">and</div>
											</div>
											<input 	name="<%=filter.getColumnName()%>" 
													id="<%=filter.getColumnName()%>" 
													type="number" 
													class="form-control">
										<%}
									}%>
								<%}
							}%>
							<div class="input-group-prepend">
								<div class="input-group-text">=</div>
							</div>
							<input name="user_id" id="user_id" class="form-control" type="text"/>
						</div>
					</div>
				<%}%>

			</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" onclick="applyFilter('<%=tableName%>_filterModal', <%=JS_tableName%>)"> Filter </button>
			</div>
		</div>
	</div>
</div>