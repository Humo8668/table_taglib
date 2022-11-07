<%@ taglib uri="uz.app.jsp.Table.Tags" prefix="t" %>  
<html>
<body>
    <div style="margin: auto; width: 600px;">
    
        <t:table name="employee_table" caption="Employees" dataStoreClass="uz.app.jsp.Table.TestModule.EmployeeDataStore" numeration="true">
            <t:column name="id" orderValue="1" label="Employee ID" show="true" filterType="equal" />
            <t:column name="name" orderValue="2" label="Name" show="true" />
            <t:column name="depId" orderValue="4" type="number" label="Department ID" show="true" filterType="option" filterOptionsClass="uz.app.jsp.Table.TestModule.DepartmentOptions"/>
            <t:column name="postId" orderValue="3" type="number" label="Post ID" show="true" />
            <t:column name="salary" orderValue="5" type="number" label="Salary" show="true" />
        </t:table>
    </div>
</body>
</html>
