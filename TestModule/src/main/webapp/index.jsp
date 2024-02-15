<%@ taglib uri="uz.app.jsp.Table.Tags" prefix="t" %>  
<html>
<body>
    <div style="margin: auto; width: 1200px;">
        <t:table name="employee_table" caption="Employees" dataStoreClass="uz.app.jsp.Table.TestModule.EmployeeDataStore" numeration="true">
            <t:column name="id" orderValue="1" label="Employee ID" show="true" filterType="equal" />
            <t:column name="name" orderValue="2" label="Name" show="true" filterType="like" />
            <t:column name="depName" orderValue="4" label="Department" show="true"/>
            <t:column name="depId" type="number" label="Department ID" filterType="option" filterOptionsClass="uz.app.jsp.Table.TestModule.DepartmentOptions"/>
            <t:column name="postName" orderValue="3" label="Post" show="true"/>
            <t:column name="postId" type="number" label="Post ID"/>
            <t:column name="salary" orderValue="5" type="number" label="Salary" show="true" filterType="range"/>
            <t:column name="hiredDate" orderValue="6" type="date" label="Hired on" show="true" filterType="range"/>
        </t:table>
    </div>
</body>
</html>
